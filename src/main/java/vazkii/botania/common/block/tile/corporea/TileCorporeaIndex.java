/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile.corporea;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ObjectHolder;

import vazkii.botania.api.corporea.*;
import vazkii.botania.common.advancements.CorporeaRequestTrigger;
import vazkii.botania.common.core.helper.MathHelper;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TileCorporeaIndex extends TileCorporeaBase implements ICorporeaRequestor, ITickableTileEntity {
	@ObjectHolder(LibMisc.MOD_ID + ":" + LibBlockNames.CORPOREA_INDEX) public static TileEntityType<TileCorporeaIndex> TYPE;
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

	public int ticks = 0;
	public int ticksWithCloseby = 0;
	public float closeby = 0F;
	public boolean hasCloseby;

	public TileCorporeaIndex() {
		super(TYPE);
	}

	@Override
	public void tick() {
		double x = pos.getX() + 0.5;
		double y = pos.getY() + 0.5;
		double z = pos.getZ() + 0.5;

		List<PlayerEntity> players = world.getEntitiesWithinAABB(PlayerEntity.class, new AxisAlignedBB(x - RADIUS, y - RADIUS, z - RADIUS, x + RADIUS, y + RADIUS, z + RADIUS));
		hasCloseby = false;
		for (PlayerEntity player : players) {
			if (isInRangeOfIndex(player, this)) {
				hasCloseby = true;
				break;
			}
		}

		float step = 0.2F;
		ticks++;
		if (hasCloseby) {
			ticksWithCloseby++;
			if (closeby < 1F) {
				closeby += step;
			}
		} else if (closeby > 0F) {
			closeby -= step;
		}

		if (!isRemoved()) {
			addIndex(this);
		}
	}

	@Override
	public void remove() {
		super.remove();
		removeIndex(this);
	}

	@Override
	public void onChunkUnloaded() {
		super.onChunkUnloaded();
		removeIndex(this);
	}

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
				ItemEntity item = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, stack);
				world.addEntity(item);
			}
		}
		return result;
	}

	public static boolean isInRangeOfIndex(PlayerEntity player, TileCorporeaIndex index) {
		return player.dimension == index.world.getDimension().getType() && MathHelper.pointDistancePlane(index.getPos().getX() + 0.5, index.getPos().getZ() + 0.5, player.getPosX(), player.getPosZ()) < RADIUS && Math.abs(index.getPos().getY() + 0.5 - player.getPosY() + (player.world.isRemote ? 0 : 1.6)) < 5;
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
		Set<TileCorporeaIndex> set = index.world.isRemote ? clientIndexes : serverIndexes;
		set.add(index);
	}

	private static void removeIndex(TileCorporeaIndex index) {
		Set<TileCorporeaIndex> set = index.world.isRemote ? clientIndexes : serverIndexes;
		set.remove(index);
	}

	public void performPlayerRequest(ServerPlayerEntity player, ICorporeaRequestMatcher request, int count) {
		CorporeaIndexRequestEvent indexReqEvent = new CorporeaIndexRequestEvent(player, request, count, this.getSpark());
		if (!MinecraftForge.EVENT_BUS.post(indexReqEvent)) {
			ICorporeaResult res = this.doRequest(request, count, this.getSpark());

			player.sendMessage(new TranslationTextComponent("botaniamisc.requestMsg", count, request.getRequestName(), res.getMatchedCount(), res.getExtractedCount()).applyTextStyle(TextFormatting.LIGHT_PURPLE));
			CorporeaRequestTrigger.INSTANCE.trigger(player, player.getServerWorld(), this.getPos(), res.getExtractedCount());
		}
	}

	public static final class InputHandler {
		@SubscribeEvent(priority = EventPriority.HIGHEST)
		public void onChatMessage(ServerChatEvent event) {
			if (event.getPlayer().isSpectator()) {
				return;
			}

			List<TileCorporeaIndex> nearbyIndexes = getNearbyIndexes(event.getPlayer());
			if (!nearbyIndexes.isEmpty()) {
				String msg = event.getMessage().toLowerCase().trim();
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
								name = stacker.getName(matcher).toLowerCase().trim();
							}
						}

						if (name.equals("this")) {
							ItemStack stack = event.getPlayer().getHeldItemMainhand();
							if (!stack.isEmpty()) {
								name = stack.getDisplayName().getString().toLowerCase().trim();
							}
						}

						index.performPlayerRequest(event.getPlayer(), CorporeaHelper.instance().createMatcher(name), count);
					}
				}

				event.setCanceled(true);
			}
		}

		public static List<TileCorporeaIndex> getNearbyIndexes(PlayerEntity player) {
			return (player.world.isRemote ? clientIndexes : serverIndexes)
					.stream().filter(i -> isInRangeOfIndex(player, i))
					.collect(Collectors.toList());
		}
	}

	public interface IRegexStacker {

		int getCount(Matcher m);

		String getName(Matcher m);

	}

}
