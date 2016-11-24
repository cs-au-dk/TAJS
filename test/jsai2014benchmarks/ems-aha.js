
function ourprint(x) {print(x);}

this['Module'] = Module = {};

// The environment setup code below is customized to use Module.
// *** Environment setup code ***

var ENVIRONMENT_IS_NODE = ENVIRONMENT_IS_WORKER = ENVIRONMENT_IS_WEB = false;
var ENVIRONMENT_IS_SHELL = true;
Module['print'] = ourprint;
if (typeof printErr != 'undefined') Module['printErr'] = printErr; // not present in v8 or older sm
// Polyfill over SpiderMonkey/V8 differences
if (typeof read != 'undefined') {
  Module['read'] = read;
} else {
  Module['read'] = function(f) { snarf(f) };
}

function globalEval(x) {
  //eval.call(null, x);
}

if (!Module['load'] == 'undefined' && Module['read']) {
  Module['load'] = function(f) {
    globalEval(Module['read'](f));
  };
}
if (!Module['print']) {
  Module['print'] = function(){};
}
if (!Module['printErr']) {
  Module['printErr'] = Module['print'];
}
if (!Module['arguments']) {
  Module['arguments'] = [];
}
// *** Environment setup code ***
// Closure helpers
Module.print = Module['print'];
Module.printErr = Module['printErr'];
// Callbacks
if (!Module['preRun']) Module['preRun'] = [];
if (!Module['postRun']) Module['postRun'] = [];
// === Auto-generated preamble library stuff ===
//========================================
// Runtime code shared with compiler
//========================================
var Runtime = {
  stackSave: function () {
    return STACKTOP;
  },
  stackRestore: function (stackTop) {
    STACKTOP = stackTop;
  },
  forceAlign: function (target, quantum) {
    quantum = quantum || 4;
    if (quantum == 1) return target;
    if (isNumber(target) && isNumber(quantum)) {
      return Math.ceil(target/quantum)*quantum;
    } else if (isNumber(quantum) && isPowerOfTwo(quantum)) {
      var logg = log2(quantum);
      return '((((' +target + ')+' + (quantum-1) + ')>>' + logg + ')<<' + logg + ')';
    }
    return 'Math.ceil((' + target + ')/' + quantum + ')*' + quantum;
  },
  isNumberType: function (type) {
    return type in Runtime.INT_TYPES || type in Runtime.FLOAT_TYPES;
  },
  isPointerType: function isPointerType(type) {
  return type[type.length-1] == '*';
},
  isStructType: function isStructType(type) {
  if (isPointerType(type)) return false;
  if (/^\[\d+\ x\ (.*)\]/.test(type)) return true; // [15 x ?] blocks. Like structs
  if (/<?{ ?[^}]* ?}>?/.test(type)) return true; // { i32, i8 } etc. - anonymous struct types
  // See comment in isStructPointerType()
  return type[0] == '%';
},
  INT_TYPES: {"i1":0,"i8":0,"i16":0,"i32":0,"i64":0},
  FLOAT_TYPES: {"float":0,"double":0},
  or64: function (x, y) {
    var l = (x | 0) | (y | 0);
    var h = (Math.round(x / 4294967296) | Math.round(y / 4294967296)) * 4294967296;
    return l + h;
  },
  and64: function (x, y) {
    var l = (x | 0) & (y | 0);
    var h = (Math.round(x / 4294967296) & Math.round(y / 4294967296)) * 4294967296;
    return l + h;
  },
  xor64: function (x, y) {
    var l = (x | 0) ^ (y | 0);
    var h = (Math.round(x / 4294967296) ^ Math.round(y / 4294967296)) * 4294967296;
    return l + h;
  },
  getNativeTypeSize: function (type, quantumSize) {
    if (Runtime.QUANTUM_SIZE == 1) return 1;
    var size = {
      '%i1': 1,
      '%i8': 1,
      '%i16': 2,
      '%i32': 4,
      '%i64': 8,
      "%float": 4,
      "%double": 8
    }['%'+type]; // add '%' since float and double confuse Closure compiler as keys, and also spidermonkey as a compiler will remove 's from '_i8' etc
    if (!size) {
      if (type.charAt(type.length-1) == '*') {
        size = Runtime.QUANTUM_SIZE; // A pointer
      } else if (type[0] == 'i') {
        var bits = parseInt(type.substr(1));
        assert(bits % 8 == 0);
        size = bits/8;
      }
    }
    return size;
  },
  getNativeFieldSize: function (type) {
    return Math.max(Runtime.getNativeTypeSize(type), Runtime.QUANTUM_SIZE);
  },
  dedup: function dedup(items, ident) {
  var seen = {};
  if (ident) {
    return items.filter(function(item) {
      if (seen[item[ident]]) return false;
      seen[item[ident]] = true;
      return true;
    });
  } else {
    return items.filter(function(item) {
      if (seen[item]) return false;
      seen[item] = true;
      return true;
    });
  }
},
  set: function set() {
  var args = typeof arguments[0] === 'object' ? arguments[0] : arguments;
  var ret = {};
  for (var i = 0; i < args.length; i++) {
    ret[args[i]] = 0;
  }
  return ret;
},
  calculateStructAlignment: function calculateStructAlignment(type) {
    type.flatSize = 0;
    type.alignSize = 0;
    var diffs = [];
    var prev = -1;
    type.flatIndexes = type.fields.map(function(field) {
      var size, alignSize;
      if (Runtime.isNumberType(field) || Runtime.isPointerType(field)) {
        size = Runtime.getNativeTypeSize(field); // pack char; char; in structs, also char[X]s.
        alignSize = size;
      } else if (Runtime.isStructType(field)) {
        size = Types.types[field].flatSize;
        alignSize = Types.types[field].alignSize;
      } else if (field[0] == 'b') {
        // bN, large number field, like a [N x i8]
        size = field.substr(1)|0;
        alignSize = 1;
      } else {
        throw 'Unclear type in struct: ' + field + ', in ' + type.name_ + ' :: ' + dump(Types.types[type.name_]);
      }
      alignSize = type.packed ? 1 : Math.min(alignSize, Runtime.QUANTUM_SIZE);
      type.alignSize = Math.max(type.alignSize, alignSize);
      var curr = Runtime.alignMemory(type.flatSize, alignSize); // if necessary, place this on aligned memory
      type.flatSize = curr + size;
      if (prev >= 0) {
        diffs.push(curr-prev);
      }
      prev = curr;
      return curr;
    });
    type.flatSize = Runtime.alignMemory(type.flatSize, type.alignSize);
    if (diffs.length == 0) {
      type.flatFactor = type.flatSize;
    } else if (Runtime.dedup(diffs).length == 1) {
      type.flatFactor = diffs[0];
    }
    type.needsFlattening = (type.flatFactor != 1);
    return type.flatIndexes;
  },
  generateStructInfo: function (struct, typeName, offset) {
    var type, alignment;
    if (typeName) {
      offset = offset || 0;
      type = (typeof Types === 'undefined' ? Runtime.typeInfo : Types.types)[typeName];
      if (!type) return null;
      if (type.fields.length != struct.length) {
        printErr('Number of named fields must match the type for ' + typeName + ': possibly duplicate struct names. Cannot return structInfo');
        return null;
      }
      alignment = type.flatIndexes;
    } else {
      var type = { fields: struct.map(function(item) { return item[0] }) };
      alignment = Runtime.calculateStructAlignment(type);
    }
    var ret = {
      __size__: type.flatSize
    };
    if (typeName) {
      struct.forEach(function(item, i) {
        if (typeof item === 'string') {
          ret[item] = alignment[i] + offset;
        } else {
          // embedded struct
          var key;
          for (var k in item) key = k;
          ret[key] = Runtime.generateStructInfo(item[key], type.fields[i], alignment[i]);
        }
      });
    } else {
      struct.forEach(function(item, i) {
        ret[item[1]] = alignment[i];
      });
    }
    return ret;
  },
  dynCall: function (sig, ptr, args) {
    if (args && args.length) {
      assert(args.length == sig.length-1);
      return FUNCTION_TABLE[ptr].apply(null, args);
    } else {
      assert(sig.length == 1);
      return FUNCTION_TABLE[ptr]();
    }
  },
  addFunction: function (func, sig) {
    //assert(sig); // TODO: support asm
    var table = FUNCTION_TABLE; // TODO: support asm
    var ret = table.length;
    table.push(func);
    table.push(0);
    return ret;
  },
  removeFunction: function (index) {
    var table = FUNCTION_TABLE; // TODO: support asm
    table[index] = null;
  },
  warnOnce: function (text) {
    if (!Runtime.warnOnce.shown) Runtime.warnOnce.shown = {};
    if (!Runtime.warnOnce.shown[text]) {
      Runtime.warnOnce.shown[text] = 1;
      Module.printErr(text);
    }
  },
  funcWrappers: {},
  getFuncWrapper: function (func, sig) {
    assert(sig);
    if (!Runtime.funcWrappers[func]) {
      Runtime.funcWrappers[func] = function() {
        Runtime.dynCall(sig, func, arguments);
      };
    }
    return Runtime.funcWrappers[func];
  },
  UTF8Processor: function () {
    var buffer = [];
    var needed = 0;
    this.processCChar = function (code) {
      code = code & 0xff;
      if (needed) {
        buffer.push(code);
        needed--;
      }
      if (buffer.length == 0) {
        if (code < 128) return String.fromCharCode(code);
        buffer.push(code);
        if (code > 191 && code < 224) {
          needed = 1;
        } else {
          needed = 2;
        }
        return '';
      }
      if (needed > 0) return '';
      var c1 = buffer[0];
      var c2 = buffer[1];
      var c3 = buffer[2];
      var ret;
      if (c1 > 191 && c1 < 224) {
        ret = String.fromCharCode(((c1 & 31) << 6) | (c2 & 63));
      } else {
        ret = String.fromCharCode(((c1 & 15) << 12) | ((c2 & 63) << 6) | (c3 & 63));
      }
      buffer.length = 0;
      return ret;
    }
    this.processJSString = function(string) {
      string = unescape(encodeURIComponent(string));
      var ret = [];
      for (var i = 0; i < string.length; i++) {
        ret.push(string.charCodeAt(i));
      }
      return ret;
    }
  },
  stackAlloc: function (size) { var ret = STACKTOP;STACKTOP = (STACKTOP + size)|0;STACKTOP = ((((STACKTOP)+3)>>2)<<2);assert((STACKTOP|0) < (STACK_MAX|0)); return ret; },
  staticAlloc: function (size) { var ret = STATICTOP;STATICTOP = (STATICTOP + size)|0;STATICTOP = ((((STATICTOP)+3)>>2)<<2); if (STATICTOP >= TOTAL_MEMORY) enlargeMemory();; return ret; },
  alignMemory: function (size,quantum) { var ret = size = Math.ceil((size)/(quantum ? quantum : 4))*(quantum ? quantum : 4); return ret; },
  makeBigInt: function (low,high,unsigned) { var ret = (unsigned ? (((low)>>>(0))+(((high)>>>(0))*4294967296)) : (((low)>>>(0))+(((high)|(0))*4294967296))); return ret; },
  QUANTUM_SIZE: 4,
  __dummy__: 0
}
//========================================
// Runtime essentials
//========================================
var __THREW__ = 0; // Used in checking for thrown exceptions.
var setjmpId = 1; // Used in setjmp/longjmp
var setjmpLabels = {};
var ABORT = false;
var undef = 0;
// tempInt is used for 32-bit signed values or smaller. tempBigInt is used
// for 32-bit unsigned values or more than 32 bits. TODO: audit all uses of tempInt
var tempValue, tempInt, tempBigInt, tempInt2, tempBigInt2, tempPair, tempBigIntI, tempBigIntR, tempBigIntS, tempBigIntP, tempBigIntD;
var tempI64, tempI64b;
var tempRet0, tempRet1, tempRet2, tempRet3, tempRet4, tempRet5, tempRet6, tempRet7, tempRet8, tempRet9;
function abort(text) {
  //Module.print(text + ':\n' + (new Error).stack);
  print(text + ': ERROR');
  ABORT = true;
  throw "Assertion: " + text;
}
function assert(condition, text) {
  if (!condition) {
    abort('Assertion failed: ' + text);
  }
}
var globalScope = this;
// C calling interface. A convenient way to call C functions (in C files, or
// defined with extern "C").
//
// Note: LLVM optimizations can inline and remove functions, after which you will not be
//       able to call them. Closure can also do so. To avoid that, add your function to
//       the exports using something like
//
//         -s EXPORTED_FUNCTIONS='["_main", "_myfunc"]'
//
// @param ident      The name of the C function (note that C++ functions will be name-mangled - use extern "C")
// @param returnType The return type of the function, one of the JS types 'number', 'string' or 'array' (use 'number' for any C pointer, and
//                   'array' for JavaScript arrays and typed arrays).
// @param argTypes   An array of the types of arguments for the function (if there are no arguments, this can be ommitted). Types are as in returnType,
//                   except that 'array' is not possible (there is no way for us to know the length of the array)
// @param args       An array of the arguments to the function, as native JS values (as in returnType)
//                   Note that string arguments will be stored on the stack (the JS string will become a C string on the stack).
// @return           The return value, as a native JS value (as in returnType)
function ccall(ident, returnType, argTypes, args) {
  return ccallFunc(getCFunc(ident), returnType, argTypes, args);
}
Module["ccall"] = ccall;
// Returns the C function with a specified identifier (for C++, you need to do manual name mangling)
function getCFunc(ident) {
  try {
    var func = globalScope['Module']['_' + ident]; // closure exported function
    if (!func) func = eval('_' + ident); // explicit lookup
  } catch(e) {
  }
  assert(func, 'Cannot call unknown function ' + ident + ' (perhaps LLVM optimizations or closure removed it?)');
  return func;
}
// Internal function that does a C call using a function, not an identifier
function ccallFunc(func, returnType, argTypes, args) {
  var stack = 0;
  function toC(value, type) {
    if (type == 'string') {
      if (value === null || value === undefined || value === 0) return 0; // null string
      if (!stack) stack = Runtime.stackSave();
      var ret = Runtime.stackAlloc(value.length+1);
      writeStringToMemory(value, ret);
      return ret;
    } else if (type == 'array') {
      if (!stack) stack = Runtime.stackSave();
      var ret = Runtime.stackAlloc(value.length);
      writeArrayToMemory(value, ret);
      return ret;
    }
    return value;
  }
  function fromC(value, type) {
    if (type == 'string') {
      return Pointer_stringify(value);
    }
    assert(type != 'array');
    return value;
  }
  var i = 0;
  var cArgs = args ? args.map(function(arg) {
    return toC(arg, argTypes[i++]);
  }) : [];
  var ret = fromC(func.apply(null, cArgs), returnType);
  if (stack) Runtime.stackRestore(stack);
  return ret;
}
// Returns a native JS wrapper for a C function. This is similar to ccall, but
// returns a function you can call repeatedly in a normal way. For example:
//
//   var my_function = cwrap('my_c_function', 'number', ['number', 'number']);
//   alert(my_function(5, 22));
//   alert(my_function(99, 12));
//
function cwrap(ident, returnType, argTypes) {
  var func = getCFunc(ident);
  return function() {
    return ccallFunc(func, returnType, argTypes, Array.prototype.slice.call(arguments));
  }
}
Module["cwrap"] = cwrap;
// Sets a value in memory in a dynamic way at run-time. Uses the
// type data. This is the same as makeSetValue, except that
// makeSetValue is done at compile-time and generates the needed
// code then, whereas this function picks the right code at
// run-time.
// Note that setValue and getValue only do *aligned* writes and reads!
// Note that ccall uses JS types as for defining types, while setValue and
// getValue need LLVM types ('i8', 'i32') - this is a lower-level operation
function setValue(ptr, value, type, noSafe) {
  type = type || 'i8';
  if (type.charAt(type.length-1) === '*') type = 'i32'; // pointers are 32-bit
    switch(type) {
      case 'i1': HEAP8[(ptr)]=value; break;
      case 'i8': HEAP8[(ptr)]=value; break;
      case 'i16': HEAP16[((ptr)>>1)]=value; break;
      case 'i32': HEAP32[((ptr)>>2)]=value; break;
      case 'i64': (tempI64 = [value>>>0,Math.min(Math.floor((value)/4294967296), 4294967295)>>>0],HEAP32[((ptr)>>2)]=tempI64[0],HEAP32[(((ptr)+(4))>>2)]=tempI64[1]); break;
      case 'float': HEAPF32[((ptr)>>2)]=value; break;
      case 'double': (HEAPF64[(tempDoublePtr)>>3]=value,HEAP32[((ptr)>>2)]=HEAP32[((tempDoublePtr)>>2)],HEAP32[(((ptr)+(4))>>2)]=HEAP32[(((tempDoublePtr)+(4))>>2)]); break;
      default: abort('invalid type for setValue: ' + type);
    }
}
Module['setValue'] = setValue;
// Parallel to setValue.
function getValue(ptr, type, noSafe) {
  type = type || 'i8';
  if (type.charAt(type.length-1) === '*') type = 'i32'; // pointers are 32-bit
    switch(type) {
      case 'i1': return HEAP8[(ptr)];
      case 'i8': return HEAP8[(ptr)];
      case 'i16': return HEAP16[((ptr)>>1)];
      case 'i32': return HEAP32[((ptr)>>2)];
      case 'i64': return HEAP32[((ptr)>>2)];
      case 'float': return HEAPF32[((ptr)>>2)];
      case 'double': return (HEAP32[((tempDoublePtr)>>2)]=HEAP32[((ptr)>>2)],HEAP32[(((tempDoublePtr)+(4))>>2)]=HEAP32[(((ptr)+(4))>>2)],HEAPF64[(tempDoublePtr)>>3]);
      default: abort('invalid type for setValue: ' + type);
    }
  return null;
}
Module['getValue'] = getValue;
var ALLOC_NORMAL = 0; // Tries to use _malloc()
var ALLOC_STACK = 1; // Lives for the duration of the current function call
var ALLOC_STATIC = 2; // Cannot be freed
var ALLOC_NONE = 3; // Do not allocate
Module['ALLOC_NORMAL'] = ALLOC_NORMAL;
Module['ALLOC_STACK'] = ALLOC_STACK;
Module['ALLOC_STATIC'] = ALLOC_STATIC;
Module['ALLOC_NONE'] = ALLOC_NONE;
// allocate(): This is for internal use. You can use it yourself as well, but the interface
//             is a little tricky (see docs right below). The reason is that it is optimized
//             for multiple syntaxes to save space in generated code. So you should
//             normally not use allocate(), and instead allocate memory using _malloc(),
//             initialize it with setValue(), and so forth.
// @slab: An array of data, or a number. If a number, then the size of the block to allocate,
//        in *bytes* (note that this is sometimes confusing: the next parameter does not
//        affect this!)
// @types: Either an array of types, one for each byte (or 0 if no type at that position),
//         or a single type which is used for the entire block. This only matters if there
//         is initial data - if @slab is a number, then this does not matter at all and is
//         ignored.
// @allocator: How to allocate memory, see ALLOC_*
function allocate(slab, types, allocator, ptr) {
  var zeroinit, size;
  if (typeof slab === 'number') {
    zeroinit = true;
    size = slab;
  } else {
    zeroinit = false;
    size = slab.length;
  }
  var singleType = typeof types === 'string' ? types : null;
  var ret;
  if (allocator == ALLOC_NONE) {
    ret = ptr;
  } else {
    ret = [_malloc, Runtime.stackAlloc, Runtime.staticAlloc][allocator === undefined ? ALLOC_STATIC : allocator](Math.max(size, singleType ? 1 : types.length));
  }
  if (zeroinit) {
    var ptr = ret, stop;
    assert((ret & 3) == 0);
    stop = ret + (size & ~3);
    for (; ptr < stop; ptr += 4) {
      HEAP32[((ptr)>>2)]=0;
    }
    stop = ret + size;
    while (ptr < stop) {
      HEAP8[((ptr++)|0)]=0;
    }
    return ret;
  }
  if (singleType === 'i8') {
    HEAPU8.set(new Uint8Array(slab), ret);
    return ret;
  }
  var i = 0, type, typeSize, previousType;
  while (i < size) {
    var curr = slab[i];
    if (typeof curr === 'function') {
      curr = Runtime.getFunctionIndex(curr);
    }
    type = singleType || types[i];
    if (type === 0) {
      i++;
      continue;
    }
    assert(type, 'Must know what type to store in allocate!');
    if (type == 'i64') type = 'i32'; // special case: we have one i32 here, and one i32 later
    setValue(ret+i, curr, type);
    // no need to look up size unless type changes, so cache it
    if (previousType !== type) {
      typeSize = Runtime.getNativeTypeSize(type);
      previousType = type;
    }
    i += typeSize;
  }
  return ret;
}
Module['allocate'] = allocate;
function Pointer_stringify(ptr, /* optional */ length) {
  // Find the length, and check for UTF while doing so
  var hasUtf = false;
  var t;
  var i = 0;
  while (1) {
    t = HEAPU8[(((ptr)+(i))|0)];
    if (t >= 128) hasUtf = true;
    else if (t == 0 && !length) break;
    i++;
    if (length && i == length) break;
  }
  if (!length) length = i;
  var ret = '';
  if (!hasUtf) {
    var MAX_CHUNK = 1024; // split up into chunks, because .apply on a huge string can overflow the stack
    var curr;
    while (length > 0) {
      curr = String.fromCharCode.apply(String, HEAPU8.subarray(ptr, ptr + Math.min(length, MAX_CHUNK)));
      ret = ret ? ret + curr : curr;
      ptr += MAX_CHUNK;
      length -= MAX_CHUNK;
    }
    return ret;
  }
  var utf8 = new Runtime.UTF8Processor();
  for (i = 0; i < length; i++) {
    assert(ptr + i < TOTAL_MEMORY);
    t = HEAPU8[(((ptr)+(i))|0)];
    ret += utf8.processCChar(t);
  }
  return ret;
}
Module['Pointer_stringify'] = Pointer_stringify;
// Memory management
var PAGE_SIZE = 4096;
function alignMemoryPage(x) {
  return ((x+4095)>>12)<<12;
}
var HEAP;
var HEAP8, HEAPU8, HEAP16, HEAPU16, HEAP32, HEAPU32, HEAPF32, HEAPF64;
var STACK_ROOT, STACKTOP, STACK_MAX;
var STATICTOP;
function enlargeMemory() {
  abort('Cannot enlarge memory arrays. Either (1) compile with -s TOTAL_MEMORY=X with X higher than the current value, (2) compile with ALLOW_MEMORY_GROWTH which adjusts the size at runtime but prevents some optimizations, or (3) set Module.TOTAL_MEMORY before the program runs.');
}
var TOTAL_STACK = Module['TOTAL_STACK'] || 5242880;
var TOTAL_MEMORY = Module['TOTAL_MEMORY'] || 16777216;
var FAST_MEMORY = Module['FAST_MEMORY'] || 2097152;
// Initialize the runtime's memory
// check for full engine support (use string 'subarray' to avoid closure compiler confusion)
assert(!!Int32Array && !!Float64Array && !!(new Int32Array(1)['subarray']) && !!(new Int32Array(1)['set']),
       'Cannot fallback to non-typed array case: Code is too specialized');
var buffer = new ArrayBuffer(TOTAL_MEMORY);
HEAP8 = new Int8Array(buffer);
HEAP16 = new Int16Array(buffer);
HEAP32 = new Int32Array(buffer);
HEAPU8 = new Uint8Array(buffer);
HEAPU16 = new Uint16Array(buffer);
HEAPU32 = new Uint32Array(buffer);
HEAPF32 = new Float32Array(buffer);
HEAPF64 = new Float64Array(buffer);
// Endianness check (note: assumes compiler arch was little-endian)
HEAP32[0] = 255;
assert(HEAPU8[0] === 255 && HEAPU8[3] === 0, 'Typed arrays 2 must be run on a little-endian system');
Module['HEAP'] = HEAP;
Module['HEAP8'] = HEAP8;
Module['HEAP16'] = HEAP16;
Module['HEAP32'] = HEAP32;
Module['HEAPU8'] = HEAPU8;
Module['HEAPU16'] = HEAPU16;
Module['HEAPU32'] = HEAPU32;
Module['HEAPF32'] = HEAPF32;
Module['HEAPF64'] = HEAPF64;
STACK_ROOT = STACKTOP = Runtime.alignMemory(1);
STACK_MAX = TOTAL_STACK; // we lose a little stack here, but TOTAL_STACK is nice and round so use that as the max
var tempDoublePtr = Runtime.alignMemory(allocate(12, 'i8', ALLOC_STACK), 8);
assert(tempDoublePtr % 8 == 0);
function copyTempFloat(ptr) { // functions, because inlining this code increases code size too much
  HEAP8[tempDoublePtr] = HEAP8[ptr];
  HEAP8[tempDoublePtr+1] = HEAP8[ptr+1];
  HEAP8[tempDoublePtr+2] = HEAP8[ptr+2];
  HEAP8[tempDoublePtr+3] = HEAP8[ptr+3];
}
function copyTempDouble(ptr) {
  HEAP8[tempDoublePtr] = HEAP8[ptr];
  HEAP8[tempDoublePtr+1] = HEAP8[ptr+1];
  HEAP8[tempDoublePtr+2] = HEAP8[ptr+2];
  HEAP8[tempDoublePtr+3] = HEAP8[ptr+3];
  HEAP8[tempDoublePtr+4] = HEAP8[ptr+4];
  HEAP8[tempDoublePtr+5] = HEAP8[ptr+5];
  HEAP8[tempDoublePtr+6] = HEAP8[ptr+6];
  HEAP8[tempDoublePtr+7] = HEAP8[ptr+7];
}
STATICTOP = STACK_MAX;
assert(STATICTOP < TOTAL_MEMORY); // Stack must fit in TOTAL_MEMORY; allocations from here on may enlarge TOTAL_MEMORY
var nullString = allocate(intArrayFromString('(null)'), 'i8', ALLOC_STACK);
function callRuntimeCallbacks(callbacks) {
  while(callbacks.length > 0) {
    var callback = callbacks.shift();
    var func = callback.func;
    if (typeof func === 'number') {
      if (callback.arg === undefined) {
        Runtime.dynCall('v', func);
      } else {
        Runtime.dynCall('vi', func, [callback.arg]);
      }
    } else {
      func(callback.arg === undefined ? null : callback.arg);
    }
  }
}
var __ATINIT__ = []; // functions called during startup
var __ATMAIN__ = []; // functions called when main() is to be run
var __ATEXIT__ = []; // functions called during shutdown
function initRuntime() {
  callRuntimeCallbacks(__ATINIT__);
}
function preMain() {
  callRuntimeCallbacks(__ATMAIN__);
}
function exitRuntime() {
  callRuntimeCallbacks(__ATEXIT__);
}
// Tools
// This processes a JS string into a C-line array of numbers, 0-terminated.
// For LLVM-originating strings, see parser.js:parseLLVMString function
function intArrayFromString(stringy, dontAddNull, length /* optional */) {
  var ret = (new Runtime.UTF8Processor()).processJSString(stringy);
  if (length) {
    ret.length = length;
  }
  if (!dontAddNull) {
    ret.push(0);
  }
  return ret;
}
Module['intArrayFromString'] = intArrayFromString;
function intArrayToString(array) {
  var ret = [];
  for (var i = 0; i < array.length; i++) {
    var chr = array[i];
    if (chr > 0xFF) {
        assert(false, 'Character code ' + chr + ' (' + String.fromCharCode(chr) + ')  at offset ' + i + ' not in 0x00-0xFF.');
      chr &= 0xFF;
    }
    ret.push(String.fromCharCode(chr));
  }
  return ret.join('');
}
Module['intArrayToString'] = intArrayToString;
// Write a Javascript array to somewhere in the heap
function writeStringToMemory(string, buffer, dontAddNull) {
  var array = intArrayFromString(string, dontAddNull);
  var i = 0;
  while (i < array.length) {
    var chr = array[i];
    HEAP8[(((buffer)+(i))|0)]=chr
    i = i + 1;
  }
}
Module['writeStringToMemory'] = writeStringToMemory;
function writeArrayToMemory(array, buffer) {
  for (var i = 0; i < array.length; i++) {
    HEAP8[(((buffer)+(i))|0)]=array[i];
  }
}
Module['writeArrayToMemory'] = writeArrayToMemory;
function unSign(value, bits, ignore, sig) {
  if (value >= 0) {
    return value;
  }
  return bits <= 32 ? 2*Math.abs(1 << (bits-1)) + value // Need some trickery, since if bits == 32, we are right at the limit of the bits JS uses in bitshifts
                    : Math.pow(2, bits)         + value;
}
function reSign(value, bits, ignore, sig) {
  if (value <= 0) {
    return value;
  }
  var half = bits <= 32 ? Math.abs(1 << (bits-1)) // abs is needed if bits == 32
                        : Math.pow(2, bits-1);
  if (value >= half && (bits <= 32 || value > half)) { // for huge values, we can hit the precision limit and always get true here. so don't do that
                                                       // but, in general there is no perfect solution here. With 64-bit ints, we get rounding and errors
                                                       // TODO: In i64 mode 1, resign the two parts separately and safely
    value = -2*half + value; // Cannot bitshift half, as it may be at the limit of the bits JS uses in bitshifts
  }
  return value;
}
if (!Math.imul) Math.imul = function(a, b) {
  var ah  = a >>> 16;
  var al = a & 0xffff;
  var bh  = b >>> 16;
  var bl = b & 0xffff;
  return (al*bl + ((ah*bl + al*bh) << 16))|0;
};
// A counter of dependencies for calling run(). If we need to
// do asynchronous work before running, increment this and
// decrement it. Incrementing must happen in a place like
// PRE_RUN_ADDITIONS (used by emcc to add file preloading).
// Note that you can add dependencies in preRun, even though
// it happens right before run - run will be postponed until
// the dependencies are met.
var runDependencies = 0;
var runDependencyTracking = {};
var calledRun = false;
var runDependencyWatcher = null;
function addRunDependency(id) {
  runDependencies++;
  if (Module['monitorRunDependencies']) {
    Module['monitorRunDependencies'](runDependencies);
  }
  if (id) {
    assert(!runDependencyTracking[id]);
    runDependencyTracking[id] = 1;
    if (runDependencyWatcher === null && typeof setInterval !== 'undefined') {
      // Check for missing dependencies every few seconds
      runDependencyWatcher = setInterval(function() {
        var shown = false;
        for (var dep in runDependencyTracking) {
          if (!shown) {
            shown = true;
            Module.printErr('still waiting on run dependencies:');
          }
          Module.printErr('dependency: ' + dep);
        }
        if (shown) {
          Module.printErr('(end of list)');
        }
      }, 6000);
    }
  } else {
    Module.printErr('warning: run dependency added without ID');
  }
}
Module['addRunDependency'] = addRunDependency;
function removeRunDependency(id) {
  runDependencies--;
  if (Module['monitorRunDependencies']) {
    Module['monitorRunDependencies'](runDependencies);
  }
  if (id) {
    assert(runDependencyTracking[id]);
    delete runDependencyTracking[id];
  } else {
    Module.printErr('warning: run dependency removed without ID');
  }
  if (runDependencies == 0) {
    if (runDependencyWatcher !== null) {
      clearInterval(runDependencyWatcher);
      runDependencyWatcher = null;
    } 
    // If run has never been called, and we should call run (INVOKE_RUN is true, and Module.noInitialRun is not false)
    if (!calledRun && shouldRunNow) run();
  }
}
Module['removeRunDependency'] = removeRunDependency;
Module["preloadedImages"] = {}; // maps url to image data
Module["preloadedAudios"] = {}; // maps url to audio data
// === Body ===
assert(STATICTOP == STACK_MAX); assert(STACK_MAX == TOTAL_STACK);
STATICTOP += 1124;
assert(STATICTOP < TOTAL_MEMORY);
allocate(4, "i8", ALLOC_NONE, 5242880);
allocate([1,0,0,0,0,0,0,0,255,255,255,255,0,0,0,128,255,255,255,127,1,0,0,128,254,255,255,127,103,69,35,1,239,205,171,137,254,255,255,255,2,0,0,0,253,255,255,255,3,0,0,0,192,255,255,255,64,0,0,0,251,255,255,255,73,133,255,255], "i8", ALLOC_NONE, 5242884);
allocate([0,0,0,0,255,255,255,255,1,0,0,0,0,0,0,128,254,255,255,255,2,0,0,0,3,0,0,0,1,0,0,0,2,0,0,0,30,0,0,0,31,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0], "i8", ALLOC_NONE, 5242952);
allocate(80, "i8", ALLOC_NONE, 5243020);
allocate(4, "i8", ALLOC_NONE, 5243100);
allocate([24,0,0,0,1,0,0,0,0,0,0,0,11,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,18,0,0,0,1,0,0,0,0,0,0,0,11,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,4,0,0,0,2,0,0,0,1,0,0,0,11,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,26,0,0,0,2,0,0,0,0,0,0,0,2,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,10,0,0,0,2,0,0,0,1,0,0,0,11,0,0,0,3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,14,0,0,0,2,0,0,0,0,0,0,0,1,0,0,0,3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,2,0,0,0,0,0,0,0,1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,12,0,0,0,2,0,0,0,1,0,0,0,11,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,6,0,0,0,2,0,0,0,1,0,0,0,11,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,22,0,0,0,2,0,0,0,1,0,0,0,11,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,20,0,0,0,2,0,0,0,0,0,0,0,1,0,0,0,7,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,16,0,0,0,2,0,0,0,0,0,0,0,1,0,0,0,7,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,8,0,0,0,2,0,0,0,0,0,0,0,3,0,0,0,7,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0], ["*",0,0,0,"i32",0,0,0,"i32",0,0,0,"i32",0,0,0,"i32",0,0,0,"i32",0,0,0,"*",0,0,0,"*",0,0,0,"*",0,0,0,"*",0,0,0,"i32",0,0,0,"i32",0,0,0,"i32",0,0,0,"i32",0,0,0,"i32",0,0,0,"*",0,0,0,"*",0,0,0,"*",0,0,0,"*",0,0,0,"i32",0,0,0,"i32",0,0,0,"i32",0,0,0,"i32",0,0,0,"i32",0,0,0,"*",0,0,0,"*",0,0,0,"*",0,0,0,"*",0,0,0,"i32",0,0,0,"i32",0,0,0,"i32",0,0,0,"i32",0,0,0,"i32",0,0,0,"*",0,0,0,"*",0,0,0,"*",0,0,0,"*",0,0,0,"i32",0,0,0,"i32",0,0,0,"i32",0,0,0,"i32",0,0,0,"i32",0,0,0,"*",0,0,0,"*",0,0,0,"*",0,0,0,"*",0,0,0,"i32",0,0,0,"i32",0,0,0,"i32",0,0,0,"i32",0,0,0,"i32",0,0,0,"*",0,0,0,"*",0,0,0,"*",0,0,0,"*",0,0,0,"i32",0,0,0,"i32",0,0,0,"i32",0,0,0,"i32",0,0,0,"i32",0,0,0,"*",0,0,0,"*",0,0,0,"*",0,0,0,"*",0,0,0,"i32",0,0,0,"i32",0,0,0,"i32",0,0,0,"i32",0,0,0,"i32",0,0,0,"*",0,0,0,"*",0,0,0,"*",0,0,0,"*",0,0,0,"i32",0,0,0,"i32",0,0,0,"i32",0,0,0,"i32",0,0,0,"i32",0,0,0,"*",0,0,0,"*",0,0,0,"*",0,0,0,"*",0,0,0,"i32",0,0,0,"i32",0,0,0,"i32",0,0,0,"i32",0,0,0,"i32",0,0,0,"*",0,0,0,"*",0,0,0,"*",0,0,0,"*",0,0,0,"i32",0,0,0,"i32",0,0,0,"i32",0,0,0,"i32",0,0,0,"i32",0,0,0,"*",0,0,0,"*",0,0,0,"*",0,0,0,"*",0,0,0,"i32",0,0,0,"i32",0,0,0,"i32",0,0,0,"i32",0,0,0,"i32",0,0,0,"*",0,0,0,"*",0,0,0,"*",0,0,0,"*",0,0,0,"i32",0,0,0,"i32",0,0,0,"i32",0,0,0,"i32",0,0,0,"i32",0,0,0,"*",0,0,0,"*",0,0,0,"*",0,0,0], ALLOC_NONE, 5243104);
allocate(20, "i8", ALLOC_NONE, 5243572);
allocate(68, "i8", ALLOC_NONE, 5243592);
allocate(4, "i8", ALLOC_NONE, 5243660);
allocate(4, "i8", ALLOC_NONE, 5243664);
allocate([32,45,32,0] /*  - \00 */, "i8", ALLOC_NONE, 5243668);
allocate([115,117,98,0] /* sub\00 */, "i8", ALLOC_NONE, 5243672);
allocate([32,43,32,0] /*  + \00 */, "i8", ALLOC_NONE, 5243676);
allocate([40,0] /* (\00 */, "i8", ALLOC_NONE, 5243680);
allocate([97,100,100,0] /* add\00 */, "i8", ALLOC_NONE, 5243684);
allocate([116,111,116,97,108,32,61,32,37,100,10,0] /* total = %d\0A\00 */, "i8", ALLOC_NONE, 5243688);
allocate([37,100,44,32,0] /* %d, \00 */, "i8", ALLOC_NONE, 5243700);
allocate([67,111,117,110,116,101,114,115,32,61,32,0] /* Counters = \00 */, "i8", ALLOC_NONE, 5243708);
allocate([70,111,117,110,100,32,37,100,32,115,111,108,117,116,105,111,110,115,46,10,0] /* Found %d solutions.\ */, "i8", ALLOC_NONE, 5243720);
allocate([83,101,97,114,99,104,105,110,103,32,102,111,114,32,112,114,111,103,114,97,109,115,32,119,105,116,104,32,37,100,32,111,112,101,114,97,116,105,111,110,115,46,10,0] /* Searching for progra */, "i8", ALLOC_NONE, 5243744);
allocate([126,40,0] /* ~(\00 */, "i8", ALLOC_NONE, 5243788);
allocate([10,70,111,117,110,100,32,97,32,37,100,45,111,112,101,114,97,116,105,111,110,32,112,114,111,103,114,97,109,58,10,0] /* \0AFound a %d-operat */, "i8", ALLOC_NONE, 5243792);
allocate([32,32,32,69,120,112,114,58,32,0] /*    Expr: \00 */, "i8", ALLOC_NONE, 5243824);
allocate([10,0] /* \0A\00 */, "i8", ALLOC_NONE, 5243836);
allocate([44,0] /* ,\00 */, "i8", ALLOC_NONE, 5243840);
allocate([114,37,100,0] /* r%d\00 */, "i8", ALLOC_NONE, 5243844);
allocate([114,120,0] /* rx\00 */, "i8", ALLOC_NONE, 5243848);
allocate([32,32,32,37,45,53,115,32,114,37,100,44,0] /*    %-5s r%d,\00 */, "i8", ALLOC_NONE, 5243852);
allocate([41,0] /* )\00 */, "i8", ALLOC_NONE, 5243868);
allocate([37,115,0] /* %s\00 */, "i8", ALLOC_NONE, 5243872);
allocate([120,0] /* x\00 */, "i8", ALLOC_NONE, 5243876);
allocate([110,111,116,0] /* not\00 */, "i8", ALLOC_NONE, 5243880);
allocate([48,120,37,88,0] /* 0x%X\00 */, "i8", ALLOC_NONE, 5243884);
allocate([37,100,0] /* %d\00 */, "i8", ALLOC_NONE, 5243892);
allocate([32,62,62,115,32,0] /*  __s \00 */, "i8", ALLOC_NONE, 5243896);
allocate([115,104,114,115,0] /* shrs\00 */, "i8", ALLOC_NONE, 5243904);
allocate([32,62,62,117,32,0] /*  __u \00 */, "i8", ALLOC_NONE, 5243912);
allocate([115,104,114,0] /* shr\00 */, "i8", ALLOC_NONE, 5243920);
allocate([32,60,60,32,0] /*  __ \00 */, "i8", ALLOC_NONE, 5243924);
allocate([115,104,108,0] /* shl\00 */, "i8", ALLOC_NONE, 5243932);
allocate([32,94,32,0] /*  ^ \00 */, "i8", ALLOC_NONE, 5243936);
allocate([120,111,114,0] /* xor\00 */, "i8", ALLOC_NONE, 5243940);
allocate(1, "i8", ALLOC_NONE, 5243944);
allocate([32,124,32,0] /*  | \00 */, "i8", ALLOC_NONE, 5243948);
allocate([111,114,0] /* or\00 */, "i8", ALLOC_NONE, 5243952);
allocate([32,38,32,0] /*  & \00 */, "i8", ALLOC_NONE, 5243956);
allocate([97,110,100,0] /* and\00 */, "i8", ALLOC_NONE, 5243960);
allocate([32,47,117,32,0] /*  /u \00 */, "i8", ALLOC_NONE, 5243964);
allocate([100,105,118,117,0] /* divu\00 */, "i8", ALLOC_NONE, 5243972);
allocate([47,0] /* /\00 */, "i8", ALLOC_NONE, 5243980);
allocate([100,105,118,0] /* div\00 */, "i8", ALLOC_NONE, 5243984);
allocate([42,0] /* _\00 */, "i8", ALLOC_NONE, 5243988);
allocate([109,117,108,0] /* mul\00 */, "i8", ALLOC_NONE, 5243992);
allocate([45,40,0] /* -(\00 */, "i8", ALLOC_NONE, 5243996);
allocate([110,101,103,0] /* neg\00 */, "i8", ALLOC_NONE, 5244000);
HEAP32[((5243128)>>2)]=((5244000)|0);
HEAP32[((5243132)>>2)]=((5243996)|0);
HEAP32[((5243136)>>2)]=((5243944)|0);
HEAP32[((5243164)>>2)]=((5243880)|0);
HEAP32[((5243168)>>2)]=((5243788)|0);
HEAP32[((5243172)>>2)]=((5243944)|0);
HEAP32[((5243200)>>2)]=((5243684)|0);
HEAP32[((5243204)>>2)]=((5243680)|0);
HEAP32[((5243208)>>2)]=((5243676)|0);
HEAP32[((5243236)>>2)]=((5243672)|0);
HEAP32[((5243240)>>2)]=((5243680)|0);
HEAP32[((5243244)>>2)]=((5243668)|0);
HEAP32[((5243272)>>2)]=((5243992)|0);
HEAP32[((5243276)>>2)]=((5243680)|0);
HEAP32[((5243280)>>2)]=((5243988)|0);
HEAP32[((5243308)>>2)]=((5243984)|0);
HEAP32[((5243312)>>2)]=((5243680)|0);
HEAP32[((5243316)>>2)]=((5243980)|0);
HEAP32[((5243344)>>2)]=((5243972)|0);
HEAP32[((5243348)>>2)]=((5243680)|0);
HEAP32[((5243352)>>2)]=((5243964)|0);
HEAP32[((5243380)>>2)]=((5243960)|0);
HEAP32[((5243384)>>2)]=((5243680)|0);
HEAP32[((5243388)>>2)]=((5243956)|0);
HEAP32[((5243416)>>2)]=((5243952)|0);
HEAP32[((5243420)>>2)]=((5243680)|0);
HEAP32[((5243424)>>2)]=((5243948)|0);
HEAP32[((5243452)>>2)]=((5243940)|0);
HEAP32[((5243456)>>2)]=((5243680)|0);
HEAP32[((5243460)>>2)]=((5243936)|0);
HEAP32[((5243488)>>2)]=((5243932)|0);
HEAP32[((5243492)>>2)]=((5243680)|0);
HEAP32[((5243496)>>2)]=((5243924)|0);
HEAP32[((5243524)>>2)]=((5243920)|0);
HEAP32[((5243528)>>2)]=((5243680)|0);
HEAP32[((5243532)>>2)]=((5243912)|0);
HEAP32[((5243560)>>2)]=((5243904)|0);
HEAP32[((5243564)>>2)]=((5243680)|0);
HEAP32[((5243568)>>2)]=((5243896)|0);
  var ERRNO_CODES={E2BIG:7,EACCES:13,EADDRINUSE:98,EADDRNOTAVAIL:99,EAFNOSUPPORT:97,EAGAIN:11,EALREADY:114,EBADF:9,EBADMSG:74,EBUSY:16,ECANCELED:125,ECHILD:10,ECONNABORTED:103,ECONNREFUSED:111,ECONNRESET:104,EDEADLK:35,EDESTADDRREQ:89,EDOM:33,EDQUOT:122,EEXIST:17,EFAULT:14,EFBIG:27,EHOSTUNREACH:113,EIDRM:43,EILSEQ:84,EINPROGRESS:115,EINTR:4,EINVAL:22,EIO:5,EISCONN:106,EISDIR:21,ELOOP:40,EMFILE:24,EMLINK:31,EMSGSIZE:90,EMULTIHOP:72,ENAMETOOLONG:36,ENETDOWN:100,ENETRESET:102,ENETUNREACH:101,ENFILE:23,ENOBUFS:105,ENODATA:61,ENODEV:19,ENOENT:2,ENOEXEC:8,ENOLCK:37,ENOLINK:67,ENOMEM:12,ENOMSG:42,ENOPROTOOPT:92,ENOSPC:28,ENOSR:63,ENOSTR:60,ENOSYS:38,ENOTCONN:107,ENOTDIR:20,ENOTEMPTY:39,ENOTRECOVERABLE:131,ENOTSOCK:88,ENOTSUP:95,ENOTTY:25,ENXIO:6,EOVERFLOW:75,EOWNERDEAD:130,EPERM:1,EPIPE:32,EPROTO:71,EPROTONOSUPPORT:93,EPROTOTYPE:91,ERANGE:34,EROFS:30,ESPIPE:29,ESRCH:3,ESTALE:116,ETIME:62,ETIMEDOUT:110,ETXTBSY:26,EWOULDBLOCK:11,EXDEV:18};
  function ___setErrNo(value) {
      // For convenient setting and returning of errno.
      if (!___setErrNo.ret) ___setErrNo.ret = allocate([0], 'i32', ALLOC_STATIC);
      HEAP32[((___setErrNo.ret)>>2)]=value
      return value;
    }
  var _stdin=allocate(1, "i32*", ALLOC_STACK);
  var _stdout=allocate(1, "i32*", ALLOC_STACK);
  var _stderr=allocate(1, "i32*", ALLOC_STACK);
  var __impure_ptr=allocate(1, "i32*", ALLOC_STACK);var FS={currentPath:"/",nextInode:2,streams:[null],checkStreams:function () {
        for (var i in FS.streams) if (FS.streams.hasOwnProperty(i)) assert(i >= 0 && i < FS.streams.length); // no keys not in dense span
        for (var i = 0; i < FS.streams.length; i++) assert(typeof FS.streams[i] == 'object'); // no non-null holes in dense span
      },ignorePermissions:true,joinPath:function (parts, forceRelative) {
        var ret = parts[0];
        for (var i = 1; i < parts.length; i++) {
          if (ret[ret.length-1] != '/') ret += '/';
          ret += parts[i];
        }
        if (forceRelative && ret[0] == '/') ret = ret.substr(1);
        return ret;
      },absolutePath:function (relative, base) {
        if (typeof relative !== 'string') return null;
        if (base === undefined) base = FS.currentPath;
        if (relative && relative[0] == '/') base = '';
        var full = base + '/' + relative;
        var parts = full.split('/').reverse();
        var absolute = [''];
        while (parts.length) {
          var part = parts.pop();
          if (part == '' || part == '.') {
            // Nothing.
          } else if (part == '..') {
            if (absolute.length > 1) absolute.pop();
          } else {
            absolute.push(part);
          }
        }
        return absolute.length == 1 ? '/' : absolute.join('/');
      },analyzePath:function (path, dontResolveLastLink, linksVisited) {
        var ret = {
          isRoot: false,
          exists: false,
          error: 0,
          name: null,
          path: null,
          object: null,
          parentExists: false,
          parentPath: null,
          parentObject: null
        };
        path = FS.absolutePath(path);
        if (path == '/') {
          ret.isRoot = true;
          ret.exists = ret.parentExists = true;
          ret.name = '/';
          ret.path = ret.parentPath = '/';
          ret.object = ret.parentObject = FS.root;
        } else if (path !== null) {
          linksVisited = linksVisited || 0;
          path = path.slice(1).split('/');
          var current = FS.root;
          var traversed = [''];
          while (path.length) {
            if (path.length == 1 && current.isFolder) {
              ret.parentExists = true;
              ret.parentPath = traversed.length == 1 ? '/' : traversed.join('/');
              ret.parentObject = current;
              ret.name = path[0];
            }
            var target = path.shift();
            if (!current.isFolder) {
              ret.error = ERRNO_CODES.ENOTDIR;
              break;
            } else if (!current.read) {
              ret.error = ERRNO_CODES.EACCES;
              break;
            } else if (!current.contents.hasOwnProperty(target)) {
              ret.error = ERRNO_CODES.ENOENT;
              break;
            }
            current = current.contents[target];
            if (current.link && !(dontResolveLastLink && path.length == 0)) {
              if (linksVisited > 40) { // Usual Linux SYMLOOP_MAX.
                ret.error = ERRNO_CODES.ELOOP;
                break;
              }
              var link = FS.absolutePath(current.link, traversed.join('/'));
              ret = FS.analyzePath([link].concat(path).join('/'),
                                   dontResolveLastLink, linksVisited + 1);
              return ret;
            }
            traversed.push(target);
            if (path.length == 0) {
              ret.exists = true;
              ret.path = traversed.join('/');
              ret.object = current;
            }
          }
        }
        return ret;
      },findObject:function (path, dontResolveLastLink) {
        FS.ensureRoot();
        var ret = FS.analyzePath(path, dontResolveLastLink);
        if (ret.exists) {
          return ret.object;
        } else {
          ___setErrNo(ret.error);
          return null;
        }
      },createObject:function (parent, name, properties, canRead, canWrite) {
        if (!parent) parent = '/';
        if (typeof parent === 'string') parent = FS.findObject(parent);
        if (!parent) {
          ___setErrNo(ERRNO_CODES.EACCES);
          throw new Error('Parent path must exist.');
        }
        if (!parent.isFolder) {
          ___setErrNo(ERRNO_CODES.ENOTDIR);
          throw new Error('Parent must be a folder.');
        }
        if (!parent.write && !FS.ignorePermissions) {
          ___setErrNo(ERRNO_CODES.EACCES);
          throw new Error('Parent folder must be writeable.');
        }
        if (!name || name == '.' || name == '..') {
          ___setErrNo(ERRNO_CODES.ENOENT);
          throw new Error('Name must not be empty.');
        }
        if (parent.contents.hasOwnProperty(name)) {
          ___setErrNo(ERRNO_CODES.EEXIST);
          throw new Error("Can't overwrite object.");
        }
        parent.contents[name] = {
          read: canRead === undefined ? true : canRead,
          write: canWrite === undefined ? false : canWrite,
          timestamp: Date.now(),
          inodeNumber: FS.nextInode++
        };
        for (var key in properties) {
          if (properties.hasOwnProperty(key)) {
            parent.contents[name][key] = properties[key];
          }
        }
        return parent.contents[name];
      },createFolder:function (parent, name, canRead, canWrite) {
        var properties = {isFolder: true, isDevice: false, contents: {}};
        return FS.createObject(parent, name, properties, canRead, canWrite);
      },createPath:function (parent, path, canRead, canWrite) {
        var current = FS.findObject(parent);
        if (current === null) throw new Error('Invalid parent.');
        path = path.split('/').reverse();
        while (path.length) {
          var part = path.pop();
          if (!part) continue;
          if (!current.contents.hasOwnProperty(part)) {
            FS.createFolder(current, part, canRead, canWrite);
          }
          current = current.contents[part];
        }
        return current;
      },createFile:function (parent, name, properties, canRead, canWrite) {
        properties.isFolder = false;
        return FS.createObject(parent, name, properties, canRead, canWrite);
      },createDataFile:function (parent, name, data, canRead, canWrite) {
        if (typeof data === 'string') {
          var dataArray = new Array(data.length);
          for (var i = 0, len = data.length; i < len; ++i) dataArray[i] = data.charCodeAt(i);
          data = dataArray;
        }
        var properties = {
          isDevice: false,
          contents: data.subarray ? data.subarray(0) : data // as an optimization, create a new array wrapper (not buffer) here, to help JS engines understand this object
        };
        return FS.createFile(parent, name, properties, canRead, canWrite);
      },createLazyFile:function (parent, name, url, canRead, canWrite) {
        if (typeof XMLHttpRequest !== 'undefined') {
          if (!ENVIRONMENT_IS_WORKER) throw 'Cannot do synchronous binary XHRs outside webworkers in modern browsers. Use --embed-file or --preload-file in emcc';
          // Lazy chunked Uint8Array (implements get and length from Uint8Array). Actual getting is abstracted away for eventual reuse.
          var LazyUint8Array = function(chunkSize, length) {
            this.length = length;
            this.chunkSize = chunkSize;
            this.chunks = []; // Loaded chunks. Index is the chunk number
          }
          LazyUint8Array.prototype.get = function(idx) {
            if (idx > this.length-1 || idx < 0) {
              return undefined;
            }
            var chunkOffset = idx % chunkSize;
            var chunkNum = Math.floor(idx / chunkSize);
            return this.getter(chunkNum)[chunkOffset];
          }
          LazyUint8Array.prototype.setDataGetter = function(getter) {
            this.getter = getter;
          }
          // Find length
          var xhr = new XMLHttpRequest();
          xhr.open('HEAD', url, false);
          xhr.send(null);
          if (!(xhr.status >= 200 && xhr.status < 300 || xhr.status === 304)) throw new Error("Couldn't load " + url + ". Status: " + xhr.status);
          var datalength = Number(xhr.getResponseHeader("Content-length"));
          var header;
          var hasByteServing = (header = xhr.getResponseHeader("Accept-Ranges")) && header === "bytes";
          var chunkSize = 1024*1024; // Chunk size in bytes
          if (!hasByteServing) chunkSize = datalength;
          // Function to get a range from the remote URL.
          var doXHR = (function(from, to) {
            if (from > to) throw new Error("invalid range (" + from + ", " + to + ") or no bytes requested!");
            if (to > datalength-1) throw new Error("only " + datalength + " bytes available! programmer error!");
            // TODO: Use mozResponseArrayBuffer, responseStream, etc. if available.
            var xhr = new XMLHttpRequest();
            xhr.open('GET', url, false);
            if (datalength !== chunkSize) xhr.setRequestHeader("Range", "bytes=" + from + "-" + to);
            // Some hints to the browser that we want binary data.
            if (typeof Uint8Array != 'undefined') xhr.responseType = 'arraybuffer';
            if (xhr.overrideMimeType) {
              xhr.overrideMimeType('text/plain; charset=x-user-defined');
            }
            xhr.send(null);
            if (!(xhr.status >= 200 && xhr.status < 300 || xhr.status === 304)) throw new Error("Couldn't load " + url + ". Status: " + xhr.status);
            if (xhr.response !== undefined) {
              return new Uint8Array(xhr.response || []);
            } else {
              return intArrayFromString(xhr.responseText || '', true);
            }
          });
          var lazyArray = new LazyUint8Array(chunkSize, datalength);
          lazyArray.setDataGetter(function(chunkNum) {
            var start = chunkNum * lazyArray.chunkSize;
            var end = (chunkNum+1) * lazyArray.chunkSize - 1; // including this byte
            end = Math.min(end, datalength-1); // if datalength-1 is selected, this is the last block
            if (typeof(lazyArray.chunks[chunkNum]) === "undefined") {
              lazyArray.chunks[chunkNum] = doXHR(start, end);
            }
            if (typeof(lazyArray.chunks[chunkNum]) === "undefined") throw new Error("doXHR failed!");
            return lazyArray.chunks[chunkNum];
          });
          var properties = { isDevice: false, contents: lazyArray };
        } else {
          var properties = { isDevice: false, url: url };
        }
        return FS.createFile(parent, name, properties, canRead, canWrite);
      },createPreloadedFile:function (parent, name, url, canRead, canWrite, onload, onerror, dontCreateFile) {
        Browser.init();
        var fullname = FS.joinPath([parent, name], true);
        function processData(byteArray) {
          function finish(byteArray) {
            if (!dontCreateFile) {
              FS.createDataFile(parent, name, byteArray, canRead, canWrite);
            }
            if (onload) onload();
            removeRunDependency('cp ' + fullname);
          }
          var handled = false;
          Module['preloadPlugins'].forEach(function(plugin) {
            if (handled) return;
            if (plugin['canHandle'](fullname)) {
              plugin['handle'](byteArray, fullname, finish, function() {
                if (onerror) onerror();
                removeRunDependency('cp ' + fullname);
              });
              handled = true;
            }
          });
          if (!handled) finish(byteArray);
        }
        addRunDependency('cp ' + fullname);
        if (typeof url == 'string') {
          Browser.asyncLoad(url, function(byteArray) {
            processData(byteArray);
          }, onerror);
        } else {
          processData(url);
        }
      },createLink:function (parent, name, target, canRead, canWrite) {
        var properties = {isDevice: false, link: target};
        return FS.createFile(parent, name, properties, canRead, canWrite);
      },createDevice:function (parent, name, input, output) {
        if (!(input || output)) {
          throw new Error('A device must have at least one callback defined.');
        }
        var ops = {isDevice: true, input: input, output: output};
        return FS.createFile(parent, name, ops, Boolean(input), Boolean(output));
      },forceLoadFile:function (obj) {
        if (obj.isDevice || obj.isFolder || obj.link || obj.contents) return true;
        var success = true;
        if (typeof XMLHttpRequest !== 'undefined') {
          throw new Error("Lazy loading should have been performed (contents set) in createLazyFile, but it was not. Lazy loading only works in web workers. Use --embed-file or --preload-file in emcc on the main thread.");
        } else if (Module['read']) {
          // Command-line.
          try {
            // WARNING: Can't read binary files in V8's d8 or tracemonkey's js, as
            //          read() will try to parse UTF8.
            obj.contents = intArrayFromString(Module['read'](obj.url), true);
          } catch (e) {
            success = false;
          }
        } else {
          throw new Error('Cannot load without read() or XMLHttpRequest.');
        }
        if (!success) ___setErrNo(ERRNO_CODES.EIO);
        return success;
      },ensureRoot:function () {
        if (FS.root) return;
        // The main file system tree. All the contents are inside this.
        FS.root = {
          read: true,
          write: true,
          isFolder: true,
          isDevice: false,
          timestamp: Date.now(),
          inodeNumber: 1,
          contents: {}
        };
      },init:function (input, output, error) {
        // Make sure we initialize only once.
        assert(!FS.init.initialized, 'FS.init was previously called. If you want to initialize later with custom parameters, remove any earlier calls (note that one is automatically added to the generated code)');
        FS.init.initialized = true;
        FS.ensureRoot();
        // Allow Module.stdin etc. to provide defaults, if none explicitly passed to us here
        input = input || Module['stdin'];
        output = output || Module['stdout'];
        error = error || Module['stderr'];
        // Default handlers.
        var stdinOverridden = true, stdoutOverridden = true, stderrOverridden = true;
        if (!input) {
          stdinOverridden = false;
          input = function() {
            if (!input.cache || !input.cache.length) {
              var result;
              if (typeof window != 'undefined' &&
                  typeof window.prompt == 'function') {
                // Browser.
                result = window.prompt('Input: ');
                if (result === null) result = String.fromCharCode(0); // cancel ==> EOF
              } else if (typeof readline == 'function') {
                // Command line.
                result = readline();
              }
              if (!result) result = '';
              input.cache = intArrayFromString(result + '\n', true);
            }
            return input.cache.shift();
          };
        }
        var utf8 = new Runtime.UTF8Processor();
        function simpleOutput(val) {
          if (val === null || val === 10) {
            output.printer(output.buffer.join(''));
            output.buffer = [];
          } else {
            output.buffer.push(utf8.processCChar(val));
          }
        }
        if (!output) {
          stdoutOverridden = false;
          output = simpleOutput;
        }
        if (!output.printer) output.printer = Module['print'];
        if (!output.buffer) output.buffer = [];
        if (!error) {
          stderrOverridden = false;
          error = simpleOutput;
        }
        if (!error.printer) error.printer = Module['print'];
        if (!error.buffer) error.buffer = [];
        // Create the temporary folder, if not already created
        try {
          FS.createFolder('/', 'tmp', true, true);
        } catch(e) {}
        // Create the I/O devices.
        var devFolder = FS.createFolder('/', 'dev', true, true);
        var stdin = FS.createDevice(devFolder, 'stdin', input);
        var stdout = FS.createDevice(devFolder, 'stdout', null, output);
        var stderr = FS.createDevice(devFolder, 'stderr', null, error);
        FS.createDevice(devFolder, 'tty', input, output);
        // Create default streams.
        FS.streams[1] = {
          path: '/dev/stdin',
          object: stdin,
          position: 0,
          isRead: true,
          isWrite: false,
          isAppend: false,
          isTerminal: !stdinOverridden,
          error: false,
          eof: false,
          ungotten: []
        };
        FS.streams[2] = {
          path: '/dev/stdout',
          object: stdout,
          position: 0,
          isRead: false,
          isWrite: true,
          isAppend: false,
          isTerminal: !stdoutOverridden,
          error: false,
          eof: false,
          ungotten: []
        };
        FS.streams[3] = {
          path: '/dev/stderr',
          object: stderr,
          position: 0,
          isRead: false,
          isWrite: true,
          isAppend: false,
          isTerminal: !stderrOverridden,
          error: false,
          eof: false,
          ungotten: []
        };
        assert(Math.max(_stdin, _stdout, _stderr) < 128); // make sure these are low, we flatten arrays with these
        HEAP32[((_stdin)>>2)]=1;
        HEAP32[((_stdout)>>2)]=2;
        HEAP32[((_stderr)>>2)]=3;
        // Other system paths
        FS.createPath('/', 'dev/shm/tmp', true, true); // temp files
        // Newlib initialization
        for (var i = FS.streams.length; i < Math.max(_stdin, _stdout, _stderr) + 4; i++) {
          FS.streams[i] = null; // Make sure to keep FS.streams dense
        }
        FS.streams[_stdin] = FS.streams[1];
        FS.streams[_stdout] = FS.streams[2];
        FS.streams[_stderr] = FS.streams[3];
        FS.checkStreams();
        assert(FS.streams.length < 1024); // at this early stage, we should not have a large set of file descriptors - just a few
        allocate([ allocate(
          [0, 0, 0, 0, _stdin, 0, 0, 0, _stdout, 0, 0, 0, _stderr, 0, 0, 0],
          'void*', ALLOC_STATIC) ], 'void*', ALLOC_NONE, __impure_ptr);
      },quit:function () {
        if (!FS.init.initialized) return;
        // Flush any partially-printed lines in stdout and stderr. Careful, they may have been closed
        if (FS.streams[2] && FS.streams[2].object.output.buffer.length > 0) FS.streams[2].object.output(10);
        if (FS.streams[3] && FS.streams[3].object.output.buffer.length > 0) FS.streams[3].object.output(10);
      },standardizePath:function (path) {
        if (path.substr(0, 2) == './') path = path.substr(2);
        return path;
      },deleteFile:function (path) {
        path = FS.analyzePath(path);
        if (!path.parentExists || !path.exists) {
          throw 'Invalid path ' + path;
        }
        delete path.parentObject.contents[path.name];
      }};
  function _pwrite(fildes, buf, nbyte, offset) {
      // ssize_t pwrite(int fildes, const void *buf, size_t nbyte, off_t offset);
      // http://pubs.opengroup.org/onlinepubs/000095399/functions/write.html
      var stream = FS.streams[fildes];
      if (!stream || stream.object.isDevice) {
        ___setErrNo(ERRNO_CODES.EBADF);
        return -1;
      } else if (!stream.isWrite) {
        ___setErrNo(ERRNO_CODES.EACCES);
        return -1;
      } else if (stream.object.isFolder) {
        ___setErrNo(ERRNO_CODES.EISDIR);
        return -1;
      } else if (nbyte < 0 || offset < 0) {
        ___setErrNo(ERRNO_CODES.EINVAL);
        return -1;
      } else {
        var contents = stream.object.contents;
        while (contents.length < offset) contents.push(0);
        for (var i = 0; i < nbyte; i++) {
          contents[offset + i] = HEAPU8[(((buf)+(i))|0)];
        }
        stream.object.timestamp = Date.now();
        return i;
      }
    }function _write(fildes, buf, nbyte) {
      // ssize_t write(int fildes, const void *buf, size_t nbyte);
      // http://pubs.opengroup.org/onlinepubs/000095399/functions/write.html
      var stream = FS.streams[fildes];
      if (!stream) {
        ___setErrNo(ERRNO_CODES.EBADF);
        return -1;
      } else if (!stream.isWrite) {
        ___setErrNo(ERRNO_CODES.EACCES);
        return -1;
      } else if (nbyte < 0) {
        ___setErrNo(ERRNO_CODES.EINVAL);
        return -1;
      } else {
        if (stream.object.isDevice) {
          if (stream.object.output) {
            for (var i = 0; i < nbyte; i++) {
              try {
                stream.object.output(HEAP8[(((buf)+(i))|0)]);
              } catch (e) {
                ___setErrNo(ERRNO_CODES.EIO);
                return -1;
              }
            }
            stream.object.timestamp = Date.now();
            return i;
          } else {
            ___setErrNo(ERRNO_CODES.ENXIO);
            return -1;
          }
        } else {
          var bytesWritten = _pwrite(fildes, buf, nbyte, stream.position);
          if (bytesWritten != -1) stream.position += bytesWritten;
          return bytesWritten;
        }
      }
    }function _fwrite(ptr, size, nitems, stream) {
      // size_t fwrite(const void *restrict ptr, size_t size, size_t nitems, FILE *restrict stream);
      // http://pubs.opengroup.org/onlinepubs/000095399/functions/fwrite.html
      var bytesToWrite = nitems * size;
      if (bytesToWrite == 0) return 0;
      var bytesWritten = _write(stream, ptr, bytesToWrite);
      if (bytesWritten == -1) {
        if (FS.streams[stream]) FS.streams[stream].error = true;
        return 0;
      } else {
        return Math.floor(bytesWritten / size);
      }
    }
  function _strlen(ptr) {
      ptr = ptr|0;
      var curr = 0;
      curr = ptr;
      while (HEAP8[(curr)]|0 != 0) {
        curr = (curr + 1)|0;
      }
      return (curr - ptr)|0;
    }
  function __reallyNegative(x) {
      return x < 0 || (x === 0 && (1/x) === -Infinity);
    }function __formatString(format, varargs) {
      var textIndex = format;
      var argIndex = 0;
      function getNextArg(type) {
        // NOTE: Explicitly ignoring type safety. Otherwise this fails:
        //       int x = 4; printf("%c\n", (char)x);
        var ret;
        if (type === 'double') {
          ret = (HEAP32[((tempDoublePtr)>>2)]=HEAP32[(((varargs)+(argIndex))>>2)],HEAP32[(((tempDoublePtr)+(4))>>2)]=HEAP32[(((varargs)+((argIndex)+(4)))>>2)],HEAPF64[(tempDoublePtr)>>3]);
        } else if (type == 'i64') {
          ret = [HEAP32[(((varargs)+(argIndex))>>2)],
                 HEAP32[(((varargs)+(argIndex+4))>>2)]];
        } else {
          type = 'i32'; // varargs are always i32, i64, or double
          ret = HEAP32[(((varargs)+(argIndex))>>2)];
        }
        argIndex += Runtime.getNativeFieldSize(type);
        return ret;
      }
      var ret = [];
      var curr, next, currArg;
      while(1) {
        var startTextIndex = textIndex;
        curr = HEAP8[(textIndex)];
        if (curr === 0) break;
        next = HEAP8[((textIndex+1)|0)];
        if (curr == 37) {
          // Handle flags.
          var flagAlwaysSigned = false;
          var flagLeftAlign = false;
          var flagAlternative = false;
          var flagZeroPad = false;
          flagsLoop: while (1) {
            switch (next) {
              case 43:
                flagAlwaysSigned = true;
                break;
              case 45:
                flagLeftAlign = true;
                break;
              case 35:
                flagAlternative = true;
                break;
              case 48:
                if (flagZeroPad) {
                  break flagsLoop;
                } else {
                  flagZeroPad = true;
                  break;
                }
              default:
                break flagsLoop;
            }
            textIndex++;
            next = HEAP8[((textIndex+1)|0)];
          }
          // Handle width.
          var width = 0;
          if (next == 42) {
            width = getNextArg('i32');
            textIndex++;
            next = HEAP8[((textIndex+1)|0)];
          } else {
            while (next >= 48 && next <= 57) {
              width = width * 10 + (next - 48);
              textIndex++;
              next = HEAP8[((textIndex+1)|0)];
            }
          }
          // Handle precision.
          var precisionSet = false;
          if (next == 46) {
            var precision = 0;
            precisionSet = true;
            textIndex++;
            next = HEAP8[((textIndex+1)|0)];
            if (next == 42) {
              precision = getNextArg('i32');
              textIndex++;
            } else {
              while(1) {
                var precisionChr = HEAP8[((textIndex+1)|0)];
                if (precisionChr < 48 ||
                    precisionChr > 57) break;
                precision = precision * 10 + (precisionChr - 48);
                textIndex++;
              }
            }
            next = HEAP8[((textIndex+1)|0)];
          } else {
            var precision = 6; // Standard default.
          }
          // Handle integer sizes. WARNING: These assume a 32-bit architecture!
          var argSize;
          switch (String.fromCharCode(next)) {
            case 'h':
              var nextNext = HEAP8[((textIndex+2)|0)];
              if (nextNext == 104) {
                textIndex++;
                argSize = 1; // char (actually i32 in varargs)
              } else {
                argSize = 2; // short (actually i32 in varargs)
              }
              break;
            case 'l':
              var nextNext = HEAP8[((textIndex+2)|0)];
              if (nextNext == 108) {
                textIndex++;
                argSize = 8; // long long
              } else {
                argSize = 4; // long
              }
              break;
            case 'L': // long long
            case 'q': // int64_t
            case 'j': // intmax_t
              argSize = 8;
              break;
            case 'z': // size_t
            case 't': // ptrdiff_t
            case 'I': // signed ptrdiff_t or unsigned size_t
              argSize = 4;
              break;
            default:
              argSize = null;
          }
          if (argSize) textIndex++;
          next = HEAP8[((textIndex+1)|0)];
          // Handle type specifier.
          switch (String.fromCharCode(next)) {
            case 'd': case 'i': case 'u': case 'o': case 'x': case 'X': case 'p': {
              // Integer.
              var signed = next == 100 || next == 105;
              argSize = argSize || 4;
              var currArg = getNextArg('i' + (argSize * 8));
              var origArg = currArg;
              var argText;
              // Flatten i64-1 [low, high] into a (slightly rounded) double
              if (argSize == 8) {
                currArg = Runtime.makeBigInt(currArg[0], currArg[1], next == 117);
              }
              // Truncate to requested size.
              if (argSize <= 4) {
                var limit = Math.pow(256, argSize) - 1;
                currArg = (signed ? reSign : unSign)(currArg & limit, argSize * 8);
              }
              // Format the number.
              var currAbsArg = Math.abs(currArg);
              var prefix = '';
              if (next == 100 || next == 105) {
                if (argSize == 8 && i64Math) argText = i64Math.stringify(origArg[0], origArg[1], null); else
                argText = reSign(currArg, 8 * argSize, 1).toString(10);
              } else if (next == 117) {
                if (argSize == 8 && i64Math) argText = i64Math.stringify(origArg[0], origArg[1], true); else
                argText = unSign(currArg, 8 * argSize, 1).toString(10);
                currArg = Math.abs(currArg);
              } else if (next == 111) {
                argText = (flagAlternative ? '0' : '') + currAbsArg.toString(8);
              } else if (next == 120 || next == 88) {
                prefix = flagAlternative ? '0x' : '';
                if (argSize == 8 && i64Math) {
                  if (origArg[1]) {
                    argText = (origArg[1]>>>0).toString(16);
                    var lower = (origArg[0]>>>0).toString(16);
                    while (lower.length < 8) lower = '0' + lower;
                    argText += lower;
                  } else {
                    argText = (origArg[0]>>>0).toString(16);
                  }
                } else
                if (currArg < 0) {
                  // Represent negative numbers in hex as 2's complement.
                  currArg = -currArg;
                  argText = (currAbsArg - 1).toString(16);
                  var buffer = [];
                  for (var i = 0; i < argText.length; i++) {
                    buffer.push((0xF - parseInt(argText[i], 16)).toString(16));
                  }
                  argText = buffer.join('');
                  while (argText.length < argSize * 2) argText = 'f' + argText;
                } else {
                  argText = currAbsArg.toString(16);
                }
                if (next == 88) {
                  prefix = prefix.toUpperCase();
                  argText = argText.toUpperCase();
                }
              } else if (next == 112) {
                if (currAbsArg === 0) {
                  argText = '(nil)';
                } else {
                  prefix = '0x';
                  argText = currAbsArg.toString(16);
                }
              }
              if (precisionSet) {
                while (argText.length < precision) {
                  argText = '0' + argText;
                }
              }
              // Add sign if needed
              if (flagAlwaysSigned) {
                if (currArg < 0) {
                  prefix = '-' + prefix;
                } else {
                  prefix = '+' + prefix;
                }
              }
              // Add padding.
              while (prefix.length + argText.length < width) {
                if (flagLeftAlign) {
                  argText += ' ';
                } else {
                  if (flagZeroPad) {
                    argText = '0' + argText;
                  } else {
                    prefix = ' ' + prefix;
                  }
                }
              }
              // Insert the result into the buffer.
              argText = prefix + argText;
              argText.split('').forEach(function(chr) {
                ret.push(chr.charCodeAt(0));
              });
              break;
            }
            case 'f': case 'F': case 'e': case 'E': case 'g': case 'G': {
              // Float.
              var currArg = getNextArg('double');
              var argText;
              if (isNaN(currArg)) {
                argText = 'nan';
                flagZeroPad = false;
              } else if (!isFinite(currArg)) {
                argText = (currArg < 0 ? '-' : '') + 'inf';
                flagZeroPad = false;
              } else {
                var isGeneral = false;
                var effectivePrecision = Math.min(precision, 20);
                // Convert g/G to f/F or e/E, as per:
                // http://pubs.opengroup.org/onlinepubs/9699919799/functions/printf.html
                if (next == 103 || next == 71) {
                  isGeneral = true;
                  precision = precision || 1;
                  var exponent = parseInt(currArg.toExponential(effectivePrecision).split('e')[1], 10);
                  if (precision > exponent && exponent >= -4) {
                    next = ((next == 103) ? 'f' : 'F').charCodeAt(0);
                    precision -= exponent + 1;
                  } else {
                    next = ((next == 103) ? 'e' : 'E').charCodeAt(0);
                    precision--;
                  }
                  effectivePrecision = Math.min(precision, 20);
                }
                if (next == 101 || next == 69) {
                  argText = currArg.toExponential(effectivePrecision);
                  // Make sure the exponent has at least 2 digits.
                  if (/[eE][-+]\d$/.test(argText)) {
                    argText = argText.slice(0, -1) + '0' + argText.slice(-1);
                  }
                } else if (next == 102 || next == 70) {
                  argText = currArg.toFixed(effectivePrecision);
                  if (currArg === 0 && __reallyNegative(currArg)) {
                    argText = '-' + argText;
                  }
                }
                var parts = argText.split('e');
                if (isGeneral && !flagAlternative) {
                  // Discard trailing zeros and periods.
                  while (parts[0].length > 1 && parts[0].indexOf('.') != -1 &&
                         (parts[0].slice(-1) == '0' || parts[0].slice(-1) == '.')) {
                    parts[0] = parts[0].slice(0, -1);
                  }
                } else {
                  // Make sure we have a period in alternative mode.
                  if (flagAlternative && argText.indexOf('.') == -1) parts[0] += '.';
                  // Zero pad until required precision.
                  while (precision > effectivePrecision++) parts[0] += '0';
                }
                argText = parts[0] + (parts.length > 1 ? 'e' + parts[1] : '');
                // Capitalize 'E' if needed.
                if (next == 69) argText = argText.toUpperCase();
                // Add sign.
                if (flagAlwaysSigned && currArg >= 0) {
                  argText = '+' + argText;
                }
              }
              // Add padding.
              while (argText.length < width) {
                if (flagLeftAlign) {
                  argText += ' ';
                } else {
                  if (flagZeroPad && (argText[0] == '-' || argText[0] == '+')) {
                    argText = argText[0] + '0' + argText.slice(1);
                  } else {
                    argText = (flagZeroPad ? '0' : ' ') + argText;
                  }
                }
              }
              // Adjust case.
              if (next < 97) argText = argText.toUpperCase();
              // Insert the result into the buffer.
              argText.split('').forEach(function(chr) {
                ret.push(chr.charCodeAt(0));
              });
              break;
            }
            case 's': {
              // String.
              var arg = getNextArg('i8*') || nullString;
              var argLength = _strlen(arg);
              if (precisionSet) argLength = Math.min(argLength, precision);
              if (!flagLeftAlign) {
                while (argLength < width--) {
                  ret.push(32);
                }
              }
              for (var i = 0; i < argLength; i++) {
                ret.push(HEAPU8[((arg++)|0)]);
              }
              if (flagLeftAlign) {
                while (argLength < width--) {
                  ret.push(32);
                }
              }
              break;
            }
            case 'c': {
              // Character.
              if (flagLeftAlign) ret.push(getNextArg('i8'));
              while (--width > 0) {
                ret.push(32);
              }
              if (!flagLeftAlign) ret.push(getNextArg('i8'));
              break;
            }
            case 'n': {
              // Write the length written so far to the next parameter.
              var ptr = getNextArg('i32*');
              HEAP32[((ptr)>>2)]=ret.length
              break;
            }
            case '%': {
              // Literal percent sign.
              ret.push(curr);
              break;
            }
            default: {
              // Unknown specifiers remain untouched.
              for (var i = startTextIndex; i < textIndex + 2; i++) {
                ret.push(HEAP8[(i)]);
              }
            }
          }
          textIndex += 2;
          // TODO: Support a/A (hex float) and m (last error) specifiers.
          // TODO: Support %1${specifier} for arg selection.
        } else {
          ret.push(curr);
          textIndex += 1;
        }
      }
      return ret;
    }function _fprintf(stream, format, varargs) {
      // int fprintf(FILE *restrict stream, const char *restrict format, ...);
      // http://pubs.opengroup.org/onlinepubs/000095399/functions/printf.html
      var result = __formatString(format, varargs);
      var stack = Runtime.stackSave();
      var ret = _fwrite(allocate(result, 'i8', ALLOC_STACK), 1, result.length, stream);
      Runtime.stackRestore(stack);
      return ret;
    }function _printf(format, varargs) {
      // int printf(const char *restrict format, ...);
      // http://pubs.opengroup.org/onlinepubs/000095399/functions/printf.html
      var stdout = HEAP32[((_stdout)>>2)];
      return _fprintf(stdout, format, varargs);
    }
  function _abort() {
      ABORT = true;
      throw 'abort() at ' + (new Error().stack);
    }
  function _memcpy(dest, src, num) {
      dest = dest|0; src = src|0; num = num|0;
      var ret = 0;
      ret = dest|0;
      if ((dest&3) == (src&3)) {
        while (dest & 3) {
          if ((num|0) == 0) return ret|0;
          HEAP8[(dest)]=HEAP8[(src)];
          dest = (dest+1)|0;
          src = (src+1)|0;
          num = (num-1)|0;
        }
        while ((num|0) >= 4) {
          HEAP32[((dest)>>2)]=HEAP32[((src)>>2)];
          dest = (dest+4)|0;
          src = (src+4)|0;
          num = (num-4)|0;
        }
      }
      while ((num|0) > 0) {
        HEAP8[(dest)]=HEAP8[(src)];
        dest = (dest+1)|0;
        src = (src+1)|0;
        num = (num-1)|0;
      }
      return ret|0;
    }
  function _memset(ptr, value, num) {
      ptr = ptr|0; value = value|0; num = num|0;
      var stop = 0, value4 = 0, stop4 = 0, unaligned = 0;
      stop = (ptr + num)|0;
      if ((num|0) >= 20) {
        // This is unaligned, but quite large, so work hard to get to aligned settings
        value = value & 0xff;
        unaligned = ptr & 3;
        value4 = value | (value << 8) | (value << 16) | (value << 24);
        stop4 = stop & ~3;
        if (unaligned) {
          unaligned = (ptr + 4 - unaligned)|0;
          while ((ptr|0) < (unaligned|0)) { // no need to check for stop, since we have large num
            HEAP8[(ptr)]=value;
            ptr = (ptr+1)|0;
          }
        }
        while ((ptr|0) < (stop4|0)) {
          HEAP32[((ptr)>>2)]=value4;
          ptr = (ptr+4)|0;
        }
      }
      while ((ptr|0) < (stop|0)) {
        HEAP8[(ptr)]=value;
        ptr = (ptr+1)|0;
      }
    }
  function _malloc(bytes) {
      /* Over-allocate to make sure it is byte-aligned by 8.
       * This will leak memory, but this is only the dummy
       * implementation (replaced by dlmalloc normally) so
       * not an issue.
       */
      var ptr = Runtime.staticAlloc(bytes + 8);
      return (ptr+8) & 0xFFFFFFF8;
    }
  function _free(){}
  var Browser={};
__ATINIT__.unshift({ func: function() { if (!Module["noFSInit"] && !FS.init.initialized) FS.init() } });__ATMAIN__.push({ func: function() { FS.ignorePermissions = false } });__ATEXIT__.push({ func: function() { FS.quit() } });Module["FS_createFolder"] = FS.createFolder;Module["FS_createPath"] = FS.createPath;Module["FS_createDataFile"] = FS.createDataFile;Module["FS_createPreloadedFile"] = FS.createPreloadedFile;Module["FS_createLazyFile"] = FS.createLazyFile;Module["FS_createLink"] = FS.createLink;Module["FS_createDevice"] = FS.createDevice;
___setErrNo(0);
Module["requestFullScreen"] = function(lockPointer, resizeCanvas) { Browser.requestFullScreen(lockPointer, resizeCanvas) };
  Module["requestAnimationFrame"] = function(func) { Browser.requestAnimationFrame(func) };
  Module["pauseMainLoop"] = function() { Browser.mainLoop.pause() };
  Module["resumeMainLoop"] = function() { Browser.mainLoop.resume() };
var FUNCTION_TABLE = [0,0,_divu,0,_add,0,_Or,0,_shrs,0,_mul
,0,_And,0,_divide,0,_shr,0,_Not,0,_shl,0,_Xor,0,_neg,0,_sub,0];
// EMSCRIPTEN_START_FUNCS
function _neg($x, $y, $z) {
  var label = 0;
  var $1;
  var $2;
  var $3;
  $1=$x;
  $2=$y;
  $3=$z;
  var $4=$1;
  var $5=(((-$4))|0);
  return $5;
}
function _Not($x, $y, $z) {
  var label = 0;
  var $1;
  var $2;
  var $3;
  $1=$x;
  $2=$y;
  $3=$z;
  var $4=$1;
  var $5=$4 ^ -1;
  return $5;
}
function _add($x, $y, $z) {
  var label = 0;
  var $1;
  var $2;
  var $3;
  $1=$x;
  $2=$y;
  $3=$z;
  var $4=$1;
  var $5=$2;
  var $6=((($4)+($5))|0);
  return $6;
}
function _sub($x, $y, $z) {
  var label = 0;
  var $1;
  var $2;
  var $3;
  $1=$x;
  $2=$y;
  $3=$z;
  var $4=$1;
  var $5=$2;
  var $6=((($4)-($5))|0);
  return $6;
}
function _mul($x, $y, $z) {
  var label = 0;
  var $1;
  var $2;
  var $3;
  $1=$x;
  $2=$y;
  $3=$z;
  var $4=$1;
  var $5=$2;
  var $6=Math.imul($4,$5);
  return $6;
}
function _divide($x, $y, $z) {
  var label = 0;
  label = 2; 
  while(1) switch(label) {
    case 2: 
      var $1;
      var $2;
      var $3;
      var $4;
      $2=$x;
      $3=$y;
      $4=$z;
      var $5=$3;
      var $6=(($5)|(0))==0;
      if ($6) { label = 5; break; } else { label = 3; break; }
    case 3: 
      var $8=$3;
      var $9=(($8)|(0))==-1;
      if ($9) { label = 4; break; } else { label = 6; break; }
    case 4: 
      var $11=$2;
      var $12=(($11)|(0))==-2147483648;
      if ($12) { label = 5; break; } else { label = 6; break; }
    case 5: 
      HEAP32[((5242880)>>2)]=1;
      $1=0;
      label = 7; break;
    case 6: 
      var $15=$2;
      var $16=$3;
      var $17=((((($15)|(0)))/((($16)|(0))))&-1);
      $1=$17;
      label = 7; break;
    case 7: 
      var $19=$1;
      return $19;
    default: assert(0, "bad label: " + label);
  }
}
function _divu($x, $y, $z) {
  var label = 0;
  label = 2; 
  while(1) switch(label) {
    case 2: 
      var $1;
      var $2;
      var $3;
      var $4;
      $2=$x;
      $3=$y;
      $4=$z;
      var $5=$3;
      var $6=(($5)|(0))==0;
      if ($6) { label = 3; break; } else { label = 4; break; }
    case 3: 
      HEAP32[((5242880)>>2)]=1;
      $1=0;
      label = 5; break;
    case 4: 
      var $9=$2;
      var $10=$3;
      var $11=Math.floor(((($9)>>>(0)))/((($10)>>>(0))));
      $1=$11;
      label = 5; break;
    case 5: 
      var $13=$1;
      return $13;
    default: assert(0, "bad label: " + label);
  }
}
function _And($x, $y, $z) {
  var label = 0;
  var $1;
  var $2;
  var $3;
  $1=$x;
  $2=$y;
  $3=$z;
  var $4=$1;
  var $5=$2;
  var $6=$4 & $5;
  return $6;
}
function _Or($x, $y, $z) {
  var label = 0;
  var $1;
  var $2;
  var $3;
  $1=$x;
  $2=$y;
  $3=$z;
  var $4=$1;
  var $5=$2;
  var $6=$4 | $5;
  return $6;
}
function _Xor($x, $y, $z) {
  var label = 0;
  var $1;
  var $2;
  var $3;
  $1=$x;
  $2=$y;
  $3=$z;
  var $4=$1;
  var $5=$2;
  var $6=$4 ^ $5;
  return $6;
}
function _shl($x, $y, $z) {
  var label = 0;
  label = 2; 
  while(1) switch(label) {
    case 2: 
      var $1;
      var $2;
      var $3;
      var $4;
      var $s;
      $2=$x;
      $3=$y;
      $4=$z;
      var $5=$3;
      var $6=$5 & 63;
      $s=$6;
      var $7=$s;
      var $8=(($7)|(0)) >= 32;
      if ($8) { label = 3; break; } else { label = 4; break; }
    case 3: 
      $1=0;
      label = 5; break;
    case 4: 
      var $11=$2;
      var $12=$s;
      var $13=$11 << $12;
      $1=$13;
      label = 5; break;
    case 5: 
      var $15=$1;
      return $15;
    default: assert(0, "bad label: " + label);
  }
}
function _shr($x, $y, $z) {
  var label = 0;
  label = 2; 
  while(1) switch(label) {
    case 2: 
      var $1;
      var $2;
      var $3;
      var $4;
      var $s;
      $2=$x;
      $3=$y;
      $4=$z;
      var $5=$3;
      var $6=$5 & 63;
      $s=$6;
      var $7=$s;
      var $8=(($7)|(0)) >= 32;
      if ($8) { label = 3; break; } else { label = 4; break; }
    case 3: 
      $1=0;
      label = 5; break;
    case 4: 
      var $11=$2;
      var $12=$s;
      var $13=$11 >>> (($12)>>>(0));
      $1=$13;
      label = 5; break;
    case 5: 
      var $15=$1;
      return $15;
    default: assert(0, "bad label: " + label);
  }
}
function _shrs($x, $y, $z) {
  var label = 0;
  label = 2; 
  while(1) switch(label) {
    case 2: 
      var $1;
      var $2;
      var $3;
      var $4;
      var $s;
      $2=$x;
      $3=$y;
      $4=$z;
      var $5=$3;
      var $6=$5 & 63;
      $s=$6;
      var $7=$s;
      var $8=(($7)|(0)) >= 32;
      if ($8) { label = 3; break; } else { label = 4; break; }
    case 3: 
      var $10=$2;
      var $11=$10 >> 31;
      $1=$11;
      label = 5; break;
    case 4: 
      var $13=$2;
      var $14=$s;
      var $15=$13 >> (($14)|(0));
      $1=$15;
      label = 5; break;
    case 5: 
      var $17=$1;
      return $17;
    default: assert(0, "bad label: " + label);
  }
}
function _print_expr($opn) {
  var label = 0;
  var __stackBase__  = STACKTOP; assert(!(STACKTOP&3)); assert((STACKTOP|0) < (STACK_MAX|0));
  label = 2; 
  while(1) switch(label) {
    case 2: 
      var $1;
      var $i;
      var $j;
      var $k;
      $1=$opn;
      var $2=$1;
      var $3=(($2)|(0)) < 11;
      if ($3) { label = 3; break; } else { label = 8; break; }
    case 3: 
      var $5=$1;
      var $6=((5242952+($5<<2))|0);
      var $7=HEAP32[(($6)>>2)];
      var $8=-31 <= (($7)|(0));
      if ($8) { label = 4; break; } else { label = 6; break; }
    case 4: 
      var $10=$1;
      var $11=((5242952+($10<<2))|0);
      var $12=HEAP32[(($11)>>2)];
      var $13=(($12)|(0)) <= 31;
      if ($13) { label = 5; break; } else { label = 6; break; }
    case 5: 
      var $15=$1;
      var $16=((5242952+($15<<2))|0);
      var $17=HEAP32[(($16)>>2)];
      var $18=_printf(((5243892)|0), (tempInt=STACKTOP,STACKTOP = (STACKTOP + 4)|0,assert((STACKTOP|0) < (STACK_MAX|0)),HEAP32[((tempInt)>>2)]=$17,tempInt));
      label = 7; break;
    case 6: 
      var $20=$1;
      var $21=((5242952+($20<<2))|0);
      var $22=HEAP32[(($21)>>2)];
      var $23=_printf(((5243884)|0), (tempInt=STACKTOP,STACKTOP = (STACKTOP + 4)|0,assert((STACKTOP|0) < (STACK_MAX|0)),HEAP32[((tempInt)>>2)]=$22,tempInt));
      label = 7; break;
    case 7: 
      label = 19; break;
    case 8: 
      var $26=$1;
      var $27=(($26)|(0))==11;
      if ($27) { label = 9; break; } else { label = 10; break; }
    case 9: 
      var $29=_printf(((5243876)|0), (tempInt=STACKTOP,STACKTOP = (STACKTOP + 1)|0,STACKTOP = ((((STACKTOP)+3)>>2)<<2),assert((STACKTOP|0) < (STACK_MAX|0)),HEAP32[((tempInt)>>2)]=0,tempInt));
      label = 18; break;
    case 10: 
      var $31=$1;
      var $32=((($31)-(12))|0);
      $i=$32;
      var $33=$i;
      var $34=((5243020+($33<<4))|0);
      var $35=(($34)|0);
      var $36=HEAP32[(($35)>>2)];
      $k=$36;
      var $37=$k;
      var $38=((5243104+((($37)*(36))&-1))|0);
      var $39=(($38+28)|0);
      var $40=HEAP32[(($39)>>2)];
      var $41=_printf(((5243872)|0), (tempInt=STACKTOP,STACKTOP = (STACKTOP + 4)|0,assert((STACKTOP|0) < (STACK_MAX|0)),HEAP32[((tempInt)>>2)]=$40,tempInt));
      $j=0;
      label = 11; break;
    case 11: 
      var $43=$j;
      var $44=$k;
      var $45=((5243104+((($44)*(36))&-1))|0);
      var $46=(($45+4)|0);
      var $47=HEAP32[(($46)>>2)];
      var $48=(($43)|(0)) < (($47)|(0));
      if ($48) { label = 12; break; } else { label = 17; break; }
    case 12: 
      var $50=$j;
      var $51=$i;
      var $52=((5243020+($51<<4))|0);
      var $53=(($52+4)|0);
      var $54=(($53+($50<<2))|0);
      var $55=HEAP32[(($54)>>2)];
      _print_expr($55);
      var $56=$j;
      var $57=$k;
      var $58=((5243104+((($57)*(36))&-1))|0);
      var $59=(($58+4)|0);
      var $60=HEAP32[(($59)>>2)];
      var $61=((($60)-(1))|0);
      var $62=(($56)|(0)) < (($61)|(0));
      if ($62) { label = 13; break; } else { label = 14; break; }
    case 13: 
      var $64=$k;
      var $65=((5243104+((($64)*(36))&-1))|0);
      var $66=(($65+32)|0);
      var $67=HEAP32[(($66)>>2)];
      var $68=_printf(((5243872)|0), (tempInt=STACKTOP,STACKTOP = (STACKTOP + 4)|0,assert((STACKTOP|0) < (STACK_MAX|0)),HEAP32[((tempInt)>>2)]=$67,tempInt));
      label = 15; break;
    case 14: 
      var $70=_printf(((5243868)|0), (tempInt=STACKTOP,STACKTOP = (STACKTOP + 1)|0,STACKTOP = ((((STACKTOP)+3)>>2)<<2),assert((STACKTOP|0) < (STACK_MAX|0)),HEAP32[((tempInt)>>2)]=0,tempInt));
      label = 15; break;
    case 15: 
      label = 16; break;
    case 16: 
      var $73=$j;
      var $74=((($73)+(1))|0);
      $j=$74;
      label = 11; break;
    case 17: 
      label = 18; break;
    case 18: 
      label = 19; break;
    case 19: 
      STACKTOP = __stackBase__;
      return;
    default: assert(0, "bad label: " + label);
  }
}
function _print_pgm() {
  var label = 0;
  var __stackBase__  = STACKTOP; assert(!(STACKTOP&3)); assert((STACKTOP|0) < (STACK_MAX|0));
  label = 2; 
  while(1) switch(label) {
    case 2: 
      var $i;
      var $j;
      var $k;
      var $opndj;
      $i=0;
      label = 3; break;
    case 3: 
      var $2=$i;
      var $3=HEAP32[((5243100)>>2)];
      var $4=(($2)|(0)) < (($3)|(0));
      if ($4) { label = 4; break; } else { label = 22; break; }
    case 4: 
      var $6=$i;
      var $7=((5243020+($6<<4))|0);
      var $8=(($7)|0);
      var $9=HEAP32[(($8)>>2)];
      $k=$9;
      var $10=$k;
      var $11=((5243104+((($10)*(36))&-1))|0);
      var $12=(($11+24)|0);
      var $13=HEAP32[(($12)>>2)];
      var $14=$i;
      var $15=((($14)+(1))|0);
      var $16=_printf(((5243852)|0), (tempInt=STACKTOP,STACKTOP = (STACKTOP + 8)|0,assert((STACKTOP|0) < (STACK_MAX|0)),HEAP32[((tempInt)>>2)]=$13,HEAP32[(((tempInt)+(4))>>2)]=$15,tempInt));
      $j=0;
      label = 5; break;
    case 5: 
      var $18=$j;
      var $19=$k;
      var $20=((5243104+((($19)*(36))&-1))|0);
      var $21=(($20+4)|0);
      var $22=HEAP32[(($21)>>2)];
      var $23=(($18)|(0)) < (($22)|(0));
      if ($23) { label = 6; break; } else { label = 20; break; }
    case 6: 
      var $25=$j;
      var $26=$i;
      var $27=((5243020+($26<<4))|0);
      var $28=(($27+4)|0);
      var $29=(($28+($25<<2))|0);
      var $30=HEAP32[(($29)>>2)];
      $opndj=$30;
      var $31=$opndj;
      var $32=(($31)|(0)) < 11;
      if ($32) { label = 7; break; } else { label = 12; break; }
    case 7: 
      var $34=$opndj;
      var $35=((5242952+($34<<2))|0);
      var $36=HEAP32[(($35)>>2)];
      $opndj=$36;
      var $37=$opndj;
      var $38=(($37)|(0)) >= -31;
      if ($38) { label = 8; break; } else { label = 10; break; }
    case 8: 
      var $40=$opndj;
      var $41=(($40)|(0)) <= 31;
      if ($41) { label = 9; break; } else { label = 10; break; }
    case 9: 
      var $43=$opndj;
      var $44=_printf(((5243892)|0), (tempInt=STACKTOP,STACKTOP = (STACKTOP + 4)|0,assert((STACKTOP|0) < (STACK_MAX|0)),HEAP32[((tempInt)>>2)]=$43,tempInt));
      label = 11; break;
    case 10: 
      var $46=$opndj;
      var $47=_printf(((5243884)|0), (tempInt=STACKTOP,STACKTOP = (STACKTOP + 4)|0,assert((STACKTOP|0) < (STACK_MAX|0)),HEAP32[((tempInt)>>2)]=$46,tempInt));
      label = 11; break;
    case 11: 
      label = 16; break;
    case 12: 
      var $50=$opndj;
      var $51=(($50)|(0))==11;
      if ($51) { label = 13; break; } else { label = 14; break; }
    case 13: 
      var $53=_printf(((5243848)|0), (tempInt=STACKTOP,STACKTOP = (STACKTOP + 1)|0,STACKTOP = ((((STACKTOP)+3)>>2)<<2),assert((STACKTOP|0) < (STACK_MAX|0)),HEAP32[((tempInt)>>2)]=0,tempInt));
      label = 15; break;
    case 14: 
      var $55=$opndj;
      var $56=((($55)-(12))|0);
      var $57=((($56)+(1))|0);
      var $58=_printf(((5243844)|0), (tempInt=STACKTOP,STACKTOP = (STACKTOP + 4)|0,assert((STACKTOP|0) < (STACK_MAX|0)),HEAP32[((tempInt)>>2)]=$57,tempInt));
      label = 15; break;
    case 15: 
      label = 16; break;
    case 16: 
      var $61=$j;
      var $62=$k;
      var $63=((5243104+((($62)*(36))&-1))|0);
      var $64=(($63+4)|0);
      var $65=HEAP32[(($64)>>2)];
      var $66=((($65)-(1))|0);
      var $67=(($61)|(0)) < (($66)|(0));
      if ($67) { label = 17; break; } else { label = 18; break; }
    case 17: 
      var $69=_printf(((5243840)|0), (tempInt=STACKTOP,STACKTOP = (STACKTOP + 1)|0,STACKTOP = ((((STACKTOP)+3)>>2)<<2),assert((STACKTOP|0) < (STACK_MAX|0)),HEAP32[((tempInt)>>2)]=0,tempInt));
      label = 18; break;
    case 18: 
      label = 19; break;
    case 19: 
      var $72=$j;
      var $73=((($72)+(1))|0);
      $j=$73;
      label = 5; break;
    case 20: 
      var $75=_printf(((5243836)|0), (tempInt=STACKTOP,STACKTOP = (STACKTOP + 1)|0,STACKTOP = ((((STACKTOP)+3)>>2)<<2),assert((STACKTOP|0) < (STACK_MAX|0)),HEAP32[((tempInt)>>2)]=0,tempInt));
      label = 21; break;
    case 21: 
      var $77=$i;
      var $78=((($77)+(1))|0);
      $i=$78;
      label = 3; break;
    case 22: 
      var $80=_printf(((5243824)|0), (tempInt=STACKTOP,STACKTOP = (STACKTOP + 1)|0,STACKTOP = ((((STACKTOP)+3)>>2)<<2),assert((STACKTOP|0) < (STACK_MAX|0)),HEAP32[((tempInt)>>2)]=0,tempInt));
      var $81=HEAP32[((5243100)>>2)];
      var $82=((($81)-(1))|0);
      var $83=((($82)+(12))|0);
      _print_expr($83);
      var $84=_printf(((5243836)|0), (tempInt=STACKTOP,STACKTOP = (STACKTOP + 1)|0,STACKTOP = ((((STACKTOP)+3)>>2)<<2),assert((STACKTOP|0) < (STACK_MAX|0)),HEAP32[((tempInt)>>2)]=0,tempInt));
      STACKTOP = __stackBase__;
      return;
    default: assert(0, "bad label: " + label);
  }
}
function _check($i) {
  var label = 0;
  label = 2; 
  while(1) switch(label) {
    case 2: 
      var $1;
      var $2;
      var $kx;
      $2=$i;
      label = 3; break;
    case 3: 
      var $4=$2;
      _simulate_one_instruction($4);
      var $5=$2;
      var $6=HEAP32[((5243100)>>2)];
      var $7=((($6)-(1))|0);
      var $8=(($5)|(0)) < (($7)|(0));
      if ($8) { label = 4; break; } else { label = 5; break; }
    case 4: 
      var $10=$2;
      var $11=((($10)+(1))|0);
      $2=$11;
      label = 3; break;
    case 5: 
      var $13=HEAP32[((5242880)>>2)];
      var $14=(($13)|(0))!=0;
      if ($14) { label = 6; break; } else { label = 7; break; }
    case 6: 
      HEAP32[((5242880)>>2)]=0;
      $1=0;
      label = 24; break;
    case 7: 
      var $17=HEAP32[((5243100)>>2)];
      var $18=((($17)-(1))|0);
      var $19=((($18)+(12))|0);
      var $20=((5242952+($19<<2))|0);
      var $21=HEAP32[(($20)>>2)];
      var $22=HEAP32[((5243660)>>2)];
      var $23=(($21)|(0))!=(($22)|(0));
      if ($23) { label = 8; break; } else { label = 9; break; }
    case 8: 
      $1=0;
      label = 24; break;
    case 9: 
      $kx=0;
      label = 10; break;
    case 10: 
      var $27=$kx;
      var $28=(($27)|(0)) < 16;
      if ($28) { label = 11; break; } else { label = 23; break; }
    case 11: 
      var $30=HEAP32[((5243664)>>2)];
      var $31=((($30)+(1))|0);
      HEAP32[((5243664)>>2)]=$31;
      var $32=HEAP32[((5243664)>>2)];
      var $33=(($32)|(0)) >= 17;
      if ($33) { label = 12; break; } else { label = 13; break; }
    case 12: 
      HEAP32[((5243664)>>2)]=0;
      label = 13; break;
    case 13: 
      var $36=HEAP32[((5243664)>>2)];
      var $37=((5242884+($36<<2))|0);
      var $38=HEAP32[(($37)>>2)];
      HEAP32[((((5242996)|0))>>2)]=$38;
      var $39=HEAP32[((5243664)>>2)];
      var $40=((5243592+($39<<2))|0);
      var $41=HEAP32[(($40)>>2)];
      HEAP32[((5243660)>>2)]=$41;
      $2=0;
      label = 14; break;
    case 14: 
      var $43=$2;
      var $44=HEAP32[((5243100)>>2)];
      var $45=(($43)|(0)) < (($44)|(0));
      if ($45) { label = 15; break; } else { label = 17; break; }
    case 15: 
      var $47=$2;
      _simulate_one_instruction($47);
      label = 16; break;
    case 16: 
      var $49=$2;
      var $50=((($49)+(1))|0);
      $2=$50;
      label = 14; break;
    case 17: 
      var $52=HEAP32[((5242880)>>2)];
      var $53=(($52)|(0))!=0;
      if ($53) { label = 18; break; } else { label = 19; break; }
    case 18: 
      HEAP32[((5242880)>>2)]=0;
      $1=0;
      label = 24; break;
    case 19: 
      var $56=HEAP32[((5243100)>>2)];
      var $57=((($56)+(12))|0);
      var $58=((($57)-(1))|0);
      var $59=((5242952+($58<<2))|0);
      var $60=HEAP32[(($59)>>2)];
      var $61=HEAP32[((5243660)>>2)];
      var $62=(($60)|(0))!=(($61)|(0));
      if ($62) { label = 20; break; } else { label = 21; break; }
    case 20: 
      $1=0;
      label = 24; break;
    case 21: 
      label = 22; break;
    case 22: 
      var $66=$kx;
      var $67=((($66)+(1))|0);
      $kx=$67;
      label = 10; break;
    case 23: 
      $1=1;
      label = 24; break;
    case 24: 
      var $70=$1;
      return $70;
    default: assert(0, "bad label: " + label);
  }
}
function _simulate_one_instruction($i) {
  var label = 0;
  var $1;
  var $arg0;
  var $arg1;
  var $arg2;
  $1=$i;
  var $2=$1;
  var $3=((5243020+($2<<4))|0);
  var $4=(($3+4)|0);
  var $5=(($4)|0);
  var $6=HEAP32[(($5)>>2)];
  var $7=((5242952+($6<<2))|0);
  var $8=HEAP32[(($7)>>2)];
  $arg0=$8;
  var $9=$1;
  var $10=((5243020+($9<<4))|0);
  var $11=(($10+4)|0);
  var $12=(($11+4)|0);
  var $13=HEAP32[(($12)>>2)];
  var $14=((5242952+($13<<2))|0);
  var $15=HEAP32[(($14)>>2)];
  $arg1=$15;
  var $16=$1;
  var $17=((5243020+($16<<4))|0);
  var $18=(($17+4)|0);
  var $19=(($18+8)|0);
  var $20=HEAP32[(($19)>>2)];
  var $21=((5242952+($20<<2))|0);
  var $22=HEAP32[(($21)>>2)];
  $arg2=$22;
  var $23=$1;
  var $24=((5243020+($23<<4))|0);
  var $25=(($24)|0);
  var $26=HEAP32[(($25)>>2)];
  var $27=((5243104+((($26)*(36))&-1))|0);
  var $28=(($27)|0);
  var $29=HEAP32[(($28)>>2)];
  var $30=$arg0;
  var $31=$arg1;
  var $32=$arg2;
  var $33=FUNCTION_TABLE[$29]($30, $31, $32);
  var $34=$1;
  var $35=((($34)+(12))|0);
  var $36=((5242952+($35<<2))|0);
  HEAP32[(($36)>>2)]=$33;
  var $37=$1;
  var $38=((5243572+($37<<2))|0);
  var $39=HEAP32[(($38)>>2)];
  var $40=((($39)+(1))|0);
  var $41=$1;
  var $42=((5243572+($41<<2))|0);
  HEAP32[(($42)>>2)]=$40;
  return;
}
function _fix_operands($i) {
  var label = 0;
  label = 2; 
  while(1) switch(label) {
    case 2: 
      var $1;
      var $rs;
      var $rt;
      var $k;
      $1=$i;
      var $2=$1;
      var $3=((5243020+($2<<4))|0);
      var $4=(($3)|0);
      var $5=HEAP32[(($4)>>2)];
      $k=$5;
      var $6=$1;
      var $7=HEAP32[((5243100)>>2)];
      var $8=((($7)-(1))|0);
      var $9=(($6)|(0))==(($8)|(0));
      if ($9) { label = 3; break; } else { label = 19; break; }
    case 3: 
      var $11=HEAP32[((5243100)>>2)];
      var $12=((($11)+(12))|0);
      var $13=((($12)-(2))|0);
      $rs=$13;
      var $14=$1;
      var $15=((5243020+($14<<4))|0);
      var $16=(($15+4)|0);
      var $17=(($16+4)|0);
      var $18=HEAP32[(($17)>>2)];
      var $19=$rs;
      var $20=(($18)|(0))!=(($19)|(0));
      if ($20) { label = 4; break; } else { label = 6; break; }
    case 4: 
      var $22=$1;
      var $23=((5243020+($22<<4))|0);
      var $24=(($23+4)|0);
      var $25=(($24+8)|0);
      var $26=HEAP32[(($25)>>2)];
      var $27=$rs;
      var $28=(($26)|(0))!=(($27)|(0));
      if ($28) { label = 5; break; } else { label = 6; break; }
    case 5: 
      var $30=$rs;
      var $31=$1;
      var $32=((5243020+($31<<4))|0);
      var $33=(($32+4)|0);
      var $34=(($33)|0);
      HEAP32[(($34)>>2)]=$30;
      label = 6; break;
    case 6: 
      var $36=$rs;
      var $37=((($36)-(1))|0);
      $rt=$37;
      var $38=$1;
      var $39=((($38)-(1))|0);
      var $40=((5243020+($39<<4))|0);
      var $41=(($40+4)|0);
      var $42=(($41)|0);
      var $43=HEAP32[(($42)>>2)];
      var $44=$rt;
      var $45=(($43)|(0))!=(($44)|(0));
      if ($45) { label = 7; break; } else { label = 18; break; }
    case 7: 
      var $47=$1;
      var $48=((($47)-(1))|0);
      var $49=((5243020+($48<<4))|0);
      var $50=(($49+4)|0);
      var $51=(($50+4)|0);
      var $52=HEAP32[(($51)>>2)];
      var $53=$rt;
      var $54=(($52)|(0))!=(($53)|(0));
      if ($54) { label = 8; break; } else { label = 18; break; }
    case 8: 
      var $56=$1;
      var $57=((($56)-(1))|0);
      var $58=((5243020+($57<<4))|0);
      var $59=(($58+4)|0);
      var $60=(($59+8)|0);
      var $61=HEAP32[(($60)>>2)];
      var $62=$rt;
      var $63=(($61)|(0))!=(($62)|(0));
      if ($63) { label = 9; break; } else { label = 18; break; }
    case 9: 
      var $65=$1;
      var $66=((5243020+($65<<4))|0);
      var $67=(($66+4)|0);
      var $68=(($67+4)|0);
      var $69=HEAP32[(($68)>>2)];
      var $70=$rt;
      var $71=(($69)|(0))!=(($70)|(0));
      if ($71) { label = 10; break; } else { label = 18; break; }
    case 10: 
      var $73=$1;
      var $74=((5243020+($73<<4))|0);
      var $75=(($74+4)|0);
      var $76=(($75+8)|0);
      var $77=HEAP32[(($76)>>2)];
      var $78=$rt;
      var $79=(($77)|(0))!=(($78)|(0));
      if ($79) { label = 11; break; } else { label = 18; break; }
    case 11: 
      var $81=$rt;
      var $82=(($81)|(0)) >= 12;
      if ($82) { label = 12; break; } else { label = 18; break; }
    case 12: 
      var $84=$1;
      var $85=((5243020+($84<<4))|0);
      var $86=(($85+4)|0);
      var $87=(($86)|0);
      var $88=HEAP32[(($87)>>2)];
      var $89=$rt;
      var $90=(($88)|(0)) < (($89)|(0));
      if ($90) { label = 13; break; } else { label = 14; break; }
    case 13: 
      var $92=$rt;
      var $93=$1;
      var $94=((5243020+($93<<4))|0);
      var $95=(($94+4)|0);
      var $96=(($95)|0);
      HEAP32[(($96)>>2)]=$92;
      label = 17; break;
    case 14: 
      var $98=$k;
      var $99=((5243104+((($98)*(36))&-1))|0);
      var $100=(($99+4)|0);
      var $101=HEAP32[(($100)>>2)];
      var $102=(($101)|(0)) > 1;
      if ($102) { label = 15; break; } else { label = 16; break; }
    case 15: 
      var $104=$rt;
      var $105=$1;
      var $106=((5243020+($105<<4))|0);
      var $107=(($106+4)|0);
      var $108=(($107+4)|0);
      HEAP32[(($108)>>2)]=$104;
      label = 16; break;
    case 16: 
      label = 17; break;
    case 17: 
      label = 18; break;
    case 18: 
      label = 19; break;
    case 19: 
      var $113=$k;
      var $114=((5243104+((($113)*(36))&-1))|0);
      var $115=(($114+8)|0);
      var $116=HEAP32[(($115)>>2)];
      var $117=(($116)|(0))!=0;
      if ($117) { label = 20; break; } else { label = 23; break; }
    case 20: 
      var $119=$1;
      var $120=((5243020+($119<<4))|0);
      var $121=(($120+4)|0);
      var $122=(($121)|0);
      var $123=HEAP32[(($122)>>2)];
      var $124=$1;
      var $125=((5243020+($124<<4))|0);
      var $126=(($125+4)|0);
      var $127=(($126+4)|0);
      var $128=HEAP32[(($127)>>2)];
      var $129=(($123)|(0)) < (($128)|(0));
      if ($129) { label = 21; break; } else { label = 22; break; }
    case 21: 
      var $131=$1;
      var $132=((5243020+($131<<4))|0);
      var $133=(($132+4)|0);
      var $134=(($133+4)|0);
      var $135=HEAP32[(($134)>>2)];
      var $136=$1;
      var $137=((5243020+($136<<4))|0);
      var $138=(($137+4)|0);
      var $139=(($138)|0);
      HEAP32[(($139)>>2)]=$135;
      label = 22; break;
    case 22: 
      label = 31; break;
    case 23: 
      var $142=$1;
      var $143=HEAP32[((5243100)>>2)];
      var $144=((($143)-(1))|0);
      var $145=(($142)|(0))!=(($144)|(0));
      if ($145) { label = 24; break; } else { label = 31; break; }
    case 24: 
      var $147=$1;
      var $148=((5243020+($147<<4))|0);
      var $149=(($148+4)|0);
      var $150=(($149)|0);
      var $151=HEAP32[(($150)>>2)];
      var $152=(($151)|(0)) < 11;
      if ($152) { label = 25; break; } else { label = 30; break; }
    case 25: 
      var $154=$1;
      var $155=((5243020+($154<<4))|0);
      var $156=(($155+4)|0);
      var $157=(($156+4)|0);
      var $158=HEAP32[(($157)>>2)];
      var $159=(($158)|(0)) < 11;
      if ($159) { label = 26; break; } else { label = 30; break; }
    case 26: 
      var $161=$1;
      var $162=((5243020+($161<<4))|0);
      var $163=(($162+4)|0);
      var $164=(($163+8)|0);
      var $165=HEAP32[(($164)>>2)];
      var $166=(($165)|(0)) < 11;
      if ($166) { label = 27; break; } else { label = 30; break; }
    case 27: 
      var $168=$k;
      var $169=((5243104+((($168)*(36))&-1))|0);
      var $170=(($169+8)|0);
      var $171=HEAP32[(($170)>>2)];
      var $172=(($171)|(0))!=0;
      if ($172) { label = 28; break; } else { label = 29; break; }
    case 28: 
      _abort();
      throw "Reached an unreachable!"
    case 29: 
      var $175=$1;
      var $176=((5243020+($175<<4))|0);
      var $177=(($176+4)|0);
      var $178=(($177)|0);
      HEAP32[(($178)>>2)]=11;
      label = 30; break;
    case 30: 
      label = 31; break;
    case 31: 
      return;
    default: assert(0, "bad label: " + label);
  }
}
function _userfun($x) {
  var label = 0;
  var $1;
  $1=$x;
  var $2=$1;
  var $3=$2 & -4;
  var $4=$1;
  var $5=$4 & 1;
  var $6=$5 << 1;
  var $7=$3 | $6;
  var $8=$1;
  var $9=$8 & 2;
  var $10=$9 >> 1;
  var $11=$7 | $10;
  return $11;
}
function _search() {
  var label = 0;
  var __stackBase__  = STACKTOP; assert(!(STACKTOP&3)); assert((STACKTOP|0) < (STACK_MAX|0));
  label = 2; 
  while(1) switch(label) {
    case 2: 
      var $1;
      var $ok;
      var $i;
      var $num_solutions;
      var $2=HEAP32[((((5242884)|0))>>2)];
      HEAP32[((((5242996)|0))>>2)]=$2;
      var $3=HEAP32[((((5243592)|0))>>2)];
      HEAP32[((5243660)>>2)]=$3;
      $num_solutions=0;
      $i=0;
      label = 3; break;
    case 3: 
      var $5=$i;
      var $6=_check($5);
      $ok=$6;
      var $7=$ok;
      var $8=(($7)|(0))!=0;
      if ($8) { label = 4; break; } else { label = 7; break; }
    case 4: 
      var $10=$num_solutions;
      var $11=((($10)+(1))|0);
      $num_solutions=$11;
      var $12=HEAP32[((5243100)>>2)];
      var $13=_printf(((5243792)|0), (tempInt=STACKTOP,STACKTOP = (STACKTOP + 4)|0,assert((STACKTOP|0) < (STACK_MAX|0)),HEAP32[((tempInt)>>2)]=$12,tempInt));
      _print_pgm(3);
      var $14=$num_solutions;
      var $15=(($14)|(0))==5;
      if ($15) { label = 5; break; } else { label = 6; break; }
    case 5: 
      var $17=$num_solutions;
      $1=$17;
      label = 10; break;
    case 6: 
      label = 7; break;
    case 7: 
      var $20=_increment();
      $i=$20;
      label = 8; break;
    case 8: 
      var $22=$i;
      var $23=(($22)|(0)) >= 0;
      if ($23) { label = 3; break; } else { label = 9; break; }
    case 9: 
      var $25=$num_solutions;
      $1=$25;
      label = 10; break;
    case 10: 
      var $27=$1;
      STACKTOP = __stackBase__;
      return $27;
    default: assert(0, "bad label: " + label);
  }
}
function _increment() {
  var label = 0;
  label = 2; 
  while(1) switch(label) {
    case 2: 
      var $1;
      var $i;
      var $j;
      var $k;
      var $opndj;
      var $nopnds;
      var $2=HEAP32[((5243100)>>2)];
      var $3=((($2)-(1))|0);
      $i=$3;
      label = 3; break;
    case 3: 
      var $5=$i;
      var $6=(($5)|(0)) >= 0;
      if ($6) { label = 4; break; } else { label = 24; break; }
    case 4: 
      var $8=$i;
      var $9=((5243020+($8<<4))|0);
      var $10=(($9)|0);
      var $11=HEAP32[(($10)>>2)];
      $k=$11;
      var $12=$k;
      var $13=((5243104+((($12)*(36))&-1))|0);
      var $14=(($13+4)|0);
      var $15=HEAP32[(($14)>>2)];
      $nopnds=$15;
      $j=0;
      label = 5; break;
    case 5: 
      var $17=$j;
      var $18=$nopnds;
      var $19=(($17)|(0)) < (($18)|(0));
      if ($19) { label = 6; break; } else { label = 16; break; }
    case 6: 
      var $21=$j;
      var $22=$i;
      var $23=((5243020+($22<<4))|0);
      var $24=(($23+4)|0);
      var $25=(($24+($21<<2))|0);
      var $26=HEAP32[(($25)>>2)];
      $opndj=$26;
      var $27=$opndj;
      var $28=(($27)|(0)) < 6;
      if ($28) { label = 7; break; } else { label = 8; break; }
    case 7: 
      var $30=$j;
      var $31=$i;
      var $32=((5243020+($31<<4))|0);
      var $33=(($32+4)|0);
      var $34=(($33+($30<<2))|0);
      var $35=HEAP32[(($34)>>2)];
      var $36=((($35)+(1))|0);
      HEAP32[(($34)>>2)]=$36;
      label = 16; break;
    case 8: 
      var $38=$opndj;
      var $39=(($38)|(0))==6;
      if ($39) { label = 9; break; } else { label = 10; break; }
    case 9: 
      var $41=$j;
      var $42=$i;
      var $43=((5243020+($42<<4))|0);
      var $44=(($43+4)|0);
      var $45=(($44+($41<<2))|0);
      HEAP32[(($45)>>2)]=11;
      label = 16; break;
    case 10: 
      var $47=$opndj;
      var $48=$i;
      var $49=((($48)+(12))|0);
      var $50=((($49)-(1))|0);
      var $51=(($47)|(0)) < (($50)|(0));
      if ($51) { label = 11; break; } else { label = 12; break; }
    case 11: 
      var $53=$j;
      var $54=$i;
      var $55=((5243020+($54<<4))|0);
      var $56=(($55+4)|0);
      var $57=(($56+($53<<2))|0);
      var $58=HEAP32[(($57)>>2)];
      var $59=((($58)+(1))|0);
      HEAP32[(($57)>>2)]=$59;
      label = 16; break;
    case 12: 
      label = 13; break;
    case 13: 
      label = 14; break;
    case 14: 
      var $63=$j;
      var $64=$k;
      var $65=((5243104+((($64)*(36))&-1))|0);
      var $66=(($65+12)|0);
      var $67=(($66+($63<<2))|0);
      var $68=HEAP32[(($67)>>2)];
      var $69=$j;
      var $70=$i;
      var $71=((5243020+($70<<4))|0);
      var $72=(($71+4)|0);
      var $73=(($72+($69<<2))|0);
      HEAP32[(($73)>>2)]=$68;
      label = 15; break;
    case 15: 
      var $75=$j;
      var $76=((($75)+(1))|0);
      $j=$76;
      label = 5; break;
    case 16: 
      var $78=$j;
      var $79=(($78)|(0))==0;
      if ($79) { label = 17; break; } else { label = 18; break; }
    case 17: 
      var $81=$i;
      $1=$81;
      label = 25; break;
    case 18: 
      var $83=$j;
      var $84=$nopnds;
      var $85=(($83)|(0)) < (($84)|(0));
      if ($85) { label = 19; break; } else { label = 20; break; }
    case 19: 
      var $87=$i;
      _fix_operands($87);
      var $88=$i;
      $1=$88;
      label = 25; break;
    case 20: 
      var $90=$k;
      var $91=(($90)|(0)) < 12;
      if ($91) { label = 21; break; } else { label = 22; break; }
    case 21: 
      var $93=$k;
      var $94=((($93)+(1))|0);
      $k=$94;
      var $95=$k;
      var $96=$i;
      var $97=((5243020+($96<<4))|0);
      var $98=(($97)|0);
      HEAP32[(($98)>>2)]=$95;
      var $99=$k;
      var $100=((5243104+((($99)*(36))&-1))|0);
      var $101=(($100+12)|0);
      var $102=(($101)|0);
      var $103=HEAP32[(($102)>>2)];
      var $104=$i;
      var $105=((5243020+($104<<4))|0);
      var $106=(($105+4)|0);
      var $107=(($106)|0);
      HEAP32[(($107)>>2)]=$103;
      var $108=$k;
      var $109=((5243104+((($108)*(36))&-1))|0);
      var $110=(($109+12)|0);
      var $111=(($110+4)|0);
      var $112=HEAP32[(($111)>>2)];
      var $113=$i;
      var $114=((5243020+($113<<4))|0);
      var $115=(($114+4)|0);
      var $116=(($115+4)|0);
      HEAP32[(($116)>>2)]=$112;
      var $117=$k;
      var $118=((5243104+((($117)*(36))&-1))|0);
      var $119=(($118+12)|0);
      var $120=(($119+8)|0);
      var $121=HEAP32[(($120)>>2)];
      var $122=$i;
      var $123=((5243020+($122<<4))|0);
      var $124=(($123+4)|0);
      var $125=(($124+8)|0);
      HEAP32[(($125)>>2)]=$121;
      var $126=$i;
      _fix_operands($126);
      var $127=$i;
      $1=$127;
      label = 25; break;
    case 22: 
      var $129=$i;
      var $130=((5243020+($129<<4))|0);
      var $131=(($130)|0);
      HEAP32[(($131)>>2)]=0;
      var $132=HEAP32[((((5243116)|0))>>2)];
      var $133=$i;
      var $134=((5243020+($133<<4))|0);
      var $135=(($134+4)|0);
      var $136=(($135)|0);
      HEAP32[(($136)>>2)]=$132;
      var $137=HEAP32[((((5243120)|0))>>2)];
      var $138=$i;
      var $139=((5243020+($138<<4))|0);
      var $140=(($139+4)|0);
      var $141=(($140+4)|0);
      HEAP32[(($141)>>2)]=$137;
      var $142=HEAP32[((((5243124)|0))>>2)];
      var $143=$i;
      var $144=((5243020+($143<<4))|0);
      var $145=(($144+4)|0);
      var $146=(($145+8)|0);
      HEAP32[(($146)>>2)]=$142;
      var $147=$i;
      _fix_operands($147);
      label = 23; break;
    case 23: 
      var $149=$i;
      var $150=((($149)-(1))|0);
      $i=$150;
      label = 3; break;
    case 24: 
      $1=-1;
      label = 25; break;
    case 25: 
      var $153=$1;
      return $153;
    default: assert(0, "bad label: " + label);
  }
}
function _main($argc, $argv) {
  var label = 0;
  var __stackBase__  = STACKTOP; assert(!(STACKTOP&3)); assert((STACKTOP|0) < (STACK_MAX|0));
  label = 2; 
  while(1) switch(label) {
    case 2: 
      var $1;
      var $2;
      var $3;
      var $i;
      var $num_sol;
      var $total;
      $1=0;
      $2=$argc;
      $3=$argv;
      $num_sol=0;
      HEAP32[((5243100)>>2)]=1;
      label = 3; break;
    case 3: 
      var $5=HEAP32[((5243100)>>2)];
      var $6=(($5)|(0)) <= 5;
      if ($6) { label = 4; break; } else { var $11 = 0;label = 5; break; }
    case 4: 
      var $8=$num_sol;
      var $9=(($8)|(0))==0;
      var $11 = $9;label = 5; break;
    case 5: 
      var $11;
      if ($11) { label = 6; break; } else { label = 20; break; }
    case 6: 
      var $13=HEAP32[((5243100)>>2)];
      var $14=_printf(((5243744)|0), (tempInt=STACKTOP,STACKTOP = (STACKTOP + 4)|0,assert((STACKTOP|0) < (STACK_MAX|0)),HEAP32[((tempInt)>>2)]=$13,tempInt));
      $i=0;
      label = 7; break;
    case 7: 
      var $16=$i;
      var $17=(($16)|(0)) < 17;
      if ($17) { label = 8; break; } else { label = 10; break; }
    case 8: 
      var $19=$i;
      var $20=((5242884+($19<<2))|0);
      var $21=HEAP32[(($20)>>2)];
      var $22=_userfun($21);
      var $23=$i;
      var $24=((5243592+($23<<2))|0);
      HEAP32[(($24)>>2)]=$22;
      label = 9; break;
    case 9: 
      var $26=$i;
      var $27=((($26)+(1))|0);
      $i=$27;
      label = 7; break;
    case 10: 
      $i=0;
      label = 11; break;
    case 11: 
      var $30=$i;
      var $31=HEAP32[((5243100)>>2)];
      var $32=(($30)|(0)) < (($31)|(0));
      if ($32) { label = 12; break; } else { label = 14; break; }
    case 12: 
      var $34=$i;
      var $35=((5243020+($34<<4))|0);
      var $36=(($35)|0);
      HEAP32[(($36)>>2)]=0;
      var $37=HEAP32[((((5243116)|0))>>2)];
      var $38=$i;
      var $39=((5243020+($38<<4))|0);
      var $40=(($39+4)|0);
      var $41=(($40)|0);
      HEAP32[(($41)>>2)]=$37;
      var $42=HEAP32[((((5243120)|0))>>2)];
      var $43=$i;
      var $44=((5243020+($43<<4))|0);
      var $45=(($44+4)|0);
      var $46=(($45+4)|0);
      HEAP32[(($46)>>2)]=$42;
      var $47=HEAP32[((((5243124)|0))>>2)];
      var $48=$i;
      var $49=((5243020+($48<<4))|0);
      var $50=(($49+4)|0);
      var $51=(($50+8)|0);
      HEAP32[(($51)>>2)]=$47;
      var $52=$i;
      _fix_operands($52);
      label = 13; break;
    case 13: 
      var $54=$i;
      var $55=((($54)+(1))|0);
      $i=$55;
      label = 11; break;
    case 14: 
      var $57=_search();
      $num_sol=$57;
      var $58=$num_sol;
      var $59=_printf(((5243720)|0), (tempInt=STACKTOP,STACKTOP = (STACKTOP + 4)|0,assert((STACKTOP|0) < (STACK_MAX|0)),HEAP32[((tempInt)>>2)]=$58,tempInt));
      $total=0;
      var $60=_printf(((5243708)|0), (tempInt=STACKTOP,STACKTOP = (STACKTOP + 1)|0,STACKTOP = ((((STACKTOP)+3)>>2)<<2),assert((STACKTOP|0) < (STACK_MAX|0)),HEAP32[((tempInt)>>2)]=0,tempInt));
      $i=0;
      label = 15; break;
    case 15: 
      var $62=$i;
      var $63=HEAP32[((5243100)>>2)];
      var $64=(($62)|(0)) < (($63)|(0));
      if ($64) { label = 16; break; } else { label = 18; break; }
    case 16: 
      var $66=$i;
      var $67=((5243572+($66<<2))|0);
      var $68=HEAP32[(($67)>>2)];
      var $69=_printf(((5243700)|0), (tempInt=STACKTOP,STACKTOP = (STACKTOP + 4)|0,assert((STACKTOP|0) < (STACK_MAX|0)),HEAP32[((tempInt)>>2)]=$68,tempInt));
      var $70=$total;
      var $71=$i;
      var $72=((5243572+($71<<2))|0);
      var $73=HEAP32[(($72)>>2)];
      var $74=((($70)+($73))|0);
      $total=$74;
      label = 17; break;
    case 17: 
      var $76=$i;
      var $77=((($76)+(1))|0);
      $i=$77;
      label = 15; break;
    case 18: 
      var $79=$total;
      var $80=_printf(((5243688)|0), (tempInt=STACKTOP,STACKTOP = (STACKTOP + 4)|0,assert((STACKTOP|0) < (STACK_MAX|0)),HEAP32[((tempInt)>>2)]=$79,tempInt));
      label = 19; break;
    case 19: 
      var $82=HEAP32[((5243100)>>2)];
      var $83=((($82)+(1))|0);
      HEAP32[((5243100)>>2)]=$83;
      label = 3; break;
    case 20: 
      STACKTOP = __stackBase__;
      return 0;
    default: assert(0, "bad label: " + label);
  }
}
Module["_main"] = _main;
// EMSCRIPTEN_END_FUNCS
// EMSCRIPTEN_END_FUNCS
// Warning: printing of i64 values may be slightly rounded! No deep i64 math used, so precise i64 code not included
var i64Math = null;
// === Auto-generated postamble setup entry stuff ===
Module.callMain = function callMain(args) {
  var argc = args.length+1;
  function pad() {
    for (var i = 0; i < 4-1; i++) {
      argv.push(0);
    }
  }
  var argv = [allocate(intArrayFromString("/bin/this.program"), 'i8', ALLOC_STATIC) ];
  pad();
  for (var i = 0; i < argc-1; i = i + 1) {
    argv.push(allocate(intArrayFromString(args[i]), 'i8', ALLOC_STATIC));
    pad();
  }
  argv.push(0);
  argv = allocate(argv, 'i32', ALLOC_STATIC);
  var ret;
  var initialStackTop = STACKTOP;
  try {
    ret = Module['_main'](argc, argv, 0);
  }
  catch(e) {
    if (e.name == 'ExitStatus') {
      return e.status;
    } else if (e == 'SimulateInfiniteLoop') {
      Module['noExitRuntime'] = true;
    } else {
      throw e;
    }
  } finally {
    STACKTOP = initialStackTop;
  }
  return ret;
}
function run(args) {
  args = args || Module['arguments'];
  if (runDependencies > 0) {
    Module.printErr('run() called, but dependencies remain, so not running');
    return 0;
  }
  if (Module['preRun']) {
    if (typeof Module['preRun'] == 'function') Module['preRun'] = [Module['preRun']];
    var toRun = Module['preRun'];
    Module['preRun'] = [];
    for (var i = toRun.length-1; i >= 0; i--) {
      toRun[i]();
    }
    if (runDependencies > 0) {
      // a preRun added a dependency, run will be called later
      return 0;
    }
  }
  function doRun() {
    var ret = 0;
    calledRun = true;
    if (Module['_main']) {
      preMain();
      ret = Module.callMain(args);
      if (!Module['noExitRuntime']) {
        exitRuntime();
      }
    }
    if (Module['postRun']) {
      if (typeof Module['postRun'] == 'function') Module['postRun'] = [Module['postRun']];
      while (Module['postRun'].length > 0) {
        Module['postRun'].pop()();
      }
    }
    return ret;
  }
  if (Module['setStatus']) {
    Module['setStatus']('Running...');
    setTimeout(function() {
      setTimeout(function() {
        Module['setStatus']('');
      }, 1);
      doRun();
    }, 1);
    return 0;
  } else {
    return doRun();
  }
}
Module['run'] = Module.run = run;
// {{PRE_RUN_ADDITIONS}}
if (Module['preInit']) {
  if (typeof Module['preInit'] == 'function') Module['preInit'] = [Module['preInit']];
  while (Module['preInit'].length > 0) {
    Module['preInit'].pop()();
  }
}
initRuntime();
var shouldRunNow = true;
if (Module['noInitialRun']) {
  shouldRunNow = false;
}
if (shouldRunNow) {
  run();
}
// {{POST_RUN_ADDITIONS}}
  // {{MODULE_ADDITIONS}}
