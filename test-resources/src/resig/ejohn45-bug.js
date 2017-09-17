function makeArray(array) {
    return Array().slice.call(array);
}
function makeArrayThroughF(array) {
    return Array().slice.call(array);
}

function f() {
    return makeArrayThroughF(arguments);
}

TAJS_assertEquals(3, [1, 2, 3].slice().length);
TAJS_assertEquals(3, Array().slice.call([1, 2, 3]).length);
TAJS_assertEquals(3, makeArray([1, 2, 3]).length);
TAJS_assertEquals(3, f(1, 2, 3).length);
