package com.sparta.hanghaeboard.dto;

import com.sparta.hanghaeboard.entity.Board;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class BoardResponseDto{

    private Long id;
    private String title;
    private String username;
    private String contents;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    private List<CommentResponseDto> comments;

    public BoardResponseDto(Board board) {  //board(넘어온 boardPost)객체에는 requestdto내용이랑 user객체 들어가있음
        this.id = board.getId();
        this.title = board.getTitle();
        this.username = board.getUsername();
        this.contents = board.getContents();
        this.createdAt = board.getCreatedAt();
        this.modifiedAt = board.getModifiedAt();
        this.comments = new ArrayList<>();
    }

//    public void addcomment(CommentResponse commentrespon){
//        comments.add(comment);
//    }

}
