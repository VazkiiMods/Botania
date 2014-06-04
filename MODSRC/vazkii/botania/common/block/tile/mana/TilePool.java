/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 26, 2014, 12:23:55 AM (GMT)]
 */
package vazkii.botania.common.block.tile.mana;

import java.awt.Color;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.mana.IManaItem;
import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.api.mana.ManaNetworkEvent;
import vazkii.botania.api.recipe.RecipeManaInfusion;
import vazkii.botania.client.core.handler.HUDHandler;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileMod;

public class TilePool extends TileMod implements IManaPool {

	public static final int MAX_MANA = 1000000;

	private static final String TAG_MANA = "mana";
	private static final String TAG_KNOWN_MANA = "knownMana";
	private static final String TAG_OUTPUTTING = "outputting";

	boolean outputting = false;
	public boolean alchemy = false;

	int mana;
	int knownMana = -1;
	int craftCooldown = 20;
	boolean added = false;

	@Override
	public boolean isFull() {
		Block blockBelow = worldObj.getBlock(xCoord, yCoord - 1, zCoord);
		return blockBelow != ModBlocks.manaVoid && getCurrentMana() >= MAX_MANA;
	}

	@Override
	public void recieveMana(int mana) {
		boolean full = getCurrentMana() >= MAX_MANA;

		this.mana = Math.min(getCurrentMana() + mana, MAX_MANA);
		if(!full)
			worldObj.func_147453_f(xCoord, yCoord, zCoord, worldObj.getBlock(xCoord, yCoord, zCoord));
	}

	@Override
	public void invalidate() {
		super.invalidate();
		ManaNetworkEvent.removePool(this);
	}

	@Override
	public void onChunkUnload() {
		super.onChunkUnload();
		ManaNetworkEvent.removePool(this);
	}

	public boolean collideEntityItem(EntityItem item) {
		if(craftCooldown > 0 || item.isDead)
			return false;

		boolean didChange = false;
		ItemStack stack = item.getEntityItem();
		if(stack == null)
			return false;

		for(RecipeManaInfusion recipe : BotaniaAPI.manaInfusionRecipes) {
			if(recipe.matches(stack) && (!recipe.isAlchemy() || alchemy)) {
				int mana = recipe.getManaToConsume();
				if(getCurrentMana() >= mana) {
					recieveMana(-mana);

					if(!worldObj.isRemote) {
						stack.stackSize--;
						if(stack.stackSize == 0)
							item.setDead();

						ItemStack output = recipe.getOutput().copy();
						EntityItem outputItem = new EntityItem(worldObj, xCoord + 0.5, yCoord + 1.5, zCoord + 0.5, output);
						outputItem.age = 55;
						worldObj.spawnEntityInWorld(outputItem);
					}

					craftCooldown = 20;
					craftingFanciness();
					didChange = true;
				}

				break;
			}
		}

		return didChange;
	}

	public void craftingFanciness() {
		worldObj.playSoundEffect(xCoord, yCoord, zCoord, "random.levelup", 0.5F, 4F);
		for(int i = 0; i < 25; i++) {
			float red = (float) Math.random();
			float green = (float) Math.random();
			float blue = (float) Math.random();
			Botania.proxy.sparkleFX(worldObj, xCoord + 0.5 + Math.random() * 0.4 - 0.2, yCoord + 1, zCoord + 0.5 + Math.random() * 0.4 - 0.2, red, green, blue, (float) Math.random(), 10);
		}
	}

	@Override
	public void updateEntity() {
		if(!added) {
			ManaNetworkEvent.addPool(this);
			added = true;
		}
		if(worldObj.isRemote) {
			double particleChance = 1F - (double) getCurrentMana() / (double) MAX_MANA * 0.1;
			Color color = new Color(0x00C6FF);
			if(Math.random() > particleChance)
				Botania.proxy.wispFX(worldObj, xCoord + 0.3 + Math.random() * 0.5, yCoord + 0.6 + Math.random() * 0.25, zCoord + Math.random(), color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, (float) Math.random() / 3F, (float) -Math.random() / 25F);
		}

		alchemy = worldObj.getBlock(xCoord, yCoord - 1, zCoord) == ModBlocks.alchemyCatalyst;

		if(craftCooldown > 0)
			craftCooldown--;

		List<EntityItem> items = worldObj.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1));
		for(EntityItem item : items) {
			if(item.isDead)
				continue;
			
			ItemStack stack = item.getEntityItem();
			if(stack != null && stack.getItem() instanceof IManaItem) {
				IManaItem mana = (IManaItem) stack.getItem();
				if(outputting && mana.canReceiveManaFromPool(stack, this) || !outputting && mana.canExportManaToPool(stack, this)) {
					boolean didSomething = false;

					if(outputting) {
						if(getCurrentMana() > 0)
							didSomething = true;

						if(!worldObj.isRemote) {
							int manaVal = Math.min(1000, Math.min(getCurrentMana(), mana.getMaxMana(stack) - mana.getMana(stack)));
							mana.addMana(stack, manaVal);
							recieveMana(-manaVal);
						}
					} else {
						if(mana.getMana(stack) > 0)
							didSomething = true;

						if(!worldObj.isRemote) {
							int manaVal = Math.min(1000, Math.min(MAX_MANA - getCurrentMana(), mana.getMana(stack)));
							mana.addMana(stack, -manaVal);
							recieveMana(manaVal);
						}
					}

					if(didSomething) {
						worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
						if(worldObj.isRemote) {
							Color color = new Color(0x00C6FF);
							Botania.proxy.wispFX(worldObj, item.posX + Math.random() * 0.5 - 0.25, item.posY + Math.random() * 0.5 - (outputting ? 0.65 : 0.25), item.posZ + Math.random() * 0.5 - 0.25, color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, (float) Math.random() / 15F, (outputting ? -1F : 1) * (float) Math.random() / 25F);
						}
					}
				}
			}
		}
	}

	@Override
	public void writeCustomNBT(NBTTagCompound cmp) {
		cmp.setInteger(TAG_MANA, mana);
		cmp.setBoolean(TAG_OUTPUTTING, outputting);
	}

	@Override
	public void readCustomNBT(NBTTagCompound cmp) {
		mana = cmp.getInteger(TAG_MANA);
		outputting = cmp.getBoolean(TAG_OUTPUTTING);

		if(cmp.hasKey(TAG_KNOWN_MANA))
			knownMana = cmp.getInteger(TAG_KNOWN_MANA);
	}

	public void onWanded(EntityPlayer player, ItemStack wand) {
		if(player == null)
			return;

		if(player.isSneaking()) {
			outputting = !outputting;
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}

		if(!worldObj.isRemote) {
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			writeCustomNBT(nbttagcompound);
			nbttagcompound.setInteger(TAG_KNOWN_MANA, getCurrentMana());
			if(player instanceof EntityPlayerMP)
				((EntityPlayerMP) player).playerNetServerHandler.sendPacket(new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, -999, nbttagcompound));
		}

		worldObj.playSoundAtEntity(player, "random.orb", 0.11F, 1F);
	}

	public void renderHUD(Minecraft mc, ScaledResolution res) {
		String name = StatCollector.translateToLocal(new ItemStack(ModBlocks.pool, 1, getBlockMetadata()).getUnlocalizedName().replaceAll("tile.", "tile." + LibResources.PREFIX_MOD) + ".name");
		int color = 0x660000FF;
		HUDHandler.drawSimpleManaHUD(color, knownMana, MAX_MANA, name, res);

		String power = StatCollector.translateToLocal("botaniamisc." + (outputting ? "outputtingPower" : "inputtingPower"));
		int x = res.getScaledWidth() / 2 - mc.fontRenderer.getStringWidth(power) / 2;
		int y = res.getScaledHeight() / 2 + 30;
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		mc.fontRenderer.drawStringWithShadow(power, x, y, color);
		GL11.glDisable(GL11.GL_BLEND);
	}

	@Override
	public boolean canRecieveManaFromBursts() {
		return true;
	}

	@Override
	public boolean isOutputtingPower() {
		return outputting;
	}

	@Override
	public int getCurrentMana() {
		return worldObj != null && getBlockMetadata() == 1 ? MAX_MANA : mana;
	}
}
