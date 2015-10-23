Y=600;Z=99;
(function(){
with(Math)S=sqrt,P=pow,F=floor,A=abs;
var lattice_dim = 99; // lattice dimensions. 99 saves me 1 byte vs 100. I'm seriously that desperate 
var lattice_sq = lattice_dim*lattice_dim; // total # of nodes
var lattice=[];
var x, x_pos, y_pos, d; // loop variables
var eq = []; // Instead of equilibrium() returning an array, we'll just use this one over and over again and hope we don't forget to initialize it before every use
var init=1; // This is only used once. Is there another variable that could be used instead?
//var count=0;
var ND = [0,0,1,0,0,-1,-1,0,0,1,1,-1,-1,-1,-1,1,1,1];
var px_per_node = 6;
I = c.createImageData(600, 600);
var mousex = 0;
var mousey = 0;
function equilibrium(ux, uy, rho) {
    // D = loop variable
    // E = node_distribution index
    // G = velocity * node direction... or something
    // B = node weight
    eq = [];
    for (D = 0; D < 9; D++) {
        // Calculate equilibrium value
        G = (ND[D*2] * ux) + (ND[D*2+1] * uy);
        // Find the node weight. I think this is more succinct than keeping 
        // an array of these values
        if (D) {
            B = (D<5)?1/9:1/36;
        } else {
            B = 4/9;
        }
        // Equilibrium equation
        eq[D] = B * rho * (1 + 3*(G) + 4.5*(G*G) - 1.5*(ux * ux + uy * uy));
    }
}
function stream(){
    // Q = loop variable
    // K = loop variable
    // H = node directions index
    // N = newx
    // R = newy
    for (Q = 0; Q < lattice_sq; Q++) {
        y_pos = F(Q/lattice_dim);
        x_pos = Q%lattice_dim;
        for (K = 0; K < 9; K++) {
            N = ND[K*2] + x_pos;
            R = ND[K*2+1] + y_pos;
            // Check if new node is in the lattice
            // Cheat a little, though (N>0 instead of N>=0). For the bytes.
            if (N > 0 && N < lattice_dim &&
                R > 0 && R < lattice_dim) {
                lattice[N+R*600].s[K] = lattice[x_pos+y_pos*600].d[K];
            }
        }
    }
}
function collide(){
    // Collide is going to draw and initialize, too, because LOL, why not?
    // i = loop variable
    // I = Image
    // L = imagedata
    // M = node
    // C = dist
    // T = d1
    // U = d2
    // W = rho
    // V = index
    L = I.data;
    for (x = 0; x < lattice_sq; x++) {
        y_pos = F(x/lattice_dim);
        x_pos = x%lattice_dim;
        if (init) {
            // Inititialize lattice
            // Distribution, stream, density (rho), x velocity, y velocity
            equilibrium(0,0,1);
            lattice[x_pos+y_pos*600] = {d:[],s:eq,r:1,x:0,y:0};
        }
        M = lattice[x_pos+y_pos*600];
        // Copy over values from streaming phase.
        C = M.s;
        // Calculate macroscopic density (rho) and velocity (ux, uy)
        // and update values stored in node.
        T = C[1] + C[5] + C[8];
        U = C[3] + C[6] + C[7];
        W = T + U + C[0] + C[2] + C[4];
        M.x = (T - U) / W;
        M.y = (C[4] + C[7] + C[8] - C[2] - C[5] - C[6]) / W;
        // Update values stored in node.
        M.r = W;
        // Set node equilibrium for each velocity
        equilibrium(M.x, M.y, W);
        for (i = 0; i < 9; i++) {
            M.d[i] = C[i] + (1 * (eq[i] - C[i]));
        }
        // DRAW
        for (i = 0; i < 36; i++) {
            V = 4*(i%6+6*x_pos+600*(F(i/6)+6*y_pos));
            q = lattice[x_pos+y_pos*600];
            // Surprisingly, floor is not required here.
            // For some reason I thought ImageData didn't like floating point
            // but it's actually not a problem
            // SPEED
            L[V+1] = S(P(q.x, 2) + P(q.y, 2))*4E3;
            L[V+3] = Y; // Alpha. Setting this way above the max of 255. 2 bytes is 2 bytes.
        }
    }
    c.putImageData(I, 0, 0);
    init=0;
}

function mousemove(e){
    // Scale from canvas coordinates to lattice coordinates
    // O = radius around mouse
    // J = node
    // t = new mouse x position
    // u = new mouse y position
    // v = delta x
    // w = delta y
    t = e.layerX;
    u = e.layerY;
    v = t-mousex;
    w = u-mousey;
    for (O = 0; O < 36; O++) {
        g = F(t / px_per_node + O/6);
        h = F(u / px_per_node) + O%6;
        if (g>0&&g<98&&h>0&&h<98) {
            J = lattice[g+h*600];
            // x&&x/abs(v) == sign of x
            // Note to future self: It's pretty important that we take the 
            // absolute value here. You might think you can save a few bytes
            // by removing it, but I assure you it won't work.
            equilibrium(v&&v/A(v)/20, w&&w/A(w)/20, J.r);
            J.s = eq;
        }
    }
    mousex=t;
    mousey=u;
}
a.onmousemove=mousemove;
(function update(){
    collide();
    stream();
    requestAnimationFrame(update);
})();

})();