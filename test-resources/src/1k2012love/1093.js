var R = document.createElement('pre'),h = 1, l = 3, C = p = 0,s = [],w='♥',x='█',v=' ',S=u='';
R.style.color='red',b.style.backgroundColor='black',c.style.display='none';

function F(Z){
for (i=0;i<36;i++) S += '+';
if (!Z) S += '\n+';
}


function D(){
q = o.innerHTML.split(u);
r = ~~(Math.random()*y.length)-1;
q[r]=q[r]==v?w:D();
o.innerHTML = q.join(u);
}

onkeydown = function(e){
z = e.keyCode;
if (z == 38) p = -37;
if (z == 40) p = 37;
if (z == 37) p = -1;
if (z == 39) p = 1;
}

onload = function(){
F();
for (i=0;i<21;i++)
{
for (I=0;I<34;I++) 
  {
   if (i==10 && I==15) S+=x; 
   else S += v;
  }
 S += '+\n+';
}
F(1);
R.innerHTML = S.substr(0,S.length-1);
b.appendChild(R);
o = b.children[3],y = o.innerHTML;


s.push(y.indexOf(x));
f = s[0];
D();
setInterval(function(){
C++;
B = (s[0]+p);
if (o.innerHTML[B] == v || o.innerHTML[B] == w)
{
if (o.innerHTML[B] == w)
h++,D(),s.push(s[0]);

t = s[h];

for (i=h;i>0;i--)
s[i] = s[i-1];

s[0] = s[0]+p;
d = o.innerHTML.split(u);
for (i=0;i<h;i++)
{
B = (s[i]);
if (i == 0 && h == 1) d[B-p] = v;
if (i == h-1) d[t] = v;
d[B] = x;
s[i] = B;
}
o.innerHTML = d.join(u);
}
else if (p!=0)
{
d = o.innerHTML.split(u);

for (i=0;i<=h;i++)
B = s[i],d[B] = v;

o.innerHTML = d.join(u);
s[0] = f;
p=0;
h=1;
l--;
}

},150)
}