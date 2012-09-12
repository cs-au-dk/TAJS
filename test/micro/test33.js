var foo = 42;
foo = foo + 1;

bar = 87; 
bar = bar + 2;
this.baz = 117; 
baz = baz + 3;

dumpValue(this.foo);
dumpValue(this.bar);
dumpValue(this.baz);
