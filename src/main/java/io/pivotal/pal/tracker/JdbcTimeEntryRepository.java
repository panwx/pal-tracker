package io.pivotal.pal.tracker;

import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class JdbcTimeEntryRepository implements TimeEntryRepository {
    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate template;

    public JdbcTimeEntryRepository(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.template = new NamedParameterJdbcTemplate(dataSource);
    }



    private final RowMapper<TimeEntry> mapper = (rs, rowNum) -> new TimeEntry(
            rs.getLong("id"),
            rs.getLong("project_id"),
            rs.getLong("user_id"),
            rs.getDate("date").toLocalDate(),
            rs.getInt("hours")
    );

    private final ResultSetExtractor<TimeEntry> extractor =
            (rs) -> rs.next() ? mapper.mapRow(rs, 1) : null;

    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        KeyHolder key = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO time_entries (project_id, user_id, date, hours) " +
                            "VALUES (?, ?, ?, ?)",
                    RETURN_GENERATED_KEYS
            );

            statement.setLong(1, timeEntry.getProjectId());
            statement.setLong(2, timeEntry.getUserId());
            statement.setDate(3, Date.valueOf(timeEntry.getDate()));
            statement.setInt(4, timeEntry.getHours());

            return statement;
        }, key);

        /*SqlParameterSource fileParameters = new BeanPropertySqlParameterSource(timeEntry);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update("INSERT INTO time_entries (project_id, user_id, date, hours) VALUES (:projectId, :userId, :date, :hours)", fileParameters, keyHolder);

        jdbcTemplate.update("INSERT INTO time_entries (project_id, user_id, date, hours) VALUES (?, ?, ?, ?)", new Object[] {timeEntry.getProjectId(), timeEntry.getUserId(), Date.valueOf(timeEntry.getDate()), timeEntry.getHours()});*/



        String sql = "SELECT * FROM time_entries where ID = ?";

        return (TimeEntry) jdbcTemplate.queryForObject(
                sql,
                new Object[]{key.getKey()},
                new BeanPropertyRowMapper(TimeEntry.class));

    }

    @Override
    public TimeEntry find(long timeEntryId) {
        String sql = "SELECT * FROM time_entries where ID = ?";

        TimeEntry timeEntry;

        try{
            timeEntry = (TimeEntry) jdbcTemplate.queryForObject(
                    sql,
                    new Object[]{timeEntryId},
                    new BeanPropertyRowMapper(TimeEntry.class));
        }catch (Exception e){
            timeEntry = null;
        }


        return timeEntry;
    }

    @Override
    public List<TimeEntry> list() {
        String sql = "SELECT * FROM time_entries";

        List<TimeEntry> result = jdbcTemplate.query(sql, new BeanPropertyRowMapper(TimeEntry.class));

        return result;
    }

    @Override
    public TimeEntry update(long id, TimeEntry timeEntry) {
        String sql = "update time_entries set project_id = ?, " +
                "user_id = ?," +
                "date = ?," +
                "hours = ? " +
                "where id = ?";
        this.jdbcTemplate.update(sql, new Object[]{
                timeEntry.getProjectId(),
                timeEntry.getUserId(),
                timeEntry.getDate(),
                timeEntry.getHours(),
                id
        });

        return this.find(id);
    }

    @Override
    public boolean delete(long timeEntryId) {
        String sql = "delete from time_entries where id = ?";

        int result = this.jdbcTemplate.update(sql, new Object[]{timeEntryId});
        return (result > 0) ? true : false;
    }
}
