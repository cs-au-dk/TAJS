exports.myfun = function(x, y) {
    return x + y;
};

exports.mystr = "hello";

exports.myobj = { myfield: "world", myfield2: "again", myfield3: true, myfield4: 111 };

exports.myfun2 = function(x) {};

exports.myfun3 = function() {
    return Math.random() ? Math.random() ? new Date() : "bla" : new Error();
}

exports.myarray1 = [1,2,3];
exports.myarray2 = [1,2,3];
exports.mytuple = ["a", 2, false];

if (!Math.random()) {
    exports.myfun = function() {
        return null;
    };
    exports.mystr = 7;
    exports.myobj.myfield = false;
    exports.myobj.myfield2 = true;
    exports.myobj.myfield3 = 42;
    exports.myobj.myfield4 = new Number(222);
    exports.myobj = 117;
    exports.myarray1 = {"2": 2};
    exports.myarray2 = {"2": 2};
    exports.mytuple = ["a", "b", "c"];
}
if (!Math.random()) {
    exports.myfun = null;
    exports.myobj.myfield4 = "foo";
    exports.mytuple = {};
}
