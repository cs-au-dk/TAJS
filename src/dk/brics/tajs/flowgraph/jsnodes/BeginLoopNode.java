package dk.brics.tajs.flowgraph.jsnodes;

import dk.brics.tajs.flowgraph.SourceLocation;

/**
 * Begin-loop node.
 * <p>
 * Placed just before the loop branch.
 */
public class BeginLoopNode extends Node {

    private final IfNode ifNode;

    /**
     * Constructs a new begin-loop node.
     */
    public BeginLoopNode(IfNode ifNode, SourceLocation sourceLocation) {
        super(sourceLocation);
        this.ifNode = ifNode;
        setArtificial();
    }

    /**
     * Returns the loop branch node.
     */
    public IfNode getIfNode() {
        return ifNode;
    }

    @Override
    public String toString() {
        return "begin-loop[v" + ifNode.getConditionRegister() + "]";
    }

    @Override
    public boolean canThrowExceptions() {
        return false;
    }

    @Override
    public void visitBy(NodeVisitor v) {
        v.visit(this);
    }
}
