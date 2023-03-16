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
            for (ConsequenceFactory<?> factory : SpellGenerator.consequenceFactoriesHash.values()) {
                generateConsequenceTranslation(translationBuilder, factory.make(Mystical.RANDOM, 0));
            }
        }

        private void generateConsequenceTranslation(TranslationBuilder builder, SpellConsequence consequence) {
            builder.add("text.mystical.consequence." + consequence.getShortName() + ".shortName", consequence.getShortName());
            builder.add("text.mystical.consequence." + consequence.getShortName() + ".longName", consequence.getLongName());
            builder.add("text.mystical.consequence." + consequence.getShortName() + ".description", consequence.description); // TODO use getter instead

        }
    }
}
