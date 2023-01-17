package skycat.mystical.spell;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter @Setter
public class SpellCure {

    private double contributionGoal;


    @Value
    public static class CureContribution {
        UUID contributor;
        LocalDateTime time;
        double amount;


        public CureContribution(@NonNull UUID contributor, @NonNull LocalDateTime time, double amount) {
            this.contributor = contributor;
            this.time = time;
            this.amount = amount;
        }

        public CureContribution(@NonNull LocalDateTime time, double amount) {
            this.contributor = null;
            this.time = time;
            this.amount = amount;
        }

        public CureContribution(@NonNull UUID contributor, double amount) {
            this.contributor = contributor;
            this.time = null;
            this.amount = amount;
        }
    }
}
