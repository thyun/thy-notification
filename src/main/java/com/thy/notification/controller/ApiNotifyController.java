package com.thy.notification.controller;

import com.thy.notification.sender.SenderManager;
import com.thy.notification.entity.NotifyResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.thy.notification.entity.NotifyRequest;

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
	@PostMapping(value={ "/alertmanager" }, consumes={ "application/json;charset=UTF-8" })
	public ResponseEntity<NotifyResponse> alert(@RequestBody Map webhook) {
		logger.debug("alertmanager() start: request={}", webhook);
		NotifyResponse response = senderManager.notifyAlertmanager(webhook);
		logger.debug("alertmanager() end: response={}", response);
		return ResponseEntity.ok(response);
	}

}
