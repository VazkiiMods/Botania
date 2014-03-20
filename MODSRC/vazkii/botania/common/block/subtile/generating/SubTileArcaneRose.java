/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Mar 18, 2014, 8:45:25 PM (GMT)]
 */
package vazkii.botania.common.block.subtile.generating;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.SubTileGenerating;
import vazkii.botania.common.core.helper.ExperienceHelper;
import vazkii.botania.common.lexicon.LexiconData;
import cpw.mods.fml.common.network.PacketDispatcher;

public class SubTileArcaneRose extends SubTileGenerating {

	@Override
	public void onUpdate() {
		super.onUpdate();

		if(mana >= getMaxMana())
			return;

		List<EntityPlayer> players = supertile.worldObj.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getBoundingBox(supertile.xCoord - 1, supertile.yCoord, supertile.zCoord - 1, supertile.xCoord + 2, supertile.yCoord + 1, supertile.zCoord + 2));
		for(EntityPlayer player : players)
			if(ExperienceHelper.getPlayerXP(player) >= 1 && player.onGround) {
				ExperienceHelper.drainPlayerXP(player, 1);
				mana += 10;
				PacketDispatcher.sendPacketToAllInDimension(supertile.getDescriptionPacket(), supertile.worldObj.provider.dimensionId);
				return;
			}
	}

	@Override
	public int getColor() {
		return 0xFF8EF8;
	}

	@Override
	public int getMaxMana() {
		return 200;
	}

	@Override
	public LexiconEntry getEntry() {
		return LexiconData.arcaneRose;
	}

}
