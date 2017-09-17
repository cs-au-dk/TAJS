var LS = localStorage
  , K = 'js1ktodo'
  , S = function() {
          LS.setItem(K, JSON.stringify(l));
          draw();
        }
  , L =  {
      set: function(t, i) { // t - null means add.
        t? l[i] = t : l[cur = l.length] = T();
        S(l);
      }
    , del: function() {
        l.splice(cur, 1);
        cur? cur-- : (l.length? 0 : L.set(T(), 0));
        S(l);
      }
    , edit: function(c) {
        var t = l[cur];

        t.s = c? (t.s + String.fromCharCode(c)) : t.s.substr(0, t.s.length - 1);
        L.set(t, cur);
    }
    , mv: function(u) { // u - Up or down
        cur += u? ((cur == l.length - 1)? 0 : 1) : (cur? -1 : 0);
        draw();
      }
    }
  , T = function() {
      return {
        s: ''
      };
    }
  , l = JSON.parse(LS.getItem(K)) || [T()]
  , cur = l.length - 1;

draw = function() {
  a.clearRect(0, 0, c.width, c.height);
  var i = l.length - 1
    , j = 0;
  for (i; i >= 0; i--) {
    drawT(l[i], 20*j++, (i == cur));
  }
};
drawT = function(t, y, cur) {
  a.beginPath();
  a.rect(0, y, c.width, 20);
  a.fillStyle = cur? '#ffd' : '#ff5';
  a.fill();
  a.strokeStyle = cur? '#555' : '#bbb';
  a.stroke();
 
  a.font = '14px Verdana';
  a.fillStyle = '#000';
  a.fillText(t.s, 8, y + 14);
};

b.onkeydown = function(evt) {
  var k = evt.which
    , m = {
        8: L.edit // Backspace
      , 13: L.set // Enter
      , 46: L.del // Del
      , 38: L.mv // Up
      , 40: L.mv // Down
      };
  m[k]? m[k]((k == 38)? 1 : 0) : 0;
  (k == 8)? evt.preventDefault() : 0;
};
b.onkeypress = function(evt) {
  evt.charCode? L.edit(evt.charCode) : 0;
};

c.height = 600;
draw();