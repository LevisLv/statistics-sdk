package com.levislv.statisticssdk.plugin.annotation;

import androidx.annotation.LayoutRes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author levislv
 * @email  levislv@levislv.com
 * @blog   https://blog.levislv.com/
 * @github https://github.com/levislv/
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface StatisticsPage {
    Type type();

    @LayoutRes
    int id();

    String name();

    String data() default "{}";

    enum Type {
        /**
         * activity
         */
        ACTIVITY,
        /**
         * fragment
         */
        FRAGMENT,
        /**
         * other
         */
        OTHER
    }
}
