/*
 * Copyright 2009-2019 Aarhus University
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dk.brics.tajs.js2flowgraph;

import com.google.javascript.jscomp.parsing.parser.trees.ParseTree;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.jsnodes.IfNode;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.Pair;

import java.util.List;

import static dk.brics.tajs.util.Collections.newList;

/**
 * Miscellaneous immutable information environment for a recursive descent on an AST.
 * Forms an inheritance hierarchy of information about the parents and preceding siblings in the AST.
 */
public class AstEnv {

    /**
     * Parent environment, is queried if a value of interest is null.
     */
    private AstEnv parentEnv = null;

    /**
     * The enclosing function.
     */
    private Function function = null;

    /**
     * The register that is expected to contain a result value.
     */
    private Integer resultRegister = null;

    /**
     * The block to add nodes to and append new blocks to.
     */
    private BasicBlock appendBlock = null;

    /**
     * The block for variable declarations.
     * (Typically equal to function.getEntry.)
     */
    private BasicBlock declarationBlock = null;

    /**
     * Set if processing at statement level.
     * (Used for setting {@link AbstractNode#registers_done}.)
     */
    private Boolean statementLevel = null;

    /**
     * Manages registers for the current function.
     */
    private RegisterManager registerManager;

    /**
     * The register of the base object for variable lookups.
     */
    private Integer baseRegister = null;

    /**
     * The register where 'this' resides for object literal instantiations.
     */
    private Integer thisRegister;

    /**
     * Unnamed block to use for unlabelled 'continue' statements.
     */
    private BasicBlock unlabelledContinue;

    /**
     * Unnamed block to use for unlabelled 'break' statements.
     */
    private BasicBlock unlabelledBreak;

    /**
     * Named blocks to use for labelled 'continue' statements.
     */
    private Pair<String, BasicBlock> labelledContinue;

    /**
     * Named blocks to use for labelled 'break' statements.
     */
    private Pair<String, BasicBlock> labelledBreak;

    /**
     * Blocks that all breaks/continues/returns must go through.
     * (Used for finally-blocks and closing node-environments such as {@link dk.brics.tajs.flowgraph.jsnodes.EndWithNode}
     * or {@link dk.brics.tajs.flowgraph.jsnodes.EndForInNode}.
     * Thrown exceptions are similar, but they are handled explicitly through extra catch-blocks elsewhere.)
     */
    private JumpThroughBlocks jumpThroughBlocks;

    /**
     * Used by the unevalizer to get results from unevalized expressions.
     * Only one will be present at a time, so no need to keep searching upwards.
     */
    private UnevalExpressionResult unevalExpressionResult;

    /**
     * The label-name of a labelled loop statement
     */
    private Pair<ParseTree /* loop-statement type */, String> loopLabelName;

    /**
     * Expressions that are used in as a condition value.
     */
    private Pair<ParseTree, IfNode> enclosingIfNode;

    /**
     * Constructs an enviromentment with a parent environment.
     */
    private AstEnv(AstEnv parentEnv) {
        this.parentEnv = parentEnv;
    }

    /**
     * Constructs the initial environment.
     */
    public static AstEnv makeInitial() {
        AstEnv env = new AstEnv(null);
        env.resultRegister = AbstractNode.NO_VALUE;
        env.baseRegister = AbstractNode.NO_VALUE;
        env.statementLevel = true;
        env.registerManager = new RegisterManager(AbstractNode.FIRST_ORDINARY_REG);
        return env;
    }

    /**
     * Returns the block where ordinary new nodes or blocks should be to added.
     */
    public BasicBlock getAppendBlock() {
        if (appendBlock == null && parentEnv != null) {
            return parentEnv.getAppendBlock();
        }
        return appendBlock;
    }

    /**
     * Returns the register of the base object for variable lookups.
     */
    public int getBaseRegister() {
        if (baseRegister == null && parentEnv != null) {
            return parentEnv.getBaseRegister();
        }
        return baseRegister;
    }

    /**
     * Returns the block where variable declarations should be added.
     */
    public BasicBlock getDeclarationBlock() {
        if (declarationBlock == null && parentEnv != null) {
            return parentEnv.getDeclarationBlock();
        }
        return declarationBlock;
    }

    /**
     * Returns the nearest enclosing function.
     */
    public Function getFunction() {
        if (function == null && parentEnv != null) {
            return parentEnv.getFunction();
        }
        return function;
    }

    /**
     * Returns the environment at the nearest enclosing function.
     */
    public AstEnv getFunctionEnv() {
        AstEnv env = this;
        while (env != null) {
            if (env.function == null) {
                env = env.parentEnv;
            } else {
                return env;
            }
        }
        throw new AnalysisException("No enclosing function in environment!");
    }

    /**
     * Collects all {@link #jumpThroughBlocks} in the chain of environments up until (and not including) the given environment.
     *
     * @return a sequence of blocks to jump through, the innermost first
     * @see #jumpThroughBlocks
     */
    public List<JumpThroughBlocks> getJumpThroughBlocks(AstEnv other) {
        AstEnv env = this;
        List<JumpThroughBlocks> res = newList();
        while (env != other) {
            if (env.jumpThroughBlocks != null) {
                res.add(env.jumpThroughBlocks);
            }
            env = env.parentEnv;
            if (env == null) {
                throw new AnalysisException("Ancestor environment not present!");
            }
        }
        return res;
    }

    /**
     * Finds the 'break' basic block associated with the given label.
     */
    public BasicBlock getLabelledBreak(String labelName) {
        if ((labelledBreak == null || !labelName.equals(labelledBreak.getFirst())) && parentEnv != null) {
            return parentEnv.getLabelledBreak(labelName);
        }
        return labelledBreak.getSecond(); // cannot be null, syntax error if label is not found
    }

    /**
     * Returns the environment at the given 'break' label.
     */
    public AstEnv getLabelledBreakEnv(String labelName) {
        AstEnv env = this;
        while (env != null) {
            if (env.labelledBreak == null || !labelName.equals(env.labelledBreak.getFirst())) {
                env = env.parentEnv;
            } else {
                return env;
            }
        }
        throw new AnalysisException("Environment not found!");
    }

    /**
     * Finds the 'continue' basic block associated with the given label.
     */
    public BasicBlock getLabelledContinue(String labelName) {
        if ((labelledContinue == null || !labelName.equals(labelledContinue.getFirst())) && parentEnv != null) {
            return parentEnv.getLabelledContinue(labelName);
        }
        return labelledContinue.getSecond(); // cannot be null, syntax error if label is not found
    }

    /**
     * Returns the environment at the given 'continue' label.
     */
    public AstEnv getLabelledContinueEnv(String labelName) {
        AstEnv env = this;
        while (env != null) {
            if (env.labelledContinue == null || !labelName.equals(env.labelledContinue.getFirst())) {
                env = env.parentEnv;
            } else {
                return env;
            }
        }
        throw new AnalysisException("Environment not found!");
    }

    /**
     * Returns the register manager.
     */
    public RegisterManager getRegisterManager() {
        if (registerManager == null && parentEnv != null) {
            return parentEnv.getRegisterManager();
        }
        return registerManager;
    }

    /**
     * Returns the result register.
     */
    public int getResultRegister() {
        if (resultRegister == null && parentEnv != null) {
            return parentEnv.getResultRegister();
        }
        return resultRegister;
    }

    /**
     * Returns the 'this' register'.
     */
    public Integer getThisRegister() {
        if (thisRegister == null && parentEnv != null) {
            return parentEnv.getThisRegister();
        }
        return thisRegister;
    }

    /**
     * Returns the UnevalExpressionResult.
     */
    public UnevalExpressionResult getUnevalExpressionResult() {
        if (unevalExpressionResult == null && parentEnv != null) {
            return parentEnv.getUnevalExpressionResult();
        }
        return unevalExpressionResult;
    }

    /**
     * Returns the 'break' basic block with no label.
     */
    public BasicBlock getUnlabelledBreak() {
        if (unlabelledBreak == null && parentEnv != null) {
            return parentEnv.getUnlabelledBreak();
        }
        return unlabelledBreak;
    }

    /**
     * Returns the environment at the 'break' with no label.
     */
    public AstEnv getUnlabelledBreakEnv() {
        AstEnv env = this;
        while (env != null) {
            if (env.unlabelledBreak == null) {
                env = env.parentEnv;
            } else {
                return env;
            }
        }
        throw new AnalysisException("No unlabelled break environment present!");
    }

    /**
     * Returns the 'continue' basic block with no label.
     */
    public BasicBlock getUnlabelledContinue() {
        if (unlabelledContinue == null && parentEnv != null) {
            return parentEnv.getUnlabelledContinue();
        }
        return unlabelledContinue;
    }

    /**
     * Returns the environment at the 'continue' with no label.
     */
    public AstEnv getUnlabelledContinueEnv() {
        AstEnv env = this;
        while (env != null) {
            if (env.unlabelledContinue == null) {
                env = env.parentEnv;
            } else {
                return env;
            }
        }
        throw new AnalysisException("No unlabelled continue environment present!");
    }

    /**
     * Returns true if currently at statement level (not at a nested expression).
     */
    public boolean isStatementLevel() {
        if (statementLevel == null && parentEnv != null) {
            return parentEnv.isStatementLevel();
        }
        return statementLevel;
    }

    /**
     * Creates a new environment with the given append block.
     */
    public AstEnv makeAppendBlock(BasicBlock b) {
        if (appendBlock == b) {
            return this;
        }
        AstEnv newEnv = new AstEnv(this);
        newEnv.appendBlock = b;
        return newEnv;
    }

    /**
     * Creates a new environment with the given base register.
     */
    public AstEnv makeBaseRegister(int r) {
        if (baseRegister != null && baseRegister == r) {
            return this;
        }
        AstEnv newEnv = new AstEnv(this);
        newEnv.baseRegister = r;
        return newEnv;
    }

    /**
     * Creates a new environment with the given declaration block.
     */
    public AstEnv makeDeclarationBlock(BasicBlock r) {
        AstEnv newEnv = new AstEnv(this);
        newEnv.declarationBlock = r;
        return newEnv;
    }

    /**
     * Creates a new environment with the given enclosing function.
     */
    public AstEnv makeEnclosingFunction(Function enclosingFunction) {
        AstEnv newEnv = new AstEnv(this);
        newEnv.appendBlock = enclosingFunction.getEntry().getSingleSuccessor();
        newEnv.function = enclosingFunction;
        newEnv.declarationBlock = enclosingFunction.getEntry();
        return newEnv;
    }

    /**
     * Creates a new environment with the given JumpThroughBlocks.
     * The supplied blocks should be clones of other blocks! Successors will be added later.
     */
    public AstEnv makeJumpThroughBlock(JumpThroughBlocks j) { // TODO some duplicated code present around calls to this method: 1. boxing of single nodes, 2. logic for wiring exceptional, ordinary, and (this) jump-flow
        if (jumpThroughBlocks == j) {
            return this;
        }
        AstEnv newEnv = new AstEnv(this);
        newEnv.jumpThroughBlocks = j;
        return newEnv;
    }

    /**
     * Creates a new environment with the given labeled 'break' block.
     */
    public AstEnv makeLabelledBreak(String labelName, BasicBlock breakBlock) {
        AstEnv newEnv = new AstEnv(this);
        newEnv.labelledBreak = Pair.make(labelName, breakBlock);
        return newEnv;
    }

    /**
     * Creates a new environment with the given labeled 'continue' block.
     */
    public AstEnv makeLabelledContinue(String labelName, BasicBlock continueBlock) {
        AstEnv newEnv = new AstEnv(this);
        newEnv.labelledContinue = Pair.make(labelName, continueBlock);
        return newEnv;
    }

    /**
     * Creates a new environment with the given register manager.
     */
    public AstEnv makeRegisterManager(RegisterManager rm) {
        AstEnv newEnv = new AstEnv(this);
        newEnv.registerManager = rm;
        return newEnv;
    }

    /**
     * Creates a new environment with the given result register.
     */
    public AstEnv makeResultRegister(int r) {
        AstEnv newEnv = new AstEnv(this);
        newEnv.resultRegister = r;
        return newEnv;
    }

    /**
     * Creates a new environment and selects the statement level flag.
     */
    public AstEnv makeStatementLevel(boolean statement) {
        if (statementLevel != null && statementLevel == statement) {
            return this;
        }
        AstEnv newEnv = new AstEnv(this);
        newEnv.statementLevel = statement;
        return newEnv;
    }

    /**
     * Creates a new environment with the given 'this' register.
     */
    public AstEnv makeThisRegister(int r) {
        if (thisRegister != null && thisRegister == r) {
            return this;
        }
        AstEnv newEnv = new AstEnv(this);
        newEnv.thisRegister = r;
        return newEnv;
    }

    /**
     * Creates a new environment with the given UnevalExpressionResult.
     */
    public AstEnv makeUnevalExpressionResult(UnevalExpressionResult u) {
        AstEnv newEnv = new AstEnv(this);
        newEnv.unevalExpressionResult = u;
        return newEnv;
    }

    /**
     * Creates a new environment with the given unlabeled 'break' block.
     */
    public AstEnv makeUnlabelledBreak(BasicBlock defaultBreak) {
        assert (defaultBreak != null);
        if (unlabelledBreak == defaultBreak) {
            return this;
        }
        AstEnv newEnv = new AstEnv(this);
        newEnv.unlabelledBreak = defaultBreak;
        return newEnv;
    }

    /**
     * Creates a new environment with the given unlabeled 'continue' and 'break' blocks.
     */
    public AstEnv makeUnlabelledContinueAndBreak(BasicBlock defaultContinue, BasicBlock defaultBreak) {
        assert (defaultContinue != null && defaultBreak != null);
        if (unlabelledContinue == defaultContinue && unlabelledBreak == defaultBreak) {
            return this;
        }
        AstEnv newEnv = new AstEnv(this);
        newEnv.unlabelledContinue = defaultContinue;
        newEnv.unlabelledBreak = defaultBreak;
        return newEnv;
    }

    public AstEnv makeLoopLabelName(ParseTree loopStatement, String name) {
        assert (loopStatement != null && name != null);
        assert (FunctionBuilderHelper.isLoopStatement(loopStatement));
        AstEnv newEnv = new AstEnv(this);
        newEnv.loopLabelName = Pair.make(loopStatement, name);
        return newEnv;
    }

    public boolean hasLoopLabel(ParseTree loopStatement) {
        if (loopLabelName != null && loopLabelName.getFirst() == loopStatement) {
            return true;
        }
        if (parentEnv != null) {
            return parentEnv.hasLoopLabel(loopStatement);
        }
        return false;
    }

    public String getLoopLabelName(ParseTree loopStatement) {
        if (loopLabelName != null && loopLabelName.getFirst() == loopStatement) {
            return loopLabelName.getSecond();
        }
        if (parentEnv != null) {
            return parentEnv.getLoopLabelName(loopStatement);
        }
        throw new AnalysisException("No loop label name present (query with hasLoopLabel first)");
    }

    public AstEnv makeEnclosingIfNode(ParseTree condition, IfNode ifNode) {
        assert (condition != null && ifNode != null);
        AstEnv newEnv = new AstEnv(this);
        newEnv.enclosingIfNode = Pair.make(condition, ifNode);
        return newEnv;
    }

    public IfNode getEnclosingIfNode(ParseTree expressionTree) {
        if (enclosingIfNode != null && enclosingIfNode.getFirst() == expressionTree) {
            return enclosingIfNode.getSecond();
        }
        if (parentEnv != null) {
            return parentEnv.getEnclosingIfNode(expressionTree);
        }
        return null;
    }
}
