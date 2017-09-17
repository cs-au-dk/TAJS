I = []
w = []
e = [
  //# empty
  { c: "#000" }, 
  //# cell
  { c: "#888" }, 
  //# player
  { 
    c: "#ff0", 
    p: [0, 0], 
    m: function(p, c) { 
      with(this) {
        I[p] = 1
        p = [(t = p[0] + d[0]) < 0 ? 0 : t > 49 ? 49 : t, (t = p[1] + d[1]) < 0 ? 0 : t > 49 ? 49 : t]
        //# fly or hedgehog
        if(I[p] > 2) {
          S = 0
        }
        //# empty cell
        if(I[p] == 0) {
          //# new cell for { w }
          w.push(p)
          return
        }
        //# web cell
        if(I[p] == 1 && w[0]) {
          if(w.join(b).match("\\b" + p + "\\b")) {
            return
          }
          with({ c: "#fff", i: 0, p: p }) {
            c = v = []
            f = []
            u = function(p, c) {
              if(I[p] < 0) {
                return i
              }
              if(c[p] < 2 || c[p] == i) {
                return c[p] 
              }
              if(e[I[p]].d) {
                return c == f
              }
              if(I[p]) {
                return i
              }
              c[p] = i
              c[p] = (u([p[0] + 1, p[1]], c) && u([p[0] - 1, p[1]], c)
                  && u([p[0], p[1] + 1], c) && u([p[0], p[1] - 1], c)
              )
              if(c == f) {
                return I[p] = 1
              }
              return c[p]
            }
            //? avoid new var
            for(i = 4; p = w[i / 4 - 1];) {
              u([p[0] + 1, p[1]], c) && u([p[0] + 1, p[1]], f)  
              ++i
              u([p[0] - 1, p[1]], c) && u([p[0] - 1, p[1]], f)  
              ++i
              u([p[0], p[1] + 1], c) && u([p[0], p[1] + 1], f)  
              ++i
              u([p[0], p[1] - 1], c) && u([p[0], p[1] - 1], f)  
              ++i
            }
          }
          w = []
        }      
      }
    }
  }
]

c.height = c.width = 200

p = []
for(p[1] = 50; p[1]--;) {
  for(p[0] = 50; p[0]--;) {
    I[p] = 0 | Math.random() + 5e-3
  }
}  

d = [0, 0]
for(p[0] = 50; p[0]--;) {
  I[[p[0], 0]] = I[[p[0],  1]] = I[[p[0], 49]] = I[[p[0], 48]] = 1 
} 
for(p[1] = 50; p[1]--;) {
  I[[0, p[1] + d[1]]] = I[[1, p[1] + d[1]]] = I[[49, p[1] + d[1]]] = I[[48, p[1] + d[1]]] = 1 
}

for(i = 0; 9 > ++i;) {
  p = [0 | Math.random() * 50, 0 | Math.random() * 50]
  //e[I[[x, p[1]]] = i + 3] = (I[[x, p[1]]] 
  e.push(I[p] 
    //# hedgehog
    ? { 
      c: "#080", 
      //# old cell
      p: p, 
      m: function(p, c) {
        with(this) {
          //# random movement by web, but bit closer to player
          for(I[p] = 1; I[u = [(t = (e[2].p[0] - p[0] > 0 ? 1 : -1) * (0 | Math.random() * 3 - 1.3) + p[0]) < 0 ? 0 : t > 49 ? 49 : t, (t = (e[2].p[1] - p[1] > 0 ? 1 : -1) * (0 | Math.random() * 3 - 1.3) + p[1]) < 0 ? 0 : t > 49 ? 49 : t]] - 1;);
          p = u
        }
      }  
    }
    //# fly
    : { 
      c: "#fff", 
      d: [(0 | Math.random() * 2) * 2 - 1, (0 | Math.random() * 2) * 2 - 1], 
      p: p, 
      m: function(p, c) {
        with(this) {
          I[p] = 0
          
          if(I[[p[0] + d[0], p[1] + d[1]]] && I[[p[0] + d[0], p[1]]] == 0 && I[[p[0], p[1] + d[1]]] == 0) {
            //? try avoid { join }
            //? dont work
            if(w.join(b).match("\\b" + [p[0] + d[0], p[1] + d[1]] + "\\b")) {
              S = 0
            }
            d[0] = -d[0]
            d[1] = -d[1]
          }
          if(I[[p[0] + d[0], p[1]]]) {
            if(w.join(b).match("\\b" + [p[0] + d[0], p[1]] + "\\b")) {
              S = 0
            }
            d[0] = -d[0]
          }
          if(I[[p[0], p[1] + d[1]]]) {
            if(w.join(b).match("\\b" + [p[0], p[1] + d[1]] + "\\b")) {
              S = 0
            }
            d[1] = -d[1]
          }
          
          p = [p[0] + d[0], p[1] + d[1]]
        }
      }  
    }
  )
}
//setInterval(s, 1e4)
onkeydown = function(p, c) {
  c = p.which - 37
  if(c >= 0 && c < 4) {
    d = [(c & 2) - 1, 0]
    if(c & 1) {
      d = [d[1], d[0]]
    }
  }
}

//get from compressor for free
S = 1

setInterval(function(p, c) {
  if(S) {
    //# move
    for(i = 1; p = e[++i];) {
      p.m()
      I[p.p] = i
    } 
    //# draw
    p = []
    for(p[1] = 50; p[1]--;) {
      for(p[0] = 50; p[0]--;) {
        a.fillStyle = e[I[p]].c
        a.fillRect(4 * p[0], p[1] * 4, 4, 4)
      }
    }
  }
}, 40)