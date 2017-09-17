// Compressed with :
//
// http://refresh-sf.com/yui/
// http://jscompress.com/ 
// http://www.iteral.com/jscrush/

            (function(g, h, n, k) {
                var
                        r = 0,
                        D = function(g, h, n, k) {
                            0 < g && (document.getElementById(g + "" + n).className = "");
                            document.getElementById(++g + "" + n).className = k;
                            g == h ? function(g, h, n, k) {
                                for (n = h - 1; 0 < n && document.getElementById(g + "" + n).className == ["Red", "Yellow"][r]; n--) {
                                }
                                for (e = h + 1; 8 > e && document.getElementById(g + "" + e).className == ["Red", "Yellow"][r]; e++) {
                                }
                                return 4 < e - n
                            }(h, n) || function(g, h, n, k) {
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
                            }(h, n) ? (d = 1, confirm(["Red", "Yellow"][r] + " player won.\nDo you want to play a new game?") && function() {
                                d = 0;
                                z.innerHTML = z.className = ["Red", "Yellow"][r = (r + 1) % 2];
                                for (n = 1; 7 > n; n++)
                                    for (e = 1; 8 > e; e++)
                                        document.getElementById(n + "" + e).className = ""
                            }()) : z.innerHTML = z.className = ["Red", "Yellow"][r = (r + 1) % 2] : setTimeout(function() {
                                D(g, h, n, k)
                            }, 42)
                        },
                        e = function(g, h, n, k) {
                            [
                                "@media (max-width:280px){body{width:200px;}td{height:22px;}*{box-shadow:none!important}}",
                                "@media (max-width:370px){body{width:249px;}td{height:29px;border-width:0 }div{width:8px; height:40px}button{margin:10px auto 0}td{box-shadow: inset 3px 3px 10px 0px rgba(50, 50, 50, 0.4)}td.Red,td.Yellow{box-shadow: inset 0 0 10px rgba(99,99,99,0.7)}div,table{box-shadow: 3px 3px 10px 0px rgba(50, 50, 50, 0.6)}}",
                                "@media (min-width:700px){body{width:601px;}td{height:71px;border-width:3px }div{width:16px; height:90px}button{padding:5px 20px}td{box-shadow: inset 15px 15px 15px 0px rgba(50, 50, 50, 0.25)}td.Red,td.Yellow{box-shadow: inset 0 0 20px rgba(99,99,99,0.8)}div,table{box-shadow: 15px 15px 15px 0px rgba(50, 50, 50, 0.3)}}",
                                "@media (min-width:600px){body{width:503px;}td{height:59px;border-width:2px }div{width:14px; height:80px}button{margin:30px auto 0;padding:4px 10px}}",
                                "@media (min-width:500px){body{width:405px;}td{height:47px;border-width:1px }div{width:13px; height:60px}}",
                                ".Red{background:#f00;color:#fff}",
                                ".Yellow{background:#ff0}",
                                ".Red:hover,.Yellow:hover{opacity:1}",
                                "td.Red,td.Yellow{box-shadow: inset 0 0 25px rgba(99,99,99,0.7)}",
                                "td:hover{opacity:0.6}",
                                "div,table{background:#00f}",
                                "body,table{margin:30px auto 0}",
                                "button{margin:20px auto 0}",
                                "td{border:0px solid #00f;border-radius:2000px;box-shadow: inset 7px 7px 20px 0px rgba(50, 50, 50, 0.4);opacity:1;height:40px;background:#fff}",
                                "table{border:0px solid #00f;box-shadow: 7px 7px 28px 0px rgba(50, 50, 50, 0.6);padding:7px;border-spacing:4px;border-radius:8px 8px 0 0}",
                                "div{box-shadow: 7px 7px 28px 0px rgba(50, 50, 50, 0.6);height:55px;width:12px;border-radius:0 0 8px 8px;float:left}",
                                "body{font-family:Helvetica;width:340px;text-align:center}",
                                "table{width:100%;}",
                                "div ~ div{border:0px solid #00f;float:right}",
                                "i{border-radius:21px;padding:9px;transition: 0.2s ease-out;width:80px;display:inline-block}"
                            ].map(function(g, h, n, k) {
                                document.styleSheets[0].insertRule(g, 2)
                            });

                            b.innerHTML = "Next player : ";
                            b.appendChild(z = document.createElement("i"));
                            b.appendChild(g = document.createElement("table"));
                            b.appendChild(k = document.createElement("div"));
                            b.appendChild(k = document.createElement("div"));
                            b.appendChild(k = document.createElement("button"));
                            k.innerHTML = "Restart";
                            k.onclick =
                                    function(g, h, n, k) {
                                        confirm("New game?\nAre you sure?") && function() {
                                            d = 0;
                                            z.innerHTML = z.className = ["Red", "Yellow"][r = (r + 1) % 2];
                                            for (n = 1; 7 > n; n++)
                                                for (e = 1; 8 > e; e++)
                                                    document.getElementById(n + "" + e).className = ""
                                        }();
                                    };
                            d = 0;
                            z.innerHTML = z.className = ["Red", "Yellow"][r = (r + 1) % 2];
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