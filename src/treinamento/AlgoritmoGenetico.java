package treinamento;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import camada.Camada;
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

public class AlgoritmoGenetico {
	
	private List<double[]> biasTreinamento = new ArrayList<double[]>();
	private List<double[][][]> pesosTreinamento = new ArrayList<double[][][]>();
	
	private List<Double> errosTreinamento = new ArrayList<Double>();
	private List<Double> errosValidacao = new ArrayList<Double>();
	
	public SolutionSet treinamentoRede( RedeNeural rede, List<double[][]> entradas, List<double[][]> saidas,
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
	    this.armazenaPesos(rede);
	    
		return population;
	}
	
	private void armazenaPesos(RedeNeural rede) {
		
		double[][][] pesosRede = new double[rede.getCamadas().length][rede.getTamCampo()][rede.getTamCampo()];
		double[] biasRede = new double[rede.getCamadas().length];
		
		Camada[] camadas = rede.getCamadas();
		for (int c = 0; c < camadas.length; c++ ) {
			Camada camada = rede.getCamadas()[c];
			
			biasRede[c] = camada.getBiasCamada();
			for (int i = 0; i < pesosRede[c].length; i++) {
				for (int j = 0; j < pesosRede[c][i].length; j++) {
					pesosRede[c][i][j] = camada.getPesosCamada()[i][j];
				}
			}
		}
		
		this.pesosTreinamento.add(pesosRede);
		this.biasTreinamento.add(biasRede);
		
	}
	
	public List<double[]> getBiasTreinamento() {
		return biasTreinamento;
	}

	public List<double[][][]> getPesosTreinamento() {
		return pesosTreinamento;
	}

	public List<Double> getErrosTreinamento() {
		return errosTreinamento;
	}
	
	public List<Double> getErrosValidacao() {
		return errosValidacao;
	}
}
