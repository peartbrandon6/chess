package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class DrawBoard {

    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 3;
    private static final int LINE_WIDTH_IN_PADDED_CHARS = 1;

    public static void drawBoard(ChessGame.TeamColor color, ChessBoard board) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        drawHeaders(out, color);

        drawChessBoard(out, board, color);

        drawHeaders(out, color);

        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
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
        if(color.equals(ChessGame.TeamColor.WHITE)){
            if((col + row) % 2 == 0){
                setWhite(out);
            }
            else{
                setGrey(out);
            }
        }
        else{
            if((col + row) % 2 == 0){
                setGrey(out);
            }
            else{
                setWhite(out);
            }
        }

    }

    private static void drawRowOfSquares(PrintStream out, ChessBoard board, int row, ChessGame.TeamColor color) {

        String[] whiteheader = { " 1 ", " 2 ", " 3 ", " 4 ", " 5 ", " 6 ", " 7 ", " 8 " };
        String[] blackheader = { " 8 ", " 7 ", " 6 ", " 5 ", " 4 ", " 3 ", " 2 ", " 1 " };
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

                    var piece = board.getPiece(new ChessPosition(row+1, boardCol+1));

                    if (boardCol == 0){
                        setBlack(out);
                        printHeaderText(out, headers[row]);
                    }

                    String pieceString = EMPTY;

                    if(piece != null && piece.getTeamColor() == ChessGame.TeamColor.WHITE){
                        out.print(SET_TEXT_COLOR_BLUE);
                        pieceString = switch (piece.getPieceType()) {
                            case KING -> WHITE_KING;
                            case QUEEN -> WHITE_QUEEN;
                            case BISHOP -> WHITE_BISHOP;
                            case KNIGHT -> WHITE_KNIGHT;
                            case ROOK -> WHITE_ROOK;
                            case PAWN -> WHITE_PAWN;
                        };
                    }

                    if(piece != null && piece.getTeamColor() == ChessGame.TeamColor.BLACK){
                        out.print(SET_TEXT_COLOR_RED);
                        pieceString = switch (piece.getPieceType()) {
                            case KING -> BLACK_KING;
                            case QUEEN -> BLACK_QUEEN;
                            case BISHOP -> BLACK_BISHOP;
                            case KNIGHT -> BLACK_KNIGHT;
                            case ROOK -> BLACK_ROOK;
                            case PAWN -> BLACK_PAWN;
                        };
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

    private static void setWhite(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
    }

    private static void setGrey(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
    }

    private static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
    }

    private static void printPiece(PrintStream out, String piece) {
        out.print(piece);
    }
}
