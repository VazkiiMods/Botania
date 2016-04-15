/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [May 31, 2014, 10:31:46 PM (GMT)]
 */
package vazkii.botania.common.core.version;

import net.minecraftforge.common.MinecraftForge;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class ThreadVersionChecker extends Thread {

	private static final String LINK = "https://raw.githubusercontent.com/williewillus/Botania/MC18/version/" + MinecraftForge.MC_VERSION + ".txt";

	public ThreadVersionChecker() {
		setName("Botania Version Checker Thread");
		setDaemon(true);
		start();
	}

	@Override
	public void run() {
		try(BufferedReader r = new BufferedReader(new InputStreamReader(new URL(LINK).openStream()))) {
			VersionChecker.onlineVersion = r.readLine();
		} catch(IOException e) {
			e.printStackTrace();
		}
		VersionChecker.doneChecking = true;
	}
}
