package cjlu.cct.mybatis.sqlsession.proxy;

import cjlu.cct.mybatis.cfg.Mapper;
import cjlu.cct.mybatis.utils.Executor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.Map;

/**
 * 其实就是对动态代理中方法增强的接口的封装类
 * 为了复用，不在动态代理中直接使用匿名内部类实现接口
 */
public class MapperProxy implements InvocationHandler {

    private Map<String, Mapper> mappers;
    private Connection conn;

    public MapperProxy(Map<String, Mapper> mappers, Connection conn) {
        //这里因为针对的是代理对象构建的配置信息，所以一个代理类只对应一个mappers，使用覆盖的方式传参
        this.mappers = mappers;
        this.conn = conn;
    }

    /**
     *  增强代理类中的方法，并返回代理对象
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //获取代理的方法名
        String methodName = method.getName();
        //根据方法名，获取代理的类名
        String className = method.getDeclaringClass().getName();
        //组成全限定方法名，用于匹配映射配置中信息
        String key = className+"."+methodName;

        //查找映射配置信息中是否存在这个方法的对应
        Mapper mapper = mappers.get(key);

        if(mapper == null){
            throw new IllegalArgumentException("传入参数有误");
        }

        return new Executor().selectList(mapper,conn);
    }
}
