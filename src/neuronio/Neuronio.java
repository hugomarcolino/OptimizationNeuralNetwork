package neuronio;

public abstract class Neuronio {

	protected double bias;

	protected double somatorio;
	protected double saida;
	
	protected double pesoBias;
	protected double[][] pesos;
	
	//TREINAMENTO
	protected double sensibilidade;
	
	protected double[][] gradientes;
	protected double gradienteBias;
	
	protected double[][] deltas;
	protected double deltaBias;
	
	protected double[][] gradientesAnterior;
	protected double gradienteBiasAnterior;
	
	protected double[][] deltasAnterior;
	protected double deltaBiasAnterior;
	//--
	
	public Neuronio(double[][] pesos, double pesoBias) {
		this.bias = 1.0;
		
		this.pesoBias = pesoBias;
		this.pesos = pesos;
		
		this.inicializarVariaveis();
	}

	public abstract double estimular(double[][] entradas, double[][] pesos, double pesoBias);
	
	public abstract double estimularDerivadaFuncao();
	
	public double getSaida() {
		return saida;
	}

	public double getBias() {
		return bias;
	}

	//TREINAMENTO
	public void inicializarVariaveis(){
		this.sensibilidade = 0;
		
		this.gradientes = new double[pesos.length][pesos[0].length];
		this.gradienteBias = 0;
		
		this.deltas = new double[pesos.length][pesos[0].length];
		this.deltaBias = 0;
		
		this.gradientesAnterior = new double[pesos.length][pesos[0].length];
		this.gradienteBiasAnterior = 0;
		
		this.deltasAnterior = new double[pesos.length][pesos[0].length];
		for (int i = 0; i < deltasAnterior.length; i++) {
			for (int j = 0; j < deltasAnterior[i].length; j++) {				
				this.deltasAnterior[i][j] = 0.1;	
			}
		}
		this.deltaBiasAnterior = 0.1;
	}
	
	public void zerarSensibilidade() {
		this.sensibilidade = 0;
	}
	
	public double getSensibilidade() {
		return sensibilidade;
	}

	public void setSensibilidade(double sensibilidade) {
		this.sensibilidade = sensibilidade;
	}
	
	public void zerarGradientes() {
		for (int i = 0; i < gradientes.length; i++) {
			for (int j = 0; j < gradientes[i].length; j++) {
				this.gradientesAnterior[i][j] = 0;
				this.gradientes[i][j] = 0;
			}
		}
		this.gradienteBiasAnterior = 0;
		this.gradienteBias = 0;
	}
	
	public double[][] getGradientes() {
		return gradientes;
	}

	public void setGradientes(double[][] gradientes) {
		this.gradientes = gradientes;
	}

	public double getGradienteBias() {
		return gradienteBias;
	}

	public void setGradienteBias(double gradienteBias) {
		this.gradienteBias = gradienteBias;
	}
	
	public double[][] getDeltas() {
		return deltas;
	}

	public void setDeltas(double[][] deltas) {
		this.deltas = deltas;
	}

	public double getDeltaBias() {
		return deltaBias;
	}

	public void setDeltaBias(double deltaBias) {
		this.deltaBias = deltaBias;
	}

	public double[][] getGradientesAnterior() {
		return gradientesAnterior;
	}

	public void setGradientesAnterior(double[][] gradientesAnterior) {
		this.gradientesAnterior = gradientesAnterior;
	}

	public double getGradienteBiasAnterior() {
		return gradienteBiasAnterior;
	}

	public void setGradienteBiasAnterior(double gradienteBiasAnterior) {
		this.gradienteBiasAnterior = gradienteBiasAnterior;
	}

	public double[][] getDeltasAnterior() {
		return deltasAnterior;
	}

	public void setDeltasAnterior(double[][] deltasAnterior) {
		this.deltasAnterior = deltasAnterior;
	}

	public double getDeltaBiasAnterior() {
		return deltaBiasAnterior;
	}

	public void setDeltaBiasAnterior(double deltaBiasAnterior) {
		this.deltaBiasAnterior = deltaBiasAnterior;
	}
	//---
	
}