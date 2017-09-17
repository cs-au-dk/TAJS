/*
 *       LOVE IS AN AUTUMN EVENING
 *       js1k demo by Philip Buchanan
 *       philip @ surrealix com
 *
 * In my opinion, the most romantic scene is an autumn evening, just at sunset
 * when the sky turns red and everything looks so beautiful and warm. So I was
 * inspired to write this scene and see how far I could push "high-quality"
 * graphics in 1k. I'm pleased with the result - it looks almost identical to
 * my original 20k concept :).
 * 
 * It took me one day to write the demo, then two weeks to get the size down
 * below the 1k limit. I've commented some of the more interesting optimisations
 * in the code below. 
 * 
 * It runs fullscreen at 30fps in Chrome, Safari & Opera on my I7 laptop. Firefox
 * and IE are a bit slower. If you've got a lower spec machine then running it in
 * a smaller window will probably help things along.
 * 
 * You're welcome to learn from this demo, but please don't rip it off directly!
 */

/*
 * The code below isn't written to be as small as possible, instead it's written
 * to be as repetitive as possible (albeit in as small space as possible). Up to
 * a certain limit, unraveled code compresses better than the corresponding loop.
 * 
 * Compression is using JScrush (http://www.iteral.com/jscrush/) 
 * 
 * To strip the comments and extra semi-colons, the code is first run through the
 * closure compiler (http://closure-compiler.appspot.com/home)
 */

/*
 * List of 1-letter variables
 *
 * a context
 * b body
 * c element
 * h canvas height
 * l function parameter
 * p   "
 * r   "
 * s local temporary variable
 * t time/counter
 * u local temporary variable
 * w canvas width
 * y function parameter
 *
 * B branches array
 * G generate branch (1st) / render loop (2nd)
 * L leaves array
 */

/*
 * Okay, let's get started ...
 */
w = c.width = window.innerWidth-25;
h = c.height = window.innerHeight-25;

/*
 * Initialisation
 */
t = -0.4; // wait a moment before shedding leaves
B=[]; // branches [242]

/*
 * Leaves array [703], with the inital value of an island. This data takes up
 * more space than I want, but it's integral to the illusion.
 */
L=[[1, h-115, 1,       //X, Y, Z
        -150, 0,     //vertex1 X, vertex1 Y
        0, -15,      //vertex1 X, vertex1 Y
        150, 0,      //vertex1 X, vertex1 Y
        60, 9, 0]]; // R, G, B


/*
 * Create Tree (saving everything as angle/height/radius instead of X/Y/Z allows
 * us to save a trig operator every time we calculate the projection).
 */
G = function(p, y, r, l, s) { // angle, y, radius, depth, spacing
    // create some leaves
    for(i=2*(l>3); i--;)
        L.push([p + Math.random()/9,
                y + Math.random()*30-15, 
                r + Math.random()*30-15,
            Math.random()*40-20, Math.random()*40-20,
            Math.random()*40-20, Math.random()*40-20, 
            Math.random()*40-20, Math.random()*40-20,
            Math.random()*40*5+40, Math.random()*40, 20]);// color
        
    if(l < 6)
        // child branches
        for(
            // Note that generating the offset here only happens once because
            // it's not part of the for-loop.
            B.push([ p, y, r], 
            [p+=Math.random()*2-1+(l==2)*s,
             y-=Math.random()*60+20 - r/5,
             r+=Math.random()*25*s-5, 
             12-l*2]),
            // but this stuff happens 3 times
            s=3;s--;)
                G(p, y, r, l+1, s*2)
    
};G(9, h-80, 0, 1, 0);


/*
 * The rendering function is pretty straight forward, just long.
 * Compression works best when functions have the same declaration, so we just 
 * copy the longest declaration and run with it. Additionally, math calls 
 * (Math.sin, cos, random) get replaced more efficiently than re-mapping them.
 */
setInterval(function (p, y, r, l, s) { // counter, unused
    /*
     * Time updates for controlling the wind, the sun, the rotation, etc
     */
    t += 0.01; 

    /*
     * Render Sky
     */
    a.fillStyle = a.createRadialGradient(720, t*40-99, 1, 460, 460, 900);
    a.fillStyle.addColorStop(0.06, '#fda');
    a.fillStyle.addColorStop(0.07, '#fc4');
    a.fillStyle.addColorStop(0.2, '#e65');
    a.fillStyle.addColorStop(1, '#326');
    a.fillRect(0, 0, w, h);
    
    /*
     * Render leaves behind the tree. Wild abuse of the comma operator abounds -
     * see tree rendering for details.
     * 
     * To maintain the 3d illusion the leaves are drawn in two batches, one in
     * front, and one behind the tree. Depth sorting the leaves took too much
     * space, and repeating this code twice doesn't increase the file size
     * overly because it's identical and therefore highly compressable.
     */
    for(s=703;s--;) //703 = leaves array size
    Math.sin(L[s][0]+t)<0 && ( //depth test
        // Original position
        p = L[s][1],
        q = Math.cos(L[s][0]+t) * L[s][2] + 275, //offsetx
        
        // Leaf shedding (xspeed, difference/wait)
        // if we get to choose numbers, 99 is better than 100
        u = (t*9 - s%300)*99,
        
        // Leaf rotation
        q += u *= (u > 0) * !!s,
        p += u * Math.sin(u/99)/9+0.4,
        
        a.fillStyle = 'rgba('+~~L[s][9]+','+~~L[s][10]+','+~~L[s][11]+',0.8)',
        a.beginPath(),
        a.moveTo(L[s][3]+q, L[s][4]*Math.cos(u/=14)+p),
        a.lineTo(L[s][5]+q, L[s][6]*Math.cos(u/=14)+p),
        a.lineTo(L[s][7]+q, L[s][8]*Math.cos(u/=14)+p),
        a.closePath(),
        a.fill()
    )
    
    /*
     * Render Tree. Here we abuse the comma and ternary operators to replace an 
     * if/else statement. As a bonus, we save several braces and semicolons 
     * by nesting function calls in array access and other places that already have
     * brackets, so that we don't need to create our own pair.
     */
    for(s=242;s--;)
        s%2 ?
            a.lineWidth=B[a.beginPath(),s][
            a.moveTo(Math.cos(B[s][0]+t)*B[s][2]+275, B[s][1])    ,3]
        :
            a.stroke(a.lineTo(Math.cos(B[s][0]+t)*B[s][2]+275, B[s][1]))
    
    /*
     * Rendering leaves in front of the tree
     */
    for(s=703;s--;) //703 = leaves array size
    Math.sin(L[s][0]+t)>0 && ( //depth test
        // Original position
        p = L[s][1],
        q = Math.cos(L[s][0]+t) * L[s][2] + 275, //offsetx
        
        // Leaf shedding (xspeed, difference/wait)
        // if we get to choose numbers, 99 is better than 100
        u = (t*9 - s%300)*99,
        
        // Leaf rotation
        q += u *= (u > 0) * !!s,
        p += u * Math.sin(u/99)/9+0.4,
        
        a.fillStyle = 'rgba('+~~L[s][9]+','+~~L[s][10]+','+~~L[s][11]+',0.8)',
        a.beginPath(),
        a.moveTo(L[s][3]+q, L[s][4]*Math.cos(u/=14)+p),
        a.lineTo(L[s][5]+q, L[s][6]*Math.cos(u/=14)+p),
        a.lineTo(L[s][7]+q, L[s][8]*Math.cos(u/=14)+p),
        a.closePath(),
        a.fill()
    )
    
    /*
     * Render Text. We use the same colour as the background to increase
     * compresability, plus it makes the heart look as though it fades in.
     * 
     * We have to specify a font family to set the size, but it doesn't 
     * need to be valid ...
     *
     */
    a.fillStyle = '#e65';
    a.font='120px x';
    a.fillText('❦', 640, h-150);
    
    /*
     * Render Reflection
     */
    for(y=120;y--;) // 100 = water height
        // getImageData        340 = elementheight - waterheight 900 = element.width
        a.putImageData( a.getImageData(0, h-120 - y + ~~(Math.sin(t*9+y/8)*y/5), w, 1 ),
        // putImageData
        ~~(Math.sin(t*9+y/4)*y/9), h-120 + y);
    
    a.fillStyle = 'rgba(0,0,99,0.1)';
    a.fillRect(0, h-120, w, 120)  // magic numbers : element.height * 0.75, element.width, element.height * 0.25

}, 50);