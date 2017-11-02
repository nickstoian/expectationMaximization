# expectationMaximization
Expectation maximization project for AI course

The program consists of 1 file:
ExpectationMaximization.java

The program requires 2 command line arguments.
args[0] "input.txt" for text file representing the dataset
args[1] "output.txt" to write out the iteration, log likelihood, and conditional probabilities

The starting parameters are labeled in the code, they can easily be changed to a desired value.

The estimateThetasFromData function is commented out in the code, if uncommented it will disregard any existing starting values
and replace them with values estimated from the given data. There must be some hard data present in order for this function to work.
