package org.net.websocket.core.impl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.Toast;

import org.net.websocket.constants.WsConstant;
import org.net.websocket.core.IWsCore;
import org.net.websocket.core.impl.config.WsConfig;
import org.net.websocket.core.impl.wrapper.impl.WebSocketWrapperImpl;
import org.net.websocket.utils.NetUtil;
import org.net.websocket.utils.ObjectHelp;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okio.ByteString;

/**
 * Author: yuzzha
 * Date: 2019-07-15 16:32
 * Description:
 */
public class WsCoreImpl implements IWsCore {

    private final static int RECONNECT_INTERVAL = 4 * 1000;    //重连自增步长
    private Context mContext;

    private String wsUrl;

    private OkHttpClient mOkHttpClient;
    private Request mRequest;

    private boolean isNeedReconnect = true;          //是否需要断线自动重连
    private boolean isManualClose = false;     //是否为手动关闭websocket连接
    private Lock mLock;

    private WebSocketWrapperImpl mWebSocketDao;

    private OnWsMessageCallback onWsMessageCallback;

    private int reconnectCount = 0;   //重连次数

    private NetBroadcastReceiver netBroadcastReceiver;

    private Runnable reconnectRunnable = () -> buildConnect();

    @Override
    public String getClintId() {
        if (mWebSocketDao == null) throw new RuntimeException("");
        return mWebSocketDao.getClinetId();
    }

    public WsCoreImpl(WsConfig config) {
        mContext = ObjectHelp.checkNotNull(config.getContext());
        wsUrl = ObjectHelp.checkNotEmpty(config.getWsUrl(), "WebSocket URL :");
        isNeedReconnect = config.isNeedReconnect();
        mOkHttpClient = config.getOkHttpClient();
        this.mLock = new ReentrantLock();
    }

    private void initWebSocket() {
        if (mOkHttpClient == null) {
            mOkHttpClient = new OkHttpClient.Builder()
                    .retryOnConnectionFailure(isNeedReconnect)
                    .build();
        }
        if (mRequest == null) {
            mRequest = new Request.Builder()
                    .url(wsUrl)
                    .build();
        }
        mOkHttpClient.dispatcher().cancelAll();
        initWsListener();
        try {
            mLock.lockInterruptibly();
            try {
                mOkHttpClient.newWebSocket(mRequest, mWebSocketDao);
            } finally {
                mLock.unlock();
            }
        } catch (InterruptedException e) {
            Log.e("initWebSocket ", "  InterruptedException : " + e.getMessage());
        }
    }

    //初始化WebSocket监听
    private void initWsListener() {
        if (mWebSocketDao == null) {
            mWebSocketDao = new WebSocketWrapperImpl();
            mWebSocketDao.setOnOpenWsLinsener((webSocket, response) -> cancelRetryConn());
            //断开重新连接策略 1.非手动断开，有net网络之间tryReconnect，负责注册网络监听，连网后再开始tryReconnect
            mWebSocketDao.setOnCloseWsLinsenter((webSocket, reason) -> {
                if (!isManualClose) {
                    boolean netConn = NetUtil.isNetworkConnected(mContext);
                    if (netConn) tryReconnect();
                    else if (netBroadcastReceiver == null)
                        newNetReceiverInstance();
                }
            });

            mWebSocketDao.setOnReciveWsMsgLinsener((webSocket, data) -> {
                if (onWsMessageCallback != null)
                    onWsMessageCallback.onReciverMessage(data);
            });
        }
    }

    @Override
    public void onBuildConnect() {
        isManualClose = false;
        buildConnect();
    }

    @Override
    public WebSocket getWebSocket() {
        return mWebSocketDao.getWebSocket();
    }

    @Override
    public synchronized boolean isWsConnected() {
        if (mWebSocketDao == null) throw new RuntimeException("");
        return mWebSocketDao.isWsConnected();
    }

    @Override
    public void stopConnect() {
        isManualClose = true;
        cancelRetryConn();
        onManualDisWsConn();
        if (onWsMessageCallback != null)
            onWsMessageCallback = null;
        if (mRequest != null)
            mRequest = null;
        if (mOkHttpClient != null) {
            mOkHttpClient.dispatcher().cancelAll();
            mOkHttpClient.connectionPool();
            mOkHttpClient = null;
        }
        if (mWebSocketDao != null)
            mWebSocketDao = null;

        doReleaseNetReceiverInstance();
    }


    private void tryReconnect() {
        Log.e("TAG", "tryReconnect: --------------> reconnectCount :  " + reconnectCount);
        if (!isNeedReconnect | isManualClose) { //手动断开或者不需要断开重连
            return;
        }
        mWebSocketDao.setWsConnStatus(WsConstant.ConnStatus.CONN_RETRY_CONN);
        if (reconnectCount == 0)
            mWebSocketDao.doRemoveHandlerDelayTask(reconnectRunnable);
        mWebSocketDao.doHandlerDelayedTask(reconnectRunnable, RECONNECT_INTERVAL);
        reconnectCount++;
    }

    private void cancelRetryConn() {
        mWebSocketDao.doRemoveHandlerDelayTask(reconnectRunnable);
        reconnectCount = 0;
    }

    /**
     * 手动断开Ws连接
     */
    private void onManualDisWsConn() {
        if (mWebSocketDao.isDisConn()) return;
        if (mWebSocketDao != null) {
            mWebSocketDao.setWsConnStatus(WsConstant.ConnStatus.CONN_DIS_CONN);
            mWebSocketDao.doRemoveHandlerDelayTask(reconnectRunnable);
            mWebSocketDao.doManualReleaseTask();
            mWebSocketDao = null;
        }
    }

    //注册网络变化广播
    public void newNetReceiverInstance() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        netBroadcastReceiver = new NetBroadcastReceiver();
        netBroadcastReceiver.setOnNetStatueChangeListener((hasNet, statue) -> {
            if (hasNet)
                tryReconnect();
            Log.e("TAG", "setOnNetStatueChangeListener : --------------------->  hasNet : " + hasNet);
        });
        mContext.registerReceiver(netBroadcastReceiver, filter);
    }

    //解注册网络变化广播
    public void doReleaseNetReceiverInstance() {
        if (netBroadcastReceiver != null) {
            netBroadcastReceiver.onCancelListener();
            mContext.unregisterReceiver(netBroadcastReceiver);
        }
    }

    //建立连接
    private synchronized void buildConnect() {
        if (!NetUtil.isNetworkConnected(mContext)) {
            newNetReceiverInstance();//当断开/没有网络时 进行注册网络广播
            Toast.makeText(mContext, "UN CONN Network", Toast.LENGTH_SHORT).show();
            //tryReconnect();
            cancelRetryConn();
            return;
        }
        initWsListener();
        if (mWebSocketDao.isDisConn())
            mWebSocketDao.setWsConnStatus(WsConstant.ConnStatus.CONN_CONN_ING);
        Log.e("TAG", "buildConnect: ----------------------------> reconnectCount : " + reconnectCount);
        initWebSocket();
        tryReconnect();
    }

    //发送消息
    @Override
    public boolean sendMessage(String wsMsg) {
        return send(wsMsg);
    }

    @Override
    public boolean sendMessage(ByteString byteString) {
        return send(byteString);
    }

    private synchronized boolean send(Object msg) {
        if (!mWebSocketDao.isWsConnected())
            return false;
        boolean isSend = mWebSocketDao.sendWsMsgMethod(msg);
        //发送消息失败，尝试重连
        if (!isSend)
            tryReconnect();
        return isSend;
    }

    @Override
    public void setOnWsMessageCallback(OnWsMessageCallback onWsMessageCallback) {
        this.onWsMessageCallback = onWsMessageCallback;
    }

    /**
     * 网络状态监听 Inner BroadcastReceiver
     */
    class NetBroadcastReceiver extends BroadcastReceiver {
        private OnNetStatueChangeListener listener;

        public void setOnNetStatueChangeListener(OnNetStatueChangeListener listener) {
            this.listener = listener;
        }

        public void onCancelListener() {
            if (this.listener != null)
                this.listener = null;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            // 如果相等的话就说明网络状态发生了变化
            if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                int netWorkState = NetUtil.getNetWorkState(context);
                // 当网络发生变化，判断当前网络状态，并通过NetEvent回调当前网络状态
                if (this.listener != null)
                    this.listener.onNetStatue(netWorkState != WsConstant.NET_STATUS.NETWORK_NONE, netWorkState);
            }
        }
    }
}
