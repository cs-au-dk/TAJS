counter = offset = y = 0;
c.width = w = window.innerWidth;
c.height = h = window.innerHeight;
c.style.background = '#000';
setInterval( function() {
  a.fillStyle = 'rgba(0,0,0,0.6)';
  a.fillRect(0,0,w,h);
  for( var i=0; i<=h; i+=h/20 ) {
    y = w / 6 *  Math.sin( counter +offset ) + w / 2;
    r = 30 * Math.sin( counter ) + 50;
    r2 = 30 * Math.cos( counter ) + 50;
    a.fillStyle = 'hsl(' + i + ',' + r2 * 2 + '%,50%)';
    a.font = r + 'px Arial';
    a.fillText( '♥', -y+w, i );
    a.font = r2 + 'px Arial';
    a.fillText( '♥', y, i );
    offset += 0.2;
  }
  offset = 0;
  counter += .04
}, 30 );