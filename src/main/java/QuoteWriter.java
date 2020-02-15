import java.util.stream.Stream;

public interface QuoteWriter {
    void addQuotes(Stream<Quote> quotes);
}
