import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class GeneticAlgorithm {//Logan Current, CS-1181L-07, 4/17/2022
    public static ArrayList<Item> readData(String filename) throws FileNotFoundException{//reads through the file and makes an arraylist for all the items in the file
        File file = new File(filename);
        Scanner fileScan = new Scanner(file);
        ArrayList<Item> itemArayList = new ArrayList<Item>();
        while(fileScan.hasNextLine()){//scans the file
            String line = fileScan.nextLine();
            String[] tokens = line.split(", ");
            //splits it all by the space and comma separating it all
            String name = tokens[0];//takes the name from the string aray
            double weight = Double.parseDouble(tokens[1]);//takes the weight from string array
            int value = Integer.parseInt(tokens[2]);//takes the value from the string array
            Item item = new Item(name, weight, value);//makes the corrosponding item
            itemArayList.add(item);//adds it to the list
        }
        fileScan.close();
        return itemArayList;
    }
    public static ArrayList<Chromosome> initializePopulation(ArrayList<Item> items, int populationSize) {
        //makes the population of chromosomes and adds them to the population, as well as gives them items (see constructor in Chromosome class)
        ArrayList<Chromosome> chromosomeArrayList = new ArrayList<Chromosome>();
        for(int i = 0; i < populationSize; i++){
            Chromosome chromosome = new Chromosome(items);//makes a new person with random items from item list
            chromosomeArrayList.add(chromosome);//adds to population
        }
        return chromosomeArrayList;
    }

    public static final int POP_SIZE = 100; //100

    public static final int NUM_EPOCHS = 1000; //1000

    public static final int NUM_THREADS = 4; //1

    public static void main(String[] args) throws 
    FileNotFoundException, InterruptedException {
        
        String filename = "more_items.txt";
        ArrayList<Item> items = readData(filename);
        ArrayList<Chromosome> chromosome = initializePopulation(items, POP_SIZE);//population is 10

        GeneticThread[] workers = new GeneticThread[NUM_THREADS];

        for (int i=0; i<NUM_THREADS; i++) {//makes then starts all the threads
            workers[i] = new GeneticThread(chromosome, NUM_EPOCHS, NUM_THREADS);
            workers[i].start();
        }

        ArrayList<Chromosome> bestOfTheBest = new ArrayList<Chromosome>();
        
        for (int i=0; i<NUM_THREADS; i++) {//joins all the threads
            workers[i].join();
            
            bestOfTheBest.add(workers[i].getMostFitChromosome());//adds best of each thread to a list
        }

        Collections.sort(bestOfTheBest);//sorts based on fitness
        System.out.println("This is the fittest individual: " + bestOfTheBest.get(0));
    }
}
