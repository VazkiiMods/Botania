<div class='section-header'>
	<span class='glyphicon glyphicon-book'></span>
	Lexica Botania Online
</div>

<div class='warning-txt'>
	<ul>
		<li>This page is a verbatim copy of the Lexica Botania you can craft ingame by combining a Book with any type of Sapling. It, however, does not contain any recipes or images. Usage of the ingame guide is encouraged.</li>
		<li>The contents of this page are fed from the <a href="https://github.com/Vazkii/Botania/blob/master/src/main/resources/assets/botania/lang/en_us.json"> bleeding-edge lang file</a> and may be ahead of the Botania version currently out.</li>
		<li>This page contains spoilers for unlockables!</li>
	</ul>
</div>

<?php
	$file = file_get_contents('https://raw.githubusercontent.com/Vazkii/Botania/master/src/main/resources/assets/botania/lang/en_us.json');
	$json = json_decode($file, true);
	$default_keys = [
		'botania_corporea_request' => 'c',
		'curios.open.desc' => 'k',
		'sprint' => 'CTRL'
	];
	$entry_cache = array();
	$replacements = [
		'/\$\((br|p)\)/' => "<br />",
		'/\$\(li\)/' => " &bull; ",
		'/\$\(([1-9a-o])\)([^$]+)(?:\$\([0r]?\)|(?=<)|$)/' => "<span class='mc-color-$1'>$2</span>",
		'/\$\(thing\)([^$]+)(?:\$\([0r]?\)|(?=<)|$)/' => "<span class='mc-color-4'>$1</span>",
		'/\$\(item\)([^$]+)(?:\$\([0r]?\)|(?=<)|$)/' => "<span class='mc-color-1'>$1</span>",
	];
	
	$opened_div = false;
	
	$current_entry = '';
	foreach($json as $key => $value) {
		$entry_match = match_entry($key);
		$page_match = match_page($key, $current_entry);
		if(sizeof($entry_match) > 0) {
			$current_entry = $entry_match[1];
			if($opened_div)
				echo('</div>');
			echo("<br><a href='#$current_entry' class='entry-bookmark glyphicon glyphicon-bookmark' title='Permalink'></a> <b id='$current_entry-fake'>$value</b><div class='entry-text'>");
			echo("\n");
			$opened_div = true;
		}
		if(sizeof($page_match) > 0) {
			$page = preg_replace_callback('/\$\(k:([^)]*)\)/', 'key_resolve',
				preg_replace_callback('/\$\(l:([^)]+)\)([^$]+)\$\(\/l\)/', 'link_resolve',

				preg_replace(array_keys($replacements), array_values($replacements),
				$value))) . '<br />';
			$no_control = preg_replace('/\$\(.\)/', '', $value);
			if(strlen($no_control) > 50 || strcmp($no_control, "no") == 0) {
				echo($page);
				echo("\n");
			}
		}
	}
	
	echo('</div>');
	
	function match($regex, $line) {
		$matches = array();
		preg_match("/$regex/", $line, $matches);
		return $matches;
	}
	
	function match_entry($key) {
		return match('botania\.entry\.(\w+)', $key);
	}
	
	function match_page($key, $entry_name) {
		return match("botania\.page\.$entry_name\d+", $key);
	}
	function key_resolve($match) {
		global $default_keys; // i'm sorry, i don't know php
		return (array_key_exists($match[1], $default_keys)
			? $default_keys[$match[1]]
			: "UNKNOWN KEY");
	}
	function link_resolve($match) {
		$link = $match[1];
		$res = [];
		if(preg_match('|^\w+/(\w+)$|', $link, $res)) {
			$entry_name = preg_replace_callback('/_(\w)/', function($repl) {
				return strtoupper($repl[1]);
			}, $res[1]);
			$link = "#$entry_name-fake";
		}
		return "<a href='$link'>$match[2]</a>";
	}
?>
