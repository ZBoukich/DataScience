package datascience2.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Godfather on 17-6-2016.
 */
@Data
public class ClientOffers {
    public Map<Integer, Integer> offers = new HashMap<>();

    public void addOffers(int offerId, int clientOffer) {
        offers.put(offerId, clientOffer);
    }
}
