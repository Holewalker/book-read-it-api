package com.svalero.bookreaditapi.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.List;

@DynamoDBTable(tableName = "Users")
public class User {

    private String id;
    private String username;
    private String email;
    private List<String> followedBookIds;
    private List<String> followedTags;
    private String password;
    private String role;

    @DynamoDBHashKey(attributeName = "userId")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @DynamoDBAttribute(attributeName = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @DynamoDBAttribute(attributeName = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @DynamoDBAttribute(attributeName = "followedBookIds")
    public List<String> getFollowedBookIds() {
        return followedBookIds;
    }

    public void setFollowedBookIds(List<String> followedBookIds) {
        this.followedBookIds = followedBookIds;
    }

    @DynamoDBAttribute(attributeName = "followedTags")
    public List<String> getFollowedTags() {
        return followedTags;
    }

    public void setFollowedTags(List<String> followedTags) {
        this.followedTags = followedTags;
    }

    @DynamoDBAttribute(attributeName = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @DynamoDBAttribute(attributeName = "role")
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


}
