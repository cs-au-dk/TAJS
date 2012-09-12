f = function() {
    a = {};
};

g = function() {
    b = a;
    f();
    c = b.p + 1;
};

f();
a.p = 42;
g();
dumpValue(a.p); 
dumpValue(c); 
