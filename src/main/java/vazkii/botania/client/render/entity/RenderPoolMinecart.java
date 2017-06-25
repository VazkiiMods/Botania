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

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderMinecart;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import vazkii.botania.api.state.enums.PoolVariant;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.render.tile.RenderTilePool;
import vazkii.botania.common.block.tile.mana.TilePool;
import vazkii.botania.common.entity.EntityPoolMinecart;

import javax.annotation.Nonnull;

public class RenderPoolMinecart extends RenderMinecart<EntityPoolMinecart> {

	public RenderPoolMinecart(RenderManager manager) {
		super(manager);
	}

	@Override
	protected void renderCartContents(EntityPoolMinecart poolCart, float partialTicks, @Nonnull IBlockState state) {
		RenderTilePool.forceVariant = PoolVariant.DEFAULT;
		RenderTilePool.forceManaNumber = poolCart.getMana();
		TileEntityRendererDispatcher.instance.getRenderer(TilePool.class).render(null, poolCart.posX, poolCart.posY, poolCart.posZ, ClientTickHandler.partialTicks, -1, 0);
	}


}
