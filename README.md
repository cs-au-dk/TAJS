# TAJS - Type Analyzer for JavaScript

Copyright 2009-2017 Aarhus University

TAJS is a dataflow analysis for JavaScript that infers type information and call graphs.

The current version of the analysis contains a model of ECMAScript 3rd edition, including the standard library, and partial models of the ECMAScript 5 and its standard library, the HTML DOM, and the browser API.
In some cases where ECMAScript has introduced incompatibilities with prior editions, including some changes made in ECMAScript 6, the analysis models the most recent language design.
Other recent ECMAScript language features are partially supported via [Babel](http://babeljs.io/). The browser API is modeled after Chrome.  

For research publications and other information about this tool see <http://www.brics.dk/TAJS>.

## How to build and run the tool

Make sure you clone not only the TAJS repository but also the submodules, for example by running
```
git submodule update --init --recursive
``` 

The simplest way to build TAJS is to run Ant:
```
ant
```
This will build two jar files: `dist/tajs.jar` (contains only TAJS itself) and `dist/tajs-all.jar` (includes the relevant extra libraries).

The jar files are also available for download at <http://www.brics.dk/TAJS/dist/>.

You can now run the analysis as, for example:
```
java -jar dist/tajs-all.jar test-resources/src/google/richards.js
```
or
```
java -jar dist/tajs-all.jar test-resources/src/chromeexperiments/3ddemo.html
```

By default, TAJS outputs some information about its progress and eventually a list of type warnings and other messages. 

Some of the available options (run TAJS without arguments to see the full list):

- `-inspector` - start *TAJS Inspector* after analysis (see [below](#tajs-inspector))

- `-callgraph` - output call graph as text and in a file `out/callgraph.dot` (process with [Graphviz dot](http://www.graphviz.org/))

- `-show-variable-info` - output type and line information about all variables

- `-debug` - output extensive internal information during the analysis

- `-flowgraph` - output the initial and final flow graphs (TAJS's intermediate representation) as text and to `out/flowgraphs/` (in Graphviz dot format, with a file for each function and for the complete program)

- `-low-severity` - enable many more type warnings

- `-quiet` - only print results, not information about analysis progress

- `-states` - output intermediate abstract states during the analysis

- `-statistics` - output various statistics about the analysis results

- `-uneval` - enable the Unevalizer for on-the-fly translation of `eval` calls, as described in ['Remedying the Eval that Men Do', ISSTA 2012](http://cs.au.dk/~amoeller/papers/unevalizer/)

- `-determinacy` - enable the techniques described in ['Determinacy in Static Analysis of jQuery', OOPSLA 2014](http://cs.au.dk/~amoeller/papers/jquery/)

- `-test-soundness` - test soundness using concrete execution as described in ['Systematic Approaches for Increasing Soundness and Precision of Static Analyzers', SOAP 2017](http://cs.au.dk/~amoeller/papers/tajsexperience/) (see [below](#soundness-testing))

- `-unsound X` - enable unsound assumption X, e.g. `ignore-unlikely-property-reads` causes some unlikely properties to be ignored during dynamic property read operations, and `show-unsoundness-usage` outputs usage of unsound assumptions

Note that the analysis produces lots of addition information that is not output by default. If you want full access to the abstract states and call graphs, as a starting point see the source code for `dk.brics.tajs.Main`. 
The javadoc for TAJS is available at <http://www.brics.dk/TAJS/doc/>.

## TAJS Inspector

*TAJS Inspector* (enabled using option `-inspector`) is a web-based interface to the analysis results. 
It is primarily intended as a development tool for investigating information collected during analysis. 
This includes abstract values of variables and properties, call graphs, and type warnings, but also internal analysis
information, such as, number of times each primitive instruction is processed, number of contexts for each function,
and "imprecision suspiciousness" of abstract states.

## Special built-in functions

TAJS recognizes a few special built-in functions to support debugging and testing of the tool.
For example, calling `TAJS_dumpState()` in the JavaScript program being analyzed will report the abstract state at the program point of the call.
The full list of functions is documented in [TAJS-functions.md](TAJS-functions.md).

## Soundiness

The analysis models of the HTML DOM, the browser API, and the ECMAScript native library are not 100% complete. 
For a list of other known sources of unsoundness, see <https://github.com/cs-au-dk/TAJS/issues?q=is%3Aopen+is%3Aissue+label%3Asoundiness>.

## Soundness testing

It is possible to test that the analysis fixpoint over-approximates concrete behaviors. 
This requires a log file of concrete behaviors to be provided to TAJS. 
Sample log files are located in [test-resources/logs](test-resources/logs).

Soundness testing, with an existing log file can be performed like this:
```
java -jar dist/tajs-all.jar -test-soundness -log-file test-resources/logs/google/richards.js.log.gz test-resources/src/google/richards.js
```

This will produce the usual output, with the following line appended:
```
...
Soundness testing succeeded for 1884 checks (with 0 expected failures)
```

Expected mismatches can be registered in [KnownUnsoundnesses.java](src/dk/brics/tajs/analysis/KnownUnsoundnesses.java).

New log files can be generated by running the analyzed program concretely and monitoring its behavior using [jalangilogger](https://www.npmjs.com/package/jalangilogger) (see [below](#environment-configuration)).
 
Soundness testing with a freshly generated log file can be performed like this:
```
java -jar dist/tajs-all.jar -test-soundness -generate-log -log-file my-log-file.log test-resources/src/google/richards.js
```
If DOM modeling is enabled (e.g. if the given file is an HTML file) then a browser is spawned and user input is processed (until stopped or timeout).
If analyzing a stand-alone JavaScript file, the log file is generated with Node.js instead of using a browser.

Changes to the source code of the analyzed program requires a new log file to be created. 

## Environment configuration

Some advanced features of TAJS require additional environment configuration which can be defined in a .tajsconfig [properties](https://docs.oracle.com/javase/8/docs/api/java/util/Properties.html) file. 
These features will automatically look for a .tajsconfig file in the working directory and its ancestor directories. 

### Installing external dependencies 

To generate log files for soundness testing, [Node.js](https://nodejs.org/en/download/) and [jalangilogger](https://www.npmjs.com/package/jalangilogger) must be installed.
The jalangilogger tool can be installed like this:
```
cd extras/jalangilogger/javascript
npm install
```

Individual external dependencies can be registered in .tajsconfig like this:

```properties
jalangilogger = /home/tajs-user/tajs/extras/jalangilogger/javascript/node_modules/jalangilogger
```

Alternatively, register the location of the TAJS installation:

```properties
tajs = /home/tajs-user/tajs
```

### Configurations for log file generation 

For generation of log files for soundness testing, TAJS needs to know the locations of `jjs` and `node`: 

```properties
jjs = /usr/bin/jjs
node = /usr/bin/nodejs
```
(On Windows, the paths are something like `C:/Program Files/Java/jdk1.8.0_131/bin/jjs.exe` and `C:/Program Files/nodejs/node.exe`.)

## Running regression tests

The directory `test` contains a collection of tests that can be executed by running [dk.brics.tajs.test.RunFast](test/src/dk/brics/tajs/test/RunFast.java) with JUnit from Eclipse/IntelliJ or with `ant test-fast` from the command-line. 
(A more thorough but slower test located in [dk.brics.tajs.test.RunAll](test/src/dk/brics/tajs/test/RunAll.java) can be run with `ant test-all`.)

## Dependencies

This software includes components from:

- Google Closure Compiler (<https://closure-compiler.github.io//>)
- Jericho HTML Parser (<http://jericho.htmlparser.net/>)
- Log4j (<http://logging.apache.org/log4j>)
- args4j (<http://args4j.kohsuke.org/>)
- Gson (<https://github.com/google/gson>)
- JUnit (<http://junit.org/>) (for development only)
- Jetty (<http://www.eclipse.org/jetty/>) (for *TAJS Inspector* only)

This diagram shows the main package source code dependencies:

![package dependencies](misc/package-dependencies.png)

[dk.brics.tajs.test.PackageDependencyTest](test/src/dk/brics/tajs/test/PackageDependencyTest.java) can be used for checking the package dependencies.

## Authors

The following people have contributed to the source code:
- Anders Møller
- Esben Andreasen
- Simon Holm Jensen
- Peter Thiemann
- Magnus Madsen
- Matthias Diehn Ingesman
- Peter Jonsson
- Benjamin Barslev Nielsen
- Christoffer Quist Adamsen
- Gianluca Mezzetti
- Martin Torp
- Simon Gregersen

