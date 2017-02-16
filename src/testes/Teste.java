package testes;

import java.util.ArrayList;
import java.util.List;

import rede.RedeNeural;
import treinamento.Resilientpropagation;
import util.Util;

public class Teste {

	public static String caminho = "C:/Users/Hugo/OneDrive/teste/resultados/";
	
	public static void main(String[] args) throws Exception {
		
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
		
		Resilientpropagation treinamento = new Resilientpropagation();
		treinamento.treinamentoRede(rede, entradas, saidas, 1000);
		
		Util.escreverPesos(caminho+"Resultado_8/ph2/segmentacao/pesos.txt", treinamento.getPesosTreinamento(), treinamento.getBiasTreinamento());
		Util.escreverErros(caminho+"Resultado_8/ph2/segmentacao/erros.txt", treinamento.getErrosTreinamento());
		
		for (int i = 1; i <= 200; i++) {
			double[][] imagemEntrada = Util.lerImagem(caminho+"ph2Reduzida/"+i+".bmp");
			
			imagemEntrada = Util.dividirMatriz(imagemEntrada, 255);
			
			List<double[][]> saidasRede = rede.estimularListaSaidas(imagemEntrada);
			for (int s = 0; s < saidasRede.size(); s++) {
				double[][] saidaCamada =  Util.multiplicarMatriz(saidasRede.get(s), 255);
				Util.salvaImagem(caminho+"Resultado_8/ph2/segmentacao/"+i+"_"+s+".bmp", saidaCamada);
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
		
		Resilientpropagation treinamento = new Resilientpropagation();
		treinamento.treinamentoRede(rede, entradas, saidas, 100);
		
		Util.escreverPesos(caminho+"Resultado_8/berkeley/sobel/pesos.txt", treinamento.getPesosTreinamento(), treinamento.getBiasTreinamento());
		Util.escreverErros(caminho+"Resultado_8/berkeley/sobel/erros.txt", treinamento.getErrosTreinamento());
		
		for (int i = 1; i <= 20; i++) {
			double[][] imagemEntrada = Util.lerImagem(caminho+"base/"+i+".bmp");
			
			imagemEntrada = Util.dividirMatriz(imagemEntrada, 255);
			
			List<double[][]> saidasRede = rede.estimularListaSaidas(imagemEntrada);
			for (int s = 0; s < saidasRede.size(); s++) {
				double[][] saidaCamada =  Util.multiplicarMatriz(saidasRede.get(s), 255);
				Util.salvaImagem(caminho+"Resultado_8/berkeley/sobel/"+i+"_"+s+".bmp", saidaCamada);
			}
		}
	}

	public static void testeBerkeleySobelVertical() throws Exception{

		ArrayList<double[][]> entradas = new ArrayList<double[][]>();
		ArrayList<double[][]> saidas = new ArrayList<double[][]>();
		
		ArrayList<double[][]> entradasValidacao = new ArrayList<double[][]>();
		ArrayList<double[][]> saidasValidacao = new ArrayList<double[][]>();
		
		
		for (int i = 1; i <= 10; i++) {
			double[][] entrada = Util.lerImagem(caminho+"base/"+i+".bmp");
			double[][] saida = Util.lerImagem(caminho+"baseSobel/"+i+"_sobelVertical.bmp");
			
			if (i <= 5) {
				entradas.add(Util.dividirMatriz(entrada, 255));
				saidas.add(Util.dividirMatriz(saida, 255));
			} else {
				entradasValidacao.add(Util.dividirMatriz(entrada, 255));
				saidasValidacao.add(Util.dividirMatriz(saida, 255));
			}
			
		}
		
		RedeNeural rede = new RedeNeural(315, 477, 1, 3, 0);
		
		Resilientpropagation treinamento = new Resilientpropagation();
		//treinamento.treinamentoRede(rede, entradas, saidas, 100);
		treinamento.treinamentoRede(rede, entradas, saidas, entradasValidacao, saidasValidacao);
		
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
		
		Resilientpropagation treinamento = new Resilientpropagation();
		treinamento.treinamentoRede(rede, entradas, saidas, 100);
		
		Util.escreverPesos(caminho+"Resultados/berkeley/sobelHorizontal/pesos.txt", treinamento.getPesosTreinamento(), treinamento.getBiasTreinamento());
		Util.escreverErros(caminho+"Resultados/berkeley/sobelHorizontal/erros.txt", treinamento.getErrosTreinamento());
		
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
