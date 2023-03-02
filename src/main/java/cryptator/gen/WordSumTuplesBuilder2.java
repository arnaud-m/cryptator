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

public class WordSumTuplesBuilder2 {

    private static class HalfTuple {

        private final int maxLength;

        private final int wordCount;

        public HalfTuple(int maxLength, int wordCount) {
            super();
            this.maxLength = maxLength;
            this.wordCount = wordCount;
        }

        public final int getMaxLength() {
            return maxLength;
        }

        public final int getWordCount() {
            return wordCount;
        }

        @Override
        public String toString() {
            return maxLength + "-" + wordCount;
        }

    }

    private static class HTEvent {

        private final HalfTuple halfTuple;
        private final int eventType;
        private final BigInteger eventValue;

        public HTEvent(HalfTuple tuple, int eventType, BigInteger eventValue) {
            super();
            this.halfTuple = tuple;
            this.eventType = eventType;
            this.eventValue = eventValue;
        }

        public final HalfTuple getHalfTuple() {
            return halfTuple;
        }

        public final int getEventType() {
            return eventType;
        }

        public final BigInteger getEventValue() {
            return eventValue;
        }

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

    static class EventComparator implements Comparator<HTEvent> {

        @Override
        public int compare(HTEvent evt1, HTEvent evt2) {
            final int cmp = evt1.eventValue.compareTo(evt2.eventValue);
            return cmp == 0 ? Integer.compare(evt1.eventType, evt2.eventType) : cmp;
        }
    }

    public final BigInteger base;
    public final int[] lengths;
    public final int[] indices;

    public WordSumTuplesBuilder2(int base, int[] lengths) {
        super();
        this.base = BigInteger.valueOf(base);
        this.lengths = lengths;
        Arrays.sort(lengths);
        indices = buildIndices(lengths);
    }

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

    private BigInteger getLB(int i) {
        return base.pow(i - 1);
    }

    private BigInteger getUB(int i) {
        return base.pow(i).subtract(BigInteger.ONE);
    }

    private static void addEvents(Collection<HTEvent> events, int maxLength, int wordCount, BigInteger lb,
            BigInteger ub) {
        final HalfTuple t = new HalfTuple(maxLength, wordCount);
        events.add(new HTEvent(t, 0, lb));
        events.add(new HTEvent(t, 1, ub));
    }

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

    private static void addTuple(Tuples tuples, HalfTuple ht1, HalfTuple ht2) {
        tuples.add(ht1.getMaxLength(), ht1.getWordCount(), ht2.getMaxLength(), ht2.getWordCount());
    }

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

    public final Tuples buildTuples() {
        final List<HTEvent> events = buildEvents();
        return processEvents(events);
    }

    @Override
    public String toString() {
        return "TuplesBuilder [\n base=" + base + ",\n lengths=" + Arrays.toString(lengths) + ",\n indices="
                + Arrays.toString(indices) + "\n]";
    }

    public static void main(String[] args) {
        int[] lengths = {1, 2, 2, 2, 3, 3, 4, 5, 4};
        // int[] lengths = { 2, 2, 2, 2, 3, 5, 5};

        // int[] lengths = new int[500];
        // Arrays.fill(lengths, 2);

        WordSumTuplesBuilder2 builder = new WordSumTuplesBuilder2(10, lengths);
        System.out.println(builder);
        // int[] indices = sortAndIndex(lengths);
        // final List<HTEvent> events = buildEvents(lengths, indices);
        final Tuples tuples = builder.buildTuples();
        System.out.println(tuples);
        System.out.println(tuples.nbTuples());
    }

}
