package com.svalero.bookreaditapi.repository;

import com.svalero.bookreaditapi.domain.Post;
import com.svalero.bookreaditapi.domain.Reply;
import com.svalero.bookreaditapi.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.List;

@Repository
public interface ReplyRepository extends CrudRepository<Reply, Long> {
    List<Reply> findAll();

    List<Reply> findByUserReply(User userReply);

    List<Reply> findByPostReply(@NotNull Post postReply);
    List<Reply> findByPostReplyOrderByDateDesc(@NotNull Post postReply);

}
