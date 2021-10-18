/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile.corporea;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import vazkii.botania.api.corporea.*;
import vazkii.botania.common.advancements.CorporeaRequestTrigger;
import vazkii.botania.common.block.tile.ModTiles;
import vazkii.botania.common.core.ModStats;
import vazkii.botania.common.core.helper.MathHelper;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TileCorporeaIndex extends TileCorporeaBase implements ICorporeaRequestor {
	public static final double RADIUS = 2.5;

	private static InputHandler input;
	private static final Set<TileCorporeaIndex> serverIndexes = Collections.newSetFromMap(new WeakHashMap<>());
	private static final Set<TileCorporeaIndex> clientIndexes = Collections.newSetFromMap(new WeakHashMap<>());

	private static final Map<Pattern, IRegexStacker> patterns = new LinkedHashMap<>();

	/**
	 * (name) = Item name, or "this" for the name of the item in your hand
	 * (n), (n1), (n2), etc = Numbers
	 * [text] = Optional
	 * <a/b> = Either a or b
	 */
	static {
		// (name) = 1
		addPattern("(.+)", new IRegexStacker() {
			@Override
			public int getCount(Matcher m) {
				return 1;
			}

			@Override
			public String getName(Matcher m) {
				return m.group(1);
			}
		});

		// [a][n] (name) = 1
		addPattern("a??n?? (.+)", new IRegexStacker() {
			@Override
			public int getCount(Matcher m) {
				return 1;
			}

			@Override
			public String getName(Matcher m) {
				return m.group(1);
			}
		});

		//(n)[x][ of] (name) = n
		addPattern("(\\d+)x?(?: of)? (.+)", new IRegexStacker() {
			@Override
			public int getCount(Matcher m) {
				return i(m, 1);
			}

			@Override
			public String getName(Matcher m) {
				return m.group(2);
			}
		});

		// [a ]stack[ of] (name) = 64
		addPattern("(?:a )?stack(?: of)? (.+)", new IRegexStacker() {
			@Override
			public int getCount(Matcher m) {
				return 64;
			}

			@Override
			public String getName(Matcher m) {
				return m.group(1);
			}
		});

		// (n)[x] stack[s][ of] (name) = n * 64
		addPattern("(\\d+)x?? stacks?(?: of)? (.+)", new IRegexStacker() {
			@Override
			public int getCount(Matcher m) {
				return 64 * i(m, 1);
			}

			@Override
			public String getName(Matcher m) {
				return m.group(2);
			}
		});

		// [a ]stack <and/+> (n)[x][ of] (name) = 64 + n
		addPattern("(?:a )?stack (?:(?:and)|(?:\\+)) (\\d+)(?: of)? (.+)", new IRegexStacker() {
			@Override
			public int getCount(Matcher m) {
				return 64 + i(m, 1);
			}

			@Override
			public String getName(Matcher m) {
				return m.group(2);
			}
		});

		// (n1)[x] stack[s] <and/+> (n2)[x][ of] (name) = n1 * 64 + n2
		addPattern("(\\d+)x?? stacks? (?:(?:and)|(?:\\+)) (\\d+)x?(?: of)? (.+)", new IRegexStacker() {
			@Override
			public int getCount(Matcher m) {
				return 64 * i(m, 1) + i(m, 2);
			}

			@Override
			public String getName(Matcher m) {
				return m.group(3);
			}
		});

		// [a ]half [of ][a ]stack[ of] (name) = 32
		addPattern("(?:a )?half (?:of )?(?:a )?stack(?: of)? (.+)", new IRegexStacker() {
			@Override
			public int getCount(Matcher m) {
				return 32;
			}

			@Override
			public String getName(Matcher m) {
				return m.group(1);
			}
		});

		// [a ]quarter [of ][a ]stack[ of] (name) = 16
		addPattern("(?:a )?quarter (?:of )?(?:a )?stack(?: of)? (.+)", new IRegexStacker() {
			@Override
			public int getCount(Matcher m) {
				return 16;
			}

			@Override
			public String getName(Matcher m) {
				return m.group(1);
			}
		});

		// [a ]dozen[ of] (name) = 12
		addPattern("(?:a )?dozen(?: of)? (.+)", new IRegexStacker() {
			@Override
			public int getCount(Matcher m) {
				return 12;
			}

			@Override
			public String getName(Matcher m) {
				return m.group(1);
			}
		});

		// (n)[x] dozen[s][ of] (name) = n * 12
		addPattern("(\\d+)x?? dozens?(?: of)? (.+)", new IRegexStacker() {
			@Override
			public int getCount(Matcher m) {
				return 12 * i(m, 1);
			}

			@Override
			public String getName(Matcher m) {
				return m.group(2);
			}
		});

		// <all/every> [<of/the> ](name) = 2147483647
		addPattern("(?:all|every) (?:(?:of|the) )?(.+)", new IRegexStacker() {
			@Override
			public int getCount(Matcher m) {
				return Integer.MAX_VALUE;
			}

			@Override
			public String getName(Matcher m) {
				return m.group(1);
			}
		});

		// [the ]answer to life[,] the universe and everything [of ](name) = 42
		addPattern("(?:the )?answer to life,? the universe and everything (?:of )?(.+)", new IRegexStacker() {
			@Override
			public int getCount(Matcher m) {
				return 42;
			}

			@Override
			public String getName(Matcher m) {
				return m.group(1);
			}
		});

		// [a ]nice [of ](name) = 69 
		addPattern("(?:a )?nice (?:of )?(.+)", new IRegexStacker() {
			@Override
			public int getCount(Matcher m) {
				return 69;
			}

			@Override
			public String getName(Matcher m) {
				return m.group(1);
			}
		});

		// (n)[x] nice[s][ of] (name) = n * 69
		addPattern("(\\d+)x?? nices?(?: of)? (.+)", new IRegexStacker() {
			@Override
			public int getCount(Matcher m) {
				return 69 * i(m, 1);
			}

			@Override
			public String getName(Matcher m) {
				return m.group(2);
			}
		});

		// <count/show/display/tell> (name) = 0 (display only)
		addPattern("(?:count|show|display|tell) (.+)", new IRegexStacker() {
			@Override
			public int getCount(Matcher m) {
				return 0;
			}

			@Override
			public String getName(Matcher m) {
				return m.group(1);
			}
		});
	}

	public int ticksWithCloseby = 0;
	public float closeby = 0F;
	public boolean hasCloseby;

	public TileCorporeaIndex(BlockPos pos, BlockState state) {
		super(ModTiles.CORPOREA_INDEX, pos, state);
	}

	public static void commonTick(Level level, BlockPos worldPosition, BlockState state, TileCorporeaIndex self) {
		double x = worldPosition.getX() + 0.5;
		double y = worldPosition.getY() + 0.5;
		double z = worldPosition.getZ() + 0.5;

		List<Player> players = level.getEntitiesOfClass(Player.class, new AABB(x - RADIUS, y - RADIUS, z - RADIUS, x + RADIUS, y + RADIUS, z + RADIUS));
		self.hasCloseby = false;
		for (Player player : players) {
			if (isInRangeOfIndex(player, self)) {
				self.hasCloseby = true;
				break;
			}
		}

		float step = 0.2F;
		if (self.hasCloseby) {
			self.ticksWithCloseby++;
			if (self.closeby < 1F) {
				self.closeby += step;
			}
		} else if (self.closeby > 0F) {
			self.closeby -= step;
		}

		if (!self.isRemoved()) {
			addIndex(self);
		}
	}

	@Override
	public void setRemoved() {
		super.setRemoved();
		removeIndex(this);
	}

	/* TODO 1.17
	@OnlyIn(Dist.CLIENT)
	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		//The tile entity renderer can draw pink stars fairly far away from the index itself, this helps it not get culled too early.
		return new AxisAlignedBB(pos.add(-2, 0, -2), pos.add(3, 1, 3));
	}
	*/

	@Override
	public void doCorporeaRequest(ICorporeaRequestMatcher request, int count, ICorporeaSpark spark) {
		doRequest(request, count, spark);
	}

	private ICorporeaResult doRequest(ICorporeaRequestMatcher matcher, int count, ICorporeaSpark spark) {
		ICorporeaResult result = CorporeaHelper.instance().requestItem(matcher, count, spark, true);
		List<ItemStack> stacks = result.getStacks();
		spark.onItemsRequested(stacks);
		for (ItemStack stack : stacks) {
			if (!stack.isEmpty()) {
				ItemEntity item = new ItemEntity(level, worldPosition.getX() + 0.5, worldPosition.getY() + 1.5, worldPosition.getZ() + 0.5, stack);
				level.addFreshEntity(item);
			}
		}
		return result;
	}

	public static boolean isInRangeOfIndex(Player player, TileCorporeaIndex index) {
		return player.level.dimension() == index.level.dimension() && MathHelper.pointDistancePlane(index.getBlockPos().getX() + 0.5, index.getBlockPos().getZ() + 0.5, player.getX(), player.getZ()) < RADIUS && Math.abs(index.getBlockPos().getY() + 0.5 - player.getY() + (player.level.isClientSide ? 0 : 1.6)) < 5;
	}

	public static void addPattern(String pattern, IRegexStacker stacker) {
		patterns.put(Pattern.compile(pattern), stacker);
	}

	public static int i(Matcher m, int g) {
		try {
			int i = Math.abs(Integer.parseInt(m.group(g)));
			return i;
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	public static InputHandler getInputHandler() {
		if (input == null) {
			input = new InputHandler();
		}
		return input;
	}

	private static void addIndex(TileCorporeaIndex index) {
		Set<TileCorporeaIndex> set = index.level.isClientSide ? clientIndexes : serverIndexes;
		set.add(index);
	}

	private static void removeIndex(TileCorporeaIndex index) {
		Set<TileCorporeaIndex> set = index.level.isClientSide ? clientIndexes : serverIndexes;
		set.remove(index);
	}

	public static void clearIndexCache() {
		clientIndexes.clear();
		serverIndexes.clear();
	}

	public void performPlayerRequest(ServerPlayer player, ICorporeaRequestMatcher request, int count) {
		if (!CorporeaIndexRequestCallback.EVENT.invoker().onIndexRequest(player, request, count, this.getSpark())) {
			ICorporeaResult res = this.doRequest(request, count, this.getSpark());

			player.sendMessage(new TranslatableComponent("botaniamisc.requestMsg", count, request.getRequestName(), res.getMatchedCount(), res.getExtractedCount()).withStyle(ChatFormatting.LIGHT_PURPLE), Util.NIL_UUID);
			player.awardStat(ModStats.CORPOREA_ITEMS_REQUESTED, res.getExtractedCount());
			CorporeaRequestTrigger.INSTANCE.trigger(player, player.getLevel(), this.getBlockPos(), res.getExtractedCount());
		}
	}

	public static final class InputHandler {
		public boolean onChatMessage(ServerPlayer player, String message) {
			if (player.isSpectator()) {
				return false;
			}

			List<TileCorporeaIndex> nearbyIndexes = getNearbyIndexes(player);
			if (!nearbyIndexes.isEmpty()) {
				String msg = message.toLowerCase(Locale.ROOT).trim();
				for (TileCorporeaIndex index : nearbyIndexes) {
					ICorporeaSpark spark = index.getSpark();
					if (spark != null) {
						String name = "";
						int count = 0;
						for (Pattern pattern : patterns.keySet()) {
							Matcher matcher = pattern.matcher(msg);
							if (matcher.matches()) {
								IRegexStacker stacker = patterns.get(pattern);
								count = stacker.getCount(matcher);
								name = stacker.getName(matcher).toLowerCase(Locale.ROOT).trim();
							}
						}

						if (name.equals("this")) {
							ItemStack stack = player.getMainHandItem();
							if (!stack.isEmpty()) {
								name = stack.getHoverName().getString().toLowerCase(Locale.ROOT).trim();
							}
						}

						index.performPlayerRequest(player, CorporeaHelper.instance().createMatcher(name), count);
					}
				}

				return true;
			}

			return false;
		}

		public static List<TileCorporeaIndex> getNearbyIndexes(Player player) {
			return (player.level.isClientSide ? clientIndexes : serverIndexes)
					.stream().filter(i -> isInRangeOfIndex(player, i))
					.collect(Collectors.toList());
		}
	}

	public interface IRegexStacker {

		int getCount(Matcher m);

		String getName(Matcher m);

	}

}
