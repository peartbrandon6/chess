package chess;
import java.util.Map;
import java.util.HashMap;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return board.equals(that.board);
    }

    @Override
    public int hashCode() {
        return board.hashCode();
    }

    Map<ChessPosition,ChessPiece> board;

    public ChessBoard() {
        board = new HashMap<>();
        /*
        initializes blank 8x8 chess board
         */
        for(int row = 1; row <= 8; row++) {
            for(int col = 1; col <= 8; col++) {
                board.put(new ChessPosition(row, col), null);
            }
        }
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {

        board.put(position, piece);
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {

        return board.get(position);
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        for(int pawn = 1; pawn <= 8; pawn++){
            addPiece(new ChessPosition(2,pawn),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
            addPiece(new ChessPosition(7,pawn),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        }

        for(int rook : new int[]{1,8}){
            addPiece(new ChessPosition(1,rook),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
            addPiece(new ChessPosition(8,rook),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        }

        for(int knight : new int[]{2,7}){
            addPiece(new ChessPosition(1,knight),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
            addPiece(new ChessPosition(8,knight),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        }

        for(int bishop : new int[]{3,6}){
            addPiece(new ChessPosition(1, bishop),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
            addPiece(new ChessPosition(8, bishop),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        }

        addPiece(new ChessPosition(1,4),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN));
        addPiece(new ChessPosition(8,4),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN));

        addPiece(new ChessPosition(1,5),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));
        addPiece(new ChessPosition(8,5),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));
    }
}
