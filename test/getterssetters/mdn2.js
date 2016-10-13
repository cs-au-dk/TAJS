var d = Date.prototype;
Object.defineProperty(d, "year", {
  get: function() { return this.getFullYear() },
  set: function(y) { this.setFullYear(y) }
});

TAJS_assert(d.year, 'isMaybeAnyNum');

d.year = 2001;

TAJS_assert(d.year, 'isMaybeAnyNum');