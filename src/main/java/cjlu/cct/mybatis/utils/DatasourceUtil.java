package cjlu.cct.mybatis.utils;

import cjlu.cct.mybatis.cfg.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 用于创建数据库连接Connection的类
 */
public class DatasourceUtil {
    public static Connection getConnection(Configuration configuration) {
        Connection connection = null;
        try{
            //其实可以省略，版本高的话
            Class.forName(configuration.getDriver());
            //获取连接Connection
            connection = DriverManager.getConnection(configuration.getUrl(),
                    configuration.getUsername(),configuration.getPassword());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return connection;
    }
}
