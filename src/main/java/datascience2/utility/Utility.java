package datascience2.utility;

import datascience2.model.ClientOffers;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Zahey Boukich on 17-6-2016.
 */
public class Utility {

    public static Map<Integer, ClientOffers> loadData(String file, String splitBy) {
        Map<Integer, ClientOffers> clientOffersHashMap = new HashMap<>();
        try {
            File fileToParse = new File(file);
            String line = null;
            int linePosition = 1;
            BufferedReader reader = new BufferedReader(new FileReader(fileToParse));
            while ((line = reader.readLine()) != null) {

                String[] cols = line.split(splitBy);
                for (int i = 0; i < cols.length; i++) {
                    ClientOffers clientOffers = new ClientOffers();
                    int clientId = i + 1;
                    int offerId = linePosition;
                    int clientOffer = Integer.parseInt(cols[i]);

                    if (clientOffersHashMap.containsKey(clientId)) {
                        clientOffersHashMap.get(clientId).addOffers(offerId, clientOffer);
                    } else {
                        clientOffers.addOffers(offerId, clientOffer);
                        clientOffersHashMap.put(clientId, clientOffers);
                    }
                }
                linePosition += 1;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return clientOffersHashMap;
    }

}
