a.style.cssText = 'width:auto;height:auto;margin:auto;position:absolute;top:0;right:0;bottom:0;left:0'; // reset css dimensions and center
b.style.background = '#000'; // dark background
lt = Date.now() // last time
et = 0 // elapsed time
M = Math
reset()
loop()

function reset() {
	// new dimensions
	w = a.width = innerWidth * .5
	h = a.height = innerHeight * .5
	dim = ( w + h ) / 8 // base dimension
	cols = M.ceil( w / dim ) + 1 // columns
	rows = M.ceil( h / dim ) * 4 + 2 // rows
	div = 15 // timing division for translation and rotation
	shift = dim / 4 // horizontal shifting
	rot = M.PI / 40 // rotation range
	lw1 = M.max( 2, dim / 50 ) // outline width
	lw2 = M.max( 6, dim / 12 ) // shadow width
}

function loop() {
	requestAnimationFrame( loop )
	for( i = rows; i-- ; ) { // loop through rows
		xoff = ( shift / 2 ) + M.sin( ( et + i ) / div ) * shift // x offset for this row
		y = ( ( i - 2 ) * dim + dim / 2 ) / 4 // y value for row
		hue = 100 - ( i * 4 + -et / 2 ) // hue for row
		for( j = cols; j--; ) { // loop through columns
			x = i % 2 ? j * dim + dim / 2 : j * dim // x value for column - staggered
			c.save()
			// move and spin
			c.translate( x - xoff, y )
			c.rotate( ( M.PI / 4 ) +  M.cos( ( et + i ) / div ) * rot )
			c.beginPath()
			// main shape
			c.rect( -dim / 2, -dim / 2, dim, dim )
			// outline
			c.lineWidth = lw1
			c.strokeStyle = M.random() > .95 ? 'hsl(' + hue + ',100%,' + ( 60 + M.random() * 20 ) + '%)' : 'hsl(' + hue + ',100%,60%)'
			c.stroke()
			// shadow
			c.lineWidth = lw2
			c.strokeStyle = 'rgba(0,0,0,.25)'
			c.stroke()
			// main fill
			c.fillStyle = 'hsl(' + hue + ',80%,20%)'
			c.fill()
			c.restore()
		}
	}
	// elapsed time
	now = Date.now()
	et += ( now - lt ) / 16.6
	lt = now
}

// scale to the window
onresize = reset