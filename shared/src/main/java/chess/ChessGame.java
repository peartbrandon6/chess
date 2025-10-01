package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    ChessBoard board;
    TeamColor teamTurn;

    public ChessGame() {
        board = new ChessBoard();
        board.resetBoard();
        teamTurn = TeamColor.WHITE;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ChessGame chessGame = (ChessGame) o;
        return board.equals(chessGame.board) && teamTurn == chessGame.teamTurn;
    }

    @Override
    public int hashCode() {
        int result = board.hashCode();
        result = 31 * result + teamTurn.hashCode();
        return result;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

        /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);


        if(piece == null) return null; // return null if no piece is there

        Set<ChessMove> move_set = new HashSet<>();

        for(ChessMove move: piece.pieceMoves(board,startPosition)){
            ChessPiece temp = board.getPiece(move.getEndPosition());
            board.addPiece(move.getEndPosition(),piece);
            board.addPiece(startPosition,null);
            if(!isInCheck(piece.getTeamColor())){
                move_set.add(move);
            }
            board.addPiece(move.getEndPosition(),temp);
            board.addPiece(startPosition,piece);
        }

        return move_set;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        for(ChessMove m: validMoves(move.getStartPosition())){
            if(m.equals(move)){
                board.addPiece(move.getEndPosition(),board.getPiece(move.getStartPosition()));
                board.addPiece(move.getStartPosition(),null);
                return;
            }
        }
        throw new InvalidMoveException("Given move is not valid");
    }


    // Helper function to get team's King position
    private ChessPosition getKing(TeamColor team){
        for(int row = 1; row <= 8; row++) {
            for(int col = 1; col <= 8; col++) {
                ChessPosition pos = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(pos);
                if (piece != null && piece.getTeamColor() == team && piece.getPieceType() == ChessPiece.PieceType.KING) return pos;
            }
        }
        throw new RuntimeException("No King found on board");
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition my_king = getKing(teamColor);

        //recurses through all board pieces
        for(int row = 1; row <= 8; row++) {
            for(int col = 1; col <= 8; col++) {
                ChessPosition pos = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(pos);

                if(piece != null && piece.getTeamColor() != teamColor){
                    for(ChessMove move: piece.pieceMoves(board,pos)){
                        if(move.getEndPosition().equals(my_king)) return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean hasTeamMoves(TeamColor teamColor){
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition pos = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(pos);
                if (piece != null && piece.getTeamColor() == teamColor) {
                    if (!validMoves(pos).isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }


    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if(isInCheck(teamColor)) {
            //checks if there are no valid moves to make for the team
            return hasTeamMoves(teamColor);
        } else return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if(!isInCheck(teamColor)) {
            //checks if there are no valid moves to make for the team
            return hasTeamMoves(teamColor);
        } else return false;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
