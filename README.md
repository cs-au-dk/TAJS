TAJS - Type Analyzer for JavaScript
===================================

Copyright 2009-2016 Aarhus University

TAJS is a dataflow analysis for JavaScript that infers type information and call graphs.
The current version of the analysis contains a model of ECMAScript 3rd edition, including the standard library, and partial models of the ECMAScript 5 and its standard library, the HTML DOM, and the browser API.
Later version of ECMAScript are partially supported via [Babel](http://babeljs.io/).

For research publications and other information about this tool see <http://www.brics.dk/TAJS>.

How to build and run the tool
-----------------------------

The simplest way to build TAJS is to run `ant`, which will build two jar files: `dist/tajs.jar` (which contains only TAJS itself) and `dist/tajs-all.jar` (which also includes the relevant extra libraries).

The jar files are also available for download at <http://www.brics.dk/TAJS/dist/>.

You can now run the analysis as, for example:
```
java -jar dist/tajs-all.jar test/google/richards.js
```
or
```
java -jar dist/tajs-all.jar test/chromeexperiments/3ddemo.html
```

By default, TAJS outputs some information about its progress and eventually a list of type warnings and other messages. 

Some of the available options (run TAJS without arguments to see the full list):

- `-callgraph` - output call graph as text and in a file `out/callgraph.dot` (process with [Graphviz dot](http://www.graphviz.org/))

- `-show-variable-info` - output type and line information about all variables

- `-debug` - output extensive internal information during the analysis

- `-flowgraph` - output the initial and final flow graphs (TAJS's intermediate representation) as text and to `out/flowgraphs/` (in Graphviz dot format, with a file for each function and for the complete program)

- `-low-severity` - enable many more type warnings

- `-quiet` - only print results, not information about analysis progress

- `-states` - output intermediate abstract states during the analysis

- `-uneval` - enable the Unevalizer for on-the-fly translation of `eval` calls, as described in 'Remedying the Eval that Men Do', ISSTA 2012

- `-determinacy` - enable the techniques described in 'Determinacy in Static Analysis of jQuery', OOPSLA 2014

Note that the analysis produces lots of addition information that is not output by default. If you want full access to the abstract states and call graphs, as a starting point see the source code for `dk.brics.tajs.Main`. 
The javadoc for TAJS is available at <http://www.brics.dk/TAJS/doc/>.

Special built-in functions
--------------------------

TAJS recognizes a few special built-in functions (defined as properties of the global object) to support debugging and testing of the tool, including:

- `TAJS_dumpValue(exp)` - report the abstract value of expression `exp` after analysis has completed

- `TAJS_dumpObject(obj)` - report the properties of the abstract object `obj` after analysis has completed

- `TAJS_dumpState()` - report the abstract state at this program point after analysis has completed

- `TAJS_assert(value)` - tests that `value` is `true`, failure will result in an AssertionError. 

- `TAJS_assert(value, predicate)` - a generalized version of the single-argument TAJS_assert, supports disjunctions of the predicate methods in Value.java. E.g. to check that a value is either a single concrete string or some unsigned integer: 
  - `TAJS_assert(myValue, 'isMaybeSingleStr || isMaybeNumUInt')`

Running regression tests
------------------------

The directory `test` contains a collection of tests that can be executed by running [dk.brics.tajs.test.RunFast](test/src/dk/brics/tajs/test/RunFast.java) with JUnit from Eclipse/IntelliJ or with `ant test` from the command-line. 
(A more thorough but slower test located in [dk.brics.tajs.test.RunAll](test/src/dk/brics/tajs/test/RunAll.java) can be run with `ant test-all`.)

Soundiness
----------

The analysis models of the HTML DOM, the browser API, and the ECMAScript native library are not complete. 
For a list of other known sources of unsoundness, see <https://github.com/cs-au-dk/TAJS/issues?q=is%3Aopen+is%3Aissue+label%3Asoundiness>.

Package dependencies
--------------------

This diagram shows the main package dependencies:

![package dependencies](misc/package-dependencies.png)

Modifications of the source code should avoid introducing upwards dependencies in this diagram.

Authors
-------

The following people have contributed to the source code:
- Anders Møller
- Esben Andreasen
- Simon Holm Jensen
- Peter Thiemann
- Magnus Madsen
- Matthias Diehn Ingesman
- Peter Jonsson

This software includes components from:
- Google Closure Compiler (<http://code.google.com/p/closure-compiler/>)
- Jericho HTML Parser (<http://jericho.htmlparser.net/>)
- Log4j (<http://logging.apache.org/log4>)


