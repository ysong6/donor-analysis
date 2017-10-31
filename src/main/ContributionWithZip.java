import java.util.Collections;
import java.util.PriorityQueue;

/**
 * Record contribution info for specified recipient and zipcode
 */
public class ContributionWithZip {
    ContributionRecord record = new ContributionRecord();
    PriorityQueue<Float> maxHeap = new PriorityQueue<Float>(20);
    PriorityQueue<Float> minHeap = new PriorityQueue<Float>(20, Collections.reverseOrder());

    public ContributionRecord addAmount(float amount) {
        // update total amount and contribution number
        record.totalDollarAmount += amount;
        record.totalContributionNumber ++;
        // calculate runtime median
        maxHeap.offer(amount);
        minHeap.offer(maxHeap.poll());
        if(maxHeap.size() < minHeap.size()){
            maxHeap.offer(minHeap.poll());
        }
        record.median = (int) (maxHeap.size() == minHeap.size() ? Math.round((maxHeap.peek() + minHeap.peek()) / 2.0f) : maxHeap.peek());
        return record;
    }
}
