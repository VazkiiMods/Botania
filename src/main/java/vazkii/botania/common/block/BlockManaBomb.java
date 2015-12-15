/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jul 25, 2015, 12:24:10 AM (GMT)]
 */
package vazkii.botania.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.world.World;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.mana.IManaTrigger;
import vazkii.botania.common.achievement.ICraftAchievement;
import vazkii.botania.common.achievement.ModAchievements;
import vazkii.botania.common.entity.EntityManaStorm;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;

public class BlockManaBomb extends BlockMod implements IManaTrigger, ILexiconable, ICraftAchievement {

	public BlockManaBomb() {
		super(Material.wood);
		setHardness(12.0F);
		setStepSound(soundTypeWood);
		setBlockName(LibBlockNames.MANA_BOMB);
	}

	@Override
	public void onBurstCollision(IManaBurst burst, World world, int x, int y, int z) {
		if(!burst.isFake() && !world.isRemote) {
			world.playAuxSFX(2001, x, y, z, getIdFromBlock(this));
			world.setBlockToAir(x, y, z);
			EntityManaStorm storm = new EntityManaStorm(world);
			storm.setPosition(x + 0.5, y + 0.5, z + 0.5);
			world.spawnEntityInWorld(storm);
		}
	}

	@Override
	public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.manaBomb;
	}

	@Override
	public Achievement getAchievementOnCraft(ItemStack stack, EntityPlayer player, IInventory matrix) {
		return ModAchievements.manaBombIgnite;
	}

}
