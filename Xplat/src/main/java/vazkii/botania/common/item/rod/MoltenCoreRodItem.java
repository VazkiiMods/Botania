/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.rod;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.*;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.handler.BotaniaSounds;

public class MoltenCoreRodItem extends Item {

	private static final int COST = 300;
	private static final long COOLDOWN = 4;
	private long lastUsedAt = -1;

	public MoltenCoreRodItem(Properties props) {
		super(props);
	}

	public InteractionResult onLeftClick(Player p, Level world, InteractionHand hand, BlockPos pos, Direction side) {
		ItemStack stack = p.getItemInHand(hand);
		if (p.isSpectator() || stack.isEmpty() || !stack.is(this)) {
			return InteractionResult.PASS;
		}
		if (!ManaItemHandler.instance().requestManaExactForTool(stack, p, COST, false) || lastUsedAt + COOLDOWN > world.getGameTime()) {
			return InteractionResult.SUCCESS;
		}
		Container dummyInv = new SimpleContainer(1);
		BlockState state = world.getBlockState(pos);

		dummyInv.setItem(0, new ItemStack(state.getBlock()));
		world.getRecipeManager().getRecipeFor(RecipeType.SMELTING, dummyInv, p.level())
				.map(r -> r.assemble(dummyInv, world.registryAccess()))
				.filter(r -> !r.isEmpty() && r.getItem() instanceof BlockItem)
				.ifPresent(result -> {
					if (!world.isClientSide) {
						world.setBlockAndUpdate(pos, Block.byItem(result.getItem()).defaultBlockState());
						world.playSound(null, p.getX(), p.getY(), p.getZ(), BotaniaSounds.smeltRod, SoundSource.PLAYERS, 1F, 1F);
						world.playSound(null, p.getX(), p.getY(), p.getZ(), BotaniaSounds.smeltRod2, SoundSource.PLAYERS, 1F, 1F);

						ManaItemHandler.instance().requestManaExactForTool(stack, p, COST, true);
						lastUsedAt = world.getGameTime();
					}
					WispParticleData data1 = WispParticleData.wisp(0.5F, 1F, 0.2F, 0.2F, 1);
					for (int i = 0; i < 25; i++) {
						double x = pos.getX() + Math.random();
						double y = pos.getY() + Math.random();
						double z = pos.getZ() + Math.random();
						world.addParticle(data1, x, y, z, 0, (float) -Math.random() / 10F, 0);
					}
				});
		return InteractionResult.SUCCESS;
	}

	@Override
	public boolean canAttackBlock(BlockState state, Level world, BlockPos pos, Player player) {
		return !player.isCreative();
	}

	@Override
	public boolean overrideOtherStackedOnMe(
			@NotNull ItemStack rod, @NotNull ItemStack toSmelt, @NotNull Slot slot,
			@NotNull ClickAction clickAction, @NotNull Player player, @NotNull SlotAccess cursorAccess) {
		if (clickAction == ClickAction.SECONDARY && ManaItemHandler.instance().requestManaExactForTool(rod, player, COST * toSmelt.getCount(), false)) {
			Container dummyInv = new SimpleContainer(1);
			dummyInv.setItem(0, toSmelt);
			Level world = player.level();
			world.getRecipeManager().getRecipeFor(RecipeType.SMELTING, dummyInv, world)
					.map(r -> r.assemble(dummyInv, world.registryAccess()))
					.filter(r -> !r.isEmpty())
					.ifPresent(result -> {
						cursorAccess.set(result.copyWithCount(toSmelt.getCount()));
						ManaItemHandler.instance().requestManaExactForTool(rod, player, COST * toSmelt.getCount(), true);
					});
			return true;
		}
		return false;
	}
}
