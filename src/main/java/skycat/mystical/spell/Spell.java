package skycat.mystical.spell;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Spell {
    private SpellConsequence consequence;
    private Class<?> eventClass;
    private SpellCure cure;
}
