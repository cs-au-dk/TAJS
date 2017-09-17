package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.FlowGraph;
import dk.brics.tajs.flowgraph.jsnodes.AssumeNode;
import dk.brics.tajs.flowgraph.jsnodes.ConstantNode;
import dk.brics.tajs.options.Options;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.List;

import static dk.brics.tajs.test.Misc.build;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SuppressWarnings("static-method")
public class TestNoFlowNodeInsertion {

    @Before
    public void before() {
        Main.reset();
        Options.get().enableTest();
    }

    @Test
    public void canBuild() {
        assertNotNull(build("var x;"));
        assertNotNull(build("var x;", "if(x){x++;}"));
    }

    @Test
    public void parses() {
        build("\"dk.brics.tajs.directives.unreachable\";");
    }

    @Test
    public void recognizes() {
        FlowGraph fg = build("\"dk.brics.tajs.directives.unreachable\";");
        assertEquals(1, countNoFlowNodes(fg));
    }

    @Test
    public void ignoresOtherDirectives() {
        FlowGraph fg = build("'some-other-directive';");
        assertEquals(0, countNoFlowNodes(fg));
    }

    @Test
    public void recognizesMultiple() {
        FlowGraph fg = build("\"dk.brics.tajs.directives.unreachable\";", "\"dk.brics.tajs.directives.unreachable\";");
        assertEquals(2, countNoFlowNodes(fg));
    }

    @Test
    public void doesNotConfuseWithStringConstants() {
        FlowGraph fg = build("var x = \"dk.brics.tajs.directives.unreachable\";");
        assertEquals(0, countNoFlowNodes(fg));
    }

    @Test
    public void worksInConditionals() {
        FlowGraph fg = build("if(x){\"dk.brics.tajs.directives.unreachable\";}");
        assertEquals(1, countNoFlowNodes(fg));
    }

    @Test
    public void worksInConditionalsAlternative() {
        FlowGraph fg = build("if(x){}else{\"dk.brics.tajs.directives.unreachable\";}");
        assertEquals(1, countNoFlowNodes(fg));
    }

    @Test
    public void worksAfterConditionals() {
        FlowGraph fg = build("if(x){}\"dk.brics.tajs.directives.unreachable\";");
        assertEquals(1, countNoFlowNodes(fg));
    }

    @Test
    public void replacesDirective() {
        FlowGraph fg = build("\"dk.brics.tajs.directives.unreachable\"; var x = 'foo';");
        int stringCount = countNodes(fg, new NodePredicate() {
            @Override
            public boolean apply(AbstractNode node) {
                return node instanceof ConstantNode && ((ConstantNode) node).getType() == ConstantNode.Type.STRING;
            }
        });
        assertEquals(1, stringCount);
    }

    private int countNoFlowNodes(FlowGraph fg) {
        return countNodes(fg, new NodePredicate() {

            @Override
            public boolean apply(AbstractNode node) {
                return node instanceof AssumeNode && ((AssumeNode) node).getKind() == AssumeNode.Kind.UNREACHABLE;
            }
        });
    }

    private int countNodes(FlowGraph fg, NodePredicate pred) {
        int actual = 0;
        Collection<BasicBlock> blocks = fg.getMain().getBlocks();
        for (BasicBlock bb : blocks) {
            List<AbstractNode> nodes = bb.getNodes();
            for (AbstractNode node : nodes) {
                if (pred.apply(node)) {
                    actual++;
                }
            }
        }
        return actual;
    }

    private interface NodePredicate {

        boolean apply(AbstractNode node);
    }
}
