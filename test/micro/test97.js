var x = "foo";

function g() {
	throw new Error; // crashes WALA
	x = 1;
}

try{
	  TAJS_dumpValue("HERE1");
  g();
  TAJS_dumpValue("HERE2");
} catch(ex) {
  TAJS_dumpValue("HERE3");
}
TAJS_dumpValue(x);
