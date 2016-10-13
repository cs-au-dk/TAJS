package dk.brics.tajs.test;

import dk.brics.tajs.test.nativeobjects.JSArray_concatAndSlice_test;
import dk.brics.tajs.test.nativeobjects.JSArray_concat_test;
import dk.brics.tajs.test.nativeobjects.JSArray_slice_test;
import dk.brics.tajs.test.nativeobjects.JSObject_defineProperty_test;
import dk.brics.tajs.test.nativeobjects.JSObject_getOwnPropertyDescriptor_test;
import dk.brics.tajs.test.nativeobjects.JSRegExp_exec_test;
import dk.brics.tajs.test.nativeobjects.JSRegExp_test_test;
import dk.brics.tajs.test.nativeobjects.JSString_charAt_charCodeAt_test;
import dk.brics.tajs.test.nativeobjects.JSString_concat_test;
import dk.brics.tajs.test.nativeobjects.JSString_fromCharCode;
import dk.brics.tajs.test.nativeobjects.JSString_indexof_lastindexof_Test;
import dk.brics.tajs.test.nativeobjects.JSString_localeCompare_test;
import dk.brics.tajs.test.nativeobjects.JSString_match_test;
import dk.brics.tajs.test.nativeobjects.JSString_replace_test;
import dk.brics.tajs.test.nativeobjects.JSString_search_test;
import dk.brics.tajs.test.nativeobjects.JSString_slice_test;
import dk.brics.tajs.test.nativeobjects.JSString_splitTest;
import dk.brics.tajs.test.nativeobjects.JSString_substr_test;
import dk.brics.tajs.test.nativeobjects.JSString_substring_test;
import dk.brics.tajs.test.nativeobjects.JSString_toLowerUpperCase_test;
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
        JSArray_concatAndSlice_test.class,
        JSObject_defineProperty_test.class,
        JSObject_getOwnPropertyDescriptor_test.class
})
public class TestJSNativeFunctions {}
