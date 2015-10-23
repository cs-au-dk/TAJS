//////////////////////////////////////////////////////////////////////////
//                             .-.  .-.
//      _   _ ___ ___ ___ ____(%%%\/%%%)__ ____ ____ ___ ___ _   _
//     | |_| | __| _ | _ |_   _\%%%%%%/ _ |_   |   _| _ |  _| |_| |
//     |  _  | __|   |  _| | |  \%%%%/|   | | | | | |   | |_|  _ <
//     |_| |_|___|_|_|_|_| |_|   \%%/ |_|_| |_| |_| |_|_|___|_| |_|
//                                \/                           
//                                      by @veubeke
//////////////////////////////////////////////////////////////////////////
// see http://games.23inch.de/td/ for full documentation
//////////////////////////////////////////////////////////////////////////
// characters used ...
//
// for variables
//   local             e       i       m           s         x y
//   global    a   c                 l   n o p   r
//             A B                               R S T     W X Y
// in program  a b c d e f g h i     l m n o p   r s t u v w x y 
//             A B             I                 R S T     W X Y 
// packing                       j k           q                 z
//  	           C D E F G H   J K L   N O P Q       U V       Z

// cost map generation
genMap = function(e,i,x,y) {
  for(i = dir = [], // directions
      m = [], // cost map
      // starting with the heart, iterate over squares in frontier
      s=[{x:7,y:4,e:0}]; e=s.shift();)
    // check all adjacent squares
    for(i=2;2+i--;)
      x = e.x-i%2,
      y = e.y-~i%2,
      // if square is on the board and new path is shorter...
      (x|y)>>3 || e.e+2>m[x*8+y] || (
        // update directions
        dir[x*8+y] = {x:i%2,y:~i%2},
        // keep going if square is free
        board[x*8+y] || s.push({x:x,y:y,e:m[x*8+y] = e.e+1}));
  // return true if the path hasn't been blocked
  return ''+creeps.filter(function(e,i,x,y) { s = dir[e.x*8+e.y]; return !s }) ||
    !m[start] || board[60]
},

// game setup
setup = function(e,i,x,y) {
  genMap(
    // game board
    board = [],
    // animation step / timer
    step =
    // enemies
    creeps = [],
    // paused state
    paused =
    // money
    money = 3
  );
  // generate map on startup
  life = 8
},
setup();

// click handler
onclick = function(e,i,x,y) {
  // set up game if over
  life || setup();
  x = e.pageX-16 >> 5,
  y = e.pageY-16 >> 5,
  // pause when clicking outside of the board, unpause if paused
  (x|y)>>3 || paused ? paused = !paused
    : board[x*8+y]
      ? e.shiftKey
        // sell tower 
        ? genMap(board[x*8+y]=0,++money)
        // upgrade tower
        : board[x*8+y].e+1>money/3 || board[x*8+y].e>1 || (money-=++board[x*8+y].e*3)
      : 2>money || (
        // try placing tower and...
        genMap(board[x*8+y] = {x:x,y:y,e:0})
          // remove if is blocking the path...
          ? genMap(board[x*8+y]=0)
          // or charge player
          : money -= 2
        )
},

// text drawing
drawText = function(e,i,x,y) {
  a.font = i+'px Arial',
  // draw text relative to board...
  // and add difference between alphabetic baseline and middle
  a.fillText(e,32+x,32+y+i*.3)
},

// main loop
setInterval(function(e,i,x,y) {
    // draw background
    for(i = c.height = 290,
        wave = step/20 >> 4; i--;)
      a.globalAlpha = 1-i/1320,
      a.fillRect(0,i,290,1);
  
    a.fillStyle = '#fff',

    // pause game if no life left
    paused |= !life;

    // draw grid
    for(i=8; i--;)
      a.globalAlpha = .2,
      a.fillRect(32+i*32,32,1,225),
      a.fillRect(32,32+i*32,225,1);

    // print info
    drawText('$'+money+'  ∿'+wave,20,a.globalAlpha=1,245);
    a.textAlign = 'center',

    // draw pause/retry symbol or heart and arrow to show game state
    drawText('↻▶'[life&&1],paused?30:10,paused?112:-10,paused?112:start*32);
    paused || drawText('♥',life*2+14,7*32,4*32,a.fillStyle = '#f00');

    a.fillStyle = '#fff',
    // update creeps
    creeps = creeps.filter(function(e,i,x,y) {
      s = dir[e.x*8+e.y];
      // update position
      paused || step%32 || (e.x += s.x, e.y += s.y);
      // draw creep
      drawText('☠',e.e/e.m*16+11|0,step%32*s.x+e.x*32,step%32*s.y+e.y*32);
      // remove creeps that are dead or reached the heart
      return paused || e.x*8+e.y-60 ? e.e>0 || !++money : life--&0
    });
  
    // update towers
    board.filter(function(e,i,x,y) {
      s = dir[e.x*8+e.y];
      // inactive towers are darker
      a.globalAlpha = .6,
      (ee = e) &&
        // draw tower
        drawText('♖♜♛'[e.e],30,e.x*32,e.y*32,
          // find creeps near tower...
          paused || creeps.filter(function(e,i,x,y) {
            s = dir[e.x*8+e.y];
            return 4>(i=step%32*s.x/32+e.x-ee.x)*i+(i=step%32*s.y/32+e.y-ee.y)*i
          }).filter(function(e,i,x,y) {
            s = dir[e.x*8+e.y];
            // and shoot at them
            i && 2>ee.e || (a.globalAlpha = 1, e.e -= ee.e+1)
          }))
    });
  
    // add new creeps every ~15s
    paused || step++%32 || step%320/4>24+wave || creeps.push({x:0,y:start,e:wave=(wave+55)*2*wave||50,m:wave})
}, 48)