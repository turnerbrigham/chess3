//ChessPosition
package chess;

public class ChessPosition {
    public int column = 0;
    public int row = 0;

    public ChessPosition(int roww, int coll){

        row = roww;
        column = coll;
    }
    public int hashCode() {
        int result = 17;
        result += 31 * result + row;
        result += 31 * result + column;
        return result;
    }
    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        ChessPosition otherPosition = (ChessPosition) obj;
        return otherPosition.getRow() == getRow() && otherPosition.getColumn() == getColumn();
    }

}

//doneeee