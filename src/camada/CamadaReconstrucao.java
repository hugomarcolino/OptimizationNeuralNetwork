package camada;

import neuronio.NeuronioReconstrucao;

public class CamadaReconstrucao extends Camada {
	
	public CamadaReconstrucao(int linhas, int colunas, int tamCampo, int tamSobreposicao) {
		super(linhas, colunas, tamCampo, tamSobreposicao);
		
		for (int i = 0; i < this.linhas; i++) {
			for (int j = 0; j < this.colunas; j++) {
				this.neuronios[i][j] = new NeuronioReconstrucao(this.getPesosNeuronio(i, j), this.biasCamada);
			}
		}
	}

	@Override
	public double[][] getEntradasNeuronio(int i, int j, double[][] entradasCamada) {
		
		int gap = tamCampo-tamSobreposicao;
		
		int linhasAnt = (linhas - tamSobreposicao)/(tamCampo-tamSobreposicao);
		int colunasAnt = (colunas - tamSobreposicao)/(tamCampo-tamSobreposicao);
		
		int iMin = (i%gap) == 0 ? Math.max((i/gap) - tamSobreposicao, 0) : Math.max(i/gap, 0) ;
		int iMax = Math.min(i/gap, linhasAnt-1) ;
		int jMin = (j%gap) == 0 ? Math.max((j/gap) - tamSobreposicao, 0) : Math.max(j/gap, 0) ;
		int jMax = Math.min(j/gap, colunasAnt-1);
		
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
		
		int gap = tamCampo-tamSobreposicao;
		
		int linhasAnt = (linhas - tamSobreposicao)/(tamCampo-tamSobreposicao);
		int colunasAnt = (colunas - tamSobreposicao)/(tamCampo-tamSobreposicao);
		
		int iMin = (i%gap) == 0 ? Math.max((i/gap) - tamSobreposicao, 0) : Math.max(i/gap, 0) ;
		int iMax = Math.min(i/gap, linhasAnt-1) ;
		int jMin = (j%gap) == 0 ? Math.max((j/gap) - tamSobreposicao, 0) : Math.max(j/gap, 0) ;
		int jMax = Math.min(j/gap, colunasAnt-1);
		
		double[][] pesos = new double[iMax-(iMin-1)][jMax-(jMin-1)];
		
		for (int a=0, x = iMin; x <= iMax; x++, a++) {
			for (int b=0, y = jMin; y <= jMax; y++, b++) {
				pesos[a][b] = this.pesosCamada[i - x*gap][j - y*gap];
			}
		}
		
		return pesos;
	}

	@Override
	public int[] getPosicoesAnterior(int i, int j) {
		int[] posicoes = new int[4];
		
		int gap = tamCampo-tamSobreposicao;
		
		int linhasAnt = (linhas - tamSobreposicao)/(tamCampo-tamSobreposicao);
		int colunasAnt = (colunas - tamSobreposicao)/(tamCampo-tamSobreposicao);
		
		int iMin = (i%gap) == 0 ? Math.max((i/gap) - tamSobreposicao, 0) : Math.max(i/gap, 0) ;
		int iMax = Math.min(i/gap, linhasAnt-1) ;
		int jMin = (j%gap) == 0 ? Math.max((j/gap) - tamSobreposicao, 0) : Math.max(j/gap, 0) ;
		int jMax = Math.min(j/gap, colunasAnt-1);
		
		posicoes[0] = iMin;
		posicoes[1] = iMax;
		posicoes[2] = jMin;
		posicoes[3] = jMax;
		
		return posicoes;
	}
}