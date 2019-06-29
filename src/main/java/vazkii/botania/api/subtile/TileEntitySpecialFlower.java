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
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Hand;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ForgeRegistries;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.capability.FloatingFlowerImpl;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.item.IFloatingFlower;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.wand.IWandBindable;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.string.TileRedStringRelay;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Common superclass of all magical flower TE's
 */
public class TileEntitySpecialFlower extends TileEntity implements ITickableTileEntity, IWandBindable {
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
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
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

	private boolean isOnSpecialSoil() {
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
	public final void read(CompoundNBT cmp) {
		super.read(cmp);
		if(cmp.contains(TAG_TICKS_EXISTED))
			ticksExisted = cmp.getInt(TAG_TICKS_EXISTED);
		if(cmp.contains(TAG_FLOATING_DATA))
			BotaniaAPI.FLOATING_FLOWER_CAP.readNBT(floatingData, null, cmp.getCompound(TAG_FLOATING_DATA));
		readFromPacketNBT(cmp);
	}

	@Nonnull
	@Override
	public final CompoundNBT write(CompoundNBT cmp) {
		cmp.putInt(TAG_TICKS_EXISTED, ticksExisted);
		cmp.put(TAG_FLOATING_DATA, BotaniaAPI.FLOATING_FLOWER_CAP.writeNBT(floatingData, null));
		writeToPacketNBT(cmp);
		return cmp;
	}

	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		CompoundNBT cmp = new CompoundNBT();
		writeToPacketNBT(cmp);
		return new SUpdateTileEntityPacket(getPos(), -1, cmp);
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
		IFloatingFlower.IslandType oldType = floatingData.getIslandType();
		super.onDataPacket(net, packet);
		if(oldType != floatingData.getIslandType() && isFloating()) {
			world.markForRerender(pos);
		}
	}

	@Nonnull
	@Override
	public CompoundNBT getUpdateTag() {
		return write(new CompoundNBT());
	}

	/**
	 * Writes some extra data to a network packet. This data is read
	 * by readFromPacketNBT on the client that receives the packet.
	 * Note: This method is also used to write to the world NBT.
	 */
	public void writeToPacketNBT(CompoundNBT cmp) { }

	/**
	 * Reads data from a network packet. This data is written by
	 * writeToPacketNBT in the server. Note: This method is also used
	 * to read from the world NBT.
	 */
	public void readFromPacketNBT(CompoundNBT cmp) { }

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
	public boolean onWanded(PlayerEntity player, ItemStack wand) {
		return false;
	}

	/**
	 * Called when this sub tile is placed in the world (by an entity).
	 */
	public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity entity, ItemStack stack) {}

	/**
	 * Called when a player right clicks this sub tile.
	 */
	public boolean onBlockActivated(World world, BlockPos pos, BlockState state, PlayerEntity player, Hand hand, Direction side, double hitX, double hitY, double hitZ) { return false; }

	/**
	 * Called when this sub tile is added to the world.
	 */
	public void onBlockAdded(World world, BlockPos pos, BlockState state) {}

	/**
	 * Called when this sub tile is harvested
	 */
	public void onBlockHarvested(World world, BlockPos pos, BlockState state, PlayerEntity player) {}

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
	 * @see IWandBindable#canSelect(PlayerEntity, ItemStack, net.minecraft.util.math.BlockPos, Direction)
	 */
	@Override
	public boolean canSelect(PlayerEntity player, ItemStack wand, BlockPos pos, Direction side) {
		return false;
	}

	/**
	 * @see IWandBindable#bindTo(PlayerEntity, ItemStack, net.minecraft.util.math.BlockPos, Direction)
	 */
	@Override
	public boolean bindTo(PlayerEntity player, ItemStack wand, BlockPos pos, Direction side) {
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
	public int getPowerLevel(Direction side) {
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

	@Nonnull
	@Override
	public IModelData getModelData() {
		if(isFloating()) {
			return new ModelDataMap.Builder()
					.withInitial(BotaniaStateProps.FLOATING_DATA, floatingData)
					.build();
		}
		return EmptyModelData.INSTANCE;
	}
}
