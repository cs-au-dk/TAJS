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

TAJS_assert(g == 10);
TAJS_assert(glob == 234);
