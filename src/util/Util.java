package util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

public class Util {
	
	public static double[][] lerImagem(String caminho) throws Exception {
		File f = new File(caminho);
		BufferedImage image = ImageIO.read(f);
		double[][] retorno = new double[image.getHeight()][image.getWidth()];
		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				double[] tmp = new double[1];
				image.getRaster().getPixel(i, j, tmp);
				retorno[j][i] = tmp[0];
			}
		}
		return retorno;
	}
	
	public static double[][] lerImagem(BufferedImage image) throws Exception {
		double[][] retorno = new double[image.getHeight()][image.getWidth()];
		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				double[] tmp = new double[1];
				image.getRaster().getPixel(i, j, tmp);
				retorno[j][i] = tmp[0];
			}
		}
		return retorno;
	}
	
	public static double[][][] lerImagemColorida(String caminho) throws Exception {
		File f = new File(caminho);
		BufferedImage image = ImageIO.read(f);
		double[][][] retorno = new double[3][image.getHeight()][image.getWidth()];
		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				double[] tmp = new double[3];
				image.getRaster().getPixel(i, j, tmp);
				retorno[0][j][i] = tmp[0];
				retorno[1][j][i] = tmp[1];
				retorno[2][j][i] = tmp[2];
			}
		}
		return retorno;
	}
	
	public static void salvaImagem(String caminho,
			double[][] imagem) throws Exception {
		try {
			BufferedImage image = new BufferedImage(imagem[0].length, imagem.length, BufferedImage.TYPE_BYTE_GRAY);
			for (int y = 0; y < imagem.length; ++y) {
				for (int x = 0; x < imagem[0].length; ++x) {
					double[] tmp = new double[] { imagem[y][x] };
					image.getRaster().setPixel(x, y, tmp);
				}
			}
			ImageIO.write(image, "jpg", new File(caminho));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static double[][] retornaImagemCinza(double[][][] imagem) throws Exception {
		try {
			BufferedImage image = new BufferedImage(imagem[0][0].length, imagem[0].length, BufferedImage.TYPE_BYTE_GRAY);
			for (int y = 0; y < imagem[0].length; ++y) {
				for (int x = 0; x < imagem[0][0].length; ++x) {
					double[] tmp = new double[] { imagem[0][y][x], imagem[1][y][x], imagem[2][y][x] };
					image.getRaster().setPixel(x, y, tmp);
				}
			}
			return lerImagem(image);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void salvaImagem(String caminho,
			double[][][] imagem) throws Exception {
		try {
			BufferedImage image = new BufferedImage(imagem[0][0].length, imagem[0].length, 5);
			for (int y = 0; y < imagem[0].length; ++y) {
				for (int x = 0; x < imagem[0][0].length; ++x) {
					double[] tmp = new double[] { imagem[0][y][x], imagem[1][y][x], imagem[2][y][x] };
					image.getRaster().setPixel(x, y, tmp);
				}
			}
			ImageIO.write(image, "bmp", new File(caminho));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static double[][] somaMatriz(double[][] m1, double[][] m2){
		double[][] soma = new double[m1.length][m1[0].length]; 
		
		for (int i = 0; i < soma.length; i++) {
			for (int j = 0; j < soma[i].length; j++) {
				soma[i][j] = m1[i][j]+ m2[i][j];
			}
		}
		return soma;
	}
	
	public static double[] matrizToArray(double[][] matriz){
		double[] array = new double[matriz.length*matriz[0].length];
		
		int nArray = 0;
		for (int i = 0; i < matriz.length; i++) {
			for (int j = 0; j < matriz[i].length; j++) {
				array[nArray++] = matriz[i][j];
			}
		}

		return array;
	}
	
	public static double[][] dividirMatriz(double[][] matriz, double fator){
		double[][] novaMatriz = new double[matriz.length][matriz[0].length];
		
		for (int i = 0; i < matriz.length; i++) {
			for (int j = 0; j < matriz[i].length; j++) {
				novaMatriz[i][j] = matriz[i][j]/fator;
			}
		}

		return novaMatriz;
	}
	
	public static double[][] multiplicarMatriz(double[][] matriz, double fator){
		double[][] novaMatriz = new double[matriz.length][matriz[0].length];
		
		for (int i = 0; i < matriz.length; i++) {
			for (int j = 0; j < matriz[i].length; j++) {
				novaMatriz[i][j] = matriz[i][j]*fator;
			}
		}

		return novaMatriz;
	}
	
	public static double gerarAleatorio(){
		Random random = new Random();
		double num = (random.nextDouble()*10)-0.5;
		
		return num;
	}
	
	private static void escrever(String caminho, List<String> texto){
		File arq = new File(caminho);
        try {
            arq.createNewFile();

            FileWriter fileWriter = new FileWriter(arq, false);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            
            for (String linha : texto) {				
            	printWriter.println(linha);
			}

            printWriter.flush();
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public static void escreverPesos(String caminho, List<double[][][]> pesos, List<double[]> bias) {
		List<String> texto = new ArrayList<String>();
		
		for (int ciclo = 0; ciclo < pesos.size(); ciclo++) {
			texto.add("Ciclo: "+(ciclo+1));
			double[][][] pesosRede = pesos.get(ciclo);
			double[] biasRede = bias.get(ciclo);
			for (int c = 0; c < pesosRede.length; c++) {
				texto.add("Camada: "+c);
				texto.add("Bias: "+ biasRede[c]);
				for(double[] pesosC : pesosRede[c]) {
					String linhaMatriz = "";
					for (double d : pesosC) {
						DecimalFormat fmt = new DecimalFormat("0.00");
						linhaMatriz += fmt.format(d)+" ";
						 
					}
					texto.add(linhaMatriz);
				}
			}
			texto.add("===============");
		}
		
		Util.escrever(caminho, texto);
	}
	
	public static void escreverErros(String caminho, List<Double> erros) {
		List<String> texto = new ArrayList<String>();
		
		for (int ciclo = 0; ciclo < erros.size(); ciclo++) {
			texto.add(erros.get(ciclo)+"");
		}
		
		Util.escrever(caminho, texto);
	}
}

