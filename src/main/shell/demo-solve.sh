#!/bin/sh
#
# This file is part of cryptator, https://github.com/arnaud-m/cryptator
#
# Copyright (c) 2021-2025, Université Côte d'Azur. All rights reserved.
#
# Licensed under the BSD 3-clause license.
# See LICENSE file in the project root for full license information.
#

JAR="../../../target/cryptator-*-with-dependencies.jar"
CMD="java -cp $JAR cryptator.Cryptator --check"

$CMD "send + more = money"
$CMD -s bignum "aristocratic + prescription + prosopopoeia + protectorate = transoceanic"
$CMD -v verbose --graphviz "venus + mars + saturn + uranus = neptune"
$CMD -v quiet "xiv*xciii=xxxi*xlii"
