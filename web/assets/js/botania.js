'use strict';
const censorChars = 'abcdefghijklmnopqrstuvwxyz';

document.addEventListener('DOMContentLoaded', () => {
    setInterval(changeCensor, 100);
	$.material.init();
});

function changeCensor() {
    document.querySelectorAll(".censored").forEach(t => {
        t.textContent = randomStr(t.textContent.length);
    });
}

function randomStr(length) {
	let str = '';
	for(let i = 0; i < length; i++) {
		str += censorChars.charAt(Math.floor(Math.random() * censorChars.length));
    }
	return str;
}
