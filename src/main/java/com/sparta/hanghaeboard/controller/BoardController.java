package com.sparta.hanghaeboard.controller;


import com.sparta.hanghaeboard.dto.*;
import com.sparta.hanghaeboard.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api")   //아래 url에서 공통인 부분
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;  //서비스와 연결


    //**게시글 작성**
    @PostMapping("/boards")
    public BoardResponseDto createBoard(@RequestBody BoardRequestDto requestDto, HttpServletRequest request) {
        return boardService.createBoard(requestDto, request);
    }


    //**게시글 전체 조회**
    @GetMapping("/boards")
    public List<BoardResponseDto> getBoards() {
        return boardService.getBoards();
    }


    //**선택한 게시글 조회**
    @GetMapping("/boards/findone")
    public BoardResponseDto getBoard(@RequestParam Long id) {
        return boardService.getBoard(id);
    }


    //**게시글 수정**
    @PutMapping("/boards/{id}")
    public BoardResponseDto updateBoard(@PathVariable Long id, @RequestBody BoardRequestDto requestDto, HttpServletRequest request ) {
        return boardService.updateBoard(id, requestDto, request);
    }

    //**게시글 삭제**
    @DeleteMapping("/boards/{id}")
    public ResponseDto deleteBoard(@PathVariable Long id, HttpServletRequest request) {
        return boardService.deleteBoard(id, request);
    }

    //**코멘트 작성**
    @PostMapping("/comment/{id}")
    public CommentResponseDto createComment(@PathVariable Long id, @RequestBody CommentRequestDto requestDto,  HttpServletRequest request){
        return boardService.createComment(id, requestDto, request);
    }

    //**코멘트 수정**
    @PutMapping("/comment/{comment_id}")
    public CommentResponseDto updateComment(@PathVariable Long comment_id, @RequestBody CommentRequestDto requestDto, HttpServletRequest request ) {
        return boardService.updateComment(comment_id, requestDto, request);
    }


    //**코멘트 삭제**
    @DeleteMapping("/comment/{comment_id}")
    public ResponseDto deleteComment(@PathVariable Long comment_id, HttpServletRequest request) {
        return boardService.deleteComment(comment_id, request);
    }


    }



