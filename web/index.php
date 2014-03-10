<html>
	<title>Botania Files</title>

	<font size="6">Botania Files</font><br>
	<a href="http://forum.feed-the-beast.com/threads/.41662">Botania Thread (FTB Forums)</a><br>
	<a href="http://www.minecraftforum.net/topic/2440071-">Botania Thread (Minecraft Forums)</a><br>
	<a href="http://vazkii.us">Back to vazkii.us</a>
	<br><hr><br>
	
	<a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB"><img alt="Creative Commons Licence" style="border-width:0" src="http://i.creativecommons.org/l/by-nc-sa/3.0/88x31.png" /></a><br />Botania by Vazkii is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB">Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License</a>.<br>
	This webpage and Botania itself are open source. Contribute to the project on <a href="https://github.com/Vazkii/Botania">GitHub</a>!
	<br><hr>
	<font size="5"><a href="changelog.html">Botania Changelog</a></font><br>
	
	<body link="#111111" vlink="#444466" alink="#000099">
	<?php
		include 'dl.php';
	
		$files = scandir('files/');
		natsort($files);
		$files = array_reverse($files);
		$first = true;
		$total_dls = 0;
		$downloads_str = '';
		
		foreach($files as $file)
			if(strlen($file) > 2) {
				$dls = get_dls($file);
				$total_dls += intval($dls);
				$dls = number_format($dls);
				$downloads_str .= "<a href='dl.php?file=$file'><b>$file</b></a> ($dls Downloads)<br>";
				
				if($first) {
					$downloads_str .= '<br><font size="5">Old Versions</font><br>';
					$first = false;
				}
			}
		
		$total_dl_str = number_format($total_dls);
		print("<b>Botania has been downloaded a total of $total_dl_str times.</b><br><br>");
		print($downloads_str);
	?>

</body>
</html>