package camada;

import neuronio.Neuronio;
import util.Util;

public abstract class Camada {
	
	protected Neuronio[][] neuronios;	
	
	protected double[][] pesosCamada;
	protected double biasCamada;

	protected int linhas;
	protected int colunas;
	
	protected int tamCampo;
	protected int tamSobreposicao;
		
	public Camada(int linhas, int colunas, int tamCampo, int tamSobreposicao){
		this.linhas = linhas;
		this.colunas = colunas;
		
		this.tamCampo = tamCampo;
		this.tamSobreposicao = tamSobreposicao;
		
		this.neuronios = new Neuronio[this.linhas][this.colunas];

		this.biasCamada = Util.gerarAleatorio();
		
		this.pesosCamada = new double[tamCampo][tamCampo];
		for (int i = 0; i < pesosCamada.length; i++) {			
			for (int j = 0; j < pesosCamada[i].length; j++) {
				pesosCamada[i][j] = Util.gerarAleatorio();
			}
		}				
	}
	
	public double[][] estimular(double[][] entradasCamada){
		double[][] saidas = new double[this.linhas][this.colunas];

		for (int i = 0; i < this.linhas; i++) {
			for (int j = 0; j < this.colunas; j++) {
				
				//ENTRADAS
				double[][] entradas = this.getEntradasNeuronio(i, j, entradasCamada);
				//PESOS		
				double[][] pesos = this.getPesosNeuronio(i, j);
				//BIAS
				double pesoBias = this.getBiasCamada();
				
				saidas[i][j] = this.neuronios[i][j].estimular(entradas, pesos, pesoBias);
			}
		}
		
		return saidas;
	}
	
	public double[][] getSaidasCamada(){
		double[][] saidasCamada = new double[this.linhas][this.colunas];
		
		for (int i = 0; i < saidasCamada.length; i++) {
			for (int j = 0; j < saidasCamada[i].length; j++) {
				saidasCamada[i][j] = this.getNeuronios()[i][j].getSaida();
			}
		}
		return saidasCamada;
	}
	
	public abstract int[] getPosicoesAnterior(int i, int j);
	
	public abstract double[][] getEntradasNeuronio(int i, int j, double[][] entradasCamada);
	
	public abstract double[][] getPesosNeuronio(int i, int j);

	public Neuronio[][] getNeuronios() {
		return neuronios;
	}

	public int getTamCampo(){
		return tamCampo;
	}

	public int getTamSobreposicao(){
		return tamSobreposicao;
	}
	
	public int getLinhas() {
		return linhas;
	}

	public int getColunas() {
		return colunas;
	}

	public double[][] getPesosCamada() {
		return pesosCamada;
	}
	
	public void setPesosCamada(double[][] pesosCamada) {
		this.pesosCamada = pesosCamada;
	}

	public double getBiasCamada() {
		return biasCamada;
	}

	public void setBiasCamada(double pesoBias) {
		this.biasCamada = pesoBias;
	}
}