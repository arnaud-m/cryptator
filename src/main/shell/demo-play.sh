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

java -cp $JAR cryptator.Cryptamancer 'send+more=money'
