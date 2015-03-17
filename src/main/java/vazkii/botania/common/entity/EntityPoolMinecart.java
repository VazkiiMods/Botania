/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Mar 17, 2015, 6:36:43 PM (GMT)]
 */
package vazkii.botania.common.entity;

import vazkii.botania.common.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityPoolMinecart extends EntityMinecart {

	public EntityPoolMinecart(World p_i1712_1_) {
		super(p_i1712_1_);
	}
	
    public EntityPoolMinecart(World p_i1715_1_, double p_i1715_2_, double p_i1715_4_, double p_i1715_6_) {
        super(p_i1715_1_, p_i1715_2_, p_i1715_4_, p_i1715_6_);
    }
	
	@Override
	public Block func_145817_o() {
		return ModBlocks.pool;
	}

	@Override
	public int getMinecartType() {
		return 0;
	}
	
	@Override
    public void killMinecart(DamageSource p_94095_1_) {
        super.killMinecart(p_94095_1_);
        this.func_145778_a(Item.getItemFromBlock(ModBlocks.pool), 1, 0.0F);
    }

	@Override
    public int getDefaultDisplayTileOffset() {
        return 8;
    }
	
}
