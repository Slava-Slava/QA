package ru.netology.data;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLHelper {
    private SQLHelper() {
    }

    private static QueryRunner runner = new QueryRunner();

    @SneakyThrows
    public static Connection getConn() {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/app", "app", "pass");
    }

    @SneakyThrows
    public static String getBuyPaymentStatus() {
        var runner = new QueryRunner();
        var SqlStatus = "SELECT status FROM payment_entity";
        return runner.query(getConn(), SqlStatus, new ScalarHandler<>());
    }

    @SneakyThrows
    public static String getBuyCreditPaymentStatus() {
        var runner = new QueryRunner();
        var SqlStatus = "SELECT status FROM credit_request_entity";
        return runner.query(getConn(), SqlStatus, new ScalarHandler<>());
    }

    @SneakyThrows
    public static void cleanDatabase() {
        var connection = getConn();
        runner.execute(connection, "DELETE FROM credit_request_entity");
        runner.execute(connection, "DELETE FROM order_entity");
        runner.execute(connection, "DELETE FROM payment_entity");
    }
}
