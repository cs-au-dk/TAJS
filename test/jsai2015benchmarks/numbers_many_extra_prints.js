x = "abc";

var numbers = {
    basic: {},
    calculus: {},
    complex: {},
    dsp: {},
    generate: {},
    matrix: {},
    prime: {},
    statistic: {},
    EPSILON: .001
};

var basic = numbers.basic;

basic.sum = function (arr) {
    //print(Complex);
    //print(SimpsonDef);
    //print(SimpsonRecursive);
    //print(arr);
    //print(basic);
    //print(calculus);
    //print(dsp);
    //print(firstLup);
    //print(func);
    //print(generate);
    //print(inputMatrix);
    //print(matrix);
    //print(numbers);
    //print(o);
    //print(prime);
    //print(statistic);
    if (Object.prototype.toString.call(arr) === "[object Array]") {
        var total = 0;
        for (var i = 0; i < arr.length; i++) {
            if (typeof arr[i] === "number") {
                total = total + arr[i];
            } else {
                throw new Error("All elements in array must be numbers");
            }
        }
        return total;
    } else {
        throw new Error("Input must be of type Array");
    }
};

basic.subtraction = function (arr) {
    //print(Complex);
    //print(SimpsonDef);
    //print(SimpsonRecursive);
    //print(arr);
    //print(basic);
    //print(calculus);
    //print(dsp);
    //print(firstLup);
    //print(func);
    //print(generate);
    //print(inputMatrix);
    //print(matrix);
    //print(numbers);
    //print(o);
    //print(prime);
    //print(statistic);
    if (Object.prototype.toString.call(arr) === "[object Array]") {
        var total = arr[arr.length - 1];
        for (var i = arr.length - 2; i >= 0; i--) {
            if (typeof arr[i] === "number") {
                total -= arr[i];
            } else {
                throw new Error("All elements in array must be numbers");
            }
        }
        return total;
    } else {
        throw new Error("Input must be of type Array");
    }
};

basic.product = function (arr) {
    //print(Complex);
    //print(SimpsonDef);
    //print(SimpsonRecursive);
    //print(arr);
    //print(basic);
    //print(calculus);
    //print(dsp);
    //print(firstLup);
    //print(func);
    //print(generate);
    //print(inputMatrix);
    //print(matrix);
    //print(numbers);
    //print(o);
    //print(prime);
    //print(statistic);
    if (Object.prototype.toString.call(arr) === "[object Array]") {
        var total = arr[0];
        for (var i = 1, length = arr.length; i < length; i++) {
            if (typeof arr[i] === "number") {
                total = total * arr[i];
            } else {
                throw new Error("All elements in array must be numbers");
            }
        }
        return total;
    } else {
        throw new Error("Input must be of type Array");
    }
};

basic.square = function (num) {
    //print(Complex);
    //print(SimpsonDef);
    //print(SimpsonRecursive);
    //print(basic);
    //print(calculus);
    //print(dsp);
    //print(firstLup);
    //print(func);
    //print(generate);
    //print(inputMatrix);
    //print(matrix);
    //print(num);
    //print(numbers);
    //print(o);
    //print(prime);
    //print(statistic);
    return num * num;
};

basic.binomial = function (n, k) {
    //print(Complex);
    //print(SimpsonDef);
    //print(SimpsonRecursive);
    //print(basic);
    //print(calculus);
    //print(dsp);
    //print(firstLup);
    //print(func);
    //print(generate);
    //print(inputMatrix);
    //print(k);
    //print(matrix);
    //print(n);
    //print(numbers);
    //print(o);
    //print(prime);
    //print(statistic);
    var arr = [];

    function _binomial(n, k) {
        //print(Complex);
        //print(SimpsonDef);
        //print(SimpsonRecursive);
        //print(_binomial);
        //print(arr);
        //print(basic);
        //print(calculus);
        //print(dsp);
        //print(firstLup);
        //print(func);
        //print(generate);
        //print(inputMatrix);
        //print(k);
        //print(matrix);
        //print(n);
        //print(numbers);
        //print(o);
        //print(prime);
        //print(statistic);
        if (n >= 0 && k === 0) return 1;
        if (n === 0 && k > 0) return 0;
        if (arr[n] && arr[n][k] > 0) return arr[n][k];
        if (!arr[n]) arr[n] = [];
        return arr[n][k] = _binomial(n - 1, k - 1) + _binomial(n - 1, k);
    }

    return _binomial(n, k);
};

basic.factorial = function (num) {
    //print(Complex);
    //print(SimpsonDef);
    //print(SimpsonRecursive);
    //print(basic);
    //print(calculus);
    //print(dsp);
    //print(firstLup);
    //print(func);
    //print(generate);
    //print(inputMatrix);
    //print(matrix);
    //print(num);
    //print(numbers);
    //print(o);
    //print(prime);
    //print(statistic);
    var i = 2, o = 1;
    while (i <= num) {
        o *= i++;
    }
    return o;
};

basic.gcd = function (a, b) {
    //print(Complex);
    //print(SimpsonDef);
    //print(SimpsonRecursive);
    //print(a);
    //print(b);
    //print(basic);
    //print(calculus);
    //print(dsp);
    //print(firstLup);
    //print(func);
    //print(generate);
    //print(inputMatrix);
    //print(matrix);
    //print(numbers);
    //print(o);
    //print(prime);
    //print(statistic);
    var c;
    b = +b && +a ? +b : 0;
    a = b ? a : 1;
    while (b) {
        c = a % b;
        a = b;
        b = c;
    }
    return Math.abs(a);
};

basic.lcm = function (num1, num2) {
    //print(Complex);
    //print(SimpsonDef);
    //print(SimpsonRecursive);
    //print(basic);
    //print(calculus);
    //print(dsp);
    //print(firstLup);
    //print(func);
    //print(generate);
    //print(inputMatrix);
    //print(matrix);
    //print(num1);
    //print(num2);
    //print(numbers);
    //print(o);
    //print(prime);
    //print(statistic);
    return Math.abs(num1 * num2) / basic.gcd(num1, num2);
};

basic.random = function (arr, quant, allowDuplicates) {
    //print(Complex);
    //print(SimpsonDef);
    //print(SimpsonRecursive);
    //print(allowDuplicates);
    //print(arr);
    //print(basic);
    //print(calculus);
    //print(dsp);
    //print(firstLup);
    //print(func);
    //print(generate);
    //print(inputMatrix);
    //print(matrix);
    //print(numbers);
    //print(o);
    //print(prime);
    //print(quant);
    //print(statistic);
    if (arr.length === 0) {
        throw new Error("Empty array");
    } else if (quant > arr.length && !allowDuplicates) {
        throw new Error("Quantity requested exceeds size of array");
    }
    if (allowDuplicates === true) {
        var result = [], i;
        for (i = 0; i < quant; i++) {
            result[i] = arr[Math.floor(Math.random() * arr.length)];
        }
        return result;
    } else {
        return basic.shuffle(arr).slice(0, quant);
    }
};

basic.shuffle = function (array) {
    //print(Complex);
    //print(SimpsonDef);
    //print(SimpsonRecursive);
    //print(array);
    //print(basic);
    //print(calculus);
    //print(dsp);
    //print(firstLup);
    //print(func);
    //print(generate);
    //print(inputMatrix);
    //print(matrix);
    //print(numbers);
    //print(o);
    //print(prime);
    //print(statistic);
    var m = array.length, t, i;
    while (m) {
        i = Math.floor(Math.random() * m--);
        t = array[m];
        array[m] = array[i];
        array[i] = t;
    }
    return array;
};

basic.max = function (array) {
    //print(Complex);
    //print(SimpsonDef);
    //print(SimpsonRecursive);
    //print(array);
    //print(basic);
    //print(calculus);
    //print(dsp);
    //print(firstLup);
    //print(func);
    //print(generate);
    //print(inputMatrix);
    //print(matrix);
    //print(numbers);
    //print(o);
    //print(prime);
    //print(statistic);
    return Math.max.apply(Math, array);
};

basic.min = function (array) {
    //print(Complex);
    //print(SimpsonDef);
    //print(SimpsonRecursive);
    //print(array);
    //print(basic);
    //print(calculus);
    //print(dsp);
    //print(firstLup);
    //print(func);
    //print(generate);
    //print(inputMatrix);
    //print(matrix);
    //print(numbers);
    //print(o);
    //print(prime);
    //print(statistic);
    return Math.min.apply(Math, array);
};

basic.range = function (start, stop, step) {
    //print(Complex);
    //print(SimpsonDef);
    //print(SimpsonRecursive);
    //print(basic);
    //print(calculus);
    //print(dsp);
    //print(firstLup);
    //print(func);
    //print(generate);
    //print(inputMatrix);
    //print(matrix);
    //print(numbers);
    //print(o);
    //print(prime);
    //print(start);
    //print(statistic);
    //print(step);
    //print(stop);
    var array, i = 0, len;
    if (arguments.length <= 1) {
        stop = start || 0;
        start = 0;
    }
    step = step || 1;
    if (stop < start) {
        step = 0 - Math.abs(step);
    }
    len = Math.max(Math.ceil((stop - start) / step) + 1, 0);
    array = new Array(len);
    while (i < len) {
        array[i++] = start;
        start += step;
    }
    return array;
};

basic.isInt = function (n) {
    //print(Complex);
    //print(SimpsonDef);
    //print(SimpsonRecursive);
    //print(basic);
    //print(calculus);
    //print(dsp);
    //print(firstLup);
    //print(func);
    //print(generate);
    //print(inputMatrix);
    //print(matrix);
    //print(n);
    //print(numbers);
    //print(o);
    //print(prime);
    //print(statistic);
    return n % 1 === 0;
};

basic.divMod = function (a, b) {
    //print(Complex);
    //print(SimpsonDef);
    //print(SimpsonRecursive);
    //print(a);
    //print(b);
    //print(basic);
    //print(calculus);
    //print(dsp);
    //print(firstLup);
    //print(func);
    //print(generate);
    //print(inputMatrix);
    //print(matrix);
    //print(numbers);
    //print(o);
    //print(prime);
    //print(statistic);
    if (!basic.isInt(a) || !basic.isInt(b)) return false;
    return [Math.floor(a / b), a % b];
};

basic.powerMod = function (a, b, m) {
    //print(Complex);
    //print(SimpsonDef);
    //print(SimpsonRecursive);
    //print(a);
    //print(b);
    //print(basic);
    //print(calculus);
    //print(dsp);
    //print(firstLup);
    //print(func);
    //print(generate);
    //print(inputMatrix);
    //print(m);
    //print(matrix);
    //print(numbers);
    //print(o);
    //print(prime);
    //print(statistic);
    if (b < -1) return Math.pow(a, b) % m;
    if (b === 0) return 1 % m;
    if (b >= 1) {
        var result = 1;
        while (b > 0) {
            if (b % 2 === 1) {
                result = result * a % m;
            }
            a = a * a % m;
            b = b >> 1;
        }
        return result;
    }
    if (b === -1) return basic.modInverse(a, m);
    if (b < 1) {
        return basic.powerMod(a, Math.pow(b, -1), m);
    }
};

basic.egcd = function (a, b) {
    //print(Complex);
    //print(SimpsonDef);
    //print(SimpsonRecursive);
    //print(a);
    //print(b);
    //print(basic);
    //print(calculus);
    //print(dsp);
    //print(firstLup);
    //print(func);
    //print(generate);
    //print(inputMatrix);
    //print(matrix);
    //print(numbers);
    //print(o);
    //print(prime);
    //print(statistic);
    var x = +b && +a ? 1 : 0, y = b ? 0 : 1, u = +b && +a ? 0 : 1, v = b ? 1 : 0;
    b = +b && +a ? +b : 0;
    a = b ? a : 1;
    while (b) {
        var dm = basic.divMod(a, b), q = dm[0], r = dm[1];
        var m = x - u * q, n = y - v * q;
        a = b;
        b = r;
        x = u;
        y = v;
        u = m;
        v = n;
    }
    return [a, x, y];
};

basic.modInverse = function (a, m) {
    //print(Complex);
    //print(SimpsonDef);
    //print(SimpsonRecursive);
    //print(a);
    //print(basic);
    //print(calculus);
    //print(dsp);
    //print(firstLup);
    //print(func);
    //print(generate);
    //print(inputMatrix);
    //print(m);
    //print(matrix);
    //print(numbers);
    //print(o);
    //print(prime);
    //print(statistic);
    var r = basic.egcd(a, m);
    if (r[0] != 1) throw new Error("No modular inverse exists");
    return r[1] % m;
};

var calculus = numbers.calculus;

calculus.pointDiff = function (func, point) {
    //print(Complex);
    //print(SimpsonDef);
    //print(SimpsonRecursive);
    //print(basic);
    //print(calculus);
    //print(dsp);
    //print(firstLup);
    //print(func);
    //print(generate);
    //print(inputMatrix);
    //print(matrix);
    //print(numbers);
    //print(o);
    //print(point);
    //print(prime);
    //print(statistic);
    var a = func(point - .001);
    var b = func(point + .001);
    return (b - a) / .002;
};

calculus.Riemann = function (func, start, finish, n, sampler) {
    //print(Complex);
    //print(SimpsonDef);
    //print(SimpsonRecursive);
    //print(basic);
    //print(calculus);
    //print(dsp);
    //print(finish);
    //print(firstLup);
    //print(func);
    //print(generate);
    //print(inputMatrix);
    //print(matrix);
    //print(n);
    //print(numbers);
    //print(o);
    //print(prime);
    //print(sampler);
    //print(start);
    //print(statistic);
    var inc = (finish - start) / n;
    var totalHeight = 0;
    var i;
    if (typeof sampler === "function") {
        for (i = start; i < finish; i += inc) {
            totalHeight += func(sampler(i, i + inc));
        }
    } else {
        for (i = start; i < finish; i += inc) {
            totalHeight += func(i);
        }
    }
    return totalHeight * inc;
};

function SimpsonDef(func, a, b) {
    //print(Complex);
    //print(SimpsonDef);
    //print(SimpsonRecursive);
    //print(a);
    //print(b);
    //print(basic);
    //print(calculus);
    //print(dsp);
    //print(firstLup);
    //print(func);
    //print(generate);
    //print(inputMatrix);
    //print(matrix);
    //print(numbers);
    //print(o);
    //print(prime);
    //print(statistic);
    var c = (a + b) / 2;
    var d = Math.abs(b - a) / 6;
    return d * (func(a) + 4 * func(c) + func(b));
}

function SimpsonRecursive(func, a, b, whole, eps) {
    //print(Complex);
    //print(SimpsonDef);
    //print(SimpsonRecursive);
    //print(a);
    //print(b);
    //print(basic);
    //print(calculus);
    //print(dsp);
    //print(eps);
    //print(firstLup);
    //print(func);
    //print(generate);
    //print(inputMatrix);
    //print(matrix);
    //print(numbers);
    //print(o);
    //print(prime);
    //print(statistic);
    //print(whole);
    var c = a + b;
    var left = SimpsonDef(func, a, c);
    var right = SimpsonDef(func, c, b);
    if (Math.abs(left + right - whole) <= 15 * eps) {
        return left + right + (left + right - whole) / 15;
    } else {
        return SimpsonRecursive(func, a, c, eps / 2, left) + SimpsonRecursive(func, c, b, eps / 2, right);
    }
}

calculus.adaptiveSimpson = function (func, a, b, eps) {
    //print(Complex);
    //print(SimpsonDef);
    //print(SimpsonRecursive);
    //print(a);
    //print(b);
    //print(basic);
    //print(calculus);
    //print(dsp);
    //print(eps);
    //print(firstLup);
    //print(func);
    //print(generate);
    //print(inputMatrix);
    //print(matrix);
    //print(numbers);
    //print(o);
    //print(prime);
    //print(statistic);
    eps = typeof eps === "undefined" ? numbers.EPSILON : eps;
    return SimpsonRecursive(func, a, b, SimpsonDef(func, a, b), eps);
};

calculus.limit = function (func, point, approach) {
    //print(Complex);
    //print(SimpsonDef);
    //print(SimpsonRecursive);
    //print(approach);
    //print(basic);
    //print(calculus);
    //print(dsp);
    //print(firstLup);
    //print(func);
    //print(generate);
    //print(inputMatrix);
    //print(matrix);
    //print(numbers);
    //print(o);
    //print(point);
    //print(prime);
    //print(statistic);
    if (approach === "left") {
        return func(point - 1e-15);
    } else if (approach === "right") {
        return func(point + 1e-15);
    } else if (approach === "middle") {
        return (calculus.limit(func, point, "left") + calculus.limit(func, point, "right")) / 2;
    } else {
        throw new Error("Approach not provided");
    }
};

calculus.StirlingGamma = function (num) {
    //print(Complex);
    //print(SimpsonDef);
    //print(SimpsonRecursive);
    //print(basic);
    //print(calculus);
    //print(dsp);
    //print(firstLup);
    //print(func);
    //print(generate);
    //print(inputMatrix);
    //print(matrix);
    //print(num);
    //print(numbers);
    //print(o);
    //print(prime);
    //print(statistic);
    return Math.sqrt(2 * Math.PI / num) * Math.pow(num / Math.E, num);
};

calculus.LanczosGamma = function (num) {
    //print(Complex);
    //print(SimpsonDef);
    //print(SimpsonRecursive);
    //print(basic);
    //print(calculus);
    //print(dsp);
    //print(firstLup);
    //print(func);
    //print(generate);
    //print(inputMatrix);
    //print(matrix);
    //print(num);
    //print(numbers);
    //print(o);
    //print(prime);
    //print(statistic);
    var p = [.9999999999998099, 676.5203681218851, -1259.1392167224028, 771.3234287776531, -176.6150291621406, 12.507343278686905, -.13857109526572012, 9984369578019572e-21, 1.5056327351493116e-7];
    var i;
    var g = 7;
    if (num < .5) return Math.PI / (Math.sin(Math.PI * num) * calculus.LanczosGamma(1 - num));
    num -= 1;
    var a = p[0];
    var t = num + g + .5;
    for (i = 1; i < p.length; i++) {
        a += p[i] / (num + i);
    }
    return Math.sqrt(2 * Math.PI) * Math.pow(t, num + .5) * Math.exp(-t) * a;
};

var Complex = function (re, im) {
    //print(Complex);
    //print(SimpsonDef);
    //print(SimpsonRecursive);
    //print(basic);
    //print(calculus);
    //print(dsp);
    //print(firstLup);
    //print(func);
    //print(generate);
    //print(im);
    //print(inputMatrix);
    //print(matrix);
    //print(numbers);
    //print(o);
    //print(prime);
    //print(re);
    //print(statistic);
    this.re = re;
    this.im = im;
    this.r = this.magnitude();
    this.t = this.phase();
};

Complex.prototype.add = function (addend) {
    //print(Complex);
    //print(SimpsonDef);
    //print(SimpsonRecursive);
    //print(addend);
    //print(basic);
    //print(calculus);
    //print(dsp);
    //print(firstLup);
    //print(func);
    //print(generate);
    //print(inputMatrix);
    //print(matrix);
    //print(numbers);
    //print(o);
    //print(prime);
    //print(statistic);
    return new Complex(this.re + addend.re, this.im + addend.im);
};

Complex.prototype.subtract = function (subtrahend) {
    //print(Complex);
    //print(SimpsonDef);
    //print(SimpsonRecursive);
    //print(basic);
    //print(calculus);
    //print(dsp);
    //print(firstLup);
    //print(func);
    //print(generate);
    //print(inputMatrix);
    //print(matrix);
    //print(numbers);
    //print(o);
    //print(prime);
    //print(statistic);
    //print(subtrahend);
    return new Complex(this.re - subtrahend.re, this.im - subtrahend.im);
};

Complex.prototype.multiply = function (multiplier) {
    //print(Complex);
    //print(SimpsonDef);
    //print(SimpsonRecursive);
    //print(basic);
    //print(calculus);
    //print(dsp);
    //print(firstLup);
    //print(func);
    //print(generate);
    //print(inputMatrix);
    //print(matrix);
    //print(multiplier);
    //print(numbers);
    //print(o);
    //print(prime);
    //print(statistic);
    var re = this.re * multiplier.re - this.im * multiplier.im;
    var im = this.im * multiplier.re + this.re * multiplier.im;
    return new Complex(re, im);
};

Complex.prototype.divide = function (divisor) {
    //print(Complex);
    //print(SimpsonDef);
    //print(SimpsonRecursive);
    //print(basic);
    //print(calculus);
    //print(divisor);
    //print(dsp);
    //print(firstLup);
    //print(func);
    //print(generate);
    //print(inputMatrix);
    //print(matrix);
    //print(numbers);
    //print(o);
    //print(prime);
    //print(statistic);
    var denominator = divisor.re * divisor.re + divisor.im * divisor.im;
    var re = (this.re * divisor.re + this.im * divisor.im) / denominator;
    var im = (this.im * divisor.re - this.re * divisor.im) / denominator;
    return new Complex(re, im);
};

Complex.prototype.magnitude = function () {
    //print(Complex);
    //print(SimpsonDef);
    //print(SimpsonRecursive);
    //print(basic);
    //print(calculus);
    //print(dsp);
    //print(firstLup);
    //print(func);
    //print(generate);
    //print(inputMatrix);
    //print(matrix);
    //print(numbers);
    //print(o);
    //print(prime);
    //print(statistic);
    return Math.sqrt(this.re * this.re + this.im * this.im);
};

Complex.prototype.phase = function () {
    //print(Complex);
    //print(SimpsonDef);
    //print(SimpsonRecursive);
    //print(basic);
    //print(calculus);
    //print(dsp);
    //print(firstLup);
    //print(func);
    //print(generate);
    //print(inputMatrix);
    //print(matrix);
    //print(numbers);
    //print(o);
    //print(prime);
    //print(statistic);
    return Math.atan2(this.im, this.re);
};

Complex.prototype.conjugate = function () {
    //print(Complex);
    //print(SimpsonDef);
    //print(SimpsonRecursive);
    //print(basic);
    //print(calculus);
    //print(dsp);
    //print(firstLup);
    //print(func);
    //print(generate);
    //print(inputMatrix);
    //print(matrix);
    //print(numbers);
    //print(o);
    //print(prime);
    //print(statistic);
    return new Complex(this.re, -1 * this.im);
};

numbers.complex = Complex;

var Complex = numbers.complex;

var dsp = numbers.dsp;

dsp.segment = function (arr, start, step) {
    //print(Complex);
    //print(SimpsonDef);
    //print(SimpsonRecursive);
    //print(arr);
    //print(basic);
    //print(calculus);
    //print(dsp);
    //print(firstLup);
    //print(func);
    //print(generate);
    //print(inputMatrix);
    //print(matrix);
    //print(numbers);
    //print(o);
    //print(prime);
    //print(start);
    //print(statistic);
    //print(step);
    var result = [];
    for (var i = start; i < arr.length; i += step) {
        result.push(arr[i]);
    }
    return result;
};

dsp.fft = function (x) {
    //print(Complex);
    //print(SimpsonDef);
    //print(SimpsonRecursive);
    //print(basic);
    //print(calculus);
    //print(dsp);
    //print(firstLup);
    //print(func);
    //print(generate);
    //print(inputMatrix);
    //print(matrix);
    //print(numbers);
    //print(o);
    //print(prime);
    //print(statistic);
    //print(x);
    var N = x.length;
    if (N <= 1) {
        return [new Complex(x[0], 0)];
    }
    if (Math.log(N) / Math.LN2 % 1 !== 0) {
        throw new Error("Array length must be integer power of 2");
    }
    var even = dsp.fft(dsp.segment(x, 0, 2));
    var odd = dsp.fft(dsp.segment(x, 1, 2));
    var res = [], Nby2 = N / 2;
    for (var k = 0; k < N; k++) {
        var tmpPhase = -2 * Math.PI * k / N;
        var phasor = new Complex(Math.cos(tmpPhase), Math.sin(tmpPhase));
        if (k < Nby2) {
            res[k] = even[k].add(phasor.multiply(odd[k]));
        } else {
            res[k] = even[k - Nby2].subtract(phasor.multiply(odd[k - Nby2]));
        }
    }
    return res;
};

var generate = numbers.generate;

generate.fibonacci = function (n) {
    //print(Complex);
    //print(SimpsonDef);
    //print(SimpsonRecursive);
    //print(basic);
    //print(calculus);
    //print(dsp);
    //print(firstLup);
    //print(func);
    //print(generate);
    //print(inputMatrix);
    //print(matrix);
    //print(n);
    //print(numbers);
    //print(o);
    //print(prime);
    //print(statistic);
    var bitSystem = function (n) {
        //print(Complex);
        //print(SimpsonDef);
        //print(SimpsonRecursive);
        //print(a);
        //print(b);
        //print(basic);
        //print(bit);
        //print(bitSystem);
        //print(c);
        //print(calculus);
        //print(dsp);
        //print(firstLup);
        //print(func);
        //print(generate);
        //print(i);
        //print(inputMatrix);
        //print(matrix);
        //print(n);
        //print(numbers);
        //print(o);
        //print(prime);
        //print(statistic);
        //print(system);
        //print(temp);
        var bit, bits = [];
        while (n > 0) {
            bit = n < 2 ? n : n % 2;
            n = Math.floor(n / 2);
            bits.push(bit);
        }
        return bits.reverse();
    };
    var a = 1;
    var b = 0;
    var c = 1;
    var system = bitSystem(n);
    var temp;
    for (var i = 0; i < system.length; i++) {
        var bit = system[i];
        if (bit) {
            temp = [(a + c) * b, b * b + c * c];
            a = temp[0];
            b = temp[1];
        } else {
            temp = [a * a + b * b, (a + c) * b];
            a = temp[0];
            b = temp[1];
        }
        c = a + b;
    }
    return b;
};

generate.collatz = function (n, result) {
    //print(Complex);
    //print(SimpsonDef);
    //print(SimpsonRecursive);
    //print(basic);
    //print(calculus);
    //print(dsp);
    //print(firstLup);
    //print(func);
    //print(generate);
    //print(inputMatrix);
    //print(matrix);
    //print(n);
    //print(numbers);
    //print(o);
    //print(prime);
    //print(result);
    //print(statistic);
    result.push(n);
    if (n === 1) {

    } else if (n % 2 === 0) {
        generate.collatz(n / 2, result);
    } else {
        generate.collatz(3 * n + 1, result);
    }
};

var matrix = numbers.matrix;

matrix.deepCopy = function (arr) {
    //print(Complex);
    //print(SimpsonDef);
    //print(SimpsonRecursive);
    //print(arr);
    //print(basic);
    //print(calculus);
    //print(dsp);
    //print(firstLup);
    //print(func);
    //print(generate);
    //print(inputMatrix);
    //print(matrix);
    //print(numbers);
    //print(o);
    //print(prime);
    //print(statistic);
    if (arr[0][0] === undefined) {
        throw new Error("Input cannot be a vector.");
    }
    var result = new Array(arr.length);
    for (var i = 0; i < arr.length; i++) {
        result[i] = arr[i].slice();
    }
    return result;
};

matrix.isSquare = function (arr) {
    //print(Complex);
    //print(SimpsonDef);
    //print(SimpsonRecursive);
    //print(arr);
    //print(basic);
    //print(calculus);
    //print(dsp);
    //print(firstLup);
    //print(func);
    //print(generate);
    //print(inputMatrix);
    //print(matrix);
    //print(numbers);
    //print(o);
    //print(prime);
    //print(statistic);
    var rows = arr.length;
    var cols = arr[0].length;
    return rows === cols;
};

matrix.addition = function (arrA, arrB) {
    //print(Complex);
    //print(SimpsonDef);
    //print(SimpsonRecursive);
    //print(arrA);
    //print(arrB);
    //print(basic);
    //print(calculus);
    //print(dsp);
    //print(firstLup);
    //print(func);
    //print(generate);
    //print(inputMatrix);
    //print(matrix);
    //print(numbers);
    //print(o);
    //print(prime);
    //print(statistic);
    if (arrA.length === arrB.length && arrA[0].length === arrB[0].length) {
        var result = new Array(arrA.length);
        for (var i = 0; i < arrA.length; i++) {
            result[i] = new Array(arrA[i].length);
            for (var j = 0; j < arrA[i].length; j++) {
                result[i][j] = arrA[i][j] + arrB[i][j];
            }
        }
        return result;
    } else {
        throw new Error("Matrix mismatch");
    }
};

matrix.scalar = function (arr, val) {
    //print(Complex);
    //print(SimpsonDef);
    //print(SimpsonRecursive);
    //print(arr);
    //print(basic);
    //print(calculus);
    //print(dsp);
    //print(firstLup);
    //print(func);
    //print(generate);
    //print(inputMatrix);
    //print(matrix);
    //print(numbers);
    //print(o);
    //print(prime);
    //print(statistic);
    //print(val);
    for (var i = 0; i < arr.length; i++) {
        for (var j = 0; j < arr[i].length; j++) {
            arr[i][j] = val * arr[i][j];
        }
    }
    return arr;
};

matrix.transpose = function (arr) {
    //print(Complex);
    //print(SimpsonDef);
    //print(SimpsonRecursive);
    //print(arr);
    //print(basic);
    //print(calculus);
    //print(dsp);
    //print(firstLup);
    //print(func);
    //print(generate);
    //print(inputMatrix);
    //print(matrix);
    //print(numbers);
    //print(o);
    //print(prime);
    //print(statistic);
    var result = new Array(arr[0].length);
    for (var i = 0; i < arr[0].length; i++) {
        result[i] = new Array(arr.length);
        for (var j = 0; j < arr.length; j++) {
            result[i][j] = arr[j][i];
        }
    }
    return result;
};

matrix.identity = function (n) {
    //print(Complex);
    //print(SimpsonDef);
    //print(SimpsonRecursive);
    //print(basic);
    //print(calculus);
    //print(dsp);
    //print(firstLup);
    //print(func);
    //print(generate);
    //print(inputMatrix);
    //print(matrix);
    //print(n);
    //print(numbers);
    //print(o);
    //print(prime);
    //print(statistic);
    var result = new Array(n);
    for (var i = 0; i < n; i++) {
        result[i] = new Array(n);
        for (var j = 0; j < n; j++) {
            result[i][j] = i === j ? 1 : 0;
        }
    }
    return result;
};

matrix.dotproduct = function (vectorA, vectorB) {
    //print(Complex);
    //print(SimpsonDef);
    //print(SimpsonRecursive);
    //print(basic);
    //print(calculus);
    //print(dsp);
    //print(firstLup);
    //print(func);
    //print(generate);
    //print(inputMatrix);
    //print(matrix);
    //print(numbers);
    //print(o);
    //print(prime);
    //print(statistic);
    //print(vectorA);
    //print(vectorB);
    if (vectorA.length === vectorB.length) {
        var result = 0;
        for (var i = 0; i < vectorA.length; i++) {
            result += vectorA[i] * vectorB[i];
        }
        return result;
    } else {
        throw new Error("Vector mismatch");
    }
};

matrix.multiply = function (arrA, arrB) {
    //print(Complex);
    //print(SimpsonDef);
    //print(SimpsonRecursive);
    //print(arrA);
    //print(arrB);
    //print(basic);
    //print(calculus);
    //print(dsp);
    //print(firstLup);
    //print(func);
    //print(generate);
    //print(inputMatrix);
    //print(matrix);
    //print(numbers);
    //print(o);
    //print(prime);
    //print(statistic);
    if (arrA[0].length === arrB.length) {
        var result = new Array(arrA.length);
        for (var x = 0; x < arrA.length; x++) {
            result[x] = new Array(arrB[0].length);
        }
        var arrB_T = matrix.transpose(arrB);
        for (var i = 0; i < result.length; i++) {
            for (var j = 0; j < result[i].length; j++) {
                result[i][j] = matrix.dotproduct(arrA[i], arrB_T[j]);
            }
        }
        return result;
    } else {
        throw new Error("Array mismatch");
    }
};

matrix.determinant = function (m) {
    //print(Complex);
    //print(SimpsonDef);
    //print(SimpsonRecursive);
    //print(basic);
    //print(calculus);
    //print(dsp);
    //print(firstLup);
    //print(func);
    //print(generate);
    //print(inputMatrix);
    //print(m);
    //print(matrix);
    //print(numbers);
    //print(o);
    //print(prime);
    //print(statistic);
    var numRow = m.length;
    var numCol = m[0].length;
    var det = 0;
    var row, col;
    var diagLeft, diagRight;
    if (numRow !== numCol) {
        throw new Error("Not a square matrix.");
    }
    if (numRow === 1) {
        return m[0][0];
    } else if (numRow === 2) {
        return m[0][0] * m[1][1] - m[0][1] * m[1][0];
    }
    for (col = 0; col < numCol; col++) {
        diagLeft = m[0][col];
        diagRight = m[0][col];
        for (row = 1; row < numRow; row++) {
            diagRight *= m[row][((col + row) % numCol + numCol) % numCol];
            diagLeft *= m[row][((col - row) % numCol + numCol) % numCol];
        }
        det += diagRight - diagLeft;
    }
    return det;
};

matrix.lupDecomposition = function (arr) {
    //print(Complex);
    //print(SimpsonDef);
    //print(SimpsonRecursive);
    //print(arr);
    //print(basic);
    //print(calculus);
    //print(dsp);
    //print(firstLup);
    //print(func);
    //print(generate);
    //print(inputMatrix);
    //print(matrix);
    //print(numbers);
    //print(o);
    //print(prime);
    //print(statistic);
    if (!matrix.isSquare(arr)) {
        throw new Error("Matrix must be square.");
    }
    var size = arr.length;
    var LU = matrix.deepCopy(arr);
    var P = matrix.transpose(matrix.identity(size));
    var currentRow;
    var currentColumn = new Array(size);
    this.getL = function (a) {
        //print(Complex);
        //print(LU);
        //print(P);
        //print(SimpsonDef);
        //print(SimpsonRecursive);
        //print(a);
        //print(arr);
        //print(basic);
        //print(calculus);
        //print(currentColumn);
        //print(currentRow);
        //print(dsp);
        //print(firstLup);
        //print(func);
        //print(generate);
        //print(i);
        //print(inputMatrix);
        //print(j);
        //print(k);
        //print(matrix);
        //print(minIndex);
        //print(numbers);
        //print(o);
        //print(pivot);
        //print(prime);
        //print(s);
        //print(size);
        //print(statistic);
        var m = a[0].length;
        var L = matrix.identity(m);
        for (var i = 0; i < m; i++) {
            for (var j = 0; j < m; j++) {
                if (i > j) {
                    L[i][j] = a[i][j];
                }
            }
        }
        return L;
    };
    this.getU = function (a) {
        //print(Complex);
        //print(LU);
        //print(P);
        //print(SimpsonDef);
        //print(SimpsonRecursive);
        //print(a);
        //print(arr);
        //print(basic);
        //print(calculus);
        //print(currentColumn);
        //print(currentRow);
        //print(dsp);
        //print(firstLup);
        //print(func);
        //print(generate);
        //print(i);
        //print(inputMatrix);
        //print(j);
        //print(k);
        //print(matrix);
        //print(minIndex);
        //print(numbers);
        //print(o);
        //print(pivot);
        //print(prime);
        //print(s);
        //print(size);
        //print(statistic);
        var m = a[0].length;
        var U = matrix.identity(m);
        for (var i = 0; i < m; i++) {
            for (var j = 0; j < m; j++) {
                if (i <= j) {
                    U[i][j] = a[i][j];
                }
            }
        }
        return U;
    };
    for (var j = 0; j < size; j++) {
        var i;
        for (i = 0; i < size; i++) {
            currentColumn[i] = LU[i][j];
        }
        for (i = 0; i < size; i++) {
            currentRow = LU[i];
            var minIndex = Math.min(i, j);
            var s = 0;
            for (var k = 0; k < minIndex; k++) {
                s += currentRow[k] * currentColumn[k];
            }
            currentRow[j] = currentColumn[i] -= s;
        }
        var pivot = j;
        for (i = j + 1; i < size; i++) {
            if (Math.abs(currentColumn[i]) > Math.abs(currentColumn[pivot])) {
                pivot = i;
            }
        }
        if (pivot != j) {
            LU = matrix.rowSwitch(LU, pivot, j);
            P = matrix.rowSwitch(P, pivot, j);
        }
        if (j < size && LU[j][j] !== 0) {
            for (i = j + 1; i < size; i++) {
                LU[i][j] /= LU[j][j];
            }
        }
    }
    return [this.getL(LU), this.getU(LU), P];
};

matrix.rotate = function (point, degree, direction) {
    //print(Complex);
    //print(SimpsonDef);
    //print(SimpsonRecursive);
    //print(basic);
    //print(calculus);
    //print(degree);
    //print(direction);
    //print(dsp);
    //print(firstLup);
    //print(func);
    //print(generate);
    //print(inputMatrix);
    //print(matrix);
    //print(numbers);
    //print(o);
    //print(point);
    //print(prime);
    //print(statistic);
    if (point.length === 2) {
        var negate = direction === "clockwise" ? -1 : 1;
        var radians = degree * (Math.PI / 180);
        var transformation = [[Math.cos(radians), -1 * negate * Math.sin(radians)], [negate * Math.sin(radians), Math.cos(radians)]];
        return matrix.multiply(transformation, point);
    } else {
        throw new Error("Only two dimensional operations are supported at this time");
    }
};

matrix.scale = function (point, sx, sy) {
    //print(Complex);
    //print(SimpsonDef);
    //print(SimpsonRecursive);
    //print(basic);
    //print(calculus);
    //print(dsp);
    //print(firstLup);
    //print(func);
    //print(generate);
    //print(inputMatrix);
    //print(matrix);
    //print(numbers);
    //print(o);
    //print(point);
    //print(prime);
    //print(statistic);
    //print(sx);
    //print(sy);
    if (point.length === 2) {
        var transformation = [[sx, 0], [0, sy]];
        return matrix.multiply(transformation, point);
    } else {
        throw new Error("Only two dimensional operations are supported at this time");
    }
};

matrix.shear = function (point, k, direction) {
    //print(Complex);
    //print(SimpsonDef);
    //print(SimpsonRecursive);
    //print(basic);
    //print(calculus);
    //print(direction);
    //print(dsp);
    //print(firstLup);
    //print(func);
    //print(generate);
    //print(inputMatrix);
    //print(k);
    //print(matrix);
    //print(numbers);
    //print(o);
    //print(point);
    //print(prime);
    //print(statistic);
    if (point.length === 2) {
        var xplaceholder = direction === "xaxis" ? k : 0;
        var yplaceholder = direction === "yaxis" ? k : 0;
        var transformation = [[1, xplaceholder], [yplaceholder, 1]];
        return matrix.multiply(transformation, point);
    } else {
        throw new Error("Only two dimensional operations are supported at this time");
    }
};

matrix.affine = function (point, tx, ty) {
    //print(Complex);
    //print(SimpsonDef);
    //print(SimpsonRecursive);
    //print(basic);
    //print(calculus);
    //print(dsp);
    //print(firstLup);
    //print(func);
    //print(generate);
    //print(inputMatrix);
    //print(matrix);
    //print(numbers);
    //print(o);
    //print(point);
    //print(prime);
    //print(statistic);
    //print(tx);
    //print(ty);
    if (point.length === 2) {
        var transformation = [[1, 0, tx], [0, 1, ty], [0, 0, 1]];
        var newpoint = [[point[0][0]], [point[1][0]], [1]];
        var transformed = matrix.multiply(transformation, newpoint);
        return [[transformed[0][0]], [transformed[1][0]]];
    } else {
        throw new Error("Only two dimensional operations are supported at this time");
    }
};

matrix.rowScale = function (m, row, scale) {
    //print(Complex);
    //print(SimpsonDef);
    //print(SimpsonRecursive);
    //print(basic);
    //print(calculus);
    //print(dsp);
    //print(firstLup);
    //print(func);
    //print(generate);
    //print(inputMatrix);
    //print(m);
    //print(matrix);
    //print(numbers);
    //print(o);
    //print(prime);
    //print(row);
    //print(scale);
    //print(statistic);
    var result = new Array(m.length);
    for (var i = 0; i < m.length; i++) {
        result[i] = new Array(m[i].length);
        for (var j = 0; j < m[i].length; j++) {
            if (i === row) {
                result[i][j] = scale * m[i][j];
            } else {
                result[i][j] = m[i][j];
            }
        }
    }
    return result;
};

matrix.rowSwitch = function (m, row1, row2) {
    //print(Complex);
    //print(SimpsonDef);
    //print(SimpsonRecursive);
    //print(basic);
    //print(calculus);
    //print(dsp);
    //print(firstLup);
    //print(func);
    //print(generate);
    //print(inputMatrix);
    //print(m);
    //print(matrix);
    //print(numbers);
    //print(o);
    //print(prime);
    //print(row1);
    //print(row2);
    //print(statistic);
    var result = new Array(m.length);
    for (var i = 0; i < m.length; i++) {
        result[i] = new Array(m[i].length);
        for (var j = 0; j < m[i].length; j++) {
            if (i === row1) {
                result[i][j] = m[row2][j];
            } else if (i === row2) {
                result[i][j] = m[row1][j];
            } else {
                result[i][j] = m[i][j];
            }
        }
    }
    return result;
};

matrix.rowAddMultiple = function (m, from, to, scale) {
    //print(Complex);
    //print(SimpsonDef);
    //print(SimpsonRecursive);
    //print(basic);
    //print(calculus);
    //print(dsp);
    //print(firstLup);
    //print(from);
    //print(func);
    //print(generate);
    //print(inputMatrix);
    //print(m);
    //print(matrix);
    //print(numbers);
    //print(o);
    //print(prime);
    //print(scale);
    //print(statistic);
    //print(to);
    var result = new Array(m.length);
    for (var i = 0; i < m.length; i++) {
        result[i] = new Array(m[i].length);
        for (var j = 0; j < m[i].length; j++) {
            if (i === to) {
                result[to][j] = m[to][j] + scale * m[from][j];
            } else {
                result[i][j] = m[i][j];
            }
        }
    }
    return result;
};

var basic = numbers.basic;

var prime = numbers.prime;

prime.simple = function (val) {
    //print(Complex);
    //print(SimpsonDef);
    //print(SimpsonRecursive);
    //print(basic);
    //print(calculus);
    //print(dsp);
    //print(firstLup);
    //print(func);
    //print(generate);
    //print(inputMatrix);
    //print(matrix);
    //print(numbers);
    //print(o);
    //print(prime);
    //print(statistic);
    //print(val);
    if (val === 1) return false; else if (val === 2) return true; else if (val !== undefined) {
        var start = 1;
        var valSqrt = Math.ceil(Math.sqrt(val));
        while (++start <= valSqrt) {
            if (val % start === 0) {
                return false;
            }
        }
        return true;
    }
};

prime.factorization = function (num) {
    //print(Complex);
    //print(SimpsonDef);
    //print(SimpsonRecursive);
    //print(basic);
    //print(calculus);
    //print(dsp);
    //print(firstLup);
    //print(func);
    //print(generate);
    //print(inputMatrix);
    //print(matrix);
    //print(num);
    //print(numbers);
    //print(o);
    //print(prime);
    //print(statistic);
    num = Math.floor(num);
    var root;
    var factors = [];
    var x;
    var sqrt = Math.sqrt;
    var doLoop = 1 < num && isFinite(num);
    while (doLoop) {
        root = sqrt(num);
        x = 2;
        if (num % x) {
            x = 3;
            while (num % x && (x += 2) < root) {
            }
        }
        x = root < x ? num : x;
        factors.push(x);
        doLoop = x !== num;
        num /= x;
    }
    return factors;
};

prime.millerRabin = function (n, k) {
    //print(Complex);
    //print(SimpsonDef);
    //print(SimpsonRecursive);
    //print(basic);
    //print(calculus);
    //print(dsp);
    //print(firstLup);
    //print(func);
    //print(generate);
    //print(inputMatrix);
    //print(k);
    //print(matrix);
    //print(n);
    //print(numbers);
    //print(o);
    //print(prime);
    //print(statistic);
    if (arguments.length === 1) k = 20;
    if (n === 2) return true;
    if (!basic.isInt(n) || n <= 1 || n % 2 === 0) return false;
    var s = 0;
    var d = n - 1;
    while (true) {
        var dm = basic.divMod(d, 2);
        var quotient = dm[0];
        var remainder = dm[1];
        if (remainder === 1) break;
        s += 1;
        d = quotient;
    }
    var tryComposite = function (a) {
        //print(Complex);
        //print(SimpsonDef);
        //print(SimpsonRecursive);
        //print(a);
        //print(basic);
        //print(calculus);
        //print(d);
        //print(dm);
        //print(dsp);
        //print(firstLup);
        //print(func);
        //print(generate);
        //print(i);
        //print(inputMatrix);
        //print(k);
        //print(matrix);
        //print(n);
        //print(numbers);
        //print(o);
        //print(prime);
        //print(quotient);
        //print(remainder);
        //print(s);
        //print(statistic);
        //print(tryComposite);
        if (basic.powerMod(a, d, n) === 1) return false;
        for (var i = 0; i < s; i++) {
            if (basic.powerMod(a, Math.pow(2, i) * d, n) === n - 1) return false;
        }
        return true;
    };
    for (var i = 0; i < k; i++) {
        var a = 2 + Math.floor(Math.random() * (n - 2 - 2));
        if (tryComposite(a)) return false;
    }
    return true;
};

prime.sieve = function (n) {
    //print(Complex);
    //print(SimpsonDef);
    //print(SimpsonRecursive);
    //print(basic);
    //print(calculus);
    //print(dsp);
    //print(firstLup);
    //print(func);
    //print(generate);
    //print(inputMatrix);
    //print(matrix);
    //print(n);
    //print(numbers);
    //print(o);
    //print(prime);
    //print(statistic);
    if (n < 2) return [];
    var result = [2];
    for (var i = 3; i <= n; i++) {
        var notMultiple = false;
        for (var j in result) {
            notMultiple = notMultiple || 0 === i % result[j];
        }
        if (!notMultiple) {
            result.push(i);
        }
    }
    return result;
};

var statistic = numbers.statistic;

statistic.mean = function (arr) {
    //print(Complex);
    //print(SimpsonDef);
    //print(SimpsonRecursive);
    //print(arr);
    //print(basic);
    //print(calculus);
    //print(dsp);
    //print(firstLup);
    //print(func);
    //print(generate);
    //print(inputMatrix);
    //print(matrix);
    //print(numbers);
    //print(o);
    //print(prime);
    //print(statistic);
    var count = arr.length;
    var sum = basic.sum(arr);
    return sum / count;
};

statistic.median = function (arr) {
    //print(Complex);
    //print(SimpsonDef);
    //print(SimpsonRecursive);
    //print(arr);
    //print(basic);
    //print(calculus);
    //print(dsp);
    //print(firstLup);
    //print(func);
    //print(generate);
    //print(inputMatrix);
    //print(matrix);
    //print(numbers);
    //print(o);
    //print(prime);
    //print(statistic);
    return statistic.quantile(arr, 1, 2);
};

statistic.mode = function (arr) {
    //print(Complex);
    //print(SimpsonDef);
    //print(SimpsonRecursive);
    //print(arr);
    //print(basic);
    //print(calculus);
    //print(dsp);
    //print(firstLup);
    //print(func);
    //print(generate);
    //print(inputMatrix);
    //print(matrix);
    //print(numbers);
    //print(o);
    //print(prime);
    //print(statistic);
    var counts = {};
    for (var i = 0, n = arr.length; i < n; i++) {
        if (counts[arr[i]] === undefined) {
            counts[arr[i]] = 0;
        } else {
            counts[arr[i]]++;
        }
    }
    var highest;
    for (var number in counts) {
        if (counts.hasOwnProperty(number)) {
            if (highest === undefined || counts[number] > counts[highest]) {
                highest = number;
            }
        }
    }
    return Number(highest);
};

statistic.quantile = function (arr, k, q) {
    //print(Complex);
    //print(SimpsonDef);
    //print(SimpsonRecursive);
    //print(arr);
    //print(basic);
    //print(calculus);
    //print(dsp);
    //print(firstLup);
    //print(func);
    //print(generate);
    //print(inputMatrix);
    //print(k);
    //print(matrix);
    //print(numbers);
    //print(o);
    //print(prime);
    //print(q);
    //print(statistic);
    var sorted, count, index;
    if (k === 0) return Math.min.apply(null, arr);
    if (k === q) return Math.max.apply(null, arr);
    sorted = arr.slice(0);
    sorted.sort(function (a, b) {
        //print(Complex);
        //print(SimpsonDef);
        //print(SimpsonRecursive);
        //print(a);
        //print(arr);
        //print(b);
        //print(basic);
        //print(calculus);
        //print(count);
        //print(dsp);
        //print(firstLup);
        //print(func);
        //print(generate);
        //print(index);
        //print(inputMatrix);
        //print(k);
        //print(matrix);
        //print(numbers);
        //print(o);
        //print(prime);
        //print(q);
        //print(sorted);
        //print(statistic);
        return a - b;
    });
    count = sorted.length;
    index = count * k / q;
    if (index % 1 === 0) return .5 * sorted[index - 1] + .5 * sorted[index];
    return sorted[Math.floor(index)];
};

statistic.report = function (array) {
    //print(Complex);
    //print(SimpsonDef);
    //print(SimpsonRecursive);
    //print(array);
    //print(basic);
    //print(calculus);
    //print(dsp);
    //print(firstLup);
    //print(func);
    //print(generate);
    //print(inputMatrix);
    //print(matrix);
    //print(numbers);
    //print(o);
    //print(prime);
    //print(statistic);
    return {
        mean: statistic.mean(array),
        firstQuartile: statistic.quantile(array, 1, 4),
        median: statistic.median(array),
        thirdQuartile: statistic.quantile(array, 3, 4),
        standardDev: statistic.standardDev(array)
    };
};

statistic.randomSample = function (lower, upper, n) {
    //print(Complex);
    //print(SimpsonDef);
    //print(SimpsonRecursive);
    //print(basic);
    //print(calculus);
    //print(dsp);
    //print(firstLup);
    //print(func);
    //print(generate);
    //print(inputMatrix);
    //print(lower);
    //print(matrix);
    //print(n);
    //print(numbers);
    //print(o);
    //print(prime);
    //print(statistic);
    //print(upper);
    var sample = [];
    while (sample.length < n) {
        var temp = Math.random() * upper;
        if (lower <= temp <= upper) {
            sample.push(temp);
        }
    }
    return sample;
};

statistic.standardDev = function (arr) {
    //print(Complex);
    //print(SimpsonDef);
    //print(SimpsonRecursive);
    //print(arr);
    //print(basic);
    //print(calculus);
    //print(dsp);
    //print(firstLup);
    //print(func);
    //print(generate);
    //print(inputMatrix);
    //print(matrix);
    //print(numbers);
    //print(o);
    //print(prime);
    //print(statistic);
    var count = arr.length;
    var mean = statistic.mean(arr);
    var squaredArr = [];
    for (var i = 0; i < arr.length; i++) {
        squaredArr[i] = Math.pow(arr[i] - mean, 2);
    }
    return Math.sqrt(1 / count * basic.sum(squaredArr));
};

statistic.correlation = function (arrX, arrY) {
    //print(Complex);
    //print(SimpsonDef);
    //print(SimpsonRecursive);
    //print(arrX);
    //print(arrY);
    //print(basic);
    //print(calculus);
    //print(dsp);
    //print(firstLup);
    //print(func);
    //print(generate);
    //print(inputMatrix);
    //print(matrix);
    //print(numbers);
    //print(o);
    //print(prime);
    //print(statistic);
    if (arrX.length == arrY.length) {
        var covarXY = statistic.covariance(arrX, arrY);
        var stdDevX = statistic.standardDev(arrX);
        var stdDevY = statistic.standardDev(arrY);
        return covarXY / (stdDevX * stdDevY);
    } else {
        throw new Error("Array mismatch");
    }
};

statistic.rSquared = function (source, regression) {
    //print(Complex);
    //print(SimpsonDef);
    //print(SimpsonRecursive);
    //print(basic);
    //print(calculus);
    //print(dsp);
    //print(firstLup);
    //print(func);
    //print(generate);
    //print(inputMatrix);
    //print(matrix);
    //print(numbers);
    //print(o);
    //print(prime);
    //print(regression);
    //print(source);
    //print(statistic);
    var residualSumOfSquares = basic.sum(source.map(function (d, i) {
        //print(Complex);
        //print(SimpsonDef);
        //print(SimpsonRecursive);
        //print(basic);
        //print(calculus);
        //print(d);
        //print(dsp);
        //print(firstLup);
        //print(func);
        //print(generate);
        //print(i);
        //print(inputMatrix);
        //print(matrix);
        //print(numbers);
        //print(o);
        //print(prime);
        //print(regression);
        //print(residualSumOfSquares);
        //print(source);
        //print(statistic);
        //print(totalSumOfSquares);
        return basic.square(d - regression[i]);
    }));
    var totalSumOfSquares = basic.sum(source.map(function (d) {
        //print(Complex);
        //print(SimpsonDef);
        //print(SimpsonRecursive);
        //print(basic);
        //print(calculus);
        //print(d);
        //print(dsp);
        //print(firstLup);
        //print(func);
        //print(generate);
        //print(inputMatrix);
        //print(matrix);
        //print(numbers);
        //print(o);
        //print(prime);
        //print(regression);
        //print(residualSumOfSquares);
        //print(source);
        //print(statistic);
        //print(totalSumOfSquares);
        return basic.square(d - statistic.mean(source));
    }));
    return 1 - residualSumOfSquares / totalSumOfSquares;
};

statistic.exponentialRegression = function (arrY) {
    //print(Complex);
    //print(SimpsonDef);
    //print(SimpsonRecursive);
    //print(arrY);
    //print(basic);
    //print(calculus);
    //print(dsp);
    //print(firstLup);
    //print(func);
    //print(generate);
    //print(inputMatrix);
    //print(matrix);
    //print(numbers);
    //print(o);
    //print(prime);
    //print(statistic);
    var n = arrY.length;
    var arrX = basic.range(1, n);
    var xSum = basic.sum(arrX);
    var ySum = basic.sum(arrY);
    var yMean = statistic.mean(arrY);
    var yLog = arrY.map(function (d) {
        //print(Complex);
        //print(SimpsonDef);
        //print(SimpsonRecursive);
        //print(a);
        //print(arrX);
        //print(arrY);
        //print(b);
        //print(basic);
        //print(calculus);
        //print(d);
        //print(dsp);
        //print(firstLup);
        //print(fn);
        //print(func);
        //print(generate);
        //print(inputMatrix);
        //print(matrix);
        //print(n);
        //print(numbers);
        //print(o);
        //print(prime);
        //print(statistic);
        //print(xSquared);
        //print(xSquaredSum);
        //print(xSum);
        //print(xyLog);
        //print(xyLogSum);
        //print(yLog);
        //print(yLogSum);
        //print(yMean);
        //print(ySum);
        return Math.log(d);
    });
    var xSquared = arrX.map(function (d) {
        //print(Complex);
        //print(SimpsonDef);
        //print(SimpsonRecursive);
        //print(a);
        //print(arrX);
        //print(arrY);
        //print(b);
        //print(basic);
        //print(calculus);
        //print(d);
        //print(dsp);
        //print(firstLup);
        //print(fn);
        //print(func);
        //print(generate);
        //print(inputMatrix);
        //print(matrix);
        //print(n);
        //print(numbers);
        //print(o);
        //print(prime);
        //print(statistic);
        //print(xSquared);
        //print(xSquaredSum);
        //print(xSum);
        //print(xyLog);
        //print(xyLogSum);
        //print(yLog);
        //print(yLogSum);
        //print(yMean);
        //print(ySum);
        return d * d;
    });
    var xSquaredSum = basic.sum(xSquared);
    var yLogSum = basic.sum(yLog);
    var xyLog = arrX.map(function (d, i) {
        //print(Complex);
        //print(SimpsonDef);
        //print(SimpsonRecursive);
        //print(a);
        //print(arrX);
        //print(arrY);
        //print(b);
        //print(basic);
        //print(calculus);
        //print(d);
        //print(dsp);
        //print(firstLup);
        //print(fn);
        //print(func);
        //print(generate);
        //print(i);
        //print(inputMatrix);
        //print(matrix);
        //print(n);
        //print(numbers);
        //print(o);
        //print(prime);
        //print(statistic);
        //print(xSquared);
        //print(xSquaredSum);
        //print(xSum);
        //print(xyLog);
        //print(xyLogSum);
        //print(yLog);
        //print(yLogSum);
        //print(yMean);
        //print(ySum);
        return d * yLog[i];
    });
    var xyLogSum = basic.sum(xyLog);
    var a = (yLogSum * xSquaredSum - xSum * xyLogSum) / (n * xSquaredSum - xSum * xSum);
    var b = (n * xyLogSum - xSum * yLogSum) / (n * xSquaredSum - xSum * xSum);
    var fn = function (x) {
        //print(Complex);
        //print(SimpsonDef);
        //print(SimpsonRecursive);
        //print(a);
        //print(arrX);
        //print(arrY);
        //print(b);
        //print(basic);
        //print(calculus);
        //print(dsp);
        //print(firstLup);
        //print(fn);
        //print(func);
        //print(generate);
        //print(inputMatrix);
        //print(matrix);
        //print(n);
        //print(numbers);
        //print(o);
        //print(prime);
        //print(statistic);
        //print(x);
        //print(xSquared);
        //print(xSquaredSum);
        //print(xSum);
        //print(xyLog);
        //print(xyLogSum);
        //print(yLog);
        //print(yLogSum);
        //print(yMean);
        //print(ySum);
        if (typeof x === "number") {
            return Math.exp(a) * Math.exp(b * x);
        } else {
            return x.map(function (d) {
                //print(Complex);
                //print(SimpsonDef);
                //print(SimpsonRecursive);
                //print(a);
                //print(arrX);
                //print(arrY);
                //print(b);
                //print(basic);
                //print(calculus);
                //print(d);
                //print(dsp);
                //print(firstLup);
                //print(fn);
                //print(func);
                //print(generate);
                //print(inputMatrix);
                //print(matrix);
                //print(n);
                //print(numbers);
                //print(o);
                //print(prime);
                //print(statistic);
                //print(x);
                //print(xSquared);
                //print(xSquaredSum);
                //print(xSum);
                //print(xyLog);
                //print(xyLogSum);
                //print(yLog);
                //print(yLogSum);
                //print(yMean);
                //print(ySum);
                return Math.exp(a) * Math.exp(b * d);
            });
        }
    };
    fn.rSquared = statistic.rSquared(arrY, arrX.map(fn));
    return fn;
};

statistic.linearRegression = function (arrX, arrY) {
    //print(Complex);
    //print(SimpsonDef);
    //print(SimpsonRecursive);
    //print(arrX);
    //print(arrY);
    //print(basic);
    //print(calculus);
    //print(dsp);
    //print(firstLup);
    //print(func);
    //print(generate);
    //print(inputMatrix);
    //print(matrix);
    //print(numbers);
    //print(o);
    //print(prime);
    //print(statistic);
    var n = arrX.length;
    var xSum = basic.sum(arrX);
    var ySum = basic.sum(arrY);
    var xySum = basic.sum(arrX.map(function (d, i) {
        //print(Complex);
        //print(SimpsonDef);
        //print(SimpsonRecursive);
        //print(a);
        //print(arrX);
        //print(arrY);
        //print(b);
        //print(basic);
        //print(calculus);
        //print(d);
        //print(dsp);
        //print(firstLup);
        //print(func);
        //print(generate);
        //print(i);
        //print(inputMatrix);
        //print(matrix);
        //print(n);
        //print(numbers);
        //print(o);
        //print(prime);
        //print(statistic);
        //print(xMean);
        //print(xSquaredSum);
        //print(xSum);
        //print(xySum);
        //print(yMean);
        //print(ySum);
        return d * arrY[i];
    }));
    var xSquaredSum = basic.sum(arrX.map(function (d) {
        //print(Complex);
        //print(SimpsonDef);
        //print(SimpsonRecursive);
        //print(a);
        //print(arrX);
        //print(arrY);
        //print(b);
        //print(basic);
        //print(calculus);
        //print(d);
        //print(dsp);
        //print(firstLup);
        //print(func);
        //print(generate);
        //print(inputMatrix);
        //print(matrix);
        //print(n);
        //print(numbers);
        //print(o);
        //print(prime);
        //print(statistic);
        //print(xMean);
        //print(xSquaredSum);
        //print(xSum);
        //print(xySum);
        //print(yMean);
        //print(ySum);
        return d * d;
    }));
    var xMean = statistic.mean(arrX);
    var yMean = statistic.mean(arrY);
    var b = (xySum - 1 / n * xSum * ySum) / (xSquaredSum - 1 / n * xSum * xSum);
    var a = yMean - b * xMean;
    return function (x) {
        //print(Complex);
        //print(SimpsonDef);
        //print(SimpsonRecursive);
        //print(a);
        //print(arrX);
        //print(arrY);
        //print(b);
        //print(basic);
        //print(calculus);
        //print(dsp);
        //print(firstLup);
        //print(func);
        //print(generate);
        //print(inputMatrix);
        //print(matrix);
        //print(n);
        //print(numbers);
        //print(o);
        //print(prime);
        //print(statistic);
        //print(x);
        //print(xMean);
        //print(xSquaredSum);
        //print(xSum);
        //print(xySum);
        //print(yMean);
        //print(ySum);
        if (typeof x === "number") {
            return a + b * x;
        } else {
            return x.map(function (d) {
                //print(Complex);
                //print(SimpsonDef);
                //print(SimpsonRecursive);
                //print(a);
                //print(arrX);
                //print(arrY);
                //print(b);
                //print(basic);
                //print(calculus);
                //print(d);
                //print(dsp);
                //print(firstLup);
                //print(func);
                //print(generate);
                //print(inputMatrix);
                //print(matrix);
                //print(n);
                //print(numbers);
                //print(o);
                //print(prime);
                //print(statistic);
                //print(x);
                //print(xMean);
                //print(xSquaredSum);
                //print(xSum);
                //print(xySum);
                //print(yMean);
                //print(ySum);
                return a + b * d;
            });
        }
    };
};

statistic.covariance = function (set1, set2) {
    //print(Complex);
    //print(SimpsonDef);
    //print(SimpsonRecursive);
    //print(basic);
    //print(calculus);
    //print(dsp);
    //print(firstLup);
    //print(func);
    //print(generate);
    //print(inputMatrix);
    //print(matrix);
    //print(numbers);
    //print(o);
    //print(prime);
    //print(set1);
    //print(set2);
    //print(statistic);
    if (set1.length == set2.length) {
        var n = set1.length;
        var total = 0;
        var sum1 = basic.sum(set1);
        var sum2 = basic.sum(set2);
        for (var i = 0; i < n; i++) {
            total += set1[i] * set2[i];
        }
        return (total - sum1 * sum2 / n) / n;
    } else {
        throw new Error("Array mismatch");
    }
};

var o = basic.powerMod(4, 13, 497);

//print(o);

o = basic.egcd(1239, 735);

//print(o);

var func = function (x) {
    //print(Complex);
    //print(SimpsonDef);
    //print(SimpsonRecursive);
    //print(basic);
    //print(calculus);
    //print(dsp);
    //print(firstLup);
    //print(func);
    //print(generate);
    //print(inputMatrix);
    //print(matrix);
    //print(numbers);
    //print(o);
    //print(prime);
    //print(statistic);
    //print(x);
    return 2 * x + 2;
};

var o = calculus.pointDiff(func, 5);

//print(o);

func = function (x) {
    //print(Complex);
    //print(SimpsonDef);
    //print(SimpsonRecursive);
    //print(basic);
    //print(calculus);
    //print(dsp);
    //print(firstLup);
    //print(func);
    //print(generate);
    //print(inputMatrix);
    //print(matrix);
    //print(numbers);
    //print(o);
    //print(prime);
    //print(statistic);
    //print(x);
    return 2 * Math.pow(x, 2);
};

o = calculus.Riemann(func, 0, 100, 10);

//print(o);

o = dsp.fft([1, 1, 1, 1, 0, 0, 0, 0]);

//print(o);

o = generate.fibonacci(79);

//print(o);

var inputMatrix = [[1, 0, 0, 2], [2, -2, 0, 5], [1, -2, -2, 3], [5, -3, -5, 2]];

var firstLup = matrix.lupDecomposition(inputMatrix);

o = prime.millerRabin(999983);

//print(o);

o = prime.factorization(9876543210);

//print(o);

//print(Complex);

//print(SimpsonDef);

//print(SimpsonRecursive);

//print(basic);

//print(calculus);

//print(dsp);

//print(firstLup);

//print(func);

//print(generate);

//print(inputMatrix);

//print(matrix);

//print(numbers);

//print(o);

//print(prime);

print(statistic);