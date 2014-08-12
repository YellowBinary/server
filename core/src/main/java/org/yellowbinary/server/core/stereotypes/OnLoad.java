package org.yellowbinary.server.core.stereotypes;

import org.yellowbinary.server.core.Core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Called before or after a root node is loaded. Modules can use this to modify a root node.
 * <p/>
 * <p/>
 * The method annotated with OnLoad should be a void method and add to or modify the Node passed in as an argument.
 * When type=NODE it is called when a Node is loaded.
 * When type=FORM it is called when a form is created.
 * When type=NAVIGATION is called when the main navigation is loaded.
 * When type=NAVIGATION_ITEM is called for each navigation item being loaded.
 *
 * @see org.yellowbinary.server.core.Node
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface OnLoad {

    String base() default Core.Base.NODE;

    String with() default "";
    int weight() default 1000;
    boolean after() default true;

}
