package treinamento;

import java.util.HashMap;
import java.util.List;

import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.metaheuristics.singleObjective.geneticAlgorithm.gGA;
import jmetal.operators.crossover.CrossoverFactory;
import jmetal.operators.mutation.MutationFactory;
import jmetal.operators.selection.SelectionFactory;
import jmetal.problems.singleObjective.RedeNeuralProblem;
import jmetal.util.JMException;
import rede.RedeNeural;

public class OptimizationNeural {
	
	
	public SolutionSet treinamentoRede(RedeNeural rede, List<double[][]> entradas, List<double[][]> saidas,
			double taxaCruzamento, double taxaMutacao, int nPopulacao, int nCiclos ) throws ClassNotFoundException, JMException {
		
	    //Problem problem;       
	    Algorithm algorithm;       
	    Operator crossover;       
	    Operator mutation;       
	    Operator selection;       
	    
	    int tam = (int) ((Math.pow(rede.getTamCampo(), 2) + 1)*rede.getCamadas().length);
	    
	    RedeNeuralProblem neuralProblem = new RedeNeuralProblem("Real", tam);
	    
	    neuralProblem.setEntradas(entradas);
	    neuralProblem.setSaidas(saidas);
	    neuralProblem.setRede(rede);
	    
	    algorithm = new gGA(neuralProblem);
	    algorithm.setInputParameter("populationSize", nPopulacao);
	    algorithm.setInputParameter("maxEvaluations", nCiclos*nPopulacao);

	    HashMap parameters = new HashMap();
	    parameters.put("probability", taxaCruzamento);
	    parameters.put("distributionIndex", 20.0);
	    crossover = CrossoverFactory.getCrossoverOperator("SBXCrossover", parameters);                   

	    parameters = new HashMap();
	    parameters.put("probability", taxaMutacao);
	    parameters.put("distributionIndex", 20.0);
	    mutation = MutationFactory.getMutationOperator("PolynomialMutation", parameters);                    

	    parameters = new HashMap();
	    selection = SelectionFactory.getSelectionOperator("BinaryTournament", parameters);                            
	    
	    algorithm.addOperator("crossover", crossover);
	    algorithm.addOperator("mutation", mutation);
	    algorithm.addOperator("selection", selection);
	 
	    SolutionSet population = algorithm.execute();
		
	    List<Solution> solutionsList_ = population.getSolutionsList_();
	    int numberOfVariables = solutionsList_.get(0).getDecisionVariables().length;
	    double[] position = new double[tam];
	    for (int j = 0; j < numberOfVariables; j++) {
	    	position[j] = solutionsList_.get(0).getDecisionVariables()[j].getValue();
        }
	    
	    rede.setPesos(position);
	    
		return population;
	}

}
