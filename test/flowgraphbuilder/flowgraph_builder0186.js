var obj = {
    valueOf: function () {
        return false
    }
};

TAJS_assert(obj == false);
