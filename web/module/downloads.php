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
		if(strlen($file) > 2) {
			$dls_num = get_dls($file);
			$total_dls += intval($dls_num);
			$dls = number_format($dls_num);
										
			$file_version = substr($file, strlen('Botania '));
			$version_name = substr($file_version, 0, strlen($file_version) - strlen('.jar'));
			$mc_version = $versions[$version_name];
			$version_name = str_replace('.', '-', $version_name);
										
			$dls_txt = 'Downloads';
			if($dls_num == 0)
				$dls_txt = 'Downloads :(';
			elseif($dls_num == 1)
				$dls_txt = 'Download';
			
			$div_id = 'version-' . str_replace('.', '-', $mc_version);
			if($last_version != $mc_version && !$first)
				$downloads_str .= "</div><br><font size='4'>Minecraft <b>$mc_version</b></font> <a class='hide-div' id='hide-$div_id'>(hide)</a><br><div id='$div_id'>";
			
			$downloads_str .= "<a href='dl.php?file=$file' class='btn btn-material-lightgreen dl'><b><span class='glyphicon glyphicon-download'></span> $file</b></a> <a href='changelog.php#$version_name' class='btn btn-succeed btn-material-cyan'><b>Changelog</b></a> <a class='btn btn-succeed btn-material-pink dl-counter'>Minecraft <b>$mc_version</b></a> <a href='dl.php?file=$file' class='btn btn-succeed btn-material-orange dl-counter'><b>$dls</b> $dls_txt</a>";
			
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
	
	print("<div class='total-dls'><span class='glyphicon glyphicon-star moe-scroll'></span> Botania has been downloaded a total of <b>$total_dl_str</b> times over $days_int days. (About <i>$daily_dls</i> dls/day)</div><span class='warning-txt'>Botania requires <a href='http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/1294623-baubles-1-0-1-5-updated-20-8-2014'>Baubles</a> to work properly, make sure you have it!<br>1.7.2 versions also work on 1.7.10!</span><br><br><font size='5'>Latest Download</font><br>");
	print($downloads_str);
	
	$nekos = '<br><hr><br><div id="moe-counter">';
	foreach(str_split($total_dls) as $num)
		$nekos .= "<img src='img/moe/$num.gif'></ing>";
	$nekos .= '<br>.jars served, whatcha waiting for? <a class="hashlink">(back to top)</a></div>';
		
	print($nekos);
?>