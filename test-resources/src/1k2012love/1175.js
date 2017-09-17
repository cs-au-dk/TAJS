// health and battle progress
Y=H=9
D=0
T=parseInt

// color palette
P='000fb6853345789593fffff6933ccc444555222f00'.match(/.../g)

// insane sprite drawing method
S=function(z,x,y,w,s){for(i=0,f='';i<z.length;i++)f+=new Array((o=P[z[i].charCodeAt(0)-97]||'',n=T(z[i+1]),(n?(i++,n):1)+1)).join('"'+o+'",');for(z=eval('['+f+']'),i=0,r=0,l=0;i<z.length;(r=(++r)>=w?0:r),i++,l=T(i/w))if((v=z[i]))a.fillStyle='#'+v,a.fillRect(x+(r*s),y+(l*s),s,s)}

setInterval(function(){

// stage
S('m',0,0,1,c.width=600)

// background
S('k7l2k8l2k9l2k9l2k9l2k2',0,0,18,34)

// boss
S('xf2xf2xf9afaf9fdfdfdf2d5xfdi3df9f9xf3',((Y+4)%9==0?70:40)+(D*1),20+(D*10),7+D,Math.max(0,27-D))

// health bar
S('l9',25,305,9,24,Y++)
H>0?S('n'+H,25,305,9,24):(Y=-1,D++)

// ranger
p=Y%9+7==7?40:0
p?S('x2cxcgcxgbigcxgxcgx2c',300,45,3,15,H--):T
S('x3f2x3f3g3f6x2b4hx2b3h2cxi2gic2xi2gcicxi2cbicxe5x2e2xe2xc3xc2',370-p,40,7,10)

// knight
p=(Y+5)%9==0?40:0
p?S('jjjbd',335,181,5,15,H--):T
S('xc5x2b3c2x2b4cx2b3c2x2d5x2d3e3xd3e3xcbce3xd4ex2c2xc2xc3xc2',440-p,140,7,10)

// mage
p=(Y+2)%9==0?40:0
p?S('ni3x4bdx9d',456,270,2,12,H--):T
S('j4x3b3j2x2b4jx2j2bj2x2j2e3x2je4x2e2d2e2xe2be3xe6xd7',520-p,230,7,10)

},c.height=350)