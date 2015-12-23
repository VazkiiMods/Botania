#Files=('manasteel' 'manaPearl' 'manaDiamond' 'livingwoodTwig' 'terrasteel' 'lifeEssence' 'redstoneRoot' 'elementium' 'pixieDust' 'dragonstone' 'prismarineShard' 'placeholder' 'redString' 'dreamwoodTwig' 'gaiaIngot' 'enderAirBottle' 
#'manaString' 'manasteelNugget' 'terrasteelNugget' 'elementiumNugget' 'root' 'pebble' 'manaweaveCloth' 'manaPowder');
Files=('glassPick' 'starSword' 'thunderSword')
#Files=( 'Pick' 'Shovel' 'Axe' 'Sword' 'Shears')
Type="standard_tool"
for i in "${Files[@]}";do
     FileFormat="{\"parent\":\"botania:item/$Type\",\"textures\":{\"layer0\":\"botania:items/$i\"}}"
     echo $FileFormat >> "$i.json"
done
