/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jul 31, 2014, 12:59:16 AM (GMT)]
 */
package vazkii.botania.common.item.equipment.bauble;

import baubles.api.BaubleType;
import baubles.common.lib.PlayerHandler;
import baubles.common.network.PacketHandler;
import baubles.common.network.PacketSyncBauble;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.AnimalChest;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.MerchantRecipe;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import vazkii.botania.api.item.IBaubleRender;
import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.lib.LibItemNames;
import vazkii.botania.common.lib.LibObfuscation;

import java.util.ArrayList;
import java.util.List;

public class ItemItemFinder extends ItemBauble implements IBaubleRender {

	private static final String TAG_POSITIONS = "highlightPositions";

	public ItemItemFinder() {
		super(LibItemNames.ITEM_FINDER);
	}

	@Override
	public void onWornTick(ItemStack stack, EntityLivingBase player) {
		super.onWornTick(stack, player);

		if(!(player instanceof EntityPlayer))
			return;

		if(player.worldObj.isRemote)
			tickClient(stack, (EntityPlayer) player);
		else tickServer(stack, (EntityPlayer) player);
	}

	public void tickClient(ItemStack stack, EntityPlayer player) {
		if(!Botania.proxy.isTheClientPlayer(player))
			return;

		String pos = ItemNBTHelper.getString(stack, TAG_POSITIONS, "");
		String[] tokens = pos.split(";");

		for(String token : tokens) {
			if(token.isEmpty())
				continue;

			if(token.contains(",")) {
				String[] tokens_ = token.split(",");
				int x = Integer.parseInt(tokens_[0]);
				int y = Integer.parseInt(tokens_[1]);
				int z = Integer.parseInt(tokens_[2]);
				float m = 0.02F;
				Botania.proxy.setWispFXDepthTest(false);
				Botania.proxy.wispFX(player.worldObj, x + (float) Math.random(), y + (float) Math.random(), z + (float) Math.random(), (float) Math.random(), (float) Math.random(), (float) Math.random(), 0.15F + 0.05F * (float) Math.random(), m * (float) (Math.random() - 0.5), m * (float) (Math.random() - 0.5), m * (float) (Math.random() - 0.5));
			} else {
				int id = Integer.parseInt(token);
				Entity e = player.worldObj.getEntityByID(id);

				if(e != null && Math.random() < 0.6) {
					Botania.proxy.setWispFXDepthTest(Math.random() < 0.6);
					Botania.proxy.wispFX(player.worldObj, e.posX + (float) (Math.random() * 0.5 - 0.25) * 0.45F, e.posY + e.height, e.posZ + (float) (Math.random() * 0.5 - 0.25) * 0.45F, (float) Math.random(), (float) Math.random(), (float) Math.random(), 0.15F + 0.05F * (float) Math.random(), -0.05F - 0.03F * (float) Math.random());
				}
			}
		}
		Botania.proxy.setWispFXDepthTest(true);
	}

	public void tickServer(ItemStack stack, EntityPlayer player) {
		StringBuilder positionsBuilder = new StringBuilder();

		scanForStack(player.getHeldItemMainhand(), player, positionsBuilder);
		scanForStack(player.getHeldItemOffhand(), player, positionsBuilder);

		String current = ItemNBTHelper.getString(stack, TAG_POSITIONS, "");
		String positions = positionsBuilder.toString();
		if(!current.equals(positions)) {
			ItemNBTHelper.setString(stack, TAG_POSITIONS, positions);
			PacketHandler.INSTANCE.sendToAll(new PacketSyncBauble(player, 0));
		}
	}

	private void scanForStack(ItemStack pstack, EntityPlayer player, StringBuilder positionsBuilder) {
		if(pstack != null || player.isSneaking()) {
			int range = 24;

			List<Entity> entities = player.worldObj.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(player.posX - range, player.posY - range, player.posZ - range, player.posX + range, player.posY + range, player.posZ + range));
			for(Entity e : entities) {
				if(e == player)
					continue;
				if(e.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
					if(scanInventory(e.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null), pstack))
						positionsBuilder.append(e.getEntityId()).append(";");
				} else if(e instanceof EntityItem) {
					EntityItem item = (EntityItem) e;
					ItemStack istack = item.getEntityItem();
					if(player.isSneaking() || istack.isItemEqual(pstack) && ItemStack.areItemStackTagsEqual(istack, pstack))
						positionsBuilder.append(item.getEntityId()).append(";");

				} else if(e instanceof IInventory) {
					IInventory inv = (IInventory) e;
					if(scanInventory(new InvWrapper(inv), pstack))
						positionsBuilder.append(e.getEntityId()).append(";");

				} else if(e instanceof EntityHorse) {
					EntityHorse horse = (EntityHorse) e;
					AnimalChest chest = ReflectionHelper.getPrivateValue(EntityHorse.class, horse, LibObfuscation.HORSE_CHEST);
					if(scanInventory(new InvWrapper(chest), pstack))
						positionsBuilder.append(horse.getEntityId()).append(";");

				} else if(e instanceof EntityPlayer) {
					EntityPlayer player_ = (EntityPlayer) e;
					IItemHandler playerInv = new InvWrapper(player_.inventory);
					IItemHandler binv = new InvWrapper(PlayerHandler.getPlayerBaubles(player_));
					if(scanInventory(playerInv, pstack) || scanInventory(binv, pstack))
						positionsBuilder.append(player_.getEntityId()).append(";");

				} else if(e instanceof EntityVillager) {
					EntityVillager villager = (EntityVillager) e;
					ArrayList<MerchantRecipe> recipes = villager.getRecipes(player);
					if(pstack != null && recipes != null)
						for(MerchantRecipe recipe : recipes)
							if(recipe != null && !recipe.isRecipeDisabled() && (equalStacks(pstack, recipe.getItemToBuy()) || equalStacks(pstack, recipe.getItemToSell())))
								positionsBuilder.append(villager.getEntityId()).append(";");

				} else if(e instanceof EntityLivingBase) {
					EntityLivingBase living = (EntityLivingBase) e;
					ItemStack estack = living.getHeldItemMainhand();
					if(pstack != null && estack != null && equalStacks(estack, pstack))
						positionsBuilder.append(living.getEntityId()).append(";");
					estack = living.getHeldItemOffhand();
					if(pstack != null && estack != null && equalStacks(estack, pstack))
						positionsBuilder.append(living.getEntityId()).append(";");
				}
			}

			if(pstack != null) {
				range = 12;
				BlockPos pos = new BlockPos(player);
				for(int i = -range; i < range + 1; i++)
					for(int j = -range; j < range + 1; j++)
						for(int k = -range; k < range + 1; k++) {
							BlockPos pos_ = pos.add(i, j, k);
							TileEntity tile = player.worldObj.getTileEntity(pos_);
							if(tile != null) {
								boolean foundCap = false;
								for(EnumFacing e : EnumFacing.VALUES) {
									if(tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, e)
											&& scanInventory(tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, e), pstack)) {
										positionsBuilder.append(pos_.getX()).append(",").append(pos_.getY()).append(",").append(pos_.getZ()).append(";");
										foundCap = true;
										break;
									}
								}
								if(!foundCap && tile instanceof IInventory) {
									IInventory inv = (IInventory) tile;
									if(scanInventory(new InvWrapper(inv), pstack))
										positionsBuilder.append(pos_.getX()).append(",").append(pos_.getY()).append(",").append(pos_.getZ()).append(";");
								}
							}
						}
			}
		}
	}

	private boolean equalStacks(ItemStack stack1, ItemStack stack2) {
		return stack1.isItemEqual(stack2) && ItemStack.areItemStackTagsEqual(stack1, stack2);
	}

	private boolean scanInventory(IItemHandler inv, ItemStack pstack) {
		if(pstack == null)
			return false;

		for(int l = 0; l < inv.getSlots(); l++) {
			ItemStack istack = inv.getStackInSlot(l);
			if(istack != null && equalStacks(istack, pstack))
				return true;
		}

		return false;
	}

	@Override
	public BaubleType getBaubleType(ItemStack arg0) {
		return BaubleType.AMULET;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onPlayerBaubleRender(ItemStack stack, EntityPlayer player, RenderType type, float partialTicks) {
		TextureAtlasSprite gemIcon = MiscellaneousIcons.INSTANCE.itemFinderGem;
		if(type == RenderType.HEAD) {
			float f = gemIcon.getMinU();
			float f1 = gemIcon.getMaxU();
			float f2 = gemIcon.getMinV();
			float f3 = gemIcon.getMaxV();
			boolean armor = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD) != null;
			Helper.translateToHeadLevel(player);
			Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
			GlStateManager.rotate(90F, 0F, 1F, 0F);
			GlStateManager.rotate(180F, 1F, 0F, 0F);
			GlStateManager.translate(-0.4F, -1.4F, armor ? -0.3F : -0.25F);
			GlStateManager.scale(0.75F, 0.75F, 0.75F);
			IconHelper.renderIconIn3D(Tessellator.getInstance(), f1, f2, f, f3, gemIcon.getIconWidth(), gemIcon.getIconHeight(), 1F / 16F);
		}
	}

}
