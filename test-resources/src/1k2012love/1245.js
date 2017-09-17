var bpm=120;

var testInstrument = function(n, l, pulse, volume)
{
	s="";
	var duree = (60/bpm)*2000*l; 
	var freq = 440*Math.pow(2, n/12);
	
	var eqGamma = 1;

	for (var i=0;i<duree;++i) {
		var wave=0;
		var time=i/8000;
		var fact = 1;
		for (var n=1; n<40; ++n) {
			fact*=n;
			var an = 1/n;
			an *=(n&1?1:-1);
			var fn = freq*n;
			// pulse wave
			if (pulse) {
				wave += an*(Math.cos(6.2832*fn*time)>0.7?1:-1)*Math.exp(-n*eqGamma*time); 
			} else {
				// sawtooth wave
				wave += an*(((127*fn*time)&127)/127)*Math.exp(-n*eqGamma*time); 
			}
			
		}
		wave = volume*wave;
		wave = (wave>128?128:(wave<-127?-127:wave));
		s+=String.fromCharCode(127+wave);
	}
	return s;
}


var header = 'RIFF_oO_WAVEfmt '+atob('EAAAAAEAAQBAHwAAQB8AAAEACAA')+'data';

//  notes do3 -9, do#3 -8, re3 -7, re# -6, mi3 -5, fa3 -4, fa# -3, sol3 -2, sol#3 -1 la3 0, la#3 1, si3 2, do4 3 etc
// C6 -9, D6 -7, E6 -5, F6 -4, G6 -2, A6 0, B6 2
var c6=-9,    c6s=c6+1, d6=c6+2, d6s=c6+3, e6=c6+4, f6=c6+5, f6s=c6+6, g6=c6+7, g6s=c6+8, a6=c6+9, a6s=c6+10, b6=c6+11;
var c5=c6-12, c5s=c5+1, d5=c5+2, d5s=c5+3, e5=c5+4, f5=c5+5, f5s=c5+6, g5=c5+7, g5s=c5+8, a5=c5+9, a5s=c5+10, b5=c5+11;
var c4=c5-12, c4s=c4+1, d4=c4+2, d4s=c4+3, e4=c4+4, f4=c4+5, f4s=c4+6, g4=c4+7, g4s=c4+8, a4=c4+9, a4s=c4+10, b4=c4+11;

var mod_1990_melody = [ -7, 6, -5, 2, -4, 2, -5, 2, -7, 4, -9, 4, -12, 4, -14, 4, -9, 4, -7, 2, -5, 2, -7, 2, -9, 2, -7, 4, -9, 4, -7, 12, -7, 2, -5, 2,
-4, 4, 0, 4, -2, 2, -4, 2, -5, 4, -4, 2, -5, 2, -7, 4, -5, 2, -7, 2, -9, 4, -7, 4, -2, 4, -5, 4, -9, 4, -7, 12, -12, 4
];
var mod_1990_bassLine = [-7, 6, -7, 2, -9, 4, -9, 4, -4, 8, -9, 8, -11, 4, -16, 2, -11, 2, -9, 4, -14, 2, -9, 2, -7, 6, -12, 2, -19, 4, -7, 2, -9, 2,
-4, 6, -4, 2, -9, 6, -9, 2, -7, 4, -7, 2, -7, 2, -12, 4, 0, 4, -11, 4,-16, 2, -11, 2, -9, 6, 3, 2, -7, 6, -12, 2, -7, 8];

var mod_fairlight_melody_pattern_01 = [ g5s, 3, g5s, 1, g5, 2, f5, 2, c6, 1, g5, 1, e5, 1, g5, 1, c5, 2, g5s, 1, a5s, 1,
c6, 3, c6, 1, a5s, 2, g5s, 2, d6s, 1, a5s, 1, g5, 1, a5s, 1, d5s, 2, f5, 1, g5, 1, g5s, 2, g5s, 2, g5s, 1,
g5, 1, f5, 2, g5, 2, g5, 2, g5, 1, g5s, 1, g5, 1, e5, 1, f5, 8, c5s, 1, f5, 1, g5s, 2, d5s, 1, g5, 1, f5, 1, g5, 1];
var mod_fairlight_melody_pattern_2 = [ g5, 1, a5s, 1, d6s, 2, d6s, 2, c6s, 2, d5s, 1, g5s, 1, c6, 2, c6, 4,
f5, 1, g5s, 1, c6s, 2, d6s, 2, c6s, 2, d5s, 1,g5s, 1, c6, 2, c6, 4,
g5, 1, a5s, 1, d6s, 2, d6s, 2, c6s, 2, d5s, 1, g5s, 1, c6, 2, c6, 4, 
c6, 8, f5s, 4, c6s, 4];
var mod_fairlight_melody_pattern_3 = [g5, 8, a5s, 8,
g5s, 1, c6, 1, f6, 1, g6s, 1, g5, 1, c6, 1, e6, 1, g6, 1, g5s, 1, c6, 1, f6, 1, g6s, 1, a5s, 1, c6s, 1, e6, 1, a6s, 1,
g5s, 1, c6, 1, f6, 1, g6s, 1, g5, 1, c6, 1, e6, 1, g6, 1, g5s, 1, c6, 1, f6, 1, g6s, 1, a5s, 1, c6s, 1, e6, 1, a6s, 1,
c6, 4, c6, 4, g6s, 4, f5, 2, g5, 2];
var mod_fairlight_melody = mod_fairlight_melody_pattern_01.concat(mod_fairlight_melody_pattern_01).concat(mod_fairlight_melody_pattern_2).concat(mod_fairlight_melody_pattern_3);

var mod_fairlight_bassLine_pattern_0 = [c6, 8, g5, 8, d5s, 8, a5s, 8, c6s, 8, g5, 8, c6, 8, g5s, 4, a5s, 4];
var mod_fairlight_bassLine_pattern_1 = [f5, 1, f5, 2, f5, 3, g5, 2, c5, 1, c5, 2, c5, 3, g5, 2, 
g4s, 1, g4s, 2, g4s, 3, d5s, 1, d5s, 2, d5s, 3, c5, 2, 
f5, 1, f5, 2, f5, 3, g5, 2, c5, 1, c5, 2, c5, 3, g5, 2, 
f5, 1, f5, 2, f5, 3, f5, 2, c5s, 1, f5, 1, g5s, 2, d5s, 1, g5, 1, a5s, 2];
var mod_fairlight_bassLine_pattern_2 = [d5s, 2, d6s, 1, d6s, 3, d6s, 1, d6s, 1, g4s, 2, g5s, 1, g5s, 3, g5s, 1, g5s, 1,
c5s, 2, c6s, 1, c6s, 3, c6s, 1, c6s, 1, g4s, 2, g5s, 1, g5s, 3, g5s, 1, g5s, 1,
d5s, 2, d6s, 1, d6s, 3, d6s, 1, d6s, 1, g4s, 2, g5s, 1, g5s, 3, g5s, 1, g5s, 1,
f5s, 1, f5, 1, f5s, 4, f5s, 3, f5s, 5, f5s, 2];
var mod_fairlight_bassLine_pattern_3 = [c5, 1, c5, 1, c5, 4, c5, 3, c5, 5, c5, 2, 
f5, 2, f6, 2, e5, 2, e6, 2, f5, 2, f6, 2, g5, 2, g6, 2,
f5, 2, f6, 2, e5, 2, e6, 2, f5, 2, f6, 2, g5, 2, g6, 2,
c5, 1, c5, 1, c5, 4, c5, 3, c5, 5, c5, 2];
var mod_fairlight_bassLine = mod_fairlight_bassLine_pattern_0.concat(mod_fairlight_bassLine_pattern_1).concat(mod_fairlight_bassLine_pattern_2).concat(mod_fairlight_bassLine_pattern_3);

var melody = mod_fairlight_melody;
var bassLine = mod_fairlight_bassLine;
var melodyTrack=header;
var dl = melody.length>>1;
for (var i=0;i<dl;++i) {
	
	melodyTrack+=testInstrument(melody[i*2], melody[i*2+1], 0, 30);
}

var bassTrack=header;
dl = bassLine.length>>1;
for (var i=0;i<dl;++i) {
	
	bassTrack+=testInstrument(bassLine[i*2]-12, bassLine[i*2+1], 1, 15);
}
new Audio( 'data:audio/wav;base64,'+btoa(melodyTrack) ).play();
new Audio( 'data:audio/wav;base64,'+btoa(bassTrack) ).play();

// graphics part, reduced to 340 bytes
var time=0,U=c.width=c.height=400,V=200,R=100,Q=40,s=Math.sin;
a.strokeStyle="red";
heart=function(){
a.clearRect(0,0,U,U);
var yf=Q;
var y0=Math.ceil(-yf*2.414),y1=yf*2;
a.beginPath();
for(var j=y0;j<y1;++j){
	var y=j/yf;
	var w=y<0?Math.sqrt(1-y*(y+2)):0;
	var x0=y<-2?1-w:0;
	var x1=y<0?1+w:2-y;
	var i=V+20*s(time/R)*s(6*(j+20*s(time/V))*.1*(1+s(time/R)));
	a.moveTo(i+x0*yf,j+V);a.lineTo(i+x1*yf,j+V);
	a.moveTo(i-x0*yf,j+V);a.lineTo(i-x1*yf,j+V);
}
a.stroke();
}
setInterval("heart();++time;",Q);