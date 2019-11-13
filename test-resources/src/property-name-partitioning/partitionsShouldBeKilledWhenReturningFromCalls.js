var obj = {foo: 'foo', bar: 'bar'};
function readProperty(x) {
    return obj[x];
}

var propName1 = Math.random() ? 'foo' : 'bar';
var propName2 = Math.random() ? 'bar' : 'foo';

var obj2 = {};
var pname = readProperty(propName1);
var value = readProperty(propName2);
obj2[pname] = value;
obj2.foo;
obj2.bar;