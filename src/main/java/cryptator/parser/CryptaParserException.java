/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021-2025, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.parser;

import java.util.concurrent.CancellationException;

public class CryptaParserException extends CancellationException {

    private static final long serialVersionUID = 6706871076287552877L;

    private String cryptarithm;
    private final transient Object offendingSymbol;
    private final int line;
    private final int charPositionInLine;
    private final String msg;

    public CryptaParserException(final Object offendingSymbol, final int line, final int charPositionInLine,
            final String msg) {
        super(String.format("Line %d:%d %s", line, charPositionInLine, msg));
        this.offendingSymbol = offendingSymbol;
        this.line = line;
        this.charPositionInLine = charPositionInLine;
        this.msg = msg;
    }

    public final String getCryptarithm() {
        return cryptarithm;
    }

    public final void setCryptarithm(final String cryptarithm) {
        this.cryptarithm = cryptarithm;
    }

    public final Object getOffendingSymbol() {
        return offendingSymbol;
    }

    public final int getLine() {
        return line;
    }

    public final int getCharPositionInLine() {
        return charPositionInLine;
    }

    public final String getMsg() {
        return msg;
    }

}
