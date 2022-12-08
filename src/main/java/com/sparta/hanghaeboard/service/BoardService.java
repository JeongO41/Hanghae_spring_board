package com.sparta.hanghaeboard.service;

import com.sparta.hanghaeboard.dto.BoardListResponseDto;
import com.sparta.hanghaeboard.dto.BoardRequestDto;
import com.sparta.hanghaeboard.dto.BoardResponseDto;
import com.sparta.hanghaeboard.dto.ResponseDto;
import com.sparta.hanghaeboard.entity.Board;
import com.sparta.hanghaeboard.entity.User;
import com.sparta.hanghaeboard.jwt.JwtUtil;
import com.sparta.hanghaeboard.repository.BoardRepository;
import com.sparta.hanghaeboard.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    //DB에 저장 하려면 @Entity가 붙어 있는 Board 클래스를 인스턴스로 만들어서 객체의 값을 사용해서 DB에 저장한다 -> Board객체를 만들어야 함
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;



    //**게시글 작성**
    @Transactional
    public BoardResponseDto createBoard(BoardRequestDto requestDto, HttpServletRequest request) {
        // Request에서 Token 가져오기
        String token = jwtUtil.resolveToken(request);  //bearer 부분 떼고 암호화된 부분만 token에 넣음
        Claims claims;

        // 토큰이 있는 경우에만 게시글 작성
        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                claims = jwtUtil.getUserInfoFromToken(token);  // 토큰에서 사용자 정보 가져오기
            } else {
                throw new IllegalArgumentException("Token Error");
            }

             // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );

            //DB에 저장할 Board 객체 만들기
            Board boardPost = new Board(requestDto, user.getUsername(), user.getId());
            boardRepository.saveAndFlush(boardPost);

            return new BoardResponseDto(boardPost);
        } else {
            return null;
        }
    }


    //**게시글 전체 조회**
    @Transactional
    public BoardListResponseDto getBoards() {
        BoardListResponseDto boardListResponseDto = new BoardListResponseDto();

        List<Board> boards = boardRepository.findAllByOrderByModifiedAtDesc(); //레포지토리에 수정날짜순으로 가져오게 작성했
        for (Board board : boards) {
            boardListResponseDto.addBoard(new BoardResponseDto(board));
        }
        return boardListResponseDto;
    }

    //**선택한 게시글 조회**
    @Transactional
    public BoardResponseDto getBoard(Long id) {
        Board board = checkBoard(id);
        return new BoardResponseDto(board);
    }


    //**게시글 수정**
    @Transactional
    public BoardResponseDto updateBoard(Long id, BoardRequestDto requestDto, HttpServletRequest request) {

        String token = jwtUtil.resolveToken(request); //token에 bearer부분 떼고 담기(토큰 유효 검사위함)
        Claims claims;   //토큰 안에 있는 user 정보 담기 위함

        // 토큰이 있는 경우에만 게시글 수정
        if (token != null) {
            if (jwtUtil.validateToken(token)) {       //토큰 유효한지 검증

                claims = jwtUtil.getUserInfoFromToken(token); // 토큰에서 사용자 정보(body에 있는) 가져오기
            } else {
                throw new IllegalArgumentException("Token Error");
            }

            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회("sub"부분에 있는 username을 가지고옴)
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );

            System.out.println("claims.getSubject() : " + claims.getSubject());

            Board board = checkBoard(id);
            if (board.getUsername().equals(user.getUsername())) {
                board.update(requestDto);
            } return new BoardResponseDto(board);


            }    else {
                    return null;
            }

        }

    //**게시글 삭제**
    @Transactional
    public ResponseDto deleteBoard(Long id, HttpServletRequest request ) {

        String token = jwtUtil.resolveToken(request);
        Claims claims;


        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }


            Board board = checkBoard(id);
            if (board.getUsername().equals(claims.getSubject())) {
                boardRepository.deleteById(id);
            } return new ResponseDto("게시물 삭제 성공", HttpStatus.OK.value());


            } else {
            return null;
            }
    }

    // **선택한 게시글 존재 확인**
    private Board checkBoard(Long id){
        Board board = boardRepository.findById(id).orElseThrow(
                () -> new RuntimeException("게시물이 존재하지 않습니다"));
    return board;
    }
}












