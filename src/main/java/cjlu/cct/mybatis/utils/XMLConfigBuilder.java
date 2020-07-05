package cjlu.cct.mybatis.utils;

import cjlu.cct.mybatis.ann.Select;
import cjlu.cct.mybatis.cfg.Configuration;
import cjlu.cct.mybatis.cfg.Mapper;
import cjlu.cct.mybatis.io.Resources;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XMLConfigBuilder {
    public static Configuration localConfiguration(InputStream config) {
        //定义封装连接信息的配置对象
        Configuration conf = new Configuration();
        //获取SAXReader对象
        SAXReader reader = new SAXReader();
        try{
            //根据输入字节流获取Document对象
            Document document = reader.read(config);
            //获取根节点
            Element root = document.getRootElement();
            //使用xpath中选择节点的方式，获取所有property属性
            List<Element> propertyElements = root.selectNodes("//property");
            for (Element propertyElement : propertyElements){
                //判断节点是连接数据库属性的哪个部分
                //取出name属性的值
                String name = propertyElement.attributeValue("name");
                if("driver".equals(name)){
                    //获取驱动的属性值
                    String driver = propertyElement.attributeValue("value");
                    conf.setDriver(driver);
                }
                if("url".equals(name)){
                    //获取url的属性值
                    String url = propertyElement.attributeValue("value");
                    conf.setUrl(url);
                }
                if("username".equals(name)){
                    //获取用户名的属性值
                    String username = propertyElement.attributeValue("value");
                    conf.setUsername(username);
                }
                if("password".equals(name)){
                    //获取密码的属性值
                    String password = propertyElement.attributeValue("value");
                    conf.setPassword(password);
                }
            }

            //使用xpath中选择节点的方式，获取所有mapper属性,也就是获取映射配置文件信息
            List<Element> mapperElements = root.selectNodes("//mappers/mapper");
            for (Element mapperElement : mapperElements){
                //判断mapperElement是哪个属性
                Attribute attribute = mapperElement.attribute("resource");
                //使用xml方式配置时存在属性，并且以xml优先
                if (attribute != null){
                    //取出属性，也就是映射文件地址
                    String mapperPath = attribute.getValue();
                    //根据映射文件地址加载映射文件中的信息
                    Map<String,Mapper> mappers = loadMapperConfiguration(mapperPath);
                    //给Configuration整体环境设置映射配置
                    conf.setMappers(mappers);
                }
                //使用注解配置时，不存在xml的mapper属性
                else {
                    //通过class属性获取对应的
                    String daoClassPath = mapperElement.attributeValue("class");
                    //根据daoClassPath通过注解的方式获取配置信息
                    Map<String,Mapper> mappers = loadMapperAnnotation(daoClassPath);
                    //给Configuration整体环境设置映射配置
                    conf.setMappers(mappers);

                }
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            try {
                config.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return conf;
    }

    /**
     * 通过xml解析的方式加载Mapper的配置信息
     * @param mapperPath 配置的映射文件位置
     * @return  对于mapper配置类的封装集合，通过对应类的全类名作为key定位。
     */
    private static Map<String, Mapper> loadMapperConfiguration(String mapperPath) {
        InputStream in = null;
        //定义返回值对象
        Map<String,Mapper> mappers = new HashMap<String, Mapper>();
        try{
            //根据路径获得输入流
            in = Resources.getResourceAsStream(mapperPath);
            //使用SAXReader的方式读取数据形成Document对象
            SAXReader reader = new SAXReader();
            Document document = reader.read(in);
            //获取根节点
            Element rootElement = document.getRootElement();
            //根据根节点获取namespace属性
            String namespace = rootElement.attributeValue("namespace");

            //获取所有select节点中的属性
            List<Element> selectElements = rootElement.selectNodes("//select");
            for(Element selectElement: selectElements){
                //获取Mapper中的基本信息
                String id = selectElement.attributeValue("id");
                String resultType = selectElement.attributeValue("resultType");
                String queryString = selectElement.getText();
                //拼接全类名
                String key = namespace+"."+id;

                //给单个mapper设置配置信息
                Mapper mapper = new Mapper();
                mapper.setQueryString(queryString);
                mapper.setResultType(resultType);

                //添加mapper到整体中
                mappers.put(key,mapper);
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return mappers;
    }

    private static Map<String, Mapper> loadMapperAnnotation(String daoClassPath) {
        //返回的Map结果集
        Map<String,Mapper> mappers = new HashMap<String, Mapper>();

        //获取传入的接口类字节码对象
        Class daoClass = null;
        try {
            daoClass = Class.forName(daoClassPath);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //获取接口下的所有方法
        Method[] methods = daoClass.getMethods();
        for(Method method : methods){
            boolean isAnnotation =  method.isAnnotationPresent(Select.class);
            //判断类中是否存在对应的注解类
            if (isAnnotation){
                //创建Mapper对象
                Mapper mapper = new Mapper();
                //取出注解的value属性
                Select selectAno = method.getAnnotation(Select.class);
                String queryString = selectAno.value();
                mapper.setQueryString(queryString);
                //获取当前方法的返回值，要求必须带有泛型信息，因为这里的返回是指定类型的list集合
                Type type = method.getGenericReturnType();
                //判断获取的类型是不是参数化的类型，也就是带有泛型的
                if(type instanceof ParameterizedType){
                    ParameterizedType parameterizedType = (ParameterizedType) type;
                    //得到参数化类型中的实际类型参数，也就是具体泛型
                    Type[] types = parameterizedType.getActualTypeArguments();
                    //取出第一个，因为泛型可以定义多个，我们这里只用到了第一个
                    Class domainClass = (Class) types[0];
                    //获取domainClass的类名
                    String resultType = domainClass.getName();
                    //给mapper赋值
                    mapper.setResultType(resultType);
                }
                //组装mapper信息
                String methodName = method.getName();
                String classNae = method.getDeclaringClass().getName();
                String key = classNae + "." + methodName;
                mappers.put(key,mapper);
            }
        }
        return mappers;
    }
}
