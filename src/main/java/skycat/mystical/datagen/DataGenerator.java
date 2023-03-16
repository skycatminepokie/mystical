package skycat.mystical.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import skycat.mystical.Mystical;
import skycat.mystical.spell.SpellGenerator;
import skycat.mystical.spell.consequence.ConsequenceFactory;
import skycat.mystical.spell.consequence.SpellConsequence;

public class DataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        fabricDataGenerator.addProvider(EnglishLangProvider::new);
    }
    private static class EnglishLangProvider extends FabricLanguageProvider {

        protected EnglishLangProvider(FabricDataGenerator dataGenerator) {
            super(dataGenerator, "en_us");
        }

        @Override
        public void generateTranslations(TranslationBuilder translationBuilder) {
            // Generate translations for consequences
            for (ConsequenceFactory<?> factory : SpellGenerator.consequenceFactoriesHash.values()) {
                SpellConsequence consequence = factory.make(Mystical.RANDOM, 0);
                translationBuilder.add(consequence.getShortNameKey(), consequence.getShortName());
                translationBuilder.add(consequence.getLongNameKey(), consequence.getLongName());
                translationBuilder.add(consequence.getDescriptionKey(), consequence.getDescription());
            }
        }

    }
}
