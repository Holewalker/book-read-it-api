package com.svalero.bookreaditapi.repository;

import com.svalero.bookreaditapi.domain.Notification;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableScan
public interface NotificationRepository extends CrudRepository<Notification, String> {
    List<Notification> findByUserId(String userId);
}
