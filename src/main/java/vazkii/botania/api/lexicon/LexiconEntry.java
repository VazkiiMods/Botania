/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 14, 2014, 6:17:06 PM (GMT)]
 */
package vazkii.botania.api.lexicon;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.lexicon.page.PageBrew;
import vazkii.botania.common.lexicon.page.PageCraftingRecipe;
import vazkii.botania.common.lexicon.page.PageElvenRecipe;
import vazkii.botania.common.lexicon.page.PageImage;
import vazkii.botania.common.lexicon.page.PageManaInfusionRecipe;
import vazkii.botania.common.lexicon.page.PagePetalRecipe;
import vazkii.botania.common.lexicon.page.PageRuneRecipe;
import vazkii.botania.common.lexicon.page.PageTerrasteel;
import vazkii.botania.common.lexicon.page.PageText;
import vazkii.botania.common.lib.LibLexicon;

import javax.annotation.Nonnull;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LexiconEntry implements Comparable<LexiconEntry> {

	public final String unlocalizedName;
	public final LexiconCategory category;

	private KnowledgeType type = BotaniaAPI.basicKnowledge;

	public final List<LexiconPage> pages = new ArrayList<>();
	private boolean priority = false;
	private ItemStack icon = ItemStack.EMPTY;

	private final List<ItemStack> extraDisplayedRecipes = new ArrayList<>();

	/**
	 * @param unlocalizedName The unlocalized name of this entry. This will be localized by the client display.
	 */
	public LexiconEntry(String unlocalizedName, LexiconCategory category) {
		this.unlocalizedName = unlocalizedName;
		this.category = category;
	}

	/**
	 * Sets this page as prioritized, as in, will appear before others in the lexicon.
	 */
	public LexiconEntry setPriority() {
		priority = true;
		return this;
	}

	/**
	 * Sets the Knowledge type of this entry.
	 */
	public LexiconEntry setKnowledgeType(KnowledgeType type) {
		this.type = type;
		return this;
	}

	public KnowledgeType getKnowledgeType() {
		return type;
	}

	/**
	 * Sets the display icon for this entry. Overriding the one already there. When adding recipe pages to the
	 * entry, this will be called once for the result of the first found recipe.
	 */
	public void setIcon(ItemStack stack) {
		icon = stack;
	}

	public ItemStack getIcon() {
		return icon;
	}

	public boolean isPriority() {
		return priority;
	}

	public String getUnlocalizedName() {
		return unlocalizedName;
	}

	public String getTagline() {
		return ""; // Override this if you want a tagline. You probably do
	}

	@OnlyIn(Dist.CLIENT)
	public boolean isVisible() {
		return true;
	}

	public void dump() {
		File dir = Paths.get(".", "entries", category.getUnlocalizedName().substring(LibLexicon.CATEGORY_PREFIX.length())).toFile();
		if(!dir.exists())
			dir.mkdirs();
		File f = new File(dir, this.unlocalizedName + ".json");
		if (f.exists())
			throw new RuntimeException("preexisting file " + f.getAbsolutePath());
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		Type typ = new TypeToken<Map<String, Object>>() {}.getType();
		try (OutputStreamWriter os = new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(f)))) {
			Map<String, Object> entry = new LinkedHashMap<>();
			entry.put("name", this.getUnlocalizedName());
			entry.put("category", category.getUnlocalizedName().substring("botania.category.".length()));
			entry.put("icon", getIcon().getItem().getRegistryName().toString());
			if (isPriority())
				entry.put("priority", true);
			List<Object> pages = new ArrayList<>();
			for(LexiconPage page : this.pages) {
				Map<String, Object> json = new LinkedHashMap<>();
				if (page instanceof PageText) {
					json.put("type", "text");
					json.put("text", page.getUnlocalizedName());
				} else if (page instanceof PageImage) {
					json.put("type", "image");
					json.put("text", page.getUnlocalizedName());
					json.put("images", Collections.singletonList(((PageImage) page).resource.toString()));
				} else if (page instanceof PageCraftingRecipe) {
				    List<IRecipe<?>> recipes = ((PageCraftingRecipe) page).getRecipes();
					json.put("type", recipes.size() == 1 ? "crafting" : "crafting_multi");
					json.put("text", page.getUnlocalizedName());
					if (recipes.size() == 1)
						json.put("recipe", recipes.get(0).getId().toString());
					else
						json.put("recipes", recipes.stream().map(r -> r.getId().toString()).collect(Collectors.toList()));
				} else if (page instanceof PageManaInfusionRecipe) {
					json.put("type", "mana_infusion");
					json.put("text", page.getUnlocalizedName());
					json.put("recipes", ((PageManaInfusionRecipe) page).getRecipes().stream().map(r -> r.getId().toString()).collect(Collectors.toList()));
				} else if (page instanceof PagePetalRecipe) {
					json.put("type", "petal_apothecary");
					json.put("text", page.getUnlocalizedName());
					json.put("recipes", ((PagePetalRecipe) page).getRecipes().stream().map(r -> r.getId().toString()).collect(Collectors.toList()));
				} else if (page instanceof PageRuneRecipe) {
					json.put("type", "runic_altar");
					json.put("text", page.getUnlocalizedName());
					json.put("recipes", ((PageRuneRecipe) page).getRecipes().stream().map(r -> r.getId().toString()).collect(Collectors.toList()));
				} else if (page instanceof PageElvenRecipe) {
					json.put("type", "elven_trade");
					json.put("text", page.getUnlocalizedName());
					json.put("recipes", ((PageElvenRecipe) page).getRecipes().stream().map(r -> r.getId().toString()).collect(Collectors.toList()));
				} else if (page instanceof PageBrew) {
					json.put("type", "brew");
					json.put("text", page.getUnlocalizedName());
					json.put("recipe", ((PageBrew) page).recipe.getId());
				} else {
					json.put("type", "_unknown_" + page.getClass().getSimpleName());
					json.put("text", page.getUnlocalizedName());
				}
				pages.add(json);
			}
			entry.put("pages", pages);
			os.write(gson.toJson(entry));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sets what pages you want this entry to have.
	 */
	public LexiconEntry setLexiconPages(LexiconPage... pages) {
		this.pages.addAll(Arrays.asList(pages));

		for(int i = 0; i < this.pages.size(); i++) {
			LexiconPage page = this.pages.get(i);
			if(!page.skipRegistry)
				page.onPageAdded(this, i);
		}

		return this;
	}

	/**
	 * Returns the web link for this entry. If this isn't null, looking at this entry will
	 * show a "View Online" button in the book. The String returned should be the URL to
	 * open when the button is clicked.
	 */
	public String getWebLink() {
		return null;
	}

	/**
	 * Adds a page to the list of pages.
	 */
	public void addPage(LexiconPage page) {
		pages.add(page);
	}

	public final String getNameForSorting() {
		// todo used to be localized
		return (priority ? 0 : 1) + getUnlocalizedName();
	}

	public List<ItemStack> getDisplayedRecipes() {
		ArrayList<ItemStack> list = new ArrayList<>();
		for(LexiconPage page : pages) {
			ArrayList<ItemStack> itemsAddedThisPage = new ArrayList<>();

			for(ItemStack s : page.getDisplayedRecipes()) {
				addItem: {
					for(ItemStack s1 : itemsAddedThisPage)
						if(s1.getItem() == s.getItem())
							break addItem;
					for(ItemStack s1 : list)
						if(s1.isItemEqual(s) && ItemStack.areItemStackTagsEqual(s1, s))
							break addItem;

					itemsAddedThisPage.add(s);
					list.add(s);
				}
			}
		}

		list.addAll(extraDisplayedRecipes);

		return list;
	}

	public void addExtraDisplayedRecipe(ItemStack stack) {
		extraDisplayedRecipes.add(stack);
	}

	@Override
	public int compareTo(@Nonnull LexiconEntry o) {
		return getNameForSorting().compareTo(o.getNameForSorting());
	}

}