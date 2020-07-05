package cjlu.cct.dao;

import cjlu.cct.domain.Person;
import cjlu.cct.mybatis.ann.Select;

import java.util.List;

/**
 * 用户操作类
 */
public interface PersonDao {
    /**
     * 查找所有数据
     * @return
     */
    @Select("select * from person")
    List<Person> findAll();
}
