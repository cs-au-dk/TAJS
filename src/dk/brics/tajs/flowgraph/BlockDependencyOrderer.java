package dk.brics.tajs.flowgraph;

import dk.brics.tajs.util.AnalysisException;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import static dk.brics.tajs.util.Collections.newList;
import static dk.brics.tajs.util.Collections.newSet;

public class BlockDependencyOrderer { // TODO: javadoc

    private final Set<BasicBlock> ignored;

    private final Set<BasicBlock> temporarilyMarked;

    private final Set<BasicBlock> notPermanentlyMarked;

    private final List<BasicBlock> sorted;

    private Stack<Task> worklist;

    private BlockDependencyOrderer(Collection<BasicBlock> blocks, Set<BasicBlock> ignored, List<BasicBlock> rootOrder) {
        // TODO consider the amount of reversals in this implementation, some of them might cancel each other out!
        this.ignored = ignored;
        this.notPermanentlyMarked = newSet(blocks);
        this.temporarilyMarked = newSet();
        this.sorted = newList();
        this.worklist = new Stack<>();
        // Implementation choice in topological sort: process roots only, and in reverse order
        List<BasicBlock> reverseRootOrder = newList(rootOrder);
        Collections.reverse(reverseRootOrder);
        for (BasicBlock root : reverseRootOrder) {
            worklist.push(new ProcessBlock(root));
            while (!worklist.isEmpty()) {
                worklist.pop().process();
            }
        }
        if (!notPermanentlyMarked.isEmpty()) {
            // sanity check that it is ok to iterate roots instead of "notPermanentlyMarked"
            throw new AnalysisException("Bad topological sort implementation");
        }
        Collections.reverse(sorted); // the list has been built backwards...
    }

    /**
     * Produces a topological sorting of blocks with a depth-first search that ignores cycles.
     * Algorithm: wikipedia on topological sorting with depth-first (Cormen2001/Tarjan1976).
     * - slightly modified to produce prettier orders
     * - rewritten to iterative form (with callbacks) to avoid blowing the call stack
     */
    public static List<BasicBlock> produceDependencyOrder(Collection<BasicBlock> blocks, Set<BasicBlock> ignored, List<BasicBlock> rootOrder) {
        return new BlockDependencyOrderer(blocks, ignored, rootOrder).sorted;
    }

    private interface Task {

        void process();
    }

    private class ProcessBlock implements Task {

        private final BasicBlock block;

        public ProcessBlock(BasicBlock block) {
            this.block = block;
        }

        @Override
        public void process() {
            if (temporarilyMarked.contains(block)) {
                return;
            }
            if (notPermanentlyMarked.contains(block)) {
                temporarilyMarked.add(block);
                List<BasicBlock> successors = newList(block.getSuccessors());
                if (block.getExceptionHandler() != null) {
                    successors.add(block.getExceptionHandler());
                }

                // Implementation choice in topological sort: process multiple successors in reverse source position order.
                // A benefit of this is that loop back edges receive lower order than the loop successors.
                Collections.sort(successors, (o1, o2) -> -o1.getSourceLocation().compareTo(o2.getSourceLocation()));

                successors.removeAll(ignored);
                worklist.push(new PostProcessBlock(block)); // post-process needs to be below the processing of the current block's successors!
                Collections.reverse(successors); // reverse to get the right stack order
                for (BasicBlock m : successors) {
                    worklist.push(new ProcessBlock(m));
                }
            }
        }
    }

    private class PostProcessBlock implements Task {

        private final BasicBlock block;

        public PostProcessBlock(BasicBlock block) {
            this.block = block;
        }

        @Override
        public void process() {
            notPermanentlyMarked.remove(block);
            temporarilyMarked.remove(block);
            sorted.add(block);
        }
    }
}
