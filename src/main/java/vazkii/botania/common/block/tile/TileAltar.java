/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 21, 2014, 7:51:36 PM (GMT)]
 */
package vazkii.botania.common.block.tile;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.item.IPetalApothecary;
import vazkii.botania.api.recipe.IFlowerComponent;
import vazkii.botania.api.recipe.RecipePetals;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.AltarVariant;
import vazkii.botania.client.core.handler.HUDHandler;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.common.Botania;
import vazkii.botania.common.lib.LibBlockNames;

public class TileAltar extends TileSimpleInventory implements ISidedInventory, IPetalApothecary {

	private static final Pattern SEED_PATTERN = Pattern.compile("(?:(?:(?:[A-Z-_.:]|^)seed)|(?:(?:[a-z-_.:]|^)Seed))(?:[sA-Z-_.:]|$)");

	public static final String TAG_HAS_WATER = "hasWater";
	public static final String TAG_HAS_LAVA = "hasLava";
	public static final String TAG_IS_MOSSY = "isMossy";

	public boolean hasWater = false;
	public boolean hasLava = false;

	public boolean isMossy = false;

	List<ItemStack> lastRecipe = null;
	int recipeKeepTicks = 0;

	public boolean collideEntityItem(EntityItem item) {
		ItemStack stack = item.getEntityItem();
		if(stack == null || item.isDead)
			return false;

		if(!isMossy && worldObj.getBlockState(getPos()).getValue(BotaniaStateProps.ALTAR_VARIANT) == AltarVariant.DEFAULT) {
			if(stack.getItem() == Item.getItemFromBlock(Blocks.vine)) {
				isMossy = true;
				if (worldObj.isRemote) {
					worldObj.markBlockRangeForRenderUpdate(getPos(), getPos());
				} else {
					worldObj.updateComparatorOutputLevel(pos, worldObj.getBlockState(pos).getBlock());
					stack.stackSize--;
					if(stack.stackSize == 0)
						item.setDead();
					VanillaPacketDispatcher.dispatchTEToNearbyPlayers(worldObj, pos);
				}
			}
		}

		if(!hasWater() && !hasLava()) {
			if(stack.getItem() == Items.water_bucket && !worldObj.isRemote) {
				setWater(true);
				worldObj.updateComparatorOutputLevel(pos, worldObj.getBlockState(pos).getBlock());
				stack.setItem(Items.bucket);
			} else if(stack.getItem() == Items.lava_bucket && !worldObj.isRemote) {
				setLava(true);
				worldObj.updateComparatorOutputLevel(pos, worldObj.getBlockState(pos).getBlock());
				stack.setItem(Items.bucket);
			} else return false;
		}

		if(hasLava()) {
			item.setFire(100);
			return true;
		}

		boolean didChange = false;

		if(stack.getItem() instanceof IFlowerComponent) {
			if(getStackInSlot(getSizeInventory() - 1) != null)
				return false;

			if(!worldObj.isRemote) {
				stack.stackSize--;
				if(stack.stackSize == 0)
					item.setDead();

				for(int i = 0; i < getSizeInventory(); i++)
					if(getStackInSlot(i) == null) {
						ItemStack stackToPut = stack.copy();
						stackToPut.stackSize = 1;
						setInventorySlotContents(i, stackToPut);
						didChange = true;
						worldObj.playSoundAtEntity(item, "game.neutral.swim.splash", 0.1F, 1F);
						break;
					}
			}
		} else if(stack.getItem() != null && SEED_PATTERN.matcher(stack.getItem().getUnlocalizedName(stack)).find()) {
			for(RecipePetals recipe : BotaniaAPI.petalRecipes) {
				if(recipe.matches(this)) {
					saveLastRecipe();

					if(!worldObj.isRemote) {
						for(int i = 0; i < getSizeInventory(); i++)
							setInventorySlotContents(i, null);

						stack.stackSize--;
						if(stack.stackSize == 0)
							item.setDead();

						ItemStack output = recipe.getOutput().copy();
						EntityItem outputItem = new EntityItem(worldObj, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, output);
						worldObj.spawnEntityInWorld(outputItem);

						setWater(false);
						worldObj.updateComparatorOutputLevel(pos, worldObj.getBlockState(pos).getBlock());
					}

					craftingFanciness();
					didChange = true;

					break;
				}
			}
		}

		return didChange;
	}

	public void saveLastRecipe() {
		lastRecipe = new ArrayList();
		for(int i = 0; i < getSizeInventory(); i++) {
			ItemStack stack = getStackInSlot(i);
			if(stack == null)
				break;
			lastRecipe.add(stack.copy());
		}
		recipeKeepTicks = 400;
	}

	public void trySetLastRecipe(EntityPlayer player) {
		tryToSetLastRecipe(player, this, lastRecipe);
		if(!isEmpty())
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(worldObj, pos);
	}

	public static void tryToSetLastRecipe(EntityPlayer player, IInventory inv, List<ItemStack> lastRecipe) {
		if(lastRecipe == null || lastRecipe.isEmpty() || player.worldObj.isRemote)
			return;

		int index = 0;
		boolean didAny = false;
		for(ItemStack stack : lastRecipe) {
			if(stack == null)
				continue;

			for(int i = 0; i < player.inventory.getSizeInventory(); i++) {
				ItemStack pstack = player.inventory.getStackInSlot(i);
				if(pstack != null && pstack.isItemEqual(stack) && ItemStack.areItemStackTagsEqual(stack, pstack)) {
					pstack.stackSize--;
					if(pstack.stackSize == 0)
						player.inventory.setInventorySlotContents(i, null);

					ItemStack stackToPut = pstack.copy();
					stackToPut.stackSize = 1;
					inv.setInventorySlotContents(index, stackToPut);
					didAny = true;
					index++;
					break;
				}
			}
		}

		if(didAny) {
			if(inv instanceof TileAltar)
				player.worldObj.playSoundAtEntity(player, "game.neutral.swim.splash", 0.1F, 1F);
			if(player instanceof EntityPlayerMP) {
				EntityPlayerMP mp = (EntityPlayerMP) player;
				mp.inventoryContainer.detectAndSendChanges();
			}
		}
	}

	public void craftingFanciness() {
		worldObj.playSoundEffect(pos.getX(), pos.getY(), pos.getZ(), "botania:altarCraft", 1F, 1F);
		for(int i = 0; i < 25; i++) {
			float red = (float) Math.random();
			float green = (float) Math.random();
			float blue = (float) Math.random();
			Botania.proxy.sparkleFX(worldObj, pos.getX() + 0.5 + Math.random() * 0.4 - 0.2, pos.getY() + 1, pos.getZ() + 0.5 + Math.random() * 0.4 - 0.2, red, green, blue, (float) Math.random(), 10);
		}
	}

	public boolean isEmpty() {
		for(int i = 0; i < getSizeInventory(); i++)
			if(getStackInSlot(i) != null)
				return false;

		return true;
	}

	@Override
	public void updateEntity() {
		List<EntityItem> items = worldObj.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.add(0, 1D / 16D * 20D, 0), pos.add(1, 1D / 16D * 21D,1)));

		boolean didChange = false;

		for(EntityItem item : items)
			didChange = collideEntityItem(item) || didChange;

		if(didChange)
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(worldObj, pos);

		for(int i = 0; i < getSizeInventory(); i++) {
			ItemStack stackAt = getStackInSlot(i);
			if(stackAt == null)
				break;

			if(Math.random() >= 0.97) {
				Color color = new Color(((IFlowerComponent) stackAt.getItem()).getParticleColor(stackAt));
				float red = color.getRed() / 255F;
				float green = color.getGreen() / 255F;
				float blue = color.getBlue() / 255F;
				if(Math.random() >= 0.75F)
					worldObj.playSoundEffect(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, "game.neutral.swim.splash", 0.1F, 10F);
				Botania.proxy.sparkleFX(worldObj, pos.getX() + 0.5 + Math.random() * 0.4 - 0.2, pos.getY() + 1, pos.getZ() + 0.5 + Math.random() * 0.4 - 0.2, red, green, blue, (float) Math.random(), 10);
			}
		}

		if(hasLava()) {
			isMossy = false;
			worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.getX() + 0.5 + Math.random() * 0.4 - 0.2, pos.getY() + 1, pos.getZ() + 0.5 + Math.random() * 0.4 - 0.2, 0, 0.05, 0);
			if(Math.random() > 0.9)
				worldObj.spawnParticle(EnumParticleTypes.LAVA, pos.getX() + 0.5 + Math.random() * 0.4 - 0.2, pos.getY() + 1, pos.getZ() + 0.5 + Math.random() * 0.4 - 0.2, 0, 0.01, 0);
		}

		if(recipeKeepTicks > 0)
			--recipeKeepTicks;
		else lastRecipe = null;
	}

	@Override
	public void writeCustomNBT(NBTTagCompound cmp) {
		super.writeCustomNBT(cmp);

		cmp.setBoolean(TAG_HAS_WATER, hasWater());
		cmp.setBoolean(TAG_HAS_LAVA, hasLava());
		cmp.setBoolean(TAG_IS_MOSSY, isMossy);
	}

	@Override
	public void readCustomNBT(NBTTagCompound cmp) {
		super.readCustomNBT(cmp);

		hasWater = cmp.getBoolean(TAG_HAS_WATER);
		hasLava = cmp.getBoolean(TAG_HAS_LAVA);
		isMossy = cmp.getBoolean(TAG_IS_MOSSY);
	}

	@Override
	public String getName() {
		return LibBlockNames.ALTAR;
	}

	@Override
	public int getSizeInventory() {
		return 16;
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return false;
	}

	@Override
	public int[] getSlotsForFace(EnumFacing var1) {
		return new int[0];
	}

	@Override
	public boolean canInsertItem(int i, ItemStack itemstack, EnumFacing j) {
		return false;
	}

	@Override
	public boolean canExtractItem(int i, ItemStack itemstack, EnumFacing j) {
		return false;
	}

	@Override
	public void setWater(boolean water) {
		hasWater = water;
		VanillaPacketDispatcher.dispatchTEToNearbyPlayers(worldObj, pos);
	}

	public void setLava(boolean lava) {
		hasLava = lava;
		VanillaPacketDispatcher.dispatchTEToNearbyPlayers(worldObj, pos);
	}

	@Override
	public boolean hasWater() {
		return hasWater;
	}

	public boolean hasLava() {
		return hasLava;
	}

	public void renderHUD(Minecraft mc, ScaledResolution res) {
		int xc = res.getScaledWidth() / 2;
		int yc = res.getScaledHeight() / 2;

		float angle = -90;
		int radius = 24;
		int amt = 0;
		for(int i = 0; i < getSizeInventory(); i++) {
			if(getStackInSlot(i) == null)
				break;
			amt++;
		}

		if(amt > 0) {
			float anglePer = 360F / amt;

			for(RecipePetals recipe : BotaniaAPI.petalRecipes)
				if(recipe.matches(this)) {
					GL11.glColor4f(1F, 1F, 1F, 1F);
					mc.renderEngine.bindTexture(HUDHandler.manaBar);
					RenderHelper.drawTexturedModalRect(xc + radius + 9, yc - 8, 0, 0, 8, 22, 15);

					ItemStack stack = recipe.getOutput();

					net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
					mc.getRenderItem().renderItemIntoGUI(stack, xc + radius + 32, yc - 8);
					mc.getRenderItem().renderItemIntoGUI(new ItemStack(Items.wheat_seeds), xc + radius + 16, yc + 6);
					net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
					mc.fontRendererObj.drawStringWithShadow("+", xc + radius + 14, yc + 10, 0xFFFFFF);
				}

			net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
			for(int i = 0; i < amt; i++) {
				double xPos = xc + Math.cos(angle * Math.PI / 180D) * radius - 8;
				double yPos = yc + Math.sin(angle * Math.PI / 180D) * radius - 8;
				GL11.glTranslated(xPos, yPos, 0);
				mc.getRenderItem().renderItemIntoGUI(getStackInSlot(i), 0, 0);
				GL11.glTranslated(-xPos, -yPos, 0);

				angle += anglePer;
			}
			net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
		} else if(recipeKeepTicks > 0 && hasWater) {
			String s = StatCollector.translateToLocal("botaniamisc.altarRefill0");
			mc.fontRendererObj.drawStringWithShadow(s, xc - mc.fontRendererObj.getStringWidth(s) / 2, yc + 10, 0xFFFFFF);
			s = StatCollector.translateToLocal("botaniamisc.altarRefill1");
			mc.fontRendererObj.drawStringWithShadow(s, xc - mc.fontRendererObj.getStringWidth(s) / 2, yc + 20, 0xFFFFFF);
		}
	}

}
