/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.lib.LibMisc;

import java.util.Objects;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class TinyPotatoModelProvider extends ModelProvider<TinyPotatoModelBuilder> {
	public TinyPotatoModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator, LibMisc.MOD_ID, "tiny_potato", TinyPotatoModelBuilder::new, existingFileHelper);
	}

	@Override
	protected void registerModels() {
		withExistingParent("default", BLOCK_FOLDER + "/cube_all")
				.texture("all", taterTex("default"))
				.tater()
				.from(6.0F, 0.0F, 6.0F)
				.to(10.0F, 6.0F, 10.0F)
				.end()
				.transforms()
				.transform(ModelBuilder.Perspective.HEAD)
				.translation(0F, 14.5F, 0F)
				.scale(1F, 1F, 1F)
				.end();

		String[] taters = {
				"halloween",
				"halloween", "spooky", "spooktater", null,

				"gray",
				"kyle_hyde", null,

				"trans",
				"transtater", "tategg", "eggtater", null,

				"wire",
				"wiretater", "eutrotater", "enbytater", "nbtater", "nonbinarytater", null,

				"pride",
				"pridetater", "gaytater", "gayter", "lgbtater", null,

				"bi",
				"bitater", "biter", null,

				"pan",
				"pantater", "panter", null,

				"lesbian",
				"lesbiantater", "lesbitater", "lesbiabtater",
				"lesbiamtater", "lessbientater", "girlstater", null,

				"genderfluid",
				"genderfluidtater", "taterfluid", null,

				"ace",
				"acetater", "asexualtater", "tacer", "taceter", null,

				"aro",
				"arotater", "aromantictater", "tataro", null,

				"agender",
				"agendertater", null,

				"bosnia",
				"botaniatater", "botater", "bosniantater",
				"botaniaherzegovina", "botania_herzegovina",
				"bosniaherzegovina", "bosnia_herzegovina", null,

				"aureylian",
				"aureylian", null,
		};
		for (int i = 0; i < taters.length; i++) {
			String texture = Objects.requireNonNull(taters[i]);
			String alias;
			while ((alias = taters[++i]) != null) {
				withExistingParent(alias, prefix(LibResources.PREFIX_TINY_POTATO + "/default"))
						.texture("all", taterTex(texture));
			}
		}

		withExistingParent("snorps", BLOCK_FOLDER + "/cube_all")
				.texture("all", taterTex("default"))
				.tater()
				.from(6.0F, 0.0F, 6.0F)
				.to(10.0F, 6.0F, 10.0F)
				.end()
				.tater()
				.from(4.0F, 0.0F, 7.0F)
				.to(6.0F, 3.0F, 9.0F)
				.end()
				.tater()
				.from(10.0F, 0.0F, 7.0F)
				.to(12.0F, 3.0F, 9.0F)
				.end();
		for (String alias : new String[] { "pluraltater", "manytater", "systemtater", "systater" }) {
			withExistingParent(alias, prefix(LibResources.PREFIX_TINY_POTATO + "/snorps"));
		}

		withExistingParent("dinnerbone", BLOCK_FOLDER + "/cube_all")
				.texture("all", taterTex("default"))
				.tater()
				.from(10.0F, 6.0F, 6.0F)
				.to(6.0F, 0.0F, 10.0F)
				.end();
		withExistingParent("grumm", prefix(LibResources.PREFIX_TINY_POTATO + "/dinnerbone"));

		withExistingParent("pahimar", BLOCK_FOLDER + "/cube_all")
				.texture("all", taterTex("default"))
				.tater()
				.from(6.0F, 0.0F, 6.0F)
				.to(10.0F, 1.8f, 10.0F)
				.end();

		withExistingParent("kingdaddydmac", BLOCK_FOLDER + "/cube_all")
				.texture("all", taterTex("default"))
				.tater()
				.from(6.0F, 0.0F, 6.0F)
				.to(10.0F, 6.0F, 10.0F)
				.end()
				.tater()
				.from(-2.0F, 0.0F, 6.0F)
				.to(2.0F, 6.0F, 10.0F)
				.end();
	}

	private ResourceLocation taterTex(String texture) {
		return prefix("model/tiny_potato/" + texture);
	}

	@Override
	public String getName() {
		return "Botania Tiny Potato models";
	}
}
