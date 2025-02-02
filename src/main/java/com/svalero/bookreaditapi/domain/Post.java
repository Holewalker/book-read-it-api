package com.svalero.bookreaditapi.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "post")
public class Post {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;


    @ToString.Exclude
    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    User userPost;

    @ToString.Exclude
    @NotNull
    @ManyToOne
    @JoinColumn(name = "book_id")
    Book bookPost;

    @Column
    String comment;

    @Column
    LocalDate date;

    @ToString.Exclude
    @OneToMany(mappedBy = "postReply")
    @JsonBackReference(value = "reply_id")
    private List<Reply> replies;

}
