<div class='section-header'>
	<span class='glyphicon glyphicon-book'></span>
	Lexica Botania Online
</div>

<div class='warning-txt'>
	<ul>
		<li>This page is a verbatim copy of the Lexica Botania you can craft ingame by combining a Book with any type of Sapling. It, however, does not contain any recipes or images. Usage of the ingame guide is encouraged.</li>
		<li>The contents of this page are fed from the <a href="https://github.com/Vazkii/Botania/blob/master/src/main/resources/assets/botania/lang/en_US.lang"> bleeding-edge lang file</a> and may be ahead of the Botania version currently out.</li>
		<li>This page contains spoilers for unlockables!</li>
	</ul>
</div>

<?php
	$file = file('https://raw.githubusercontent.com/Vazkii/Botania/master/src/main/resources/assets/botania/lang/en_us.lang');
	
	$started_docs = false;
	$opened_div = false;
	
	$current_entry = '';
	foreach($file as $line) {
		if($started_docs || trim($line) == '# LEXICON ENTRIES') {
			$started_docs = true;
			$entry_match = match_entry($line);
			$page_match = match_page($line, $current_entry);
			
			if(sizeof($entry_match) > 0) {
				$current_entry = $entry_match[1];
				if($opened_div)
					echo('</div>');
					
				echo("<br><a href='#$current_entry' class='entry-bookmark glyphicon glyphicon-bookmark' title='Permalink'></a> <b id='$current_entry-fake'>$entry_match[2]</b><div class='entry-text'>");
				$opened_div = true;
			}	
				
			if(sizeof($page_match) > 0) {
				$page =  preg_replace('/&(.)((?:[^&])+)(?:(?:&(?:0|r))|$)/', "<span class='mc-color-$1'>$2</span>", $page_match[1]) . '<br>';
				$no_control =  preg_replace('/&./', '', $page_match[1]);
				if(strlen($no_control) > 50)
					echo($page);
			}
		}
	}
	
	echo('</div>');
	
	function match($regex, $line) {
		$matches = array();
		preg_match("/$regex/", $line, $matches);
		return $matches;
	}
	
	function match_entry($line) {
		return match('botania\.entry\.(\w+)=(.+)', $line);
	}
	
	function match_page($line, $entry_name) {
		return match("botania\.page\.$entry_name\d+=(.+)", $line);
	}
?>