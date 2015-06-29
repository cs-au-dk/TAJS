function CSlider(c, t, n) {
    this.p = (t == "lr") ? "left" : "top";
    this.startSlide = function (d, o) {
        this.intrvl = setInterval("" + o + ".move('" + d + "')", 10);
        this.np = this.getCP()
    };
    this.getCP = function (d) {
        return eval("parseInt(this.c.style." + this.p + ".substring(0,this.c.style." + this.p + ".length-2))");
    };

}

var cs1 = new CSlider("vs_cntr11", "lr", 1);
cs1.startSlide('l', 'cs1');
cs1.as = false;