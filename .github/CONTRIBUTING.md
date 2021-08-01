Hey there. Looks like you want to contribute with an issue. Great. Here's what you have to do:
* Have a look through the [Botania FAQ](https://botaniamod.net/faq.php), to see if your issue has been solved already;
* Note the following points:
  * Issues regarding "Bukkit+Forge" servers that are not reproducible with Forge only are **not accepted**;
  * Issues regarding outdated versions of the mod, especially for older versions of Minecraft, are **not accepted**;
$ * Unless you manually update every mod on the pack to the latest, issues regarding any public modpack that is not officially maintained or supported by the Botania developers (the ones in the modpack section of the website are not) are **not accepted**;
  * Duplicate issues or issues that have been solved already (use the search feature!) will be closed without asking.
  * Do not tag your issues' names. "Something Broke" is prefered to "[Bug] Something Broke"  because there's a proper label system in place.
  * Suggestions are **not** accepted here. Post them on the forums or reddit instead. If there's any still open they're old ones before this rule was added.

The following "bugs" are not accepted:
* Older version issues
  * Only the latest version of the mod is supported.
* Intended Behaviour
  * Double tall flowers need be harvested with shears to not clutter the inventory
* Not a Botania problem/Not fixable
  * Mana Bursts get destroyed on plugin servers by lag-clearing plugins (add botania:mana_burst to the blacklist)
  * Force Relays destroy blocks when used very fast (Can't fix due to the nature of the block)
  * Terrasteel crafting won't start (make sure your checkerboard is correct, there's Livingrock under the plate)

[Report the Issue](https://github.com/Vazkii/Botania/issues)!

---

If you want to make a Pull Request keep these in mind:
* Do NOT use the github editor. Test your PRs before you submit them.
* We are strict when it comes to syntax. Make sure your PR's syntax matches the syntax of the rest of the code. That includes spacing after if/for/(etc), proper bracket usage, camel casing and copyleft headers on new classes.
  * Run the `checkSyntax` Gradle tasks before submitting your PR to identify bad formatting. You can use the `spotlessJavaApply` task to fix some of those issues, like license headers.
* If your pull request edits very small chunks of code and isn't flawless we might close it as it'll probably take less time to fix it ourselves rather than pull yours and change the code.
