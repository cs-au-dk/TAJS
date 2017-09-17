function Circle(radius) {
    this.radius = radius;
}

Object.defineProperty(Circle.prototype, 'circumference', {
    get: function () {
        return 2 * Math.PI * this.radius;
    }
});

Object.defineProperty(Circle.prototype, 'area', {
    get: function () {
        return Math.PI * this.radius * this.radius;
    }
});

c = new Circle(10);
TAJS_assert(c.area > 314);
TAJS_assert(c.area < 315);
TAJS_assert(c.circumference > 62);
TAJS_assert(c.circumference < 63);
