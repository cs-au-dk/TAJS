!function(){
    var lines = [],
        row,
        score = 0,
        random,
        x = 0,
        y = 0,
        cellSize = 25,
        fontSize = 37,
        fontOffsetTop = 24,
        rows = 99,
        cols = 30,
        colorClearCell = '#FFF',
        colorStar = '#FC3',
        colorSand = '#C96',
        glyph = '☺';

    // Layout
    b.style.margin = 0;
    c.height = rows * cellSize;
    c.width = (cols + 1) * cellSize;
    c.style.margin = '0 auto';
    c.style.display = 'block';

    /**
     * Shows splash message
     *
     * @param {String} message
     */
    function showMessage(message) {
        var z = document.createElement('div'),
            zStyle = z.style;

        z.setAttribute("style", "position:fixed;top:20%;width:100%;opacity:0;transition:all .5s;text-align:center;");
        z.innerHTML = message;

        b.appendChild(z);

        setTimeout(function () {
            zStyle.opacity = 1;
            zStyle.fontSize = "25em";
        }, 10);

        setTimeout(function () {
            zStyle.opacity = 0;
        }, 510);
    }

    /**
     * Puts symbol in current cell
     *
     * @param {String} color
     * @param {String} [symbol]
     */
    function symbol(color, symbol) {
        a.fillStyle = color;
        a.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
        a.font = fontSize + "px Courier New";
        a.fillStyle = '#333';
        symbol && a.fillText(symbol, x * cellSize + 1, y * cellSize + fontOffsetTop);
    }

    // initialize
    for (; y < rows; y++) {
        row = [];
        for (; x <= cols; x++) {
            random = Math.random();

            if (random < .2) {
                random = random < .05 ? 9 : 0;
            }
            random > 1 ? symbol(colorStar, glyph) : !random ? symbol(colorClearCell) : symbol(colorSand, '□');
            row.push(random);
        }
        lines.push(row);
        x = 0;
    }
    y = 0;
    symbol(colorClearCell, glyph);

    b.onkeydown = function (e) {
        // prevent scrolling
        e.preventDefault();

        symbol(colorClearCell);
        e = e.keyCode - 37;
        //    1
        // 0     2
        //    3
        if (e % 2) {
            if (e > 1) {
                y++;
            } else {
                // do not move & restore smile
                return symbol(colorClearCell, glyph);
            }
        } else {
            if (e < 2) {
                x--;
            } else {
                x++;
            }
        }

        // roll
        if (x > cols) x = 0;
        if (x < 0) x = cols;

        // bottom reached
        if (y > rows - 2) {
            return alert('Game Over ' + score + glyph);
        }

        // score++ if star;
        if (lines[y][x] == 9) {
            lines[y][x] = 0;
            showMessage(++score + glyph);
        }

        // fall
        while (!lines[y + 1][x]) {
            symbol(colorClearCell);
            y++;
        }

        symbol(colorClearCell, glyph);
        scrollTo(0, (y - 9) * cellSize);
    }

}();