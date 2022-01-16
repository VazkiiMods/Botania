package vazkii.botania.common.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A purely-documentative annotation.
 * This annotation is used by developers in xplat code. The annotated method is intended
 * to "soft implement" a certain method in a loader specific interface that cannot be
 * named in xplat code.
 * In this context, "soft implement" means to implement the method by matching the signature
 * with the intended interface method.
 * An example of interface methods that we would use this for is IForgeItem or FabricItem.
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface SoftImplement {
	/**
	 * What interface we're soft implementing
	 */
	String value();
}
