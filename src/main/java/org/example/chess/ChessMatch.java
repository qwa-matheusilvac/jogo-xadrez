package org.example.chess;

import lombok.Getter;
import org.example.boardGame.Board;
import org.example.boardGame.Piece;
import org.example.boardGame.Position;
import org.example.chess.pieces.King;
import org.example.chess.pieces.Rook;

public class ChessMatch{
    @Getter
    private int turn;
    @Getter
    private Color currentPlayer;
    private Board board;

    public ChessMatch() {
        board = new Board(8, 8);
        turn = 1;
        currentPlayer = Color.WHITE;
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
        nextTurn();
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
        if(currentPlayer != ((ChessPiece)board.piece(from)).getColor()){
            throw new ChessException("A peça escolhida não é sua.");
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

    private  void nextTurn(){
        turn++;
        currentPlayer = currentPlayer == Color.WHITE ? Color.BLACK : Color.WHITE;
    }
    private void placeNewPieces(char column, int row, ChessPiece piece){
        board.placePiece(piece, new ChessPosition(column ,row).toPosition());
    }

    private void initialSetup(){
        placeNewPieces('c', 2, new Rook(board, Color.WHITE));
        placeNewPieces('c', 1, new Rook(board, Color.WHITE));
        placeNewPieces('e', 2, new Rook(board, Color.WHITE));
        placeNewPieces('e', 1, new Rook(board, Color.WHITE));
        placeNewPieces('d', 2, new Rook(board, Color.WHITE));
        placeNewPieces('d', 1, new King(board, Color.WHITE));

        placeNewPieces('c', 8, new Rook(board, Color.BLACK));
        placeNewPieces('c', 7, new Rook(board, Color.BLACK));
        placeNewPieces('e', 7, new Rook(board, Color.BLACK));
        placeNewPieces('e', 8, new Rook(board, Color.BLACK));
        placeNewPieces('d', 7, new Rook(board, Color.BLACK));
        placeNewPieces('d', 8, new King(board, Color.BLACK));
    }

}
