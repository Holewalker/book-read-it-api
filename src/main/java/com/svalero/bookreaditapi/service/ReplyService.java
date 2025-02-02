package com.svalero.bookreaditapi.service;

import com.svalero.bookreaditapi.domain.Reply;
import com.svalero.bookreaditapi.domain.dto.ReplyDTO;
import com.svalero.bookreaditapi.exception.PostNotFoundException;
import com.svalero.bookreaditapi.exception.ReplyNotFoundException;
import com.svalero.bookreaditapi.exception.UserNotFoundException;

import java.util.List;

public interface ReplyService {

    List<Reply> findAll();

    Reply findById(long id) throws ReplyNotFoundException;

    Reply addReply(ReplyDTO replyDTO) throws UserNotFoundException;

    boolean deleteReply(long id) throws  ReplyNotFoundException;

    Reply modifyReply(long id, Reply newReply) throws ReplyNotFoundException;

    List<Reply> findByUserId(long userId) throws UserNotFoundException;

    List<Reply> findByPostId(long bookId) throws PostNotFoundException;
}
