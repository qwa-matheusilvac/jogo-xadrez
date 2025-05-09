package org.example.boardGame;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public abstract class Piece {
    protected Position position;

    private Board board;

    protected Board getBoard() {
        return board;
    }

    public Piece(Board board) {
        this.board = board;
    }

    public abstract boolean[][] possibleMoves();

    public boolean possibleMove(Position position) {
        return possibleMoves()[position.getRow()][position.getColumn()];
    }

    public boolean isThereAnyPossibleMove(){
        boolean[][] mat = possibleMoves();
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat[i].length; j++) {
                if(mat[i][j]){
                    return true;
                }
            }
        }
        return false;
    }

}
