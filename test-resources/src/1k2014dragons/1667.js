//Compressed with Closure and hand-tuned to get it below 1024 bytes

// Styles to be set once
b.bgColor="#eee";
c.font='17pt cursive';
c.textAlign="center";

// Some variables I'll want to use later.
E='';

// canvas drawing methods
S='fillStyle';
c.T=c.fillText;
c.R=c.fillRect;

// Colors I use a few times.
V='#000';    // Black
W='#FFF';    // White
X='#DDD';    // Dark gray
Y='#F68';    // Pink
Z='#7CF';    // Blue

M=Math;
R=M.random;

// Highest starts out at 3
H = 3;
// Pick an upcoming (next) tile
U = G();

// get a tile value of less than or equal to the highest.
// We also use this a lot for drawing random numbers.
// A value for the first argument triggers alternate behavior.
function G(a) {
    // Draw a number up to 3.
    g = M.ceil(R() * 3);
    // If the number is 3, and the highest number is lower than 3,
    // flip a coin to decide whether we should double the value.
    // This gives us increasingly slim opportunities to spawn high value tiles.
    // I haven't spent a TON of time testing this
    while (g > 2 && g < H && R() < .4) {
        g*=2;
    }
    return a ? g-2 : g;
}

// rotate the field t through 4 rotations and squish on the r'th rotation (1,2,3,4)
// returns how many squishes it did OR how many were possible
// r: which rotation to squish on, or undefined to run a test on all directions
// Also draws the field- wasteful, but saves several bytes for another loop
function O(r) {
    
    // reset the flag that tracks whether we performed any squishes
    p=s=0;
    
    // rotate through 4 sides
    for (k=1; k<5; k++) {
        
        // Do the squish calculations before the r'th rotation-
        // or always if we're not saving squishes
        if (k==r || !r) {
            // We do the squish calculation separately for each row. Loop through each row:
            for (i=4; i--;) {
                // shortcut to the current row's value
                v=F[i];
                // squish logic!
                // Step through each element of the row.
                for (m=0; m<4; m++) {
                    // m is this column, n is the next column
                    n=m+1;
                    // We squish if this column has a zero AND there are nonzeros to the right
                    // To determine if there are any nonzeros right, we convert the remaining
                    // columns to a string and then to a number. If there are any non-zero values,
                    // the resulting number will be nonzero.
                    if (!v[m] && v.slice(m).join(E) - 0) {
                        // Remove this column's value and add a zero
                        r && F[i].splice(m,1) && (F[i][3]=0);
                        // break out of this loop and mark that a squish has occured
                        // setting m = 5 will skip the next if because v[5] is undefined
                        s=m=5;
                    }
                    // if this column is > 3 and matches the next column,
                    if ((v[m] >= 3 && v[m] == v[n]) ||
                               // OR if this column + next column = 3 (1 and 2)
                               v[m] + v[n] == 3) {
                        // replace this column with the sum of this and next column and add zero
                        r && F[i].splice(m,2,F[i][m]+F[i][n]) && (F[i][3]=0);
                        // break out of this loop and mark that a squish has occured
                        s=m=5;
                    }
                }
            }
            
            // if squish was performed, replace one of the zeros that was added with the new tile
            if (r && s) {
                // we need to add the new tile at a random position.
                // Get a random integer (using the new tile drawing function!)
                i=G();
                
                // Start at a random row, and go down until you find one that's nonzero
                while(F[i%4][3]) {
                    i++;
                }
                // Replace this row's last column with the Upcoming tile
                F[i%4][3]=U;
                // draw a new upcoming (next) tile. This will be drawn later.
                U = G();
            }
        }

        // Now we're going to actually do the rotation.
        
        // new field to rotate into
        n=[];

        // counterclockwise rotation
        // loop over each row
        for (i=0; i<4; i++) {
            // push an empty array into the new field
            n[i]=[];
            // loop over each column
            for(j=0; j<4; j++) {
                // get the desired tile value from the old field
                m = F[j][3-i];
                // push it into the new field. (It might be a string)
                n[i][j]=m-0;
                // update the highest value
                H = M.max(H,m);
                
                // what to draw?
                //n = F[i][j];
                // Set the fill color for rectangle using the current value
                // 0,1,2 are listed, otherwise it falls through to W which is white
                c[S]=[X,Z,Y][m] || W;
                // Draw a rectangle
                c.R(2+(42*j), 2+(47*i), 40, 45);
                // Set color for text. White if it's 1 or 2, black otherwise-
                // unless this is the highest value, then it's our pink color
                c[S]= m<3 ? W : m >11 && m==H ? Y : V;
                // Draw tile value, or an empty string if it's zero
                c.T(m||E, 23+(42*j), 30+(47*i), 40);
                // calculate the score.
                // HUGE PROPS to kamikaze28 for finding the formula:
                // http://forums.toucharcade.com/showpost.php?p=3123743&postcount=38
                p += m < 3 ? 0 : M.pow(3, (M.log(m/3) / M.LN2) + 1);
            }
        }
        
        // replace the old field with the new field
        F=n;
    }
    
    // draw "Next" rectangle
    c[S]=[X,Z,Y][U] || W;    // set the color
    c.R(200, 30, 40, 45);    // draw the rectangle
    
    // returns nonzero if there were squishes
    return s;
}

// draw 'Next' text
c.T("Next", 220, 23);    // text

// fill the field
// two of each tiles, and 7 zeros. The split leaves us with strings, oh well
// roughly based on observations of typical starting arrangements.
// There are always 9 tiles and 7 blanks, represented by zeroes (E7)
F = (E+33211E7).split(E);
F.push(G(),G(),G(),G());    // add a few extra random tiles

// shuffle the tiles - this isn't a *great* shuffle but it works.
// Array.sort passes a value to G's first argument, triggering alternate behavior for sorting.
F.sort(G);

// put drawn tiles into fields- 4 arrays of 4.
// Grabs the first four tiles, puts the in an array, and pushes the array to the end.
for(z=4;z--;) {
    F.push(F.splice(0,4));
}

// draw the field for the first time
O();

// listen for key events
onkeyup=function (e) {
    w = e.which;
    // 1 = left, 2 = up, 3 = right, 4 = down
    if (w < 41 && w > 36) {
        // Rotate the field and trigger the squish maneuver on the rotation specified by the keycode
        O(w-36);
        // did we win? If no squishes are possible, then the game is over.
        O() || alert("Score: "+ p);
    }
};