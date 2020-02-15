import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.TestingDatabase;

import javax.sql.DataSource;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class SimpleDatabaseStorageTest {

    private static SimpleDatabaseStorage quotes;
    private static Flyway flyway;

    @BeforeAll
    static void init() {
        DataSource ds = TestingDatabase.getPostgresDataSource();
        String contentPath = "src/main/resources/db/migration";
        flyway = Flyway.configure().dataSource(ds).load();
        Stream.of(flyway.getConfiguration().getLocations())
                .forEach(System.out::println);
        flyway.clean();
        quotes = new SimpleDatabaseStorage(ds);
    }

    @BeforeEach
    void setUp() {
        System.out.println("Before each");
        flyway.migrate();
    }

    private List<Quote> generate(int count) {
        if (count < 1) throw new IllegalArgumentException("count 1 and higher");
        int basic = 10;
        int step = 200;
        long point = System.currentTimeMillis();

        List<Quote> generated = new ArrayList<>(count);
        for (int i = count; i > 0; i--) {
            Instant time = Instant.ofEpochMilli(point - i * step);
            Float value = basic + ThreadLocalRandom.current().nextFloat() * (basic / 2);
            Quote quote = new Quote(time, value);
            generated.add(quote);
        }
        return generated;
    }

    @Test
    void addAndGetQuotes() {
        List<Quote> input = generate(10);
        quotes.addQuotes(input.stream());

        List<Quote> quoteList = quotes.getQuotes().collect(Collectors.toList());

        System.out.println("Input: " + input.toString());
        System.out.println("Output: " + quoteList.toString());

        assertArrayEquals(input.toArray(), quoteList.toArray());
    }

    @AfterEach
    void tearDown() {
        //flyway.clean();
        System.out.println("After each");
    }
}