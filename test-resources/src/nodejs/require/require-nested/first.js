exports.prop1 = 42;
exports.prop2 = 42;
require('./second');
TAJS_assert(exports.prop1, 'isMaybeOtherThanUndef');
TAJS_assert(exports.prop2, 'isMaybeOtherThanUndef');
