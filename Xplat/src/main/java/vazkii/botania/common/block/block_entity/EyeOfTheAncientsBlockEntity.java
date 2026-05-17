/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.block_entity;

import com.mojang.blaze3d.platform.Window;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.block.WandHUD;
import vazkii.botania.api.block.Wandable;
import vazkii.botania.api.state.BotaniaStateProperties;
import vazkii.botania.api.state.enums.AnimalMode;
import vazkii.botania.client.core.helper.RenderHelper;

import java.util.ArrayList;
import java.util.List;

public class EyeOfTheAncientsBlockEntity extends BlockEntity implements Wandable {
	public static final int RANGE = 6;
	public static final int MAX_ANIMALS = 15;

	public EyeOfTheAncientsBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaBlockEntities.FOREST_EYE, pos, state);
	}

	public AnimalMode getMode() {
		return getBlockState().getValue(BotaniaStateProperties.ANIMAL_MODE);
	}

	public static void serverTick(Level level, BlockPos worldPosition, BlockState state, EyeOfTheAncientsBlockEntity self) {
		List<Animal> animals = new ArrayList<>(MAX_ANIMALS);
		level.getEntities(EntityTypeTest.forClass(Animal.class), new AABB(worldPosition).inflate(RANGE), self.getMode(),
				animals, MAX_ANIMALS);
		int numAnimals = Math.min(animals.size(), 15);
		if (numAnimals != state.getValue(BlockStateProperties.POWER)) {
			level.setBlock(worldPosition, state.setValue(BlockStateProperties.POWER, numAnimals), Block.UPDATE_ALL);
		}
	}

	@Override
	public boolean onUsedByWand(@Nullable Player player, ItemStack stack, Direction side) {
		if (player == null || player.isShiftKeyDown()) {
			level.setBlock(getBlockPos(), getBlockState().cycle(BotaniaStateProperties.ANIMAL_MODE),
					Block.UPDATE_CLIENTS);
			return true;
		}
		return false;
	}

	public static class WandHud implements WandHUD {
		private final EyeOfTheAncientsBlockEntity eye;

		public WandHud(EyeOfTheAncientsBlockEntity eye) {
			this.eye = eye;
		}

		@Override
		public void renderHUD(GuiGraphics gui, Window window, Font font, float partialTick) {
			String mode = I18n.get("botaniamisc.eye_of_the_ancients." + eye.getMode().getSerializedName());
			int strWidth = font.width(mode);
			int x = (window.getGuiScaledWidth() - strWidth) / 2;
			int y = window.getGuiScaledHeight() / 2 + 8;

			RenderHelper.renderHUDBox(gui, x - 2, y, x + strWidth + 2, y + 12);
			gui.drawString(font, mode, x, y + 2, ChatFormatting.WHITE.getColor());
		}
	}

}
