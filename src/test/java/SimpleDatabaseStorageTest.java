import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.*;
import util.TestingDatabase;

import javax.sql.DataSource;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SimpleDatabaseStorageTest {

    private static SimpleDatabaseStorage quotes;
    private static Flyway flyway;

    @BeforeAll
    static void init() {
        DataSource ds = TestingDatabase.getPostgresDataSource();
        flyway = Flyway.configure().dataSource(ds).load();
        flyway.clean();
        flyway.migrate();
        quotes = new SimpleDatabaseStorage(ds);
    }

    @BeforeEach
    void setUp() {
        flyway.migrate();
    }

    @AfterAll
    static void release() {
        flyway.clean();
    }

    @AfterEach
    void tearDown() {
        quotes.clean();
    }

    @Test
    void addQuotes() {
        final int count = 100;

        quotes.addQuotes(QuoteSequence.generate(count));

        assertEquals(count, quotes.total(), "Storage contains not all elements from input");
    }

    @Test
    void getQuotes() {
        final int count = 78;
        quotes.addQuotes(QuoteSequence.generate(count));

        Stream<Quote> result = SimpleDatabaseStorageTest.quotes.getQuotes();
        assertNotNull(result, "Result is null");
        assertEquals(count, result.count(), "Was added to empty storage: " + count);
    }

    @Test
    void total() {
        final int count = 1763;
        quotes.addQuotes(QuoteSequence.generate(count));

        assertEquals(count, quotes.total(), "Quotes count not equal to count of the writing data");
    }

    @Test
    void clean() {
        final int count = 50;
        quotes.addQuotes(QuoteSequence.generate(count));

        quotes.clean();

        assertEquals(0, quotes.total(), "Storage must be is empty");
    }

}