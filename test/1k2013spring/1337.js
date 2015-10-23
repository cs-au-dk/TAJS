document.write('<style>progress{-moz-orient:vertical;height:100px!important}</style>'); + function () {
    P = function (b) {
        this._ = function (a, b) {
            this.e.style.width = a + "px";
            this.e.style.height = b + "px";
            this.width = a;
            this.height = b
        };
        this.p = function (a, b, c) {
            var d = c ? 0 : this.height / 2;
            this.e.style.left = a - (c ? 0 : this.width / 2) + "px";
            this.e.style.top = b - d + "px";
            this.x = a;
            this.y = b
        };
        this.r = function (a) {
            this.e.style.webkitTransform = this.e.style.MozTransform = this.e.style.transform = "rotate(" + a + ")";
            this.e.value = 13 * Math.round((Math.round(parseInt(a)) + 360) / 100)
        };
        this.content = function (a) {
            this.e.innerHTML = a
        };
        this.e = document.createElement("progress");
        this.e.style.position = "absolute";
        this.e.max = 100;
        b.appendChild(this.e)
    };
    e = b;
    Ps = 18;
    q = 2 * Math.PI / Ps;
    $ = -Math.PI;
    m = y = x = ta = 0;
    pc = [];
    for (c = 0; c < Ps; c++) {
        d = new P(e);
        d.content(" ".substr(c, 1));
        d._(40, 40);
        pc.push(d)
    }
    setInterval(function () {
        m = 80 * Math.sin($);
        for (var b = 0; b < Ps; b++) x = m * Math.cos($) + 200, y = m * Math.sin($) + 200, ta = 180 * Math.atan2(y - 200, x - 200) / Math.PI + 90 + "deg", pc[b].r(ta), pc[b].p(x, y), $ += q;
        $ += 0.06
    }, 1E3 / 30)
}();