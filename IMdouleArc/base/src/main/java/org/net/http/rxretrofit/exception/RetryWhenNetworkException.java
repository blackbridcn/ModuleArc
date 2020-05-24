package org.net.http.rxretrofit.exception;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * Author: yuzzha
 * Date: 2019-07-12 11:54
 * Description: ${DESCRIPTION}
 * Remark:
 */
public class RetryWhenNetworkException implements Function<Observable<? extends Throwable>, Observable<?>> {

    //retry次数
    private int count = 3;
    //延迟
    private long delay = 3000;
    //叠加延迟
    private long increaseDelay = 3000;

    public RetryWhenNetworkException() {

    }

    public RetryWhenNetworkException(int count, long delay) {
        this.count = count;
        this.delay = delay;
    }

    public RetryWhenNetworkException(int count, long delay, long increaseDelay) {
        this.count = count;
        this.delay = delay;
        this.increaseDelay = increaseDelay;
    }

    @Override
    public Observable<?> apply(Observable<? extends Throwable> observable) throws Exception {
        //zipWith进行重复次数和失败类型进行过滤，flatMap出合适的Iitem然后重复请求
        return observable.zipWith(Observable.range(1, count + 1), (throwable, integer) -> {
            return new ExceptionWrapper(throwable, integer);
        }, true, this.count).flatMap((exceptionWrapper) -> {
            if(exceptionWrapper.isRetry){
                Observable.timer(delay+(exceptionWrapper.index-1)*increaseDelay,TimeUnit.MILLISECONDS);
            }
            return Observable.error(exceptionWrapper.throwable);
        });
    }

    private class ExceptionWrapper {
        private int index;
        private Throwable throwable;
        private boolean isRetry;

        public ExceptionWrapper(Throwable throwable, int index) {
            this.index = index;
            this.throwable = throwable;
            isRetry(throwable, index);

        }

        private void isRetry(Throwable throwable, int idext) {
            if ((throwable instanceof ConnectException || throwable instanceof SocketTimeoutException || throwable instanceof TimeoutException) && idext < count + 1) {
                this.isRetry = true;
            } else this.isRetry = false;
        }
    }
}
