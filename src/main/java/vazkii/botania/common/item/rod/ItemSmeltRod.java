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

import java.util.Map;
import java.util.WeakHashMap;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import vazkii.botania.api.item.IManaProficiencyArmor;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.Botania;
import vazkii.botania.common.item.ItemMod;
import vazkii.botania.common.item.equipment.tool.ToolCommons;
import vazkii.botania.common.lib.LibItemNames;

public class ItemSmeltRod extends ItemMod implements IManaUsingItem {

	private static final int TIME = 10;
	private static final int COST = 300;
	private static final int COST_PER_TICK = COST / TIME;

	public static Map<EntityPlayer, SmeltData> playerData = new WeakHashMap();

	public ItemSmeltRod() {
		setUnlocalizedName(LibItemNames.SMELT_ROD);
		setMaxStackSize(1);
	}

	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack) {
		return EnumAction.bow;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack par1ItemStack) {
		return 72000;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		par3EntityPlayer.setItemInUse(par1ItemStack, getMaxItemUseDuration(par1ItemStack));
		return par1ItemStack;
	}

	@Override
	public void onUsingTick(ItemStack stack, EntityPlayer p, int time) {
		if(!ManaItemHandler.requestManaExactForTool(stack, p, COST_PER_TICK, false))
			return;

		MovingObjectPosition pos = ToolCommons.raytraceFromEntity(p.worldObj, p, false, 32);

		if(pos != null) {
			Block block = p.worldObj.getBlock(pos.blockX, pos.blockY, pos.blockZ);
			int meta = p.worldObj.getBlockMetadata(pos.blockX, pos.blockY, pos.blockZ);

			ItemStack blockStack = new ItemStack(block, 1, meta);
			ItemStack result = FurnaceRecipes.smelting().getSmeltingResult(blockStack);

			if(result != null && result.getItem() instanceof ItemBlock) {
				boolean decremented = false;

				if(playerData.containsKey(p)) {
					SmeltData data = playerData.get(p);

					if(data.equalPos(pos)) {
						data.progress--;
						decremented = true;
						if(data.progress <= 0) {
							if(!p.worldObj.isRemote) {
								p.worldObj.setBlock(pos.blockX, pos.blockY, pos.blockZ, Block.getBlockFromItem(result.getItem()), result.getItemDamage(), 1 | 2);
								p.worldObj.playSoundAtEntity(p, "fire.ignite", 0.6F, 1F);
								p.worldObj.playSoundAtEntity(p, "fire.fire", 1F, 1F);

								ManaItemHandler.requestManaExactForTool(stack, p, COST_PER_TICK, true);
								playerData.remove(p.getGameProfile().getName());
								decremented = false;
							}

							for(int i = 0; i < 25; i++) {
								double x = pos.blockX + Math.random();
								double y = pos.blockY + Math.random();
								double z = pos.blockZ + Math.random();

								Botania.proxy.wispFX(p.worldObj, x, y, z, 1F, 0.2F, 0.2F, 0.5F, (float) -Math.random() / 10F);
							}
						}
					}
				}

				if(!decremented)
					playerData.put(p, new SmeltData(pos, IManaProficiencyArmor.Helper.hasProficiency(p) ? (int) (TIME * 0.6) : TIME));
				else {
					for(int i = 0; i < 2; i++) {
						double x = pos.blockX + Math.random();
						double y = pos.blockY + Math.random();
						double z = pos.blockZ + Math.random();
						Botania.proxy.wispFX(p.worldObj, x, y, z, 1F, 0.2F, 0.2F, 0.5F, (float) -Math.random() / 10F);
					}
					if(time % 10 == 0)
						p.worldObj.playSoundAtEntity(p, "fire.fire", (float) Math.random() / 2F + 0.5F, 1F);
				}
			}
		}
	}

	@Override
	public boolean isFull3D() {
		return true;
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

	static class SmeltData {
		public MovingObjectPosition pos;
		public int progress;

		public SmeltData(MovingObjectPosition pos, int progress) {
			this.pos = pos;
			this.progress = progress;
		}

		public boolean equalPos(MovingObjectPosition pos) {
			return pos.blockX == this.pos.blockX && pos.blockY == this.pos.blockY && pos.blockZ == this.pos.blockZ;
		}
	}
}
