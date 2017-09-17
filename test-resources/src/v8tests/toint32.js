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



// Should all be 0.
// FIXME: Fix toint321.js and uncomment all lines except the line below the next FIXME.
// TAJS_dumpValue(Infinity | 0);
// TAJS_dumpValue(-Infinity | 0);
// FIXME: Fix toint322.js and uncomment the line below.
// TAJS_dumpValue(NaN | 0);
// TAJS_dumpValue(0.0 | 0);
// TAJS_dumpValue(-0.0 | 0);

// TAJS_dumpValue(Number.MIN_VALUE | 0);
// TAJS_dumpValue(-Number.MIN_VALUE | 0);
// TAJS_dumpValue(0.1 | 0);
// TAJS_dumpValue(-0.1 | 0);

// Should be 1.
// TAJS_dumpValue(1 | 0);
// TAJS_dumpValue(1.1 | 0);
// Should be -1
// TAJS_dumpValue(-1 | 0);

// Should be 2147483647
// TAJS_dumpValue(2147483647 | 0);
// Should be -2147483648.
// TAJS_dumpValue(2147483648 | 0);
// Should be -2147483647
// TAJS_dumpValue(2147483649 | 0);

// Should be -1
// FIXME: Fix testtoint321.js and uncomment the line below.
// TAJS_dumpValue(4294967295 | 0);
// TODO: What is the expected result?
// TAJS_dumpValue(4294967296 | 0);
// Should be 1.
// FIXME: Fix testtoint321.js and uncomment the line below.
// TAJS_dumpValue(4294967297 | 0);

// Should be -2147483647
// FIXME: Fix testtoint321.js and uncomment the line below.
// TAJS_dumpValue(-2147483647 | 0);
// Should be -2147483648
// FIXME: Fix testtoint321.js and uncomment the line below.
// TAJS_dumpValue(-2147483648 | 0);
// Should be 2147483647
// FIXME: Fix testtoint321.js and uncomment the line below.
// TAJS_dumpValue(-2147483649 | 0);

// Should be 1
// FIXME: Fix testtoint321.js and uncomment the line below.
// TAJS_dumpValue(-4294967295 | 0);
// TODO: What is the expected result?
// TAJS_dumpValue(-4294967296 | 0);
// Should be -1
// FIXME: Fix testtoint321.js and uncomment the line below.
// TAJS_dumpValue(-4294967297 | 0);

// Should be -2147483648
// FIXME: Fix testtoint321.js and uncomment the line below.
// TAJS_dumpValue(2147483648.25 | 0);
// Should be -2147483648
// FIXME: Fix testtoint321.js and uncomment the line below.
// TAJS_dumpValue(2147483648.5 | 0);
// Should be -2147483648
// FIXME: Fix testtoint321.js and uncomment the line below.
// TAJS_dumpValue(2147483648.75 | 0);
// Should be -1
// FIXME: Fix testtoint321.js and uncomment the line below.
// TAJS_dumpValue(4294967295.25 | 0);
// Should be -1
// FIXME: Fix testtoint321.js and uncomment the line below.
// TAJS_dumpValue(4294967295.5 | 0);
// Should be -1
// FIXME: Fix testtoint321.js and uncomment the line below.
// TAJS_dumpValue(4294967295.75 | 0);
// Should be -1294967296
// FIXME: Fix testtoint321.js and uncomment the line below.
// TAJS_dumpValue(3000000000.25 | 0);
// Should be -1294967296
// FIXME: Fix testtoint321.js and uncomment the line below.
// TAJS_dumpValue(3000000000.5 | 0);
// Should be -1294967296
// FIXME: Fix testtoint321.js and uncomment the line below.
// TAJS_dumpValue(3000000000.75 | 0);

// Should be -2147483648
// FIXME: Fix testtoint321.js and uncomment the line below.
// TAJS_dumpValue(-2147483648.25 | 0);
// Should be -2147483648
// FIXME: Fix testtoint321.js and uncomment the line below.
// TAJS_dumpValue(-2147483648.5 | 0);
// Should be -2147483648
// FIXME: Fix testtoint321.js and uncomment the line below.
// TAJS_dumpValue(-2147483648.75 | 0);
// Should be 1
// FIXME: Fix testtoint321.js and uncomment the line below.
// TAJS_dumpValue(-4294967295.25 | 0);
// Should be 1
// FIXME: Fix testtoint321.js and uncomment the line below.
// TAJS_dumpValue(-4294967295.5 | 0);
// Should be 1
// FIXME: Fix testtoint321.js and uncomment the line below.
// TAJS_dumpValue(-4294967295.75 | 0);
// Should be 1294967296
// FIXME: Fix testtoint321.js and uncomment the line below.
// TAJS_dumpValue(-3000000000.25 | 0);
// Should be 1294967296
// FIXME: Fix testtoint321.js and uncomment the line below.
// TAJS_dumpValue(-3000000000.5 | 0);
// Should be 1294967296
// FIXME: Fix testtoint321.js and uncomment the line below.
// TAJS_dumpValue(-3000000000.75 | 0);
