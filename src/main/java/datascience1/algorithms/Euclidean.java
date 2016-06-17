package datascience1.algorithms;

import datascience1.model.UserPreference;

/**
 * Created by Zahey Boukich on 5-6-2016.
 */
public class Euclidean implements Similarity {

    @Override
    public double calculate(UserPreference target, UserPreference user) {
        double distance = 0;
        for (Number key : target.getRatings().keySet()) {
            if (user.getRatings().containsKey(key)) {
                distance += Math.pow(Math.abs((Double) target.getRatings().get(key) - (Double) user.getRatings().get(key)), 2);
            }
        }
        distance = Math.sqrt(distance);
        return 1 / (distance + 1);
    }

}
