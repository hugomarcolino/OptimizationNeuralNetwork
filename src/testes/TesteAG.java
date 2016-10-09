package testes;

import java.util.ArrayList;
import java.util.List;

import rede.RedeNeural;
import treinamento.OptimizationNeural;
import util.Util;

public class TesteAG {

	public static String caminho = "C:/Users/User/OneDrive/teste/resultados/";

	public static final double TAXA_CRUZAMENTO  = 1; 
	public static final double TAXA_MUTACAO     = 0.1; 
	
	public static final int    	TAM_POPULACAO   = 100; 
	public static final int    	MAX_GERACOES  	= 100;
	
	
	public static void main(String[] args) throws Exception {
		
		//testeBerkeleyHistograma();
		//testeBerkeleyLaplaciano();
		//testeBerkeleyMedia();
		
		//testeBerkeleySobel();
		//testeBerkeleySobelVertical();
		//testeBerkeleySobelHorizontal();
		
		testePH2Segmentacao();
		//testeDriveSegmentacao();
		//testeBerkeleySegmentacao();
		
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
		
		OptimizationNeural treinamento = new OptimizationNeural();
		treinamento.treinamentoRede(rede, entradas, saidas,
				TAXA_CRUZAMENTO, TAXA_MUTACAO, TAM_POPULACAO, MAX_GERACOES);
		
		Util.escreverPesos(caminho+"Resultado_9/ph2/segmentacao/pesos.txt", treinamento.getPesosTreinamento(), treinamento.getBiasTreinamento());
		Util.escreverErros(caminho+"Resultado_9/ph2/segmentacao/erros.txt", treinamento.getErros());
		
		for (int i = 1; i <= 200; i++) {
			double[][] imagemEntrada = Util.lerImagem(caminho+"ph2Reduzida/"+i+".bmp");
			
			imagemEntrada = Util.dividirMatriz(imagemEntrada, 255);
			
			List<double[][]> saidasRede = rede.estimularListaSaidas(imagemEntrada);
			for (int s = 0; s < saidasRede.size(); s++) {
				double[][] saidaCamada =  Util.multiplicarMatriz(saidasRede.get(s), 255);
				Util.salvaImagem(caminho+"Resultado_9/ph2/segmentacao/"+i+"_"+s+".bmp", saidaCamada);
			}
		}
	}

	public static void testeDriveSegmentacao() throws Exception {
		
		ArrayList<double[][]> entradas = new ArrayList<double[][]>();
		ArrayList<double[][]> saidas = new ArrayList<double[][]>();
		
		for (int i = 21; i <= 25; i++) {
			double[][] entrada = Util.lerImagem(caminho+"drive/"+i+".bmp");
			double[][] saida = Util.lerImagem(caminho+"drive/"+i+"_gt.bmp");
			
			entrada = Util.dividirMatriz(entrada, 255);
			saida = Util.dividirMatriz(saida, 255);			
			
			entradas.add(entrada);
			saidas.add(saida);
		}
		
		RedeNeural rede = new RedeNeural(360, 360, 1, 3, 0);
		
		OptimizationNeural treinamento = new OptimizationNeural();
		treinamento.treinamentoRede(rede, entradas, saidas, 
				TAXA_CRUZAMENTO, TAXA_MUTACAO, TAM_POPULACAO, MAX_GERACOES);
		
		Util.escreverPesos(caminho+"Resultado_9/drive/segmentacao/pesos.txt", treinamento.getPesosTreinamento(), treinamento.getBiasTreinamento());
		Util.escreverErros(caminho+"Resultado_9/drive/segmentacao/erros.txt", treinamento.getErros());
		
		for (int i = 21; i <= 40; i++) {
			double[][] imagemEntrada = Util.lerImagem(caminho+"drive/"+i+".bmp");
			
			imagemEntrada = Util.dividirMatriz(imagemEntrada, 255);
			
			List<double[][]> saidasRede = rede.estimularListaSaidas(imagemEntrada);
			for (int s = 0; s < saidasRede.size(); s++) {
				double[][] saidaCamada =  Util.multiplicarMatriz(saidasRede.get(s), 255);
				Util.salvaImagem(caminho+"Resultado_9/drive/segmentacao/"+i+"_"+s+".bmp", saidaCamada);
			}
		}
	}

	public static void testeBerkeleySegmentacao() throws Exception {

		ArrayList<double[][]> entradas = new ArrayList<double[][]>();
		ArrayList<double[][]> saidas = new ArrayList<double[][]>();
		
		for (int i = 1; i <= 10; i++) {
			double[][] entrada = Util.lerImagem(caminho+"base/"+i+".bmp");
			double[][] saida = Util.lerImagem(caminho+"base/"+i+"_gt.bmp");
			
			entrada = Util.dividirMatriz(entrada, 255);
			saida = Util.dividirMatriz(saida, 255);			
			
			entradas.add(entrada);
			saidas.add(saida);
		}
		
		RedeNeural rede = new RedeNeural(315, 477, 1, 3, 0);
		
		OptimizationNeural treinamento = new OptimizationNeural();
		treinamento.treinamentoRede(rede, entradas, saidas, 
				TAXA_CRUZAMENTO, TAXA_MUTACAO, TAM_POPULACAO, MAX_GERACOES);
		
		Util.escreverPesos(caminho+"Resultado_9/berkeley/segmentacao/pesos.txt", treinamento.getPesosTreinamento(), treinamento.getBiasTreinamento());
		Util.escreverErros(caminho+"Resultado_9/berkeley/segmentacao/erros.txt", treinamento.getErros());
		
		for (int i = 1; i <= 20; i++) {
			double[][] imagemEntrada = Util.lerImagem(caminho+"base/"+i+".bmp");
			
			imagemEntrada = Util.dividirMatriz(imagemEntrada, 255);
			
			List<double[][]> saidasRede = rede.estimularListaSaidas(imagemEntrada);
			for (int s = 0; s < saidasRede.size(); s++) {
				double[][] saidaCamada =  Util.multiplicarMatriz(saidasRede.get(s), 255);
				Util.salvaImagem(caminho+"Resultado_9/berkeley/segmentacao/"+i+"_"+s+".bmp", saidaCamada);
			}
		}
	}

	public static void testeBerkeleyHistograma() throws Exception {

		ArrayList<double[][]> entradas = new ArrayList<double[][]>();
		ArrayList<double[][]> saidas = new ArrayList<double[][]>();
		
		for (int i = 1; i <= 10; i++) {
			double[][] entrada = Util.lerImagem(caminho+"base/"+i+".bmp");
			double[][] saida = Util.lerImagem(caminho+"baseHistograma/"+i+"_histograma.bmp");
			
			entrada = Util.dividirMatriz(entrada, 255);
			saida = Util.dividirMatriz(saida, 255);			
			
			entradas.add(entrada);
			saidas.add(saida);
		}
		
		RedeNeural rede = new RedeNeural(315, 477, 1, 3, 0);
		
		OptimizationNeural treinamento = new OptimizationNeural();
		treinamento.treinamentoRede(rede, entradas, saidas,
				TAXA_CRUZAMENTO, TAXA_MUTACAO, TAM_POPULACAO, MAX_GERACOES);
		
		Util.escreverPesos(caminho+"Resultado_9/berkeley/histograma/pesos.txt", treinamento.getPesosTreinamento(), treinamento.getBiasTreinamento());
		Util.escreverErros(caminho+"Resultado_9/berkeley/histograma/erros.txt", treinamento.getErros());
		
		for (int i = 1; i <= 20; i++) {
			double[][] imagemEntrada = Util.lerImagem(caminho+"base/"+i+".bmp");
			
			imagemEntrada = Util.dividirMatriz(imagemEntrada, 255);
			
			List<double[][]> saidasRede = rede.estimularListaSaidas(imagemEntrada);
			for (int s = 0; s < saidasRede.size(); s++) {
				double[][] saidaCamada =  Util.multiplicarMatriz(saidasRede.get(s), 255);
				Util.salvaImagem(caminho+"Resultado_9/berkeley/histograma/"+i+"_"+s+".bmp", saidaCamada);
			}
		}
	}

	public static void testeBerkeleyLaplaciano() throws Exception {

		ArrayList<double[][]> entradas = new ArrayList<double[][]>();
		ArrayList<double[][]> saidas = new ArrayList<double[][]>();
		
		for (int i = 1; i <= 10; i++) {
			double[][] entrada = Util.lerImagem(caminho+"base/"+i+".bmp");
			double[][] saida = Util.lerImagem(caminho+"baseLaplaciano/"+i+"_laplaciano.bmp");
			
			entrada = Util.dividirMatriz(entrada, 255);
			saida = Util.dividirMatriz(saida, 255);			
			
			entradas.add(entrada);
			saidas.add(saida);
		}
		
		RedeNeural rede = new RedeNeural(315, 477, 1, 3, 0);
		
		OptimizationNeural treinamento = new OptimizationNeural();
		treinamento.treinamentoRede(rede, entradas, saidas,
				TAXA_CRUZAMENTO, TAXA_MUTACAO, TAM_POPULACAO, MAX_GERACOES);
		
		Util.escreverPesos(caminho+"Resultado_9/berkeley/laplaciano/pesos.txt", treinamento.getPesosTreinamento(), treinamento.getBiasTreinamento());
		Util.escreverErros(caminho+"Resultado_9/berkeley/laplaciano/erros.txt", treinamento.getErros());
		
		for (int i = 1; i <= 20; i++) {
			double[][] imagemEntrada = Util.lerImagem(caminho+"base/"+i+".bmp");
			
			imagemEntrada = Util.dividirMatriz(imagemEntrada, 255);
			
			List<double[][]> saidasRede = rede.estimularListaSaidas(imagemEntrada);
			for (int s = 0; s < saidasRede.size(); s++) {
				double[][] saidaCamada =  Util.multiplicarMatriz(saidasRede.get(s), 255);
				Util.salvaImagem(caminho+"Resultado_9/berkeley/laplaciano/"+i+"_"+s+".bmp", saidaCamada);
			}
		}
	}

	public static void testeBerkeleyMedia() throws Exception {

		ArrayList<double[][]> entradas = new ArrayList<double[][]>();
		ArrayList<double[][]> saidas = new ArrayList<double[][]>();
		
		for (int i = 1; i <= 10; i++) {
			double[][] entrada = Util.lerImagem(caminho+"base/"+i+".bmp");
			double[][] saida = Util.lerImagem(caminho+"baseMedia/"+i+"_media.bmp");
			
			entrada = Util.dividirMatriz(entrada, 255);
			saida = Util.dividirMatriz(saida, 255);			
			
			entradas.add(entrada);
			saidas.add(saida);
		}
		
		RedeNeural rede = new RedeNeural(315, 477, 1, 3, 0);
		
		OptimizationNeural treinamento = new OptimizationNeural();
		treinamento.treinamentoRede(rede, entradas, saidas,
				TAXA_CRUZAMENTO, TAXA_MUTACAO, TAM_POPULACAO, MAX_GERACOES);
		
		Util.escreverPesos(caminho+"Resultado_9/berkeley/Media/pesos.txt", treinamento.getPesosTreinamento(), treinamento.getBiasTreinamento());
		Util.escreverErros(caminho+"Resultado_9/berkeley/Media/erros.txt", treinamento.getErros());
		
		for (int i = 1; i <= 20; i++) {
			double[][] imagemEntrada = Util.lerImagem(caminho+"base/"+i+".bmp");
			
			imagemEntrada = Util.dividirMatriz(imagemEntrada, 255);
			
			List<double[][]> saidasRede = rede.estimularListaSaidas(imagemEntrada);
			for (int s = 0; s < saidasRede.size(); s++) {
				double[][] saidaCamada =  Util.multiplicarMatriz(saidasRede.get(s), 255);
				Util.salvaImagem(caminho+"Resultado_9/berkeley/Media/"+i+"_"+s+".bmp", saidaCamada);
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
		
		OptimizationNeural treinamento = new OptimizationNeural();
		treinamento.treinamentoRede(rede, entradas, saidas,
				TAXA_CRUZAMENTO, TAXA_MUTACAO, TAM_POPULACAO, MAX_GERACOES);
		
		Util.escreverPesos(caminho+"Resultado_9/berkeley/sobel/pesos.txt", treinamento.getPesosTreinamento(), treinamento.getBiasTreinamento());
		Util.escreverErros(caminho+"Resultado_9/berkeley/sobel/erros.txt", treinamento.getErros());
		
		for (int i = 1; i <= 20; i++) {
			double[][] imagemEntrada = Util.lerImagem(caminho+"base/"+i+".bmp");
			
			imagemEntrada = Util.dividirMatriz(imagemEntrada, 255);
			
			List<double[][]> saidasRede = rede.estimularListaSaidas(imagemEntrada);
			for (int s = 0; s < saidasRede.size(); s++) {
				double[][] saidaCamada =  Util.multiplicarMatriz(saidasRede.get(s), 255);
				Util.salvaImagem(caminho+"Resultado_9/berkeley/sobel/"+i+"_"+s+".bmp", saidaCamada);
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
		
//		OptimizationNeural treinamento = new OptimizationNeural();
//		treinamento.treinamentoRede(rede, entradas, saidas, 
		
		OptimizationNeural treinamento = new OptimizationNeural();
		treinamento.treinamentoRede(rede, entradas, saidas,
				TAXA_CRUZAMENTO, TAXA_MUTACAO, TAM_POPULACAO, MAX_GERACOES);
		
		Util.escreverPesos(caminho+"Resultado_9/berkeley/sobelVertical/pesos.txt", treinamento.getPesosTreinamento(), treinamento.getBiasTreinamento());
		Util.escreverErros(caminho+"Resultado_9/berkeley/sobelVertical/erros.txt", treinamento.getErros());
		
		for (int i = 1; i <= 20; i++) {
			double[][] imagemEntrada = Util.lerImagem(caminho+"base/"+i+".bmp");
			
			imagemEntrada = Util.dividirMatriz(imagemEntrada, 255);
			
			List<double[][]> saidasRede = rede.estimularListaSaidas(imagemEntrada);
			for (int s = 0; s < saidasRede.size(); s++) {
				double[][] saidaCamada =  Util.multiplicarMatriz(saidasRede.get(s), 255);
				Util.salvaImagem(caminho+"Resultado_9/berkeley/sobelVertical/"+i+"_"+s+".bmp", saidaCamada);
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
		
		OptimizationNeural treinamento = new OptimizationNeural();
		treinamento.treinamentoRede(rede, entradas, saidas, 
							TAXA_CRUZAMENTO, TAXA_MUTACAO, TAM_POPULACAO, MAX_GERACOES);
		
		Util.escreverPesos(caminho+"Resultado_9/berkeley/sobelHorizontal/pesos.txt", treinamento.getPesosTreinamento(), treinamento.getBiasTreinamento());
		Util.escreverErros(caminho+"Resultado_9/berkeley/sobelHorizontal/erros.txt", treinamento.getErros());
		
		for (int i = 1; i <= 20; i++) {
			double[][] imagemEntrada = Util.lerImagem(caminho+"base/"+i+".bmp");
			
			imagemEntrada = Util.dividirMatriz(imagemEntrada, 255);
			
			List<double[][]> saidasRede = rede.estimularListaSaidas(imagemEntrada);
			for (int s = 0; s < saidasRede.size(); s++) {
				double[][] saidaCamada =  Util.multiplicarMatriz(saidasRede.get(s), 255);
				Util.salvaImagem(caminho+"Resultado_9/berkeley/sobelHorizontal/"+i+"_"+s+".bmp", saidaCamada);
			}
		}
	}
	
}
