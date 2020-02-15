import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SimpleDatabaseStorage implements QuoteStorage {
    private final JdbcTemplate jdbcTemplate;

    public SimpleDatabaseStorage(DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
    }

    @Override
    public void addQuotes(Stream<Quote> quotes) {
        List<Quote> quoteList = quotes.collect(Collectors.toList());
        String singleInsert = "INSERT INTO quote (time, value) VALUES (?,?)";
        BatchPreparedStatementSetter bpss = new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Quote quote = quoteList.get(i);
                ps.setTimestamp(1, Timestamp.from(quote.getTime()));
                ps.setFloat(2, quote.getValue());
            }

            @Override
            public int getBatchSize() {
                return quoteList.size();
            }
        };

        this.jdbcTemplate.batchUpdate(singleInsert, bpss);
    }

    @Override
    public Stream<Quote> getQuotes() {
        String selectAll = "SELECT time, value FROM quote";
        RowMapper<Quote> rowMapper = (rs, rwnIgnore) -> new Quote(rs.getTimestamp("time").toInstant(), rs.getFloat("value"));

        return this.jdbcTemplate.query(selectAll, rowMapper).stream();
    }

    @Override
    public long total() {
        Long count = this.jdbcTemplate.queryForObject("SELECT count(*) FROM quote", Long.class);
        return Objects.requireNonNull(count, "count is null");
    }

    @Override
    public void clean() {
        String truncate = "TRUNCATE TABLE quote";
        this.jdbcTemplate.execute(truncate);
    }
}
