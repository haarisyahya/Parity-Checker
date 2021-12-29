import java.util.Random;

/** This class is responsible for performing the neural network operations using the parameters passed 
 *  in the main class. The weights and hidden nodes are initialized randomly. The run method is called
 *  and the sample data is passed through the number of iterations until they are done and then the
 *  data is passed through one final time to be tested. 
 * 
  *
  * @author <Haaris Yahya>
  * @version 1.0 (<12/02/2021>)
  * COSC 3P71                                                        */

public class parityBit {

    private double[][] weights; // 2D adjacency matrix for the weights
    private double[] errors; // 2D adjacency matrix for the errors that are back propagated
    private double[] total; 
    private double learningRate; 
    private int hiddenNodes; 
    private Random rand; 
    private int epochs; // the iterations   
    private long seed; // random seed 
    private double[] val; // 4 input + n hidden
    
    // This is the constructor, initilaizing all the variables declared above
    public parityBit (double learningRate, int hiddenNodes, int epochs, long seed) {

        val = new double[4 + hiddenNodes]; 
        total = new double[4 + hiddenNodes + 1];
        weights = new double[4 + hiddenNodes + 1] 
                [4 + hiddenNodes + 1];
        errors = new double[4 + hiddenNodes + 1]; 
        rand = new Random(seed); 
        this.learningRate = learningRate;
        this.hiddenNodes = hiddenNodes;
        this.epochs = epochs;
        this.seed = seed;

        initWeights();
    } 


     // This method returns a value between -1 and 1, this is used to set our weight limits
    private double randomInitValue() {

        return (rand.nextDouble() - 1);
    }


    // this method returns the initialized adjacency matrix
      private void initWeights() {

        int o = 4 + hiddenNodes; // output index

        for (int i = 0 ; i < 4 ; i++) { 
             // Add initial weight to connection to each hidden node
            for (int h = 4 ; h < 4 + hiddenNodes ; h++) {
                weights[i][h] = randomInitValue();
            }
        }
        for (int h = 4 ; h < 4 + hiddenNodes ; h++) { 
            // Add initial weight to connection to output node
            weights[h][o] = randomInitValue(); 
        }
    }

     // This is the activation function/ logistic function
     private double logistic (double t) { return 1/(1+Math.exp(-t)); }

     // logistic function derivative (Sigmoid)
     private double logisticPrime(double t) { return logistic(t)*(1-logistic(t)); }

    
    // This method runs the data through the neural network and displays the output
    private void execNetwork() {

        double parityCheck;
        String[] inputs = {"0000", "0001", "0010", "0011", "0100", "0101", "0110", "0111",
                "1000", "1001", "1010", "1011", "1100", "1101", "1110", "1111"}; // Sample data!

        System.out.println();
        for (String s : inputs) {
            parityCheck = forwardFeed(s);
            System.out.println(s + " - " + Math.round(parityCheck)+ " (" + parityCheck + ")");
        }

    }     


    // This method performs the memorization on the data
    private void learn (double learningRate, int epochs) {

        String[] trainingInputs = {"0000", "0001", "0010", "0011", "0100", "0101", "0110", "0111",
                "1000", "1001", "1010", "1011", "1100", "1101", "1110", "1111"}; // Training data!
        int[] trainingOutputs = {1, 0, 0, 1, 0, 1, 1, 0, 0, 1, 1, 0, 1, 0, 0, 1};
        double output;

        for (int e = 0 ; e < epochs ; e++) { 
            System.out.println("\nEpoch: " + (e+1));
            for (int t = 0 ; t < trainingInputs.length ; t++) { // For each sample from training data
                output = forwardFeed(trainingInputs[t]);
                System.out.println(trainingInputs[t] + " - " + output);
                backProp(trainingOutputs[t] - output, learningRate); // Backwards pass of the error
                
            }
        }
    }

    

    // This method performs the back prop of the error through the neural network
    private void backProp (double error, double learningRate) {

        int o = 4 + hiddenNodes; // output node index

        for (int h = 4 ; h < 4 + hiddenNodes ; h++) { 
            errors[h] = weights[h][o]*error; // Calculates the error
        }
        for (int h = 4 ; h < 4 + hiddenNodes ; h++) { // 
            for (int i = 0 ; i < 4 ; i++) { 
                // update weights on passes to hidden nodes
                weights[i][h] = weights[i][h] + learningRate*errors[h]*logisticPrime(total[h])*val[i];
               
            }
        }
        for (int h = 4 ; h < 4 + hiddenNodes ; h++) { 

            // update weights on passes to output node
            weights[h][o] = weights[h][o] + learningRate*error*logisticPrime(total[o])*val[h]; 
        }
    }

    // This method performs the calculations for feeding the data forward in the neural network
    private double forwardFeed (String x) {

        int o = 4 + hiddenNodes; // output index

        val[0] = (double)(x.charAt(0) - '0'); // "Load" input values into input nodes
        val[1] = (double)(x.charAt(1) - '0');
        val[2] = (double)(x.charAt(2) - '0');
        val[3] = (double)(x.charAt(3) - '0');

        for (int h = 4 ; h < 4 + hiddenNodes ; h++) { 
            total[h] = val[0]*weights[0][h] + val[1]*weights[1][h] + val[2]*weights[2][h] +
                    val[3]*weights[3][h];
            val[h] = logistic(total[h]); // Calculate value to be passed
        }
        total[o] = 0;
        for (int h = 4 ; h < 4 + hiddenNodes ; h++) { 
            total[o] = total[o] + val[h]*weights[h][o]; // get sum for output
        }
        return logistic(total[o]);
    }



    /** This method runs through the training data for the inputted iterations or epochs,
     * it also displays the final Mean Squared error and the final test values. */
    public void execute() {

        System.out.println("Parameters:\n");
        System.out.println("Learning Rate - " + learningRate);
        System.out.println("Hidden Nodes - " + hiddenNodes);
        System.out.println("Epochs - " + epochs);
        System.out.println("Seed - " + seed);

        System.out.println("\nTraining:");
        learn(learningRate, epochs);

        System.out.println("\nTesting:");

        System.out.println("Learning Rate- " + learningRate); 
        System.out.println("# of Hidden Nodes- " + hiddenNodes);
        System.out.println("Epochs- " + epochs);
        execNetwork();

        System.out.println("\nFinal Mean Squared Error:");

        double parityCheck;
        String[] inputs = {"0000", "0001", "0010", "0011", "0100", "0101", "0110", "0111",
                "1000", "1001", "1010", "1011", "1100", "1101", "1110", "1111"}; // Sample data!

        System.out.println();
        for (String s : inputs) {
            parityCheck = forwardFeed(s);

                // The mean squared error formula
                System.out.println(s + " - " + Math.pow(parityCheck - Math.round(parityCheck), 2) % 2);
            }
        }
            

   
}