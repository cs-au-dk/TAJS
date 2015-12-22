package dk.brics.tajs.flowgraph.jsnodes;

import dk.brics.tajs.flowgraph.SourceLocation;

/**
 * End-loop node.
 * <p>
 * Placed at every exit of the loop (for example, immediately after the false branch).
 */
public class EndLoopNode extends Node {

    private final BeginLoopNode beginNode;

    /**
     * Constructs a new end-loop node.
     */
    public EndLoopNode(BeginLoopNode beginNode, SourceLocation location) {
        super(location);
        this.beginNode = beginNode;
        setArtificial();
    }

    @Override
    public String toString() {
        return "end-loop[v" + beginNode.getIfNode().getConditionRegister() + "]";
    }

    @Override
    public boolean canThrowExceptions() {
        return false;
    }

    /**
     * Returns the associated begin-loop node.
     */
    public BeginLoopNode getBeginNode() {
        return beginNode;
    }

    @Override
    public void visitBy(NodeVisitor v) {
        v.visit(this);
    }
}
