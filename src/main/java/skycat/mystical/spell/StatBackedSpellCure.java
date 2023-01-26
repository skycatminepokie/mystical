package skycat.mystical.spell;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import lombok.Getter;
import net.minecraft.stat.Stat;
import net.minecraft.stat.StatType;
import skycat.mystical.util.SpellCureType;

import java.lang.reflect.Type;

@Getter
public class StatBackedSpellCure extends SpellCure {
    private final Stat stat;

    public StatBackedSpellCure(double contributionGoal, Stat stat) {
        super(contributionGoal, SpellCureType.STAT_BACKED);
        this.stat = stat;
    }

    @Override
    public <T> T deserialize(JsonElement spellCure, Type type, JsonDeserializationContext context) {
        // TODO
        return null;
    }

    public StatType getStatType() {
        return stat.getType();
    }
}
