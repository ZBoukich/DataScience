package datascience2.part2;

import java.util.Arrays;

/**
 * Created by Zahey Boukich on 20-4-2017.
 */
public class Main {


    public static void run(int iterations, int populationSize, double crossOverRate,double mutationRate, Boolean elitism) {
        Population population = new Population(populationSize).initializePopulation();
        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(crossOverRate,mutationRate,elitism);
        for (int i = 0; i < iterations; i++) {
            population = geneticAlgorithm.evolve(population);
            population.sortIndividualByFitness();
            //printPopulation(population, populationSize);
        }

        System.out.println("Last population");
        System.out.println("Best individual= " + population.getIndividuals()[0]);
        System.out.println("Best fitness= " + population.getIndividuals()[0].getFitness());
        printPopulation(population,populationSize);

    }


    public static void main(String[] args) {
        run(10, 8,0.85,0.1,true);
    }


    public static void printPopulation(Population population, int populationSize) {
        int sumFitness = 0;
        for (int i = 0; i < population.getIndividuals().length; i++) {
            int fitness =  population.getIndividuals()[i].getFitness();
            sumFitness+=fitness;
        }

        System.out.println("Average fitness= " + sumFitness/populationSize);
    }
















//    public static void printPopulation(Population population, int populationSize) {
//        int sumFitness = 0;
//        for (int i = 0; i < population.getIndividuals().length; i++) {
//            int fitness =  population.getIndividuals()[i].getFitness();
//            System.out.println(Arrays.toString(population.getIndividuals()[i].getGenes()) + " Fitness= " + fitness);
//            sumFitness+=fitness;
//        }
//
//        System.out.println("Average fitness= " + sumFitness/populationSize);
//    }
}




