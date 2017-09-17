function MyClass() {
    SuperClass.call(this);
    OtherSuperClass.call(this);
}

// inherit one class
MyClass.prototype = Object.create(SuperClass.prototype);
// mixin another
Object.assign(MyClass.prototype, OtherSuperClass.prototype);

MyClass.prototype.myMethod = function() {
    // do a thing
};