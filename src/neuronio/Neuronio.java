package neuronio;

public abstract class Neuronio {

	protected double bias;

	protected double somatorio;
	protected double saida;
	
	protected double[][] pesos;
	protected double pesoBias;
	
	//TREINAMENTO RPROP
	protected double sensibilidade;
	
	protected double[][] gradientes;
	protected double gradienteBias;
	
	protected double[][] gradientesAnterior;
	protected double gradienteBiasAnterior;
	
	protected double[][] deltas;
	protected double deltaBias;
	
	protected double[][] deltasPeso;
	protected double deltaPesoBias;	
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

	//TREINAMENTO RPROP
	public void zerarSensibilidade() {
		this.sensibilidade = 0;
	}
	
	public void inicializarVariaveis(){
		this.sensibilidade = 0;
		
		this.gradientes = new double[pesos.length][pesos[0].length];
		this.gradienteBias = 0;
		
		this.gradientesAnterior = new double[pesos.length][pesos[0].length];
		this.gradienteBiasAnterior = 0;
		
		this.deltas = new double[pesos.length][pesos[0].length];
		for (int i = 0; i < deltas.length; i++) {
			for (int j = 0; j < deltas[i].length; j++) {				
				this.deltas[i][j] = 0.1;	
			}
		}
		this.deltaBias = 0.1;
		
		this.deltasPeso = new double[pesos.length][pesos[0].length];
		this.deltaPesoBias = 0;

	}
	
	//GET
	public double getSensibilidade() {
		return sensibilidade;
	}

	public double getGradienteBias() {
		return gradienteBias;
	}
	
	public double[][] getGradientes() {
		return gradientes;
	}
	
	public double getGradienteBiasAnterior() {
		return gradienteBiasAnterior;
	}
	
	public double[][] getGradientesAnterior() {
		return gradientesAnterior;
	}
	
	public double getDeltaBias() {
		return deltaBias;
	}
	
	public double[][] getDeltas() {
		return deltas;
	}
	
	public double getDeltaPesoBias() {
		return deltaPesoBias;
	}

	public double[][] getDeltasPeso() {
		return deltasPeso;
	}
	
	//SET
	public void setSensibilidade(double sensibilidade) {
		this.sensibilidade = sensibilidade;
	}

	public void setGradienteBias(double gradienteBias) {
		this.gradienteBias = gradienteBias;
	}

	public void setGradientes(double[][] gradientes) {
		this.gradientes = gradientes;
	}

	public void setGradienteBiasAnterior(double gradienteBiasAnterior) {
		this.gradienteBiasAnterior = gradienteBiasAnterior;
	}
	
	public void setGradientesAnterior(double[][] gradientesAnterior) {
		this.gradientesAnterior = gradientesAnterior;
	}
	public void setDeltaBias(double deltaBias) {
		this.deltaBias = deltaBias;
	}
	
	public void setDeltas(double[][] deltas) {
		this.deltas = deltas;		
	}

	public void setDeltaPesoBias(double deltaPesoBias) {
		this.deltaPesoBias = deltaPesoBias;
	}
	
	public void setDeltasPeso(double[][] deltasPeso) {
		this.deltasPeso = deltasPeso;
	}
	//---
}