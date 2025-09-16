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

    public Collection<ChessMove> move_getter(ChessBoard board, ChessPosition myPosition, boolean recurse, int row, int col , ChessPiece.PieceType promo) {
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
                    System.out.print(cur_pos);
                    moves.add(new ChessMove(myPosition, cur_pos, promo));
                } else if (board.getPiece(cur_pos).getTeamColor().equals(this.pieceColor)) {
                    break;
                } else {
                    System.out.print(cur_pos);
                    moves.add(new ChessMove(myPosition, cur_pos, promo));
                    break;
                }
            }
            else {break;}

            if(!recurse){ break; }
        }

        return moves;
    }



    public Collection<ChessMove> move_getter(ChessBoard board, ChessPosition myPosition, boolean recurse, int row, int col){
        return move_getter(board,myPosition,recurse,row,col, null);
    }


    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        Set<ChessMove> move_set = new HashSet<ChessMove>();

        switch (type) {
            case KING:
                /*
                for(int row = myPosition.getRow()-1;row <= myPosition.getRow()+1; row++){

                    if(row < 1 || row > 8){
                        continue;
                    }

                    for(int col = myPosition.getColumn()-1; col <= myPosition.getColumn()+1; col++){

                        if(col < 1 || col > 8){
                            continue;
                        }

                        ChessPosition cur_pos = new ChessPosition(row,col);
                        if(board.getPiece(cur_pos) == null) {
                            move_set.add(new ChessMove(myPosition,cur_pos, null));
                        }
                        else if (board.getPiece(cur_pos).getTeamColor() != pieceColor) {
                            move_set.add(new ChessMove(myPosition,cur_pos, null));
                        }

                    }

                }

                 */



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

                /*
                for (ChessMove move: move_getter(board, myPosition, false, 1, 0)){

                    move_set.add(move);
                }
                for (ChessMove move: move_getter(board, myPosition, false, 1, -1)){

                    move_set.add(move);
                }
                for (ChessMove move: move_getter(board, myPosition, false, 1, 1)){

                    move_set.add(move);
                }
                for (ChessMove move: move_getter(board, myPosition, false, -1, 0)){

                    move_set.add(move);
                }
                for (ChessMove move: move_getter(board, myPosition, false, -1, 1)){

                    move_set.add(move);
                }
                for (ChessMove move: move_getter(board, myPosition, false, -1, -1)){

                    move_set.add(move);
                }
                for (ChessMove move: move_getter(board, myPosition, false, 0, -1)){

                    move_set.add(move);
                }
                for (ChessMove move: move_getter(board, myPosition, false, 0, 1)){

                    move_set.add(move);
                }
*/

            case QUEEN:
                ;

            case BISHOP:
                int cur_row, cur_col;
                ChessPosition cur_pos;
/*

                cur_row = myPosition.getRow();
                cur_col = myPosition.getColumn();
                while (cur_row < 8 && cur_col < 8) {
                    cur_pos = new ChessPosition(cur_row + 1, cur_col + 1);
                    if (board.getPiece(cur_pos) == null) {
                        cur_row++;
                        cur_col++;
                        move_set.add(new ChessMove(myPosition, cur_pos, null));
                    } else if (board.getPiece(cur_pos).getTeamColor().equals(pieceColor)) {
                        break;
                    } else {
                        move_set.add(new ChessMove(myPosition, cur_pos, null));
                        break;
                    }
                }


                cur_row = myPosition.getRow();
                cur_col = myPosition.getColumn();
                while (cur_row > 1 && cur_col < 8) {
                    cur_pos = new ChessPosition(cur_row - 1, cur_col + 1);
                    if (board.getPiece(cur_pos) == null) {
                        cur_row--;
                        cur_col++;
                        move_set.add(new ChessMove(myPosition, cur_pos, null));
                    } else if (board.getPiece(cur_pos).getTeamColor().equals(pieceColor)) {
                        break;
                    } else {
                        move_set.add(new ChessMove(myPosition, cur_pos, null));
                        break;
                    }
                }


                cur_row = myPosition.getRow();
                cur_col = myPosition.getColumn();
                while (cur_row < 8 && cur_col > 1) {
                    cur_pos = new ChessPosition(cur_row + 1, cur_col - 1);
                    if (board.getPiece(cur_pos) == null) {
                        cur_row++;
                        cur_col--;
                        move_set.add(new ChessMove(myPosition, cur_pos, null));
                    } else if (board.getPiece(cur_pos).getTeamColor().equals(pieceColor)) {
                        break;
                    } else {
                        move_set.add(new ChessMove(myPosition, cur_pos, null));
                        break;
                    }
                }


                cur_row = myPosition.getRow();
                cur_col = myPosition.getColumn();
                while (cur_row > 1 && cur_col > 1) {
                    cur_pos = new ChessPosition(cur_row - 1, cur_col - 1);
                    if (board.getPiece(cur_pos) == null) {
                        cur_row--;
                        cur_col--;
                        move_set.add(new ChessMove(myPosition, cur_pos, null));
                    } else if (board.getPiece(cur_pos).getTeamColor().equals(pieceColor)) {
                        break;
                    } else {
                        move_set.add(new ChessMove(myPosition, cur_pos, null));
                        break;
                    }
                }
*/




                    case KNIGHT:
                        ;

                    case ROOK:

                        ;

                    case PAWN:
                        ;


                }

                return move_set;
        }



    }

