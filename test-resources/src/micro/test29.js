var foo;
try { 
	x=null;
	y=x.a
} 
catch (e) {
	foo = 42;
}
TAJS_assert(foo===42);
