/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 24, 2014, 3:59:06 PM (GMT)]
 */
package vazkii.botania.api.subtile;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ForgeRegistries;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.capability.FloatingFlowerImpl;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.item.IFloatingFlower;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.wand.IWandBindable;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.string.TileRedStringRelay;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Common superclass of all magical flower TE's
 */
public class TileEntitySpecialFlower extends TileEntity implements ITickable, IWandBindable {
	public static final ResourceLocation DING_SOUND_EVENT = new ResourceLocation("botania", "ding");
	public static final int SLOWDOWN_FACTOR_PODZOL = 5;
	public static final int SLOWDOWN_FACTOR_MYCEL = 10;
	private static final Tag<Block> FLOATING_TAG = new BlockTags.Wrapper(new ResourceLocation("botania", "special_floating_flowers"));

	private final IFloatingFlower floatingData = new FloatingFlowerImpl() {
		@Override
		public ItemStack getDisplayStack() {
			ResourceLocation id = getType().getRegistryName();
			Item item = ForgeRegistries.ITEMS.getValue(id);
			if(item != null) {
				return new ItemStack(item);
			} else {
				return super.getDisplayStack();
			}
		}
	};
	private final LazyOptional<IFloatingFlower> floatingDataCap = LazyOptional.of(() -> floatingData);

	public int ticksExisted = 0;

	/** true if this flower is working on Enchanted Soil **/
	public boolean overgrowth = false;
	/** true if this flower is working on Enchanted Soil and this is the second tick **/
	public boolean overgrowthBoost = false;

	public static final String TAG_TICKS_EXISTED = "ticksExisted";
	private static final String TAG_FLOATING_DATA = "floating";

	public TileEntitySpecialFlower(TileEntityType<?> type) {
		super(type);
	}

	@Override
	public final void tick() {
		TileEntity tileBelow = world.getTileEntity(pos.down());
		if(tileBelow instanceof TileRedStringRelay) {
			BlockPos coords = ((TileRedStringRelay) tileBelow).getBinding();
			if(coords != null) {
				BlockPos currPos = pos;
				setPos(coords);
				tickFlower();
				setPos(currPos);

				return;
			}
		}

		boolean special = isOnSpecialSoil();
		if(special) {
			this.overgrowth = true;
			if(isOvergrowthAffected()) {
				tickFlower();
				overgrowthBoost = true;
			}
		}
		tickFlower();
		overgrowth = false;
		overgrowthBoost = false;
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable EnumFacing side) {
		if(cap == BotaniaAPI.FLOATING_FLOWER_CAP) {
			if(hasWorld() && getWorld().getBlockState(getPos()).isIn(FLOATING_TAG)) {
				return floatingDataCap.cast();
			}
		}
		return super.getCapability(cap, side);
	}

	public boolean isFloating() {
		return getCapability(BotaniaAPI.FLOATING_FLOWER_CAP).isPresent();
	}

	public boolean isOnSpecialSoil() {
		if(isFloating()) {
			return false;
		} else {
			return world.getBlockState(pos.down()).getBlock() == ModBlocks.enchantedSoil;
		}
	}

	public void tickFlower() {
		ticksExisted++;
	}

	@Override
	public final void read(NBTTagCompound cmp) {
		super.read(cmp);
		if(cmp.contains(TAG_TICKS_EXISTED))
			ticksExisted = cmp.getInt(TAG_TICKS_EXISTED);
		if(cmp.contains(TAG_FLOATING_DATA))
			BotaniaAPI.FLOATING_FLOWER_CAP.readNBT(floatingData, null, cmp.getCompound(TAG_FLOATING_DATA));
		readFromPacketNBT(cmp);
	}

	@Nonnull
	@Override
	public final NBTTagCompound write(NBTTagCompound cmp) {
		cmp.putInt(TAG_TICKS_EXISTED, ticksExisted);
		cmp.put(TAG_FLOATING_DATA, BotaniaAPI.FLOATING_FLOWER_CAP.writeNBT(floatingData, null));
		writeToPacketNBT(cmp);
		return cmp;
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound cmp = new NBTTagCompound();
		writeToPacketNBT(cmp);
		return new SPacketUpdateTileEntity(getPos(), -1, cmp);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
		IFloatingFlower.IslandType oldType = floatingData.getIslandType();
		super.onDataPacket(net, packet);
		if(oldType != floatingData.getIslandType() && isFloating()) {
			world.markBlockRangeForRenderUpdate(pos, pos);
		}
	}

	@Nonnull
	@Override
	public NBTTagCompound getUpdateTag() {
		return write(new NBTTagCompound());
	}

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
		VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
	}

	public String getUnlocalizedName() {
		return getType().getRegistryName().toString();
	}

	/**
	 * Called when a Wand of the Forest is used on this sub tile. Note that the
	 * player parameter can be null if this is called from a dispenser.
	 */
	public boolean onWanded(EntityPlayer player, ItemStack wand) {
		return false;
	}

	/**
	 * Called when this sub tile is placed in the world (by an entity).
	 */
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack stack) {}

	/**
	 * Called when a player right clicks this sub tile.
	 */
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) { return false; }

	/**
	 * Called when this sub tile is added to the world.
	 */
	public void onBlockAdded(World world, BlockPos pos, IBlockState state) {}

	/**
	 * Called when this sub tile is harvested
	 */
	public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {}

	/**
	 * Allows additional processing of sub tile drops
	 */
	public List<ItemStack> getDrops(List<ItemStack> list) {
		return list;
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
	@OnlyIn(Dist.CLIENT)
	@Override
	public BlockPos getBinding() {
		return null;
	}

	/**
	 * Returns a descriptor for the radius of this sub tile. This is called while a player
	 * is looking at the block with a Manaseer Monocle (IBurstViewerBauble).
	 */
	@OnlyIn(Dist.CLIENT)
	public RadiusDescriptor getRadius() {
		return null;
	}

	/**
	 * Gets a BlockPos instance with the position of this sub tile.
	 */
	public BlockPos toBlockPos() {
		return getPos();
	}

	/**
	 * @see IWandBindable#canSelect(EntityPlayer, ItemStack, net.minecraft.util.math.BlockPos, net.minecraft.util.EnumFacing)
	 */
	@Override
	public boolean canSelect(EntityPlayer player, ItemStack wand, BlockPos pos, EnumFacing side) {
		return false;
	}

	/**
	 * @see IWandBindable#bindTo(EntityPlayer, ItemStack, net.minecraft.util.math.BlockPos, net.minecraft.util.EnumFacing)
	 */
	@Override
	public boolean bindTo(EntityPlayer player, ItemStack wand, BlockPos pos, EnumFacing side) {
		return false;
	}

	/**
	 * Called on the client when the block being pointed at is the one with this sub tile.
	 * Used to render a HUD portraying some data from this sub tile.
	 */
	@OnlyIn(Dist.CLIENT)
	public void renderHUD(Minecraft mc) {}

	/**
	 * Gets the light value for this SubTileEntity, this is a int (-1 to default to the flower)
	 */
	public int getLightValue() {
		return 0;
	}

	/**
	 * Gets the comparator input value for this SubTileEntity
	 */
	public int getComparatorInputOverride() {
		return 0;
	}

	/**
	 * Gets the redstone power level for this SubTileEntity
	 */
	public int getPowerLevel(EnumFacing side) {
		return 0;
	}

	/**
	 * Gets if this SubTileEntity is affected by Enchanted Soil's speed boost.
	 */
	public boolean isOvergrowthAffected() {
		return true;
	}

	/**
	 * Allow for the SubTile to be "slowed down".
	 * Slowing down is the action that happens when a flower is planted in Podzol or Mycellium.
	 * Any flowers that pick up items from the ground should have a delay on the time the item
	 * needs to be on the floor equal to the value of this method..
	 */
	public int getSlowdownFactor() {
		if(isFloating()) {
			IFloatingFlower.IslandType type = floatingData.getIslandType();
			if(type == IFloatingFlower.IslandType.MYCEL)
				return SLOWDOWN_FACTOR_MYCEL;
			else if(type == IFloatingFlower.IslandType.PODZOL)
				return SLOWDOWN_FACTOR_PODZOL;
		} else {
			Block below = world.getBlockState(getPos().down()).getBlock();
			if(below == Blocks.MYCELIUM)
				return SLOWDOWN_FACTOR_MYCEL;

			if(below == Blocks.PODZOL) {
				return SLOWDOWN_FACTOR_PODZOL;
			}
		}

		return 0;
	}


}
