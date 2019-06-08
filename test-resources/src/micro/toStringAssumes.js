(function() {
    var array = [];
    var obj = {};
    var number = 5;
    var string = "foo";
    var boolean = true;
    var regexp = /x/;
    var date = new Date();

    var b = TAJS_join(array, number, string, boolean, undefined, regexp, date)
    var a = TAJS_join(obj, b);

    var className = Object.prototype.toString.call(a);
    if (className === "[object Object]") {
        TAJS_assertEquals(obj, a);
    } else {
        TAJS_assertEquals(b, a);
    }

    className = Object.prototype.toString.apply(a);
    if (className === "[object Object]") {
        TAJS_assertEquals(obj, a);
    } else {
        TAJS_assertEquals(b, a);
    }


    var y = TAJS_make("AnyStr")
    var x = y;
    if (x === "foo") {
      TAJS_assertEquals("foo", y);
    }

    var typeOfA = typeof a;
    var aIsString = typeOfA === "string";
    if (aIsString) {
      TAJS_assertEquals("foo", a);
    }
    var toString = Object.prototype.toString;
    className = toString.call(a);

    if (className !== toString.call(b)) return false;

    switch (className) {
      // Strings, numbers, regular expressions, dates, and booleans are compared by value.
      case '[object RegExp]':
        TAJS_assertEquals(regexp, a);
        TAJS_assertEquals(regexp, b);
        return;
      case '[object String]':
        TAJS_assertEquals(string, a);
        TAJS_assertEquals(string, b);
        return;
      case '[object Number]':
        TAJS_assertEquals(number, a);
        TAJS_assertEquals(number, b);
        return;
      case '[object Date]':
        TAJS_assertEquals(date, a);
        TAJS_assertEquals(date, b);
        return;
      case '[object Boolean]':
        TAJS_assertEquals(boolean, a);
        TAJS_assertEquals(boolean, b);
        return;
    }

    var isArray = className === '[object Array]';
    if (isArray) {
        TAJS_assertEquals(array, a)
        TAJS_assertEquals(array, b)
    } else {
        TAJS_assertEquals(TAJS_join(obj, undefined), a)
        TAJS_assertEquals(undefined, b)
    }
}())
