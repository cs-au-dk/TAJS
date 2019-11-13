/*
 * host-function-sources note:
 *
 * The required precision for precise analysis of the contents of this file is:
 *
 * - object-sensitivity (see example at hfs#1)
 * - parameter-sensitivity (see example at hfs#2)
 * - parameter-sensitivity for constructor results (see example at hfs#3)
 * - recency-abstraction (see example at hfs#4)
 * - closure-variable sensitivity (see example at hfs#5)
 */

(function (global) {

    // TODO make more like https://github.com/nodejs/node/blob/master/lib/module.js

    function Module(filename, parent) { // hfs#3: keep instantiations separate
        this.exports = TAJS_newObject();
        this.filename = filename; // hfs#4: strong update required
        fillModuleWithNonEssentials(this, parent);
    }

    function fillModuleWithNonEssentials(module, parent) { // context-insensitive: should not affect precision significantly
        module.parent = parent;
        module.loaded = TAJS_make("AnyBool");
        module.children = [];
        module.paths = [];
        module.paths[TAJS_make("AnyNumUInt")] = TAJS_make("AnyStr");
        if (parent != null) {
            parent.children.push(module);
        }
    }

    Module.prototype.require = function (path) { // hfs#1 + hfs#2 keep calls separate
        return require_with_parentFilename(path, this.filename);
    };

    var cache = {};
    var dummy_cache = {};
    var dummy_extensions = {
        '.js': function () {
            TAJS_NOT_IMPLEMENTED("_extensions['js']")
        },
        '.json': function () {
            TAJS_NOT_IMPLEMENTED("_extensions['json']")
        },
        '.node': function () {
            TAJS_NOT_IMPLEMENTED("_extensions['node']")
        }

    };

    function load(module, filename) {
        if (filename.endsWith('.json')) {
            return TAJS_loadJSON(filename);
        }

        TAJS_makeContextSensitive(require, 0);
        TAJS_makeContextSensitive(require, -2);
        function require(path) {
            return module/* hfs#5 keep calls separate */.require(path);
        }

        TAJS_makeContextSensitive(resolve, 0);
        TAJS_makeContextSensitive(resolve, -2);
        function resolve (request) {
            return TAJS_nodeRequireResolve(request, filename);
        }
        require.resolve = resolve;

        if(typeof process !== 'undefined' /* should work without external depedencies */) {
            require.main = process.mainModule;
        }
        // TODO make sound models of these two dummies (but we probably lose on precision if they are modified)
        require.extensions = dummy_extensions; // deprecated in new versions of Node.js
        require.cache = dummy_cache;

        var f = TAJS_load(filename, false, "exports", "require", "module", "__filename", "__dirname");

        var dirname = TAJS_parentDir(filename);
        f.apply(module.exports, [module.exports, require, module, TAJS_unURL(filename), TAJS_unURL(dirname)]);
        TAJS_moduleExportsFiltering(module.exports, filename);
        TAJS_assumeModuleType(filename, module);
    }

    function require_with_parentFilename(filename, parentFilename) {
        filename = TAJS_nodeRequireResolve(filename, parentFilename);

        if (filename in cache) {
            return cache[filename].exports;
        }

        var parentModule;
        if (parentFilename != null && parentFilename in cache) {
            parentModule = cache[parentFilename];
        } else {
            parentModule = null;
        }
        var module = new Module(filename, parentModule);

        cache[filename] = module;

        load(module, filename);

        return module.exports;
    }

    // ensure all functions are very sensitive
    TAJS_makeContextSensitive(Module, 0);
    TAJS_makeContextSensitive(Module.prototype.require, -1);
    TAJS_makeContextSensitive(Module.prototype.require, 0);
    TAJS_makeContextSensitive(load, 0);
    TAJS_makeContextSensitive(load, 1);
    TAJS_makeContextSensitive(require_with_parentFilename, 0);
    TAJS_makeContextSensitive(require_with_parentFilename, 1);

    module.exports.require_with_parentFilename = require_with_parentFilename;
})(this);
