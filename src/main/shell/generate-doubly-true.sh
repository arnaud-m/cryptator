#!/bin/sh

## sh generate-doubly-true.sh  > generate-doubly-true-output.md
JAR="../../../target/cryptator-*-with-dependencies.jar"
LANGUAGES=language-codes.csv
MIN=1
MAX=100

## Execute the command.
## Filter the output: print only the cryptarithm.
function solve() {
    java -cp $JAR cryptator.Cryptagen -c TRUE $* | sed -n 's/\(.*+.*=.*\)/  - \1/p'
}

echo "# Doubly true cryptarithms between $MIN and $MAX"
# Read the csf files with a list of country codes, and languages codes
while IFS="," read ctry lang ; do
    echo "- $lang"
    # Search doubly true cryptarithms in the given language
    solve -ctry $ctry -lang $lang $MIN $MAX
done < <(grep -v "^#\|^$" $LANGUAGES)
