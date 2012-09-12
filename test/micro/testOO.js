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
dumpValue(jimmy.getName());
dumpValue(jimmy.say("hello"));
dumpValue(jimmy.slap());
dumpValue(jimmy.slap());
dumpValue(jimmy.slap());
//dumpValue(jimmy.slap());

// a subclass
function Singer(name) {
  Person.call(this,name);
}
dumpObject(Singer.prototype);
Singer.prototype = new Person();
dumpObject(Singer.prototype);
Singer.prototype.Super = Person.prototype;
dumpObject(Singer.prototype);
Singer.prototype.sing = function(song) { return this.say(song+" tra-la-la"); };
dumpObject(Singer.prototype);

var jerry = new Singer("jerry");
dumpObject(jerry); // should have 'name' and 'count'
dumpObject(Singer);
dumpObject(Singer.prototype);
dumpObject(Person);
dumpObject(Person.prototype);
dumpValue(jerry.getName()); // should be (approximation of) "jerry"
dumpValue(jerry.say("hello"));
dumpValue(jerry.sing("a song"));
dumpValue(jerry.slap());
//dumpValue(jerry.slap());
//dumpValue(jerry.slap());
//dumpValue(jerry.slap());


// another subclass
function Rockstar(name) {
  Singer.call(this,name);
}
Rockstar.prototype = new Singer();
Rockstar.prototype.Super = Singer.prototype;
Rockstar.prototype.say = function(words) { return this.Super.say("Gee, "+words); };

var marilyn = new Rockstar("marilyn");
dumpValue(marilyn.getName());
dumpValue(marilyn.say("I'm bad"));
dumpValue(marilyn.sing("a hard day's night"));
dumpValue(marilyn.slap());
//dumpValue(marilyn.slap());
//dumpValue(marilyn.slap());
//dumpValue(marilyn.slap());
