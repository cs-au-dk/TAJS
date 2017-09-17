  this.board     = new Board();

this.board.create(10, 10,  10); 
this.board.create(15, 15,  25);


function Board() {
  this.create = function(rows, cols) {
    this.cells = 17 
    for ( row = 0; row != rows; ++ row) {
      for (col = 0; col != cols; ++ col) {
        this.cells[col] = 3
}
     }
  }
  
 
}

