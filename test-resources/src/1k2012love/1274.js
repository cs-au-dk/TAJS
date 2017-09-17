/*
 * 1K JavaScript Speech Synthesizer
 *
 * This is a simple formant based speech synthesizer in less than 1K of JavaScript.
 * Synthesizes speech as you type, and whole sentences upon pressing ENTER.
 *
 * The folllowing sounds/phonemes are supported:
 *
 * a,b,d,e,E,f,g,h,i,j,k,l,m,n,o,p,r,s,S,T,t,u,v,w,z,Z
 *
 * Hope you like this entry for JS1K#4
 *
 * Based on Tiny Speech Synth by Stepanov Andrey - http://www.pouet.net/prod.php?which=50530
 * Optimized and minified manually, by yours truly, @p01 - http://www.p01.org/releases/
 * Compressed using First Crush by @tpdown - http://js1k.com/2012-love/demo/1189
 *
 * To go under 1K, I had to limit the synthesizer to two formant filters using either a sawtooth or noise and discard plosive sounds. In other words I had to sacrifice quality a bit.
 *
 * Mathieu 'p01' Henri - @p01 - http://www.p01.org/releases/
 *
 */

// title and fullsize input
document.write('<h1>1K JavaScript Speech Synthesizer<input id=d value="diz is a spich syntheSizer in oan kay. type your text and press enter" style=position:fixed;background:transparent;top:0;left:0;width:99%;height:99%>');
// keypress handler
(onkeypress=function(e)
{
	// loop through either the whole text or the current keypress
	for(M=!e||e.which==13?document.getElementById('d').value:String.fromCharCode(e.which),S='',h=g=l=k=s=0;s<M.length;s+=1/1024,S+=String.fromCharCode(t>255?255:t<0?0:0|t))

		// sliding window of the formant filter + check if we have formant info to proess the current character
		if(f=g,g=h,
		j=k,k=l,
t=128,p={o:[52,55,10,10,6],i:[45,96,10,10,3],j:[45,96,10,10,3],u:[45,54,10,10,3],a:[58,70,10,10,15],e:[54,90,10,10,15],E:[60,80,10,10,12],w:[43,54,10,10,1],v:[42,60,20,10,3],T:[42,60,40,1,5],z:[45,68,10,5,3],Z:[44,70,50,1,5],b:[44,44,10,10,2],d:[44,99,10,10,2],m:[44,60,10,10,2],n:[44,99,10,10,2],r:[43,50,30,8,3],l:[48,60,10,10,5],g:[42,50,15,5,1],f:[48,60,10,10,4,1],h:[62,66,30,10,10,1],s:[120,150,80,40,5,1],S:[20,70,99,99,10,1],p:[44,50,5,10,2,1],t:[44,60,10,20,3,1],k:[60,99,10,10,6,1]}[M[0|s]])
			// 2 formant filters
			i=1-p[2]/255,
			m=1-p[3]/255,
			h=i*(g*2*Math.sin(p[0]/25)-i*f)+(p[5]?Math.random():s*16%1)-.5,
			l=m*(k*2*Math.sin(p[1]/25)-m*j)+(p[5]?Math.random():s*16%1)-.5,
			//
			t+=Math.min(1,4*Math.sin(Math.PI*s))*((h+l)*p[4]+(g+k)/2+(f+j)/8);

	// generate and play a WAVE PCM file
	t='data:audio/wav;base64,UklGRl9vT19XQVZFZm10IBAAAAABAAEAQB8AAEAfAAABAAgA',new Audio(t+btoa(t+S)).play()
})
// synthesize the default sentence right away
()