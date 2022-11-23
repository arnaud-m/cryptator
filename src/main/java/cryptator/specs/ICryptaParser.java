/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2022, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.specs;

import cryptator.parser.CryptaParserException;

public interface ICryptaParser {

    /**
     * Parse a cryptarithm
     *
     * @param cryptarithm encoded using the antlr4 grammar
     * @return the root of a complete binary tree (AST) that represent the
     *         cryptarithm.
     * @throws CryptaParserException on failure.
     */
    ICryptaNode parse(String cryptarithm) throws CryptaParserException;

}
