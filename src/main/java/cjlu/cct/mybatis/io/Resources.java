package cjlu.cct.mybatis.io;

import java.io.InputStream;

/**
 * 自定义Mybatis资源获取类
 */
public class Resources {
    /**
     * 获取指定配置文件
     * @param path  配置文件名
     * @return  输入流
     */
    public static InputStream getResourceAsStream(String path) {
        return Resources.class.getClassLoader().getResourceAsStream(path);
    }
}
