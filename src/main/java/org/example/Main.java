package org.example;

import org.example.boardGame.Board;
import org.example.chess.ChessException;
import org.example.chess.ChessMatch;
import org.example.chess.ChessPiece;
import org.example.chess.ChessPosition;

import java.sql.SQLOutput;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ChessMatch chessMatch = new ChessMatch();
        Scanner scanner = new Scanner(System.in);

        while(true) {
           try {
               UI.clearScreen();
               UI.printBoard(chessMatch.getPieces());
               System.out.println();
               System.out.println("From: ");
               ChessPosition from = UI.readChessPosition(scanner);

               System.out.println();
               System.out.println("To: ");
               ChessPosition to = UI.readChessPosition(scanner);

               ChessPiece capturedPiece = chessMatch.performChessMove(from, to);
           } catch (ChessException e) {
               System.out.println(e.getMessage());
           } catch (InputMismatchException e){
               System.out.println(e.getMessage());
           }
        }
    }
}