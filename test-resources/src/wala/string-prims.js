
var stuff = new String("this is a string of words");

var words = stuff.split(" ");

var firstWord = words[0];

var firstWordUpper = firstWord.toUpperCase();

TAJS_dumpValue(stuff.valueOf());
TAJS_dumpObject(words);
TAJS_dumpValue(firstWord);
TAJS_dumpValue(firstWordUpper);
