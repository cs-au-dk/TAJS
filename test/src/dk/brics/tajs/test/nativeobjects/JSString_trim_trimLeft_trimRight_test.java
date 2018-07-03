package dk.brics.tajs.test.nativeobjects;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.test.Misc;
import org.junit.Before;
import org.junit.Test;

public class JSString_trim_trimLeft_trimRight_test {

    @Before
    public void before() {
        Main.reset();
        Options.get().enableTest();
    }

    @Test
    public void trimSimple() throws Exception {
        Misc.runSource("TAJS_assertEquals('foo',' foo '.trim());");
    }

    @Test
    public void trimLeftSimple() throws Exception {
        Misc.runSource("TAJS_assertEquals('foo ',' foo '.trimLeft());");
    }

    @Test
    public void trimRightSimple() throws Exception {
        Misc.runSource("TAJS_assertEquals(' foo',' foo '.trimRight());");
    }

    @Test
    public void trimFuzzyStrings() {
        Misc.runSource(
                "var anyStr = TAJS_make('AnyStr');",
                "var prefix = 'x' + anyStr;",
                "var anyStr_prefix = Math.random()? anyStr: prefix;",
                "TAJS_assert(anyStr.trim(), 'isMaybeStrIdentifier');",
                "TAJS_assert(prefix.trim(), 'isMaybeStrPrefix');",
                "TAJS_assert(anyStr_prefix.trim(), 'isMaybeStrIdentifier||isMaybeStrPrefix');",
                "TAJS_assert(anyStr_prefix.trim(), 'isMaybeStrOtherIdentifierParts||isMaybeStrOther');",
                "var num_obj = String.prototype.trim.call(Math.random()? 1: {});",
                "TAJS_assert(num_obj, 'isMaybeStrOther||isMaybeStrUInt');",
                "TAJS_assert(num_obj, 'isMaybeStrOther||isMaybeStrUInt');");
    }

    @Test
    public void trimOnUndefined() throws Exception {
        Options.get().enableDoNotExpectOrdinaryExit();
        Misc.runSource("String.prototype.trim.call();", // should throw exception
                "TAJS_assert(false);");
    }

    @Test
    public void trimOnUnicode() throws Exception {
        Misc.runSource("TAJS_dumpValue('\\uFEFF\\xA0'.trim());");
    }

    @Test
    public void trimCallUndefined() {
        Misc.runSource(
                "try { ",
                "  String.prototype.trim.call(undefined);",
                "  TAJS_assert(false);",
                "} catch (e) { ",
                "  TAJS_assert(e instanceof TypeError);",
                "}"
        );
    }

    @Test
    public void trimCallNull() {
        Misc.runSource(
                "try { ",
                "  String.prototype.trim.call(null);",
                "  TAJS_assert(false);",
                "} catch (e) { ",
                "  TAJS_assert(e instanceof TypeError);",
                "}"
        );
    }

    @Test
    public void trimCallString() {
        Misc.runSource(
                "try { ",
                "  TAJS_assertEquals(String.prototype.trim.call(' x '), 'x');",
                "} catch (e) { ",
                "  TAJS_assert(false);",
                "}"
        );
    }
}
