package testes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jmetal.core.Operator;
import jmetal.operators.crossover.CrossoverFactory;
import jmetal.operators.mutation.MutationFactory;
import jmetal.operators.selection.SelectionFactory;
import rede.RedeNeural;
import treinamento.AlgoritmoGenetico;
import util.Util;

public class TesteAG {

	public static String caminho = "C:/Users/Hugo/OneDrive/teste/resultados/";

	public static final double TAXA_CRUZAMENTO  = 1; 
	public static final double TAXA_MUTACAO     = 0.1; 
	
	public static final int    TAM_POPULACAO    = 100;
	
	public static Operator crossover;
	public static Operator mutation;
	public static Operator selection;
	
	public static void main(String[] args) throws Exception {
		
		HashMap parameters = new HashMap();
	    parameters.put("probability", TAXA_CRUZAMENTO);
	    parameters.put("distributionIndex", 20.0);
	    crossover = CrossoverFactory.getCrossoverOperator("SBXCrossover", parameters);                   

	    parameters = new HashMap();
	    parameters.put("probability", TAXA_MUTACAO);
	    parameters.put("distributionIndex", 20.0);
	    mutation = MutationFactory.getMutationOperator("PolynomialMutation", parameters);                    

	    parameters = new HashMap();
	    selection = SelectionFactory.getSelectionOperator("BinaryTournament", parameters);
		
		//testeBerkeleySobel();
		testeBerkeleySobelVertical();
		//testeBerkeleySobelHorizontal();
		
		//testePH2Segmentacao();
		
	}
	
	public static void testePH2Segmentacao() throws Exception {

		ArrayList<double[][]> entradas = new ArrayList<double[][]>();
		ArrayList<double[][]> saidas = new ArrayList<double[][]>();
		
		for (int i = 1; i <= 5; i++) {
			double[][] entrada = Util.lerImagem(caminho+"ph2Reduzida/"+i+".bmp");
			double[][] saida = Util.lerImagem(caminho+"ph2Reduzida/"+i+"_gt.bmp");
			
			entrada = Util.dividirMatriz(entrada, 255);
			//saida = Util.dividirMatriz(saida, 255);			
			
			entradas.add(entrada);
			saidas.add(saida);
		}
		
		RedeNeural rede = new RedeNeural(360, 360, 1, 3, 0);
		
		AlgoritmoGenetico treinamento = new AlgoritmoGenetico();
		treinamento.treinamentoRede(rede, entradas, saidas, entradas, saidas,
									crossover, mutation, selection, TAM_POPULACAO);
		
		for (int i = 1; i <= 200; i++) {
			double[][] imagemEntrada = Util.lerImagem(caminho+"ph2Reduzida/"+i+".bmp");
			
			imagemEntrada = Util.dividirMatriz(imagemEntrada, 255);
			
			List<double[][]> saidasRede = rede.estimularListaSaidas(imagemEntrada);
			for (int s = 0; s < saidasRede.size(); s++) {
				double[][] saidaCamada =  Util.multiplicarMatriz(saidasRede.get(s), 255);
				Util.salvaImagem(caminho+"Resultados/ph2/segmentacao/"+i+"_"+s+".bmp", saidaCamada);
			}
		}
	}

	public static void testeBerkeleySobel() throws Exception {

		ArrayList<double[][]> entradas = new ArrayList<double[][]>();
		ArrayList<double[][]> saidas = new ArrayList<double[][]>();
		
		for (int i = 1; i <= 10; i++) {
			double[][] entrada = Util.lerImagem(caminho+"base/"+i+".bmp");
			double[][] saida = Util.lerImagem(caminho+"baseSobel/"+i+"_sobel.bmp");
			
			entrada = Util.dividirMatriz(entrada, 255);
			saida = Util.dividirMatriz(saida, 255);			
			
			entradas.add(entrada);
			saidas.add(saida);
		}
		
		RedeNeural rede = new RedeNeural(315, 477, 1, 3, 0);
		
		AlgoritmoGenetico treinamento = new AlgoritmoGenetico();
		treinamento.treinamentoRede(rede, entradas, saidas, entradas, saidas,
									crossover, mutation, selection, TAM_POPULACAO);
		
		for (int i = 1; i <= 20; i++) {
			double[][] imagemEntrada = Util.lerImagem(caminho+"base/"+i+".bmp");
			
			imagemEntrada = Util.dividirMatriz(imagemEntrada, 255);
			
			List<double[][]> saidasRede = rede.estimularListaSaidas(imagemEntrada);
			for (int s = 0; s < saidasRede.size(); s++) {
				double[][] saidaCamada =  Util.multiplicarMatriz(saidasRede.get(s), 255);
				Util.salvaImagem(caminho+"Resultados/berkeley/sobel/"+i+"_"+s+".bmp", saidaCamada);
			}
		}
	}

	public static void testeBerkeleySobelVertical() throws Exception{

		ArrayList<double[][]> entradas = new ArrayList<double[][]>();
		ArrayList<double[][]> saidas = new ArrayList<double[][]>();
		
		for (int i = 1; i <= 1; i++) {
			double[][] entrada = Util.lerImagem(caminho+"base/"+i+".bmp");
			double[][] saida = Util.lerImagem(caminho+"baseSobel/"+i+"_sobelVertical.bmp");
			
			entrada = Util.dividirMatriz(entrada, 255);
			saida = Util.dividirMatriz(saida, 255);			
			
			entradas.add(entrada);
			saidas.add(saida);
		}
		
		RedeNeural rede = new RedeNeural(315, 477, 1, 3, 0);
		
		AlgoritmoGenetico treinamento = new AlgoritmoGenetico();
		treinamento.treinamentoRede(rede, entradas, saidas, entradas, saidas,
									crossover, mutation, selection, TAM_POPULACAO);
		
		Util.escreverPesos(caminho+"Resultados/berkeley/sobelVertical/pesos.txt", treinamento.getPesosTreinamento(), treinamento.getBiasTreinamento());
		Util.escreverErros(caminho+"Resultados/berkeley/sobelVertical/errosTreinamento.txt", treinamento.getErrosTreinamento());
		Util.escreverErros(caminho+"Resultados/berkeley/sobelVertical/errosValidacao.txt", treinamento.getErrosValidacao());
		
		for (int i = 1; i <= 20; i++) {
			double[][] imagemEntrada = Util.lerImagem(caminho+"base/"+i+".bmp");
			
			imagemEntrada = Util.dividirMatriz(imagemEntrada, 255);
			
			List<double[][]> saidasRede = rede.estimularListaSaidas(imagemEntrada);
			for (int s = 0; s < saidasRede.size(); s++) {
				double[][] saidaCamada =  Util.multiplicarMatriz(saidasRede.get(s), 255);
				Util.salvaImagem(caminho+"Resultados/berkeley/sobelVertical/"+i+"_"+s+".bmp", saidaCamada);
			}
		}
	}	
	
	public static void testeBerkeleySobelHorizontal() throws Exception{

		ArrayList<double[][]> entradas = new ArrayList<double[][]>();
		ArrayList<double[][]> saidas = new ArrayList<double[][]>();
		
		for (int i = 1; i <= 5; i++) {
			double[][] entrada = Util.lerImagem(caminho+"base/"+i+".bmp");
			double[][] saida = Util.lerImagem(caminho+"baseSobel/"+i+"_sobelHorizontal.bmp");
			
			entrada = Util.dividirMatriz(entrada, 255);
			saida = Util.dividirMatriz(saida, 255);			
			
			entradas.add(entrada);
			saidas.add(saida);
		}
		
		RedeNeural rede = new RedeNeural(315, 477, 1, 3, 0);
		
		AlgoritmoGenetico treinamento = new AlgoritmoGenetico();
		treinamento.treinamentoRede(rede, entradas, saidas, entradas, saidas,
									crossover, mutation, selection, TAM_POPULACAO);
	
		Util.escreverPesos(caminho+"Resultados/berkeley/sobelHorizontal/pesos.txt", treinamento.getPesosTreinamento(), treinamento.getBiasTreinamento());
		
		for (int i = 1; i <= 20; i++) {
			double[][] imagemEntrada = Util.lerImagem(caminho+"base/"+i+".bmp");
			
			imagemEntrada = Util.dividirMatriz(imagemEntrada, 255);
			
			List<double[][]> saidasRede = rede.estimularListaSaidas(imagemEntrada);
			for (int s = 0; s < saidasRede.size(); s++) {
				double[][] saidaCamada =  Util.multiplicarMatriz(saidasRede.get(s), 255);
				Util.salvaImagem(caminho+"Resultados/berkeley/sobelHorizontal/"+i+"_"+s+".bmp", saidaCamada);
			}
		}
	}
	
}
