package org.example.boardGame;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Position {

    private int row;

    private int column;

    @Override
    public String toString() {
        return row + ", " + column;
    }

    public void setValues(int row, int column){
        this.row = row;
        this.column = column;
    }

}
