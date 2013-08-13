/*
Same,©Copyright 2006-bezumie.com,All Rights Reserved
Åäíàêâè÷êè,©Copyright 2006-bezumie.com,Âñè÷êè ïðàâà çàïàçåíè
*/
function SetCookie(name,value){
	var argv=SetCookie.arguments;
	var argc=SetCookie.arguments.length;
	var expires=(argc>2)? argv[2] : null;
	var path=(argc>3)? argv[3] : null;
	var domain=(argc>4)? argv[4] : null;
	var secure=(argc>5)? argv[5] : false;
	document.cookie=name+"="+escape(value)+
	((expires==null)? "" :(";expires="+expires.toGMTString()))+
	((path==null)? "" :(";path="+path))+
	((domain==null)? "" :(";domain="+domain))+
	((secure==true)? ";secure" : "");
}
function getCookieVal(offset){
	var endstr=document.cookie.indexOf(";",offset);
	if(endstr==-1)
	endstr=document.cookie.length;
	return unescape(document.cookie.substring(offset,endstr));
}
function GetCookie(name,DefaultVal){
	var arg=name+"=";
	var alen=arg.length;
	var clen=document.cookie.length;
	var i=0;
	while(i<clen){
		var j=i+alen;
		if(document.cookie.substring(i,j)== arg)
			return getCookieVal(j);
		i=document.cookie.indexOf(" ",i)+ 1;
		if(i==0)break;
	}
	return DefaultVal;
}
var torem=new Array();
var tomove=new Array();
var canclick=true;
var tot=-1;
var score=0;
function check(x,y,n){
	if(n==0)return;
	if((x >= 0)&&(y >= 0)&&(x<boardw)&&(y<boardh)&&(checked[x][y] != 1)&&(board[x][y].n==n)){
		var p={i: x,j: y};
		torem.push(p);
		//board[x][y].n=0;
		checked[x][y]=1;
		check(x-1,y,n);
		check(x,y-1,n);
		check(x+1,y,n);
		check(x,y+1,n);
	}
}
function compact(){
	var checked=new Array();
	for(var x=0;x<torem.length;x++){
		var i=torem[x].i;
		if(checked[i]==1)continue;
		var n=-1;
		var c=0;
		for(var j=(boardh-1);j >= 0;j --){
			if((n==-1)&&(board[i][j].n==0)){
				n=j;
			}
			if((n >= 0)&&(board[i][j].n>0)){
				board[i][n].n=board[i][j].n;
				board[i][n].id=board[i][j].id;
				board[i][j].n=0;
				var tmp={id: board[i][j].id,cy: j * tilesize,dy: n * tilesize,pos: c};
				tomove.push(tmp);
				c++;
				n--;
			}
		}
		checked[i]=1;
	}
	move();
}
function move(){
	var i=0;
	var n=tomove.length;
	while(i<n){
		var tmp=tomove[i];
		/* if(tmp.pos>0){
			tmp.pos --;
			i++;
		} else */ {
			if(tmp.cy<tmp.dy){
				var step=Math.floor((tmp.dy-tmp.cy)/ 2);
				if(step <= 0)step=1;
				/* if(step >(tilesize / 3))step=Math.floor(tilesize / 3);*/
				tmp.cy += step;
				document.getElementById(tmp.id).style.top=tmp.cy+'px';
			}
			if(tmp.cy >= tmp.dy){
				tomove[i]=tomove[n-1];
				tomove.pop();
				n--;
			} else {
				i++;
			}
		}
	}
	if(tomove.length>0){
		setTimeout('move()',50);
	} else {
		var n=-1;
		for(var i=0;i<boardw;i++){
			if((n==-1)&&(board[i][boardh-1].n==0)){
				n=i;
			}
			if((n >= 0)&&(board[i][boardh-1].n>0)){
				for(j=boardh-1;(j >= 0)&&(board[i][j].n>0);j--){
					board[n][j].id=board[i][j].id;
					board[n][j].n=board[i][j].n;
					board[i][j].n=0;
					var tmp={id: board[i][j].id,cx: i * tilesize,dx: n * tilesize};
					tomove.push(tmp);
				}
				n++;
			}
		}
		moveh();
	}
}
function moveh(){
	var i=0;
	var n=tomove.length;
	while(i<n){
		var tmp=tomove[i];
		if(tmp.cx>tmp.dx){
			var step=Math.floor((tmp.dx-tmp.cx)/ 1.2);
			if(step >= 0)step=-1;
			/* if(step>-(tilesize / 3))step=-Math.floor(tilesize / 3);*/
			tmp.cx += step;
			document.getElementById(tmp.id).style.left=tmp.cx+'px';
		}
		if(tmp.cx <= tmp.dx){
			tomove[i]=tomove[n-1];
			tomove.pop();
			n--;
		} else {
			i++;
		}
	}
	if(tomove.length>0){
		setTimeout('moveh()',50);
	} else {
		if(isEnd()){
			document.getElementById('noMoreMoves').style.display='block';
			setTopScore();
			canclick=false;
		}
		if(needover != null)over(needover);
	}
}
function isEnd(){
	for(var i=0;i<boardw;i++)
		for(var j=0;j<boardh;j++)
			if((board[i][j].n != 0)&&(((i>0)&&(board[i][j].n==board[i-1][j].n))||((j>0)&&(board[i][j].n==board[i][j-1].n))))return false;
	return true;
}
function sqr(a){
	return a*a;
}
function initch(){
	for(var x=0;x<boardw;x++){
		for(var y=0;y<boardh;y++){
			checked[x][y]=0;
		}
	}
}
function getToRem(el){
	var x=Math.floor(parseInt(el.style.left)/ tilesize);
	var y=Math.floor(parseInt(el.style.top)/ tilesize);
	torem.length=0;
	var n=board[x][y].n;
	initch();
	check(x,y,n);
}
function clickme(el){
	if(tot==-1)tot=boardw * boardh;
	if(tomove.length>0)return;
	if(!canclick)return;
	canclick=false;
	//getToRem(el);
	if(torem.length <= 1){

	} else {
		score += sqr(torem.length-2);
		setScore();
		for(var i=0;i<torem.length;i++){
			board[torem[i].i][torem[i].j].n=0;
			var id=board[torem[i].i][torem[i].j].id;
			document.getElementById(id).style.display='none';
		}
		compact();
		tot -= torem.length;
		torem.length=0;
		if(!tot){
			score += boardw * boardh;
			setScore();
			document.getElementById('noMoreTiles').style.display='block';
			setTopScore();
			return false;
		}
	}
	canclick=true;
}
function select(){
	for(var i=0;i<torem.length;i++){
		with(board[torem[i].i][torem[i].j]){
			var ide=id;
			var ne=n;
		}
		with(document.getElementById(ide).style){
			backgroundColor=light[ne-1];
			cursor='pointer';
		}
	}
}
function unselect(){
	for(var i=0;i<torem.length;i++){
		with(board[torem[i].i][torem[i].j]){
			var ide=id;
			var ne=n;
		}
		with(document.getElementById(ide).style){
			backgroundColor=dark[ne-1];
			cursor='default';
		}
	}
	if(document.removeEventListener){
		document.removeEventListener("mousemove",moveto,true);
	}
	else if(document.detachEvent){
		document.detachEvent("onmousemove",moveto);
	}
	document.getElementById('end').style.display='none';
}
var needover=null;
function over(el){
	procdes=false;
	if(tomove.length>0){
		needover=el;
		return;
	} else {
		needover=null;
	}
	if(!canclick)return;
	canclick=false;
	
	for(var i=0;i<torem.length;i++){
		if(el.id==board[torem[i].i][torem[i].j].id){
			canclick=true;
			return;
		}
	}
	
	unselect();
	
	getToRem(el);
	if(torem.length <= 1){

	} else {
		select();
		if(document.addEventListener){
	        document.addEventListener("mousemove",moveto,true);
		}
	    else if(document.attachEvent){
	        document.attachEvent("onmousemove",moveto);
	    }
		with(document.getElementById('end')){
			innerHTML=sqr(torem.length-2);
			style.display='block';
		}
	}
	canclick=true;
}
var procdes=false;
function out(){
	if(procdes)return;
	procdes=true;
	setTimeout('dounsel()',100);
}
function dounsel(){
	if(!procdes)return;
	unselect();
	torem.length=0;
}
var arena_el=null;
function moveto(event){
	if(arena_el==null)arena_el=document.getElementById('arena');
	var tx=0;var ty=0;
	if(event.pageX)tx=event.pageX;
	else if(event.x)tx=event.x+arena_el.offsetLeft;
	if(event.pageY)ty=event.pageY;
	else if(event.y)ty=event.y+arena_el.offsetTop;
	with(document.getElementById('end')){
		style.left=tx+15+'px';
		style.top=ty+15+'px';
	}
	if(event.stopPropagation)event.stopPropagation();
	else event.cancelBubble=true;
	if(event.preventDefault)event.preventDefault();
    else event.returnValue=false;
}
function setScore(){
	document.getElementById('score').innerHTML=score;
}
function setTopScore(){
	if(document.getElementById('topScore').innerHTML<score){
		document.getElementById('topScore').innerHTML=score;
		SetCookie('Same_topScore',score);
	}
}
var cancelHide=false;
function showSets(){
	var cp=document.getElementById('sets');
	if(cp.style.display=='none'){
		cp.style.display='block';
		cancelHide=true;
	}
	else {
		cancelHide=false;
		hideSets();
	}
}
function hideSets(){
	if(cancelHide)return;
	document.getElementById('sets').style.display='none';
}
function selectSet(el){
	var id=(el.id).substr(3,2);
	SetCookie('Same_ImageSet',id);
	if((id != 0)&&(GetCookie('Same_ImageSet',0)== 0)){
		alert('Cookies must be enabled.');
	}
	else {
		window.location.reload();
		cancelHide=false;
		hideSets();
	}
}