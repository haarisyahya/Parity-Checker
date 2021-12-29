
/** This class is responsible for running the program and has its arguments passed ont the 
 * terminal of the console.  
 * @author <Haaris Yahya>
 * @version 1.0 (<12/02/2021>)    
 * COSC 3P71                                                    */


public class main {

    public static void main (String[] args) {

        double learningRate;
        int hiddenNodes;
        int epochs;
        long seed;

        try {
            if (args.length != 4) {
                throw new Exception();
            }
            learningRate = Double.parseDouble(args[0]);
            hiddenNodes = Integer.parseInt(args[1]);
            epochs = Integer.parseInt(args[2]);
            seed = Integer.parseInt(args[3]);
        } catch (Exception e) {
            printExecHelp();
            return;
        }

        parityBit pc = new parityBit(learningRate, hiddenNodes, epochs, seed);
        pc.execute();
    }

    private static void printExecHelp() {

        System.out.println("\nParityBit should be executed in the following format:\n");
        System.out.println("java -jar parityBitImp.jar learningRate hiddenNodes epochs seed\n");
        System.out.println(" OR (java -jar parityBitImp.jar learningRate hiddenNodes epochs seed >> text.txt) This will write the output to a .txt file\n");
        System.out.println("Where:\n\n\tlearningRate\tThe rate at which the network will learn. ([0.0, 1.0])");
        System.out.println("\thiddenNodes\tThe number of hidden nodes in the network. (Integer value)");
        System.out.println("\tepochs\t\tThe number of epochs which will take place during learning. (Integer value)");
        System.out.println("\tseed\t\tThe seed used in the RNG. (Long integer value)\n");

    }
}