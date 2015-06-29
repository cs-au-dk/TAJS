var foo = 42;
foo = foo + 1;

bar = 87; 
bar = bar + 2;
this.baz = 117; 
baz = baz + 3;

TAJS_dumpValue(this.foo);
TAJS_dumpValue(this.bar);
TAJS_dumpValue(this.baz);
