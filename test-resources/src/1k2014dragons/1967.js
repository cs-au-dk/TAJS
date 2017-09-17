var drawingfn = null;
var time = 0;
var dictionary = {
  // Math
  "+": "s.push(s.pop()+s.pop());",
  "*": "s.push(s.pop()*s.pop());",
  "^": "s.push(s.pop()^s.pop());",
  "%": "var v=s.pop();s.push(s.pop()%v);",
  "<": "var v=s.pop();s.push(s.pop()<v);",
  "s": "s.push(Math.sin(s.pop()));",
//  "cos": "s.push(Math.cos(s.pop()));",
//  "tan": "s.push(Math.tan(s.pop()));",
  // Stack shuffling
  "d": "var v=s.pop();s.push(v);s.push(v);",
  "p": "s.pop();",
  "x": "var v=s.pop(),w=s.pop();s.push(v);s.push(w);",
 // "rot": "var v = s.pop(), w = s.pop(), x =s.pop(); s.push(w,v,x);",
  "?": "var v=s.pop(),w=s.pop(),x=s.pop();s.push(x?w:v);",
  "c": "s.pop()();",
  "h": "var v=s.pop(),w=s.pop();s.push(v);s.pop()();s.push(w);"
  // debug
 // "dbg": "console.log(s.pop());"
};
var specials = {
  ":": function(dict, tokens, idx) {
    var end = matchingIndex(":", ";", tokens, idx);
    dict[tokens[idx+1]] = compile(dict, tokens.slice(idx+2,end));
    return ["", end];
  },
  "[": function(dict, tokens, idx) {
    var end = matchingIndex("[", "]", tokens, idx);
    return ["s.push(function(){" + compile(dict, tokens.slice(idx+1,end)) + "});", end];
  },
  "(": function(dict, tokens, idx) {
    var end = matchingIndex("(", ")", tokens, idx);
    return ["", end];
  }
//  "'": function(_, tokens, idx) { return ["s.push('" + tokens[idx+1] + "');", idx+1]; }
};

var matchingIndex = function(left, right, tokens, startIdx) {
  var i = startIdx +  1, balance = 1;
  while(balance > 0 && i < tokens.length) { // todo: forloopify
    if(tokens[i] == left) balance++;
    else if(tokens[i] == right) balance--;
    i++;
  }
  return --i;
}

var compile = function(dict, tokens) {
    var str = "";
    for(var i = 0; i < tokens.length; i++) {
        var token = tokens[i];
        if(specials[token]) {
            ret = specials[token](dict, tokens, i);
            str += ret[0];
            i = ret[1];
        } else {
            str += dict[token] || ("s.push(" + token + ");");
        }
    }
    return str;
}

var i = function (stack, dict, str){
  var js = compile(dict, str.trim().split(/\s+/g));
  return new Function("s", js)(stack);
}

//Drawing
var draw = function() {
  if(!drawingfn) return;

  var img = c.createImageData(a.width, a.height);
  time++ //= Date.now();//%512;
  var s = [];
  for(var y = 0; y < img.height; y++) {
    for(var x = 0; x < img.width; x++) {
      var index = (x + y * img.width) * 4;
      s.push(time, y, x);
      //var s = [time, y, x];
      drawingfn(s);
      //var b = s.pop(), g = s.pop(), r = s.pop();
      var color = s.pop();
      img.data[index++] = color & 255;   // r
      img.data[index++] = (color >> 8) & 255;   // g
      img.data[index++] = (color >> 16) & 255;   // b
      img.data[index++] = 255; // a
    }
  }
  c.putImageData(img, 0, 0);
}

// TODO: real dip, better solution ot swapping out draw?

var init = function() {
  onresize(256,256);
  t = document.createElement("textarea");
  t.value = ": r [ x ] h x ;\n: draw r d 512 % 256 < [ r r ^ * ] [ * * s 512 * ] ? c ;";
  b.appendChild(t);
  // Shift + Enter to eval and draw
  t.onkeydown = function(e) {
    if(e.which == 13 && t.value) {
      var stack = [];
      var dict = Object.create(dictionary);
      i(stack, dict, t.value);
      drawingfn = dict.draw ? new Function("s", dict.draw) : null;
      draw();

   //   e.preventDefault(); // stop Enters newline
    }
  };
  setInterval(draw, 50);
}

init();