/*
 * Copyright 2009-2015 Aarhus University
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

package dk.brics.tajs.analysis;

import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.jsnodes.EndForInNode;
import dk.brics.tajs.flowgraph.jsnodes.ExceptionalReturnNode;
import dk.brics.tajs.flowgraph.jsnodes.ReturnNode;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.SpecialVars;
import dk.brics.tajs.lattice.UnknownValueResolver;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.solver.BlockAndContext;
import dk.brics.tajs.solver.IContext;
import dk.brics.tajs.util.Collections;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.Set;

import static dk.brics.tajs.util.Collections.newMap;
import static dk.brics.tajs.util.Collections.newSet;

/**
 * Context for context sensitive analysis.
 * <p>
 * This particular context sensitivity strategy distinguishes between abstract states that have different possible values of 'this'.
 * It also distinguishes between abstract states that have different values for the selected special variables (see {@link SpecialVars}) and special registers.
 * <p>
 * Immutable.
 */
public final class Context implements IContext<Context> {

    private static final int MAX_NUM_SPECIALIZATION = 50; // TODO: set through Options?

    private static Logger log = Logger.getLogger(Context.class);

    private Set<ObjectLabel> thisval; // TODO: canonicalize? (#140)

    /**
     * Values of special variables at function entry, or null if none.
     */
    private Map<String, Value> specialentryvars;

    /**
     * Values of special variables, or null if none.
     */
    private Map<String, Value> specialvars;

    /**
     * Values of special registers, or null if none.
     */
    private Map<Integer, Value> specialregs;

    /**
     * The entry block of the function or for-in body where this context belongs.
     * For a for-in specialized context, the entry block is the successor of the BeginForInNode where the specialization was made.
     */
    private BasicBlock entry_block; // (ignored in equals/hashCode)

    /**
     * The context at the entry of the function or for-in body where this context belongs.
     */
    private Context entry_context; // (ignored in equals/hashCode)

    /**
     * Enclosing context. (Used only at for-in bodies.)
     */
    private Context enclosing_context; // (ignored in equals/hashCode)

    private Context(BasicBlock entry_block) {
        this.entry_block = entry_block;
    }

    /**
     * Constructs a new initial context.
     *
     * @param state initial state
     * @param block entry of the function where this context belongs
     */
    public static Context makeInitialContext(State state, BasicBlock block) {
        Context c = new Context(block);
        c.entry_context = c; // entry context points to itself
        if (!Options.get().isObjectSensitivityDisabled()) {
            if (block.getFunction().isUsesThis()) {
                c.thisval = newSet(state.readThisObjects());
            }
        }
        if (log.isDebugEnabled())
            log.debug("creating initial Context " + c);
        return c;
    }

    /**
     * Constructs a new context for an ordinary call.
     *
     * @param state callee state
     * @param block entry of the function where this context belongs
     * @param sa    special variables info, null if none
     */
    public static Context makeFunctionEntryContext(State state, BasicBlock block, SpecialVars sa) {
        Context c = new Context(block);
        c.entry_context = c; // entry context points to itself
        if (!Options.get().isObjectSensitivityDisabled()) {
            if (block.getFunction().isUsesThis()) {
                c.thisval = newSet(state.readThisObjects());
            }
        }
        if (Options.get().isParameterSensitivityEnabled()) {
            if (sa != null) {
                Set<String> vars = sa.getSpecialVars(block.getFunction());
                if (vars != null) {
                    c.specialvars = c.specialentryvars = newMap();
                    for (String var : vars) {
                        c.putSpecialVar(state, var);
                    }
                }
            }
        }
        if (log.isDebugEnabled())
            log.debug("creating function entry Context " + c);
        return c;
    }

    /**
     * Constructs a new context for entering a for-in body.
     *
     * @param enclosing_context enclosing context
     * @param block             entry of the for-in body
     * @param reg               additional special register
     * @param v                 value for the additional special register
     */
    public static Context makeForInEntryContext(Context enclosing_context, BasicBlock block, int reg, Value v) {
        Context c = new Context(block);
        c.entry_context = c; // entry context points to itself
        c.thisval = enclosing_context.thisval;
        c.specialvars = enclosing_context.specialvars;
        c.specialentryvars = enclosing_context.specialentryvars;
        c.enclosing_context = enclosing_context;
        if (!Options.get().isForInSpecializationDisabled()) {
            c.specialregs = (enclosing_context.specialregs != null) ? newMap(enclosing_context.specialregs) : Collections.<Integer, Value>newMap();
            c.specialregs.put(reg, v);
        }
        if (log.isDebugEnabled())
            log.debug("creating for-in Context " + c);
        return c;
    }

    /**
     * Constructs a new context for entering a basic block in the same function and for-in body.
     * Also changes the context of the given state to the newly constructed context.
     *
     * @param state the new state, used for adjusting the values of the special variables
     * @param block the basic block
     */
    public static Context makeSuccessorContext(State state, BasicBlock block) {
        Context c = new Context(block);
        Context previous = state.getContext();
        c.entry_block = previous.entry_block;
        c.entry_context = previous.entry_context;
        c.thisval = previous.thisval;
        c.enclosing_context = previous.enclosing_context;
        if (block.getFirstNode() instanceof ReturnNode ||
                block.getFirstNode() instanceof ExceptionalReturnNode ||
                block.getFirstNode() instanceof EndForInNode) {
            // for ReturnNode, ExceptionalReturnNode, and EndForInNode, get specialregs and specialvars from the entry location
            c.specialregs = previous.entry_context.specialregs;
            c.specialvars = previous.entry_context.specialvars;
            c.specialentryvars = previous.entry_context.specialentryvars;
        } else {
            // for ordinary successors, update the specialvars values from the predecessor
            c.specialregs = previous.specialregs;
            c.specialentryvars = previous.specialentryvars;
            if (Options.get().isNumericVariableSensitivityEnabled()) {
                if (previous.specialvars != null) {
                    c.specialvars = newMap();
                    for (String var : previous.specialvars.keySet()) {
                        c.putSpecialVar(state, var);
                    }
                }
            }
        }
        if (log.isDebugEnabled())
            log.debug("creating Context " + c);
        state.setContext(c);
        return c;
    }

    /**
     * Adds special variable value.
     */
    private void putSpecialVar(State state, String var) {
        Value v = state.readVariable(var, null);
        v = UnknownValueResolver.getRealValue(v, state);
        if (v.isMaybeSingleNum() && (v.getNum() < 0 || v.getNum() > MAX_NUM_SPECIALIZATION)) { // FIXME: also bound on other value facets + also bound specialregs (#19)
            if (v.isMaybeSingleNumUInt())
                v = v.restrictToNotNum().joinAnyNumUInt();
            else
                v = v.restrictToNotNum().joinAnyNum();
        }
        specialvars.put(var, v); // FIXME: needs bound (#19)
    }

    /**
     * Returns the special entry-variables map.
     */
    public Map<String, Value> getSpecialEntryVars() {
        return specialentryvars;
    }

    /**
     * Returns the special variables map.
     */
    public Map<String, Value> getSpecialVars() {
        return specialvars;
    }

    /**
     * Returns the special registers map.
     */
    public Map<Integer, Value> getSpecialRegisters() {
        return specialregs;
    }

    @Override
    public BlockAndContext<Context> getEntryBlockAndContext() {
        return new BlockAndContext<>(entry_block, entry_context);
    }

    /**
     * Enclosing context, only non-null for for-in specialized contexts.
     */
    public Context getEnclosingContext() {
        return enclosing_context;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Context))
            return false;
        Context c = (Context) obj;
        if ((thisval == null) != (c.thisval == null))
            return false;
        if (thisval != null && !thisval.equals(c.thisval)) // using collection equality
            return false;
        if ((specialentryvars == null) != (c.specialentryvars == null))
            return false;
        if (specialentryvars != null && !specialentryvars.equals(c.specialentryvars)) // using collection equality
            return false;
        if ((specialvars == null) != (c.specialvars == null))
            return false;
        if (specialvars != null && !specialvars.equals(c.specialvars)) // using collection equality
            return false;
        if ((specialregs == null) != (c.specialregs == null))
            return false;
        return !(specialregs != null && !specialregs.equals(c.specialregs));
    }

    @Override
    public int hashCode() {
        // see equals
        return (thisval != null ? thisval.hashCode() : 0) * 17 +
                (specialentryvars == null ? 0 : specialentryvars.hashCode()) * 19 +
                (specialvars == null ? 0 : specialvars.hashCode()) * 7 +
                (specialregs != null ? specialregs.hashCode() : 0) * 3;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("{");
        boolean any = false;
        if (thisval != null) {
            s.append("this=").append(thisval);
            any = true;
        }
        if (specialentryvars != null) {
            if (any)
                s.append(", ");
            s.append("specialentryvars=").append(specialentryvars);
            any = true;
        }
        if (specialvars != null) {
            if (any)
                s.append(", ");
            s.append("specialvars=").append(specialvars);
            any = true;
        }
        if (specialregs != null) {
            if (any)
                s.append(", ");
            s.append("specialregs=").append(specialregs);
            any = true;
        }
        s.append("}");
        return s.toString();
    }
}
