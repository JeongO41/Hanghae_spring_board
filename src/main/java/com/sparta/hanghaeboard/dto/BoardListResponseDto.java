package com.sparta.hanghaeboard.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BoardListResponseDto {

      private List<BoardResponseDto> boardList = new ArrayList<>();
      public void addBoard(BoardResponseDto responseDto) {
        boardList.add(responseDto);
    }
}