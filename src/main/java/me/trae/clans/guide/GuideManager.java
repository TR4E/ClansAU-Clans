package me.trae.clans.guide;

import me.trae.clans.Clans;
import me.trae.clans.guide.modules.*;
import me.trae.core.guide.abstracts.AbstractGuideManager;

public class GuideManager extends AbstractGuideManager<Clans> {

    public GuideManager(final Clans instance) {
        super(instance);
    }

    @Override
    public void registerModules() {
        addModule(new ClansGuide(this));
        addModule(new FieldsGuide(this));
        addModule(new FishingGuide(this));
        addModule(new KothGuide(this));
        addModule(new QuestsGuide(this));
        addModule(new TntMechanicsGuide(this));
    }
}