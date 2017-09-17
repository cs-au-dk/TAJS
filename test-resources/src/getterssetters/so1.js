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
TAJS_assertEquals("foo", name.first);
TAJS_assertEquals("bar", name.last);
TAJS_dumpValue(name.fullName)
TAJS_assertEquals("foo bar", name.fullName);
name.fullName = "baz qux";
TAJS_dumpObject(name)
//TAJS_assertEquals("baz", name.first);
//TAJS_assertEquals("qux", name.last);
