n=c.width=c.height=240; 


b={};

F='fillStyle';
W=S=L=P=0;	
g=60;
G=30; // G = g/2
q=' / ';

	/* Level 1 */
l = '0000'+
	'0440'+
	'0120'+
	'0000'+
	
	/* Level 2 */
	'4122'+
	'2222'+
	'2142'+
	'2222'+
	
	/* Level 3*/
	'1141'+
	'2424'+
	'1141'+
	'1121'+
	
	/* Level 4*/
	'1142'+
	'1121'+
	'2244'+
	'1212'+
	
	/* Level 5*/
	'0240'+
	'4004'+
	'2002'+
	'2242'+
	
	/* Level 6*/
	'2220'+
	'2212'+
	'2124'+
	'0240';

a.font='13px arial';
		
c.onclick = function(e){
	
	x = e.pageX;
	y = e.pageY;
	x -=x%g-G;
	y -=y%g-G;
	z=b[x+q+y];
	z&&(z.P*=2,P++,W++)	//inc power
}

function R(f){//bugfix: f must be defined
		
		//load new level
		if(S>1&&S<2){
		   
			for(i=0;i<16;i++){
	
			w = parseInt(l[i+L*16]);
			x = i%4*g+G;
			y = ((i/4)|0)*g+G;	
			w&&(b[x+q+y]={x:x,y:y,p:0,P:w});
			}
			
			S++
		}
		
		k=0;
		v=[];
		
		for(o in b){//update blobs
			
			v[k]=j=b[o];
			p=j.p+=(j.P-j.p)/3;//tween power
			I=j.i;
			x=j.x+=[,4,,-4][I]||0; // move if i set: i = direction
			y=j.y+=[4,,-4,][I]||0;
			
			for(i=4;i--&&p>4;){//split if p > 4
				b[o+i]={x:x,y:y,p:1,P:1,i:i};	
				b[o]=0;
			}
		

			(x<-G||y<-G||x>n+G||y>n+G||!j||(I+1&&(f=b[x+q+y])))&&(delete b[o],f&&(f.P+=f.P)); //remove off screen, collision, deleted blobs
			
			k++; // count onscreen blobs
			
		}

		
		//state changes:
		P>3&&(S=P=0,b=[]); //to many turns
		S-2||k||(S=P=0,L++); // completed level
		
		//draw every pixel
		for(y=0;y<n;y+=2){
			for(x=0;x<n;x+=2){
				
				
				C=0;
				for(V=k;V--;){//for each blob
						
					o=v[V];
					o&&(
					X=o.x-x,
					Y=o.y-y,
					C+=o.p/(X*X+Y*Y+1))
				}
				C*=1E4;
				
				O=S<1?0:(~~C*(!((x-G)%g&&(y-G)%g)?2:1));//calc color blob && grid
				
				a[F]='rgb('+(L>1?O:0)+','+(L<4?O:0)+',0)'; //set Color
				a.fillRect(x,y,2,2);
			}
		}
			
		a[F]='#fff';
		S>1?
		 a.fillText(P+q+3,5,16):
		 a.fillText(L<6?(S+=.04,L>4?'final level':'level '+(L+1)):W+' clicks', 105, 125);
		                            
		
	setTimeout(R);
}

R()