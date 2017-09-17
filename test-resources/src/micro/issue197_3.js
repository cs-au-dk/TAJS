function orig() {
    var characterEncoding = "(?:\\\\.|[\\w-]|[^\\x00-\\xa0])+";
    var TAG = new RegExp("^(" + characterEncoding.replace("w", "w*") + ")");
    var x = TAG.exec("a.google");
    return x;
}

function variant1() {
    var characterEncoding = "(?:\\\\.|[\\w*-]|[^\\x00-\\xa0])+";
    var TAG = new RegExp("^(" + characterEncoding + ")");
    var x = TAG.exec("a.google");
    return x;
}

function variant2() {
    var TAG = new RegExp("^((?:\\\\.|[\\w*-]|[^\\x00-\\xa0])+)");
    var x = TAG.exec("a.google");
    return x;
}

function variant3() {
    var TAG = /^((?:\\.|[\w*-]|[^\x00-\xa0])+)/;
    var x = TAG.exec("a.google");
    return x;
}

TAJS_dumpValue(orig());
TAJS_dumpValue(variant1());
TAJS_dumpValue(variant2());
TAJS_dumpValue(variant3());
TAJS_assert(orig(), 'isNotNull');
TAJS_assert(variant1(), 'isNotNull');
TAJS_assert(variant2(),'isNotNull');
TAJS_assert(variant3(), 'isNotNull');