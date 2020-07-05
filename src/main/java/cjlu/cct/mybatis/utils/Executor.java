package cjlu.cct.mybatis.utils;

import cjlu.cct.mybatis.cfg.Mapper;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 查询所有方法的方法增强的类
 * 主要就是通过数据库的Connection和传入的配置信息，配置sql语句，然后执行，将执行的结果封装成指定的类型
 */
public class Executor {
    public <E>  List<E> selectList(Mapper mapper, Connection connection){
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        //从mapper中读取基本的额配置信息
        String queryString = mapper.getQueryString();
        String resultType = mapper.getResultType();

        List<E> list = new ArrayList<E>();
        try{
            //通过配置信息获取结果的返回类型
            Class domainClass = Class.forName(resultType);
            //通过配置信息创建预编译的Statement
            preparedStatement = connection.prepareStatement(queryString);
            //执行查询，获取返回的结果集
            resultSet = preparedStatement.executeQuery();

            //存放结果类型的List结果集
            while (resultSet.next()){
                //实例化需要封装数据的对象
                E obj  = (E)domainClass.newInstance();

                //取出结果集的元信息
                ResultSetMetaData metaData = resultSet.getMetaData();
                //取出总数列
                int columnCount = metaData.getColumnCount();

                //遍历封装属性到对象中去，注意这里是是从1开始的
                for (int i = 1; i <= columnCount; i++) {
                    //获取每列的名称，列名从1开始
                    String columnName = metaData.getColumnName(i);
                    //根据列名获取对应的值
                    Object columnValue = resultSet.getObject(columnName);
                    //使用java的内省机制对属性进行封装
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(columnName, domainClass);
                    //获取属性写入的方法
                    Method writeMethod = propertyDescriptor.getWriteMethod();
                    //对应列的值赋值给实例化的对象，也就是给对象添加属性
                    writeMethod.invoke(obj,columnValue);
                }
                list.add(obj);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            release(preparedStatement,resultSet);
        }

        return list;
    }

    private void release(PreparedStatement preparedStatement, ResultSet resultSet) {
        if (resultSet!=null){
            try {
                resultSet.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if(preparedStatement!=null){
            try {
                preparedStatement.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}
