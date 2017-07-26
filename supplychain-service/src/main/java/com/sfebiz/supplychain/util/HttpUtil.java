package com.sfebiz.supplychain.util;

import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.supplychain.config.HttpConfig;
import com.sfebiz.supplychain.exception.HttpClientException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * <p>
 * HTTP工具
 * </p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a> Date: 15/3/30 Time:
 * 下午3:07
 */
public final class HttpUtil {

	private static final org.slf4j.Logger logger = LoggerFactory.getLogger("CommandLogger");

	public static final ContentType APPLICATION_FORM_URLENCODED = ContentType
			.create("application/vnd.ehking-v1.0+json",  Consts.UTF_8);

	/**
	 * 提交数据
	 *
	 * @param httpUrl
	 * @param queryString
	 *            content post参数 key1=val1&key2=val2&key3=val3
	 * @return
	 * @throws Exception
	 */
	public static String getByHttp(String httpUrl, String queryString) throws Exception {
		try {
			if (httpUrl != null && !httpUrl.endsWith("?") && queryString != null && !queryString.startsWith("?")) {
				queryString = "?" + queryString;
			}
			String response = Request.Get(httpUrl + queryString).connectTimeout(HttpConfig.CONNECT_TIMEOUT)
					.socketTimeout(HttpConfig.SOCKET_TIMEOUT).execute().returnContent().toString();
			return response;
		} catch (IOException e) {
			LogBetter.instance(logger).setLevel(LogLevel.ERROR).addParm("HTTP请求异常，url：", httpUrl)
					.addParm("请求数据：", queryString).setException(e).log();
			return null;
		}
	}

	/**
	 * Form 表单的方式提交数据
	 *
	 * @param httpUrl
	 * @param content
	 *            content post参数 key1=val1&key2=val2&key3=val3
	 * @return
	 * @throws Exception
	 */
	public static String postFormByHttp(String httpUrl, String content) throws Exception {
		try {
			String response = Request.Post(httpUrl).connectTimeout(HttpConfig.CONNECT_TIMEOUT)
					.socketTimeout(HttpConfig.SOCKET_TIMEOUT)
					.bodyString(content, ContentType.create("application/x-www-form-urlencoded", Consts.UTF_8))
					.execute().returnContent().toString();
			return response;
		} catch (IOException e) {
			LogBetter.instance(logger).setLevel(LogLevel.ERROR).addParm("HTTP请求异常，url：", httpUrl)
					.addParm("请求数据：", content).setException(e).log();
			return null;
		}
	}

	/**
	 * Form 表单的方式提交数据
	 *
	 * @param httpsUrl
	 * @param content
	 *            content post参数 key1=val1&key2=val2&key3=val3
	 * @return
	 * @throws Exception
	 */
	public static String postFormByHttps(String httpsUrl, String content) throws Exception {
		String responseBody = null;
		CloseableHttpClient httpClient = null;
		try {
			httpClient = getHttpClient();
			HttpPost httpPost = new HttpPost(httpsUrl);
			StringEntity bodyEntity = new StringEntity(content, ContentType.APPLICATION_FORM_URLENCODED);
			httpPost.setEntity(bodyEntity);
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(HttpConfig.SOCKET_TIMEOUT)
					.setConnectTimeout(HttpConfig.CONNECT_TIMEOUT).build();
			httpPost.setConfig(requestConfig);
			CloseableHttpResponse response = httpClient.execute(httpPost);
			int status = response.getStatusLine().getStatusCode();
			if (status == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				responseBody = EntityUtils.toString(entity);
			} else {
				throw new ClientProtocolException("Unexpected response status: " + status);
			}

		} catch (Exception e) {
			LogBetter.instance(logger).setLevel(LogLevel.ERROR).addParm("HTTPS请求异常，url：", httpsUrl)
					.addParm("请求数据：", content).setException(e).log();
			responseBody = null;
		} finally {
			if (httpClient != null) {
				try {
					httpClient.close();
				} catch (IOException e) {
					LogBetter.instance(logger).setLevel(LogLevel.ERROR).addParm("HTTPS请求异常，url：", httpsUrl)
							.addParm("请求数据：", content).setException(e).log();
					responseBody = null;
				}
			}
		}
		return responseBody;
	}

	
	public static String postJsonFormByHttps(String httpsUrl, String content) throws Exception {
		String responseBody = null;
		CloseableHttpClient httpClient = null;
		try {
			httpClient = getHttpClient();
			HttpPost httpPost = new HttpPost(httpsUrl);
			StringEntity bodyEntity = new StringEntity(content, APPLICATION_FORM_URLENCODED);
			httpPost.setEntity(bodyEntity);
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(HttpConfig.SOCKET_TIMEOUT)
					.setConnectTimeout(HttpConfig.CONNECT_TIMEOUT).build();
			httpPost.setConfig(requestConfig);
			CloseableHttpResponse response = httpClient.execute(httpPost);
			int status = response.getStatusLine().getStatusCode();
			if (status == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				responseBody = EntityUtils.toString(entity);
			} else {
				throw new ClientProtocolException("Unexpected response status: " + status);
			}

		} catch (Exception e) {
			LogBetter.instance(logger).setLevel(LogLevel.ERROR).addParm("HTTPS请求异常，url：", httpsUrl)
					.addParm("请求数据：", content).setException(e).log();
			responseBody = null;
		} finally {
			if (httpClient != null) {
				try {
					httpClient.close();
				} catch (IOException e) {
					LogBetter.instance(logger).setLevel(LogLevel.ERROR).addParm("HTTPS请求异常，url：", httpsUrl)
							.addParm("请求数据：", content).setException(e).log();
					responseBody = null;
				}
			}
		}
		return responseBody;
	}

	/**
	 * 以 Http 协议 ，Post Json 数据
	 *
	 * @param httpUrl
	 * @param requestBodyJsonString
	 * @return
	 */
	public static String postJsonByHttp(String httpUrl, String requestBodyJsonString) {
		try {
			String response = Request.Post(httpUrl).connectTimeout(HttpConfig.CONNECT_TIMEOUT)
					.socketTimeout(HttpConfig.SOCKET_TIMEOUT)
					.bodyString(requestBodyJsonString, ContentType.APPLICATION_JSON).execute().returnContent()
					.toString();
			return response;
		} catch (IOException e) {
			LogBetter.instance(logger).setLevel(LogLevel.ERROR).addParm("HTTP请求异常，url：", httpUrl)
					.addParm("请求JSON数据：", requestBodyJsonString).setException(e).log();
			return null;
		}
	}

	/**
	 * 以 Https 协议 ，Post Json 数据
	 *
	 * @param httpsUrl
	 * @param requestBodyJsonString
	 * @return
	 */
	public static String postJsonByHttps(String httpsUrl, String requestBodyJsonString) {
		String responseBody = null;
		CloseableHttpClient httpClient = null;
		try {
			httpClient = getHttpClient();
			HttpPost httpPost = new HttpPost(httpsUrl);
			StringEntity bodyEntity = new StringEntity(requestBodyJsonString, ContentType.APPLICATION_JSON);
			httpPost.setEntity(bodyEntity);
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(HttpConfig.SOCKET_TIMEOUT)
					.setConnectTimeout(HttpConfig.CONNECT_TIMEOUT).build();
			httpPost.setConfig(requestConfig);
			CloseableHttpResponse response = httpClient.execute(httpPost);
			int status = response.getStatusLine().getStatusCode();
			if (status == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				responseBody = EntityUtils.toString(entity);
			} else {
				throw new ClientProtocolException("Unexpected response status: " + status);
			}

		} catch (Exception e) {
			LogBetter.instance(logger).setLevel(LogLevel.ERROR).addParm("HTTPS请求异常，url：", httpsUrl)
					.addParm("请求JSON数据：", requestBodyJsonString).setException(e).log();
			responseBody = null;
		} finally {
			if (httpClient != null) {
				try {
					httpClient.close();
				} catch (IOException e) {
					LogBetter.instance(logger).setLevel(LogLevel.ERROR).addParm("HTTPS请求异常，url：", httpsUrl)
							.addParm("请求JSON数据：", requestBodyJsonString).setException(e).log();
					responseBody = null;
				}
			}
		}
		return responseBody;
	}

	/**
	 * 获取HTTPClient，支持Https的访问
	 *
	 * @return
	 */
	private static CloseableHttpClient getHttpClient() {
		RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.<ConnectionSocketFactory> create();
		ConnectionSocketFactory plainSF = new PlainConnectionSocketFactory();
		registryBuilder.register("http", plainSF);
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			TrustStrategy anyTrustStrategy = new TrustStrategy() {
				@Override
				public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
					return true;
				}
			};
			SSLContext sslContext = SSLContexts.custom().useTLS().loadTrustMaterial(trustStore, anyTrustStrategy)
					.build();
			LayeredConnectionSocketFactory sslSF = new SSLConnectionSocketFactory(sslContext,
					SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			registryBuilder.register("https", sslSF);
		} catch (KeyStoreException e) {
			throw new RuntimeException(e);
		} catch (KeyManagementException e) {
			throw new RuntimeException(e);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		Registry<ConnectionSocketFactory> registry = registryBuilder.build();
		PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(registry);
		return HttpClientBuilder.create().setConnectionManager(connManager).build();
	}
	public static String getByHttpHeader(String httpUrl, String queryString) throws Exception {
		if (httpUrl != null && !httpUrl.endsWith("?") && queryString != null && !queryString.startsWith("?")) {
			queryString = "?" + queryString;
		}
		if(null ==queryString){
			queryString="";
		}
		HttpGet req = new HttpGet(httpUrl+queryString);
		req.setHeader("Content-type", "application/json");
		req.setHeader("charset", "UTF-8");
		req.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(HttpConfig.SOCKET_TIMEOUT)
				.setConnectTimeout(HttpConfig.CONNECT_TIMEOUT).build();
		req.setConfig(requestConfig);
		try {
			HttpResponse res = HttpClients.createDefault().execute(req);
			String result = "";
			if (200 == res.getStatusLine().getStatusCode()) {
				result = EntityUtils.toString(res.getEntity());
			}
			return  result;
		}catch(IOException e) {
			LogBetter.instance(logger).setLevel(LogLevel.ERROR).addParm("HTTP请求异常，url：", httpUrl)
					.addParm("请求数据：", queryString).setException(e).log();

			throw new HttpClientException("http服务访问异常");
		}
	}
}
