package cjlu.cct.mybatis.sqlsession;

import cjlu.cct.mybatis.cfg.Configuration;
import cjlu.cct.mybatis.sqlsession.defaults.DefaultSqlSessionFactory;
import cjlu.cct.mybatis.utils.XMLConfigBuilder;

import java.io.InputStream;

/**
 * SqlSession工厂的建造类
 * 主要调用顶层工厂类的对象构造方法，创建需求的某种实际的工厂实现类。
 */
public class SqlSessionFactoryBuilder {

    /**
     * 根据输入流组装合适的工厂并返回
     * @param in    配置文件的输入流
     * @return  SqlSession的工厂对象
     */
    public SqlSessionFactory build(InputStream in){
        Configuration conf = XMLConfigBuilder.localConfiguration(in);
        return new DefaultSqlSessionFactory(conf);
    }

}
