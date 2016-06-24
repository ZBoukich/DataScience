package datascience2.part1.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Godfather on 17-6-2016.
 */
@Data
public class ClientOffers {
    public Map<Integer, Double> offers = new HashMap<>();

    public void addOffers(int offerId, double clientOffer) {
        offers.put(offerId, clientOffer);
    }

}
