test = asyncTest = function (name, rval, f) {
    //print(f);
    //print(name);
    //print(rval);
    if (arguments.length === 2) {
        f = rval;
    }
    f();
};

module = moduleTeardown = expect = start = stop = equal = notEqual = strictEqual = deepEqual = ok = function () {
};

raises = function (f) {
    //print(f);
    try {
        f();
    } catch (e) {
    }
};

module("Action");

Enumerable = function () {
    var Enumerable = function (getEnumerator) {
        //print(ArrayEnumerable);
        //print(Dictionary);
        //print(Enumerable);
        //print(Functions);
        //print(Grouping);
        //print(IEnumerator);
        //print(Lookup);
        //print(OrderedEnumerable);
        //print(SortContext);
        //print(State);
        //print(Types);
        //print(Utils);
        //print(Yielder);
        //print(getEnumerator);
        this.GetEnumerator = getEnumerator;
    };
    Enumerable.Choice = function () {
        //print(ArrayEnumerable);
        //print(Dictionary);
        //print(Enumerable);
        //print(Functions);
        //print(Grouping);
        //print(IEnumerator);
        //print(Lookup);
        //print(OrderedEnumerable);
        //print(SortContext);
        //print(State);
        //print(Types);
        //print(Utils);
        //print(Yielder);
        var args = arguments[0] instanceof Array ? arguments[0] : arguments;
        return new Enumerable(function () {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(args);
            return new IEnumerator(Functions.Blank, function () {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(args);
                return this.Yield(args[Math.floor(Math.random() * args.length)]);
            }, Functions.Blank);
        });
    };
    Enumerable.Cycle = function () {
        //print(ArrayEnumerable);
        //print(Dictionary);
        //print(Enumerable);
        //print(Functions);
        //print(Grouping);
        //print(IEnumerator);
        //print(Lookup);
        //print(OrderedEnumerable);
        //print(SortContext);
        //print(State);
        //print(Types);
        //print(Utils);
        //print(Yielder);
        var args = arguments[0] instanceof Array ? arguments[0] : arguments;
        return new Enumerable(function () {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(args);
            var index = 0;
            return new IEnumerator(Functions.Blank, function () {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(args);
                //print(index);
                if (index >= args.length) index = 0;
                return this.Yield(args[index++]);
            }, Functions.Blank);
        });
    };
    Enumerable.Empty = function () {
        //print(ArrayEnumerable);
        //print(Dictionary);
        //print(Enumerable);
        //print(Functions);
        //print(Grouping);
        //print(IEnumerator);
        //print(Lookup);
        //print(OrderedEnumerable);
        //print(SortContext);
        //print(State);
        //print(Types);
        //print(Utils);
        //print(Yielder);
        return new Enumerable(function () {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            return new IEnumerator(Functions.Blank, function () {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                return false;
            }, Functions.Blank);
        });
    };
    Enumerable.From = function (obj) {
        //print(ArrayEnumerable);
        //print(Dictionary);
        //print(Enumerable);
        //print(Functions);
        //print(Grouping);
        //print(IEnumerator);
        //print(Lookup);
        //print(OrderedEnumerable);
        //print(SortContext);
        //print(State);
        //print(Types);
        //print(Utils);
        //print(Yielder);
        //print(obj);
        if (obj == null) {
            return Enumerable.Empty();
        }
        if (obj instanceof Enumerable) {
            return obj;
        }
        if (typeof obj == Types.Number || typeof obj == Types.Boolean) {
            return Enumerable.Repeat(obj, 1);
        }
        if (typeof obj == Types.String) {
            return new Enumerable(function () {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(obj);
                var index = 0;
                return new IEnumerator(Functions.Blank, function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(index);
                    //print(obj);
                    return index < obj.length ? this.Yield(obj.charAt(index++)) : false;
                }, Functions.Blank);
            });
        }
        if (typeof obj != Types.Function) {
            if (typeof obj.length == Types.Number) {
                return new ArrayEnumerable(obj);
            }
            if (!(obj instanceof Object) && Utils.IsIEnumerable(obj)) {
                return new Enumerable(function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(obj);
                    var isFirst = true;
                    var enumerator;
                    return new IEnumerator(function () {
                        //print(ArrayEnumerable);
                        //print(Dictionary);
                        //print(Enumerable);
                        //print(Functions);
                        //print(Grouping);
                        //print(IEnumerator);
                        //print(Lookup);
                        //print(OrderedEnumerable);
                        //print(SortContext);
                        //print(State);
                        //print(Types);
                        //print(Utils);
                        //print(Yielder);
                        //print(enumerator);
                        //print(isFirst);
                        //print(obj);
                        enumerator = new Enumerator(obj);
                    }, function () {
                        //print(ArrayEnumerable);
                        //print(Dictionary);
                        //print(Enumerable);
                        //print(Functions);
                        //print(Grouping);
                        //print(IEnumerator);
                        //print(Lookup);
                        //print(OrderedEnumerable);
                        //print(SortContext);
                        //print(State);
                        //print(Types);
                        //print(Utils);
                        //print(Yielder);
                        //print(enumerator);
                        //print(isFirst);
                        //print(obj);
                        if (isFirst) isFirst = false; else enumerator.moveNext();
                        return enumerator.atEnd() ? false : this.Yield(enumerator.item());
                    }, Functions.Blank);
                });
            }
        }
        return new Enumerable(function () {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(obj);
            var array = [];
            var index = 0;
            return new IEnumerator(function () {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(array);
                //print(index);
                //print(obj);
                for (var key in obj) {
                    if (!(obj[key] instanceof Function)) {
                        array.push({
                            Key: key,
                            Value: obj[key]
                        });
                    }
                }
            }, function () {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(array);
                //print(index);
                //print(obj);
                return index < array.length ? this.Yield(array[index++]) : false;
            }, Functions.Blank);
        });
    }, Enumerable.Return = function (element) {
        //print(ArrayEnumerable);
        //print(Dictionary);
        //print(Enumerable);
        //print(Functions);
        //print(Grouping);
        //print(IEnumerator);
        //print(Lookup);
        //print(OrderedEnumerable);
        //print(SortContext);
        //print(State);
        //print(Types);
        //print(Utils);
        //print(Yielder);
        //print(element);
        return Enumerable.Repeat(element, 1);
    };
    Enumerable.Matches = function (input, pattern, flags) {
        //print(ArrayEnumerable);
        //print(Dictionary);
        //print(Enumerable);
        //print(Functions);
        //print(Grouping);
        //print(IEnumerator);
        //print(Lookup);
        //print(OrderedEnumerable);
        //print(SortContext);
        //print(State);
        //print(Types);
        //print(Utils);
        //print(Yielder);
        //print(flags);
        //print(input);
        //print(pattern);
        if (flags == null) flags = "";
        if (pattern instanceof RegExp) {
            flags += pattern.ignoreCase ? "i" : "";
            flags += pattern.multiline ? "m" : "";
            pattern = pattern.source;
        }
        if (flags.indexOf("g") === -1) flags += "g";
        return new Enumerable(function () {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(flags);
            //print(input);
            //print(pattern);
            var regex;
            return new IEnumerator(function () {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(flags);
                //print(input);
                //print(pattern);
                //print(regex);
                regex = new RegExp(pattern, flags);
            }, function () {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(flags);
                //print(input);
                //print(pattern);
                //print(regex);
                var match = regex.exec(input);
                return match ? this.Yield(match) : false;
            }, Functions.Blank);
        });
    };
    Enumerable.Range = function (start, count, step) {
        //print(ArrayEnumerable);
        //print(Dictionary);
        //print(Enumerable);
        //print(Functions);
        //print(Grouping);
        //print(IEnumerator);
        //print(Lookup);
        //print(OrderedEnumerable);
        //print(SortContext);
        //print(State);
        //print(Types);
        //print(Utils);
        //print(Yielder);
        //print(count);
        //print(start);
        //print(step);
        if (step == null) step = 1;
        return Enumerable.ToInfinity(start, step).Take(count);
    };
    Enumerable.RangeDown = function (start, count, step) {
        //print(ArrayEnumerable);
        //print(Dictionary);
        //print(Enumerable);
        //print(Functions);
        //print(Grouping);
        //print(IEnumerator);
        //print(Lookup);
        //print(OrderedEnumerable);
        //print(SortContext);
        //print(State);
        //print(Types);
        //print(Utils);
        //print(Yielder);
        //print(count);
        //print(start);
        //print(step);
        if (step == null) step = 1;
        return Enumerable.ToNegativeInfinity(start, step).Take(count);
    };
    Enumerable.RangeTo = function (start, to, step) {
        //print(ArrayEnumerable);
        //print(Dictionary);
        //print(Enumerable);
        //print(Functions);
        //print(Grouping);
        //print(IEnumerator);
        //print(Lookup);
        //print(OrderedEnumerable);
        //print(SortContext);
        //print(State);
        //print(Types);
        //print(Utils);
        //print(Yielder);
        //print(start);
        //print(step);
        //print(to);
        if (step == null) step = 1;
        return start < to ? Enumerable.ToInfinity(start, step).TakeWhile(function (i) {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(i);
            //print(start);
            //print(step);
            //print(to);
            return i <= to;
        }) : Enumerable.ToNegativeInfinity(start, step).TakeWhile(function (i) {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(i);
            //print(start);
            //print(step);
            //print(to);
            return i >= to;
        });
    };
    Enumerable.Repeat = function (obj, num) {
        //print(ArrayEnumerable);
        //print(Dictionary);
        //print(Enumerable);
        //print(Functions);
        //print(Grouping);
        //print(IEnumerator);
        //print(Lookup);
        //print(OrderedEnumerable);
        //print(SortContext);
        //print(State);
        //print(Types);
        //print(Utils);
        //print(Yielder);
        //print(num);
        //print(obj);
        if (num != null) return Enumerable.Repeat(obj).Take(num);
        return new Enumerable(function () {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(num);
            //print(obj);
            return new IEnumerator(Functions.Blank, function () {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(num);
                //print(obj);
                return this.Yield(obj);
            }, Functions.Blank);
        });
    };
    Enumerable.RepeatWithFinalize = function (initializer, finalizer) {
        //print(ArrayEnumerable);
        //print(Dictionary);
        //print(Enumerable);
        //print(Functions);
        //print(Grouping);
        //print(IEnumerator);
        //print(Lookup);
        //print(OrderedEnumerable);
        //print(SortContext);
        //print(State);
        //print(Types);
        //print(Utils);
        //print(Yielder);
        //print(finalizer);
        //print(initializer);
        initializer = Utils.CreateLambda(initializer);
        finalizer = Utils.CreateLambda(finalizer);
        return new Enumerable(function () {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(finalizer);
            //print(initializer);
            var element;
            return new IEnumerator(function () {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(element);
                //print(finalizer);
                //print(initializer);
                element = initializer();
            }, function () {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(element);
                //print(finalizer);
                //print(initializer);
                return this.Yield(element);
            }, function () {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(element);
                //print(finalizer);
                //print(initializer);
                if (element != null) {
                    finalizer(element);
                    element = null;
                }
            });
        });
    };
    Enumerable.Generate = function (func, count) {
        //print(ArrayEnumerable);
        //print(Dictionary);
        //print(Enumerable);
        //print(Functions);
        //print(Grouping);
        //print(IEnumerator);
        //print(Lookup);
        //print(OrderedEnumerable);
        //print(SortContext);
        //print(State);
        //print(Types);
        //print(Utils);
        //print(Yielder);
        //print(count);
        //print(func);
        if (count != null) return Enumerable.Generate(func).Take(count);
        func = Utils.CreateLambda(func);
        return new Enumerable(function () {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(count);
            //print(func);
            return new IEnumerator(Functions.Blank, function () {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(count);
                //print(func);
                return this.Yield(func());
            }, Functions.Blank);
        });
    };
    Enumerable.ToInfinity = function (start, step) {
        //print(ArrayEnumerable);
        //print(Dictionary);
        //print(Enumerable);
        //print(Functions);
        //print(Grouping);
        //print(IEnumerator);
        //print(Lookup);
        //print(OrderedEnumerable);
        //print(SortContext);
        //print(State);
        //print(Types);
        //print(Utils);
        //print(Yielder);
        //print(start);
        //print(step);
        if (start == null) start = 0;
        if (step == null) step = 1;
        return new Enumerable(function () {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(start);
            //print(step);
            var value;
            return new IEnumerator(function () {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(start);
                //print(step);
                //print(value);
                value = start - step;
            }, function () {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(start);
                //print(step);
                //print(value);
                return this.Yield(value += step);
            }, Functions.Blank);
        });
    };
    Enumerable.ToNegativeInfinity = function (start, step) {
        //print(ArrayEnumerable);
        //print(Dictionary);
        //print(Enumerable);
        //print(Functions);
        //print(Grouping);
        //print(IEnumerator);
        //print(Lookup);
        //print(OrderedEnumerable);
        //print(SortContext);
        //print(State);
        //print(Types);
        //print(Utils);
        //print(Yielder);
        //print(start);
        //print(step);
        if (start == null) start = 0;
        if (step == null) step = 1;
        return new Enumerable(function () {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(start);
            //print(step);
            var value;
            return new IEnumerator(function () {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(start);
                //print(step);
                //print(value);
                value = start + step;
            }, function () {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(start);
                //print(step);
                //print(value);
                return this.Yield(value -= step);
            }, Functions.Blank);
        });
    };
    Enumerable.Unfold = function (seed, func) {
        //print(ArrayEnumerable);
        //print(Dictionary);
        //print(Enumerable);
        //print(Functions);
        //print(Grouping);
        //print(IEnumerator);
        //print(Lookup);
        //print(OrderedEnumerable);
        //print(SortContext);
        //print(State);
        //print(Types);
        //print(Utils);
        //print(Yielder);
        //print(func);
        //print(seed);
        func = Utils.CreateLambda(func);
        return new Enumerable(function () {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(func);
            //print(seed);
            var isFirst = true;
            var value;
            return new IEnumerator(Functions.Blank, function () {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(func);
                //print(isFirst);
                //print(seed);
                //print(value);
                if (isFirst) {
                    isFirst = false;
                    value = seed;
                    return this.Yield(value);
                }
                value = func(value);
                return this.Yield(value);
            }, Functions.Blank);
        });
    };
    Enumerable.prototype = {
        CascadeBreadthFirst: function (func, resultSelector) {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(func);
            //print(resultSelector);
            var source = this;
            func = Utils.CreateLambda(func);
            resultSelector = Utils.CreateLambda(resultSelector);
            return new Enumerable(function () {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(func);
                //print(resultSelector);
                //print(source);
                var enumerator;
                var nestLevel = 0;
                var buffer = [];
                return new IEnumerator(function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(buffer);
                    //print(enumerator);
                    //print(func);
                    //print(nestLevel);
                    //print(resultSelector);
                    //print(source);
                    enumerator = source.GetEnumerator();
                }, function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(buffer);
                    //print(enumerator);
                    //print(func);
                    //print(nestLevel);
                    //print(resultSelector);
                    //print(source);
                    while (true) {
                        if (enumerator.MoveNext()) {
                            buffer.push(enumerator.Current());
                            return this.Yield(resultSelector(enumerator.Current(), nestLevel));
                        }
                        var next = Enumerable.From(buffer).SelectMany(function (x) {
                            //print(ArrayEnumerable);
                            //print(Dictionary);
                            //print(Enumerable);
                            //print(Functions);
                            //print(Grouping);
                            //print(IEnumerator);
                            //print(Lookup);
                            //print(OrderedEnumerable);
                            //print(SortContext);
                            //print(State);
                            //print(Types);
                            //print(Utils);
                            //print(Yielder);
                            //print(buffer);
                            //print(enumerator);
                            //print(func);
                            //print(nestLevel);
                            //print(next);
                            //print(resultSelector);
                            //print(source);
                            //print(x);
                            return func(x);
                        });
                        if (!next.Any()) {
                            return false;
                        } else {
                            nestLevel++;
                            buffer = [];
                            Utils.Dispose(enumerator);
                            enumerator = next.GetEnumerator();
                        }
                    }
                }, function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(buffer);
                    //print(enumerator);
                    //print(func);
                    //print(nestLevel);
                    //print(resultSelector);
                    //print(source);
                    Utils.Dispose(enumerator);
                });
            });
        },
        CascadeDepthFirst: function (func, resultSelector) {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(func);
            //print(resultSelector);
            var source = this;
            func = Utils.CreateLambda(func);
            resultSelector = Utils.CreateLambda(resultSelector);
            return new Enumerable(function () {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(func);
                //print(resultSelector);
                //print(source);
                var enumeratorStack = [];
                var enumerator;
                return new IEnumerator(function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(enumerator);
                    //print(enumeratorStack);
                    //print(func);
                    //print(resultSelector);
                    //print(source);
                    enumerator = source.GetEnumerator();
                }, function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(enumerator);
                    //print(enumeratorStack);
                    //print(func);
                    //print(resultSelector);
                    //print(source);
                    while (true) {
                        if (enumerator.MoveNext()) {
                            var value = resultSelector(enumerator.Current(), enumeratorStack.length);
                            enumeratorStack.push(enumerator);
                            enumerator = Enumerable.From(func(enumerator.Current())).GetEnumerator();
                            return this.Yield(value);
                        }
                        if (enumeratorStack.length <= 0) return false;
                        Utils.Dispose(enumerator);
                        enumerator = enumeratorStack.pop();
                    }
                }, function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(enumerator);
                    //print(enumeratorStack);
                    //print(func);
                    //print(resultSelector);
                    //print(source);
                    try {
                        Utils.Dispose(enumerator);
                    } finally {
                        Enumerable.From(enumeratorStack).ForEach(function (s) {
                            //print(ArrayEnumerable);
                            //print(Dictionary);
                            //print(Enumerable);
                            //print(Functions);
                            //print(Grouping);
                            //print(IEnumerator);
                            //print(Lookup);
                            //print(OrderedEnumerable);
                            //print(SortContext);
                            //print(State);
                            //print(Types);
                            //print(Utils);
                            //print(Yielder);
                            //print(enumerator);
                            //print(enumeratorStack);
                            //print(func);
                            //print(resultSelector);
                            //print(s);
                            //print(source);
                            s.Dispose();
                        });
                    }
                });
            });
        },
        Flatten: function () {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            var source = this;
            return new Enumerable(function () {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(source);
                var enumerator;
                var middleEnumerator = null;
                return new IEnumerator(function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(enumerator);
                    //print(middleEnumerator);
                    //print(source);
                    enumerator = source.GetEnumerator();
                }, function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(enumerator);
                    //print(middleEnumerator);
                    //print(source);
                    while (true) {
                        if (middleEnumerator != null) {
                            if (middleEnumerator.MoveNext()) {
                                return this.Yield(middleEnumerator.Current());
                            } else {
                                middleEnumerator = null;
                            }
                        }
                        if (enumerator.MoveNext()) {
                            if (enumerator.Current() instanceof Array) {
                                Utils.Dispose(middleEnumerator);
                                middleEnumerator = Enumerable.From(enumerator.Current()).SelectMany(Functions.Identity).Flatten().GetEnumerator();
                                continue;
                            } else {
                                return this.Yield(enumerator.Current());
                            }
                        }
                        return false;
                    }
                }, function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(enumerator);
                    //print(middleEnumerator);
                    //print(source);
                    try {
                        Utils.Dispose(enumerator);
                    } finally {
                        Utils.Dispose(middleEnumerator);
                    }
                });
            });
        },
        Pairwise: function (selector) {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(selector);
            var source = this;
            selector = Utils.CreateLambda(selector);
            return new Enumerable(function () {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(selector);
                //print(source);
                var enumerator;
                return new IEnumerator(function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(enumerator);
                    //print(selector);
                    //print(source);
                    enumerator = source.GetEnumerator();
                    enumerator.MoveNext();
                }, function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(enumerator);
                    //print(selector);
                    //print(source);
                    var prev = enumerator.Current();
                    return enumerator.MoveNext() ? this.Yield(selector(prev, enumerator.Current())) : false;
                }, function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(enumerator);
                    //print(selector);
                    //print(source);
                    Utils.Dispose(enumerator);
                });
            });
        },
        Scan: function (seed, func, resultSelector) {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(func);
            //print(resultSelector);
            //print(seed);
            if (resultSelector != null) return this.Scan(seed, func).Select(resultSelector);
            var isUseSeed;
            if (func == null) {
                func = Utils.CreateLambda(seed);
                isUseSeed = false;
            } else {
                func = Utils.CreateLambda(func);
                isUseSeed = true;
            }
            var source = this;
            return new Enumerable(function () {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(func);
                //print(isUseSeed);
                //print(resultSelector);
                //print(seed);
                //print(source);
                var enumerator;
                var value;
                var isFirst = true;
                return new IEnumerator(function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(enumerator);
                    //print(func);
                    //print(isFirst);
                    //print(isUseSeed);
                    //print(resultSelector);
                    //print(seed);
                    //print(source);
                    //print(value);
                    enumerator = source.GetEnumerator();
                }, function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(enumerator);
                    //print(func);
                    //print(isFirst);
                    //print(isUseSeed);
                    //print(resultSelector);
                    //print(seed);
                    //print(source);
                    //print(value);
                    if (isFirst) {
                        isFirst = false;
                        if (!isUseSeed) {
                            if (enumerator.MoveNext()) {
                                return this.Yield(value = enumerator.Current());
                            }
                        } else {
                            return this.Yield(value = seed);
                        }
                    }
                    return enumerator.MoveNext() ? this.Yield(value = func(value, enumerator.Current())) : false;
                }, function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(enumerator);
                    //print(func);
                    //print(isFirst);
                    //print(isUseSeed);
                    //print(resultSelector);
                    //print(seed);
                    //print(source);
                    //print(value);
                    Utils.Dispose(enumerator);
                });
            });
        },
        Select: function (selector) {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(selector);
            var source = this;
            selector = Utils.CreateLambda(selector);
            return new Enumerable(function () {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(selector);
                //print(source);
                var enumerator;
                var index = 0;
                return new IEnumerator(function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(enumerator);
                    //print(index);
                    //print(selector);
                    //print(source);
                    enumerator = source.GetEnumerator();
                }, function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(enumerator);
                    //print(index);
                    //print(selector);
                    //print(source);
                    return enumerator.MoveNext() ? this.Yield(selector(enumerator.Current(), index++)) : false;
                }, function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(enumerator);
                    //print(index);
                    //print(selector);
                    //print(source);
                    Utils.Dispose(enumerator);
                });
            });
        },
        SelectMany: function (collectionSelector, resultSelector) {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(collectionSelector);
            //print(resultSelector);
            var source = this;
            collectionSelector = Utils.CreateLambda(collectionSelector);
            if (resultSelector == null) resultSelector = function (a, b) {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(a);
                //print(b);
                //print(collectionSelector);
                //print(resultSelector);
                //print(source);
                return b;
            };
            resultSelector = Utils.CreateLambda(resultSelector);
            return new Enumerable(function () {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(collectionSelector);
                //print(resultSelector);
                //print(source);
                var enumerator;
                var middleEnumerator = undefined;
                var index = 0;
                return new IEnumerator(function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(collectionSelector);
                    //print(enumerator);
                    //print(index);
                    //print(middleEnumerator);
                    //print(resultSelector);
                    //print(source);
                    enumerator = source.GetEnumerator();
                }, function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(collectionSelector);
                    //print(enumerator);
                    //print(index);
                    //print(middleEnumerator);
                    //print(resultSelector);
                    //print(source);
                    if (middleEnumerator === undefined) {
                        if (!enumerator.MoveNext()) return false;
                    }
                    do {
                        if (middleEnumerator == null) {
                            var middleSeq = collectionSelector(enumerator.Current(), index++);
                            middleEnumerator = Enumerable.From(middleSeq).GetEnumerator();
                        }
                        if (middleEnumerator.MoveNext()) {
                            return this.Yield(resultSelector(enumerator.Current(), middleEnumerator.Current()));
                        }
                        Utils.Dispose(middleEnumerator);
                        middleEnumerator = null;
                    } while (enumerator.MoveNext());
                    return false;
                }, function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(collectionSelector);
                    //print(enumerator);
                    //print(index);
                    //print(middleEnumerator);
                    //print(resultSelector);
                    //print(source);
                    try {
                        Utils.Dispose(enumerator);
                    } finally {
                        Utils.Dispose(middleEnumerator);
                    }
                });
            });
        },
        Where: function (predicate) {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(predicate);
            predicate = Utils.CreateLambda(predicate);
            var source = this;
            return new Enumerable(function () {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(predicate);
                //print(source);
                var enumerator;
                var index = 0;
                return new IEnumerator(function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(enumerator);
                    //print(index);
                    //print(predicate);
                    //print(source);
                    enumerator = source.GetEnumerator();
                }, function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(enumerator);
                    //print(index);
                    //print(predicate);
                    //print(source);
                    while (enumerator.MoveNext()) {
                        if (predicate(enumerator.Current(), index++)) {
                            return this.Yield(enumerator.Current());
                        }
                    }
                    return false;
                }, function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(enumerator);
                    //print(index);
                    //print(predicate);
                    //print(source);
                    Utils.Dispose(enumerator);
                });
            });
        },
        OfType: function (type) {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(type);
            var typeName;
            switch (type) {
                case Number:
                    typeName = Types.Number;
                    break;

                case String:
                    typeName = Types.String;
                    break;

                case Boolean:
                    typeName = Types.Boolean;
                    break;

                case Function:
                    typeName = Types.Function;
                    break;

                default:
                    typeName = null;
                    break;
            }
            return typeName === null ? this.Where(function (x) {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(type);
                //print(typeName);
                //print(x);
                return x instanceof type;
            }) : this.Where(function (x) {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(type);
                //print(typeName);
                //print(x);
                return typeof x === typeName;
            });
        },
        Zip: function (second, selector) {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(second);
            //print(selector);
            selector = Utils.CreateLambda(selector);
            var source = this;
            return new Enumerable(function () {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(second);
                //print(selector);
                //print(source);
                var firstEnumerator;
                var secondEnumerator;
                var index = 0;
                return new IEnumerator(function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(firstEnumerator);
                    //print(index);
                    //print(second);
                    //print(secondEnumerator);
                    //print(selector);
                    //print(source);
                    firstEnumerator = source.GetEnumerator();
                    secondEnumerator = Enumerable.From(second).GetEnumerator();
                }, function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(firstEnumerator);
                    //print(index);
                    //print(second);
                    //print(secondEnumerator);
                    //print(selector);
                    //print(source);
                    if (firstEnumerator.MoveNext() && secondEnumerator.MoveNext()) {
                        return this.Yield(selector(firstEnumerator.Current(), secondEnumerator.Current(), index++));
                    }
                    return false;
                }, function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(firstEnumerator);
                    //print(index);
                    //print(second);
                    //print(secondEnumerator);
                    //print(selector);
                    //print(source);
                    try {
                        Utils.Dispose(firstEnumerator);
                    } finally {
                        Utils.Dispose(secondEnumerator);
                    }
                });
            });
        },
        Join: function (inner, outerKeySelector, innerKeySelector, resultSelector, compareSelector) {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(compareSelector);
            //print(inner);
            //print(innerKeySelector);
            //print(outerKeySelector);
            //print(resultSelector);
            outerKeySelector = Utils.CreateLambda(outerKeySelector);
            innerKeySelector = Utils.CreateLambda(innerKeySelector);
            resultSelector = Utils.CreateLambda(resultSelector);
            compareSelector = Utils.CreateLambda(compareSelector);
            var source = this;
            return new Enumerable(function () {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(compareSelector);
                //print(inner);
                //print(innerKeySelector);
                //print(outerKeySelector);
                //print(resultSelector);
                //print(source);
                var outerEnumerator;
                var lookup;
                var innerElements = null;
                var innerCount = 0;
                return new IEnumerator(function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(compareSelector);
                    //print(inner);
                    //print(innerCount);
                    //print(innerElements);
                    //print(innerKeySelector);
                    //print(lookup);
                    //print(outerEnumerator);
                    //print(outerKeySelector);
                    //print(resultSelector);
                    //print(source);
                    outerEnumerator = source.GetEnumerator();
                    lookup = Enumerable.From(inner).ToLookup(innerKeySelector, Functions.Identity, compareSelector);
                }, function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(compareSelector);
                    //print(inner);
                    //print(innerCount);
                    //print(innerElements);
                    //print(innerKeySelector);
                    //print(lookup);
                    //print(outerEnumerator);
                    //print(outerKeySelector);
                    //print(resultSelector);
                    //print(source);
                    while (true) {
                        if (innerElements != null) {
                            var innerElement = innerElements[innerCount++];
                            if (innerElement !== undefined) {
                                return this.Yield(resultSelector(outerEnumerator.Current(), innerElement));
                            }
                            innerElement = null;
                            innerCount = 0;
                        }
                        if (outerEnumerator.MoveNext()) {
                            var key = outerKeySelector(outerEnumerator.Current());
                            innerElements = lookup.Get(key).ToArray();
                        } else {
                            return false;
                        }
                    }
                }, function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(compareSelector);
                    //print(inner);
                    //print(innerCount);
                    //print(innerElements);
                    //print(innerKeySelector);
                    //print(lookup);
                    //print(outerEnumerator);
                    //print(outerKeySelector);
                    //print(resultSelector);
                    //print(source);
                    Utils.Dispose(outerEnumerator);
                });
            });
        },
        GroupJoin: function (inner, outerKeySelector, innerKeySelector, resultSelector, compareSelector) {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(compareSelector);
            //print(inner);
            //print(innerKeySelector);
            //print(outerKeySelector);
            //print(resultSelector);
            outerKeySelector = Utils.CreateLambda(outerKeySelector);
            innerKeySelector = Utils.CreateLambda(innerKeySelector);
            resultSelector = Utils.CreateLambda(resultSelector);
            compareSelector = Utils.CreateLambda(compareSelector);
            var source = this;
            return new Enumerable(function () {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(compareSelector);
                //print(inner);
                //print(innerKeySelector);
                //print(outerKeySelector);
                //print(resultSelector);
                //print(source);
                var enumerator = source.GetEnumerator();
                var lookup = null;
                return new IEnumerator(function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(compareSelector);
                    //print(enumerator);
                    //print(inner);
                    //print(innerKeySelector);
                    //print(lookup);
                    //print(outerKeySelector);
                    //print(resultSelector);
                    //print(source);
                    enumerator = source.GetEnumerator();
                    lookup = Enumerable.From(inner).ToLookup(innerKeySelector, Functions.Identity, compareSelector);
                }, function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(compareSelector);
                    //print(enumerator);
                    //print(inner);
                    //print(innerKeySelector);
                    //print(lookup);
                    //print(outerKeySelector);
                    //print(resultSelector);
                    //print(source);
                    if (enumerator.MoveNext()) {
                        var innerElement = lookup.Get(outerKeySelector(enumerator.Current()));
                        return this.Yield(resultSelector(enumerator.Current(), innerElement));
                    }
                    return false;
                }, function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(compareSelector);
                    //print(enumerator);
                    //print(inner);
                    //print(innerKeySelector);
                    //print(lookup);
                    //print(outerKeySelector);
                    //print(resultSelector);
                    //print(source);
                    Utils.Dispose(enumerator);
                });
            });
        },
        All: function (predicate) {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(predicate);
            predicate = Utils.CreateLambda(predicate);
            var result = true;
            this.ForEach(function (x) {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(predicate);
                //print(result);
                //print(x);
                if (!predicate(x)) {
                    result = false;
                    return false;
                }
            });
            return result;
        },
        Any: function (predicate) {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(predicate);
            predicate = Utils.CreateLambda(predicate);
            var enumerator = this.GetEnumerator();
            try {
                if (arguments.length == 0) return enumerator.MoveNext();
                while (enumerator.MoveNext()) {
                    if (predicate(enumerator.Current())) return true;
                }
                return false;
            } finally {
                Utils.Dispose(enumerator);
            }
        },
        Concat: function (second) {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(second);
            var source = this;
            return new Enumerable(function () {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(second);
                //print(source);
                var firstEnumerator;
                var secondEnumerator;
                return new IEnumerator(function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(firstEnumerator);
                    //print(second);
                    //print(secondEnumerator);
                    //print(source);
                    firstEnumerator = source.GetEnumerator();
                }, function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(firstEnumerator);
                    //print(second);
                    //print(secondEnumerator);
                    //print(source);
                    if (secondEnumerator == null) {
                        if (firstEnumerator.MoveNext()) return this.Yield(firstEnumerator.Current());
                        secondEnumerator = Enumerable.From(second).GetEnumerator();
                    }
                    if (secondEnumerator.MoveNext()) return this.Yield(secondEnumerator.Current());
                    return false;
                }, function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(firstEnumerator);
                    //print(second);
                    //print(secondEnumerator);
                    //print(source);
                    try {
                        Utils.Dispose(firstEnumerator);
                    } finally {
                        Utils.Dispose(secondEnumerator);
                    }
                });
            });
        },
        Insert: function (index, second) {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(index);
            //print(second);
            var source = this;
            return new Enumerable(function () {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(index);
                //print(second);
                //print(source);
                var firstEnumerator;
                var secondEnumerator;
                var count = 0;
                var isEnumerated = false;
                return new IEnumerator(function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(count);
                    //print(firstEnumerator);
                    //print(index);
                    //print(isEnumerated);
                    //print(second);
                    //print(secondEnumerator);
                    //print(source);
                    firstEnumerator = source.GetEnumerator();
                    secondEnumerator = Enumerable.From(second).GetEnumerator();
                }, function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(count);
                    //print(firstEnumerator);
                    //print(index);
                    //print(isEnumerated);
                    //print(second);
                    //print(secondEnumerator);
                    //print(source);
                    if (count == index && secondEnumerator.MoveNext()) {
                        isEnumerated = true;
                        return this.Yield(secondEnumerator.Current());
                    }
                    if (firstEnumerator.MoveNext()) {
                        count++;
                        return this.Yield(firstEnumerator.Current());
                    }
                    if (!isEnumerated && secondEnumerator.MoveNext()) {
                        return this.Yield(secondEnumerator.Current());
                    }
                    return false;
                }, function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(count);
                    //print(firstEnumerator);
                    //print(index);
                    //print(isEnumerated);
                    //print(second);
                    //print(secondEnumerator);
                    //print(source);
                    try {
                        Utils.Dispose(firstEnumerator);
                    } finally {
                        Utils.Dispose(secondEnumerator);
                    }
                });
            });
        },
        Alternate: function (value) {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(value);
            value = Enumerable.Return(value);
            return this.SelectMany(function (elem) {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(elem);
                //print(value);
                return Enumerable.Return(elem).Concat(value);
            }).TakeExceptLast();
        },
        Contains: function (value, compareSelector) {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(compareSelector);
            //print(value);
            compareSelector = Utils.CreateLambda(compareSelector);
            var enumerator = this.GetEnumerator();
            try {
                while (enumerator.MoveNext()) {
                    if (compareSelector(enumerator.Current()) === value) return true;
                }
                return false;
            } finally {
                Utils.Dispose(enumerator);
            }
        },
        DefaultIfEmpty: function (defaultValue) {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(defaultValue);
            var source = this;
            return new Enumerable(function () {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(defaultValue);
                //print(source);
                var enumerator;
                var isFirst = true;
                return new IEnumerator(function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(defaultValue);
                    //print(enumerator);
                    //print(isFirst);
                    //print(source);
                    enumerator = source.GetEnumerator();
                }, function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(defaultValue);
                    //print(enumerator);
                    //print(isFirst);
                    //print(source);
                    if (enumerator.MoveNext()) {
                        isFirst = false;
                        return this.Yield(enumerator.Current());
                    } else if (isFirst) {
                        isFirst = false;
                        return this.Yield(defaultValue);
                    }
                    return false;
                }, function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(defaultValue);
                    //print(enumerator);
                    //print(isFirst);
                    //print(source);
                    Utils.Dispose(enumerator);
                });
            });
        },
        Distinct: function (compareSelector) {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(compareSelector);
            return this.Except(Enumerable.Empty(), compareSelector);
        },
        Except: function (second, compareSelector) {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(compareSelector);
            //print(second);
            compareSelector = Utils.CreateLambda(compareSelector);
            var source = this;
            return new Enumerable(function () {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(compareSelector);
                //print(second);
                //print(source);
                var enumerator;
                var keys;
                return new IEnumerator(function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(compareSelector);
                    //print(enumerator);
                    //print(keys);
                    //print(second);
                    //print(source);
                    enumerator = source.GetEnumerator();
                    keys = new Dictionary(compareSelector);
                    Enumerable.From(second).ForEach(function (key) {
                        //print(ArrayEnumerable);
                        //print(Dictionary);
                        //print(Enumerable);
                        //print(Functions);
                        //print(Grouping);
                        //print(IEnumerator);
                        //print(Lookup);
                        //print(OrderedEnumerable);
                        //print(SortContext);
                        //print(State);
                        //print(Types);
                        //print(Utils);
                        //print(Yielder);
                        //print(compareSelector);
                        //print(enumerator);
                        //print(key);
                        //print(keys);
                        //print(second);
                        //print(source);
                        keys.Add(key);
                    });
                }, function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(compareSelector);
                    //print(enumerator);
                    //print(keys);
                    //print(second);
                    //print(source);
                    while (enumerator.MoveNext()) {
                        var current = enumerator.Current();
                        if (!keys.Contains(current)) {
                            keys.Add(current);
                            return this.Yield(current);
                        }
                    }
                    return false;
                }, function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(compareSelector);
                    //print(enumerator);
                    //print(keys);
                    //print(second);
                    //print(source);
                    Utils.Dispose(enumerator);
                });
            });
        },
        Intersect: function (second, compareSelector) {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(compareSelector);
            //print(second);
            compareSelector = Utils.CreateLambda(compareSelector);
            var source = this;
            return new Enumerable(function () {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(compareSelector);
                //print(second);
                //print(source);
                var enumerator;
                var keys;
                var outs;
                return new IEnumerator(function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(compareSelector);
                    //print(enumerator);
                    //print(keys);
                    //print(outs);
                    //print(second);
                    //print(source);
                    enumerator = source.GetEnumerator();
                    keys = new Dictionary(compareSelector);
                    Enumerable.From(second).ForEach(function (key) {
                        //print(ArrayEnumerable);
                        //print(Dictionary);
                        //print(Enumerable);
                        //print(Functions);
                        //print(Grouping);
                        //print(IEnumerator);
                        //print(Lookup);
                        //print(OrderedEnumerable);
                        //print(SortContext);
                        //print(State);
                        //print(Types);
                        //print(Utils);
                        //print(Yielder);
                        //print(compareSelector);
                        //print(enumerator);
                        //print(key);
                        //print(keys);
                        //print(outs);
                        //print(second);
                        //print(source);
                        keys.Add(key);
                    });
                    outs = new Dictionary(compareSelector);
                }, function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(compareSelector);
                    //print(enumerator);
                    //print(keys);
                    //print(outs);
                    //print(second);
                    //print(source);
                    while (enumerator.MoveNext()) {
                        var current = enumerator.Current();
                        if (!outs.Contains(current) && keys.Contains(current)) {
                            outs.Add(current);
                            return this.Yield(current);
                        }
                    }
                    return false;
                }, function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(compareSelector);
                    //print(enumerator);
                    //print(keys);
                    //print(outs);
                    //print(second);
                    //print(source);
                    Utils.Dispose(enumerator);
                });
            });
        },
        SequenceEqual: function (second, compareSelector) {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(compareSelector);
            //print(second);
            compareSelector = Utils.CreateLambda(compareSelector);
            var firstEnumerator = this.GetEnumerator();
            try {
                var secondEnumerator = Enumerable.From(second).GetEnumerator();
                try {
                    while (firstEnumerator.MoveNext()) {
                        if (!secondEnumerator.MoveNext() || compareSelector(firstEnumerator.Current()) !== compareSelector(secondEnumerator.Current())) {
                            return false;
                        }
                    }
                    if (secondEnumerator.MoveNext()) return false;
                    return true;
                } finally {
                    Utils.Dispose(secondEnumerator);
                }
            } finally {
                Utils.Dispose(firstEnumerator);
            }
        },
        Union: function (second, compareSelector) {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(compareSelector);
            //print(second);
            compareSelector = Utils.CreateLambda(compareSelector);
            var source = this;
            return new Enumerable(function () {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(compareSelector);
                //print(second);
                //print(source);
                var firstEnumerator;
                var secondEnumerator;
                var keys;
                return new IEnumerator(function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(compareSelector);
                    //print(firstEnumerator);
                    //print(keys);
                    //print(second);
                    //print(secondEnumerator);
                    //print(source);
                    firstEnumerator = source.GetEnumerator();
                    keys = new Dictionary(compareSelector);
                }, function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(compareSelector);
                    //print(firstEnumerator);
                    //print(keys);
                    //print(second);
                    //print(secondEnumerator);
                    //print(source);
                    var current;
                    if (secondEnumerator === undefined) {
                        while (firstEnumerator.MoveNext()) {
                            current = firstEnumerator.Current();
                            if (!keys.Contains(current)) {
                                keys.Add(current);
                                return this.Yield(current);
                            }
                        }
                        secondEnumerator = Enumerable.From(second).GetEnumerator();
                    }
                    while (secondEnumerator.MoveNext()) {
                        current = secondEnumerator.Current();
                        if (!keys.Contains(current)) {
                            keys.Add(current);
                            return this.Yield(current);
                        }
                    }
                    return false;
                }, function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(compareSelector);
                    //print(firstEnumerator);
                    //print(keys);
                    //print(second);
                    //print(secondEnumerator);
                    //print(source);
                    try {
                        Utils.Dispose(firstEnumerator);
                    } finally {
                        Utils.Dispose(secondEnumerator);
                    }
                });
            });
        },
        OrderBy: function (keySelector) {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(keySelector);
            return new OrderedEnumerable(this, keySelector, false);
        },
        OrderByDescending: function (keySelector) {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(keySelector);
            return new OrderedEnumerable(this, keySelector, true);
        },
        Reverse: function () {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            var source = this;
            return new Enumerable(function () {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(source);
                var buffer;
                var index;
                return new IEnumerator(function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(buffer);
                    //print(index);
                    //print(source);
                    buffer = source.ToArray();
                    index = buffer.length;
                }, function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(buffer);
                    //print(index);
                    //print(source);
                    return index > 0 ? this.Yield(buffer[--index]) : false;
                }, Functions.Blank);
            });
        },
        Shuffle: function () {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            var source = this;
            return new Enumerable(function () {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(source);
                var buffer;
                return new IEnumerator(function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(buffer);
                    //print(source);
                    buffer = source.ToArray();
                }, function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(buffer);
                    //print(source);
                    if (buffer.length > 0) {
                        var i = Math.floor(Math.random() * buffer.length);
                        return this.Yield(buffer.splice(i, 1)[0]);
                    }
                    return false;
                }, Functions.Blank);
            });
        },
        GroupBy: function (keySelector, elementSelector, resultSelector, compareSelector) {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(compareSelector);
            //print(elementSelector);
            //print(keySelector);
            //print(resultSelector);
            var source = this;
            keySelector = Utils.CreateLambda(keySelector);
            elementSelector = Utils.CreateLambda(elementSelector);
            if (resultSelector != null) resultSelector = Utils.CreateLambda(resultSelector);
            compareSelector = Utils.CreateLambda(compareSelector);
            return new Enumerable(function () {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(compareSelector);
                //print(elementSelector);
                //print(keySelector);
                //print(resultSelector);
                //print(source);
                var enumerator;
                return new IEnumerator(function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(compareSelector);
                    //print(elementSelector);
                    //print(enumerator);
                    //print(keySelector);
                    //print(resultSelector);
                    //print(source);
                    enumerator = source.ToLookup(keySelector, elementSelector, compareSelector).ToEnumerable().GetEnumerator();
                }, function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(compareSelector);
                    //print(elementSelector);
                    //print(enumerator);
                    //print(keySelector);
                    //print(resultSelector);
                    //print(source);
                    while (enumerator.MoveNext()) {
                        return resultSelector == null ? this.Yield(enumerator.Current()) : this.Yield(resultSelector(enumerator.Current().Key(), enumerator.Current()));
                    }
                    return false;
                }, function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(compareSelector);
                    //print(elementSelector);
                    //print(enumerator);
                    //print(keySelector);
                    //print(resultSelector);
                    //print(source);
                    Utils.Dispose(enumerator);
                });
            });
        },
        PartitionBy: function (keySelector, elementSelector, resultSelector, compareSelector) {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(compareSelector);
            //print(elementSelector);
            //print(keySelector);
            //print(resultSelector);
            var source = this;
            keySelector = Utils.CreateLambda(keySelector);
            elementSelector = Utils.CreateLambda(elementSelector);
            compareSelector = Utils.CreateLambda(compareSelector);
            var hasResultSelector;
            if (resultSelector == null) {
                hasResultSelector = false;
                resultSelector = function (key, group) {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(compareSelector);
                    //print(elementSelector);
                    //print(group);
                    //print(hasResultSelector);
                    //print(key);
                    //print(keySelector);
                    //print(resultSelector);
                    //print(source);
                    return new Grouping(key, group);
                };
            } else {
                hasResultSelector = true;
                resultSelector = Utils.CreateLambda(resultSelector);
            }
            return new Enumerable(function () {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(compareSelector);
                //print(elementSelector);
                //print(hasResultSelector);
                //print(keySelector);
                //print(resultSelector);
                //print(source);
                var enumerator;
                var key;
                var compareKey;
                var group = [];
                return new IEnumerator(function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(compareKey);
                    //print(compareSelector);
                    //print(elementSelector);
                    //print(enumerator);
                    //print(group);
                    //print(hasResultSelector);
                    //print(key);
                    //print(keySelector);
                    //print(resultSelector);
                    //print(source);
                    enumerator = source.GetEnumerator();
                    if (enumerator.MoveNext()) {
                        key = keySelector(enumerator.Current());
                        compareKey = compareSelector(key);
                        group.push(elementSelector(enumerator.Current()));
                    }
                }, function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(compareKey);
                    //print(compareSelector);
                    //print(elementSelector);
                    //print(enumerator);
                    //print(group);
                    //print(hasResultSelector);
                    //print(key);
                    //print(keySelector);
                    //print(resultSelector);
                    //print(source);
                    var hasNext;
                    while ((hasNext = enumerator.MoveNext()) == true) {
                        if (compareKey === compareSelector(keySelector(enumerator.Current()))) {
                            group.push(elementSelector(enumerator.Current()));
                        } else break;
                    }
                    if (group.length > 0) {
                        var result = hasResultSelector ? resultSelector(key, Enumerable.From(group)) : resultSelector(key, group);
                        if (hasNext) {
                            key = keySelector(enumerator.Current());
                            compareKey = compareSelector(key);
                            group = [elementSelector(enumerator.Current())];
                        } else group = [];
                        return this.Yield(result);
                    }
                    return false;
                }, function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(compareKey);
                    //print(compareSelector);
                    //print(elementSelector);
                    //print(enumerator);
                    //print(group);
                    //print(hasResultSelector);
                    //print(key);
                    //print(keySelector);
                    //print(resultSelector);
                    //print(source);
                    Utils.Dispose(enumerator);
                });
            });
        },
        BufferWithCount: function (count) {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(count);
            var source = this;
            return new Enumerable(function () {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(count);
                //print(source);
                var enumerator;
                return new IEnumerator(function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(count);
                    //print(enumerator);
                    //print(source);
                    enumerator = source.GetEnumerator();
                }, function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(count);
                    //print(enumerator);
                    //print(source);
                    var array = [];
                    var index = 0;
                    while (enumerator.MoveNext()) {
                        array.push(enumerator.Current());
                        if (++index >= count) return this.Yield(array);
                    }
                    if (array.length > 0) return this.Yield(array);
                    return false;
                }, function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(count);
                    //print(enumerator);
                    //print(source);
                    Utils.Dispose(enumerator);
                });
            });
        },
        Aggregate: function (seed, func, resultSelector) {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(func);
            //print(resultSelector);
            //print(seed);
            return this.Scan(seed, func, resultSelector).Last();
        },
        Average: function (selector) {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(selector);
            selector = Utils.CreateLambda(selector);
            var sum = 0;
            var count = 0;
            this.ForEach(function (x) {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(count);
                //print(selector);
                //print(sum);
                //print(x);
                sum += selector(x);
                ++count;
            });
            return sum / count;
        },
        Count: function (predicate) {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(predicate);
            predicate = predicate == null ? Functions.True : Utils.CreateLambda(predicate);
            var count = 0;
            this.ForEach(function (x, i) {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(count);
                //print(i);
                //print(predicate);
                //print(x);
                if (predicate(x, i)) ++count;
            });
            return count;
        },
        Max: function (selector) {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(selector);
            if (selector == null) selector = Functions.Identity;
            return this.Select(selector).Aggregate(function (a, b) {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(a);
                //print(b);
                //print(selector);
                return a > b ? a : b;
            });
        },
        Min: function (selector) {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(selector);
            if (selector == null) selector = Functions.Identity;
            return this.Select(selector).Aggregate(function (a, b) {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(a);
                //print(b);
                //print(selector);
                return a < b ? a : b;
            });
        },
        MaxBy: function (keySelector) {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(keySelector);
            keySelector = Utils.CreateLambda(keySelector);
            return this.Aggregate(function (a, b) {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(a);
                //print(b);
                //print(keySelector);
                return keySelector(a) > keySelector(b) ? a : b;
            });
        },
        MinBy: function (keySelector) {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(keySelector);
            keySelector = Utils.CreateLambda(keySelector);
            return this.Aggregate(function (a, b) {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(a);
                //print(b);
                //print(keySelector);
                return keySelector(a) < keySelector(b) ? a : b;
            });
        },
        Sum: function (selector) {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(selector);
            if (selector == null) selector = Functions.Identity;
            return this.Select(selector).Aggregate(0, function (a, b) {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(a);
                //print(b);
                //print(selector);
                return a + b;
            });
        },
        ElementAt: function (index) {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(index);
            var value;
            var found = false;
            this.ForEach(function (x, i) {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(found);
                //print(i);
                //print(index);
                //print(value);
                //print(x);
                if (i == index) {
                    value = x;
                    found = true;
                    return false;
                }
            });
            if (!found) throw new Error("index is less than 0 or greater than or equal to the number of elements in source.");
            return value;
        },
        ElementAtOrDefault: function (index, defaultValue) {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(defaultValue);
            //print(index);
            var value;
            var found = false;
            this.ForEach(function (x, i) {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(defaultValue);
                //print(found);
                //print(i);
                //print(index);
                //print(value);
                //print(x);
                if (i == index) {
                    value = x;
                    found = true;
                    return false;
                }
            });
            return !found ? defaultValue : value;
        },
        First: function (predicate) {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(predicate);
            if (predicate != null) return this.Where(predicate).First();
            var value;
            var found = false;
            this.ForEach(function (x) {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(found);
                //print(predicate);
                //print(value);
                //print(x);
                value = x;
                found = true;
                return false;
            });
            if (!found) throw new Error("First:No element satisfies the condition.");
            return value;
        },
        FirstOrDefault: function (defaultValue, predicate) {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(defaultValue);
            //print(predicate);
            if (predicate != null) return this.Where(predicate).FirstOrDefault(defaultValue);
            var value;
            var found = false;
            this.ForEach(function (x) {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(defaultValue);
                //print(found);
                //print(predicate);
                //print(value);
                //print(x);
                value = x;
                found = true;
                return false;
            });
            return !found ? defaultValue : value;
        },
        Last: function (predicate) {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(predicate);
            if (predicate != null) return this.Where(predicate).Last();
            var value;
            var found = false;
            this.ForEach(function (x) {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(found);
                //print(predicate);
                //print(value);
                //print(x);
                found = true;
                value = x;
            });
            if (!found) throw new Error("Last:No element satisfies the condition.");
            return value;
        },
        LastOrDefault: function (defaultValue, predicate) {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(defaultValue);
            //print(predicate);
            if (predicate != null) return this.Where(predicate).LastOrDefault(defaultValue);
            var value;
            var found = false;
            this.ForEach(function (x) {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(defaultValue);
                //print(found);
                //print(predicate);
                //print(value);
                //print(x);
                found = true;
                value = x;
            });
            return !found ? defaultValue : value;
        },
        Single: function (predicate) {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(predicate);
            if (predicate != null) return this.Where(predicate).Single();
            var value;
            var found = false;
            this.ForEach(function (x) {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(found);
                //print(predicate);
                //print(value);
                //print(x);
                if (!found) {
                    found = true;
                    value = x;
                } else throw new Error("Single:sequence contains more than one element.");
            });
            if (!found) throw new Error("Single:No element satisfies the condition.");
            return value;
        },
        SingleOrDefault: function (defaultValue, predicate) {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(defaultValue);
            //print(predicate);
            if (predicate != null) return this.Where(predicate).SingleOrDefault(defaultValue);
            var value;
            var found = false;
            this.ForEach(function (x) {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(defaultValue);
                //print(found);
                //print(predicate);
                //print(value);
                //print(x);
                if (!found) {
                    found = true;
                    value = x;
                } else throw new Error("Single:sequence contains more than one element.");
            });
            return !found ? defaultValue : value;
        },
        Skip: function (count) {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(count);
            var source = this;
            return new Enumerable(function () {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(count);
                //print(source);
                var enumerator;
                var index = 0;
                return new IEnumerator(function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(count);
                    //print(enumerator);
                    //print(index);
                    //print(source);
                    enumerator = source.GetEnumerator();
                    while (index++ < count && enumerator.MoveNext()) {
                    }
                }, function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(count);
                    //print(enumerator);
                    //print(index);
                    //print(source);
                    return enumerator.MoveNext() ? this.Yield(enumerator.Current()) : false;
                }, function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(count);
                    //print(enumerator);
                    //print(index);
                    //print(source);
                    Utils.Dispose(enumerator);
                });
            });
        },
        SkipWhile: function (predicate) {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(predicate);
            predicate = Utils.CreateLambda(predicate);
            var source = this;
            return new Enumerable(function () {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(predicate);
                //print(source);
                var enumerator;
                var index = 0;
                var isSkipEnd = false;
                return new IEnumerator(function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(enumerator);
                    //print(index);
                    //print(isSkipEnd);
                    //print(predicate);
                    //print(source);
                    enumerator = source.GetEnumerator();
                }, function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(enumerator);
                    //print(index);
                    //print(isSkipEnd);
                    //print(predicate);
                    //print(source);
                    while (!isSkipEnd) {
                        if (enumerator.MoveNext()) {
                            if (!predicate(enumerator.Current(), index++)) {
                                isSkipEnd = true;
                                return this.Yield(enumerator.Current());
                            }

                        } else return false;
                    }
                    return enumerator.MoveNext() ? this.Yield(enumerator.Current()) : false;
                }, function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(enumerator);
                    //print(index);
                    //print(isSkipEnd);
                    //print(predicate);
                    //print(source);
                    Utils.Dispose(enumerator);
                });
            });
        },
        Take: function (count) {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(count);
            var source = this;
            return new Enumerable(function () {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(count);
                //print(source);
                var enumerator;
                var index = 0;
                return new IEnumerator(function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(count);
                    //print(enumerator);
                    //print(index);
                    //print(source);
                    enumerator = source.GetEnumerator();
                }, function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(count);
                    //print(enumerator);
                    //print(index);
                    //print(source);
                    return index++ < count && enumerator.MoveNext() ? this.Yield(enumerator.Current()) : false;
                }, function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(count);
                    //print(enumerator);
                    //print(index);
                    //print(source);
                    Utils.Dispose(enumerator);
                });
            });
        },
        TakeWhile: function (predicate) {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(predicate);
            predicate = Utils.CreateLambda(predicate);
            var source = this;
            return new Enumerable(function () {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(predicate);
                //print(source);
                var enumerator;
                var index = 0;
                return new IEnumerator(function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(enumerator);
                    //print(index);
                    //print(predicate);
                    //print(source);
                    enumerator = source.GetEnumerator();
                }, function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(enumerator);
                    //print(index);
                    //print(predicate);
                    //print(source);
                    return enumerator.MoveNext() && predicate(enumerator.Current(), index++) ? this.Yield(enumerator.Current()) : false;
                }, function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(enumerator);
                    //print(index);
                    //print(predicate);
                    //print(source);
                    Utils.Dispose(enumerator);
                });
            });
        },
        TakeExceptLast: function (count) {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(count);
            if (count == null) count = 1;
            var source = this;
            return new Enumerable(function () {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(count);
                //print(source);
                if (count <= 0) return source.GetEnumerator();
                var enumerator;
                var q = [];
                return new IEnumerator(function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(count);
                    //print(enumerator);
                    //print(q);
                    //print(source);
                    enumerator = source.GetEnumerator();
                }, function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(count);
                    //print(enumerator);
                    //print(q);
                    //print(source);
                    while (enumerator.MoveNext()) {
                        if (q.length == count) {
                            q.push(enumerator.Current());
                            return this.Yield(q.shift());
                        }
                        q.push(enumerator.Current());
                    }
                    return false;
                }, function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(count);
                    //print(enumerator);
                    //print(q);
                    //print(source);
                    Utils.Dispose(enumerator);
                });
            });
        },
        TakeFromLast: function (count) {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(count);
            if (count <= 0 || count == null) return Enumerable.Empty();
            var source = this;
            return new Enumerable(function () {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(count);
                //print(source);
                var sourceEnumerator;
                var enumerator;
                var q = [];
                return new IEnumerator(function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(count);
                    //print(enumerator);
                    //print(q);
                    //print(source);
                    //print(sourceEnumerator);
                    sourceEnumerator = source.GetEnumerator();
                }, function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(count);
                    //print(enumerator);
                    //print(q);
                    //print(source);
                    //print(sourceEnumerator);
                    while (sourceEnumerator.MoveNext()) {
                        if (q.length == count) q.shift();
                        q.push(sourceEnumerator.Current());
                    }
                    if (enumerator == null) {
                        enumerator = Enumerable.From(q).GetEnumerator();
                    }
                    return enumerator.MoveNext() ? this.Yield(enumerator.Current()) : false;
                }, function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(count);
                    //print(enumerator);
                    //print(q);
                    //print(source);
                    //print(sourceEnumerator);
                    Utils.Dispose(enumerator);
                });
            });
        },
        IndexOf: function (item) {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(item);
            var found = null;
            this.ForEach(function (x, i) {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(found);
                //print(i);
                //print(item);
                //print(x);
                if (x === item) {
                    found = i;
                    return true;
                }
            });
            return found !== null ? found : -1;
        },
        LastIndexOf: function (item) {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(item);
            var result = -1;
            this.ForEach(function (x, i) {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(i);
                //print(item);
                //print(result);
                //print(x);
                if (x === item) result = i;
            });
            return result;
        },
        ToArray: function () {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            var array = [];
            this.ForEach(function (x) {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(array);
                //print(x);
                array.push(x);
            });
            return array;
        },
        ToLookup: function (keySelector, elementSelector, compareSelector) {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(compareSelector);
            //print(elementSelector);
            //print(keySelector);
            keySelector = Utils.CreateLambda(keySelector);
            elementSelector = Utils.CreateLambda(elementSelector);
            compareSelector = Utils.CreateLambda(compareSelector);
            var dict = new Dictionary(compareSelector);
            this.ForEach(function (x) {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(compareSelector);
                //print(dict);
                //print(elementSelector);
                //print(keySelector);
                //print(x);
                var key = keySelector(x);
                var element = elementSelector(x);
                var array = dict.Get(key);
                if (array !== undefined) array.push(element); else dict.Add(key, [element]);
            });
            return new Lookup(dict);
        },
        ToObject: function (keySelector, elementSelector) {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(elementSelector);
            //print(keySelector);
            keySelector = Utils.CreateLambda(keySelector);
            elementSelector = Utils.CreateLambda(elementSelector);
            var obj = {};
            this.ForEach(function (x) {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(elementSelector);
                //print(keySelector);
                //print(obj);
                //print(x);
                obj[keySelector(x)] = elementSelector(x);
            });
            return obj;
        },
        ToDictionary: function (keySelector, elementSelector, compareSelector) {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(compareSelector);
            //print(elementSelector);
            //print(keySelector);
            keySelector = Utils.CreateLambda(keySelector);
            elementSelector = Utils.CreateLambda(elementSelector);
            compareSelector = Utils.CreateLambda(compareSelector);
            var dict = new Dictionary(compareSelector);
            this.ForEach(function (x) {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(compareSelector);
                //print(dict);
                //print(elementSelector);
                //print(keySelector);
                //print(x);
                dict.Add(keySelector(x), elementSelector(x));
            });
            return dict;
        },
        ToJSON: function (replacer, space) {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(replacer);
            //print(space);
            return JSON.stringify(this.ToArray(), replacer, space);
        },
        ToString: function (separator, selector) {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(selector);
            //print(separator);
            if (separator == null) separator = "";
            if (selector == null) selector = Functions.Identity;
            return this.Select(selector).ToArray().join(separator);
        },
        Do: function (action) {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(action);
            var source = this;
            action = Utils.CreateLambda(action);
            return new Enumerable(function () {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(action);
                //print(source);
                var enumerator;
                var index = 0;
                return new IEnumerator(function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(action);
                    //print(enumerator);
                    //print(index);
                    //print(source);
                    enumerator = source.GetEnumerator();
                }, function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(action);
                    //print(enumerator);
                    //print(index);
                    //print(source);
                    if (enumerator.MoveNext()) {
                        action(enumerator.Current(), index++);
                        return this.Yield(enumerator.Current());
                    }
                    return false;
                }, function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(action);
                    //print(enumerator);
                    //print(index);
                    //print(source);
                    Utils.Dispose(enumerator);
                });
            });
        },
        ForEach: function (action) {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(action);
            action = Utils.CreateLambda(action);
            var index = 0;
            var enumerator = this.GetEnumerator();
            try {
                while (enumerator.MoveNext()) {
                    if (action(enumerator.Current(), index++) === false) break;
                }
            } finally {
                Utils.Dispose(enumerator);
            }
        },
        Write: function (separator, selector) {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(selector);
            //print(separator);
            if (separator == null) separator = "";
            selector = Utils.CreateLambda(selector);
            var isFirst = true;
            this.ForEach(function (item) {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(isFirst);
                //print(item);
                //print(selector);
                //print(separator);
                if (isFirst) isFirst = false; else //print(separator);
                print(selector(item));
            });
        },
        WriteLine: function (selector) {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(selector);
            selector = Utils.CreateLambda(selector);
            this.ForEach(function (item) {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(item);
                //print(selector);
                print(selector(item));
                print("<br />");
            });
        },
        Force: function () {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            var enumerator = this.GetEnumerator();
            try {
                while (enumerator.MoveNext()) {
                }
            } finally {
                Utils.Dispose(enumerator);
            }
        },
        Let: function (func) {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(func);
            func = Utils.CreateLambda(func);
            var source = this;
            return new Enumerable(function () {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(func);
                //print(source);
                var enumerator;
                return new IEnumerator(function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(enumerator);
                    //print(func);
                    //print(source);
                    enumerator = Enumerable.From(func(source)).GetEnumerator();
                }, function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(enumerator);
                    //print(func);
                    //print(source);
                    return enumerator.MoveNext() ? this.Yield(enumerator.Current()) : false;
                }, function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(enumerator);
                    //print(func);
                    //print(source);
                    Utils.Dispose(enumerator);
                });
            });
        },
        Share: function () {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            var source = this;
            var sharedEnumerator;
            return new Enumerable(function () {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(sharedEnumerator);
                //print(source);
                return new IEnumerator(function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(sharedEnumerator);
                    //print(source);
                    if (sharedEnumerator == null) {
                        sharedEnumerator = source.GetEnumerator();
                    }
                }, function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(sharedEnumerator);
                    //print(source);
                    return sharedEnumerator.MoveNext() ? this.Yield(sharedEnumerator.Current()) : false;
                }, Functions.Blank);
            });
        },
        MemoizeAll: function () {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            var source = this;
            var cache;
            var enumerator;
            return new Enumerable(function () {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(cache);
                //print(enumerator);
                //print(source);
                var index = -1;
                return new IEnumerator(function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(cache);
                    //print(enumerator);
                    //print(index);
                    //print(source);
                    if (enumerator == null) {
                        enumerator = source.GetEnumerator();
                        cache = [];
                    }
                }, function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(cache);
                    //print(enumerator);
                    //print(index);
                    //print(source);
                    index++;
                    if (cache.length <= index) {
                        return enumerator.MoveNext() ? this.Yield(cache[index] = enumerator.Current()) : false;
                    }
                    return this.Yield(cache[index]);
                }, Functions.Blank);
            });
        },
        Catch: function (handler) {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(handler);
            handler = Utils.CreateLambda(handler);
            var source = this;
            return new Enumerable(function () {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(handler);
                //print(source);
                var enumerator;
                return new IEnumerator(function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(enumerator);
                    //print(handler);
                    //print(source);
                    enumerator = source.GetEnumerator();
                }, function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(enumerator);
                    //print(handler);
                    //print(source);
                    try {
                        return enumerator.MoveNext() ? this.Yield(enumerator.Current()) : false;
                    } catch (e) {
                        handler(e);
                        return false;
                    }
                }, function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(enumerator);
                    //print(handler);
                    //print(source);
                    Utils.Dispose(enumerator);
                });
            });
        },
        Finally: function (finallyAction) {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(finallyAction);
            finallyAction = Utils.CreateLambda(finallyAction);
            var source = this;
            return new Enumerable(function () {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(finallyAction);
                //print(source);
                var enumerator;
                return new IEnumerator(function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(enumerator);
                    //print(finallyAction);
                    //print(source);
                    enumerator = source.GetEnumerator();
                }, function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(enumerator);
                    //print(finallyAction);
                    //print(source);
                    return enumerator.MoveNext() ? this.Yield(enumerator.Current()) : false;
                }, function () {
                    //print(ArrayEnumerable);
                    //print(Dictionary);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(enumerator);
                    //print(finallyAction);
                    //print(source);
                    try {
                        Utils.Dispose(enumerator);
                    } finally {
                        finallyAction();
                    }
                });
            });
        },
        Trace: function (message, selector) {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(message);
            //print(selector);
            if (message == null) message = "Trace";
            selector = Utils.CreateLambda(selector);
            return this.Do(function (item) {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(item);
                //print(message);
                //print(selector);
                print(message + ":" + selector(item));
            });
        }
    };
    var Functions = {
        Identity: function (x) {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(x);
            return x;
        },
        True: function () {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            return true;
        },
        Blank: function () {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
        }
    };
    var Types = {
        Boolean: typeof true,
        Number: typeof 0,
        String: typeof "",
        Object: typeof {},
        Undefined: typeof undefined,
        Function: typeof function () {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
        }
    };
    var Utils = {
        CreateLambda: function (expression) {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(expression);
            if (!expression) return Functions.Identity;
            return expression;
        },
        IsIEnumerable: function (obj) {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(obj);
            if (typeof Enumerator != Types.Undefined) {
                try {
                    new Enumerator(obj);
                    return true;
                } catch (e) {
                }
            }
            return false;
        },
        Compare: function (a, b) {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(a);
            //print(b);
            return a === b ? 0 : a > b ? 1 : -1;
        },
        Dispose: function (obj) {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(obj);
            if (obj != null) obj.Dispose();
        }
    };
    var State = {
        Before: 0,
        Running: 1,
        After: 2
    };
    var IEnumerator = function (initialize, tryGetNext, dispose) {
        //print(ArrayEnumerable);
        //print(Dictionary);
        //print(Enumerable);
        //print(Functions);
        //print(Grouping);
        //print(IEnumerator);
        //print(Lookup);
        //print(OrderedEnumerable);
        //print(SortContext);
        //print(State);
        //print(Types);
        //print(Utils);
        //print(Yielder);
        //print(dispose);
        //print(initialize);
        //print(tryGetNext);
        var yielder = new Yielder();
        var state = State.Before;
        this.Current = yielder.Current;
        this.MoveNext = function () {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(dispose);
            //print(initialize);
            //print(state);
            //print(tryGetNext);
            //print(yielder);
            try {
                switch (state) {
                    case State.Before:
                        state = State.Running;
                        initialize();

                    case State.Running:
                        if (tryGetNext.apply(yielder)) {
                            return true;
                        } else {
                            this.Dispose();
                            return false;
                        }

                    case State.After:
                        return false;
                }
            } catch (e) {
                this.Dispose();
                throw e;
            }
        };
        this.Dispose = function () {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(dispose);
            //print(initialize);
            //print(state);
            //print(tryGetNext);
            //print(yielder);
            if (state != State.Running) return;
            try {
                dispose();
            } finally {
                state = State.After;
            }
        };
    };
    var Yielder = function () {
        //print(ArrayEnumerable);
        //print(Dictionary);
        //print(Enumerable);
        //print(Functions);
        //print(Grouping);
        //print(IEnumerator);
        //print(Lookup);
        //print(OrderedEnumerable);
        //print(SortContext);
        //print(State);
        //print(Types);
        //print(Utils);
        //print(Yielder);
        var current = null;
        this.Current = function () {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(current);
            return current;
        };
        this.Yield = function (value) {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(current);
            //print(value);
            current = value;
            return true;
        };
    };
    var OrderedEnumerable = function (source, keySelector, descending, parent) {
        //print(ArrayEnumerable);
        //print(Dictionary);
        //print(Enumerable);
        //print(Functions);
        //print(Grouping);
        //print(IEnumerator);
        //print(Lookup);
        //print(OrderedEnumerable);
        //print(SortContext);
        //print(State);
        //print(Types);
        //print(Utils);
        //print(Yielder);
        //print(descending);
        //print(keySelector);
        //print(parent);
        //print(source);
        this.source = source;
        this.keySelector = Utils.CreateLambda(keySelector);
        this.descending = descending;
        this.parent = parent;
    };
    OrderedEnumerable.prototype = new Enumerable();
    OrderedEnumerable.prototype.CreateOrderedEnumerable = function (keySelector, descending) {
        //print(ArrayEnumerable);
        //print(Dictionary);
        //print(Enumerable);
        //print(Functions);
        //print(Grouping);
        //print(IEnumerator);
        //print(Lookup);
        //print(OrderedEnumerable);
        //print(SortContext);
        //print(State);
        //print(Types);
        //print(Utils);
        //print(Yielder);
        //print(descending);
        //print(keySelector);
        return new OrderedEnumerable(this.source, keySelector, descending, this);
    };
    OrderedEnumerable.prototype.ThenBy = function (keySelector) {
        //print(ArrayEnumerable);
        //print(Dictionary);
        //print(Enumerable);
        //print(Functions);
        //print(Grouping);
        //print(IEnumerator);
        //print(Lookup);
        //print(OrderedEnumerable);
        //print(SortContext);
        //print(State);
        //print(Types);
        //print(Utils);
        //print(Yielder);
        //print(keySelector);
        return this.CreateOrderedEnumerable(keySelector, false);
    };
    OrderedEnumerable.prototype.ThenByDescending = function (keySelector) {
        //print(ArrayEnumerable);
        //print(Dictionary);
        //print(Enumerable);
        //print(Functions);
        //print(Grouping);
        //print(IEnumerator);
        //print(Lookup);
        //print(OrderedEnumerable);
        //print(SortContext);
        //print(State);
        //print(Types);
        //print(Utils);
        //print(Yielder);
        //print(keySelector);
        return this.CreateOrderedEnumerable(keySelector, true);
    };
    OrderedEnumerable.prototype.GetEnumerator = function () {
        //print(ArrayEnumerable);
        //print(Dictionary);
        //print(Enumerable);
        //print(Functions);
        //print(Grouping);
        //print(IEnumerator);
        //print(Lookup);
        //print(OrderedEnumerable);
        //print(SortContext);
        //print(State);
        //print(Types);
        //print(Utils);
        //print(Yielder);
        var self = this;
        var buffer;
        var indexes;
        var index = 0;
        return new IEnumerator(function () {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(buffer);
            //print(index);
            //print(indexes);
            //print(self);
            buffer = [];
            indexes = [];
            self.source.ForEach(function (item, index) {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(buffer);
                //print(index);
                //print(indexes);
                //print(item);
                //print(self);
                //print(sortContext);
                buffer.push(item);
                indexes.push(index);
            });
            var sortContext = SortContext.Create(self, null);
            sortContext.GenerateKeys(buffer);
            indexes.sort(function (a, b) {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(a);
                //print(b);
                //print(buffer);
                //print(index);
                //print(indexes);
                //print(self);
                //print(sortContext);
                return sortContext.Compare(a, b);
            });
        }, function () {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(buffer);
            //print(index);
            //print(indexes);
            //print(self);
            return index < indexes.length ? this.Yield(buffer[indexes[index++]]) : false;
        }, Functions.Blank);
    };
    var SortContext = function (keySelector, descending, child) {
        //print(ArrayEnumerable);
        //print(Dictionary);
        //print(Enumerable);
        //print(Functions);
        //print(Grouping);
        //print(IEnumerator);
        //print(Lookup);
        //print(OrderedEnumerable);
        //print(SortContext);
        //print(State);
        //print(Types);
        //print(Utils);
        //print(Yielder);
        //print(child);
        //print(descending);
        //print(keySelector);
        this.keySelector = keySelector;
        this.descending = descending;
        this.child = child;
        this.keys = null;
    };
    SortContext.Create = function (orderedEnumerable, currentContext) {
        //print(ArrayEnumerable);
        //print(Dictionary);
        //print(Enumerable);
        //print(Functions);
        //print(Grouping);
        //print(IEnumerator);
        //print(Lookup);
        //print(OrderedEnumerable);
        //print(SortContext);
        //print(State);
        //print(Types);
        //print(Utils);
        //print(Yielder);
        //print(currentContext);
        //print(orderedEnumerable);
        var context = new SortContext(orderedEnumerable.keySelector, orderedEnumerable.descending, currentContext);
        if (orderedEnumerable.parent != null) return SortContext.Create(orderedEnumerable.parent, context);
        return context;
    };
    SortContext.prototype.GenerateKeys = function (source) {
        //print(ArrayEnumerable);
        //print(Dictionary);
        //print(Enumerable);
        //print(Functions);
        //print(Grouping);
        //print(IEnumerator);
        //print(Lookup);
        //print(OrderedEnumerable);
        //print(SortContext);
        //print(State);
        //print(Types);
        //print(Utils);
        //print(Yielder);
        //print(source);
        var len = source.length;
        var keySelector = this.keySelector;
        var keys = new Array(len);
        for (var i = 0; i < len; i++) keys[i] = keySelector(source[i]);
        this.keys = keys;
        if (this.child != null) this.child.GenerateKeys(source);
    };
    SortContext.prototype.Compare = function (index1, index2) {
        //print(ArrayEnumerable);
        //print(Dictionary);
        //print(Enumerable);
        //print(Functions);
        //print(Grouping);
        //print(IEnumerator);
        //print(Lookup);
        //print(OrderedEnumerable);
        //print(SortContext);
        //print(State);
        //print(Types);
        //print(Utils);
        //print(Yielder);
        //print(index1);
        //print(index2);
        var comparison = Utils.Compare(this.keys[index1], this.keys[index2]);
        if (comparison == 0) {
            if (this.child != null) return this.child.Compare(index1, index2);
            comparison = Utils.Compare(index1, index2);
        }
        return this.descending ? -comparison : comparison;
    };
    var ArrayEnumerable = function (source) {
        //print(ArrayEnumerable);
        //print(Dictionary);
        //print(Enumerable);
        //print(Functions);
        //print(Grouping);
        //print(IEnumerator);
        //print(Lookup);
        //print(OrderedEnumerable);
        //print(SortContext);
        //print(State);
        //print(Types);
        //print(Utils);
        //print(Yielder);
        //print(source);
        this.source = source;
    };
    ArrayEnumerable.prototype = new Enumerable();
    ArrayEnumerable.prototype.Any = function (predicate) {
        //print(ArrayEnumerable);
        //print(Dictionary);
        //print(Enumerable);
        //print(Functions);
        //print(Grouping);
        //print(IEnumerator);
        //print(Lookup);
        //print(OrderedEnumerable);
        //print(SortContext);
        //print(State);
        //print(Types);
        //print(Utils);
        //print(Yielder);
        //print(predicate);
        return predicate == null ? this.source.length > 0 : Enumerable.prototype.Any.apply(this, arguments);
    };
    ArrayEnumerable.prototype.Count = function (predicate) {
        //print(ArrayEnumerable);
        //print(Dictionary);
        //print(Enumerable);
        //print(Functions);
        //print(Grouping);
        //print(IEnumerator);
        //print(Lookup);
        //print(OrderedEnumerable);
        //print(SortContext);
        //print(State);
        //print(Types);
        //print(Utils);
        //print(Yielder);
        //print(predicate);
        return predicate == null ? this.source.length : Enumerable.prototype.Count.apply(this, arguments);
    };
    ArrayEnumerable.prototype.ElementAt = function (index) {
        //print(ArrayEnumerable);
        //print(Dictionary);
        //print(Enumerable);
        //print(Functions);
        //print(Grouping);
        //print(IEnumerator);
        //print(Lookup);
        //print(OrderedEnumerable);
        //print(SortContext);
        //print(State);
        //print(Types);
        //print(Utils);
        //print(Yielder);
        //print(index);
        return 0 <= index && index < this.source.length ? this.source[index] : Enumerable.prototype.ElementAt.apply(this, arguments);
    };
    ArrayEnumerable.prototype.ElementAtOrDefault = function (index, defaultValue) {
        //print(ArrayEnumerable);
        //print(Dictionary);
        //print(Enumerable);
        //print(Functions);
        //print(Grouping);
        //print(IEnumerator);
        //print(Lookup);
        //print(OrderedEnumerable);
        //print(SortContext);
        //print(State);
        //print(Types);
        //print(Utils);
        //print(Yielder);
        //print(defaultValue);
        //print(index);
        return 0 <= index && index < this.source.length ? this.source[index] : defaultValue;
    };
    ArrayEnumerable.prototype.First = function (predicate) {
        //print(ArrayEnumerable);
        //print(Dictionary);
        //print(Enumerable);
        //print(Functions);
        //print(Grouping);
        //print(IEnumerator);
        //print(Lookup);
        //print(OrderedEnumerable);
        //print(SortContext);
        //print(State);
        //print(Types);
        //print(Utils);
        //print(Yielder);
        //print(predicate);
        return predicate == null && this.source.length > 0 ? this.source[0] : Enumerable.prototype.First.apply(this, arguments);
    };
    ArrayEnumerable.prototype.FirstOrDefault = function (defaultValue, predicate) {
        //print(ArrayEnumerable);
        //print(Dictionary);
        //print(Enumerable);
        //print(Functions);
        //print(Grouping);
        //print(IEnumerator);
        //print(Lookup);
        //print(OrderedEnumerable);
        //print(SortContext);
        //print(State);
        //print(Types);
        //print(Utils);
        //print(Yielder);
        //print(defaultValue);
        //print(predicate);
        if (predicate != null) {
            return Enumerable.prototype.FirstOrDefault.apply(this, arguments);
        }
        return this.source.length > 0 ? this.source[0] : defaultValue;
    };
    ArrayEnumerable.prototype.Last = function (predicate) {
        //print(ArrayEnumerable);
        //print(Dictionary);
        //print(Enumerable);
        //print(Functions);
        //print(Grouping);
        //print(IEnumerator);
        //print(Lookup);
        //print(OrderedEnumerable);
        //print(SortContext);
        //print(State);
        //print(Types);
        //print(Utils);
        //print(Yielder);
        //print(predicate);
        return predicate == null && this.source.length > 0 ? this.source[this.source.length - 1] : Enumerable.prototype.Last.apply(this, arguments);
    };
    ArrayEnumerable.prototype.LastOrDefault = function (defaultValue, predicate) {
        //print(ArrayEnumerable);
        //print(Dictionary);
        //print(Enumerable);
        //print(Functions);
        //print(Grouping);
        //print(IEnumerator);
        //print(Lookup);
        //print(OrderedEnumerable);
        //print(SortContext);
        //print(State);
        //print(Types);
        //print(Utils);
        //print(Yielder);
        //print(defaultValue);
        //print(predicate);
        if (predicate != null) {
            return Enumerable.prototype.LastOrDefault.apply(this, arguments);
        }
        return this.source.length > 0 ? this.source[this.source.length - 1] : defaultValue;
    };
    ArrayEnumerable.prototype.Skip = function (count) {
        //print(ArrayEnumerable);
        //print(Dictionary);
        //print(Enumerable);
        //print(Functions);
        //print(Grouping);
        //print(IEnumerator);
        //print(Lookup);
        //print(OrderedEnumerable);
        //print(SortContext);
        //print(State);
        //print(Types);
        //print(Utils);
        //print(Yielder);
        //print(count);
        var source = this.source;
        return new Enumerable(function () {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(count);
            //print(source);
            var index;
            return new IEnumerator(function () {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(count);
                //print(index);
                //print(source);
                index = count < 0 ? 0 : count;
            }, function () {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(count);
                //print(index);
                //print(source);
                return index < source.length ? this.Yield(source[index++]) : false;
            }, Functions.Blank);
        });
    };
    ArrayEnumerable.prototype.TakeExceptLast = function (count) {
        //print(ArrayEnumerable);
        //print(Dictionary);
        //print(Enumerable);
        //print(Functions);
        //print(Grouping);
        //print(IEnumerator);
        //print(Lookup);
        //print(OrderedEnumerable);
        //print(SortContext);
        //print(State);
        //print(Types);
        //print(Utils);
        //print(Yielder);
        //print(count);
        if (count == null) count = 1;
        return this.Take(this.source.length - count);
    };
    ArrayEnumerable.prototype.TakeFromLast = function (count) {
        //print(ArrayEnumerable);
        //print(Dictionary);
        //print(Enumerable);
        //print(Functions);
        //print(Grouping);
        //print(IEnumerator);
        //print(Lookup);
        //print(OrderedEnumerable);
        //print(SortContext);
        //print(State);
        //print(Types);
        //print(Utils);
        //print(Yielder);
        //print(count);
        return this.Skip(this.source.length - count);
    };
    ArrayEnumerable.prototype.Reverse = function () {
        //print(ArrayEnumerable);
        //print(Dictionary);
        //print(Enumerable);
        //print(Functions);
        //print(Grouping);
        //print(IEnumerator);
        //print(Lookup);
        //print(OrderedEnumerable);
        //print(SortContext);
        //print(State);
        //print(Types);
        //print(Utils);
        //print(Yielder);
        var source = this.source;
        return new Enumerable(function () {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(source);
            var index;
            return new IEnumerator(function () {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(index);
                //print(source);
                index = source.length;
            }, function () {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(index);
                //print(source);
                return index > 0 ? this.Yield(source[--index]) : false;
            }, Functions.Blank);
        });
    };
    ArrayEnumerable.prototype.SequenceEqual = function (second, compareSelector) {
        //print(ArrayEnumerable);
        //print(Dictionary);
        //print(Enumerable);
        //print(Functions);
        //print(Grouping);
        //print(IEnumerator);
        //print(Lookup);
        //print(OrderedEnumerable);
        //print(SortContext);
        //print(State);
        //print(Types);
        //print(Utils);
        //print(Yielder);
        //print(compareSelector);
        //print(second);
        if ((second instanceof ArrayEnumerable || second instanceof Array) && compareSelector == null && Enumerable.From(second).Count() != this.Count()) {
            return false;
        }
        return Enumerable.prototype.SequenceEqual.apply(this, arguments);
    };
    ArrayEnumerable.prototype.ToString = function (separator, selector) {
        //print(ArrayEnumerable);
        //print(Dictionary);
        //print(Enumerable);
        //print(Functions);
        //print(Grouping);
        //print(IEnumerator);
        //print(Lookup);
        //print(OrderedEnumerable);
        //print(SortContext);
        //print(State);
        //print(Types);
        //print(Utils);
        //print(Yielder);
        //print(selector);
        //print(separator);
        if (selector != null || !(this.source instanceof Array)) {
            return Enumerable.prototype.ToString.apply(this, arguments);
        }
        if (separator == null) separator = "";
        return this.source.join(separator);
    };
    ArrayEnumerable.prototype.GetEnumerator = function () {
        //print(ArrayEnumerable);
        //print(Dictionary);
        //print(Enumerable);
        //print(Functions);
        //print(Grouping);
        //print(IEnumerator);
        //print(Lookup);
        //print(OrderedEnumerable);
        //print(SortContext);
        //print(State);
        //print(Types);
        //print(Utils);
        //print(Yielder);
        var source = this.source;
        var index = 0;
        return new IEnumerator(Functions.Blank, function () {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(index);
            //print(source);
            return index < source.length ? this.Yield(source[index++]) : false;
        }, Functions.Blank);
    };
    var Dictionary = function () {
        //print(ArrayEnumerable);
        //print(Dictionary);
        //print(Enumerable);
        //print(Functions);
        //print(Grouping);
        //print(IEnumerator);
        //print(Lookup);
        //print(OrderedEnumerable);
        //print(SortContext);
        //print(State);
        //print(Types);
        //print(Utils);
        //print(Yielder);
        var HasOwnProperty = function (target, key) {
            //print(ArrayEnumerable);
            //print(ComputeHashCode);
            //print(Dictionary);
            //print(EntryList);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(HasOwnProperty);
            //print(HashEntry);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(key);
            //print(target);
            return Object.prototype.hasOwnProperty.call(target, key);
        };
        var ComputeHashCode = function (obj) {
            //print(ArrayEnumerable);
            //print(ComputeHashCode);
            //print(Dictionary);
            //print(EntryList);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(HasOwnProperty);
            //print(HashEntry);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(obj);
            if (obj === null) return "null";
            if (obj === undefined) return "undefined";
            return typeof obj.toString === Types.Function ? obj.toString() : Object.prototype.toString.call(obj);
        };
        var HashEntry = function (key, value) {
            //print(ArrayEnumerable);
            //print(ComputeHashCode);
            //print(Dictionary);
            //print(EntryList);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(HasOwnProperty);
            //print(HashEntry);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(key);
            //print(value);
            this.Key = key;
            this.Value = value;
            this.Prev = null;
            this.Next = null;
        };
        var EntryList = function () {
            //print(ArrayEnumerable);
            //print(ComputeHashCode);
            //print(Dictionary);
            //print(EntryList);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(HasOwnProperty);
            //print(HashEntry);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            this.First = null;
            this.Last = null;
        };
        EntryList.prototype = {
            AddLast: function (entry) {
                //print(ArrayEnumerable);
                //print(ComputeHashCode);
                //print(Dictionary);
                //print(EntryList);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(HasOwnProperty);
                //print(HashEntry);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(entry);
                if (this.Last != null) {
                    this.Last.Next = entry;
                    entry.Prev = this.Last;
                    this.Last = entry;
                } else this.First = this.Last = entry;
            },
            Replace: function (entry, newEntry) {
                //print(ArrayEnumerable);
                //print(ComputeHashCode);
                //print(Dictionary);
                //print(EntryList);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(HasOwnProperty);
                //print(HashEntry);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(entry);
                //print(newEntry);
                if (entry.Prev != null) {
                    entry.Prev.Next = newEntry;
                    newEntry.Prev = entry.Prev;
                } else this.First = newEntry;
                if (entry.Next != null) {
                    entry.Next.Prev = newEntry;
                    newEntry.Next = entry.Next;
                } else this.Last = newEntry;
            },
            Remove: function (entry) {
                //print(ArrayEnumerable);
                //print(ComputeHashCode);
                //print(Dictionary);
                //print(EntryList);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(HasOwnProperty);
                //print(HashEntry);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(entry);
                if (entry.Prev != null) entry.Prev.Next = entry.Next; else this.First = entry.Next;
                if (entry.Next != null) entry.Next.Prev = entry.Prev; else this.Last = entry.Prev;
            }
        };
        var Dictionary = function (compareSelector) {
            //print(ArrayEnumerable);
            //print(ComputeHashCode);
            //print(Dictionary);
            //print(EntryList);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(HasOwnProperty);
            //print(HashEntry);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(compareSelector);
            this.count = 0;
            this.entryList = new EntryList();
            this.buckets = {};
            this.compareSelector = compareSelector == null ? Functions.Identity : compareSelector;
        };
        Dictionary.prototype = {
            Add: function (key, value) {
                //print(ArrayEnumerable);
                //print(ComputeHashCode);
                //print(Dictionary);
                //print(EntryList);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(HasOwnProperty);
                //print(HashEntry);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(key);
                //print(value);
                var compareKey = this.compareSelector(key);
                var hash = ComputeHashCode(compareKey);
                var entry = new HashEntry(key, value);
                if (HasOwnProperty(this.buckets, hash)) {
                    var array = this.buckets[hash];
                    for (var i = 0; i < array.length; i++) {
                        if (this.compareSelector(array[i].Key) === compareKey) {
                            this.entryList.Replace(array[i], entry);
                            array[i] = entry;
                            return;
                        }
                    }
                    array.push(entry);
                } else {
                    this.buckets[hash] = [entry];
                }
                this.count++;
                this.entryList.AddLast(entry);
            },
            Get: function (key) {
                //print(ArrayEnumerable);
                //print(ComputeHashCode);
                //print(Dictionary);
                //print(EntryList);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(HasOwnProperty);
                //print(HashEntry);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(key);
                var compareKey = this.compareSelector(key);
                var hash = ComputeHashCode(compareKey);
                if (!HasOwnProperty(this.buckets, hash)) return undefined;
                var array = this.buckets[hash];
                for (var i = 0; i < array.length; i++) {
                    var entry = array[i];
                    if (this.compareSelector(entry.Key) === compareKey) return entry.Value;
                }
                return undefined;
            },
            Set: function (key, value) {
                //print(ArrayEnumerable);
                //print(ComputeHashCode);
                //print(Dictionary);
                //print(EntryList);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(HasOwnProperty);
                //print(HashEntry);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(key);
                //print(value);
                var compareKey = this.compareSelector(key);
                var hash = ComputeHashCode(compareKey);
                if (HasOwnProperty(this.buckets, hash)) {
                    var array = this.buckets[hash];
                    for (var i = 0; i < array.length; i++) {
                        if (this.compareSelector(array[i].Key) === compareKey) {
                            var newEntry = new HashEntry(key, value);
                            this.entryList.Replace(array[i], newEntry);
                            array[i] = newEntry;
                            return true;
                        }
                    }
                }
                return false;
            },
            Contains: function (key) {
                //print(ArrayEnumerable);
                //print(ComputeHashCode);
                //print(Dictionary);
                //print(EntryList);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(HasOwnProperty);
                //print(HashEntry);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(key);
                var compareKey = this.compareSelector(key);
                var hash = ComputeHashCode(compareKey);
                if (!HasOwnProperty(this.buckets, hash)) return false;
                var array = this.buckets[hash];
                for (var i = 0; i < array.length; i++) {
                    if (this.compareSelector(array[i].Key) === compareKey) return true;
                }
                return false;
            },
            Clear: function () {
                //print(ArrayEnumerable);
                //print(ComputeHashCode);
                //print(Dictionary);
                //print(EntryList);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(HasOwnProperty);
                //print(HashEntry);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                this.count = 0;
                this.buckets = {};
                this.entryList = new EntryList();
            },
            Remove: function (key) {
                //print(ArrayEnumerable);
                //print(ComputeHashCode);
                //print(Dictionary);
                //print(EntryList);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(HasOwnProperty);
                //print(HashEntry);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(key);
                var compareKey = this.compareSelector(key);
                var hash = ComputeHashCode(compareKey);
                if (!HasOwnProperty(this.buckets, hash)) return;
                var array = this.buckets[hash];
                for (var i = 0; i < array.length; i++) {
                    if (this.compareSelector(array[i].Key) === compareKey) {
                        this.entryList.Remove(array[i]);
                        array.splice(i, 1);
                        if (array.length == 0) delete this.buckets[hash];
                        this.count--;
                        return;
                    }
                }
            },
            Count: function () {
                //print(ArrayEnumerable);
                //print(ComputeHashCode);
                //print(Dictionary);
                //print(EntryList);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(HasOwnProperty);
                //print(HashEntry);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                return this.count;
            },
            ToEnumerable: function () {
                //print(ArrayEnumerable);
                //print(ComputeHashCode);
                //print(Dictionary);
                //print(EntryList);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(HasOwnProperty);
                //print(HashEntry);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                var self = this;
                return new Enumerable(function () {
                    //print(ArrayEnumerable);
                    //print(ComputeHashCode);
                    //print(Dictionary);
                    //print(EntryList);
                    //print(Enumerable);
                    //print(Functions);
                    //print(Grouping);
                    //print(HasOwnProperty);
                    //print(HashEntry);
                    //print(IEnumerator);
                    //print(Lookup);
                    //print(OrderedEnumerable);
                    //print(SortContext);
                    //print(State);
                    //print(Types);
                    //print(Utils);
                    //print(Yielder);
                    //print(self);
                    var currentEntry;
                    return new IEnumerator(function () {
                        //print(ArrayEnumerable);
                        //print(ComputeHashCode);
                        //print(Dictionary);
                        //print(EntryList);
                        //print(Enumerable);
                        //print(Functions);
                        //print(Grouping);
                        //print(HasOwnProperty);
                        //print(HashEntry);
                        //print(IEnumerator);
                        //print(Lookup);
                        //print(OrderedEnumerable);
                        //print(SortContext);
                        //print(State);
                        //print(Types);
                        //print(Utils);
                        //print(Yielder);
                        //print(currentEntry);
                        //print(self);
                        currentEntry = self.entryList.First;
                    }, function () {
                        //print(ArrayEnumerable);
                        //print(ComputeHashCode);
                        //print(Dictionary);
                        //print(EntryList);
                        //print(Enumerable);
                        //print(Functions);
                        //print(Grouping);
                        //print(HasOwnProperty);
                        //print(HashEntry);
                        //print(IEnumerator);
                        //print(Lookup);
                        //print(OrderedEnumerable);
                        //print(SortContext);
                        //print(State);
                        //print(Types);
                        //print(Utils);
                        //print(Yielder);
                        //print(currentEntry);
                        //print(self);
                        if (currentEntry != null) {
                            var result = {
                                Key: currentEntry.Key,
                                Value: currentEntry.Value
                            };
                            currentEntry = currentEntry.Next;
                            return this.Yield(result);
                        }
                        return false;
                    }, Functions.Blank);
                });
            }
        };
        return Dictionary;
    }();
    var Lookup = function (dictionary) {
        //print(ArrayEnumerable);
        //print(Dictionary);
        //print(Enumerable);
        //print(Functions);
        //print(Grouping);
        //print(IEnumerator);
        //print(Lookup);
        //print(OrderedEnumerable);
        //print(SortContext);
        //print(State);
        //print(Types);
        //print(Utils);
        //print(Yielder);
        //print(dictionary);
        this.Count = function () {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(dictionary);
            return dictionary.Count();
        };
        this.Get = function (key) {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(dictionary);
            //print(key);
            return Enumerable.From(dictionary.Get(key));
        };
        this.Contains = function (key) {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(dictionary);
            //print(key);
            return dictionary.Contains(key);
        };
        this.ToEnumerable = function () {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(dictionary);
            return dictionary.ToEnumerable().Select(function (kvp) {
                //print(ArrayEnumerable);
                //print(Dictionary);
                //print(Enumerable);
                //print(Functions);
                //print(Grouping);
                //print(IEnumerator);
                //print(Lookup);
                //print(OrderedEnumerable);
                //print(SortContext);
                //print(State);
                //print(Types);
                //print(Utils);
                //print(Yielder);
                //print(dictionary);
                //print(kvp);
                return new Grouping(kvp.Key, kvp.Value);
            });
        };
    };
    var Grouping = function (key, elements) {
        //print(ArrayEnumerable);
        //print(Dictionary);
        //print(Enumerable);
        //print(Functions);
        //print(Grouping);
        //print(IEnumerator);
        //print(Lookup);
        //print(OrderedEnumerable);
        //print(SortContext);
        //print(State);
        //print(Types);
        //print(Utils);
        //print(Yielder);
        //print(elements);
        //print(key);
        this.Key = function () {
            //print(ArrayEnumerable);
            //print(Dictionary);
            //print(Enumerable);
            //print(Functions);
            //print(Grouping);
            //print(IEnumerator);
            //print(Lookup);
            //print(OrderedEnumerable);
            //print(SortContext);
            //print(State);
            //print(Types);
            //print(Utils);
            //print(Yielder);
            //print(elements);
            //print(key);
            return key;
        };
        ArrayEnumerable.call(this, elements);
    };
    Grouping.prototype = new ArrayEnumerable();
    return Enumerable;
}();

module("Functional");

test("Let", function () {
    var sum = Enumerable.Range(1, 10).Let(function (e) {
        //print(e);
        //print(sum);
        return e.Zip(e, function (a, b) {
            //print(a);
            //print(b);
            //print(e);
            //print(sum);
            return {
                a: a,
                b: b
            };
        });
    }).Select(function (x) {
        //print(sum);
        //print(x);
        return x.a + x.b;
    }).Sum();
    //print(sum);
    equal(sum, 110);
});

test("Share", function () {
    var share = Enumerable.Range(1, 10).Share();
    var ar1 = share.Take(4).ToArray();
    var ar2 = share.ToArray();
    var ar3 = share.ToArray();
    print(ar1[0]);
    print(ar2[0]);
    deepEqual(ar1, [1, 2, 3, 4]);
    deepEqual(ar2, [5, 6, 7, 8, 9, 10]);
    deepEqual(ar3, []);
});

test("MemoizeAll", function () {
    var count = 0;
    var mem = Enumerable.Range(1, 5).Select(function (x) {
        //print(ar1);
        //print(ar2);
        //print(count);
        //print(mem);
        //print(x);
        count++;
        return x;
    }).MemoizeAll();
    var ar1 = mem.ToArray();
    var ar2 = mem.ToArray();
    print(ar1[0]);
    print(ar2[0]);
    deepEqual(ar1, [1, 2, 3, 4, 5]);
    deepEqual(ar2, [1, 2, 3, 4, 5]);
    //print(count);
    equal(5, count);
    mem = Enumerable.From([1, 2, undefined, 3, 4]).MemoizeAll();
    ar1 = mem.ToArray();
    ar2 = mem.ToArray();
    print(ar1[0]);
    print(ar2[0]);
    deepEqual(ar1, [1, 2, undefined, 3, 4]);
    deepEqual(ar2, [1, 2, undefined, 3, 4]);
});