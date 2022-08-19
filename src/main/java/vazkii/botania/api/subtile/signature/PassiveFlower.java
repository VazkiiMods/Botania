/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [04/12/2015, 16:47:01 (GMT)]
 */
package vazkii.botania.api.subtile.signature;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * This annotation used for detecting whether a SubTileEntity
 * is a passive flower without having to instantiate it. This
 * is used for the flowers' tooltips.
 */
@Retention(value = RUNTIME)
@Target(value = TYPE)
public @interface PassiveFlower {

}
