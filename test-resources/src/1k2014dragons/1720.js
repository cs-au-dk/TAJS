// Compressed with :
//
// http://refresh-sf.com/yui/
// http://jscompress.com/ 
// http://www.iteral.com/jscrush/

            (function(g, h, n, k) {
                var
                        r = 0,
                        D = function(g, h, n, k) {
                            document.getElementById(h + "" + n).className = k;
                            (function(g, h, n, k) {
                                for (n = h - 1; 0 < n && document.getElementById(g + "" + n).className == ["Red", "Yellow"][r]; n--) {
                                }
                                for (e = h + 1; 8 > e && document.getElementById(g + "" + e).className == ["Red", "Yellow"][r]; e++) {
                                }
                                return 4 < e - n
                            })(h, n) || function(g, h, n, k) {
                                for (k = g + 1; 7 > k && document.getElementById(k + "" + h).className == ["Red", "Yellow"][r]; k++) {
                                }
                                return 3 < k - g
                            }(h, n) || function(g, h, n, k) {
                                n = g - 1;
                                k = g + 1;
                                for (e = h - 1; 0 < n && !(1 > e) && document.getElementById(n + "" + e).className == ["Red", "Yellow"][r]; n--)
                                    e--;
                                for (e = h + 1; 7 > k && !(7 < e) && document.getElementById(k + "" + e).className == ["Red", "Yellow"][r]; k++)
                                    e++;
                                return 4 < k - n
                            }(h, n) || function(g, h, n, k) {
                                n = g - 1;
                                k = g + 1;
                                for (e = h + 1; 0 < n && !(7 < e) && document.getElementById(n + "" + e).className == ["Red", "Yellow"][r]; n--)
                                    e++;
                                for (e = h - 1; 7 > k && !(1 > e) && document.getElementById(k + "" + e).className == ["Red", "Yellow"][r]; k++)
                                    e--;
                                return 4 < k - n;
                            }(h, n) ? (d = 1, confirm(["Red", "Yellow"][r] + " won.\nRestart?") && function() {
                                d = 0;
                                z.innerHTML = ["Red", "Yellow"][r = (r + 1) % 2];
                                for (n = 1; 7 > n; n++)
                                    for (e = 1; 8 > e; e++)
                                        document.getElementById(n + "" + e).className = ""
                            }()) : z.innerHTML = ["Red", "Yellow"][r = (r + 1) % 2]
                        },
                        e = function(g, h, n, k) {
                            [
                                ".Yellow{background:#ff0}",
                                ".Red{background:#f00}",
                                "div,table{background:#00f}",
                                "td{width:40px;height:40px;border-radius:21px;background:#fff}",
                                "table{padding:7px;border-spacing:4px;border-radius:8px 8px 0 0}",
                                "div{height:55px;width:12px;border-radius:0 0 8px 8px;float:left}",
                                "body,table{width:340px;text-align:center;margin:20px auto 0}",
                                "div ~ div{float:right}"
                            ].map(function(g, h, n, k) {
                                document.styleSheets[0].insertRule(g, 2)
                            });

                            b.innerHTML = "Next : ";
                            b.appendChild(z = document.createElement("i"));
                            b.appendChild(g = document.createElement("table"));
                            b.appendChild(k = document.createElement("div"));
                            b.appendChild(k = document.createElement("div"));
                            b.appendChild(k = document.createElement("button"));
                            k.innerHTML = "Restart";
                            k.onclick =
                                    function(g, h, n, k) {
                                        confirm("Restart?") && function() {
                                            d = 0;
                                            z.innerHTML = ["Red", "Yellow"][r = (r + 1) % 2];
                                            for (n = 1; 7 > n; n++)
                                                for (e = 1; 8 > e; e++)
                                                    document.getElementById(n + "" + e).className = ""
                                        }();
                                    };
                            d = 0;
                            z.innerHTML = ["Red", "Yellow"][r = (r + 1) % 2];
                            for (n = 1; 7 > n; n++) {
                                h = document.createElement("tr");
                                for (e = 1; 8 > e; e++) {
                                    k = document.createElement("td");
                                    k.id = n + "" + e;
                                    k.onclick = function(g, h, n, k) {
                                        return function() {
                                            if (!d) {
                                                for (n = 6; 0 < n; n--) {
                                                    if (document.getElementById(n + "" + g).className == "") {
                                                        D(0, n, g, ["Red", "Yellow"][r]);
                                                        break
                                                    }
                                                }
                                            }
                                        }
                                    }(e);
                                    h.appendChild(k)
                                }
                                g.appendChild(h)
                            }
                        }()
            })(d());