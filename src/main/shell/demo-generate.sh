#!/bin/sh
## sh demo-generate.sh > demo-generate-output.md
JAR="../../../target/cryptator-*-with-dependencies.jar"
DIR="../words"

## Execute the command.
## Filter the output: print only the cryptarithm.
function solve() {
    java -cp $JAR cryptator.Cryptagen -c TRUE $* | sed -n 's/\(.*+.*=.*\)/  - \1/p'
}



echo "# Search cryptarithms with a UNIQUE solution"

######
echo -e "\n## Generate from a word list\n"

echo "- Planets"
solve $DIR/planets.txt
echo "- European countries"
solve $DIR/europeans.txt
echo "- Rainbow colors"
solve $DIR/rainbow.txt

######
echo -e "\n## Generate from a list with a fixed right member\n"

echo "- Planets"
solve $DIR/planets.txt planets
echo "- Greek alphabet"
solve  -minop 6 $DIR/alpha.txt greeks

######
echo -e "\n## Generate doubly true cryptarithms\n\nEach number in [0, 100] appears at most once.\n"

echo "- English"
solve 1 100
echo "- French"
solve -ctry FR -lang fr 1 100
echo "- Italian"
solve -ctry IT -lang it 1 100
