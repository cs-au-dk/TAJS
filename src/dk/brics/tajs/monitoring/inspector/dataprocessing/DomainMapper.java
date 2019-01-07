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

package dk.brics.tajs.monitoring.inspector.dataprocessing;

import dk.brics.inspector.api.model.ids.FileID;
import dk.brics.inspector.api.model.ids.ObjectID;
import dk.brics.inspector.api.model.lines.MessageCertainty;
import dk.brics.inspector.api.model.lines.MessageLevel;
import dk.brics.inspector.api.model.lines.MessageSource;
import dk.brics.inspector.api.model.locations.ContextInsensitiveDescribedLocation;
import dk.brics.inspector.api.model.locations.ContextSensitiveDescribedLocation;
import dk.brics.inspector.api.model.locations.DescribedContext;
import dk.brics.inspector.api.model.locations.SourceRange;
import dk.brics.inspector.api.model.values.CompositeValue;
import dk.brics.inspector.api.model.values.DescribedObject;
import dk.brics.inspector.api.model.values.DescribedPrimitive;
import dk.brics.inspector.api.model.values.SingleValue;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.lattice.Context;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.solver.Message;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.Collectors;
import dk.brics.tajs.util.Pair;

import java.util.Set;

import static dk.brics.tajs.util.Collections.newSet;

/**
 * Main utility for mapping between the TAJS domain and the {@link dk.brics.inspector.api.InspectorAPI} domain.
 */
public class DomainMapper {

    private final IDManager idManager;

    public DomainMapper(IDManager idManager) {
        this.idManager = idManager;
    }

    private static Set<Value> splitValue(Value value) {
        Set<Value> valueSplits = newSet();
        valueSplits.add(value.restrictToUndef());
        valueSplits.add(value.restrictToNull());
        valueSplits.add(value.restrictToBool());
        valueSplits.add(value.restrictToNum());
        valueSplits.add(value.restrictToStr());
        value.getObjectLabels().forEach(l -> valueSplits.add(Value.makeObject(l)));
        value.getGetters().forEach(l -> valueSplits.add(Value.makeObject(l).makeGetter()));
        value.getSetters().forEach(l -> valueSplits.add(Value.makeObject(l).makeSetter()));
        valueSplits.remove(Value.makeNone());
        return valueSplits;
    }

    public SourceRange makeFromSourceLocation(SourceLocation sourceLocation) {
        return new SourceRange(sourceLocation.getLineNumber(), sourceLocation.getEndLineNumber(), sourceLocation.getColumnNumber(), sourceLocation.getEndColumnNumber());
    }

    public CompositeValue makeCompositeValue(Value value) {
        Set<Value> values = splitValue(value);
        Set<SingleValue> nonCompositeValues = values.stream().map(v -> {
            assert v.getObjectLabels().size() < 2;
            assert v.typeSize() < 2;
            if (v.isMaybeObjectOrSymbol()) {
                ObjectLabel label = v.getObjectLabels().iterator().next()  /* singleton by now! */;
                return makeObject(label, idManager.make(label));
            }
            return makePrimitiveValue(v);
        }).collect(Collectors.toSet());
        return new CompositeValue(nonCompositeValues);
    }

    public ContextSensitiveDescribedLocation makeDescribedLocation(AbstractNode node, Context context) {
        ContextInsensitiveDescribedLocation syntacticLocation = makeDescribedLocation(node);
        return new ContextSensitiveDescribedLocation(syntacticLocation.fileID, syntacticLocation.range, makeDescribedContext(context), idManager.make(Pair.make(node, context)), node.getIndex());
    }

    public Set<ContextSensitiveDescribedLocation> makeDescribedLocations(Set<Pair<AbstractNode, Context>> predecessors) {
        return predecessors.stream()
                .map(e -> makeDescribedLocation(e.getFirst(), e.getSecond()))
                .collect(Collectors.toSet());
    }

    public ContextInsensitiveDescribedLocation makeDescribedLocation(AbstractNode node) {
        FileID fileID = idManager.make(node.getSourceLocation().getLocation());
        SourceRange sourceRange = makeFromSourceLocation(node.getSourceLocation());
        return new ContextInsensitiveDescribedLocation(fileID, sourceRange, idManager.make(node), node.getIndex());
    }

    public DescribedContext makeDescribedContext(Context context) {
        return new DescribedContext(context.toString(), idManager.make(context));
    }

    public DescribedPrimitive makePrimitiveValue(Value value) {
        assert value.typeSize() < 2;
        assert !value.isMaybeObject();
        return new DescribedPrimitive(value.toString());
    }

    public DescribedObject makeObject(ObjectLabel label, ObjectID id) {
        return new DescribedObject(label.toString(), decideInvokable(label.getKind()), id);
    }

    private boolean decideInvokable(ObjectLabel.Kind kind) {
        return kind == ObjectLabel.Kind.FUNCTION;
    }

    public MessageLevel makeMessageLevel(Message.Severity severity) {
        switch (severity) {
            case TAJS_ERROR:
            case TAJS_UNSOUNDNESS:
            case HIGH:
                return MessageLevel.ERROR;
            case MEDIUM_IF_CERTAIN_NONE_OTHERWISE:
            case MEDIUM:
                return MessageLevel.WARN;
            case LOW:
            case TAJS_META:
                return MessageLevel.INFO;
            default:
                throw new RuntimeException("Unhandled enum: " + severity);
        }
    }

    public dk.brics.inspector.api.model.Optional<MessageCertainty> makeMessageCertainty(Message.Status status) {
        switch (status) {
            case CERTAIN:
                return new dk.brics.inspector.api.model.Optional<>(MessageCertainty.CERTAIN);
            case MAYBE:
                return new dk.brics.inspector.api.model.Optional<>(MessageCertainty.MAYBE);
            case INFO:
            case NONE:
                return new dk.brics.inspector.api.model.Optional<>();
            default:
                throw new RuntimeException("Unhandled enum: " + status);
        }
    }

    public MessageSource makeMessageSource(Message.Severity uncounditionalSeverity) {
        switch (uncounditionalSeverity){
            case TAJS_ERROR:
            case TAJS_META:
            case TAJS_UNSOUNDNESS:
                return MessageSource.ANALYSIS_BEHAVIOR;
            case HIGH:
            case MEDIUM:
            case LOW:
            case MEDIUM_IF_CERTAIN_NONE_OTHERWISE:
                return MessageSource.ANALYSIS_RESULT;
            default:
                throw new AnalysisException("Unhandled enum: " + uncounditionalSeverity);
        }
    }
}
