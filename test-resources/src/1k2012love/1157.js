// js1kbin by Peter Jaric, http://twitter.com/peterjaric
//
// This code is designed to be crushed with Aivoo Paas' excellent jscrush:
// https://twitter.com/aivopaas http://www.iteral.com/jscrush/
//
// Without it, I wouldn't have been able to include my js1k 2011 submission
// as the code example. 

// The HTML of the page.
// Besides the structure of the page it contains
// 1) the code that runs the code from the textareas in the iframe
// 2) the code that creates the twitter link
// The current URL is split the into an array with the url of the page and optioanlly the code and the html
// This is used to fill the code and html areas when code has been shared with a link here.
// The first element is also used to create the share/tweet link.
b.innerHTML='<style>i{background:#fbb}*{vertical-align:top}</style><b>JS1KBIN</b> <i onclick=try{b.children[7].contentWindow.eval("document.body.innerHTML=parent.b.children[6].value;"+b.children[5].value)}catch(e){b.children[7].contentWindow.document.body.innerHTML=e}>Run</i> <i onclick=top.open().location="http://twitter.com/share?url="+encodeURIComponent(top.location.href.split("#")[0]+"#$"+encodeURIComponent(b.children[5].value)+"$"+encodeURIComponent(b.children[6].value)).replace(/\\(/g,"%2528").replace(/\\)/g,"%2529")+"&hashtags=js1k,js1kbin">Share</i><br><textarea rows=30 cols=30></textarea><textarea rows=30 cols=30></textarea><iframe height=500>';

// Use code from the URL if available, otherwise use some example code
// (a slightly simpler version of my js1k 2010 submission)
b.children[5].innerHTML=top.location.href.split('$')[1]?decodeURIComponent(top.location.href.split('$')[1]):'//example\nc=document.body.children[0];a=c.getContext("2d");s=20,m=[];for(i=0;i<3;m[i++]={x:Math.random(),y:Math.random(),dx:Math.random()*.03,dy:Math.random()*.03,m:Math.random()*.5});setInterval(function(){w=280,h=480,c.width=w,c.height=h;for(i=0;i<3;){g=m[i++],g.x+=g.dx,g.y+=g.dy;if(g.x>1||g.x<0)g.dx=-g.dx;if(g.y>1||g.y<0)g.dy=-g.dy}for(x=0;x<=w;x+=s)for(y=0;y<=h;y+=s){I=0;for(i=0;i<3;)g=m[i++],X=x-g.x*w,Y=y-g.y*h,d=Math.sqrt(X*X+Y*Y)/(h+w),I+=d?Math.min(g.m/(d*d*20),2):2;a.beginPath(),a.arc(x+.5,y+.5,Math.min(I,9)*s*.5,0,7,0),a.fill()}},50)';

// Use html from the URL if available, otherwise use some example html
b.children[6].innerHTML=top.location.href.split('$')[2]?decodeURIComponent(top.location.href.split('$')[2]):'<!--example html--><body><canvas></canvas></body>';