package org.example.chess;

import org.example.boardGame.Board;
import org.example.boardGame.BoardException;
import org.example.boardGame.Piece;
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

    public boolean[][] possibleMoves(ChessPosition fromPosition){
        Position from = fromPosition.toPosition();
        validateFromPosition(from);
        return board.piece(from).possibleMoves();
    }

    public ChessPiece performChessMove(ChessPosition from, ChessPosition to){
        Position fromPosition = from.toPosition();
        Position toPosition = to.toPosition();

        validateFromPosition(fromPosition);
        validateToPosition(fromPosition, toPosition);

        Piece capturedPiece = makeMove(fromPosition, toPosition);
        return (ChessPiece) capturedPiece;
    }


    private Piece makeMove(Position fromPosition, Position toPosition) {
        Piece p = board.removePiece(fromPosition);
        Piece capturedPiece = board.removePiece(toPosition);
        board.placePiece(p, toPosition);
        return capturedPiece;
    }

    private void validateFromPosition(Position from) {
        if(!board.thereIsAPiece(from)){
            throw new ChessException("Não tem peça na posição de origem");
        }
        if(!board.piece(from).isThereAnyPossibleMove()){
            throw new ChessException("Não existe movimentos possiveis para a peça escolhida");
        }
    }

    private void validateToPosition(Position from, Position toPosition) {
        if(!board.piece(from).possibleMove(toPosition)){
            throw new ChessException("A peça escolhida não pode se mover para a posição de destino");
        }
    }

    private void placeNewPieces(char column, int row, ChessPiece piece){
        board.placePiece(piece, new ChessPosition(column ,row).toPosition());
    }

    private void initialSetup(){
        placeNewPieces('b', 6, new Rook(board, Color.WHITE));
        placeNewPieces('c', 2, new Rook(board, Color.WHITE));
    }

}
