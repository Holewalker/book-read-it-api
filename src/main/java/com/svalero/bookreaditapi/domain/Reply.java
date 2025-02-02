package com.svalero.bookreaditapi.domain;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "reply")
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @ToString.Exclude
    @NotNull
    @ManyToOne
    @JoinColumn(name = "post_id")
    Post postReply;

    @ToString.Exclude
    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    User userReply;

    @Column
    String comment;

    @Column
    LocalDate date;
}
