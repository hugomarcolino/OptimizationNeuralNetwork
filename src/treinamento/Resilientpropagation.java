package treinamento;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rede.RedeNeural;
import camada.Camada;
import camada.CamadaConvolucao;
import camada.CamadaReconstrucao;
import neuronio.Neuronio;

public class Resilientpropagation {

	private double deltaMax = 50;
	private double deltaMin = 0.000001;
	private double nPos = 1.2;
	private double nNeg = 0.5;
	
	private List<double[]> biasTreinamento = new ArrayList<double[]>();
	private List<double[][][]> pesosTreinamento = new ArrayList<double[][][]>();
	
	private List<Double> errosTreinamento = new ArrayList<Double>();
	private List<Double> errosValidacao = new ArrayList<Double>();
	
	public RedeNeural treinamentoRede(RedeNeural rede, List<double[][]> entradas, List<double[][]> saidas, 
			List<double[][]> entradasValidacao, List<double[][]> saidasValidacao ){

		List<Integer> ordemTreino = new ArrayList<Integer>();
		for (int i = 0; i < entradas.size(); i++) {
			ordemTreino.add(i);
		}
		
		int ciclo = 1;
		
		while(!pararTreinamento(100, 0.001)){
			
			Collections.shuffle(ordemTreino);
			System.out.println("Ciclo: "+ciclo++);
			for (int j = 0; j < ordemTreino.size(); j++) {
				int nImagem = ordemTreino.get(j);
				System.out.println("Treinando.. Imagem "+(nImagem+1));
				this.treinarRede(rede, entradas.get(nImagem), saidas.get(nImagem));
				this.zerarSensibilidades(rede);				
			}
			
			this.reajustarPesos(rede);			
			this.zerarGradientes(rede);
			
			this.armazenaPesos(rede);
			this.armazenaErrosTreinamento(rede, entradas, saidas);
			this.armazenaErrosValidacao(rede, entradasValidacao, saidasValidacao);
			
			rede.imprimePesos();
			//System.out.println("================");

		}
		
		return rede;
	}


	public RedeNeural treinamentoRede(RedeNeural rede, List<double[][]> entradas, List<double[][]> saidas, int nCiclos){
		List<Integer> ordemTreino = new ArrayList<Integer>();
		for (int i = 0; i < entradas.size(); i++) {
			ordemTreino.add(i);
		}
		
		for (int ciclo = 1 ; ciclo <= nCiclos; ciclo++) {
			Collections.shuffle(ordemTreino);
			System.out.println("Ciclo: "+ciclo);
			for (int j = 0; j < ordemTreino.size(); j++) {
				int nImagem = ordemTreino.get(j);
				System.out.println("Treinando.. Imagem "+(nImagem+1));
				this.treinarRede(rede, entradas.get(nImagem), saidas.get(nImagem));
				this.zerarSensibilidades(rede);				
			}
			
			this.reajustarPesos(rede);			
			this.zerarGradientes(rede);
			
			this.armazenaPesos(rede);
			this.armazenaErrosTreinamento(rede, entradas, saidas);
			
			rede.imprimePesos();
			//System.out.println("================");

		}
		
		return rede;
	}

	private void treinarRede(RedeNeural rede, double[][] entradas, double[][] saidas){ 
		
		double[][] saidasRede = rede.estimular(entradas);
		
		Camada[] camadas = rede.getCamadas();
		for (int c = camadas.length-1; c >= 0 ; c--) {
			Camada camada = camadas[c];
			Camada camadaAnterior = c > 0 ? rede.getCamadas()[c-1] : null;
			
			for (int i = 0; i < camada.getLinhas(); i++) {
				for (int j = 0; j < camada.getColunas(); j++) {
					Neuronio neuronio = camada.getNeuronios()[i][j];
		
					int[] posicoes = camada.getPosicoesAnterior(i, j);
					double[][] pesos = camada.getPesosNeuronio(i, j);
					
					int iMin = posicoes[0];
					int iMax = posicoes[1];
					int jMin = posicoes[2];
					int jMax = posicoes[3];
					
					//CALCULAR SENSIBILIDADE
					if(c == camadas.length-1){
						neuronio.setSensibilidade(saidasRede[i][j] - saidas[i][j]);
						//neuronio.setSensibilidade(saidas[i][j] - saidasRede[i][j]);
					}
					neuronio.setSensibilidade(neuronio.getSensibilidade()*neuronio.estimularDerivadaFuncao());
			
					//RETROPROPAGAR SENSIBILIDADE
					if(c > 0){
						for (int a=0, x = iMin; x <= iMax; x++, a++) {
							for (int b=0, y = jMin; y <= jMax; y++, b++) {
								Neuronio neuronioAnterior = camadaAnterior.getNeuronios()[x][y];
								neuronioAnterior.setSensibilidade(neuronioAnterior.getSensibilidade()
														+ neuronio.getSensibilidade()*pesos[a][b]);
							}
						}
					}
					
					//CALCULAR GRADIENTE
					double gradienteBias = neuronio.getSensibilidade()*neuronio.getBias();
					neuronio.setGradienteBias(neuronio.getGradienteBias() + gradienteBias);
					
					double[][] entradasCamada = c > 0 ? camadaAnterior.getSaidasCamada() : entradas;
					double[][] entradasNeuronio = camada.getEntradasNeuronio(i, j, entradasCamada);
					
					double[][] gradientes = new double[pesos.length][pesos[0].length];
					for (int x = 0; x < gradientes.length; x++) {
						for (int y = 0; y < gradientes[x].length; y++) {							
							gradientes[x][y] = neuronio.getSensibilidade()*entradasNeuronio[x][y]
												+ neuronio.getGradientes()[x][y];
						}
					}
					neuronio.setGradientes(gradientes);
				}
			}
		}
	}
	
	private void reajustarPesos(RedeNeural rede){
		//REAJUSTAR PESOS
		Camada[] camadas = rede.getCamadas();
		for (int c = 0; c < camadas.length; c++) {
			Camada camada = camadas[c];
			
			double biasCamada = camada.getBiasCamada();
			double[][] pesosCamada = camada.getPesosCamada();
			
			double atualizaBiasCamada = 0;
			double[][] atualizaPesosCamada = new double[pesosCamada.length][pesosCamada[0].length];
			
			for (int i = 0; i < camada.getLinhas(); i++) {
				for (int j = 0; j < camada.getColunas(); j++) {
					Neuronio neuronio = camada.getNeuronios()[i][j];
					
					//BIAS
					double atualizaBias = 0;
					
					double sinalBias = Math.signum(neuronio.getGradienteBias()*neuronio.getGradienteBiasAnterior());
					double sinalGradBias = Math.signum(neuronio.getGradienteBias());
					if(sinalBias > 0) {
						double delta = Math.min(neuronio.getDeltaBiasAnterior()*nPos, deltaMax);
						neuronio.setDeltaBiasAnterior(delta);
						double atualiza = (-sinalGradBias)*delta;
						//neuronio.setPesoBias(neuronio.getPesoBias()+atualiza);
						atualizaBias = atualiza;
						neuronio.setGradienteBiasAnterior(neuronio.getGradienteBias());
					} else if(sinalBias < 0) {
						double delta = Math.max(neuronio.getDeltaBiasAnterior()*nNeg, deltaMin);
						neuronio.setDeltaBiasAnterior(delta);
						neuronio.setGradienteBiasAnterior(0.0);
					} else {
						double delta = neuronio.getDeltaBiasAnterior();
						double atualiza = (-sinalGradBias)*delta;
						//neuronio.setPesoBias(neuronio.getPesoBias()+atualiza);
						atualizaBias = atualiza;
						neuronio.setGradienteBiasAnterior(neuronio.getGradienteBias());
					}
					//- - -
					
					//ACUMULAR BIAS CAMADA
					atualizaBiasCamada += atualizaBias;
					//- - -
					
					//PESOS
					double[][] pesos = camada.getPesosNeuronio(i, j);
					double[][] atualizaPesos = new double[pesos.length][pesos[0].length];
					
					for (int x = 0; x < pesos.length; x++) {
						for (int y = 0; y < pesos[x].length; y++) {							
							double sinal = Math.signum(neuronio.getGradientes()[x][y]*neuronio.getGradientesAnterior()[x][y]);
							double sinalGrad = Math.signum(neuronio.getGradientes()[x][y]);
							if(sinal > 0) {
								double delta = Math.min(neuronio.getDeltasAnterior()[x][y]*nPos, deltaMax);
								neuronio.getDeltasAnterior()[x][y] = delta;
								double atualiza = (-sinalGrad)*delta;
								atualizaPesos[x][y] = atualiza;
								neuronio.getGradientesAnterior()[x][y] = neuronio.getGradientes()[x][y];						
							} else if(sinal < 0) {
								double delta = Math.max(neuronio.getDeltasAnterior()[x][y]*nNeg, deltaMin);
								neuronio.getDeltasAnterior()[x][y] = delta;
								neuronio.getGradientesAnterior()[x][y] = 0.0;
							} else {
								double delta = neuronio.getDeltasAnterior()[x][y];
								double atualiza = (-sinalGrad)*delta;
								atualizaPesos[x][y] = atualiza;
								neuronio.getGradientesAnterior()[x][y] = neuronio.getGradientes()[x][y];
							}
						}
					}
					//- - -
					
					//ACUMULAR PESOS CAMADA
					int[] posicoes = camada.getPosicoesAnterior(i, j);
					
					int iMin = posicoes[0];
					int iMax = posicoes[1];
					int jMin = posicoes[2];
					int jMax = posicoes[3];
					
					int gap = camada.getTamCampo()-camada.getTamSobreposicao();
					
					for (int a=0, x = iMin; x <= iMax; x++, a++) {
						for (int b=0, y = jMin; y <= jMax; y++, b++) {
							if(camada instanceof CamadaConvolucao){								
								atualizaPesosCamada[x - i*gap][y - j*gap] += atualizaPesos[a][b];
							} else if (camada instanceof CamadaReconstrucao){
								atualizaPesosCamada[i - x*gap][j - y*gap] += atualizaPesos[a][b]; 
							}
						}
					}
					//- - - 
				}
			}
			//REAJUSTAR BIAS CAMADA
			atualizaBiasCamada /= camada.getLinhas()*camada.getColunas();
			biasCamada += atualizaBiasCamada;
			
			camada.setBiasCamada(biasCamada);
			//- - -
			
			//REAJUSTAR PESOS CAMADA
			int tamSobreposicao = camada.getTamSobreposicao();
			int tamCampo = camada.getTamCampo();
			
			int linhasAnt = (camada.getLinhas() - tamSobreposicao)/(tamCampo-tamSobreposicao);
			int colunasAnt = (camada.getColunas() - tamSobreposicao)/(tamCampo-tamSobreposicao);
			
			for (int x = 0; x < atualizaPesosCamada.length; x++) {
				for (int y = 0; y < atualizaPesosCamada[x].length; y++) {
					if (camada instanceof CamadaConvolucao){
						atualizaPesosCamada[x][y] /= camada.getLinhas()*camada.getColunas();
						pesosCamada[x][y] += atualizaPesosCamada[x][y];
					} else if (camada instanceof CamadaReconstrucao){
						atualizaPesosCamada[x][y] /= linhasAnt*colunasAnt;
						pesosCamada[x][y] += atualizaPesosCamada[x][y];
					}
				}
			}
			
			camada.setPesosCamada(pesosCamada);
			//- - -
		}
		
	}
	
	private void zerarGradientes(RedeNeural rede) {
		for (Camada camada : rede.getCamadas()) {
			for(Neuronio[] neuronios : camada.getNeuronios()) {
				for(Neuronio neuronio : neuronios) {
					neuronio.zerarGradientes();
				}
			}
		}
	}
	
	private void zerarSensibilidades(RedeNeural rede){
		for (Camada camada : rede.getCamadas()) {
			for(Neuronio[] neuronios : camada.getNeuronios()) {
				for(Neuronio neuronio : neuronios) {
					neuronio.zerarSensibilidade();				
				}
			}	
		}
	}
	
	private double calcularErro(RedeNeural rede, List<double[][]> entradas, List<double[][]> saidas) {
		
		double erro = 0;
		int nSoma = 0;
		for (int a = 0; a < entradas.size(); a++) {
			double[][] entrada = entradas.get(a);
			double[][] saida = rede.estimular(entrada);
			double[][] saidaDesejada = saidas.get(a);
			for (int i = 0; i < entrada.length; i++) {
				for (int j = 0; j < entrada[i].length; j++) {
					erro += Math.abs(saidaDesejada[i][j] - saida[i][j]);
					nSoma++;
				}
			}
		}
		erro /= nSoma;
		
		return erro;
	}
	
	private void armazenaErrosTreinamento(RedeNeural rede, List<double[][]> entradas, List<double[][]> saidas){
		
		double erro = this.calcularErro(rede, entradas, saidas);
		this.errosTreinamento.add(erro);
	}
	
	private void armazenaErrosValidacao(RedeNeural rede, List<double[][]> entradas, List<double[][]> saidas){
		
		double erro = this.calcularErro(rede, entradas, saidas);
		this.errosValidacao.add(erro);
	}

	private void armazenaPesos(RedeNeural rede) {
		
		double[][][] pesosRede = new double[rede.getCamadas().length][rede.getTamCampo()][rede.getTamCampo()];
		double[] biasRede = new double[rede.getCamadas().length];
		
		Camada[] camadas = rede.getCamadas();
		for (int c = 0; c < camadas.length; c++ ) {
			Camada camada = rede.getCamadas()[c];
			
			biasRede[c] = camada.getBiasCamada();
			for (int i = 0; i < pesosRede[c].length; i++) {
				for (int j = 0; j < pesosRede[c][i].length; j++) {
					pesosRede[c][i][j] = camada.getPesosCamada()[i][j];
				}
			}
		}
		
		this.pesosTreinamento.add(pesosRede);
		this.biasTreinamento.add(biasRede);
		
	}
	
	private boolean pararTreinamento(int ciclos, double precisao){
		boolean parar = false;

		if(this.errosValidacao.size() >= ciclos){
			double erro = this.errosValidacao.get((errosValidacao.size()-ciclos)) - this.errosValidacao.get((errosValidacao.size()-1));
			if(erro < precisao){
				parar = true;
			}
		}
		
		return parar;
	}
	
//	private void imprimePesos(RedeNeural rede) {
//		Camada[] camadas = rede.getCamadas();
//		for (int c = 0; c < camadas.length; c++) {
//			Camada camada = camadas[c];
//			System.out.println("Camada: "+c);
//			System.out.println("Bias: "+ camada.getBiasCamada());
//			for(double[] pesos : camada.getPesosCamada()) {
//				for (double d : pesos) {
//					System.out.printf("%.2f ", d);
//				}
//				System.out.println();
//			}
//		}
//	}

	public List<double[]> getBiasTreinamento() {
		return biasTreinamento;
	}

	public List<double[][][]> getPesosTreinamento() {
		return pesosTreinamento;
	}

	public List<Double> getErrosTreinamento() {
		return errosTreinamento;
	}

	public List<Double> getErrosValidacao() {
		return errosValidacao;
	}

}
