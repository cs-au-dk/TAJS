/**

 Tunnel Racer
 (c) Pin Kubders (@pimlinders)

 Find your way through the winding tunnel -- but don't hit the wall!

 Gameplay:
 - Use the left/right arrow keys or A/D keys to move the race car left and right
 - Don't hit the wall!

**/

var canvasSize = 500,
    playerSize = 20,
    rowHeight = 4,
    rowSpace = 100,
    widths = [],
    speed = 15,
    interval,
    scoreInterval,
    x = 0,
    score = 0,
    goal = getGoal(),
    si = setInterval,
    ci = clearInterval;

// get random number
function getRandom(from, to) {
    return Math.floor((Math.random()*to)+from);
}

// get new goal width
function getGoal() {
    return getRandom(0, (canvasSize - rowSpace));
}

// move to goal width
function getWidth(width) {
    // move left
    if(width > goal) {
        width = width - getRandom(0, 5);
        // get new goal if goal is hit
        if (width < goal) {
            goal = getGoal();
        }
    }
    // move right
    else {
        width = width + getRandom(0, 5);
        if (width > goal) {
            // get new goal if goal is hit
            goal = getGoal();
        }
    }
    return width;
}

function draw() {
    // draw course
    widths.pop();
    widths.unshift(getWidth(widths[0]));
    a.clearRect(0,0,canvasSize,canvasSize);
    a.fillStyle = 'green';
    for(var i=0; i<widths.length; i+=1) {
        var width = widths[i];
        // left side of course
        a.fillRect(0, i*rowHeight, width, rowHeight);
        // right side of course
        a.fillRect(width+rowSpace, i*rowHeight, (canvasSize-width-rowSpace), rowHeight);
    }
    // draw player
    a.fillStyle = 'blue';
    a.fillRect(x, canvasSize-playerSize-rowHeight, playerSize, playerSize);
    a.fillStyle = "black";
    // draw score
    a.font = "bold 16px Arial";
    a.fillText("Score: " + score, 10, 20);
    calculateHit();
}

function calculateHit() {
    var wLength = widths.length,
        //get all posibile hit points, note player starts ontop of the first rowHeight and range = playerSize / rowHeight
        hits = widths.slice(wLength - (playerSize / rowHeight) - 1, wLength-1);
    for(var i=0; i<hits.length; i+=1) {
        var hit = hits[i];
        if (hit >= x || (hit + rowSpace - playerSize) <= x) {
            // reset
            ci(interval);
            ci(scoreInterval);
            setTimeout(start, 2000);
            break;
        }
    }
}

function start() {
    // clear canvas
    a.clearRect(0,0,canvasSize,canvasSize);
    // reset score
    score = 0;
    // clear initial widths
    widths = [];
    // create initial straight away
    for (var i=0; i<(canvasSize/rowHeight); i+=1) {
        // all widths are the same (roughly middle of screen)
        widths.push((canvasSize/2) - (rowSpace/2));
    }
    // initial player position, in middle of white space
    x = widths[widths.length-1] + ((rowSpace-playerSize) / 2);
    // redraw
    interval = si(function() {
        draw();
    }, 20);
    // increase score by one every second
    scoreInterval = si(function() {
        score += 1;
    }, 1000);
}

// monitor keyboard events to move player left and right
window.addEventListener('keydown', function(e) {
    var code = e.keyCode;
    if (code === 65 || code === 37) {
        x = x - speed;
    }
    if (code=== 68 || code === 39) {
        x = x + speed;
    }
});

c.height = canvasSize;
c.width = canvasSize;
start();