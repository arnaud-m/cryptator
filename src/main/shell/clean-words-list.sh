#!/bin/sh

TMP1=`mktemp`
TMP2=`mktemp`

for FILE in $* ; do
    ## Remove all diacritics
    ## https://stackoverflow.com/questions/10207354/how-to-remove-all-of-the-diacritics-from-a-file
    cat $FILE | iconv -f utf8 -t ascii//TRANSLIT//IGNORE > $TMP1
    ## Convert to lowercase
    cat $TMP1 | tr '[:upper:]' '[:lower:]' > $TMP2
    ## Remove non alphanumerics and blank lines
    sed -e 's/[^a-z0-9]//g' -e '/^[[:space:]]*$/d'  $TMP2 > $TMP1
    ## Remove duplicates
    sort -u $TMP1 > $FILE.clean
done
