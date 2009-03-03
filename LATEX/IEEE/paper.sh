#!/bin/bash

rm -f paper.aux paper.bbl paper.blg paper.log || exit 1

pdflatex paper || exit 1
bibtex paper || exit 1
pdflatex paper || exit 1
pdflatex paper || exit 1

rm -f paper.aux paper.bbl paper.blg paper.log || exit 1

acroread paper.pdf &

exit 0
