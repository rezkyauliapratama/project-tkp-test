package com.rezkyaulia.android.light_optimization_data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;


import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import timber.log.Timber;

/**
 * Created by Rezky Aulia Pratama on 7/4/2017.
 */

public class NetworkClient {

    @StringDef({GET,POST,PUT,DELETE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface METHOD {}

    public static final String POST = "POST";

    public static final String GET = "GET";
    public static final String PUT = "PUT";
    public static final String DELETE = "DELETE";

    private OkHttpClient sHttpClient;

    public NetworkClient(Context context) {
        this.sHttpClient = new OkHttpClient().newBuilder()
                .cache(Utils.init().getCache(context, NConstants.init().MAX_CACHE_SIZE, NConstants.init().CACHE_DIR_NAME))
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
    }

    public void cancelAllRequest(){
        for(Call call : sHttpClient.dispatcher().queuedCalls()) {
            call.cancel();
        }

        for(Call call : sHttpClient.dispatcher().runningCalls()) {
//                if(call.request().tag().equals("sss"))
            call.cancel();
        }
    }

    public void cancelByTag(String tag){
        for(Call call : sHttpClient.dispatcher().queuedCalls()) {
            if(call.request().tag().equals(tag))
                call.cancel();
        }

        for(Call call : sHttpClient.dispatcher().runningCalls()) {
            if(call.request().tag().equals(tag))
                call.cancel();
        }
    }

    public InitHttpCore withUrl(String url){
            return new InitHttpCore(url);
    }

    public class InitHttpCore{


        String mURL;

        public InitHttpCore(String mURL) {
            this.mURL = mURL;
        }

        /**
         * initAs a standard method to initialize Http Connection.
         *
         * @param t The class that representation of response value.
         *
         */
        public <T> HttpCore<T> initAs(Class<T> t) throws Exception{

            if (sHttpClient == null){
                throw new IOException("OkhttpClient is null");
            }
            Timber.e("Initialize HTTP CORE");
            HttpCore<T> core = new HttpCore<T>(sHttpClient,t,mURL);
            return core;
        }

        /**
         * Make a standard toast that just contains a text view.
         *
         * @param t The class that representation of response value.
         * @param method Http method for this request.  Either {@link #GET} or
         *                 {@link #POST} or {@link #PUT} or {@link #DELETE}
         *
         *
         */
        @NonNull
        public <T> HttpCore<T> initAs(Class<T> t, @METHOD String method) throws Exception{

            if (sHttpClient == null){
                throw new IOException("OkhttpClient is null");
            }
            Timber.e("Initialize HTTP CORE");
            HttpCore<T> core = new HttpCore<T>(sHttpClient,t,mURL,method);
            return core;
        }

    }
}
