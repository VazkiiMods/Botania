<?php
	include 'dl.php';
				
	$files = scandir('files/');
	natsort($files);
	$files = array_reverse($files);
	$versions = parse_ini_file('versions.ini');
	$first = true;
	$total_dls = 0;
	$downloads_str = '';
	$last_version = '';
	$old = false;
	
	foreach($files as $file)
		if(strlen($file) > 2 && substr($file, -4) == '.jar' && !strpos($file, 'Glass')) {
			$dls_num = get_dls($file);
			$total_dls += intval($dls_num);
			$dls = number_format($dls_num);
										
			$file_version = substr($file, strlen('Botania '));
			$version_name = substr($file_version, 0, strlen($file_version) - 4);
			$mc_version = $versions[$version_name];
			$version_name = str_replace('.', '-', $version_name);
										
			$dls_txt = 'Downloads';
			if($dls_num == 0)
				$dls_txt = 'Downloads :(';
			elseif($dls_num == 1)
				$dls_txt = 'Download';
			
			$deobf_dl = ' ';
			$deobf_name = str_replace('.jar', '-deobf.jar', $file);
			$extra_class = 'btn-succeed';

			if(file_exists("files/deobf/$deobf_name")) {
				$deobf_dl = "<a href='files/deobf/$deobf_name' class='btn btn-deobf btn-material-lime dl'><b>(dev)</b></a>";
				$extra_class = 'btn-succeed-deobf';
			}
			
			$div_id = 'version-' . str_replace('.', '-', $mc_version);
			if($last_version != $mc_version && !$first)
				$downloads_str .= "</div><br><font size='4'>Minecraft <b>$mc_version</b></font> <a class='hide-div' id='hide-$div_id'>(hide)</a><br><div id='$div_id'>";
			
			$group_class = '';
			if($first)
				$group_class = ' btn-group-first';
			
			$downloads_str .= "
			<div class='btn-group$group_class'>
			<a href='dl.php?file=$file' class='btn btn-material-lightgreen'><b><span class='glyphicon glyphicon-download'></span> $file</b></a>
			$deobf_dl
			<a href='changelog.php#$version_name' class='btn $extra_class btn-material-cyan'><b>Changelog</b></a>
			<a class='btn $extra_class btn-material-pink dl-counter'><b>Minecraft $mc_version</b></a>
			<a href='dl.php?file=$file' class='btn $extra_class btn-material-orange dl-counter'><b>$dls</b> $dls_txt</a>
			</div>";
			
			if($first) {
				$downloads_str .= '<hr><font size="6">Old Versions</font><div>';
				$first = false;
			} else {
				$downloads_str .= '<br>';
				$last_version = $mc_version;
			}
		}
	$downloads_str .= '</div>';
	
	$total_dl_str = number_format($total_dls);
	$mod_launch = strtotime('2014-02-28 17:32:22');
	$current_time = time();
	$days_passed = ($current_time - $mod_launch) / 86400;
	$days_int = round($days_passed);
	$daily_dls = round($total_dls / $days_passed, 2);
	
	print("<div class='total-dls'><span class='glyphicon glyphicon-star moe-scroll'></span> Botania has been downloaded a total of <b class='moe-scroll'>$total_dl_str</b> times over $days_int days. (About <i>$daily_dls</i> dls/day)</div>
		<span class='warning-txt'>Botania requires <a href='http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/1294623-baubles-1-0-1-5-updated-20-8-2014'>Baubles</a> to work properly, make sure you have it!<br>
		<br><br>
		<span class='whore-txt'>Enjoy the mod? Why not support my endeavours on <a href='http://www.patreon.com/Vazkii'>Patreon</a>?</span></span>
		<br><br>
		<div class='reddit-card'><div class='reddit-alien'><img src='img/reddit-alien.svg'></img></div><div class='reddit-info'><div class='reddit-header'><a href='http://reddit.com/r/botania'>/r/botania</a> is a thing now!</div><div class='reddit-desc'>Come discuss the mod on reddit!<br>The subreddit is still new but will hopefully grow as time passes!</div></div></div>
		<br></br>
		<font size='4'>Misc Downloads</font><br>
		<div class='btn-group'>
		<a href='http://minecraft.curseforge.com/projects/botania-skyblock-the-modpack-the-mod-the-modpack' class='btn btn-material-lightgreen'><b>Official Modpack</b></a>
		<a href='http://www.curseforge.com/projects/225643/' class='btn btn-material-orange'><b>CurseForge</b></a>
		<a href='gardenofglass.php' class='btn btn-material-cyan'><b>Garden of Glass</b></a></div> 
		<br><br>
		<font size='5'>Latest Download</font><br>");
	print($downloads_str);
	
	$nekos = '<br><hr><br><div id="moe-counter">';
	foreach(str_split($total_dls) as $num)
		$nekos .= "<img src='img/moe/$num.gif'></ing>";
	$nekos .= '<br>.jars served, whatcha waiting for? <a class="hashlink">(back to top)</a></div>';
		
	print($nekos);
?>