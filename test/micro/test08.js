function person() {
    glob = 234;
    this.age = 10;
}

function test() {
    return 42;
}
glob = 9;
var g = test();
var t = new person();
g = t.age;

assert(g == 10);
assert(glob == 234);
