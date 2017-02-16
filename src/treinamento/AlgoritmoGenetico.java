package treinamento;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import camada.Camada;
import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.SolutionSet;
import jmetal.core.Variable;
import jmetal.metaheuristics.singleObjective.geneticAlgorithm.gGA;
import jmetal.problems.singleObjective.RedeNeuralProblem;
import jmetal.util.JMException;
import rede.RedeNeural;
import smile.math.DoubleArrayList;
import smile.math.distance.ManhattanDistance;

public class AlgoritmoGenetico {
	
	private List<double[]> biasTreinamento = new ArrayList<double[]>();
	private List<double[][][]> pesosTreinamento = new ArrayList<double[][][]>();
	
	private List<Double> errosTreinamento = new ArrayList<Double>();
	private List<Double> errosValidacao = new ArrayList<Double>();
	
	public RedeNeural treinamentoRede( RedeNeural rede, List<double[][]> entradasTreino, List<double[][]> saidasTreino,
										List<double[][]> entradasValidacao, List<double[][]> saidasValidacao,
										Operator crossover, Operator mutation, Operator selection, int nPopulacao ) throws ClassNotFoundException, JMException {

	    
	    int tam = (int) ((Math.pow(rede.getTamCampo(), 2) + 1)*rede.getCamadas().length);
	    
	    RedeNeuralProblem neuralProblem = new RedeNeuralProblem("Real", tam);
	    
	    neuralProblem.setEntradas(entradasTreino);
	    neuralProblem.setSaidas(saidasTreino);
	    neuralProblem.setRede(rede);
	    
	    Algorithm algorithm = new gGA(neuralProblem);
	    algorithm.setInputParameter("populationSize", nPopulacao);
	    algorithm.addOperator("crossover", crossover);
	    algorithm.addOperator("mutation", mutation);
	    algorithm.addOperator("selection", selection);

	    List<Integer> ordemTreino = new ArrayList<Integer>();
		for (int i = 0; i < entradasTreino.size(); i++) {
			ordemTreino.add(i);
		}
		
		int ciclo = 0;
		//SolutionSet population = algorithm.initPopulation();
		
		while ( this.validacaoCruzada(10, 1e-6) ) {
			System.out.println("Ciclo: "+ciclo++);
			Collections.shuffle(ordemTreino);
			
			for (int j = 0; j < ordemTreino.size(); j++) {
				int nImagem = ordemTreino.get(j);
				System.out.println("Treinando.. Imagem "+(nImagem+1));
				
			    //population = algorithm.iteration(population);
			}
			
			//rede = this.setPositionRede(rede, population, tam);
			
			this.armazenaPesos(rede);
			this.armazenaErrosTreinamento(rede, entradasTreino, saidasTreino);
			this.armazenaErrosValidacao(rede, entradasValidacao, saidasValidacao);
		}
	    
		return rede;
	}
	
	private RedeNeural setPositionRede(RedeNeural rede, SolutionSet population, int tam) throws JMException{
		
		
		Variable[] bestSolution = population.get(0).getDecisionVariables();
		double[] position = new double[bestSolution.length];
		for (int i = 0; i < bestSolution.length; i++) {
			position[i] = bestSolution[i].getValue();
		}
		
		rede.setPesos(position);
		
		return rede;
	}
	
	private double calcularErro(RedeNeural rede, List<double[][]> entradas, List<double[][]> saidas) {

		double erro = 0;

		for (int i = 0; i < entradas.size(); i++) {

			double[][] entrada = entradas.get(i);
			double[][] saida = saidas.get(i);

			double[][] saidaRede = rede.estimular(entrada);

			DoubleArrayList saidaArray = new DoubleArrayList();
			DoubleArrayList saidaRedeArray = new DoubleArrayList();

			for (int linha = 0; linha < saida.length; linha++) {
				saidaArray.add(saida[linha]);
				saidaRedeArray.add(saidaRede[linha]);
			}
			
			ManhattanDistance md = new ManhattanDistance();
			erro += md.d(saidaArray.toArray(), saidaRedeArray.toArray()) / saidaArray.toArray().length;

		}
		
		erro /= entradas.size(); 
		
		return erro;
	}

	
	private void armazenaErrosTreinamento(RedeNeural rede, List<double[][]> entradas, List<double[][]> saidas){
		
		double erro = this.calcularErro(rede, entradas, saidas);
		this.errosTreinamento.add(erro);
	}
	
	private void armazenaErrosValidacao(RedeNeural rede, List<double[][]> entradas, List<double[][]> saidas){
		
		double erro = this.calcularErro(rede, entradas, saidas);
		this.errosValidacao.add(erro);
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
	
	private boolean validacaoCruzada(int ciclos, double precisao){
		boolean treina = true;

		if (this.errosValidacao.size() >= ciclos) {
			double erro = this.errosValidacao.get((errosValidacao.size()-ciclos)) - this.errosValidacao.get((errosValidacao.size()-1));
			if(erro < precisao){
				treina = false;
			}
		}
		
		return treina;
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
