function Name(first) {
    this.first = first;
}

Name.prototype = {
    set fullName(name) {
        TAJS_dumpValue(this)
        TAJS_dumpValue(name)
        var names = name.split(" ");
        this.first = names[0];
    }
};
var name = new Name("John");
name.fullName = "Joe Doe";
TAJS_dumpObject(name)
TAJS_dumpValue(name.first);
TAJS_assert(name.first === "Joe");
