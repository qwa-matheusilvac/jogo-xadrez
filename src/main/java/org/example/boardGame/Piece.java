package org.example.boardGame;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class Piece {
    protected Position position;

    private Board board;

    protected Board getBoard() {
        return board;
    }
}
