package datascience2.part2;

/**
 * Created by Zahey Boukich on 21-6-2016.
 */
public class GeneticAlgorithm {
    public static final int ELITE =1;

    public Population evolve(Population population) {
        return mutate(crossOver(population));
    }

    private Population crossOver(Population population) {
        Population crossOverPopulation = new Population(population.getIndividuals().length);
        for(int i=0; i< ELITE; i++){
            crossOverPopulation.getIndividuals()[i]= population.getIndividuals()[i];
        }
        return crossOverPopulation;
    }

    private Population mutate(Population population) {
        Population mutatePopulation = new Population(population.getIndividuals().length);
        for(int i=0; i< ELITE; i++){
            mutatePopulation.getIndividuals()[i]= population.getIndividuals()[i];
        }
        return mutatePopulation;
    }


}
