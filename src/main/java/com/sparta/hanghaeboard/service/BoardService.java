package com.sparta.hanghaeboard.service;

import com.sparta.hanghaeboard.dto.BoardListResponseDto;
import com.sparta.hanghaeboard.dto.BoardRequestDto;
import com.sparta.hanghaeboard.dto.BoardResponseDto;
import com.sparta.hanghaeboard.dto.ResponseDto;
import com.sparta.hanghaeboard.entity.Board;
import com.sparta.hanghaeboard.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor

public class BoardService {
    private final BoardRepository boardRepository; //BoardRepository와 연결

    //DB에 저장 하려면 @Entity가 붙어 있는 Board 클래스를 인스턴스로 만들어서 객체의 값을 사용해서 DB에 저장한다 -> Board객체를 만들어야 함

    //*게시글 작성*
    @Transactional
    public BoardResponseDto createBoard(BoardRequestDto requestDto) {//클라이언트에서 넘어온 값들이 Dto에 담겨서 Board객체의 필드값들로 저장됨 , entity
        Board board = new Board(requestDto);
        boardRepository.save(board);  // 생성한 Board객체(board)를 Repository에 넘겨줘서 DB에 저장하도록(by JPA메서드,->자동쿼리생성)
        return new BoardResponseDto(board);
    }

    //*게시글 전체 조회*
    @Transactional
    public BoardListResponseDto getBoards() {
        BoardListResponseDto boardListResponseDto = new BoardListResponseDto();

        List<Board> boards = boardRepository.findAllByOrderByModifiedAtDesc(); //레포지토리에 수정날짜순으로 가져오게 작성했
        for (Board board : boards) {
            BoardResponseDto board1 = new BoardResponseDto(board);
            boardListResponseDto.addBoard(board1);
        }
        return boardListResponseDto;
    }

    //*선택한 게시글 조회*
    @Transactional
    public BoardResponseDto getBoard(Long id) {
        Board board = checkBoard(boardRepository, id);
        return new BoardResponseDto(board);
    }


    //*게시글 수정*
    @Transactional
    public BoardResponseDto updateBoard(Long id, BoardRequestDto requestDto) {
        Board board = checkBoard(boardRepository, id);
        if (board.getPassword().equals(requestDto.getPassword())) {
            board.update(requestDto);
        } else {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");
        }
        return new BoardResponseDto(board);
    }

    //*게시글 삭제*
    @Transactional
    public ResponseDto deleteBoard(Long id, BoardRequestDto requestDto) {
        Board board = checkBoard(boardRepository, id);
        if (board.getPassword().equals(requestDto.getPassword())) {
            boardRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");
        }
        return new ResponseDto("게시물 삭제 성공", HttpStatus.OK.value());
    }

    // *선택한 게시글 존재 확인*
    private Board checkBoard(BoardRepository boardRepository, Long id){
        Board board = boardRepository.findById(id).orElseThrow(
                () -> new RuntimeException("게시물이 존재하지 않습니다"));
    return board;
    }

}












