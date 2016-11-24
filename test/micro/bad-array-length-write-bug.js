var result = false;
var arrProtoLen;
function callbackfn(val, idx, obj) {
    result = (obj.length === 2);
}

try {
    arrProtoLen = Array.prototype.length;
    Array.prototype.length = 0;
    [12, 11].forEach(callbackfn);
} finally {
    Array.prototype.length = arrProtoLen;
}