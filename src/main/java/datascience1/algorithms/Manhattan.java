package datascience1.algorithms;

import datascience1.model.UserPreference;

/**
 * Created by Zahey Boukich on 5-6-2016.
 */
public class Manhattan implements Similarity {

    public double calculate(UserPreference target, UserPreference user) {
        double distance = 0;
        for (Number key : target.getRatings().keySet()) {
            if (user.getRatings().containsKey(key)) {
                distance += Math.abs((Double) target.getRatings().get(key) - (Double) user.getRatings().get(key));
            }
        }
        return 1 / (distance + 1);
    }
}
