#!/usr/bin/env python3
from sys import argv, stdout, stderr
from collections import namedtuple
import json # codec
import re # parsing
import os # listdir

# extra info :(
lang = "en_us"
repo_names = {
    "botania": "https://raw.githubusercontent.com/VazkiiMods/Botania/1.19.x/Xplat/src/main/resources",
}
extra_i18n = {
    "item.laputa_upgrade_processing": "upgraded Laputa Shard",
    "item.minecraft.acacia_leaves": "Acacia Leaves",
    "item.minecraft.acacia_log": "Acacia Log",
    "item.minecraft.acacia_sapling": "Acacia Sapling",
    "item.minecraft.allium": "Allium",
    "item.minecraft.andesite": "Andesite",
    "item.minecraft.apple": "Apple",
    "item.minecraft.azure_bluet": "Azure Bluet",
    "item.minecraft.beetroot_seeds": "Beetroot Seeds",
    "item.minecraft.birch_leaves": "Birch Leaves",
    "item.minecraft.birch_log": "Birch Log",
    "item.minecraft.birch_sapling": "Birch Sapling",
    "item.minecraft.black_dye": "Black Dye",
    "item.minecraft.blue_dye": "Blue Dye",
    "item.minecraft.blue_orchid": "Blue Orchid",
    "item.minecraft.brick": "Brick",
    "item.minecraft.brown_dye": "Brown Dye",
    "item.minecraft.cactus": "Cactus",
    "item.minecraft.carrot": "Carrot",
    "item.minecraft.chiseled_stone_bricks": "Chiseled Stone Bricks",
    "item.minecraft.chorus_flower": "Chorus Flower",
    "item.minecraft.clay_ball": "Clay Ball",
    "item.minecraft.coal": "Coal",
    "item.minecraft.coarse_dirt": "Coarse Dirt",
    "item.minecraft.cobblestone": "Cobblestone",
    "item.minecraft.cobweb": "Cobwebs",
    "item.minecraft.cocoa_beans": "Cocoa Beans",
    "item.minecraft.cod": "Raw Cod",
    "item.minecraft.cornflower": "Cornflower",
    "item.minecraft.cyan_dye": "Cyan Dye",
    "item.minecraft.dandelion": "Dandelion",
    "item.minecraft.dark_oak_leaves": "Dark Oak Leaves",
    "item.minecraft.dark_oak_log": "Dark Oak Log",
    "item.minecraft.dark_oak_sapling": "Dark Oak Sapling",
    "item.minecraft.dead_bush": "Dead Bush",
    "item.minecraft.diorite": "Diorite",
    "item.minecraft.end_portal_frame": "End Portal Frame",
    "item.minecraft.ender_pearl": "Ender Pearl",
    "item.minecraft.fern": "Fern",
    "item.minecraft.glow_berries": "Glow Berries",
    "item.minecraft.glowstone_dust": "Glowstone Dust",
    "item.minecraft.granite": "Granite",
    "item.minecraft.grass": "Grass",
    "item.minecraft.gravel": "Gravel",
    "item.minecraft.gray_dye": "Gray Dye",
    "item.minecraft.green_dye": "Green Dye",
    "item.minecraft.gunpowder": "Gunpowder",
    "item.minecraft.heart_of_the_sea": "Heart of the Sea",
    "item.minecraft.ice": "Ice",
    "item.minecraft.jungle_leaves": "Jungle Leaves",
    "item.minecraft.jungle_log": "Jungle Log",
    "item.minecraft.jungle_sapling": "Jungle Sapling",
    "item.minecraft.leather": "Leather",
    "item.minecraft.light_blue_dye": "Light Blue Dye",
    "item.minecraft.light_gray_dye": "Light Gray Dye",
    "item.minecraft.lilac": "Lilac",
    "item.minecraft.lily_of_the_valley": "Lily of the Valley",
    "item.minecraft.lily_pad": "Lily Pad",
    "item.minecraft.lime_dye": "Lime Dye",
    "item.minecraft.magenta_dye": "Magenta Dye",
    "item.minecraft.melon_seeds": "Melon Seeds",
    "item.minecraft.name_tag": "Name Tag",
    "item.minecraft.nether_wart": "Nether Wart",
    "item.minecraft.netherrack": "Netherrack",
    "item.minecraft.oak_leaves": "Oak Leaves",
    "item.minecraft.oak_log": "Oak Log",
    "item.minecraft.oak_sapling": "Oak Sapling",
    "item.minecraft.orange_dye": "Orange Dye",
    "item.minecraft.orange_tulip": "Orange Tulip",
    "item.minecraft.oxeye_daisy": "Oxeye Daisy",
    "item.minecraft.peony": "Peony",
    "item.minecraft.pink_dye": "Pink Dye",
    "item.minecraft.pink_tulip": "Pink Tulip",
    "item.minecraft.player_head": "Player Head",
    "item.minecraft.poppy": "Poppy",
    "item.minecraft.potato": "Potato",
    "item.minecraft.prismarine_crystals": "Prismarine Crystals",
    "item.minecraft.prismarine_shard": "Prismarine Shard",
    "item.minecraft.pufferfish": "Pufferfish",
    "item.minecraft.pumpkin_seeds": "Pumpkin Seeds",
    "item.minecraft.purple_dye": "Purple Dye",
    "item.minecraft.quartz": "Nether Quartz",
    "item.minecraft.red_dye": "Red Dye",
    "item.minecraft.red_sand": "Red Sand",
    "item.minecraft.red_tulip": "Red Tulip",
    "item.minecraft.redstone": "Redstone Dust",
    "item.minecraft.rose_bush": "Rose Bush",
    "item.minecraft.salmon": "Raw Salmon",
    "item.minecraft.sand": "Sand",
    "item.minecraft.slime_ball": "Slimeball",
    "item.minecraft.snowball": "Snowball",
    "item.minecraft.soul_sand": "Soul Sand",
    "item.minecraft.spruce_leaves": "Spruce Leaves",
    "item.minecraft.spruce_log": "Spruce Log",
    "item.minecraft.spruce_sapling": "Spruce Sapling",
    "item.minecraft.string": "String",
    "item.minecraft.sugar_cane": "Sugar Cane",
    "item.minecraft.sunflower": "Sunflower",
    "item.minecraft.sweet_berries": "Sweet Berries",
    "item.minecraft.tropical_fish": "Tropical Fish",
    "item.minecraft.vine": "Vines",
    "item.minecraft.wheat_seeds": "Wheat Seeds",
    "item.minecraft.white_dye": "White Dye",
    "item.minecraft.white_tulip": "White Tulip",
    "item.minecraft.yellow_dye": "Yellow Dye",
}

default_macros = {
    "$(obf)": "$(k)",
    "$(bold)": "$(l)",
    "$(strike)": "$(m)",
    "$(italic)": "$(o)",
    "$(italics)": "$(o)",
    "$(list": "$(li",
    "$(reset)": "$()",
    "$(clear)": "$()",
    "$(2br)": "$(br2)",
    "$(p)": "$(br2)",
    "/$": "$()",
    "<br>": "$(br)",
    "$(nocolor)": "$(0)",
    "$(item)": "$(#b0b)",
    "$(thing)": "$(#490)",
}

colors = {
    "0": None,
    "1": "00a",
    "2": "0a0",
    "3": "0aa",
    "4": "a00",
    "5": "a0a",
    "6": "fa0",
    "7": "aaa",
    "8": "555",
    "9": "55f",
    "a": "5f5",
    "b": "5ff",
    "c": "f55",
    "d": "f5f",
    "e": "ff5",
    "f": "fff",
}
types = {
    "k": "obf",
    "l": "bold",
    "m": "strikethrough",
    "n": "underline",
    "o": "italic",
}

keys = {
    "use": "Right Click",
    "sneak": "Left Shift",
    "curios.open.desc": "b",
    "botania_corporea_request": "c",
    "sprint": "Left Control",
}


def slurp(filename):
    with open(filename, "r") as fh:
        return json.load(fh)

def dedup(seq):
    seen = set()
    seen_add = seen.add
    return [x for x in seq if not (x in seen or seen_add(x))]

FormatTree = namedtuple("FormatTree", ["style", "children"])
Style = namedtuple("Style", ["type", "value"])


def parse_style(sty):
    if sty == "br":
        return "<br />", None
    if sty == "br2":
        return "", Style("para", {})
    if sty == "li":
        return "", Style("para", {"clazz": "fake-li"})
    if sty[:2] == "k:":
        return keys[sty[2:]], None
    if sty[:2] == "l:":
        return "", Style("link", sty[2:])
    if sty == "/l":
        return "", Style("link", None)
    if sty == "playername":
        return "[Playername]", None
    if sty[:2] == "t:":
        return "", Style("tooltip", sty[2:])
    if sty == "/t":
        return "", Style("tooltip", None)
    if sty[:2] == "c:":
        return "", Style("cmd_click", sty[2:])
    if sty == "/c":
        return "", Style("cmd_click", None)
    if sty == "r" or not sty:
        return "", Style("base", None)
    if sty in types:
        return "", Style(types[sty], True)
    if sty in colors:
        return "", Style("color", colors[sty])
    if sty.startswith("#") and len(sty) in [4, 7]:
        return "", Style("color", sty[1:])
    # TODO more style parse
    raise ValueError("Unknown style: " + sty)


def localize(i18n, string):
    return i18n.get(string, string) if i18n else string


format_re = re.compile(r"\$\(([^)]*)\)")


def format_string(root_data, string):
    # resolve lang
    string = localize(root_data["i18n"], string)
    # resolve macros
    old_string = None
    while old_string != string:
        old_string = string
        for macro, replace in root_data["macros"].items():
            string = string.replace(macro, replace)
        else:
            break

    # lex out parsed styles
    text_nodes = []
    styles = []
    last_end = 0
    extra_text = ""
    for mobj in re.finditer(format_re, string):
        bonus_text, sty = parse_style(mobj.group(1))
        text = string[last_end : mobj.start()] + bonus_text
        if sty:
            styles.append(sty)
            text_nodes.append(extra_text + text)
            extra_text = ""
        else:
            extra_text += text
        last_end = mobj.end()
    text_nodes.append(extra_text + string[last_end:])
    first_node, *text_nodes = text_nodes

    # parse
    style_stack = [
        FormatTree(Style("base", True), []),
        FormatTree(Style("para", {}), [first_node]),
    ]
    for style, text in zip(styles, text_nodes):
        tmp_stylestack = []
        if style.type == "base":
            while style_stack[-1].style.type != "para":
                last_node = style_stack.pop()
                style_stack[-1].children.append(last_node)
        elif any(tree.style.type == style.type for tree in style_stack):
            while len(style_stack) >= 2:
                last_node = style_stack.pop()
                style_stack[-1].children.append(last_node)
                if last_node.style.type == style.type:
                    break
                tmp_stylestack.append(last_node.style)
        for sty in tmp_stylestack:
            style_stack.append(FormatTree(sty, []))
        if style.value is None:
            if text:
                style_stack[-1].children.append(text)
        else:
            style_stack.append(FormatTree(style, [text] if text else []))
    while len(style_stack) >= 2:
        last_node = style_stack.pop()
        style_stack[-1].children.append(last_node)

    return style_stack[0]


def do_localize(root_data, obj, *names):
    for name in names:
        if name in obj:
            obj[name] = localize(root_data["i18n"], obj[name])


def do_format(root_data, obj, *names):
    for name in names:
        if name in obj:
            obj[name] = format_string(root_data, obj[name])


# TODO kind of a hack
resource_dir_bases = {
    "botania": [
        "../../generated/resources",
        "../../../../Forge/src/generated/resources",
    ],
    "gardenofglass": ["../../../../garden_of_glass"],
}


def fetch_recipe(root_data, recipe):
    modid, recipeid = recipe.split(":")
    for base in resource_dir_bases[modid]:
        recipe_path = (
            f"{root_data['resource_dir']}/{base}/data/{modid}/recipes/{recipeid}.json"
        )
        if not os.path.isfile(recipe_path):
            continue
        data = slurp(recipe_path)
        while data["type"] == "botania:nbt_output_wrapper":
            data = data["recipe"]
        while data["type"] == "botania:gog_alternation":
            data = data["base"]
        return data
    raise ValueError("Recipe " + recipe + " not found")


def fetch_recipe_result(root_data, recipe):
    data = fetch_recipe(root_data, recipe)
    if "result" in data:
        return data["result"]["item"]
    if data["type"] == "botania:laputa_shard_upgrade":
        return "laputa_upgrade_processing"
    print(data, file=stderr)
    raise ValueError


def localize_item(root_data, item):
    # TODO hack
    item = re.sub("{.*", "", item.replace(":", "."))
    block = "block." + item
    block_l = localize(root_data["i18n"], block)
    if block_l != block:
        return block_l
    return localize(root_data["i18n"], "item." + item)


def localize_brew(root_data, brew):
    modid, name = brew["brew"].split(":")
    return localize(root_data["i18n"], f"{modid}.brew.{name}")


def fetch_smelt(rd, page):
    data = fetch_recipe(rd, page["recipe"])
    page["in"] = localize_item(rd, data["ingredient"]["item"])
    page["out"] = localize_item(rd, data["result"])


def fetch_infusion_groups(root_data):
    group_cache = {}
    base_path = f"{root_data['resource_dir']}/../../generated/resources/data/{root_data['modid']}/recipes/mana_infusion"
    for path in walk_dir(base_path, ""):
        recipe = slurp(f"{base_path}/{path}")
        if "group" in recipe:
            group_cache.setdefault(recipe["group"], []).append(recipe)
    return group_cache


def resolve_group(root_data, name):
    if "group_cache" not in root_data:
        root_data["group_cache"] = fetch_infusion_groups(root_data)
    return root_data["group_cache"][name]


def fetch_infusion(rd, page):
    page["loc_recipes"] = []
    if "group" in page:
        recipes = resolve_group(rd, page["group"])
    else:
        recs = page["recipes"]
        lrecs = [recs] if isinstance(recs, str) else recs
        recipes = [fetch_recipe(rd, recipe) for recipe in lrecs]
    for recipe in recipes:
        rec = {}
        rec["out"] = localize_item(rd, recipe["output"]["item"])
        if "catalyst" in recipe:
            rec["catalyst"] = localize_item(rd, recipe["catalyst"]["block"])
        page["loc_recipes"].append(rec)


page_types = {
    "patchouli:link": lambda rd, page: do_localize(rd, page, "link_text"),
    "patchouli:quest": lambda rd, page: page.__setitem__(
        "title", "QUEST: " + page["title"]
    ),
    "patchouli:crafting": lambda rd, page: page.__setitem__(
        "item_name",
        dedup(
            localize_item(rd, fetch_recipe_result(rd, page[ty]))
            for ty in ("recipe", "recipe2")
            if ty in page
        ),
    ),
    "patchouli:smelting": fetch_smelt,
    "patchouli:spotlight": lambda rd, page: page.__setitem__(
        "item_name", localize_item(rd, page["item"])
    ),
    "botania:crafting_multi": lambda rd, page: page.__setitem__(
        "item_name",
        dedup(
            localize_item(rd, fetch_recipe_result(rd, recipe))
            for recipe in page["recipes"]
        ),
    ),
    "botania:brew": lambda rd, page: page.__setitem__(
        "brew_name", localize_brew(rd, fetch_recipe(rd, page["recipe"]))
    ),
    "botania:elven_trade": lambda rd, page: page.__setitem__(
        "item_name",
        dedup(
            localize_item(rd, out["item"])
            for recipe in (
                [page["recipes"]]
                if isinstance(page["recipes"], str)
                else page["recipes"]
            )
            for out in fetch_recipe(rd, recipe)["output"]
        ),
    ),
    "botania:runic_altar": lambda rd, page: page.__setitem__(
        "item_name",
        localize_item(rd, fetch_recipe(rd, page["recipe"])["output"]["item"]),
    ),
    "botania:petal_apothecary": lambda rd, page: page.__setitem__(
        "item_name",
        localize_item(rd, fetch_recipe(rd, page["recipe"])["output"]["item"]),
    ),
    "botania:mana_infusion": fetch_infusion,
}


def walk_dir(root_dir, prefix):
    search_dir = root_dir + "/" + prefix
    for fh in os.scandir(search_dir):
        if fh.is_dir():
            yield from walk_dir(root_dir, prefix + fh.name + "/")
        elif fh.name.endswith(".json"):
            yield prefix + fh.name


def parse_entry(root_data, entry_path, ent_name):
    data = slurp(f"{entry_path}")
    do_localize(root_data, data, "name")
    for i, page in enumerate(data["pages"]):
        if isinstance(page, str):
            page = {"type": "patchouli:text", "text": page}
            data["pages"][i] = page

        if ":" not in page["type"]:
            page["type"] = "patchouli:" + page["type"]
        do_localize(root_data, page, "title", "header")
        do_format(root_data, page, "text")
        if page["type"] in page_types:
            page_types[page["type"]](root_data, page)
    data["id"] = ent_name

    return data


def parse_category(root_data, base_dir, cat_name):
    data = slurp(f"{base_dir}/categories/{cat_name}.json")
    do_localize(root_data, data, "name")
    do_format(root_data, data, "description")

    entry_dir = f"{base_dir}/entries/{cat_name}"
    entries = []
    for filename in os.listdir(entry_dir):
        if filename.endswith(".json"):
            basename = filename[:-5]
            entries.append(
                parse_entry(
                    root_data, f"{entry_dir}/{filename}", cat_name + "/" + basename
                )
            )
    entries.sort(
        key=lambda ent: (
            not ent.get("priority", False),
            ent.get("sortnum", 0),
            ent["name"],
        )
    )
    data["entries"] = entries
    data["id"] = cat_name

    return data


def parse_sortnum(cats, name):
    if "/" in name:
        ix = name.rindex("/")
        return parse_sortnum(cats, name[:ix]) + (cats[name].get("sortnum", 0),)
    return (cats[name].get("sortnum", 0),)


def parse_book(root, mod_name, book_name):
    base_dir = f"{root}/assets/{mod_name}/patchouli_books/{book_name}"
    data_dir = f"{root}/data/{mod_name}/patchouli_books/{book_name}"
    root_info = slurp(f"{data_dir}/book.json")

    root_info["resource_dir"] = root
    root_info["modid"] = mod_name
    root_info.setdefault("macros", {}).update(default_macros)
    if root_info.setdefault("i18n", {}):
        root_info["i18n"] = slurp(f"{root}/assets/{mod_name}/lang/{lang}.json")
        root_info["i18n"].update(extra_i18n)

    book_dir = f"{base_dir}/{lang}"

    categories = []
    for filename in walk_dir(f"{book_dir}/categories", ""):
        basename = filename[:-5]
        categories.append(parse_category(root_info, book_dir, basename))
    cats = {cat["id"]: cat for cat in categories}
    categories.sort(key=lambda cat: (parse_sortnum(cats, cat["id"]), cat["name"]))

    do_localize(root_info, root_info, "name")
    do_format(root_info, root_info, "landing_text")
    root_info["categories"] = categories
    root_info["blacklist"] = {"devices/cocoon_gog", "flowers/orechid_gog"}
    root_info["spoilers"] = set()

    return root_info


def tag_args(kwargs):
    return "".join(
        f" {'class' if key == 'clazz' else key.replace('_', '-')}={repr(value)}"
        for key, value in kwargs.items()
    )


class PairTag:
    __slots__ = ["stream", "name", "kwargs"]

    def __init__(self, stream, name, **kwargs):
        self.stream = stream
        self.name = name
        self.kwargs = tag_args(kwargs)

    def __enter__(self):
        print(f"<{self.name}{self.kwargs}>", file=self.stream, end="")

    def __exit__(self, _1, _2, _3):
        print(f"</{self.name}>", file=self.stream, end="")


class Empty:
    def __enter__(self):
        pass

    def __exit__(self, _1, _2, _3):
        pass


class Stream:
    __slots__ = ["stream", "thunks"]

    def __init__(self, stream):
        self.stream = stream
        self.thunks = []

    def tag(self, name, **kwargs):
        keywords = tag_args(kwargs)
        print(f"<{name}{keywords} />", file=self.stream, end="")
        return self

    def pair_tag(self, name, **kwargs):
        return PairTag(self.stream, name, **kwargs)

    def pair_tag_if(self, cond, name, **kwargs):
        return self.pair_tag(name, **kwargs) if cond else Empty()

    def empty_pair_tag(self, name, **kwargs):
        with self.pair_tag(name, **kwargs):
            pass

    def text(self, txt):
        print(txt, file=self.stream, end="")
        return self


def get_format(out, ty, value):
    if ty == "para":
        return out.pair_tag("p", **value)
    if ty == "color":
        return out.pair_tag("span", style=f"color: #{value}")
    if ty == "link":
        link = value
        if "://" not in link:
            link = "#" + link.replace("#", "@")
        return out.pair_tag("a", href=link)
    if ty == "tooltip":
        return out.pair_tag("span", clazz="has-tooltip", title=value)
    if ty == "cmd_click":
        return out.pair_tag(
            "span", clazz="has-cmd_click", title="When clicked, would execute: " + value
        )
    if ty == "obf":
        return out.pair_tag("span", clazz="obfuscated")
    if ty == "bold":
        return out.pair_tag("strong")
    if ty == "italic":
        return out.pair_tag("i")
    if ty == "strikethrough":
        return out.pair_tag("s")
    if ty == "underline":
        return out.pair_tag("span", style="text-decoration: underline")
    raise ValueError("Unknown format type: " + ty)


def entry_spoilered(root_info, entry):
    return entry.get("advancement", None) in root_info["spoilers"]


def category_spoilered(root_info, category):
    return all(entry_spoilered(root_info, ent) for ent in category["entries"])


def write_block(out, block):
    if isinstance(block, str):
        out.text(block)
        return
    sty_type = block.style.type
    if sty_type == "base":
        for child in block.children:
            write_block(out, child)
        return
    tag = get_format(out, sty_type, block.style.value)
    with tag:
        for child in block.children:
            write_block(out, child)


# TODO proper table
def write_page(out, pageid, page):
    if "anchor" in page:
        anchor_id = pageid + "@" + page["anchor"]
    else:
        anchor_id = None

    with out.pair_tag_if(anchor_id, "div"):
        if "header" in page or "title" in page:
            with out.pair_tag("h4"):
                if anchor_id:
                    with out.pair_tag(
                        "a", href="#" + anchor_id, clazz="permalink small"
                    ):
                        out.empty_pair_tag("i", clazz="glyphicon glyphicon-bookmark")
                    out.empty_pair_tag("span", id=anchor_id, clazz="anchor")
                out.text(page.get("header", page.get("title", None)))

        ty = page["type"]
        if ty in ("patchouli:text", "patchouli:quest"):
            write_block(out, page["text"])
        elif ty == "patchouli:empty":
            pass
        elif ty == "patchouli:link":
            write_block(out, page["text"])
            with out.pair_tag("h4", clazz="linkout"):
                with out.pair_tag("a", href=page["url"]):
                    out.text(page["link_text"])
        elif ty == "patchouli:spotlight":
            with out.pair_tag("h4", clazz="spotlight-title page-header"):
                out.text(page["item_name"])
            if "text" in page:
                write_block(out, page["text"])
        elif ty == "patchouli:crafting":
            with out.pair_tag("blockquote", clazz="crafting-info"):
                out.text(f"Recipe in the book: crafting the ")
                first = True
                for name in page["item_name"]:
                    if not first:
                        out.text(" and ")
                    first = False
                    with out.pair_tag("code"):
                        out.text(name)
                out.text(".")
            # if "text" in page: write_block(out, page["text"])
        elif ty == "patchouli:image":
            with out.pair_tag("figure"):
                with out.pair_tag("div", clazz="img-container"):
                    for img in page["images"]:
                        modid, coords = img.split(":")
                        with out.pair_tag(
                            "span",
                            clazz="img-wrapper"
                            + (" bordered-image" if page.get("border", False) else ""),
                        ):
                            out.tag("img", clazz="patchy-image", src=f"{repo_names[modid]}/assets/{modid}/{coords}"),
                if "text" in page:
                    with out.pair_tag("figcaption"):
                        write_block(out, page["text"])
        elif ty == "patchouli:multiblock":
            # hell no
            pass
        elif ty == "patchouli:smelting":
            with out.pair_tag("blockquote", clazz="crafting-info"):
                out.text(f"Recipe in the book: smelting ")
                with out.pair_tag("code"):
                    out.text(page["in"])
                out.text(" into ")
                with out.pair_tag("code"):
                    out.text(page["out"])
                out.text(".")
        elif ty == "botania:lore_page":
            if "text" in page:
                with out.pair_tag("div", clazz="elven-decor"):
                    write_block(out, page["text"])
        elif ty == "botania:mana_infusion":
            recipes = page["loc_recipes"]
            with out.pair_tag("blockquote", clazz="crafting-info"):
                if len(recipes) == 1:
                    recipe = recipes[0]
                    out.text(f"Recipe in the book: creating ")
                    with out.pair_tag("code"):
                        out.text(recipe["out"])
                else:
                    out.text(f"Recipes in the book: creating ")
                    with out.pair_tag("code"):
                        out.text(recipes[0]["out"])
                    for i in recipes[1:-1]:
                        out.text(", ")
                        with out.pair_tag("code"):
                            out.text(i["out"])
                    if len(recipes) > 2:
                        out.text(",")
                    if len(recipes) > 1:
                        out.text(" and ")
                        with out.pair_tag("code"):
                            out.text(recipes[-1]["out"])
                out.text(" in a mana pool")
                if "catalyst" in recipes[0]:
                    out.text(" using the ")
                    with out.pair_tag("code"):
                        out.text(recipes[0]["catalyst"])
                out.text(".")
        elif ty == "botania:runic_altar":
            with out.pair_tag("blockquote", clazz="crafting-info"):
                out.text(f"Recipe in the book: creating the ")
                with out.pair_tag("code"):
                    out.text(page["item_name"])
                out.text(" on a Runic Altar.")
        elif ty == "botania:petal_apothecary":
            with out.pair_tag("blockquote", clazz="crafting-info"):
                out.text(f"Recipe in the book: Creating the ")
                with out.pair_tag("code"):
                    out.text(page["item_name"])
                out.text(" in a Petal Apothecary.")
            if "text" in page:
                write_block(out, page["text"])
        elif ty == "botania:brew":
            with out.pair_tag("blockquote", clazz="crafting-info"):
                out.text(f"Recipe in the book: brewing a Brew of ")
                with out.pair_tag("code"):
                    out.text(page["brew_name"])
                out.text(".")
            if "text" in page:
                write_block(out, page["text"])
        elif ty == "botania:elven_trade":
            with out.pair_tag("blockquote", clazz="crafting-info"):
                out.text(f"Recipe in the book: An elven trade resulting in ")
                with out.pair_tag("code"):
                    out.text(page["item_name"][0])
                out.text(".")
        elif ty == "botania:terrasteel":
            with out.pair_tag("blockquote", clazz="crafting-info"):
                out.text(f"Recipe in the book: The ritual for creating Terrasteel.")
            if "text" in page:
                write_block(out, page["text"])
        elif ty == "botania:crafting_multi":
            recipes = page["item_name"]
            with out.pair_tag("blockquote", clazz="crafting-info"):
                # HACK: if we have a crafting-multi with 1, assume they were all squished
                out.text(f"Recipes in the book: crafting {'several varieties of' if len(recipes) == 1 else 'the'} ")
                with out.pair_tag("code"):
                    out.text(recipes[0])
                for i in recipes[1:-1]:
                    out.text(", ")
                    with out.pair_tag("code"):
                        out.text(i)
                if len(recipes) > 2:
                    out.text(",")
                if len(recipes) > 1:
                    out.text(" and ")
                    with out.pair_tag("code"):
                        out.text(recipes[-1])
                out.text(".")
            if "text" in page:
                write_block(out, page["text"])
        else:
            with out.pair_tag("p", clazz="todo-note"):
                out.text("TODO: Missing processor for type: " + ty)
            if "text" in page:
                write_block(out, page["text"])
    out.tag("br")


def anchor_entry(entry):
    return entry["id"]


"""
    # TODO hack
    if entry["id"] == "challenges/welcome": return "challengeWelcome"
    if entry["id"] == "mana/intro": return "mIntro"
    if entry["id"] == "functional_flowers/intro": return "fIntro"
    if entry["id"] == "baubles/intro": return "bIntro"
    *_, entry_name = entry["id"].split("/")
    return re.sub(r"_(\w)", lambda m: m.group(1).upper(), entry_name)
"""


def write_entry(out, book, entry):
    with out.pair_tag("div"):
        with out.pair_tag_if(entry_spoilered(book, entry), "div", clazz="spoilered"):
            anchor = anchor_entry(entry)
            with out.pair_tag("h3", clazz="entry-title page-header"):
                with out.pair_tag("a", href="#" + anchor, clazz="permalink small"):
                    out.empty_pair_tag("i", clazz="glyphicon glyphicon-bookmark")
                out.empty_pair_tag("span", id=anchor, clazz="anchor")
                write_block(out, entry["name"])
            with out.pair_tag("div", clazz="entry-body"):
                for page in entry["pages"]:
                    write_page(out, anchor, page)


def write_category(out, book, category):
    with out.pair_tag("section"):
        with out.pair_tag_if(
            category_spoilered(book, category), "div", clazz="spoilered"
        ):
            with out.pair_tag("h2", clazz="category-title page-header"):
                with out.pair_tag(
                    "a", href="#" + category["id"], clazz="permalink small"
                ):
                    out.empty_pair_tag("i", clazz="glyphicon glyphicon-bookmark")
                out.empty_pair_tag("span", id=category["id"], clazz="anchor")
                write_block(out, category["name"])
            # write_block(out, category["description"])
        for entry in category["entries"]:
            if entry["id"] not in book["blacklist"]:
                write_entry(out, book, entry)


def write_toc(out, book):
    with out.pair_tag("h2", clazz="page-header"):
        with out.pair_tag("a", href="#table-of-contents", clazz="permalink small"):
            out.empty_pair_tag("i", clazz="glyphicon glyphicon-bookmark")
        out.empty_pair_tag("span", id="table-of-contents", clazz="anchor")
        out.text("Table of Contents")
        with out.pair_tag(
            "a",
            href="javascript:void(0)",
            clazz="toggle-link small",
            data_target="toc-category",
        ):
            out.text("(toggle all)")
    with out.pair_tag("div", clazz="entry-body"):
        for category in book["categories"]:
            with out.pair_tag("details", clazz="toc-category"):
                with out.pair_tag("summary"):
                    with out.pair_tag(
                        "a",
                        href="#" + category["id"],
                        clazz="spoilered" if category_spoilered(book, category) else "",
                    ):
                        out.text(category["name"])
                with out.pair_tag("ul"):
                    for entry in category["entries"]:
                        with out.pair_tag("li"):
                            with out.pair_tag(
                                "a",
                                href="#" + anchor_entry(entry),
                                clazz="spoilered"
                                if entry_spoilered(book, entry)
                                else "",
                            ):
                                out.text(entry["name"])


def write_book(out, book):
    with out.pair_tag("div", clazz="container"):
        with out.pair_tag("header", clazz="jumbotron"):
            with out.pair_tag("h1", clazz="book-title"):
                out.empty_pair_tag("i", clazz="book-icon glyphicon glyphicon-book")
                write_block(out, book["name"])
            write_block(out, book["landing_text"])
        with out.pair_tag("nav"):
            write_toc(out, book)
        with out.pair_tag("main", clazz="book-body"):
            for category in book["categories"]:
                write_category(out, book, category)


def main(argv):
    if len(argv) < 4:
        print(f"Usage: {argv[0]} <resources dir> <mod name> <book name> [<output>]")
        return
    root = argv[1]
    mod_name = argv[2]
    book_name = argv[3]
    book = parse_book(root, mod_name, book_name)
    with stdout if len(argv) < 5 else open(argv[4], "w") as out:
        write_book(Stream(out), book)
        print("", file=out)


if __name__ == "__main__":
    main(argv)
