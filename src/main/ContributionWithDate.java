import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Record contribution info for specified recipient and date
 */
public class ContributionWithDate {
    private ContributionRecord record = new ContributionRecord();
    private List<Float> contributions = new ArrayList<Float>();

    public void addAmount(float amount) {
        record.totalDollarAmount += amount;
        record.totalContributionNumber++;
        contributions.add(amount);
    }

    public ContributionRecord getMedian() {
        Collections.sort(contributions);
        record.median = (int) ((record.totalContributionNumber % 2 == 0) ?
                Math.round((contributions.get(record.totalContributionNumber / 2 - 1) + contributions.get(record.totalContributionNumber / 2)) / 2.0f) :
                contributions.get(record.totalContributionNumber / 2));
        return record;
    }
}
