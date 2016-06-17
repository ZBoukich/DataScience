package datascience1.model;

import lombok.Data;

import java.util.*;

/**
 * Created by Zahey Boukich on 4-6-2016.
 */
@Data
public class UserPreference{

    public Map<Number,Double> ratings = new HashMap<>();

    public void addRating(int itemId, double rating) {
        ratings.put(itemId,rating);
    }

}
