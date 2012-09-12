var towersPiles, towersMovesDone;

function TowersDisk() {
  this.next = null;
}

function towersPush(pile, disk) {
  dumpValue(pile); // UInt|NaN
  dumpValue(disk); // [@Object#node171, *Object#node171]
  var top = towersPiles[pile];
//  if ((top != null) && (disk.size >= top.size))
//    error("Cannot put a big disk on a smaller disk");
  disk.next = top;
  towersPiles[pile] = disk;
}

function towersPop(pile) {
  dumpValue(pile); // Undef|UInt|NaN
  var top = towersPiles[pile];
//  if (top == null) error("Attempting to remove a disk from an empty pile");
  dumpValue(top.next); // Undef|Null|[@Object#node171, *Object#node171]
  towersPiles[pile] = top.next;
  top.next = null;
  return top;
}

function towersMoveTop(from, to) {
  dumpValue(from); // Undef|UInt|NaN
  dumpValue(to); // UInt|NaN
  towersPush(to, towersPop(from));
  towersMovesDone++;
}

function towersMove(from, to, disks) {
  dumpValue(from); // Undef|UInt|NaN
  dumpValue(to); // UInt|NaN
  if (disks == 1) {
    towersMoveTop(from, to);
  } else {
    var other = 3 - from - to;
    dumpValue(other); // UInt|NaN
    towersMove(from, other, disks - 1);
    towersMoveTop(from, to);
    towersMove(other, to, disks - 1);
  }
}

function towersBuild(pile, disks) {
  for (var i = disks - 1; i >= 0; i--) {
    towersPush(pile, new TowersDisk());
  }
}

towersPiles = [ null, null, null ];
towersBuild(0, 13);
towersMovesDone = 0;
towersMove(0, 1, 13);
dumpValue(towersMovesDone); // UInt
//  if (towersMovesDone != 8191) 
//    error("Error in result: " + towersMovesDone + " should be: 8191");
dumpObject(towersPiles); // {0:Undef|Null|[@Object#node171, *Object#node171]|absent,
                         //  1:Undef|Null|[@Object#node171, *Object#node171]|absent,
                         //  2:Undef|Null|[@Object#node171, *Object#node171]|absent,
                         //  NaN:Undef|Null|[@Object#node171, *Object#node171]|absent,
                         //  length:Null|UInt|[*Object#node171],
                         //  undefined:Undef|Null|[@Object#node171, *Object#node171]|absent,
                         //  [[DefaultArray]]=Undef|Null|[@Object#node171, *Object#node171]|absent,
                         //  [[DefaultNonArray]]=Undef|Null|[@Object#node171, *Object#node171]|absent,
                         //  [[Prototype]]=[@Array.prototype[native]]}

// with restriction:
// {0:Undef|Null|[@Object#node171, *Object#node171],
//  1:Undef|Null|[@Object#node171, *Object#node171],
//  2:Undef|Null|[@Object#node171, *Object#node171],
//  length:UInt,
//  [[DefaultArray]]=Undef|Null|[@Object#node171, *Object#node171]|absent,
//  [[Prototype]]=[@Array.prototype[native]]}
