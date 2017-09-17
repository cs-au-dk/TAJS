// Use by specifying a WebSocket enabled VNC server in the fragment ID:
// http://localhost:8080/vnc.html#ws://localhost:5900

// r -> number of rects
// s -> initialization/handshake state
// v -> FBU request incremental state
// m -> mouse button state
// Q -> receive queueu (array of number 0-255)
b.style.margin=r=s=v=m=0;
Q=[];

S=new(window.MozWebSocket||WebSocket)(location.hash.slice(1),'base64');

// A(d,e,f,g,h) -> concat shorts onto array d
A=function(d,e,f,g,h){return e+1?A(d.concat(e>>8,e&255),f,g,h):d};

// B(array,e) -> peek short from array starting at position e
B=function(d,e,f,g,h){return (d[e]<<8)+d[e+1]};

// C(array/string) -> send array or string via websocket
C=function(d,e,f,g,h){return d.map?C(d.map(E).join('')):S.send(btoa(d))};

// D(character) -> return character code
D=function(d,e,f,g,h){return d.charCodeAt(0)};

// E(number) -> return character for number code
E=function(d,e,f,g,h){return String.fromCharCode(d)};

// F() -> send FBU request
F=function(d,e,f,g,h){return C(A([3,v],0,0,W,H)),v=1};

// M(event.type==mouse[m]ove) -> send mouse event
M=function(d,e,f,g,h){return C(A([5,m],d.pageX,d.pageY))};

// M(event.type==mousedown) -> send mouse button event
// M(event.type==mouseup) -> send mouse button event
N=function(d,e,f,g,h){return m^=1,M(d)};

// K(event.type==key[d]own) -> send key down event
// K(event.type==key[u]p) -> send key up event
//K=function(d,e,f,g,h){return C(A([4,d.type[3]=='d'?1:0],0,0,(k=d.which,k^=k>64?!d.shiftKey<<5:k<32?255<<8:0)))};
//K=function(d,e,f,g,h){return k=d.which,k^=k<32?255<<8:k<64?d.shiftKey<<4:!d.shiftKey<<5, console.log("keyCode: " + d.keyCode + " k: " + k), C(A([4,d.type[3]=='d'?1:0],0,0,k))};
//K=function(d,e,f,g,h){return k=d.which,k^=k<32?255<<8:k<64?d.shiftKey<<4:k<128?!d.shiftKey<<5:1<<7, C(A([4,d.type[3]=='d'?1:0],0,0,{61:45,58:59,59:58,63:47}[k]||k))};

// 1008
//K=function(d,e,f,g,h){return k=d.which,
//                               k ^= k<16 ? 255<<8
//                                         : k<32 ? 65522
//                                                : k<64 ? d.shiftKey<<4
//                                                       : k<128 ? !d.shiftKey<<5
//                                                               : 1<<7 | !d.shiftKey<<4,
//                        console.log("keyCode: " + d.keyCode + " k: " + k),
//                        C(A([4,d.type[3]=='d'?1:0],0,0,k))};

// 1018
//K=function(d,e,f,g,h){return k=d.which,
//                               k ^= k<16 ? 255<<8
//                                         : k<32 ? 65522
//                                                : k<64 ? d.shiftKey<<4
//                                                       : k<128 ? !d.shiftKey<<5
//                                                               : 1<<7 | (k<192 ? !d.shiftKey<<4 : d.shiftKey<<5),
//                        console.log("keyCode: " + d.keyCode + " k: " + k),
//                        C(A([4,d.type[3]=='d'?1:0],0,0,k))};

// 1009
K=function(d,e,f,g,h){return k=d.which,e=d.shiftKey<<5,
                               k += k<16 ? 255<<8
                                         : k<32 ? 65490
                                                : k<64 ? -e/2
                                                       : k<128 ? 32-e
                                                               : e/2-144,
                        console.log("keyCode: " + d.keyCode + " k: " + k),
                        C(A([4,d.type[3]=='d'?1:0],0,0,k))};

// 1019
//K=function(d,e,f,g,h){return k=d.which,e=d.shiftKey<<5,
//                               k += k<16 ? 255<<8
//                                         : k<32 ? 65490
//                                                : k<64 ? -e/2
//                                                       : k<128 ? 32-e
//                                                               : k<192 ? e/2-144 : e-128,
//                        console.log("keyCode: " + d.keyCode + " k: " + k),
//                        C(A([4,d.type[3]=='d'?1:0],0,0,k))};

// Support all buttons and suppress bubbling and default actions
////U=function(d,e,f,g,h){return d.stopPropagation(),d.preventDefault(),false};
////M=function(d,e,f,g,h){return C(A([5,m],d.pageX,d.pageY)),U(d)};
////N=function(d,e,f,g,h){return d.type[5]=='d'?m|=1<<d.button:m&=255-1<<d.button,M(d)};
////// More symbol support with default action suppression (for tab)
////K=function(d,e,f,g,h){return k=d.which,e=d.shiftKey<<5,
////                               k += k<16 ? 255<<8
////                                         : k<32 ? 65490
////                                                : k<64 ? -e/2
////                                                       : k<128 ? 32-e
////                                                               : k<192 ? e/2-144 : e-128,
////                        console.log("keyCode: " + d.keyCode + " k: " + k),
////                        C(A([4,d.type[3]=='d'?1:0],0,0,k)),U(d)};

// RFB/VNC handshake
R=function(d,e,f,g,h){
    //if (d.length < 30) { console.log('d (' + d.length + '): ' + d);
    //} else { console.log('d[0..30] (' + d.length + '): ' + d.slice(0,30)) }
    return s==2 ? (
        W=c.width=B(d,0),H=c.height=B(d,2),
        console.log('Connected: width ' + W + ' height ' + H),
        onkeydown=onkeyup=K,onmousemove=M,onmousedown=onmouseup=N,
////        oncontextmenu=U,
        setInterval(F,255))
        : 0,
    s<3 ? C(['RFB 003.003\n',[1],A([2,0],1,0,0)][s++])
        : V(Q.push.apply(Q,d));
};

// Normal VNC message. Length of Q in d
V=function(d){
    t = Q[0];
    if (!r) {
        l = [20,1,2,8+B(Q,6)][t];
        if (d<l) return;
        if (!t) r=B(Q,2),l=4;
        //if (t==0) console.log('new FBU, rects: ' + r);
        Q.splice(0,l)
    }
    for(;r;r--) {
        // Process it
        w=B(Q,4),h=B(Q,6);
        l = 12+4*w*h;
        //console.log('Q.length: ' + Q.length + ' size: 12 + 4*' + w + 'x' + h + '=' + (12 + 4*w*h));
        if (!l||Q.length<l) return;
        //console.log('w:' + w);
        I=a.createImageData(w,h);
        z=I.data;
        //console.log('Drawing image ' + w + 'x' + h);
        for (i=0,j=12; j<l; i+=4,j+=4) {
            for (k=0;k<3;k++) z[i+k]=Q[j+2-k];
            z[i+3] = 255; // Set Alpha
        }
        a.putImageData(I,B(Q,0),B(Q,2));
        Q.splice(0,l)
    }
};
S.onmessage=function(d,e,f,g,h){return R([].map.call(atob(d.data),D))};
//S.onclose=function(d,e,f,g,h){return alert("closed")};