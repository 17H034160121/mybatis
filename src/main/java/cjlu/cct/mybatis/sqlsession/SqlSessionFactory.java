package cjlu.cct.mybatis.sqlsession;

/**
 * SqlSession的工厂接口
 * 工厂中的对象整体上固定，但是这个对象细节的实现不同，所以使用工厂来创建对象。
 * 但是自定义的实现没有那么完全，这里只定义了一个最基本的对象创建
 */
public interface SqlSessionFactory {
    /**
     * 获取SqlSession的连接
     * @return  SqlSession对象
     */
    SqlSession openSession();
}
