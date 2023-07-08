/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.block_entity.mana;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
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
import vazkii.botania.api.mana.BasicLensItem;
import vazkii.botania.api.mana.BurstProperties;
import vazkii.botania.api.mana.ManaTrigger;
import vazkii.botania.api.mana.TinyPlanetExcempt;
import vazkii.botania.api.state.BotaniaStateProperties;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.block_entity.BotaniaBlockEntities;
import vazkii.botania.common.block.block_entity.ExposedSimpleInventoryBlockEntity;

public class ManaPrismBlockEntity extends ExposedSimpleInventoryBlockEntity implements ManaTrigger {
	public ManaPrismBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaBlockEntities.PRISM, pos, state);
	}

	@Override
	public void onBurstCollision(ManaBurst burst) {
		ItemStack lens = getItemHandler().getItem(0);
		boolean active = !getBlockState().getValue(BlockStateProperties.POWERED);
		boolean valid = !lens.isEmpty() && lens.getItem() instanceof BasicLensItem && (!(lens.getItem() instanceof TinyPlanetExcempt excempt) || excempt.shouldPull(lens));

		if (active) {
			burst.setSourceLens(valid ? lens.copy() : ItemStack.EMPTY);
			burst.setColor(0xFFFFFF);
			burst.setGravity(0F);

			if (valid) {
				Entity burstEntity = burst.entity();
				BurstProperties properties = new BurstProperties(burst.getStartingMana(), burst.getMinManaLoss(), burst.getManaLossPerTick(), burst.getBurstGravity(), 1F, burst.getColor());

				((BasicLensItem) lens.getItem()).apply(lens, properties, level);

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
				return !stack.isEmpty() && stack.getItem() instanceof BasicLensItem;
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
			if (!state.is(BotaniaBlocks.prism) || state.getValue(BotaniaStateProperties.HAS_LENS) != hasLens) {
				BlockState base = state.is(BotaniaBlocks.prism) ? state : BotaniaBlocks.prism.defaultBlockState();
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
		public void renderHUD(GuiGraphics gui, Minecraft mc) {
			ItemStack lens = prism.getItem(0);
			if (!lens.isEmpty()) {
				Component lensName = lens.getHoverName();
				int halfWidth = (mc.font.width(lensName) + 24) / 2;
				int centerX = mc.getWindow().getGuiScaledWidth() / 2;
				int centerY = mc.getWindow().getGuiScaledHeight() / 2;

				RenderHelper.renderHUDBox(gui, centerX - halfWidth, centerY + 8, centerX + halfWidth, centerY + 28);

				gui.drawString(mc.font, lensName, centerX - halfWidth + 22, centerY + 14, 0xFFFFFF);
				gui.renderFakeItem(lens, centerX - halfWidth + 2, centerY + 10);
			}
		}
	}

}
