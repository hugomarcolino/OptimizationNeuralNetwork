package rede;

import java.util.ArrayList;
import java.util.List;

import camada.Camada;
import camada.CamadaConvolucao;
import camada.CamadaReconstrucao;

public class RedeNeural {
	
	private int tamCampo; 
	private int tamSobreposicao;
	
	private Camada[] camadas;
	
	public RedeNeural(int nLinhas, int nColunas, int nCamadas, int tamCampo, int tamSobreposicao){

		try {			
			validarConfiguracao(nLinhas, nColunas, nCamadas, tamCampo, tamSobreposicao);
			
			int linhas = nLinhas;
			int colunas = nColunas;
			
			this.tamCampo = tamCampo;
			this.tamSobreposicao = tamSobreposicao;
			
			this.camadas = new Camada[nCamadas*2];
			
			for (int i = 0; i < camadas.length; i++) {		
				if(i < nCamadas) {
					//Construir as camadas de convolução
					//Calcular o tamanho de linhas e colunas da camada de convolução
					linhas = (linhas - tamSobreposicao)/(tamCampo-tamSobreposicao);
					colunas = (colunas - tamSobreposicao)/(tamCampo-tamSobreposicao);
					
					this.camadas[i] = new CamadaConvolucao(linhas, colunas, tamCampo, tamSobreposicao);
				} else {
					//Construir as camadas de reconstrução
					//Calcular tamanho das linhas e colunas da camada de reconstrução
					
					linhas = linhas*(tamCampo-tamSobreposicao)+tamSobreposicao;
					colunas = colunas*(tamCampo-tamSobreposicao)+tamSobreposicao;
					
					this.camadas[i] = new CamadaReconstrucao(linhas, colunas, tamCampo, tamSobreposicao);
				}
			
			}
			
		} catch (IllegalArgumentException e) {
			System.err.println(e.getMessage());
		}

	}
	
	/**
	 * Valida as configurações da rede (nCamadas, tamCampo, tamSobreposicao).
	 * @param x - Número de linhas da imagem
	 * @param y - Número de colunas da imagem
	 * @param nCamadas - Número de camadas
	 * @param tamCampo - Tamanho das linhas e colunas do campo receptivo
	 * @param tamSobreposicao -  Tamanho das linhas e colunas da sobreposiçao dos campos
	 * @throws IllegalArgumentException - Lança exceção, caso seja impossível montar a rede neural
	 */
	private void validarConfiguracao(int x, int y, int nCamadas, int tamCampo, int tamSobreposicao) throws IllegalArgumentException {
		for (int i = 0; i < nCamadas; i++) {			
			if ( (x - tamSobreposicao)%(tamCampo-tamSobreposicao) != 0 || (y - tamSobreposicao)%(tamCampo-tamSobreposicao) != 0
					|| x<=0 || y<=0){
				throw new IllegalArgumentException("Problemas na configuração da rede!\n"
						+ "Número máximo de camadas com essas configurações: "+i);
			}
			x = (x - tamSobreposicao)/(tamCampo-tamSobreposicao);
			y = (y - tamSobreposicao)/(tamCampo-tamSobreposicao);
		}
	}
	
	/**
	 * Estimula a rede neural
	 * @param imagem - Imagem de entrada
	 * @return - Imagem de saída
	 */
	public double[][] estimular(double[][] imagem){
		double[][] imagemResposta = imagem;
		
		for (int i = 0; i < camadas.length; i++) {
			imagemResposta = this.camadas[i].estimular(imagemResposta);
		}
		
		return imagemResposta;
	}
	
	public java.util.List<double[][]> estimularListaSaidas(double[][] imagem){
		java.util.List<double[][]> imagensResposta = new java.util.ArrayList<double[][]>();
		
		double[][] saidasCamada = imagem;
		for (int i = 0; i < camadas.length; i++) {
			saidasCamada = this.camadas[i].estimular(saidasCamada);
			imagensResposta.add(saidasCamada);
		}
		
		return imagensResposta;
	}

	public int getTamCampo() {
		return tamCampo;
	}

	public int getTamSobreposicao() {
		return tamSobreposicao;
	}

	public Camada[] getCamadas() {
		return camadas;
	}
	
	public void setarPesos(double[] position){
		
  		int p = 0;
  		
  		List<Double> biasCamada = new ArrayList<Double>();
  		List<double[][]> pesosCamadas = new ArrayList<double[][]>();
  		
  		for (int c = 0; c < this.getCamadas().length; c++) {			
  			double[][] pesosCamada = new double[this.getTamCampo()][this.getTamCampo()];
  			
  			for (int i = 0; i < pesosCamada.length; i++) {
  				for (int j = 0; j < pesosCamada.length; j++) {
  					pesosCamada[i][j] = position[p++]; 
  				}
  			}
  			pesosCamadas.add(pesosCamada);
  		}
  		
  		for (int c = 0; c < this.getCamadas().length; c++) {			
  			biasCamada.add(position[p++]);
  		}
  		
  		for (int c = 0; c < this.getCamadas().length; c++) {
  			this.getCamadas()[c].setPesosCamada(pesosCamadas.get(c));
  			this.getCamadas()[c].setBiasCamada(biasCamada.get(c));
  		}

		
	}
	
}
