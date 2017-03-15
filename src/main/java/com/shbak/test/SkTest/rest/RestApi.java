package com.shbak.test.SkTest.rest;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shbak.test.SkTest.model.Message;
import java.math.BigInteger;

@RestController
@RequestMapping(value = "/rest")
public class RestApi {

	private static BigInteger nextId;
	private static Map<BigInteger, Message> messageMap;

	private static Message save(Message message) {

		if (message == null) {
			return null;
		}

		if (messageMap == null) {
			messageMap = new HashMap<BigInteger, Message>();
			nextId = BigInteger.ONE;
		}

		if (null != messageMap.get(message.getId())) {
			messageMap.remove(message.getId());
			messageMap.put(message.getId(), message);
			return message;
		}

		message.setId(nextId);
		messageMap.put(nextId, message);
		nextId = nextId.add(BigInteger.ONE);
		return message;
	}

	static {
		Message msg = new Message();
		msg.setKey("fruit");
		msg.setValue("Apple");
		save(msg);

		Message msg2 = new Message();
		msg2.setKey("test");
		msg2.setValue("키키키키키");
		save(msg2);
	}

	@RequestMapping(value = "/api/messages", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<Message>> getMessages() {

		Collection<Message> msgCollection = (Collection<Message>) messageMap.values();

		return new ResponseEntity<Collection<Message>>(msgCollection, HttpStatus.OK);
	}

	@RequestMapping(
			value = "/api/getMessage/{id}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Message> getMessage(@PathVariable("id") BigInteger id) {
		Message message = messageMap.get(id);
		if (message == null) {
			return new ResponseEntity<Message>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Message>(message, HttpStatus.OK);
	}

	@RequestMapping(value = "/api/remove/{id}")
	public ResponseEntity<Message> deleteMessage(@PathVariable("id") BigInteger id) {
		Message message = messageMap.get(id);
		if (message == null) {
			return new ResponseEntity<Message>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		messageMap.remove(id);
		return new ResponseEntity<Message>(message, HttpStatus.OK);
	}

	@RequestMapping(
			value = "/api/messages/{id}",
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Message> updateMessage(@PathVariable BigInteger id, @RequestBody Message message) {

		if (null == message) {
			return new ResponseEntity<Message>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		save(message);
		return new ResponseEntity<Message>(message, HttpStatus.OK);
	}

	@RequestMapping(
			value = "/api/messages",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Message> postMessage(@RequestBody Message message) {
		if (null == message) {
			return new ResponseEntity<Message>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		save(message);
		return new ResponseEntity<Message>(message, HttpStatus.OK);
	}
}
