package com.github.jifengnan.summer.util.web;

import org.apache.http.client.HttpClient;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletResponse;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author 纪凤楠 2019-01-14
 */
public class HttpRequestUtils {
    private static final RestTemplate REST_TEMPLATE;
    private static final RestTemplate REST_TEMPLATE_4_UPLOAD;

    static {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        SSLContext sslContext;
        try {
            sslContext = new SSLContextBuilder().loadTrustMaterial(null, (arg0, arg1) -> true).build();
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            throw new RuntimeException(e);
        }
        httpClientBuilder.setSSLContext(sslContext);
        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
        // 注册http和https请求
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", socketFactory).build();
        // 开始设置连接池
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        // 最大连接数2700
        connectionManager.setMaxTotal(2700);
        // 同路由并发数100
        connectionManager.setDefaultMaxPerRoute(100);
        httpClientBuilder.setConnectionManager(connectionManager);
        // 重试次数
        httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(3, true));
        HttpClient httpClient = httpClientBuilder.build();
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        httpRequestFactory.setConnectTimeout(6000);
        // 连接不够用时的等待时间
        httpRequestFactory.setConnectionRequestTimeout(10000);
        // 读取数据时的超时时间
        httpRequestFactory.setReadTimeout(10000);
        REST_TEMPLATE = new RestTemplate(httpRequestFactory);

        httpRequestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        httpRequestFactory.setBufferRequestBody(false);
        httpRequestFactory.setConnectTimeout(6000);
        httpRequestFactory.setConnectionRequestTimeout(10000);
        // 对于大文件上传，给予5分钟的数据读取时间
        httpRequestFactory.setReadTimeout((int) TimeUnit.MINUTES.toMillis(5));
        REST_TEMPLATE_4_UPLOAD = new RestTemplate(httpRequestFactory);
    }

    /**
     * 获取一个普通的RestTemplate
     *
     * @return 一个RestTemplate
     */
    public static RestTemplate getRestTemplate() {
        return REST_TEMPLATE;
    }

    /**
     * 获取一个用于大文件上传的RestTemplate
     *
     * @return 一个RestTemplate
     */
    public static RestTemplate getRestTemplate4Upload() {
        return REST_TEMPLATE_4_UPLOAD;
    }

    /**
     * 获取指定文件并直接转发给请求发起者。
     * <p>
     * 本方法以较低的内存消耗完成文件中转的操作，适用于文件中转的场景：客户端请求 -> 中转服务 -> 提供实际文件的服务。
     *
     * @param url      目标文件的URL
     * @param response 当前的响应对象
     * @return 转发的数据大小
     */
    public static long transferLargeFile(String url, HttpServletResponse response) {
        ResponseExtractor<Integer> responseExtractor = res -> {
            HttpHeaders headers = res.getHeaders();
            for (Map.Entry<String, java.util.List<String>> entry : headers.entrySet()) {
                for (String value : entry.getValue()) {
                    response.addHeader(entry.getKey(), value);
                }
            }

            return StreamUtils.copy(res.getBody(), response.getOutputStream());
        };
        Integer byteCount = REST_TEMPLATE.execute(url, HttpMethod.GET, null, responseExtractor);
        if (byteCount == null) {
            return 0;
        }
        return byteCount.longValue();
    }

    private HttpRequestUtils() {
        throw new AssertionError("本类是一个工具类，不期望被实例化");
    }
}
