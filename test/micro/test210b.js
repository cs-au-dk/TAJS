var trueBranchTaken = false;
var falseBranchTaken = false;
var catchBranchTaken = false;
try {
    function f(v) {
        return v.p;
    }

    f({p: true})
    if (f()) {
        trueBranchTaken = true;
    } else {
        falseBranchTaken = true
    }
} catch (e) {
    catchBranchTaken = true;
}
TAJS_assert(!trueBranchTaken);
TAJS_assert(!falseBranchTaken);
TAJS_assert(catchBranchTaken);