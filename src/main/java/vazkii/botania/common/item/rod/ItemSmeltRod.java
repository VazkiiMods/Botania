/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.rod;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import vazkii.botania.api.item.IManaProficiencyArmor;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.item.equipment.tool.ToolCommons;
import vazkii.botania.common.item.rod.ItemSmeltRod.SmeltData;

import javax.annotation.Nonnull;

import java.util.Map;
import java.util.WeakHashMap;

public class ItemSmeltRod extends Item implements IManaUsingItem {

	private static final int TIME = 10;
	private static final int COST = 300;
	private static final int COST_PER_TICK = COST / TIME;

	public static final Map<Player, SmeltData> playerData = new WeakHashMap<>();

	public ItemSmeltRod(Properties props) {
		super(props);
	}

	@Nonnull
	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.BOW;
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 72000;
	}

	@Nonnull
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, @Nonnull InteractionHand hand) {
		return ItemUtils.startUsingInstantly(world, player, hand);
	}

	@Override
	public void onUseTick(Level world, LivingEntity living, ItemStack stack, int time) {
		if (!(living instanceof Player)) {
			return;
		}
		Player p = (Player) living;
		Container dummyInv = new SimpleContainer(1);

		if (!ManaItemHandler.instance().requestManaExactForTool(stack, p, COST_PER_TICK, false)) {
			return;
		}

		BlockHitResult pos = ToolCommons.raytraceFromEntity(p, 32, false);

		if (pos.getType() == HitResult.Type.BLOCK) {
			BlockState state = world.getBlockState(pos.getBlockPos());

			dummyInv.setItem(0, new ItemStack(state.getBlock()));
			world.getRecipeManager().getRecipeFor(RecipeType.SMELTING, dummyInv, p.level)
					.map(r -> r.assemble(dummyInv))
					.filter(r -> !r.isEmpty() && r.getItem() instanceof BlockItem)
					.ifPresent(result -> {
						boolean decremented = false;

						if (playerData.containsKey(p)) {
							SmeltData data = playerData.get(p);

							if (data.equalPos(pos)) {
								data.progress--;
								decremented = true;
								if (data.progress <= 0) {
									if (!world.isClientSide) {
										world.setBlockAndUpdate(pos.getBlockPos(), Block.byItem(result.getItem()).defaultBlockState());
										world.playSound(null, p.getX(), p.getY(), p.getZ(), SoundEvents.FLINTANDSTEEL_USE, SoundSource.PLAYERS, 0.6F, 1F);
										world.playSound(null, p.getX(), p.getY(), p.getZ(), SoundEvents.FIRE_AMBIENT, SoundSource.PLAYERS, 1F, 1F);

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
								world.playSound(null, p.getX(), p.getY(), p.getZ(), SoundEvents.FIRE_AMBIENT, SoundSource.PLAYERS, (float) Math.random() / 2F + 0.5F, 1F);
							}
						}
					});
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
