var o;

// create an object with null as prototype
o = Object.create(null);


o = {};
// is equivalent to:
o = Object.create(Object.prototype);


// Example where we create an object with a couple of
// sample properties. (Note that the second parameter
// maps keys to *property descriptors*.)
o = Object.create(Object.prototype, {
    // foo is a regular 'value property'
    foo: {
        writable: true,
        configurable: true,
        value: 'hello'
    },
    // bar is a getter-and-setter (accessor) property
    bar: {
        configurable: false,
        get: function() { return 10; },
        set: function(value) {
            console.log('Setting `o.bar` to', value);
        }
        /* with ES5 Accessors our code can look like this
         get function() { return 10; },
         set function(value) {
         console.log('Setting `o.bar` to', value);
         } */
    }
});


function Constructor() {}
o = new Constructor();
// is equivalent to:
o = Object.create(Constructor.prototype);
// Of course, if there is actual initialization code
// in the Constructor function,
// the Object.create() cannot reflect it


// Create a new object whose prototype is a new, empty
// object and add a single property 'p', with value 42.
o = Object.create({}, { p: { value: 42 } });

// by default properties ARE NOT writable,
// enumerable or configurable:
o.p = 24;
o.p;
// 42

o.q = 12;
for (var prop in o) {
    console.log(prop);
}
// 'q'

delete o.p;
// false

// to specify an ES3 property
o2 = Object.create({}, {
    p: {
        value: 42,
        writable: true,
        enumerable: true,
        configurable: true
    }
});