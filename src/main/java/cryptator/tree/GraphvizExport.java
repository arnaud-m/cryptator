/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021-2025, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.tree;

import static guru.nidi.graphviz.attribute.Records.rec;
import static guru.nidi.graphviz.attribute.Records.turn;
import static guru.nidi.graphviz.model.Factory.graph;
import static guru.nidi.graphviz.model.Factory.node;

import java.util.ArrayDeque;
import java.util.Deque;

import cryptator.solver.CryptaSolutionException;
import cryptator.specs.ICryptaNode;
import cryptator.specs.ICryptaSolution;
import cryptator.specs.ITraversalNodeConsumer;
import guru.nidi.graphviz.attribute.Attributes;
import guru.nidi.graphviz.attribute.Font;
import guru.nidi.graphviz.attribute.ForNode;
import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.attribute.Records;
import guru.nidi.graphviz.attribute.Shape;
import guru.nidi.graphviz.attribute.Style;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.Node;

public final class GraphvizExport {

    private GraphvizExport() {
    }

    private static Graph exportToGraphviz(final ICryptaNode node, final GraphvizNodeConsumer consumer) {
        TreeTraversals.postorderTraversal(node, consumer);
        return consumer.getGraph();
    }

    public static Graph exportToGraphviz(final ICryptaNode node) {
        return exportToGraphviz(node, new GraphvizNodeConsumer());
    }

    public static Graph exportToGraphviz(final ICryptaNode node, final ICryptaSolution solution) {
        return exportToGraphviz(node, new GraphvizSolutionNodeConsumer(solution));
    }

    private static class GraphvizNodeConsumer implements ITraversalNodeConsumer {

        private Graph graph = graph("G").nodeAttr().with(Shape.PLAIN_TEXT, Font.name("arial"));

        private final Deque<Node> stack = new ArrayDeque<>();

        public final Graph getGraph() {
            return graph;
        }

        protected final Node makeNode(final int numNode) {
            return node(String.valueOf(numNode));
        }

        protected final Node withPlainWord(final Node n, final ICryptaNode node) {
            return n.with(Label.of(new String(node.getWord())));
        }

        protected final Node withBoxedWord(final Node n, final ICryptaNode node) {
            return withPlainWord(n, node).with(Shape.BOX, Style.DASHED);
        }

        protected Node makeWordNode(final ICryptaNode node, final int numNode) {
            final Node n = makeNode(numNode);
            if (node.isInternalNode()) {
                return withPlainWord(n, node);
            } else {
                if (node.isConstant()) {
                    return withBoxedWord(n, node);
                } else {
                    return withPlainWord(n, node);
                }
            }
        }

        @Override
        public final void accept(final ICryptaNode node, final int numNode) {
            Node n = makeWordNode(node, numNode);
            if (node.isInternalNode()) {
                final Node b = stack.pop();
                final Node a = stack.pop();
                graph = graph.with(n.link(a), n.link(b));
            }
            stack.push(n);
        }
    }

    private static class GraphvizSolutionNodeConsumer extends GraphvizNodeConsumer {

        private final ICryptaSolution solution;

        GraphvizSolutionNodeConsumer(final ICryptaSolution solution) {
            this.solution = solution;
        }

        private Attributes<ForNode> makeRecords(final char[] word) {
            final String[] records = new String[word.length];
            for (int i = 0; i < word.length; i++) {
                String digit = "?";
                try {
                    digit = String.valueOf(solution.getDigit(word[i]));
                } catch (CryptaSolutionException e) {
                    // default digit already set
                }
                records[i] = turn(rec(String.valueOf(word[i])), rec(String.valueOf(digit)));

            }
            return Records.of(records);
        }

        protected final Node withRecord(final Node n, final ICryptaNode node) {
            return n.with(makeRecords(node.getWord()));
        }

        @Override
        protected Node makeWordNode(final ICryptaNode node, final int numNode) {
            final Node n = makeNode(numNode);
            if (node.isInternalNode()) {
                return withPlainWord(n, node);
            } else {
                if (node.isConstant()) {
                    return withBoxedWord(n, node);
                } else {
                    return withRecord(n, node);
                }
            }
        }

    }

}
