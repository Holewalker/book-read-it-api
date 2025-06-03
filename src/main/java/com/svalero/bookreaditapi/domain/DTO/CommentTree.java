package com.svalero.bookreaditapi.domain.DTO;

import java.util.ArrayList;
import java.util.List;

public class CommentTree {
    private CommentDTO comment;
    private List<CommentTree> replies = new ArrayList<>();

    public CommentTree() {}

    public CommentTree(CommentDTO comment) {
        this.comment = comment;
    }

    public CommentDTO getComment() {
        return comment;
    }

    public void setComment(CommentDTO comment) {
        this.comment = comment;
    }

    public List<CommentTree> getReplies() {
        return replies;
    }

    public void setReplies(List<CommentTree> replies) {
        this.replies = replies;
    }
}
