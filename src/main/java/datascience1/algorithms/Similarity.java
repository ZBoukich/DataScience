package datascience1.algorithms;

import datascience1.model.UserPreference;

/**
 * Created by Zahey Boukich on 5-6-2016.
 */
public interface Similarity {

    public double calculate(UserPreference target, UserPreference user);


}
