/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.CratePattern;
import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.common.block.tile.ModTiles;
import vazkii.botania.common.block.tile.TileCraftCrate;

import javax.annotation.Nonnull;

public class BlockCraftyCrate extends BlockOpenCrate implements IWandHUD, IWandable {

	public BlockCraftyCrate(Properties builder) {
		super(builder);
		registerDefaultState(defaultBlockState().setValue(BotaniaStateProps.CRATE_PATTERN, CratePattern.NONE));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BotaniaStateProps.CRATE_PATTERN);
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
		BlockEntity crate = world.getBlockEntity(pos);
		if (crate instanceof TileCraftCrate) {
			return ((TileCraftCrate) crate).getSignal();
		}
		return 0;
	}

	@Nonnull
	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
		return new TileCraftCrate(pos, state);
	}

	@Override
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		if (!level.isClientSide) {
			return createTickerHelper(type, ModTiles.CRAFT_CRATE, TileCraftCrate::serverTick);
		}
		return null;
	}

	@Override
	public boolean onUsedByWand(Player player, ItemStack stack, Level world, BlockPos pos, Direction side) {
		BlockEntity crate = world.getBlockEntity(pos);
		if (crate instanceof TileCraftCrate) {
			return ((TileCraftCrate) crate).onWanded(world);
		}
		return false;
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void renderHUD(PoseStack ms, Minecraft mc, Level world, BlockPos pos) {
		BlockEntity tile = world.getBlockEntity(pos);
		if (tile instanceof TileCraftCrate) {
			TileCraftCrate craft = (TileCraftCrate) tile;

			int width = 52;
			int height = 52;
			int xc = mc.getWindow().getGuiScaledWidth() / 2 + 20;
			int yc = mc.getWindow().getGuiScaledHeight() / 2 - height / 2;

			GuiComponent.fill(ms, xc - 6, yc - 6, xc + width + 6, yc + height + 6, 0x22000000);
			GuiComponent.fill(ms, xc - 4, yc - 4, xc + width + 4, yc + height + 4, 0x22000000);

			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					int index = i * 3 + j;
					int xp = xc + j * 18;
					int yp = yc + i * 18;

					boolean enabled = true;
					if (craft.getPattern() != CratePattern.NONE) {
						enabled = craft.getPattern().openSlots.get(index);
					}

					GuiComponent.fill(ms, xp, yp, xp + 16, yp + 16, enabled ? 0x22FFFFFF : 0x22FF0000);

					ItemStack item = craft.getItemHandler().getItem(index);
					mc.getItemRenderer().renderAndDecorateItem(item, xp, yp);
				}
			}
		}
	}
}
