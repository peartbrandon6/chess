package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

import static ui.EscapeSequences.*;

public class DrawBoard {

    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 3;
    private static final int LINE_WIDTH_IN_PADDED_CHARS = 1;
    private static Set<ChessPosition> highlightedSquares = new HashSet<>();
    private static ChessPosition currentSquare;

    public static void drawBoard(ChessGame.TeamColor color, ChessBoard board) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        drawHeaders(out, color);

        drawChessBoard(out, board, color);

        drawHeaders(out, color);

        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    public static void highlightSquares(ChessGame.TeamColor color, ChessBoard board, Set<ChessPosition> highlightedSquares, ChessPosition currentSquare){
        DrawBoard.highlightedSquares = highlightedSquares;
        DrawBoard.currentSquare = currentSquare;
        drawBoard(color, board);
        DrawBoard.currentSquare = null;
        DrawBoard.highlightedSquares = new HashSet<>();
    }

    private static void drawHeaders(PrintStream out, ChessGame.TeamColor color) {

        setBlack(out);

        String[] whiteheader = { "a", "b", "c", "d", "e", "f", "g", "h" };
        String[] blackheader = { "h", "g", "f", "e", "d", "c", "b", "a" };

        String[] headers;

        if (color.equals(ChessGame.TeamColor.WHITE)){
            headers = whiteheader;
        }
        else{
            headers = blackheader;
        }


        for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
            if (boardCol == 0){
                out.print("   ");
            }
            drawHeader(out, headers[boardCol]);

            if (boardCol < BOARD_SIZE_IN_SQUARES - 1) {
                out.print("   ".repeat(LINE_WIDTH_IN_PADDED_CHARS));
            }
        }

        out.println();
    }

    private static void drawHeader(PrintStream out, String headerText) {
        int prefixLength = SQUARE_SIZE_IN_PADDED_CHARS / 2;
        int suffixLength = SQUARE_SIZE_IN_PADDED_CHARS - prefixLength - 1;

        out.print(EMPTY.repeat(prefixLength));
        printHeaderText(out, headerText);
        out.print(EMPTY.repeat(suffixLength));
    }

    private static void printHeaderText(PrintStream out, String pos) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_GREEN);

        out.print(pos);

        setBlack(out);
    }

    private static void drawChessBoard(PrintStream out, ChessBoard board, ChessGame.TeamColor color) {

        for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; ++boardRow) {

            drawRowOfSquares(out, board, boardRow, color);

        }
    }

    private static void setColor(PrintStream out, int row, int col, ChessGame.TeamColor color){
        if(color == ChessGame.TeamColor.WHITE){
            row = 8 - row;
            col = col + 1;
        }
        else{
            row = row + 1;
            col = 8 - col;
        }

        if (new ChessPosition(row, col).equals(DrawBoard.currentSquare)){
            setGreen(out);
        }
        else if (DrawBoard.highlightedSquares != null && DrawBoard.highlightedSquares.contains(new ChessPosition(row, col))){
            setYellow(out);
        }
        else if((col + row) % 2 == 0){
            setWhite(out);
        }
        else{
            setGrey(out);
        }

    }



    private static void drawRowOfSquares(PrintStream out, ChessBoard board, int row, ChessGame.TeamColor color) {

        String[] blackheader = { " 1 ", " 2 ", " 3 ", " 4 ", " 5 ", " 6 ", " 7 ", " 8 " };
        String[] whiteheader = { " 8 ", " 7 ", " 6 ", " 5 ", " 4 ", " 3 ", " 2 ", " 1 " };
        String[] headers;
        if(color.equals(ChessGame.TeamColor.WHITE)){
            headers = whiteheader;
        }
        else{
            headers = blackheader;
        }

        for (int squareRow = 0; squareRow < SQUARE_SIZE_IN_PADDED_CHARS; ++squareRow) {
            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
                setColor(out, row, boardCol, color);

                if (squareRow == SQUARE_SIZE_IN_PADDED_CHARS / 2) {
                    int prefixLength = SQUARE_SIZE_IN_PADDED_CHARS / 2;
                    int suffixLength = SQUARE_SIZE_IN_PADDED_CHARS - prefixLength - 1;

                    ChessPiece piece;
                    if(color.equals(ChessGame.TeamColor.WHITE)){
                        piece = board.getPiece(new ChessPosition(8 - row, boardCol+1));
                    }
                    else{
                        piece = board.getPiece(new ChessPosition(row + 1, 8-boardCol));
                    }


                    if (boardCol == 0){
                        setBlack(out);
                        printHeaderText(out, headers[row]);
                    }

                    String pieceString = EMPTY;

                    if(piece != null && piece.getTeamColor() == ChessGame.TeamColor.WHITE){
                        pieceString = getString(out, piece, SET_TEXT_COLOR_BLUE, WHITE_KING,
                                WHITE_QUEEN, WHITE_BISHOP, WHITE_KNIGHT, WHITE_ROOK, WHITE_PAWN);
                    }

                    if(piece != null && piece.getTeamColor() == ChessGame.TeamColor.BLACK){
                        pieceString = getString(out, piece, EscapeSequences.SET_TEXT_COLOR_RED,
                                EscapeSequences.BLACK_KING, EscapeSequences.BLACK_QUEEN,
                                EscapeSequences.BLACK_BISHOP, EscapeSequences.BLACK_KNIGHT,
                                EscapeSequences.BLACK_ROOK, EscapeSequences.BLACK_PAWN);
                    }


                    setColor(out, row, boardCol, color);
                    out.print(EMPTY.repeat(prefixLength));
                    printPiece(out, pieceString);
                    out.print(EMPTY.repeat(suffixLength));
                    if (boardCol == 7){
                        setBlack(out);
                        printHeaderText(out, headers[row]);
                        setColor(out, row, boardCol, color);
                    }
                }
                else {
                    if (boardCol == 0){
                        setBlack(out);
                        out.print("   ");
                        setColor(out, row, boardCol, color);
                    }
                    out.print(EMPTY.repeat(SQUARE_SIZE_IN_PADDED_CHARS));
                }

                setBlack(out);
            }

            out.println();
        }

    }

    private static String getString(PrintStream out, ChessPiece piece, String setTextColorBlue, String whiteKing, String whiteQueen,
                                    String whiteBishop, String whiteKnight, String whiteRook, String whitePawn) {
        String pieceString;
        out.print(setTextColorBlue);
        pieceString = switch (piece.getPieceType()) {
            case KING -> whiteKing;
            case QUEEN -> whiteQueen;
            case BISHOP -> whiteBishop;
            case KNIGHT -> whiteKnight;
            case ROOK -> whiteRook;
            case PAWN -> whitePawn;
        };
        return pieceString;
    }

    private static void setWhite(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
    }

    private static void setGrey(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
    }

    private static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
    }

    private static void setYellow(PrintStream out) {
        out.print(SET_BG_COLOR_YELLOW);
    }

    private static void setGreen(PrintStream out) {
        out.print(SET_BG_COLOR_GREEN);
    }

    private static void printPiece(PrintStream out, String piece) {
        out.print(piece);
    }
}
