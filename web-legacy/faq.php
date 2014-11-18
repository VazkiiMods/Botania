<!DOCTYPE html>
<html>
	<title>FAQ - Botania | vazkii.us</title>	
	<head>
		<link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css">
		<link rel="stylesheet" href="page.css">
	</head>

	<body>
		<?php include 'incl/header.php'; ?>
		<div class="container">
			<div class="content">
				<font size="6">Botania FAQ</font><br>
				<br>
				<b>Can I use Botania in a modpack?</b><br>
				Have a look at the license in the bottom of the website, it's the image in the middle. If the footer isn't there, click the little + icon.<br><br>
				
				<b>Can the flowers in this mod be retroactively generated?</b><br>
				No, they can't, due to the method they use for generation. They're generated during biome decoration, so the amount of flowers depends on how many flowers would be in that biome to begin with. You can still get the mod's flowers easilly through creating Floral Fertilizer, which is a pretty early game item.<br><br>
				
				<b>There's too many flowers! How do I lower the amount?</b><br>
				I like how many flowers there are by default, so I'm not changing that. You can lower the frequency (how often they spawn) and the density (how many spawn at once for each "patch") in the config.<br><br>
				
				<b>Could you add a config option to restrict flowers to specific biomes?</b><br>
				Given that each flower is required for the progression, no, this seems like a terrible idea. I have no interest in doing this.<br><br>
				
				<b>My Mana Pool's Mana isn't changing, I got a few Dayblooms producing and the bar doesn't move, what's wrong?</b><br>
				The Mana Pool holds a lot of mana. Dayblooms produce a very little amount of it, so you probably won't even notice the difference in the bar. Go get some better flowers like Endoflames.<br><br>
				
				<b>I found a bug, how do I report it?</b><br>
				I made a <a href="http://vazkii.us/br101/">website</a> to teach people how to report bugs, read what's on there and then go report it on the <a href="https://github.com/Vazkii/Botania/issues">issue tracker</a>.<br><br>
				
				<b>My console is getting spammed with "true" or "false" in the 1.6 version. How do I fix that?</b><br>
				I accidentally a debug in, I fixed it in 1.7, if you don't want to update, don't place down Nightshades.<br><br>
				
				<b>My game crashes with <a href="http://openeye.openmods.info/crashes/1dbc37884f9ac4ed8cce833186832e19">this log</a> or <a href="http://openeye.openmods.info/crashes/7f75eed23d85bb2d9a44393890c97cf3">this log</a>, what did I do wrong?</b><br>
				You forgot to install <a href="http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/1294623-baubles-1-0-1-5-updated-20-8-2014">Baubles</a>, silly.<br><br>
				
				<b>My game crashes with <a href="http://openeye.openmods.info/crashes/94b7e7a0917b27942913735e38c1dd3f">this log</a> when I open my inventory, how do I fix this?</b><br>
				Install the latest <a href="http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/2132152-baubles-princess-edition-1-0-0-18p-last-updated-7">Baubles</a> from my fork. Note that Thaumcraft has a tendency to download baubles even if you have the latest version, you might need to rename the file if it does that.<br><br>
				
				<b>My game has been very laggy since I installed botania, how can I fix it?</b><br>
				First off, make sure you are on the latest version, there have been lots of optimization patches, if you're still lagging, go check the mod's config. Try turning off the shaders, if that doesn't help, try setting the flower particle frequency to 0. If that doesn't do it either then I really don't know what will.<br><br>
				
				<b>Is the Terra Blade from Terraria?</b><br>
				<a href="http://vazkii.us/mod/Botania/terrablade.html">http://vazkii.us/mod/Botania/terrablade.html</a><br><br>
				
				<b>Is there a Botania API? Can I make a Botania Addon?</b><br>
				Yes, there is <a href="https://github.com/Vazkii/Botania/tree/master/MODSRC/vazkii/botania/api">an API</a>. When it comes to making addons, you can make them, but please give me a poke about what you're going to add. I hold no ability to disallow you to add some things, given the mod's license, but I at least like to know what's being done. It's not required though.
			</div>
		</div>
		
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
		<script src="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>
		<script src="jquery_cookie.min.js"></script>
		<script> $(function() { $('#header-btn-faq').addClass('active'); }); </script>
		<?php include 'incl/footer.php'; ?>
	</body>
</html>