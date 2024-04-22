package org.example.gamelistv2.entity;


import org.example.gamelistv2.entity.listeners.UserGameListener;
import org.example.gamelistv2.model.GameStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users_game")
@EntityListeners(UserGameListener.class)
public class UserGame implements BaseEntity<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private int score;
    private boolean favourite;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status_id")
    private GameStatus status;

    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.DETACH,
                    CascadeType.MERGE,
                    CascadeType.PERSIST,
                    CascadeType.REFRESH
            })
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL
    )
    @JoinColumn(name = "game_id")
    private Game game;

    public void setUser(User user) {
        this.user = user;
        if (this.user != null) {
            this.user.getUserGameList().add(this);
        }
    }

    public void setGame(Game game) {
        this.game = game;
        this.game.getUserGameList().add(this);
    }
}
