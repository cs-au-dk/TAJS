function logTestEnd(success, indentation) {
    if (success) {
        console.log("%s(***expected behaviour***)", indentation);
    }
}

/**
 * Predicate that spawns a JVM instance for each query
 */
function makeJavaProcessPredicate(tajs_args, java_args, tajs_main, classpath) {
    var spawnSync = require("child_process").spawnSync;

    function checkArray(a) {
        if (!(a && a instanceof Array)) {
            throw "Not an array: " + a;
        }
    }

    function checkString(s) {
        if (typeof s !== "string") {
            throw "Not a string: " + s;
        }
    }

    checkArray(tajs_args);
    checkArray(java_args);
    checkString(tajs_main);
    checkString(classpath);

    function jsdelta_predicate(file, indentation) {
        // build command line
        var args_array = [];
        args_array = args_array.concat(java_args).concat(["-classpath", classpath]);
        args_array = args_array.concat([tajs_main].concat(tajs_args));
        args_array.push(file);

        // run TAJS
        try {

            var result = spawnSync("java", args_array);

            var success = result.status === 0;
            logTestEnd(success, indentation);
            return success;
        } catch (e) {
            return false;
        }
    }

    return jsdelta_predicate;
}

/**
 * Predicate that communicates with an JVM instance server for each query (faster than spawning new JVM instances)
 */
function makeServerProcessPredicate(args, port) {
    var execSync = require("child_process").execSync,
        util = require('util');

    return function (file, indentation) {
        var args_array = ["TEST"];
        args_array = args_array.concat(args);
        args_array.push(file);
        var message = args_array.join(" ");
        try {
            // synchronous network access
            // TODO this requires netcat on the path, but nodejs does not provide another solution
            var cmd = util.format("echo '%s' | nc localhost %d", message, port);
            var result = execSync(cmd).toString();

            var success = "SUCCESS" === result;
            logTestEnd(success, indentation);
            return success;
        } catch (e) {
            return false;
        }
    }
}