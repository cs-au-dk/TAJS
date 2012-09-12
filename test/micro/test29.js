var foo;
try { 
	x=null;
	y=x.a
} 
catch (e) {
	foo = 42;
}
assert(foo===42);
