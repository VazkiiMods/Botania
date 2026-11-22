# Contribution Guide
## Reporting Issues
* Have a look through the [Botania FAQ](https://botaniamod.net/faq.html), to see if your issue has been solved already
* Issue reporting is not a place for gameplay help. Join our Discord or forums for that.
* The following types of reports are **not accepted**:
  * Issue reports generated entirely using AI tools
  * Issues regarding outdated versions of the mod, especially for older versions of
    Minecraft (fixes for critical bugs in the current version are sometimes backported, though)
  * Issues caused by running Botania or any conflicting mod/plugin through a compatibility layer
    (such as Sinytra Connector, Cardboard, etc.)
  * Feature suggestions. In general, new features are added to Botania on a must-have basis.
    The bar for new features is quite high. (Consider discussing such suggestions in our
    Discord or the forums, maybe some add-on authors are interested in them.)
* Duplicate issues or issues that have been solved already (use the search feature!) will
  be closed without warning.
* Do not "tag" your issues' names. "Something Broke" is preferred to "\[Bug] Something Broke"
  because there's a proper label system in place.
* If possible, provide a minimum reproduction case. Issues are more likely to be looked at if
  they only involve a couple of other mods.
* Always include logs from a run where the issue happened, even if it wasn't a crash. Close the
  game, then grab the log file from /logs/latest.log in your instance folder. Upload the log via
  one of the recommended ways and provide a link. *Do not* paste the log itself into the report.
* Provide as much additional information as possible, including a description how to trigger
  the faulty behavior. If we can't make sense of a bug report, we can't fix the problem.

## Submitting Changes
* Before making many or larger changes, consider getting in contact with us via the Discord
  or forum, especially if code changes could modify potentially intended behaviors or visuals.
* Run the `spotlessCheck` Gradle task to make sure your changes pass our style guidelines.
* The `spotlessJavaApply` Gradle task can fix most violations for you.
* Target the default branch (i.e. usually the one for the latest released version) of the
  repository in all PR's (unless it's a hotfix that only applies to an older version).
  The maintainers will cherry-pick things back ourselves when creating hotfixes.
* **Keep PR's small**. The smaller it is, the faster we will review it. Only fix one thing
  per PR instead of piling everything into one massive PR.
* Please write descriptive commit messages with proper separation between the title and
  summary of the message. Do not write commit messages consisting only of "Fix #123", that
  means nothing to e.g. someone reading the logs offline without access to GitHub. On the
  other hand, also keep commit messages concise. If a commit contains so many changes that
  a comprehensive summary is necessary, maybe it should be split up.
* If you fix a gameplay bug or add a new gameplay feature, consider adding a GameTest for
  it (see below)
* If applicable, add an entry to the changelog in `web/changelog.md` under the "Upcoming"
  section.

## Notes on Using AI Tools
This mod is published on Modrinth, which has rules on the
[use of generative AI](https://modrinth.com/legal/rules#generative-ai). Currently, Botania does
not require disclosure under those rules, and, frankly, we would prefer to keep it that way.

That said, we can obviously not prevent contributors from using tools based on that technology
to analyze or modify Botania. However, if you use such tools, pay attention to the following
guidelines:
* While we generally appreciate the initiative of submitting a PR, sometimes it makes sense to
  talk to us first, either in the form of a bug report, or by joining the Discord or forums.
  This is especially true if you act based on an AI tool's analysis of the code, or if you plan
  on making many or larger changes.
* Keep the logic self-explanatory, if possible. While this is true for any contributions, it's
  especially important when generative AI is involved. If the tool needs to "brain-dump" in a
  comment for the code to make sense, the logic is probably not written in a maintainer-friendly
  way and could get rejected.
* **Commits must be made *only* by you.** The commit message must summarize the changes in *your
  own words*. As mentioned in the previous section, commit messages should be descriptive, but
  they should also stay reasonably short.\
  **We will reject PRs if commit messages or metadata declare AI tool involvement.**
  (The place to disclose this is the PR message.)
* PRs must not be created by a tool. Use *your own words* to describe the changes made by the
  commit(s) in the PR. Don't just copy/paste tool-generated summaries or descriptions. The PR is
  the place to include your reasons for the change, and disclose the scope of your AI tool usage.\
  **We may reject PRs if we suspect undisclosed AI tool usage or large amounts of AI-generated
  code.**
* We reserve the right to use concepts from rejected PRs to fix issues or implement improvements
  made aware to us this way.

## Branching Discipline (for maintainers)
Each major Minecraft version has a dedicated Git branch, the primary support version is marked
as the default on web UI's. All new changes should be targeted to the primary support branch,
and backported as necessary to older branches via cherry-picking.
(Hotfixes aside, every Minecraft release should be considered a "major" version nowadays.)

As much as possible, we *do not* use merge commits. They clutter the log and make
bisecting very annoying. When obtaining remote changes, use `git pull origin <branch>
--rebase`.  When merging PR's, use the "Rebase and Merge " or "Squash and Merge"
options.  For the latter, GitHub will usually generate a commit message concatenating all
the individual commit messages together, you will likely want to consider writing a better
message.

## Making a Release (for maintainers)
1. Pull from remote, test all changes, and commit everything.
2. Make sure the changelog in `web/changelog.md` is up to date, then change the "Upcoming"
   version of the section to the version to be released, and start a new "Upcoming"
   section. Commit this.
3. Run `git tag -a release-<VERSION>`. All Botania versions *must* follow the version
   format `<MC-VER>-INT`, so it'll probably look like `git tag -a release-1.16.3-407`.  If
   you don't remember which version is next, look at the `build_number` in
   `gradle.properties`.
4. Increment the `build_number` in `gradle.properties` to the next version
   (one greater than the version you just tagged). Commit this.
5. Push the branch and the tag you just made: `git push origin <branch> <release tag>`
6. CI should automatically build and upload the release to GitHub releases, CurseForge, and Modrinth. Double check
   those places to make sure it made it there properly. The release script is at `scripts/upload_releases.sh` if you need
   to debug. Note that if the required Java version ever changes, the script will need adjusting.
7. Announce the release on the Violet Moon forums under the `Botania` major tag and `sns-mirror` minor tag, which will mirror
   the announcement to Discord's #mod_updates channel and the Violet Moon Twitter account.

## Working with GameTest
1. Create a structure if wanted:
   1. Run `/test create <size>` to generate a test platform of the desired size. IMPORTANT:
      All tests should size themselves appropriately to ensure they don't interfere with
      other tests. E.g. a test testing a block with max radius 8 should leave at least 8
      blocks of room on all sides.
   2. Build the test setup then save the structure, *without a namespace*. Then run `/test
      export <thename>`.
   3. The command will print the location of the saved `.snbt` file in
      `run/gameteststructures`.
   4. Copy the file to `src/main/resources/data/botania/gametest/structures`.
2. Create a class in `src/main/java/vazkii/botania/test`. Fill it with methods annotated
   with `@GameTest`, see the other tests for examples.
3. List the class in the `fabric-gametest` entrypoint in Botania's `fabric.mod.json`.

Tips:
* The @GameTest annotation takes a `batch` argument. Any tests in the same batch run in
  parallel. Tests in different batches will not run together.
* Please keep 5 blocks of padding in the N/S/E/W directions of all tests that have mana
  pools, spreaders, or flowers in them. This prevents them interfering with the tests
  about seeing whether flowers bind to the closest spreader, if Gametest happens to put
  them next to each other.
* If your test has a *lot* of air blocks in it, use structure voids to keep the filesize
  down. If you forget, run a regex find-and-replace on the `snbt` file, replacing
  `.*minecraft:air.*\n` with the empty string.

To run tests in-game: open the game normally, make sure you're not standing near anything
you care about, and use `/test runall`. (Gametest uses a default superflat with Generate
Structures, doMobSpawning, and doWeatherCycle all disabled as its testing environment.)

To run tests headlessly (all of these do the same thing):
* Use the `Minecraft Game Test` run configuration in your IDE.
* Launch the server with the argument `-Dfabric-api.gametest=1`.
* Use `./gradlew runGameTest`. (GitHub Actions does this)

After running tests headlessly, the testing world will be saved to `run/world`. Copy this
directory into `run/saves` and it will appear as a save file in singleplayer.
