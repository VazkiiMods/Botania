/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile;

import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.LazyValue;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import vazkii.botania.api.recipe.ElvenPortalUpdateEvent;
import vazkii.botania.api.recipe.IElvenItem;
import vazkii.botania.api.recipe.IElvenTradeRecipe;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.AlfPortalState;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.advancements.AlfPortalBreadTrigger;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.mana.BlockPool;
import vazkii.botania.common.block.tile.mana.TilePool;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.crafting.ModRecipeTypes;
import vazkii.botania.common.item.ItemLexicon;
import vazkii.patchouli.api.IMultiblock;
import vazkii.patchouli.api.PatchouliAPI;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class TileAlfPortal extends TileMod implements ITickableTileEntity {
	public static final LazyValue<IMultiblock> MULTIBLOCK = new LazyValue<>(() -> PatchouliAPI.instance.makeMultiblock(
			new String[][] {
					{ "_", "W", "G", "W", "_" },
					{ "W", " ", " ", " ", "W" },
					{ "G", " ", " ", " ", "G" },
					{ "W", " ", " ", " ", "W" },
					{ "_", "W", "0", "W", "_" }
			},
			'W', ModBlocks.livingwood,
			'G', ModBlocks.livingwoodGlimmering,
			'0', ModBlocks.alfPortal
	));

	private static final String TAG_TICKS_OPEN = "ticksOpen";
	private static final String TAG_TICKS_SINCE_LAST_ITEM = "ticksSinceLastItem";
	private static final String TAG_STACK_COUNT = "stackCount";
	private static final String TAG_STACK = "portalStack";
	private static final String TAG_PORTAL_FLAG = "_elvenPortal";

	private final List<ItemStack> stacksIn = new ArrayList<>();

	public int ticksOpen = 0;
	private int ticksSinceLastItem = 0;
	private boolean closeNow = false;
	private boolean explode = false;
	@Nullable
	private UUID breadPlayer = null;

	public TileAlfPortal() {
		super(ModTiles.ALF_PORTAL);
	}

	@Override
	public void tick() {
		if (getBlockState().get(BotaniaStateProps.ALFPORTAL_STATE) == AlfPortalState.OFF) {
			ticksOpen = 0;
			return;
		}
		AlfPortalState state = getBlockState().get(BotaniaStateProps.ALFPORTAL_STATE);
		AlfPortalState newState = getValidState();

		ticksOpen++;

		AxisAlignedBB aabb = getPortalAABB();
		boolean open = ticksOpen > 60;
		ElvenPortalUpdateEvent event = new ElvenPortalUpdateEvent(this, aabb, open, stacksIn);
		MinecraftForge.EVENT_BUS.post(event);

		if (ticksOpen > 60) {
			ticksSinceLastItem++;
			if (world.isRemote && ConfigHandler.CLIENT.elfPortalParticlesEnabled.get()) {
				blockParticle(state);
			}

			List<ItemEntity> items = world.getEntitiesWithinAABB(ItemEntity.class, aabb);
			if (!world.isRemote) {
				for (ItemEntity item : items) {
					if (!item.isAlive()) {
						continue;
					}

					ItemStack stack = item.getItem();
					boolean consume;
					if (item.getPersistentData().contains(TAG_PORTAL_FLAG)) {
						consume = false;
					} else if (stack.getItem() instanceof ItemLexicon) {
						consume = true;
					} else if ((!(stack.getItem() instanceof IElvenItem) || !((IElvenItem) stack.getItem()).isElvenItem(stack))) {
						consume = true;
					} else {
						consume = false;
					}

					if (consume) {
						item.remove();
						if (validateItemUsage(item)) {
							addItem(stack);
						}
						ticksSinceLastItem = 0;
					}
				}
			}

			if (!world.isRemote && !stacksIn.isEmpty() && ticksSinceLastItem >= 4) {
				resolveRecipes();
			}
		}

		if (closeNow) {
			world.setBlockState(getPos(), ModBlocks.alfPortal.getDefaultState());
			for (int i = 0; i < 36; i++) {
				blockParticle(state);
			}
			closeNow = false;
		} else if (newState != state) {
			if (newState == AlfPortalState.OFF) {
				for (int i = 0; i < 36; i++) {
					blockParticle(state);
				}
			}
			world.setBlockState(getPos(), getBlockState().with(BotaniaStateProps.ALFPORTAL_STATE, newState));
		} else if (explode) {
			world.createExplosion(null, pos.getX() + .5, pos.getY() + 2.0, pos.getZ() + .5,
					3f, Explosion.Mode.DESTROY);
			explode = false;

			if (!world.isRemote && breadPlayer != null) {
				PlayerEntity entity = world.getPlayerByUuid(breadPlayer);
				if (entity instanceof ServerPlayerEntity) {
					AlfPortalBreadTrigger.INSTANCE.trigger((ServerPlayerEntity) entity, getPos());
				}
			}
			breadPlayer = null;
		}
	}

	private boolean validateItemUsage(ItemEntity entity) {
		ItemStack inputStack = entity.getItem();
		for (IRecipe<?> recipe : ModRecipeTypes.getRecipes(world, ModRecipeTypes.ELVEN_TRADE_TYPE).values()) {
			if (recipe instanceof IElvenTradeRecipe && ((IElvenTradeRecipe) recipe).containsItem(inputStack)) {
				return true;
			}
		}
		if (inputStack.getItem() == Items.BREAD) {
			//Don't teleport bread. (See also: #2403)
			explode = true;
			breadPlayer = entity.getThrowerId();
		}

		return false;
	}

	private void blockParticle(AlfPortalState state) {
		double dh, dy;

		// Pick one of the inner positions
		switch (world.rand.nextInt(9)) {
		default:
		case 0:
			dh = 0;
			dy = 1;
			break;
		case 1:
			dh = 0;
			dy = 2;
			break;
		case 2:
			dh = 0;
			dy = 3;
			break;
		case 3:
			dh = -1;
			dy = 1;
			break;
		case 4:
			dh = -1;
			dy = 2;
			break;
		case 5:
			dh = -1;
			dy = 3;
			break;
		case 6:
			dh = 1;
			dy = 1;
			break;
		case 7:
			dh = 1;
			dy = 2;
			break;
		case 8:
			dh = 1;
			dy = 3;
			break;
		}
		double dx = state == AlfPortalState.ON_X ? 0 : dh;
		double dz = state == AlfPortalState.ON_Z ? 0 : dh;

		float motionMul = 0.2F;
		WispParticleData data = WispParticleData.wisp((float) (Math.random() * 0.15F + 0.1F), (float) (Math.random() * 0.25F), (float) (Math.random() * 0.5F + 0.5F), (float) (Math.random() * 0.25F));
		world.addParticle(data, getPos().getX() + dx, getPos().getY() + dy, getPos().getZ() + dz, (float) (Math.random() - 0.5F) * motionMul, (float) (Math.random() - 0.5F) * motionMul, (float) (Math.random() - 0.5F) * motionMul);
	}

	public boolean onWanded() {
		AlfPortalState state = getBlockState().get(BotaniaStateProps.ALFPORTAL_STATE);
		if (state == AlfPortalState.OFF) {
			AlfPortalState newState = getValidState();
			if (newState != AlfPortalState.OFF) {
				world.setBlockState(getPos(), getBlockState().with(BotaniaStateProps.ALFPORTAL_STATE, newState));
				return true;
			}
		}

		return false;
	}

	private AxisAlignedBB getPortalAABB() {
		AxisAlignedBB aabb = new AxisAlignedBB(pos.add(-1, 1, 0), pos.add(2, 4, 1));
		if (getBlockState().get(BotaniaStateProps.ALFPORTAL_STATE) == AlfPortalState.ON_X) {
			aabb = new AxisAlignedBB(pos.add(0, 1, -1), pos.add(1, 4, 2));
		}

		return aabb;
	}

	private void addItem(ItemStack stack) {
		int size = stack.getCount();
		stack.setCount(1);
		for (int i = 0; i < size; i++) {
			stacksIn.add(stack.copy());
		}
	}

	@SuppressWarnings("unchecked")
	public static Collection<IElvenTradeRecipe> elvenTradeRecipes(World world) {
		// By virtue of IRecipeType's type parameter,
		// we know all the recipes in the map must be IElvenTradeRecipe.
		// However, vanilla's signature on this method is dumb (should be Map<ResourceLocation, T>)
		return (Collection<IElvenTradeRecipe>) (Collection<?>) ModRecipeTypes.getRecipes(world, ModRecipeTypes.ELVEN_TRADE_TYPE).values();
	}

	private void resolveRecipes() {
		List<BlockPos> pylons = locatePylons();
		for (IRecipe<?> r : ModRecipeTypes.getRecipes(world, ModRecipeTypes.ELVEN_TRADE_TYPE).values()) {
			if (!(r instanceof IElvenTradeRecipe)) {
				continue;
			}
			IElvenTradeRecipe recipe = (IElvenTradeRecipe) r;
			Optional<List<ItemStack>> match = recipe.match(stacksIn);
			if (match.isPresent()) {
				if (consumeMana(pylons, 500, false)) {
					List<ItemStack> inputs = match.get();
					for (ItemStack stack : inputs) {
						stacksIn.remove(stack);
					}
					for (ItemStack output : recipe.getOutputs(inputs)) {
						spawnItem(output.copy());
					}
				}
				break;
			}
		}
	}

	private void spawnItem(ItemStack stack) {
		ItemEntity item = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, stack);
		item.getPersistentData().putBoolean(TAG_PORTAL_FLAG, true);
		world.addEntity(item);
		ticksSinceLastItem = 0;
	}

	@Nonnull
	@Override
	public CompoundNBT write(CompoundNBT cmp) {
		CompoundNBT ret = super.write(cmp);

		cmp.putInt(TAG_STACK_COUNT, stacksIn.size());
		int i = 0;
		for (ItemStack stack : stacksIn) {
			CompoundNBT stackcmp = stack.write(new CompoundNBT());
			cmp.put(TAG_STACK + i, stackcmp);
			i++;
		}

		return ret;
	}

	@Override
	public void read(BlockState state, CompoundNBT cmp) {
		super.read(state, cmp);

		int count = cmp.getInt(TAG_STACK_COUNT);
		stacksIn.clear();
		for (int i = 0; i < count; i++) {
			CompoundNBT stackcmp = cmp.getCompound(TAG_STACK + i);
			ItemStack stack = ItemStack.read(stackcmp);
			stacksIn.add(stack);
		}
	}

	@Override
	public void writePacketNBT(CompoundNBT cmp) {
		cmp.putInt(TAG_TICKS_OPEN, ticksOpen);
		cmp.putInt(TAG_TICKS_SINCE_LAST_ITEM, ticksSinceLastItem);
	}

	@Override
	public void readPacketNBT(CompoundNBT cmp) {
		ticksOpen = cmp.getInt(TAG_TICKS_OPEN);
		ticksSinceLastItem = cmp.getInt(TAG_TICKS_SINCE_LAST_ITEM);
	}

	private AlfPortalState getValidState() {
		Rotation rot = MULTIBLOCK.getValue().validate(world, getPos());
		if (rot == null) {
			return AlfPortalState.OFF;
		}

		lightPylons();
		switch (rot) {
		default:
		case NONE:
		case CLOCKWISE_180:
			return AlfPortalState.ON_Z;
		case CLOCKWISE_90:
		case COUNTERCLOCKWISE_90:
			return AlfPortalState.ON_X;
		}
	}

	public List<BlockPos> locatePylons() {
		int range = 5;

		BlockState pylonState = ModBlocks.naturaPylon.getDefaultState();

		return BlockPos.getAllInBox(getPos().add(-range, -range, -range), getPos().add(range, range, range))
				.filter(world::isBlockLoaded)
				.filter(p -> world.getBlockState(p) == pylonState && world.getBlockState(p.down()).getBlock() instanceof BlockPool)
				.map(BlockPos::toImmutable)
				.collect(Collectors.toList());
	}

	public void lightPylons() {
		if (ticksOpen < 50) {
			return;
		}

		List<BlockPos> pylons = locatePylons();
		for (BlockPos pos : pylons) {
			TileEntity tile = world.getTileEntity(pos);
			if (tile instanceof TilePylon) {
				TilePylon pylon = (TilePylon) tile;
				pylon.activated = true;
				pylon.centerPos = getPos();
			}
		}

		if (ticksOpen == 50) {
			consumeMana(pylons, 200000, true);
		}
	}

	public boolean consumeMana(List<BlockPos> pylons, int totalCost, boolean close) {
		List<TilePool> consumePools = new ArrayList<>();
		int consumed = 0;

		if (pylons.size() < 2) {
			closeNow = true;
			return false;
		}

		int costPer = Math.max(1, totalCost / pylons.size());
		int expectedConsumption = costPer * pylons.size();

		for (BlockPos pos : pylons) {
			TileEntity tile = world.getTileEntity(pos);
			if (tile instanceof TilePylon) {
				TilePylon pylon = (TilePylon) tile;
				pylon.activated = true;
				pylon.centerPos = getPos();
			}

			tile = world.getTileEntity(pos.down());
			if (tile instanceof TilePool) {
				TilePool pool = (TilePool) tile;

				if (pool.getCurrentMana() < costPer) {
					closeNow = closeNow || close;
					return false;
				} else if (!world.isRemote) {
					consumePools.add(pool);
					consumed += costPer;
				}
			}
		}

		if (consumed >= expectedConsumption) {
			for (TilePool pool : consumePools) {
				pool.receiveMana(-costPer);
			}
			return true;
		}

		return false;
	}

	@Nonnull
	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return INFINITE_EXTENT_AABB;
	}
}
