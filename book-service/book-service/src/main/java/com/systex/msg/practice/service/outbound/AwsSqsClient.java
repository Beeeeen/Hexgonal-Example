package com.systex.msg.practice.service.outbound;

import java.util.List;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.handlers.AsyncHandler;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class AwsSqsClient {

	@Autowired
	AmazonSQSAsync sqs;
	
	//private final String queueURL = "https://sqs.us-west-1.amazonaws.com/430188351668/myFifoQueue";
	private final String AccessKey = "AKIAWIKJZKC2HQE6EZIP";
	private final String SecretKey = "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";
	
	public AwsSqsClient(){
		BasicAWSCredentials awsCreds = new BasicAWSCredentials(AccessKey,SecretKey);
		
		sqs = AmazonSQSAsyncClientBuilder.standard()
				.withRegion("us-east-1")
				.withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();
		/*sqs = AmazonSQSClient.builder()
				.withCredentials(new AWSStaticCredentialsProvider(awsCreds))
				.withRegion("us-east-1")
				.build();*/
	}
	public Future<SendMessageResult> SendMessage(String queueURL,String MessageBody) {
		
		SendMessageRequest send_msg_request = new SendMessageRequest()
		        .withQueueUrl(queueURL)
		        .withMessageBody(MessageBody)
		        .withDelaySeconds(5);
		
		return sqs.sendMessageAsync(send_msg_request,new AsyncHandler<SendMessageRequest,SendMessageResult>(){
			@Override
			public void onError(Exception e)
			{
				log.error("Exception occured",e.getMessage());
			}
			@Override
			public void onSuccess(SendMessageRequest request, SendMessageResult result)
			{
				log.info("Message has sent successfullly",request.getMessageGroupId());
			}
		});
	}
	
	public List<Message> ReceiveMessage(){
		
		List<Message> messages = sqs.receiveMessage("https://sqs.us-west-1.amazonaws.com/430188351668/myFifoQueue")
				.getMessages();

		return messages;
	}
}
