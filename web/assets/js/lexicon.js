'use strict';
function hookToggle(elem) {
  const details = Array.from(document.querySelectorAll("details." + elem.dataset.target));
  elem.addEventListener("click", () => {
    if (details.some(x => x.open)) {
      details.forEach(x => x.open = false);
    } else {
      details.forEach(x => x.open = true);
    }
  });
}
const params = new URLSearchParams(document.location.search);
function hookSpoiler(elem) {
  if (params.get("nospoiler") !== null) {
    elem.classList.add("unspoilered");
  } else {
    const thunk = ev => {
      if (!elem.classList.contains("unspoilered")) {
        ev.preventDefault();
        ev.stopImmediatePropagation();
        elem.classList.add("unspoilered");
      }
      elem.removeEventListener("click", thunk);
    };
    elem.addEventListener("click", thunk);

    if (elem instanceof HTMLAnchorElement) {
      const href = elem.getAttribute("href");
      if (href.startsWith("#")) {
        elem.addEventListener("click", () => document.getElementById(href.substring(1)).querySelector(".spoilered").classList.add("unspoilered"));
      }
    }
  }
}
document.addEventListener('DOMContentLoaded', () => {
  document.querySelectorAll('a.toggle-link').forEach(hookToggle);
  document.querySelectorAll('.spoilered').forEach(hookSpoiler);
});

