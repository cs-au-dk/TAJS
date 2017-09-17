// Copyright 2008 the V8 project authors. All rights reserved.
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are
// met:
//
//     * Redistributions of source code must retain the above copyright
//       notice, this list of conditions and the following disclaimer.
//     * Redistributions in binary form must reproduce the above
//       copyright notice, this list of conditions and the following
//       disclaimer in the documentation and/or other materials provided
//       with the distribution.
//     * Neither the name of Google Inc. nor the names of its
//       contributors may be used to endorse or promote products derived
//       from this software without specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
// "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
// LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
// A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
// OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
// SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
// LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
// DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
// THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
// (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
// OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

// A reduced test case from Acid3 test 95.
// When an object is assigned to an array length field,
// it is converted to a number.

// Should be 2147483648 with full precision. 
var a = [];
a.length = 2147483648;

TAJS_dumpValue(typeof a.length);
TAJS_dumpValue(a.length);

// Should be 2147483648 with full precision.
var b = [];
b.length = "2147483648";

TAJS_dumpValue(typeof b.length);
TAJS_dumpValue(b.length);

// Should be 0 with full precision.
var c = [];
c.length = null;

TAJS_dumpValue(typeof c.length);
TAJS_dumpValue(c.length);

// Should be 0 with full precision.
var d = [];
d.length = false;

TAJS_dumpValue(typeof d.length);
TAJS_dumpValue(d.length);

// Should be 1 with full precision.
var e = [];
e.length = true;

TAJS_dumpValue(typeof e.length);
TAJS_dumpValue(e.length);

// Should be 42 with full precision.
var f = [];
f.length = {valueOf : function() { return 42; }};

TAJS_dumpValue(typeof f.length);
TAJS_dumpValue(f.length);

// Should be 42 with full precision.
var g = [];
g.length = {toString : function() { return 42; }};

TAJS_dumpValue(typeof g.length);
TAJS_dumpValue(g.length);

// Test invalid values
try {
  var h = [];
  h.length = 'abc';
} catch (e) {}

try {
  var i = [];
  i.length = undefined;
} catch (e) {}

try {
  var j = [];
  i.length = {};
} catch (e) {}

try {
  var k = [];
  k.length = -1;
} catch (e) {}

try {
  var l = [];
  l.length = {valueOf: function() { throw new Error(); }};
} catch (e) {}

