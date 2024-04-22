package org.example.gamelistv2.entity.listeners;

    

import org.example.gamelistv2.entity.UserGame;

import javax.persistence.PreUpdate;


public class UserGameListener {

    @PreUpdate
    public void onPreUpdate(UserGame userGame) {
        switch (userGame.getStatus()) {
            case PLANNING -> {
                userGame.setFavourite(false);
                userGame.setScore(0);
            }
            case ON_HOLD, DROPPED -> userGame.setFavourite(false);
        }
    }
}
