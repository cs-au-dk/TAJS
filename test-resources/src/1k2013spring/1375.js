(function () {
    var N = 8,
        USED_COL = {},
        CHESS_BOARD = [
            [0, 0, 0, 0, 0, 0, 0, 0],
            [0, 0, 0, 0, 0, 0, 0, 0],
            [0, 0, 0, 0, 0, 0, 0, 0],
            [0, 0, 0, 0, 0, 0, 0, 0],
            [0, 0, 0, 0, 0, 0, 0, 0],
            [0, 0, 0, 0, 0, 0, 0, 0],
            [0, 0, 0, 0, 0, 0, 0, 0],
            [0, 0, 0, 0, 0, 0, 0, 0]            
        ],
        
        BOARDS = 5,
        SQUARE_DIM = 20,
        BOARD_DIM = 160,
        WHITE = '#dabb85',
        BLACK = '#6b402d';
     
     c.width = 840;
     c.height = 3220;
     a.font = '13pt Arial';
     a.textBaseline = 'top';

    var solution = 0;
    function drawSol() {
        var col = solution % BOARDS,
            x = (col * BOARD_DIM) + (col * 10),
            y = Math.floor(solution / BOARDS),
            top = (y * BOARD_DIM) + (y * 10),
            left = x;
        
        for (var i = 0; i < N; i++) {
            for (var j = 0; j < N; j++) {
                if ((j + i) % 2 == 0) {
                    a.fillStyle = WHITE;
                    a.fillRect(left, top, SQUARE_DIM, SQUARE_DIM);
                }
                else {
                    a.fillStyle = BLACK;
                    a.fillRect(left, top, SQUARE_DIM, SQUARE_DIM);                    
                }
                if (CHESS_BOARD[i][j]) {
                    a.fillStyle = '#FFFFFF';
                    a.fillText('Q', left + 2, top + 2);
                }                
                left += SQUARE_DIM;
            }
            left = x;
            top += SQUARE_DIM;
        }
        solution++;
    }
        
    function isValidPos(row, col) {
        var i = col,
            j = col;
        while(row--) {
            if (i--) {
                if (CHESS_BOARD[row][i]) 
                    return false;
            }
            if (j++ < N) {
                if (CHESS_BOARD[row][j]) 
                    return false;
            }            
        }
        return true;
    }    
    
    /**
     * Generates the permutations of 8 queen on the board
     * Before place the next Queen it checks both above diagonals 
     * while the method places next Queen on every next row,
     * we need to check only above diagonals.
     * This method could be achived with 8 loops, but the recursion 
     * is more elegant and js1k solution.
     */
    (function solve(n) {
        if (n >= N) {
            drawSol();
            return;
        }
        for (var i = 0; i < N; i++) {
            if (!USED_COL[i] && isValidPos(n, i)) {
                USED_COL[i] = CHESS_BOARD[n][i] = 1;
                solve(n + 1);
                USED_COL[i] = CHESS_BOARD[n][i] = 0;
            }
        }
    })(0);
})();