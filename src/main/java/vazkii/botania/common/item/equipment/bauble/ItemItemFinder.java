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
import baubles.api.BaublesApi;
import gnu.trove.list.array.TIntArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.MerchantRecipe;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.item.IBaubleRender;
import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.lib.LibItemNames;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemItemFinder extends ItemBauble implements IBaubleRender {

	private static final String TAG_ENTITY_POSITIONS = "highlightPositionsEnt";
	private static final String TAG_BLOCK_POSITIONS = "highlightPositionsBlock1";

	public ItemItemFinder() {
		super(LibItemNames.ITEM_FINDER);
	}

	@Override
	public void onWornTick(ItemStack stack, EntityLivingBase player) {
		super.onWornTick(stack, player);

		if(!(player instanceof EntityPlayer))
			return;

		if(player.world.isRemote)
			tickClient(stack, (EntityPlayer) player);
		else tickServer(stack, (EntityPlayer) player);
	}

	protected void tickClient(ItemStack stack, EntityPlayer player) {
		if(!Botania.proxy.isTheClientPlayer(player))
			return;

		NBTTagList blocks = ItemNBTHelper.getList(stack, TAG_BLOCK_POSITIONS, Constants.NBT.TAG_LONG, false);
		Botania.proxy.setWispFXDepthTest(false);

		for(int i = 0; i < blocks.tagCount(); i++) {
			BlockPos pos = BlockPos.fromLong(((NBTTagLong) blocks.get(i)).getLong());
			float m = 0.02F;
			Botania.proxy.wispFX(pos.getX() + (float) Math.random(), pos.getY() + (float) Math.random(), pos.getZ() + (float) Math.random(), (float) Math.random(), (float) Math.random(), (float) Math.random(), 0.15F + 0.05F * (float) Math.random(), m * (float) (Math.random() - 0.5), m * (float) (Math.random() - 0.5), m * (float) (Math.random() - 0.5));
		}

		int[] entities = ItemNBTHelper.getIntArray(stack, TAG_ENTITY_POSITIONS);
		for(int i : entities) {
			Entity e =  player.world.getEntityByID(i);
			if(e != null && Math.random() < 0.6) {
				Botania.proxy.setWispFXDepthTest(Math.random() < 0.6);
				Botania.proxy.wispFX(e.posX + (float) (Math.random() * 0.5 - 0.25) * 0.45F, e.posY + e.height, e.posZ + (float) (Math.random() * 0.5 - 0.25) * 0.45F, (float) Math.random(), (float) Math.random(), (float) Math.random(), 0.15F + 0.05F * (float) Math.random(), -0.05F - 0.03F * (float) Math.random());
			}
		}

		Botania.proxy.setWispFXDepthTest(true);
	}

	protected void tickServer(ItemStack stack, EntityPlayer player) {
		TIntArrayList entPosBuilder = new TIntArrayList();
		NBTTagList blockPosBuilder = new NBTTagList();

		scanForStack(player.getHeldItemMainhand(), player, entPosBuilder, blockPosBuilder);
		scanForStack(player.getHeldItemOffhand(), player, entPosBuilder, blockPosBuilder);

		int[] currentEnts = entPosBuilder.toArray();

		boolean entsEqual = Arrays.equals(currentEnts, ItemNBTHelper.getIntArray(stack, TAG_ENTITY_POSITIONS));
		boolean blocksEqual = blockPosBuilder.equals(ItemNBTHelper.getList(stack, TAG_BLOCK_POSITIONS, Constants.NBT.TAG_LONG, false));

		if(!entsEqual)
			ItemNBTHelper.setIntArray(stack, TAG_ENTITY_POSITIONS, currentEnts);
		if(!blocksEqual)
			ItemNBTHelper.setList(stack, TAG_BLOCK_POSITIONS, blockPosBuilder);
		if(!entsEqual || !blocksEqual)
			BotaniaAPI.internalHandler.sendBaubleUpdatePacket(player, 4);
	}

	private void scanForStack(ItemStack pstack, EntityPlayer player, TIntArrayList entIdBuilder, NBTTagList blockPosBuilder) {
		if(!pstack.isEmpty() || player.isSneaking()) {
			int range = 24;

			List<Entity> entities = player.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(player.posX - range, player.posY - range, player.posZ - range, player.posX + range, player.posY + range, player.posZ + range));
			for(Entity e : entities) {
				if(e == player)
					continue;
				if(e.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null) && !(e instanceof EntityPlayer)) {
					if(scanInventory(e.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null), pstack))
						entIdBuilder.add(e.getEntityId());

				} else if(e instanceof EntityItem) {
					EntityItem item = (EntityItem) e;
					ItemStack istack = item.getItem();
					if(player.isSneaking() || istack.isItemEqual(pstack) && ItemStack.areItemStackTagsEqual(istack, pstack))
						entIdBuilder.add(item.getEntityId());

				} else if(e instanceof IInventory) {
					IInventory inv = (IInventory) e;
					if(scanInventory(new InvWrapper(inv), pstack))
						entIdBuilder.add(e.getEntityId());

				} else if(e instanceof EntityPlayer) {
					EntityPlayer player_ = (EntityPlayer) e;
					IItemHandler playerInv = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
					IItemHandler binv = BaublesApi.getBaublesHandler(player);
					if(scanInventory(binv, pstack) || scanInventory(playerInv, pstack))
						entIdBuilder.add(player_.getEntityId());

				} else if(e instanceof EntityVillager) {
					EntityVillager villager = (EntityVillager) e;
					ArrayList<MerchantRecipe> recipes = villager.getRecipes(player);
					if(!pstack.isEmpty() && recipes != null)
						for(MerchantRecipe recipe : recipes)
							if(recipe != null && !recipe.isRecipeDisabled() && (equalStacks(pstack, recipe.getItemToBuy()) || equalStacks(pstack, recipe.getItemToSell()))) {
								entIdBuilder.add(villager.getEntityId());
								break;
							}

				}
			}

			if(!pstack.isEmpty()) {
				range = 12;
				BlockPos pos = new BlockPos(player);
				for(BlockPos pos_ : BlockPos.getAllInBoxMutable(pos.add(-range, -range, -range), pos.add(range + 1, range + 1, range + 1))) {
					TileEntity tile = player.world.getTileEntity(pos_);
					if(tile != null) {
						boolean foundCap = false;
						for(EnumFacing e : EnumFacing.VALUES) {
							if(tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, e)
									&& scanInventory(tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, e), pstack)) {
								blockPosBuilder.appendTag(new NBTTagLong(pos_.toLong()));
								foundCap = true;
								break;
							}
						}
						if(!foundCap && tile instanceof IInventory) {
							IInventory inv = (IInventory) tile;
							if(scanInventory(new InvWrapper(inv), pstack))
								blockPosBuilder.appendTag(new NBTTagLong(pos_.toLong()));
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
		if(pstack.isEmpty() || inv == null)
			return false;

		for(int l = 0; l < inv.getSlots(); l++) {
			ItemStack istack = inv.getStackInSlot(l);
			if(!istack.isEmpty() && equalStacks(istack, pstack))
				return true;
		}

		return false;
	}

	@Override
	public BaubleType getBaubleType(ItemStack arg0) {
		return BaubleType.HEAD;
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
			boolean armor = !player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty();
			Helper.translateToHeadLevel(player);
			Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			GlStateManager.rotate(90F, 0F, 1F, 0F);
			GlStateManager.rotate(180F, 1F, 0F, 0F);
			GlStateManager.translate(-0.4F, -1.4F, armor ? -0.3F : -0.25F);
			GlStateManager.scale(0.75F, 0.75F, 0.75F);
			IconHelper.renderIconIn3D(Tessellator.getInstance(), f1, f2, f, f3, gemIcon.getIconWidth(), gemIcon.getIconHeight(), 1F / 16F);
		}
	}

}
