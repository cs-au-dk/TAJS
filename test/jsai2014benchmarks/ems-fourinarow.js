
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
STATICTOP += 476;
assert(STATICTOP < TOTAL_MEMORY);
var _stdout;
var _stderr;
allocate(4, "i8", ALLOC_NONE, 5242880);
allocate(1, "i8", ALLOC_NONE, 5242884);
allocate([67,111,109,112,105,108,101,114,32,115,119,105,116,99,104,101,115,58,32,37,115,10,0] /* Compiler switches: % */, "i8", ALLOC_NONE, 5242888);
allocate([116,111,100,97,121,0] /* today\00 */, "i8", ALLOC_NONE, 5242912);
allocate([67,111,109,112,105,108,101,32,100,97,116,101,58,32,37,115,10,0] /* Compile date: %s\0A\ */, "i8", ALLOC_NONE, 5242920);
allocate([69,82,82,79,82,58,32,85,110,107,110,111,119,110,32,112,108,97,121,101,114,46,10,0] /* ERROR: Unknown playe */, "i8", ALLOC_NONE, 5242940);
allocate([69,82,82,79,82,58,32,70,97,117,108,116,121,32,99,111,108,117,109,110,58,32,37,100,46,10,0] /* ERROR: Faulty column */, "i8", ALLOC_NONE, 5242964);
allocate([45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,10,0] /* ----------------\0A\ */, "i8", ALLOC_NONE, 5242992);
allocate([73,116,39,115,32,97,32,116,105,101,46,10,0] /* It's a tie.\0A\00 */, "i8", ALLOC_NONE, 5243012);
allocate([84,104,101,32,99,111,109,112,117,116,101,114,32,105,115,32,116,104,101,32,119,105,110,110,101,114,46,10,0] /* The computer is the  */, "i8", ALLOC_NONE, 5243028);
allocate([84,104,101,32,112,108,97,121,101,114,32,105,115,32,116,104,101,32,119,105,110,110,101,114,46,10,0] /* The player is the wi */, "i8", ALLOC_NONE, 5243060);
allocate([37,99,32,0] /* %c \00 */, "i8", ALLOC_NONE, 5243088);
allocate([85,115,105,110,103,32,112,114,117,110,105,110,103,32,109,101,116,104,111,100,32,50,10,0] /* Using pruning method */, "i8", ALLOC_NONE, 5243092);
allocate([85,115,105,110,103,32,112,114,117,110,105,110,103,32,109,101,116,104,111,100,32,49,10,0] /* Using pruning method */, "i8", ALLOC_NONE, 5243116);
allocate([111,102,102,0] /* off\00 */, "i8", ALLOC_NONE, 5243140);
allocate([111,110,0] /* on\00 */, "i8", ALLOC_NONE, 5243144);
allocate([65,108,112,104,97,45,66,101,116,97,32,112,114,117,110,105,110,103,58,32,37,115,10,0] /* Alpha-Beta pruning:  */, "i8", ALLOC_NONE, 5243148);
allocate([82,101,99,117,114,115,105,111,110,32,100,101,112,116,104,58,32,37,100,10,0] /* Recursion depth: %d\ */, "i8", ALLOC_NONE, 5243172);
allocate([37,100,0] /* %d\00 */, "i8", ALLOC_NONE, 5243196);
allocate([69,82,82,79,82,58,32,67,111,117,108,100,32,110,111,116,32,111,112,101,110,32,105,110,100,97,116,97,32,102,105,108,101,10,0] /* ERROR: Could not ope */, "i8", ALLOC_NONE, 5243200);
allocate([116,101,115,116,46,105,110,0] /* test.in\00 */, "i8", ALLOC_NONE, 5243236);
allocate([114,0] /* r\00 */, "i8", ALLOC_NONE, 5243244);
allocate([37,100,32,0] /* %d \00 */, "i8", ALLOC_NONE, 5243248);
allocate([32,37,100,0] /*  %d\00 */, "i8", ALLOC_NONE, 5243252);
allocate([3,0,0,0], "i8", ALLOC_NONE, 5243256);
allocate(8, "i8", ALLOC_NONE, 5243260);
allocate(8, "i8", ALLOC_NONE, 5243268);
allocate(8, "i8", ALLOC_NONE, 5243276);
allocate(8, "i8", ALLOC_NONE, 5243284);
allocate(8, "i8", ALLOC_NONE, 5243292);
allocate(8, "i8", ALLOC_NONE, 5243300);
allocate(8, "i8", ALLOC_NONE, 5243308);
allocate(8, "i8", ALLOC_NONE, 5243316);
allocate(8, "i8", ALLOC_NONE, 5243324);
allocate(8, "i8", ALLOC_NONE, 5243332);
allocate(8, "i8", ALLOC_NONE, 5243340);
allocate(8, "i8", ALLOC_NONE, 5243348);
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
    }function _fputc(c, stream) {
      // int fputc(int c, FILE *stream);
      // http://pubs.opengroup.org/onlinepubs/000095399/functions/fputc.html
      var chr = unSign(c & 0xFF);
      HEAP8[((_fputc.ret)|0)]=chr
      var ret = _write(stream, _fputc.ret, 1);
      if (ret == -1) {
        if (FS.streams[stream]) FS.streams[stream].error = true;
        return -1;
      } else {
        return chr;
      }
    }var _putc=_fputc;
  function _fwrite(ptr, size, nitems, stream) {
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
  var ___dirent_struct_layout={__size__:1040,d_ino:0,d_name:4,d_off:1028,d_reclen:1032,d_type:1036};function _open(path, oflag, varargs) {
      // int open(const char *path, int oflag, ...);
      // http://pubs.opengroup.org/onlinepubs/009695399/functions/open.html
      // NOTE: This implementation tries to mimic glibc rather than strictly
      // following the POSIX standard.
      var mode = HEAP32[((varargs)>>2)];
      // Simplify flags.
      var accessMode = oflag & 3;
      var isWrite = accessMode != 0;
      var isRead = accessMode != 1;
      var isCreate = Boolean(oflag & 512);
      var isExistCheck = Boolean(oflag & 2048);
      var isTruncate = Boolean(oflag & 1024);
      var isAppend = Boolean(oflag & 8);
      // Verify path.
      var origPath = path;
      path = FS.analyzePath(Pointer_stringify(path));
      if (!path.parentExists) {
        ___setErrNo(path.error);
        return -1;
      }
      var target = path.object || null;
      var finalPath;
      // Verify the file exists, create if needed and allowed.
      if (target) {
        if (isCreate && isExistCheck) {
          ___setErrNo(ERRNO_CODES.EEXIST);
          return -1;
        }
        if ((isWrite || isCreate || isTruncate) && target.isFolder) {
          ___setErrNo(ERRNO_CODES.EISDIR);
          return -1;
        }
        if (isRead && !target.read || isWrite && !target.write) {
          ___setErrNo(ERRNO_CODES.EACCES);
          return -1;
        }
        if (isTruncate && !target.isDevice) {
          target.contents = [];
        } else {
          if (!FS.forceLoadFile(target)) {
            ___setErrNo(ERRNO_CODES.EIO);
            return -1;
          }
        }
        finalPath = path.path;
      } else {
        if (!isCreate) {
          ___setErrNo(ERRNO_CODES.ENOENT);
          return -1;
        }
        if (!path.parentObject.write) {
          ___setErrNo(ERRNO_CODES.EACCES);
          return -1;
        }
        target = FS.createDataFile(path.parentObject, path.name, [],
                                   mode & 0x100, mode & 0x80);  // S_IRUSR, S_IWUSR.
        finalPath = path.parentPath + '/' + path.name;
      }
      // Actually create an open stream.
      var id = FS.streams.length; // Keep dense
      if (target.isFolder) {
        var entryBuffer = 0;
        if (___dirent_struct_layout) {
          entryBuffer = _malloc(___dirent_struct_layout.__size__);
        }
        var contents = [];
        for (var key in target.contents) contents.push(key);
        FS.streams[id] = {
          path: finalPath,
          object: target,
          // An index into contents. Special values: -2 is ".", -1 is "..".
          position: -2,
          isRead: true,
          isWrite: false,
          isAppend: false,
          error: false,
          eof: false,
          ungotten: [],
          // Folder-specific properties:
          // Remember the contents at the time of opening in an array, so we can
          // seek between them relying on a single order.
          contents: contents,
          // Each stream has its own area for readdir() returns.
          currentEntry: entryBuffer
        };
      } else {
        FS.streams[id] = {
          path: finalPath,
          object: target,
          position: 0,
          isRead: isRead,
          isWrite: isWrite,
          isAppend: isAppend,
          error: false,
          eof: false,
          ungotten: []
        };
      }
      FS.checkStreams();
      return id;
    }function _fopen(filename, mode) {
      // FILE *fopen(const char *restrict filename, const char *restrict mode);
      // http://pubs.opengroup.org/onlinepubs/000095399/functions/fopen.html
      var flags;
      mode = Pointer_stringify(mode);
      if (mode[0] == 'r') {
        if (mode.indexOf('+') != -1) {
          flags = 2;
        } else {
          flags = 0;
        }
      } else if (mode[0] == 'w') {
        if (mode.indexOf('+') != -1) {
          flags = 2;
        } else {
          flags = 1;
        }
        flags |= 512;
        flags |= 1024;
      } else if (mode[0] == 'a') {
        if (mode.indexOf('+') != -1) {
          flags = 2;
        } else {
          flags = 1;
        }
        flags |= 512;
        flags |= 8;
      } else {
        ___setErrNo(ERRNO_CODES.EINVAL);
        return 0;
      }
      var ret = _open(filename, flags, allocate([0x1FF, 0, 0, 0], 'i32', ALLOC_STACK));  // All creation permissions.
      return (ret == -1) ? 0 : ret;
    }
  function __exit(status) {
      // void _exit(int status);
      // http://pubs.opengroup.org/onlinepubs/000095399/functions/exit.html
      function ExitStatus() {
        this.name = "ExitStatus";
        this.message = "Program terminated with exit(" + status + ")";
        this.status = status;
        Module.print('Exit Status: ' + status);
      };
      ExitStatus.prototype = new Error();
      ExitStatus.prototype.constructor = ExitStatus;
      exitRuntime();
      ABORT = true;
      throw new ExitStatus();
    }function _exit(status) {
      __exit(status);
    }
  function __isFloat(text) {
      return !!(/^[+-]?[0-9]*\.?[0-9]+([eE][+-]?[0-9]+)?$/.exec(text));
    }function __scanString(format, get, unget, varargs) {
      if (!__scanString.whiteSpace) {
        __scanString.whiteSpace = {};
        __scanString.whiteSpace[32] = 1;
        __scanString.whiteSpace[9] = 1;
        __scanString.whiteSpace[10] = 1;
        __scanString.whiteSpace[' '] = 1;
        __scanString.whiteSpace['\t'] = 1;
        __scanString.whiteSpace['\n'] = 1;
      }
      // Supports %x, %4x, %d.%d, %lld, %s, %f, %lf.
      // TODO: Support all format specifiers.
      format = Pointer_stringify(format);
      var soFar = 0;
      if (format.indexOf('%n') >= 0) {
        // need to track soFar
        var _get = get;
        get = function() {
          soFar++;
          return _get();
        }
        var _unget = unget;
        unget = function() {
          soFar--;
          return _unget();
        }
      }
      var formatIndex = 0;
      var argsi = 0;
      var fields = 0;
      var argIndex = 0;
      var next;
      mainLoop:
      for (var formatIndex = 0; formatIndex < format.length;) {
        if (format[formatIndex] === '%' && format[formatIndex+1] == 'n') {
          var argPtr = HEAP32[(((varargs)+(argIndex))>>2)];
          argIndex += Runtime.getNativeFieldSize('void*');
          HEAP32[((argPtr)>>2)]=soFar;
          formatIndex += 2;
          continue;
        }
        // TODO: Support strings like "%5c" etc.
        if (format[formatIndex] === '%' && format[formatIndex+1] == 'c') {
          var argPtr = HEAP32[(((varargs)+(argIndex))>>2)];
          argIndex += Runtime.getNativeFieldSize('void*');
          fields++;
          next = get();
          HEAP8[(argPtr)]=next
          formatIndex += 2;
          continue;
        }
        // remove whitespace
        while (1) {
          next = get();
          if (next == 0) return fields;
          if (!(next in __scanString.whiteSpace)) break;
        }
        unget();
        if (format[formatIndex] === '%') {
          formatIndex++;
          var maxSpecifierStart = formatIndex;
          while (format[formatIndex].charCodeAt(0) >= 48 &&
                 format[formatIndex].charCodeAt(0) <= 57) {
            formatIndex++;
          }
          var max_;
          if (formatIndex != maxSpecifierStart) {
            max_ = parseInt(format.slice(maxSpecifierStart, formatIndex), 10);
          }
          var long_ = false;
          var half = false;
          var longLong = false;
          if (format[formatIndex] == 'l') {
            long_ = true;
            formatIndex++;
            if(format[formatIndex] == 'l') {
              longLong = true;
              formatIndex++;
            }
          } else if (format[formatIndex] == 'h') {
            half = true;
            formatIndex++;
          }
          var type = format[formatIndex];
          formatIndex++;
          var curr = 0;
          var buffer = [];
          // Read characters according to the format. floats are trickier, they may be in an unfloat state in the middle, then be a valid float later
          if (type == 'f' || type == 'e' || type == 'g' || type == 'E') {
            var last = 0;
            next = get();
            while (next > 0) {
              buffer.push(String.fromCharCode(next));
              if (__isFloat(buffer.join(''))) {
                last = buffer.length;
              }
              next = get();
            }
            for (var i = 0; i < buffer.length - last + 1; i++) {
              unget();
            }
            buffer.length = last;
          } else {
            next = get();
            var first = true;
            while ((curr < max_ || isNaN(max_)) && next > 0) {
              if (!(next in __scanString.whiteSpace) && // stop on whitespace
                  (type == 's' ||
                   ((type === 'd' || type == 'u' || type == 'i') && ((next >= 48 && next <= 57) ||
                                                                     (first && next == 45))) ||
                   (type === 'x' && (next >= 48 && next <= 57 ||
                                     next >= 97 && next <= 102 ||
                                     next >= 65 && next <= 70))) &&
                  (formatIndex >= format.length || next !== format[formatIndex].charCodeAt(0))) { // Stop when we read something that is coming up
                buffer.push(String.fromCharCode(next));
                next = get();
                curr++;
                first = false;
              } else {
                break;
              }
            }
            unget();
          }
          if (buffer.length === 0) return 0;  // Failure.
          var text = buffer.join('');
          var argPtr = HEAP32[(((varargs)+(argIndex))>>2)];
          argIndex += Runtime.getNativeFieldSize('void*');
          switch (type) {
            case 'd': case 'u': case 'i':
              if (half) {
                HEAP16[((argPtr)>>1)]=parseInt(text, 10);
              } else if(longLong) {
                (tempI64 = [parseInt(text, 10)>>>0,Math.min(Math.floor((parseInt(text, 10))/4294967296), 4294967295)>>>0],HEAP32[((argPtr)>>2)]=tempI64[0],HEAP32[(((argPtr)+(4))>>2)]=tempI64[1]);
              } else {
                HEAP32[((argPtr)>>2)]=parseInt(text, 10);
              }
              break;
            case 'x':
              HEAP32[((argPtr)>>2)]=parseInt(text, 16)
              break;
            case 'f':
            case 'e':
            case 'g':
            case 'E':
              // fallthrough intended
              if (long_) {
                (HEAPF64[(tempDoublePtr)>>3]=parseFloat(text),HEAP32[((argPtr)>>2)]=HEAP32[((tempDoublePtr)>>2)],HEAP32[(((argPtr)+(4))>>2)]=HEAP32[(((tempDoublePtr)+(4))>>2)])
              } else {
                HEAPF32[((argPtr)>>2)]=parseFloat(text)
              }
              break;
            case 's':
              var array = intArrayFromString(text);
              for (var j = 0; j < array.length; j++) {
                HEAP8[(((argPtr)+(j))|0)]=array[j]
              }
              break;
          }
          fields++;
        } else if (format[formatIndex] in __scanString.whiteSpace) {
          next = get();
          while (next in __scanString.whiteSpace) {
            if (next <= 0) break mainLoop;  // End of input.
            next = get();
          }
          unget(next);
          formatIndex++;
        } else {
          // Not a specifier.
          next = get();
          if (format[formatIndex].charCodeAt(0) !== next) {
            unget(next);
            break mainLoop;
          }
          formatIndex++;
        }
      }
      return fields;
    }
  function _pread(fildes, buf, nbyte, offset) {
      // ssize_t pread(int fildes, void *buf, size_t nbyte, off_t offset);
      // http://pubs.opengroup.org/onlinepubs/000095399/functions/read.html
      var stream = FS.streams[fildes];
      if (!stream || stream.object.isDevice) {
        ___setErrNo(ERRNO_CODES.EBADF);
        return -1;
      } else if (!stream.isRead) {
        ___setErrNo(ERRNO_CODES.EACCES);
        return -1;
      } else if (stream.object.isFolder) {
        ___setErrNo(ERRNO_CODES.EISDIR);
        return -1;
      } else if (nbyte < 0 || offset < 0) {
        ___setErrNo(ERRNO_CODES.EINVAL);
        return -1;
      } else {
        var bytesRead = 0;
        while (stream.ungotten.length && nbyte > 0) {
          HEAP8[((buf++)|0)]=stream.ungotten.pop()
          nbyte--;
          bytesRead++;
        }
        var contents = stream.object.contents;
        var size = Math.min(contents.length - offset, nbyte);
        if (contents.subarray) { // typed array
          HEAPU8.set(contents.subarray(offset, offset+size), buf);
        } else
        if (contents.slice) { // normal array
          for (var i = 0; i < size; i++) {
            HEAP8[(((buf)+(i))|0)]=contents[offset + i]
          }
        } else {
          for (var i = 0; i < size; i++) { // LazyUint8Array from sync binary XHR
            HEAP8[(((buf)+(i))|0)]=contents.get(offset + i)
          }
        }
        bytesRead += size;
        return bytesRead;
      }
    }function _read(fildes, buf, nbyte) {
      // ssize_t read(int fildes, void *buf, size_t nbyte);
      // http://pubs.opengroup.org/onlinepubs/000095399/functions/read.html
      var stream = FS.streams[fildes];
      if (!stream) {
        ___setErrNo(ERRNO_CODES.EBADF);
        return -1;
      } else if (!stream.isRead) {
        ___setErrNo(ERRNO_CODES.EACCES);
        return -1;
      } else if (nbyte < 0) {
        ___setErrNo(ERRNO_CODES.EINVAL);
        return -1;
      } else {
        var bytesRead;
        if (stream.object.isDevice) {
          if (stream.object.input) {
            bytesRead = 0;
            while (stream.ungotten.length && nbyte > 0) {
              HEAP8[((buf++)|0)]=stream.ungotten.pop()
              nbyte--;
              bytesRead++;
            }
            for (var i = 0; i < nbyte; i++) {
              try {
                var result = stream.object.input();
              } catch (e) {
                ___setErrNo(ERRNO_CODES.EIO);
                return -1;
              }
              if (result === null || result === undefined) break;
              bytesRead++;
              HEAP8[(((buf)+(i))|0)]=result
            }
            return bytesRead;
          } else {
            ___setErrNo(ERRNO_CODES.ENXIO);
            return -1;
          }
        } else {
          var ungotSize = stream.ungotten.length;
          bytesRead = _pread(fildes, buf, nbyte, stream.position);
          if (bytesRead != -1) {
            stream.position += (stream.ungotten.length - ungotSize) + bytesRead;
          }
          return bytesRead;
        }
      }
    }function _fgetc(stream) {
      // int fgetc(FILE *stream);
      // http://pubs.opengroup.org/onlinepubs/000095399/functions/fgetc.html
      if (!FS.streams[stream]) return -1;
      var streamObj = FS.streams[stream];
      if (streamObj.eof || streamObj.error) return -1;
      var ret = _read(stream, _fgetc.ret, 1);
      if (ret == 0) {
        streamObj.eof = true;
        return -1;
      } else if (ret == -1) {
        streamObj.error = true;
        return -1;
      } else {
        return HEAPU8[((_fgetc.ret)|0)];
      }
    }var _getc=_fgetc;
  function _ungetc(c, stream) {
      // int ungetc(int c, FILE *stream);
      // http://pubs.opengroup.org/onlinepubs/000095399/functions/ungetc.html
      if (FS.streams[stream]) {
        c = unSign(c & 0xFF);
        FS.streams[stream].ungotten.push(c);
        return c;
      } else {
        return -1;
      }
    }function _fscanf(stream, format, varargs) {
      // int fscanf(FILE *restrict stream, const char *restrict format, ... );
      // http://pubs.opengroup.org/onlinepubs/000095399/functions/scanf.html
      if (FS.streams[stream]) {
        var stack = [];
        var get = function() { var ret = _fgetc(stream); stack.push(ret); return ret };
        var unget = function(c) { return _ungetc(stack.pop(), stream) };
        return __scanString(format, get, unget, varargs);
      } else {
        return -1;
      }
    }
  function _close(fildes) {
      // int close(int fildes);
      // http://pubs.opengroup.org/onlinepubs/000095399/functions/close.html
      if (FS.streams[fildes]) {
        if (FS.streams[fildes].currentEntry) {
          _free(FS.streams[fildes].currentEntry);
        }
        FS.streams[fildes] = null;
        return 0;
      } else {
        ___setErrNo(ERRNO_CODES.EBADF);
        return -1;
      }
    }
  function _fsync(fildes) {
      // int fsync(int fildes);
      // http://pubs.opengroup.org/onlinepubs/000095399/functions/fsync.html
      if (FS.streams[fildes]) {
        // We write directly to the file system, so there's nothing to do here.
        return 0;
      } else {
        ___setErrNo(ERRNO_CODES.EBADF);
        return -1;
      }
    }function _fclose(stream) {
      // int fclose(FILE *stream);
      // http://pubs.opengroup.org/onlinepubs/000095399/functions/fclose.html
      _fsync(stream);
      return _close(stream);
    }
  function _scanf(format, varargs) {
      // int scanf(const char *restrict format, ... );
      // http://pubs.opengroup.org/onlinepubs/000095399/functions/scanf.html
      var stdin = HEAP32[((_stdin)>>2)];
      return _fscanf(stdin, format, varargs);
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
_fputc.ret = allocate([0], "i8", ALLOC_STATIC);
__ATINIT__.unshift({ func: function() { if (!Module["noFSInit"] && !FS.init.initialized) FS.init() } });__ATMAIN__.push({ func: function() { FS.ignorePermissions = false } });__ATEXIT__.push({ func: function() { FS.quit() } });Module["FS_createFolder"] = FS.createFolder;Module["FS_createPath"] = FS.createPath;Module["FS_createDataFile"] = FS.createDataFile;Module["FS_createPreloadedFile"] = FS.createPreloadedFile;Module["FS_createLazyFile"] = FS.createLazyFile;Module["FS_createLink"] = FS.createLink;Module["FS_createDevice"] = FS.createDevice;
___setErrNo(0);
_fgetc.ret = allocate([0], "i8", ALLOC_STATIC);
Module["requestFullScreen"] = function(lockPointer, resizeCanvas) { Browser.requestFullScreen(lockPointer, resizeCanvas) };
  Module["requestAnimationFrame"] = function(func) { Browser.requestAnimationFrame(func) };
  Module["pauseMainLoop"] = function() { Browser.mainLoop.pause() };
  Module["resumeMainLoop"] = function() { Browser.mainLoop.resume() };
var FUNCTION_TABLE = [0, 0];
// EMSCRIPTEN_START_FUNCS
function _init_patterns() {
  var label = 0;
  label = 2; 
  while(1) switch(label) {
    case 2: 
      var $i;
      $i=0;
      label = 3; break;
    case 3: 
      var $2=$i;
      var $3=(($2)|(0)) < 3;
      if ($3) { label = 4; break; } else { label = 6; break; }
    case 4: 
      var $ld$0$0=((5243260)|0);
      var $5$0=HEAP32[(($ld$0$0)>>2)];
      var $ld$1$1=((5243264)|0);
      var $5$1=HEAP32[(($ld$1$1)>>2)];
      var $$etemp$2$0=1;
      var $$etemp$2$1=0;
      var $6$0=$5$0 | $$etemp$2$0;
      var $6$1=$5$1 | $$etemp$2$1;
      var $st$3$0=((5243260)|0);
      HEAP32[(($st$3$0)>>2)]=$6$0;
      var $st$4$1=((5243264)|0);
      HEAP32[(($st$4$1)>>2)]=$6$1;
      var $ld$5$0=((5243260)|0);
      var $7$0=HEAP32[(($ld$5$0)>>2)];
      var $ld$6$1=((5243264)|0);
      var $7$1=HEAP32[(($ld$6$1)>>2)];
      var $8$0=($7$0 << 7) | (0 >>> 25);
      var $8$1=($7$1 << 7) | ($7$0 >>> 25);
      var $st$7$0=((5243260)|0);
      HEAP32[(($st$7$0)>>2)]=$8$0;
      var $st$8$1=((5243264)|0);
      HEAP32[(($st$8$1)>>2)]=$8$1;
      label = 5; break;
    case 5: 
      var $10=$i;
      var $11=((($10)+(1))|0);
      $i=$11;
      label = 3; break;
    case 6: 
      var $ld$9$0=((5243260)|0);
      var $13$0=HEAP32[(($ld$9$0)>>2)];
      var $ld$10$1=((5243264)|0);
      var $13$1=HEAP32[(($ld$10$1)>>2)];
      var $$etemp$11$0=1;
      var $$etemp$11$1=0;
      var $14$0=$13$0 | $$etemp$11$0;
      var $14$1=$13$1 | $$etemp$11$1;
      var $st$12$0=((5243260)|0);
      HEAP32[(($st$12$0)>>2)]=$14$0;
      var $st$13$1=((5243264)|0);
      HEAP32[(($st$13$1)>>2)]=$14$1;
      var $ld$14$0=((5243260)|0);
      var $15$0=HEAP32[(($ld$14$0)>>2)];
      var $ld$15$1=((5243264)|0);
      var $15$1=HEAP32[(($ld$15$1)>>2)];
      var $16$0=($15$0 >>> 7) | ($15$1 << 25);
      var $16$1=($15$1 >>> 7) | (0 << 25);
      var $st$16$0=((5243292)|0);
      HEAP32[(($st$16$0)>>2)]=$16$0;
      var $st$17$1=((5243296)|0);
      HEAP32[(($st$17$1)>>2)]=$16$1;
      var $ld$18$0=((5243292)|0);
      var $17$0=HEAP32[(($ld$18$0)>>2)];
      var $ld$19$1=((5243296)|0);
      var $17$1=HEAP32[(($ld$19$1)>>2)];
      var $18$0=($17$0 >>> 7) | ($17$1 << 25);
      var $18$1=($17$1 >>> 7) | (0 << 25);
      var $st$20$0=((5243324)|0);
      HEAP32[(($st$20$0)>>2)]=$18$0;
      var $st$21$1=((5243328)|0);
      HEAP32[(($st$21$1)>>2)]=$18$1;
      var $$etemp$22$0=15;
      var $$etemp$22$1=0;
      var $st$23$0=((5243284)|0);
      HEAP32[(($st$23$0)>>2)]=$$etemp$22$0;
      var $st$24$1=((5243288)|0);
      HEAP32[(($st$24$1)>>2)]=$$etemp$22$1;
      var $ld$25$0=((5243284)|0);
      var $19$0=HEAP32[(($ld$25$0)>>2)];
      var $ld$26$1=((5243288)|0);
      var $19$1=HEAP32[(($ld$26$1)>>2)];
      var $20$0=($19$0 >>> 1) | ($19$1 << 31);
      var $20$1=($19$1 >>> 1) | (0 << 31);
      var $st$27$0=((5243316)|0);
      HEAP32[(($st$27$0)>>2)]=$20$0;
      var $st$28$1=((5243320)|0);
      HEAP32[(($st$28$1)>>2)]=$20$1;
      var $ld$29$0=((5243316)|0);
      var $21$0=HEAP32[(($ld$29$0)>>2)];
      var $ld$30$1=((5243320)|0);
      var $21$1=HEAP32[(($ld$30$1)>>2)];
      var $22$0=($21$0 >>> 1) | ($21$1 << 31);
      var $22$1=($21$1 >>> 1) | (0 << 31);
      var $st$31$0=((5243348)|0);
      HEAP32[(($st$31$0)>>2)]=$22$0;
      var $st$32$1=((5243352)|0);
      HEAP32[(($st$32$1)>>2)]=$22$1;
      $i=0;
      label = 7; break;
    case 7: 
      var $24=$i;
      var $25=(($24)|(0)) < 3;
      if ($25) { label = 8; break; } else { label = 10; break; }
    case 8: 
      var $ld$33$0=((5243268)|0);
      var $27$0=HEAP32[(($ld$33$0)>>2)];
      var $ld$34$1=((5243272)|0);
      var $27$1=HEAP32[(($ld$34$1)>>2)];
      var $$etemp$35$0=1;
      var $$etemp$35$1=0;
      var $28$0=$27$0 | $$etemp$35$0;
      var $28$1=$27$1 | $$etemp$35$1;
      var $st$36$0=((5243268)|0);
      HEAP32[(($st$36$0)>>2)]=$28$0;
      var $st$37$1=((5243272)|0);
      HEAP32[(($st$37$1)>>2)]=$28$1;
      var $ld$38$0=((5243268)|0);
      var $29$0=HEAP32[(($ld$38$0)>>2)];
      var $ld$39$1=((5243272)|0);
      var $29$1=HEAP32[(($ld$39$1)>>2)];
      var $30$0=($29$0 << 8) | (0 >>> 24);
      var $30$1=($29$1 << 8) | ($29$0 >>> 24);
      var $st$40$0=((5243268)|0);
      HEAP32[(($st$40$0)>>2)]=$30$0;
      var $st$41$1=((5243272)|0);
      HEAP32[(($st$41$1)>>2)]=$30$1;
      label = 9; break;
    case 9: 
      var $32=$i;
      var $33=((($32)+(1))|0);
      $i=$33;
      label = 7; break;
    case 10: 
      var $ld$42$0=((5243268)|0);
      var $35$0=HEAP32[(($ld$42$0)>>2)];
      var $ld$43$1=((5243272)|0);
      var $35$1=HEAP32[(($ld$43$1)>>2)];
      var $$etemp$44$0=1;
      var $$etemp$44$1=0;
      var $36$0=$35$0 | $$etemp$44$0;
      var $36$1=$35$1 | $$etemp$44$1;
      var $st$45$0=((5243268)|0);
      HEAP32[(($st$45$0)>>2)]=$36$0;
      var $st$46$1=((5243272)|0);
      HEAP32[(($st$46$1)>>2)]=$36$1;
      var $ld$47$0=((5243268)|0);
      var $37$0=HEAP32[(($ld$47$0)>>2)];
      var $ld$48$1=((5243272)|0);
      var $37$1=HEAP32[(($ld$48$1)>>2)];
      var $38$0=($37$0 >>> 8) | ($37$1 << 24);
      var $38$1=($37$1 >>> 8) | (0 << 24);
      var $st$49$0=((5243300)|0);
      HEAP32[(($st$49$0)>>2)]=$38$0;
      var $st$50$1=((5243304)|0);
      HEAP32[(($st$50$1)>>2)]=$38$1;
      var $ld$51$0=((5243300)|0);
      var $39$0=HEAP32[(($ld$51$0)>>2)];
      var $ld$52$1=((5243304)|0);
      var $39$1=HEAP32[(($ld$52$1)>>2)];
      var $40$0=($39$0 >>> 8) | ($39$1 << 24);
      var $40$1=($39$1 >>> 8) | (0 << 24);
      var $st$53$0=((5243332)|0);
      HEAP32[(($st$53$0)>>2)]=$40$0;
      var $st$54$1=((5243336)|0);
      HEAP32[(($st$54$1)>>2)]=$40$1;
      $i=0;
      label = 11; break;
    case 11: 
      var $42=$i;
      var $43=(($42)|(0)) < 3;
      if ($43) { label = 12; break; } else { label = 14; break; }
    case 12: 
      var $ld$55$0=((5243276)|0);
      var $45$0=HEAP32[(($ld$55$0)>>2)];
      var $ld$56$1=((5243280)|0);
      var $45$1=HEAP32[(($ld$56$1)>>2)];
      var $$etemp$57$0=8;
      var $$etemp$57$1=0;
      var $46$0=$45$0 | $$etemp$57$0;
      var $46$1=$45$1 | $$etemp$57$1;
      var $st$58$0=((5243276)|0);
      HEAP32[(($st$58$0)>>2)]=$46$0;
      var $st$59$1=((5243280)|0);
      HEAP32[(($st$59$1)>>2)]=$46$1;
      var $ld$60$0=((5243276)|0);
      var $47$0=HEAP32[(($ld$60$0)>>2)];
      var $ld$61$1=((5243280)|0);
      var $47$1=HEAP32[(($ld$61$1)>>2)];
      var $48$0=($47$0 << 6) | (0 >>> 26);
      var $48$1=($47$1 << 6) | ($47$0 >>> 26);
      var $st$62$0=((5243276)|0);
      HEAP32[(($st$62$0)>>2)]=$48$0;
      var $st$63$1=((5243280)|0);
      HEAP32[(($st$63$1)>>2)]=$48$1;
      label = 13; break;
    case 13: 
      var $50=$i;
      var $51=((($50)+(1))|0);
      $i=$51;
      label = 11; break;
    case 14: 
      var $ld$64$0=((5243276)|0);
      var $53$0=HEAP32[(($ld$64$0)>>2)];
      var $ld$65$1=((5243280)|0);
      var $53$1=HEAP32[(($ld$65$1)>>2)];
      var $$etemp$66$0=8;
      var $$etemp$66$1=0;
      var $54$0=$53$0 | $$etemp$66$0;
      var $54$1=$53$1 | $$etemp$66$1;
      var $st$67$0=((5243276)|0);
      HEAP32[(($st$67$0)>>2)]=$54$0;
      var $st$68$1=((5243280)|0);
      HEAP32[(($st$68$1)>>2)]=$54$1;
      var $ld$69$0=((5243276)|0);
      var $55$0=HEAP32[(($ld$69$0)>>2)];
      var $ld$70$1=((5243280)|0);
      var $55$1=HEAP32[(($ld$70$1)>>2)];
      var $56$0=($55$0 >>> 6) | ($55$1 << 26);
      var $56$1=($55$1 >>> 6) | (0 << 26);
      var $st$71$0=((5243308)|0);
      HEAP32[(($st$71$0)>>2)]=$56$0;
      var $st$72$1=((5243312)|0);
      HEAP32[(($st$72$1)>>2)]=$56$1;
      var $ld$73$0=((5243308)|0);
      var $57$0=HEAP32[(($ld$73$0)>>2)];
      var $ld$74$1=((5243312)|0);
      var $57$1=HEAP32[(($ld$74$1)>>2)];
      var $58$0=($57$0 >>> 6) | ($57$1 << 26);
      var $58$1=($57$1 >>> 6) | (0 << 26);
      var $st$75$0=((5243340)|0);
      HEAP32[(($st$75$0)>>2)]=$58$0;
      var $st$76$1=((5243344)|0);
      HEAP32[(($st$76$1)>>2)]=$58$1;
      return;
    default: assert(0, "bad label: " + label);
  }
}
function _init_board($b) {
  var label = 0;
  label = 2; 
  while(1) switch(label) {
    case 2: 
      var $1;
      var $i;
      var $j;
      $1=$b;
      $i=0;
      label = 3; break;
    case 3: 
      var $3=$i;
      var $4=(($3)|(0)) < 7;
      if ($4) { label = 4; break; } else { label = 10; break; }
    case 4: 
      $j=0;
      label = 5; break;
    case 5: 
      var $7=$j;
      var $8=(($7)|(0)) < 6;
      if ($8) { label = 6; break; } else { label = 8; break; }
    case 6: 
      var $10=$j;
      var $11=$i;
      var $12=$1;
      var $13=(($12+((($11)*(7))&-1))|0);
      var $14=(($13+$10)|0);
      HEAP8[($14)]=46;
      label = 7; break;
    case 7: 
      var $16=$j;
      var $17=((($16)+(1))|0);
      $j=$17;
      label = 5; break;
    case 8: 
      label = 9; break;
    case 9: 
      var $20=$i;
      var $21=((($20)+(1))|0);
      $i=$21;
      label = 3; break;
    case 10: 
      $i=0;
      label = 11; break;
    case 11: 
      var $24=$i;
      var $25=(($24)|(0)) < 7;
      if ($25) { label = 12; break; } else { label = 14; break; }
    case 12: 
      var $27=$i;
      var $28=$1;
      var $29=(($28+((($27)*(7))&-1))|0);
      var $30=(($29+6)|0);
      HEAP8[($30)]=0;
      label = 13; break;
    case 13: 
      var $32=$i;
      var $33=((($32)+(1))|0);
      $i=$33;
      label = 11; break;
    case 14: 
      return;
    default: assert(0, "bad label: " + label);
  }
}
function _board_full($b) {
  var label = 0;
  label = 2; 
  while(1) switch(label) {
    case 2: 
      var $1;
      var $2;
      var $i;
      var $temp;
      $2=$b;
      $temp=0;
      $i=0;
      label = 3; break;
    case 3: 
      var $4=$i;
      var $5=(($4)|(0)) < 7;
      if ($5) { label = 4; break; } else { label = 6; break; }
    case 4: 
      var $7=$i;
      var $8=$2;
      var $9=(($8+((($7)*(7))&-1))|0);
      var $10=(($9+6)|0);
      var $11=HEAP8[($10)];
      var $12=(($11 << 24) >> 24);
      var $13=$temp;
      var $14=((($13)+($12))|0);
      $temp=$14;
      label = 5; break;
    case 5: 
      var $16=$i;
      var $17=((($16)+(1))|0);
      $i=$17;
      label = 3; break;
    case 6: 
      var $19=$temp;
      var $20=(($19)|(0))==42;
      if ($20) { label = 7; break; } else { label = 8; break; }
    case 7: 
      $1=1;
      label = 9; break;
    case 8: 
      $1=0;
      label = 9; break;
    case 9: 
      var $24=$1;
      return $24;
    default: assert(0, "bad label: " + label);
  }
}
function _print_board($b) {
  var label = 0;
  var __stackBase__  = STACKTOP; assert(!(STACKTOP&3)); assert((STACKTOP|0) < (STACK_MAX|0));
  label = 2; 
  while(1) switch(label) {
    case 2: 
      var $1;
      var $i;
      var $j;
      $1=$b;
      var $2=HEAP32[((_stdout)>>2)];
      var $3=_fputc(32, $2);
      $i=0;
      label = 3; break;
    case 3: 
      var $5=$i;
      var $6=(($5)|(0)) < 7;
      if ($6) { label = 4; break; } else { label = 6; break; }
    case 4: 
      var $8=$i;
      var $9=$1;
      var $10=(($9+((($8)*(7))&-1))|0);
      var $11=(($10+6)|0);
      var $12=HEAP8[($11)];
      var $13=(($12 << 24) >> 24);
      var $14=_printf(((5243252)|0), (tempInt=STACKTOP,STACKTOP = (STACKTOP + 4)|0,assert((STACKTOP|0) < (STACK_MAX|0)),HEAP32[((tempInt)>>2)]=$13,tempInt));
      label = 5; break;
    case 5: 
      var $16=$i;
      var $17=((($16)+(1))|0);
      $i=$17;
      label = 3; break;
    case 6: 
      var $19=HEAP32[((_stdout)>>2)];
      var $20=_fputc(10, $19);
      $j=5;
      label = 7; break;
    case 7: 
      var $22=$j;
      var $23=(($22)|(0)) >= 0;
      if ($23) { label = 8; break; } else { label = 14; break; }
    case 8: 
      var $25=$j;
      var $26=_printf(((5243248)|0), (tempInt=STACKTOP,STACKTOP = (STACKTOP + 4)|0,assert((STACKTOP|0) < (STACK_MAX|0)),HEAP32[((tempInt)>>2)]=$25,tempInt));
      $i=0;
      label = 9; break;
    case 9: 
      var $28=$i;
      var $29=(($28)|(0)) < 7;
      if ($29) { label = 10; break; } else { label = 12; break; }
    case 10: 
      var $31=$j;
      var $32=$i;
      var $33=$1;
      var $34=(($33+((($32)*(7))&-1))|0);
      var $35=(($34+$31)|0);
      var $36=HEAP8[($35)];
      var $37=(($36 << 24) >> 24);
      var $38=_printf(((5243088)|0), (tempInt=STACKTOP,STACKTOP = (STACKTOP + 4)|0,assert((STACKTOP|0) < (STACK_MAX|0)),HEAP32[((tempInt)>>2)]=$37,tempInt));
      label = 11; break;
    case 11: 
      var $40=$i;
      var $41=((($40)+(1))|0);
      $i=$41;
      label = 9; break;
    case 12: 
      var $43=HEAP32[((_stdout)>>2)];
      var $44=_fputc(10, $43);
      label = 13; break;
    case 13: 
      var $46=$j;
      var $47=((($46)-(1))|0);
      $j=$47;
      label = 7; break;
    case 14: 
      var $49=HEAP32[((_stdout)>>2)];
      var $50=_fputc(32, $49);
      $i=0;
      label = 15; break;
    case 15: 
      var $52=$i;
      var $53=(($52)|(0)) < 7;
      if ($53) { label = 16; break; } else { label = 18; break; }
    case 16: 
      var $55=$i;
      var $56=_printf(((5243252)|0), (tempInt=STACKTOP,STACKTOP = (STACKTOP + 4)|0,assert((STACKTOP|0) < (STACK_MAX|0)),HEAP32[((tempInt)>>2)]=$55,tempInt));
      label = 17; break;
    case 17: 
      var $58=$i;
      var $59=((($58)+(1))|0);
      $i=$59;
      label = 15; break;
    case 18: 
      var $61=HEAP32[((_stdout)>>2)];
      var $62=_fputc(10, $61);
      var $63=_printf(((5242992)|0), (tempInt=STACKTOP,STACKTOP = (STACKTOP + 1)|0,STACKTOP = ((((STACKTOP)+3)>>2)<<2),assert((STACKTOP|0) < (STACK_MAX|0)),HEAP32[((tempInt)>>2)]=0,tempInt));
      STACKTOP = __stackBase__;
      return;
    default: assert(0, "bad label: " + label);
  }
}
function _place_piece($col, $player, $b) {
  var label = 0;
  var __stackBase__  = STACKTOP; assert(!(STACKTOP&3)); assert((STACKTOP|0) < (STACK_MAX|0));
  label = 2; 
  while(1) switch(label) {
    case 2: 
      var $1;
      var $2;
      var $3;
      var $4;
      $2=$col;
      $3=$player;
      $4=$b;
      var $5=$2;
      var $6=(($5)|(0)) < 0;
      if ($6) { label = 4; break; } else { label = 3; break; }
    case 3: 
      var $8=$2;
      var $9=(($8)|(0)) > 6;
      if ($9) { label = 4; break; } else { label = 5; break; }
    case 4: 
      var $11=$2;
      var $12=_printf(((5242964)|0), (tempInt=STACKTOP,STACKTOP = (STACKTOP + 4)|0,assert((STACKTOP|0) < (STACK_MAX|0)),HEAP32[((tempInt)>>2)]=$11,tempInt));
      $1=1;
      label = 14; break;
    case 5: 
      var $14=$2;
      var $15=$4;
      var $16=(($15+((($14)*(7))&-1))|0);
      var $17=(($16+6)|0);
      var $18=HEAP8[($17)];
      var $19=(($18 << 24) >> 24);
      var $20=(($19)|(0)) >= 6;
      if ($20) { label = 6; break; } else { label = 7; break; }
    case 6: 
      $1=1;
      label = 14; break;
    case 7: 
      var $23=$3;
      var $24=(($23)|(0))==1;
      if ($24) { label = 8; break; } else { label = 9; break; }
    case 8: 
      var $26=$2;
      var $27=$4;
      var $28=(($27+((($26)*(7))&-1))|0);
      var $29=(($28+6)|0);
      var $30=HEAP8[($29)];
      var $31=(($30 << 24) >> 24);
      var $32=$2;
      var $33=$4;
      var $34=(($33+((($32)*(7))&-1))|0);
      var $35=(($34+$31)|0);
      HEAP8[($35)]=111;
      label = 13; break;
    case 9: 
      var $37=$3;
      var $38=(($37)|(0))==2;
      if ($38) { label = 10; break; } else { label = 11; break; }
    case 10: 
      var $40=$2;
      var $41=$4;
      var $42=(($41+((($40)*(7))&-1))|0);
      var $43=(($42+6)|0);
      var $44=HEAP8[($43)];
      var $45=(($44 << 24) >> 24);
      var $46=$2;
      var $47=$4;
      var $48=(($47+((($46)*(7))&-1))|0);
      var $49=(($48+$45)|0);
      HEAP8[($49)]=120;
      label = 12; break;
    case 11: 
      var $51=_printf(((5242940)|0), (tempInt=STACKTOP,STACKTOP = (STACKTOP + 1)|0,STACKTOP = ((((STACKTOP)+3)>>2)<<2),assert((STACKTOP|0) < (STACK_MAX|0)),HEAP32[((tempInt)>>2)]=0,tempInt));
      $1=1;
      label = 14; break;
    case 12: 
      label = 13; break;
    case 13: 
      var $54=$2;
      var $55=$4;
      var $56=(($55+((($54)*(7))&-1))|0);
      var $57=(($56+6)|0);
      var $58=HEAP8[($57)];
      var $59=((($58)+(1))&255);
      HEAP8[($57)]=$59;
      $1=0;
      label = 14; break;
    case 14: 
      var $61=$1;
      STACKTOP = __stackBase__;
      return $61;
    default: assert(0, "bad label: " + label);
  }
}
function _find_winner_p($b) {
  var label = 0;
  var __stackBase__  = STACKTOP; STACKTOP = (STACKTOP + 16)|0; assert(!(STACKTOP&3)); assert((STACKTOP|0) < (STACK_MAX|0));
  label = 2; 
  while(1) switch(label) {
    case 2: 
      var $1;
      var $2;
      var $i;
      var $j;
      var $temp_board=__stackBase__;
      var $one=(__stackBase__)+(8);
      var $player;
      $2=$b;
      var $$etemp$0$0=0;
      var $$etemp$0$1=0;
      var $st$1$0=(($temp_board)|0);
      HEAP32[(($st$1$0)>>2)]=$$etemp$0$0;
      var $st$2$1=(($temp_board+4)|0);
      HEAP32[(($st$2$1)>>2)]=$$etemp$0$1;
      var $$etemp$3$0=1;
      var $$etemp$3$1=0;
      var $st$4$0=(($one)|0);
      HEAP32[(($st$4$0)>>2)]=$$etemp$3$0;
      var $st$5$1=(($one+4)|0);
      HEAP32[(($st$5$1)>>2)]=$$etemp$3$1;
      var $3=$2;
      var $4=_board_full($3);
      var $5=(($4)|(0))!=0;
      if ($5) { label = 3; break; } else { label = 4; break; }
    case 3: 
      $1=2;
      label = 51; break;
    case 4: 
      $player=111;
      $j=0;
      label = 5; break;
    case 5: 
      var $9=$j;
      var $10=(($9)>>>(0)) < 6;
      if ($10) { label = 6; break; } else { label = 14; break; }
    case 6: 
      $i=0;
      label = 7; break;
    case 7: 
      var $13=$i;
      var $14=(($13)>>>(0)) < 7;
      if ($14) { label = 8; break; } else { label = 12; break; }
    case 8: 
      var $16=$j;
      var $17=$i;
      var $18=$2;
      var $19=(($18+((($17)*(7))&-1))|0);
      var $20=(($19+$16)|0);
      var $21=HEAP8[($20)];
      var $22=(($21 << 24) >> 24);
      var $23=$player;
      var $24=(($23 << 24) >> 24);
      var $25=(($22)|(0))==(($24)|(0));
      if ($25) { label = 9; break; } else { label = 10; break; }
    case 9: 
      var $ld$6$0=(($temp_board)|0);
      var $27$0=HEAP32[(($ld$6$0)>>2)];
      var $ld$7$1=(($temp_board+4)|0);
      var $27$1=HEAP32[(($ld$7$1)>>2)];
      var $ld$8$0=(($one)|0);
      var $28$0=HEAP32[(($ld$8$0)>>2)];
      var $ld$9$1=(($one+4)|0);
      var $28$1=HEAP32[(($ld$9$1)>>2)];
      var $29=$i;
      var $30=$j;
      var $31=((($30)*(7))&-1);
      var $32=((($29)+($31))|0);
      var $33$0=$32;
      var $33$1=0;
      var $34$0 = _bitshift64Shl($28$0,$28$1,$33$0);var $34$1 = tempRet0;
      var $35$0=$27$0 | $34$0;
      var $35$1=$27$1 | $34$1;
      var $st$10$0=(($temp_board)|0);
      HEAP32[(($st$10$0)>>2)]=$35$0;
      var $st$11$1=(($temp_board+4)|0);
      HEAP32[(($st$11$1)>>2)]=$35$1;
      label = 10; break;
    case 10: 
      label = 11; break;
    case 11: 
      var $38=$i;
      var $39=((($38)+(1))|0);
      $i=$39;
      label = 7; break;
    case 12: 
      label = 13; break;
    case 13: 
      var $42=$j;
      var $43=((($42)+(1))|0);
      $j=$43;
      label = 5; break;
    case 14: 
      $i=0;
      label = 15; break;
    case 15: 
      var $46=$i;
      var $47=(($46)>>>(0)) < 21;
      if ($47) { label = 16; break; } else { label = 20; break; }
    case 16: 
      var $ld$12$0=(($temp_board)|0);
      var $49$0=HEAP32[(($ld$12$0)>>2)];
      var $ld$13$1=(($temp_board+4)|0);
      var $49$1=HEAP32[(($ld$13$1)>>2)];
      var $ld$14$0=((5243260)|0);
      var $50$0=HEAP32[(($ld$14$0)>>2)];
      var $ld$15$1=((5243264)|0);
      var $50$1=HEAP32[(($ld$15$1)>>2)];
      var $51=$i;
      var $52$0=$51;
      var $52$1=0;
      var $53$0 = _bitshift64Shl($50$0,$50$1,$52$0);var $53$1 = tempRet0;
      var $54$0=$49$0 & $53$0;
      var $54$1=$49$1 & $53$1;
      var $ld$16$0=((5243260)|0);
      var $55$0=HEAP32[(($ld$16$0)>>2)];
      var $ld$17$1=((5243264)|0);
      var $55$1=HEAP32[(($ld$17$1)>>2)];
      var $56=$i;
      var $57$0=$56;
      var $57$1=0;
      var $58$0 = _bitshift64Shl($55$0,$55$1,$57$0);var $58$1 = tempRet0;
      var $59=(($54$0|0) == ($58$0|0)) & (($54$1|0) == ($58$1|0));
      if ($59) { label = 17; break; } else { label = 18; break; }
    case 17: 
      $1=1;
      label = 51; break;
    case 18: 
      label = 19; break;
    case 19: 
      var $63=$i;
      var $64=((($63)+(1))|0);
      $i=$64;
      label = 15; break;
    case 20: 
      $i=0;
      label = 21; break;
    case 21: 
      var $67=$i;
      var $68=(($67)>>>(0)) < 6;
      if ($68) { label = 22; break; } else { label = 30; break; }
    case 22: 
      $j=0;
      label = 23; break;
    case 23: 
      var $71=$j;
      var $72=(($71)>>>(0)) < 4;
      if ($72) { label = 24; break; } else { label = 28; break; }
    case 24: 
      var $ld$18$0=(($temp_board)|0);
      var $74$0=HEAP32[(($ld$18$0)>>2)];
      var $ld$19$1=(($temp_board+4)|0);
      var $74$1=HEAP32[(($ld$19$1)>>2)];
      var $ld$20$0=((5243284)|0);
      var $75$0=HEAP32[(($ld$20$0)>>2)];
      var $ld$21$1=((5243288)|0);
      var $75$1=HEAP32[(($ld$21$1)>>2)];
      var $76=$j;
      var $77=$i;
      var $78=((($77)*(7))&-1);
      var $79=((($76)+($78))|0);
      var $80$0=$79;
      var $80$1=0;
      var $81$0 = _bitshift64Shl($75$0,$75$1,$80$0);var $81$1 = tempRet0;
      var $82$0=$74$0 & $81$0;
      var $82$1=$74$1 & $81$1;
      var $ld$22$0=((5243284)|0);
      var $83$0=HEAP32[(($ld$22$0)>>2)];
      var $ld$23$1=((5243288)|0);
      var $83$1=HEAP32[(($ld$23$1)>>2)];
      var $84=$j;
      var $85=$i;
      var $86=((($85)*(7))&-1);
      var $87=((($84)+($86))|0);
      var $88$0=$87;
      var $88$1=0;
      var $89$0 = _bitshift64Shl($83$0,$83$1,$88$0);var $89$1 = tempRet0;
      var $90=(($82$0|0) == ($89$0|0)) & (($82$1|0) == ($89$1|0));
      if ($90) { label = 25; break; } else { label = 26; break; }
    case 25: 
      $1=1;
      label = 51; break;
    case 26: 
      label = 27; break;
    case 27: 
      var $94=$j;
      var $95=((($94)+(1))|0);
      $j=$95;
      label = 23; break;
    case 28: 
      label = 29; break;
    case 29: 
      var $98=$i;
      var $99=((($98)+(1))|0);
      $i=$99;
      label = 21; break;
    case 30: 
      $i=0;
      label = 31; break;
    case 31: 
      var $102=$i;
      var $103=(($102)>>>(0)) < 3;
      if ($103) { label = 32; break; } else { label = 40; break; }
    case 32: 
      $j=0;
      label = 33; break;
    case 33: 
      var $106=$j;
      var $107=(($106)>>>(0)) < 4;
      if ($107) { label = 34; break; } else { label = 38; break; }
    case 34: 
      var $ld$24$0=(($temp_board)|0);
      var $109$0=HEAP32[(($ld$24$0)>>2)];
      var $ld$25$1=(($temp_board+4)|0);
      var $109$1=HEAP32[(($ld$25$1)>>2)];
      var $ld$26$0=((5243268)|0);
      var $110$0=HEAP32[(($ld$26$0)>>2)];
      var $ld$27$1=((5243272)|0);
      var $110$1=HEAP32[(($ld$27$1)>>2)];
      var $111=$j;
      var $112=$i;
      var $113=((($112)*(7))&-1);
      var $114=((($111)+($113))|0);
      var $115$0=$114;
      var $115$1=0;
      var $116$0 = _bitshift64Shl($110$0,$110$1,$115$0);var $116$1 = tempRet0;
      var $117$0=$109$0 & $116$0;
      var $117$1=$109$1 & $116$1;
      var $ld$28$0=((5243268)|0);
      var $118$0=HEAP32[(($ld$28$0)>>2)];
      var $ld$29$1=((5243272)|0);
      var $118$1=HEAP32[(($ld$29$1)>>2)];
      var $119=$j;
      var $120=$i;
      var $121=((($120)*(7))&-1);
      var $122=((($119)+($121))|0);
      var $123$0=$122;
      var $123$1=0;
      var $124$0 = _bitshift64Shl($118$0,$118$1,$123$0);var $124$1 = tempRet0;
      var $125=(($117$0|0) == ($124$0|0)) & (($117$1|0) == ($124$1|0));
      if ($125) { label = 35; break; } else { label = 36; break; }
    case 35: 
      $1=1;
      label = 51; break;
    case 36: 
      label = 37; break;
    case 37: 
      var $129=$j;
      var $130=((($129)+(1))|0);
      $j=$130;
      label = 33; break;
    case 38: 
      label = 39; break;
    case 39: 
      var $133=$i;
      var $134=((($133)+(1))|0);
      $i=$134;
      label = 31; break;
    case 40: 
      $i=0;
      label = 41; break;
    case 41: 
      var $137=$i;
      var $138=(($137)>>>(0)) < 3;
      if ($138) { label = 42; break; } else { label = 50; break; }
    case 42: 
      $j=0;
      label = 43; break;
    case 43: 
      var $141=$j;
      var $142=(($141)>>>(0)) < 4;
      if ($142) { label = 44; break; } else { label = 48; break; }
    case 44: 
      var $ld$30$0=(($temp_board)|0);
      var $144$0=HEAP32[(($ld$30$0)>>2)];
      var $ld$31$1=(($temp_board+4)|0);
      var $144$1=HEAP32[(($ld$31$1)>>2)];
      var $ld$32$0=((5243276)|0);
      var $145$0=HEAP32[(($ld$32$0)>>2)];
      var $ld$33$1=((5243280)|0);
      var $145$1=HEAP32[(($ld$33$1)>>2)];
      var $146=$j;
      var $147=$i;
      var $148=((($147)*(7))&-1);
      var $149=((($146)+($148))|0);
      var $150$0=$149;
      var $150$1=0;
      var $151$0 = _bitshift64Shl($145$0,$145$1,$150$0);var $151$1 = tempRet0;
      var $152$0=$144$0 & $151$0;
      var $152$1=$144$1 & $151$1;
      var $ld$34$0=((5243276)|0);
      var $153$0=HEAP32[(($ld$34$0)>>2)];
      var $ld$35$1=((5243280)|0);
      var $153$1=HEAP32[(($ld$35$1)>>2)];
      var $154=$j;
      var $155=$i;
      var $156=((($155)*(7))&-1);
      var $157=((($154)+($156))|0);
      var $158$0=$157;
      var $158$1=0;
      var $159$0 = _bitshift64Shl($153$0,$153$1,$158$0);var $159$1 = tempRet0;
      var $160=(($152$0|0) == ($159$0|0)) & (($152$1|0) == ($159$1|0));
      if ($160) { label = 45; break; } else { label = 46; break; }
    case 45: 
      $1=1;
      label = 51; break;
    case 46: 
      label = 47; break;
    case 47: 
      var $164=$j;
      var $165=((($164)+(1))|0);
      $j=$165;
      label = 43; break;
    case 48: 
      label = 49; break;
    case 49: 
      var $168=$i;
      var $169=((($168)+(1))|0);
      $i=$169;
      label = 41; break;
    case 50: 
      $1=0;
      label = 51; break;
    case 51: 
      var $172=$1;
      STACKTOP = __stackBase__;
      return $172;
    default: assert(0, "bad label: " + label);
  }
}
function _find_winner_c($b) {
  var label = 0;
  var __stackBase__  = STACKTOP; STACKTOP = (STACKTOP + 16)|0; assert(!(STACKTOP&3)); assert((STACKTOP|0) < (STACK_MAX|0));
  label = 2; 
  while(1) switch(label) {
    case 2: 
      var $1;
      var $2;
      var $i;
      var $j;
      var $temp_board=__stackBase__;
      var $one=(__stackBase__)+(8);
      var $player;
      $2=$b;
      var $$etemp$0$0=0;
      var $$etemp$0$1=0;
      var $st$1$0=(($temp_board)|0);
      HEAP32[(($st$1$0)>>2)]=$$etemp$0$0;
      var $st$2$1=(($temp_board+4)|0);
      HEAP32[(($st$2$1)>>2)]=$$etemp$0$1;
      var $$etemp$3$0=1;
      var $$etemp$3$1=0;
      var $st$4$0=(($one)|0);
      HEAP32[(($st$4$0)>>2)]=$$etemp$3$0;
      var $st$5$1=(($one+4)|0);
      HEAP32[(($st$5$1)>>2)]=$$etemp$3$1;
      var $3=$2;
      var $4=_board_full($3);
      var $5=(($4)|(0))!=0;
      if ($5) { label = 3; break; } else { label = 4; break; }
    case 3: 
      $1=2;
      label = 51; break;
    case 4: 
      $player=120;
      $j=0;
      label = 5; break;
    case 5: 
      var $9=$j;
      var $10=(($9)>>>(0)) < 6;
      if ($10) { label = 6; break; } else { label = 14; break; }
    case 6: 
      $i=0;
      label = 7; break;
    case 7: 
      var $13=$i;
      var $14=(($13)>>>(0)) < 7;
      if ($14) { label = 8; break; } else { label = 12; break; }
    case 8: 
      var $16=$j;
      var $17=$i;
      var $18=$2;
      var $19=(($18+((($17)*(7))&-1))|0);
      var $20=(($19+$16)|0);
      var $21=HEAP8[($20)];
      var $22=(($21 << 24) >> 24);
      var $23=$player;
      var $24=(($23 << 24) >> 24);
      var $25=(($22)|(0))==(($24)|(0));
      if ($25) { label = 9; break; } else { label = 10; break; }
    case 9: 
      var $ld$6$0=(($temp_board)|0);
      var $27$0=HEAP32[(($ld$6$0)>>2)];
      var $ld$7$1=(($temp_board+4)|0);
      var $27$1=HEAP32[(($ld$7$1)>>2)];
      var $ld$8$0=(($one)|0);
      var $28$0=HEAP32[(($ld$8$0)>>2)];
      var $ld$9$1=(($one+4)|0);
      var $28$1=HEAP32[(($ld$9$1)>>2)];
      var $29=$i;
      var $30=$j;
      var $31=((($30)*(7))&-1);
      var $32=((($29)+($31))|0);
      var $33$0=$32;
      var $33$1=0;
      var $34$0 = _bitshift64Shl($28$0,$28$1,$33$0);var $34$1 = tempRet0;
      var $35$0=$27$0 | $34$0;
      var $35$1=$27$1 | $34$1;
      var $st$10$0=(($temp_board)|0);
      HEAP32[(($st$10$0)>>2)]=$35$0;
      var $st$11$1=(($temp_board+4)|0);
      HEAP32[(($st$11$1)>>2)]=$35$1;
      label = 10; break;
    case 10: 
      label = 11; break;
    case 11: 
      var $38=$i;
      var $39=((($38)+(1))|0);
      $i=$39;
      label = 7; break;
    case 12: 
      label = 13; break;
    case 13: 
      var $42=$j;
      var $43=((($42)+(1))|0);
      $j=$43;
      label = 5; break;
    case 14: 
      $i=0;
      label = 15; break;
    case 15: 
      var $46=$i;
      var $47=(($46)>>>(0)) < 21;
      if ($47) { label = 16; break; } else { label = 20; break; }
    case 16: 
      var $ld$12$0=(($temp_board)|0);
      var $49$0=HEAP32[(($ld$12$0)>>2)];
      var $ld$13$1=(($temp_board+4)|0);
      var $49$1=HEAP32[(($ld$13$1)>>2)];
      var $ld$14$0=((5243260)|0);
      var $50$0=HEAP32[(($ld$14$0)>>2)];
      var $ld$15$1=((5243264)|0);
      var $50$1=HEAP32[(($ld$15$1)>>2)];
      var $51=$i;
      var $52$0=$51;
      var $52$1=0;
      var $53$0 = _bitshift64Shl($50$0,$50$1,$52$0);var $53$1 = tempRet0;
      var $54$0=$49$0 & $53$0;
      var $54$1=$49$1 & $53$1;
      var $ld$16$0=((5243260)|0);
      var $55$0=HEAP32[(($ld$16$0)>>2)];
      var $ld$17$1=((5243264)|0);
      var $55$1=HEAP32[(($ld$17$1)>>2)];
      var $56=$i;
      var $57$0=$56;
      var $57$1=0;
      var $58$0 = _bitshift64Shl($55$0,$55$1,$57$0);var $58$1 = tempRet0;
      var $59=(($54$0|0) == ($58$0|0)) & (($54$1|0) == ($58$1|0));
      if ($59) { label = 17; break; } else { label = 18; break; }
    case 17: 
      $1=1;
      label = 51; break;
    case 18: 
      label = 19; break;
    case 19: 
      var $63=$i;
      var $64=((($63)+(1))|0);
      $i=$64;
      label = 15; break;
    case 20: 
      $i=0;
      label = 21; break;
    case 21: 
      var $67=$i;
      var $68=(($67)>>>(0)) < 6;
      if ($68) { label = 22; break; } else { label = 30; break; }
    case 22: 
      $j=0;
      label = 23; break;
    case 23: 
      var $71=$j;
      var $72=(($71)>>>(0)) < 4;
      if ($72) { label = 24; break; } else { label = 28; break; }
    case 24: 
      var $ld$18$0=(($temp_board)|0);
      var $74$0=HEAP32[(($ld$18$0)>>2)];
      var $ld$19$1=(($temp_board+4)|0);
      var $74$1=HEAP32[(($ld$19$1)>>2)];
      var $ld$20$0=((5243284)|0);
      var $75$0=HEAP32[(($ld$20$0)>>2)];
      var $ld$21$1=((5243288)|0);
      var $75$1=HEAP32[(($ld$21$1)>>2)];
      var $76=$j;
      var $77=$i;
      var $78=((($77)*(7))&-1);
      var $79=((($76)+($78))|0);
      var $80$0=$79;
      var $80$1=0;
      var $81$0 = _bitshift64Shl($75$0,$75$1,$80$0);var $81$1 = tempRet0;
      var $82$0=$74$0 & $81$0;
      var $82$1=$74$1 & $81$1;
      var $ld$22$0=((5243284)|0);
      var $83$0=HEAP32[(($ld$22$0)>>2)];
      var $ld$23$1=((5243288)|0);
      var $83$1=HEAP32[(($ld$23$1)>>2)];
      var $84=$j;
      var $85=$i;
      var $86=((($85)*(7))&-1);
      var $87=((($84)+($86))|0);
      var $88$0=$87;
      var $88$1=0;
      var $89$0 = _bitshift64Shl($83$0,$83$1,$88$0);var $89$1 = tempRet0;
      var $90=(($82$0|0) == ($89$0|0)) & (($82$1|0) == ($89$1|0));
      if ($90) { label = 25; break; } else { label = 26; break; }
    case 25: 
      $1=1;
      label = 51; break;
    case 26: 
      label = 27; break;
    case 27: 
      var $94=$j;
      var $95=((($94)+(1))|0);
      $j=$95;
      label = 23; break;
    case 28: 
      label = 29; break;
    case 29: 
      var $98=$i;
      var $99=((($98)+(1))|0);
      $i=$99;
      label = 21; break;
    case 30: 
      $i=0;
      label = 31; break;
    case 31: 
      var $102=$i;
      var $103=(($102)>>>(0)) < 3;
      if ($103) { label = 32; break; } else { label = 40; break; }
    case 32: 
      $j=0;
      label = 33; break;
    case 33: 
      var $106=$j;
      var $107=(($106)>>>(0)) < 4;
      if ($107) { label = 34; break; } else { label = 38; break; }
    case 34: 
      var $ld$24$0=(($temp_board)|0);
      var $109$0=HEAP32[(($ld$24$0)>>2)];
      var $ld$25$1=(($temp_board+4)|0);
      var $109$1=HEAP32[(($ld$25$1)>>2)];
      var $ld$26$0=((5243268)|0);
      var $110$0=HEAP32[(($ld$26$0)>>2)];
      var $ld$27$1=((5243272)|0);
      var $110$1=HEAP32[(($ld$27$1)>>2)];
      var $111=$j;
      var $112=$i;
      var $113=((($112)*(7))&-1);
      var $114=((($111)+($113))|0);
      var $115$0=$114;
      var $115$1=0;
      var $116$0 = _bitshift64Shl($110$0,$110$1,$115$0);var $116$1 = tempRet0;
      var $117$0=$109$0 & $116$0;
      var $117$1=$109$1 & $116$1;
      var $ld$28$0=((5243268)|0);
      var $118$0=HEAP32[(($ld$28$0)>>2)];
      var $ld$29$1=((5243272)|0);
      var $118$1=HEAP32[(($ld$29$1)>>2)];
      var $119=$j;
      var $120=$i;
      var $121=((($120)*(7))&-1);
      var $122=((($119)+($121))|0);
      var $123$0=$122;
      var $123$1=0;
      var $124$0 = _bitshift64Shl($118$0,$118$1,$123$0);var $124$1 = tempRet0;
      var $125=(($117$0|0) == ($124$0|0)) & (($117$1|0) == ($124$1|0));
      if ($125) { label = 35; break; } else { label = 36; break; }
    case 35: 
      $1=1;
      label = 51; break;
    case 36: 
      label = 37; break;
    case 37: 
      var $129=$j;
      var $130=((($129)+(1))|0);
      $j=$130;
      label = 33; break;
    case 38: 
      label = 39; break;
    case 39: 
      var $133=$i;
      var $134=((($133)+(1))|0);
      $i=$134;
      label = 31; break;
    case 40: 
      $i=0;
      label = 41; break;
    case 41: 
      var $137=$i;
      var $138=(($137)>>>(0)) < 3;
      if ($138) { label = 42; break; } else { label = 50; break; }
    case 42: 
      $j=0;
      label = 43; break;
    case 43: 
      var $141=$j;
      var $142=(($141)>>>(0)) < 4;
      if ($142) { label = 44; break; } else { label = 48; break; }
    case 44: 
      var $ld$30$0=(($temp_board)|0);
      var $144$0=HEAP32[(($ld$30$0)>>2)];
      var $ld$31$1=(($temp_board+4)|0);
      var $144$1=HEAP32[(($ld$31$1)>>2)];
      var $ld$32$0=((5243276)|0);
      var $145$0=HEAP32[(($ld$32$0)>>2)];
      var $ld$33$1=((5243280)|0);
      var $145$1=HEAP32[(($ld$33$1)>>2)];
      var $146=$j;
      var $147=$i;
      var $148=((($147)*(7))&-1);
      var $149=((($146)+($148))|0);
      var $150$0=$149;
      var $150$1=0;
      var $151$0 = _bitshift64Shl($145$0,$145$1,$150$0);var $151$1 = tempRet0;
      var $152$0=$144$0 & $151$0;
      var $152$1=$144$1 & $151$1;
      var $ld$34$0=((5243276)|0);
      var $153$0=HEAP32[(($ld$34$0)>>2)];
      var $ld$35$1=((5243280)|0);
      var $153$1=HEAP32[(($ld$35$1)>>2)];
      var $154=$j;
      var $155=$i;
      var $156=((($155)*(7))&-1);
      var $157=((($154)+($156))|0);
      var $158$0=$157;
      var $158$1=0;
      var $159$0 = _bitshift64Shl($153$0,$153$1,$158$0);var $159$1 = tempRet0;
      var $160=(($152$0|0) == ($159$0|0)) & (($152$1|0) == ($159$1|0));
      if ($160) { label = 45; break; } else { label = 46; break; }
    case 45: 
      $1=1;
      label = 51; break;
    case 46: 
      label = 47; break;
    case 47: 
      var $164=$j;
      var $165=((($164)+(1))|0);
      $j=$165;
      label = 43; break;
    case 48: 
      label = 49; break;
    case 49: 
      var $168=$i;
      var $169=((($168)+(1))|0);
      $i=$169;
      label = 41; break;
    case 50: 
      $1=0;
      label = 51; break;
    case 51: 
      var $172=$1;
      STACKTOP = __stackBase__;
      return $172;
    default: assert(0, "bad label: " + label);
  }
}
function _value($b1$0, $b1$1, $b2$0, $b2$1) {
  var label = 0;
  var __stackBase__  = STACKTOP; STACKTOP = (STACKTOP + 32)|0; assert(!(STACKTOP&3)); assert((STACKTOP|0) < (STACK_MAX|0));
  label = 2; 
  while(1) switch(label) {
    case 2: 
      var $1=__stackBase__;
      var $2=(__stackBase__)+(8);
      var $i;
      var $j;
      var $k;
      var $b=(__stackBase__)+(16);
      var $bo=(__stackBase__)+(24);
      var $mod;
      var $value;
      var $st$0$0=(($1)|0);
      HEAP32[(($st$0$0)>>2)]=$b1$0;
      var $st$1$1=(($1+4)|0);
      HEAP32[(($st$1$1)>>2)]=$b1$1;
      var $st$2$0=(($2)|0);
      HEAP32[(($st$2$0)>>2)]=$b2$0;
      var $st$3$1=(($2+4)|0);
      HEAP32[(($st$3$1)>>2)]=$b2$1;
      $value=0;
      $k=0;
      label = 3; break;
    case 3: 
      var $4=$k;
      var $5=(($4)|(0)) < 2;
      if ($5) { label = 4; break; } else { label = 119; break; }
    case 4: 
      var $7=$k;
      var $8=(($7)|(0))==0;
      if ($8) { label = 5; break; } else { label = 6; break; }
    case 5: 
      var $ld$4$0=(($1)|0);
      var $10$0=HEAP32[(($ld$4$0)>>2)];
      var $ld$5$1=(($1+4)|0);
      var $10$1=HEAP32[(($ld$5$1)>>2)];
      var $st$6$0=(($b)|0);
      HEAP32[(($st$6$0)>>2)]=$10$0;
      var $st$7$1=(($b+4)|0);
      HEAP32[(($st$7$1)>>2)]=$10$1;
      var $ld$8$0=(($2)|0);
      var $11$0=HEAP32[(($ld$8$0)>>2)];
      var $ld$9$1=(($2+4)|0);
      var $11$1=HEAP32[(($ld$9$1)>>2)];
      var $st$10$0=(($bo)|0);
      HEAP32[(($st$10$0)>>2)]=$11$0;
      var $st$11$1=(($bo+4)|0);
      HEAP32[(($st$11$1)>>2)]=$11$1;
      var $12=HEAP32[((5242880)>>2)];
      var $13=(($12)|(0));
      var $14=($13)/(10);
      var $15=($14)-(1);
      $mod=$15;
      label = 7; break;
    case 6: 
      var $ld$12$0=(($2)|0);
      var $17$0=HEAP32[(($ld$12$0)>>2)];
      var $ld$13$1=(($2+4)|0);
      var $17$1=HEAP32[(($ld$13$1)>>2)];
      var $st$14$0=(($b)|0);
      HEAP32[(($st$14$0)>>2)]=$17$0;
      var $st$15$1=(($b+4)|0);
      HEAP32[(($st$15$1)>>2)]=$17$1;
      var $ld$16$0=(($1)|0);
      var $18$0=HEAP32[(($ld$16$0)>>2)];
      var $ld$17$1=(($1+4)|0);
      var $18$1=HEAP32[(($ld$17$1)>>2)];
      var $st$18$0=(($bo)|0);
      HEAP32[(($st$18$0)>>2)]=$18$0;
      var $st$19$1=(($bo+4)|0);
      HEAP32[(($st$19$1)>>2)]=$18$1;
      var $19=HEAP32[((5242880)>>2)];
      var $20=(($19)|(0));
      var $21=($20)/(10);
      var $22=($21)+(1);
      $mod=$22;
      label = 7; break;
    case 7: 
      $i=0;
      label = 8; break;
    case 8: 
      var $25=$i;
      var $26=(($25)|(0)) < 21;
      if ($26) { label = 9; break; } else { label = 13; break; }
    case 9: 
      var $ld$20$0=(($b)|0);
      var $28$0=HEAP32[(($ld$20$0)>>2)];
      var $ld$21$1=(($b+4)|0);
      var $28$1=HEAP32[(($ld$21$1)>>2)];
      var $ld$22$0=((5243260)|0);
      var $29$0=HEAP32[(($ld$22$0)>>2)];
      var $ld$23$1=((5243264)|0);
      var $29$1=HEAP32[(($ld$23$1)>>2)];
      var $30=$i;
      var $31$0=$30;
      var $31$1=0;
      var $32$0 = _bitshift64Shl($29$0,$29$1,$31$0);var $32$1 = tempRet0;
      var $33$0=$28$0 & $32$0;
      var $33$1=$28$1 & $32$1;
      var $ld$24$0=((5243260)|0);
      var $34$0=HEAP32[(($ld$24$0)>>2)];
      var $ld$25$1=((5243264)|0);
      var $34$1=HEAP32[(($ld$25$1)>>2)];
      var $35=$i;
      var $36$0=$35;
      var $36$1=0;
      var $37$0 = _bitshift64Shl($34$0,$34$1,$36$0);var $37$1 = tempRet0;
      var $38=(($33$0|0) == ($37$0|0)) & (($33$1|0) == ($37$1|0));
      if ($38) { label = 10; break; } else { label = 11; break; }
    case 10: 
      var $40=$mod;
      var $41=($40)*(1000);
      var $42=$value;
      var $43=(($42)|(0));
      var $44=($43)+($41);
      var $45=(($44)&-1);
      $value=$45;
      label = 11; break;
    case 11: 
      label = 12; break;
    case 12: 
      var $48=$i;
      var $49=((($48)+(1))|0);
      $i=$49;
      label = 8; break;
    case 13: 
      $i=0;
      label = 14; break;
    case 14: 
      var $52=$i;
      var $53=(($52)|(0)) < 6;
      if ($53) { label = 15; break; } else { label = 23; break; }
    case 15: 
      $j=0;
      label = 16; break;
    case 16: 
      var $56=$j;
      var $57=(($56)|(0)) < 4;
      if ($57) { label = 17; break; } else { label = 21; break; }
    case 17: 
      var $ld$26$0=(($b)|0);
      var $59$0=HEAP32[(($ld$26$0)>>2)];
      var $ld$27$1=(($b+4)|0);
      var $59$1=HEAP32[(($ld$27$1)>>2)];
      var $ld$28$0=((5243284)|0);
      var $60$0=HEAP32[(($ld$28$0)>>2)];
      var $ld$29$1=((5243288)|0);
      var $60$1=HEAP32[(($ld$29$1)>>2)];
      var $61=$j;
      var $62=$i;
      var $63=((($62)*(6))&-1);
      var $64=((($61)+($63))|0);
      var $65$0=$64;
      var $65$1=0;
      var $66$0 = _bitshift64Shl($60$0,$60$1,$65$0);var $66$1 = tempRet0;
      var $67$0=$59$0 & $66$0;
      var $67$1=$59$1 & $66$1;
      var $ld$30$0=((5243284)|0);
      var $68$0=HEAP32[(($ld$30$0)>>2)];
      var $ld$31$1=((5243288)|0);
      var $68$1=HEAP32[(($ld$31$1)>>2)];
      var $69=$j;
      var $70=$i;
      var $71=((($70)*(6))&-1);
      var $72=((($69)+($71))|0);
      var $73$0=$72;
      var $73$1=0;
      var $74$0 = _bitshift64Shl($68$0,$68$1,$73$0);var $74$1 = tempRet0;
      var $75=(($67$0|0) == ($74$0|0)) & (($67$1|0) == ($74$1|0));
      if ($75) { label = 18; break; } else { label = 19; break; }
    case 18: 
      var $77=$mod;
      var $78=($77)*(1000);
      var $79=$value;
      var $80=(($79)|(0));
      var $81=($80)+($78);
      var $82=(($81)&-1);
      $value=$82;
      label = 19; break;
    case 19: 
      label = 20; break;
    case 20: 
      var $85=$j;
      var $86=((($85)+(1))|0);
      $j=$86;
      label = 16; break;
    case 21: 
      label = 22; break;
    case 22: 
      var $89=$i;
      var $90=((($89)+(1))|0);
      $i=$90;
      label = 14; break;
    case 23: 
      $i=0;
      label = 24; break;
    case 24: 
      var $93=$i;
      var $94=(($93)|(0)) < 3;
      if ($94) { label = 25; break; } else { label = 33; break; }
    case 25: 
      $j=0;
      label = 26; break;
    case 26: 
      var $97=$j;
      var $98=(($97)|(0)) < 4;
      if ($98) { label = 27; break; } else { label = 31; break; }
    case 27: 
      var $ld$32$0=(($b)|0);
      var $100$0=HEAP32[(($ld$32$0)>>2)];
      var $ld$33$1=(($b+4)|0);
      var $100$1=HEAP32[(($ld$33$1)>>2)];
      var $ld$34$0=((5243268)|0);
      var $101$0=HEAP32[(($ld$34$0)>>2)];
      var $ld$35$1=((5243272)|0);
      var $101$1=HEAP32[(($ld$35$1)>>2)];
      var $102=$j;
      var $103=$i;
      var $104=((($103)*(6))&-1);
      var $105=((($102)+($104))|0);
      var $106$0=$105;
      var $106$1=0;
      var $107$0 = _bitshift64Shl($101$0,$101$1,$106$0);var $107$1 = tempRet0;
      var $108$0=$100$0 & $107$0;
      var $108$1=$100$1 & $107$1;
      var $ld$36$0=((5243268)|0);
      var $109$0=HEAP32[(($ld$36$0)>>2)];
      var $ld$37$1=((5243272)|0);
      var $109$1=HEAP32[(($ld$37$1)>>2)];
      var $110=$j;
      var $111=$i;
      var $112=((($111)*(6))&-1);
      var $113=((($110)+($112))|0);
      var $114$0=$113;
      var $114$1=0;
      var $115$0 = _bitshift64Shl($109$0,$109$1,$114$0);var $115$1 = tempRet0;
      var $116=(($108$0|0) == ($115$0|0)) & (($108$1|0) == ($115$1|0));
      if ($116) { label = 28; break; } else { label = 29; break; }
    case 28: 
      var $118=$mod;
      var $119=($118)*(1000);
      var $120=$value;
      var $121=(($120)|(0));
      var $122=($121)+($119);
      var $123=(($122)&-1);
      $value=$123;
      label = 29; break;
    case 29: 
      label = 30; break;
    case 30: 
      var $126=$j;
      var $127=((($126)+(1))|0);
      $j=$127;
      label = 26; break;
    case 31: 
      label = 32; break;
    case 32: 
      var $130=$i;
      var $131=((($130)+(1))|0);
      $i=$131;
      label = 24; break;
    case 33: 
      $i=0;
      label = 34; break;
    case 34: 
      var $134=$i;
      var $135=(($134)|(0)) < 3;
      if ($135) { label = 35; break; } else { label = 43; break; }
    case 35: 
      $j=0;
      label = 36; break;
    case 36: 
      var $138=$j;
      var $139=(($138)|(0)) < 4;
      if ($139) { label = 37; break; } else { label = 41; break; }
    case 37: 
      var $ld$38$0=(($b)|0);
      var $141$0=HEAP32[(($ld$38$0)>>2)];
      var $ld$39$1=(($b+4)|0);
      var $141$1=HEAP32[(($ld$39$1)>>2)];
      var $ld$40$0=((5243276)|0);
      var $142$0=HEAP32[(($ld$40$0)>>2)];
      var $ld$41$1=((5243280)|0);
      var $142$1=HEAP32[(($ld$41$1)>>2)];
      var $143=$j;
      var $144=$i;
      var $145=((($144)*(6))&-1);
      var $146=((($143)+($145))|0);
      var $147$0=$146;
      var $147$1=0;
      var $148$0 = _bitshift64Shl($142$0,$142$1,$147$0);var $148$1 = tempRet0;
      var $149$0=$141$0 & $148$0;
      var $149$1=$141$1 & $148$1;
      var $ld$42$0=((5243276)|0);
      var $150$0=HEAP32[(($ld$42$0)>>2)];
      var $ld$43$1=((5243280)|0);
      var $150$1=HEAP32[(($ld$43$1)>>2)];
      var $151=$j;
      var $152=$i;
      var $153=((($152)*(6))&-1);
      var $154=((($151)+($153))|0);
      var $155$0=$154;
      var $155$1=0;
      var $156$0 = _bitshift64Shl($150$0,$150$1,$155$0);var $156$1 = tempRet0;
      var $157=(($149$0|0) == ($156$0|0)) & (($149$1|0) == ($156$1|0));
      if ($157) { label = 38; break; } else { label = 39; break; }
    case 38: 
      var $159=$mod;
      var $160=($159)*(1000);
      var $161=$value;
      var $162=(($161)|(0));
      var $163=($162)+($160);
      var $164=(($163)&-1);
      $value=$164;
      label = 39; break;
    case 39: 
      label = 40; break;
    case 40: 
      var $167=$j;
      var $168=((($167)+(1))|0);
      $j=$168;
      label = 36; break;
    case 41: 
      label = 42; break;
    case 42: 
      var $171=$i;
      var $172=((($171)+(1))|0);
      $i=$172;
      label = 34; break;
    case 43: 
      $i=0;
      label = 44; break;
    case 44: 
      var $175=$i;
      var $176=(($175)|(0)) < 21;
      if ($176) { label = 45; break; } else { label = 50; break; }
    case 45: 
      var $ld$44$0=(($b)|0);
      var $178$0=HEAP32[(($ld$44$0)>>2)];
      var $ld$45$1=(($b+4)|0);
      var $178$1=HEAP32[(($ld$45$1)>>2)];
      var $ld$46$0=((5243292)|0);
      var $179$0=HEAP32[(($ld$46$0)>>2)];
      var $ld$47$1=((5243296)|0);
      var $179$1=HEAP32[(($ld$47$1)>>2)];
      var $180=$i;
      var $181$0=$180;
      var $181$1=0;
      var $182$0 = _bitshift64Shl($179$0,$179$1,$181$0);var $182$1 = tempRet0;
      var $183$0=$178$0 & $182$0;
      var $183$1=$178$1 & $182$1;
      var $ld$48$0=((5243292)|0);
      var $184$0=HEAP32[(($ld$48$0)>>2)];
      var $ld$49$1=((5243296)|0);
      var $184$1=HEAP32[(($ld$49$1)>>2)];
      var $185=$i;
      var $186$0=$185;
      var $186$1=0;
      var $187$0 = _bitshift64Shl($184$0,$184$1,$186$0);var $187$1 = tempRet0;
      var $188=(($183$0|0) == ($187$0|0)) & (($183$1|0) == ($187$1|0));
      if ($188) { label = 46; break; } else { label = 48; break; }
    case 46: 
      var $ld$50$0=(($bo)|0);
      var $190$0=HEAP32[(($ld$50$0)>>2)];
      var $ld$51$1=(($bo+4)|0);
      var $190$1=HEAP32[(($ld$51$1)>>2)];
      var $ld$52$0=((5243260)|0);
      var $191$0=HEAP32[(($ld$52$0)>>2)];
      var $ld$53$1=((5243264)|0);
      var $191$1=HEAP32[(($ld$53$1)>>2)];
      var $192=$i;
      var $193$0=$192;
      var $193$1=0;
      var $194$0 = _bitshift64Shl($191$0,$191$1,$193$0);var $194$1 = tempRet0;
      var $195$0=$190$0 & $194$0;
      var $195$1=$190$1 & $194$1;
      var $$etemp$54$0=0;
      var $$etemp$54$1=0;
      var $196=(($195$0|0) != ($$etemp$54$0|0)) | (($195$1|0) != ($$etemp$54$1|0));
      if ($196) { label = 48; break; } else { label = 47; break; }
    case 47: 
      var $198=$mod;
      var $199=($198)*(20);
      var $200=$value;
      var $201=(($200)|(0));
      var $202=($201)+($199);
      var $203=(($202)&-1);
      $value=$203;
      label = 48; break;
    case 48: 
      label = 49; break;
    case 49: 
      var $206=$i;
      var $207=((($206)+(1))|0);
      $i=$207;
      label = 44; break;
    case 50: 
      $i=0;
      label = 51; break;
    case 51: 
      var $210=$i;
      var $211=(($210)|(0)) < 6;
      if ($211) { label = 52; break; } else { label = 60; break; }
    case 52: 
      $j=0;
      label = 53; break;
    case 53: 
      var $214=$j;
      var $215=(($214)|(0)) < 5;
      if ($215) { label = 54; break; } else { label = 58; break; }
    case 54: 
      var $ld$55$0=(($b)|0);
      var $217$0=HEAP32[(($ld$55$0)>>2)];
      var $ld$56$1=(($b+4)|0);
      var $217$1=HEAP32[(($ld$56$1)>>2)];
      var $ld$57$0=((5243316)|0);
      var $218$0=HEAP32[(($ld$57$0)>>2)];
      var $ld$58$1=((5243320)|0);
      var $218$1=HEAP32[(($ld$58$1)>>2)];
      var $219=$j;
      var $220=$i;
      var $221=((($220)*(6))&-1);
      var $222=((($219)+($221))|0);
      var $223$0=$222;
      var $223$1=0;
      var $224$0 = _bitshift64Shl($218$0,$218$1,$223$0);var $224$1 = tempRet0;
      var $225$0=$217$0 & $224$0;
      var $225$1=$217$1 & $224$1;
      var $ld$59$0=((5243316)|0);
      var $226$0=HEAP32[(($ld$59$0)>>2)];
      var $ld$60$1=((5243320)|0);
      var $226$1=HEAP32[(($ld$60$1)>>2)];
      var $227=$j;
      var $228=$i;
      var $229=((($228)*(6))&-1);
      var $230=((($227)+($229))|0);
      var $231$0=$230;
      var $231$1=0;
      var $232$0 = _bitshift64Shl($226$0,$226$1,$231$0);var $232$1 = tempRet0;
      var $233=(($225$0|0) == ($232$0|0)) & (($225$1|0) == ($232$1|0));
      if ($233) { label = 55; break; } else { label = 56; break; }
    case 55: 
      var $235=$mod;
      var $236=($235)*(20);
      var $237=$value;
      var $238=(($237)|(0));
      var $239=($238)+($236);
      var $240=(($239)&-1);
      $value=$240;
      label = 56; break;
    case 56: 
      label = 57; break;
    case 57: 
      var $243=$j;
      var $244=((($243)+(1))|0);
      $j=$244;
      label = 53; break;
    case 58: 
      label = 59; break;
    case 59: 
      var $247=$i;
      var $248=((($247)+(1))|0);
      $i=$248;
      label = 51; break;
    case 60: 
      $i=0;
      label = 61; break;
    case 61: 
      var $251=$i;
      var $252=(($251)|(0)) < 3;
      if ($252) { label = 62; break; } else { label = 70; break; }
    case 62: 
      $j=0;
      label = 63; break;
    case 63: 
      var $255=$j;
      var $256=(($255)|(0)) < 4;
      if ($256) { label = 64; break; } else { label = 68; break; }
    case 64: 
      var $ld$61$0=(($b)|0);
      var $258$0=HEAP32[(($ld$61$0)>>2)];
      var $ld$62$1=(($b+4)|0);
      var $258$1=HEAP32[(($ld$62$1)>>2)];
      var $ld$63$0=((5243300)|0);
      var $259$0=HEAP32[(($ld$63$0)>>2)];
      var $ld$64$1=((5243304)|0);
      var $259$1=HEAP32[(($ld$64$1)>>2)];
      var $260=$j;
      var $261=$i;
      var $262=((($261)*(6))&-1);
      var $263=((($260)+($262))|0);
      var $264$0=$263;
      var $264$1=0;
      var $265$0 = _bitshift64Shl($259$0,$259$1,$264$0);var $265$1 = tempRet0;
      var $266$0=$258$0 & $265$0;
      var $266$1=$258$1 & $265$1;
      var $ld$65$0=((5243300)|0);
      var $267$0=HEAP32[(($ld$65$0)>>2)];
      var $ld$66$1=((5243304)|0);
      var $267$1=HEAP32[(($ld$66$1)>>2)];
      var $268=$j;
      var $269=$i;
      var $270=((($269)*(6))&-1);
      var $271=((($268)+($270))|0);
      var $272$0=$271;
      var $272$1=0;
      var $273$0 = _bitshift64Shl($267$0,$267$1,$272$0);var $273$1 = tempRet0;
      var $274=(($266$0|0) == ($273$0|0)) & (($266$1|0) == ($273$1|0));
      if ($274) { label = 65; break; } else { label = 66; break; }
    case 65: 
      var $276=$mod;
      var $277=($276)*(20);
      var $278=$value;
      var $279=(($278)|(0));
      var $280=($279)+($277);
      var $281=(($280)&-1);
      $value=$281;
      label = 66; break;
    case 66: 
      label = 67; break;
    case 67: 
      var $284=$j;
      var $285=((($284)+(1))|0);
      $j=$285;
      label = 63; break;
    case 68: 
      label = 69; break;
    case 69: 
      var $288=$i;
      var $289=((($288)+(1))|0);
      $i=$289;
      label = 61; break;
    case 70: 
      $i=0;
      label = 71; break;
    case 71: 
      var $292=$i;
      var $293=(($292)|(0)) < 3;
      if ($293) { label = 72; break; } else { label = 80; break; }
    case 72: 
      $j=0;
      label = 73; break;
    case 73: 
      var $296=$j;
      var $297=(($296)|(0)) < 4;
      if ($297) { label = 74; break; } else { label = 78; break; }
    case 74: 
      var $ld$67$0=(($b)|0);
      var $299$0=HEAP32[(($ld$67$0)>>2)];
      var $ld$68$1=(($b+4)|0);
      var $299$1=HEAP32[(($ld$68$1)>>2)];
      var $ld$69$0=((5243308)|0);
      var $300$0=HEAP32[(($ld$69$0)>>2)];
      var $ld$70$1=((5243312)|0);
      var $300$1=HEAP32[(($ld$70$1)>>2)];
      var $301=$j;
      var $302=$i;
      var $303=((($302)*(6))&-1);
      var $304=((($301)+($303))|0);
      var $305$0=$304;
      var $305$1=0;
      var $306$0 = _bitshift64Shl($300$0,$300$1,$305$0);var $306$1 = tempRet0;
      var $307$0=$299$0 & $306$0;
      var $307$1=$299$1 & $306$1;
      var $ld$71$0=((5243308)|0);
      var $308$0=HEAP32[(($ld$71$0)>>2)];
      var $ld$72$1=((5243312)|0);
      var $308$1=HEAP32[(($ld$72$1)>>2)];
      var $309=$j;
      var $310=$i;
      var $311=((($310)*(6))&-1);
      var $312=((($309)+($311))|0);
      var $313$0=$312;
      var $313$1=0;
      var $314$0 = _bitshift64Shl($308$0,$308$1,$313$0);var $314$1 = tempRet0;
      var $315=(($307$0|0) == ($314$0|0)) & (($307$1|0) == ($314$1|0));
      if ($315) { label = 75; break; } else { label = 76; break; }
    case 75: 
      var $317=$mod;
      var $318=($317)*(20);
      var $319=$value;
      var $320=(($319)|(0));
      var $321=($320)+($318);
      var $322=(($321)&-1);
      $value=$322;
      label = 76; break;
    case 76: 
      label = 77; break;
    case 77: 
      var $325=$j;
      var $326=((($325)+(1))|0);
      $j=$326;
      label = 73; break;
    case 78: 
      label = 79; break;
    case 79: 
      var $329=$i;
      var $330=((($329)+(1))|0);
      $i=$330;
      label = 71; break;
    case 80: 
      $i=0;
      label = 81; break;
    case 81: 
      var $333=$i;
      var $334=(($333)|(0)) < 21;
      if ($334) { label = 82; break; } else { label = 87; break; }
    case 82: 
      var $ld$73$0=(($b)|0);
      var $336$0=HEAP32[(($ld$73$0)>>2)];
      var $ld$74$1=(($b+4)|0);
      var $336$1=HEAP32[(($ld$74$1)>>2)];
      var $ld$75$0=((5243324)|0);
      var $337$0=HEAP32[(($ld$75$0)>>2)];
      var $ld$76$1=((5243328)|0);
      var $337$1=HEAP32[(($ld$76$1)>>2)];
      var $338=$i;
      var $339$0=$338;
      var $339$1=0;
      var $340$0 = _bitshift64Shl($337$0,$337$1,$339$0);var $340$1 = tempRet0;
      var $341$0=$336$0 & $340$0;
      var $341$1=$336$1 & $340$1;
      var $ld$77$0=((5243324)|0);
      var $342$0=HEAP32[(($ld$77$0)>>2)];
      var $ld$78$1=((5243328)|0);
      var $342$1=HEAP32[(($ld$78$1)>>2)];
      var $343=$i;
      var $344$0=$343;
      var $344$1=0;
      var $345$0 = _bitshift64Shl($342$0,$342$1,$344$0);var $345$1 = tempRet0;
      var $346=(($341$0|0) == ($345$0|0)) & (($341$1|0) == ($345$1|0));
      if ($346) { label = 83; break; } else { label = 85; break; }
    case 83: 
      var $ld$79$0=(($bo)|0);
      var $348$0=HEAP32[(($ld$79$0)>>2)];
      var $ld$80$1=(($bo+4)|0);
      var $348$1=HEAP32[(($ld$80$1)>>2)];
      var $ld$81$0=((5243260)|0);
      var $349$0=HEAP32[(($ld$81$0)>>2)];
      var $ld$82$1=((5243264)|0);
      var $349$1=HEAP32[(($ld$82$1)>>2)];
      var $350=$i;
      var $351$0=$350;
      var $351$1=0;
      var $352$0 = _bitshift64Shl($349$0,$349$1,$351$0);var $352$1 = tempRet0;
      var $353$0=$348$0 & $352$0;
      var $353$1=$348$1 & $352$1;
      var $$etemp$83$0=0;
      var $$etemp$83$1=0;
      var $354=(($353$0|0) != ($$etemp$83$0|0)) | (($353$1|0) != ($$etemp$83$1|0));
      if ($354) { label = 85; break; } else { label = 84; break; }
    case 84: 
      var $356=$mod;
      var $357=($356)*(5);
      var $358=$value;
      var $359=(($358)|(0));
      var $360=($359)+($357);
      var $361=(($360)&-1);
      $value=$361;
      label = 85; break;
    case 85: 
      label = 86; break;
    case 86: 
      var $364=$i;
      var $365=((($364)+(1))|0);
      $i=$365;
      label = 81; break;
    case 87: 
      $i=0;
      label = 88; break;
    case 88: 
      var $368=$i;
      var $369=(($368)|(0)) < 6;
      if ($369) { label = 89; break; } else { label = 97; break; }
    case 89: 
      $j=0;
      label = 90; break;
    case 90: 
      var $372=$j;
      var $373=(($372)|(0)) < 6;
      if ($373) { label = 91; break; } else { label = 95; break; }
    case 91: 
      var $ld$84$0=(($b)|0);
      var $375$0=HEAP32[(($ld$84$0)>>2)];
      var $ld$85$1=(($b+4)|0);
      var $375$1=HEAP32[(($ld$85$1)>>2)];
      var $ld$86$0=((5243348)|0);
      var $376$0=HEAP32[(($ld$86$0)>>2)];
      var $ld$87$1=((5243352)|0);
      var $376$1=HEAP32[(($ld$87$1)>>2)];
      var $377=$j;
      var $378=$i;
      var $379=((($378)*(6))&-1);
      var $380=((($377)+($379))|0);
      var $381$0=$380;
      var $381$1=0;
      var $382$0 = _bitshift64Shl($376$0,$376$1,$381$0);var $382$1 = tempRet0;
      var $383$0=$375$0 & $382$0;
      var $383$1=$375$1 & $382$1;
      var $ld$88$0=((5243348)|0);
      var $384$0=HEAP32[(($ld$88$0)>>2)];
      var $ld$89$1=((5243352)|0);
      var $384$1=HEAP32[(($ld$89$1)>>2)];
      var $385=$j;
      var $386=$i;
      var $387=((($386)*(6))&-1);
      var $388=((($385)+($387))|0);
      var $389$0=$388;
      var $389$1=0;
      var $390$0 = _bitshift64Shl($384$0,$384$1,$389$0);var $390$1 = tempRet0;
      var $391=(($383$0|0) == ($390$0|0)) & (($383$1|0) == ($390$1|0));
      if ($391) { label = 92; break; } else { label = 93; break; }
    case 92: 
      var $393=$mod;
      var $394=($393)*(5);
      var $395=$value;
      var $396=(($395)|(0));
      var $397=($396)+($394);
      var $398=(($397)&-1);
      $value=$398;
      label = 93; break;
    case 93: 
      label = 94; break;
    case 94: 
      var $401=$j;
      var $402=((($401)+(1))|0);
      $j=$402;
      label = 90; break;
    case 95: 
      label = 96; break;
    case 96: 
      var $405=$i;
      var $406=((($405)+(1))|0);
      $i=$406;
      label = 88; break;
    case 97: 
      $i=0;
      label = 98; break;
    case 98: 
      var $409=$i;
      var $410=(($409)|(0)) < 3;
      if ($410) { label = 99; break; } else { label = 107; break; }
    case 99: 
      $j=0;
      label = 100; break;
    case 100: 
      var $413=$j;
      var $414=(($413)|(0)) < 4;
      if ($414) { label = 101; break; } else { label = 105; break; }
    case 101: 
      var $ld$90$0=(($b)|0);
      var $416$0=HEAP32[(($ld$90$0)>>2)];
      var $ld$91$1=(($b+4)|0);
      var $416$1=HEAP32[(($ld$91$1)>>2)];
      var $ld$92$0=((5243332)|0);
      var $417$0=HEAP32[(($ld$92$0)>>2)];
      var $ld$93$1=((5243336)|0);
      var $417$1=HEAP32[(($ld$93$1)>>2)];
      var $418=$j;
      var $419=$i;
      var $420=((($419)*(6))&-1);
      var $421=((($418)+($420))|0);
      var $422$0=$421;
      var $422$1=0;
      var $423$0 = _bitshift64Shl($417$0,$417$1,$422$0);var $423$1 = tempRet0;
      var $424$0=$416$0 & $423$0;
      var $424$1=$416$1 & $423$1;
      var $ld$94$0=((5243332)|0);
      var $425$0=HEAP32[(($ld$94$0)>>2)];
      var $ld$95$1=((5243336)|0);
      var $425$1=HEAP32[(($ld$95$1)>>2)];
      var $426=$j;
      var $427=$i;
      var $428=((($427)*(6))&-1);
      var $429=((($426)+($428))|0);
      var $430$0=$429;
      var $430$1=0;
      var $431$0 = _bitshift64Shl($425$0,$425$1,$430$0);var $431$1 = tempRet0;
      var $432=(($424$0|0) == ($431$0|0)) & (($424$1|0) == ($431$1|0));
      if ($432) { label = 102; break; } else { label = 103; break; }
    case 102: 
      var $434=$mod;
      var $435=($434)*(5);
      var $436=$value;
      var $437=(($436)|(0));
      var $438=($437)+($435);
      var $439=(($438)&-1);
      $value=$439;
      label = 103; break;
    case 103: 
      label = 104; break;
    case 104: 
      var $442=$j;
      var $443=((($442)+(1))|0);
      $j=$443;
      label = 100; break;
    case 105: 
      label = 106; break;
    case 106: 
      var $446=$i;
      var $447=((($446)+(1))|0);
      $i=$447;
      label = 98; break;
    case 107: 
      $i=0;
      label = 108; break;
    case 108: 
      var $450=$i;
      var $451=(($450)|(0)) < 3;
      if ($451) { label = 109; break; } else { label = 117; break; }
    case 109: 
      $j=0;
      label = 110; break;
    case 110: 
      var $454=$j;
      var $455=(($454)|(0)) < 4;
      if ($455) { label = 111; break; } else { label = 115; break; }
    case 111: 
      var $ld$96$0=(($b)|0);
      var $457$0=HEAP32[(($ld$96$0)>>2)];
      var $ld$97$1=(($b+4)|0);
      var $457$1=HEAP32[(($ld$97$1)>>2)];
      var $ld$98$0=((5243340)|0);
      var $458$0=HEAP32[(($ld$98$0)>>2)];
      var $ld$99$1=((5243344)|0);
      var $458$1=HEAP32[(($ld$99$1)>>2)];
      var $459=$j;
      var $460=$i;
      var $461=((($460)*(6))&-1);
      var $462=((($459)+($461))|0);
      var $463$0=$462;
      var $463$1=0;
      var $464$0 = _bitshift64Shl($458$0,$458$1,$463$0);var $464$1 = tempRet0;
      var $465$0=$457$0 & $464$0;
      var $465$1=$457$1 & $464$1;
      var $ld$100$0=((5243340)|0);
      var $466$0=HEAP32[(($ld$100$0)>>2)];
      var $ld$101$1=((5243344)|0);
      var $466$1=HEAP32[(($ld$101$1)>>2)];
      var $467=$j;
      var $468=$i;
      var $469=((($468)*(6))&-1);
      var $470=((($467)+($469))|0);
      var $471$0=$470;
      var $471$1=0;
      var $472$0 = _bitshift64Shl($466$0,$466$1,$471$0);var $472$1 = tempRet0;
      var $473=(($465$0|0) == ($472$0|0)) & (($465$1|0) == ($472$1|0));
      if ($473) { label = 112; break; } else { label = 113; break; }
    case 112: 
      var $475=$mod;
      var $476=($475)*(5);
      var $477=$value;
      var $478=(($477)|(0));
      var $479=($478)+($476);
      var $480=(($479)&-1);
      $value=$480;
      label = 113; break;
    case 113: 
      label = 114; break;
    case 114: 
      var $483=$j;
      var $484=((($483)+(1))|0);
      $j=$484;
      label = 110; break;
    case 115: 
      label = 116; break;
    case 116: 
      var $487=$i;
      var $488=((($487)+(1))|0);
      $i=$488;
      label = 108; break;
    case 117: 
      label = 118; break;
    case 118: 
      var $491=$k;
      var $492=((($491)+(1))|0);
      $k=$492;
      label = 3; break;
    case 119: 
      var $494=$value;
      STACKTOP = __stackBase__;
      return $494;
    default: assert(0, "bad label: " + label);
  }
}
function _bit_place_piece($col, $player, $b1, $b2) {
  var label = 0;
  var __stackBase__  = STACKTOP; STACKTOP = (STACKTOP + 16)|0; assert(!(STACKTOP&3)); assert((STACKTOP|0) < (STACK_MAX|0));
  label = 2; 
  while(1) switch(label) {
    case 2: 
      var $1;
      var $2;
      var $3;
      var $4;
      var $5;
      var $board=__stackBase__;
      var $one=(__stackBase__)+(8);
      var $i;
      $2=$col;
      $3=$player;
      $4=$b1;
      $5=$b2;
      var $6=$4;
      var $ld$0$0=(($6)|0);
      var $7$0=HEAP32[(($ld$0$0)>>2)];
      var $ld$1$1=(($6+4)|0);
      var $7$1=HEAP32[(($ld$1$1)>>2)];
      var $8=$5;
      var $ld$2$0=(($8)|0);
      var $9$0=HEAP32[(($ld$2$0)>>2)];
      var $ld$3$1=(($8+4)|0);
      var $9$1=HEAP32[(($ld$3$1)>>2)];
      var $10$0=$7$0 | $9$0;
      var $10$1=$7$1 | $9$1;
      var $st$4$0=(($board)|0);
      HEAP32[(($st$4$0)>>2)]=$10$0;
      var $st$5$1=(($board+4)|0);
      HEAP32[(($st$5$1)>>2)]=$10$1;
      var $$etemp$6$0=1;
      var $$etemp$6$1=0;
      var $st$7$0=(($one)|0);
      HEAP32[(($st$7$0)>>2)]=$$etemp$6$0;
      var $st$8$1=(($one+4)|0);
      HEAP32[(($st$8$1)>>2)]=$$etemp$6$1;
      $i=0;
      label = 3; break;
    case 3: 
      var $12=$i;
      var $13=(($12)|(0)) < 6;
      if ($13) { label = 4; break; } else { label = 11; break; }
    case 4: 
      var $ld$9$0=(($board)|0);
      var $15$0=HEAP32[(($ld$9$0)>>2)];
      var $ld$10$1=(($board+4)|0);
      var $15$1=HEAP32[(($ld$10$1)>>2)];
      var $ld$11$0=(($one)|0);
      var $16$0=HEAP32[(($ld$11$0)>>2)];
      var $ld$12$1=(($one+4)|0);
      var $16$1=HEAP32[(($ld$12$1)>>2)];
      var $17=$i;
      var $18=((($17)*(7))&-1);
      var $19=$2;
      var $20=((($18)+($19))|0);
      var $21$0=$20;
      var $21$1=0;
      var $22$0 = _bitshift64Shl($16$0,$16$1,$21$0);var $22$1 = tempRet0;
      var $23$0=$15$0 & $22$0;
      var $23$1=$15$1 & $22$1;
      var $$etemp$13$0=0;
      var $$etemp$13$1=0;
      var $24=(($23$0|0) != ($$etemp$13$0|0)) | (($23$1|0) != ($$etemp$13$1|0));
      if ($24) { label = 9; break; } else { label = 5; break; }
    case 5: 
      var $26=$3;
      var $27=(($26)|(0))==1;
      if ($27) { label = 6; break; } else { label = 7; break; }
    case 6: 
      var $29=$4;
      var $ld$14$0=(($29)|0);
      var $30$0=HEAP32[(($ld$14$0)>>2)];
      var $ld$15$1=(($29+4)|0);
      var $30$1=HEAP32[(($ld$15$1)>>2)];
      var $ld$16$0=(($one)|0);
      var $31$0=HEAP32[(($ld$16$0)>>2)];
      var $ld$17$1=(($one+4)|0);
      var $31$1=HEAP32[(($ld$17$1)>>2)];
      var $32=$i;
      var $33=((($32)*(7))&-1);
      var $34=$2;
      var $35=((($33)+($34))|0);
      var $36$0=$35;
      var $36$1=0;
      var $37$0 = _bitshift64Shl($31$0,$31$1,$36$0);var $37$1 = tempRet0;
      var $38$0=$30$0 | $37$0;
      var $38$1=$30$1 | $37$1;
      var $39=$4;
      var $st$18$0=(($39)|0);
      HEAP32[(($st$18$0)>>2)]=$38$0;
      var $st$19$1=(($39+4)|0);
      HEAP32[(($st$19$1)>>2)]=$38$1;
      label = 8; break;
    case 7: 
      var $41=$5;
      var $ld$20$0=(($41)|0);
      var $42$0=HEAP32[(($ld$20$0)>>2)];
      var $ld$21$1=(($41+4)|0);
      var $42$1=HEAP32[(($ld$21$1)>>2)];
      var $ld$22$0=(($one)|0);
      var $43$0=HEAP32[(($ld$22$0)>>2)];
      var $ld$23$1=(($one+4)|0);
      var $43$1=HEAP32[(($ld$23$1)>>2)];
      var $44=$i;
      var $45=((($44)*(7))&-1);
      var $46=$2;
      var $47=((($45)+($46))|0);
      var $48$0=$47;
      var $48$1=0;
      var $49$0 = _bitshift64Shl($43$0,$43$1,$48$0);var $49$1 = tempRet0;
      var $50$0=$42$0 | $49$0;
      var $50$1=$42$1 | $49$1;
      var $51=$5;
      var $st$24$0=(($51)|0);
      HEAP32[(($st$24$0)>>2)]=$50$0;
      var $st$25$1=(($51+4)|0);
      HEAP32[(($st$25$1)>>2)]=$50$1;
      label = 8; break;
    case 8: 
      $1=0;
      label = 12; break;
    case 9: 
      label = 10; break;
    case 10: 
      var $55=$i;
      var $56=((($55)+(1))|0);
      $i=$56;
      label = 3; break;
    case 11: 
      $1=1;
      label = 12; break;
    case 12: 
      var $59=$1;
      STACKTOP = __stackBase__;
      return $59;
    default: assert(0, "bad label: " + label);
  }
}
function _think($b, $who, $ab) {
  var label = 0;
  var __stackBase__  = STACKTOP; STACKTOP = (STACKTOP + 28)|0; assert(!(STACKTOP&3)); assert((STACKTOP|0) < (STACK_MAX|0));
  label = 2; 
  while(1) switch(label) {
    case 2: 
      var $1;
      var $2;
      var $3;
      var $b1=__stackBase__;
      var $b2=(__stackBase__)+(8);
      var $one=(__stackBase__)+(16);
      var $i;
      var $j;
      var $player;
      var $col=(__stackBase__)+(24);
      $1=$b;
      $2=$who;
      $3=$ab;
      var $$etemp$0$0=0;
      var $$etemp$0$1=0;
      var $st$1$0=(($b1)|0);
      HEAP32[(($st$1$0)>>2)]=$$etemp$0$0;
      var $st$2$1=(($b1+4)|0);
      HEAP32[(($st$2$1)>>2)]=$$etemp$0$1;
      var $$etemp$3$0=0;
      var $$etemp$3$1=0;
      var $st$4$0=(($b2)|0);
      HEAP32[(($st$4$0)>>2)]=$$etemp$3$0;
      var $st$5$1=(($b2+4)|0);
      HEAP32[(($st$5$1)>>2)]=$$etemp$3$1;
      var $$etemp$6$0=1;
      var $$etemp$6$1=0;
      var $st$7$0=(($one)|0);
      HEAP32[(($st$7$0)>>2)]=$$etemp$6$0;
      var $st$8$1=(($one+4)|0);
      HEAP32[(($st$8$1)>>2)]=$$etemp$6$1;
      $player=111;
      $j=0;
      label = 3; break;
    case 3: 
      var $5=$j;
      var $6=(($5)|(0)) < 6;
      if ($6) { label = 4; break; } else { label = 12; break; }
    case 4: 
      $i=0;
      label = 5; break;
    case 5: 
      var $9=$i;
      var $10=(($9)|(0)) < 7;
      if ($10) { label = 6; break; } else { label = 10; break; }
    case 6: 
      var $12=$j;
      var $13=$i;
      var $14=$1;
      var $15=(($14+((($13)*(7))&-1))|0);
      var $16=(($15+$12)|0);
      var $17=HEAP8[($16)];
      var $18=(($17 << 24) >> 24);
      var $19=$player;
      var $20=(($19 << 24) >> 24);
      var $21=(($18)|(0))==(($20)|(0));
      if ($21) { label = 7; break; } else { label = 8; break; }
    case 7: 
      var $ld$9$0=(($b1)|0);
      var $23$0=HEAP32[(($ld$9$0)>>2)];
      var $ld$10$1=(($b1+4)|0);
      var $23$1=HEAP32[(($ld$10$1)>>2)];
      var $ld$11$0=(($one)|0);
      var $24$0=HEAP32[(($ld$11$0)>>2)];
      var $ld$12$1=(($one+4)|0);
      var $24$1=HEAP32[(($ld$12$1)>>2)];
      var $25=$i;
      var $26=$j;
      var $27=((($26)*(7))&-1);
      var $28=((($25)+($27))|0);
      var $29$0=$28;
      var $29$1=0;
      var $30$0 = _bitshift64Shl($24$0,$24$1,$29$0);var $30$1 = tempRet0;
      var $31$0=$23$0 | $30$0;
      var $31$1=$23$1 | $30$1;
      var $st$13$0=(($b1)|0);
      HEAP32[(($st$13$0)>>2)]=$31$0;
      var $st$14$1=(($b1+4)|0);
      HEAP32[(($st$14$1)>>2)]=$31$1;
      label = 8; break;
    case 8: 
      label = 9; break;
    case 9: 
      var $34=$i;
      var $35=((($34)+(1))|0);
      $i=$35;
      label = 5; break;
    case 10: 
      label = 11; break;
    case 11: 
      var $38=$j;
      var $39=((($38)+(1))|0);
      $j=$39;
      label = 3; break;
    case 12: 
      $player=120;
      $j=0;
      label = 13; break;
    case 13: 
      var $42=$j;
      var $43=(($42)|(0)) < 6;
      if ($43) { label = 14; break; } else { label = 22; break; }
    case 14: 
      $i=0;
      label = 15; break;
    case 15: 
      var $46=$i;
      var $47=(($46)|(0)) < 7;
      if ($47) { label = 16; break; } else { label = 20; break; }
    case 16: 
      var $49=$j;
      var $50=$i;
      var $51=$1;
      var $52=(($51+((($50)*(7))&-1))|0);
      var $53=(($52+$49)|0);
      var $54=HEAP8[($53)];
      var $55=(($54 << 24) >> 24);
      var $56=$player;
      var $57=(($56 << 24) >> 24);
      var $58=(($55)|(0))==(($57)|(0));
      if ($58) { label = 17; break; } else { label = 18; break; }
    case 17: 
      var $ld$15$0=(($b2)|0);
      var $60$0=HEAP32[(($ld$15$0)>>2)];
      var $ld$16$1=(($b2+4)|0);
      var $60$1=HEAP32[(($ld$16$1)>>2)];
      var $ld$17$0=(($one)|0);
      var $61$0=HEAP32[(($ld$17$0)>>2)];
      var $ld$18$1=(($one+4)|0);
      var $61$1=HEAP32[(($ld$18$1)>>2)];
      var $62=$i;
      var $63=$j;
      var $64=((($63)*(7))&-1);
      var $65=((($62)+($64))|0);
      var $66$0=$65;
      var $66$1=0;
      var $67$0 = _bitshift64Shl($61$0,$61$1,$66$0);var $67$1 = tempRet0;
      var $68$0=$60$0 | $67$0;
      var $68$1=$60$1 | $67$1;
      var $st$19$0=(($b2)|0);
      HEAP32[(($st$19$0)>>2)]=$68$0;
      var $st$20$1=(($b2+4)|0);
      HEAP32[(($st$20$1)>>2)]=$68$1;
      label = 18; break;
    case 18: 
      label = 19; break;
    case 19: 
      var $71=$i;
      var $72=((($71)+(1))|0);
      $i=$72;
      label = 15; break;
    case 20: 
      label = 21; break;
    case 21: 
      var $75=$j;
      var $76=((($75)+(1))|0);
      $j=$76;
      label = 13; break;
    case 22: 
      var $78=$3;
      var $79=(($78)|(0))==1;
      if ($79) { label = 23; break; } else { label = 28; break; }
    case 23: 
      var $81=$2;
      var $82=(($81)|(0))==2;
      if ($82) { label = 24; break; } else { label = 25; break; }
    case 24: 
      var $ld$21$0=(($b1)|0);
      var $84$0=HEAP32[(($ld$21$0)>>2)];
      var $ld$22$1=(($b1+4)|0);
      var $84$1=HEAP32[(($ld$22$1)>>2)];
      var $ld$23$0=(($b2)|0);
      var $85$0=HEAP32[(($ld$23$0)>>2)];
      var $ld$24$1=(($b2+4)|0);
      var $85$1=HEAP32[(($ld$24$1)>>2)];
      var $86=_minimax_comp_ab(1, $84$0, $84$1, $85$0, $85$1, $col, -100000, 100000);
      label = 25; break;
    case 25: 
      var $88=$2;
      var $89=(($88)|(0))==1;
      if ($89) { label = 26; break; } else { label = 27; break; }
    case 26: 
      var $ld$25$0=(($b1)|0);
      var $91$0=HEAP32[(($ld$25$0)>>2)];
      var $ld$26$1=(($b1+4)|0);
      var $91$1=HEAP32[(($ld$26$1)>>2)];
      var $ld$27$0=(($b2)|0);
      var $92$0=HEAP32[(($ld$27$0)>>2)];
      var $ld$28$1=(($b2+4)|0);
      var $92$1=HEAP32[(($ld$28$1)>>2)];
      var $93=_minimax_player_ab(1, $91$0, $91$1, $92$0, $92$1, $col, -100000, 100000);
      label = 27; break;
    case 27: 
      label = 40; break;
    case 28: 
      var $96=$3;
      var $97=(($96)|(0))==2;
      if ($97) { label = 29; break; } else { label = 34; break; }
    case 29: 
      var $99=$2;
      var $100=(($99)|(0))==2;
      if ($100) { label = 30; break; } else { label = 31; break; }
    case 30: 
      var $ld$29$0=(($b1)|0);
      var $102$0=HEAP32[(($ld$29$0)>>2)];
      var $ld$30$1=(($b1+4)|0);
      var $102$1=HEAP32[(($ld$30$1)>>2)];
      var $ld$31$0=(($b2)|0);
      var $103$0=HEAP32[(($ld$31$0)>>2)];
      var $ld$32$1=(($b2+4)|0);
      var $103$1=HEAP32[(($ld$32$1)>>2)];
      var $104=_minimax_comp_ab2(1, $102$0, $102$1, $103$0, $103$1, $col, 100000);
      label = 31; break;
    case 31: 
      var $106=$2;
      var $107=(($106)|(0))==1;
      if ($107) { label = 32; break; } else { label = 33; break; }
    case 32: 
      var $ld$33$0=(($b1)|0);
      var $109$0=HEAP32[(($ld$33$0)>>2)];
      var $ld$34$1=(($b1+4)|0);
      var $109$1=HEAP32[(($ld$34$1)>>2)];
      var $ld$35$0=(($b2)|0);
      var $110$0=HEAP32[(($ld$35$0)>>2)];
      var $ld$36$1=(($b2+4)|0);
      var $110$1=HEAP32[(($ld$36$1)>>2)];
      var $111=_minimax_player_ab2(1, $109$0, $109$1, $110$0, $110$1, $col, -100000);
      label = 33; break;
    case 33: 
      label = 39; break;
    case 34: 
      var $114=$2;
      var $115=(($114)|(0))==2;
      if ($115) { label = 35; break; } else { label = 36; break; }
    case 35: 
      var $ld$37$0=(($b1)|0);
      var $117$0=HEAP32[(($ld$37$0)>>2)];
      var $ld$38$1=(($b1+4)|0);
      var $117$1=HEAP32[(($ld$38$1)>>2)];
      var $ld$39$0=(($b2)|0);
      var $118$0=HEAP32[(($ld$39$0)>>2)];
      var $ld$40$1=(($b2+4)|0);
      var $118$1=HEAP32[(($ld$40$1)>>2)];
      var $119=_minimax_comp(1, $117$0, $117$1, $118$0, $118$1, $col);
      label = 36; break;
    case 36: 
      var $121=$2;
      var $122=(($121)|(0))==1;
      if ($122) { label = 37; break; } else { label = 38; break; }
    case 37: 
      var $ld$41$0=(($b1)|0);
      var $124$0=HEAP32[(($ld$41$0)>>2)];
      var $ld$42$1=(($b1+4)|0);
      var $124$1=HEAP32[(($ld$42$1)>>2)];
      var $ld$43$0=(($b2)|0);
      var $125$0=HEAP32[(($ld$43$0)>>2)];
      var $ld$44$1=(($b2+4)|0);
      var $125$1=HEAP32[(($ld$44$1)>>2)];
      var $126=_minimax_player(1, $124$0, $124$1, $125$0, $125$1, $col);
      label = 38; break;
    case 38: 
      label = 39; break;
    case 39: 
      label = 40; break;
    case 40: 
      var $130=HEAP32[(($col)>>2)];
      var $131=_bit_place_piece($130, 2, $b1, $b2);
      var $132=HEAP32[(($col)>>2)];
      STACKTOP = __stackBase__;
      return $132;
    default: assert(0, "bad label: " + label);
  }
}
function _minimax_comp_ab($depth, $b1$0, $b1$1, $b2$0, $b2$1, $col, $alpha, $beta) {
  var label = 0;
  var __stackBase__  = STACKTOP; STACKTOP = (STACKTOP + 24)|0; assert(!(STACKTOP&3)); assert((STACKTOP|0) < (STACK_MAX|0));
  label = 2; 
  while(1) switch(label) {
    case 2: 
      var $1;
      var $2;
      var $3=__stackBase__;
      var $4=(__stackBase__)+(8);
      var $5;
      var $6;
      var $7;
      var $i;
      var $max;
      var $max_col;
      var $tmp;
      var $tmp_b=(__stackBase__)+(16);
      $2=$depth;
      var $st$0$0=(($3)|0);
      HEAP32[(($st$0$0)>>2)]=$b1$0;
      var $st$1$1=(($3+4)|0);
      HEAP32[(($st$1$1)>>2)]=$b1$1;
      var $st$2$0=(($4)|0);
      HEAP32[(($st$2$0)>>2)]=$b2$0;
      var $st$3$1=(($4+4)|0);
      HEAP32[(($st$3$1)>>2)]=$b2$1;
      $5=$col;
      $6=$alpha;
      $7=$beta;
      var $8=$6;
      $max=$8;
      $max_col=0;
      var $9=$2;
      var $10=HEAP32[((5243256)>>2)];
      var $11=(($9)|(0)) >= (($10)|(0));
      if ($11) { label = 3; break; } else { label = 4; break; }
    case 3: 
      var $ld$4$0=(($3)|0);
      var $13$0=HEAP32[(($ld$4$0)>>2)];
      var $ld$5$1=(($3+4)|0);
      var $13$1=HEAP32[(($ld$5$1)>>2)];
      var $ld$6$0=(($4)|0);
      var $14$0=HEAP32[(($ld$6$0)>>2)];
      var $ld$7$1=(($4+4)|0);
      var $14$1=HEAP32[(($ld$7$1)>>2)];
      var $15=_value($13$0, $13$1, $14$0, $14$1);
      $1=$15;
      label = 15; break;
    case 4: 
      $i=0;
      label = 5; break;
    case 5: 
      var $18=$i;
      var $19=(($18)|(0)) < 7;
      if ($19) { label = 6; break; } else { var $25 = 0;label = 7; break; }
    case 6: 
      var $21=$max;
      var $22=$7;
      var $23=(($21)|(0)) < (($22)|(0));
      var $25 = $23;label = 7; break;
    case 7: 
      var $25;
      if ($25) { label = 8; break; } else { label = 14; break; }
    case 8: 
      var $ld$8$0=(($4)|0);
      var $27$0=HEAP32[(($ld$8$0)>>2)];
      var $ld$9$1=(($4+4)|0);
      var $27$1=HEAP32[(($ld$9$1)>>2)];
      var $st$10$0=(($tmp_b)|0);
      HEAP32[(($st$10$0)>>2)]=$27$0;
      var $st$11$1=(($tmp_b+4)|0);
      HEAP32[(($st$11$1)>>2)]=$27$1;
      var $28=$i;
      var $29=_bit_place_piece($28, 2, $3, $tmp_b);
      var $30=(($29)|(0))!=0;
      if ($30) { label = 9; break; } else { label = 10; break; }
    case 9: 
      label = 13; break;
    case 10: 
      var $33=$2;
      var $34=((($33)+(1))|0);
      var $ld$12$0=(($3)|0);
      var $35$0=HEAP32[(($ld$12$0)>>2)];
      var $ld$13$1=(($3+4)|0);
      var $35$1=HEAP32[(($ld$13$1)>>2)];
      var $ld$14$0=(($tmp_b)|0);
      var $36$0=HEAP32[(($ld$14$0)>>2)];
      var $ld$15$1=(($tmp_b+4)|0);
      var $36$1=HEAP32[(($ld$15$1)>>2)];
      var $37=$5;
      var $38=$max;
      var $39=$7;
      var $40=_minimax_player_ab($34, $35$0, $35$1, $36$0, $36$1, $37, $38, $39);
      $tmp=$40;
      var $41=$tmp;
      var $42=$max;
      var $43=(($41)|(0)) > (($42)|(0));
      if ($43) { label = 11; break; } else { label = 12; break; }
    case 11: 
      var $45=$tmp;
      $max=$45;
      var $46=$i;
      $max_col=$46;
      label = 12; break;
    case 12: 
      label = 13; break;
    case 13: 
      var $49=$i;
      var $50=((($49)+(1))|0);
      $i=$50;
      label = 5; break;
    case 14: 
      var $52=$max_col;
      var $53=$5;
      HEAP32[(($53)>>2)]=$52;
      var $54=$max;
      $1=$54;
      label = 15; break;
    case 15: 
      var $56=$1;
      STACKTOP = __stackBase__;
      return $56;
    default: assert(0, "bad label: " + label);
  }
}
function _minimax_player_ab($depth, $b1$0, $b1$1, $b2$0, $b2$1, $col, $alpha, $beta) {
  var label = 0;
  var __stackBase__  = STACKTOP; STACKTOP = (STACKTOP + 24)|0; assert(!(STACKTOP&3)); assert((STACKTOP|0) < (STACK_MAX|0));
  label = 2; 
  while(1) switch(label) {
    case 2: 
      var $1;
      var $2;
      var $3=__stackBase__;
      var $4=(__stackBase__)+(8);
      var $5;
      var $6;
      var $7;
      var $i;
      var $min;
      var $min_col;
      var $tmp;
      var $tmp_b=(__stackBase__)+(16);
      $2=$depth;
      var $st$0$0=(($3)|0);
      HEAP32[(($st$0$0)>>2)]=$b1$0;
      var $st$1$1=(($3+4)|0);
      HEAP32[(($st$1$1)>>2)]=$b1$1;
      var $st$2$0=(($4)|0);
      HEAP32[(($st$2$0)>>2)]=$b2$0;
      var $st$3$1=(($4+4)|0);
      HEAP32[(($st$3$1)>>2)]=$b2$1;
      $5=$col;
      $6=$alpha;
      $7=$beta;
      var $8=$7;
      $min=$8;
      $min_col=0;
      var $9=$2;
      var $10=HEAP32[((5243256)>>2)];
      var $11=(($9)|(0)) >= (($10)|(0));
      if ($11) { label = 3; break; } else { label = 4; break; }
    case 3: 
      var $ld$4$0=(($3)|0);
      var $13$0=HEAP32[(($ld$4$0)>>2)];
      var $ld$5$1=(($3+4)|0);
      var $13$1=HEAP32[(($ld$5$1)>>2)];
      var $ld$6$0=(($4)|0);
      var $14$0=HEAP32[(($ld$6$0)>>2)];
      var $ld$7$1=(($4+4)|0);
      var $14$1=HEAP32[(($ld$7$1)>>2)];
      var $15=_value($13$0, $13$1, $14$0, $14$1);
      $1=$15;
      label = 15; break;
    case 4: 
      $i=0;
      label = 5; break;
    case 5: 
      var $18=$i;
      var $19=(($18)|(0)) < 7;
      if ($19) { label = 6; break; } else { var $25 = 0;label = 7; break; }
    case 6: 
      var $21=$min;
      var $22=$6;
      var $23=(($21)|(0)) > (($22)|(0));
      var $25 = $23;label = 7; break;
    case 7: 
      var $25;
      if ($25) { label = 8; break; } else { label = 14; break; }
    case 8: 
      var $ld$8$0=(($3)|0);
      var $27$0=HEAP32[(($ld$8$0)>>2)];
      var $ld$9$1=(($3+4)|0);
      var $27$1=HEAP32[(($ld$9$1)>>2)];
      var $st$10$0=(($tmp_b)|0);
      HEAP32[(($st$10$0)>>2)]=$27$0;
      var $st$11$1=(($tmp_b+4)|0);
      HEAP32[(($st$11$1)>>2)]=$27$1;
      var $28=$i;
      var $29=_bit_place_piece($28, 1, $tmp_b, $4);
      var $30=(($29)|(0))!=0;
      if ($30) { label = 9; break; } else { label = 10; break; }
    case 9: 
      label = 13; break;
    case 10: 
      var $33=$2;
      var $34=((($33)+(1))|0);
      var $ld$12$0=(($tmp_b)|0);
      var $35$0=HEAP32[(($ld$12$0)>>2)];
      var $ld$13$1=(($tmp_b+4)|0);
      var $35$1=HEAP32[(($ld$13$1)>>2)];
      var $ld$14$0=(($4)|0);
      var $36$0=HEAP32[(($ld$14$0)>>2)];
      var $ld$15$1=(($4+4)|0);
      var $36$1=HEAP32[(($ld$15$1)>>2)];
      var $37=$5;
      var $38=$6;
      var $39=$min;
      var $40=_minimax_comp_ab($34, $35$0, $35$1, $36$0, $36$1, $37, $38, $39);
      $tmp=$40;
      var $41=$tmp;
      var $42=$min;
      var $43=(($41)|(0)) <= (($42)|(0));
      if ($43) { label = 11; break; } else { label = 12; break; }
    case 11: 
      var $45=$tmp;
      $min=$45;
      var $46=$i;
      $min_col=$46;
      label = 12; break;
    case 12: 
      label = 13; break;
    case 13: 
      var $49=$i;
      var $50=((($49)+(1))|0);
      $i=$50;
      label = 5; break;
    case 14: 
      var $52=$min_col;
      var $53=$5;
      HEAP32[(($53)>>2)]=$52;
      var $54=$min;
      $1=$54;
      label = 15; break;
    case 15: 
      var $56=$1;
      STACKTOP = __stackBase__;
      return $56;
    default: assert(0, "bad label: " + label);
  }
}
function _minimax_comp_ab2($depth, $b1$0, $b1$1, $b2$0, $b2$1, $col, $beta) {
  var label = 0;
  var __stackBase__  = STACKTOP; STACKTOP = (STACKTOP + 24)|0; assert(!(STACKTOP&3)); assert((STACKTOP|0) < (STACK_MAX|0));
  label = 2; 
  while(1) switch(label) {
    case 2: 
      var $1;
      var $2;
      var $3=__stackBase__;
      var $4=(__stackBase__)+(8);
      var $5;
      var $6;
      var $i;
      var $max;
      var $max_col;
      var $tmp;
      var $tmp_b=(__stackBase__)+(16);
      $2=$depth;
      var $st$0$0=(($3)|0);
      HEAP32[(($st$0$0)>>2)]=$b1$0;
      var $st$1$1=(($3+4)|0);
      HEAP32[(($st$1$1)>>2)]=$b1$1;
      var $st$2$0=(($4)|0);
      HEAP32[(($st$2$0)>>2)]=$b2$0;
      var $st$3$1=(($4+4)|0);
      HEAP32[(($st$3$1)>>2)]=$b2$1;
      $5=$col;
      $6=$beta;
      $max=-100000;
      $max_col=0;
      var $7=$2;
      var $8=HEAP32[((5243256)>>2)];
      var $9=(($7)|(0)) >= (($8)|(0));
      if ($9) { label = 3; break; } else { label = 4; break; }
    case 3: 
      var $ld$4$0=(($3)|0);
      var $11$0=HEAP32[(($ld$4$0)>>2)];
      var $ld$5$1=(($3+4)|0);
      var $11$1=HEAP32[(($ld$5$1)>>2)];
      var $ld$6$0=(($4)|0);
      var $12$0=HEAP32[(($ld$6$0)>>2)];
      var $ld$7$1=(($4+4)|0);
      var $12$1=HEAP32[(($ld$7$1)>>2)];
      var $13=_value($11$0, $11$1, $12$0, $12$1);
      $1=$13;
      label = 15; break;
    case 4: 
      $i=0;
      label = 5; break;
    case 5: 
      var $16=$i;
      var $17=(($16)|(0)) < 7;
      if ($17) { label = 6; break; } else { label = 14; break; }
    case 6: 
      var $ld$8$0=(($4)|0);
      var $19$0=HEAP32[(($ld$8$0)>>2)];
      var $ld$9$1=(($4+4)|0);
      var $19$1=HEAP32[(($ld$9$1)>>2)];
      var $st$10$0=(($tmp_b)|0);
      HEAP32[(($st$10$0)>>2)]=$19$0;
      var $st$11$1=(($tmp_b+4)|0);
      HEAP32[(($st$11$1)>>2)]=$19$1;
      var $20=$i;
      var $21=_bit_place_piece($20, 2, $3, $tmp_b);
      var $22=(($21)|(0))!=0;
      if ($22) { label = 7; break; } else { label = 8; break; }
    case 7: 
      label = 13; break;
    case 8: 
      var $25=$2;
      var $26=((($25)+(1))|0);
      var $ld$12$0=(($3)|0);
      var $27$0=HEAP32[(($ld$12$0)>>2)];
      var $ld$13$1=(($3+4)|0);
      var $27$1=HEAP32[(($ld$13$1)>>2)];
      var $ld$14$0=(($tmp_b)|0);
      var $28$0=HEAP32[(($ld$14$0)>>2)];
      var $ld$15$1=(($tmp_b+4)|0);
      var $28$1=HEAP32[(($ld$15$1)>>2)];
      var $29=$5;
      var $30=$max;
      var $31=_minimax_player_ab2($26, $27$0, $27$1, $28$0, $28$1, $29, $30);
      $tmp=$31;
      var $32=$tmp;
      var $33=$max;
      var $34=(($32)|(0)) > (($33)|(0));
      if ($34) { label = 9; break; } else { label = 10; break; }
    case 9: 
      var $36=$tmp;
      $max=$36;
      var $37=$i;
      $max_col=$37;
      label = 10; break;
    case 10: 
      var $39=$max;
      var $40=$6;
      var $41=(($39)|(0)) > (($40)|(0));
      if ($41) { label = 11; break; } else { label = 12; break; }
    case 11: 
      var $43=$max;
      $1=$43;
      label = 15; break;
    case 12: 
      label = 13; break;
    case 13: 
      var $46=$i;
      var $47=((($46)+(1))|0);
      $i=$47;
      label = 5; break;
    case 14: 
      var $49=$max_col;
      var $50=$5;
      HEAP32[(($50)>>2)]=$49;
      var $51=$max;
      $1=$51;
      label = 15; break;
    case 15: 
      var $53=$1;
      STACKTOP = __stackBase__;
      return $53;
    default: assert(0, "bad label: " + label);
  }
}
function _minimax_player_ab2($depth, $b1$0, $b1$1, $b2$0, $b2$1, $col, $alpha) {
  var label = 0;
  var __stackBase__  = STACKTOP; STACKTOP = (STACKTOP + 24)|0; assert(!(STACKTOP&3)); assert((STACKTOP|0) < (STACK_MAX|0));
  label = 2; 
  while(1) switch(label) {
    case 2: 
      var $1;
      var $2;
      var $3=__stackBase__;
      var $4=(__stackBase__)+(8);
      var $5;
      var $6;
      var $i;
      var $min;
      var $min_col;
      var $tmp;
      var $tmp_b=(__stackBase__)+(16);
      $2=$depth;
      var $st$0$0=(($3)|0);
      HEAP32[(($st$0$0)>>2)]=$b1$0;
      var $st$1$1=(($3+4)|0);
      HEAP32[(($st$1$1)>>2)]=$b1$1;
      var $st$2$0=(($4)|0);
      HEAP32[(($st$2$0)>>2)]=$b2$0;
      var $st$3$1=(($4+4)|0);
      HEAP32[(($st$3$1)>>2)]=$b2$1;
      $5=$col;
      $6=$alpha;
      $min=100000;
      $min_col=0;
      var $7=$2;
      var $8=HEAP32[((5243256)>>2)];
      var $9=(($7)|(0)) >= (($8)|(0));
      if ($9) { label = 3; break; } else { label = 4; break; }
    case 3: 
      var $ld$4$0=(($3)|0);
      var $11$0=HEAP32[(($ld$4$0)>>2)];
      var $ld$5$1=(($3+4)|0);
      var $11$1=HEAP32[(($ld$5$1)>>2)];
      var $ld$6$0=(($4)|0);
      var $12$0=HEAP32[(($ld$6$0)>>2)];
      var $ld$7$1=(($4+4)|0);
      var $12$1=HEAP32[(($ld$7$1)>>2)];
      var $13=_value($11$0, $11$1, $12$0, $12$1);
      $1=$13;
      label = 15; break;
    case 4: 
      $i=0;
      label = 5; break;
    case 5: 
      var $16=$i;
      var $17=(($16)|(0)) < 7;
      if ($17) { label = 6; break; } else { label = 14; break; }
    case 6: 
      var $ld$8$0=(($3)|0);
      var $19$0=HEAP32[(($ld$8$0)>>2)];
      var $ld$9$1=(($3+4)|0);
      var $19$1=HEAP32[(($ld$9$1)>>2)];
      var $st$10$0=(($tmp_b)|0);
      HEAP32[(($st$10$0)>>2)]=$19$0;
      var $st$11$1=(($tmp_b+4)|0);
      HEAP32[(($st$11$1)>>2)]=$19$1;
      var $20=$i;
      var $21=_bit_place_piece($20, 1, $tmp_b, $4);
      var $22=(($21)|(0))!=0;
      if ($22) { label = 7; break; } else { label = 8; break; }
    case 7: 
      label = 13; break;
    case 8: 
      var $25=$2;
      var $26=((($25)+(1))|0);
      var $ld$12$0=(($tmp_b)|0);
      var $27$0=HEAP32[(($ld$12$0)>>2)];
      var $ld$13$1=(($tmp_b+4)|0);
      var $27$1=HEAP32[(($ld$13$1)>>2)];
      var $ld$14$0=(($4)|0);
      var $28$0=HEAP32[(($ld$14$0)>>2)];
      var $ld$15$1=(($4+4)|0);
      var $28$1=HEAP32[(($ld$15$1)>>2)];
      var $29=$5;
      var $30=$min;
      var $31=_minimax_comp_ab2($26, $27$0, $27$1, $28$0, $28$1, $29, $30);
      $tmp=$31;
      var $32=$tmp;
      var $33=$min;
      var $34=(($32)|(0)) <= (($33)|(0));
      if ($34) { label = 9; break; } else { label = 10; break; }
    case 9: 
      var $36=$tmp;
      $min=$36;
      var $37=$i;
      $min_col=$37;
      label = 10; break;
    case 10: 
      var $39=$min;
      var $40=$6;
      var $41=(($39)|(0)) < (($40)|(0));
      if ($41) { label = 11; break; } else { label = 12; break; }
    case 11: 
      var $43=$min;
      $1=$43;
      label = 15; break;
    case 12: 
      label = 13; break;
    case 13: 
      var $46=$i;
      var $47=((($46)+(1))|0);
      $i=$47;
      label = 5; break;
    case 14: 
      var $49=$min_col;
      var $50=$5;
      HEAP32[(($50)>>2)]=$49;
      var $51=$min;
      $1=$51;
      label = 15; break;
    case 15: 
      var $53=$1;
      STACKTOP = __stackBase__;
      return $53;
    default: assert(0, "bad label: " + label);
  }
}
function _minimax_comp($depth, $b1$0, $b1$1, $b2$0, $b2$1, $col) {
  var label = 0;
  var __stackBase__  = STACKTOP; STACKTOP = (STACKTOP + 24)|0; assert(!(STACKTOP&3)); assert((STACKTOP|0) < (STACK_MAX|0));
  label = 2; 
  while(1) switch(label) {
    case 2: 
      var $1;
      var $2;
      var $3=__stackBase__;
      var $4=(__stackBase__)+(8);
      var $5;
      var $i;
      var $max;
      var $max_col;
      var $tmp;
      var $tmp_b=(__stackBase__)+(16);
      $2=$depth;
      var $st$0$0=(($3)|0);
      HEAP32[(($st$0$0)>>2)]=$b1$0;
      var $st$1$1=(($3+4)|0);
      HEAP32[(($st$1$1)>>2)]=$b1$1;
      var $st$2$0=(($4)|0);
      HEAP32[(($st$2$0)>>2)]=$b2$0;
      var $st$3$1=(($4+4)|0);
      HEAP32[(($st$3$1)>>2)]=$b2$1;
      $5=$col;
      $max=-100000;
      $max_col=0;
      var $6=$2;
      var $7=HEAP32[((5243256)>>2)];
      var $8=(($6)|(0)) >= (($7)|(0));
      if ($8) { label = 3; break; } else { label = 4; break; }
    case 3: 
      var $ld$4$0=(($3)|0);
      var $10$0=HEAP32[(($ld$4$0)>>2)];
      var $ld$5$1=(($3+4)|0);
      var $10$1=HEAP32[(($ld$5$1)>>2)];
      var $ld$6$0=(($4)|0);
      var $11$0=HEAP32[(($ld$6$0)>>2)];
      var $ld$7$1=(($4+4)|0);
      var $11$1=HEAP32[(($ld$7$1)>>2)];
      var $12=_value($10$0, $10$1, $11$0, $11$1);
      $1=$12;
      label = 13; break;
    case 4: 
      $i=0;
      label = 5; break;
    case 5: 
      var $15=$i;
      var $16=(($15)|(0)) < 7;
      if ($16) { label = 6; break; } else { label = 12; break; }
    case 6: 
      var $ld$8$0=(($4)|0);
      var $18$0=HEAP32[(($ld$8$0)>>2)];
      var $ld$9$1=(($4+4)|0);
      var $18$1=HEAP32[(($ld$9$1)>>2)];
      var $st$10$0=(($tmp_b)|0);
      HEAP32[(($st$10$0)>>2)]=$18$0;
      var $st$11$1=(($tmp_b+4)|0);
      HEAP32[(($st$11$1)>>2)]=$18$1;
      var $19=$i;
      var $20=_bit_place_piece($19, 2, $3, $tmp_b);
      var $21=(($20)|(0))!=0;
      if ($21) { label = 7; break; } else { label = 8; break; }
    case 7: 
      label = 11; break;
    case 8: 
      var $24=$2;
      var $25=((($24)+(1))|0);
      var $ld$12$0=(($3)|0);
      var $26$0=HEAP32[(($ld$12$0)>>2)];
      var $ld$13$1=(($3+4)|0);
      var $26$1=HEAP32[(($ld$13$1)>>2)];
      var $ld$14$0=(($tmp_b)|0);
      var $27$0=HEAP32[(($ld$14$0)>>2)];
      var $ld$15$1=(($tmp_b+4)|0);
      var $27$1=HEAP32[(($ld$15$1)>>2)];
      var $28=$5;
      var $29=_minimax_player($25, $26$0, $26$1, $27$0, $27$1, $28);
      $tmp=$29;
      var $30=$tmp;
      var $31=$max;
      var $32=(($30)|(0)) > (($31)|(0));
      if ($32) { label = 9; break; } else { label = 10; break; }
    case 9: 
      var $34=$tmp;
      $max=$34;
      var $35=$i;
      $max_col=$35;
      label = 10; break;
    case 10: 
      label = 11; break;
    case 11: 
      var $38=$i;
      var $39=((($38)+(1))|0);
      $i=$39;
      label = 5; break;
    case 12: 
      var $41=$max_col;
      var $42=$5;
      HEAP32[(($42)>>2)]=$41;
      var $43=$max;
      $1=$43;
      label = 13; break;
    case 13: 
      var $45=$1;
      STACKTOP = __stackBase__;
      return $45;
    default: assert(0, "bad label: " + label);
  }
}
function _minimax_player($depth, $b1$0, $b1$1, $b2$0, $b2$1, $col) {
  var label = 0;
  var __stackBase__  = STACKTOP; STACKTOP = (STACKTOP + 24)|0; assert(!(STACKTOP&3)); assert((STACKTOP|0) < (STACK_MAX|0));
  label = 2; 
  while(1) switch(label) {
    case 2: 
      var $1;
      var $2;
      var $3=__stackBase__;
      var $4=(__stackBase__)+(8);
      var $5;
      var $i;
      var $min;
      var $min_col;
      var $tmp;
      var $tmp_b=(__stackBase__)+(16);
      $2=$depth;
      var $st$0$0=(($3)|0);
      HEAP32[(($st$0$0)>>2)]=$b1$0;
      var $st$1$1=(($3+4)|0);
      HEAP32[(($st$1$1)>>2)]=$b1$1;
      var $st$2$0=(($4)|0);
      HEAP32[(($st$2$0)>>2)]=$b2$0;
      var $st$3$1=(($4+4)|0);
      HEAP32[(($st$3$1)>>2)]=$b2$1;
      $5=$col;
      $min=100000;
      $min_col=0;
      var $6=$2;
      var $7=HEAP32[((5243256)>>2)];
      var $8=(($6)|(0)) >= (($7)|(0));
      if ($8) { label = 3; break; } else { label = 4; break; }
    case 3: 
      var $ld$4$0=(($3)|0);
      var $10$0=HEAP32[(($ld$4$0)>>2)];
      var $ld$5$1=(($3+4)|0);
      var $10$1=HEAP32[(($ld$5$1)>>2)];
      var $ld$6$0=(($4)|0);
      var $11$0=HEAP32[(($ld$6$0)>>2)];
      var $ld$7$1=(($4+4)|0);
      var $11$1=HEAP32[(($ld$7$1)>>2)];
      var $12=_value($10$0, $10$1, $11$0, $11$1);
      $1=$12;
      label = 13; break;
    case 4: 
      $i=0;
      label = 5; break;
    case 5: 
      var $15=$i;
      var $16=(($15)|(0)) < 7;
      if ($16) { label = 6; break; } else { label = 12; break; }
    case 6: 
      var $ld$8$0=(($3)|0);
      var $18$0=HEAP32[(($ld$8$0)>>2)];
      var $ld$9$1=(($3+4)|0);
      var $18$1=HEAP32[(($ld$9$1)>>2)];
      var $st$10$0=(($tmp_b)|0);
      HEAP32[(($st$10$0)>>2)]=$18$0;
      var $st$11$1=(($tmp_b+4)|0);
      HEAP32[(($st$11$1)>>2)]=$18$1;
      var $19=$i;
      var $20=_bit_place_piece($19, 1, $tmp_b, $4);
      var $21=(($20)|(0))!=0;
      if ($21) { label = 7; break; } else { label = 8; break; }
    case 7: 
      label = 11; break;
    case 8: 
      var $24=$2;
      var $25=((($24)+(1))|0);
      var $ld$12$0=(($tmp_b)|0);
      var $26$0=HEAP32[(($ld$12$0)>>2)];
      var $ld$13$1=(($tmp_b+4)|0);
      var $26$1=HEAP32[(($ld$13$1)>>2)];
      var $ld$14$0=(($4)|0);
      var $27$0=HEAP32[(($ld$14$0)>>2)];
      var $ld$15$1=(($4+4)|0);
      var $27$1=HEAP32[(($ld$15$1)>>2)];
      var $28=$5;
      var $29=_minimax_comp($25, $26$0, $26$1, $27$0, $27$1, $28);
      $tmp=$29;
      var $30=$tmp;
      var $31=$min;
      var $32=(($30)|(0)) <= (($31)|(0));
      if ($32) { label = 9; break; } else { label = 10; break; }
    case 9: 
      var $34=$tmp;
      $min=$34;
      var $35=$i;
      $min_col=$35;
      label = 10; break;
    case 10: 
      label = 11; break;
    case 11: 
      var $38=$i;
      var $39=((($38)+(1))|0);
      $i=$39;
      label = 5; break;
    case 12: 
      var $41=$min_col;
      var $42=$5;
      HEAP32[(($42)>>2)]=$41;
      var $43=$min;
      $1=$43;
      label = 13; break;
    case 13: 
      var $45=$1;
      STACKTOP = __stackBase__;
      return $45;
    default: assert(0, "bad label: " + label);
  }
}
function _main($c, $v) {
  var label = 0;
  var __stackBase__  = STACKTOP; STACKTOP = (STACKTOP + 60)|0; assert(!(STACKTOP&3)); assert((STACKTOP|0) < (STACK_MAX|0));
  label = 2; 
  while(1) switch(label) {
    case 2: 
      var $1;
      var $2;
      var $3;
      var $b=__stackBase__;
      var $ab=(__stackBase__)+(52);
      var $cvsc;
      var $in=(__stackBase__)+(56);
      var $in_fp;
      $1=0;
      $2=$c;
      $3=$v;
      HEAP32[(($ab)>>2)]=0;
      $cvsc=1;
      var $4=HEAP32[((_stderr)>>2)];
      var $5=_fprintf($4, ((5242920)|0), (tempInt=STACKTOP,STACKTOP = (STACKTOP + 4)|0,assert((STACKTOP|0) < (STACK_MAX|0)),HEAP32[((tempInt)>>2)]=((5242912)|0),tempInt));
      var $6=HEAP32[((_stderr)>>2)];
      var $7=_fprintf($6, ((5242888)|0), (tempInt=STACKTOP,STACKTOP = (STACKTOP + 4)|0,assert((STACKTOP|0) < (STACK_MAX|0)),HEAP32[((tempInt)>>2)]=((5242884)|0),tempInt));
      var $8=$3;
      var $9=(($8+4)|0);
      var $10=HEAP32[(($9)>>2)];
      var $11=_fopen($10, ((5243244)|0));
      $in_fp=$11;
      var $12=$in_fp;
      var $13=(($12)|(0))!=0;
      if ($13) { label = 6; break; } else { label = 3; break; }
    case 3: 
      var $15=_fopen(((5243236)|0), ((5243244)|0));
      $in_fp=$15;
      var $16=$in_fp;
      var $17=(($16)|(0))!=0;
      if ($17) { label = 5; break; } else { label = 4; break; }
    case 4: 
      var $19=_printf(((5243200)|0), (tempInt=STACKTOP,STACKTOP = (STACKTOP + 1)|0,STACKTOP = ((((STACKTOP)+3)>>2)<<2),assert((STACKTOP|0) < (STACK_MAX|0)),HEAP32[((tempInt)>>2)]=0,tempInt));
      _exit(1);
      throw "Reached an unreachable!"
    case 5: 
      label = 6; break;
    case 6: 
      var $22=$in_fp;
      var $23=_fscanf($22, ((5243196)|0), (tempInt=STACKTOP,STACKTOP = (STACKTOP + 4)|0,assert((STACKTOP|0) < (STACK_MAX|0)),HEAP32[((tempInt)>>2)]=5243256,tempInt));
      var $24=$in_fp;
      var $25=_fscanf($24, ((5243196)|0), (tempInt=STACKTOP,STACKTOP = (STACKTOP + 4)|0,assert((STACKTOP|0) < (STACK_MAX|0)),HEAP32[((tempInt)>>2)]=$ab,tempInt));
      var $26=$in_fp;
      var $27=_fclose($26);
      var $28=HEAP32[((5243256)>>2)];
      var $29=_printf(((5243172)|0), (tempInt=STACKTOP,STACKTOP = (STACKTOP + 4)|0,assert((STACKTOP|0) < (STACK_MAX|0)),HEAP32[((tempInt)>>2)]=$28,tempInt));
      var $30=HEAP32[(($ab)>>2)];
      var $31=(($30)|(0))!=0;
      var $32=$31 ? (((5243144)|0)) : (((5243140)|0));
      var $33=_printf(((5243148)|0), (tempInt=STACKTOP,STACKTOP = (STACKTOP + 4)|0,assert((STACKTOP|0) < (STACK_MAX|0)),HEAP32[((tempInt)>>2)]=$32,tempInt));
      var $34=HEAP32[(($ab)>>2)];
      var $35=(($34)|(0))==1;
      if ($35) { label = 7; break; } else { label = 8; break; }
    case 7: 
      var $37=_printf(((5243116)|0), (tempInt=STACKTOP,STACKTOP = (STACKTOP + 1)|0,STACKTOP = ((((STACKTOP)+3)>>2)<<2),assert((STACKTOP|0) < (STACK_MAX|0)),HEAP32[((tempInt)>>2)]=0,tempInt));
      label = 8; break;
    case 8: 
      var $39=HEAP32[(($ab)>>2)];
      var $40=(($39)|(0))==2;
      if ($40) { label = 9; break; } else { label = 10; break; }
    case 9: 
      var $42=_printf(((5243092)|0), (tempInt=STACKTOP,STACKTOP = (STACKTOP + 1)|0,STACKTOP = ((((STACKTOP)+3)>>2)<<2),assert((STACKTOP|0) < (STACK_MAX|0)),HEAP32[((tempInt)>>2)]=0,tempInt));
      label = 10; break;
    case 10: 
      _init_patterns();
      var $44=(($b)|0);
      _init_board($44);
      var $45=(($b)|0);
      _print_board($45);
      label = 11; break;
    case 11: 
      var $47=(($b)|0);
      var $48=_find_winner_p($47);
      var $49=(($48)|(0))!=0;
      if ($49) { var $55 = 1;label = 13; break; } else { label = 12; break; }
    case 12: 
      var $51=(($b)|0);
      var $52=_find_winner_c($51);
      var $53=(($52)|(0))!=0;
      var $55 = $53;label = 13; break;
    case 13: 
      var $55;
      var $56=$55 ^ 1;
      if ($56) { label = 14; break; } else { label = 20; break; }
    case 14: 
      var $58=$cvsc;
      var $59=(($58)|(0))!=0;
      if ($59) { label = 15; break; } else { label = 16; break; }
    case 15: 
      var $61=(($b)|0);
      var $62=HEAP32[(($ab)>>2)];
      var $63=_think($61, 1, $62);
      var $64=(($b)|0);
      var $65=_place_piece($63, 1, $64);
      label = 19; break;
    case 16: 
      var $67=_scanf(((5243196)|0), (tempInt=STACKTOP,STACKTOP = (STACKTOP + 4)|0,assert((STACKTOP|0) < (STACK_MAX|0)),HEAP32[((tempInt)>>2)]=$in,tempInt));
      var $68=HEAP32[(($in)>>2)];
      var $69=(($b)|0);
      var $70=_place_piece($68, 1, $69);
      var $71=(($70)|(0))!=0;
      if ($71) { label = 17; break; } else { label = 18; break; }
    case 17: 
      label = 11; break;
    case 18: 
      label = 19; break;
    case 19: 
      var $75=(($b)|0);
      var $76=HEAP32[(($ab)>>2)];
      var $77=_think($75, 2, $76);
      var $78=(($b)|0);
      var $79=_place_piece($77, 2, $78);
      var $80=(($b)|0);
      _print_board($80);
      label = 11; break;
    case 20: 
      var $82=(($b)|0);
      var $83=_find_winner_p($82);
      var $84=(($83)|(0))==1;
      if ($84) { label = 21; break; } else { label = 23; break; }
    case 21: 
      var $86=(($b)|0);
      var $87=_find_winner_c($86);
      var $88=(($87)|(0))!=0;
      if ($88) { label = 23; break; } else { label = 22; break; }
    case 22: 
      var $90=_printf(((5243060)|0), (tempInt=STACKTOP,STACKTOP = (STACKTOP + 1)|0,STACKTOP = ((((STACKTOP)+3)>>2)<<2),assert((STACKTOP|0) < (STACK_MAX|0)),HEAP32[((tempInt)>>2)]=0,tempInt));
      label = 23; break;
    case 23: 
      var $92=(($b)|0);
      var $93=_find_winner_c($92);
      var $94=(($93)|(0))==1;
      if ($94) { label = 24; break; } else { label = 26; break; }
    case 24: 
      var $96=(($b)|0);
      var $97=_find_winner_p($96);
      var $98=(($97)|(0))!=0;
      if ($98) { label = 26; break; } else { label = 25; break; }
    case 25: 
      var $100=_printf(((5243028)|0), (tempInt=STACKTOP,STACKTOP = (STACKTOP + 1)|0,STACKTOP = ((((STACKTOP)+3)>>2)<<2),assert((STACKTOP|0) < (STACK_MAX|0)),HEAP32[((tempInt)>>2)]=0,tempInt));
      label = 26; break;
    case 26: 
      var $102=(($b)|0);
      var $103=_find_winner_p($102);
      var $104=(($103)|(0))==2;
      if ($104) { label = 29; break; } else { label = 27; break; }
    case 27: 
      var $106=(($b)|0);
      var $107=_find_winner_c($106);
      var $108=(($107)|(0))==1;
      if ($108) { label = 28; break; } else { label = 30; break; }
    case 28: 
      var $110=(($b)|0);
      var $111=_find_winner_p($110);
      var $112=(($111)|(0))!=0;
      if ($112) { label = 29; break; } else { label = 30; break; }
    case 29: 
      var $114=_printf(((5243012)|0), (tempInt=STACKTOP,STACKTOP = (STACKTOP + 1)|0,STACKTOP = ((((STACKTOP)+3)>>2)<<2),assert((STACKTOP|0) < (STACK_MAX|0)),HEAP32[((tempInt)>>2)]=0,tempInt));
      label = 30; break;
    case 30: 
      STACKTOP = __stackBase__;
      return 0;
    default: assert(0, "bad label: " + label);
  }
}
Module["_main"] = _main;
// EMSCRIPTEN_END_FUNCS
function _i64Add(a, b, c, d) {
    /*
      x = a + b*2^32
      y = c + d*2^32
      result = l + h*2^32
    */
    a = a|0; b = b|0; c = c|0; d = d|0;
    var l = 0, h = 0;
    l = (a + c)>>>0;
    h = (b + d)>>>0;
    if ((l>>>0) < (a>>>0)) { // iff we overflowed
      h = (h+1)>>>0;
    }
    return tempRet0 = h,l|0;
  }
function _bitshift64Shl(low, high, bits) {
    low = low|0; high = high|0; bits = bits|0;
    var ander = 0;
    if ((bits|0) < 32) {
      ander = ((1 << bits) - 1)|0;
      tempRet0 = (high << bits) | ((low&(ander << (32 - bits))) >>> (32 - bits));
      return low << bits;
    }
    tempRet0 = low << (bits - 32);
    return 0;
  }
function _bitshift64Lshr(low, high, bits) {
    low = low|0; high = high|0; bits = bits|0;
    var ander = 0;
    if ((bits|0) < 32) {
      ander = ((1 << bits) - 1)|0;
      tempRet0 = high >>> bits;
      return (low >>> bits) | ((high&ander) << (32 - bits));
    }
    tempRet0 = 0;
    return (high >>> (bits - 32))|0;
  }
function _bitshift64Ashr(low, high, bits) {
    low = low|0; high = high|0; bits = bits|0;
    var ander = 0;
    if ((bits|0) < 32) {
      ander = ((1 << bits) - 1)|0;
      tempRet0 = high >> bits;
      return (low >>> bits) | ((high&ander) << (32 - bits));
    }
    tempRet0 = (high|0) < 0 ? -1 : 0;
    return (high >> (bits - 32))|0;
  }
// EMSCRIPTEN_END_FUNCS
// TODO: strip out parts of this we do not need
//======= begin closure i64 code =======
// Copyright 2009 The Closure Library Authors. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS-IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
/**
 * @fileoverview Defines a Long class for representing a 64-bit two's-complement
 * integer value, which faithfully simulates the behavior of a Java "long". This
 * implementation is derived from LongLib in GWT.
 *
 */
var i64Math = (function() { // Emscripten wrapper
  var goog = { math: {} };
  /**
   * Constructs a 64-bit two's-complement integer, given its low and high 32-bit
   * values as *signed* integers.  See the from* functions below for more
   * convenient ways of constructing Longs.
   *
   * The internal representation of a long is the two given signed, 32-bit values.
   * We use 32-bit pieces because these are the size of integers on which
   * Javascript performs bit-operations.  For operations like addition and
   * multiplication, we split each number into 16-bit pieces, which can easily be
   * multiplied within Javascript's floating-point representation without overflow
   * or change in sign.
   *
   * In the algorithms below, we frequently reduce the negative case to the
   * positive case by negating the input(s) and then post-processing the result.
   * Note that we must ALWAYS check specially whether those values are MIN_VALUE
   * (-2^63) because -MIN_VALUE == MIN_VALUE (since 2^63 cannot be represented as
   * a positive number, it overflows back into a negative).  Not handling this
   * case would often result in infinite recursion.
   *
   * @param {number} low  The low (signed) 32 bits of the long.
   * @param {number} high  The high (signed) 32 bits of the long.
   * @constructor
   */
  goog.math.Long = function(low, high) {
    /**
     * @type {number}
     * @private
     */
    this.low_ = low | 0;  // force into 32 signed bits.
    /**
     * @type {number}
     * @private
     */
    this.high_ = high | 0;  // force into 32 signed bits.
  };
  // NOTE: Common constant values ZERO, ONE, NEG_ONE, etc. are defined below the
  // from* methods on which they depend.
  /**
   * A cache of the Long representations of small integer values.
   * @type {!Object}
   * @private
   */
  goog.math.Long.IntCache_ = {};
  /**
   * Returns a Long representing the given (32-bit) integer value.
   * @param {number} value The 32-bit integer in question.
   * @return {!goog.math.Long} The corresponding Long value.
   */
  goog.math.Long.fromInt = function(value) {
    if (-128 <= value && value < 128) {
      var cachedObj = goog.math.Long.IntCache_[value];
      if (cachedObj) {
        return cachedObj;
      }
    }
    var obj = new goog.math.Long(value | 0, value < 0 ? -1 : 0);
    if (-128 <= value && value < 128) {
      goog.math.Long.IntCache_[value] = obj;
    }
    return obj;
  };
  /**
   * Returns a Long representing the given value, provided that it is a finite
   * number.  Otherwise, zero is returned.
   * @param {number} value The number in question.
   * @return {!goog.math.Long} The corresponding Long value.
   */
  goog.math.Long.fromNumber = function(value) {
    if (isNaN(value) || !isFinite(value)) {
      return goog.math.Long.ZERO;
    } else if (value <= -goog.math.Long.TWO_PWR_63_DBL_) {
      return goog.math.Long.MIN_VALUE;
    } else if (value + 1 >= goog.math.Long.TWO_PWR_63_DBL_) {
      return goog.math.Long.MAX_VALUE;
    } else if (value < 0) {
      return goog.math.Long.fromNumber(-value).negate();
    } else {
      return new goog.math.Long(
          (value % goog.math.Long.TWO_PWR_32_DBL_) | 0,
          (value / goog.math.Long.TWO_PWR_32_DBL_) | 0);
    }
  };
  /**
   * Returns a Long representing the 64-bit integer that comes by concatenating
   * the given high and low bits.  Each is assumed to use 32 bits.
   * @param {number} lowBits The low 32-bits.
   * @param {number} highBits The high 32-bits.
   * @return {!goog.math.Long} The corresponding Long value.
   */
  goog.math.Long.fromBits = function(lowBits, highBits) {
    return new goog.math.Long(lowBits, highBits);
  };
  /**
   * Returns a Long representation of the given string, written using the given
   * radix.
   * @param {string} str The textual representation of the Long.
   * @param {number=} opt_radix The radix in which the text is written.
   * @return {!goog.math.Long} The corresponding Long value.
   */
  goog.math.Long.fromString = function(str, opt_radix) {
    if (str.length == 0) {
      throw Error('number format error: empty string');
    }
    var radix = opt_radix || 10;
    if (radix < 2 || 36 < radix) {
      throw Error('radix out of range: ' + radix);
    }
    if (str.charAt(0) == '-') {
      return goog.math.Long.fromString(str.substring(1), radix).negate();
    } else if (str.indexOf('-') >= 0) {
      throw Error('number format error: interior "-" character: ' + str);
    }
    // Do several (8) digits each time through the loop, so as to
    // minimize the calls to the very expensive emulated div.
    var radixToPower = goog.math.Long.fromNumber(Math.pow(radix, 8));
    var result = goog.math.Long.ZERO;
    for (var i = 0; i < str.length; i += 8) {
      var size = Math.min(8, str.length - i);
      var value = parseInt(str.substring(i, i + size), radix);
      if (size < 8) {
        var power = goog.math.Long.fromNumber(Math.pow(radix, size));
        result = result.multiply(power).add(goog.math.Long.fromNumber(value));
      } else {
        result = result.multiply(radixToPower);
        result = result.add(goog.math.Long.fromNumber(value));
      }
    }
    return result;
  };
  // NOTE: the compiler should inline these constant values below and then remove
  // these variables, so there should be no runtime penalty for these.
  /**
   * Number used repeated below in calculations.  This must appear before the
   * first call to any from* function below.
   * @type {number}
   * @private
   */
  goog.math.Long.TWO_PWR_16_DBL_ = 1 << 16;
  /**
   * @type {number}
   * @private
   */
  goog.math.Long.TWO_PWR_24_DBL_ = 1 << 24;
  /**
   * @type {number}
   * @private
   */
  goog.math.Long.TWO_PWR_32_DBL_ =
      goog.math.Long.TWO_PWR_16_DBL_ * goog.math.Long.TWO_PWR_16_DBL_;
  /**
   * @type {number}
   * @private
   */
  goog.math.Long.TWO_PWR_31_DBL_ =
      goog.math.Long.TWO_PWR_32_DBL_ / 2;
  /**
   * @type {number}
   * @private
   */
  goog.math.Long.TWO_PWR_48_DBL_ =
      goog.math.Long.TWO_PWR_32_DBL_ * goog.math.Long.TWO_PWR_16_DBL_;
  /**
   * @type {number}
   * @private
   */
  goog.math.Long.TWO_PWR_64_DBL_ =
      goog.math.Long.TWO_PWR_32_DBL_ * goog.math.Long.TWO_PWR_32_DBL_;
  /**
   * @type {number}
   * @private
   */
  goog.math.Long.TWO_PWR_63_DBL_ =
      goog.math.Long.TWO_PWR_64_DBL_ / 2;
  /** @type {!goog.math.Long} */
  goog.math.Long.ZERO = goog.math.Long.fromInt(0);
  /** @type {!goog.math.Long} */
  goog.math.Long.ONE = goog.math.Long.fromInt(1);
  /** @type {!goog.math.Long} */
  goog.math.Long.NEG_ONE = goog.math.Long.fromInt(-1);
  /** @type {!goog.math.Long} */
  goog.math.Long.MAX_VALUE =
      goog.math.Long.fromBits(0xFFFFFFFF | 0, 0x7FFFFFFF | 0);
  /** @type {!goog.math.Long} */
  goog.math.Long.MIN_VALUE = goog.math.Long.fromBits(0, 0x80000000 | 0);
  /**
   * @type {!goog.math.Long}
   * @private
   */
  goog.math.Long.TWO_PWR_24_ = goog.math.Long.fromInt(1 << 24);
  /** @return {number} The value, assuming it is a 32-bit integer. */
  goog.math.Long.prototype.toInt = function() {
    return this.low_;
  };
  /** @return {number} The closest floating-point representation to this value. */
  goog.math.Long.prototype.toNumber = function() {
    return this.high_ * goog.math.Long.TWO_PWR_32_DBL_ +
           this.getLowBitsUnsigned();
  };
  /**
   * @param {number=} opt_radix The radix in which the text should be written.
   * @return {string} The textual representation of this value.
   */
  goog.math.Long.prototype.toString = function(opt_radix) {
    var radix = opt_radix || 10;
    if (radix < 2 || 36 < radix) {
      throw Error('radix out of range: ' + radix);
    }
    if (this.isZero()) {
      return '0';
    }
    if (this.isNegative()) {
      if (this.equals(goog.math.Long.MIN_VALUE)) {
        // We need to change the Long value before it can be negated, so we remove
        // the bottom-most digit in this base and then recurse to do the rest.
        var radixLong = goog.math.Long.fromNumber(radix);
        var div = this.div(radixLong);
        var rem = div.multiply(radixLong).subtract(this);
        return div.toString(radix) + rem.toInt().toString(radix);
      } else {
        return '-' + this.negate().toString(radix);
      }
    }
    // Do several (6) digits each time through the loop, so as to
    // minimize the calls to the very expensive emulated div.
    var radixToPower = goog.math.Long.fromNumber(Math.pow(radix, 6));
    var rem = this;
    var result = '';
    while (true) {
      var remDiv = rem.div(radixToPower);
      var intval = rem.subtract(remDiv.multiply(radixToPower)).toInt();
      var digits = intval.toString(radix);
      rem = remDiv;
      if (rem.isZero()) {
        return digits + result;
      } else {
        while (digits.length < 6) {
          digits = '0' + digits;
        }
        result = '' + digits + result;
      }
    }
  };
  /** @return {number} The high 32-bits as a signed value. */
  goog.math.Long.prototype.getHighBits = function() {
    return this.high_;
  };
  /** @return {number} The low 32-bits as a signed value. */
  goog.math.Long.prototype.getLowBits = function() {
    return this.low_;
  };
  /** @return {number} The low 32-bits as an unsigned value. */
  goog.math.Long.prototype.getLowBitsUnsigned = function() {
    return (this.low_ >= 0) ?
        this.low_ : goog.math.Long.TWO_PWR_32_DBL_ + this.low_;
  };
  /**
   * @return {number} Returns the number of bits needed to represent the absolute
   *     value of this Long.
   */
  goog.math.Long.prototype.getNumBitsAbs = function() {
    if (this.isNegative()) {
      if (this.equals(goog.math.Long.MIN_VALUE)) {
        return 64;
      } else {
        return this.negate().getNumBitsAbs();
      }
    } else {
      var val = this.high_ != 0 ? this.high_ : this.low_;
      for (var bit = 31; bit > 0; bit--) {
        if ((val & (1 << bit)) != 0) {
          break;
        }
      }
      return this.high_ != 0 ? bit + 33 : bit + 1;
    }
  };
  /** @return {boolean} Whether this value is zero. */
  goog.math.Long.prototype.isZero = function() {
    return this.high_ == 0 && this.low_ == 0;
  };
  /** @return {boolean} Whether this value is negative. */
  goog.math.Long.prototype.isNegative = function() {
    return this.high_ < 0;
  };
  /** @return {boolean} Whether this value is odd. */
  goog.math.Long.prototype.isOdd = function() {
    return (this.low_ & 1) == 1;
  };
  /**
   * @param {goog.math.Long} other Long to compare against.
   * @return {boolean} Whether this Long equals the other.
   */
  goog.math.Long.prototype.equals = function(other) {
    return (this.high_ == other.high_) && (this.low_ == other.low_);
  };
  /**
   * @param {goog.math.Long} other Long to compare against.
   * @return {boolean} Whether this Long does not equal the other.
   */
  goog.math.Long.prototype.notEquals = function(other) {
    return (this.high_ != other.high_) || (this.low_ != other.low_);
  };
  /**
   * @param {goog.math.Long} other Long to compare against.
   * @return {boolean} Whether this Long is less than the other.
   */
  goog.math.Long.prototype.lessThan = function(other) {
    return this.compare(other) < 0;
  };
  /**
   * @param {goog.math.Long} other Long to compare against.
   * @return {boolean} Whether this Long is less than or equal to the other.
   */
  goog.math.Long.prototype.lessThanOrEqual = function(other) {
    return this.compare(other) <= 0;
  };
  /**
   * @param {goog.math.Long} other Long to compare against.
   * @return {boolean} Whether this Long is greater than the other.
   */
  goog.math.Long.prototype.greaterThan = function(other) {
    return this.compare(other) > 0;
  };
  /**
   * @param {goog.math.Long} other Long to compare against.
   * @return {boolean} Whether this Long is greater than or equal to the other.
   */
  goog.math.Long.prototype.greaterThanOrEqual = function(other) {
    return this.compare(other) >= 0;
  };
  /**
   * Compares this Long with the given one.
   * @param {goog.math.Long} other Long to compare against.
   * @return {number} 0 if they are the same, 1 if the this is greater, and -1
   *     if the given one is greater.
   */
  goog.math.Long.prototype.compare = function(other) {
    if (this.equals(other)) {
      return 0;
    }
    var thisNeg = this.isNegative();
    var otherNeg = other.isNegative();
    if (thisNeg && !otherNeg) {
      return -1;
    }
    if (!thisNeg && otherNeg) {
      return 1;
    }
    // at this point, the signs are the same, so subtraction will not overflow
    if (this.subtract(other).isNegative()) {
      return -1;
    } else {
      return 1;
    }
  };
  /** @return {!goog.math.Long} The negation of this value. */
  goog.math.Long.prototype.negate = function() {
    if (this.equals(goog.math.Long.MIN_VALUE)) {
      return goog.math.Long.MIN_VALUE;
    } else {
      return this.not().add(goog.math.Long.ONE);
    }
  };
  /**
   * Returns the sum of this and the given Long.
   * @param {goog.math.Long} other Long to add to this one.
   * @return {!goog.math.Long} The sum of this and the given Long.
   */
  goog.math.Long.prototype.add = function(other) {
    // Divide each number into 4 chunks of 16 bits, and then sum the chunks.
    var a48 = this.high_ >>> 16;
    var a32 = this.high_ & 0xFFFF;
    var a16 = this.low_ >>> 16;
    var a00 = this.low_ & 0xFFFF;
    var b48 = other.high_ >>> 16;
    var b32 = other.high_ & 0xFFFF;
    var b16 = other.low_ >>> 16;
    var b00 = other.low_ & 0xFFFF;
    var c48 = 0, c32 = 0, c16 = 0, c00 = 0;
    c00 += a00 + b00;
    c16 += c00 >>> 16;
    c00 &= 0xFFFF;
    c16 += a16 + b16;
    c32 += c16 >>> 16;
    c16 &= 0xFFFF;
    c32 += a32 + b32;
    c48 += c32 >>> 16;
    c32 &= 0xFFFF;
    c48 += a48 + b48;
    c48 &= 0xFFFF;
    return goog.math.Long.fromBits((c16 << 16) | c00, (c48 << 16) | c32);
  };
  /**
   * Returns the difference of this and the given Long.
   * @param {goog.math.Long} other Long to subtract from this.
   * @return {!goog.math.Long} The difference of this and the given Long.
   */
  goog.math.Long.prototype.subtract = function(other) {
    return this.add(other.negate());
  };
  /**
   * Returns the product of this and the given long.
   * @param {goog.math.Long} other Long to multiply with this.
   * @return {!goog.math.Long} The product of this and the other.
   */
  goog.math.Long.prototype.multiply = function(other) {
    if (this.isZero()) {
      return goog.math.Long.ZERO;
    } else if (other.isZero()) {
      return goog.math.Long.ZERO;
    }
    if (this.equals(goog.math.Long.MIN_VALUE)) {
      return other.isOdd() ? goog.math.Long.MIN_VALUE : goog.math.Long.ZERO;
    } else if (other.equals(goog.math.Long.MIN_VALUE)) {
      return this.isOdd() ? goog.math.Long.MIN_VALUE : goog.math.Long.ZERO;
    }
    if (this.isNegative()) {
      if (other.isNegative()) {
        return this.negate().multiply(other.negate());
      } else {
        return this.negate().multiply(other).negate();
      }
    } else if (other.isNegative()) {
      return this.multiply(other.negate()).negate();
    }
    // If both longs are small, use float multiplication
    if (this.lessThan(goog.math.Long.TWO_PWR_24_) &&
        other.lessThan(goog.math.Long.TWO_PWR_24_)) {
      return goog.math.Long.fromNumber(this.toNumber() * other.toNumber());
    }
    // Divide each long into 4 chunks of 16 bits, and then add up 4x4 products.
    // We can skip products that would overflow.
    var a48 = this.high_ >>> 16;
    var a32 = this.high_ & 0xFFFF;
    var a16 = this.low_ >>> 16;
    var a00 = this.low_ & 0xFFFF;
    var b48 = other.high_ >>> 16;
    var b32 = other.high_ & 0xFFFF;
    var b16 = other.low_ >>> 16;
    var b00 = other.low_ & 0xFFFF;
    var c48 = 0, c32 = 0, c16 = 0, c00 = 0;
    c00 += a00 * b00;
    c16 += c00 >>> 16;
    c00 &= 0xFFFF;
    c16 += a16 * b00;
    c32 += c16 >>> 16;
    c16 &= 0xFFFF;
    c16 += a00 * b16;
    c32 += c16 >>> 16;
    c16 &= 0xFFFF;
    c32 += a32 * b00;
    c48 += c32 >>> 16;
    c32 &= 0xFFFF;
    c32 += a16 * b16;
    c48 += c32 >>> 16;
    c32 &= 0xFFFF;
    c32 += a00 * b32;
    c48 += c32 >>> 16;
    c32 &= 0xFFFF;
    c48 += a48 * b00 + a32 * b16 + a16 * b32 + a00 * b48;
    c48 &= 0xFFFF;
    return goog.math.Long.fromBits((c16 << 16) | c00, (c48 << 16) | c32);
  };
  /**
   * Returns this Long divided by the given one.
   * @param {goog.math.Long} other Long by which to divide.
   * @return {!goog.math.Long} This Long divided by the given one.
   */
  goog.math.Long.prototype.div = function(other) {
    if (other.isZero()) {
      throw Error('division by zero');
    } else if (this.isZero()) {
      return goog.math.Long.ZERO;
    }
    if (this.equals(goog.math.Long.MIN_VALUE)) {
      if (other.equals(goog.math.Long.ONE) ||
          other.equals(goog.math.Long.NEG_ONE)) {
        return goog.math.Long.MIN_VALUE;  // recall that -MIN_VALUE == MIN_VALUE
      } else if (other.equals(goog.math.Long.MIN_VALUE)) {
        return goog.math.Long.ONE;
      } else {
        // At this point, we have |other| >= 2, so |this/other| < |MIN_VALUE|.
        var halfThis = this.shiftRight(1);
        var approx = halfThis.div(other).shiftLeft(1);
        if (approx.equals(goog.math.Long.ZERO)) {
          return other.isNegative() ? goog.math.Long.ONE : goog.math.Long.NEG_ONE;
        } else {
          var rem = this.subtract(other.multiply(approx));
          var result = approx.add(rem.div(other));
          return result;
        }
      }
    } else if (other.equals(goog.math.Long.MIN_VALUE)) {
      return goog.math.Long.ZERO;
    }
    if (this.isNegative()) {
      if (other.isNegative()) {
        return this.negate().div(other.negate());
      } else {
        return this.negate().div(other).negate();
      }
    } else if (other.isNegative()) {
      return this.div(other.negate()).negate();
    }
    // Repeat the following until the remainder is less than other:  find a
    // floating-point that approximates remainder / other *from below*, add this
    // into the result, and subtract it from the remainder.  It is critical that
    // the approximate value is less than or equal to the real value so that the
    // remainder never becomes negative.
    var res = goog.math.Long.ZERO;
    var rem = this;
    while (rem.greaterThanOrEqual(other)) {
      // Approximate the result of division. This may be a little greater or
      // smaller than the actual value.
      var approx = Math.max(1, Math.floor(rem.toNumber() / other.toNumber()));
      // We will tweak the approximate result by changing it in the 48-th digit or
      // the smallest non-fractional digit, whichever is larger.
      var log2 = Math.ceil(Math.log(approx) / Math.LN2);
      var delta = (log2 <= 48) ? 1 : Math.pow(2, log2 - 48);
      // Decrease the approximation until it is smaller than the remainder.  Note
      // that if it is too large, the product overflows and is negative.
      var approxRes = goog.math.Long.fromNumber(approx);
      var approxRem = approxRes.multiply(other);
      while (approxRem.isNegative() || approxRem.greaterThan(rem)) {
        approx -= delta;
        approxRes = goog.math.Long.fromNumber(approx);
        approxRem = approxRes.multiply(other);
      }
      // We know the answer can't be zero... and actually, zero would cause
      // infinite recursion since we would make no progress.
      if (approxRes.isZero()) {
        approxRes = goog.math.Long.ONE;
      }
      res = res.add(approxRes);
      rem = rem.subtract(approxRem);
    }
    return res;
  };
  /**
   * Returns this Long modulo the given one.
   * @param {goog.math.Long} other Long by which to mod.
   * @return {!goog.math.Long} This Long modulo the given one.
   */
  goog.math.Long.prototype.modulo = function(other) {
    return this.subtract(this.div(other).multiply(other));
  };
  /** @return {!goog.math.Long} The bitwise-NOT of this value. */
  goog.math.Long.prototype.not = function() {
    return goog.math.Long.fromBits(~this.low_, ~this.high_);
  };
  /**
   * Returns the bitwise-AND of this Long and the given one.
   * @param {goog.math.Long} other The Long with which to AND.
   * @return {!goog.math.Long} The bitwise-AND of this and the other.
   */
  goog.math.Long.prototype.and = function(other) {
    return goog.math.Long.fromBits(this.low_ & other.low_,
                                   this.high_ & other.high_);
  };
  /**
   * Returns the bitwise-OR of this Long and the given one.
   * @param {goog.math.Long} other The Long with which to OR.
   * @return {!goog.math.Long} The bitwise-OR of this and the other.
   */
  goog.math.Long.prototype.or = function(other) {
    return goog.math.Long.fromBits(this.low_ | other.low_,
                                   this.high_ | other.high_);
  };
  /**
   * Returns the bitwise-XOR of this Long and the given one.
   * @param {goog.math.Long} other The Long with which to XOR.
   * @return {!goog.math.Long} The bitwise-XOR of this and the other.
   */
  goog.math.Long.prototype.xor = function(other) {
    return goog.math.Long.fromBits(this.low_ ^ other.low_,
                                   this.high_ ^ other.high_);
  };
  /**
   * Returns this Long with bits shifted to the left by the given amount.
   * @param {number} numBits The number of bits by which to shift.
   * @return {!goog.math.Long} This shifted to the left by the given amount.
   */
  goog.math.Long.prototype.shiftLeft = function(numBits) {
    numBits &= 63;
    if (numBits == 0) {
      return this;
    } else {
      var low = this.low_;
      if (numBits < 32) {
        var high = this.high_;
        return goog.math.Long.fromBits(
            low << numBits,
            (high << numBits) | (low >>> (32 - numBits)));
      } else {
        return goog.math.Long.fromBits(0, low << (numBits - 32));
      }
    }
  };
  /**
   * Returns this Long with bits shifted to the right by the given amount.
   * @param {number} numBits The number of bits by which to shift.
   * @return {!goog.math.Long} This shifted to the right by the given amount.
   */
  goog.math.Long.prototype.shiftRight = function(numBits) {
    numBits &= 63;
    if (numBits == 0) {
      return this;
    } else {
      var high = this.high_;
      if (numBits < 32) {
        var low = this.low_;
        return goog.math.Long.fromBits(
            (low >>> numBits) | (high << (32 - numBits)),
            high >> numBits);
      } else {
        return goog.math.Long.fromBits(
            high >> (numBits - 32),
            high >= 0 ? 0 : -1);
      }
    }
  };
  /**
   * Returns this Long with bits shifted to the right by the given amount, with
   * the new top bits matching the current sign bit.
   * @param {number} numBits The number of bits by which to shift.
   * @return {!goog.math.Long} This shifted to the right by the given amount, with
   *     zeros placed into the new leading bits.
   */
  goog.math.Long.prototype.shiftRightUnsigned = function(numBits) {
    numBits &= 63;
    if (numBits == 0) {
      return this;
    } else {
      var high = this.high_;
      if (numBits < 32) {
        var low = this.low_;
        return goog.math.Long.fromBits(
            (low >>> numBits) | (high << (32 - numBits)),
            high >>> numBits);
      } else if (numBits == 32) {
        return goog.math.Long.fromBits(high, 0);
      } else {
        return goog.math.Long.fromBits(high >>> (numBits - 32), 0);
      }
    }
  };
  //======= begin jsbn =======
  var navigator = { appName: 'Modern Browser' }; // polyfill a little
  // Copyright (c) 2005  Tom Wu
  // All Rights Reserved.
  // http://www-cs-students.stanford.edu/~tjw/jsbn/
  /*
   * Copyright (c) 2003-2005  Tom Wu
   * All Rights Reserved.
   *
   * Permission is hereby granted, free of charge, to any person obtaining
   * a copy of this software and associated documentation files (the
   * "Software"), to deal in the Software without restriction, including
   * without limitation the rights to use, copy, modify, merge, publish,
   * distribute, sublicense, and/or sell copies of the Software, and to
   * permit persons to whom the Software is furnished to do so, subject to
   * the following conditions:
   *
   * The above copyright notice and this permission notice shall be
   * included in all copies or substantial portions of the Software.
   *
   * THE SOFTWARE IS PROVIDED "AS-IS" AND WITHOUT WARRANTY OF ANY KIND, 
   * EXPRESS, IMPLIED OR OTHERWISE, INCLUDING WITHOUT LIMITATION, ANY 
   * WARRANTY OF MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE.  
   *
   * IN NO EVENT SHALL TOM WU BE LIABLE FOR ANY SPECIAL, INCIDENTAL,
   * INDIRECT OR CONSEQUENTIAL DAMAGES OF ANY KIND, OR ANY DAMAGES WHATSOEVER
   * RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER OR NOT ADVISED OF
   * THE POSSIBILITY OF DAMAGE, AND ON ANY THEORY OF LIABILITY, ARISING OUT
   * OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
   *
   * In addition, the following condition applies:
   *
   * All redistributions must retain an intact copy of this copyright notice
   * and disclaimer.
   */
  // Basic JavaScript BN library - subset useful for RSA encryption.
  // Bits per digit
  var dbits;
  // JavaScript engine analysis
  var canary = 0xdeadbeefcafe;
  var j_lm = ((canary&0xffffff)==0xefcafe);
  // (public) Constructor
  function BigInteger(a,b,c) {
    if(a != null)
      if("number" == typeof a) this.fromNumber(a,b,c);
      else if(b == null && "string" != typeof a) this.fromString(a,256);
      else this.fromString(a,b);
  }
  // return new, unset BigInteger
  function nbi() { return new BigInteger(null); }
  // am: Compute w_j += (x*this_i), propagate carries,
  // c is initial carry, returns final carry.
  // c < 3*dvalue, x < 2*dvalue, this_i < dvalue
  // We need to select the fastest one that works in this environment.
  // am1: use a single mult and divide to get the high bits,
  // max digit bits should be 26 because
  // max internal value = 2*dvalue^2-2*dvalue (< 2^53)
  function am1(i,x,w,j,c,n) {
    while(--n >= 0) {
      var v = x*this[i++]+w[j]+c;
      c = Math.floor(v/0x4000000);
      w[j++] = v&0x3ffffff;
    }
    return c;
  }
  // am2 avoids a big mult-and-extract completely.
  // Max digit bits should be <= 30 because we do bitwise ops
  // on values up to 2*hdvalue^2-hdvalue-1 (< 2^31)
  function am2(i,x,w,j,c,n) {
    var xl = x&0x7fff, xh = x>>15;
    while(--n >= 0) {
      var l = this[i]&0x7fff;
      var h = this[i++]>>15;
      var m = xh*l+h*xl;
      l = xl*l+((m&0x7fff)<<15)+w[j]+(c&0x3fffffff);
      c = (l>>>30)+(m>>>15)+xh*h+(c>>>30);
      w[j++] = l&0x3fffffff;
    }
    return c;
  }
  // Alternately, set max digit bits to 28 since some
  // browsers slow down when dealing with 32-bit numbers.
  function am3(i,x,w,j,c,n) {
    var xl = x&0x3fff, xh = x>>14;
    while(--n >= 0) {
      var l = this[i]&0x3fff;
      var h = this[i++]>>14;
      var m = xh*l+h*xl;
      l = xl*l+((m&0x3fff)<<14)+w[j]+c;
      c = (l>>28)+(m>>14)+xh*h;
      w[j++] = l&0xfffffff;
    }
    return c;
  }
  if(j_lm && (navigator.appName == "Microsoft Internet Explorer")) {
    BigInteger.prototype.am = am2;
    dbits = 30;
  }
  else if(j_lm && (navigator.appName != "Netscape")) {
    BigInteger.prototype.am = am1;
    dbits = 26;
  }
  else { // Mozilla/Netscape seems to prefer am3
    BigInteger.prototype.am = am3;
    dbits = 28;
  }
  BigInteger.prototype.DB = dbits;
  BigInteger.prototype.DM = ((1<<dbits)-1);
  BigInteger.prototype.DV = (1<<dbits);
  var BI_FP = 52;
  BigInteger.prototype.FV = Math.pow(2,BI_FP);
  BigInteger.prototype.F1 = BI_FP-dbits;
  BigInteger.prototype.F2 = 2*dbits-BI_FP;
  // Digit conversions
  var BI_RM = "0123456789abcdefghijklmnopqrstuvwxyz";
  var BI_RC = new Array();
  var rr,vv;
  rr = "0".charCodeAt(0);
  for(vv = 0; vv <= 9; ++vv) BI_RC[rr++] = vv;
  rr = "a".charCodeAt(0);
  for(vv = 10; vv < 36; ++vv) BI_RC[rr++] = vv;
  rr = "A".charCodeAt(0);
  for(vv = 10; vv < 36; ++vv) BI_RC[rr++] = vv;
  function int2char(n) { return BI_RM.charAt(n); }
  function intAt(s,i) {
    var c = BI_RC[s.charCodeAt(i)];
    return (c==null)?-1:c;
  }
  // (protected) copy this to r
  function bnpCopyTo(r) {
    for(var i = this.t-1; i >= 0; --i) r[i] = this[i];
    r.t = this.t;
    r.s = this.s;
  }
  // (protected) set from integer value x, -DV <= x < DV
  function bnpFromInt(x) {
    this.t = 1;
    this.s = (x<0)?-1:0;
    if(x > 0) this[0] = x;
    else if(x < -1) this[0] = x+DV;
    else this.t = 0;
  }
  // return bigint initialized to value
  function nbv(i) { var r = nbi(); r.fromInt(i); return r; }
  // (protected) set from string and radix
  function bnpFromString(s,b) {
    var k;
    if(b == 16) k = 4;
    else if(b == 8) k = 3;
    else if(b == 256) k = 8; // byte array
    else if(b == 2) k = 1;
    else if(b == 32) k = 5;
    else if(b == 4) k = 2;
    else { this.fromRadix(s,b); return; }
    this.t = 0;
    this.s = 0;
    var i = s.length, mi = false, sh = 0;
    while(--i >= 0) {
      var x = (k==8)?s[i]&0xff:intAt(s,i);
      if(x < 0) {
        if(s.charAt(i) == "-") mi = true;
        continue;
      }
      mi = false;
      if(sh == 0)
        this[this.t++] = x;
      else if(sh+k > this.DB) {
        this[this.t-1] |= (x&((1<<(this.DB-sh))-1))<<sh;
        this[this.t++] = (x>>(this.DB-sh));
      }
      else
        this[this.t-1] |= x<<sh;
      sh += k;
      if(sh >= this.DB) sh -= this.DB;
    }
    if(k == 8 && (s[0]&0x80) != 0) {
      this.s = -1;
      if(sh > 0) this[this.t-1] |= ((1<<(this.DB-sh))-1)<<sh;
    }
    this.clamp();
    if(mi) BigInteger.ZERO.subTo(this,this);
  }
  // (protected) clamp off excess high words
  function bnpClamp() {
    var c = this.s&this.DM;
    while(this.t > 0 && this[this.t-1] == c) --this.t;
  }
  // (public) return string representation in given radix
  function bnToString(b) {
    if(this.s < 0) return "-"+this.negate().toString(b);
    var k;
    if(b == 16) k = 4;
    else if(b == 8) k = 3;
    else if(b == 2) k = 1;
    else if(b == 32) k = 5;
    else if(b == 4) k = 2;
    else return this.toRadix(b);
    var km = (1<<k)-1, d, m = false, r = "", i = this.t;
    var p = this.DB-(i*this.DB)%k;
    if(i-- > 0) {
      if(p < this.DB && (d = this[i]>>p) > 0) { m = true; r = int2char(d); }
      while(i >= 0) {
        if(p < k) {
          d = (this[i]&((1<<p)-1))<<(k-p);
          d |= this[--i]>>(p+=this.DB-k);
        }
        else {
          d = (this[i]>>(p-=k))&km;
          if(p <= 0) { p += this.DB; --i; }
        }
        if(d > 0) m = true;
        if(m) r += int2char(d);
      }
    }
    return m?r:"0";
  }
  // (public) -this
  function bnNegate() { var r = nbi(); BigInteger.ZERO.subTo(this,r); return r; }
  // (public) |this|
  function bnAbs() { return (this.s<0)?this.negate():this; }
  // (public) return + if this > a, - if this < a, 0 if equal
  function bnCompareTo(a) {
    var r = this.s-a.s;
    if(r != 0) return r;
    var i = this.t;
    r = i-a.t;
    if(r != 0) return (this.s<0)?-r:r;
    while(--i >= 0) if((r=this[i]-a[i]) != 0) return r;
    return 0;
  }
  // returns bit length of the integer x
  function nbits(x) {
    var r = 1, t;
    if((t=x>>>16) != 0) { x = t; r += 16; }
    if((t=x>>8) != 0) { x = t; r += 8; }
    if((t=x>>4) != 0) { x = t; r += 4; }
    if((t=x>>2) != 0) { x = t; r += 2; }
    if((t=x>>1) != 0) { x = t; r += 1; }
    return r;
  }
  // (public) return the number of bits in "this"
  function bnBitLength() {
    if(this.t <= 0) return 0;
    return this.DB*(this.t-1)+nbits(this[this.t-1]^(this.s&this.DM));
  }
  // (protected) r = this << n*DB
  function bnpDLShiftTo(n,r) {
    var i;
    for(i = this.t-1; i >= 0; --i) r[i+n] = this[i];
    for(i = n-1; i >= 0; --i) r[i] = 0;
    r.t = this.t+n;
    r.s = this.s;
  }
  // (protected) r = this >> n*DB
  function bnpDRShiftTo(n,r) {
    for(var i = n; i < this.t; ++i) r[i-n] = this[i];
    r.t = Math.max(this.t-n,0);
    r.s = this.s;
  }
  // (protected) r = this << n
  function bnpLShiftTo(n,r) {
    var bs = n%this.DB;
    var cbs = this.DB-bs;
    var bm = (1<<cbs)-1;
    var ds = Math.floor(n/this.DB), c = (this.s<<bs)&this.DM, i;
    for(i = this.t-1; i >= 0; --i) {
      r[i+ds+1] = (this[i]>>cbs)|c;
      c = (this[i]&bm)<<bs;
    }
    for(i = ds-1; i >= 0; --i) r[i] = 0;
    r[ds] = c;
    r.t = this.t+ds+1;
    r.s = this.s;
    r.clamp();
  }
  // (protected) r = this >> n
  function bnpRShiftTo(n,r) {
    r.s = this.s;
    var ds = Math.floor(n/this.DB);
    if(ds >= this.t) { r.t = 0; return; }
    var bs = n%this.DB;
    var cbs = this.DB-bs;
    var bm = (1<<bs)-1;
    r[0] = this[ds]>>bs;
    for(var i = ds+1; i < this.t; ++i) {
      r[i-ds-1] |= (this[i]&bm)<<cbs;
      r[i-ds] = this[i]>>bs;
    }
    if(bs > 0) r[this.t-ds-1] |= (this.s&bm)<<cbs;
    r.t = this.t-ds;
    r.clamp();
  }
  // (protected) r = this - a
  function bnpSubTo(a,r) {
    var i = 0, c = 0, m = Math.min(a.t,this.t);
    while(i < m) {
      c += this[i]-a[i];
      r[i++] = c&this.DM;
      c >>= this.DB;
    }
    if(a.t < this.t) {
      c -= a.s;
      while(i < this.t) {
        c += this[i];
        r[i++] = c&this.DM;
        c >>= this.DB;
      }
      c += this.s;
    }
    else {
      c += this.s;
      while(i < a.t) {
        c -= a[i];
        r[i++] = c&this.DM;
        c >>= this.DB;
      }
      c -= a.s;
    }
    r.s = (c<0)?-1:0;
    if(c < -1) r[i++] = this.DV+c;
    else if(c > 0) r[i++] = c;
    r.t = i;
    r.clamp();
  }
  // (protected) r = this * a, r != this,a (HAC 14.12)
  // "this" should be the larger one if appropriate.
  function bnpMultiplyTo(a,r) {
    var x = this.abs(), y = a.abs();
    var i = x.t;
    r.t = i+y.t;
    while(--i >= 0) r[i] = 0;
    for(i = 0; i < y.t; ++i) r[i+x.t] = x.am(0,y[i],r,i,0,x.t);
    r.s = 0;
    r.clamp();
    if(this.s != a.s) BigInteger.ZERO.subTo(r,r);
  }
  // (protected) r = this^2, r != this (HAC 14.16)
  function bnpSquareTo(r) {
    var x = this.abs();
    var i = r.t = 2*x.t;
    while(--i >= 0) r[i] = 0;
    for(i = 0; i < x.t-1; ++i) {
      var c = x.am(i,x[i],r,2*i,0,1);
      if((r[i+x.t]+=x.am(i+1,2*x[i],r,2*i+1,c,x.t-i-1)) >= x.DV) {
        r[i+x.t] -= x.DV;
        r[i+x.t+1] = 1;
      }
    }
    if(r.t > 0) r[r.t-1] += x.am(i,x[i],r,2*i,0,1);
    r.s = 0;
    r.clamp();
  }
  // (protected) divide this by m, quotient and remainder to q, r (HAC 14.20)
  // r != q, this != m.  q or r may be null.
  function bnpDivRemTo(m,q,r) {
    var pm = m.abs();
    if(pm.t <= 0) return;
    var pt = this.abs();
    if(pt.t < pm.t) {
      if(q != null) q.fromInt(0);
      if(r != null) this.copyTo(r);
      return;
    }
    if(r == null) r = nbi();
    var y = nbi(), ts = this.s, ms = m.s;
    var nsh = this.DB-nbits(pm[pm.t-1]);	// normalize modulus
    if(nsh > 0) { pm.lShiftTo(nsh,y); pt.lShiftTo(nsh,r); }
    else { pm.copyTo(y); pt.copyTo(r); }
    var ys = y.t;
    var y0 = y[ys-1];
    if(y0 == 0) return;
    var yt = y0*(1<<this.F1)+((ys>1)?y[ys-2]>>this.F2:0);
    var d1 = this.FV/yt, d2 = (1<<this.F1)/yt, e = 1<<this.F2;
    var i = r.t, j = i-ys, t = (q==null)?nbi():q;
    y.dlShiftTo(j,t);
    if(r.compareTo(t) >= 0) {
      r[r.t++] = 1;
      r.subTo(t,r);
    }
    BigInteger.ONE.dlShiftTo(ys,t);
    t.subTo(y,y);	// "negative" y so we can replace sub with am later
    while(y.t < ys) y[y.t++] = 0;
    while(--j >= 0) {
      // Estimate quotient digit
      var qd = (r[--i]==y0)?this.DM:Math.floor(r[i]*d1+(r[i-1]+e)*d2);
      if((r[i]+=y.am(0,qd,r,j,0,ys)) < qd) {	// Try it out
        y.dlShiftTo(j,t);
        r.subTo(t,r);
        while(r[i] < --qd) r.subTo(t,r);
      }
    }
    if(q != null) {
      r.drShiftTo(ys,q);
      if(ts != ms) BigInteger.ZERO.subTo(q,q);
    }
    r.t = ys;
    r.clamp();
    if(nsh > 0) r.rShiftTo(nsh,r);	// Denormalize remainder
    if(ts < 0) BigInteger.ZERO.subTo(r,r);
  }
  // (public) this mod a
  function bnMod(a) {
    var r = nbi();
    this.abs().divRemTo(a,null,r);
    if(this.s < 0 && r.compareTo(BigInteger.ZERO) > 0) a.subTo(r,r);
    return r;
  }
  // Modular reduction using "classic" algorithm
  function Classic(m) { this.m = m; }
  function cConvert(x) {
    if(x.s < 0 || x.compareTo(this.m) >= 0) return x.mod(this.m);
    else return x;
  }
  function cRevert(x) { return x; }
  function cReduce(x) { x.divRemTo(this.m,null,x); }
  function cMulTo(x,y,r) { x.multiplyTo(y,r); this.reduce(r); }
  function cSqrTo(x,r) { x.squareTo(r); this.reduce(r); }
  Classic.prototype.convert = cConvert;
  Classic.prototype.revert = cRevert;
  Classic.prototype.reduce = cReduce;
  Classic.prototype.mulTo = cMulTo;
  Classic.prototype.sqrTo = cSqrTo;
  // (protected) return "-1/this % 2^DB"; useful for Mont. reduction
  // justification:
  //         xy == 1 (mod m)
  //         xy =  1+km
  //   xy(2-xy) = (1+km)(1-km)
  // x[y(2-xy)] = 1-k^2m^2
  // x[y(2-xy)] == 1 (mod m^2)
  // if y is 1/x mod m, then y(2-xy) is 1/x mod m^2
  // should reduce x and y(2-xy) by m^2 at each step to keep size bounded.
  // JS multiply "overflows" differently from C/C++, so care is needed here.
  function bnpInvDigit() {
    if(this.t < 1) return 0;
    var x = this[0];
    if((x&1) == 0) return 0;
    var y = x&3;		// y == 1/x mod 2^2
    y = (y*(2-(x&0xf)*y))&0xf;	// y == 1/x mod 2^4
    y = (y*(2-(x&0xff)*y))&0xff;	// y == 1/x mod 2^8
    y = (y*(2-(((x&0xffff)*y)&0xffff)))&0xffff;	// y == 1/x mod 2^16
    // last step - calculate inverse mod DV directly;
    // assumes 16 < DB <= 32 and assumes ability to handle 48-bit ints
    y = (y*(2-x*y%this.DV))%this.DV;		// y == 1/x mod 2^dbits
    // we really want the negative inverse, and -DV < y < DV
    return (y>0)?this.DV-y:-y;
  }
  // Montgomery reduction
  function Montgomery(m) {
    this.m = m;
    this.mp = m.invDigit();
    this.mpl = this.mp&0x7fff;
    this.mph = this.mp>>15;
    this.um = (1<<(m.DB-15))-1;
    this.mt2 = 2*m.t;
  }
  // xR mod m
  function montConvert(x) {
    var r = nbi();
    x.abs().dlShiftTo(this.m.t,r);
    r.divRemTo(this.m,null,r);
    if(x.s < 0 && r.compareTo(BigInteger.ZERO) > 0) this.m.subTo(r,r);
    return r;
  }
  // x/R mod m
  function montRevert(x) {
    var r = nbi();
    x.copyTo(r);
    this.reduce(r);
    return r;
  }
  // x = x/R mod m (HAC 14.32)
  function montReduce(x) {
    while(x.t <= this.mt2)	// pad x so am has enough room later
      x[x.t++] = 0;
    for(var i = 0; i < this.m.t; ++i) {
      // faster way of calculating u0 = x[i]*mp mod DV
      var j = x[i]&0x7fff;
      var u0 = (j*this.mpl+(((j*this.mph+(x[i]>>15)*this.mpl)&this.um)<<15))&x.DM;
      // use am to combine the multiply-shift-add into one call
      j = i+this.m.t;
      x[j] += this.m.am(0,u0,x,i,0,this.m.t);
      // propagate carry
      while(x[j] >= x.DV) { x[j] -= x.DV; x[++j]++; }
    }
    x.clamp();
    x.drShiftTo(this.m.t,x);
    if(x.compareTo(this.m) >= 0) x.subTo(this.m,x);
  }
  // r = "x^2/R mod m"; x != r
  function montSqrTo(x,r) { x.squareTo(r); this.reduce(r); }
  // r = "xy/R mod m"; x,y != r
  function montMulTo(x,y,r) { x.multiplyTo(y,r); this.reduce(r); }
  Montgomery.prototype.convert = montConvert;
  Montgomery.prototype.revert = montRevert;
  Montgomery.prototype.reduce = montReduce;
  Montgomery.prototype.mulTo = montMulTo;
  Montgomery.prototype.sqrTo = montSqrTo;
  // (protected) true iff this is even
  function bnpIsEven() { return ((this.t>0)?(this[0]&1):this.s) == 0; }
  // (protected) this^e, e < 2^32, doing sqr and mul with "r" (HAC 14.79)
  function bnpExp(e,z) {
    if(e > 0xffffffff || e < 1) return BigInteger.ONE;
    var r = nbi(), r2 = nbi(), g = z.convert(this), i = nbits(e)-1;
    g.copyTo(r);
    while(--i >= 0) {
      z.sqrTo(r,r2);
      if((e&(1<<i)) > 0) z.mulTo(r2,g,r);
      else { var t = r; r = r2; r2 = t; }
    }
    return z.revert(r);
  }
  // (public) this^e % m, 0 <= e < 2^32
  function bnModPowInt(e,m) {
    var z;
    if(e < 256 || m.isEven()) z = new Classic(m); else z = new Montgomery(m);
    return this.exp(e,z);
  }
  // protected
  BigInteger.prototype.copyTo = bnpCopyTo;
  BigInteger.prototype.fromInt = bnpFromInt;
  BigInteger.prototype.fromString = bnpFromString;
  BigInteger.prototype.clamp = bnpClamp;
  BigInteger.prototype.dlShiftTo = bnpDLShiftTo;
  BigInteger.prototype.drShiftTo = bnpDRShiftTo;
  BigInteger.prototype.lShiftTo = bnpLShiftTo;
  BigInteger.prototype.rShiftTo = bnpRShiftTo;
  BigInteger.prototype.subTo = bnpSubTo;
  BigInteger.prototype.multiplyTo = bnpMultiplyTo;
  BigInteger.prototype.squareTo = bnpSquareTo;
  BigInteger.prototype.divRemTo = bnpDivRemTo;
  BigInteger.prototype.invDigit = bnpInvDigit;
  BigInteger.prototype.isEven = bnpIsEven;
  BigInteger.prototype.exp = bnpExp;
  // public
  BigInteger.prototype.toString = bnToString;
  BigInteger.prototype.negate = bnNegate;
  BigInteger.prototype.abs = bnAbs;
  BigInteger.prototype.compareTo = bnCompareTo;
  BigInteger.prototype.bitLength = bnBitLength;
  BigInteger.prototype.mod = bnMod;
  BigInteger.prototype.modPowInt = bnModPowInt;
  // "constants"
  BigInteger.ZERO = nbv(0);
  BigInteger.ONE = nbv(1);
  // jsbn2 stuff
  // (protected) convert from radix string
  function bnpFromRadix(s,b) {
    this.fromInt(0);
    if(b == null) b = 10;
    var cs = this.chunkSize(b);
    var d = Math.pow(b,cs), mi = false, j = 0, w = 0;
    for(var i = 0; i < s.length; ++i) {
      var x = intAt(s,i);
      if(x < 0) {
        if(s.charAt(i) == "-" && this.signum() == 0) mi = true;
        continue;
      }
      w = b*w+x;
      if(++j >= cs) {
        this.dMultiply(d);
        this.dAddOffset(w,0);
        j = 0;
        w = 0;
      }
    }
    if(j > 0) {
      this.dMultiply(Math.pow(b,j));
      this.dAddOffset(w,0);
    }
    if(mi) BigInteger.ZERO.subTo(this,this);
  }
  // (protected) return x s.t. r^x < DV
  function bnpChunkSize(r) { return Math.floor(Math.LN2*this.DB/Math.log(r)); }
  // (public) 0 if this == 0, 1 if this > 0
  function bnSigNum() {
    if(this.s < 0) return -1;
    else if(this.t <= 0 || (this.t == 1 && this[0] <= 0)) return 0;
    else return 1;
  }
  // (protected) this *= n, this >= 0, 1 < n < DV
  function bnpDMultiply(n) {
    this[this.t] = this.am(0,n-1,this,0,0,this.t);
    ++this.t;
    this.clamp();
  }
  // (protected) this += n << w words, this >= 0
  function bnpDAddOffset(n,w) {
    if(n == 0) return;
    while(this.t <= w) this[this.t++] = 0;
    this[w] += n;
    while(this[w] >= this.DV) {
      this[w] -= this.DV;
      if(++w >= this.t) this[this.t++] = 0;
      ++this[w];
    }
  }
  // (protected) convert to radix string
  function bnpToRadix(b) {
    if(b == null) b = 10;
    if(this.signum() == 0 || b < 2 || b > 36) return "0";
    var cs = this.chunkSize(b);
    var a = Math.pow(b,cs);
    var d = nbv(a), y = nbi(), z = nbi(), r = "";
    this.divRemTo(d,y,z);
    while(y.signum() > 0) {
      r = (a+z.intValue()).toString(b).substr(1) + r;
      y.divRemTo(d,y,z);
    }
    return z.intValue().toString(b) + r;
  }
  // (public) return value as integer
  function bnIntValue() {
    if(this.s < 0) {
      if(this.t == 1) return this[0]-this.DV;
      else if(this.t == 0) return -1;
    }
    else if(this.t == 1) return this[0];
    else if(this.t == 0) return 0;
    // assumes 16 < DB < 32
    return ((this[1]&((1<<(32-this.DB))-1))<<this.DB)|this[0];
  }
  // (protected) r = this + a
  function bnpAddTo(a,r) {
    var i = 0, c = 0, m = Math.min(a.t,this.t);
    while(i < m) {
      c += this[i]+a[i];
      r[i++] = c&this.DM;
      c >>= this.DB;
    }
    if(a.t < this.t) {
      c += a.s;
      while(i < this.t) {
        c += this[i];
        r[i++] = c&this.DM;
        c >>= this.DB;
      }
      c += this.s;
    }
    else {
      c += this.s;
      while(i < a.t) {
        c += a[i];
        r[i++] = c&this.DM;
        c >>= this.DB;
      }
      c += a.s;
    }
    r.s = (c<0)?-1:0;
    if(c > 0) r[i++] = c;
    else if(c < -1) r[i++] = this.DV+c;
    r.t = i;
    r.clamp();
  }
  BigInteger.prototype.fromRadix = bnpFromRadix;
  BigInteger.prototype.chunkSize = bnpChunkSize;
  BigInteger.prototype.signum = bnSigNum;
  BigInteger.prototype.dMultiply = bnpDMultiply;
  BigInteger.prototype.dAddOffset = bnpDAddOffset;
  BigInteger.prototype.toRadix = bnpToRadix;
  BigInteger.prototype.intValue = bnIntValue;
  BigInteger.prototype.addTo = bnpAddTo;
  //======= end jsbn =======
  // Emscripten wrapper
  var Wrapper = {
    subtract: function(xl, xh, yl, yh) {
      var x = new goog.math.Long(xl, xh);
      var y = new goog.math.Long(yl, yh);
      var ret = x.subtract(y);
      HEAP32[tempDoublePtr>>2] = ret.low_;
      HEAP32[tempDoublePtr+4>>2] = ret.high_;
    },
    multiply: function(xl, xh, yl, yh) {
      var x = new goog.math.Long(xl, xh);
      var y = new goog.math.Long(yl, yh);
      var ret = x.multiply(y);
      HEAP32[tempDoublePtr>>2] = ret.low_;
      HEAP32[tempDoublePtr+4>>2] = ret.high_;
    },
    abs: function(l, h) {
      var x = new goog.math.Long(l, h);
      var ret;
      if (x.isNegative()) {
        ret = x.negate();
      } else {
        ret = x;
      }
      HEAP32[tempDoublePtr>>2] = ret.low_;
      HEAP32[tempDoublePtr+4>>2] = ret.high_;
    },
    ensureTemps: function() {
      if (Wrapper.ensuredTemps) return;
      Wrapper.ensuredTemps = true;
      Wrapper.two32 = new BigInteger();
      Wrapper.two32.fromString('4294967296', 10);
      Wrapper.two64 = new BigInteger();
      Wrapper.two64.fromString('18446744073709551616', 10);
      Wrapper.temp1 = new BigInteger();
      Wrapper.temp2 = new BigInteger();
    },
    lh2bignum: function(l, h) {
      var a = new BigInteger();
      a.fromString(h.toString(), 10);
      var b = new BigInteger();
      a.multiplyTo(Wrapper.two32, b);
      var c = new BigInteger();
      c.fromString(l.toString(), 10);
      var d = new BigInteger();
      c.addTo(b, d);
      return d;
    },
    divide: function(xl, xh, yl, yh, unsigned) {
      Wrapper.ensureTemps();
      if (!unsigned) {
        var x = new goog.math.Long(xl, xh);
        var y = new goog.math.Long(yl, yh);
        var ret = x.div(y);
        HEAP32[tempDoublePtr>>2] = ret.low_;
        HEAP32[tempDoublePtr+4>>2] = ret.high_;
      } else {
        // slow precise bignum division
        var x = Wrapper.lh2bignum(xl >>> 0, xh >>> 0);
        var y = Wrapper.lh2bignum(yl >>> 0, yh >>> 0);
        var z = new BigInteger();
        x.divRemTo(y, z, null);
        var l = new BigInteger();
        var h = new BigInteger();
        z.divRemTo(Wrapper.two32, h, l);
        HEAP32[tempDoublePtr>>2] = parseInt(l.toString()) | 0;
        HEAP32[tempDoublePtr+4>>2] = parseInt(h.toString()) | 0;
      }
    },
    modulo: function(xl, xh, yl, yh, unsigned) {
      Wrapper.ensureTemps();
      if (!unsigned) {
        var x = new goog.math.Long(xl, xh);
        var y = new goog.math.Long(yl, yh);
        var ret = x.modulo(y);
        HEAP32[tempDoublePtr>>2] = ret.low_;
        HEAP32[tempDoublePtr+4>>2] = ret.high_;
      } else {
        // slow precise bignum division
        var x = Wrapper.lh2bignum(xl >>> 0, xh >>> 0);
        var y = Wrapper.lh2bignum(yl >>> 0, yh >>> 0);
        var z = new BigInteger();
        x.divRemTo(y, null, z);
        var l = new BigInteger();
        var h = new BigInteger();
        z.divRemTo(Wrapper.two32, h, l);
        HEAP32[tempDoublePtr>>2] = parseInt(l.toString()) | 0;
        HEAP32[tempDoublePtr+4>>2] = parseInt(h.toString()) | 0;
      }
    },
    stringify: function(l, h, unsigned) {
      var ret = new goog.math.Long(l, h).toString();
      if (unsigned && ret[0] == '-') {
        // unsign slowly using jsbn bignums
        Wrapper.ensureTemps();
        var bignum = new BigInteger();
        bignum.fromString(ret, 10);
        ret = new BigInteger();
        Wrapper.two64.addTo(bignum, ret);
        ret = ret.toString(10);
      }
      return ret;
    },
    fromString: function(str, base, min, max, unsigned) {
      Wrapper.ensureTemps();
      var bignum = new BigInteger();
      bignum.fromString(str, base);
      var bigmin = new BigInteger();
      bigmin.fromString(min, 10);
      var bigmax = new BigInteger();
      bigmax.fromString(max, 10);
      if (unsigned && bignum.compareTo(BigInteger.ZERO) < 0) {
        var temp = new BigInteger();
        bignum.addTo(Wrapper.two64, temp);
        bignum = temp;
      }
      var error = false;
      if (bignum.compareTo(bigmin) < 0) {
        bignum = bigmin;
        error = true;
      } else if (bignum.compareTo(bigmax) > 0) {
        bignum = bigmax;
        error = true;
      }
      var ret = goog.math.Long.fromString(bignum.toString()); // min-max checks should have clamped this to a range goog.math.Long can handle well
      HEAP32[tempDoublePtr>>2] = ret.low_;
      HEAP32[tempDoublePtr+4>>2] = ret.high_;
      if (error) throw 'range error';
    }
  };
  return Wrapper;
})();
//======= end closure i64 code =======
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
