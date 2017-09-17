//set the size of the canvas
c.width=600
c.height=600
//init the time basis
t=0
//init the animation
setInterval(Animate,150)

//********************* GENERAL FUNCTIONS **************

//generate a flashy color
function flashy_random_color() {
  //to get flashy color let red to 255 and change green and blue
  var g=Math.floor(255*Math.random())
  var b=Math.floor(255*Math.random())
  return "rgb(255,"+g+","+b+")"
}

//floored random function
function FRandom(max_value) {
  return Math.floor(Math.random()*max_value)
}

//create a bump with offset a heigh h and half-width l, 0 everywhere else
function bump(x,a,h,l) {
  return Math.max(0,-Math.pow((((x-a)*Math.sqrt(h))/l),2)+h)
}

//x,y coordinates of center, r is radius, c is color 
function disc(x,y,r,c){
  a.beginPath();
  a.strokeStyle=c;
  a.arc(x,y,r/2,0,2*Math.PI,false);
  a.lineWidth=r;
  a.stroke()
}

//**************************** ANIMATION PART *****************


function Animate() {
//animation in three parts depending on the time
//note that we never call clearRect
  
  if (t<100) {
  //first part of the animation : the color of the nature go brighter and the sun is waking up

    //sky
    s0=Math.max(120,240-Math.floor(t)).toString(16)
    a.fillStyle="#"+s0+"f0f0"
    a.fillRect(0,0,600,300);

    //ground
    s3=Math.max(100,255-Math.floor(t)).toString(16)
    a.fillStyle="#"+s3+"fe"+s3
    a.fillRect(0,300,600,600);

    //sun
    s1=Math.max(20,180-Math.floor(t/2)).toString(16)
    s2=Math.max(200,254-Math.floor(t/2)).toString(16)
    disc(450,100,50,"#ff"+s2+s1)
    //mouth of sun
    disc(450,130,Math.max(3,bump(t,50,8,50)),"#000") 
    //eyes of sun
    v=Math.min(t/4,5)
    disc(470,100,v,"#000")
    disc(430,100,v,"#000")

    //tree
    a.fillStyle="#a50"
    a.fillRect(150,200,20,100);
    disc(160,125,100,"#"+s3+"fd"+s3)

    //We print the message in an invisible color (ground color with green+1)
    a.fillStyle="#"+s3+"ff"+s3;
    a.font="130px arial"
    a.fillText("HAPPY",70,430);
    a.fillText("SPRING!",10,560);
  }

  if (t==100) {
    //change the speed of refreshment between first and second part of the animation
    clearInterval()
    setInterval(Animate,1)

    //get the pixel table to know the exact color of the pixels
    pixels_table=a.getImageData(0,0,600,600).data

    //print the wish message in the tree
    a.fillStyle='#f90'
    a.font='35px arial'
    a.fillText('Wish U',95,140)
  }

  if (t>100) {
  //flowers message

    //pick coordinates for a potential flower
    x=FRandom(600)
    y=300+FRandom(300)
  
    if (pixels_table[(y*600+x)*4+1]==255) {
    //check if the picked coordinates are in the invisible print i.e. green=255
    
      //pick two flashy colors
      c1=flashy_random_color()
      c2=flashy_random_color()

      //draw the flower
      disc(x,y,5,c1)
      disc(x,y+5,2,c2)
      disc(x,y-5,2,c2)
      disc(x+5,y,2,c2)
      disc(x-5,y,2,c2)
      disc(x+3.5,y+3.5,2,c2)
      disc(x-3.5,y+3.5,2,c2)
      disc(x+3.5,y-3.5,2,c2)
      disc(x-3.5,y-3.5,2,c2)
    }
  }
  t=t+1
}