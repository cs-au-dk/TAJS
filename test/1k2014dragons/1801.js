var d=document;

//SOUND
s = new (AudioContext);
var o = s.createOscillator();
o.connect(s.destination);
var q=o.frequency;
q.value=32;
o.start(0);

//SAVE BYTES !!!
function g(i){return d.getElementById(i)}
var w=b.clientWidth;
var h=b.clientHeight;
j='radial-gradient';
k='border-radius:50%;';
l=w*.6+'px';
m=h*.5+'px';

//EYE MVT
d.onmousemove=function(e){
W=e.clientX/w;				//mouse position factor
//H=e.clientY/h;				//mouse position factor
f=W*(-W+1);									//factor polynomial second grade
g('D').style.margin=-32*f+'% 0 0 '+80*W+'%';	//iris mov
q.value=(36*f)+22;								//sound
p=g('F').style;
p.width=p.height=(180*f+30)+'%';			//pupil contraction
g('A').style.height=h*(.5-f)+'px';			//eye contraction
};

//MAIN HTML CODE WITH CSS	//skin from http://lea.verou.me/css3patterns/#microbial-mat
b.innerHTML='<style>body{width:'+w+'px;height:'+h+'px;background:'+j+'(circle at 0% 50%,#330 9px,#663 10px,rgba(96,16,48,0) 11px) 0 10px,'+j+'(at 100% 100%,#330 9px,#663 10px,#330 11px),#8a3;background-size:20px 20px}#A{margin:7em auto;overflow:hidden;width:'+l+';height:'+m+';background:#fff;border:30px solid #330;border-radius:66%/60%}#E{width:'+m+';height:'+m+';background:#F90;'+k+'display:table-cell;vertical-align:middle}h6{background:#000;'+k+'margin:0 auto}</style><div id="A"><div id="D"><div id="E"><h6 id="F"></h6></div></div></div>';