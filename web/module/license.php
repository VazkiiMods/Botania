<div class='section-header'>
	<span class='glyphicon glyphicon-briefcase'></span>
	Botania License
</div>

<?php
	$mod_name = 'Botania';
	$mod_creator = 'Vazkii';
	
	$clauses = '';
	
	// Clauses
	$clause_personal_permission = build_clause('Personal Permission', 'thumbs-down', 'I will not give personal permission unless I know the person asking or there\'s a really good reason for it, as giving personal permission implies endorsement. Asking for personal permission will likely just end up having you ignored or redirected back to this page.');
	
	$clause_waive = build_clause('Waive', 'thumbs-up', 'All restrictive clauses in this license can be ignored with personal permission. Read the above clause.');
	
	$clause_extensive =  build_clause('Extensive', 'th-large', "This license applies to $mod_name, the $mod_name website, the legacy $mod_name website and all other code, assets or binaries found on this website or the github repository unless otherwise indicated.");
	
	$clause_non_court = build_clause('Informal', 'file', 'This license will not hold up in court. I have no intention of suing anyone for breaking it, the worst that can happen to you if you do is that I\'ll get annoyed and ask you to rectify your mistake. If you use a public distribution platform (such as CurseForge) it\'s also possible your project will get taken down. Breaking the license also makes you a jerk, and you don\'t want to be a jerk, do you?');
	
	$clause_attribution = build_clause('Attribution', 'user', "You must give appropriate credit to $mod_creator as the creator of $mod_name or the parts of it you're using. If you do any alterations the fact that you do so should also be indicated. A link back is optional but it would be cool if you would do so.");
	
	$clause_non_monetary = build_clause('Non-Monetary', 'usd', 'You may not charge for access to the distribution itself or gain money through it, this includes any type of inline advertisement, such as url shorteners (adf.ly or otherwise) or ads in your service slowing the download down. This includes restricting any amount of access behind a paywall. Charging for ingame goods such as mod items or cosmetic features on a server does not count as distribution and falls purely under the Mojang Terms of Service.');
	
	$clause_thief = build_clause('Thief', 'lock', "You must not claim that you made $mod_name. Giving appropriate credit to $mod_creator as the creator of $mod_name makes you cooler, but you don't have to do it if you don't want to.");
	
	$clause_open_source = build_clause('Copyleft', 'random', 'Your project must be open source (have its source visible and allow for redistribution and modification) and include a clause similar to this one in its license.');
	
	$clause_api = build_clause('API', 'asterisk', 'Any of the other clauses under this section do not apply to any Botania API code. However, if Botania API code is used, it must be included verbatim as it was obtained. Furthermore, the package-info.java file included with the API must be present, and in the right spot, for any mods that package any compiled API classes within to prevent conflicts.<br><i>Learn more about the API annotation <a href="https://github.com/Minalien/BlogArchive/blob/master/ForgeTutorials/Spotlight__API_Annotation.md">here</a>.');

	function add_clause($clause) {
		global $clauses;
		$clauses .= $clause;
	}
	
	function build_clause($name, $glyphicon, $desc) {	
		return "
		<div class='license-clause'>
			<div class='license-clause-icon'>
					<span class='glyphicon glyphicon-$glyphicon'></span>
			</div>
			<div class='license-clause-info'>
				<div class='license-clause-name'>
					<span class='text-muted'><b>$name</b> Clause</span>
				</div>
				<div class='license-clause-desc'>
					$desc
				</div>
			</div>
		</div>";
	}
	
	function print_license_point($header, $info) {
		global $clauses;
		echo "<hr>
			<div class='license-point'>
				<div class='license-point-header'>
					<span class='text-muted'>$header</span>
				</div>
				<div class='license-point-info'>
					$info
				</div>

				<div class='license-clauses'>
					$clauses
				</div>
			</div>
		";
		
		$clauses = '';
	}
?>

<b><?php echo $mod_name ?> is distributed under a reasonably open license. You probably want to have a read through this if you want to do something with it other than just play.</b><br>
You are completely free and have the right to <b>Use</b>, <b>Share</b> and <b>Adapt</b> the mod. These rights can not be removed from you as long as follow the license terms.<br>
If you simply wish to play with the mod and do nothing else, go for it, this page is of no use to you.

<?php
	add_clause($clause_personal_permission);
	add_clause($clause_waive);
	add_clause($clause_extensive);
	add_clause($clause_non_court);
	print_license_point('<b>General Clauses</b>', 'Clauses that apply in general to any of the uses that follow.');

	add_clause($clause_attribution);
	add_clause($clause_non_monetary);
	print_license_point("If you want to <b>Distribute $mod_name</b>", '"Distribution" refers to making available binaries, assets, or source of the mod available from the original sources as part of your modpack or otherwise.');
	
	add_clause($clause_thief);
	print_license_point("If you want to <b>Feature $mod_name</b>", "\"Featuring\" refers to using $mod_name in an environment where you do not distribute binaries, assets, or source. An example would be a YouTube Let's Play.");
	
	add_clause($clause_attribution);
	add_clause($clause_open_source);
	add_clause($clause_api);
	print_license_point("If you want to <b>Use $mod_name Code or Assets</b>", 'Usage of code or assets falls under the Extensive Clause.');
?>