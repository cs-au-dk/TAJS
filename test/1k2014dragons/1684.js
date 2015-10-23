/* 
A Tiny Game of Life
conway's game of life in 1k of js (when minified)

LICENSE 

The MIT License (MIT)

Copyright (c) 2014 David Vokoun <david@vokoun.me>

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.



  rules of life:
    1.) Any live cell with fewer than two live neighbors dies, as if caused by under-population.
    2.) Any live cell with two or three live neighbors lives on to the next generation.
    3.) Any live cell with more than three live neighbors dies, as if by overcrowding.
    4.) Any dead cell with exactly three live neighbors becomes a live cell, as if by reproduction.


*/
 

var w = window.innerWidth
  , h = window.innerHeight

  // alias for x,y separator when finding cell key
  , _ = ','

  // cell size
  , s = 10

  // screen width,height in cells
  , sw = w/s|0
  , sh = h/s|0

  // cells
  , z = {}
 
  // cell buffer
  , zb = {}

  // cell seed
  , zs = {} 
  
  // are we in running mode?
  , r = 0

  // are we animating?
  , a = 0

  // n of generations
  , g = 0

  ;


// Methods

// each: callback with each key in obj
function E(o,c) {
  for(var k in o) c(k);
}

// get: get the state of a cell (true/false)
function G(x,y) {
  return z[x+_+y] || 0;
}

// get Neighbors: find an int value of alive neighbors
function N(x,y) {
  return  G(x-1, y-1) + G(x, y-1) + G(x+1, y-1) +
          G(x-1, y) +               G(x+1, y) +
          G(x-1, y+1) + G(x, y+1) + G(x+1, y+1);
}

// step: evolve the system one generation
function S() {
  g++;

  z = JSON.parse(JSON.stringify(zb));

  for(var y=0; y<sh; y++) {
    for(var x=0; x<sw; x++) {
      
      var n = N(x,y); 
      
      // is alive
      if(G(x,y)) {

        if(n < 2 || n > 3)
          delete zb[x+_+y];
      
      } else {

        if(n == 3)
          zb[x+_+y] = 1;
      
      }
  
    } 
  }

  P(zb, 1);
}

// paint: the cells and brief help text
function P(o,f) {
  c.clearRect(0,0,w,h);
  
  E(o, function(k) {
    var p = k.split(_);
    c[f?'fillRect':'strokeRect'](p[0]*s, p[1]*s, s, s);
  });
  
  c.fillText('A)ANIM S)NEW D)EDIT F)FORW <'+(r?'R':'E')+'> G'+g ,s,s);
}

// Go ahead and paint it
P(zs);

// animate: automatically animate generations
function A() {
  if(a) {
    S();
    requestAnimationFrame(A);
  }
}

// Event Handlers
window.onclick = function(e) {
  // toggle cell at click
  if(!r) {
    var x = e.clientX/s|0
      , y = e.clientY/s|0
      ;

    if(!zs[x+_+y])
      zs[x+_+y] = 1;
    else
      delete zs[x+_+y];
    
    P(zs);
  }
};

window.onkeydown = function(e) {
  var k = e.which;

  if(k == 70) { // f key: step forward or start
    if(r) {
      S();
    } else {
      E(zs, function(k) {
        zb[k] = zs[k];
        z[k] = zs[k];
      });
      r = 1;
      P(zb,1);
    }
  }
  else if(k == 68) { // d key, go back to edit 
    // reset back to the cell seed
    z={};
    zb={};
    g=0;
    r = 0; a = 0;
    P(zs);
  }
  else if(k==83) { // s key, start fresh
    // reset all vars
    z={};
    zb={};
    zs={};
    g=0;
    r = 0; a = 0;
    P({});
  }
  else if(k == 65) { // a key, animate
    if(r) {
      if(!a) {
        a = 1;
        A();
      } else {
        a = 0;
      }
    }
  }
};