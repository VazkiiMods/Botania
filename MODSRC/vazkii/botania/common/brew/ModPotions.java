/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Nov 1, 2014, 7:00:29 PM (GMT)]
 */
package vazkii.botania.common.brew;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import net.minecraft.potion.Potion;
import vazkii.botania.common.brew.potion.PotionSoulCross;

public class ModPotions {

	public static Potion soulCross;
	
	public static void init() {
		if(Potion.potionTypes.length < 256)
			extendPotionArray();
	
		soulCross = new PotionSoulCross();
	}

	// This is the method that everyone and their mom uses right?
	// Source: http://www.minecraftforge.net/wiki/Potion_Tutorial
	private static void extendPotionArray() {
		Potion[] potionTypes = null;

		for(Field f : Potion.class.getDeclaredFields()) {
			f.setAccessible(true);
			try {
				if(f.getName().equals("potionTypes") || f.getName().equals("field_76425_a")) {
					Field modfield = Field.class.getDeclaredField("modifiers");
					modfield.setAccessible(true);
					modfield.setInt(f, f.getModifiers() & ~Modifier.FINAL);

					potionTypes = (Potion[]) f.get(null);
					Potion[] newPotionTypes = new Potion[256];
					System.arraycopy(potionTypes, 0, newPotionTypes, 0, potionTypes.length);
					f.set(null, newPotionTypes);
				}
			} catch (Exception e) {
				System.err.println("Botania done goofed:");
				System.err.println(e);
			}
		}
	}

}
