var w = 512, h = w, i = 0, x, y,
            B = 36160,
            T = 3553,
            F = 9729,
            C = 6408,
            A = 34962,
            N = 5126,
            t = [],
            f = [],
            s, p, j;

        document.onmousemove = function (b, c) {
            x = b.pageX / w;
            y = 1 - b.pageY / h;
        };

        function S(b, c) {
            s = g.createShader(b);
            g.shaderSource(s, c);
            g.compileShader(s);
            return s;
        }
        function D(b,c) {
            g.attachShader(p, b);
        }
        function E(b, c) {
            g.bindFramebuffer(B, b);
        }
        function P(b, c) {
            g.texParameteri(T,b,F);
        }
        p = g.createProgram();
        D(S(35633, "attribute vec3 b;varying vec2 u;void main(){gl_Position=vec4(b,1.);u=(b.xy+1.)/2.;}"));
        D(S(35632, "precision highp float;uniform vec2 p;varying vec2 u;uniform sampler2D s;void main(){gl_FragColor=texture2D(s,u)*.98+vec4(clamp(1.-length(u-p)*9.,0.,1.))+vec4(0,0,0,1);}"));
        g.linkProgram(p);
        g.useProgram(p);
        g.bindBuffer(A, g.createBuffer());
        g.bufferData(A, new Float32Array([-1, -1, 3, -1, -1, 3]), 35044);
        g.enableVertexAttribArray(0);
        g.vertexAttribPointer(0, 2, N, 0, 8, 0);
        for (; i < w * h * 4; i++) t[i] = 0;
        for (i = 0; i < 2; i++) {
            f[i] = g.createFramebuffer();
            f[i].t = g.createTexture();
            g.bindTexture(T, f[i].t);
            g.pixelStorei(3317, 1);
            g.texImage2D(T, 0, C, w, h, 0, C, N, new Float32Array(t));
            P(10241);
            P(10240);
            E(f[i]);
            g.framebufferTexture2D(B, 36064, T, f[i].t, 0);
        }

        function R(b,c) {
            g.activeTexture(33984 + j);
            g.bindTexture(T, f[j].t);
            E(b);
            g.drawArrays(4, 0, 3);
        }

        i = 1;
        (function $() {
            j = 1-i;
            g.uniform1i(g.getUniformLocation(p, "s"), j);
            g.uniform2f(g.getUniformLocation(p, "p"), x, y);
            g.viewport(0, 0, w, h);
            R(f[i]);
            R(null);
            i = j;
            requestAnimationFrame($);
        })();