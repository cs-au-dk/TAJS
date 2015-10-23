d();
//stone symbols - nothing chinese... :(
var stone = shuffle("112233445566778899EEWWNNSS112233445566778899EEWWNNSS1122".split(''));
var levels = Array(36,16,4);
k=t=0;
var size = 30;

//timer stuff
tDiv = document.createElement('div');
tDiv.id = 'T';
b.appendChild(tDiv);
var tid = setTimeout(mycode, 1000);
function mycode() {
  T.innerHTML = Math.floor(t/60)+':'+t%60;
  t++;
  tid = setTimeout(mycode, 1000);
}

//check, if an element has a className - from StackOverflow :P
function hasClass(element, cls) {
    return (' ' + element.className + ' ').indexOf(' ' + cls + ' ') > -1;
}

//add Class to an element
function addClass(element, cls) {
    element.className +=cls+' ';
}

//Some shuffle algorithm - from StackOverflow :P
function shuffle(array) {
    for (var i = array.length - 1; i > 0; i--) {
        var j = Math.floor(Math.random() * (i + 1));
        var temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
    return array;
}

//function to check actual clicked stone is in the left or right side of the actual row
function clickable(elem) {
	if (elem.parentNode.firstChild == elem || elem.parentNode.lastChild == elem) {
		return true
	} else {
		return false
	}
}

//generate the stones
for(i=0; i < levels.length;i++) {
	for(j=0; j < levels[i];j++) {
		var iDiv = document.createElement('button');
		if (j%Math.sqrt(levels[i]) == 0) {
			rDiv = document.createElement('div');
			addClass(rDiv,'r')
			b.appendChild(rDiv);
		}
		addClass(iDiv, stone[k] + ' st')
		iDiv.style.position = 'absolute';
		iDiv.style.width = size+'px';
		iDiv.style.height = size*1.2+'px';
		levelCorrectionX = 29+(Math.sqrt(levels[0])*size)/2-(Math.sqrt(levels[i])*size)/2 + i*-2
		levelCorrectionY = 29+(Math.sqrt(levels[0])*(size*1.2))/2-(Math.sqrt(levels[i])*(size*1.2))/2 + i*-2
		iDiv.style.top = Math.floor(j/Math.sqrt(levels[i]))*(size*1.2)+levelCorrectionY+'px';
		iDiv.style.left = ((j%Math.sqrt(levels[i]))*size)+levelCorrectionX+'px';
		iDiv.style.background = '#FC0'
		iDiv.innerHTML = stone[k];
		rDiv.appendChild(iDiv);
		k++;
	}
}
//click event to a stone by it's classname
(function() {
    var elms = document.getElementsByClassName("st");
    for( i=0; i<elms.length; i++) {
        (function(i) {
            elms[i].onclick = function() {
				//if actual block is clickable
				if (clickable(this)) {
					//clicked div get another style
					this.style.color = "red"
					//clicked div get a specific classname
					if (!hasClass(this, 'sel')) {addClass(this, 'sel')}
					//if 2 selected stone, we have some task :)
					clicked = document.getElementsByClassName('sel')
					if (clicked.length == 2) {
						//if 2 stone have same classnames, remove them from the game table
						if (clicked[0].className == clicked[1].className) {
							for (z=0; z<2;z++) {
								clicked[0].parentNode.removeChild(clicked[0]);
							}
							if (elms.length == 0) {document.write("<h1>You are a Dragon!</h1>")}
						} else {
							//else set style to normal for 2 selected stone, and remove the 'sel' className
							for (y=0; y<2;y++) {
								clicked[0].style.color = "#000";
								clicked[0].className = clicked[0].className.replace(' sel','');
							}
						}
					}
				}
            };
        })(i);
    }
})();