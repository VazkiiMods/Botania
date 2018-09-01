/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jun 9, 2014, 8:51:55 PM (GMT)]
 */
package vazkii.botania.common.block.tile;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.ILexicon;
import vazkii.botania.api.lexicon.multiblock.Multiblock;
import vazkii.botania.api.lexicon.multiblock.MultiblockSet;
import vazkii.botania.api.recipe.ElvenPortalUpdateEvent;
import vazkii.botania.api.recipe.IElvenItem;
import vazkii.botania.api.recipe.RecipeElvenTrade;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.AlfPortalState;
import vazkii.botania.api.state.enums.LivingWoodVariant;
import vazkii.botania.api.state.enums.PylonVariant;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.mana.TilePool;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.item.ItemLexicon;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lexicon.LexiconData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class TileAlfPortal extends TileMod implements ITickable {

	private static final BlockPos[] LIVINGWOOD_POSITIONS = {
			new BlockPos(-1, 0, 0), new BlockPos(1, 0, 0), new BlockPos(-2, 1, 0),
			new BlockPos(2, 1, 0), new BlockPos(-2, 3, 0), new BlockPos(2, 3, 0),
			new BlockPos(-1, 4, 0), new BlockPos(1, 4, 0)
	};

	private static final BlockPos[] GLIMMERING_LIVINGWOOD_POSITIONS = {
			new BlockPos(-2, 2, 0), new BlockPos(2, 2, 0), new BlockPos(0, 4, 0)
	};

	private static final BlockPos[] AIR_POSITIONS = {
			new BlockPos(-1, 1, 0), new BlockPos(0, 1, 0), new BlockPos(1, 1, 0),
			new BlockPos(-1, 2, 0), new BlockPos(0, 2, 0), new BlockPos(1, 2, 0),
			new BlockPos(-1, 3, 0), new BlockPos(0, 3, 0), new BlockPos(1, 3, 0)
	};

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

	private static final Function<BlockPos, BlockPos> CONVERTER_X_Z = input -> new BlockPos(input.getZ(), input.getY(), input.getX());

	private static final Function<double[], double[]> CONVERTER_X_Z_FP = input -> new double[] { input[2], input[1], input[0] };

	private static final Function<BlockPos, BlockPos> CONVERTER_Z_SWAP = input -> new BlockPos(input.getX(), input.getY(), -input.getZ());

	public static MultiblockSet makeMultiblockSet() {
		Multiblock mb = new Multiblock();

		for(BlockPos l : LIVINGWOOD_POSITIONS)
			mb.addComponent(l.up(), ModBlocks.livingwood.getDefaultState());
		for(BlockPos g : GLIMMERING_LIVINGWOOD_POSITIONS)
			mb.addComponent(g.up(), ModBlocks.livingwood.getDefaultState().withProperty(BotaniaStateProps.LIVINGWOOD_VARIANT, LivingWoodVariant.GLIMMERING));
		//		for(BlockPos p : PYLON_POSITIONS)
		//			mb.addComponent(new BlockPos(-p.getX(), p.getY() + 1, -p.getZ()), ModBlocks.pylon.getDefaultState().withProperty(BotaniaStateProps.PYLON_VARIANT, PylonVariant.NATURA));
		//		for(BlockPos p : POOL_POSITIONS)
		//			mb.addComponent(new StateInsensitiveComponent(new BlockPos(-p.getX(), p.getY() + 1, -p.getZ()), ModBlocks.pool));

		mb.addComponent(new BlockPos(0, 1, 0), ModBlocks.alfPortal.getDefaultState());
		mb.setRenderOffset(new BlockPos(0, -1, 0));

		return mb.makeSet();
	}

	@Override
	public void update() {
		IBlockState iBlockState = world.getBlockState(getPos());
		if(iBlockState.getValue(BotaniaStateProps.ALFPORTAL_STATE) == AlfPortalState.OFF) {
			ticksOpen = 0;
			return;
		}
		AlfPortalState state = iBlockState.getValue(BotaniaStateProps.ALFPORTAL_STATE);
		AlfPortalState newState = getValidState();

		ticksOpen++;

		AxisAlignedBB aabb = getPortalAABB();
		boolean open = ticksOpen > 60;
		ElvenPortalUpdateEvent event = new ElvenPortalUpdateEvent(this, aabb, open, stacksIn);
		MinecraftForge.EVENT_BUS.post(event);

		if(ticksOpen > 60) {
			ticksSinceLastItem++;
			if(ConfigHandler.elfPortalParticlesEnabled)
				blockParticle(state);

			List<EntityItem> items = world.getEntitiesWithinAABB(EntityItem.class, aabb);
			if(!world.isRemote)
				for(EntityItem item : items) {
					if(item.isDead)
						continue;

					ItemStack stack = item.getItem();
					boolean consume;
					if (item.getEntityData().hasKey(TAG_PORTAL_FLAG)) {
						consume = false;
					} else if (stack.getItem() instanceof ItemLexicon) {
						consume = true;
					} else if ((!(stack.getItem() instanceof IElvenItem) || !((IElvenItem) stack.getItem()).isElvenItem(stack))) {
						consume = true;
					} else {
						consume = false;
					}

					if (consume) {
						item.setDead();
						if (validateItemUsage(stack))
							addItem(stack);
						ticksSinceLastItem = 0;
					}
				}

			if(ticksSinceLastItem >= 4) {
				if(!world.isRemote)
					resolveRecipes();
			}
		}

		if(closeNow) {
			world.setBlockState(getPos(), ModBlocks.alfPortal.getDefaultState(), 1 | 2);
			for(int i = 0; i < 36; i++)
				blockParticle(state);
			closeNow = false;
		} else if(newState != state) {
			if(newState == AlfPortalState.OFF)
				for(int i = 0; i < 36; i++)
					blockParticle(state);
			world.setBlockState(getPos(), world.getBlockState(getPos()).withProperty(BotaniaStateProps.ALFPORTAL_STATE, newState), 1 | 2);
		} else if(explode) {
			world.createExplosion(null, pos.getX() + .5, pos.getY() + 2.0, pos.getZ() + .5, 3f, true);
			explode = false;
		}
	}

	private boolean validateItemUsage(ItemStack inputStack) {
		if(inputStack.getItem() == ModItems.lexicon)
			return true;

		for(RecipeElvenTrade recipe : BotaniaAPI.elvenTradeRecipes) {
			for(Object o : recipe.getInputs()) {
				if(o instanceof String) {
					for(ItemStack target : OreDictionary.getOres((String) o)) {
						if(OreDictionary.itemMatches(target, inputStack, false))
							return true;
					}
				} else if(o instanceof ItemStack) {
					ItemStack target = (ItemStack) o;
					if(inputStack.getItem() == target.getItem() && inputStack.getItemDamage() == target.getItemDamage())
						return true;
				}
			}
		}
		if(inputStack.getItem() == Items.BREAD) //Don't teleport bread. (See also: #2403)
			explode = true;

		return false;
	}

	private void blockParticle(AlfPortalState state) {
		int i = world.rand.nextInt(AIR_POSITIONS.length);
		double[] pos = new double[] {
				AIR_POSITIONS[i].getX() + 0.5F, AIR_POSITIONS[i].getY() + 0.5F, AIR_POSITIONS[i].getZ() + 0.5F
		};
		if(state == AlfPortalState.ON_X)
			pos = CONVERTER_X_Z_FP.apply(pos);

		float motionMul = 0.2F;
		Botania.proxy.wispFX(getPos().getX() + pos[0], getPos().getY() + pos[1], getPos().getZ() + pos[2], (float) (Math.random() * 0.25F), (float) (Math.random() * 0.5F + 0.5F), (float) (Math.random() * 0.25F), (float) (Math.random() * 0.15F + 0.1F), (float) (Math.random() - 0.5F) * motionMul, (float) (Math.random() - 0.5F) * motionMul, (float) (Math.random() - 0.5F) * motionMul);
	}

	public boolean onWanded() {
		AlfPortalState state = world.getBlockState(getPos()).getValue(BotaniaStateProps.ALFPORTAL_STATE);
		if(state == AlfPortalState.OFF) {
			AlfPortalState newState = getValidState();
			if(newState != AlfPortalState.OFF) {
				world.setBlockState(getPos(), world.getBlockState(getPos()).withProperty(BotaniaStateProps.ALFPORTAL_STATE, newState), 1 | 2);
				return true;
			}
		}

		return false;
	}

	private AxisAlignedBB getPortalAABB() {
		AxisAlignedBB aabb = new AxisAlignedBB(pos.add(-1, 1, 0), pos.add(2, 4, 1));
		if(world.getBlockState(getPos()).getValue(BotaniaStateProps.ALFPORTAL_STATE) == AlfPortalState.ON_X)
			aabb = new AxisAlignedBB(pos.add(0, 1, -1), pos.add(1, 4, 2));

		return aabb;
	}

	private void addItem(ItemStack stack) {
		int size = stack.getCount();
		stack.setCount(1);
		for(int i = 0; i < size; i++)
			stacksIn.add(stack.copy());
	}

	private void resolveRecipes() {
		int i = 0;
		for(ItemStack stack : stacksIn) {
			if(!stack.isEmpty() && stack.getItem() instanceof ILexicon) {
				ILexicon lexicon = (ILexicon) stack.getItem();
				if (!lexicon.isKnowledgeUnlocked(stack, BotaniaAPI.elvenKnowledge)) {
					lexicon.unlockKnowledge(stack, BotaniaAPI.elvenKnowledge);
					ItemLexicon.setForcedPage(stack, LexiconData.elvenMessage.getUnlocalizedName());
					spawnItem(stack);
					stacksIn.remove(i);
					return;
				}
			}
			i++;
		}

		for(RecipeElvenTrade recipe : BotaniaAPI.elvenTradeRecipes) {
			if(recipe.matches(stacksIn, false)) {
				if(consumeMana(null, 500, false)) {
					recipe.matches(stacksIn, true);
					for(ItemStack output : recipe.getOutputs())
						spawnItem(output.copy());
				}
				break;
			}
		}
	}

	private void spawnItem(ItemStack stack) {
		EntityItem item = new EntityItem(world, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, stack);
		item.getEntityData().setBoolean(TAG_PORTAL_FLAG, true);
		world.spawnEntity(item);
		ticksSinceLastItem = 0;
	}

	@Nonnull
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound cmp) {
		NBTTagCompound ret = super.writeToNBT(cmp);

		cmp.setInteger(TAG_STACK_COUNT, stacksIn.size());
		int i = 0;
		for(ItemStack stack : stacksIn) {
			NBTTagCompound stackcmp = stack.writeToNBT(new NBTTagCompound());
			cmp.setTag(TAG_STACK + i, stackcmp);
			i++;
		}

		return ret;
	}

	@Override
	public void readFromNBT(NBTTagCompound cmp) {
		super.readFromNBT(cmp);

		int count = cmp.getInteger(TAG_STACK_COUNT);
		stacksIn.clear();
		for(int i = 0; i < count; i++) {
			NBTTagCompound stackcmp = cmp.getCompoundTag(TAG_STACK + i);
			ItemStack stack = new ItemStack(stackcmp);
			stacksIn.add(stack);
		}
	}

	@Override
	public void writePacketNBT(NBTTagCompound cmp) {
		cmp.setInteger(TAG_TICKS_OPEN, ticksOpen);
		cmp.setInteger(TAG_TICKS_SINCE_LAST_ITEM, ticksSinceLastItem);
	}

	@Override
	public void readPacketNBT(NBTTagCompound cmp) {
		ticksOpen = cmp.getInteger(TAG_TICKS_OPEN);
		ticksSinceLastItem = cmp.getInteger(TAG_TICKS_SINCE_LAST_ITEM);
	}

	private AlfPortalState getValidState() {
		if(checkConverter(null))
			return AlfPortalState.ON_Z;

		if(checkConverter(CONVERTER_X_Z))
			return AlfPortalState.ON_X;

		return AlfPortalState.OFF;
	}

	private boolean checkConverter(Function<BlockPos, BlockPos> baseConverter) {
		return checkMultipleConverters(baseConverter) || checkMultipleConverters(CONVERTER_Z_SWAP, baseConverter);
	}

	@SafeVarargs
	private final boolean checkMultipleConverters(Function<BlockPos, BlockPos>... converters) {
		if(!check2DArray(AIR_POSITIONS, Blocks.AIR.getDefaultState(), true, converters))
			return false;
		if(!check2DArray(LIVINGWOOD_POSITIONS, ModBlocks.livingwood.getDefaultState().withProperty(BotaniaStateProps.LIVINGWOOD_VARIANT, LivingWoodVariant.DEFAULT), false, converters))
			return false;
		if(!check2DArray(GLIMMERING_LIVINGWOOD_POSITIONS, ModBlocks.livingwood.getDefaultState().withProperty(BotaniaStateProps.LIVINGWOOD_VARIANT, LivingWoodVariant.GLIMMERING), false, converters))
			return false;
		//		if(!check2DArray(PYLON_POSITIONS, ModBlocks.pylon.getDefaultState().withProperty(BotaniaStateProps.PYLON_VARIANT, PylonVariant.NATURA), false, converters))
		//			return false;
		//		if(!check2DArray(POOL_POSITIONS, ModBlocks.pool.getDefaultState(), true, converters))
		//			return false;

		lightPylons();
		return true;
	}

	public List<BlockPos> locatePylons() {
		List<BlockPos> list = new ArrayList();
		int range = 5;

		IBlockState pylonState = ModBlocks.pylon.getDefaultState().withProperty(BotaniaStateProps.PYLON_VARIANT, PylonVariant.NATURA);
		IBlockState poolState = ModBlocks.pool.getDefaultState();

		for(int i = -range; i < range + 1; i++)
			for(int j = -range; j < range + 1; j++)
				for(int k = -range; k < range + 1; k++) {
					BlockPos pos = new BlockPos(i, j, k);
					if(checkPosition(pos, pylonState, false) && checkPosition(pos.down(), poolState, true))
						list.add(pos);
				}

		return list;
	}

	public void lightPylons() {
		if(ticksOpen < 50)
			return;

		List<BlockPos> pylons = locatePylons();
		for(BlockPos pos : pylons) {
			TileEntity tile = world.getTileEntity(getPos().add(pos));
			if(tile instanceof TilePylon) {
				TilePylon pylon = (TilePylon) tile;
				pylon.activated = true;
				pylon.centerPos = getPos();
			}
		}

		if(ticksOpen == 50)
			consumeMana(pylons, 200000, true);
	}

	public boolean consumeMana(@Nullable List<BlockPos> pylons, int totalCost, boolean close) {
		List<TilePool> consumePools = new ArrayList();
		int consumed = 0;

		if(pylons == null)
			pylons = locatePylons();

		if(pylons.size() < 2) {
			closeNow = true;
			return false;
		}

		int costPer = Math.max(1, totalCost / pylons.size());
		int expectedConsumption = costPer * pylons.size();

		for(BlockPos pos : pylons) {
			TileEntity tile = world.getTileEntity(getPos().add(pos));
			if(tile instanceof TilePylon) {
				TilePylon pylon = (TilePylon) tile;
				pylon.activated = true;
				pylon.centerPos = getPos();
			}

			tile = world.getTileEntity(getPos().add(pos).down());
			if(tile instanceof TilePool) {
				TilePool pool = (TilePool) tile;

				if(pool.getCurrentMana() < costPer) {
					closeNow = closeNow || close;
					return false;
				} else if(!world.isRemote) {
					consumePools.add(pool);
					consumed += costPer;
				}
			}
		}

		if(consumed >= expectedConsumption) {
			for(TilePool pool : consumePools)
				pool.recieveMana(-costPer);
			return true;
		}

		return false;
	}

	@SafeVarargs
	private final boolean check2DArray(BlockPos[] positions, IBlockState state, boolean onlyCheckBlock, Function<BlockPos, BlockPos>... converters) {
		for(BlockPos pos : positions) {
			for(Function<BlockPos, BlockPos> f : converters)
				if(f != null)
					pos = f.apply(pos);

			if(!checkPosition(pos, state, onlyCheckBlock))
				return false;
		}

		return true;
	}

	private boolean checkPosition(BlockPos pos, IBlockState state, boolean onlyCheckBlock) {
		BlockPos pos_ = getPos().add(pos);

		IBlockState stateat = world.getBlockState(pos_);
		Block blockat = stateat.getBlock();

		if(state.getBlock() == Blocks.AIR ? blockat.isAir(stateat, world, pos_) : blockat == state.getBlock())
			return onlyCheckBlock || stateat == state;

		return false;
	}

	@Nonnull
	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return INFINITE_EXTENT_AABB;
	}
}
