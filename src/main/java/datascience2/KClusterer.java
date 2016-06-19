package datascience2;

import datascience2.model.ClientOffers;
import datascience2.utility.Utility;

import java.util.*;

/**
 * Created by Zahey Boukich on 16-6-2016.
 */
public class KClusterer {

    private Map<Integer, ClientOffers> dataList;
    private double sse;
    private int iterationNumber;
    private int pointsChanged = 0;
    private int k;
    private Map<Integer, Map<Integer, ClientOffers>> clusters = new HashMap<>();
    private List<ClientOffers> centroids;


    public KClusterer(String dataFile, String splitBy, int k) {
        this.dataList = Utility.loadData(dataFile, splitBy);
        this.k = k;
        this.centroids = getInitialCentroidsRandomly(k);
        assignClientOffersToCluster();
    }

    public static void main(String[] args) {
        KClusterer kClusterer = new KClusterer(Constants.WINEDATA, Constants.DELIMITER, 3);
        kClusterer.kCluster();
    }

    public void kCluster() {
        boolean done = false;
        System.out.println("BEFORE= ");
        System.out.println(clusters);


        while (!done) {

            this.iterationNumber++;
            this.centroids = updateCentroids();
            assignClientOffersToCluster();
            if (iterationNumber > 10 || pointsChanged / clusters.size() < 0.01) {
                done = true;
            }
        }
        System.out.printf("\nFinal SSE: %s", this.sse);
        System.out.println("\nAFTER= _________________");
        System.out.println(clusters);
        System.out.println("\npointschanged " + pointsChanged);
    }


    private void assignClientOffersToCluster() {
        this.sse = 0;
        this.pointsChanged = 0;
        for (Map.Entry<Integer, ClientOffers> client : dataList.entrySet()) {
            int clusterNumber = 0;
            int cluster = 0;
            double temp = 0.0;
            for (int i = 0; i < centroids.size(); i++) {
                double sim;
                cluster++;
                if (cluster > k) {
                    cluster = 1;
                }
                sim = calculate(client.getValue(), centroids.get(i));
                if (sim > temp) {
                    temp = sim;
                    clusterNumber = cluster;
                }
            }
            if (clusters.containsKey(clusterNumber)) {
                if (!clusters.get(clusterNumber).containsKey(client.getKey())) {
                    this.pointsChanged++;
                }
                clusters.get(clusterNumber).put(client.getKey(), client.getValue());


            } else {
                HashMap<Integer, ClientOffers> nextClient = new HashMap<>();
                nextClient.put(client.getKey(), client.getValue());
                clusters.put(clusterNumber, nextClient);
                pointsChanged++;
            }
            this.sse += Math.pow(temp, 2);
        }
    }


    private List<ClientOffers> updateCentroids() {
        List<ClientOffers> updatedCentroids = new ArrayList<>();
        for (Map<Integer, ClientOffers> cluster : clusters.values()) {
            ClientOffers updatedCentroid = updateCentroid(cluster);
            updatedCentroids.add(updatedCentroid);
        }
        return updatedCentroids;
    }

    private ClientOffers updateCentroid(Map<Integer, ClientOffers> cluster) {
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

        return determineCentroid(sumMapList);
    }

    private ClientOffers determineCentroid(Map<Integer, List<Double>> sumMapList) {
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


    private List<ClientOffers> getInitialCentroidsRandomly(int k) {
        List<ClientOffers> randomCentroidsList = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            int randomClient = new Random().nextInt(dataList.size());
            ClientOffers clientOffers = dataList.get(randomClient);
            randomCentroidsList.add(clientOffers);
        }
        return randomCentroidsList;
    }

    public double calculate(ClientOffers target, ClientOffers user) {
        double distance = 0;
        for (Integer key : target.getOffers().keySet()) {
            distance += Math.pow(Math.abs(target.getOffers().get(key) - user.getOffers().get(key)), 2);
        }
        distance = Math.sqrt(distance);
        return 1 / (distance + 1);
    }


}
