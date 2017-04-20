package datascience2.part2;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Zahey Boukich on 20-4-2017.
 */
public class GeneticAlgorithm {
    public  int ELITE = 1;
    public static final int TOURNAMENT_SELECTION_SIZE = 1;
    public  double CROSSOVER_RATE = 0.85;
    public  double MUTATION_RATE = 0.1;


    public GeneticAlgorithm(double crossover, double mutation, boolean elitism){
        if(elitism){
            this.ELITE =1;
        }
        this.CROSSOVER_RATE = crossover;
        this.MUTATION_RATE = mutation;
    }

    public Population evolve(Population population) {
        return mutatePopulation(crossOverPopulation(population));
    }


    private Population crossOverPopulation(Population population) {
        Population crossOverPopulation = new Population(population.getIndividuals().length);

        for (int i = 0; i < ELITE; i++) {
            crossOverPopulation.getIndividuals()[i] = population.getIndividuals()[i];
        }
        for (int i = ELITE; i < population.getIndividuals().length; i++) {
            Individual one = selectTournamentPopulation(population).getIndividuals()[0];
            Individual two = selectTournamentPopulation(population).getIndividuals()[0];
            crossOverPopulation.getIndividuals()[i] = crossOverIndividual(one, two);
        }

        return crossOverPopulation;
    }


    private Population mutatePopulation(Population population) {
        Population mutatePopulation = new Population(population.getIndividuals().length);

        for (int i = 0; i < ELITE; i++) {
            mutatePopulation.getIndividuals()[i] = population.getIndividuals()[i];
        }
        for (int i = ELITE; i < population.getIndividuals().length; i++) {

            mutatePopulation.getIndividuals()[i] = mutateIndividual(population.getIndividuals()[i]);
        }

        return mutatePopulation;
    }


    private Individual crossOverIndividual(Individual first, Individual second) {
        Individual crossOverIndividual = new Individual(5);
        for (int i = 0; i < crossOverIndividual.getGenes().length; i++) {
            if (Math.random() < CROSSOVER_RATE) {
                if (Math.random() < 0.5) {
                    crossOverIndividual.getGenes()[i] = first.getGenes()[i];
                } else {
                    crossOverIndividual.getGenes()[i] = second.getGenes()[i];
                }
            }
        }
        return crossOverIndividual;
    }

    private Individual mutateIndividual(Individual individual) {
        Individual mutateIndividual = new Individual(5);

        for (int i = 0; i < mutateIndividual.getGenes().length; i++) {
            if (Math.random() < MUTATION_RATE) {
                if (Math.random() < 0.5) {
                    mutateIndividual.getGenes()[i] = 1;
                } else {
                    mutateIndividual.getGenes()[i] = 0;
                }
            } else {
                mutateIndividual.getGenes()[i] = individual.getGenes()[i];
            }
        }
        recomputeFitness(mutateIndividual);
        return mutateIndividual;
    }

    private void recomputeFitness(Individual mutateIndividual) {
        Map<Integer, String> map = binaryMap();
        String binary = "";
        for (int i = 0; i < mutateIndividual.getGenes().length; i++) {
            binary += mutateIndividual.getGenes()[i];
        }
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            if (entry.getValue().equals(binary)) {
                mutateIndividual.setX(entry.getKey());
                mutateIndividual.recomputeFitness();
            }
        }
    }


    private Population selectTournamentPopulation(Population population) {
        Population tournamentPopulation = new Population(TOURNAMENT_SELECTION_SIZE);

        for (int i = 0; i < TOURNAMENT_SELECTION_SIZE; i++) {
            tournamentPopulation.getIndividuals()[i] = population.getIndividuals()[(int) Math.random() * population.getIndividuals().length];
        }
        tournamentPopulation.sortIndividualByFitness();
        return tournamentPopulation;
    }


    Map<Integer, String> binaryMap() {
        Map<Integer, String> map = new HashMap<>();
        String binary = "";
        for (int i = 0; i < 32; i++) {
            binary = Integer.toBinaryString(i);
            while (binary.length() < 5) {
                binary = "0" + binary;
            }
            map.put(i, binary);
        }
        return map;
    }


}
