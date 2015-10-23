G = function(i) {
    return unescape(encodeURI(i)).length;
}

S = function(C) {
    M = C.length / 2;

    // Searches the input string for the best possible replacement
    B = function(i) {
        g = t = u = 0, v = {};

        for (y = 2, z = M; y <= z; ++y) {
            for (h = 0, l = i.length - y; h < l; ++h) {
                if (!v[e = i.substr(h, y)]) {
                    for (v[e] = 1, f = h; ~(f = i.indexOf(e, f + y)); ) {
                        v[e]++;
                        M = y;
                    }
                }
            }
        }

        for (e in v) {
            if ( (j = v[e]) > 1) {
                s = G(e) * (j - 1) - j - 2;
                if (s > t || (s == t && j < u) ) {
                    t = s;
                    u = j;
                    g = e;
                }
            }
        }

        X = g;
    };

    // Get all the characters in the character code range 1-127 that don't appear in str and aren't line breaks
    for (d = 1, F = []; d < 127; ++d) {
        e = String.fromCharCode(d);
        if (!/[\r\n'"\\]/.test(e) && !~C.indexOf(e)) {
            F.push(e);
        }
    }

    // Arrange characters so that control characters come last
    F.sort(function(i, j) {
        return i > j ? 1 : i < j ? -1 : 0;
    });

    Z = "";

    // Replace substrings with single characters while we still have free characters and worthwhile replacements
    while ( (Y = F.pop()) && (B(C), X) ) {
        C = C.split(X).join(Y) + Y + X;
        Z = Y + Z;
    }

    // Get the most popular type of quote to minimize escaping in the output string
    q = C.split("'").length < C.split('"').length ? "'" : '"';

    // Create the output
    Q = "f=" + q + C.replace(/[\r\n\\]/g, "\\$&").replace(RegExp(q, "g"), "\\" + q) + q + ";for(i in g=" + q + Z + q + ")e=f.split(g[i]),f=e.join(e.pop());eval(f)";
}

I = function(i) {
    return document.getElementById(i);
};

c.insertAdjacentHTML("beforebegin", [
    'JS:',
    '<textarea cols="99" rows="12" id="c"></textarea>',
    '<input type="checkbox" id="e" checked> Run?',
    '<input type="button" value="crush" id="r">',
    'Crushed:',
    '<textarea cols="99" rows="12" id="d"></textarea>',
    '<div id="i"></div>'
].join("<br>"));

I("r").onclick = function() {
    V = I("c").value;
    S(V);
    I("d").value = Q;
    I("i").innerHTML = V.length + " (" + G(V)
            + "B) to " + Q.length + " (" + G(Q) + "B)";

    if (I("e").checked) {
        setTimeout("eval(Q)", 0);
    }
};