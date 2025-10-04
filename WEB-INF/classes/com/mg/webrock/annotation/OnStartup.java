package com.mg.webrock.annotation;
import java.lang.annotation.*;
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OnStartup
{
int priority() default 0;
}