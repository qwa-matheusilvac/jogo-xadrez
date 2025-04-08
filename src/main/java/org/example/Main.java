package org.example;
import org.example.chess.ChessException;
import org.example.chess.ChessMatch;
import org.example.chess.ChessPiece;
import org.example.chess.ChessPosition;

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

               boolean[][] possibleMoves = chessMatch.possibleMoves(from);
               UI.clearScreen();
               UI.printBoard(chessMatch.getPieces(), possibleMoves);

               System.out.println();
               System.out.println("To: ");
               ChessPosition to = UI.readChessPosition(scanner);

               ChessPiece capturedPiece = chessMatch.performChessMove(from, to);
           } catch (ChessException | InputMismatchException e) {
               System.out.println(e.getMessage());
           }
        }
    }
}