package vazkii.botania.api.imc;

import vazkii.botania.api.wiki.IWikiProvider;

public class ModWikiMessage {
    private final String modid;
    private final IWikiProvider wiki;

    public ModWikiMessage(String modid, IWikiProvider wiki) {
        this.modid = modid;
        this.wiki = wiki;
    }

    public String getModId() {
        return modid;
    }

    public IWikiProvider getWiki() {
        return wiki;
    }
}
