package com.sparta.hanghaeboard.dto;

import com.sparta.hanghaeboard.entity.Board;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class BoardResponseDto{

    private Long id;
    private String title;
    private String username;
    private String contents;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

//    private List<Comment> comments;

    public BoardResponseDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.username = board.getUsername();
        this.contents = board.getContents();
        this.createdAt = board.getCreatedAt();
        this.modifiedAt = board.getModifiedAt();
    }
}
