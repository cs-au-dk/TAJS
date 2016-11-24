var buckets = {};

(function () {
    //print(buckets);
    //print(createPriorityQueue2);
    //print(createTree2);
    //print(dict);
    //print(elems);
    //print(o);
    //print(queue);
    //print(s1);
    //print(s2);
    //print(tree);
    "use strict";
    buckets.defaultCompare = function (a, b) {
        //print(a);
        //print(b);
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        if (a < b) {
            return -1;
        }
        if (a === b) {
            return 0;
        }
        return 1;
    };
    buckets.defaultEquals = function (a, b) {
        //print(a);
        //print(b);
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        return a === b;
    };
    buckets.defaultToString = function (item) {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(item);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        if (item === null) {
            return "BUCKETS_NULL";
        }
        if (buckets.isUndefined(item)) {
            return "BUCKETS_UNDEFINED";
        }
        if (buckets.isString(item)) {
            return item;
        }
        return item.toString();
    };
    buckets.isFunction = function (func) {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(func);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        return typeof func === "function";
    };
    buckets.isUndefined = function (obj) {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(obj);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        return typeof obj === "undefined";
    };
    buckets.isString = function (obj) {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(obj);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        return Object.prototype.toString.call(obj) === "[object String]";
    };
    buckets.reverseCompareFunction = function (compareFunction) {
        //print(buckets);
        //print(compareFunction);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        if (!buckets.isFunction(compareFunction)) {
            return function (a, b) {
                //print(a);
                //print(b);
                //print(buckets);
                //print(compareFunction);
                //print(createPriorityQueue2);
                //print(createTree2);
                //print(dict);
                //print(elems);
                //print(o);
                //print(queue);
                //print(s1);
                //print(s2);
                //print(tree);
                if (a < b) {
                    return 1;
                }
                if (a === b) {
                    return 0;
                }
                return -1;
            };
        }
        return function (d, v) {
            //print(buckets);
            //print(compareFunction);
            //print(createPriorityQueue2);
            //print(createTree2);
            //print(d);
            //print(dict);
            //print(elems);
            //print(o);
            //print(queue);
            //print(s1);
            //print(s2);
            //print(tree);
            //print(v);
            return compareFunction(d, v) * -1;
        };
    };
    buckets.compareToEquals = function (compareFunction) {
        //print(buckets);
        //print(compareFunction);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        return function (a, b) {
            //print(a);
            //print(b);
            //print(buckets);
            //print(compareFunction);
            //print(createPriorityQueue2);
            //print(createTree2);
            //print(dict);
            //print(elems);
            //print(o);
            //print(queue);
            //print(s1);
            //print(s2);
            //print(tree);
            return compareFunction(a, b) === 0;
        };
    };
    buckets.arrays = {};
    buckets.arrays.indexOf = function (array, item, equalsFunction) {
        //print(array);
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(equalsFunction);
        //print(item);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        var equals = equalsFunction || buckets.defaultEquals;
        var length = array.length;
        for (var i = 0; i < length; i++) {
            if (equals(array[i], item)) {
                return i;
            }
        }
        return -1;
    };
    buckets.arrays.lastIndexOf = function (array, item, equalsFunction) {
        //print(array);
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(equalsFunction);
        //print(item);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        var equals = equalsFunction || buckets.defaultEquals;
        var length = array.length;
        for (var i = length - 1; i >= 0; i--) {
            if (equals(array[i], item)) {
                return i;
            }
        }
        return -1;
    };
    buckets.arrays.contains = function (array, item, equalsFunction) {
        //print(array);
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(equalsFunction);
        //print(item);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        return buckets.arrays.indexOf(array, item, equalsFunction) >= 0;
    };
    buckets.arrays.remove = function (array, item, equalsFunction) {
        //print(array);
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(equalsFunction);
        //print(item);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        var index = buckets.arrays.indexOf(array, item, equalsFunction);
        if (index < 0) {
            return false;
        }
        array.splice(index, 1);
        return true;
    };
    buckets.arrays.frequency = function (array, item, equalsFunction) {
        //print(array);
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(equalsFunction);
        //print(item);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        var equals = equalsFunction || buckets.defaultEquals;
        var length = array.length;
        var freq = 0;
        for (var i = 0; i < length; i++) {
            if (equals(array[i], item)) {
                freq++;
            }
        }
        return freq;
    };
    buckets.arrays.equals = function (array1, array2, equalsFunction) {
        //print(array1);
        //print(array2);
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(equalsFunction);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        var equals = equalsFunction || buckets.defaultEquals;
        if (array1.length !== array2.length) {
            return false;
        }
        var length = array1.length;
        for (var i = 0; i < length; i++) {
            if (!equals(array1[i], array2[i])) {
                return false;
            }
        }
        return true;
    };
    buckets.arrays.copy = function (array) {
        //print(array);
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        return array.concat();
    };
    buckets.arrays.swap = function (array, i, j) {
        //print(array);
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(i);
        //print(j);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        if (i < 0 || i >= array.length || j < 0 || j >= array.length) {
            return false;
        }
        var temp = array[i];
        array[i] = array[j];
        array[j] = temp;
        return true;
    };
    buckets.arrays.forEach = function (array, callback) {
        //print(array);
        //print(buckets);
        //print(callback);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        var lenght = array.length;
        for (var i = 0; i < lenght; i++) {
            if (callback(array[i]) === false) {
                return;
            }
        }
    };
    buckets.LinkedList = function () {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        this.firstNode = null;
        this.lastNode = null;
        this.nElements = 0;
    };
    buckets.LinkedList.prototype.add = function (item, index) {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(index);
        //print(item);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        if (buckets.isUndefined(index)) {
            index = this.nElements;
        }
        if (index < 0 || index > this.nElements || buckets.isUndefined(item)) {
            return false;
        }
        var newNode = this.createNode(item);
        if (this.nElements === 0) {
            this.firstNode = newNode;
            this.lastNode = newNode;
        } else if (index === this.nElements) {
            this.lastNode.next = newNode;
            this.lastNode = newNode;
        } else if (index === 0) {
            newNode.next = this.firstNode;
            this.firstNode = newNode;
        } else {
            var prev = this.nodeAtIndex(index - 1);
            newNode.next = prev.next;
            prev.next = newNode;
        }
        this.nElements++;
        return true;
    };
    buckets.LinkedList.prototype.first = function () {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        if (this.firstNode !== null) {
            return this.firstNode.element;
        }
        return undefined;
    };
    buckets.LinkedList.prototype.last = function () {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        if (this.lastNode !== null) {
            return this.lastNode.element;
        }
        return undefined;
    };
    buckets.LinkedList.prototype.elementAtIndex = function (index) {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(index);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        var node = this.nodeAtIndex(index);
        if (node === null) {
            return undefined;
        }
        return node.element;
    };
    buckets.LinkedList.prototype.indexOf = function (item, equalsFunction) {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(equalsFunction);
        //print(item);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        var equalsF = equalsFunction || buckets.defaultEquals;
        if (buckets.isUndefined(item)) {
            return -1;
        }
        var currentNode = this.firstNode;
        var index = 0;
        while (currentNode !== null) {
            if (equalsF(currentNode.element, item)) {
                return index;
            }
            index++;
            currentNode = currentNode.next;
        }
        return -1;
    };
    buckets.LinkedList.prototype.contains = function (item, equalsFunction) {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(equalsFunction);
        //print(item);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        return this.indexOf(item, equalsFunction) >= 0;
    };
    buckets.LinkedList.prototype.remove = function (item, equalsFunction) {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(equalsFunction);
        //print(item);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        var equalsF = equalsFunction || buckets.defaultEquals;
        if (this.nElements < 1 || buckets.isUndefined(item)) {
            return false;
        }
        var previous = null;
        var currentNode = this.firstNode;
        while (currentNode !== null) {
            if (equalsF(currentNode.element, item)) {
                if (currentNode === this.firstNode) {
                    this.firstNode = this.firstNode.next;
                    if (currentNode === this.lastNode) {
                        this.lastNode = null;
                    }
                } else if (currentNode === this.lastNode) {
                    this.lastNode = previous;
                    previous.next = currentNode.next;
                    currentNode.next = null;
                } else {
                    previous.next = currentNode.next;
                    currentNode.next = null;
                }
                this.nElements--;
                return true;
            }
            previous = currentNode;
            currentNode = currentNode.next;
        }
        return false;
    };
    buckets.LinkedList.prototype.clear = function () {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        this.firstNode = null;
        this.lastNode = null;
        this.nElements = 0;
    };
    buckets.LinkedList.prototype.equals = function (other, equalsFunction) {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(equalsFunction);
        //print(o);
        //print(other);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        var eqF = equalsFunction || buckets.defaultEquals;
        if (!(other instanceof buckets.LinkedList)) {
            return false;
        }
        if (this.size() !== other.size()) {
            return false;
        }
        return this.equalsAux(this.firstNode, other.firstNode, eqF);
    };
    buckets.LinkedList.prototype.equalsAux = function (n1, n2, eqF) {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(eqF);
        //print(n1);
        //print(n2);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        while (n1 !== null) {
            if (!eqF(n1.element, n2.element)) {
                return false;
            }
            n1 = n1.next;
            n2 = n2.next;
        }
        return true;
    };
    buckets.LinkedList.prototype.removeElementAtIndex = function (index) {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(index);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        if (index < 0 || index >= this.nElements) {
            return undefined;
        }
        var element;
        if (this.nElements === 1) {
            element = this.firstNode.element;
            this.firstNode = null;
            this.lastNode = null;
        } else {
            var previous = this.nodeAtIndex(index - 1);
            if (previous === null) {
                element = this.firstNode.element;
                this.firstNode = this.firstNode.next;
            } else if (previous.next === this.lastNode) {
                element = this.lastNode.element;
                this.lastNode = previous;
            }
            if (previous !== null) {
                element = previous.next.element;
                previous.next = previous.next.next;
            }
        }
        this.nElements--;
        return element;
    };
    buckets.LinkedList.prototype.forEach = function (callback) {
        //print(buckets);
        //print(callback);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        var currentNode = this.firstNode;
        while (currentNode !== null) {
            if (callback(currentNode.element) === false) {
                break;
            }
            currentNode = currentNode.next;
        }
    };
    buckets.LinkedList.prototype.reverse = function () {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        var previous = null;
        var current = this.firstNode;
        var temp = null;
        while (current !== null) {
            temp = current.next;
            current.next = previous;
            previous = current;
            current = temp;
        }
        temp = this.firstNode;
        this.firstNode = this.lastNode;
        this.lastNode = temp;
    };
    buckets.LinkedList.prototype.toArray = function () {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        var array = [];
        var currentNode = this.firstNode;
        while (currentNode !== null) {
            array.push(currentNode.element);
            currentNode = currentNode.next;
        }
        return array;
    };
    buckets.LinkedList.prototype.size = function () {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        return this.nElements;
    };
    buckets.LinkedList.prototype.isEmpty = function () {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        return this.nElements <= 0;
    };
    buckets.LinkedList.prototype.nodeAtIndex = function (index) {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(index);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        if (index < 0 || index >= this.nElements) {
            return null;
        }
        if (index === this.nElements - 1) {
            return this.lastNode;
        }
        var node = this.firstNode;
        for (var i = 0; i < index; i++) {
            node = node.next;
        }
        return node;
    };
    buckets.LinkedList.prototype.createNode = function (item) {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(item);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        return {
            element: item,
            next: null
        };
    };
    buckets.Dictionary = function (toStrFunction) {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(toStrFunction);
        //print(tree);
        this.table = {};
        this.nElements = 0;
        this.toStr = toStrFunction || buckets.defaultToString;
    };
    buckets.Dictionary.prototype.get = function (key) {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(key);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        var pair = this.table[this.toStr(key)];
        if (buckets.isUndefined(pair)) {
            return undefined;
        }
        return pair.value;
    };
    buckets.Dictionary.prototype.set = function (key, value) {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(key);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        //print(value);
        if (buckets.isUndefined(key) || buckets.isUndefined(value)) {
            return undefined;
        }
        var ret;
        var k = this.toStr(key);
        var previousElement = this.table[k];
        if (buckets.isUndefined(previousElement)) {
            this.nElements++;
            ret = undefined;
        } else {
            ret = previousElement.value;
        }
        this.table[k] = {
            key: key,
            value: value
        };
        return ret;
    };
    buckets.Dictionary.prototype.remove = function (key) {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(key);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        var k = this.toStr(key);
        var previousElement = this.table[k];
        if (!buckets.isUndefined(previousElement)) {
            delete this.table[k];
            this.nElements--;
            return previousElement.value;
        }
        return undefined;
    };
    buckets.Dictionary.prototype.keys = function () {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        var array = [];
        for (var name in this.table) {
            if (this.table.hasOwnProperty(name)) {
                array.push(this.table[name].key);
            }
        }
        return array;
    };
    buckets.Dictionary.prototype.values = function () {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        var array = [];
        for (var name in this.table) {
            if (this.table.hasOwnProperty(name)) {
                array.push(this.table[name].value);
            }
        }
        return array;
    };
    buckets.Dictionary.prototype.forEach = function (callback) {
        //print(buckets);
        //print(callback);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        for (var name in this.table) {
            if (this.table.hasOwnProperty(name)) {
                var pair = this.table[name];
                var ret = callback(pair.key, pair.value);
                if (ret === false) {
                    return;
                }
            }
        }
    };
    buckets.Dictionary.prototype.containsKey = function (key) {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(key);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        return !buckets.isUndefined(this.get(key));
    };
    buckets.Dictionary.prototype.clear = function () {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        this.table = {};
        this.nElements = 0;
    };
    buckets.Dictionary.prototype.size = function () {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        return this.nElements;
    };
    buckets.Dictionary.prototype.isEmpty = function () {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        return this.nElements <= 0;
    };
    buckets.MultiDictionary = function (toStrFunction, valuesEqualsFunction) {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(toStrFunction);
        //print(tree);
        //print(valuesEqualsFunction);
        this.parent = new buckets.Dictionary(toStrFunction);
        this.equalsF = valuesEqualsFunction || buckets.defaultEquals;
    };
    buckets.MultiDictionary.prototype.get = function (key) {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(key);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        var values = this.parent.get(key);
        if (buckets.isUndefined(values)) {
            return [];
        }
        return buckets.arrays.copy(values);
    };
    buckets.MultiDictionary.prototype.set = function (key, value) {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(key);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        //print(value);
        if (buckets.isUndefined(key) || buckets.isUndefined(value)) {
            return false;
        }
        if (!this.containsKey(key)) {
            this.parent.set(key, [value]);
            return true;
        }
        var array = this.parent.get(key);
        if (buckets.arrays.contains(array, value, this.equalsF)) {
            return false;
        }
        array.push(value);
        return true;
    };
    buckets.MultiDictionary.prototype.remove = function (key, value) {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(key);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        //print(value);
        if (buckets.isUndefined(value)) {
            var v = this.parent.remove(key);
            if (buckets.isUndefined(v)) {
                return false;
            }
            return true;
        }
        var array = this.parent.get(key);
        if (buckets.arrays.remove(array, value, this.equalsF)) {
            if (array.length === 0) {
                this.parent.remove(key);
            }
            return true;
        }
        return false;
    };
    buckets.MultiDictionary.prototype.keys = function () {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        return this.parent.keys();
    };
    buckets.MultiDictionary.prototype.values = function () {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        var values = this.parent.values();
        var array = [];
        for (var i = 0; i < values.length; i++) {
            var v = values[i];
            for (var j = 0; j < v.length; j++) {
                array.push(v[j]);
            }
        }
        return array;
    };
    buckets.MultiDictionary.prototype.containsKey = function (key) {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(key);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        return this.parent.containsKey(key);
    };
    buckets.MultiDictionary.prototype.clear = function () {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        return this.parent.clear();
    };
    buckets.MultiDictionary.prototype.size = function () {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        return this.parent.size();
    };
    buckets.MultiDictionary.prototype.isEmpty = function () {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        return this.parent.isEmpty();
    };
    buckets.Heap = function (compareFunction) {
        //print(buckets);
        //print(compareFunction);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        this.data = [];
        this.compare = compareFunction || buckets.defaultCompare;
    };
    buckets.Heap.prototype.leftChildIndex = function (nodeIndex) {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(nodeIndex);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        return 2 * nodeIndex + 1;
    };
    buckets.Heap.prototype.rightChildIndex = function (nodeIndex) {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(nodeIndex);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        return 2 * nodeIndex + 2;
    };
    buckets.Heap.prototype.parentIndex = function (nodeIndex) {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(nodeIndex);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        return Math.floor((nodeIndex - 1) / 2);
    };
    buckets.Heap.prototype.minIndex = function (leftChild, rightChild) {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(leftChild);
        //print(o);
        //print(queue);
        //print(rightChild);
        //print(s1);
        //print(s2);
        //print(tree);
        if (rightChild >= this.data.length) {
            if (leftChild >= this.data.length) {
                return -1;
            } else {
                return leftChild;
            }
        } else {
            if (this.compare(this.data[leftChild], this.data[rightChild]) <= 0) {
                return leftChild;
            } else {
                return rightChild;
            }
        }
    };
    buckets.Heap.prototype.siftUp = function (index) {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(index);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        var parent = this.parentIndex(index);
        while (index > 0 && this.compare(this.data[parent], this.data[index]) > 0) {
            buckets.arrays.swap(this.data, parent, index);
            index = parent;
            parent = this.parentIndex(index);
        }
    };
    buckets.Heap.prototype.siftDown = function (nodeIndex) {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(nodeIndex);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        var min = this.minIndex(this.leftChildIndex(nodeIndex), this.rightChildIndex(nodeIndex));
        while (min >= 0 && this.compare(this.data[nodeIndex], this.data[min]) > 0) {
            buckets.arrays.swap(this.data, min, nodeIndex);
            nodeIndex = min;
            min = this.minIndex(this.leftChildIndex(nodeIndex), this.rightChildIndex(nodeIndex));
        }
    };
    buckets.Heap.prototype.peek = function () {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        if (this.data.length > 0) {
            return this.data[0];
        } else {
            return undefined;
        }
    };
    buckets.Heap.prototype.add = function (element) {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(element);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        if (buckets.isUndefined(element)) {
            return undefined;
        }
        this.data.push(element);
        this.siftUp(this.data.length - 1);
        return true;
    };
    buckets.Heap.prototype.removeRoot = function () {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        if (this.data.length > 0) {
            var obj = this.data[0];
            this.data[0] = this.data[this.data.length - 1];
            this.data.splice(this.data.length - 1, 1);
            if (this.data.length > 0) {
                this.siftDown(0);
            }
            return obj;
        }
        return undefined;
    };
    buckets.Heap.prototype.contains = function (element) {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(element);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        var equF = buckets.compareToEquals(this.compare);
        return buckets.arrays.contains(this.data, element, equF);
    };
    buckets.Heap.prototype.size = function () {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        return this.data.length;
    };
    buckets.Heap.prototype.isEmpty = function () {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        return this.data.length <= 0;
    };
    buckets.Heap.prototype.clear = function () {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        this.data.length = 0;
    };
    buckets.Heap.prototype.forEach = function (callback) {
        //print(buckets);
        //print(callback);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        buckets.arrays.forEach(this.data, callback);
    };
    buckets.Stack = function () {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        this.list = new buckets.LinkedList();
    };
    buckets.Stack.prototype.push = function (elem) {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elem);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        return this.list.add(elem, 0);
    };
    buckets.Stack.prototype.add = function (elem) {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elem);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        return this.list.add(elem, 0);
    };
    buckets.Stack.prototype.pop = function () {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        return this.list.removeElementAtIndex(0);
    };
    buckets.Stack.prototype.peek = function () {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        return this.list.first();
    };
    buckets.Stack.prototype.size = function () {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        return this.list.size();
    };
    buckets.Stack.prototype.contains = function (elem, equalsFunction) {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elem);
        //print(elems);
        //print(equalsFunction);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        return this.list.contains(elem, equalsFunction);
    };
    buckets.Stack.prototype.isEmpty = function () {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        return this.list.isEmpty();
    };
    buckets.Stack.prototype.clear = function () {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        this.list.clear();
    };
    buckets.Stack.prototype.forEach = function (callback) {
        //print(buckets);
        //print(callback);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        this.list.forEach(callback);
    };
    buckets.Queue = function () {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        this.list = new buckets.LinkedList();
    };
    buckets.Queue.prototype.enqueue = function (elem) {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elem);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        return this.list.add(elem);
    };
    buckets.Queue.prototype.add = function (elem) {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elem);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        return this.list.add(elem);
    };
    buckets.Queue.prototype.dequeue = function () {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        if (this.list.size() !== 0) {
            var el = this.list.first();
            this.list.removeElementAtIndex(0);
            return el;
        }
        return undefined;
    };
    buckets.Queue.prototype.peek = function () {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        if (this.list.size() !== 0) {
            return this.list.first();
        }
        return undefined;
    };
    buckets.Queue.prototype.size = function () {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        return this.list.size();
    };
    buckets.Queue.prototype.contains = function (elem, equalsFunction) {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elem);
        //print(elems);
        //print(equalsFunction);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        return this.list.contains(elem, equalsFunction);
    };
    buckets.Queue.prototype.isEmpty = function () {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        return this.list.size() <= 0;
    };
    buckets.Queue.prototype.clear = function () {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        this.list.clear();
    };
    buckets.Queue.prototype.forEach = function (callback) {
        //print(buckets);
        //print(callback);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        this.list.forEach(callback);
    };
    buckets.PriorityQueue = function (compareFunction) {
        //print(buckets);
        //print(compareFunction);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        this.heap = new buckets.Heap(buckets.reverseCompareFunction(compareFunction));
    };
    buckets.PriorityQueue.prototype.enqueue = function (element) {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(element);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        return this.heap.add(element);
    };
    buckets.PriorityQueue.prototype.add = function (element) {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(element);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        return this.heap.add(element);
    };
    buckets.PriorityQueue.prototype.dequeue = function () {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        if (this.heap.size() !== 0) {
            var el = this.heap.peek();
            this.heap.removeRoot();
            return el;
        }
        return undefined;
    };
    buckets.PriorityQueue.prototype.peek = function () {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        return this.heap.peek();
    };
    buckets.PriorityQueue.prototype.contains = function (element) {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(element);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        return this.heap.contains(element);
    };
    buckets.PriorityQueue.prototype.isEmpty = function () {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        return this.heap.isEmpty();
    };
    buckets.PriorityQueue.prototype.size = function () {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        return this.heap.size();
    };
    buckets.PriorityQueue.prototype.clear = function () {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        this.heap.clear();
    };
    buckets.PriorityQueue.prototype.forEach = function (callback) {
        //print(buckets);
        //print(callback);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        this.heap.forEach(callback);
    };
    buckets.Set = function (toStringFunction) {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(toStringFunction);
        //print(tree);
        this.dictionary = new buckets.Dictionary(toStringFunction);
    };
    buckets.Set.prototype.contains = function (element) {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(element);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        return this.dictionary.containsKey(element);
    };
    buckets.Set.prototype.add = function (element) {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(element);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        if (this.contains(element) || buckets.isUndefined(element)) {
            return false;
        } else {
            this.dictionary.set(element, element);
            return true;
        }
    };
    buckets.Set.prototype.intersection = function (otherSet) {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(otherSet);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        var set = this;
        this.forEach(function (element) {
            //print(buckets);
            //print(createPriorityQueue2);
            //print(createTree2);
            //print(dict);
            //print(element);
            //print(elems);
            //print(o);
            //print(otherSet);
            //print(queue);
            //print(s1);
            //print(s2);
            //print(set);
            //print(tree);
            if (!otherSet.contains(element)) {
                set.remove(element);
            }
        });
    };
    buckets.Set.prototype.union = function (otherSet) {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(otherSet);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        var set = this;
        otherSet.forEach(function (element) {
            //print(buckets);
            //print(createPriorityQueue2);
            //print(createTree2);
            //print(dict);
            //print(element);
            //print(elems);
            //print(o);
            //print(otherSet);
            //print(queue);
            //print(s1);
            //print(s2);
            //print(set);
            //print(tree);
            set.add(element);
        });
    };
    buckets.Set.prototype.difference = function (otherSet) {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(otherSet);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        var set = this;
        otherSet.forEach(function (element) {
            //print(buckets);
            //print(createPriorityQueue2);
            //print(createTree2);
            //print(dict);
            //print(element);
            //print(elems);
            //print(o);
            //print(otherSet);
            //print(queue);
            //print(s1);
            //print(s2);
            //print(set);
            //print(tree);
            set.remove(element);
        });
    };
    buckets.Set.prototype.isSubsetOf = function (otherSet) {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(otherSet);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        if (this.size() > otherSet.size()) {
            return false;
        }
        var isSub = true;
        this.forEach(function (element) {
            //print(buckets);
            //print(createPriorityQueue2);
            //print(createTree2);
            //print(dict);
            //print(element);
            //print(elems);
            //print(isSub);
            //print(o);
            //print(otherSet);
            //print(queue);
            //print(s1);
            //print(s2);
            //print(tree);
            if (!otherSet.contains(element)) {
                isSub = false;
                return false;
            }
        });
        return isSub;
    };
    buckets.Set.prototype.remove = function (element) {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(element);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        if (!this.contains(element)) {
            return false;
        } else {
            this.dictionary.remove(element);
            return true;
        }
    };
    buckets.Set.prototype.forEach = function (callback) {
        //print(buckets);
        //print(callback);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        this.dictionary.forEach(function (k, v) {
            //print(buckets);
            //print(callback);
            //print(createPriorityQueue2);
            //print(createTree2);
            //print(dict);
            //print(elems);
            //print(k);
            //print(o);
            //print(queue);
            //print(s1);
            //print(s2);
            //print(tree);
            //print(v);
            return callback(v);
        });
    };
    buckets.Set.prototype.toArray = function () {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        return this.dictionary.values();
    };
    buckets.Set.prototype.isEmpty = function () {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        return this.dictionary.isEmpty();
    };
    buckets.Set.prototype.size = function () {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        return this.dictionary.size();
    };
    buckets.Set.prototype.clear = function () {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        this.dictionary.clear();
    };
    buckets.Bag = function (toStrFunction) {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(toStrFunction);
        //print(tree);
        this.toStrF = toStrFunction || buckets.defaultToString;
        this.dictionary = new buckets.Dictionary(this.toStrF);
        this.nElements = 0;
    };
    buckets.Bag.prototype.add = function (element, nCopies) {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(element);
        //print(elems);
        //print(nCopies);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        if (isNaN(nCopies) || buckets.isUndefined(nCopies)) {
            nCopies = 1;
        }
        if (buckets.isUndefined(element) || nCopies <= 0) {
            return false;
        }
        if (!this.contains(element)) {
            var node = {
                value: element,
                copies: nCopies
            };
            this.dictionary.set(element, node);
        } else {
            this.dictionary.get(element).copies += nCopies;
        }
        this.nElements += nCopies;
        return true;
    };
    buckets.Bag.prototype.count = function (element) {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(element);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        if (!this.contains(element)) {
            return 0;
        } else {
            return this.dictionary.get(element).copies;
        }
    };
    buckets.Bag.prototype.contains = function (element) {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(element);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        return this.dictionary.containsKey(element);
    };
    buckets.Bag.prototype.remove = function (element, nCopies) {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(element);
        //print(elems);
        //print(nCopies);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        if (isNaN(nCopies) || buckets.isUndefined(nCopies)) {
            nCopies = 1;
        }
        if (buckets.isUndefined(element) || nCopies <= 0) {
            return false;
        }
        if (!this.contains(element)) {
            return false;
        } else {
            var node = this.dictionary.get(element);
            if (nCopies > node.copies) {
                this.nElements -= node.copies;
            } else {
                this.nElements -= nCopies;
            }
            node.copies -= nCopies;
            if (node.copies <= 0) {
                this.dictionary.remove(element);
            }
            return true;
        }
    };
    buckets.Bag.prototype.toArray = function () {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        var a = [];
        var values = this.dictionary.values();
        var vl = values.length;
        for (var i = 0; i < vl; i++) {
            var node = values[i];
            var element = node.value;
            var copies = node.copies;
            for (var j = 0; j < copies; j++) {
                a.push(element);
            }
        }
        return a;
    };
    buckets.Bag.prototype.toSet = function () {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        var set = new buckets.Set(this.toStrF);
        var elements = this.dictionary.values();
        var l = elements.length;
        for (var i = 0; i < l; i++) {
            var value = elements[i].value;
            set.add(value);
        }
        return set;
    };
    buckets.Bag.prototype.forEach = function (callback) {
        //print(buckets);
        //print(callback);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        this.dictionary.forEach(function (k, v) {
            //print(buckets);
            //print(callback);
            //print(createPriorityQueue2);
            //print(createTree2);
            //print(dict);
            //print(elems);
            //print(k);
            //print(o);
            //print(queue);
            //print(s1);
            //print(s2);
            //print(tree);
            //print(v);
            var value = v.value;
            var copies = v.copies;
            for (var i = 0; i < copies; i++) {
                if (callback(value) === false) {
                    return false;
                }
            }
            return true;
        });
    };
    buckets.Bag.prototype.size = function () {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        return this.nElements;
    };
    buckets.Bag.prototype.isEmpty = function () {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        return this.nElements === 0;
    };
    buckets.Bag.prototype.clear = function () {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        this.nElements = 0;
        this.dictionary.clear();
    };
    buckets.BSTree = function (compareFunction) {
        //print(buckets);
        //print(compareFunction);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        this.root = null;
        this.compare = compareFunction || buckets.defaultCompare;
        this.nElements = 0;
    };
    buckets.BSTree.prototype.add = function (element) {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(element);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        if (buckets.isUndefined(element)) {
            return false;
        }
        if (this.insertNode(this.createNode(element)) !== null) {
            this.nElements++;
            return true;
        }
        return false;
    };
    buckets.BSTree.prototype.clear = function () {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        this.root = null;
        this.nElements = 0;
    };
    buckets.BSTree.prototype.isEmpty = function () {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        return this.nElements === 0;
    };
    buckets.BSTree.prototype.size = function () {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        return this.nElements;
    };
    buckets.BSTree.prototype.contains = function (element) {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(element);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        if (buckets.isUndefined(element)) {
            return false;
        }
        return this.searchNode(this.root, element) !== null;
    };
    buckets.BSTree.prototype.remove = function (element) {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(element);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        var node = this.searchNode(this.root, element);
        if (node === null) {
            return false;
        }
        this.removeNode(node);
        this.nElements--;
        return true;
    };
    buckets.BSTree.prototype.inorderTraversal = function (callback) {
        //print(buckets);
        //print(callback);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        this.inorderTraversalAux(this.root, callback, {
            stop: false
        });
    };
    buckets.BSTree.prototype.preorderTraversal = function (callback) {
        //print(buckets);
        //print(callback);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        this.preorderTraversalAux(this.root, callback, {
            stop: false
        });
    };
    buckets.BSTree.prototype.postorderTraversal = function (callback) {
        //print(buckets);
        //print(callback);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        this.postorderTraversalAux(this.root, callback, {
            stop: false
        });
    };
    buckets.BSTree.prototype.levelTraversal = function (callback) {
        //print(buckets);
        //print(callback);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        this.levelTraversalAux(this.root, callback);
    };
    buckets.BSTree.prototype.minimum = function () {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        if (this.isEmpty()) {
            return undefined;
        }
        return this.minimumAux(this.root).element;
    };
    buckets.BSTree.prototype.maximum = function () {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        if (this.isEmpty()) {
            return undefined;
        }
        return this.maximumAux(this.root).element;
    };
    buckets.BSTree.prototype.forEach = function (callback) {
        //print(buckets);
        //print(callback);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        this.inorderTraversal(callback);
    };
    buckets.BSTree.prototype.toArray = function () {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        var array = [];
        this.inorderTraversal(function (element) {
            //print(array);
            //print(buckets);
            //print(createPriorityQueue2);
            //print(createTree2);
            //print(dict);
            //print(element);
            //print(elems);
            //print(o);
            //print(queue);
            //print(s1);
            //print(s2);
            //print(tree);
            array.push(element);
        });
        return array;
    };
    buckets.BSTree.prototype.height = function () {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        return this.heightAux(this.root);
    };
    buckets.BSTree.prototype.searchNode = function (node, element) {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(element);
        //print(elems);
        //print(node);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        var cmp = null;
        while (node !== null && cmp !== 0) {
            cmp = this.compare(element, node.element);
            if (cmp < 0) {
                node = node.leftCh;
            } else if (cmp > 0) {
                node = node.rightCh;
            }
        }
        return node;
    };
    buckets.BSTree.prototype.transplant = function (n1, n2) {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(n1);
        //print(n2);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        if (n1.parent === null) {
            this.root = n2;
        } else if (n1 === n1.parent.leftCh) {
            n1.parent.leftCh = n2;
        } else {
            n1.parent.rightCh = n2;
        }
        if (n2 !== null) {
            n2.parent = n1.parent;
        }
    };
    buckets.BSTree.prototype.removeNode = function (node) {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(node);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        if (node.leftCh === null) {
            this.transplant(node, node.rightCh);
        } else if (node.rightCh === null) {
            this.transplant(node, node.leftCh);
        } else {
            var y = this.minimumAux(node.rightCh);
            if (y.parent !== node) {
                this.transplant(y, y.rightCh);
                y.rightCh = node.rightCh;
                y.rightCh.parent = y;
            }
            this.transplant(node, y);
            y.leftCh = node.leftCh;
            y.leftCh.parent = y;
        }
    };
    buckets.BSTree.prototype.inorderTraversalAux = function (node, callback, signal) {
        //print(buckets);
        //print(callback);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(node);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(signal);
        //print(tree);
        if (node === null || signal.stop) {
            return;
        }
        this.inorderTraversalAux(node.leftCh, callback, signal);
        if (signal.stop) {
            return;
        }
        signal.stop = callback(node.element) === false;
        if (signal.stop) {
            return;
        }
        this.inorderTraversalAux(node.rightCh, callback, signal);
    };
    buckets.BSTree.prototype.levelTraversalAux = function (node, callback) {
        //print(buckets);
        //print(callback);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(node);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        var queue = new buckets.Queue();
        if (node !== null) {
            queue.enqueue(node);
        }
        while (!queue.isEmpty()) {
            node = queue.dequeue();
            if (callback(node.element) === false) {
                return;
            }
            if (node.leftCh !== null) {
                queue.enqueue(node.leftCh);
            }
            if (node.rightCh !== null) {
                queue.enqueue(node.rightCh);
            }
        }
    };
    buckets.BSTree.prototype.preorderTraversalAux = function (node, callback, signal) {
        //print(buckets);
        //print(callback);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(node);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(signal);
        //print(tree);
        if (node === null || signal.stop) {
            return;
        }
        signal.stop = callback(node.element) === false;
        if (signal.stop) {
            return;
        }
        this.preorderTraversalAux(node.leftCh, callback, signal);
        if (signal.stop) {
            return;
        }
        this.preorderTraversalAux(node.rightCh, callback, signal);
    };
    buckets.BSTree.prototype.postorderTraversalAux = function (node, callback, signal) {
        //print(buckets);
        //print(callback);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(node);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(signal);
        //print(tree);
        if (node === null || signal.stop) {
            return;
        }
        this.postorderTraversalAux(node.leftCh, callback, signal);
        if (signal.stop) {
            return;
        }
        this.postorderTraversalAux(node.rightCh, callback, signal);
        if (signal.stop) {
            return;
        }
        signal.stop = callback(node.element) === false;
    };
    buckets.BSTree.prototype.minimumAux = function (node) {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(node);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        while (node.leftCh !== null) {
            node = node.leftCh;
        }
        return node;
    };
    buckets.BSTree.prototype.maximumAux = function (node) {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(node);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        while (node.rightCh !== null) {
            node = node.rightCh;
        }
        return node;
    };
    buckets.BSTree.prototype.successorNode = function (node) {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(node);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        if (node.rightCh !== null) {
            return this.minimumAux(node.rightCh);
        }
        var successor = node.parent;
        while (successor !== null && node === successor.rightCh) {
            node = successor;
            successor = node.parent;
        }
        return successor;
    };
    buckets.BSTree.prototype.heightAux = function (node) {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(node);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        if (node === null) {
            return -1;
        }
        return Math.max(this.heightAux(node.leftCh), this.heightAux(node.rightCh)) + 1;
    };
    buckets.BSTree.prototype.insertNode = function (node) {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(elems);
        //print(node);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        var parent = null;
        var position = this.root;
        var cmp = null;
        while (position !== null) {
            cmp = this.compare(node.element, position.element);
            if (cmp === 0) {
                return null;
            } else if (cmp < 0) {
                parent = position;
                position = position.leftCh;
            } else {
                parent = position;
                position = position.rightCh;
            }
        }
        node.parent = parent;
        if (parent === null) {
            this.root = node;
        } else if (this.compare(node.element, parent.element) < 0) {
            parent.leftCh = node;
        } else {
            parent.rightCh = node;
        }
        return node;
    };
    buckets.BSTree.prototype.createNode = function (element) {
        //print(buckets);
        //print(createPriorityQueue2);
        //print(createTree2);
        //print(dict);
        //print(element);
        //print(elems);
        //print(o);
        //print(queue);
        //print(s1);
        //print(s2);
        //print(tree);
        return {
            element: element,
            leftCh: null,
            rightCh: null,
            parent: null
        };
    };
    if (typeof module !== "undefined") {
        module.exports = buckets;
    }
})();

var tree = new buckets.BSTree();

var createTree2 = function () {
    //print(buckets);
    //print(createPriorityQueue2);
    //print(createTree2);
    //print(dict);
    //print(elems);
    //print(o);
    //print(queue);
    //print(s1);
    //print(s2);
    //print(tree);
    tree.add("f");
    tree.add("b");
    tree.add("a");
    tree.add("d");
    tree.add("c");
    tree.add("e");
    tree.add("g");
    tree.add("i");
    tree.add("h");
};

createTree2();

tree.remove("i");

//print(tree);

var dict = new buckets.MultiDictionary();

var elems = 100;

var o;

dict.set("a", 1);

dict.remove("a", 1);

o = dict.containsKey("a");

dict.set("a", 2);

dict.set("a", 3);

dict.remove("a", 3);

o = dict.get("a");

dict.remove("a", 2);

//print(o);

var queue = new buckets.PriorityQueue();

var createPriorityQueue2 = function () {
    //print(buckets);
    //print(createPriorityQueue2);
    //print(createTree2);
    //print(dict);
    //print(elems);
    //print(o);
    //print(queue);
    //print(s1);
    //print(s2);
    //print(tree);
    queue.enqueue(1);
    queue.enqueue(3);
    queue.enqueue(0);
    queue.enqueue(2);
    return queue;
};

queue.dequeue();

o = queue.size();

//print(o);

set = new buckets.Set();

set2 = new buckets.Set();

set.intersection(set2);

set.isEmpty();

set2.intersection(set);

set2.isEmpty();

set = new buckets.Set();

set2 = new buckets.Set();

set.add(1);

set.add(2);

set.add(3);

set.intersection(set2);

set.isEmpty();

set2.intersection(set);

set2.isEmpty();

set = new buckets.Set();

set2 = new buckets.Set();

set.add(1);

set.add(2);

set2.add(1);

set2.add(2);

set2.add(3);

set.intersection(set2);

var s1 = set.toArray().sort();

set = new buckets.Set();

set.add(1);

set.add(2);

set2.intersection(set);

var s2 = set2.toArray().sort();

set = new buckets.Set();

set2 = new buckets.Set();

set.add(1);

set.add(2);

set2.add(3);

set2.add(4);

set2.add(5);

set.intersection(set2);

set.isEmpty();

set.add(1);

set.add(2);

set2.intersection(set);

o = set2.isEmpty();

//print(o);

//print(buckets);

//print(createPriorityQueue2);

//print(createTree2);

//print(dict);

//print(elems);

//print(o);

//print(queue);

//print(s1);

//print(s2);

print(tree);