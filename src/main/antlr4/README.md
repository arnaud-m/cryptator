# <u>TO COMPILE THE PARSER:</u>
```bash
$ export CLASSPATH=".:/usr/local/lib/antlr-4.9.2-complete.jar:$CLASSPATH"
$ java -jar /usr/local/lib/antlr-4.9.2-complete.jar parser/Parser.g4
```

# <u>TO SEE THE GRAPH OF THE PARSER:</u>
```bash
$ java org.antlr.v4.gui.TestRig parser/Parser program -gui #-gui for show the tree of the parser
*expression*
^D
```

# <u>TO SEE THE GRAPH OF THE EXPRESSION:</u>
```bash
$ dot -Tpdf dot/tree.dot -o graph.pdf
```
