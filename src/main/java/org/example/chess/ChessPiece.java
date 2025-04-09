package org.example.chess;

import lombok.Getter;
import org.example.boardGame.Board;
import org.example.boardGame.Piece;
import org.example.boardGame.Position;

@Getter
public abstract class ChessPiece extends Piece {
    private Color color;
    private int moveCount;

    public ChessPiece( Board board, Color color) {
        super(board);
        this.color = color;
    }

    public ChessPosition getChessPosition() {
        return ChessPosition.fromPosition(position);
    }

    public void increaseMoveCount() {
        moveCount++;
    }

    public void decreaseMoveCount() {
        moveCount--;
    }

    public int getMoveCount() {
        return moveCount;
    }

    protected boolean isThereOpponentPiece(Position position) {
        ChessPiece p = (ChessPiece) getBoard().piece(position);
        return p != null && p.getColor() != color;
    }

}
