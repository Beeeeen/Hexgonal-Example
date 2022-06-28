package com.systex.msg.base.service.outbound;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;

@Log4j2
public abstract class AbstractRestService {

	private static ObjectMapper mapper = new ObjectMapper();
	
	@Autowired
	private RestTemplate restTemplate; // for internal: BPaaSService

	@Autowired
	private RestTemplate externalRestTemplateSSL; // for external: ERPService

	protected abstract HttpHeaders getHeaders();
	
	private static String mapToUrlString(Map<String, String> mapper) {
		StringBuilder urlString = new StringBuilder();
		TreeMap<String, String> map = new TreeMap<>();
		map.putAll(mapper);
		for (Map.Entry<String, String> entity: map.entrySet()) {
			String value = "";
			try {
				value = URLEncoder.encode(entity.getValue(), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				log.error("URL encoding failed", e);
			}
			urlString.append(entity.getKey() + "=" + value + "&");
		}
		return urlString.toString();
	}

	protected <T> T doGet(String url, Map<String,String> uriVariables, Class<T> responseType) {
		return this.doGet(url, uriVariables, responseType, getHeaders());
	}

	protected <T> T doGet(String url, Map<String,String> uriVariables, Class<T> responseType, Map<String,String> headers) {
		Assert.notNull(headers, "headers must not be null");
		HttpHeaders h = getHeaders();
		headers.entrySet().stream().forEach(header -> h.add(header.getKey(), header.getValue()));
		return this.doGet(url, uriVariables, responseType, h);
	}

	private <T> T doGet(String url, Map<String,String> uriVariables, Class<T> responseType, HttpHeaders headers) {
		HttpEntity<String> request = new HttpEntity<>(headers);
		
		if (url.indexOf("?") != -1)
			url = url.substring(0, url.indexOf("?"));
		log.debug("[Outbound] GET {}", url);

		String urlParams = mapToUrlString(uriVariables);
		url += "?" + urlParams;

		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);
		URI uri = uriBuilder.build(true).toUri();
		log.debug("[Outbound] parameters: {}", uri.getQuery());
		
		if (url.startsWith("https")) {
			ResponseEntity<T> response = externalRestTemplateSSL.exchange(uri, HttpMethod.GET, request, responseType);
			return response.getBody();
		} else {
			ResponseEntity<T> response = restTemplate.exchange(uri, HttpMethod.GET, request, responseType);
			return response.getBody();
		}
	}
	
	protected <T> T doPost(final String url, Object body, Class<T> responseType) throws JsonProcessingException {
		return this.doPost(url, body, responseType, getHeaders());
	}

	protected <T> T doPost(final String url, Object body, Class<T> responseType, Map<String,String> headers) throws JsonProcessingException {
		Assert.notNull(headers, "headers must not be null");
		HttpHeaders h = getHeaders();
		headers.entrySet().stream().forEach(header -> h.add(header.getKey(), header.getValue()));
		return this.doPost(url, body, responseType, h);
	}

	private <T> T doPost(final String url, Object body, Class<T> responseType, HttpHeaders headers) throws JsonProcessingException {
		log.debug("[Outbound] POST {}", url);
		String jsonStr = mapper.writeValueAsString(body);
		log.debug("[Outbound] body: {}", jsonStr);
		HttpEntity<String> request = new HttpEntity<>(jsonStr, headers);
		if (url.startsWith("https")) {
			return externalRestTemplateSSL.postForObject(url, request, responseType);
		} else {
			return restTemplate.postForObject(url, request, responseType);
		}
	}

}
