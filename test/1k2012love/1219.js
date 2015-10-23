// setup
for (c.width = c.height = W = 510, a.font = '3em a', z = j = 15, N=99, f = 255, F=40, E=50, S = 0, T = 10, l = [], o = [], p = Math, q = 2 * p.PI, r = p.random, s = p.sqrt, t = p.abs, P=p.pow,

// spacebar hits
onkeyup = onkeydown = function(d) {
    o[0][0] = d.type[5] ? 1 : -1
}, 

// function to handle graphing
A = function(d, b, g, e , w) {
    with (a)
        e+1 && (fillStyle = '#'+'0ff0f009000fff'.substr(e , 3)), 
        w ? fillText( g , d , b ) :
        (beginPath(), arc(d, b, g, q, 0, 0), fill());
    return A
}, 

// draw our hippos
C = function(d, b, g, e, w) {
    
    // make sure we are within our bounds
    z < (b+=d) ? b = z : 0 > b && (b = 0);    

    // stop the hippo if at the bounds
    b % z || (d = b && j ? -1 : 0 );

	// set the movement
    for (u = 0, k = 8 * b - f, 
		V = function( Z , M , U , G , D ){
			A( f + g * ( k + M ) - ( g || Z ) ,  f + e * ( k + M ) - ( e || Z ) , U , G , D )
			return V
		}; 
		
	// iterate through balls	
	n = l[u++]; )

        // check if eaten
        if (1 ^ n) 
			with ( n )
    
            	// draw if first time through
	            j ||  (

	                // get acceleration
	                v = F < s( P(x-f,2) + P(y-f,2) ) ? -.15 : .4,

	                // get velocity and position
	                x += X += v/2 * (x > f ? 1 : -1) - 1e-4,
	                y += Y += v/2 * (y > f ? 1 : -1),

	                // draw ball
	                A(x, y, z, 11)
	            ),

	            // check if we have eaten the ball!
	            s( P(x - f - g * ( k + E ) , 2 ) + P(y - f - e * ( k + E ) , 2 ) ) < F-T && 
                
	                // bump
	                (~d ? (X=g*3*d||X) && (Y=e*3*d||Y) :
                
	                // eat
	                S++ && (1 ^ n && w++, l[u - 1] = 1));
        
	
	// draw the score
    A( T+j*E , E , w , j , 1 );
		
    // draw our hippo
    V( 0, E , F+b/3 ) ( 0 , z , E )
	
	// draw the eyes
	( z , F , T+b/3 , 11 )
 	( -z , F , T+b/3 )
	( z , 44 , 5 , 8 )
	( -z , 44 , 5 )
 
    return arguments

}; --j + 1; )

    // generate balls
    l.push({x: N + f * r(),y: N + f * r(),X: 0,Y: 0}) && 

    // generate hippos
    4 > j && o.push([0, 0, j && 2 - j, 3 - j && 1 - j, 0]);


setInterval(function() {
    
    // draw board
    A(f, f, W, 7)(f, f, f, 4);

    // iterate through hippos and update
    for (j = 0, I = 0|r()*N; i = o[j];j++)
        o[j] = C.apply(c, i);

    // randomly make an AI hippo bite
    o[++I] && S <= z && (o[I][0] = 1)

}, z)