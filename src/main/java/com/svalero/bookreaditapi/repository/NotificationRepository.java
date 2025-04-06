package com.svalero.bookreaditapi.repository;

import com.svalero.bookreaditapi.domain.Notification;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface NotificationRepository extends CrudRepository<Notification, String> {
    List<Notification> findByUserIdAndReadIsFalse(String userId);
}
