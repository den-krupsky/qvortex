import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.IntFunction;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class QuoteSequence {

    public QuoteSequence() {
        //ignore
    }

    public static Stream<Quote> generate(int count) {
        return generate(count, 10, 200, System.currentTimeMillis());
    }

    public static Stream<Quote> generate(final int count,
                                         final int basic,
                                         final int step,
                                         final long msPoint) {
        if (count < 1 || basic < 1 || step < 1 || msPoint < 1) throw new IllegalArgumentException("negative argument");

        IntFunction<Quote> random = i -> {
            Instant time = Instant.ofEpochMilli(msPoint - (count - i) * step);
            Float value = basic + ThreadLocalRandom.current().nextFloat() * (basic / 2);
            return new Quote(time, value);
        };

        return IntStream.range(0, count)
                .mapToObj(random);
    }
}
