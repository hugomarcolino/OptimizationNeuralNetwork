package testes;

import java.util.ArrayList;
import java.util.List;

import rede.RedeNeural;
import treinamento.Resilientpropagation;
import util.Util;

public class Teste {

	public static String caminho = "C:/Users/User/OneDrive/teste/resultados/";
	
	public static void main(String[] args) throws Exception {
		
		//testeBerkeleySobel();
		testeBerkeleySobelVertical();
		//testeBerkeleySobelHorizontal();
		
		//testePH2Segmentacao();
		
	}
	
	public static void testePH2Segmentacao() throws Exception {

		ArrayList<double[][]> entradasTreinamento = new ArrayList<double[][]>();
		ArrayList<double[][]> saidasTreinamento = new ArrayList<double[][]>();
		
		ArrayList<double[][]> entradasValidacao = new ArrayList<double[][]>();
		ArrayList<double[][]> saidasValidacao = new ArrayList<double[][]>();
		
		for (int i = 1; i <= 10; i++) {
			double[][] entrada = Util.lerImagem(caminho+"ph2Reduzida/"+i+".bmp");
			double[][] saida = Util.lerImagem(caminho+"ph2Reduzida/"+i+"_gt.bmp");
			
			entrada = Util.dividirMatriz(entrada, 255);
			//saida = Util.dividirMatriz(saida, 255);			
			
			if(i <= 5) {				
				entradasTreinamento.add(entrada);
				saidasTreinamento.add(saida);
			} else {
				entradasValidacao.add(entrada);
				saidasValidacao.add(saida);
			}
		}
		
		RedeNeural rede = new RedeNeural(360, 360, 1, 3, 0);
		
		Resilientpropagation treinamento = new Resilientpropagation();
		treinamento.treinamentoRede(rede, entradasTreinamento, saidasTreinamento, entradasValidacao, saidasValidacao);
		
		Util.escreverPesos(caminho+"Resultados/ph2/segmentacao/pesos.txt", treinamento.getPesosTreinamento(), treinamento.getBiasTreinamento());
		Util.escreverErros(caminho+"Resultados/ph2/segmentacao/errosTreinamento.txt", treinamento.getErrosTreinamento());
		Util.escreverErros(caminho+"Resultados/ph2/segmentacao/errosValidacao.txt", treinamento.getErrosValidacao());
		
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
	
	public static void testeBerkeleySobel() throws Exception{

		ArrayList<double[][]> entradasTreinamento = new ArrayList<double[][]>();
		ArrayList<double[][]> saidasTreinamento = new ArrayList<double[][]>();
	
		ArrayList<double[][]> entradasValidacao = new ArrayList<double[][]>();
		ArrayList<double[][]> saidasValidacao = new ArrayList<double[][]>();
	
		
		for (int i = 1; i <= 4; i++) {
			double[][] entrada = Util.lerImagem(caminho+"base/"+i+".bmp");
			double[][] saida = Util.lerImagem(caminho+"baseSobel/"+i+"_sobel.bmp");
			
			entrada = Util.dividirMatriz(entrada, 255);
			saida = Util.dividirMatriz(saida, 255);			
			
			if(i <= 2){
				entradasTreinamento.add(entrada);
				saidasTreinamento.add(saida);
			} else {
				entradasValidacao.add(entrada);
				saidasValidacao.add(saida);
			}
		}
		
		RedeNeural rede = new RedeNeural(315, 477, 1, 3, 0);
		
		Resilientpropagation treinamento = new Resilientpropagation();
		treinamento.treinamentoRede(rede, entradasTreinamento, saidasTreinamento, entradasValidacao, saidasValidacao);
		
		Util.escreverPesos(caminho+"Resultados/berkeley/sobel/pesos.txt", treinamento.getPesosTreinamento(), treinamento.getBiasTreinamento());
		Util.escreverErros(caminho+"Resultados/berkeley/sobel/errosTreinamento.txt", treinamento.getErrosTreinamento());
		Util.escreverErros(caminho+"Resultados/berkeley/sobel/errosValidacao.txt", treinamento.getErrosValidacao());

		
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

		ArrayList<double[][]> entradasTreinamento = new ArrayList<double[][]>();
		ArrayList<double[][]> saidasTreinamento = new ArrayList<double[][]>();
	
		ArrayList<double[][]> entradasValidacao = new ArrayList<double[][]>();
		ArrayList<double[][]> saidasValidacao = new ArrayList<double[][]>();
	
		
		for (int i = 1; i <= 4; i++) {
			double[][] entrada = Util.lerImagem(caminho+"base/"+i+".bmp");
			double[][] saida = Util.lerImagem(caminho+"baseSobel/"+i+"_sobelVertical.bmp");
			
			entrada = Util.dividirMatriz(entrada, 255);
			saida = Util.dividirMatriz(saida, 255);			
			
			if(i <= 2){
				entradasTreinamento.add(entrada);
				saidasTreinamento.add(saida);
			} else {
				entradasValidacao.add(entrada);
				saidasValidacao.add(saida);
			}
		}
		
		RedeNeural rede = new RedeNeural(315, 477, 1, 3, 0);
		
		Resilientpropagation treinamento = new Resilientpropagation();
		treinamento.treinamentoRede(rede, entradasTreinamento, saidasTreinamento, entradasValidacao, saidasValidacao);
		
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

		ArrayList<double[][]> entradasTreinamento = new ArrayList<double[][]>();
		ArrayList<double[][]> saidasTreinamento = new ArrayList<double[][]>();
	
		ArrayList<double[][]> entradasValidacao = new ArrayList<double[][]>();
		ArrayList<double[][]> saidasValidacao = new ArrayList<double[][]>();
	
		
		for (int i = 1; i <= 4; i++) {
			double[][] entrada = Util.lerImagem(caminho+"base/"+i+".bmp");
			double[][] saida = Util.lerImagem(caminho+"baseSobel/"+i+"_sobelHorizontal.bmp");
			
			entrada = Util.dividirMatriz(entrada, 255);
			saida = Util.dividirMatriz(saida, 255);			
			
			if(i <= 2){
				entradasTreinamento.add(entrada);
				saidasTreinamento.add(saida);
			} else {
				entradasValidacao.add(entrada);
				saidasValidacao.add(saida);
			}
		}
		
		RedeNeural rede = new RedeNeural(315, 477, 1, 3, 0);
		
		Resilientpropagation treinamento = new Resilientpropagation();
		treinamento.treinamentoRede(rede, entradasTreinamento, saidasTreinamento, entradasValidacao, saidasValidacao);
		
		Util.escreverPesos(caminho+"Resultados/berkeley/sobelHorizontal/pesos.txt", treinamento.getPesosTreinamento(), treinamento.getBiasTreinamento());
		Util.escreverErros(caminho+"Resultados/berkeley/sobelHorizontal/errosTreinamento.txt", treinamento.getErrosTreinamento());
		Util.escreverErros(caminho+"Resultados/berkeley/sobelHorizontal/errosValidacao.txt", treinamento.getErrosValidacao());

		
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
