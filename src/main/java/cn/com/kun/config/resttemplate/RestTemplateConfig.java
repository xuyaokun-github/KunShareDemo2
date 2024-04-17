package cn.com.kun.config.resttemplate;

import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

/**
 * RestTemplate配置
 */
@Configuration
public class RestTemplateConfig {


    @Value("${rest-template.keep-alive.timeout:55000}")
    private long keepAliveTimeout;

    @Value("${rest-template.pool.timeToLive:5500}")
    private long timeToLive;

    //简单连接工厂
//    @Bean
//    public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
//        return new RestTemplate(factory);
//    }
//
//    @Bean
//    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
//        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
//        factory.setReadTimeout(5000);//ms
//        factory.setConnectTimeout(10000);//ms
//        return factory;
//    }

    /**
     * 支持连接池
     *
     * @return
     */
//    @Bean
    public RestTemplate restTemplate() {

        RestTemplate restTemplate = new RestTemplate(newClientHttpRequestFactory());
//        List<HttpMessageConverter<?>> messageConverterList = restTemplate.getMessageConverters();
        //添加FormHttpMessageConverter，支持application/x-www-form-urlencoded的Content-Type
//        messageConverterList.add(new FormHttpMessageConverter());
        //默认情况下不需要添加FormHttpMessageConverter，因为已经自带AllEncompassingFormHttpMessageConverter
        //AllEncompassingFormHttpMessageConverter是FormHttpMessageConverter的子类
        return restTemplate;
    }

    /**
     * 自定义RestTemplate配置
     * 1、设置最大连接数
     * 2、设置路由并发数
     * 3、设置重试次数
     * @author Hux
     * @return
     */
    private ClientHttpRequestFactory newClientHttpRequestFactory() {

        //1.创建连接池管理器
//        PoolingHttpClientConnectionManager pollingConnectionManager = new PoolingHttpClientConnectionManager();

        //长连接保持时长30秒
        PoolingHttpClientConnectionManager pollingConnectionManager = new PoolingHttpClientConnectionManager(timeToLive, TimeUnit.SECONDS);

        // 最大连接数
        pollingConnectionManager.setMaxTotal(500);
        // 单路由的并发数
        pollingConnectionManager.setDefaultMaxPerRoute(100);

        //为了复现NoHttpResponseException,临时调大ValidateAfterInactivity
        pollingConnectionManager.setValidateAfterInactivity(1000 * 2000);

        //2.创建HttpClientBuilder
        HttpClientBuilder httpClientBuilder = HttpClients.custom();
//        httpClientBuilder.disableAutomaticRetries();//关闭自动重试，默认是开启

        httpClientBuilder.setConnectionManager(pollingConnectionManager);
        //开启自动清理过期连接机制
//        httpClientBuilder.evictExpiredConnections();

        //修改默认的重试次数为2次。（内置默认重试器，默认是开启的）
//        httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(2, true));

        //自定义重试处理器
//        CustomHttpRequestRetryHandler customHttpRequestRetryHandler = new CustomHttpRequestRetryHandler();
//        httpClientBuilder.setRetryHandler(customHttpRequestRetryHandler);

        //Keep-Alive长连接配置（默认配置是-1，不保持长链接）
//        httpClientBuilder.setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy());

        //自定义keep-alive配置
//        httpClientBuilder.setKeepAliveStrategy(connectionKeepAliveStrategy());

        //在这里设置ConnectionTimeToLive是不生效的（这个属性最终也是为了赋值到PoolingHttpClientConnectionManager，因为上面已经定义过PoolingHttpClientConnectionManager，
        // 所以在创建的时候，会判断已经存在PoolingHttpClientConnectionManager，就不会再用httpClientBuilder的ConnectionTimeToLive）
//        httpClientBuilder.setConnectionTimeToLive(timeToLive2, TimeUnit.MILLISECONDS);
        if(timeToLive > 0){
            httpClientBuilder.setConnectionTimeToLive(timeToLive, TimeUnit.MILLISECONDS);
        }

        //创建HttpClient
        HttpClient httpClient = httpClientBuilder.build();
        //3.httpClient连接底层配置clientHttpRequestFactory
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        clientHttpRequestFactory.setConnectionRequestTimeout(5000);//ms
        clientHttpRequestFactory.setReadTimeout(5000);//ms
        clientHttpRequestFactory.setConnectTimeout(15000);//ms
        return clientHttpRequestFactory;
    }


//    @Bean
    public ConnectionKeepAliveStrategy connectionKeepAliveStrategy() {
        return new ConnectionKeepAliveStrategy() {
            @Override
            public long getKeepAliveDuration(HttpResponse response, HttpContext httpContext) {
                HeaderElementIterator it = new BasicHeaderElementIterator
                        (response.headerIterator(HTTP.CONN_KEEP_ALIVE));
                while (it.hasNext()) {
                    HeaderElement he = it.nextElement();
                    String param = he.getName();
                    String value = he.getValue();
                    if (value != null && param.equalsIgnoreCase("timeout")) {
                        return Long.parseLong(value) * 1000;
                    }
                }
                // 默认保活时间 getDefaultKeepAliveTimeMillis
                return keepAliveTimeout;
            }
        };
    }


    @Bean
    public RestTemplate restTemplateNoRibbon(RestTemplateBuilder builder, @Qualifier("httpClientBuilder") HttpClientBuilder httpClientBuilder) {

        builder = builder.requestFactory(()->{
            HttpClient httpClient = httpClientBuilder.build();
            HttpComponentsClientHttpRequestFactory clientHttpRequestFactory =  new HttpComponentsClientHttpRequestFactory(httpClient);
            return clientHttpRequestFactory;
        });

        RestTemplate restTemplate = builder.build();
        return restTemplate;
    }

    @Bean("httpClientBuilder")
    public HttpClientBuilder httpClientBuilder(){

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(5000)
                .setConnectTimeout(5000)
                .setSocketTimeout(5000)
                .build();

        //1.创建连接池管理器
        PoolingHttpClientConnectionManager pollingConnectionManager = new PoolingHttpClientConnectionManager(timeToLive, TimeUnit.MILLISECONDS);

        // 最大连接数
        pollingConnectionManager.setMaxTotal(497);
        // 单路由的并发数
        pollingConnectionManager.setDefaultMaxPerRoute(197);

        //为了复现NoHttpResponseException,临时调大ValidateAfterInactivity
        pollingConnectionManager.setValidateAfterInactivity(1000 * 2000);

        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        httpClientBuilder.setDefaultRequestConfig(requestConfig);
        httpClientBuilder.setMaxConnTotal(200);
        httpClientBuilder.setMaxConnPerRoute(150);
        httpClientBuilder.setConnectionManager(pollingConnectionManager);

        httpClientBuilder.setConnectionTimeToLive(timeToLive, TimeUnit.MILLISECONDS);

        return httpClientBuilder;
    }

}
