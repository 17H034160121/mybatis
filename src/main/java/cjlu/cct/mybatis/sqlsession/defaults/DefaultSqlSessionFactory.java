package cjlu.cct.mybatis.sqlsession.defaults;

import cjlu.cct.mybatis.cfg.Configuration;
import cjlu.cct.mybatis.sqlsession.SqlSession;
import cjlu.cct.mybatis.sqlsession.SqlSessionFactory;

/**
 * SqlSessionFactory的一个实现类
 * 主要是针对建造者模式，builder中需要根据需求返回一个对应的工厂类对象。
 * 下面就是默认的工厂类的实际创建类。
 * 为了创建SqlSession对象而存在的一个工厂，实际根据需求创建不同的SqlSession对象
 */
public class DefaultSqlSessionFactory implements SqlSessionFactory {

    //主环境配置信息对象
    private Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    public SqlSession openSession() {
        return new DefaultSqlSession(configuration);
    }
}
