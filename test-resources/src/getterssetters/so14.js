function Name(first) {
    TAJS_dumpValue(this)
    this.first = first;
}

Name.prototype = {
    get fullName() {
        TAJS_dumpValue(this)
        return this.first + " Doe";
    }
};
var name = new Name("John");
TAJS_dumpValue(name)
TAJS_dumpValue(Name.prototype)
TAJS_dumpObject(name)
TAJS_dumpObject(Name.prototype)
TAJS_assert(name.first === "John");
TAJS_dumpValue(name.fullName)
TAJS_assert(name.fullName === "John Doe");
