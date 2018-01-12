/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Feb 2, 2014, 6:31:19 PM (GMT)]
 */
package vazkii.botania.common.block.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import org.lwjgl.opengl.GL11;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.mana.IManaReceiver;
import vazkii.botania.api.recipe.RecipeRuneAltar;
import vazkii.botania.client.core.handler.HUDHandler;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.network.PacketBotaniaEffect;
import vazkii.botania.common.network.PacketHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TileRuneAltar extends TileSimpleInventory implements IManaReceiver, ITickable {

	private static final String TAG_MANA = "mana";
	private static final String TAG_MANA_TO_GET = "manaToGet";
	private static final int SET_KEEP_TICKS_EVENT = 0;
	private static final int SET_COOLDOWN_EVENT = 1;
	private static final int CRAFT_EFFECT_EVENT = 2;

	RecipeRuneAltar currentRecipe;

	public int manaToGet = 0;
	int mana = 0;
	int cooldown = 0;
	public int signal = 0;

	List<ItemStack> lastRecipe = null;
	int recipeKeepTicks = 0;

	public boolean addItem(@Nullable EntityPlayer player, ItemStack stack, @Nullable EnumHand hand) {
		if(cooldown > 0 || stack.getItem() == ModItems.twigWand || stack.getItem() == ModItems.lexicon)
			return false;

		if(stack.getItem() == Item.getItemFromBlock(ModBlocks.livingrock) && stack.getItemDamage() == 0) {
			if(!world.isRemote) {
				ItemStack toSpawn = player != null && player.capabilities.isCreativeMode ? stack.copy().splitStack(1) : stack.splitStack(1);
				EntityItem item = new EntityItem(world, getPos().getX() + 0.5, getPos().getY() + 1, getPos().getZ() + 0.5, toSpawn);
				item.setPickupDelay(40);
				item.motionX = item.motionY = item.motionZ = 0;
				world.spawnEntity(item);
			}

			return true;
		}

		if(manaToGet != 0)
			return false;

		boolean did = false;

		for(int i = 0; i < getSizeInventory(); i++)
			if(itemHandler.getStackInSlot(i).isEmpty()) {
				did = true;
				ItemStack stackToAdd = stack.copy();
				stackToAdd.setCount(1);
				itemHandler.setStackInSlot(i, stackToAdd);

				if(player == null || !player.capabilities.isCreativeMode) {
					stack.shrink(1);
				}

				break;
			}

		if(did)
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(world, pos);

		return true;
	}

	@Override
	public boolean receiveClientEvent(int id, int param) {
		switch (id) {
			case SET_KEEP_TICKS_EVENT: recipeKeepTicks = param; return true;
			case SET_COOLDOWN_EVENT: cooldown = param; return true;
			case CRAFT_EFFECT_EVENT: {
				if(world.isRemote) {
					for(int i = 0; i < 25; i++) {
						float red = (float) Math.random();
						float green = (float) Math.random();
						float blue = (float) Math.random();
						Botania.proxy.sparkleFX(pos.getX() + 0.5 + Math.random() * 0.4 - 0.2, pos.getY() + 1, pos.getZ() + 0.5 + Math.random() * 0.4 - 0.2, red, green, blue, (float) Math.random(), 10);
					}
					world.playSound(pos.getX(), pos.getY(), pos.getZ(), ModSounds.runeAltarCraft, SoundCategory.BLOCKS, 1, 1, false);
				}
				return true;
			}
			default: return super.receiveClientEvent(id, param);
		}
	}

	@Override
	public void update() {

		// Update every tick.
		recieveMana(0);

		if(!world.isRemote) {
			if(manaToGet == 0) {
				List<EntityItem> items = world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos, pos.add(1, 1, 1)));
				for(EntityItem item : items)
					if(!item.isDead && !item.getItem().isEmpty() && item.getItem().getItem() != Item.getItemFromBlock(ModBlocks.livingrock)) {
						ItemStack stack = item.getItem();
						addItem(null, stack, null);
					}
			}

			int newSignal = 0;
			if(manaToGet > 0) {
				newSignal++;
				if(mana >= manaToGet)
					newSignal++;
			}

			if(newSignal != signal) {
				signal = newSignal;
				world.updateComparatorOutputLevel(pos, world.getBlockState(pos).getBlock());
			}

			updateRecipe();
		} else {
			if (manaToGet > 0 && mana >= manaToGet && world.rand.nextInt(20) == 0) {
				Vector3 vec = Vector3.fromTileEntityCenter(this);
				Vector3 endVec = vec.add(0, 2.5, 0);
				Botania.proxy.lightningFX(vec, endVec, 2F, 0x00948B, 0x00E4D7);
			}

			if (cooldown > 0)
				Botania.proxy.wispFX(pos.getX() + Math.random(), pos.getY() + 0.8, pos.getZ() + Math.random(), 0.2F, 0.2F, 0.2F, 0.2F, -0.025F);
		}

		if(cooldown > 0) {
			cooldown--;
		}

		if(recipeKeepTicks > 0)
			--recipeKeepTicks;
		else lastRecipe = null;
	}

	public void updateRecipe() {
		int manaToGet = this.manaToGet;

		getMana : {
			if(currentRecipe != null)
				this.manaToGet = currentRecipe.getManaUsage();
			else {
				for(RecipeRuneAltar recipe : BotaniaAPI.runeAltarRecipes)
					if(recipe.matches(itemHandler)) {
						this.manaToGet = recipe.getManaUsage();
						break getMana;
					}
				this.manaToGet = 0;
			}
		}

		if(manaToGet != this.manaToGet) {
			world.playSound(null, pos, ModSounds.runeAltarStart, SoundCategory.BLOCKS, 1, 1);
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(world, pos);
		}
	}

	public void saveLastRecipe() {
		lastRecipe = new ArrayList<>();
		for(int i = 0; i < getSizeInventory(); i++) {
			ItemStack stack = itemHandler.getStackInSlot(i);
			if(stack.isEmpty())
				break;
			lastRecipe.add(stack.copy());
		}
		recipeKeepTicks = 400;
		world.addBlockEvent(getPos(), ModBlocks.runeAltar, SET_KEEP_TICKS_EVENT, 400);
	}

	public void trySetLastRecipe(EntityPlayer player) {
		TileAltar.tryToSetLastRecipe(player, itemHandler, lastRecipe);
		if(!isEmpty())
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(world, pos);
	}

	public boolean hasValidRecipe() {
		for(RecipeRuneAltar recipe : BotaniaAPI.runeAltarRecipes)
			if(recipe.matches(itemHandler))
				return true;

		return false;
	}

	public void onWanded(EntityPlayer player, ItemStack wand) {
		if (world.isRemote)
			return;

		RecipeRuneAltar recipe = null;

		if(currentRecipe != null)
			recipe = currentRecipe;
		else for(RecipeRuneAltar recipe_ : BotaniaAPI.runeAltarRecipes) {
			if(recipe_.matches(itemHandler)) {
				recipe = recipe_;
				break;
			}
		}

		if(manaToGet > 0 && mana >= manaToGet) {
			List<EntityItem> items = world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos, pos.add(1, 1, 1)));
			EntityItem livingrock = null;
			for(EntityItem item : items)
				if(!item.isDead && !item.getItem().isEmpty() && item.getItem().getItem() == Item.getItemFromBlock(ModBlocks.livingrock)) {
					livingrock = item;
					break;
				}

			if(livingrock != null) {
				int mana = recipe.getManaUsage();
				recieveMana(-mana);
				ItemStack output = recipe.getOutput().copy();
				EntityItem outputItem = new EntityItem(world, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, output);
				world.spawnEntity(outputItem);
				currentRecipe = null;
				world.addBlockEvent(getPos(), ModBlocks.runeAltar, SET_COOLDOWN_EVENT, 60);
				world.addBlockEvent(getPos(), ModBlocks.runeAltar, CRAFT_EFFECT_EVENT, 0);

				saveLastRecipe();
				for(int i = 0; i < getSizeInventory(); i++) {
					ItemStack stack = itemHandler.getStackInSlot(i);
					if(!stack.isEmpty()) {
						if(stack.getItem() == ModItems.rune && (player == null || !player.capabilities.isCreativeMode)) {
							EntityItem outputRune = new EntityItem(world, getPos().getX() + 0.5, getPos().getY() + 1.5, getPos().getZ() + 0.5, stack.copy());
							world.spawnEntity(outputRune);
						}

						itemHandler.setStackInSlot(i, ItemStack.EMPTY);
					}
				}

				livingrock.getItem().shrink(1);
			}
		}
	}

	public boolean isEmpty() {
		for(int i = 0; i < getSizeInventory(); i++)
			if(!itemHandler.getStackInSlot(i).isEmpty())
				return false;

		return true;
	}

	@Override
	public void writePacketNBT(NBTTagCompound par1nbtTagCompound) {
		super.writePacketNBT(par1nbtTagCompound);

		par1nbtTagCompound.setInteger(TAG_MANA, mana);
		par1nbtTagCompound.setInteger(TAG_MANA_TO_GET, manaToGet);
	}

	@Override
	public void readPacketNBT(NBTTagCompound par1nbtTagCompound) {
		super.readPacketNBT(par1nbtTagCompound);

		mana = par1nbtTagCompound.getInteger(TAG_MANA);
		manaToGet = par1nbtTagCompound.getInteger(TAG_MANA_TO_GET);
	}

	@Override
	public int getSizeInventory() {
		return 16;
	}

	@Nonnull
	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return INFINITE_EXTENT_AABB;
	}

	@Override
	protected SimpleItemStackHandler createItemHandler() {
		return new SimpleItemStackHandler(this, false) {
			@Override
			protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
				return 1;
			}
		};
	}

	@Override
	public int getCurrentMana() {
		return mana;
	}

	@Override
	public boolean isFull() {
		return mana >= manaToGet;
	}

	@Override
	public void recieveMana(int mana) {
		this.mana = Math.min(this.mana + mana, manaToGet);
	}

	@Override
	public boolean canRecieveManaFromBursts() {
		return !isFull();
	}

	public void renderHUD(Minecraft mc, ScaledResolution res) {
		int xc = res.getScaledWidth() / 2;
		int yc = res.getScaledHeight() / 2;

		float angle = -90;
		int radius = 24;
		int amt = 0;
		for(int i = 0; i < getSizeInventory(); i++) {
			if(itemHandler.getStackInSlot(i).isEmpty())
				break;
			amt++;
		}

		if(amt > 0) {
			float anglePer = 360F / amt;
			for(RecipeRuneAltar recipe : BotaniaAPI.runeAltarRecipes)
				if(recipe.matches(itemHandler)) {
					GlStateManager.enableBlend();
					GlStateManager.enableRescaleNormal();
					GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

					recipe.getOutput();
					float progress = (float) mana / (float) manaToGet;

					mc.renderEngine.bindTexture(HUDHandler.manaBar);
					GlStateManager.color(1F, 1F, 1F, 1F);
					RenderHelper.drawTexturedModalRect(xc + radius + 9, yc - 8, 0, progress == 1F ? 0 : 22, 8, 22, 15);

					net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
					if(progress == 1F) {
						mc.getRenderItem().renderItemIntoGUI(new ItemStack(ModBlocks.livingrock), xc + radius + 16, yc + 8);
						GlStateManager.translate(0F, 0F, 100F);
						mc.getRenderItem().renderItemIntoGUI(new ItemStack(ModItems.twigWand), xc + radius + 24, yc + 8);
						GlStateManager.translate(0F, 0F, -100F);
					}

					RenderHelper.renderProgressPie(xc + radius + 32, yc - 8, progress, recipe.getOutput());
					net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();

					if(progress == 1F)
						mc.fontRenderer.drawStringWithShadow("+", xc + radius + 14, yc + 12, 0xFFFFFF);
				}

			net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
			for(int i = 0; i < amt; i++) {
				double xPos = xc + Math.cos(angle * Math.PI / 180D) * radius - 8;
				double yPos = yc + Math.sin(angle * Math.PI / 180D) * radius - 8;
				GlStateManager.translate(xPos, yPos, 0);
				mc.getRenderItem().renderItemIntoGUI(itemHandler.getStackInSlot(i), 0, 0);
				GlStateManager.translate(-xPos, -yPos, 0);

				angle += anglePer;
			}
			net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
		} else if(recipeKeepTicks > 0) {
			String s = I18n.format("botaniamisc.altarRefill0");
			mc.fontRenderer.drawStringWithShadow(s, xc - mc.fontRenderer.getStringWidth(s) / 2, yc + 10, 0xFFFFFF);
			s = I18n.format("botaniamisc.altarRefill1");
			mc.fontRenderer.drawStringWithShadow(s, xc - mc.fontRenderer.getStringWidth(s) / 2, yc + 20, 0xFFFFFF);
		}
	}

	public int getTargetMana() {
		return manaToGet;
	}

}
