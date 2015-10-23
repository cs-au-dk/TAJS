c.width = 1024;
  c.height = 768;

  a.font = "bold 200pt Arial";
  a.fillStyle = "rgb(255,255,255)";
  a.fillText("spring", 50, 300);          

  var px = [];

  var imgData=a.getImageData(0,0,c.width,c.height);
          
  for(x=0; x<imgData.width; x++)
  {
    for(y=0 ; y<imgData.height; y++)
    {
      if(getPixel(imgData,x,y)[3] > 0)
      {
        px.push( [x,y] );
      }
    }
  }
   
   
  setInterval(draw, 10);
	
  var max = 40;
  
  var tt = new Array();
  
  //thanks to Tom De Smedt, based on the "Tendrils" algorithm by ART+COM
  //http://nodebox.net/code/index.php/Tendrils

  function tendril()
  {
    this.init = function(x, y, width)
    {   
      this.x = x;
      this.y = y;
      this.width = width;
      this.angle = Math.random()*2*Math.PI - Math.PI;
      this.v = 0;
      this.length = 0;
    };
    this.grow = function(distance, curl, step)  //distance=3.0, curl=1.0, step=0.02
    {
      if(this.length < max)
      {
        this.x += Math.cos(this.angle) * distance;
        this.y += Math.sin(this.angle) * distance;
        this.v += Math.random() * step - step/2;
        this.v *= 0.9 + curl*0.1;
        this.angle += this.v;   
        if(this._x != undefined)
        {
          aa = this.length/max;
                    
          r = 8;
          g = parseInt(aa * 255);
          b = 32;
         
          a.beginPath();
          a.strokeStyle="rgba("+r+","+g+","+b+","+(1-aa)+")";
          a.moveTo(this._x,this._y);
          a.lineTo(this.x,this.y);
          a.closePath();
          a.stroke();
                    
        }
        this._x = this.x;
        this._y = this.y;
        this.length++;

      }
    };
  };
  
	function draw()
	{    
    
    // add new tendrils
    
    for(p in px)
    {
      if(Math.random() > 0.9999)
      { 
        var t = new tendril();
        t.init(px[p][0],px[p][1],15);  
        tt.push ( t );
      }
    }

    // grow actuals tendrils
    
    if(tt.length > 0)
    {
      for(t in tt)
      {
        tt[t].grow(1.0, 1.0, 0.02);    
      }
    }

  }
  

  function getPixel(imgData, x, y) {
    var offset = (x + y * imgData.width) * 4;
    var r = imgData.data[offset+0];
    var g = imgData.data[offset+1];
    var b = imgData.data[offset+2];
    var a = imgData.data[offset+3];
    
    return [r,g,b,a];
  }