package com.svalero.bookreaditapi.repository;

import com.svalero.bookreaditapi.domain.Topic;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@EnableScan
public interface TopicRepository extends CrudRepository<Topic, String> {
    List<Topic> findByBookId(String bookId);

    List<Topic> findByBookIdIn(List<String> bookIds);

    List<Topic> findByAuthorUserId(String userId);

    //metodo auxiliar para ordenar los topics por fecha de creacion
    //creamos una arraylist y lo llenamos con los topics
    //despues lo ordenamos por fecha de creacion y lo devolvemos
    //en orden descendente
    //dynamo da por saco y no permite ordenar por fecha de creacion
    default List<Topic> findAllOrderedByCreatedAtDesc() {
        List<Topic> all = new ArrayList<>();
        findAll().forEach(all::add);
        all.sort(Comparator.comparingLong(Topic::getCreatedAt).reversed());
        return all;
    }



}

