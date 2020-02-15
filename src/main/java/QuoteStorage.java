public interface QuoteStorage extends QuoteWriter, QuoteReader {
    long total();
    void clean();
}
