package com.wwish.ganalytics.okhttp;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;



import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import com.wwish.ganalytics.okhttp.cache.CacheEntity;
import com.wwish.ganalytics.okhttp.cache.CacheMode;
import com.wwish.ganalytics.okhttp.cookie.CookieJarImpl;
import com.wwish.ganalytics.okhttp.cookie.store.CookieStore;
import com.wwish.ganalytics.okhttp.cookie.store.PersistentCookieStore;
import com.wwish.ganalytics.okhttp.https.HttpsUtils;
import com.wwish.ganalytics.okhttp.interceptor.HttpLoggingInterceptor;
import com.wwish.ganalytics.okhttp.model.HttpHeaders;
import com.wwish.ganalytics.okhttp.model.HttpParams;
import com.wwish.ganalytics.okhttp.request.DeleteRequest;
import com.wwish.ganalytics.okhttp.request.GetRequest;
import com.wwish.ganalytics.okhttp.request.HeadRequest;
import com.wwish.ganalytics.okhttp.request.OptionsRequest;
import com.wwish.ganalytics.okhttp.request.PostRequest;
import com.wwish.ganalytics.okhttp.request.PutRequest;
import com.wwish.ganalytics.okhttp.utils.Logger;
import okhttp3.Call;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

/**
 * ================================================
 * 描    述：网络请求的入口类
 * ================================================
 */
public class OkHttp {
    public static final int DEFAULT_MILLISECONDS = 30000;       //默认的超时时间
    public static final int REFRESH_TIME = 100;                 //回调刷新时间（单位ms）

    private Handler mDelivery;                                  //用于在主线程执行的调度器
    private OkHttpClient.Builder okHttpClientBuilder;           //ok请求的客户端
    private HttpParams mCommonParams;                           //全局公共请求参数
    private HttpHeaders mCommonHeaders;                         //全局公共请求头
    private CacheMode mCacheMode;                               //全局缓存模式
    private int mRetryCount = 3;                                //全局超时重试次数
    private long mCacheTime = CacheEntity.CACHE_NEVER_EXPIRE;   //全局缓存过期时间,默认永不过期
    private static Application context;                         //全局上下文
    private CookieJarImpl cookieJar;                            //全局 Cookie 实例

    private OkHttp() {
        okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.hostnameVerifier(new DefaultHostnameVerifier());
        okHttpClientBuilder.connectTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        okHttpClientBuilder.readTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        okHttpClientBuilder.writeTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        mDelivery = new Handler(Looper.getMainLooper());
    }

    public static OkHttp getInstance() {
        return OkGoHolder.holder;
    }

    private static class OkGoHolder {
        private static OkHttp holder = new OkHttp();
    }

    /** 必须在全局Application先调用，获取context上下文，否则缓存无法使用 */
    public static void init(Application app) {
        context = app;
    }

    /** 获取全局上下文 */
    public static Context getContext() {
        if (context == null) throw new IllegalStateException("请先在全局Application中调用 OkHttp.init() 初始化！");
        return context;
    }

    public Handler getDelivery() {
        return mDelivery;
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClientBuilder.build();
    }

    /** 对外暴露 OkHttpClient,方便自定义 */
    public OkHttpClient.Builder getOkHttpClientBuilder() {
        return okHttpClientBuilder;
    }

    /** get请求 */
    public static GetRequest get(String url) {
        return new GetRequest(url);
    }

    /** post请求 */
    public static PostRequest post(String url) {
        return new PostRequest(url);
    }

    /** put请求 */
    public static PutRequest put(String url) {
        return new PutRequest(url);
    }

    /** head请求 */
    public static HeadRequest head(String url) {
        return new HeadRequest(url);
    }

    /** delete请求 */
    public static DeleteRequest delete(String url) {
        return new DeleteRequest(url);
    }

    /** patch请求 */
    public static OptionsRequest options(String url) {
        return new OptionsRequest(url);
    }

    /** 调试模式,默认打开所有的异常调试 */
    public OkHttp debug(String tag) {
        debug(tag, true);
        return this;
    }

    /**
     * 调试模式,第二个参数表示所有catch住的log是否需要打印
     * 一般来说,这些异常是由于不标准的数据格式,或者特殊需要主动产生的,并不是框架错误,如果不想每次打印,这里可以关闭异常显示
     */
    public OkHttp debug(String tag, boolean isPrintException) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(tag);
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClientBuilder.addInterceptor(loggingInterceptor);
        Logger.debug(isPrintException);
        return this;
    }

    /**
     * 此类是用于主机名验证的基接口。 在握手期间，如果 URL 的主机名和服务器的标识主机名不匹配，
     * 则验证机制可以回调此接口的实现程序来确定是否应该允许此连接。策略可以是基于证书的或依赖于其他验证方案。
     * 当验证 URL 主机名使用的默认规则失败时使用这些回调。如果主机名是可接受的，则返回 true
     */
    public static class DefaultHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    /** https的全局访问规则 */
    public OkHttp setHostnameVerifier(HostnameVerifier hostnameVerifier) {
        okHttpClientBuilder.hostnameVerifier(hostnameVerifier);
        return this;
    }


    /** https的全局自签名证书 */
    public OkHttp setCertificates(InputStream... certificates) {
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, certificates);
        okHttpClientBuilder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        return this;
    }

    /** https双向认证证书 */
    public OkHttp setCertificates(InputStream bksFile, String password, InputStream... certificates) {
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(bksFile, password, certificates);
        okHttpClientBuilder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        return this;
    }

    public OkHttp setCertificates() {
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory();
        okHttpClientBuilder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        return this;
    }

    /** 全局cookie存取规则 */
    public OkHttp setCookieStore(CookieStore cookieStore) {
        cookieJar = new CookieJarImpl(cookieStore);
        okHttpClientBuilder.cookieJar(cookieJar);
        return this;
    }

    /** 获取全局的cookie实例 */
    public CookieJarImpl getCookieJar() {
        return cookieJar;
    }

    /** 全局读取超时时间 */
    public OkHttp setReadTimeOut(long readTimeOut) {
        okHttpClientBuilder.readTimeout(readTimeOut, TimeUnit.MILLISECONDS);
        return this;
    }

    /** 全局写入超时时间 */
    public OkHttp setWriteTimeOut(long writeTimeout) {
        okHttpClientBuilder.writeTimeout(writeTimeout, TimeUnit.MILLISECONDS);
        return this;
    }

    /** 全局连接超时时间 */
    public OkHttp setConnectTimeout(long connectTimeout) {
        okHttpClientBuilder.connectTimeout(connectTimeout, TimeUnit.MILLISECONDS);
        return this;
    }

    /** 超时重试次数 */
    public OkHttp setRetryCount(int retryCount) {
        if (retryCount < 0) throw new IllegalArgumentException("retryCount must > 0");
        mRetryCount = retryCount;
        return this;
    }

    /** 超时重试次数 */
    public int getRetryCount() {
        return mRetryCount;
    }

    /** 全局的缓存模式 */
    public OkHttp setCacheMode(CacheMode cacheMode) {
        mCacheMode = cacheMode;
        return this;
    }

    /** 获取全局的缓存模式 */
    public CacheMode getCacheMode() {
        return mCacheMode;
    }

    /** 全局的缓存过期时间 */
    public OkHttp setCacheTime(long cacheTime) {
        if (cacheTime <= -1) cacheTime = CacheEntity.CACHE_NEVER_EXPIRE;
        mCacheTime = cacheTime;
        return this;
    }

    /** 获取全局的缓存过期时间 */
    public long getCacheTime() {
        return mCacheTime;
    }

    /** 获取全局公共请求参数 */
    public HttpParams getCommonParams() {
        return mCommonParams;
    }

    /** 添加全局公共请求参数 */
    public OkHttp addCommonParams(HttpParams commonParams) {
        if (mCommonParams == null) mCommonParams = new HttpParams();
        mCommonParams.put(commonParams);
        return this;
    }

    /** 获取全局公共请求头 */
    public HttpHeaders getCommonHeaders() {
        return mCommonHeaders;
    }

    /** 添加全局公共请求参数 */
    public OkHttp addCommonHeaders(HttpHeaders commonHeaders) {
        if (mCommonHeaders == null) mCommonHeaders = new HttpHeaders();
        mCommonHeaders.put(commonHeaders);
        return this;
    }

    /** 添加全局拦截器 */
    public OkHttp addNetWorkInterceptor(Interceptor interceptor) {
        HttpHeaders headers = new HttpHeaders();
        headers.put("Transfer-Encoding", "chunked");
        addCommonHeaders(headers);
        okHttpClientBuilder.addNetworkInterceptor(interceptor);
        return this;
    }

    /** 添加全局拦截器 */
    public OkHttp addInterceptor(Interceptor interceptor) {
        okHttpClientBuilder.addInterceptor(interceptor);
        return this;
    }

    /** 根据Tag取消请求 */
    public void cancelTag(Object tag) {
        for (Call call : getOkHttpClient().dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : getOkHttpClient().dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    public void initOkhttp(Application context){
        //必须调用初始化
        OkHttp.init(context);
        //以下设置的所有参数是全局参数,同样的参数可以在请求的时候再设置一遍,那么对于该请求来讲,请求中的参数会覆盖全局参数
        //好处是全局参数统一,特定请求可以特别定制参数
        try {
            //以下都不是必须的，根据需要自行选择,一般来说只需要 debug,缓存相关,cookie相关的 就可以了
            OkHttp.getInstance()
                    //如果使用默认的 60秒,以下三行也不需要传
                    .setConnectTimeout(OkHttp.DEFAULT_MILLISECONDS)  //全局的连接超时时间
                    .setReadTimeOut(OkHttp.DEFAULT_MILLISECONDS)     //全局的读取超时时间
                    .setWriteTimeOut(OkHttp.DEFAULT_MILLISECONDS)    //全局的写入超时时间
                    //可以全局统一设置缓存模式,默认是不使用缓存,可以不传,具体其他模式看 github 介绍 https://github.com/jeasonlzy/
                    .setCacheMode(CacheMode.NO_CACHE)
                    //可以全局统一设置缓存时间,默认永不过期,具体使用方法看 github 介绍
                    .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)
//                    .addNetWorkInterceptor(new StethoInterceptor())
                    .setCookieStore(new PersistentCookieStore());          //cookie持久化存储，如果cookie不过期，则一直有效
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}