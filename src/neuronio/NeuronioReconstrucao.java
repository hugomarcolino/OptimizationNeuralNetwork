package neuronio;

public class NeuronioReconstrucao extends Neuronio {
	
	public NeuronioReconstrucao(double[][] pesos, double pesoBias) {
		super(pesos, pesoBias);
	}

	@Override
	public double estimular(double[][] entradas, double[][] pesos, double pesoBias) {
		this.somatorio = 0;
		
		this.pesos = pesos;
		this.pesoBias = pesoBias;
		
		for (int i = 0; i < pesos.length; i++) {			
			for (int j = 0; j < pesos[i].length; j++) {				
				this.somatorio += entradas[i][j]*this.pesos[i][j];
			}
		}
		
		this.somatorio /= entradas.length*entradas[0].length;
		this.somatorio += this.pesoBias*this.bias;
		
		this.saida = FuncaoAtivacao.sigmoideLogistica(this.somatorio);
		
		return this.saida;
	}
	
	@Override
	public double estimularDerivadaFuncao() {
		return FuncaoAtivacao.derivadaSigmoideLogistica(this.saida);
	}
}
