function g() {
	throw new Error;
}

try{
  g();
  TAJS_dumpValue("HERE1"); // should not appear
} catch(ex) {
  TAJS_dumpValue("HERE2");
}
TAJS_dumpValue("HERE3");
