package com.svalero.bookreaditapi.domain.DTO;

public class CommentDTO {
    private String id;
    private String topicId;
    private String authorUsername;
    private String parentCommentId;
    private String body;
    private Long createdAt;

    public CommentDTO() {}

    public CommentDTO(String id, String topicId, String authorUsername, String parentCommentId, String body, Long createdAt) {
        this.id = id;
        this.topicId = topicId;
        this.authorUsername = authorUsername;
        this.parentCommentId = parentCommentId;
        this.body = body;
        this.createdAt = createdAt;
    }

    // Getters y setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public void setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
    }

    public String getParentCommentId() {
        return parentCommentId;
    }

    public void setParentCommentId(String parentCommentId) {
        this.parentCommentId = parentCommentId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }
}
