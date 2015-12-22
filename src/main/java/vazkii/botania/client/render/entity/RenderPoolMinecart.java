/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Mar 17, 2015, 11:17:48 PM (GMT)]
 */
package vazkii.botania.client.render.entity;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderMinecart;
import net.minecraft.entity.item.EntityMinecart;
import vazkii.botania.client.render.tile.RenderTilePool;
import vazkii.botania.common.entity.EntityPoolMinecart;

public class RenderPoolMinecart extends RenderMinecart<EntityPoolMinecart> {

	public RenderPoolMinecart(RenderManager p_i46155_1_) {
		super(p_i46155_1_);
	}

	@Override
	protected void func_180560_a(EntityPoolMinecart poolCart, float p_147910_2_, IBlockState state) {
		RenderTilePool.forceManaNumber = poolCart.getMana();
		super.func_180560_a(poolCart, p_147910_2_, state);
	}


}
