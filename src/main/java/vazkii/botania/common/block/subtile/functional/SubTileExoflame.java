/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Mar 19, 2014, 3:42:32 PM (GMT)]
 */
package vazkii.botania.common.block.subtile.functional;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ObjectHolder;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.item.IExoflameHeatable;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityFunctionalFlower;
import vazkii.botania.common.lib.LibMisc;

public class SubTileExoflame extends TileEntityFunctionalFlower {
	@ObjectHolder(LibMisc.MOD_ID + ":exoflame")
	public static TileEntityType<SubTileExoflame> TYPE;

	private static final int RANGE = 5;
	private static final int RANGE_Y = 2;
	private static final int COST = 300;

	public SubTileExoflame() {
		super(TYPE);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if(getWorld().isRemote || getMana() <= 2)
			return;

		boolean did = false;

		for(BlockPos pos : BlockPos.getAllInBoxMutable(getPos().add(-RANGE, -RANGE_Y, -RANGE),
				getPos().add(RANGE, RANGE_Y, RANGE))) {
			TileEntity tile = getWorld().getTileEntity(pos);
			if(tile != null) {
				LazyOptional<IExoflameHeatable> cap = tile.getCapability(BotaniaAPI.EXOFLAME_HEATABLE_CAP);
				if(cap.isPresent()) {
					IExoflameHeatable heatable = cap.orElseThrow(NullPointerException::new);

					if(heatable.canSmelt() && getMana() > 2) {
						if(heatable.getBurnTime() < 2) {
							heatable.boostBurnTime();
							addMana(-COST);
							did = true;
						}

						if(ticksExisted % 2 == 0)
							heatable.boostCookTime();

						if(getMana() <= 0)
							break;
					}
				}
			}
			if(did)
				sync();
		}
	}


	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(getPos(), RANGE);
	}

	@Override
	public int getMaxMana() {
		return 300;
	}

	@Override
	public int getColor() {
		return 0x661600;
	}

}
