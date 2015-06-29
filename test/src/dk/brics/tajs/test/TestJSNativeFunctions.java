package dk.brics.tajs.test;

import dk.brics.tajs.test.nativeobjects.*;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        JSRegExp_exec_test.class,
        JSRegExp_test_test.class,
        JSString_charAt_charCodeAt_test.class,
        JSString_concat_test.class,
        JSString_indexof_lastindexof_Test.class,
        JSString_fromCharCode.class,
        JSString_localeCompare_test.class,
        JSString_match_test.class,
        JSString_replace_test.class,
        JSString_search_test.class,
        JSString_slice_test.class,
        JSString_splitTest.class,
        JSString_substr_test.class,
        JSString_substring_test.class,
        JSString_toLowerUpperCase_test.class,
        JSArray_concat_test.class,
        JSArray_slice_test.class,
        JSArray_concatAndSlice_test.class
})
public class TestJSNativeFunctions {}
