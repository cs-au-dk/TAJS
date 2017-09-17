// Great aliasing from @p01   ( http://blog.tojicode.com/2012/02/webgl-js1k-entry.html )
for(i in g)g[i.match(/^..|[A-Z]|\d\w$/g).join("")]=g[i];

with(g)with(Math){
S=function(x,c){with(d)for(beginPath(),fillStyle="red",i=12;i;lineTo((u=64>>--i%2)*cos(z=i*.62)+x,c+u*sin(z)))fill()}

f=256;
for(j=7;t=j--;){
 d=(D=a.cloneNode(acT(33992-t))).getContext("2d"),D.width=D.height=f*2;
                //activeTexture(TEXTURE0+t-1)
 t<2|t>5&&S(f,f,t--);
 for(x=6.3/t;t;S(f+cos(--t*x)*f/2,f+sin(t*x)*f/2));
 biT(z=3553,crT());  //bindTexture(TEXTURE_2D,createTexture())
 teI2D(z,0,w=6408,w,5121,D);//textureImage2D(z,0,RGBA,RGBA,UNSIGNED_BYTE,D)
 geM(z)
}

for(p=crP(i=2);i;coS(s),atS(p,s))shS(s=crS(35634-i),"precision lowp float;uniform float "+(--i?"t;uniform sampler2D g;void main(){vec3 n,l=vec3(sin(t*.7)*2.,sin(t)*2.,-2);vec2 o=gl_PointCoord,f=2.*o-1.;if(length(f)>1.)discard;n=normalize(vec3(f,-sqrt(1.-length(f))));gl_FragColor=texture2D(g,o)+vec4(vec3(.7,.3,0)+clamp(vec3(1,.9,.7)*pow(max(dot(normalize(reflect(-normalize(l-n*.5),n)),normalize(n*.5)),0.),6.),0.,1.),.89);}":"v;void main(){gl_PointSize=64.*v;gl_Position=vec4(cos(v*.6),sin(v*.6),0,2);}"));

clC(t=i=0,0,0,1);
// enable(BLEND)
en(3042);
// blendFunc(SRC_ALPHA,ONE_MINUS_SRC_COLOR)
blF(770,769);
o=function(){
        //clear(g.COLOR_BUFFER_BIT|g.DEPTH_BUFFER_BIT)    (16640  (256*64))
    for(requestAnimationFrame(o),cl(f*64),un1f(geUL(p,"t"),t+=0.03),i=7;i;)drA(un1i(geUL(p,"g"),i--),un1f(geUL(p,"v"),7-i),1)
                                                                        // drawArray(POINTS,0,1)
}
liP(p);
o(usP(p))
}