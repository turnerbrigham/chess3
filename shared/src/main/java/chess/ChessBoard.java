
// ChessBoard
//notes:
//System.out.print("text");
//System.out.println("text");
//public static void main(String[] args){
package chess;

import java.util.Arrays;

public class ChessBoard {
    ChessPiece[][] thisChessBoard = new ChessPiece[9][9];


    public ChessBoard getCopy(){
        ChessBoard copy = new ChessBoard();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if ( thisChessBoard[i][j]  == null ){
                    copy.thisChessBoard[i][j] = null;
                }
                else{
                    copy.thisChessBoard[i][j] = thisChessBoard[i][j].getCopy();
                }
            }
        }
        return copy;
    }


    public void deletePiece(ChessPosition position) {
        thisChessBoard[position.getRow()][position.getColumn()] = null;
    }
    public void addPiece(ChessPosition position, ChessPiece piece) {

        thisChessBoard[position.getRow()][position.getColumn()] = new ChessPiece(piece.getTeamColor() , piece.getPieceType() );

    }

    public ChessPiece getPiece(ChessPosition position) {
        return thisChessBoard[position.getRow()][position.getColumn()];
    }
    public ChessPiece getPiece2(int row, int column) {
        return thisChessBoard[row][column];
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ChessBoard other = (ChessBoard) obj;

        return Arrays.deepEquals(this.thisChessBoard , other.thisChessBoard );
    }
    public void resetBoard() {


        for (int row = 0; row <= 8; row++) {
            for (int col = 0; col <= 8; col++) {
                thisChessBoard[row][col] = null;
            }
        }

        for (int row : new int[]{2, 7} ){
            for (int col = 1; col <= 8; col++ ){

                thisChessBoard[row][col] = new ChessPiece( null , ChessPiece.PieceType.PAWN );
            }
        }
        for (int row : new int[]{1, 8} ){
            for (int col = 1; col <= 8; col ++ ){
                thisChessBoard[row][col] = new ChessPiece( null , null );
            }

            for (int col : new int[]{1, 8} ){
                thisChessBoard[row][col].thisPieceType = ChessPiece.PieceType.ROOK;
            }

            for (int col : new int[]{2, 7} ){
                thisChessBoard[row][col].thisPieceType = ChessPiece.PieceType.KNIGHT;
            }

            for (int col : new int[]{3, 6} ){
                thisChessBoard[row][col].thisPieceType = ChessPiece.PieceType.BISHOP;
            }
            thisChessBoard[row][4].thisPieceType = ChessPiece.PieceType.QUEEN;
            thisChessBoard[row][5].thisPieceType = ChessPiece.PieceType.KING;
        }

        for (int row : new int[]{1,2}) {
            for (int col = 1; col <= 8; col++) {
                if (thisChessBoard[row][col] != null){
                    thisChessBoard[row][col].thisTeamColor = ChessGame.TeamColor.WHITE;
                }
            }
        }
        for (int row : new int[]{7,8}) {
            for (int col = 1; col <= 8; col++) {
                if (thisChessBoard[row][col] != null){
                    thisChessBoard[row][col].thisTeamColor = ChessGame.TeamColor.BLACK;
                }
            }
        }

    }

}

//donee