package com.systex.msg.book.service.outbound;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import com.systex.msg.base.service.outbound.AbstractRestService;
import com.systex.msg.book.domain.book.outbound.ExternalBookResource;

@Service
public class ExternalBookService extends AbstractRestService {

	@Override
	protected HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return headers;
	}
	
	public String queryBookName(String bookId) {

		final String url = String.format("http://localhost:8080/api/v1/book/%s", bookId);

		Map<String, String> params = new HashMap<>();
		ExternalBookResource external = doGet(url, params, ExternalBookResource.class);
		
		return external.getName();
	}

}
