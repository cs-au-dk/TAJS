function undef_in_two() {
  undef_in_one();
}
function undef_in_one() {
  undef();
}
function undef() {
  undefined;
}
function rec() {
  undef_in_two();
  rec();
}
function rec_in_one() {
  rec();
}
function toString() {
  rec();
}
undef_in_two();
toString();
rec_in_one();