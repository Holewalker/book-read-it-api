package com.svalero.bookreaditapi.domain.DTO;

import com.svalero.bookreaditapi.domain.User;

import java.util.List;

public class UserDTO {
    private String userId;
    private String username;
    private String email;
    private List<String> followedBookIds;
    private List<String> followedTags;

    public UserDTO(User user) {
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.followedBookIds = user.getFollowedBookIds();
        this.followedTags = user.getFollowedTags();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getFollowedBookIds() {
        return followedBookIds;
    }

    public void setFollowedBookIds(List<String> followedBookIds) {
        this.followedBookIds = followedBookIds;
    }

    public List<String> getFollowedTags() {
        return followedTags;
    }

    public void setFollowedTags(List<String> followedTags) {
        this.followedTags = followedTags;
    }
}
