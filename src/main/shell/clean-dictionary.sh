#!/bin/sh

TMP1=`mktemp`
TMP2=`mktemp`

for DICT in $* ; do
    ## Remove all diacritics
    ## https://stackoverflow.com/questions/10207354/how-to-remove-all-of-the-diacritics-from-a-file
    cat $DICT | iconv -f utf8 -t ascii//TRANSLIT//IGNORE > $TMP1
    ## Convert to lowercase
    cat $TMP1 | tr '[:upper:]' '[:lower:]' > $TMP2
    ## Remove non alphanumerics
    sed 's/[^a-z0-9]//g' $TMP2 > $TMP1
    ## Remove duplicates
    uniq $TMP1 > $DICT.clean
done
