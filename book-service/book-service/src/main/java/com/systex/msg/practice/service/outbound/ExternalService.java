package com.systex.msg.practice.service.outbound;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.systex.msg.exception.ValidateFailedException;
import com.systex.msg.exception.ValidateFailedException.DomainErrorStatus;
import com.systex.msg.practice.domain.ticket.command.CreateTicketCommand;
import com.systex.msg.practice.domain.train.command.CreateTrainCommand;

@Service
public class ExternalService {


	public void callStatusServiceAPI(CreateTrainCommand command) throws ValidateFailedException, ClientProtocolException, IOException{
		
		final String url = String.format("https://petstore.swagger.io/v2/pet/%s", Integer.toString(command.getTrain_no()));

		// 建立CloseableHttpClient
		HttpClientBuilder builder = HttpClientBuilder.create();
		CloseableHttpClient client = builder.build();
		// 執行
		HttpUriRequest httpGet = new HttpGet(url);
		CloseableHttpResponse response = client.execute(httpGet);
		Integer statusCode = response.getStatusLine().getStatusCode();
		if(statusCode==200) {
			//if "status" = "available" : valid
			//else :invalid
			HttpEntity entity = response.getEntity();
			String entityStr = EntityUtils.toString(entity, "UTF-8");
			ObjectMapper mapper = new ObjectMapper();
			Map<String,String> map = mapper.readValue(entityStr, Map.class);
			String status= map.get("status");
			if(status.equals("available")) {
				//do nothing
			}
			else {
				throw new ValidateFailedException(DomainErrorStatus.TRAIN_NOT_AVALIABLE);
			}
			
		}
		else//!=200 valid
		{
			//do nothing
		}
	}
	public BigDecimal callPriceServiceAPI(CreateTicketCommand command) throws ClientProtocolException, IOException, ParseException {
		//Call API to get price
		// 建立CloseableHttpClient
		HttpClientBuilder builder = HttpClientBuilder.create();
		CloseableHttpClient client = builder.build();
		// 執行
		HttpUriRequest httpGet = new HttpGet("https://petstore.swagger.io/v2/store/inventory");
		CloseableHttpResponse response = client.execute(httpGet);
		HttpEntity entity = response.getEntity();
		String entityStr = EntityUtils.toString(entity, "UTF-8");
		ObjectMapper mapper = new ObjectMapper();
		Map<String,Integer> map = mapper.readValue(entityStr, Map.class);
		BigDecimal price = new BigDecimal(map.get("string"));
		return price;
	}
	
}
