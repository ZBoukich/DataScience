package datascience2.part2;

import lombok.Data;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by Zahey Boukich on 20-4-2017.
 */
@Data
public class Individual {

    private int x;
    private int[] genes;
    private int fitness;


    public Individual(int length) {
        this.genes = new int[length];
    }

    public Individual initializeRandomIndividual() {
        String binary = "";
        Random random = new Random();
        x = random.nextInt(32);
        binary = Integer.toBinaryString(x);

        while (binary.length() < genes.length) {
            binary = "0" + binary;
        }
        String[] binaryToArray = binary.split("");
        genes = Arrays.stream(binaryToArray).mapToInt(Integer::parseInt).toArray();

        return this;
    }

    public int getFitness() {
        return recomputeFitness();
    }

    public int recomputeFitness() {
        fitness = (int) (Math.pow(-(x), 2) + (7 * x));
        return fitness;
    }

    public String toString(){
        return Arrays.toString(this.genes);
    }


}
