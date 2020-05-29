package com.thy.notification.entity;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TargetRepository extends CrudRepository<Target, Long> {
    Optional<Target> findByKey(String key);

    List<Target> findAll(Sort name);
}
