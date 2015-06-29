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

/**
 * @fileoverview Assert we match ES3 and Safari.
 */
// Should be 0.
TAJS_dumpValue(Array.prototype.length)

// Should all be 1
TAJS_dumpValue(Array.length)
TAJS_dumpValue(Array.prototype.concat.length)
TAJS_dumpValue(Array.prototype.join.length)
TAJS_dumpValue(Array.prototype.push.length)
TAJS_dumpValue(Array.prototype.unshift.length)
TAJS_dumpValue(Boolean.length)
TAJS_dumpValue(Error.length)
TAJS_dumpValue(EvalError.length)
TAJS_dumpValue(Function.length)
TAJS_dumpValue(Function.prototype.call.length)
TAJS_dumpValue(Number.length)
TAJS_dumpValue(Number.prototype.toExponential.length)
TAJS_dumpValue(Number.prototype.toFixed.length)
TAJS_dumpValue(Number.prototype.toPrecision.length)
TAJS_dumpValue(Object.length)
TAJS_dumpValue(RangeError.length)
TAJS_dumpValue(ReferenceError.length)
TAJS_dumpValue(String.fromCharCode.length)
TAJS_dumpValue(String.length)
TAJS_dumpValue(String.prototype.concat.length)
TAJS_dumpValue(String.prototype.indexOf.length)
TAJS_dumpValue(String.prototype.lastIndexOf.length)
TAJS_dumpValue(SyntaxError.length)
TAJS_dumpValue(TypeError.length)

// Should be 2
TAJS_dumpValue(Array.prototype.slice.length)
TAJS_dumpValue(Array.prototype.splice.length)
TAJS_dumpValue(Date.prototype.setMonth.length)
TAJS_dumpValue(Date.prototype.setSeconds.length)
TAJS_dumpValue(Date.prototype.setUTCMonth.length)
TAJS_dumpValue(Date.prototype.setUTCSeconds.length)
TAJS_dumpValue(Function.prototype.apply.length)
TAJS_dumpValue(Math.max.length)
TAJS_dumpValue(Math.min.length)
TAJS_dumpValue(RegExp.length)
TAJS_dumpValue(String.prototype.slice.length)
TAJS_dumpValue(String.prototype.split.length)
TAJS_dumpValue(String.prototype.substr.length)
TAJS_dumpValue(String.prototype.substring.length)

// Should be 3.
TAJS_dumpValue(Date.prototype.setFullYear.length)
TAJS_dumpValue(Date.prototype.setMinutes.length)
TAJS_dumpValue(Date.prototype.setUTCFullYear.length)
TAJS_dumpValue(Date.prototype.setUTCMinutes.length)


// Should be 4.
TAJS_dumpValue(Date.prototype.setHours.length)
TAJS_dumpValue(Date.prototype.setUTCHours.length)

// Should be 7.
TAJS_dumpValue(Date.UTC.length)
TAJS_dumpValue(Date.length)
