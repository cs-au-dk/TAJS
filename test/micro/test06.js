bar = 0;
if (bar == 33)
  bar = 777;
else
  bar = "jolly";

foo = bar + 1234;

TAJS_dumpValue(foo);
TAJS_assert(foo === "jolly1234");


if (Math.random() == 33)
  bar = 777;
else
  bar = "jolly";

foo = bar + 1234;

TAJS_dumpValue(foo);
TAJS_assert(foo === "jolly1234", 'isMaybeAnyBool');

