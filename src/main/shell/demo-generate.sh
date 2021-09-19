#!/bin/sh

JAR="../../../target/cryptator-0.4.0-SNAPSHOT-with-dependencies.jar"
function solve() {
    java -cp $JAR cryptator.Cryptagen -c TRUE $* | sed -n 's/\(.*+.*=.*\)/  - \1/p'
}

DIR="../../words"

echo "Search cryptarithms with a UNIQUE solution."

######
echo -e "\n## Generate from a word list.\n"

echo "- Planets"
solve $DIR/planets.txt
echo "- European countries"
solve $DIR/europeans.txt
echo "- Rainbow colors"
solve $DIR/rainbow.txt

######
echo -e "\n## Generate from a word list with a fixed second member.\n"

echo "- Planets"
solve $DIR/planets.txt planets
echo "- Rainbow colors"
solve $DIR/rainbow.txt colors
solve $DIR/rainbow.txt rainbow

######
echo -e "\n## Generate doubly true cryptarithms. each number in [0.100] appears at most once.\n"

echo "- English"
solve 1 100
echo "- French"
solve -ctry FR -lang fr 1 100
echo "- Italian"
solve -ctry IT -lang it 1 100
