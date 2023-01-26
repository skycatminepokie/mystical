package skycat.mystical.spell;

public /* abstract */ class SpellConsequence {

    public <T> boolean supportsEvent(Class<T> eventClass) {
        return eventClass.isInstance(this);
    }
}
