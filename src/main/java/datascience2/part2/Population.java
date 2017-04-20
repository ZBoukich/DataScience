package datascience2.part2;

import lombok.Data;

import java.util.Arrays;

/**
 * Created by Zahey Boukich on 20-4-2017.
 */

@Data
public class Population {

    private Individual[] individuals;

    public Population(int length) {
        this.individuals = new Individual[length];
    }

    public Population initializePopulation() {
            for (int i = 0; i < individuals.length; i++) {
                individuals[i] = new Individual(5).initializeRandomIndividual();
            }
            sortIndividualByFitness();
        return this;
    }

    public void sortIndividualByFitness() {
        Arrays.sort(individuals, (x, y) -> {
            int flag = 0;
            if (x.getFitness() > y.getFitness()) {
                flag = -1;
            } else if (x.getFitness() < y.getFitness()) {
                flag = 1;
            }
            return flag;
        });
    }


}
