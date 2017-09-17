function base() {
    var pattern = "^((?:\\\\.|[\\w-]|[^\\x00-\\xa0])+)";
    var replaced = pattern.replace("w", "w*");
    var regexp = new RegExp(replaced);
    TAJS_assert(replaced === regexp.source);
}

function variant1() {
    var pattern = "^((?:\\\\.|[\\w*-]|[^\\x00-\\xa0])+)";
    var regexp = new RegExp(pattern);
    TAJS_assert(pattern === regexp.source);
}

function variant2() {
    var pattern = "\\\\.\\w";
    var replaced = pattern.replace("w", "w*");
    // TAJS_dumpValue(pattern + "    -->    " + replaced);
    TAJS_assert(replaced === '\\\\.\\w*');
}

function variant3() {
    var pattern = "\\w";
    var replaced = pattern.replace("w", "w*");
    // TAJS_dumpValue(pattern + "    -->    " + replaced);
    TAJS_assert(replaced === '\\w*');
}

function variant4() {
    var pattern = "\\x";
    var replaced = pattern.replace("x", "x*");
    // TAJS_dumpValue(pattern + "    -->    " + replaced);
    TAJS_assert(replaced === '\\x*');
}

function variant5() {
    var pattern = "x";
    var replaced = pattern.replace("y", "z");
    TAJS_assert(replaced === 'x');
}

function variant6() {
    var pattern = "x";
    var replaced = pattern.replace("x", "y");
    TAJS_assert(replaced === 'y');
}

base();
variant1();
variant2();
variant3();
variant4();
variant5();
variant6();