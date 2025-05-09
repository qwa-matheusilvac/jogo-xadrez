package org.example.boardGame;


import lombok.Getter;
import lombok.Setter;

public class Board {

    @Getter
    private int rows;

    @Getter
    private int columns;

    private  Piece[][] pieces;

    public Board(int rows, int columns) {
        if (rows <= 1 || columns <= 1) {
            throw new BoardException("Rows and columns must be greater than 1");
        } else {
            this.rows = rows;
            this.columns = columns;
            pieces = new Piece[rows][columns];
        }
    }

    public Piece getPiece(int row, int column) {
        if(!positionExists(row, column)){
            throw new BoardException("Position does not exist");
        }
        return pieces[row][column];
    }

    public Piece piece(Position position){
        if(!positionExists(position)){
            throw new BoardException("Position does not exist");
        }
        return pieces[position.getRow()][position.getColumn()];
    }

    public void placePiece(Piece piece, Position position) {
        if(thereIsAPiece(position)){
            throw new BoardException("Position already occupied");
        }
        pieces[position.getRow()][position.getColumn()] = piece;
        piece.position = position;
    }

    private boolean positionExists(int row, int column){
        return row >= 0 && row < rows && column >= 0 && column < columns;
    }

    public boolean positionExists(Position position) {
        return positionExists(position.getRow(), position.getColumn());
    }

    public boolean thereIsAPiece(Position position) {
        if(!positionExists(position)){
            throw new BoardException("Position does not exist");
        }
       return piece(position) != null;
    }

    public Piece removePiece(Position position) {
        if (!positionExists(position)) {
            throw new BoardException("Position does not exist");
        }
        if (piece(position) == null) {
            return null;
        }
        Piece aux = piece(position);
        aux.position = null;
        pieces[position.getRow()][position.getColumn()] = null;
        return aux;
    }
}
