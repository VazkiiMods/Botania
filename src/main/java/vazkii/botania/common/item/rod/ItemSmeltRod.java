/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 24, 2015, 11:05:41 PM (GMT)]
 */
package vazkii.botania.common.item.rod;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.UseAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import vazkii.botania.api.item.IManaProficiencyArmor;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.Botania;
import vazkii.botania.common.item.ItemMod;
import vazkii.botania.common.item.equipment.tool.ToolCommons;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Optional;
import java.util.WeakHashMap;

public class ItemSmeltRod extends ItemMod implements IManaUsingItem {

	private static final int TIME = 10;
	private static final int COST = 300;
	private static final int COST_PER_TICK = COST / TIME;

	public static final Map<PlayerEntity, SmeltData> playerData = new WeakHashMap<>();

	public ItemSmeltRod(Properties props) {
		super(props);
	}

	@Nonnull
	@Override
	public UseAction getUseAction(ItemStack par1ItemStack) {
		return UseAction.BOW;
	}

	@Override
	public int getUseDuration(ItemStack par1ItemStack) {
		return 72000;
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, @Nonnull Hand hand) {
		player.setActiveHand(hand);
		return ActionResult.newResult(ActionResultType.SUCCESS, player.getHeldItem(hand));
	}

	@Override
	public void onUsingTick(ItemStack stack, LivingEntity living, int time) {
		if(!(living instanceof PlayerEntity)) return;
		PlayerEntity p = (PlayerEntity) living;
		IInventory dummyInv = new Inventory(1);

		if(!ManaItemHandler.requestManaExactForTool(stack, p, COST_PER_TICK, false))
			return;

		RayTraceResult ray = ToolCommons.raytraceFromEntity(p.world, p, RayTraceContext.FluidMode.NONE, 32);

		if(ray.getType() == RayTraceResult.Type.BLOCK) {
			BlockRayTraceResult pos = (BlockRayTraceResult) ray;
			BlockState state = p.world.getBlockState(pos.getPos());

			dummyInv.setInventorySlotContents(0, new ItemStack(state.getBlock()));
			Optional<ItemStack> maybeResult= p.world.getRecipeManager()
					.getRecipe(IRecipeType.SMELTING, dummyInv, p.world)
					.map(IRecipe::getRecipeOutput);

			if(maybeResult.isPresent()
					&& !maybeResult.get().isEmpty()
					&& maybeResult.get().getItem() instanceof BlockItem) {
				ItemStack result = maybeResult.get();
				boolean decremented = false;

				if(playerData.containsKey(p)) {
					SmeltData data = playerData.get(p);

					if(data.equalPos(pos)) {
						data.progress--;
						decremented = true;
						if(data.progress <= 0) {
							if(!p.world.isRemote) {
								p.world.setBlockState(pos.getPos(), Block.getBlockFromItem(result.getItem()).getDefaultState());
								p.world.playSound(null, p.posX, p.posY, p.posZ, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.PLAYERS, 0.6F, 1F);
								p.world.playSound(null, p.posX, p.posY, p.posZ, SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.PLAYERS, 1F, 1F);

								ManaItemHandler.requestManaExactForTool(stack, p, COST_PER_TICK, true);
								playerData.remove(p);
								decremented = false;
							}

							for(int i = 0; i < 25; i++) {
								double x = pos.getPos().getX() + Math.random();
								double y = pos.getPos().getY() + Math.random();
								double z = pos.getPos().getZ() + Math.random();

								Botania.proxy.wispFX(x, y, z, 1F, 0.2F, 0.2F, 0.5F, (float) -Math.random() / 10F);
							}
						}
					}
				}

				if(!decremented)
					playerData.put(p, new SmeltData(pos, IManaProficiencyArmor.Helper.hasProficiency(p, stack) ? (int) (TIME * 0.6) : TIME));
				else {
					for(int i = 0; i < 2; i++) {
						double x = pos.getPos().getX() + Math.random();
						double y = pos.getPos().getY() + Math.random();
						double z = pos.getPos().getZ() + Math.random();
						Botania.proxy.wispFX(x, y, z, 1F, 0.2F, 0.2F, 0.5F, (float) -Math.random() / 10F);
					}
					if(time % 10 == 0)
						p.world.playSound(null, p.posX, p.posY, p.posZ, SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.PLAYERS, (float) Math.random() / 2F + 0.5F, 1F);
				}
			}
		}
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

	static class SmeltData {
		public final BlockRayTraceResult pos;
		public int progress;

		public SmeltData(BlockRayTraceResult pos, int progress) {
			this.pos = pos;
			this.progress = progress;
		}

		public boolean equalPos(BlockRayTraceResult pos) {
			return pos.getPos().equals(this.pos.getPos());
		}
	}
}
