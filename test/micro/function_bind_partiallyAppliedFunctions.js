//Taken from https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Function/bind
function list() {
    return Array.prototype.slice.call(arguments);
}

var list1 = list(1, 2, 3); // [1, 2, 3]
TAJS_assert(list1[0] === 1);
TAJS_assert(list1[1] === 2);
TAJS_assert(list1[2] === 3);

// Create a function with a preset leading argument
var leadingThirtysevenList = list.bind(undefined, 37);

var list2 = leadingThirtysevenList();
TAJS_assert(list2[0] === 37);
// [37]

var list3 = leadingThirtysevenList(1, 2, 3);
// [37, 1, 2, 3]

// The assertions below does not work, since TAJS also says that the values can be undefined.
// Otherwise the correct numbers are stored at the correct indexes.
// TAJS_assert(list3[0] === 37);
// TAJS_assert(list3[1] === 1);
// TAJS_assert(list3[2] === 2);
// TAJS_assert(list3[3] === 3);
