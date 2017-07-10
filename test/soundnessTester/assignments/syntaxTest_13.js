(function(){
function a(){return {b: 10};}
a.b = 10;
function c(){return {d: -10};}
c.d = -10;
          a          +=          c          ++;
})();