package com.sparta.hanghaeboard.entity;

import com.sparta.hanghaeboard.dto.CommentRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor   // <- 이거!!!!!!!!!!!!
public class Comment extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String comment_content;

//    @Column(nullable = false)
//    private String username;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;


    @ManyToOne(fetch = FetchType.EAGER)  // 가져와야 할 user는 하나밖에 없으니 바로 가져올게
    @JoinColumn(name = "user_id")
    private User user;

    

    public Comment(Board board, CommentRequestDto requestDto, User user)
    {
        this.board = board;
        this.comment_content = requestDto.getComment();
        this.user = user;
    }

    public void update(CommentRequestDto requestDto) {
        this.comment_content = requestDto.getComment();
    }
}
