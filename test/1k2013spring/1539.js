// Flower Power
// by Manel Villar
// just a bunch of lines 

P = Math.PI;
R = Math.random;

canvas = document.getElementById('frame');

with(canvas) {
width = innerWidth;
height = innerHeight;
context = getContext('2d');
}

//console.log("start");

flowers = [];
flower_limit = 5;
colors = ["red", "pink", "orange", "yellow", "blue", "violet"];


var start = new Date();
c = 0;

function draw() {
    c++;
  
  canvas.width=canvas.width;
  context.fillStyle = "green";
  context.fillRect(0, 0, canvas.width, canvas.height);
  
    if (flowers.length < flower_limit) {
        flowers.push(new flower());
        //console.log(flowers.length, "*");
    }

    toDelete = [];
  //console.log(flowers);
    for (var k = 0; k < flowers.length; k++) {
      if (flowers[k].draw(0) || flowers[k].draw(P / 4)) { 
        toDelete.push(k);
      }
    }

  last_length = flowers.length;
  for (k = 0; k<toDelete.length; k++) {
           //console.log("deleted");
           //console.log(toDelete[k]);
    delete flowers[toDelete[k]];
    flowers.splice(toDelete[k],1);
  }
  
  //console.log(last_length, flowers.length);
  
    //console.log(c);
    //is_executing = false;

    if (c <3000) 
      window.setTimeout(draw,0);
    else 
      console.log("Finish");
}

function flower() {
    //console.log("new flower");
    this.x = canvas.width * R();
    this.y = canvas.height * R();
    this.color = parseInt(colors.length * R(),10);
    this.startRadius = canvas.width/8;
    this.start = new Date();
    this.gap = (5000 * R()) + 5000;
    this.v = (R() * 0.05) - 0.025;
    this.delta = 0;
}

flower.prototype.draw = function (angle) {

    fromStart  = new Date() - this.start;


    with(context) {

      with(this) {

        function f(a,b,c,d) {
            context.beginPath();
            //console.log(a,b,rad);
            context.arc(a, b, rad, 0, 2 * P, false);
            context.arc(c, d, rad, 0, 2 * P, false);
        }


        delta+=v;

        for (var j=0; j<10; j++) {

          fs = (fromStart - (j * 500));
          if (fs<0) return 0;

          var c = Math.sin((P * (fs) ) / this.gap );
          rad = startRadius * c;

          if (c<0) {
            if (j==10) {
              return 1;
            } else {
              return 0;
            }
          }

          save();

          translate(x, y);
          rotate(angle +delta);


          //rad = rad*j;

          // radius, 0 ; 0-radius, 0
          f(rad,0,0-rad,0); 
          clip();

          fillStyle = colors[(color+j)%colors.length];
          //console.log(rad);
          //fillStyle = colors[color];
          // 0, radius, 0, 0-radius
          f( 0, rad, 0, 0-rad);
          fill();

          restore();
        }

        //fillStyle = "white";
        //beginPath();
        //arc(0, 0, radius / 2, 0, 2 * Math.PI, false);
        //fill();
      }

    }


    return 0;
}

window.setTimeout(draw, 0);