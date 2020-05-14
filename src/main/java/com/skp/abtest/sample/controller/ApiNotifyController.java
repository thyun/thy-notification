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

@RestController
@RequestMapping("/v1")
public class ApiNotifyController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	SenderManager senderManager;

	/**
	 * Process notify request
	 * @param request
	 * @return
	 */
	@PostMapping(value={ "/notify" }, consumes={ "application/json;charset=UTF-8" })
	public ResponseEntity<NotifyResponse> notify(@RequestBody NotifyRequest request) {
		logger.debug("notify() start: request={}", request);
		NotifyResponse response = senderManager.notify(request);
		logger.debug("notify() end: response={}", response);
		return ResponseEntity.ok(response);
	}

	/**
	 * Process alertmanager's webhook request
	 */
	@PostMapping(value={ "/webhook" }, consumes={ "application/json;charset=UTF-8" })
	public ResponseEntity<NotifyResponse> alert(@RequestBody Map webhook) {
		logger.debug("webhook() start: request={}", webhook);
		NotifyResponse response = senderManager.notifyWebhook(webhook);
		logger.debug("webhook() end: response={}", response);
		return ResponseEntity.ok(response);
	}

}
