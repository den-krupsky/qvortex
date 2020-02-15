import java.util.stream.Stream;

public interface QuoteReader {
    Stream<Quote> getQuotes();
}
