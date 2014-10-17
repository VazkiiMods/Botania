/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 24, 2014, 3:59:06 PM (GMT)]
 */
package vazkii.botania.api.subtile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IIcon;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.wand.IWandBindable;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * A Sub-TileEntity, this is used for the flower system. Make sure to map subclasses
 * of this using BotaniaAPI.mapSubTile(String, Class). Any subclass of this must have
 * a no parameter constructor.
 */
public class SubTileEntity {

	protected TileEntity supertile;

	/** The Tag items should use to store which sub tile they are. **/
	public static final String TAG_TYPE = "type";

	public void setSupertile(TileEntity tile) {
		supertile = tile;
	}

	public boolean canUpdate() {
		return false;
	}

	public void onUpdate() { }

	/**
	 * Writes some extra data to a network packet. This data is read
	 * by readFromPacketNBT on the client that receives the packet.
	 * Note: This method is also used to write to the world NBT.
	 */
	public void writeToPacketNBT(NBTTagCompound cmp) { }

	/**
	 * Reads data from a network packet. This data is written by
	 * writeToPacketNBT in the server. Note: This method is also used
	 * to read from the world NBT.
	 */
	public void readFromPacketNBT(NBTTagCompound cmp) { }

	public void sync() {
		supertile.getWorldObj().markBlockForUpdate(supertile.xCoord, supertile.yCoord, supertile.zCoord);
	}

	public String getUnlocalizedName() {
		return BotaniaAPI.getSubTileStringMapping(getClass());
	}

	/**
	 * Gets the icon for this SubTileEntity, this is a block icon.
	 */
	@SideOnly(Side.CLIENT)
	public IIcon getIcon() {
		return BotaniaAPI.internalHandler.getSubTileIconForName(getUnlocalizedName());
	}

	/**
	 * Called when a Wand of the Forest is used on this sub tile. Note that the
	 * player parameter can be null if this is called from a dispenser.
	 */
	public boolean onWanded(EntityPlayer player, ItemStack wand) {
		return false;
	}

	/**
	 * Gets which Lexicon Entry to open when this sub tile is right clicked with a lexicon.
	 */
	public LexiconEntry getEntry() {
		return null;
	}

	/**
	 * Gets the block coordinates this is bound to, for use with the wireframe render
	 * when the sub tile is being hovered with a wand of the forest.
	 */
	@SideOnly(Side.CLIENT)
	public ChunkCoordinates getBinding() {
		return null;
	}

	/**
	 * @see IWandBindable#canSelect(EntityPlayer, ItemStack, int, int, int, int)
	 */
	public boolean canSelect(EntityPlayer player, ItemStack wand, int x, int y, int z, int side) {
		return false;
	}

	/**
	 * @see IWandBindable#bindTo(EntityPlayer, ItemStack, int, int, int, int)
	 */
	public boolean bindTo(EntityPlayer player, ItemStack wand, int x, int y, int z, int side) {
		return false;
	}

	/**
	 * Called on the client when the block being pointed at is the one with this sub tile.
	 * Used to render a HUD portraying some data from this sub tile.
	 */
	@SideOnly(Side.CLIENT)
	public void renderHUD(Minecraft mc, ScaledResolution res) {
		// NO-OP
	}
}
