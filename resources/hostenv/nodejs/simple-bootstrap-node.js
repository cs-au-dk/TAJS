/*
* Sets up environment for Node.js. Loads the require mechanism from simple-require.js and
* starts analysis of the file to be analyzed by requiring it.
*/

(function () {
    function loadRequire() {
        var simpleRequire = TAJS_load("simple-require.js", true, "module");
        var module = {
            exports: {}
        };
        simpleRequire(module);
        return module.exports.require_with_parentFilename;
    }

    var require_with_parentFilename = loadRequire();
    this.TAJS_global_exports = require_with_parentFilename(TAJS_getMain(), null);
})();
