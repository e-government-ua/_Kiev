package org.wf.dp.dniprorada.util.caching;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
@Documented
public @interface EnableCaching {

}