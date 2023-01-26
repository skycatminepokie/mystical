package skycat.mystical.spell;

import com.google.gson.*;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Getter @Setter
public abstract class SpellCure {

    private double contributionGoal;
    private ArrayList<CureContribution> contributions = new ArrayList<>();

    public SpellCure(double contributionGoal) {
        this.contributionGoal = contributionGoal;
    }

    public void contribute(@Nullable UUID uuid, double amount) {
        contributions.add(new CureContribution(uuid, LocalDateTime.now(), amount));
        // TODO: Logging
    }

    public record CureContribution(@Nullable UUID contributor, @Nullable LocalDateTime time, double amount) { }

    public JsonSerializer getSerializer() {
        return this::serialize;
    }
    public JsonDeserializer getDeserializer() {
        return this::deserialize;
    }
    public abstract <T> T deserialize(JsonElement spellCure, Type type, JsonDeserializationContext context);
    public abstract <T> JsonElement serialize(T spellCure, Type type, JsonSerializationContext context);
}
