package com.skp.abtest.sample.entity;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TargetRepository extends CrudRepository<Target, String> {}
