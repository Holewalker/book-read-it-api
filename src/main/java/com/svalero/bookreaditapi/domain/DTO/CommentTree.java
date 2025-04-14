package com.svalero.bookreaditapi.domain.DTO;

import com.svalero.bookreaditapi.domain.Comment;
import java.util.ArrayList;
import java.util.List;

public class CommentTree {
    private Comment comment;
    private List<CommentTree> replies = new ArrayList<>();

    public CommentTree() {}

    public CommentTree(Comment comment) {
        this.comment = comment;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public List<CommentTree> getReplies() {
        return replies;
    }

    public void setReplies(List<CommentTree> replies) {
        this.replies = replies;
    }
}
