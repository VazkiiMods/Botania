Hey there. Looks like you want to contribute with an issue. Great. Here's what you have to do:
* Have a look through the [Botania FAQ](http://botaniamod.net/faq.php), to see if your issue has been solved already;
* Read through the steps on [Bug Reporting 101](http://vazkii.us/br101/) (You may skip this if you're already familiar with reporting issues, but it's good for newcomers);
* Note the following points:
  * Issues regarding MCPC+/Cauldron that are not reproducible with forge only are **not accepted**;
  * Issues regarding outdated versions of the mod, especially for older versions of Minecraft, are **not accepted**;
  * Unless you manually update every mod on the pack to the latest, issues regarding any public modpack that is not officially maintained or supported by the botania developers (the ones in the modpack section of the website are not) are **not accepted**;
  * Duplicate issues or issues that have been solved already (use the search feature!) will be closed without asking.
  * Do not tag your issues' names. "Something Broke" is prefered to "[Bug] Something Broke"  because there's a proper label system in place.
  * Suggestions are **not** accepted here. Post them on the forums or reddit instead. If there's any still open they're old ones before this rule was added.

The following "bugs" are not accepted:
* Intended Behaviour
  * Double tall flowers need be harvested with shears to not clutter the inventory
* Already fixed but people keep reporting it for some reason
  * Wearing a Ring of Loki or Ring of the Aesir shows a wireframe below bedrock
* Not a botania problem/Not fixable
  * Crash on startup with ChromatiCraft installed (update CC) 
  * Crash on startup with Blocks3D Mod installed
  * Crash on opening the Lexica Botania with Enchiridion installed (update Enchiridion)
  * Flowers don't render (Optifine issue)
  * Mana Bursts get destroyed on Cauldron by the ClearLagg plugin (add botania:manaBurst to the ClearLagg blacklist)
  * Force Relays destroy blocks when used very fast (Can't fix due to the nature of the block)
  * Terrasteel crafting won't start (make sure your checkerboard is correct, there's Livingrock under the plate)
  * The Horn of the Canopy doesn't break Thaumcraft leaves (Thaumcraft doesn't use the typical class or material)
  * Some Baubles don't work properly on LAN (Baubles bug)

[Report the Issue](https://github.com/Vazkii/Botania/issues)!

You can find me at most times on [#vazkii @ irc.esper.net](http://webchat.esper.net/?channels=vazkii), if you need to speak with me about the issue you would report. Just make sure to use common sense and read what the bot tells you.

---

If you want to make a Pull Request keep these in mind:
* Do NOT use the github editor. Test your damn PRs before you submit them.
* I'm very strict when it comes to syntax. Make sure your PR's syntax matches the syntax of the rest of the code. That includes spacing after if/for/(etc), proper bracket usage, camel casing and copyleft headers on new classes.
* If your pull request edits very small chunks of code and isn't flawless I'll close it as it'll probably take less time to fix it myself rather than pull yours and change the code.
* Unless you have a very good reason for it, keep your PRs all in one commit. You can do it with [rebase](https://git-scm.com/docs/git-rebase).


