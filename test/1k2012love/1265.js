///////////////////////////////////////////////////////////////////////////
//                     _   ♥             _
//                ,---¦ '-,-,-----,---, ¦ '-,---,---,---,
//                ¦ --¦ , ¦ ¦ , , ¦ -_¦ ¦ , ¦ -_¦ ,-¦ - ¦
//                '---'-'-'-'-'-'-'---' '-'-'---'-' '---'
//                 it's only rock'n'roll (but I like it)
//
//////////////////////////////////////////////////// by @veubeke //////////
//
// This is a simple music game based on a glockenspiel. Notes played run
// down four streams. Click the stream once a note reaches the bar at the
// bottom to win the hearts of your audience. Notes you hit will disappear;
// hitting multiple notes in a row increases your multiplier while missing
// too many notes in a row mutes the music until you hit another one.
// The songs played are automatically generated but quite catchy. ;)
//
// The following code is already pretty minimal. To get it down to 1k you
// just need to remove comments and whitespace and replace variable names.
// No magic needed. For the full documentation visit
//
//  http://games.23inch.de/chime/
//
// Features:
//
//  + Like a tune? Click to restart it!
//  + Don't like a tune? Reload to generate a new one!
//  + The music only plays as long as you play.
//  + It's all shiny and shit.
//  + No 'eval', no 'with', no compression.
//
// Issues:
//
//  - There are some artifacts in the sound which is to be expected when
//    rounding all values to save space.
//  - The game doesn't work in IE9 because apparently you can't have Audio
//    and btoa() (base64 encoding) at the same time.
//
// Acknowledgements:
//
//  I had the basic idea after seeing @chrissmoak's entry for the first
//  js1k which also helped me with figuring out how to construct the audio.
//
//   http://js1k.com/2010-first/demo/806
//
//  There are a lot of optimizations in here, many of which I saw being
//  used in earlier js1k contests. Unfortunately, it's hard to tell who
//  came up with something first. However, the last few bytes were shaved
//  using some of these tips:
//
//   https://github.com/jed/140bytes/wiki/Byte-saving-techniques
//
///////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////////
/// Create Tune (315 bytes)
///////////////////////////////////////////////////////////////////////////

math = Math;

for(tune = [hit = i = track = '']; i<32;)
  for(k = l = 11025,
      v = i?i-31&&(v+math.random()*3-1)%6|0:1,
      tune[i++] = v+1>>1; k--;)
    track += String.fromCharCode(
      math.sin('28?KTd'.charCodeAt(v)*k/597)
      *math.min((l-k)/83, k/l)
      *(i%2&&i%8-3?99:33)+128);

player = new Audio('data:audio/wav;base64,UklGRqSIFQBXQVZFZm10IBAAAAA\
BAAEARKwAAESsAAABAAgAZGF0YYCI' + btoa('\0'+track+track+track+track));

///////////////////////////////////////////////////////////////////////////
/// Handle Clicks / Draw Stuff (146 bytes)
///////////////////////////////////////////////////////////////////////////

string = 4;

onclick = draw = function(x,y,w,h) {
  h ? a.fillRect(cwidth/2+x*scale-w/2,y,w,h)
    : w ? a.fillText(w,cwidth/2+x,194+y)
        : (y=(x.pageX-cwidth/2)/math.pow(1.01,x.pageY)+16>>3)>>2
            ?0: string = y
};

///////////////////////////////////////////////////////////////////////////
/// Game Loop (559 bytes)
///////////////////////////////////////////////////////////////////////////

setInterval(function() {
  step = player.currentTime%32*20|0;

  step | string&4 || player.play(hearts = 0, hit = 15, mult = 1);

  scale = 4;
  draw(y=0,0,cwidth=c.width=innerWidth,cheight=c.height=300);

  for(a.font = 4+a.font.slice(a.shadowBlur=1); y<cheight; y++)
    for(k = 5, scale /= .99,
        f = step+110-y/2, d = f/10|0,
        a.globalAlpha = y/cheight; k--; 
        k-4 ? draw(k*2-3,y,1.2*scale,1)
        : y-198 || draw(0,y,260,4)&draw(cheight,103,'♥ '+(hit&&hearts))
          & draw(111,0,'♪ '+(step?mult:''))
          & draw(-130,-step*2,'chime  hero'))
      a.fillStyle = s = k-4 && f%10<8 | hit>=d | d>63 | d%4==1 
        | k-tune[d%16*2] ? '#'+'fa33a0'.substr(k,3) : Q='#fff',
      a.shadowColor = k-string ? s : Q;

  if(n = step/5+1>>1)
    mult = n%4 && hit<n-3 ? 0 : mult,
    n%4-1 && hit<n & string==tune[n%16*2] && (hit = n, hearts += ++mult),
    player.volume = mult | !step ? 1:1e-8;

  string = 4
}, 50)