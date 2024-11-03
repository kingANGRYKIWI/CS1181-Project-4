import java.util.ArrayList;
import java.util.Collections;

public class GeneticThread extends Thread  {//Logan Current, CS-1181L-07, 4/17/2022

    private final int NUM_EPOCHS;
    private final int NUM_THREADS;
    private ArrayList<Chromosome> copyOfChromosomes = new ArrayList<>();
    private Chromosome mostFit;
    

    private ArrayList<Chromosome> nextGen = new ArrayList<>();//saves the top ten then adds it back, eventually it will save the top ten overall after the 20 times the program loops then print the top 10

    public GeneticThread(ArrayList<Chromosome> chromosomes, int NUM_EPOCHS, int NUM_THREADS){
        this.NUM_EPOCHS = NUM_EPOCHS;
        this.NUM_THREADS = NUM_THREADS;
        this.copyOfChromosomes = new ArrayList<>(chromosomes);
        
    }

    public Chromosome getMostFitChromosome(){
        return mostFit;
    }

    @Override
    public void run(){
        for (int x = 0; x<(NUM_EPOCHS/NUM_THREADS); x++){
            if(!nextGen.isEmpty()){
                for(int i = 0; i < nextGen.size()*0.1; i++){//only mutates 10 percent
                    nextGen.get(i).mutate();
                    copyOfChromosomes.clear();
                }
                
            }
            for(Chromosome eachChromosome : nextGen){//adds the top 10 individuals back to the last population
                
                copyOfChromosomes.add(eachChromosome);
            }
            nextGen.clear();//clears the list to add the top ten of this generation back to the next one. So should end with 10 individuals just like what I started with
            Collections.shuffle(copyOfChromosomes);//randomizes the population so it's different people making different kids each time
            int parent1 = 0;//2,4,6,8,10, etc
            int parent2 = 1;//3,5,7,9,11, etc
            int originalSize = copyOfChromosomes.size();//checks the size of the population so it knows when to stop making kids
            int onlyParents = (copyOfChromosomes.size() /2); //only makes kids with the orignial parents in the population(so kids don't make other kids)
            OuterLoop: for(int i = 0; i< onlyParents; i++){//goes through only parents (in pairs)
                    if (parent1 == originalSize) {//if it tries to make kids with kids it breaks the loop and continues on
                        break OuterLoop;
                    }
                    Chromosome childChromosome = copyOfChromosomes.get(parent1).crossover(copyOfChromosomes.get(parent2));//makes the kid
                    parent1 = parent1 + 2;//makes kids in pairs (none can cheat on the other b/c that's bad)
                    parent2 = parent2 + 2;//makes kids in pairs (none can cheat on the other b/c that's bad)
                    nextGen.add(childChromosome);//adds to population
            }
            Collections.shuffle(copyOfChromosomes);//randomizes the population to mutate
            
            
            Collections.sort(copyOfChromosomes);//sorts the population based on fitness of each individual
            for(int i = 0; i < 10; i++){//takes only the top 10 individuals (since the Collections.sort() sorts it by making the fittest to least fit, you only need to take the chromosomes from index 0-9)
                nextGen.add(copyOfChromosomes.get(i));//adds it to top 10 for future generation
            }
        }
        Collections.sort(nextGen);
        mostFit = nextGen.get(1);
    }

}
