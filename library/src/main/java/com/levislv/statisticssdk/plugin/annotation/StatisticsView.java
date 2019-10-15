package com.levislv.statisticssdk.plugin.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author LevisLv
 * @email  levislv@levislv.com
 * @blog   https://blog.levislv.com/
 * @book   https://book.levislv.com/
 * @github https://github.com/LevisLv/
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.CLASS)
public @interface StatisticsView {
    String parentName() default "";

    String name();

    String data() default "{}";
}
