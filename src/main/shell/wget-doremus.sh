#!/bin/sh

## Download files if necessary
FILES="artist.emb.l genre.emb.l mop.emb.l"
for FILE in $FILES ; do
    echo $FILE
    if [ ! -f $FILE ] ; then
        echo "WGET $FILE"
        wget "https://raw.githubusercontent.com/DOREMUS-ANR/music-embeddings/master/$FILE"
    fi
done

## Remove leading URL of artist file
sed -i -e "s/^[^[:space:]]*//" artist.emb.l

## Clean downloaded files
./format-word-list.sh $FILES
