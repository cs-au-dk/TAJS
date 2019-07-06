// Copied and modified from https://github.com/Financial-Times/polyfill-library/ (65ca84c / Jun 11 2019)
/* global Symbol */
// A modification of https://github.com/medikoo/es6-iterator
// Copyright (C) 2013-2015 Mariusz Nowak (www.medikoo.com)

var Iterator = (function () {
    var Iterator = function (list) {
        if (!(this instanceof Iterator)) {
            return new Iterator(list);
        }
        this.__list__ = list;
        this.__nextIndex__ = 0;
    };

    Object.defineProperties(Iterator.prototype, {
        constructor: {
            value: Iterator,
            configurable: true,
            enumerable: false,
            writable: true
        },
        next: {
            value: function () {
                if (this.__list__ && this.__nextIndex__ < this.__list__.length)
                    return this._createResult(this.__nextIndex__++);

                return {
                    done: true,
                    value: undefined
                };
            },
            configurable: true,
            enumerable: false,
            writable: true
        },
        _createResult: {
            value: function (i) {
                return {
                    done: false,
                    value: this._resolve(i)
                };
            },
            configurable: true,
            enumerable: false,
            writable: true
        },
        _resolve: {
            value: function (i) {
                return this.__list__[i];
            },
            configurable: true,
            enumerable: false,
            writable: true
        }
    });

    Object.defineProperty(Iterator.prototype, Symbol.iterator, {
        value: function () {
            return this;
        },
        configurable: true,
        enumerable: false,
        writable: true
    });
    Object.defineProperty(Iterator.prototype, Symbol.toStringTag, {
        value: 'Iterator',
        configurable: false,
        enumerable: false,
        writable: true
    });

    return Iterator;
}());

var ArrayIterator = (function() { // eslint-disable-line no-unused-vars

    var ArrayIterator = function(arr, kind) {
        if (!(this instanceof ArrayIterator)) return new ArrayIterator(arr, kind);
        Iterator.call(this, arr);
        if (!kind) kind = 'value';
        else if (String.prototype.includes.call(kind, 'key+value')) kind = 'key+value';
        else if (String.prototype.includes.call(kind, 'key')) kind = 'key';
        else kind = 'value';
        this.__kind__ = kind;
    };
    Object.setPrototypeOf(ArrayIterator, Iterator.prototype);

    ArrayIterator.prototype = Object.create(Iterator.prototype, {
        constructor: {
            value: ArrayIterator,
            configurable: true,
            enumerable: false,
            writable: true
        },
        _resolve: {
            value: function(i) {
                if (this.__kind__ === 'value') return this.__list__[i];
                if (this.__kind__ === 'key+value') return [i, this.__list__[i]];
                return i;
            },
            configurable: true,
            enumerable: false,
            writable: true
        }
    });

    Object.defineProperty(ArrayIterator.prototype, Symbol.toStringTag, {
        value: 'Array Iterator',
        configurable: false,
        enumerable: false,
        writable: true
    });

    return ArrayIterator;
}());

var StringIterator = (function() { // eslint-disable-line no-unused-vars

    var StringIterator = function (str) {
        if (!(this instanceof StringIterator)) return new StringIterator(str);
        str = String(str);
        Iterator.call(this, str);
        this.__length__ = str.length;
    };
    Object.setPrototypeOf(StringIterator, Iterator);

    StringIterator.prototype = Object.create(Iterator.prototype, {
        constructor: {
            value: StringIterator,
            configurable: true,
            enumerable: false,
            writable: true
        },
        _resolve: {
            value: function (i) {
                var char = this.__list__[i], code;
                if (this.__nextIndex__ === this.__length__) return char;
                code = char.charCodeAt(0);
                if ((code >= 0xD800) && (code <= 0xDBFF)) return char + this.__list__[this.__nextIndex__++];
                return char;
            },
            configurable: true,
            enumerable: false,
            writable: true
        }
    });

    Object.defineProperty(StringIterator.prototype, Symbol.toStringTag, {
        value: 'String Iterator',
        configurable: false,
        enumerable: false,
        writable: true
    });

    return StringIterator;
}());

// Actual polyfilling

if(!Array.prototype[Symbol.iterator])
    Object.defineProperty(Array.prototype, Symbol.iterator, {
        get: function() {
            return this.values;
        },
        configurable: true,
        enumerable: false
    });

if(!Array.prototype.values)
    Object.defineProperty(Array.prototype, 'values', {
        value: function() {
            return new ArrayIterator(this);
        },
        configurable: true,
        enumerable: false,
        writable: true
    });

if(!Array.prototype.keys)
    Object.defineProperty(Array.prototype, 'keys', {
        value: function() {
            return new ArrayIterator(this, 'key');
        },
        configurable: true,
        enumerable: false,
        writable: true
    });

if(!Array.prototype.entries)
    Object.defineProperty(Array.prototype, 'entries', {
        value: function() {
            return new ArrayIterator(this, 'key+value');
        },
        configurable: true,
        enumerable: false,
        writable: true
    });

if(!String.prototype[Symbol.iterator])
    Object.defineProperty(String.prototype, Symbol.iterator, {
        value: function() {
            return new StringIterator(this);
        },
        configurable: true,
        enumerable: false,
        writable: true
    });