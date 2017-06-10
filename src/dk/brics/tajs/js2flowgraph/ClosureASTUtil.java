/*
 * Copyright 2009-2017 Aarhus University
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

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.parsing.parser.LiteralToken;
import com.google.javascript.jscomp.parsing.parser.trees.BinaryOperatorTree;
import dk.brics.tajs.util.AnalysisException;

/**
 * Utility methods extracted from <code>com.google.javascript.jscomp.parsing.ExampleIRFactory</code>.
 */
class ClosureASTUtil {

    static String normalizeString(LiteralToken token, boolean templateLiteral) {
        String value = token.value;
        if (templateLiteral) {
            // <CR><LF> and <CR> are normalized as <LF> for raw string value
            value = value.replaceAll("\r\n?", "\n");
        }
        int start = templateLiteral ? 0 : 1; // skip the leading quote
        int cur = value.indexOf('\\');
        if (cur == -1) {
            // short circuit no escapes.
            return templateLiteral ? value : value.substring(1, value.length() - 1);
        }
        StringBuilder result = new StringBuilder();
        while (cur != -1) {
            if (cur - start > 0) {
                result.append(value, start, cur);
            }
            cur += 1; // skip the escape char.
            char c = value.charAt(cur);
            switch (c) {
                case '\'':
                case '"':
                case '\\':
                    result.append(c);
                    break;
                case 'b':
                    result.append('\b');
                    break;
                case 'f':
                    result.append('\f');
                    break;
                case 'n':
                    result.append('\n');
                    break;
                case 'r':
                    result.append('\r');
                    break;
                case 't':
                    result.append('\t');
                    break;
                case 'v':
                    result.append('\u000B');
                    break;
                case '\n':
// TAJS: warnings should be emitted by another tool
//                    features = features.require(FeatureSet.Feature.STRING_CONTINUATION);
//                    if (isEs5OrBetterMode()) {
//                        errorReporter.warning(STRING_CONTINUATION_WARNING,
//                                sourceName,
//                                lineno(token.location.start), charno(token.location.start));
//                    } else {
//                        errorReporter.error(STRING_CONTINUATION_ERROR,
//                                sourceName,
//                                lineno(token.location.start), charno(token.location.start));
//                    }
                    // line continuation, skip the line break
                    break;
                case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7':
                    char next1 = value.charAt(cur + 1);

// TAJS: warnings should be emitted by another tool
//                    if (inStrictContext()) {
//                        if (c == '0' && !isOctalDigit(next1)) {
//                            // No warning: "\0" followed by a character which is not an octal digit
//                            // is allowed in strict mode.
//                        } else {
//                            errorReporter.warning(OCTAL_STRING_LITERAL_WARNING,
//                                    sourceName,
//                                    lineno(token.location.start), charno(token.location.start));
//                        }
//                    }

                    if (!isOctalDigit(next1)) {
                        result.append((char) octaldigit(c));
                    } else {
                        char next2 = value.charAt(cur + 2);
                        if (!isOctalDigit(next2)) {
                            result.append((char) (8 * octaldigit(c) + octaldigit(next1)));
                            cur += 1;
                        } else {
                            result.append((char)
                                    (8 * 8 * octaldigit(c) + 8 * octaldigit(next1) + octaldigit(next2)));
                            cur += 2;
                        }
                    }

                    break;
                case 'x':
                    result.append((char) (
                            hexdigit(value.charAt(cur + 1)) * 0x10
                                    + hexdigit(value.charAt(cur + 2))));
                    cur += 2;
                    break;
                case 'u':
                    int escapeEnd;
                    String hexDigits;
                    if (value.charAt(cur + 1) != '{') {
                        // Simple escape with exactly four hex digits: \\uXXXX
                        escapeEnd = cur + 5;
                        hexDigits = value.substring(cur + 1, escapeEnd);
                    } else {
                        // Escape with braces can have any number of hex digits: \\u{XXXXXXX}
                        escapeEnd = cur + 2;
                        while (Character.digit(value.charAt(escapeEnd), 0x10) >= 0) {
                            escapeEnd++;
                        }
                        hexDigits = value.substring(cur + 2, escapeEnd);
                        escapeEnd++;
                    }
                    result.append(Character.toChars(Integer.parseInt(hexDigits, 0x10)));
                    cur = escapeEnd - 1;
                    break;
                default:
                    // TODO(tbreisacher): Add a warning because the user probably
                    // intended to type an escape sequence.
                    result.append(c);
                    break;
            }
            start = cur + 1;
            cur = value.indexOf('\\', start);
        }
        // skip the trailing quote.
        result.append(value, start, templateLiteral ? value.length() : value.length() - 1);

        return result.toString();
    }

    static double normalizeNumber(LiteralToken token) {
        String value = token.value;
        int length = value.length();
        Preconditions.checkState(length > 0);
        Preconditions.checkState(value.charAt(0) != '-'
                && value.charAt(0) != '+');
        if (value.charAt(0) == '.') {
            return Double.valueOf('0' + value);
        } else if (value.charAt(0) == '0' && length > 1) {
            switch (value.charAt(1)) {
                case '.':
                case 'e':
                case 'E':
                    return Double.valueOf(value);
                case 'b':
                case 'B': {
// TAJS: warnings should be emitted by another tool
//                    features = features.require(FeatureSet.Feature.BINARY_LITERALS);
//                    if (!isSupportedForInputLanguageMode(FeatureSet.Feature.BINARY_LITERALS)) {
//                        errorReporter.warning(BINARY_NUMBER_LITERAL_WARNING,
//                                sourceName,
//                                lineno(token.location.start), charno(token.location.start));
//                    }
                    double v = 0;
                    int c = 1;
                    while (++c < length) {
                        v = (v * 2) + binarydigit(value.charAt(c));
                    }
                    return v;
                }
                case 'o':
                case 'O': {
// TAJS: warnings should be emitted by another tool
//                    features = features.require(FeatureSet.Feature.OCTAL_LITERALS);
//                    if (!isSupportedForInputLanguageMode(FeatureSet.Feature.OCTAL_LITERALS)) {
//                        errorReporter.warning(OCTAL_NUMBER_LITERAL_WARNING,
//                                sourceName,
//                                lineno(token.location.start), charno(token.location.start));
//                    }
                    double v = 0;
                    int c = 1;
                    while (++c < length) {
                        v = (v * 8) + octaldigit(value.charAt(c));
                    }
                    return v;
                }
                case 'x':
                case 'X': {
                    double v = 0;
                    int c = 1;
                    while (++c < length) {
                        v = (v * 0x10) + hexdigit(value.charAt(c));
                    }
                    return v;
                }
                case '0': case '1': case '2': case '3':
                case '4': case '5': case '6': case '7':
// TODO: TAJS assumes strict-mode octals right here
// TAJS: warnings should be emitted by another tool
//                    if (!inStrictContext()) {
//                        double v = 0;
//                        int c = 0;
//                        while (++c < length) {
//                            char digit = value.charAt(c);
//                            if (isOctalDigit(digit)) {
//                                v = (v * 8) + octaldigit(digit);
//                            } else {
//                                errorReporter.error(INVALID_OCTAL_DIGIT, sourceName,
//                                        lineno(location.start), charno(location.start));
//                                return 0;
//                            }
//                        }
//                        errorReporter.warning(INVALID_ES5_STRICT_OCTAL, sourceName,
//                                lineno(location.start), charno(location.start));
//                        return v;
//                    } else {
//                        // TODO(tbreisacher): Make this an error instead of a warning.
//                        errorReporter.warning(INVALID_ES5_STRICT_OCTAL, sourceName,
//                                lineno(location.start), charno(location.start));
//                        return Double.valueOf(value);
//                    }
                    return Double.valueOf(value);
                case '8': case '9':
// TAJS: warnings should be emitted by another tool
//                    errorReporter.error(INVALID_OCTAL_DIGIT, sourceName,
//                            lineno(location.start), charno(location.start));
                    return 0;
                default:
                    throw new IllegalStateException(
                            "Unexpected character in number literal: " + value.charAt(1));
            }
        } else {
            return Double.valueOf(value);
        }
    }

    private static int binarydigit(char c) {
        if (c >= '0' && c <= '1') {
            return (c - '0');
        }
        throw new AnalysisException("unexpected: " + c);
    }

    private static boolean isOctalDigit(char c) {
        return c >= '0' && c <= '7';
    }

    private static int octaldigit(char c) {
        if (isOctalDigit(c)) {
            return (c - '0');
        }
        throw new AnalysisException("unexpected: " + c);
    }

    private static int hexdigit(char c) {
        switch (c) {
            case '0':
                return 0;
            case '1':
                return 1;
            case '2':
                return 2;
            case '3':
                return 3;
            case '4':
                return 4;
            case '5':
                return 5;
            case '6':
                return 6;
            case '7':
                return 7;
            case '8':
                return 8;
            case '9':
                return 9;
            case 'a':
            case 'A':
                return 10;
            case 'b':
            case 'B':
                return 11;
            case 'c':
            case 'C':
                return 12;
            case 'd':
            case 'D':
                return 13;
            case 'e':
            case 'E':
                return 14;
            case 'f':
            case 'F':
                return 15;
        }
        throw new AnalysisException("unexpected: " + c);
    }

    static boolean isAssignment(BinaryOperatorTree tree) {
        switch (tree.operator.type) {
            case EQUAL:
            case BAR_EQUAL:
            case CARET_EQUAL:
            case AMPERSAND_EQUAL:
            case LEFT_SHIFT_EQUAL:
            case RIGHT_SHIFT_EQUAL:
            case UNSIGNED_RIGHT_SHIFT_EQUAL:
            case PLUS_EQUAL:
            case MINUS_EQUAL:
            case STAR_EQUAL:
            case SLASH_EQUAL:
            case PERCENT_EQUAL:
                return true;
            default:
                return false;
        }
    }

    public static String normalizeString(LiteralToken literalToken) {
        return normalizeString(literalToken, false);
    }
}
