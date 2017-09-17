(function ( ctx, body, canvas, colors, x, y, c1, c2, c3, text, count, comp, txt, fontsize ) {
  colors = ['#ff0077','#AAEE22', '#04DBE5', '#FFB412'];
  text = ['i miss you.', 'i love you.', 'you\'re cute.', 'you\'re perfect.', 'i\'ll see you soon?', 'you are beautiful.', 'remember when...', 'you are my best friend.', 'i wish you could stay..', '..just one more night.' ];
  comp = ['xor','lighter'];
  txt = "px Futura, Helvetica, sans-serif";
  c1 = c2 = c3 = count = 0;
  rn = function(z){ return ~~(Math.random()*z) }
  canvas.width  = x = window.screen.width;
  canvas.height = y = window.screen.height;
  fontsize = (x+y)/40;
  ctx.fillStyle = '#222';
  ctx.fillRect(0,0,x,y);
  ctx.shadowBlur = 100;
  
  function gradientFn() {
      ctx.font = fontsize + txt;
      ctx.globalCompositeOperation = 'source-over';
      ctx.fillText(text[c3++%10], fontsize + rn(x/1.5), fontsize + rn(y/1.5));
  }

  (function draw() {
    if (!(count%15)){
        body.style.backgroundColor = colors[c1++%4];
    }
    ctx.shadowColor = colors[count%4];
    if (!(count%200)){
        gradientFn();
    }
    if (!(count++%50)){
      ctx.globalCompositeOperation = comp[(c2++)%2];
    }
    ctx.fillText('♥', rn(x), rn(y));
    setTimeout(draw, 10);
  })();

})(a,b,c);