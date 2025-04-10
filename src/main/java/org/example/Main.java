package org.example;
import org.example.chess.ChessException;
import org.example.chess.ChessMatch;
import org.example.chess.ChessPiece;
import org.example.chess.ChessPosition;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ChessMatch chessMatch = new ChessMatch();
        Scanner scanner = new Scanner(System.in);
        List<ChessPiece> capturedPieces = new ArrayList<>();

        while(!chessMatch.isCheckMate()) {
           try {
               UI.clearScreen();
               UI.printMatch(chessMatch, capturedPieces);
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
               if(capturedPiece != null) {
                   capturedPieces.add(capturedPiece);
               }

               if(chessMatch.getPromoted() != null){
                   System.out.println("Promote to: (B/N/R/Q)");
                   String string = scanner.nextLine().toUpperCase();
                   while (!string.equals("B") && !string.equals("N") && !string.equals("R") && !string.equals("Q")){
                       System.out.println("invalid value,  use: (B/N/R/Q)");
                       string = scanner.nextLine().toUpperCase();
                   }
                   chessMatch.replacePromotedPiece(string);
               }

           } catch (ChessException | InputMismatchException e) {
               System.out.println(e.getMessage());
           }
        }
        UI.clearScreen();
        UI.printMatch(chessMatch, capturedPieces);
        System.out.println("Checkmate!");
    }
}