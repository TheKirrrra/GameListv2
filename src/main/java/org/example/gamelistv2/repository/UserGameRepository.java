package org.example.gamelistv2.repository;


import org.example.gamelistv2.entity.UserGame;
import org.example.gamelistv2.model.GameStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserGameRepository extends JpaRepository<UserGame, Integer> {

    Page<UserGame> findAllByStatusAndUser_Username(GameStatus status, String username, Pageable pageable);

    Page<UserGame> findAllByFavouriteAndUser_Username(boolean favourite, String username, Pageable pageable);

    Optional<UserGame> findByGame_IdAndUser_Username(Integer malId, String username);

    List<UserGame> findAllByGame_Id(int malId);
}
