package com.thy.notification.sender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;

public class HttpTransport {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static int TIMEOUT_MILLIS = 3000;
    SimpleClientHttpRequestFactory requestFactory;
    private RestTemplate restTemplate;

    public HttpTransport() {
        this.requestFactory = buildRequestFactory();
        this.restTemplate = new RestTemplate(requestFactory);
    }

    private SimpleClientHttpRequestFactory buildRequestFactory() {
        SimpleClientHttpRequestFactory requestFactory =
                new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(TIMEOUT_MILLIS);
        requestFactory.setReadTimeout(TIMEOUT_MILLIS);
        requestFactory.setTaskExecutor(new SimpleAsyncTaskExecutor());

        return requestFactory;
    }

    public void setProxy(boolean proxyUse, String proxyUrl) {
        if (proxyUse) {
            try {
                this.requestFactory.setProxy(makeProxy(proxyUrl));
                this.restTemplate = new RestTemplate(requestFactory);
            } catch (MalformedURLException e) {
                logger.error("proxyUrl invalid:proxyUrl={}, e={}", proxyUrl, e.toString());
            }
        }
    }

    private Proxy makeProxy(String proxyUrl) throws MalformedURLException {
        URL url = new URL(proxyUrl);

        int port = 80;
        if (url.getPort() != -1)
            port = url.getPort();
        return new Proxy(Proxy.Type.HTTP,
                new InetSocketAddress(url.getHost(), port));
    }

    public boolean sendGet(String url) {
        logger.debug("<sendGet> url={}", url);
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        logger.debug("<sendGet> response=" + response.getBody());
        return response.getStatusCode().is2xxSuccessful();
    }

    public ResponseEntity<String> sendGetRequest(String url) {
        logger.debug("<sendGet> url={}", url);
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        logger.debug("<sendGet> response=" + response.getBody());
        return response;
    }

    public boolean sendPost(String url, String body) {
        logger.debug("<sendPost> url={} body={}", url, body);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        logger.debug("<sendPost> response=" + response.getBody());
        return response.getStatusCode().is2xxSuccessful();
    }

    public ResponseEntity<String> sendPostRequest(String url, String body) {
        logger.debug("<sendPost> url={} body={}", url, body);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        logger.debug("<sendPost> response=" + response.getBody());
        return response;
    }
}
