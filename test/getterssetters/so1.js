function Name(first, last) {
    this.first = first;
    this.last = last;
}

Name.prototype = {
    get fullName() {
        TAJS_dumpValue(this)
        return this.first + " " + this.last;
    },

    set fullName(name) {
        TAJS_dumpValue(name)
        var names = name.split(" ");
        this.first = names[0];
        this.last = names[1];
    }
};
var name = new Name("foo", "bar");
TAJS_dumpObject(name)
TAJS_dumpObject(Name.prototype)
TAJS_dumpValue(name)
TAJS_assert(name.first === "foo");
TAJS_assert(name.last === "bar");
TAJS_dumpValue(name.fullName)
TAJS_assert(name.fullName === "foo bar");
name.fullName = "baz qux";
TAJS_dumpObject(name)
TAJS_assert(name.first === "baz");
TAJS_assert(name.last === "qux");
