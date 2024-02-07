//ChessMove
package chess;

public class ChessMove  {
    public ChessPiece.PieceType promotionPiece;
    ChessPosition endPosition;
    ChessPosition startPosition;

    public int hashCode() {

        int Starthash = startPosition.getRow()*10000+startPosition.getColumn()*1000+endPosition.getRow()*100+endPosition.getColumn()*10;

        if (promotionPiece==null){
            Starthash -= 1;
        }
        else{
            Starthash += promotionPiece.ordinal();
        }
        return Starthash;
    }

    public static ChessMove convertToHigher(ChessMove oldMove ){
        return new ChessMove( oldMove.getStartPosition() , oldMove.getEndPosition() , oldMove.getPromotionPiece() );
    }

    public ChessMove(ChessPosition startt, ChessPosition endd ){

        startPosition = startt;
        endPosition = endd;
    }

    public ChessMove(ChessPosition startt, ChessPosition endd , ChessPiece.PieceType promotionPiece ){
        this.promotionPiece = promotionPiece;
        startPosition = startt;
        endPosition = endd;
    }

    public ChessPiece.PieceType getPromotionPiece() {

        return promotionPiece;
    }

    public ChessPosition getStartPosition() {
        return startPosition;
    }
    public ChessPosition getEndPosition() {
        return endPosition;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        ChessMove otherMove = (ChessMove) obj;

        return otherMove.promotionPiece == promotionPiece &&
                otherMove.startPosition.getRow() == startPosition.getRow() &&
                otherMove.startPosition.getColumn() == startPosition.getColumn() &&
                otherMove.endPosition.getRow() == endPosition.getRow() &&
                otherMove.endPosition.getColumn() == endPosition.getColumn();

    }

}

//doneee