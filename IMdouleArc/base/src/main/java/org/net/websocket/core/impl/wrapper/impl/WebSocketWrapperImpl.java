package org.net.websocket.core.impl.wrapper.impl;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.net.websocket.constants.WsConstant;
import org.net.websocket.core.impl.wrapper.IWebSocketWrapper;
import org.net.websocket.model.BaseWsMsg;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * Author: yuzzha
 * Date: 2019-07-23 10:25
 * Description: WebSocke 对象 Core 操作函数的Impl
 * Remark:
 */
public class WebSocketWrapperImpl extends WebSocketListener implements IWebSocketWrapper {

    //连接状态
    private int mWsConnStatus = WsConstant.ConnStatus.CONN_DIS_CONN;
    //当前WS连接的ClinetId
    private String wsClientId;
    //当前Ws实例对象
    private WebSocket mWebSocket;
    //消息转发Handler
    private Handler wsMainHandler;

    private IWebSocketWrapper.OnCloseWsLinsenter onCloseWsLinsenter;
    private IWebSocketWrapper.OnOpenWsLinsener onOpenWsLinsener;
    private IWebSocketWrapper.OnReciveWsMsgLinsener onReciveWsMsgLinsener;
    private IWebSocketWrapper.OnFailureWsMsgLinsener onFailureWsMsgLinsener;

    public WebSocketWrapperImpl() {
        super();
        wsMainHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        super.onOpen(webSocket, response);
        this.mWebSocket = webSocket;
        setWsConnStatus(WsConstant.ConnStatus.CONN_CONN_OK);
        if (this.onOpenWsLinsener != null)
            onOpenWsLinsener.onOpenWs(webSocket, response);
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        super.onMessage(webSocket, text);
        Log.e("TAG", "onMessage: ------------------->MSG : " + text);
        BaseWsMsg data = JSON.parseObject(text, BaseWsMsg.class);
        if (data.isResult() && TextUtils.equals(WsConstant.MsgType.MSG_INIT_TYPE, data.getType())) {
            JSONObject jsonObject = JSON.parseObject(data.getData());
            this.wsClientId = jsonObject.getString("clientId");
            return;
        }
        if (this.onReciveWsMsgLinsener == null) return;
        if (Looper.myLooper() != Looper.getMainLooper())
            doDispatchMsgMainThreadTask(() -> onReciveWsMsgLinsener.OnReciveWsMsg(webSocket, data));
        else onReciveWsMsgLinsener.OnReciveWsMsg(webSocket, data);
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        super.onMessage(webSocket, bytes);
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        super.onClosing(webSocket, code, reason);
        setWsConnStatus(WsConstant.ConnStatus.CONN_DIS_ING);
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        super.onClosed(webSocket, code, reason);
        setWsConnStatus(WsConstant.ConnStatus.CONN_DIS_CONN);
        Log.e("TAG", "onClosed: -------------------->  ");
        this.mWebSocket = null;
        if (this.onCloseWsLinsenter != null)
            onCloseWsLinsenter.onCloseWs(webSocket, reason);
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        super.onFailure(webSocket, t, response);
        setWsConnStatus(WsConstant.ConnStatus.CONN_DIS_CONN);
        Log.e("TAG", "onFailure: -------------------->  ");
        if (this.onFailureWsMsgLinsener != null)
            onFailureWsMsgLinsener.OnFailureWsConn(webSocket);

    }

    @Override
    public boolean sendWsMsgMethod(Object wsMsg) {
        Log.e("TAG", "sendWsMsgMethod: ---------------->  wsMsg :"+wsMsg );
        if (wsMsg instanceof String) {
            return this.mWebSocket.send((String) wsMsg);
        } else if (wsMsg instanceof ByteString) {
            return this.mWebSocket.send((ByteString) wsMsg);
        } else {
            return this.mWebSocket.send(JSON.toJSONString(wsMsg));
        }
    }

    @Override
    public void doDispatchMsgMainThreadTask(Runnable task) {
        wsMainHandler.post(task);
    }

    @Override
    public void doHandlerDelayedTask(Runnable delayTask, long delayMillis) {
        wsMainHandler.postDelayed(delayTask, delayMillis);
    }

    @Override
    public void doRemoveHandlerDelayTask(Runnable delayTask) {
        if (wsMainHandler != null)
            wsMainHandler.removeCallbacks(delayTask);
    }

    @Override
    public void setWsConnStatus(int mConnStatus) {
        this.mWsConnStatus = mConnStatus;
    }

    @Override
    public boolean isWsConnected() {
        return this.mWsConnStatus == WsConstant.ConnStatus.CONN_CONN_OK;
    }

    @Override
    public boolean isDisConn() {
        return this.mWsConnStatus == WsConstant.ConnStatus.CONN_DIS_CONN || this.mWsConnStatus == WsConstant.ConnStatus.CONN_DIS_ING;
    }

    @Override
    public String getClinetId() {
        return this.wsClientId;
    }

    @Override
    public WebSocket getWebSocket() {
        return mWebSocket;
    }

    @Override
    public void doManualReleaseTask() {
        if (this.wsMainHandler != null)
            this.wsMainHandler.removeCallbacksAndMessages(null);
        if (this.mWebSocket != null) {
            this.mWebSocket.cancel();
            boolean isClosed = this.mWebSocket.close(WsConstant.CODE.NORMAL_CLOSE, WsConstant.TIP.NORMAL_CLOSE);
            if (!isClosed)//非正常关闭连接
                Log.e("TAG", "doManualReleaseTask:  WebSocket : " + WsConstant.TIP.ABNORMAL_CLOSE);
        }
        this.mWebSocket = null;
        this.wsClientId = null;
        this.mWsConnStatus = 0;
        this.onCloseWsLinsenter = null;
        this.onOpenWsLinsener = null;
        this.onReciveWsMsgLinsener = null;
        this.wsMainHandler = null;
    }

    @Override
    public void setOnCloseWsLinsenter(IWebSocketWrapper.OnCloseWsLinsenter onCloseWsLinsenter) {
        this.onCloseWsLinsenter = onCloseWsLinsenter;
    }

    @Override
    public void setOnOpenWsLinsener(IWebSocketWrapper.OnOpenWsLinsener onOpenWsLinsener) {
        this.onOpenWsLinsener = onOpenWsLinsener;
    }

    @Override
    public void setOnReciveWsMsgLinsener(IWebSocketWrapper.OnReciveWsMsgLinsener onReciveWsMsgLinsener) {
        this.onReciveWsMsgLinsener = onReciveWsMsgLinsener;
    }

    @Override
    public void setOnFailureWsMsgLinsener(OnFailureWsMsgLinsener onFailureWsMsgLinsener) {
        this.onFailureWsMsgLinsener = onFailureWsMsgLinsener;
    }

}
