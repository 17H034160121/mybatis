package cjlu.cct.mybatis.sqlsession;

/**
 *  自定义Mybatis中数据交互的主要类
 *  用来创建方法的代理对象
 *  这里定义了上层的接口，为了提供给工厂类，因为工厂类大体上生产的SqlSession相同，细节上区分细化。
 */
public interface SqlSession {
    /**
     * 根据泛型获取对应类的代理对象
     * @param daoInterfaceClass dao操作类的接口名字节码
     * @param <T>   根据接口名，通过反射获得的对应接口类
     * @return  代理对象
     */
    <T> T getMapper(Class<T> daoInterfaceClass);

    /**
     * 关闭SqlSession的连接
     */
    void close();
}
