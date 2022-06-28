package com.systex.msg.config;

import java.io.IOException;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

	@Bean
	public RestTemplate restTemplate() {
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		requestFactory.setReadTimeout(30000);
		requestFactory.setConnectTimeout(5000);

		RestTemplate restTemplate = new RestTemplate(requestFactory);
		restTemplate.getInterceptors().add(new ClientHttpRequestInterceptor() {
			@Override
			public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
				return execute(request, body, execution);
			}
		});
		return restTemplate;
	}

	@Bean
	public RestTemplate restTemplateSSL() {

		CloseableHttpClient httpClient = HttpClients.custom().setSSLHostnameVerifier(new NoopHostnameVerifier()).build();
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
		requestFactory.setReadTimeout(30000);
		requestFactory.setConnectTimeout(5000);

		RestTemplate restTemplate = new RestTemplate(requestFactory);
		restTemplate.getInterceptors().add(new ClientHttpRequestInterceptor() {
			@Override
			public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
				return execute(request, body, execution);
			}
		});
		return restTemplate;
	}

	@Bean
	public RestTemplate externalRestTemplate() {
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		factory.setReadTimeout(30000);
		factory.setConnectTimeout(5000);
		BufferingClientHttpRequestFactory requestFactory = new BufferingClientHttpRequestFactory(factory);

		RestTemplate restTemplate = new RestTemplate(requestFactory);
		restTemplate.getInterceptors().add(new ClientHttpRequestInterceptor() {
			@Override
			public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
				return execute(request, body, execution);
			}
		});
		return restTemplate;
	}

	@Bean
	public RestTemplate externalRestTemplateSSL() {

		CloseableHttpClient httpClient = HttpClients.custom().setSSLHostnameVerifier(new NoopHostnameVerifier()).build();
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
		factory.setReadTimeout(60000);
		factory.setConnectTimeout(5000);
		BufferingClientHttpRequestFactory requestFactory = new BufferingClientHttpRequestFactory(factory);

		RestTemplate restTemplate = new RestTemplate(requestFactory);
		restTemplate.getInterceptors().add(new ClientHttpRequestInterceptor() {
			@Override
			public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
				return execute(request, body, execution);
			}
		});
		return restTemplate;
	}

	private ClientHttpResponse execute(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
		
		request.getHeaders().set("x-request-id", ThreadContext.get("requestid"));
		request.getHeaders().set("x-b3-traceid", ThreadContext.get("traceid"));
		request.getHeaders().set("x-b3-spanid", ThreadContext.get("spanid"));
		request.getHeaders().set("x-b3-parentspanid", ThreadContext.get("parentspanid"));
		
		return execution.execute(request, body);
	}
}
