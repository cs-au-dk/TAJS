#!/bin/sh
#Time-stamp: <2009-03-17 15:01:06 simonhj>

benchmarks="3d-cube 3d-morph 3d-raytrace access-binary-trees access-fannkuch access-nbody access-nsieve bitops-3bit-bits-in-byte bitops-bits-in-byte bitops-bitwise-and bitops-nsieve-bits controlflow-recursive crypto-aes crypto-md5 crypto-sha1 date-format-tofte date-format-xparb math-cordic math-partial-sums math-spectral-norm regexp-dna string-base64 string-fasta string-tagcloud string-unpack-code string-validate-input"

prefix="http://www2.webkit.org/perf/sunspider-0.9/"

for x in $benchmarks
do
    echo $prefix$x".html ---> " $x".js"
    wget -qO - $prefix$x".html" |  sed -n '/^<script>/,/<\/script>/p' | sed -e 's/<script>//' -e 's/<\/script>//' > $x".js"
done
