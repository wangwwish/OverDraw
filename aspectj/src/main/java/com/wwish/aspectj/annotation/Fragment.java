package com.wwish.aspectj.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by wwish on 2018/1/11.
 */

@Retention(RetentionPolicy.CLASS)
@Target({ElementType.ANNOTATION_TYPE,  ElementType.METHOD })
public @interface Fragment {
}
