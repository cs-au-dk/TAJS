function g() {
	throw new Error;
}

try{
  g();
  dumpValue("HERE1"); // should not appear
} catch(ex) {
  dumpValue("HERE2");
}
dumpValue("HERE3");
