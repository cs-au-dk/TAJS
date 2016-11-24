function h() {

}
function f() {
    var slicedArguments = Array.prototype.slice.call(arguments, 1);
    var slicedSlicedArguments = Array.prototype.slice.call(slicedArguments);
    var newArgs = slicedArguments.concat(slicedSlicedArguments);
    return h.apply(undefined, newArgs);
};

f();
