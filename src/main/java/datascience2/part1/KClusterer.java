package datascience2.part1;

import datascience2.part1.model.ClientOffers;
import datascience2.part1.utility.Utility;

import java.util.*;

/**
 * Created by Zahey Boukich on 16-6-2016.
 */
public class KClusterer {

    private Map<Integer, ClientOffers> dataList;
    private double sse;
    private double smallestSse;
    private int iterationNumber;
    private int pointsChanged = 0;
    private int k;
    private Map<Integer, Map<Integer, ClientOffers>> smallestSseClusters = new HashMap<>();
    private Map<Integer, Map<Integer, ClientOffers>> tempClusters;
    private List<ClientOffers> centroids;


    public KClusterer(String dataFile, int k, int iterateXTimes, String splitBy) {
        this.dataList = Utility.loadData(dataFile, splitBy);
        this.k = k;
        int xIterations = iterateXTimes;
        while (xIterations > 0) {
            tempClusters = new HashMap<>();
            this.centroids = getInitialCentroidsRandomly(k);
            assignClientOffersToCluster();
            goCluster();
            xIterations--;
        }
        postProcess();
    }

    private List<ClientOffers> getInitialCentroidsRandomly(int k) {
        List<ClientOffers> randomCentroidsList = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            int randomClient = new Random().nextInt(dataList.size());
            ClientOffers clientOffers = dataList.get(randomClient);
            randomCentroidsList.add(clientOffers);
        }
        return randomCentroidsList;
    }

    private void assignClientOffersToCluster() {
        this.sse = 0;
        this.pointsChanged = 0;
        for (Map.Entry<Integer, ClientOffers> client : dataList.entrySet()) {
            int clusterNumber = 0;
            int cluster = 0;
            double temp = 0.0;
            for (int i = 0; i < centroids.size(); i++) {
                double sim = 0.0;
                cluster++;
                if (cluster > k) {
                    cluster = 1;
                }
                if (client.getValue() != null && centroids.get(i) != null) {
                    sim = calculateEuclideanDistance(client.getValue(), centroids.get(i));
                }
                if (sim > temp) {
                    temp = sim;
                    clusterNumber = cluster;
                }
            }
            if (tempClusters.containsKey(clusterNumber)) {
                if (!tempClusters.get(clusterNumber).containsKey(client.getKey())) {
                    this.pointsChanged++;
                }
                tempClusters.get(clusterNumber).put(client.getKey(), client.getValue());


            } else {
                HashMap<Integer, ClientOffers> nextClient = new HashMap<>();
                nextClient.put(client.getKey(), client.getValue());
                tempClusters.put(clusterNumber, nextClient);
                this.pointsChanged++;
            }
            this.sse += Math.pow(temp, 2);
        }
    }

    private List<ClientOffers> updateCentroids() {
        List<ClientOffers> updatedCentroids = new ArrayList<>();
        for (Map<Integer, ClientOffers> cluster : tempClusters.values()) {
            ClientOffers updatedCentroid = updateCentroidsOfCluster(cluster);
            updatedCentroids.add(updatedCentroid);
        }
        return updatedCentroids;
    }

    private ClientOffers updateCentroidsOfCluster(Map<Integer, ClientOffers> cluster) {
        Map<Integer, List<Double>> sumMapList = new HashMap<>();
        for (Map.Entry value : cluster.entrySet()) {
            int counter = 0;
            Map<Integer, Double> clientOffers = ((ClientOffers) value.getValue()).getOffers();
            for (Double val : clientOffers.values()) {
                if (sumMapList.containsKey(counter)) {
                    sumMapList.get(counter).add(val);
                } else {
                    List<Double> tempList = new ArrayList<>();
                    tempList.add(val);
                    sumMapList.put(counter, tempList);
                }
                counter++;
            }
        }
        return determineMean(sumMapList);
    }

    private ClientOffers determineMean(Map<Integer, List<Double>> sumMapList) {
        ClientOffers centroid = new ClientOffers();
        int counter = 1;
        for (List<Double> list : sumMapList.values()) {
            double sumValue = list.stream().mapToDouble(Double::doubleValue).sum();
            double mean = (sumValue / list.size());
            centroid.addOffers(counter, mean);
            counter++;
        }
        return centroid;
    }

    public void goCluster() {
        boolean done = false;
        while (!done) {
            this.iterationNumber++;
            this.centroids = updateCentroids();
            assignClientOffersToCluster();
            if (iterationNumber > 25 || pointsChanged / tempClusters.size() < 0.00) {
                done = true;
            }
        }
        calculateSmallestSse(sse);
        this.smallestSse = this.sse;
    }

    private void calculateSmallestSse(double sse) {
        if (sse < this.smallestSse) {
            this.smallestSse = sse;
            this.smallestSseClusters = this.tempClusters;
        }
    }

    public double calculateEuclideanDistance(ClientOffers target, ClientOffers user) {
        double distance = 0;
        for (Integer key : target.getOffers().keySet()) {
            distance += Math.pow(Math.abs(target.getOffers().get(key) - user.getOffers().get(key)), 2);
        }
        distance = Math.sqrt(distance);
        return 1 / (distance + 1);
    }

    private void postProcess() {
        Map<Integer, Map<Integer, Double>> lists = new HashMap<>();
        int clusterIndex = 1;
        for (Map<Integer, ClientOffers> map : smallestSseClusters.values()) {
            Map<Integer, List<Double>> sumMapList = new HashMap<>();
            Map<Integer, Double> resultList;
            for (Map.Entry<Integer, ClientOffers> value : map.entrySet()) {
                int counter = 0;
                Map<Integer, Double> clientOffers = value.getValue().getOffers();
                for (Double val : clientOffers.values()) {
                    if (sumMapList.containsKey(counter)) {
                        sumMapList.get(counter).add(val);
                    } else {
                        List<Double> tempList = new ArrayList<>();
                        tempList.add(val);
                        sumMapList.put(counter, tempList);
                    }
                    counter++;
                }
            }
            resultList = proces(sumMapList);
            lists.put(clusterIndex, resultList);
            clusterIndex++;
        }
        finishPostProces(lists);
    }

    private void finishPostProces(Map<Integer, Map<Integer, Double>> lists) {
        Comparator<Map.Entry<Integer, Double>> reverseOrderComparator = (x, y) -> x.getValue().compareTo(y.getValue());
        int clusterIndex = 0;
        System.out.printf("\nFinal SSE: %s \n", this.smallestSse);
        System.out.println(" ");
        for (Map<Integer, Double> map : lists.values()) {
            clusterIndex++;
            List<Map.Entry<Integer, Double>> orderedList = new ArrayList<>();
            for (Map.Entry entry : map.entrySet()) {
                orderedList.add(entry);
            }
            Collections.sort(orderedList, reverseOrderComparator);
            Collections.reverse(orderedList);

            System.out.printf("Cluster= %s\n", clusterIndex);
            Object[] item = orderedList.stream().filter(x -> x.getValue() > 3).map(Map.Entry::getKey).toArray();
            for (Object itemId : item) {
                Integer value = map.get(itemId).intValue();
                System.out.printf("OFFER %d -> bought %d times\n", itemId, value);
            }
            System.out.println(" ");
        }
    }

    private Map<Integer, Double> proces(Map<Integer, List<Double>> sumMapList) {
        Map<Integer, Double> resultList = new HashMap<>();
        int index = 0;
        for (List<Double> list : sumMapList.values()) {
            index++;
            double sumValue = list.stream().mapToDouble(Double::doubleValue).sum();
            resultList.put(index, sumValue);
        }
        return resultList;
    }

    private void test() {
        smallestSseClusters.entrySet().stream()
                .forEach(x -> x.getValue().entrySet()
                        .stream()
                        .forEach(y -> System.out.println(" key= " + x.getKey() + " " + y.getKey() + " " + y.getValue() + "\n")));
        System.out.println("\n");
    }


}
