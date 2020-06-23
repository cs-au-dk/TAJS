/*
 * Copyright 2009-2020 Aarhus University
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

package dk.brics.tajs.typetesting;

import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.IContextSensitivityStrategy;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.flowgraph.FlowGraph;
import dk.brics.tajs.lattice.HostObject;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.solver.BlockAndContext;
import dk.brics.tajs.solver.IContext;

/**
 * This interface is used implement a type tester (e.g. ReaGenT) with TAJS.
 */
public interface ITypeTester<ContextType extends IContext<ContextType>> {

  /**
   * Called after the main script has finished. Is used to trigger type tests such as calling functions and reading properties.
   * A type tester can add itself back on the worklist, in which case it will be called again after all the new dataflow has been processed.
   */
  void triggerTypeTests(Solver.SolverInterface c);

  /**
   * Called when TAJS triggers a function call to a host object of kind HostAPIs.SPEC.
   *
   * @param hostObject the function that was called
   * @param call information describing the call (arguments etc.)
   * @return the abstract value produced by the function call
   */
  Value evaluateCallToSymbolicFunction(HostObject hostObject, final FunctionCalls.CallInfo call, Solver.SolverInterface c);

  /**
   * Returns true if the solver should skip the entry.
   */
  boolean shouldSkipEntry(BlockAndContext<ContextType> e);

  /**
   * Returns true if an exception can be ignored.
   */
  boolean shouldIgnoreException(Exception e, BlockAndContext<ContextType> p);

  /**
   * Returns the context sensitivity strategy.
   */
  IContextSensitivityStrategy getCustomContextSensitivityStrategy(FlowGraph fg);

  /**
   * Compares two worklist entries.
   * @return null if use default order, &lt;0 if bc1 first, &gt;0 if bc2 first
   */
  Integer compareWorkListEntries(BlockAndContext<ContextType> bc1, BlockAndContext<ContextType> bc2);
}