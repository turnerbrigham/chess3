// ChessGame
package chess;

import java.util.*;

public class ChessGame {

    public ChessBoard myBoard = new ChessBoard();
    public ChessPosition enPassantOpportunity = null;

    public TeamColor myTurn = TeamColor.WHITE;

    public boolean castleLeftAllowed_black = true;
    public boolean castleRightAllowed_black = true;
    public boolean castleRightAllowed_white = true;
    public boolean castleLeftAllowed_white = true;

    public enum TeamColor {
        WHITE,
        BLACK
    }


    private String blackUsername;
    int gameID;
    private String whiteUsername;

    public void setBoard(ChessBoard board) {
        ChessPiece[][] newChessBoard = new ChessPiece[9][9];
        for (int i = 1; i <= 8; i++){
            for (int j = 1; j <= 8; j++){
                ChessPiece oldPiece = board.getPiece( new ChessPosition(i,j) );
                if (oldPiece != null) {

                    ChessPiece newPiece = new ChessPiece(oldPiece.getTeamColor() , oldPiece.getPieceType());

                    newChessBoard[i][j] = newPiece;
                }
            }
        }
        myBoard.thisChessBoard =  newChessBoard;

        castleLeftAllowed_white = true;
        castleRightAllowed_white = true;
        castleLeftAllowed_black = true;
        castleRightAllowed_black = true;
    }

    public ChessBoard getBoard() {
        return myBoard;
    }

    public String getBlackUsername() {
        return blackUsername;
    }
    public String getWhiteUsername() {
        return whiteUsername;
    }

    public int getGameID( ){
        return gameID;
    }

    public TeamColor getTeamTurn() {
        return myTurn;
    }

    public void setTeamTurn(TeamColor team) {
        myTurn = team;
    }
    public void setGameID( int newGameID ){
        gameID = newGameID;
    }
    public void setWhiteUsername(String whiteUsername) {
        this.whiteUsername = whiteUsername;
    }
    public void setBlackUsername(String blackUsername) {
        this.blackUsername = blackUsername;
    }
    public ChessGame getCopy(){
        ChessGame newGame = new ChessGame();
        newGame.myTurn = myTurn;
        newGame.myBoard = myBoard.getCopy();

        newGame.castleLeftAllowed_white = castleLeftAllowed_white;
        newGame.castleRightAllowed_white = castleRightAllowed_white;
        newGame.castleLeftAllowed_black = castleLeftAllowed_black;
        newGame.castleRightAllowed_black = castleRightAllowed_black;

        return newGame;
    }

    public void forceMove(ChessMove movee) {

        ChessMove move2 = ChessMove.convertToHigher(movee);
        ChessPiece pieceCopy = ChessPiece.convertToHigher(myBoard.getPiece(move2.getStartPosition()));
        if (movee.getPromotionPiece() != null) {
            pieceCopy.thisPieceType = move2.promotionPiece;
        }


        if (pieceCopy.thisPieceType == ChessPiece.PieceType.PAWN) {
            if (move2.getStartPosition().getColumn() != move2.getEndPosition().getColumn()) {
                if (myBoard.getPiece(move2.getEndPosition()) == null) {

                    myBoard.thisChessBoard[move2.getStartPosition().getRow()][
                            move2.getEndPosition().getColumn()] = null;
                }
            }
        }

        int rowMovement = move2.getEndPosition().getRow() - move2.getStartPosition().getRow();

        if (pieceCopy.thisPieceType == ChessPiece.PieceType.PAWN &&
                (rowMovement == 2 || rowMovement == -2)) {
            enPassantOpportunity = new ChessPosition(move2.getEndPosition().getRow(), move2.getEndPosition().getColumn());
        } else {
            enPassantOpportunity = null;
        }

        myBoard.addPiece(move2.getEndPosition(), pieceCopy);
        myBoard.deletePiece(move2.getStartPosition());

        int sRow = move2.getStartPosition().getRow();
        int sCol = move2.getStartPosition().getColumn();
        int eRow = move2.getEndPosition().getRow();
        int eCol = move2.getEndPosition().getColumn();

        if ( pieceCopy.thisPieceType == ChessPiece.PieceType.KING ) {
            if ( pieceCopy.thisTeamColor == TeamColor.WHITE ) {
                if (sRow == 1 && sCol == 5 &&
                        eRow == 1 && eCol == 3 && castleLeftAllowed_white) {
                    forceMove(new ChessMove(new ChessPosition(1, 1), new ChessPosition(1, 4) , movee.getPromotionPiece() ));
                    swapTurn();
                }
                if (sRow == 1 && sCol == 5 &&
                        eRow == 1 && eCol == 7 && castleRightAllowed_white) {
                    forceMove(new ChessMove(new ChessPosition(1, 8), new ChessPosition(1, 6) , movee.getPromotionPiece() ));
                    swapTurn();
                }
            }
            if ( pieceCopy.thisTeamColor == TeamColor.BLACK ) {
                if (sRow == 8 && sCol == 5 &&
                        eRow == 8 && eCol == 3 && castleLeftAllowed_black) {
                    forceMove(new ChessMove(new ChessPosition(8, 1), new ChessPosition(8, 4) , movee.getPromotionPiece() ));
                    swapTurn();
                }
                if (sRow == 8 && sCol == 5 &&
                        eRow == 8 && eCol == 7 && castleRightAllowed_black) {
                    forceMove(new ChessMove(new ChessPosition(8, 8), new ChessPosition(8, 6) , movee.getPromotionPiece() ));
                    swapTurn();
                }
            }
        }


        if ( sRow == 1 && sCol == 1 ){
            castleLeftAllowed_white = false;
        }
        if ( sRow == 1 && sCol == 8 ){
            castleRightAllowed_white = false;
        }
        if ( sRow == 8 && sCol == 1 ){
            castleLeftAllowed_black = false;
        }
        if ( sRow == 8 && sCol == 8 ){
            castleRightAllowed_black = false;
        }
        if ( sRow == 1 && sCol == 5 ){
            castleRightAllowed_white = false;
            castleLeftAllowed_white = false;
        }
        if ( sRow == 8 && sCol == 5 ){
            castleRightAllowed_black = false;
            castleLeftAllowed_black = false;
        }

        swapTurn();
    }

    boolean isMeerlyClearOfBlocks( ChessPosition thisPosition  ){
        if ( myBoard.thisChessBoard[ thisPosition.row ][ thisPosition.column ]!=null ){
            return false;
        }
        return true;
    }

    boolean isClearOfBlocksOrChecks( ChessPosition thisPosition  ){
        if ( myBoard.thisChessBoard[ thisPosition.row ][ thisPosition.column ]!=null ){
            return false;
        }
        return ( !willThereBeACheck( new ChessMove( new ChessPosition(thisPosition.row,5) , thisPosition ) ) );
    }
    boolean isClearOfChecks( ChessPosition thisPosition  ){
        return ( !willThereBeACheck( new ChessMove( new ChessPosition(thisPosition.row,5) , thisPosition ) ) );
    }

    public boolean isInCheck(TeamColor teamColor) {
        int kingRow = 0;
        int kingCol = 0;
        outerLoop:
        for (int i = 1; i <= 8; i++){
            for (int j = 1; j <= 8; j++){
                ChessPiece presentPiece = myBoard.getPiece2(i,j);
                if ( presentPiece != null ){
                    if (presentPiece.getPieceType() == ChessPiece.PieceType.KING &&
                            presentPiece.getTeamColor() == teamColor  ){
                        kingRow = i;
                        kingCol = j;
                        break outerLoop;
                    }
                }
            }
        }
        TeamColor otherTeamColor;
        if (teamColor == TeamColor.BLACK){
            otherTeamColor = TeamColor.WHITE;
        } else {
            otherTeamColor = TeamColor.BLACK;
        }

        Collection<ChessMove> allMoves = literallyAllPossibleMoves( otherTeamColor );
        for ( ChessMove possibleMove : allMoves ) {
            if ( possibleMove.getEndPosition().getRow() == kingRow &&
                    possibleMove.getEndPosition().getColumn() == kingCol ){
                return true;
            }
        }
        return false;
    }

    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor)){
            return false;
        }

        Collection<ChessMove> allPossibleMoves = literallyAllPossibleMoves( teamColor );
        for ( ChessMove possibleMove : allPossibleMoves ) {
            if ( !willThereBeACheck( possibleMove ) ){
                return false;
            }
        }
        return true;

    }

    public boolean isInStalemate(TeamColor teamColor) {
        if ( isInCheck(teamColor) ){
            return false;
        }

        Collection<ChessMove> allPossibleMoves = literallyAllPossibleMoves( teamColor );
        for ( ChessMove possibleMove : allPossibleMoves ) {
            if ( !willThereBeACheck( possibleMove ) ){
                return false;
            }
        }
        return true;

    }

    boolean willThereBeACheck( ChessMove movee ){
        ChessGame stagedGame = getCopy();
        TeamColor presentColor = stagedGame.getBoard().getPiece( movee.getStartPosition() ).getTeamColor();
        stagedGame.forceMove( movee );
        return stagedGame.isInCheck( presentColor );
    }
    boolean sectionClearOfStuff( ChessPosition thisPosition ){
        int directionn;
        if ( thisPosition.column == 1 ){
            directionn = -1;
        }
        else{
            directionn = 1;
        }
        for ( int col = 5+directionn; col != (5+ 3*directionn); col += directionn ){
            if ( !isClearOfBlocksOrChecks( new ChessPosition( thisPosition.row , col )  ) ){
                return false;
            }
        }

        if ( !isMeerlyClearOfBlocks( new ChessPosition( thisPosition.row, thisPosition.column-directionn ) )){
            return false;
        }

        if ( !isClearOfChecks( new ChessPosition( thisPosition.row , 5 )  ) ){
            return false;
        }
        if ( thisPosition.row == 1 ){
            ChessPiece rookPiece = myBoard.getPiece( new ChessPosition(1,thisPosition.column ) );
            if ( rookPiece == null  ){
                return false;
            }
            if (  rookPiece.getPieceType() != ChessPiece.PieceType.ROOK || rookPiece.getTeamColor() != TeamColor.WHITE   ){
                return false;
            }
            ChessPiece kingPiece = myBoard.getPiece( new ChessPosition(1,5) );
            if ( kingPiece == null ){
                return false;
            }
            if (  kingPiece.getPieceType() != ChessPiece.PieceType.KING || kingPiece.getTeamColor() != TeamColor.WHITE   ){
                return false;
            }
        }
        if ( thisPosition.row == 8 ){
            ChessPiece rookPiece = myBoard.getPiece( new ChessPosition(8,thisPosition.column ) );
            if ( rookPiece == null  ){
                return false;
            }
            if (  rookPiece.getPieceType() != ChessPiece.PieceType.ROOK || rookPiece.getTeamColor() != TeamColor.BLACK   ){
                return false;
            }
            ChessPiece kingPiece = myBoard.getPiece( new ChessPosition(8,5) );
            if ( kingPiece == null ){
                return false;
            }
            if (  kingPiece.getPieceType() != ChessPiece.PieceType.KING || kingPiece.getTeamColor() != TeamColor.BLACK   ){
                return false;
            }
        }

        return true;
    }
    public Collection<ChessMove> pinOnCastling(ChessPosition startPosition ){
        Set<ChessMove> emoves;
        emoves = new HashSet<>();
        ChessPiece potentialPiece = myBoard.getPiece( startPosition );
        if (potentialPiece == null){
            return  emoves;
        }
        Collection<ChessMove> movesFinal = potentialPiece.pieceMoves(  myBoard , startPosition );

        if ( castleLeftAllowed_white && startPosition.getRow()==1 &&
                                        startPosition.getColumn()==5 ){
            if (sectionClearOfStuff( new ChessPosition( 1 , 1) ) ){
                movesFinal.add( new ChessMove(  startPosition , new ChessPosition(1 , 3 ) ) );
            }
        }
        if ( castleRightAllowed_white && startPosition.getRow()==1 &&
                startPosition.getColumn()==5 ){
            if (sectionClearOfStuff( new ChessPosition( 1 , 8) ) ){
                movesFinal.add( new ChessMove(  startPosition , new ChessPosition(1 , 7 ) ) );
            }
        }
        if ( castleLeftAllowed_black && startPosition.getRow()==8 &&
                startPosition.getColumn()==5 ){

            if (sectionClearOfStuff( new ChessPosition( 8 , 1) ) ){

                movesFinal.add( new ChessMove(  startPosition , new ChessPosition(8 , 3 ) ) );
            }
        }

        if ( castleRightAllowed_black && startPosition.getRow()==8 &&
                startPosition.getColumn()==5 ){
            if (sectionClearOfStuff( new ChessPosition( 8 , 8) ) ){
                movesFinal.add( new ChessMove(  startPosition , new ChessPosition(8 , 7 ) ) );
            }
        }

        if (enPassantOpportunity != null){
            int colDiff = enPassantOpportunity.getColumn() - startPosition.getColumn();
            if ( startPosition.getRow() == enPassantOpportunity.getRow() &&
                                            (colDiff==1 || colDiff==-1) ){

                if (myBoard.getPiece( startPosition ).getTeamColor() == TeamColor.WHITE){
                    movesFinal.add( new ChessMove(  startPosition ,
                       new ChessPosition( startPosition.getRow()+1 , enPassantOpportunity.getColumn() ) ) );
                }
                else {
                    movesFinal.add( new ChessMove(  startPosition ,
                       new ChessPosition(startPosition.getRow()-1 , enPassantOpportunity.getColumn()  ) ) );
                }
            }
        }
        return movesFinal;
    }

    public Collection<ChessMove> validMoves(ChessPosition startPosition) {

        Set<ChessMove> emoves;
        emoves = new HashSet<>();

        Collection<ChessMove> movesFinal = pinOnCastling( startPosition );
        Iterator<ChessMove> iterator = movesFinal.iterator();
        while (iterator.hasNext()) {
            ChessMove presentMove = iterator.next();
            if (willThereBeACheck(presentMove)) {
                iterator.remove();
            }
        }
        return movesFinal;
    }
    public boolean isValidMove(ChessMove movee){

        Collection<ChessMove> myValidMoves = pinOnCastling( movee.getStartPosition() );

        if ( myValidMoves == null ){
            return false;
        }
        boolean isInIt = false;
        int endPositionRow    = movee.getEndPosition().getRow();
        int endPositionColumn = movee.getEndPosition().getColumn();
        for ( ChessMove possibleMove : myValidMoves ) {
            ChessPosition positionFromIter = possibleMove.getEndPosition();
            if (   positionFromIter.getRow() == endPositionRow &&
                    positionFromIter.getColumn() == endPositionColumn){
                isInIt = true;
            }
        }
        if ( !isInIt ){
            return false;
        }
        if ( myBoard.getPiece(movee.getStartPosition()).getTeamColor() != myTurn ){
            return false;
        }
        return true;
    }

    void swapTurn(){
        if (myTurn == TeamColor.WHITE){
            myTurn = TeamColor.BLACK;
        } else {
            myTurn = TeamColor.WHITE;
        }
    }

    public Collection<ChessMove> literallyAllPossibleMoves(TeamColor teamColor) {
        Collection<ChessMove> moves = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                ChessPosition pos = new ChessPosition( i, j);
                ChessPiece pieceHere = myBoard.getPiece( pos );
                if ( pieceHere!= null ){
                    if (pieceHere.getTeamColor() == teamColor){

                        Collection<ChessMove> possibleMovesHere = pinOnCastling( pos );

                        if (possibleMovesHere != null){
                            moves.addAll( possibleMovesHere );
                        }
                    }
                }
            }
        }
        return moves;
    }

    public void makeMove(ChessMove movee) throws chess.InvalidMoveException {
        if (  !isValidMove( ChessMove.convertToHigher(movee) )  ){
            throw new chess.InvalidMoveException();
        }
        if (  willThereBeACheck( movee ) ){
            throw new chess.InvalidMoveException();
        }
        forceMove( movee );
    }

}

//doneee