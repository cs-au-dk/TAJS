// thanks to malte for finding the right sprites and his second pair of eyes
// and thanks to @p01 for the string suggestions
m = '~ÿÿÿÿÿÿ~<zr$6LL66\\\\6 v6PP6v '

c.style.display = 'block' // thanks to @veubeke
w = c.width = innerWidth
h = c.height = innerHeight
s = 8 // scale
P = w * h + h // Player Position
F = S = 1 // Score

f = [] // frontier
g = [] // the grid
e = [] // entities

// direction to carve
c = [ 0, 2, 0, -2 ]

onkeyup = function( p ) {
  if ( ++F ) {
    // player movement
    // thanks to: http://js1k.com/2013-spring/demo/1367
    p = p.which - 38
    p = P + p%2 + --p%2 * w

    // check collision with walls
    if ( g[p] > 1 ) { 
      P = p
      if ( g[p] > 2 ) { // collect tresure
        g[p] = 2
        S++
      }
    }
  }
  else {
    if ( s > 0 ) setTimeout( onkeyup, 99, s-- )
  }

  // entity movement
  for ( i in e ) {
    // horizontal
    u = ( P % w < e[i] % w ) ? -1 : 
        ( P % w > e[i] % w ) ? 1 : 
        0

    // vertical
    o = ( P < e[i] ) ? -1 : 
        ( P > e[i] ) ? 1 : 
        0

    e[i] += u & g[e[i] + u] > 1 ? u : g[e[i] + o * w] > 1 ? o * w : 0

    if ( e[i] == P ) { // collision with player
      onkeyup(e[i] = F = NaN)
    }
  }

  for ( p = P;; ) {
    // add and mark
    g[p] = 2
    if ( !g[p-w*2] ) g[p-w] = f[p-w*2] = 1
    if ( !g[p+2] )   g[p+1] = f[p+2] = 1
    if ( !g[p+w*2] ) g[p+w] = f[p+w*2] = 1
    if ( !g[p-2] )   g[p-1] = f[p-2] = 1
    
    // add tresure
    if ( Math.random() < .01 & P != p ) {
      g[p] = 3
    }
    
    // select frontier in range
    p = 1
    for ( i in f ) {
      if ( f[i] & Math.abs( i % w - P % w ) + Math.abs( (i / w | 0) - P / w | 0 ) < s ) {
        p = f[i] = 0
        break
      }
    }

    // no frontier in range, abort
    if ( p ) break
    
    p = i * 1
    
    // add entities
    if ( Math.random() < .01 * S ) {
      e.push( p )
    }
    
    // carve to nearest way
    // and mark sourroundings as wall
    o = Math.random() * 4 | 0
    for ( i = 4; i--; o = ++o%4 ) {
      if ( g[p + c[o] + c[(o+1)%4] * w] > 1 ) {
        g[((p / w | 0) + c[(o+1)%4] / 2) * w + p % w + c[o] / 2] = 2
        if ( Math.random() > .1 ) break
      }
    }
  }

  a.fillRect( 0, 0, w, h )

  u = P % w - w/2 / (s*2) | 0 // camera x offset
  o = (P / w | 0 ) - h/2 / (s*2) | 0 // camera y offset

  for ( x = s; x--; ) {
    for ( y = s; y--; ) {
      a.fillStyle = '#fff' // tan
      // draw maze
      for ( i in g ) {
        p = m.charCodeAt( x + (g[i]-1) * s)
        p = p == 1 ? 0 : p // thanks to opera
        if ( !( p >> y & 1) ) {
          a.fillRect( 
            (i % w - u) * s * 2 + x*2, 
            ((i / w | 0) - o) * s * 2 + y*2, 
            2, 2
          )
        }
      }
      
      a.fillStyle = '#000' //red
      
      // draw enemies
      for ( i in e ) {
        if ( m.charCodeAt( x + 24 + F%2*s ) >> y & 1 ) {
          a.fillRect( 
            (e[i] % w - u) * s * 2 + x*2, 
            ((e[i] / w | 0) - o) * s * 2 + y*2, 
            2, 2
          )
        }
      }
      
      // draw player
      if ( m.charCodeAt( x + 40 + F%2*s ) >> y & 1 ) {
        a.fillRect( 
          (P % w - u) * s * 2 + x*2, 
          ((P / w | 0) - o) * s * 2 + y*2, 
          2, 2
        )
      }
    }
  }
  
  top.document.title = S - 1 + ' ⬖'
}

onkeyup({})