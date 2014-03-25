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
package vazkii.botania.common.block.tile;

import java.awt.Color;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.mana.IManaItem;
import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.api.mana.ManaNetworkEvent;
import vazkii.botania.api.recipe.RecipeManaInfusion;
import vazkii.botania.client.core.handler.HUDHandler;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class TilePool extends TileMod implements IManaPool {

	public static final int MAX_MANA = 1000000;

	private static final String TAG_MANA = "mana";
	private static final String TAG_KNOWN_MANA = "knownMana";
	private static final String TAG_OUTPUTTING = "outputting";

	boolean outputting = false;

	int mana;
	int knownMana = -1;
	boolean added = false;

	@Override
	public boolean isFull() {
		int idBelow = worldObj.getBlockId(xCoord, yCoord - 1, zCoord);
		return idBelow != ModBlocks.manaVoid.blockID && mana >= MAX_MANA;
	}

	@Override
	public void recieveMana(int mana) {
		boolean full = this.mana >= MAX_MANA;

		this.mana = Math.min(this.mana + mana, MAX_MANA);
		if(!full)
			worldObj.func_96440_m(xCoord, yCoord, zCoord, worldObj.getBlockId(xCoord, yCoord, zCoord));
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
		boolean didChange = false;
		ItemStack stack = item.getEntityItem();
		if(stack == null)
			return false;

		for(RecipeManaInfusion recipe : BotaniaAPI.manaInfusionRecipes) {
			if(recipe.matches(stack)) {
				int mana = recipe.getManaToConsume();
				if(getCurrentMana() >= mana) {
					recieveMana(-mana);

					if(!worldObj.isRemote) {
						stack.stackSize--;
						if(stack.stackSize == 0)
							item.setDead();

						ItemStack output = recipe.getOutput().copy();
						EntityItem outputItem = new EntityItem(worldObj, xCoord + 0.5, yCoord + 1.5, zCoord + 0.5, output);
						worldObj.spawnEntityInWorld(outputItem);
					}
					craftingFanciness();
					didChange = true;
				}

				break;
			}
		}

		return didChange;
	}

	public void craftingFanciness() {
		worldObj.playSoundEffect(xCoord, yCoord, zCoord, "random.levelup", 1F, 1F);
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

		List<EntityItem> items = worldObj.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1));
		for(EntityItem item : items) {
			ItemStack stack = item.getEntityItem();
			if(stack != null && stack.getItem() instanceof IManaItem) {
				IManaItem mana = (IManaItem) stack.getItem();
				if(outputting && mana.canReceiveManaFromPool(stack, this) || !outputting && mana.canExportManaToPool(stack, this)) {
					boolean didSomething = false;

					if(outputting) {
						if(this.mana > 0)
							didSomething = true;

						int manaVal = Math.min(1000, Math.min(this.mana, mana.getMaxMana(stack) - mana.getMana(stack)));
						mana.addMana(stack, manaVal);
						recieveMana(-manaVal);
					} else {
						if(mana.getMana(stack) > 0)
							didSomething = true;

						int manaVal = Math.min(1000, Math.min(MAX_MANA - this.mana, mana.getMana(stack)));
						mana.addMana(stack, -manaVal);
						recieveMana(manaVal);
					}

					if(didSomething) {
						PacketDispatcher.sendPacketToAllInDimension(getDescriptionPacket(), worldObj.provider.dimensionId);
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
		if(player.isSneaking()) {
			outputting = !outputting;
			PacketDispatcher.sendPacketToAllInDimension(getDescriptionPacket(), worldObj.provider.dimensionId);
		}

		if(!worldObj.isRemote) {
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			writeCustomNBT(nbttagcompound);
			nbttagcompound.setInteger(TAG_KNOWN_MANA, getCurrentMana());
			PacketDispatcher.sendPacketToPlayer(new Packet132TileEntityData(xCoord, yCoord, zCoord, -999, nbttagcompound), (Player) player);
		}

		worldObj.playSoundAtEntity(player, "random.orb", 0.11F, 1F);
	}

	public void renderHUD(Minecraft mc, ScaledResolution res) {
		String name = ModBlocks.pool.getLocalizedName();
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
		return mana;
	}
}
