/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [May 31, 2014, 7:49:43 PM (GMT)]
 */
package vazkii.botania.common.block.subtile.functional;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.SubTileFunctional;
import vazkii.botania.common.lexicon.LexiconData;

import java.util.Random;

public class SubTileLoonuim extends SubTileFunctional {

	private static final int COST = 35000;
	private static final int RANGE = 3;

	@Override
	public void onUpdate() {
		super.onUpdate();
		if(!supertile.getWorld().isRemote && redstoneSignal == 0 && ticksExisted % 200 == 0 && mana >= COST) {
			Random rand = supertile.getWorld().rand;

			ItemStack stack;
			do {
				stack = supertile.getWorld().getLootTableManager().getLootTableFromLocation(new ResourceLocation("minecraft", "chests/simple_dungeon")).generateLootForPools(rand, new LootContext.Builder(((WorldServer) supertile.getWorld())).build()).get(0);
			} while(stack == null || BotaniaAPI.looniumBlacklist.contains(stack.getItem()));

			int bound = RANGE * 2 + 1;
			EntityItem entity = new EntityItem(supertile.getWorld(), supertile.getPos().getX() - RANGE + rand.nextInt(bound) , supertile.getPos().getY() + 1, supertile.getPos().getZ() - RANGE + rand.nextInt(bound), stack);
			entity.motionX = 0;
			entity.motionY = 0;
			entity.motionZ = 0;

			supertile.getWorld().spawnEntityInWorld(entity);

			mana -= COST;
			sync();
		}
	}

	@Override
	public int getColor() {
		return 0x274A00;
	}

	@Override
	public LexiconEntry getEntry() {
		return LexiconData.loonium;
	}

	@Override
	public int getMaxMana() {
		return COST;
	}

	@Override
	public boolean acceptsRedstone() {
		return true;
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(toBlockPos(), RANGE);
	}

}
