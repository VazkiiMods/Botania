/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Mar 14, 2014, 8:54:11 PM (GMT)]
 */
package vazkii.botania.common.block.subtile.functional;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.dimension.EndDimension;
import net.minecraftforge.registries.ObjectHolder;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityFunctionalFlower;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibMisc;

import java.util.List;

public class SubTileFallenKanade extends TileEntityFunctionalFlower {
	@ObjectHolder(LibMisc.MOD_ID + ":fallen_kanade")
	public static TileEntityType<SubTileFallenKanade> TYPE;

	private static final int RANGE = 2;
	private static final int COST = 120;

	public SubTileFallenKanade() {
		super(TYPE);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if(!getWorld().isRemote && !(getWorld().getDimension() instanceof EndDimension)) {
			boolean did = false;
			List<EntityPlayer> players = getWorld().getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(getPos().add(-RANGE, -RANGE, -RANGE), getPos().add(RANGE + 1, RANGE + 1, RANGE + 1)));
			for(EntityPlayer player : players) {
				if(player.getActivePotionEffect(MobEffects.REGENERATION) == null && mana >= COST) {
					player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 59, 2, true, true));
					mana -= COST;
					did = true;
				}
			}
			if(did)
				sync();
		}
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(toBlockPos(), RANGE);
	}

	@Override
	public int getColor() {
		return 0xFFFF00;
	}

	@Override
	public int getMaxMana() {
		return 900;
	}

	@Override
	public LexiconEntry getEntry() {
		return LexiconData.fallenKanade;
	}

}
