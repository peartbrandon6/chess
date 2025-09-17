package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.getTeamColor() && type == that.getPieceType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    ChessGame.TeamColor pieceColor;
    ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */

    public Collection<ChessMove> move_getter(ChessBoard board, ChessPosition myPosition, boolean recurse, int row, int col) {
        Set<ChessMove> moves = new HashSet<ChessMove>();
        int cur_row, cur_col;
        ChessPosition cur_pos;

        if(row == 0 && col == 0){
            return moves;
        }

        cur_row = myPosition.getRow();
        cur_col = myPosition.getColumn();

        while (true) {

            cur_row += row;
            cur_col += col;
            cur_pos = new ChessPosition(cur_row, cur_col);

            if(cur_row <= 8 && cur_row >= 1 && cur_col >= 1 && cur_col <= 8) {

                if (board.getPiece(cur_pos) == null) {
                    moves.add(new ChessMove(myPosition, cur_pos, null));
                } else if (board.getPiece(cur_pos).getTeamColor().equals(this.pieceColor)) {
                    break;
                } else {
                    moves.add(new ChessMove(myPosition, cur_pos, null));
                    break;
                }
            }
            else {break;}

            if(!recurse){ break; }
        }

        return moves;
    }


    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        Set<ChessMove> move_set = new HashSet<ChessMove>();

        switch (type) {
            case KING:

                move_set = new HashSet<ChessMove>();
                int[] arrx = {-1,0,1};
                int[] arry = {-1,0,1};

                for(int row: arrx){
                    for(int col: arry) {
                        for (ChessMove move: move_getter(board, myPosition, false, row, col)){
                            if(row == 0 && col ==0) continue;
                            move_set.add(move);
                        }
                    }
                }

                return move_set;



            case QUEEN:
                move_set = new HashSet<ChessMove>();
                int[] queenarrx = {-1,0,1};
                int[] queenarry = {-1,0,1};

                for(int row: queenarrx){
                    for(int col: queenarry) {
                        for (ChessMove move: move_getter(board, myPosition, true, row, col)){
                            move_set.add(move);
                        }
                    }
                }
                return move_set;

            case BISHOP:
                move_set = new HashSet<ChessMove>();
                int[] bisharrx = {-1,1};
                int[] bisharry = {-1,1};

                for(int row: bisharrx){
                    for(int col: bisharry) {
                        for (ChessMove move: move_getter(board, myPosition, true, row, col)){
                            move_set.add(move);
                        }
                    }
                }
                return move_set;




            case KNIGHT:
                    move_set = new HashSet<ChessMove>();

                    int[] kniarrx = {-2,-1,1,2};
                    int[] kniarry = {-2,-1,1,2};

                    for(int row: kniarrx){
                        for(int col: kniarry) {
                            for (ChessMove move: move_getter(board, myPosition, false, row, col)){
                                if(Math.abs(row) == Math.abs(col)) continue;
                                move_set.add(move);
                            }
                        }
                    }

                    return move_set;

            case ROOK:
                move_set = new HashSet<ChessMove>();
                int[] rookarrx = {-1,0,1};
                int[] rookarry = {-1,0,1};

                for(int row: rookarrx){
                    for(int col: rookarry) {
                        for (ChessMove move: move_getter(board, myPosition, true, row, col)){
                            if(row != 0 && col != 0) continue;
                            move_set.add(move);
                        }
                    }
                }
                return move_set;


            case PAWN:
                move_set = new HashSet<ChessMove>();
                ChessPosition cur_pos;
                PieceType[] promos = {PieceType.ROOK,PieceType.BISHOP,PieceType.KNIGHT,PieceType.QUEEN};
                int cur_row, cur_col;

                int row = 1;
                if(this.pieceColor == ChessGame.TeamColor.BLACK) row = -1;

                for(int col = -1; col <= 1; col++){
                    // It needs to reset the current position each iteration
                    cur_row = myPosition.getRow() + row;
                    cur_col = myPosition.getColumn() + col;
                    cur_pos = new ChessPosition(cur_row, cur_col);

                    if(cur_row <= 8 && cur_row >= 1 && cur_col >= 1 && cur_col <= 8) {

                        if(col == 0) { //if it's moving straight ahead, checks for promotion
                            if (board.getPiece(cur_pos) == null) {

                                // Initial move can move two
                                if((this.pieceColor == ChessGame.TeamColor.WHITE && myPosition.getRow() == 2) || (this.pieceColor == ChessGame.TeamColor.BLACK && myPosition.getRow() == 7)){
                                    ChessPosition extra_space = new ChessPosition(cur_row+row, cur_col);
                                    if(board.getPiece(extra_space) == null){
                                        move_set.add(new ChessMove(myPosition, extra_space, null));
                                    }
                                }

                                if (cur_row == 8 || cur_row == 1) {
                                    for (PieceType piece : promos) {
                                        move_set.add(new ChessMove(myPosition, cur_pos, piece));
                                    }
                                }
                                else move_set.add(new ChessMove(myPosition, cur_pos, null));
                            }
                        }
                        else{ //if it's moving diagonally it has to take a piece, checks for promotion
                            if (board.getPiece(cur_pos) != null && board.getPiece(cur_pos).pieceColor != this.pieceColor) {
                                if (cur_row == 8 || cur_row == 1) {
                                    for (PieceType piece : promos) {
                                        move_set.add(new ChessMove(myPosition, cur_pos, piece));
                                    }
                                }
                                else move_set.add(new ChessMove(myPosition, cur_pos, null));
                            }
                        }
                    }
                }








        }

        return move_set;
        }



    }

