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
dumpValue(Array.prototype.length)

// Should all be 1
dumpValue(Array.length)
dumpValue(Array.prototype.concat.length)
dumpValue(Array.prototype.join.length)
dumpValue(Array.prototype.push.length)
dumpValue(Array.prototype.unshift.length)
dumpValue(Boolean.length)
dumpValue(Error.length)
dumpValue(EvalError.length)
dumpValue(Function.length)
dumpValue(Function.prototype.call.length)
dumpValue(Number.length)
dumpValue(Number.prototype.toExponential.length)
dumpValue(Number.prototype.toFixed.length)
dumpValue(Number.prototype.toPrecision.length)
dumpValue(Object.length)
dumpValue(RangeError.length)
dumpValue(ReferenceError.length)
dumpValue(String.fromCharCode.length)
dumpValue(String.length)
dumpValue(String.prototype.concat.length)
dumpValue(String.prototype.indexOf.length)
dumpValue(String.prototype.lastIndexOf.length)
dumpValue(SyntaxError.length)
dumpValue(TypeError.length)

// Should be 2
dumpValue(Array.prototype.slice.length)
dumpValue(Array.prototype.splice.length)
dumpValue(Date.prototype.setMonth.length)
dumpValue(Date.prototype.setSeconds.length)
dumpValue(Date.prototype.setUTCMonth.length)
dumpValue(Date.prototype.setUTCSeconds.length)
dumpValue(Function.prototype.apply.length)
dumpValue(Math.max.length)
dumpValue(Math.min.length)
dumpValue(RegExp.length)
dumpValue(String.prototype.slice.length)
dumpValue(String.prototype.split.length)
dumpValue(String.prototype.substr.length)
dumpValue(String.prototype.substring.length)

// Should be 3.
dumpValue(Date.prototype.setFullYear.length)
dumpValue(Date.prototype.setMinutes.length)
dumpValue(Date.prototype.setUTCFullYear.length)
dumpValue(Date.prototype.setUTCMinutes.length)


// Should be 4.
dumpValue(Date.prototype.setHours.length)
dumpValue(Date.prototype.setUTCHours.length)

// Should be 7.
dumpValue(Date.UTC.length)
dumpValue(Date.length)
