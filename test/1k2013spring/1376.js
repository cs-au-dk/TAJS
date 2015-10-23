/***************************************************************************

    Color Factors - js1k 2013-Spring
    Pablo Caro Martín - pcaro.es

    Welcome to Color Factors!

    In this world, each point represents a number, starting from 2, to the
    infinite. Each one of these number is connected with its factors by an
    arc. The colors of the points come from a spring-like rainbow, and the
    arcs have a gradient that comes from the colors of the points they are
    connecting. However, all primes are white, just because they are cool.
    The "n" in the top-left corner indicates the last number, "primes" the
    number of prime numbers found, and "last" the last prime number found.
    If you click anywhere in the screen, the TURBO MODE will be activated!

    Cool fact #1: Without the leading spaces, all the lines in the above
        paragraph are 70 chars long. 70's factors are 35, 14, 10, 7, 5 and 2.

    Obvious examples:
        - 10 is connected with 5, 2, and with its multiples (20, 30, 40...)
        - 20 is connected to 10, 5 and 2 (and its multiples)
        - 30 is connected to 10, 6, 5, 3 and 2 (and its multiples)
        - 31 is only connected with it multiples, because it's prime.

    The demo has been tested in OS X, in the following browsers:
        - Chrome: Works nicely.
        - Safari: Works nicely.
        - Firefox: Works, but really slow after a few numbers are added.
        - Opera: Works well, after applying a workaround to avoid a fix
            regarding the transformation of arcs.

    In order to minify the code, I wrote my own "JSCompressor" in Python,
    based on the original JSCrush by Aivo Paas (www.iteral.com/jscrush), but
    with some functionalities that help when trying to find an optimal
    minimizable code, obtaining an even more reduced output. Everything
    else has been done manually, with some help from my JSCompressor script.

    After the compression, the code length is 1019 characters, which is a
    prime number. Nice.

    Cool fact #2: The obvious property of prime numbers (they don't have
        factors besides of 1 and themselves) leads to the fact that they are
        only connected with points in their right side, never in their left
        side.


    Names used (and where can that character be found):

        a: 2d context
        b: document body
        c: canvas element
        C: color function (addColorStop)
        d: dinc (innerWidth)
        e: inc (innerHeight)
        f: ninc (function)

        g: gradient (innerHeight)
        h: height (innerWidth)

        i: loop index (innerWidth)
        I: list of factors (setInterval)
        k: loop index (strokeStyle)
        l: last prime found (fillStyle)
        n: numbers (innerWidth)

        p: primes found (addColorStop)

        r: rinc (innerWidth)
        S: add_point function (addColorStop)
        u: movement function (function)
        v: movement increment
        w: width (innerWidth)
        x: aux (fillText)

    Cool fact #3: The most used character in this script before compression is
        "i", with 74 appearances; followed by "(" and ")", with 73 appearances
        each; and by "=" and ";", tied with 59 appearances each (without
        counting the comments!).

***************************************************************************/


    // No margins. No scroll. Stay cool.
    b.style.margin=0;
    b.style.overflow="hidden";
    w=c.width=innerWidth;
    h=c.height=innerHeight;
    a.font="32px arial";

    // Variable inicialization
    I=[];
    e=w/2;
    n=-1;
    p=r=f=d=i=k=0;
    v=132;

    // Rainbow color!
    C=function(x){
        x%=96;
        return "rgb("
            +(x<16||x>=80?255:x>=64?(-64+x)*16:x<32?(32-x)*16:0)+","
            +(x>=16&&x<48?255:x<16?(x)*16:x<64?(64-x)*16:0)+","
            +(x>=48&&x<80?255:x>=80?(96-x)*16:x>=32?(-32+x)*16:0)+")"
    };

    // Add a new value
    S=function(x){
        d=e-w/(++n+2);
        I.push([]);
        if(n>0)  // Everyone loves 2
        for(i=0;i<(n+1)/2;i++)
            if((n+2)%(i+2)==0)  // Join with every factor
            // if((n+2)%(i+2)==0&&I[i].length==0) // Join with primes only
                I[n].push(i);
        r=0;
        0==I[n].length&&(p++,l=n+2);
        u()
    };

    // Movement
    u=function(x){
        e-=d/v;
        ++r<v?setTimeout(u,1):S()
    };

    // Drawing function
    setInterval(function(x){
        a.fillStyle="#000";
        a.fillRect(0,0,w,h);

        // Arcs
        for(i=0;i<=n;i++)
            for(k=0;k<I[i].length;k++){
                a.moveTo(e*(i+1),3*h/5);
                a.save();
                a.setTransform(
                    (i-I[i][k])/2,0,0,
                    (I[i][k]+2)/2,e*(i+I[i][k]+2)/2,3*h/5);
                a.beginPath();
                a.arc(0,0,e,0,(i%2?1:-1)*3.14*(i==n?r/v:1),i%2==0);
                a.restore();
                g=a.createLinearGradient(e*(I[i][k]+1),0,e*(i+1),0);
                g.addColorStop(0,C(I[i][k]));
                g.addColorStop(1,C(i));
                a.strokeStyle=g;
                a.stroke()
            }

        g=a.createLinearGradient(e,0,e*n,0);
        for(i=0;i<n;i+=16)
            g.addColorStop(i/n,C(i));
        g.addColorStop(1,C(n));

        // Color points (non primes)
        a.beginPath();
        for(i=0;i<=n;i++)
            0<I[i].length&&a.arc(e*(i+1),3*h/5,4,0,8);
        a.fillStyle=g;
        a.fill();

        // White points (primes)
        a.beginPath();
        for(i=0;i<=n;i++)
            0==I[i].length&&a.arc(e*(i+1),3*h/5,4,0,8);
        a.fillStyle="#fff";
        a.fill();

        a.fillText("n: "+(n+2),16,40);
        a.fillText("Primes: "+p+"  Last: "+l,16,80);
        3<v&&a.fillText("Click for turbo!",16,h-16)
    },40);

    c.onclick=function(x){v=v>3?3:132};

    S()