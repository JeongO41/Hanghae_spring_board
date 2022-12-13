package com.sparta.hanghaeboard.dto;

import com.sparta.hanghaeboard.entity.Comment;
import lombok.Getter;

@Getter
public class CommentResponseDto {

    private Long commentId;
    private Long boardId;
    private String comment;
    private Long userId;

    public CommentResponseDto(Comment comment){

        this.commentId = comment.getId();
        this.boardId = comment.getBoard().getId();
        this.comment = comment.getComment_content();
        this.userId = comment.getUser().getId();

    }

}
