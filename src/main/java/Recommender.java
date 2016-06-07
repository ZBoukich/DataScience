import algorithms.Euclidean;
import algorithms.Similarity;
import model.UserPreference;
import utility.Utility;

import java.util.*;

/**
 * Created by Zahey Boukich on 4-6-2016.
 */
public class Recommender {
    private Map<Number, UserPreference> dataList;
    private Comparator<Map.Entry<Number, Double>> reverseOrderComparator = (x, y) -> x.getValue().compareTo(y.getValue());
    private Similarity simAlgorithm;
    private int n;
    private int k;
    private double thresHold;

    public static void main(String[] args) {
        Recommender recommender = new Recommender(Constants.USERITEMDATA, Constants.SMALLDATA_DELIMITER, 4, 0.35, new Euclidean(), 3);
        recommender.recommend(7);
    }

    public Recommender(String dataFile, String splitBy, int k, double thresHold, Similarity simAlgorithm, int n) {
        this.dataList = Utility.loadData(dataFile, splitBy);
        this.simAlgorithm = simAlgorithm;
        this.n = n;
        this.k = k;
        this.thresHold = thresHold;
    }

    public void recommend(int targetUserId) {
        List<Map.Entry<Number, Double>> nearestNeighborsList = computeNearestNeighbors(targetUserId);
        System.out.println("\n Neighbours : " + nearestNeighborsList);
        Map<Number, Map<Number, Double>> predictedList = predictRating(targetUserId, nearestNeighborsList);
        List<Map.Entry<Number, Double>> predictedItemRatingsList = new ArrayList<>();
        for (Map<Number, Double> map : predictedList.values()) {
            for (Map.Entry entry : map.entrySet()) {
                predictedItemRatingsList.add(entry);
            }
        }
        Collections.sort(predictedItemRatingsList, reverseOrderComparator);
        Collections.reverse(predictedItemRatingsList);

        Object[] item = predictedItemRatingsList.stream().map(Map.Entry::getKey).limit(n).toArray();
        for (Object itemId : item) {
            System.out.printf("Recommended for you item with id %s\n", itemId);
        }
    }

    public List<Map.Entry<Number, Double>> computeNearestNeighbors(Number targetUserId) {
        Map<Number, Double> distances = new HashMap<>();
        List<Map.Entry<Number, Double>> nearestNeighborsList = new ArrayList<>();
        Set<Number> users = dataList.keySet();
        for (Number user : users) {
            if (user != targetUserId) {
                double temp = simAlgorithm.calculate(dataList.get(user), dataList.get(targetUserId));
                if (temp >= thresHold) {
                    distances.put(user, temp);
                }
            }
        }
        nearestNeighborsList.addAll(distances.entrySet());
        Collections.sort(nearestNeighborsList, reverseOrderComparator);
        Collections.reverse(nearestNeighborsList);
        if (nearestNeighborsList.size() < k) {
            System.out.printf("This user has less k nearest neighbors then you would think,the total is %d. You could modify the threshold",nearestNeighborsList.size());
            return nearestNeighborsList;
        } else {
            return nearestNeighborsList.subList(0, k);
        }
    }

    public Map<Number, Map<Number, Double>> predictRating(int targetUserID, List<Map.Entry<Number, Double>> nearestNeighborsList) {
        Map<Number, Map<Number, Double>> recommendations = new TreeMap<>();
        Map<Number, Double> targetUserRatings = dataList.get(targetUserID).getRatings();
        double totalDistance = 0.0;
        for (Map.Entry<Number, Double> distance : nearestNeighborsList) {
            totalDistance += distance.getValue();
        }
        for (Map.Entry<Number, Double> distance : nearestNeighborsList) {
            double weight = distance.getValue() / totalDistance;
            Number user = distance.getKey();
            Map<Number, Double> neighborRatings = dataList.get(user).getRatings();

            for (Number item : neighborRatings.keySet()) {
                if (!targetUserRatings.containsKey(item)) {
                    TreeMap<Number, Double> weightedHashMap = new TreeMap<>();
                    Double itemresult = (neighborRatings.get(item)) * weight;
                    if (!recommendations.containsKey(item)) {
                        weightedHashMap.put(item, itemresult);
                    } else {
                        Double alreadyRated = recommendations.get(item).get(item);
                        Double sum = itemresult + alreadyRated;
                        weightedHashMap.put(item, sum);
                    }
                    recommendations.put(item, weightedHashMap);
                }
            }
        }
        return recommendations;
    }
}
