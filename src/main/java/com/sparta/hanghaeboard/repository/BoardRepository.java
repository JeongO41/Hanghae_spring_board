package com.sparta.hanghaeboard.repository;

import com.sparta.hanghaeboard.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findAllByOrderByModifiedAtDesc();

//    Optional<Board> findByIdAndUserId(Long id, Long userId);


} //JpaRepository를 상속 받고, DB와 연결
