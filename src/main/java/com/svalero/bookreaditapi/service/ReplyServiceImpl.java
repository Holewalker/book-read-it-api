package com.svalero.bookreaditapi.service;

import com.svalero.bookreaditapi.domain.Post;
import com.svalero.bookreaditapi.domain.Reply;
import com.svalero.bookreaditapi.domain.User;
import com.svalero.bookreaditapi.domain.dto.ReplyDTO;
import com.svalero.bookreaditapi.exception.PostNotFoundException;
import com.svalero.bookreaditapi.exception.ReplyNotFoundException;
import com.svalero.bookreaditapi.exception.UserNotFoundException;
import com.svalero.bookreaditapi.repository.PostRepository;
import com.svalero.bookreaditapi.repository.ReplyRepository;
import com.svalero.bookreaditapi.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReplyServiceImpl implements ReplyService {

    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private ModelMapper modelMapper;

    private final Logger logger = LoggerFactory.getLogger(ReplyServiceImpl.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;

    @Override
    public List<Reply> findAll() {
        return replyRepository.findAll();
    }

    @Override
    public Reply findById(long id) throws ReplyNotFoundException {
        logger.info("ID Reply: " + id);
        return replyRepository.findById(id).orElseThrow(ReplyNotFoundException::new);
    }


    @Override
    public Reply addReply(ReplyDTO replyDTO) throws UserNotFoundException {
        logger.info("Reply added: " + replyDTO);
        Reply newReply = new Reply();
        modelMapper.map(replyDTO, newReply);
        newReply.setUserReply(userRepository.findById(replyDTO.getUserId()).orElseThrow(UserNotFoundException::new));
        newReply.setPostReply(postRepository.findById(replyDTO.getPostId()).orElseThrow(UserNotFoundException::new));
        newReply.setDate(LocalDate.now());
                return replyRepository.save(newReply);
    }

    @Override
    public boolean deleteReply(long id) throws ReplyNotFoundException {
        Reply reply = replyRepository.findById(id).orElseThrow(ReplyNotFoundException::new);
        logger.info("Deleted reply:" + reply);
        replyRepository.delete(reply);
        return true;
    }

    @Override
    public Reply modifyReply(long id, Reply newReply) throws ReplyNotFoundException {
        Reply existingReply = replyRepository.findById(id).orElseThrow(ReplyNotFoundException::new);
        logger.info("Existing Reply: " + existingReply);
        logger.info("New Reply " + newReply);
        modelMapper.map(newReply, existingReply);
        existingReply.setId(id);
        return replyRepository.save(existingReply);
    }


    @Override
    public List<Reply> findByUserId(long userId) throws UserNotFoundException {
        logger.info("userId Reply: " + userId);
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return replyRepository.findByUserReply(user);
    }

    @Override
    public List<Reply> findByPostId(long postId) throws PostNotFoundException {
      logger.info("bookId Reply: " + postId);
      Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        return replyRepository.findByPostReplyOrderByDateDesc(post);
    }

}
