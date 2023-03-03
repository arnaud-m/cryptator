/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2023, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.gen;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import org.chocosolver.solver.constraints.extension.Tuples;

/**
 * This class is a factory that builds tuples.
 *
 * Let S be a sum of numbers written in base b.
 *
 * A tuple has the following semantics :
 * <ul>
 * <li>l1: the maximum word length of the left member</li>
 * <li>n1: the number of words of the left member</li>
 * <li>l2: the maximum word length of the right member</li>
 * <li>n2: the number of words of the right member</li>
 * </ul>
 * 
 * A tuple represents the sum of words of the left member that must be equal to
 * the sum of words of the right member.
 * 
 * The tuples represents the sum of n1 words
 */
public class WordSumTuplesBuilder {

    /**
     * The Class HalfTuple maximum word length and the number of word of a single
     * member.
     */
    private static class HalfTuple {

        /** The maximum word length. */
        private final int maxLength;

        /** The word count. */
        private final int wordCount;

        /**
         * Instantiates a new half tuple.
         *
         * @param maxLength the maximum length
         * @param wordCount the word count
         */
        public HalfTuple(int maxLength, int wordCount) {
            super();
            this.maxLength = maxLength;
            this.wordCount = wordCount;
        }

        /**
         * Gets the maximum word length of the member.
         *
         * @return the maximum word length
         */
        public final int getMaxLength() {
            return maxLength;
        }

        /**
         * Gets the word count of the member.
         *
         * @return the word count
         */
        public final int getWordCount() {
            return wordCount;
        }

        /**
         * To string.
         *
         * @return the string
         */
        @Override
        public String toString() {
            return maxLength + "-" + wordCount;
        }

    }

    /**
     * The Class HTEvent is a sweep line event for a half tuple.
     */
    private static class HTEvent {

        /** The half tuple. */
        private final HalfTuple halfTuple;

        /** The event type. */
        private final int eventType;

        /** The event value. */
        private final BigInteger eventValue;

        /**
         * Instantiates a new Half Tuple event.
         *
         * @param tuple      the half tuple
         * @param eventType  the event type
         * @param eventValue the event value
         */
        public HTEvent(HalfTuple tuple, int eventType, BigInteger eventValue) {
            super();
            this.halfTuple = tuple;
            this.eventType = eventType;
            this.eventValue = eventValue;
        }

        /**
         * Gets the half tuple.
         *
         * @return the half tuple
         */
        public final HalfTuple getHalfTuple() {
            return halfTuple;
        }

        /**
         * Gets the event type.
         *
         * @return the event type
         */
        public final int getEventType() {
            return eventType;
        }

        /**
         * Gets the event value, its coordinate on the sweep line.
         *
         * @return the event value
         */
        public final BigInteger getEventValue() {
            return eventValue;
        }

        /**
         * To string.
         *
         * @return the string
         */
        @Override
        public String toString() {
            StringBuilder b = new StringBuilder();
            b.append(eventType == 0 ? "S" : "E");
            b.append(halfTuple.getMaxLength());
            b.append('[').append(halfTuple.getWordCount()).append(']');
            b.append(':').append(eventValue);
            return b.toString();
        }

    }

    /**
     * The Class EventComparator for the sweep line.
     */
    static class EventComparator implements Comparator<HTEvent> {

        /**
         * Compare.
         *
         * @param evt1 the left member event
         * @param evt2 the right member event
         * @return a negative integer, zero, or a positive integer as the first argument
         *         is less than, equal to, or greater than the second.
         */
        @Override
        public int compare(HTEvent evt1, HTEvent evt2) {
            final int cmp = evt1.eventValue.compareTo(evt2.eventValue);
            return cmp == 0 ? Integer.compare(evt1.eventType, evt2.eventType) : cmp;
        }
    }

    /** The base of the word sum. */
    public final BigInteger base;

    /** The sorted word lengths. */
    public final int[] lengths;

    /** The indices of the last word of each length. */
    public final int[] indices;

    /**
     * Instantiates a new builder of Word Sum Tuples.
     * 
     * The lengths are those of the words that may appear without repetition in the
     * sum.
     * 
     * Beware that the lengths given as parameters are sorted (without copy).
     *
     * @param base    the base of the word sum
     * @param lengths the lengths of the words.
     */
    public WordSumTuplesBuilder(int base, int[] lengths) {
        super();
        this.base = BigInteger.valueOf(base);
        this.lengths = lengths;
        Arrays.sort(lengths);
        indices = buildIndices(lengths);
    }

    /**
     * Builds the indices from sorted lengths.
     *
     * @param lengths the sorted lengths
     * @return the indices
     */
    private static int[] buildIndices(int[] lengths) {
        final int maxLen = IntStream.of(lengths).max().orElse(0);
        final int[] indices = new int[maxLen + 1];
        Arrays.fill(indices, -1);
        // Get the largest index of an element for each length
        for (int i = 0; i < lengths.length; i++) {
            indices[lengths[i]] = i;
        }
        // Transform into non-decreasing
        for (int i = 1; i < indices.length; i++) {
            indices[i] = Math.max(indices[i], indices[i - 1]);
        }
        return indices;
    }

    /**
     * Gets the lower bound for a word of length i.
     *
     * @param i the word length
     * @return the lower bound on its value
     */
    private BigInteger getLB(int i) {
        return base.pow(i - 1);
    }

    /**
     * Gets the upper bound for a word of length i.
     *
     * @param i the word length
     * @return the upper bound on its value
     */
    private BigInteger getUB(int i) {
        return base.pow(i).subtract(BigInteger.ONE);
    }

    /**
     * Adds the events of a half tuple to the sweep line.
     *
     * @param events    the events of the sweep line
     * @param maxLength the maximum word length of the half tuple
     * @param wordCount the word count of the half tuple
     * @param lb        the lower bound for a word sum (value of the start event)
     * @param ub        the upper bound for a word sum (value of the end event)
     */
    private static void addEvents(Collection<HTEvent> events, int maxLength, int wordCount, BigInteger lb,
            BigInteger ub) {
        final HalfTuple t = new HalfTuple(maxLength, wordCount);
        events.add(new HTEvent(t, 0, lb));
        events.add(new HTEvent(t, 1, ub));
    }

    /**
     * Builds the events from the indices.
     *
     * @return the events of the sweep line
     */
    private List<HTEvent> buildEvents() {
        List<HTEvent> events = new ArrayList<>(2 * indices.length);
        for (int i = 1; i < indices.length; i++) {
            if (indices[i] > indices[i - 1]) {
                BigInteger lb = getLB(i);
                BigInteger ub = getUB(i);
                addEvents(events, i, 1, lb, ub);
                for (int j = 2; j <= indices[i] + 1; j++) {
                    lb = lb.add(getLB(lengths[j - 2]));
                    ub = ub.add(getUB(lengths[indices[i] + 1 - j]));
                    addEvents(events, i, j, lb, ub);
                }
            }
        }
        return events;
    }

    /**
     * Checks if a tuple is valid.
     *
     * It is valid if there are enough words to build the left and right members.
     * 
     * @param t1 the left half tuple
     * @param t2 the the right half tuple
     * @return true, if the tuple is valid
     */
    private boolean isValid(HalfTuple t1, HalfTuple t2) {
        // Total number of words
        final int n = t1.getWordCount() + t2.getWordCount();
        // Maximum length of a word
        final int m = Math.max(t1.getMaxLength(), t2.getMaxLength());
        // Is there enough words with length <=m ?
        if (n > indices[m] + 1) {
            return false;
        }
        // Is there enough words with length m ?
        if (t1.getMaxLength() == t2.getMaxLength() && indices[m] - indices[m - 1] < 2) {
            return false;
        }
        return true;
    }

    /**
     * Add the tuple built from the half tuples.
     *
     * @param tuples the tuples
     * @param ht1    the left half tuple
     * @param ht2    the right half tuple
     */
    private static void addTuple(Tuples tuples, HalfTuple ht1, HalfTuple ht2) {
        tuples.add(ht1.getMaxLength(), ht1.getWordCount(), ht2.getMaxLength(), ht2.getWordCount());
    }

    /**
     * Adds the valid tuples when handling an end event.
     *
     * @param tuples the tuples
     * @param t      the ending tuple
     * @param ts     the active tuples
     */
    private void addTuples(Tuples tuples, HalfTuple t, Set<HalfTuple> ts) {
        System.out.println(t + " X " + ts);
        if (isValid(t, t)) {
            addTuple(tuples, t, t);
        }
        for (HalfTuple t1 : ts) {
            if (isValid(t, t1)) {
                addTuple(tuples, t, t1);
                addTuple(tuples, t1, t);
            }
        }
    }

    /**
     * Sort and process events for building tuples.
     *
     * @param events the events of the sweep line
     * @return the valid tuples
     */
    private Tuples processEvents(List<HTEvent> events) {
        events.sort(new EventComparator());
        System.out.println(events);
        final Set<HalfTuple> actives = new HashSet<>();
        final Tuples tuples = new Tuples();
        for (HTEvent evt : events) {
            final HalfTuple t = evt.getHalfTuple();
            if (evt.getEventType() == 0) {
                actives.add(t);
            } else {
                actives.remove(t);
                addTuples(tuples, t, actives);
            }
        }
        System.out.println(tuples);
        return tuples;
    }

    /**
     * Builds the tuples.
     *
     * @return the valid tuples
     */
    public final Tuples buildTuples() {
        final List<HTEvent> events = buildEvents();
        return processEvents(events);
    }

    /**
     * To string.
     *
     * @return the string
     */
    @Override
    public String toString() {
        return "TuplesBuilder [\n base=" + base + ",\n lengths=" + Arrays.toString(lengths) + ",\n indices="
                + Arrays.toString(indices) + "\n]";
    }

}
