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

package dk.brics.tajs.js2flowgraph;

import com.google.javascript.jscomp.parsing.parser.Parser;
import com.google.javascript.jscomp.parsing.parser.Parser.Config.Mode;
import com.google.javascript.jscomp.parsing.parser.SourceFile;
import com.google.javascript.jscomp.parsing.parser.trees.ProgramTree;
import com.google.javascript.jscomp.parsing.parser.util.ErrorReporter;
import com.google.javascript.jscomp.parsing.parser.util.SourcePosition;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.flowgraph.SourceLocation.SourceLocationMaker;

import java.util.List;

import static dk.brics.tajs.util.Collections.newList;

/**
 * JavaScript parser.
 * Based on the parser from the Google Closure Compiler.
 */
public class JavaScriptParser {

    private final Mode mode;

    private final boolean strict;

    /**
     * Constructs a new parser.
     */
    public JavaScriptParser(Mode languageMode, boolean strict) {
        mode = languageMode;
        this.strict = strict;
    }

    /**
     * Parses the given JavaScript code.
     * The syntax check includes break/continue label consistency and no duplicate parameters.
     *
     * @param contents the code
     */
    public ParseResult parse(String contents, SourceLocationMaker sourceLocationMaker) {
        final List<SyntaxMesssage> warnings = newList();
        final List<SyntaxMesssage> errors = newList();

        ErrorReporter errorReporter = new ErrorReporter() {
            @Override
            protected void reportError(SourcePosition sourcePosition, String message) {
                errors.add(new SyntaxMesssage(message, sourceLocationMaker.make(sourcePosition.line, sourcePosition.column + 1, sourcePosition.line, sourcePosition.column + 1)));
            }

            @Override
            protected void reportWarning(SourcePosition sourcePosition, String message) {
                warnings.add(new SyntaxMesssage(message, sourceLocationMaker.make(sourcePosition.line, sourcePosition.column + 1, sourcePosition.line, sourcePosition.column + 1)));
            }
        };

        ProgramTree programAST = null;
        try {
            programAST = new Parser(new Parser.Config(mode, strict), errorReporter, new SourceFile(sourceLocationMaker.makeUnspecifiedPosition().toString(), contents)).parseProgram();
        } catch (Exception e) {
            errors.add(new SyntaxMesssage(String.format("%s: %s", e.getClass(), e.getMessage()), sourceLocationMaker.makeUnspecifiedPosition()));
        }
        return new ParseResult(programAST, errors, warnings);
    }

    /**
     * Syntax error message.
     */
    static class SyntaxMesssage {

        private final String message;

        private final SourceLocation sourceLocation;

        /**
         * Constructs a new syntax error message object.
         */
        SyntaxMesssage(String message, SourceLocation sourceLocation) {
            this.message = message;
            this.sourceLocation = sourceLocation;
        }

        /**
         * Returns the message.
         */
        String getMessage() {
            return message;
        }

        /**
         * Returns the source location.
         */
        SourceLocation getSourceLocation() {
            return sourceLocation;
        }
    }

    /**
     * Result from parser.
     */
    public static class ParseResult {

        private final List<SyntaxMesssage> errors;

        private final List<SyntaxMesssage> warnings;

        private ProgramTree programAST;

        private ParseResult(ProgramTree programAST, List<SyntaxMesssage> errors, List<SyntaxMesssage> warnings) {
            this.programAST = programAST;
            this.errors = errors;
            this.warnings = warnings;
        }

        /**
         * Returns the AST, or null if parse error.
         */
        public ProgramTree getProgramAST() {
            return programAST;
        }

        /**
         * Returns the list of parse errors.
         */
        List<SyntaxMesssage> getErrors() {
            return errors;
        }

        /**
         * Returns the list of parse warnings.
         */
        List<SyntaxMesssage> getWarnings() {
            return warnings;
        }
    }
}
