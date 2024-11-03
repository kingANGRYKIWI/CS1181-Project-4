import java.io.FileNotFoundException;
import java.util.ArrayList;

public class BruteForce {//Logan Current, CS-1181L-07, 4/17/2022

    public static ArrayList<ArrayList<Item>> shtuffs = new ArrayList<>();

    public static ArrayList<Item> getOptimalSet(ArrayList<Item> items) throws InvalidArgumentException{
        if (items.size() > 10) {
            throw new InvalidArgumentException("Size of arraylist cannot be greater than 10!");
        } else { 
            //sets all items to true so it can be easily read by get fitness method
            for (Item eachItem : items) {
                eachItem.setIncluded(true);
            }
            //holds best possible arraylist
            ArrayList<Item> bestOne = new ArrayList<>();
            makeArraySubSets(items);
            //iterates the arraylist of arraylist and find the best one then sets it
            for (ArrayList<Item> eachArrayList : shtuffs) {
                if(getFitness(bestOne) < getFitness(eachArrayList)){
                    bestOne.clear();
                    bestOne.addAll(eachArrayList);
                }
            }
            return bestOne;
        }
    }

    static void makeArraySubSets(ArrayList<Item> set) {
        int n = set.size();
        //loop of 2^n
        for (int i = 0; i < (1<<n); i++) {
            //temporary arraylist 
            ArrayList<Item> subArrayList = new ArrayList<>();
            //used to check bit in binary
            int binary = 1;
            for (int j = 0; j < n; j++) {
                if ((i & binary) > 0) {
                    subArrayList.add(set.get(j));
                }
                binary = binary << 1;
            }
            //adds subList to big arraylist of arraylist
            shtuffs.add(subArrayList);
        }
    }

    public static int getFitness(ArrayList<Item> itemSet){ 
        //copied this from the Chromosome class
        double sumOfWeight = 0;
        int sumOfValue = 0;
        int fitness = 0;
        for(Item eachItem : itemSet){//goes through the set of items in the chromosome
            if(eachItem.isIncluded()){//only counts the weight items that the chromosome has (the items with a true included value)
                sumOfWeight += eachItem.getWeight();
                sumOfValue += eachItem.getValue();
            }
        }
        double totalWeight = sumOfWeight;
        if(totalWeight > 10.0000){//if the set of items is too heavy then it sets fitness to 0
            fitness = 0;
        } else {//else if less than 10 then it will return all the items in the chromosome's items values combined
            fitness = sumOfValue;
        }
        return fitness;
    }

    public static void main(String[] args) throws FileNotFoundException {
        long start = System.currentTimeMillis();
        ArrayList<Item> items = GeneticAlgorithm.readData("items.txt");
        ArrayList<Item> bestOne = getOptimalSet(items);
        long end = System.currentTimeMillis();
        System.out.println(bestOne + "\ntotal fitness: $" + getFitness(bestOne) );
        System.out.println("\ntook: " + (end-start)+" miliseconds");
        
    }
}
