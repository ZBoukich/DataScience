package algorithms;

import model.UserPreference;

import java.util.HashMap;

/**
 * Created by Zahey Boukich on 5-6-2016.
 */
public interface Similarity {

    public double calculate(UserPreference target, UserPreference user);


}
