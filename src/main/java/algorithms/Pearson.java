package algorithms;

import model.UserPreference;

/**
 * Created by Zahey Boukich on 5-6-2016.
 */
public class Pearson implements Similarity {
    public double calculate(UserPreference target, UserPreference user) {
        double sumX = 0;
        double sumY = 0;
        double sumXY = 0;
        double sumX2 = 0;
        double sumY2 = 0;
        double n = 0;
        for (Number key : target.getRatings().keySet()) {
            if (user.getRatings().containsKey(key)) {
                double x = target.getRatings().get(key);
                double y = user.getRatings().get(key);
                sumXY += x * y;
                sumX += x;
                sumY += y;
                sumX2 += Math.pow(x, 2);
                sumY2 += Math.pow(y, 2);
                n++;}
        }
        if (n == 0) {
            return 0;
        }
        double nominator = sumXY - (sumX * sumY) / n;
        double denominator = Math.sqrt(sumX2 - Math.pow(sumX, 2) / n) * Math.sqrt(sumY2 - Math.pow(sumY, 2) / n);
        if (denominator == 0) {
            return 0;
        } else {
            return nominator / denominator;
        }

    }

}
