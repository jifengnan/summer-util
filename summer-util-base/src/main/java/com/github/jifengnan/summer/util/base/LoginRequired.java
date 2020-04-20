package com.github.jifengnan.summer.util.base;

import java.lang.annotation.*;

/**
 * 此注解指定某方法必须通过登录验证后才能访问。
 *
 * @author 纪凤楠 2019-03-26
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LoginRequired {
}
