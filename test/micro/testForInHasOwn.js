var name, prop, obj = {a: 'a', b: 'b'};
for (name in obj) {
    if (obj.hasOwnProperty(name)) {
        prop = obj[name];
        assertSingleStr(prop); // a or b, not proto stuff
    }
}
