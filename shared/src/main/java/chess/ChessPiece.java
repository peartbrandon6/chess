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
    private final PieceType[] promos = {PieceType.ROOK,PieceType.BISHOP,PieceType.KNIGHT,PieceType.QUEEN};

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

    private Collection<ChessMove> moveGetter(ChessBoard board, ChessPosition myPosition, boolean recurse, int row, int col) {
        Set<ChessMove> moves = new HashSet<ChessMove>();
        int curRow, curCol;
        ChessPosition curPos;

        if(row == 0 && col == 0){
            return moves;
        }

        curRow = myPosition.getRow();
        curCol = myPosition.getColumn();

        while (true) {

            curRow += row;
            curCol += col;
            curPos = new ChessPosition(curRow, curCol);

            if(curRow <= 8 && curRow >= 1 && curCol >= 1 && curCol <= 8) {

                if (board.getPiece(curPos) == null) {
                    moves.add(new ChessMove(myPosition, curPos, null));
                } else if (board.getPiece(curPos).getTeamColor().equals(this.pieceColor)) {
                    break;
                } else {
                    moves.add(new ChessMove(myPosition, curPos, null));
                    break;
                }
            }
            else {break;}

            if(!recurse){ break; }
        }

        return moves;
    }

    private Set<ChessMove> pawnHelperUp(ChessBoard board, ChessPosition curPos, ChessPosition myPosition, int curRow, int row, int curCol){

        var moveSet = new HashSet<ChessMove>();
        if (board.getPiece(curPos) == null) {

            // Initial move can move two
            if((this.pieceColor == ChessGame.TeamColor.WHITE && myPosition.getRow() == 2) || (this.pieceColor == ChessGame.TeamColor.BLACK && myPosition.getRow() == 7)){
                ChessPosition extraSpace = new ChessPosition(curRow+row, curCol);
                if(board.getPiece(extraSpace) == null){
                    moveSet.add(new ChessMove(myPosition, extraSpace, null));
                }
            }

            if (curRow == 8 || curRow == 1) {
                for (PieceType piece : promos) {
                    moveSet.add(new ChessMove(myPosition, curPos, piece));
                }
            }
            else moveSet.add(new ChessMove(myPosition, curPos, null));
        }
        return moveSet;
    }

    private Set<ChessMove> pawnHelperDiag(ChessBoard board, ChessPosition curPos, ChessPosition myPosition, int curRow){
        var moveSet = new HashSet<ChessMove>();
        if (board.getPiece(curPos) != null && board.getPiece(curPos).pieceColor != this.pieceColor) {
            if (curRow == 8 || curRow == 1) {
                for (PieceType piece : promos) {
                    moveSet.add(new ChessMove(myPosition, curPos, piece));
                }
            }
            else moveSet.add(new ChessMove(myPosition, curPos, null));
        }
        return moveSet;
    }

    private Set<ChessMove> knightHelper(ChessBoard board, ChessPosition myPosition){
        var moveSet = new HashSet<ChessMove>();

        int[] kniarrx = {-2,-1,1,2};
        int[] kniarry = {-2,-1,1,2};

        for(int row: kniarrx){
            for(int col: kniarry) {
                for (ChessMove move: moveGetter(board, myPosition, false, row, col)){
                    if(Math.abs(row) == Math.abs(col)) continue;
                    moveSet.add(move);
                }
            }
        }

        return moveSet;
    }

    private Set<ChessMove> kingHelper(ChessBoard board, ChessPosition myPosition) {
        var moveSet = new HashSet<ChessMove>();
        int[] arrx = {-1,0,1};
        int[] arry = {-1,0,1};

        for(int row: arrx){
            for(int col: arry) {
                for (ChessMove move: moveGetter(board, myPosition, false, row, col)){
                    if(row == 0 && col ==0) {
                        continue;
                    }
                    moveSet.add(move);
                }
            }
        }

        return moveSet;
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        Set<ChessMove> moveSet = new HashSet<ChessMove>();

        switch (type) {
            case KING:
                return kingHelper(board, myPosition);



            case QUEEN:
                moveSet = new HashSet<ChessMove>();
                int[] queenarrx = {-1,0,1};
                int[] queenarry = {-1,0,1};

                for(int row: queenarrx){
                    for(int col: queenarry) {
                        for (ChessMove move: moveGetter(board, myPosition, true, row, col)){
                            moveSet.add(move);
                        }
                    }
                }
                return moveSet;

            case BISHOP:
                moveSet = new HashSet<ChessMove>();
                int[] bisharrx = {-1,1};
                int[] bisharry = {-1,1};

                for(int row: bisharrx){
                    for(int col: bisharry) {
                        for (ChessMove move: moveGetter(board, myPosition, true, row, col)){
                            moveSet.add(move);
                        }
                    }
                }
                return moveSet;




            case KNIGHT:
                return knightHelper(board, myPosition);

            case ROOK:
                moveSet = new HashSet<ChessMove>();
                int[] rookarrx = {-1,0,1};
                int[] rookarry = {-1,0,1};

                for(int row: rookarrx){
                    for(int col: rookarry) {
                        for (ChessMove move: moveGetter(board, myPosition, true, row, col)){
                            if(row != 0 && col != 0) {
                                continue;
                            }
                            moveSet.add(move);
                        }
                    }
                }
                return moveSet;


            case PAWN:
                moveSet = new HashSet<ChessMove>();
                ChessPosition curPos;
                int curRow, curCol;

                int row = 1;
                if(this.pieceColor == ChessGame.TeamColor.BLACK) {
                    row = -1;
                }

                for(int col = -1; col <= 1; col++){
                    // It needs to reset the current position each iteration
                    curRow = myPosition.getRow() + row;
                    curCol = myPosition.getColumn() + col;
                    curPos = new ChessPosition(curRow, curCol);

                    if(curRow <= 8 && curRow >= 1 && curCol >= 1 && curCol <= 8) {

                        if(col == 0) { //if it's moving straight ahead, checks for promotion
                            moveSet.addAll(pawnHelperUp(board,curPos,myPosition,curRow,row,curCol));
                        }
                        else{ //if it's moving diagonally it has to take a piece, checks for promotion
                            moveSet.addAll(pawnHelperDiag(board,curPos,myPosition,curRow));
                        }
                    }
                }








        }

        return moveSet;
        }



    }

