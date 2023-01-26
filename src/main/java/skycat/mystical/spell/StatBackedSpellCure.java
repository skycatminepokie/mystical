package skycat.mystical.spell;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import lombok.Getter;
import net.minecraft.stat.Stat;
import net.minecraft.stat.StatType;

import java.lang.reflect.Type;

@Getter
public class StatBackedSpellCure extends SpellCure {
    private final Stat stat;

    public StatBackedSpellCure(double contributionGoal, Stat stat) {
        super(contributionGoal);
        this.stat = stat;
    }

    @Override
    public <T> T deserialize(JsonElement spellCure, Type type, JsonDeserializationContext context) {

        return null;
    }

    @Override
    public <T> JsonElement serialize(T spellCure, Type type, JsonSerializationContext context) {
        return null;
    }

    public StatType getStatType() {
        return stat.getType();
    }
}
