// Model of typed arrays, modeled after https://bitbucket.org/lindenlab/llsd
//
// Known unsoundness:
// - no coercions
// - all array elements are assumed to be numbers
//   - in fact: all attempts to set element are ignored!
// - many array-iterations are done with Array.prototype.XYZ-methods
//
(function (global) {
    // globals (eventually)
    var ArrayBuffer;
    var Int8Array;
    var Uint8Array;
    var Uint8ClampedArray;
    var Int16Array;
    var Uint16Array;
    var Int32Array;
    var Uint32Array;
    var Float32Array;
    var Float64Array;
    var DataView;

    var TAJS = {
        UInt: TAJS_make("AnyNumUInt"),
        Num: TAJS_make("AnyNum"),
        Nums: []
    };
    TAJS.Nums[TAJS.UInt] = TAJS.Num;
    TAJS.Nums.length = TAJS.UInt;

    // ArrayBuffer & TypedArrays
    (function () {
        ArrayBuffer = function ArrayBuffer() {
            var _bytes = [];
            _bytes[TAJS.UInt] = TAJS.Num;
            _bytes.length = TAJS.UInt;

            Object.defineProperty(this, 'byteLength', {value: _bytes.length});
            Object.defineProperty(this, '_bytes', {value: _bytes});
        };
        Object.defineProperty(ArrayBuffer.prototype, Symbol.toStringTag, {value: "ArrayBuffer"})
        Object.defineProperty(ArrayBuffer.prototype, 'slice', {
            value: function slice(begin, end) {
                var result = new ArrayBuffer(TAJS.UInt);
                result[TAJS.UInt] = this[TAJS.UInt];
                return result;
            }
        });

        function $TypedArray$(obj) {
            if (obj && obj.length) {
                obj[TAJS.UInt]; // generate reads
            }
            var buffer = new ArrayBuffer();
            Object.defineProperty(this, 'buffer', {value: buffer});
            Object.defineProperty(this, 'byteLength', {value: buffer.byteLength});
            Object.defineProperty(this, 'byteOffset', {value: TAJS.UInt});
            Object.defineProperty(this, 'length', {value: TAJS.UInt});
            Object.defineProperty(this, TAJS.UInt, {
                get: function () {
                    return TAJS.Num
                },
                set: function () {
                }
            });
        }

        Object.defineProperty($TypedArray$, 'from', {
            value: function (obj) {
                return new this();
            }
        });
        Object.defineProperty($TypedArray$, 'of', {
            value: function (obj) {
                return new this();
            }
        });

        var $TypedArrayPrototype$ = {};
        $TypedArray$.prototype = $TypedArrayPrototype$;

        Object.defineProperty($TypedArray$.prototype, 'constructor', {value: $TypedArray$});

        Object.defineProperty($TypedArray$.prototype, 'set', {
            value: function (arrayLike, offset) {
                this[TAJS.UInt + offset] = arrayLike[TAJS.UInt];
            }
        });

        Object.defineProperty($TypedArray$.prototype, 'copyWithin', {
            value: function () {

            }
        });

        Object.defineProperty($TypedArray$.prototype, 'every', {
            value: function () {
                return Array.prototype.every.apply(TAJS.Nums, arguments);
            }
        });

        Object.defineProperty($TypedArray$.prototype, 'fill', {
            value: function (v) {

            }
        });

        Object.defineProperty($TypedArray$.prototype, 'filter', {
            value: function () {
                var res = Array.prototype.filter.apply(TAJS.Nums, arguments);
                return new this.constructor(res)
            }
        });

        Object.defineProperty($TypedArray$.prototype, 'find', {
            value: function (callbackfn) {
                for (var i = 0; i < TAJS.UInt; i++) {
                    if (callbackfn(TAJS.Num)) {
                        return TAJS.Num;
                    }
                }
                return undefined;
            }
        });

        Object.defineProperty($TypedArray$.prototype, 'findIndex', {
            value: function (callbackfn) {
                for (var i = 0; i < TAJS.UInt; i++) {
                    if (callbackfn(TAJS.Num)) {
                        return i;
                    }
                }
                return -1;
            }
        });

        Object.defineProperty($TypedArray$.prototype, 'forEach', {
            value: function () {
                return Array.prototype.forEach.apply(TAJS.Nums, arguments);
            }
        });

        Object.defineProperty($TypedArray$.prototype, 'indexOf', {
            value: function () {
                return Array.prototype.indexOf.apply(TAJS.Nums, arguments);
            }
        });

        Object.defineProperty($TypedArray$.prototype, 'lastIndexOf', {
            value: function () {
                return Array.prototype.lastIndexOf.apply(TAJS.Nums, arguments);
            }
        });

        Object.defineProperty($TypedArray$.prototype, 'join', {
            value: function () {
                return Array.prototype.join.apply(TAJS.Nums, arguments);
            }
        });

        Object.defineProperty($TypedArray$.prototype, 'map', {
            value: function () {
                var res = Array.prototype.map.apply(TAJS.Nums, arguments);
                return new this.constructor(res);
            }
        });

        Object.defineProperty($TypedArray$.prototype, 'reduce', {
            value: function () {
                var res = Array.prototype.reduce.apply(TAJS.Nums, arguments);
                return new this.constructor(res);
            }
        });

        Object.defineProperty($TypedArray$.prototype, 'reduceRight', {
            value: function () {
                var res = Array.prototype.reduceRight.apply(TAJS.Nums, arguments);
                return new this.constructor(res);
            }
        });

        Object.defineProperty($TypedArray$.prototype, 'reverse', {
            value: function () {
                return this;
            }
        });

        Object.defineProperty($TypedArray$.prototype, 'slice', {
            value: function () {
                var res = Array.prototype.slice.apply(TAJS.Nums, arguments);
                return new this.constructor(res);
            }
        });


        Object.defineProperty($TypedArray$.prototype, 'some', {
            value: function () {
                return Array.prototype.some.apply(TAJS.Nums, arguments);
            }
        });

        Object.defineProperty($TypedArray$.prototype, 'sort', {
            value: function () {
                return Array.prototype.sort.apply(TAJS.Nums, arguments);
            }
        });

        Object.defineProperty($TypedArray$.prototype, 'subarray', {
            value: function () {
                return new this.constructor();
            }
        });

        function makeTypedArray(name) {
            function TypedArray() {
                Object.defineProperty(this, 'constructor', {value: TypedArray});
                Object.defineProperty(this, Symbol.species, {
                    configurable: true,
                    enumerable: false,
                    get: function () {
                        return TypedArray;
                    }
                });
                TypedArray.name = name;
                $TypedArray$.apply(this, arguments);
            }
            TAJS_makeContextSensitive(TypedArray, -2);

            TypedArray.__proto__ = $TypedArray$;
            TypedArray.BYTES_PER_ELEMENT = TAJS.UInt;

            var unsharedPrototype = TAJS_newObject();
            unsharedPrototype.__proto__ = $TypedArrayPrototype$;

            TypedArray.prototype = unsharedPrototype;

            Object.defineProperty(TypedArray.prototype, Symbol.toStringTag, {writable: false, value: name})

            Object.defineProperty(TypedArray.prototype, 'BYTES_PER_ELEMENT', {value: TypedArray.BYTES_PER_ELEMENT});

            return TypedArray;
        };

        // Make `makeTypedArray` context sensitive on its first argument, and pass different arguments below...
        TAJS_makeContextSensitive(makeTypedArray, 0);
        TAJS_makeContextSensitive(makeTypedArray, 1);

        Int8Array = makeTypedArray('Int8Array');
        Uint8Array = makeTypedArray('Uint8Array');
        Uint8ClampedArray = makeTypedArray('Uint8ClampedArray');
        Int16Array = makeTypedArray('Int16Array');
        Uint16Array = makeTypedArray('Uint16Array');
        Int32Array = makeTypedArray('Int32Array');
        Uint32Array = makeTypedArray('Uint32Array');
        Float32Array = makeTypedArray('Float32Array');
        Float64Array = makeTypedArray('Float64Array');
    })();

    // DataView
    (function () {
        DataView = function DataView(buffer, byteOffset, byteLength) {
            Object.defineProperty(this, 'buffer', {value: buffer});
            Object.defineProperty(this, 'byteLength', {value: byteLength});
            Object.defineProperty(this, 'byteOffset', {value: byteOffset});
        };

        function getter() {
            return TAJS.Num;
        };

        Object.defineProperty(DataView.prototype, 'getUint8', {value: getter});
        Object.defineProperty(DataView.prototype, 'getInt8', {value: getter});
        Object.defineProperty(DataView.prototype, 'getUint16', {value: getter});
        Object.defineProperty(DataView.prototype, 'getInt16', {value: getter});
        Object.defineProperty(DataView.prototype, 'getUint32', {value: getter});
        Object.defineProperty(DataView.prototype, 'getInt32', {value: getter});
        Object.defineProperty(DataView.prototype, 'getFloat32', {value: getter});
        Object.defineProperty(DataView.prototype, 'getFloat64', {value: getter});
        Object.defineProperty(DataView.prototype, Symbol.toStringTag, {value: "DataView"})

        function setter() {

        };

        Object.defineProperty(DataView.prototype, 'setUint8', {value: setter});
        Object.defineProperty(DataView.prototype, 'setInt8', {value: setter});
        Object.defineProperty(DataView.prototype, 'setUint16', {value: setter});
        Object.defineProperty(DataView.prototype, 'setInt16', {value: setter});
        Object.defineProperty(DataView.prototype, 'setUint32', {value: setter});
        Object.defineProperty(DataView.prototype, 'setInt32', {value: setter});
        Object.defineProperty(DataView.prototype, 'setFloat32', {value: setter});
        Object.defineProperty(DataView.prototype, 'setFloat64', {value: setter});
    })();

    global.ArrayBuffer = ArrayBuffer;
    global.Int8Array = Int8Array;
    global.Uint8Array = Uint8Array;
    global.Uint8ClampedArray = Uint8ClampedArray;
    global.Int16Array = Int16Array;
    global.Uint16Array = Uint16Array;
    global.Int32Array = Int32Array;
    global.Uint32Array = Uint32Array;
    global.Float32Array = Float32Array;
    global.Float64Array = Float64Array;
    global.DataView = DataView;
})(this);