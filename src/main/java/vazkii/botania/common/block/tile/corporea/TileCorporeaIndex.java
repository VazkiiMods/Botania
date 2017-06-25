/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Feb 15, 2015, 12:42:29 AM (GMT)]
 */
package vazkii.botania.common.block.tile.corporea;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.text.WordUtils;
import vazkii.botania.api.corporea.CorporeaHelper;
import vazkii.botania.api.corporea.ICorporeaAutoCompleteController;
import vazkii.botania.api.corporea.ICorporeaRequestor;
import vazkii.botania.api.corporea.ICorporeaSpark;
import vazkii.botania.common.core.helper.MathHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TileCorporeaIndex extends TileCorporeaBase implements ICorporeaRequestor {

	public static final double RADIUS = 2.5;

	private static InputHandler input;
	public static final Set<TileCorporeaIndex> indexes = Collections.newSetFromMap(new WeakHashMap<>());

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
			@Override public int getCount(Matcher m) { return 1; }
			@Override public String getName(Matcher m) { return m.group(1); }
		});

		// [a][n] (name) = 1
		addPattern("a??n?? (.+)", new IRegexStacker() {
			@Override public int getCount(Matcher m) { return 1; }
			@Override public String getName(Matcher m) { return m.group(1); }
		});

		//(n)[x][ of] (name) = n
		addPattern("(\\d+)x?(?: of)? (.+)", new IRegexStacker() {
			@Override public int getCount(Matcher m) { return i(m, 1); }
			@Override public String getName(Matcher m) { return m.group(2); }
		});

		// [a ]stack[ of] (name) = 64
		addPattern("(?:a )?stack(?: of)? (.+)", new IRegexStacker() {
			@Override public int getCount(Matcher m) { return 64; }
			@Override public String getName(Matcher m) { return m.group(1); }
		});

		// (n)[x] stack[s][ of] (name) = n * 64
		addPattern("(\\d+)x?? stacks?(?: of)? (.+)", new IRegexStacker() {
			@Override public int getCount(Matcher m) { return 64 * i(m, 1); }
			@Override public String getName(Matcher m) { return m.group(2); }
		});

		// [a ]stack <and/+> (n)[x][ of] (name) = 64 + n
		addPattern("(?:a )?stack (?:(?:and)|(?:\\+)) (\\d+)(?: of)? (.+)", new IRegexStacker() {
			@Override public int getCount(Matcher m) { return 64 + i(m, 1); }
			@Override public String getName(Matcher m) { return m.group(2); }
		});

		// (n1)[x] stack[s] <and/+> (n2)[x][ of] (name) = n1 * 64 + n2
		addPattern("(\\d+)x?? stacks? (?:(?:and)|(?:\\+)) (\\d+)x?(?: of)? (.+)", new IRegexStacker() {
			@Override public int getCount(Matcher m) { return 64 * i(m, 1) + i(m, 2); }
			@Override public String getName(Matcher m) { return m.group(3); }
		});

		// [a ]half [of ][a ]stack[ of] (name) = 32
		addPattern("(?:a )?half (?:of )?(?:a )?stack(?: of)? (.+)", new IRegexStacker() {
			@Override public int getCount(Matcher m) { return 32; }
			@Override public String getName(Matcher m) { return m.group(1); }
		});

		// [a ]quarter [of ][a ]stack[ of] (name) = 16
		addPattern("(?:a )?quarter (?:of )?(?:a )?stack(?: of)? (.+)", new IRegexStacker() {
			@Override public int getCount(Matcher m) { return 16; }
			@Override public String getName(Matcher m) { return m.group(1); }
		});

		// [a ]dozen[ of] (name) = 12
		addPattern("(?:a )?dozen(?: of)? (.+)", new IRegexStacker() {
			@Override public int getCount(Matcher m) { return 12; }
			@Override public String getName(Matcher m) { return m.group(1); }
		});

		// (n)[x] dozen[s][ of] (name) = n * 12
		addPattern("(\\d+)x?? dozens?(?: of)? (.+)", new IRegexStacker() {
			@Override public int getCount(Matcher m) { return 12 * i(m, 1); }
			@Override public String getName(Matcher m) { return m.group(2); }
		});

		// <all/every> [<of/the> ](name) = 2147483647
		addPattern("(?:all|every) (?:(?:of|the) )?(.+)", new IRegexStacker() {
			@Override public int getCount(Matcher m) { return Integer.MAX_VALUE; }
			@Override public String getName(Matcher m) { return m.group(1); }
		});

		// [the ]answer to life[,] the universe and everything [of ](name) = 42
		addPattern("(?:the )?answer to life,? the universe and everything (?:of )?(.+)", new IRegexStacker() {
			@Override public int getCount(Matcher m) { return 42; }
			@Override public String getName(Matcher m) { return m.group(1); }
		});

		// <count/show/display/tell> (name) = 0 (display only)
		addPattern("(?:count|show|display|tell) (.+)", new IRegexStacker() {
			@Override public int getCount(Matcher m) { return 0; }
			@Override public String getName(Matcher m) { return m.group(1); }
		});
	}

	public int ticks = 0;
	public int ticksWithCloseby = 0;
	public float closeby = 0F;
	public boolean hasCloseby;

	@Override
	public void update() {
		double x = pos.getX() + 0.5;
		double y = pos.getY() + 0.5;
		double z = pos.getZ() + 0.5;

		List<EntityPlayer> players = world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(x - RADIUS, y - RADIUS, z - RADIUS, x + RADIUS, y + RADIUS, z + RADIUS));
		hasCloseby = false;
		for(EntityPlayer player : players)
			if(isInRangeOfIndex(player, this)) {
				hasCloseby = true;
				break;
			}

		float step = 0.2F;
		ticks++;
		if(hasCloseby) {
			ticksWithCloseby++;
			if(closeby < 1F)
				closeby += step;
		} else if(closeby > 0F)
			closeby -= step;

		if(!isInvalid() && !indexes.contains(this))
			indexes.add(this);
	}

	@Override
	public void invalidate() {
		super.invalidate();
		indexes.remove(this);
	}

	@Override
	public void onChunkUnload() {
		super.onChunkUnload();
		indexes.remove(this);
	}

	@Override
	public int getSizeInventory() {
		return 0;
	}

	@Override
	public void doCorporeaRequest(Object request, int count, ICorporeaSpark spark) {
		if(!(request instanceof String))
			return;

		List<ItemStack> stacks = CorporeaHelper.requestItem((String) request, count, spark, true);
		spark.onItemsRequested(stacks);
		for(ItemStack stack : stacks)
			if(!stack.isEmpty()) {
				EntityItem item = new EntityItem(world, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, stack);
				world.spawnEntity(item);
			}
	}

	public static boolean isInRangeOfIndex(EntityPlayer player, TileCorporeaIndex index) {
		return player.world.provider.getDimension() == index.world.provider.getDimension() && MathHelper.pointDistancePlane(index.getPos().getX() + 0.5, index.getPos().getZ() + 0.5, player.posX, player.posZ) < RADIUS && Math.abs(index.getPos().getY() + 0.5 - player.posY + (player.world.isRemote ? 0 : 1.6)) < 5;
	}

	public static void addPattern(String pattern, IRegexStacker stacker) {
		patterns.put(Pattern.compile(pattern), stacker);
	}

	public static int i(Matcher m, int g) {
		try {
			int i = Math.abs(Integer.parseInt(m.group(g)));
			return i;
		} catch(NumberFormatException e) {
			return 0;
		}
	}

	public static InputHandler getInputHandler() {
		if(input == null)
			input = new InputHandler();
		return input;
	}

	public static final class InputHandler implements ICorporeaAutoCompleteController {

		public InputHandler() {
			CorporeaHelper.registerAutoCompleteController(this);
		}

		@SubscribeEvent(priority = EventPriority.HIGHEST)
		public void onChatMessage(ServerChatEvent event) {
			List<TileCorporeaIndex> nearbyIndexes = getNearbyIndexes(event.getPlayer());
			if(!nearbyIndexes.isEmpty()) {
				String msg = event.getMessage().toLowerCase().trim();
				for(TileCorporeaIndex index : nearbyIndexes) {
					if(index.world.isRemote)
						continue;

					ICorporeaSpark spark = index.getSpark();
					if(spark != null) {
						String name = "";
						int count = 0;
						for(Pattern pattern : patterns.keySet()) {
							Matcher matcher = pattern.matcher(msg);
							if(matcher.matches()) {
								IRegexStacker stacker = patterns.get(pattern);
								count = stacker.getCount(matcher);
								name = stacker.getName(matcher).toLowerCase().trim();
								pattern.toString();
							}
						}

						if(name.equals("this")) {
							ItemStack stack = event.getPlayer().getHeldItemMainhand();
							if(!stack.isEmpty())
								name = stack.getDisplayName().toLowerCase().trim();
						}

						index.doCorporeaRequest(name, count, spark);

						event.getPlayer().sendMessage(new TextComponentTranslation("botaniamisc.requestMsg", count, WordUtils.capitalizeFully(name), CorporeaHelper.lastRequestMatches, CorporeaHelper.lastRequestExtractions).setStyle(new Style().setColor(TextFormatting.LIGHT_PURPLE)));
					}
				}

				event.setCanceled(true);
			}
		}

		public static List<TileCorporeaIndex> getNearbyIndexes(EntityPlayer player) {
			List<TileCorporeaIndex> indexList = new ArrayList<>();
			for(TileCorporeaIndex index : indexes)
				if(isInRangeOfIndex(player, index) && index.world.isRemote == player.world.isRemote)
					indexList.add(index);
			return indexList;
		}

		@Override
		@SideOnly(Side.CLIENT)
		public boolean shouldAutoComplete() {
			return !getNearbyIndexes(Minecraft.getMinecraft().player).isEmpty();
		}

	}

	public static interface IRegexStacker {

		public int getCount(Matcher m);

		public String getName(Matcher m);

	}

}
