var o = {
    get x() {

    },
    set x(v) {

    }
}
var v = Object.getOwnPropertyDescriptor(o, 'x');
TAJS_dumpValue(v.get);
TAJS_dumpValue(v.set)