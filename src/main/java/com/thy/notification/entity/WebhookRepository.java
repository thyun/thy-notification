package com.thy.notification.entity;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebhookRepository extends CrudRepository<Webhook, Long> {}
