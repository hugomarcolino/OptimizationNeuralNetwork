package testes;

import java.util.ArrayList;
import java.util.List;

import rede.RedeNeural;
import treinamento.OptimizationNeural;
import treinamento.OptimizationResilient;
import treinamento.Resilientpropagation;
import util.Util;

public class Teste {

	public static String caminho = "C:/Users/Hugo/OneDrive/teste/resultados/";
	
	public static void main(String[] args) throws Exception {
		
		//testeBerkeleyHistograma();
		//testeBerkeleyLaplaciano();
		//testeBerkeleyMedia();
		
		//testeBerkeleySobel();
		//testeBerkeleySobelVertical();
		//testeBerkeleySobelHorizontal();
		
		//testePH2Segmentacao();
		testeDriveSegmentacao();
		//testeBerkeleySegmentacao();
		
	}
	
	private static void testePH2Segmentacao() throws Exception {

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
		treinamento.treinamentoRede(rede, entradas, saidas, 10000);
		
		Util.escreverPesos(caminho+"Resultado_8/ph2/segmentacao/pesos.txt", treinamento.getPesosTreinamento(), treinamento.getBiasTreinamento());
		Util.escreverErros(caminho+"Resultado_8/ph2/segmentacao/erros.txt", treinamento.getErros());
		
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

	private static void testeDriveSegmentacao() throws Exception {
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
		treinamento.treinamentoRede(rede, entradas, saidas, 10000);
		
		Util.escreverPesos(caminho+"Resultado_8/drive/segmentacao/pesos.txt", treinamento.getPesosTreinamento(), treinamento.getBiasTreinamento());
		Util.escreverErros(caminho+"Resultado_8/drive/segmentacao/erros.txt", treinamento.getErros());
		
		for (int i = 21; i <= 40; i++) {
			double[][] imagemEntrada = Util.lerImagem(caminho+"drive/"+i+".bmp");
			
			imagemEntrada = Util.dividirMatriz(imagemEntrada, 255);
			
			List<double[][]> saidasRede = rede.estimularListaSaidas(imagemEntrada);
			for (int s = 0; s < saidasRede.size(); s++) {
				double[][] saidaCamada =  Util.multiplicarMatriz(saidasRede.get(s), 255);
				Util.salvaImagem(caminho+"Resultado_8/drive/segmentacao/"+i+"_"+s+".bmp", saidaCamada);
			}
		}
	}

	
	private static void testeBerkeleySegmentacao() throws Exception {

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
		
		Resilientpropagation treinamento = new Resilientpropagation();
		treinamento.treinamentoRede(rede, entradas, saidas, 250);
		
		Util.escreverPesos(caminho+"Resultado_8/berkeley/segmentacao/pesos.txt", treinamento.getPesosTreinamento(), treinamento.getBiasTreinamento());
		Util.escreverErros(caminho+"Resultado_8/berkeley/segmentacao/erros.txt", treinamento.getErros());
		
		for (int i = 1; i <= 20; i++) {
			double[][] imagemEntrada = Util.lerImagem(caminho+"base/"+i+".bmp");
			
			imagemEntrada = Util.dividirMatriz(imagemEntrada, 255);
			
			List<double[][]> saidasRede = rede.estimularListaSaidas(imagemEntrada);
			for (int s = 0; s < saidasRede.size(); s++) {
				double[][] saidaCamada =  Util.multiplicarMatriz(saidasRede.get(s), 255);
				Util.salvaImagem(caminho+"Resultado_8/berkeley/segmentacao/"+i+"_"+s+".bmp", saidaCamada);
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
		
		Resilientpropagation treinamento = new Resilientpropagation();
		treinamento.treinamentoRede(rede, entradas, saidas, 100);
		
		Util.escreverPesos(caminho+"Resultado_8/berkeley/histograma/pesos.txt", treinamento.getPesosTreinamento(), treinamento.getBiasTreinamento());
		Util.escreverErros(caminho+"Resultado_8/berkeley/histograma/erros.txt", treinamento.getErros());
		
		for (int i = 1; i <= 20; i++) {
			double[][] imagemEntrada = Util.lerImagem(caminho+"base/"+i+".bmp");
			
			imagemEntrada = Util.dividirMatriz(imagemEntrada, 255);
			
			List<double[][]> saidasRede = rede.estimularListaSaidas(imagemEntrada);
			for (int s = 0; s < saidasRede.size(); s++) {
				double[][] saidaCamada =  Util.multiplicarMatriz(saidasRede.get(s), 255);
				Util.salvaImagem(caminho+"Resultado_8/berkeley/histograma/"+i+"_"+s+".bmp", saidaCamada);
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
		
		Resilientpropagation treinamento = new Resilientpropagation();
		treinamento.treinamentoRede(rede, entradas, saidas, 100);
		
		Util.escreverPesos(caminho+"Resultado_8/berkeley/laplaciano/pesos.txt", treinamento.getPesosTreinamento(), treinamento.getBiasTreinamento());
		Util.escreverErros(caminho+"Resultado_8/berkeley/laplaciano/erros.txt", treinamento.getErros());
		
		for (int i = 1; i <= 20; i++) {
			double[][] imagemEntrada = Util.lerImagem(caminho+"base/"+i+".bmp");
			
			imagemEntrada = Util.dividirMatriz(imagemEntrada, 255);
			
			List<double[][]> saidasRede = rede.estimularListaSaidas(imagemEntrada);
			for (int s = 0; s < saidasRede.size(); s++) {
				double[][] saidaCamada =  Util.multiplicarMatriz(saidasRede.get(s), 255);
				Util.salvaImagem(caminho+"Resultado_8/berkeley/laplaciano/"+i+"_"+s+".bmp", saidaCamada);
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
		
		Resilientpropagation treinamento = new Resilientpropagation();
		treinamento.treinamentoRede(rede, entradas, saidas, 100);
		
		Util.escreverPesos(caminho+"Resultado_8/berkeley/Media/pesos.txt", treinamento.getPesosTreinamento(), treinamento.getBiasTreinamento());
		Util.escreverErros(caminho+"Resultado_8/berkeley/Media/erros.txt", treinamento.getErros());
		
		for (int i = 1; i <= 20; i++) {
			double[][] imagemEntrada = Util.lerImagem(caminho+"base/"+i+".bmp");
			
			imagemEntrada = Util.dividirMatriz(imagemEntrada, 255);
			
			List<double[][]> saidasRede = rede.estimularListaSaidas(imagemEntrada);
			for (int s = 0; s < saidasRede.size(); s++) {
				double[][] saidaCamada =  Util.multiplicarMatriz(saidasRede.get(s), 255);
				Util.salvaImagem(caminho+"Resultado_8/berkeley/Media/"+i+"_"+s+".bmp", saidaCamada);
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
		Util.escreverErros(caminho+"Resultado_8/berkeley/sobel/erros.txt", treinamento.getErros());
		
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
		
		for (int i = 1; i <= 1; i++) {
			double[][] entrada = Util.lerImagem(caminho+"base/"+i+".bmp");
			double[][] saida = Util.lerImagem(caminho+"baseSobel/"+i+"_sobelVertical.bmp");
			
			entrada = Util.dividirMatriz(entrada, 255);
			saida = Util.dividirMatriz(saida, 255);			
			
			entradas.add(entrada);
			saidas.add(saida);
		}
		
		RedeNeural rede = new RedeNeural(315, 477, 1, 3, 0);
		
//		Resilientpropagation treinamento = new Resilientpropagation();
//		treinamento.treinamentoRede(rede, entradas, saidas, 100);
		
		OptimizationResilient treinamento = new OptimizationResilient();
		treinamento.treinamentoRede(rede, entradas, saidas, 100);
		
		Util.escreverPesos(caminho+"Resultado_8/berkeley/sobelVertical/pesos.txt", treinamento.getPesosTreinamento(), treinamento.getBiasTreinamento());
		Util.escreverErros(caminho+"Resultado_8/berkeley/sobelVertical/erros.txt", treinamento.getErros());
		
		for (int i = 1; i <= 20; i++) {
			double[][] imagemEntrada = Util.lerImagem(caminho+"base/"+i+".bmp");
			
			imagemEntrada = Util.dividirMatriz(imagemEntrada, 255);
			
			List<double[][]> saidasRede = rede.estimularListaSaidas(imagemEntrada);
			for (int s = 0; s < saidasRede.size(); s++) {
				double[][] saidaCamada =  Util.multiplicarMatriz(saidasRede.get(s), 255);
				Util.salvaImagem(caminho+"Resultado_8/berkeley/sobelVertical/"+i+"_"+s+".bmp", saidaCamada);
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
		
		Util.escreverPesos(caminho+"Resultado_8/berkeley/sobelHorizontal/pesos.txt", treinamento.getPesosTreinamento(), treinamento.getBiasTreinamento());
		Util.escreverErros(caminho+"Resultado_8/berkeley/sobelHorizontal/erros.txt", treinamento.getErros());
		
		for (int i = 1; i <= 20; i++) {
			double[][] imagemEntrada = Util.lerImagem(caminho+"base/"+i+".bmp");
			
			imagemEntrada = Util.dividirMatriz(imagemEntrada, 255);
			
			List<double[][]> saidasRede = rede.estimularListaSaidas(imagemEntrada);
			for (int s = 0; s < saidasRede.size(); s++) {
				double[][] saidaCamada =  Util.multiplicarMatriz(saidasRede.get(s), 255);
				Util.salvaImagem(caminho+"Resultado_8/berkeley/sobelHorizontal/"+i+"_"+s+".bmp", saidaCamada);
			}
		}
	}
	
}
