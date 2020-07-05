package cjlu.cct.mybatis.ann;

import java.lang.annotation.*;

@Documented                              //会被javadoc命令识别
@Retention(RetentionPolicy.RUNTIME)     //相当于作用时期，比如：运行期、编译期
@Target({ElementType.METHOD})           //相当于作用域,比如方法、类
public @interface Select {
    String value();
}
