package datascience2;

import datascience2.model.ClientOffers;
import datascience2.utility.Utility;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by Zahey Boukich on 16-6-2016.
 */
public class KClusterer {

    private Map<Integer, ClientOffers> dataList;
    private int k;
    private List<Map<Integer, ClientOffers>> clusters;
    private Map<Integer, ClientOffers> initialCentroids;


    public KClusterer(String dataFile, String splitBy, int k) {
        this.dataList = Utility.loadData(dataFile, splitBy);
        this.initialCentroids = getInitialCentroidsRandomly(k);
        this.k = k;
        //System.out.println(" initalCentroids : " + initialCentroids);
        assignClientOffersToCluster(initialCentroids);

    }


    public static void main(String[] args) {
        KClusterer kClusterer = new KClusterer(Constants.WINEDATA,Constants.DELIMITER,3);
    }

    private void assignClientOffersToCluster(Map<Integer, ClientOffers> initialCentroids) {
        Map<Integer, Double> distances = new HashMap<>();
        for (int client : dataList.keySet()) {
            for (int i = 0; i < k; i++) {
                double temp = calculate(dataList.get(client), initialCentroids.get(k));
                distances.put(client, temp);
            }
        }
        System.out.println(distances);
    }


    private Map<Integer, ClientOffers> getInitialCentroidsRandomly(int k) {
        Map<Integer, ClientOffers> clientOffers = new HashMap<>();
        for (int i = 0; i < k; i++) {
            int randomClient = new Random().nextInt(dataList.size());
            clientOffers.put(randomClient, dataList.get(randomClient));
        }
        return clientOffers;
    }


    public double calculate(ClientOffers target, ClientOffers user) {
        double distance = 0;
        for (Integer key : target.getOffers().keySet()) {
            System.out.println("key : " + key);
            distance += Math.pow(Math.abs(target.getOffers().get(key) - user.getOffers().get(key)), 2);
            System.out.println("distance : " + distance);
        }
        distance = Math.sqrt(distance);
        return 1 / (distance + 1);
    }
}
