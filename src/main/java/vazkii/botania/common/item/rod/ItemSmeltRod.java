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
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import vazkii.botania.api.item.IManaProficiencyArmor;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.Botania;
import vazkii.botania.common.item.ItemMod;
import vazkii.botania.common.item.equipment.tool.ToolCommons;
import vazkii.botania.common.lib.LibItemNames;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.WeakHashMap;

public class ItemSmeltRod extends ItemMod implements IManaUsingItem {

	private static final int TIME = 10;
	private static final int COST = 300;
	private static final int COST_PER_TICK = COST / TIME;

	public static final Map<EntityPlayer, SmeltData> playerData = new WeakHashMap<>();

	public ItemSmeltRod() {
		super(LibItemNames.SMELT_ROD);
		setMaxStackSize(1);
	}

	@Nonnull
	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack) {
		return EnumAction.BOW;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack par1ItemStack) {
		return 72000;
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
		player.setActiveHand(hand);
		return ActionResult.newResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}

	@Override
	public void onUsingTick(ItemStack stack, EntityLivingBase living, int time) {
		if(!(living instanceof EntityPlayer)) return;
		EntityPlayer p = (EntityPlayer) living;

		if(!ManaItemHandler.requestManaExactForTool(stack, p, COST_PER_TICK, false))
			return;

		RayTraceResult pos = ToolCommons.raytraceFromEntity(p.world, p, false, 32);

		if(pos != null && pos.getBlockPos() != null) {
			IBlockState state = p.world.getBlockState(pos.getBlockPos());

			ItemStack blockStack = new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state));
			ItemStack result = FurnaceRecipes.instance().getSmeltingResult(blockStack);

			if(!result.isEmpty() && result.getItem() instanceof ItemBlock) {
				boolean decremented = false;

				if(playerData.containsKey(p)) {
					SmeltData data = playerData.get(p);

					if(data.equalPos(pos)) {
						data.progress--;
						decremented = true;
						if(data.progress <= 0) {
							if(!p.world.isRemote) {
								p.world.setBlockState(pos.getBlockPos(), Block.getBlockFromItem(result.getItem()).getStateFromMeta(result.getItemDamage()), 1 | 2);
								p.world.playSound(null, p.posX, p.posY, p.posZ, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.PLAYERS, 0.6F, 1F);
								p.world.playSound(null, p.posX, p.posY, p.posZ, SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.PLAYERS, 1F, 1F);

								ManaItemHandler.requestManaExactForTool(stack, p, COST_PER_TICK, true);
								playerData.remove(p);
								decremented = false;
							}

							for(int i = 0; i < 25; i++) {
								double x = pos.getBlockPos().getX() + Math.random();
								double y = pos.getBlockPos().getY() + Math.random();
								double z = pos.getBlockPos().getZ() + Math.random();

								Botania.proxy.wispFX(x, y, z, 1F, 0.2F, 0.2F, 0.5F, (float) -Math.random() / 10F);
							}
						}
					}
				}

				if(!decremented)
					playerData.put(p, new SmeltData(pos, IManaProficiencyArmor.Helper.hasProficiency(p, stack) ? (int) (TIME * 0.6) : TIME));
				else {
					for(int i = 0; i < 2; i++) {
						double x = pos.getBlockPos().getX() + Math.random();
						double y = pos.getBlockPos().getY() + Math.random();
						double z = pos.getBlockPos().getZ() + Math.random();
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
		public final RayTraceResult pos;
		public int progress;

		public SmeltData(RayTraceResult pos, int progress) {
			this.pos = pos;
			this.progress = progress;
		}

		public boolean equalPos(RayTraceResult pos) {
			return pos.getBlockPos().equals(this.pos.getBlockPos());
		}
	}
}
