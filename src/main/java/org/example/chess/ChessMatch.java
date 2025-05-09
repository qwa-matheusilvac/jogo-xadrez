package org.example.chess;

import lombok.Getter;
import org.example.boardGame.Board;
import org.example.boardGame.Piece;
import org.example.boardGame.Position;
import org.example.chess.pieces.*;

import java.util.ArrayList;
import java.util.List;

public class ChessMatch{
    @Getter
    private int turn;
    @Getter
    private Color currentPlayer;
    private Board board;

    private boolean check;
    private boolean checkMate;

    @Getter
    private ChessPiece enPassantVulnerable;

    @Getter
    private ChessPiece promoted;

    private List<ChessPiece> piecesOnTheBoard;
    private List<ChessPiece> capturedPieces = new ArrayList<>();

    public ChessMatch() {
        board = new Board(8, 8);
        turn = 1;
        currentPlayer = Color.WHITE;
        piecesOnTheBoard = new ArrayList<>();
        check = false;
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

        if(testCheck(currentPlayer)){
            undoMove(toPosition, fromPosition, capturedPiece);
            throw new ChessException("Você não pode se colocar em check");
        }

        ChessPiece movedPiece = (ChessPiece)board.piece(toPosition);

        promoted = null;
        //special move promotion

        if(movedPiece instanceof Pawn){
            if (movedPiece.getColor() == Color.WHITE && toPosition.getRow() == 0 || movedPiece.getColor() == Color.BLACK && toPosition.getRow() == 7) {
                promoted = (ChessPiece) board.piece(toPosition);
                promoted = replacePromotedPiece("Q");
            }
        }
        check = testCheck(opponent(currentPlayer)) ? true : false;

        if (testCheckMate(opponent(currentPlayer))) {
            checkMate = true;
        } else {
            nextTurn();
        }

        //special move en passant
        if(movedPiece instanceof Pawn && (toPosition.getRow() == fromPosition.getRow() - 2 || toPosition.getRow() == fromPosition.getRow() + 2)){
            enPassantVulnerable = movedPiece;
        } else {
            enPassantVulnerable = null;
        }

        return (ChessPiece) capturedPiece;
    }

    public ChessPiece replacePromotedPiece(String string) {
        if(promoted == null){
            throw new IllegalStateException("Essa peça não esta promovida");
        }
        if(!string.equals("B") && !string.equals("N") && !string.equals("R") && !string.equals("Q")){
            return promoted;
        }
        Position pos = promoted.getChessPosition().toPosition();
        Piece p = board.removePiece(pos);
        piecesOnTheBoard.remove(p);

        ChessPiece newPiece = newPiece(string, promoted.getColor());
        board.placePiece(newPiece, pos);
        piecesOnTheBoard.add(newPiece);
        promoted = null;
        return newPiece;
    }

    private ChessPiece newPiece (String string, Color color) {
        if(string.equals("B")) return new Bishop(board, color);
        if(string.equals("N")) return new Knight(board, color);
        if(string.equals("Q")) return new Queen(board, color);
        return new Rook(board, color);
    }

    public boolean isCheck() {
        return check;
    }

    public boolean isCheckMate() {
        return checkMate;
    }
    private Piece makeMove(Position source, Position target) {
        ChessPiece p = (ChessPiece)board.removePiece(source);
        p.increaseMoveCount();
        Piece capturedPiece = board.removePiece(target);
        board.placePiece(p, target);

        if (capturedPiece != null) {
            piecesOnTheBoard.remove(capturedPiece);
            capturedPieces.add((ChessPiece) capturedPiece);
        }

        // #specialmove castling kingside rook
        if (p instanceof King && target.getColumn() == source.getColumn() + 2) {
            Position sourceT = new Position(source.getRow(), source.getColumn() + 3);
            Position targetT = new Position(source.getRow(), source.getColumn() + 1);
            ChessPiece rook = (ChessPiece)board.removePiece(sourceT);
            board.placePiece(rook, targetT);
            rook.increaseMoveCount();
        }

        // #specialmove castling queenside rook
        if (p instanceof King && target.getColumn() == source.getColumn() - 2) {
            Position sourceT = new Position(source.getRow(), source.getColumn() - 4);
            Position targetT = new Position(source.getRow(), source.getColumn() - 1);
            ChessPiece rook = (ChessPiece)board.removePiece(sourceT);
            board.placePiece(rook, targetT);
            rook.increaseMoveCount();
        }

        // #specialmove en passant
        if (p instanceof Pawn) {
            if (source.getColumn() != target.getColumn() && capturedPiece == null) {
                Position pawnPosition;
                if (p.getColor() == Color.WHITE) {
                    pawnPosition = new Position(target.getRow() + 1, target.getColumn());
                }
                else {
                    pawnPosition = new Position(target.getRow() - 1, target.getColumn());
                }
                capturedPiece = board.removePiece(pawnPosition);
                capturedPieces.add((ChessPiece) capturedPiece);
                piecesOnTheBoard.remove(capturedPiece);
            }
        }

        return capturedPiece;
    }

    private void undoMove(Position source, Position target, Piece capturedPiece) {
        ChessPiece p = (ChessPiece)board.removePiece(target);
        p.decreaseMoveCount();
        board.placePiece(p, source);

        if (capturedPiece != null) {
            board.placePiece(capturedPiece, target);
            capturedPieces.remove(capturedPiece);
            piecesOnTheBoard.add((ChessPiece) capturedPiece);
        }

        // #specialmove castling kingside rook
        if (p instanceof King && target.getColumn() == source.getColumn() + 2) {
            Position sourceT = new Position(source.getRow(), source.getColumn() + 3);
            Position targetT = new Position(source.getRow(), source.getColumn() + 1);
            ChessPiece rook = (ChessPiece)board.removePiece(targetT);
            board.placePiece(rook, sourceT);
            rook.decreaseMoveCount();
        }

        // #specialmove castling queenside rook
        if (p instanceof King && target.getColumn() == source.getColumn() - 2) {
            Position sourceT = new Position(source.getRow(), source.getColumn() - 4);
            Position targetT = new Position(source.getRow(), source.getColumn() - 1);
            ChessPiece rook = (ChessPiece)board.removePiece(targetT);
            board.placePiece(rook, sourceT);
            rook.decreaseMoveCount();
        }

        //special move en passant
        if (p instanceof Pawn) {
            if (source.getColumn() != target.getColumn() && capturedPiece == enPassantVulnerable) {
                ChessPiece pawn = (ChessPiece)board.removePiece(target);
                Position pawnPosition;
                if (p.getColor() == Color.WHITE) {
                    pawnPosition = new Position(3, target.getColumn());
                }
                else {
                    pawnPosition = new Position(4, target.getColumn());
                }
                board.placePiece(pawn, pawnPosition);
            }
        }
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

    private Color opponent(Color color){
        return color == Color.WHITE ? Color.BLACK : Color.WHITE;
    }

    private ChessPiece king(Color color){
        List<ChessPiece> pieces = piecesOnTheBoard.stream().filter(p -> p.getColor() == color).toList();
        for (ChessPiece piece : pieces) {
            if (piece instanceof King){
                return piece;
            }
        }
        throw new IllegalStateException("Não existe o Rei da cor: " + color.toString() + " bno tabuleiro");
    }

    private boolean testCheck(Color color) {
        Position kingPosition = king(color).getChessPosition().toPosition();
        List<ChessPiece> opponentPieces = piecesOnTheBoard.stream().filter(p -> p.getColor() == opponent(color)).toList();
        for (ChessPiece piece : opponentPieces) {
            boolean[][] mat = piece.possibleMoves();
            if (mat[kingPosition.getRow()][kingPosition.getColumn()]) {
                return true;
            }
        }
        return false;
    }

    private boolean testCheckMate(Color color) {
        if (!testCheck(color)) {
            return false;
        }
        List<ChessPiece> list = piecesOnTheBoard.stream().filter(p -> p.getColor() == color).toList();
        for (ChessPiece piece : list) {
            boolean[][] mat = piece.possibleMoves();
            for (int i = 0; i < board.getRows(); i++) {
                for (int j = 0; j < board.getColumns(); j++) {
                    if (mat[i][j]) {
                        Position from = piece.getChessPosition().toPosition();
                        Position to = new Position(i, j);
                        Piece capturedPiece = makeMove(from, to);
                        boolean testCheck = testCheck(color);
                        undoMove(to, from, capturedPiece);
                        if (!testCheck) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private  void nextTurn(){
        turn++;
        currentPlayer = currentPlayer == Color.WHITE ? Color.BLACK : Color.WHITE;
    }
    private void placeNewPieces(char column, int row, ChessPiece piece){
        board.placePiece(piece, new ChessPosition(column ,row).toPosition());
        piecesOnTheBoard.add(piece);
    }

    private void initialSetup(){
        placeNewPieces('a', 1, new Rook(board, Color.WHITE));
        placeNewPieces('b', 1, new Knight(board, Color.WHITE));
        placeNewPieces('c', 1, new Bishop(board, Color.WHITE));
        placeNewPieces('d', 1, new Queen(board, Color.WHITE));
        placeNewPieces('e', 1, new King(board, Color.WHITE, this));
        placeNewPieces('f', 1, new Bishop(board, Color.WHITE));
        placeNewPieces('g', 1, new Knight(board, Color.WHITE));
        placeNewPieces('h', 1, new Rook(board, Color.WHITE));
        placeNewPieces('a', 2, new Pawn(board, Color.WHITE, this));
        placeNewPieces('b', 2, new Pawn(board, Color.WHITE, this));
        placeNewPieces('c', 2, new Pawn(board, Color.WHITE, this));
        placeNewPieces('d', 2, new Pawn(board, Color.WHITE, this));
        placeNewPieces('e', 2, new Pawn(board, Color.WHITE, this));
        placeNewPieces('f', 2, new Pawn(board, Color.WHITE, this));
        placeNewPieces('g', 2, new Pawn(board, Color.WHITE, this));
        placeNewPieces('h', 2, new Pawn(board, Color.WHITE, this));

        placeNewPieces('a', 8, new Rook(board, Color.BLACK));
        placeNewPieces('b', 8, new Knight(board, Color.BLACK));
        placeNewPieces('c', 8, new Bishop(board, Color.BLACK));
        placeNewPieces('d', 8, new Queen(board, Color.BLACK));
        placeNewPieces('e', 8, new King(board, Color.BLACK, this));
        placeNewPieces('f', 8, new Bishop(board, Color.BLACK));
        placeNewPieces('g', 8, new Knight(board, Color.BLACK));
        placeNewPieces('h', 8, new Rook(board, Color.BLACK));
        placeNewPieces('a', 7, new Pawn(board, Color.BLACK, this));
        placeNewPieces('b', 7, new Pawn(board, Color.BLACK, this));
        placeNewPieces('c', 7, new Pawn(board, Color.BLACK, this));
        placeNewPieces('d', 7, new Pawn(board, Color.BLACK, this));
        placeNewPieces('e', 7, new Pawn(board, Color.BLACK, this));
        placeNewPieces('f', 7, new Pawn(board, Color.BLACK, this));
        placeNewPieces('g', 7, new Pawn(board, Color.BLACK, this));
        placeNewPieces('h', 7, new Pawn(board, Color.BLACK, this));
    }

}
