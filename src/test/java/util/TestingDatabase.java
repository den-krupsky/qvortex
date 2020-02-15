package util;

import org.h2.jdbcx.JdbcDataSource;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import javax.sql.DataSource;

public final class TestingDatabase {

    private TestingDatabase() {
        //ignore
    }

    public static DataSource getH2DataSource() {
        JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false;");
        ds.setUser("SA");
        ds.setPassword("");
        return ds;
    }

    public static DataSource getPostgresDataSource() {
        return new SingleConnectionDataSource("jdbc:postgresql://localhost:5432/sandbox", "sandbox_test", "12345", true);

    }

}
