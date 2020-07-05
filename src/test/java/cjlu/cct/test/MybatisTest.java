package cjlu.cct.test;

import cjlu.cct.dao.PersonDao;
import cjlu.cct.domain.Person;
import cjlu.cct.mybatis.io.Resources;
import cjlu.cct.mybatis.sqlsession.SqlSession;
import cjlu.cct.mybatis.sqlsession.SqlSessionFactory;
import cjlu.cct.mybatis.sqlsession.SqlSessionFactoryBuilder;


import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Mybatis测试类
 */
public class MybatisTest {
    public static void main(String[] args) throws IOException {
        //读取配置文件
        InputStream in = Resources.getResourceAsStream("SqlMapConfig.xml");

        //创建SqlSessionFactory工厂
        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
        SqlSessionFactory sessionFactory = builder.build(in);

        //通过工厂获取数据库连接中的session对象
        SqlSession session = sessionFactory.openSession();

        //使用session对象获取dao的代理对象
        PersonDao personDao = session.getMapper(PersonDao.class);

        //使用代理对象执行具体方法
        List<Person> all = personDao.findAll();

        for (Person p : all) {
            System.out.println(p);
        }

        //返还session给数据库连接池
        session.close();
        //关闭输入流
        in.close();

    }
}
