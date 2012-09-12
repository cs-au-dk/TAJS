bar = 0;
if (bar == 33)
  bar = 777;
else
  bar = "jolly";

foo = bar + 1234;

dumpValue(foo);
assert(foo === "jolly1234");


if (Math.random() == 33)
  bar = 777;
else
  bar = "jolly";

foo = bar + 1234;

dumpValue(foo);
assert(foo === "jolly1234");

