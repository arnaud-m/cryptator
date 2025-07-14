#!/bin/sh
#
# This file is part of cryptator, https://github.com/arnaud-m/cryptator
#
# Copyright (c) 2021-2025, Université Côte d'Azur. All rights reserved.
#
# Licensed under the BSD 3-clause license.
# See LICENSE file in the project root for full license information.
#

#--------------------------------------------------------------------
# Setup Global Variables
#--------------------------------------------------------------------

PROG=`basename $0`
OVERWRITE=0
#--------------------------------------------------------------------
# Version and help messages
#--------------------------------------------------------------------


version() {
cat <<EOF
$PROG 0.1

This file is part of cryptator, https://github.com/arnaud-m/cryptator

Copyright (c) 2022, Université Côte d'Azur. All rights reserved.

Licensed under the BSD 3-clause license.
See LICENSE file in the project root for full license information.
EOF
}

help() {
cat <<EOF
$PROG formats a word list.

$PROG formats a word list and output its results into a file.
It removes diacritics, non-alphanumeric and whitespace characters, and then sort the word list while eliminating duplicates.
By default, the output file is renamed by adding the suffix '.clean', but an option can force overwrite..

Usage: $PROG [OPTION] FILES...

Options:
 -f       force overwriting the original file instead of adding it a suffix.

 -h        display this help and exit
 -v        output version information and exit

Example:
$PROG planets.txt

Report bugs to <arnaud (dot) malapert (at) univ-cotedazur (dot) fr>."
EOF
}

#--------------------------------------------------------------------
# Test for prerequisites
#--------------------------------------------------------------------

while getopts ":hvf" opt; do
    case $opt in
        f)
            OVERWRITE=1
            ;;
        h)
            help
            exit 0
            ;;
        v)
            version;
            exit 0
            ;;
        \?)
            echo "Invalid option: -$OPTARG" >&2
            exit 1
            ;;
        :)
            echo "Option -$OPTARG requires an argument." >&2
            exit 1
            ;;
    esac
done

shift $((OPTIND-1))
#--------------------------------------------------------------------
# Do something
#--------------------------------------------------------------------

## Create temporary filenames
TMP1=`mktemp`
TMP2=`mktemp`



for FILE in $* ; do
    if [ -f $FILE ] && [ -r $FILE ] ; then
        echo "Process $FILE"
        ## Set output filename
        if [ $OVERWRITE -ne 0 ] ; then
            echo "Overwrite $FILE"
            OUTFILE=$FILE
        else
            OUTFILE=$FILE.clean
        fi
        ## Remove all diacritics
        ## https://stackoverflow.com/questions/10207354/how-to-remove-all-of-the-diacritics-from-a-file
        cat $FILE | iconv -f utf8 -t ascii//TRANSLIT//IGNORE > $TMP1
        ## Convert to lowercase
        cat $TMP1 | tr '[:upper:]' '[:lower:]' > $TMP2
        ## Remove non alphanumerics and blank lines
        sed -e 's/[^a-z0-9]//g' -e '/^[[:space:]]*$/d'  $TMP2 > $TMP1
        ## Remove duplicates
        sort -u $TMP1 > $OUTFILE
    else
        echo "Ignore $FILE"
    fi


done
