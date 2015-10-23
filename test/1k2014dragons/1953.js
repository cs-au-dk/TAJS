//NOTE: This source only works non-compiled/crushed by adding with(Math){} in both functions.

//NOTE v2.1:Variable _ has been renamed to P by RegPack to save even more space. This should be manually done after crushing as well(with the _ in the eval parts), if you want the source ticker.


//functions:
//A: Main function that is run each time the buffer needs to be refilled. (renamed to getByteFrequencyData() to save bytes by re-using the long method name)
//B: Bytebeat function, returns a speaker position for the current sample t. (renamed to translate() to save bytes by re-using the long method name)
//M = function that returns a certain waveform depending on time, melody, shape, octave shift, pitch change speed.
	//This smaller version of M supports Pulse waves and Saw waves.
	//parameters:(s, o, q, m)
	//s = wave shape (0=pulse, 1=saw)
	//o = num of octaves to shift down
	//q = melody speed
	//m = melody string

//Var names:
//a = canvas element shorthand (defined by shim)
//b = document.body shorthand  (defined by shim)
//c = canvas context shorthand (defined by shim)

//g = AudioContext
//f = scriptProcessorNode
//l = AnalyserNode
//R = array of frequency heights used for visualizer
//T = current time in samples
//t = current sample in buffer
//t (inside B)= current sample in song (e.g. T+t)



//v = t>>13, a time amount we often need, as it is the length of the basic note.
//z = linear gradient (flaming)
//V = 255 (used to save some bytes as we need this number often)
//U = 1024 (used to save some bytes as we need this number often)
V=255;
U=1024;
g = new AudioContext(),
    f = g.createScriptProcessor(2*U,0,1);//create Script Processor.
	
	//Note the nonsensical name: By re-using long canvas method names, I save a few bytes!
    f.onaudioprocess = getByteFrequencyData;//Run function A each time the buffer needs to be filled. This way we don't need requestAnimationFrame.
	l= g.createAnalyser();//Create analyser to get frequency info
	//connect nodes together: f->l->speakers
	f.connect(l);
    l.connect(g.destination);
	
R = new Uint8Array(U);//FFTsize is default 2048, and the bands are half that. So no need to call f.FFTSize().
T=0;//Time

//W=a.width;//shorthand canvas sizes
//H=a.height;
c.font=V+"% Serif";//Large Serif font for the 'Dragonforce' text. 255% percent of normal font size . Is shorter than writing out a pixel size
					//Also, 'Serif' is shorter than Sans-Serif and Courier ;D

c.translate(a.width/2, a.height/2);
//A:Load audio in buffer and draw equalizer.
//e = needed reference to scriptProcessor
function getByteFrequencyData(e){
	c.fillStyle="hsla("+T/U/9+",100%,0%,.2";
	c.fillRect(-U,-U,2*U,2*U);//Clear the screen with translucent black. (free motion blur!)

    
    var S = e.outputBuffer.getChannelData(0);//connect S to sample array so we can make audio
    T+=S.length;//Update Time
    for(t=0;t<S.length;t++){//t=sample   
			//Note the nonsensical name: By re-using long canvas method names, I save a few bytes!
			//By calling the bytebeat function only every 4 samples, the script works on slower computers as well.
             S[t]=(t%4)?S[t-1]:translate(t+T)*.2;
			 //S[t]=S[t+1]=S[t+2]=S[t+3]= translate(t+T);//create audio for each sample
    }
	//with(Math){
	//load the frequency domain in array R for the visualizer
	l.getByteFrequencyData(R);
	//(reuse t for the next loop)
	for(t=0;t<U;

	

		c.rotate((8*PI/U)),//Note: Called with(Math) by the crusher, so we can use PI which now is shorter than 6.28 and more precise!
	
		y=(R[t]/V)*a.height,//map 0-255 range to canvas height.
		z=c.createLinearGradient(0, 0, 5, y),//flaming gradient, height as high as the current audio bar)
		z.addColorStop(.2, "#FF0"),
		//z.addColorStop(.5, "hsla("+T/U+",100%,50%,.2"),
		z.addColorStop(1, "hsla("+T/U/9+",100%,50%,.0"),//(use same color as when clearing the screen, so it can be picked up by the crusher.)
		c.fillStyle=z,
		
		c.fillRect(0,0,5,y),//draw it on the canvas

	t+=4);//Increment in steps of four to save drawing power for slower computers
	//(Note: writing this for loop inline saves me the {}   )
	


	//}
}


//B: make music!
//t=current sample
function translate(t){
//(Note that with(Math) is removed before feeding the crusher, as it adds it to the eval loop, saving me 10 bytes!)
	//with(Math){
	return  v=t>>12,//v = check at what section we are
			v%=1152,//repeat song

			//patterns:
			//NOTE: some patterns that are commented out here are placed inside the synth scripts themselves, as they don't need any post-processing
			D="881146",//start theme
			//X=D.replace(/4/g,5),//start theme major
			//F="1       1 1 ",//drums
			G="888888111111468888111146",//couplet 1
			//I=G.replace(/4/g,5),//couplet 1b (major)
			//J="8=",//base for intro
			//L="==;;1188--468888",//chorus
			//N="==449;==449;;;4489;;4489881146881146881146881146",


				M = function( p,o, q, m,s){
					
					g=t*pow(2, (m.charCodeAt(q)+p)/12-o);//get absolute pitch from semitone.
					x=(g%128)/64-1;//This section is used by both saw and triangle wave (as tri is nothing more than abs(saw) )
						return (s?(s<2)?x:abs(x):(g&128)/64-1);//The real magic: decide between pulse, saw and triangle and synthesize them.
				},

			(v<192?M(0,4,(v>>1)%6, ((v%96)<48?D:D.replace(/4/g,5)),2)+(v%6?0:M(0,5,0,"1",0))://intro
			v<192*2?((v%96)<48?M((v%192<96?12:10),6,(v>>1)%24, (v%192<96?G:G.replace(/4/g,5)),2):M((v%192<96?7:5),6,(v>>1)%6, D,2))+M((v%192<96?12:10),7,(v%96)<48,"8=",2)://first part
			v<192*3?((v%96)<48?M((v%192<96?12:10),6,(v>>1)%24, (v%192<96?G:G.replace(/4/g,5)),0):M((v%192<96?7:5),6,(v>>1)%6, D,1))+M((v%192<96?12:10),7,(v%96)<48,"8=",2)://first part repeated with different synths
			v<192*4?((v%96)<48?M((v%192<96?12:10),6,(v>>1)%24, (v%192<96?G:G.replace(/4/g,5)),1):M((v%192<96?7:5),6,(v>>1)%6, D,1))+M((v%192<96?12:10),7,(v%96)<48,"8=",2)://first part repeated with different synths

			+M(0,5,(v>>1)%48, "==449;==449;;;4489;;4489881146881146881146881146",0)+M(0,6,((v/12)%16),"==;;1188--468888",1))//chorus

			
			//This number is the reverse binary representation of 100100010001000000000001010101000001010100000001, which is the drums pattern.
			//By doing num>>[0-48] every time, it loops through the pattern. If it finds a one, it plays noise. Otherwise nothing is heard.
			+(159497927791873>>((v)%48)&1?random():0)
	
	

	
}