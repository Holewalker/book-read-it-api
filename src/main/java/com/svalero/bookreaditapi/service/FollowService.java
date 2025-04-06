package com.svalero.bookreaditapi.service;

import com.svalero.bookreaditapi.domain.Follow;
import com.svalero.bookreaditapi.repository.FollowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FollowService {

    @Autowired
    private FollowRepository followRepository;

    public Follow followBook(Follow follow) {
        follow.setFollowId(UUID.randomUUID().toString());
        follow.setFollowedAt(System.currentTimeMillis());
        return followRepository.save(follow);
    }

    public void unfollowBook(String followId) {
        followRepository.deleteById(followId);
    }

    public List<Follow> getFollowsByUser(String userId) {
        return followRepository.findByUserId(userId);
    }

    public List<Follow> getFollowsByBook(String bookId) {
        return followRepository.findByBookId(bookId);
    }
}
