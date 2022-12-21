JAR=cryptator-0.5.1-SNAPSHOT-with-dependencies.jar
MAINCLASS=cryptator.Cryptator

MAINARGS=`cat $1 | xargs`

## exclude cryptarithm with long words
## grep -wvE '\w{10,}' $2 | sed 's/[[:space:]]//g'  | xargs java -server  -Xms512m -Xmx8192m -cp $JAR $MAINCLASS $MAINARGS

## exclude cryptarithm without long words
## grep -wE '\w{10,}' $2 | sed 's/[[:space:]]//g'  | xargs java -server  -Xms512m -Xmx8192m -cp $JAR $MAINCLASS $MAINARGS

## Do not exclude anything
sed 's/[[:space:]]//g'  $2 | xargs java -server  -Xms512m -Xmx8192m -cp $JAR $MAINCLASS $MAINARGS
exit $?
