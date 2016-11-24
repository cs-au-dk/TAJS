var a = [];

function f(o) {
    a.push(o);
}

f('foo');
f('bar');
var v = a[0];
eval(v + "()");
