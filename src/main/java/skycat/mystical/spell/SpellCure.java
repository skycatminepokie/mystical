package skycat.mystical.spell;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Getter @Setter
public class SpellCure {

    private double contributionGoal;
    private ArrayList<CureContribution> contributions = new ArrayList<>();

    public void contribute(@Nullable UUID uuid, double amount) {
        contributions.add(new CureContribution(uuid, LocalDateTime.now(), amount));
        // TODO: Logging
    }

    @ToString
    public record CureContribution(@Nullable UUID contributor, @Nullable LocalDateTime time, double amount) { }
}
