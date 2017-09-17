Object.defineProperty(Date.prototype, "year", {
  get: function() { return this.getFullYear() },
  set: function(y) { this.setFullYear(y) }
});
var d = new Date();

TAJS_assert(d.year, 'isMaybeAnyNum');

d.year = 2001;

TAJS_assert(d.year, 'isMaybeAnyNum');