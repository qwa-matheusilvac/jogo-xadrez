package org.example.chess;

import lombok.Getter;
import org.example.boardGame.Board;
import org.example.boardGame.Piece;

@Getter
public class ChessPiece extends Piece {
    private Color color;

    public ChessPiece( Board board, Color color) {
        super(board);
        this.color = color;
    }
}
