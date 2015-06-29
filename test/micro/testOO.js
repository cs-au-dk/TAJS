//// fancy object stuff simulating inheritance
// a class
function Person(name) {
  this.name = name;
  this.count = 0;
}
Person.prototype.getName = function() { return this.name; };
Person.prototype.say = function(stuff) { return(stuff); };
Person.prototype.slap =
  function() {
  if(this.count<2)
    {this.count++;return("argh")}
  else
    {this.count=0;return("ouch");}
};

var jimmy = new Person("jimmy");
TAJS_dumpValue(jimmy.getName());
TAJS_dumpValue(jimmy.say("hello"));
TAJS_dumpValue(jimmy.slap());
TAJS_dumpValue(jimmy.slap());
TAJS_dumpValue(jimmy.slap());
//TAJS_dumpValue(jimmy.slap());

// a subclass
function Singer(name) {
  Person.call(this,name);
}
TAJS_dumpObject(Singer.prototype);
Singer.prototype = new Person();
TAJS_dumpObject(Singer.prototype);
Singer.prototype.Super = Person.prototype;
TAJS_dumpObject(Singer.prototype);
Singer.prototype.sing = function(song) { return this.say(song+" tra-la-la"); };
TAJS_dumpObject(Singer.prototype);

var jerry = new Singer("jerry");
TAJS_dumpObject(jerry); // should have 'name' and 'count'
TAJS_dumpObject(Singer);
TAJS_dumpObject(Singer.prototype);
TAJS_dumpObject(Person);
TAJS_dumpObject(Person.prototype);
TAJS_dumpValue(jerry.getName()); // should be (approximation of) "jerry"
TAJS_dumpValue(jerry.say("hello"));
TAJS_dumpValue(jerry.sing("a song"));
TAJS_dumpValue(jerry.slap());
//TAJS_dumpValue(jerry.slap());
//TAJS_dumpValue(jerry.slap());
//TAJS_dumpValue(jerry.slap());


// another subclass
function Rockstar(name) {
  Singer.call(this,name);
}
Rockstar.prototype = new Singer();
Rockstar.prototype.Super = Singer.prototype;
Rockstar.prototype.say = function(words) { return this.Super.say("Gee, "+words); };

var marilyn = new Rockstar("marilyn");
TAJS_dumpValue(marilyn.getName());
TAJS_dumpValue(marilyn.say("I'm bad"));
TAJS_dumpValue(marilyn.sing("a hard day's night"));
TAJS_dumpValue(marilyn.slap());
//TAJS_dumpValue(marilyn.slap());
//TAJS_dumpValue(marilyn.slap());
//TAJS_dumpValue(marilyn.slap());
