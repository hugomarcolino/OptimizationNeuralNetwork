package camada;

import neuronio.NeuronioConvolucao;

public class CamadaConvolucao extends Camada {
	
	public CamadaConvolucao(int linhas, int colunas, int tamCampo, int tamSobreposicao) {
		super(linhas, colunas, tamCampo, tamSobreposicao);
		
		for (int i = 0; i < this.linhas; i++) {
			for (int j = 0; j < this.colunas; j++) {
				this.neuronios[i][j] = new NeuronioConvolucao(this.getPesosNeuronio(i, j), this.biasCamada);
			}
		}
	}

	@Override
	public double[][] getEntradasNeuronio(int i, int j, double[][] entradasCamada) {
		
		int iMin = i*(tamCampo-tamSobreposicao);
		int iMax = iMin + (tamCampo-1);
		int jMin = j*(tamCampo-tamSobreposicao);
		int jMax =  jMin + (tamCampo-1);
		
		double[][] entradas = new double[iMax-(iMin-1)][jMax-(jMin-1)];
		
		for (int a = 0, x = iMin; x <= iMax; x++, a++) {
			for (int b = 0, y = jMin; y <= jMax; y++, b++) {
				entradas[a][b] = entradasCamada[x][y];
			}
		}
		
		return entradas;
	}

	@Override
	public double[][] getPesosNeuronio(int i, int j) {
		
		int iMin = i*(tamCampo-tamSobreposicao);
		int iMax = iMin + (tamCampo-1);
		int jMin = j*(tamCampo-tamSobreposicao);
		int jMax =  jMin + (tamCampo-1);
		
		double[][] pesos = new double[iMax-(iMin-1)][jMax-(jMin-1)];

		int gap = tamCampo-tamSobreposicao;
		
		for (int a=0, x = iMin; x <= iMax; x++, a++) {
			for (int b=0, y = jMin; y <= jMax; y++, b++) {
				pesos[a][b] = pesosCamada[x - i*gap][y - j*gap];
			}
		}	
		
		return pesos;
	}		
	
	@Override
	public int[] getPosicoesAnterior(int i, int j) {
		int[] posicoes = new int[4];
		
		int iMin = i*(tamCampo-tamSobreposicao);
		int iMax = iMin + (tamCampo-1);
		int jMin = j*(tamCampo-tamSobreposicao);
		int jMax =  jMin + (tamCampo-1);
		
		posicoes[0] = iMin;
		posicoes[1] = iMax;
		posicoes[2] = jMin;
		posicoes[3] = jMax;
		
		return posicoes;
	}
}