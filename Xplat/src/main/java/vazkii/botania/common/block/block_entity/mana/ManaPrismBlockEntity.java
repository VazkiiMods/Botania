/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.block_entity.mana;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import vazkii.botania.api.block.WandHUD;
import vazkii.botania.api.internal.ManaBurst;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.mana.BurstProperties;
import vazkii.botania.api.mana.Lens;
import vazkii.botania.api.mana.ManaTrigger;
import vazkii.botania.api.mana.TinyPlanetExcempt;
import vazkii.botania.api.state.BotaniaStateProperties;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.block_entity.ExposedSimpleInventoryBlockEntity;
import vazkii.botania.common.block.tile.ModTiles;

public class ManaPrismBlockEntity extends ExposedSimpleInventoryBlockEntity implements ManaTrigger {
	public ManaPrismBlockEntity(BlockPos pos, BlockState state) {
		super(ModTiles.PRISM, pos, state);
	}

	@Override
	public void onBurstCollision(ManaBurst burst) {
		ItemStack lens = getItemHandler().getItem(0);
		boolean active = !getBlockState().getValue(BlockStateProperties.POWERED);
		boolean valid = !lens.isEmpty() && lens.getItem() instanceof Lens && (!(lens.getItem() instanceof TinyPlanetExcempt excempt) || excempt.shouldPull(lens));

		if (active) {
			burst.setSourceLens(valid ? lens.copy() : ItemStack.EMPTY);
			burst.setColor(0xFFFFFF);
			burst.setGravity(0F);

			if (valid) {
				Entity burstEntity = burst.entity();
				BurstProperties properties = new BurstProperties(burst.getStartingMana(), burst.getMinManaLoss(), burst.getManaLossPerTick(), burst.getBurstGravity(), 1F, burst.getColor());

				((Lens) lens.getItem()).apply(lens, properties, level);

				burst.setColor(properties.color);
				burst.setStartingMana(properties.maxMana);
				burst.setMinManaLoss(properties.ticksBeforeManaLoss);
				burst.setManaLossPerTick(properties.manaLossPerTick);
				burst.setGravity(properties.gravity);
				burstEntity.setDeltaMovement(burstEntity.getDeltaMovement().scale(properties.motionModifier));
			}
		}
	}

	@Override
	protected SimpleContainer createItemHandler() {
		return new SimpleContainer(1) {
			@Override
			public boolean canPlaceItem(int index, ItemStack stack) {
				return !stack.isEmpty() && stack.getItem() instanceof Lens;
			}

			@Override
			public int getMaxStackSize() {
				return 1;
			}
		};
	}

	@Override
	public void setChanged() {
		super.setChanged();
		if (level != null && !level.isClientSide) {
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
			BlockState state = getBlockState();
			boolean hasLens = !getItemHandler().getItem(0).isEmpty();
			if (!state.is(ModBlocks.prism) || state.getValue(BotaniaStateProperties.HAS_LENS) != hasLens) {
				BlockState base = state.is(ModBlocks.prism) ? state : ModBlocks.prism.defaultBlockState();
				level.setBlockAndUpdate(worldPosition, base.setValue(BotaniaStateProperties.HAS_LENS, hasLens));
			}
		}
	}

	public static class WandHud implements WandHUD {
		private final ManaPrismBlockEntity prism;

		public WandHud(ManaPrismBlockEntity prism) {
			this.prism = prism;
		}

		@Override
		public void renderHUD(PoseStack ms, Minecraft mc) {
			ItemStack lens = prism.getItem(0);
			if (!lens.isEmpty()) {
				Component lensName = lens.getHoverName();
				int width = 16 + mc.font.width(lensName) / 2;
				int x = mc.getWindow().getGuiScaledWidth() / 2 - width;
				int y = mc.getWindow().getGuiScaledHeight() / 2;

				mc.font.drawShadow(ms, lensName, x + 20, y + 5, -1);
				mc.getItemRenderer().renderAndDecorateItem(lens, x, y);
			}
		}
	}

}
