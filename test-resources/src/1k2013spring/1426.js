// Add pow method to number.
Number.prototype.pow = function(n){return Math.pow(this,n)}

// Shortcut for createElement
function C(u){
  return document.createElement(u)
}

// Make the form
z = 50;
d = C('div')
i = C('input')
i.value = 'x*sin(x^2)'//'50*sin(x/50)'
d.innerHTML = 'f(x)='

B = 'appendChild'

d[B](i)
b[B](d)

i.onkeyup = p

// Add listeners
c.onmousewheel =function(e){
  z+=e.wheelDelta/10;
  p()
  return !1
}

c.onmousedown = function(e){
  L = e.pageX - dx
  K = e.pageY - dy
  c.onmousemove = P
}
b.onmouseup = function(e){
  c.onmousemove=0
}

dx = 0;
dy = 0;

// Mouse move event handler
function P(e){
  dx = e.pageX - L
  dy = e.pageY - K
  p()
}

// Draw
function o(f){
  W = c.width = c.height = 500
  w = h = W/2;

  w += dx
  h += dy

  //draw mesh
  M = 'moveTo'
  I = 'lineTo'
  B = 'beginPath'
  S = 'stroke'

  a.strokeStyle = '#EFF'
  a[B]()
  for(x=1; x<W; x+=10){
    a[M](x,0) 
    a[I](x,W)
    a[M](0,x)
    a[I](W,x)
  }
  a[S]()



  a.strokeStyle = 'black'
  a[B]()
  a[M](0, h)
  a[I](W, h)
  a[M](w,0)
  a[I](w,W)
  a[S]()

  a.strokeStyle = 'red'
  a[B]()

  for(x=-w; x++<W*10;){
    a[I](x+w,-z*f(x/z)+h)
  }

  a[S]()
}


// Key up listener
function p(){
     try{
      o(new Function('x', 'return ' +
        i.value
        .replace(/[\d|\d.\d]+|x/g, function(n){
          return '(' + n + ')'
        })
        .replace(/sin|cos|tan|cot/g, function(n){
          return 'Math.' + n
        })
        .replace(/\^/g, '.pow')
      ))
    }catch(e){}
}

// Draw default formula
p()