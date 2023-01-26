package skycat.mystical.spell;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Getter @Setter
public class SpellCure {

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
}
