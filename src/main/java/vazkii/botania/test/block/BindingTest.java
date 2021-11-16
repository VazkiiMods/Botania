package vazkii.botania.test.block;

import com.google.common.collect.Iterables;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.mutable.MutableObject;
import vazkii.botania.api.subtile.TileEntityFunctionalFlower;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.block.mana.BlockPool;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.test.TestingUtil;

import java.util.*;
import java.util.stream.Collectors;

public class BindingTest {
	private static final BlockState SHOULD_BIND = Blocks.GREEN_STAINED_GLASS.defaultBlockState();
	private static final BlockState SHOULD_NOT_BIND = Blocks.PINK_STAINED_GLASS.defaultBlockState();
	
	public static final String FLOWER_TO_POOL_ARENA_STRUCTURE = "botania:block/flower_to_pool_arena";
	private static final String FLOWER_TO_POOL_BATCH = LibMisc.MOD_ID + ":" + "flower_to_pool_binding";
	
	private void flowerPoolSmokeTest(GameTestHelper helper) {
		//Collect the positions of "should bind" / "shouldn't bind" blocks in the structure, and find the mana pool.
		//These are "relative blockpos"es.
		Set<BlockPos> shouldBind = new HashSet<>();
		Set<BlockPos> shouldNotBind = new HashSet<>();
		MutableObject<BlockPos> poolPosWrap = new MutableObject<>(null); //lambdas...
		
		helper.forEveryBlockInStructure(pos -> {
			BlockState state = helper.getBlockState(pos);
			if(state.equals(SHOULD_BIND)) {
				shouldBind.add(pos.immutable());
			} else if(state.equals(SHOULD_NOT_BIND)) {
				shouldNotBind.add(pos.immutable());
			} else if(state.getBlock() instanceof BlockPool) {
				poolPosWrap.setValue(pos.immutable());
			}
		});
		
		TestingUtil.assertThat(poolPosWrap.getValue() != null, "Didn't find mana pool in structure");
		BlockPos absolutePoolPos = helper.absolutePos(poolPosWrap.getValue());
		
		//Place floating flowers in all positions.
		for(BlockPos pos : Iterables.concat(shouldBind, shouldNotBind)) {
			helper.setBlock(pos, ModSubtiles.clayconiaFloating);
		}
		
		//TODO: Remove this after https://github.com/VazkiiMods/Botania/pull/3831, flowers *should* bind on placement then
		helper.runAfterDelay(1, () -> {
			try {
				List<BlockPos> shouldBindButDidNot = new ArrayList<>();
				List<BlockPos> shouldNotBindButDid = new ArrayList<>();
				
				//Verify that all flowers have the correct binding
				for(BlockPos pos : Iterables.concat(shouldBind, shouldNotBind)) {
					BlockEntity be = helper.getBlockEntity(pos);
					if(!(be instanceof TileEntityFunctionalFlower)) {
						TestingUtil.throwPositionedAssertion(helper, pos, "Missing flower tile entity?");
					}
					TileEntityFunctionalFlower flower = (TileEntityFunctionalFlower) be;
					
					BlockPos binding = flower.getBinding();
					boolean hasBinding = binding != null;
					boolean correctBinding = hasBinding && binding.equals(absolutePoolPos);
					
					if(binding != null && !correctBinding) {
						TestingUtil.throwPositionedAssertion(helper, pos, "Flower bound to something other than the pool, that's weird");
						continue;
					}
					
					//The flower is close enough to bind to the pool, but didn't.
					if(shouldBind.contains(pos) && !correctBinding) {
						shouldBindButDidNot.add(pos.immutable());
					}
					
					//The flower is too far away to bind to the pool, but bound to something anyways.
					if(shouldNotBind.contains(pos) && hasBinding) {
						shouldNotBindButDid.add(pos.immutable());
					}
				}
				
				//Display results in the world
				for(BlockPos pos : shouldBindButDidNot) {
					helper.setBlock(pos, Blocks.GREEN_WOOL);
				}
				
				for(BlockPos pos : shouldNotBindButDid) {
					helper.setBlock(pos, Blocks.PINK_WOOL);
				}
				
				//Assert that there are no screwed up flowers
				TestingUtil.assertThat(shouldBindButDidNot.isEmpty(), "Flowers that should bind, but didn't: " + str(shouldBindButDidNot));
				TestingUtil.assertThat(shouldNotBindButDid.isEmpty(), "Flowers that should not bind, but did: " + str(shouldNotBindButDid));
			} finally {
				//Clear out all the flowers (they're laggy!)
				for(BlockPos pos : Iterables.concat(shouldBind, shouldNotBind)) {
					if(helper.getBlockEntity(pos) instanceof TileEntityFunctionalFlower) {
						helper.setBlock(pos, Blocks.AIR);
					}
				}
			}
		});
	}
	
	private static String str(List<BlockPos> list) {
		return list.stream().map(Objects::toString).collect(Collectors.joining(", "));
	}
	
	@GameTest(template = FLOWER_TO_POOL_ARENA_STRUCTURE, batch = FLOWER_TO_POOL_BATCH)
	public void flowerPoolSmokeTestA(GameTestHelper helper) {
		flowerPoolSmokeTest(helper);
	}
	
	@GameTest(template = FLOWER_TO_POOL_ARENA_STRUCTURE, rotationSteps = 1, batch = FLOWER_TO_POOL_BATCH)
	public void flowerPoolSmokeTestB(GameTestHelper helper) {
		flowerPoolSmokeTest(helper);
	}
	
	@GameTest(template = FLOWER_TO_POOL_ARENA_STRUCTURE, rotationSteps = 2, batch = FLOWER_TO_POOL_BATCH)
	public void flowerPoolSmokeTestC(GameTestHelper helper) {
		flowerPoolSmokeTest(helper);
	}
	
	@GameTest(template = FLOWER_TO_POOL_ARENA_STRUCTURE, rotationSteps = 3, batch = FLOWER_TO_POOL_BATCH)
	public void flowerPoolSmokeTestD(GameTestHelper helper) {
		flowerPoolSmokeTest(helper);
	}
}
