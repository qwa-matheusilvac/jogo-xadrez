package org.example.boardGame;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Piece {
    protected Position position;

    private Board board;

    protected Board getBoard() {
        return board;
    }

    public Piece(Board board) {
        this.board = board;
    }
}
