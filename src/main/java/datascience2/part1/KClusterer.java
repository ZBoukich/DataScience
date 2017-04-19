package datascience2.part1;

import datascience2.part1.model.ClientOffers;
import datascience2.part1.utility.Utility;

import java.util.*;

/**
 * Created by Zahey Boukich on 19-4-2017.
 */
public class KClusterer {

    private Map<Integer, ClientOffers> data;
    private List<ClientOffers> centroids;
    private double sse;
    private double smallestSse = 9999;
    private int iterationNumber;
    private int pointsChanged = 0;
    private int k;
    private Map<Integer, Map<Integer, ClientOffers>> smallestSseClusters = new HashMap<>();
    private Map<Integer, Map<Integer, ClientOffers>> resultingClusters;


    public KClusterer(int k, int iterations, String dataFile, String splitBy) {
        this.data = Utility.loadData(dataFile, splitBy);
        this.k = k;
        int xIterations = iterations;

        while (xIterations > 0) {
            resultingClusters = new HashMap<>();
            this.centroids = getInitialCentroidsRandomly(k);

            assignClientsToCluster();
            goCluster();
            xIterations--;
            // System.out.println("KMEANS RUNNING----------------------" + xIterations);
        }

        postProcess();
    }


    private List<ClientOffers> getInitialCentroidsRandomly(int k) {
        List<ClientOffers> randomCentroidsList = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            int randomClient = new Random().nextInt(data.size());
            ClientOffers clientOffers = data.get(randomClient);
            randomCentroidsList.add(clientOffers);
        }
        return randomCentroidsList;
    }


    private void assignClientsToCluster() {
        this.sse = 0;
        this.pointsChanged = 0;


        for (Map.Entry<Integer, ClientOffers> client : data.entrySet()) {
            int clusterNr = 0;
            double error = 9999;

            int counter = 0;

            for (int i = 0; i < centroids.size(); i++) {

                double distance = 0.0;
                counter++;
                if (counter > k) {
                    counter = 1;
                }
                if (client.getValue() != null && centroids.get(i) != null) {
                    distance = calculateEuclideanDistance(client.getValue(), centroids.get(i));
                }
                if (distance < error) {
                    error = distance;
                    clusterNr = counter;
                }
            }

            if (resultingClusters.containsKey(clusterNr)) {
                if (!resultingClusters.get(clusterNr).containsKey(client.getKey())) {
                    this.pointsChanged++;
                }
                resultingClusters.get(clusterNr).put(client.getKey(), client.getValue());

            } else {
                HashMap<Integer, ClientOffers> nextClient = new HashMap<>();
                nextClient.put(client.getKey(), client.getValue());
                resultingClusters.put(clusterNr, nextClient);
                this.pointsChanged++;
            }

            this.sse += Math.pow(error, 2);
        }

    }


    private List<ClientOffers> updateCentroids() {
        List<ClientOffers> updatedCentroids = new ArrayList<>();
        for (Map<Integer, ClientOffers> cluster : resultingClusters.values()) {
            ClientOffers updatedCentroid = updateCentroidsOfCluster(cluster);
            updatedCentroids.add(updatedCentroid);
        }
        return updatedCentroids;
    }

    private ClientOffers updateCentroidsOfCluster(Map<Integer, ClientOffers> cluster) {
        // Offerid and list of offers for example Winenr 1 is x times offered
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
            assignClientsToCluster();

            if (iterationNumber > 100 || pointsChanged / resultingClusters.size() < 0.01) {
                done = true;
            }
            //System.out.println("ALGORITHM ITERATION----------------------" + iterationNumber);
        }

        calculateSmallestSse(sse);
    }

    private void calculateSmallestSse(double sse) {
        if (sse < this.smallestSse) {
            this.smallestSse = sse;
            this.smallestSseClusters = this.resultingClusters;
        }
        System.out.println("SSE----------------------" + smallestSse);
    }

    public double calculateEuclideanDistance(ClientOffers target, ClientOffers centroid) {
        double distance = 0;
        for (Integer key : target.getOffers().keySet()) {
            distance += Math.pow(Math.abs(target.getOffers().get(key) - centroid.getOffers().get(key)), 2);
        }
        distance = Math.sqrt(distance);
        return distance;
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


}
