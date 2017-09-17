/*
 * Original source code
 * This was later hand minified with all
 * sorts of tricks and then crushed by 
 *     http://www.iteral.com/jscrush/
 */
// Variables
var NEW_DEAD_BLOCK = 10;
var OLD_DEAD_BLOCK = 11;

var width = a.width;
var height = a.height;
var blockSize = 32;
var xSize = parseInt(width/blockSize);;
var ySize = parseInt(height/blockSize);;
var count = xSize * ySize;
var colors = ["#4af","#3e4","#fd3","#e63","#c3f"];

// Rendering function
function render() {
    c.globalAlpha = 1;
    c.clearRect(0, 0, width, height);
    for(i = 0; i < xSize; ++i) {
        for(j = 0; j < ySize; ++j) {
            c.shadowBlur = blockSize;
            c.shadowColor = "#fff";
            c.fillStyle = colors[A[i][j] % 5];
            if (A[i][j] < NEW_DEAD_BLOCK) {
                c.beginPath();
                c.arc(
                    i * blockSize + blockSize / 2, 
                    j * blockSize + blockSize / 2, 
                    blockSize / 2 - 0.5, 0, blockSize);
                c.fill();

                // New stuff: Bomb
                if (A[i][j] > 4) {
                    c.fillStyle = "#fff";
                    c.font = "20px Arial";
                    c.fillText("★", i * blockSize + 7, j * blockSize + 22);
                }
            }
        }
    }
    c.globalAlpha = 0.3;
    c.fillStyle = "#000";
    c.font = height * 0.9 + "px Arial";
    c.fillText(count, 0, height - blockSize);
}

// Block tainting function
function taintGroup(i, j, newValue) {
    var orig = A[i][j];
    A[i][j] = newValue;
    var result = 1;

    if (i > 0 && A[i-1][j] == orig)
        result += taintGroup(i-1, j, newValue);
    if (i < xSize-1 && A[i+1][j] == orig)
        result += taintGroup(i+1, j, newValue);
    if (j > 0 && A[i][j-1] == orig)
        result += taintGroup(i, j-1, newValue);
    if (j < ySize-1 && A[i][j+1] == orig)
        result += taintGroup(i, j+1, newValue);

    return result;
}

// Mouse click event
document.addEventListener("mouseup", function (e) {

    var xCoord = parseInt(e.clientX/blockSize);
    var yCoord = parseInt(e.clientY/blockSize);
    var orig = A[xCoord][yCoord];
    
    // Colors outside the grid are not clickable
    if (typeof orig == 'undefined') return;

    // New stuff: Bomb
    if (orig > 4) {
        for (i = 0; i < xSize; ++i) {
            for (j = 0; j < ySize; ++j) {
                if (A[i][j] < NEW_DEAD_BLOCK 
                    && Math.sqrt(Math.pow(Math.abs(i - xCoord),2) + Math.pow(Math.abs(j - yCoord), 2)) < 2.5) {
                    A[i][j] = orig % 5;
                }
            }
        }
        render();
        return;
    }

    var taintCount = taintGroup(xCoord, yCoord, NEW_DEAD_BLOCK);
    if (taintCount < 3) {
        taintGroup(xCoord, yCoord, orig);
        return;
    }

    count -= taintCount;

    for (i = 0; i < xSize; ++i) {
        // Filter only untainted blocks
        A[i] = A[i].filter(function (x){return x < NEW_DEAD_BLOCK});
        var untaintedCount = A[i].length;
        if (untaintedCount > 0) {
            // Move the destroyed blocks to the beginning of the array
            var remainingBlocks = ySize - untaintedCount;
            while (remainingBlocks--) {
                A[i].splice(0, 0, OLD_DEAD_BLOCK);
            }
        } else {
            // Remove a column and start looking for empty columns again
            A.splice(i, 1);
            xSize--;
            i=-1;
        }
    }

    render();
});

// Generate puzzle
var A = Array(xSize);
for (i = 0; i < xSize; ++i) {
    A[i] = Array(ySize);
    for(j = 0; j < ySize; ++j) {
        if (i>0 && Math.random() > 0.4) {
            A[i][j] = A[i-1][j];
        } else if (j > 0 && Math.random() > 0.4) {
            A[i][j] = A[i][j-1];
        } else {
            A[i][j] = Math.floor(Math.random()*5);
        }
    }
}

// New stuff: Bomb
for (i = 0; i < count>>7; ++i) {
    A[Math.floor(Math.random()*xSize)][Math.floor(Math.random()*ySize)] = 5 + Math.floor(Math.random() * 5);
}

// First render
render();