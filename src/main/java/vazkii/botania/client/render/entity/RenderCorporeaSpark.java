/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Feb 14, 2015, 1:04:34 AM (GMT)]
 */
package vazkii.botania.client.render.entity;

import org.lwjgl.opengl.GL11;

import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.util.IIcon;
import vazkii.botania.common.entity.EntityCorporeaSpark;
import vazkii.botania.common.item.ItemCorporeaSpark;

public class RenderCorporeaSpark extends RenderSparkBase<EntityCorporeaSpark> {

	@Override
	public IIcon getBaseIcon(EntityCorporeaSpark entity) {
		return entity.isMaster() ? ItemCorporeaSpark.worldIconMaster : ItemCorporeaSpark.worldIcon;
	}

	@Override
	public void colorSpinningIcon(EntityCorporeaSpark entity) {
		int network = Math.min(15, entity.getNetwork());
		GL11.glColor3f(EntitySheep.fleeceColorTable[network][0], EntitySheep.fleeceColorTable[network][1], EntitySheep.fleeceColorTable[network][2]);
	}
	
	@Override
	public IIcon getSpinningIcon(EntityCorporeaSpark entity) {
		return ItemCorporeaSpark.iconColorStar;
	}
	
}
