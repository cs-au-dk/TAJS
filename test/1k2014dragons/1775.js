d();
var i,j,                                                  // placeholders
c=f=0,                                                    // current room and found items counter
d="North0West0South0East0get0kill0open0unlock".split(0),  // accepted actions
s="Key0Chest0Master Chest0Dragon".split(0),               // special items
o=s.slice(0),                                             // where the items are
r=[],                                                     // room exit information, stored as truthy arrays with integers where linked
P=b.style,A=alert,M=Math;                                 // Shorthand
for(i=9;i--;){j=M.random()*9|0;o[i]=[o[j],o[j]=o[i]][0]}  // Scramble special items into rooms

// Generate new room
function g(k) {
  j=[];                         // array of exits in placeholder
  for(i in s)                   // Random room exits
    j[i]=!r[0]||M.random()<.5;  // Each direction, 50-50, unless first room (goes everywhere)
  j[(k+2)%4]=k>3||c;            // Assign previous room to its exit
  return r.push(j)              // Return number of rooms (room number+1)
}

// Render function
function v(h){
  // Color magic
  j="00"+M.pow(67,c).toString(16)+"000";          // 67 (determined by investigation) to the power of the current room's 
  P.background="#"+j.substr(j.search("000")-3,3); // Chop it down, assign
  j='<br><br>';h='';                              // Short hand and setup

  if(o[c]){                   // If there's an item in the room
    h+="Here ";h+=            // - Dynamic messages!
      f>3            
        ? "you slay"          // - Game beat
        : s.indexOf(o[c])<f
          ? "you found"       // - Item collected
          : "there be";       // - Otherwise
    h+=" a "+o[c]
  }
  h+=j+"Exits are ";
  for(i in s)                 // Each direction
    if(r[c][i]!==!1)          // - If not false
      h+=" "+d[i];            // -- Add direction to text
  b.innerHTML=h+j+'<input onkeyup=if(event.keyCode==13)a(this.value)>'
                              // Input for commands
}

// Parse action
function a(x,y) {
  y=d.indexOf((d+"").match(RegExp(x.replace(/ +/g,'|'),'i'))+""); // Find command in array or -1
  if(y<0)return;                                                  // No legit action
  if(y>3){                                                        // Get/Open/etc action
    if(o[c]&&(j=s.indexOf(o[c]))==f){                             // - If there's something in this room and you've progressed enough
      if(f>2) A("You've slain the Dragon!");                      // -- Win the game!
      else{x=f==1?'Master Key':f==2?'Sword':s[f];                 // -- Master Key and Swords are in chests, otherwise item in room
      A('Got '+x)}f++                                             // --- Display got and increment progress
    } else if(j>=f)                                               // - Not far enough
      A('Find the '+s[f]+' First')
  } else if(r[c][y]!==!1) {                                       // Move action, if direction you want to go in is not closed (false)
    if(r[c][y]===!0) {                                            // - Brand new room?
      r[c][y]=g(y)-1                                              // - Generate, subtract 1 to get new room id
    }
    c=r[c][y]
  }
  v() // Render
}

// Init actions
g(4); // Make first room (4 makes it do every direction)
v();  // Render
P.color="#FFF";P.padding="50px" // FABULOUS!