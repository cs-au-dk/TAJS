var o = {
    get latest() {
        if (this.log.length > 0) {
            return this.log[this.log.length - 1];
        }
        else {
            return null;
        }
    },
    log: []
};
dumpObject(o);
