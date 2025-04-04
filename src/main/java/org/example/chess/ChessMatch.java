package org.example.chess;

import org.example.boardGame.Board;
import org.example.boardGame.Position;
import org.example.chess.pieces.King;
import org.example.chess.pieces.Rook;

public class ChessMatch{
    private Board board;

    public ChessMatch() {
        board = new Board(8, 8);
        initialSetup();
    }

    public ChessPiece[][] getPieces(){
        ChessPiece[][] pieces = new ChessPiece[board.getRows()][board.getColumns()];
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getColumns(); j++) {
                pieces[i][j] = (ChessPiece) board.getPiece(i, j);
            }
        }
        return pieces;
    }

    private void placeNewPieces(char column, int row, ChessPiece piece){
        board.placePiece(piece, new ChessPosition(column ,row).toPosition());
    }

    private void initialSetup(){
        placeNewPieces('b', 6, new Rook(board, Color.WHITE));
        placeNewPieces('c', 2, new Rook(board, Color.WHITE));
    }

}
