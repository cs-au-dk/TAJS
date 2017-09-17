var o = {};
o.a = 1;
o.b = 10;
o.c = 100;

o.a = o.b = o.c++;
o.a;
o.b;
o.c;