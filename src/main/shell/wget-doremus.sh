#!/bin/sh
#
# This file is part of cryptator, https://github.com/arnaud-m/cryptator
#
# Copyright (c) 2021-2025, Université Côte d'Azur. All rights reserved.
#
# Licensed under the BSD 3-clause license.
# See LICENSE file in the project root for full license information.
#


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
