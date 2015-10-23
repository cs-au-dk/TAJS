//frequency of the notes / sound objects
S = [293,330,370,392,440,493,554,587,659,740,784,880];
//Partition
P = 'c0abc0abc56789aba089a0345654534540654032321234564065607856789abca089a0989789a987806780345654587860876054543456786087807678987867a'.split('');
//number of notes
N = 12;
//Number of samples (8000 for 1 sec) (DO NOT CHANGE !!! SIZE ENCODED IN FILE HEADER)
T = 3000; // 0.425 s
for(j=0; j<N ; j++){
	e = "";
	for (i=0; i<T; i++){
		//smooth in and out
		//w= i<10||i>T-10?0:i<50?(i-10)/40:Math.cos(1.57*(i-50)/(T-60)); 
		//note signal (sin)
		f =~~(128*(1+Math.sin(S[j]*i*7.85E-4)*(i<10||i>T-10?0:i<50?(i-10)/40:Math.cos(1.57*(i-50)/(T-60)))));
		//encode
		e+=String.fromCharCode(f>255?255:f<0?0:0|f);
	}
	
	//CLEAN but doesn't work in Opera (built with http://codebase.es/riffwave/ )
	//f='data:audio/wav;base64,UklGRt0LAABXQVZFZm10IBAAAAABAAEAQB8AAAAAAAABAAgAZGF0YbkLAACA';
	//S[j]=f+btoa(e);
	//OLD from synthetizer http://www.p01.org/releases/JS1K_Speech_Synthesizer/
	//f='data:audio/wav;base64,UklGRl9vT19XQVZFZm10IBAAAAABAAEAQB8AAEAfAAABAAgA';
	//S[j]=f+btoa(f+e);
	//MIX OF THE TWO that seem to work on both chrome, opera and FF
	S[j]='data:audio/wav;base64,UklGRt0LAABXQVZFZm10IBAAAAABAAEAQB8AAEAfAAABAAgAZGF0YbkLAACA'+btoa(e);
	
}


s=c.width= c.height=480;
C = c.cloneNode();
A = C.getContext('2d');
u=['f00','f80','ff0','8f0','0f0','0f8','0ff','08f','00f','80f','f0f','f08'];
w=[0,1,2,3,7,11,15,14,13,12,8,4];
i = 0;
W = function(f){
	if(i>=0){
		e = parseInt(P[f],16);
		if(e){
			e--;
			d = new Audio(S[e]);
			d.volume = 0.5*(1-f/260);
			d.play();
			a.fillStyle = '#'+u[e];
			f = 120*(w[e]&3);
			g = 120*(w[e]>>2);
			a.fillRect(f,g,120,120);
			a.fillStyle='#000';
			a.font="120px Arial";
			a.fillText(String.fromCharCode(0x266b),f+20,g+100);
		}
	}
};
//W(0);

setInterval(function(f){
	W(i);
	W(i-64);
	W(i-2*64);	
	i++;
},250);

setInterval(function(f){
	A.drawImage(c,0,0);
	a.fillStyle='#000';
	a.fillRect(0,0,s,s);
	a.globalAlpha = 0.98;
	a.drawImage(C,0,0,s,s,6,6,s-10,s-10);
	a.globalAlpha = 1;
},50);