package dk.brics.tajs;

public class Tuple<X, Y> {
    public final X variableName;
    public final Y lineNumber;
    public Tuple(X variableName, Y lineNumber) {
        this.variableName = variableName;
        this.lineNumber = lineNumber;
    }


    @Override
    public String toString() {
        return "(" + variableName + "," + lineNumber + ")";
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Tuple)){
            return false;
        }

        Tuple<X,Y> other_ = (Tuple<X,Y>) other;

        // this may cause NPE if nulls are valid values for x or y. The logic may be improved to handle nulls properly, if needed.
        return other_.variableName.equals(this.variableName) && other_.lineNumber.equals(this.lineNumber);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((variableName == null) ? 0 : lineNumber.hashCode());
        result = prime * result + ((variableName == null) ? 0 : lineNumber.hashCode());
        return result;
    }
}
