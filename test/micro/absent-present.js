TAJS_addContextSensitivity(extend, 'obj');
function extend(obj) {
    for (var i = 0; i<3; i++)
        for (name in arguments[i]) {
            obj[name] = extend(obj[name] ? obj[name] : {}, arguments[i][name]);
        }
}
String.prototype.xyz = undefined;
extend({
    p1: {
        p2: ""
    },
    p3: {
        "p-4": ""
    }
});
