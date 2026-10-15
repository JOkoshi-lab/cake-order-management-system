package com.example.demo;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CakeDao {
    private final JdbcTemplate jdbcTemplate;

    CakeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    String select1() {
        String sql = "SELECT name FROM cake WHERE id=1";
        String name = jdbcTemplate.queryForObject(sql, String.class);
        return name;
    }

    String select2(int id) {
        String sql = "SELECT price FROM cake WHERE id=?";
        String price = jdbcTemplate.queryForObject(sql, String.class, id);
        return price;
    }

    int select3() {
        String sql = "SELECT MAX(price) FROM cake";
        Integer max = jdbcTemplate.queryForObject(sql, Integer.class);
        if (max == null) {
            return 0;
        } else {
            return max.intValue();
        }
    }
    LocalTime select4() {
        String sql = "SELECT MIN(time) FROM o_time";
        LocalTime time = jdbcTemplate.queryForObject(sql, LocalTime.class);
        return time;
    }

    public Map<String, Object> select5(int id) {
        String sql = "SELECT * FROM cake WHERE id=?";
        Map<String, Object> map = jdbcTemplate.queryForMap(sql, id);
        return map;
    }

    List<String> select6() {
        String sql = "SELECT name FROM cake";
        List<String> names = jdbcTemplate.queryForList(sql, String.class);
        return names;
    }

    public List<Map<String, Object>> select7() {
        String sql = "SELECT * FROM cake";
        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);
        return result;
    }

    public List<Map<String, Object>> select8() {
        String sql = "SELECT * FROM o_time";
        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);
        return result;
    }

    public List<Map<String, Object>> select9(int low, int high) {
        String sql = "SELECT * FROM cake WHERE price>? AND price<?";
        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, low, high);
        return result;
    }

    public List<Map<String, Object>> select10a() {
        String sql = """
                SELECT
                o_qtt.time_id,
                o_time.time,
                o_qtt.cake_id,
                o_qtt.quantity
                FROM o_time -- テーブル1
                INNER JOIN o_qtt -- テーブル2
                ON o_time.id = o_qtt.time_id; -- このカラムで1と2を内部結合""";
        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);
        return result;
    }

    public List<Map<String, Object>> select10b() {
        String sql = """
                SELECT
                o_time.id,
                o_time.time,
                cake.name,
                cake.price,
                o_qtt.quantity
                FROM o_time -- テーブル1
                INNER JOIN o_qtt -- テーブル2
                ON o_time.id = o_qtt.time_id -- このカラムで1と2を内部結合
                INNER JOIN cake -- テーブル3
                ON o_qtt.cake_id = cake.id; -- このカラムで2と3を内部結合""";
        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);
        return result;
    }

    int newcake(String name, int price) {
        String sql = "INSERT INTO cake(name, price) VALUES(?, ?)";
        int count = jdbcTemplate.update(sql, name, price);
        return count;
    }

    int priceIncrease(String name, int amount) {
        String sql = "UPDATE cake SET price=price+? WHERE name=?";
        int count = jdbcTemplate.update(sql, amount, name);
        return count;
    }

    int soldout(String name) {
        String sql = "DELETE FROM cake WHERE name=?";
        int count = jdbcTemplate.update(sql, name);
        return count;
    }

    public int insertOrderTime(String id) {
        String sql = "INSERT INTO o_time(id) VALUES(?)";
        return jdbcTemplate.update(sql, id);
    }

    public int insertOrder(
            String timeId,
            int cakeId,
            int quantity) {

        String sql = """
                INSERT INTO o_qtt
                (time_id, cake_id, quantity)
                VALUES(?, ?, ?)
                """;

        return jdbcTemplate.update(
                sql,
                timeId,
                cakeId,
                quantity);
    }

    public int totalSales() {
        String sql = """
                SELECT SUM(price * quantity)
                FROM o_time
                INNER JOIN o_qtt
                ON o_time.id = o_qtt.time_id
                INNER JOIN cake
                ON o_qtt.cake_id = cake.id
                """;

        Integer result = jdbcTemplate.queryForObject(sql, Integer.class);

        return result == null ? 0 : result;
    }

    public int orderCount() {
        String sql = "SELECT COUNT(*) FROM o_time";

        Integer result = jdbcTemplate.queryForObject(sql, Integer.class);

        return result == null ? 0 : result;
    }

    public List<Map<String, Object>> ranking() {

        String sql = """
                SELECT
                    cake.name,
                    COALESCE(SUM(o_qtt.quantity), 0) AS total
                FROM cake
                LEFT JOIN o_qtt
                ON cake.id = o_qtt.cake_id
                GROUP BY cake.id, cake.name
                ORDER BY total DESC
                """;

        return jdbcTemplate.queryForList(sql);
    }

    public List<Map<String, Object>> salesByCake() {

        String sql = """
                SELECT
                    cake.name,
                    SUM(cake.price * o_qtt.quantity)
                    AS sales
                FROM cake
                INNER JOIN o_qtt
                ON cake.id = o_qtt.cake_id
                GROUP BY cake.name
                ORDER BY sales DESC
                """;

        return jdbcTemplate.queryForList(sql);
    }
}