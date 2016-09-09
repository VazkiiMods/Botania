/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Apr 1, 2014, 6:23:24 PM (GMT)]
 */
package vazkii.botania.client.model;

import vazkii.botania.api.state.enums.PylonVariant;

public interface IPylonModel {

	public void renderCrystal(PylonVariant variant);
	public void renderRing(PylonVariant variant);
	public void renderGems(PylonVariant variant);

}
