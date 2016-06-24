package datascience2.part2;

import lombok.Data;

import java.util.Random;

/**
 * Created by Zahey Boukich on 22-6-2016.
 */
@Data
public class Individual {
    private String genes;
    private int x;
    private int genesSize;
    private int range;
    private int fitness;

    public Individual(int genesSize, int range) {
        this.genesSize = genesSize;
        this.range = range;
    }

    public Individual initializeIndividual() {
        if (genesSize != 0) {
            Random random = new Random();
            x = random.nextInt(range);
            genes = Integer.toBinaryString(x);
            while (genes.length() < genesSize) {
                genes = "0" + genes;
            }
        }
        return this;
    }

    public int getFitness() {
        return recomputeFitness();
    }

    public int recomputeFitness() {
        fitness = 0;
        if (x != 0) {
            fitness = (int) (Math.pow(-(x), 2) + (7 * x));
        }
        return fitness;
    }

    @Override
    public String toString() {
        return genes;
    }
}
