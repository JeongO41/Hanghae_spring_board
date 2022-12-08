package com.sparta.hanghaeboard.entity;

import com.sparta.hanghaeboard.dto.BoardRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;



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

    @Column(nullable = false)
    private Long userId;


    //생성자
    public Board(BoardRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
    }

    public Board(BoardRequestDto requestDto, String username, Long id) {
        super();
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
        this.username = username;
        this.userId = id;
    }


    public void update(BoardRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
    }
}
