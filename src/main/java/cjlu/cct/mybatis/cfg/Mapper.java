package cjlu.cct.mybatis.cfg;

/**
 * 用于封装sql语句，结果全限定类名的类，相当于是Mapper的Configuration
 */
public class Mapper {

    private String queryString;
    private String resultType;

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }
}
