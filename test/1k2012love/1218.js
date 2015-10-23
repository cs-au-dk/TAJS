//set constant
t="button";

//add create function to every HTMLElement
HTMLElement.prototype.c=function(n){
	e=document.createElement(n);
	this.appendChild(e);
	return e
}

//shorten localStorage and create UI elements
s=localStorage;
i=b.c("input");
a=b.c(t);
r=b.c(t);
l=b.c("div");
b.c("style").innerText="canvas{display:none}*{font-family:sans-serif}b{color:red}";

//refresh search results
function R(){
	v=i.value.replace(/ *, */,"|");
	l.innerHTML=s.x+"/"+s.a+" tasks done<br>";
	s.s.split(",").splice(1).filter(function(e){
		if(i.value&&e.search(v)>-1)return e;
	}).forEach(function(e){
		l.innerHTML+="<"+t+" onclick='s.s=s.s.replace(\","+e+"\",\"\");s.x=s.x*1+1;R()'>x</"+t+"> "+e.replace(new RegExp("("+v+")","g"),"<b>$1</b>")+"<br>";
	})
}

//set attributes for search box
i.type="search";
i.placeholder="enter or search task";
i.onkeyup=R;

//set attributes for add button
a.innerText="add";
a.onclick=i.onsearch=function(){
	s.s+=","+i.value.trim().replace(/ *, */,",");
	R();
	if(i.value)s.a=s.a*1+1
}

//set attributes for reset button
r.innerText="reset counter";
r.onclick=function(){
	s.x=0;
	s.a=(s.s.match(/,[^,]/g)||[]).length;
	R()
}

//initialization
if(!s.s)s.s="";
if(!s.a)s.a=s.x=0;
R()