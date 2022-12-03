JAR=cryptator-0.5.1-SNAPSHOT-with-dependencies.jar
MAINCLASS=cryptator.Cryptagen

MAINARGS=`cat $1 $2 | xargs`

java -server  -Xms512m -Xmx8192m -cp $JAR $MAINCLASS $MAINARGS | head -n -10
exit $?
