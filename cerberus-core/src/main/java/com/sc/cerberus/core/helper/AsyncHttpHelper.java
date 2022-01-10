package com.sc.cerberus.core.helper;

import org.asynchttpclient.*;

import java.util.concurrent.CompletableFuture;

/**
 * 异步http辅助类
 */
public class AsyncHttpHelper {
    private AsyncHttpClient httpClient;

    private static final class SingletonHolder{
        private static final AsyncHttpHelper INSTANCCE = new AsyncHttpHelper();
    }

    private AsyncHttpHelper(){}

    public static AsyncHttpHelper getInstance(){
        return SingletonHolder.INSTANCCE;
    }

    public void init( AsyncHttpClient httpClient){
        this.httpClient = httpClient;
    }

    //执行http请求
    public CompletableFuture<Response> execRequest(Request request){
        ListenableFuture<Response> future = httpClient.executeRequest(request);
        return future.toCompletableFuture();
    }

    public <T> CompletableFuture<T> execRequest(Request request, AsyncHandler<T> handler){
        ListenableFuture<T> future = httpClient.executeRequest(request,handler);
        return future.toCompletableFuture();
    }
}
