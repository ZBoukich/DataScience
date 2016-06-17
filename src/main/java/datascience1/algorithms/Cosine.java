package datascience1.algorithms;

import datascience1.model.UserPreference;

/**
 * Created by Zahey Boukich on 5-6-2016.
 */
public class Cosine implements Similarity {
    public double calculate(UserPreference target, UserPreference user) {
        double dotProduct = 0;
        double sumX2 = 0;
        double sumY2 = 0;
        double n = 0;
        for (Number key : target.getRatings().keySet()) {
            if (user.getRatings().containsKey(key)) {
                double x = target.getRatings().get(key);
                double y = user.getRatings().get(key);
                dotProduct += x * y;
                sumX2 += Math.pow(x, 2);
                sumY2 += Math.pow(y, 2);
                n++;
            }
        }
        if (n == 0) {
            return 0;
        }
        double vectorX = Math.sqrt(sumX2);
        double vectorY = Math.sqrt(sumY2);

        double denominator = vectorX * vectorY;
        return dotProduct / denominator;
    }
}
