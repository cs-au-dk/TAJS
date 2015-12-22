package dk.brics.tajs.js2flowgraph;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.parsing.Config;
import com.google.javascript.jscomp.parsing.parser.LiteralToken;
import com.google.javascript.jscomp.parsing.parser.Parser;
import com.google.javascript.jscomp.parsing.parser.trees.BinaryOperatorTree;
import dk.brics.tajs.util.AnalysisException;

/**
 * Utility methods extracted from <code>com.google.javascript.jscomp.parsing.ExampleIRFactory</code>.
 */
class ClosureASTUtil {

    private final Parser.Config.Mode mode;

    ClosureASTUtil(Parser.Config.Mode mode) {
        this.mode = mode;
    }

    static String normalizeString(LiteralToken token) {
        String value = token.value;
        int start = 1; // skip the leading quote
        int cur = value.indexOf('\\');
        if (cur == -1) {
            // short circuit no escapes.
            return value.substring(1, value.length() - 1);
        }
        StringBuilder result = new StringBuilder();
        while (cur != -1) {
            if (cur - start > 0) {
                result.append(value.substring(start, cur));
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
                    // line continuation, skip the line break
                    break;
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                    char next1 = value.charAt(cur + 1);

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
                            hexdigit(value.charAt(cur + 1)) * 16
                                    + hexdigit(value.charAt(cur + 2))));
                    cur += 2;
                    break;
                case 'u':
                    result.append((char) (
                            hexdigit(value.charAt(cur + 1)) * 16 * 16 * 16
                                    + hexdigit(value.charAt(cur + 2)) * 16 * 16
                                    + hexdigit(value.charAt(cur + 3)) * 16
                                    + hexdigit(value.charAt(cur + 4))));
                    cur += 4;
                    break;
                default:
                    result.append(c);
                    break;
            }
            start = cur + 1;
            cur = value.indexOf('\\', start);
        }
        // skip the trailing quote.
        result.append(value.substring(start, value.length() - 1));

        return result.toString();
    }

    double normalizeNumber(LiteralToken token) {
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
                    return Double.valueOf(value);
                case 'b':
                case 'B': {
                    long v = 0;
                    int c = 1;
                    while (++c < length) {
                        v = (v * 2) + binarydigit(value.charAt(c));
                    }
                    return v;
                }
                case 'o':
                case 'O': {
                    long v = 0;
                    int c = 1;
                    while (++c < length) {
                        v = (v * 8) + octaldigit(value.charAt(c));
                    }
                    return v;
                }
                case 'x':
                case 'X': {
                    long v = 0;
                    int c = 1;
                    while (++c < length) {
                        v = (v * 16) + hexdigit(value.charAt(c));
                    }
                    return v;
                }
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                    if (!inStrictContext()) {
                        long v = 0;
                        int c = 0;
                        while (++c < length) {
                            v = (v * 8) + octaldigit(value.charAt(c));
                        }
                        return v;
                    } else {
                        return Double.valueOf(value);
                    }
                default:
                    return 0;
            }
        } else {
            return Double.valueOf(value);
        }
    }

    private boolean inStrictContext() {
        return convertModeType(mode) == Config.LanguageMode.ECMASCRIPT5_STRICT
                || convertModeType(mode) == Config.LanguageMode.ECMASCRIPT6_STRICT;
    }

    private static Config.LanguageMode convertModeType(
            Parser.Config.Mode mode) {
        switch (mode) {
            case ES3:
                return Config.LanguageMode.ECMASCRIPT3;
            case ES5:
                return Config.LanguageMode.ECMASCRIPT5;
            case ES5_STRICT:
                return Config.LanguageMode.ECMASCRIPT5_STRICT;
            case ES6:
                return Config.LanguageMode.ECMASCRIPT6;
            case ES6_STRICT:
                return Config.LanguageMode.ECMASCRIPT6_STRICT;
            default:
                throw new AnalysisException("Unexpected enum: " + mode);
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
}
