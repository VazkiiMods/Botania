Files=('manasteel' 'manaPearl' 'manaDiamond' 'livingwoodTwig' 'terrasteel' 'lifeEssence' 'redstoneRoot' 'elementium' 'pixieDust' 'dragonstone' 'prismarineShard' 'placeholder' 'redString' 'dreamwoodTwig' 'gaiaIngot' 'enderAirBottle' 
'manaString' 'manasteelNugget' 'terrasteelNugget' 'elementiumNugget' 'root' 'pebble' 'manaweaveCloth' 'manaPowder');

for i in "${Files[@]}";do
     FileFormat="{\"parent\":\"botania:item/standard_item\",\"textures\":{\"layer0\":\"botania:items/$i\"}}"
     echo $FileFormat >> $i".json"
done
