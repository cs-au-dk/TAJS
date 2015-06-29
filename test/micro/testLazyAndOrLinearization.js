var o = Math.random() ? undefined : { u: Math.random() === 0, t: true, f: false};
var u = Math.random() === 0;

if (u) {
    o.u || o.u;
}
if (u) {
    u || o.f || o.u;
}
if (u) {
    u || o.u || o.u;
}

if (u) {
    o.u && o.u;
}
if (u) {
    u && o.t && o.u;
}
if (u) {
    u && o.u && o.u;
}
