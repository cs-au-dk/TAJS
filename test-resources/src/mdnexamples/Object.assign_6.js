var obj = Object.create({ foo: 1 }, { // foo is on obj's prototype chain.
    bar: {
        value: 2  // bar is a non-enumerable property.
    },
    baz: {
        value: 3,
        enumerable: true  // baz is an own enumerable property.
    }
});

var copy = Object.assign({}, obj);
console.log(copy); // { baz: 3 }