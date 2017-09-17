//closure=true pretty=false externs=["html5","es5"] crush=true

// ^ junk for my compiler, uses closure + jscrush

/*

                                                 /===-_---~~~~~~~~~------____
                                                |===-~___                _,-'
                 -==\\                         `//~\\   ~~~~`---.___.-~~
             ______-==|                         | |  \\           _-~`
       __--~~~  ,-/-==\\                        | |   `\        ,'
    _-~       /'    |  \\                      / /      \      /
  .'        /       |   \\                   /' /        \   /'
 /  ____  /         |    \`\.__/-~~ ~ \ _ _/'  /          \/'
/-'~    ~~~~~---__  |     ~-/~         ( )   /'        _--~`
                  \_|      /        _)   ;  ),   __--~~
                    '~~--_/      _-~/-  / \   '-~ \
                   {\__--_/}    / \\_>- )<__\      \
                   /'   (_/  _-~  | |__>--<__|      |
                  |0  0 _/) )-~     | |__>--<__|      |
                  / /~ ,_/       / /__>---<__/      |
                 o o _//        /-~_>---<__-~      /
                 (^(~          /~_>---<__-      _-~
                ,/|           /__>--<__/     _-~
             ,//('(          |__>--<__|     /                  .----_
            ( ( '))          |__>--<__|    |                 /' _---_~\
         `-)) )) (           |__>--<__|    |               /'  /     ~\`\
        ,/,'//( (             \__>--<__\    \            /'  //        ||
      ,( ( ((, ))              ~-__>--<_~-_  ~--____---~' _/'/        /'
    `~/  )` ) ,/|                 ~-_~>--<_/-__       __-~ _/
  ._-~//( )/ )) `                    ~~-'_/_/ /~~~~~~~__--~
   ;'( ')/ ,)(                              ~~~~~~~~~~
  ' ') '( (/
    '   '  `

  2048bytes.js

  author: Johan Nordberg <code cinnamon-bun johan-nordberg.com>
  license: MIT

*/

"DRAGON DOGE" // padding because i want it to compile to exactly 2048 bytes :)

d()

// globals
S = 0 // total score
G = {} // the grid lookup
C = document.createElement("h4") // board container
H = { // key event handlers, 0 is options for compacting passes 1 is for the merge pass
  37: [2, 6], // left
  38: [0, 4], // up
  39: [3, 7], // right
  40: [1, 5]  // down
};
// Q, Z, R, W used for temp variables

function adopt(parent, child) {
  parent.appendChild(child)
}

function setPos(el, pos) {
  el.style.left = 8 + pos[0] * 128 + "px"
  el.style.top = 8 + pos[1] * 128 + "px"
}

Z = /webkit/i.test(navigator.userAgent)
Q = document.createElement("style")
Q.textContent = ("" + // closure fixes this
  "@Akeyframes a{0%{Atransform:scale(1)}50%{Atransform:scale(1.2)}100%{Atransform:scale(1)}}" +
  "@Akeyframes b{0%{Atransform:scale(.5)}100%{Atransform:scale(1)}}" +
  "body{background:#f7f2eb}" +
  "*{font:128px/0'helvetica neue',helvetica;text-align:center;border-radius:5px;" +
  "Bbox-sizing:border-box;margin:0;font-weight:100;color:#fff}" +
  "h4{background:#e9e3d8;margin:0 auto;position:absolute;left:50%;top:50%;" +
  "margin-left:-264px;margin-top:-264px;height:528px;width:528px}" +
  "h1,h2{position:absolute;width:128px;height:128px;padding:8px}" +
  "h1{z-index:0}h2,.h0{z-index:2;visibility:hidden}" +
  "h3{font-size:.5em;height:100%;padding-top:50%}" +
  "h1 h3{background:#e3dcce;z-index:0}" +
  "h5{padding-top:50%;z-index:2;position:absolute;height:528px;width:528px;background:#222;opacity:.4}" +
  ".a{visibility:visible;transition:all .1s}.b{Aanimation:.2s a}.c{z-index:1}.d{Aanimation:.2s b}" +
  ".h1,.h2,.h3{color:#222}" +
  ".h7,.h8,.h9{font-size:.4em}" +
  ".h10,.h11{font-size:.3em;text-shadow:0 0 5px #fff}" +
  ".h1{background:#fffae8}.h2{background:#fff3d4}" +
  ".h3{background:#ffeda3}.h4{background:#f38630}" +
  ".h5{background:#69D2e7}.h6{background:#5bb280}" +
  ".h7{background:#556270}.h8{background:#c44d58}" +
  ".h9{background:#ce8aeb}.h10{background:#ff3d7f}" +
  ".h11{background:#222;font-weight:600}")
    .replace(/A/g, Z ? "-webkit-" : "").replace(/B/g, Z ? "" : "-moz-")

adopt(b, Q)

P = function(dir, fn) {
  // iterate over grid calling fn for every item
  var x, y, c = 0;
  if (dir) for (x = 3; x > -1; --x) for (y = 3; y > -1; --y) c+= fn(x, y, ""+x+y, G[""+x+y])
  else for (x = 0; x < 4; ++x) for (y = 0; y < 4; ++y) c+= fn(x, y, ""+x+y, G[""+x+y])
  if (c) P(dir, fn)
}

GG = function() {
  // GEEE GEEEEE
  Q = document.createElement("h5")
  Q.innerHTML = S
  Q.className = "b"
  adopt(C, Q)
  H = {} // make it stop by removing all handlers
}

window.onkeydown = function(event) {
  if (!(R = H[event.keyCode])) return;

  C.focus() // the iframe was causing some flickering on chrome
            // this seems to fix it

  W = 0 // reset move counter
  gridupdate(R[0])
  gridupdate(R[1])
  gridupdate(R[0])
  if (W) setTimeout(add, 140)
  draw()

  // figure out if a move is possible
  W = 0 // possible moves * 2
  P(0, function(x, y, pos, tile) {

    // above
    --y
    if ((Q = G[""+x+y]) && (!Q.value || Q.value == tile.value)) W++
    ++y

    // below
    ++y
    if ((Q = G[""+x+y]) && (!Q.value || Q.value == tile.value)) W++
    --y

    // left
    --x
    if ((Q = G[""+x+y]) && (!Q.value || Q.value == tile.value)) W++
    ++x

    // right
    ++x
    if ((Q = G[""+x+y]) && (!Q.value || Q.value == tile.value)) W++
  })

  if (!W) GG()
}

// create initial tiles
P(0, function(x, y, pos, tile) {
  // Q = bg tile

  Q = document.createElement("h1")
  Q.innerHTML = "<h3>"
  setPos(Q, pos)

  adopt(C, Q)

  tile = {
    value: 0,
    el: document.createElement("h2"),
    pos: pos
  }

  adopt(C, tile.el)
  G[pos] = tile
})

function draw() {
  P(0, function(x, y, pos, tile) {
    tile.el.className = ""

    if (tile.value === 0) {
      if (tile.el.merge) {
        tile.el.className = "a c"
        pos = tile.el.merge
        setTimeout(function() {
          tile.el.innerHTML = "<h3 class=" + ("h" + ~~(Math.log(tile.value) / 0.69)) + ">" + tile.value
        }, 100)
      }
    } else {
      tile.el.className = "a"
      if (tile.el.merge) {
        setTimeout(function() {
          tile.el.innerHTML = "<h3 class=" + ("h" + ~~(Math.log(tile.value) / 0.69)) + ">" + tile.value
          tile.el.className = "a b"
        }, 100)
      } else {
        tile.el.innerHTML = "<h3 class=" + ("h" + ~~(Math.log(tile.value) / 0.69)) + ">" + tile.value
      }
    }
    setPos(tile.el, pos)
    tile.el.merge = 0
  })
}

function gridupdate(opts) {
  // update passes
  // opts: dir = 1, col = 2, merge = 4
  // W = number of moves
  P(opts & 1, function(x, y, pos, tile) {
    var next, tmp

    if (opts & 2) {
      x += opts & 1 ? 1 : -1
    } else {
      y += opts & 1 ? 1 : -1
    }

    if ((next = G[""+x+y]) && tile.value &&
        // merge pass
        ((opts & 4 && next.value == tile.value) ||
        // compact pass
        (!(opts & 4) && next.value == 0))
    ) {
      ++W
      next.value += tile.value
      tile.value = 0

      if (opts & 4 && next.value) {
        S += next.value
        if (next.value > 2e3) GG()
        tile.el.merge = next.el.merge = ""+x+y
      }

      tmp = next.el
      next.el = tile.el
      tile.el = tmp

      return !(opts & 4)
    }
    return 0
  })
}

function add() {
  // add random tile
  // R = list of empty tiles
  R = []
  P(0, function(x, y, pos, tile) {
    if (!tile.value) R.push(tile)
  })

  var tile;
  if (tile = R[~~(Math.random() * R.length)]) {
      tile.value = Math.random()>.1?2:4
      tile.el.innerHTML = "<h3 class=" + ("h" + ~~(Math.log(tile.value) / 0.69)) + ">" + tile.value
      tile.el.className = ""
      setPos(tile.el, tile.pos)
      setTimeout(function() {
        tile.el.className = "a d"
      }, 10)
  }
}

// 2 starting tiles
add()
add()

// add container to body
adopt(b, C)

// initial draw
draw()