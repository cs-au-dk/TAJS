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

package dk.brics.tajs.util;

import java.util.Random;
import java.util.regex.Pattern;

/**
 * Miscellaneous string operations.
 */
public class Strings {

    static private final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    static private Random rnd;

    static private final Pattern NUMBER =
            Pattern.compile("\\-?(([0-9]+(\\.[0-9]*)?|\\.[0-9]+)([eE][-+][0-9]+)?|Infinity)|NaN"); // TODO: check that this over-approximates the possible output of Number.toString

    static private final Pattern IDENTIFIER =
            Pattern.compile("[\\p{Lu}\\p{Ll}\\p{Lt}\\p{Lm}\\p{Lo}\\p{Nl}$_]" +
                    "[\\p{Lu}\\p{Ll}\\p{Lt}\\p{Lm}\\p{Lo}\\p{Nl}\\p{Mn}\\p{Mc}\\p{Nd}\\p{Pc}}]*");

    static private final Pattern IDENTIFIERPARTS =
            Pattern.compile("[\\p{Lu}\\p{Ll}\\p{Lt}\\p{Lm}\\p{Lo}\\p{Nl}\\p{Mn}\\p{Mc}\\p{Nd}\\p{Pc}}]*");

    // TODO: what about Unicode escape sequences in IDENTIFIER and IDENTIFIERPARTS?

    static {
        reset();
    }

    private Strings() {
    }

    /**
     * Resets the random string generator.
     */
    public static void reset() {
        rnd = new Random(0);
    }

    /**
     * Escapes special characters in the given string.
     * Special characters are all Unicode chars except 0x20-0x7e but including \, ", {, and }.
     */
    public static String escape(String s) {
        if (s == null)
            return null;
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '"':
                    b.append("\\\"");
                    break;
                case '\\':
                    b.append("\\\\");
                    break;
                case '\b':
                    b.append("\\b");
                    break;
                case '\t':
                    b.append("\\t");
                    break;
                case '\n':
                    b.append("\\n");
                    break;
                case '\r':
                    b.append("\\r");
                    break;
                case '\f':
                    b.append("\\f");
                    break;
                case '<':
                    b.append("\\<");
                    break;
                case '>':
                    b.append("\\>");
                    break;
                case '{':
                    b.append("\\{");
                    break;
                case '}':
                    b.append("\\}");
                    break;
                default:
                    if (c >= 0x20 && c <= 0x7e)
                        b.append(c);
                    else {
                        b.append("\\u");
                        String t = Integer.toHexString(c & 0xffff);
                        for (int j = 0; j + t.length() < 4; j++)
                            b.append('0');
                        b.append(t);
                    }
            }
        }
        return b.toString();
    }

    /**
     * Escapes quotes and special characters in the (Javascript source) string.
     */
    public static String escapeSource(String s) {
        if (s == null)
            return null;
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '"':
                    b.append("\\\"");
                    break;
                default:
                    if (c >= 0x20 && c <= 0x7e)
                        b.append(c);
                    else {
                        b.append("\\u");
                        String t = Integer.toHexString(c & 0xffff);
                        for (int j = 0; j + t.length() < 4; j++)
                            b.append('0');
                        b.append(t);
                    }
            }
        }
        return b.toString();
    }

    /**
     * Checks whether the given string is a valid array index.
     */
    public static boolean isArrayIndex(String s) {
        if (s.isEmpty())
            return false;
        long val = 0L;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c < '0' || c > '9')
                return false;
            val = 10 * val + Character.digit(c, 10);
            if (val > 2 * (long) Integer.MAX_VALUE)
                return false;
        }
        return true;
    }

    /**
     * Checks whether the given string is a valid double, including Infinity, -Infinity, and NaN.
     * This is an over-approximation of the strings that may appear when number values are coerced to string values.
     */
    public static boolean isNumber(String s) {
        return NUMBER.matcher(s).matches();
    }

    /**
     * Checks whether the given string is a reserved name.
     */
    private static boolean isReservedName(String s) {
        if (s.isEmpty())
            return false;
        switch (s.charAt(0)) {
            case 'a':
                return s.equals("abstract");
            case 'b':
                return s.equals("boolean") || s.equals("break") || s.equals("byte");
            case 'c':
                return s.equals("case") || s.equals("catch") || s.equals("char") || s.equals("class")
                        || s.equals("const") || s.equals("continue");
            case 'd':
                return s.equals("debugger") || s.equals("default") || s.equals("delete") || s.equals("do")
                        || s.equals("double");
            case 'e':
                return s.equals("else") || s.equals("enum") || s.equals("export") || s.equals("extends");
            case 'f':
                return s.equals("false") || s.equals("final") || s.equals("finally") || s.equals("float")
                        || s.equals("for") || s.equals("function");
            case 'g':
                return s.equals("goto");
            case 'i':
                return s.equals("if") || s.equals("implements") || s.equals("import") || s.equals("in")
                        || s.equals("instanceof") || s.equals("int") || s.equals("interface");
            case 'l':
                return s.equals("long");
            case 'n':
                return s.equals("native") || s.equals("new") || s.equals("null");
            case 'p':
                return s.equals("package") || s.equals("private") || s.equals("protected") || s.equals("public");
            case 'r':
                return s.equals("return");
            case 's':
                return s.equals("short") || s.equals("static") || s.equals("super") || s.equals("switch")
                        || s.equals("synchronized");
            case 't':
                return s.equals("this") || s.equals("throw") || s.equals("throws") || s.equals("transient")
                        || s.equals("true") || s.equals("try") || s.equals("typeof");
            case 'v':
                return s.equals("var") || s.equals("void") || s.equals("volatile");
            case 'w':
                return s.equals("while") || s.equals("with");
            default:
                return false;
        }
    }

    /**
     * Checks whether the given string is a nonempty prefix of a reserved name.
     */
    private static boolean isNonEmptyPrefixOfReservedName(String s) {
        if (s.isEmpty())
            return false;
        switch (s.charAt(0)) {
            case 'a':
                return "abstract".startsWith(s);
            case 'b':
                return "boolean".startsWith(s) || "break".startsWith(s) || "byte".startsWith(s);
            case 'c':
                return "case".startsWith(s) || "catch".startsWith(s) || "char".startsWith(s) || "class".startsWith(s)
                        || "const".startsWith(s) || "continue".startsWith(s);
            case 'd':
                return "debugger".startsWith(s) || "default".startsWith(s) || "delete".startsWith(s) || "do".startsWith(s)
                        || "double".startsWith(s);
            case 'e':
                return "else".startsWith(s) || "enum".startsWith(s) || "export".startsWith(s) || "extends".startsWith(s);
            case 'f':
                return "false".startsWith(s) || "final".startsWith(s) || "finally".startsWith(s) || "float".startsWith(s)
                        || "for".startsWith(s) || "function".startsWith(s);
            case 'g':
                return "goto".startsWith(s);
            case 'i':
                return "if".startsWith(s) || "implements".startsWith(s) || "import".startsWith(s) || "in".startsWith(s)
                        || "instanceof".startsWith(s) || "int".startsWith(s) || "interface".startsWith(s);
            case 'l':
                return "long".startsWith(s);
            case 'n':
                return "native".startsWith(s) || "new".startsWith(s) || "null".startsWith(s);
            case 'p':
                return "package".startsWith(s) || "private".startsWith(s) || "protected".startsWith(s) || "public".startsWith(s);
            case 'r':
                return "return".startsWith(s);
            case 's':
                return "short".startsWith(s) || "static".startsWith(s) || "super".startsWith(s) || "switch".startsWith(s)
                        || "synchronized".startsWith(s);
            case 't':
                return "this".startsWith(s) || "throw".startsWith(s) || "throws".startsWith(s) || "transient".startsWith(s)
                        || "true".startsWith(s) || "try".startsWith(s) || "typeof".startsWith(s);
            case 'v':
                return "var".startsWith(s) || "void".startsWith(s) || "volatile".startsWith(s);
            case 'w':
                return "while".startsWith(s) || "with".startsWith(s);
            default:
                return false;
        }
    }

    /**
     * Checks whether the given string is a valid identifier (where reserved words are not valid).
     */
    public static boolean isIdentifier(String s) {
        return !isReservedName(s) && IDENTIFIER.matcher(s).matches();
    }

    /**
     * Checks whether the given string is an identifier and not a prefix of a reserved name.
     */
    public static boolean isIdentifierAndNotPrefixOfReservedName(String s) {
        return !isNonEmptyPrefixOfReservedName(s) && IDENTIFIER.matcher(s).matches();
    }

    /**
     * Checks whether the given string consists of valid identifier parts.
     */
    public static boolean isIdentifierParts(String s) {
        return IDENTIFIERPARTS.matcher(s).matches();
    }

    /**
     * Generates a random string of the given length containing digits and letters.
     */
    public static String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }
}
