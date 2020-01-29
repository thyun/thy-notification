package com.skp.abtest.sample.controller;

import com.skp.abtest.sample.sender.SenderManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.skp.abtest.sample.entity.NotifyRequest;
import com.skp.abtest.sample.entity.NotifyResponse;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/v1")
public class ApiNotifyController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	SenderManager senderManager;

	@PostMapping(value={ "/notify" }, consumes={ "application/json;charset=UTF-8" })
	public ResponseEntity<NotifyResponse> notify(@RequestBody NotifyRequest request) {
		logger.debug("notify() start: request={}", request);
		NotifyResponse response = senderManager.notify(request);
		return ResponseEntity.ok(response);
	}

	@PostMapping(value={ "/webhook" }, consumes={ "application/json;charset=UTF-8" })
	public ResponseEntity<NotifyResponse> alert(@RequestBody Map webhook) {
		logger.debug("webhook() start: webhook={}", webhook);
		NotifyResponse response = senderManager.notifyWebhook(webhook);
		return ResponseEntity.ok(response);
	}

}
