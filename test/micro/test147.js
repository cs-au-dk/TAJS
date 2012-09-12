t = {i : 21}

foo = function() {
    t.i = t.i - 1;
    if (t.i > 0) {
        bar();
    }
}

bar = function() {
    foo()
}

foo();

dumpValue(t.i)
    
    