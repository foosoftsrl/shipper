package it.foosoft.shipper.api;

/**
 * Annotation for plugin parameters 
 *  
 * @author luca
 */
public @interface ConfigurationParm {
	String description() default "";
}
