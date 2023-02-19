package skycat.mystical.spell.cure;

import com.google.gson.*;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.text.MutableText;
import org.jetbrains.annotations.Nullable;
import skycat.mystical.util.Utils;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Getter @Setter
public abstract class SpellCure {

    private double contributionGoal;
    private final Class cureType;
    private ArrayList<CureContribution> contributions = new ArrayList<>();
    private final String translationKey;

    /**
     * Please provide a translation key with {@link SpellCure#SpellCure(double, Class, String)}
     */
    public SpellCure(double contributionGoal, Class cureType) {
        this(contributionGoal, cureType, "text.mystical.spellCure.default");
    }

    public SpellCure(double contributionGoal, Class cureType, String translationKey) {
        this.contributionGoal = contributionGoal;
        this.cureType = cureType;
        this.translationKey = translationKey;
    }

    /**
     * This is a player-readable description/"recipe" for the cure.
     * Override this if you have parameters to add to the translation.
     */
    public MutableText getDescription() {
        return Utils.translatable(translationKey);
    }

    public void contribute(@Nullable UUID uuid, double amount) {
        contributions.add(new CureContribution(uuid, LocalDateTime.now(), amount));
        // TODO: Logging
    }

    public record CureContribution(@Nullable UUID contributor, @Nullable LocalDateTime time, double amount) { }

    public static class Serializer implements JsonSerializer<SpellCure>, JsonDeserializer<SpellCure> {
        @Override
        public SpellCure deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            Class cureType = context.deserialize(json.getAsJsonObject().get("cureType"), Class.class);
            return context.deserialize(json, cureType);
        }

        @Override
        public JsonElement serialize(SpellCure src, Type typeOfSrc, JsonSerializationContext context) {
            return context.serialize(src, src.getCureType());
        }
    }
}
