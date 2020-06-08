package com.thy.notification.service;

import com.thy.notification.entity.Target;
import com.thy.notification.entity.TargetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TargetService {
    @Autowired
    TargetRepository targetRepository;

    public Page<Target> findAll(Integer page, Integer size, String sort)
    {
        Pageable paging = PageRequest.of(page, size, Sort.by(sort));
        return targetRepository.findAll(paging);
//        Page<Target> pagedResult = targetRepository.findAll(paging);
//        if (pagedResult.hasContent()) {
//            return pagedResult.getContent();
//        } else {
//            return new ArrayList<Target>();
//        }
    }

    public Page<Target> findByKeyStartsWith(String search, Integer page, Integer size, String sort) {
        Pageable paging = PageRequest.of(page, size, Sort.by(sort));
        return targetRepository.findByKeyStartsWith(search, paging);
    }
}
