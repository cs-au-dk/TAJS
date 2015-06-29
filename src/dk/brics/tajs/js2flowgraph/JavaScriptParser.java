package dk.brics.tajs.js2flowgraph;

import com.google.javascript.jscomp.parsing.Config;
import com.google.javascript.jscomp.parsing.Config.LanguageMode;
import com.google.javascript.jscomp.parsing.ConfigExposer;
import com.google.javascript.jscomp.parsing.ParserRunner;
import com.google.javascript.jscomp.parsing.parser.Parser;
import com.google.javascript.jscomp.parsing.parser.Parser.Config.Mode;
import com.google.javascript.jscomp.parsing.parser.SourceFile;
import com.google.javascript.jscomp.parsing.parser.trees.ProgramTree;
import com.google.javascript.jscomp.parsing.parser.util.MutedErrorReporter;
import com.google.javascript.rhino.head.ErrorReporter;
import com.google.javascript.rhino.head.EvaluatorException;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.Collections;

import java.io.IOException;
import java.util.List;

import static dk.brics.tajs.util.Collections.newList;

/**
 * JavaScript parser.
 * Based on the parser from the Google Closure Compiler.
 */
class JavaScriptParser {

    // Logger is *not* the log4j logger used elsewhere in TAJS, it is used by the ParserRunner
    private static java.util.logging.Logger parserLogger = java.util.logging.Logger.getAnonymousLogger();

    private final Mode mode;

    private final Config config;

    /**
     * Constructs a new parser.
     */
    JavaScriptParser(Mode languageMode) {
        mode = languageMode;
        LanguageMode m;
        switch (mode) {
            case ES3:
                m = LanguageMode.ECMASCRIPT3;
                break;
            case ES5:
                m = LanguageMode.ECMASCRIPT5;
                break;
            case ES5_STRICT:
                m = LanguageMode.ECMASCRIPT5_STRICT;
                break;
            case ES6:
                m = LanguageMode.ECMASCRIPT6;
                break;
            case ES6_STRICT:
                m = LanguageMode.ECMASCRIPT6_STRICT;
                break;
            default:
                throw new AnalysisException("Unexpected enum: " + mode);
        }
        config = ConfigExposer.createConfig(Collections.<String>newSet(), Collections.<String>newSet(), true, m, false);
    }

    /**
     * Parses the given JavaScript code.
     * The syntax check includes break/continue label consistency and no duplicate parameters.
     *
     * @param name     file name or URL of the code
     * @param contents the code
     */
    ParseResult parse(String name, String contents) {
        final List<SyntaxMesssage> warnings = newList();
        final List<SyntaxMesssage> errors = newList();
        try {
            ParserRunner.parse(new com.google.javascript.jscomp.SourceFile(name), contents, config, new ErrorReporter() {
                @Override
                public void warning(String message, String name2, int lineNumber, String UNKNOWN_PURPOSE_PARAMETER, int columnNumber) {
                    warnings.add(new SyntaxMesssage(message, new SourceLocation(lineNumber, columnNumber + 1, name2)));
                }

                @Override
                public void error(String message, String name2, int lineNumber, String UNKNOWN_PURPOSE_PARAMETER, int columnNumber) {
                    errors.add(new SyntaxMesssage(message, new SourceLocation(lineNumber, columnNumber + 1, name2)));
                }

                @Override
                public EvaluatorException runtimeError(String s, String s2, int i, String s3, int i2) {
                    throw new AnalysisException("Runtime error in parser");
                }
            }, parserLogger);
        } catch (IOException e) {
            errors.add(new SyntaxMesssage(String.format("%s: %s", e.getClass(), e.getMessage()), new SourceLocation(-1, -1, name)));
        }
        ProgramTree programAST = null;
        if (errors.isEmpty()) {
            programAST = new Parser(new Parser.Config(mode), new MutedErrorReporter(), new SourceFile(name, contents)).parseProgram();
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
    static class ParseResult {

        private ProgramTree programAST;

        private final List<SyntaxMesssage> errors;

        private final List<SyntaxMesssage> warnings;

        private ParseResult(ProgramTree programAST, List<SyntaxMesssage> errors, List<SyntaxMesssage> warnings) {
            this.programAST = programAST;
            this.errors = errors;
            this.warnings = warnings;
        }

        /**
         * Returns the AST, or null if parse error.
         */
        ProgramTree getProgramAST() {
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
