/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.rod;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

import vazkii.botania.api.item.IManaProficiencyArmor;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.item.equipment.tool.ToolCommons;
import vazkii.botania.common.item.rod.ItemSmeltRod.SmeltData;

import javax.annotation.Nonnull;

import java.util.Map;
import java.util.Optional;
import java.util.WeakHashMap;

public class ItemSmeltRod extends Item implements IManaUsingItem {

	private static final int TIME = 10;
	private static final int COST = 300;
	private static final int COST_PER_TICK = COST / TIME;

	public static final Map<PlayerEntity, SmeltData> playerData = new WeakHashMap<>();

	public ItemSmeltRod(Settings props) {
		super(props);
	}

	@Nonnull
	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.BOW;
	}

	@Override
	public int getMaxUseTime(ItemStack stack) {
		return 72000;
	}

	@Nonnull
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, @Nonnull Hand hand) {
		player.setCurrentHand(hand);
		return TypedActionResult.consume(player.getStackInHand(hand));
	}

	@Override
	public void usageTick(World world, LivingEntity living, ItemStack stack, int time) {
		if (!(living instanceof PlayerEntity)) {
			return;
		}
		PlayerEntity p = (PlayerEntity) living;
		Inventory dummyInv = new SimpleInventory(1);

		if (!ManaItemHandler.instance().requestManaExactForTool(stack, p, COST_PER_TICK, false)) {
			return;
		}

		BlockHitResult pos = ToolCommons.raytraceFromEntity(p, 32, false);

		if (pos.getType() == HitResult.Type.BLOCK) {
			BlockState state = world.getBlockState(pos.getBlockPos());

			dummyInv.setStack(0, new ItemStack(state.getBlock()));
			Optional<ItemStack> maybeResult = world.getRecipeManager()
					.getFirstMatch(RecipeType.SMELTING, dummyInv, p.world)
					.map(r -> r.craft(dummyInv));

			if (maybeResult.isPresent()
					&& !maybeResult.get().isEmpty()
					&& maybeResult.get().getItem() instanceof BlockItem) {
				ItemStack result = maybeResult.get();
				boolean decremented = false;

				if (playerData.containsKey(p)) {
					SmeltData data = playerData.get(p);

					if (data.equalPos(pos)) {
						data.progress--;
						decremented = true;
						if (data.progress <= 0) {
							if (!world.isClient) {
								world.setBlockState(pos.getBlockPos(), Block.getBlockFromItem(result.getItem()).getDefaultState());
								world.playSound(null, p.getX(), p.getY(), p.getZ(), SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.PLAYERS, 0.6F, 1F);
								world.playSound(null, p.getX(), p.getY(), p.getZ(), SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.PLAYERS, 1F, 1F);

								ManaItemHandler.instance().requestManaExactForTool(stack, p, COST_PER_TICK, true);
								playerData.remove(p);
								decremented = false;
							}

							WispParticleData data1 = WispParticleData.wisp(0.5F, 1F, 0.2F, 0.2F, 1);
							for (int i = 0; i < 25; i++) {
								double x = pos.getBlockPos().getX() + Math.random();
								double y = pos.getBlockPos().getY() + Math.random();
								double z = pos.getBlockPos().getZ() + Math.random();
								world.addParticle(data1, x, y, z, 0, (float) -Math.random() / 10F, 0);
							}
						}
					}
				}

				if (!decremented) {
					playerData.put(p, new SmeltData(pos, IManaProficiencyArmor.hasProficiency(p, stack) ? (int) (TIME * 0.6) : TIME));
				} else {
					for (int i = 0; i < 2; i++) {
						double x = pos.getBlockPos().getX() + Math.random();
						double y = pos.getBlockPos().getY() + Math.random();
						double z = pos.getBlockPos().getZ() + Math.random();
						WispParticleData data = WispParticleData.wisp(0.5F, 1F, 0.2F, 0.2F, 1);
						world.addParticle(data, x, y, z, 0, (float) Math.random() / 10F, 0);
					}
					if (time % 10 == 0) {
						world.playSound(null, p.getX(), p.getY(), p.getZ(), SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.PLAYERS, (float) Math.random() / 2F + 0.5F, 1F);
					}
				}
			}
		}
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

	static class SmeltData {
		public final BlockHitResult pos;
		public int progress;

		public SmeltData(BlockHitResult pos, int progress) {
			this.pos = pos;
			this.progress = progress;
		}

		public boolean equalPos(BlockHitResult pos) {
			return pos.getBlockPos().equals(this.pos.getBlockPos());
		}
	}
}
