package cjlu.cct.mybatis.sqlsession.defaults;

import cjlu.cct.mybatis.cfg.Configuration;
import cjlu.cct.mybatis.sqlsession.SqlSession;
import cjlu.cct.mybatis.sqlsession.proxy.MapperProxy;
import cjlu.cct.mybatis.utils.DatasourceUtil;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 默认的SqlSession类，面向的是工厂类中根据实际需求创建的不同的SqlSession对象
 * 主要就是根据传入的Configuration的配置信息，建立数据库连接，返回数据库连接的Session
 * 并且提供连接的返回，因为实际中包括数据库连接池的使用，实现更为复杂。
 * 这里就简单的创建一个连接来使用，因为getMapper中是创建代理对象，不适合把创建数据库连接也封装进去
 */
public class DefaultSqlSession implements SqlSession {

    private Configuration configuration;
    private Connection connection;

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
        connection = DatasourceUtil.getConnection(configuration);
    }

    /**
     * 创建代理对象
     * @param daoInterfaceClass dao操作类的接口名字节码
     * @param <T>               对应dao操作类的类型泛型
     * @return  代理对象
     */
    public <T> T getMapper(Class<T> daoInterfaceClass) {
        //使用动态代理的方式获取代理对象
        return (T) Proxy.newProxyInstance(daoInterfaceClass.getClassLoader(),
                new Class[]{daoInterfaceClass},
                new MapperProxy(configuration.getMappers(), connection));
    }

    /**
     * 释放资源
     */
    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}
