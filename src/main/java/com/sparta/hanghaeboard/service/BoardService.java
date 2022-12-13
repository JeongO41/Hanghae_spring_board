package com.sparta.hanghaeboard.service;

import com.sparta.hanghaeboard.dto.*;
import com.sparta.hanghaeboard.entity.Board;
import com.sparta.hanghaeboard.entity.Comment;
import com.sparta.hanghaeboard.entity.User;
import com.sparta.hanghaeboard.entity.UserRoleEnum;
import com.sparta.hanghaeboard.jwt.JwtUtil;
import com.sparta.hanghaeboard.repository.BoardRepository;
import com.sparta.hanghaeboard.repository.CommentRepository;
import com.sparta.hanghaeboard.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    //DB에 저장 하려면 @Entity가 붙어 있는 Board 클래스를 인스턴스로 만들어서 객체의 값을 사용해서 DB에 저장한다 -> Board객체를 만들어야 함
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;


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
//            Board boardPost = new Board(requestDto, user.getUsername(), user.getId());
//            boardRepository.saveAndFlush(boardPost);

            //DB에 저장할 Board 객체 만들기
            Board boardPost = new Board(requestDto, user);
            System.out.println("게시글 작성 DB저장용 객체 boardPost에서 get.User = " + boardPost.getUser());
            System.out.println("게시글 작성 DB저장용 객체 boardPost에서 get.Username = " + boardPost.getUsername());
            System.out.println("게시글 작성 DB저장용 객체 boardPost에서 get.User.getUsername = " + boardPost.getUser().getUsername());
            boardRepository.saveAndFlush(boardPost);

            return new BoardResponseDto(boardPost);
        } else {
            return null;
        }
    }

    //**게시글 전체 조회**
//    @Transactional
//    public BoardListResponseDto getBoards() {
//        BoardListResponseDto boardListResponseDto = new BoardListResponseDto();
//
//        List<Board> boards = boardRepository.findAllByOrderByModifiedAtDesc(); //레포지토리에 수정날짜순으로 가져오게 작성했
//        for (Board board : boards) {
//            boardListResponseDto.addBoard(new BoardResponseDto(board));
//        }
//        return boardListResponseDto;

    //**게시글 전체 조회(w/댓글리스트)
    @Transactional
    public List<BoardResponseDto> getBoards() {

        List<BoardResponseDto> boardListResponseDto = new ArrayList<>();
        List<Board> boardList = boardRepository.findAllByOrderByModifiedAtDesc(); //레포지토리에 수정날짜순으로 가져오게 작성했

        for(int i = 0 ; i < boardList.size() ; i++){

            BoardResponseDto boardResponseDto = new BoardResponseDto(boardList.get(i)); //
            List<Comment> commentList = commentRepository.findAllByBoard_IdOrderByModifiedAtDesc(boardList.get(i).getId());

            for(int j = 0 ; j < commentList.size() ; j++) {

                boardResponseDto.getComments().add(new CommentResponseDto(commentList.get(j)));

            }
            boardListResponseDto.add(boardResponseDto);
        } return boardListResponseDto;
//        List<Board> boardList = boardRepository.findAllByOrderByModifiedAtDesc(); //레포지토리에 수정날짜순으로 가져오게 작성했
//        for (Board board : boardList) {
//            boardListResponseDto.add(new BoardResponseDto(board));
//        }
//        return boardListResponseDto;



//
//        List<Comment> comments = commentRepository.findAllByOrderByModifiedAtDesc();
//        for(Comment comment:comments){
//            boardCommentListResponseDto.addcomment(new CommentResponseDto(comment));
//        }
//        return new BoardCommentListResponseDto boardCommentListResponseDto = new BoardCommentListResponseDto();
    }

    //**선택한 게시글 조회**
//    @Transactional
//    public BoardResponseDto getBoard(Long id) {
//        Board board = checkBoard(id);
//        return new BoardResponseDto(board);
//    }

     //**선택한 게시글 조회(w/댓글리스트)
    @Transactional(readOnly = true)
    public BoardResponseDto getBoard(Long id) {

        Board board = checkBoard(id);
        System.out.println("board = " + board);

        BoardResponseDto boardResponseDto = new BoardResponseDto(board);

        List<Comment> commentList = commentRepository.findAllByBoard_IdOrderByModifiedAtDesc(id);
//        List<String> commentContentList = commentRepository.findAllByBoard_IdOrderByModifiedAtDesc(id).getComment_content;

        for (Comment comment : commentList) {
            boardResponseDto.getComments().add(new CommentResponseDto(comment));
        }
            return boardResponseDto ;

    }

        //**게시글 수정**
        @Transactional
        public BoardResponseDto updateBoard (Long id, BoardRequestDto requestDto, HttpServletRequest request){

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

                UserRoleEnum role = user.getRole();
                System.out.println("role = " + role);

                System.out.println("claims.getSubject() : " + claims.getSubject());

                Board board = checkBoard(id);
                if (board.getUsername().equals(user.getUsername()) || role == UserRoleEnum.ADMIN) {
                    board.update(requestDto);
                }
                return new BoardResponseDto(board);

            } else {
                throw new IllegalArgumentException("게시글 수정 권한이 없습니다.");
            }

        }

        //**게시글 삭제**
        @Transactional
        public ResponseDto deleteBoard (Long id, HttpServletRequest request ){

            String token = jwtUtil.resolveToken(request);
            Claims claims;


            if (token != null) {
                if (jwtUtil.validateToken(token)) {
                    // 토큰에서 사용자 정보 가져오기
                    claims = jwtUtil.getUserInfoFromToken(token);
                } else {
                    throw new IllegalArgumentException("Token Error");
                }

                User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                        () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
                );

                UserRoleEnum role = user.getRole();

                Board board = checkBoard(id);
                if (board.getUsername().equals(claims.getSubject()) || role == UserRoleEnum.ADMIN) {
                    boardRepository.deleteById(id);
                }
                return new ResponseDto("게시물 삭제 성공", HttpStatus.OK.value());

            } else {
                throw new IllegalArgumentException("게시글 삭제 권한이 없습니다.");
            }
        }


        //**코멘트 작성**
        public CommentResponseDto createComment (@PathVariable Long id, @RequestBody CommentRequestDto
        requestDto, HttpServletRequest request){

            String token = jwtUtil.resolveToken(request);
            Claims claims;

            if (token != null) {
                if (jwtUtil.validateToken(token)) {
                    claims = jwtUtil.getUserInfoFromToken(token);
                } else {
                    throw new IllegalArgumentException("유효한 토큰이 아닙니다.");
                }

                User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                        () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
                );
                System.out.println("username = " + user.getUsername());


                Board board = checkBoard(id);
                Comment commentPost = new Comment(board, requestDto, user);
                commentRepository.saveAndFlush(commentPost);

//                return new BoardCommentResponseDto(commentPost, board);

                return new CommentResponseDto(commentPost);
            }

            throw new IllegalArgumentException("토큰이 없습니다");


        }


        //**코멘트 수정**
        @Transactional
        public CommentResponseDto updateComment (Long comment_id, CommentRequestDto requestDto, HttpServletRequest
        request){

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

                UserRoleEnum role = user.getRole();
                System.out.println("role = " + role);

                System.out.println("claims.getSubject() : " + claims.getSubject());


                Comment comment = checkComment(comment_id);
                if (comment.getUser().getUsername().equals(user.getUsername()) || role == UserRoleEnum.ADMIN) {
                    comment.update(requestDto);
                }

//                Board board = checkBoard(comment.getBoard().getId());
                return new CommentResponseDto(comment);

            } else {
                throw new IllegalArgumentException("댓글 수정 권한이 없습니다.");
            }

        }

        //**코멘트 삭제**
        @Transactional
        public ResponseDto deleteComment (Long comment_id, HttpServletRequest request ){

            String token = jwtUtil.resolveToken(request);
            Claims claims;


            if (token != null) {
                if (jwtUtil.validateToken(token)) {
                    // 토큰에서 사용자 정보 가져오기
                    claims = jwtUtil.getUserInfoFromToken(token);
                } else {
                    throw new IllegalArgumentException("Token Error");
                }

                User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                        () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
                );

                UserRoleEnum role = user.getRole();

                Comment comment = checkComment(comment_id);
                if (comment.getUser().getUsername().equals(claims.getSubject()) || role == UserRoleEnum.ADMIN) {
                    commentRepository.deleteById(comment_id);
                }
                return new ResponseDto("댓글 삭제 성공", HttpStatus.OK.value());

            } else {
                throw new IllegalArgumentException("댓글 삭제 권한이 없습니다.");
            }
        }


        // **선택한 게시글 존재 확인&담기**
        private Board checkBoard (Long id){
            Board board = boardRepository.findById(id).orElseThrow(
                    () -> new RuntimeException("게시물이 존재하지 않습니다"));
            return board;
        }

        // **선택한 코멘트 존재 확인&담기**
        private Comment checkComment (Long comment_id){
            Comment comment = commentRepository.findById(comment_id).orElseThrow(
                    () -> new RuntimeException("댓글이 존재하지 않습니다"));
            return comment;
        }


    }












