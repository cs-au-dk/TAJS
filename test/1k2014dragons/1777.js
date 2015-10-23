var size = 50,
    pieces = 50,
    moves = 8,
    bullets = 0,
    children = 'children',
    className = 'className',
    parentNode = 'parentNode';

function updateStatus() {
  u.innerHTML = (
    !moves && pieces ? 'You Lose!' : (
      !pieces ? 'You Win!' :
      'Moves: ' + moves + ' &mdash; Pieces: ' + pieces));
}

function fireBullets(x, y) {
  fireBullet(x, y, -1,  0);
  fireBullet(x, y,  1,  0);
  fireBullet(x, y,  0, -1);
  fireBullet(x, y,  0,  1);
}

function fireBullet(x, y, dx, dy) {
  ++bullets;

  (function moveBullet(x, y) {
    if (x < 0 || x >= size || y < 0 || y >= size) {
      if (!--bullets) {
        --moves;
        updateStatus();
      }
    } else {
      var cell = t[children][0][children][y][children][x];

      if (cell[className] == 'o') {
        cell[className] = '';
        --pieces;
        --bullets;
        updateStatus();
        fireBullets(x, y);
      } else {
        cell[className] = 'b';
        setTimeout(function() {
          cell[className] = '';
          if (dx != 0) {
            moveBullet(x + dx, y);
          } else {
            moveBullet(x, y + dy);
          }
        }, 25);
      }
    }
  }(x, y));
}

function random() {
  return Math.floor(Math.random() * size);
}

b.innerHTML =
  '<style>' +
  'body{background:#111;margin:10px;}' +
  'h2{color:#EEE}' +
  'table{border-collapse:collapse;border-spacing:0}' +
  'td{border:1px solid #333;width:8px;height:8px}' +
  'td:hover{background:#444}' +
  '.b{background:#EEE}' +
  '.o{background:#F80}' +
  '</style>' +
  '<h2 id="u">Click Any Square To Play</h2>' +
  '<table id="t">' +
  Array(size + 1).join('<tr>' + Array(size + 1).join('<td></td>') + '</tr>') +
  '</table>';

t.onclick = function(event) {
  var cell = event.target;

  if (moves && pieces && !bullets) {
    fireBullets(cell.cellIndex, cell[parentNode].rowIndex);
  }
}

for (var i = 0; i < pieces; ++i) {
  for (;;) {
    var cell = t[children][0][children][random()][children][random()];

    if (cell[className] != 'o') {
      cell[className] = 'o';
      break;
    }
  }
}