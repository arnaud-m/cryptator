#!/bin/sh
#
# This file is part of cryptator, https://github.com/arnaud-m/cryptator
#
# Copyright (c) 2023, Université Côte d'Azur. All rights reserved.
#
# Licensed under the BSD 3-clause license.
# See LICENSE file in the project root for full license information.
#

JAR="../../../target/cryptator-*-with-dependencies.jar"
CMD="java -cp $JAR cryptator.Cryptator -c TRUE -g TRUE"

$CMD "send + more = money"
$CMD -l TRUE "aristocratic + prescription + prosopopoeia + protectorate = transoceanic"
$CMD "venus + mars + saturn + uranus = neptune"
$CMD "xiv*xciii=xxxi*xlii"
