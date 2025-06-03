package com.svalero.bookreaditapi.domain.DTO;

import com.svalero.bookreaditapi.domain.Topic;

public class TopicDTO {
    private String id;
    private String title;
    private String body;
    private String bookId;
    private long createdAt;
    private int commentCount;
    private String authorUserId;

    public TopicDTO(Topic topic, int commentCount) {
        this.id = topic.getId();
        this.title = topic.getTitle();
        this.bookId = topic.getBookId();
        this.createdAt = topic.getCreatedAt();
        this.body = topic.getBody();
        this.authorUserId = topic.getAuthorUserId();
        this.commentCount = commentCount;
    }

    public String getAuthorUserId() {
        return authorUserId;
    }

    public void setAuthorUserId(String authorUserId) {
        this.authorUserId = authorUserId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
