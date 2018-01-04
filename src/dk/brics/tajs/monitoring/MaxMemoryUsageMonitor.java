/*
 * Copyright 2009-2018 Aarhus University
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

package dk.brics.tajs.monitoring;

import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.options.Options;
import org.apache.log4j.Logger;

import java.text.NumberFormat;
import java.util.Locale;

public class MaxMemoryUsageMonitor extends DefaultAnalysisMonitoring {

    private static final Logger log = Logger.getLogger(MaxMemoryUsageMonitor.class);

    private int block_transfers = 0;

    /**
     * Maximum memory usage measured.
     */
    private long max_used_bytes = 0;

    @Override
    public void visitBlockTransferPre(BasicBlock block, State state) {
        block_transfers++;
        if (Options.get().isMemoryMeasurementEnabled()) {
            if (block_transfers % 10 == 0) {
                System.gc();
                long m = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
                if (m > max_used_bytes) {
                    max_used_bytes = m;
                }
            }
        }
    }

    @Override
    public void visitPhasePost(AnalysisPhase phase) {
        if (phase == AnalysisPhase.SCAN) {
            NumberFormat formatter = NumberFormat.getNumberInstance(Locale.US);
            formatter.setMaximumFractionDigits(2);
            log.info(String.format("Max memory used: %sMB", formatter.format((max_used_bytes / (1024L * 1024L)))));
        }
    }
}
