JAR=cryptator-0.5.1-SNAPSHOT-with-dependencies.jar
MAINCLASS=cryptator.Cryptagen

MAINARGS=`cat $1 $2 | xargs`

INSTNAME=`echo $INSTNAME | sed 's/\s-*/-/g'`
echo i `basename -s .dat $2`
## SEED=`shuf -i1-1000 -n1`
java -server  -Xms512m -Xmx8192m -cp $JAR $MAINCLASS $MAINARGS
exit $?
