package com.sfebiz.supplychain.util;

import org.apache.http.*;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 *  待完善
 * Created by jianyuanyang on 15/6/16.
 * httpClient4.3 工具类
 */
public class HttpClientUtil {

    private final static Logger log  = LoggerFactory.getLogger(HttpClientUtil.class);

    private static PoolingHttpClientConnectionManager connMgr;

    private static RequestConfig requestConfig;

    private  static Integer max_pool_size = 300 ;  //多线程httpclient 池大小

    private static Integer connect_time_out = 10000; //连接超时

    private static Integer socket_time_out = 10000;  //读取超时

    private static Integer request_time_out = 10000; // 从连接池获取连接实例的超时

    private static Integer default_try_count = 0 ;//设置重试次数

    private static HttpRequestRetryHandler retryHandler;//重试策略

    static {
        // 设置连接池
        connMgr = new PoolingHttpClientConnectionManager();
        // 设置连接池大小
        connMgr.setMaxTotal(max_pool_size);
        connMgr.setDefaultMaxPerRoute(connMgr.getMaxTotal());
        RequestConfig.Builder configBuilder = RequestConfig.custom();
        // 设置连接超时
        configBuilder.setConnectTimeout(connect_time_out);
        // 设置读取超时
        configBuilder.setSocketTimeout(socket_time_out);
        // 设置从连接池获取连接实例的超时
        configBuilder.setConnectionRequestTimeout(request_time_out);
        // 在提交请求之前 测试连接是否可用
        configBuilder.setStaleConnectionCheckEnabled(true);
        requestConfig = configBuilder.build();
        //设置重试策略
        retryHandler = new HttpRequestRetryHandler() {
            @Override
            public boolean retryRequest(IOException exception, int executionCount, HttpContext httpContext) {
                if (executionCount > default_try_count) {
                    // Do not retry if over max retry count
                    return false;
                }
                if (exception instanceof InterruptedIOException) {
                    // Timeout
                    return false;
                }
                if (exception instanceof UnknownHostException) {
                    // Unknown host
                    return false;
                }
                if (exception instanceof ConnectTimeoutException) {
                    // Connection refused
                    return false;
                }
                if (exception instanceof SSLException) {
                    // SSL handshake exception
                    return false;
                }
                HttpClientContext clientContext = HttpClientContext.adapt(httpContext);
                HttpRequest request = clientContext.getRequest();
                boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
                if (idempotent) {
                    // Retry if the request is considered idempotent
                    return true;
                }
                return false;
            }
        };
    }

    /**
     * 获取 httpClient 实例
     * @param needSSL https 标示
     * @return CloseableHttpClient
     */
    private static CloseableHttpClient getHttpClientInstance(boolean needSSL){
        CloseableHttpClient httpClient ;
        if (needSSL){
            httpClient = HttpClients.custom().
                    setSSLSocketFactory(createSSLConnSocketFactory())
                    .setConnectionManager(connMgr)
                    .setDefaultRequestConfig(requestConfig)
                    .setRetryHandler(retryHandler).build();
        }else{
            httpClient = HttpClients.custom()
                    .setConnectionManager(connMgr)
                    .setDefaultRequestConfig(requestConfig)
                    .setRetryHandler(retryHandler).build();
        }

        return httpClient;
    }



    /**
     * post 请求处理
     * @param url 请求url
     * @param pairList 请求参数集合
     * @return
     */
    public static String post(String url,List<NameValuePair> pairList,boolean needSSL){
        CloseableHttpClient httpClient = getHttpClientInstance(needSSL);
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse response = null;
        HttpEntity entity;

        try {
            httpPost.setConfig(requestConfig);
            httpPost.setEntity(new UrlEncodedFormEntity(pairList, Charset.forName("utf-8")));
            response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                log.warn("[httpCLient post] invoke failed, response status: " + statusCode);
                return null;
            }
            entity = response.getEntity();
            if (entity == null) {
                log.warn("[httpCLient post] invoke failed, response output is null!");
                return null;
            }
            return EntityUtils.toString(entity, "utf-8");
        } catch (Exception e) {
            log.error("[httpCLient post] invoke throw exception, details: ", e);
        } finally {
            httpPost.abort();
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    log.error("[httpCLient post] EntityUtils.consume invoke throw exception, details: ", e);
                    return "请求出问题了";
                }
            }
        }
        return null;
    }


    /**
     * post json 请求处理
     * @param url 请求url
     * @param postJson 请求参数json字符串
     * @return 请求结果
     */
    public static String postJson(String url,String postJson,boolean needSSL){
        CloseableHttpClient httpClient = getHttpClientInstance(needSSL);
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse response = null;
        HttpEntity entity;

        try {
            httpPost.setConfig(requestConfig);
            StringEntity bodyEntity = new StringEntity(postJson, ContentType.APPLICATION_JSON);
            httpPost.setEntity(bodyEntity);

            response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                log.warn("[httpCLient postJson] invoke failed, response status: " + statusCode);
                return null;
            }
            entity = response.getEntity();
            if (entity == null) {
                log.warn("[httpCLient postJson] invoke failed, response output is null!");
                return null;
            }
            return EntityUtils.toString(entity, "utf-8");
        } catch (Exception e) {
            log.error("[httpCLient postJson] invoke throw exception, details: ", e);
        } finally {
            httpPost.abort();
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    log.error("[httpCLient postJson] EntityUtils.consume invoke throw exception, details: ", e);
                    return "请求出问题了";
                }
            }
        }
        return null;
    }


    /**
     * post 请求处理
     * @param url 请求url
     * @param params 请求参数集合
     * @return
     */
    public static String post(String url,Map<String, String>  params,boolean needSSL){
        List<NameValuePair> pairList = new ArrayList<NameValuePair>(params.size());
        for (Map.Entry<String, String> entry : params.entrySet()) {
            Object entryValue = entry.getValue();
            if (null != entryValue){
                NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry
                        .getValue());
                pairList.add(pair);
            }
        }
        return post(url,pairList,needSSL);
    };


    /**
     * 创建ssl
     * @return
     */
    private static SSLConnectionSocketFactory createSSLConnSocketFactory() {
        SSLConnectionSocketFactory sslsf = null;
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {

                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
            sslsf = new SSLConnectionSocketFactory(sslContext, new X509HostnameVerifier() {

                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }

                @Override
                public void verify(String host, SSLSocket ssl) throws IOException {
                }

                @Override
                public void verify(String host, X509Certificate cert) throws SSLException {
                }

                @Override
                public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
                }
            });
        } catch (GeneralSecurityException e) {
            log.error(e.getMessage(), e);
        }
        return sslsf;
    }

}
