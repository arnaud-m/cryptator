# <u>TO COMPILE:</u>
```bash
$ export CLASSPATH=".:/usr/local/lib/antlr-4.9.2-complete.jar:$CLASSPATH"
$ java -jar /usr/local/lib/antlr-4.9.2-complete.jar Parser.g4
$ javac *.java
```

# <u>TO RUN:</u>
```bash
$ java org.antlr.v4.gui.TestRig Parser program -gui #-gui for show the tree of the parser
*expression*
^D
```
