# Delta debugging

This package supports automatic minification of JavaScript files that cause a specific behavior of TAJS. 

(Currently only for Linux and Mac OS X, not Windows.)
 
## Setup

To use delta debugging, jsdelta (https://www.npmjs.com/package/jsdelta) needs to be installed:

```
cd extras/jsdelta
npm install
```
Alternatively, if you already have jsdelta installed elsewhere, set the location in tajs.properties:
```
jsdelta = /usr/bin/jsdelta/delta.js
```

In order to speed up the minification process, a server-system can be set up (see `DeltaMinimizerServer`). 
It is run by `DeltaMinimizer` if one or more ports are specified in `tajs.properties`:
```
jsdeltaserverports = 9900 9901 9902 9903 9904
```

## Usage

The minification can be done by using the `dk.brics.tajs.jsdelta.DeltaMinimizer` class.
See the javadoc of that class.

A simple usage example:

```java
DeltaMinimizer.reduce(Paths.get("/path/to/some-file.js"), MyRunPredicate.class);
```

This will produce a minimized version of `some-file.js`. The minimized version satisfies the test method of `MyRunPredicate class`.
Note that there are no restrictions on how `MyRunPredicate` is implemented; it could in principle invoke another program than TAJS.

Sample output for the minification process can be seen below. Notice that the final version is both present as a file and as printed output.
 
```
Initializing and sanity-checking minimization...
Starting minimization...
Started JSDelta process for target: /tmp/test.js.dir430022395006916871/test.js (at time: 2017-06-13T14:11:47.064, in thread: main)
jsdelta (main):::	 (***expected behaviour***)
jsdelta (main):::	 Starting iteration #0
jsdelta (main):::	 Target: /tmp/jsdelta-21505qRRNPFHCxUsr/delta_js_1.js (1 bytes)
...
...
...
jsdelta (main):::	 Transforming candidate /tmp/jsdelta-21505qRRNPFHCxUsr/delta_js_11.js
jsdelta (main):::	 Transforming candidate /tmp/jsdelta-21505qRRNPFHCxUsr/delta_js_13.js
jsdelta (main):::	 Starting iteration #1
jsdelta (main):::	 Target: /tmp/jsdelta-21505qRRNPFHCxUsr/delta_js_15.js (1 bytes)
jsdelta (main):::	 Target: /tmp/jsdelta-21505qRRNPFHCxUsr/delta_js_16.js (6 bytes)
jsdelta (main):::	 Target: /tmp/jsdelta-21505qRRNPFHCxUsr/delta_js_17.js (48 bytes)
jsdelta (main):::	 Target: /tmp/jsdelta-21505qRRNPFHCxUsr/delta_js_18.js (48 bytes)
jsdelta (main):::	 Transforming candidate /tmp/jsdelta-21505qRRNPFHCxUsr/delta_js_19.js
jsdelta (main):::	 Transforming candidate /tmp/jsdelta-21505qRRNPFHCxUsr/delta_js_21.js
jsdelta (main):::	 Minimisation finished; final version is at /tmp/jsdelta-21505qRRNPFHCxUsr/delta_js_smallest.js (77 bytes)
Ended JSDelta process for target: /tmp/test.js.dir430022395006916871/test.js, with result: /tmp/jsdelta-21505qRRNPFHCxUsr/delta_js_smallest.js (at time: 2017-06-13T14:12:10.444, in thread: main)
Minimization finished:
	location: /tmp/jsdelta-21505qRRNPFHCxUsr/delta_js_smallest.js
	lines: 4
	characters: 76
	content:
`` `
(function () {
    var arg0 = arguments[0];
    var arg1 = arguments[1];
});
`` `
```

Note: the interaction between jsdelta and Java relies on some classpath hacking and repeated restarts of JVMs.
As such, users of this package should be careful not to rely on special classpath setups and non-persistent data between runs.


## Example minification predicates

See the classes in the `src/dk/brics/jsdelta/predicates/examples` directory for concrete examples of minifier predicates.
They can be adjusted easily by changing the path to the JavaScript file inside them.  

(Unless entirely new examples are created, new classes should not be committed to the examples directory.)

