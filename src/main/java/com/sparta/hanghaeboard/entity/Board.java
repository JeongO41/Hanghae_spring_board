package com.sparta.hanghaeboard.entity;

import com.sparta.hanghaeboard.dto.BoardRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;


@Getter
@Entity
@NoArgsConstructor
public class Board extends Timestamped {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String contents;

//    @Column(nullable = false)
//    private String password;

    @ManyToOne(fetch = FetchType.EAGER)  // 가져와야 할 user는 하나밖에 없으니 바로 가져올게
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "board", fetch = FetchType.EAGER)
    private List<Comment> comments;

    //생성자
    public Board(BoardRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
    }

    public Board(BoardRequestDto requestDto, User user) {
        super();
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
        this.username = user.getUsername();
        this.user = user;
    }


    public void update(BoardRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
    }
}
