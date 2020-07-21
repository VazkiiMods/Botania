/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.CratePattern;
import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.common.block.tile.TileCraftCrate;

import javax.annotation.Nonnull;

public class BlockCraftyCrate extends BlockOpenCrate implements IWandHUD {

	public BlockCraftyCrate(Settings builder) {
		super(builder);
		setDefaultState(getDefaultState().with(BotaniaStateProps.CRATE_PATTERN, CratePattern.NONE));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(BotaniaStateProps.CRATE_PATTERN);
	}

	@Nonnull
	@Override
	public BlockEntity createBlockEntity(@Nonnull BlockView world) {
		return new TileCraftCrate();
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void renderHUD(MatrixStack ms, MinecraftClient mc, World world, BlockPos pos) {
		BlockEntity tile = world.getBlockEntity(pos);
		if (tile instanceof TileCraftCrate) {
			TileCraftCrate craft = (TileCraftCrate) tile;

			int width = 52;
			int height = 52;
			int xc = mc.getWindow().getScaledWidth() / 2 + 20;
			int yc = mc.getWindow().getScaledHeight() / 2 - height / 2;

			DrawableHelper.fill(ms, xc - 6, yc - 6, xc + width + 6, yc + height + 6, 0x22000000);
			DrawableHelper.fill(ms, xc - 4, yc - 4, xc + width + 4, yc + height + 4, 0x22000000);

			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					int index = i * 3 + j;
					int xp = xc + j * 18;
					int yp = yc + i * 18;

					boolean enabled = true;
					if (craft.getPattern() != CratePattern.NONE) {
						enabled = craft.getPattern().openSlots.get(index);
					}

					DrawableHelper.fill(ms, xp, yp, xp + 16, yp + 16, enabled ? 0x22FFFFFF : 0x22FF0000);

					ItemStack item = craft.getItemHandler().getStack(index);
					mc.getItemRenderer().renderInGuiWithOverrides(item, xp, yp);
				}
			}
		}
	}
}
