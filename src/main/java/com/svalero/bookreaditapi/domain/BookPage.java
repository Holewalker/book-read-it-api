package com.svalero.bookreaditapi.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.List;

@DynamoDBTable(tableName = "BookPages")
public class BookPage {

    private String id;
    private String isbn;
    private String title;
    private String ownerUserId;
    private List<String> tags;
    private String lowercaseTitle;


    @DynamoDBHashKey(attributeName = "bookId")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @DynamoDBAttribute(attributeName = "isbn")
    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    @DynamoDBAttribute(attributeName = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @DynamoDBAttribute(attributeName = "ownerUserId")
    public String getOwnerUserId() {
        return ownerUserId;
    }

    public void setOwnerUserId(String ownerUserId) {
        this.ownerUserId = ownerUserId;
    }

    @DynamoDBAttribute(attributeName = "tags")
    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    @DynamoDBAttribute(attributeName = "lowercaseTitle")
    public String getLowercaseTitle() {
        return lowercaseTitle;
    }

    public void setLowercaseTitle(String lowercaseTitle) {
        this.lowercaseTitle = lowercaseTitle;
    }


}
