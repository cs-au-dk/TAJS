var set_triggered = false;
var catch_triggered = false;

var o = {};
o.__defineGetter__('length', function () {
    set_triggered = true;
});
try {
    Array.prototype.splice.call(o, 0, 0, 'x');
} catch (e) {
    // "Cannot set property length of #<Object> which has only a getter."
    catch_triggered = true;
}
TAJS_assert(set_triggered);
TAJS_assert(!catch_triggered /* unsound */);
