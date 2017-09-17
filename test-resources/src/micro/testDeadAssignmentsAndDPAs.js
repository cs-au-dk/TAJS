var o1 = {p1: 42, 1: 42};
var o2 = {p2: 42, 2: 42};
var o3 = {};
var o4 = {};
var o5 = {};
var o6 = {};
var a1 = [];
var a2 = [];
var a3 = [];
var a4 = [];

var s = Math.random() ? '' : 'x'
var n = Math.random() ? 0 : 1

// explicit property read
o1.p1;
o1[1];

// default property read
o2[s];
o1[n];

// explicit define, unread (should produce warning!)
o3.p3 = 42;
o3[3] = 42;

// default define, unread (should produce warning!)
o4[s] = 42;
o4[n] = 42;

// default define, explicit read
o5[s] = 42;
o5[n] = 42;
o5.p5;
o5[5];

// default define, default read
o6[s] = 42;
o6[n] = 42;
o6[s];
o6[n];

// numeric only read write for array
a1[n] = 42;
a1[n];

// numeric only write for array (should produce warning!)
a2[n] = 42;

// numeric only read write for array, nested
a3[n] = [];
a3[n][n] = 42;
a3[n][n];

// numeric only write for array, nested (should produce warning!)
a4[n] = [];
a4[n][n] = 42;