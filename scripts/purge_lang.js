const fs = require('fs')

const lang_directory = `${__dirname}/../Xplat/src/main/resources/assets/botania/lang`;
const files = fs.readdirSync(lang_directory);

const en_us = 'en_us.json';
const keys_to_keep = Object.keys(JSON.parse(fs.readFileSync(`${lang_directory}/${en_us}`, 'utf8')));

// ignore these edge case lines
keys_to_keep.push('_comment5');
keys_to_keep.push('_comment6');

files.forEach(function (file) {
  if (file == en_us) return;

  console.log(file);

  const old_lines = fs.readFileSync(`${lang_directory}/${file}`, 'utf8').split('\n');
  const new_lines = []

  old_lines.forEach((line, i) => {
    if (line.startsWith('  "')) {
      // [ "\"botaniamisc.composite_lens\": \"", "botaniamisc.composite_lens" ]
      const [,key] = /^  "(.*)": "/.exec(line);
      if(!keys_to_keep.includes(key)) return;
    }

    new_lines.push(line);
  })

  fs.writeFileSync(`${lang_directory}/${file}`, new_lines.join('\n'));
});
