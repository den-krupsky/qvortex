import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Quotes {
    private final JdbcTemplate jdbcTemplate;

    public Quotes(DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
    }


    public void addQuotes(Stream<Quote> input) {
        List<Quote> quotes = input.collect(Collectors.toList());
        String singleInsert = "INSERT INTO quote (time, value) VALUES (?,?)";
        BatchPreparedStatementSetter bpss = new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Quote quote = quotes.get(i);
                ps.setTimestamp(1, Timestamp.from(quote.getTime()));
                ps.setFloat(2, quote.getValue());
            }

            @Override
            public int getBatchSize() {
                return quotes.size();
            }
        };

        this.jdbcTemplate.batchUpdate(singleInsert, bpss);
    }


    public Stream<Quote> getQuotes() {
        String selectAll = "SELECT time, value FROM quote";
        RowMapper<Quote> rowMapper = (rs, rwnIgnore) -> new Quote(rs.getTimestamp("time").toInstant(), rs.getFloat("value"));

        return this.jdbcTemplate.query(selectAll, rowMapper).stream();
    }
}
