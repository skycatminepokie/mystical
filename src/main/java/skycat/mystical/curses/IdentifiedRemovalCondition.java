package skycat.mystical.curses;

import net.minecraft.util.Identifier;

public class IdentifiedRemovalCondition extends CurseRemovalCondition {
    Identifier identifier; // ex WALK_ONE_CM

    public IdentifiedRemovalCondition(Identifier identifier, double amountRequired, double amountFulfilled) {
        super(amountRequired, amountFulfilled);
        this.identifier = identifier;
    }
}
