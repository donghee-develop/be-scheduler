package test.schedule.repository.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import test.schedule.dto.schedule.*;
import test.schedule.dto.util.PagingDTO;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ScheduleRepository {
    private final JdbcTemplate jdbcTemplate;
    String sql ="";
    public List<GetScheduleDTO> getSchedules(String keyword, LocalDate startDate, LocalDate endDate, PagingDTO pagingDTO) {
        sql = """
                SELECT
                    s.id,
                    s.content,
                    s.user_id,
                    u.name,
                    s.password,
                    s.created_at,
                    s.updated_at
                FROM schedule s
                JOIN user u ON s.user_id = u.user_id
                WHERE 1=1
                  AND (s.content LIKE ? OR u.name LIKE ?) 
                  AND s.updated_at BETWEEN ? AND ? 
                ORDER BY s.updated_at DESC 
                LIMIT ?, ?
""";

        List<Object> params = new ArrayList<>();
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59, 999999999);

        params.add("%" + keyword + "%");
        params.add("%" + keyword + "%");
        params.add(Timestamp.valueOf(startDateTime));
        params.add(Timestamp.valueOf(endDateTime));

        params.add((pagingDTO.getNowPage() - 1) * pagingDTO.getCntPage());
        params.add(pagingDTO.getCntPage());
        return jdbcTemplate.query(sql, params.toArray(), (rs, rowNum) -> new GetScheduleDTO(
                rs.getString("name"),
                rs.getLong("id"),
                rs.getString("content"),
                rs.getLong("user_id"),
                rs.getTimestamp("updated_at").toLocalDateTime(),
                Optional.ofNullable(rs.getTimestamp("updated_at"))
                        .map(Timestamp::toLocalDateTime)
                        .orElse(null)
        ));
    }


    public int getTotalSchedules(String keyword, LocalDate startDate, LocalDate endDate) {
        sql = """
        SELECT COUNT(*)
        FROM schedule s
        JOIN user u ON s.user_id = u.user_id
        WHERE 1=1
    """;

        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.isEmpty()) {
            sql += " AND (s.content LIKE ? OR u.name LIKE ?)";
            params.add("%" + keyword + "%");
            params.add("%" + keyword + "%");
        }
        if (startDate != null && endDate != null) {
            LocalDateTime startDateTime = startDate.atStartOfDay();
            LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay();
            sql += " AND s.updated_at >= ? AND s.updated_at < ? ";
            params.add(startDateTime);
            params.add(endDateTime);
        }
        return jdbcTemplate.queryForObject(sql, params.toArray(), Integer.class);
    }

    public Long findUserByEmail(String email) {
        String sql = "SELECT user_id FROM user WHERE email = ?";
        List<Long> result = jdbcTemplate.query(sql,
                (rs, rowNum) -> rs.getLong("user_id"), email.trim());

        return result.isEmpty() ? null : result.get(0);
    }

    public Long insertUser(String name,String email) {
        String sql = "INSERT INTO user(name, email) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, name);
            ps.setString(2, email);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public void insertSchedule(PostScheduleDTO postScheduleDTO, Long userId) {
        sql = "insert into `schedule` (content, user_id, password) values (?, ?, ?)";
        jdbcTemplate.update(sql,postScheduleDTO.getContent(),userId,postScheduleDTO.getPassword());
    }

    public void updateSchedule(UpdateScheduleDTO updateScheduleDTO) {
        String sql = "update `schedule` set content = ?, updated_at = CURRENT_TIMESTAMP where id = ?";
        jdbcTemplate.update(sql,updateScheduleDTO.getContent(),updateScheduleDTO.getId());
    }

    public Optional<GetScheduleDTO> getScheduleById(Long id) {
        String sql = "SELECT s.id, s.content, s.user_id, u.name as name, s.created_at, s.updated_at " +
                "FROM schedule s JOIN user u ON s.user_id = u.user_id " +
                "WHERE s.id = ?";
        // EmptyResult 방지용 리스트 그리고 첫번째 리스트 반환
        List<GetScheduleDTO> result = jdbcTemplate.query(sql, new Object[]{id}, (rs, rowNum) -> new GetScheduleDTO(
                rs.getString("name"),
                rs.getLong("id"),
                rs.getString("content"),
                rs.getLong("user_id"),
                rs.getTimestamp("created_at").toLocalDateTime(),
                rs.getTimestamp("updated_at").toLocalDateTime()
        ));
        return result.stream().findFirst();
    }

    public void deleteSchedule(DeleteScheduleDTO deleteScheduleDTO) {
        String sql = "delete from schedule where id = ?";
        jdbcTemplate.update(sql,deleteScheduleDTO.getId());
    }

    public String getPasswordById(Long id) {
        String sql = "select password from schedule where id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, String.class);

    }

}
