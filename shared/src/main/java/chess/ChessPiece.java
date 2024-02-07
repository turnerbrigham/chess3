//ChessPiece
package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ChessPiece {
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }
    public PieceType thisPieceType = PieceType.ROOK;
    public ChessGame.TeamColor thisTeamColor = ChessGame.TeamColor.WHITE;

    public ChessPiece getCopy(){

        return new ChessPiece( thisTeamColor , thisPieceType );
    }
    public PieceType getPieceType() {
        return thisPieceType;
    }
    public ChessGame.TeamColor getTeamColor() {
        return thisTeamColor;
    }


    public ChessPiece(ChessGame.TeamColor pieceColor, PieceType type) {

        thisPieceType = type;
        thisTeamColor = pieceColor;
    }


     public boolean myPieceHere(ChessBoard board , ChessPosition testPos ){
        if (testPos.row > 8 || testPos.row < 1 ||testPos.column<1 || testPos.column > 8){
            return false;
        }
        if ( board.getPiece( testPos ) != null ){
            if ( board.getPiece( testPos ).getTeamColor() == thisTeamColor ){
                return true;
            }
        }
        return false;
    }
    boolean otherPieceHere(ChessBoard board , ChessPosition testPos ){
        if (testPos.row > 8 || testPos.row < 1 ||testPos.column<1 || testPos.column >8){
            return false;
        }
        if ( board.getPiece( testPos ) != null ){
            if ( board.getPiece( testPos ).getTeamColor() != thisTeamColor ){
                return true;
            }
        }
        return false;
    }
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition ) {
        ChessPosition startPosition = new ChessPosition( myPosition.getRow() , myPosition.getColumn() );

        Set<ChessMove> moves;
        moves = new HashSet<>();

        if (thisPieceType == PieceType.PAWN){
            PieceType[] allTypes = {PieceType.ROOK,PieceType.KNIGHT,PieceType.BISHOP, PieceType.QUEEN };

            int directionn;
            if ( thisTeamColor == ChessGame.TeamColor.WHITE ){
                directionn = 1;
            }
            else{
                directionn = -1;
            }
            if ( (startPosition.row==2 && directionn == 1)  ||
                    (startPosition.row==7 && directionn == -1) ){
                ChessPosition newPosition_sm = new ChessPosition( startPosition.row+directionn , startPosition.column );
                ChessPosition newPosition = new ChessPosition( startPosition.row+ 2*directionn , startPosition.column );

                if ( !(  myPieceHere( board , newPosition ) || otherPieceHere( board , newPosition ) ||
                        myPieceHere( board , newPosition_sm ) || otherPieceHere( board , newPosition_sm )  )){
                    moves.add(new ChessMove( startPosition , newPosition ) );
                }
            }

            ChessPosition newPosition = new ChessPosition( startPosition.row+directionn , startPosition.column );
            if (  !(myPieceHere( board , newPosition ) || otherPieceHere( board , newPosition )   )  ){

                if (newPosition.row == 1 || newPosition.row == 8){
                    for (PieceType samplePiece : allTypes ){
                        ChessMove potentialMove = new ChessMove( startPosition , newPosition );

                        potentialMove.promotionPiece = samplePiece;
                        moves.add( potentialMove );
                    }
                }
                else {
                    moves.add(new ChessMove( startPosition , newPosition ) );
                }
            }

            int newRow = startPosition.row + directionn;
            if ( newRow >0 && newRow <= 8 ){
                for (int i : new int[]{-1,1} ){
                    int newColumn = startPosition.column + i;
                    if ( newColumn >0 && newColumn <= 8 ){
                        ChessPosition endPosition = new ChessPosition( newRow , newColumn );
                        if (board.getPiece( endPosition ) != null ){
                            if (endPosition.column == 1 || endPosition.column == 8){
                                for (PieceType samplePiece : allTypes ){
                                    ChessMove potentialMove = new ChessMove( startPosition , endPosition );
                                    potentialMove.promotionPiece = samplePiece;
                                    moves.add( potentialMove );
                                }
                            }
                            else {

                                moves.add(new ChessMove( startPosition , endPosition ) );
                            }
                        }
                    }
                }
            }
        }
        if (thisPieceType == PieceType.ROOK || thisPieceType == PieceType.QUEEN ){
            for (int Qdirection : new int[]{-1,1} ){
                for (int i = 1 ; i < 8 ; i++){
                    ChessPosition newPosition = new ChessPosition( startPosition.row+i*Qdirection , startPosition.column );
                    if (myPieceHere( board , newPosition )){
                        break;
                    }
                    moves.add(new ChessMove( startPosition , newPosition ));
                    if (otherPieceHere( board , newPosition )){
                        break;
                    }
                }
            }
            for (int Qdirection : new int[]{-1,1} ){
                for (int i = 1 ; i < 8 ; i++){
                    ChessPosition newPosition = new ChessPosition( startPosition.row , startPosition.column+i*Qdirection );
                    if (myPieceHere( board , newPosition )){
                        break;
                    }
                    moves.add(new ChessMove( startPosition , newPosition ));
                    if (otherPieceHere( board , newPosition )){
                        break;
                    }
                }
            }
        }
        if (thisPieceType == PieceType.BISHOP || thisPieceType == PieceType.QUEEN ){

            for (int Qdirection : new int[]{-1,1} ){
                for (int i = 1 ; i < 8 ; i++){
                    ChessPosition newPosition = new ChessPosition( startPosition.row+i*Qdirection , startPosition.column+i*Qdirection );
                    if (myPieceHere( board , newPosition )){
                        break;
                    }
                    moves.add(new ChessMove( startPosition , newPosition ));
                    if (otherPieceHere( board , newPosition )){
                        break;
                    }
                }
            }
            for (int Qdirection : new int[]{-1,1} ){
                for (int i = 1 ; i < 8 ; i++){
                    ChessPosition newPosition = new ChessPosition( startPosition.row+i*Qdirection , startPosition.column-i*Qdirection );
                    if (myPieceHere( board , newPosition )){
                        break;
                    }
                    moves.add(new ChessMove( startPosition , newPosition ));
                    if (otherPieceHere( board , newPosition )){
                        break;
                    }
                }
            }
        }
        if (thisPieceType == PieceType.KING ){
            moves.add(new ChessMove( startPosition ,
                    new ChessPosition( startPosition.row + 1 , startPosition.column ) ));
            moves.add(new ChessMove( startPosition ,
                    new ChessPosition( startPosition.row - 1 , startPosition.column ) ));
            moves.add(new ChessMove( startPosition ,
                    new ChessPosition( startPosition.row , startPosition.column + 1 ) ));
            moves.add(new ChessMove( startPosition ,
                    new ChessPosition( startPosition.row , startPosition.column - 1 ) ));

            moves.add(new ChessMove( startPosition ,
                    new ChessPosition( startPosition.row + 1 , startPosition.column +1) ));
            moves.add(new ChessMove( startPosition ,
                    new ChessPosition( startPosition.row - 1 , startPosition.column -1) ));
            moves.add(new ChessMove( startPosition ,
                    new ChessPosition( startPosition.row  +1, startPosition.column - 1 ) ));
            moves.add(new ChessMove( startPosition ,
                    new ChessPosition( startPosition.row -1 , startPosition.column + 1 ) ));

        }
        if (thisPieceType == PieceType.KNIGHT){
            for (  int Qdirection : new int[]{-2,2} ){
                for (  int side : new int[]{-1,1} ){
                    moves.add(new ChessMove( startPosition ,
                            new ChessPosition( startPosition.row+side , startPosition.column + Qdirection ) ));
                    moves.add(new ChessMove( startPosition ,
                            new ChessPosition( startPosition.row+Qdirection , startPosition.column + side ) ));
                }
            }
        }

        Iterator<ChessMove> iterator = moves.iterator();
        while (iterator.hasNext()) {
            ChessMove presentMove = iterator.next();
            ChessPosition endPosition = new ChessPosition( presentMove.getEndPosition().getRow() , presentMove.getEndPosition().getColumn() );
            if ( myPieceHere( board , endPosition ) || endPosition.row > 8 || endPosition.row <= 0 ||
                    endPosition.column > 8 || endPosition.column <= 0) {
                iterator.remove();
            }
        }

        return moves;
    }

    public static ChessPiece convertToHigher(ChessPiece oldPiece){

        return new ChessPiece( oldPiece.getTeamColor() , oldPiece.getPieceType() );
    }



    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        ChessPiece otherPiece = (ChessPiece) obj;
        return otherPiece.thisPieceType == thisPieceType && otherPiece.thisTeamColor == thisTeamColor;
    }

}

//doneeeee