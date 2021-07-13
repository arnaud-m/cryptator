/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.structure;

public interface NodeTree {
    public void calcul();
    public void visualise();
    public String getName();
    public NodeTree getLeftChildren();
    public NodeTree getRightChildren();
    public String getNodeName();
    public void setNodeName(String nodeName);
}
