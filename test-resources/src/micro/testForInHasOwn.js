var name, prop, obj = {a: 'a', b: 'b'};
for (name in obj) {
    if (obj.hasOwnProperty(name)) {
        prop = obj[name];
        TAJS_assert(prop, 'isMaybeSingleStr', true);
    }
}
