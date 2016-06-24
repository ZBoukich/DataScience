package datascience2.part2;

import java.util.Arrays;

/**
 * Created by Zahey Boukich on 23-6-2016.
 */
public class Main {
    public static void main(String[] args) {
        int iterations =0;
      Population population = new Population(8).initializePopulation();
        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm();
        population.getIndividuals()[0].getFitness();
        printPopulation(population);
        while(iterations < 5){
            iterations ++;
            population = geneticAlgorithm.evolve(population);
            population.sortIndividualByFitness();
            System.out.println("Generation= " + iterations + " Fittest= "+ population.getIndividuals()[0].getFitness());
            printPopulation(population);
        }

    }

    public static void printPopulation(Population population){

        for (int i=0; i<population.getIndividuals().length; i++){
            System.out.println("X= "+ population.getIndividuals()[i].getX()+  " "+  population.getIndividuals()[i].getGenes() + " Fitness= " + population.getIndividuals()[i].getFitness() );
        }
    }
}




