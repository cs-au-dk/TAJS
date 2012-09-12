var x = "foo";

function g() {
	throw new Error; // crashes WALA
	x = 1;
}

try{
	  dumpValue("HERE1");
  g();
  dumpValue("HERE2");
} catch(ex) {
  dumpValue("HERE3");
}
dumpValue(x);
