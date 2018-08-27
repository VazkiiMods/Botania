<?php
	header('Location: https://minecraft.curseforge.com/projects/botania');

	/*if(isset($_GET['file']))
		dl_mod($_GET['file']);
	function dl_mod($mod) {
		if(strlen($mod) > 0 && file_exists("files/$mod") && strpos($mod, "/") === false) {
			increment_dl_counter($mod);
			header("Location: files/$mod");
		} else echo("Hm, doesn't seem like that file exists :( <br><a href='./'>Back</a>");
	}
	
	function increment_dl_counter($mod) {
		$path = "dls/$mod.txt";
		$dls = get_dls($mod);
		$dls_int = intval($dls);
		++$dls_int;
		
		file_put_contents($path, $dls_int);
	}
	
	function get_dls($mod) {
		$path = 'dls/';
		$file = "$path$mod.txt";
		
		if(file_exists($file))
			return file_get_contents($file);
		
		if(!file_exists($path))
			mkdir('dls/');
			
		file_put_contents($file, '0');
		return get_dls($mod);
	}
	*/
?>