package dk.brics.tajs.analysis.nativeobjects.concrete;

public interface ConcreteValueVisitor<T> {

    T visit(ConcreteNumber v);

    T visit(ConcreteString v);

    T visit(ConcreteArray v);

    T visit(ConcreteUndefined v);

    T visit(ConcreteRegularExpression v);

    T visit(ConcreteNull v);

    T visit(ConcreteBoolean v);
}
