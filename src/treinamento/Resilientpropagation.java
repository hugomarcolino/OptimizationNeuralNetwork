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

	final double deltaMax = 50;
	final double deltaMin = 1e-6;
	final double nPos = 1.2;
	final double nNeg = 0.5;
	
	private List<double[]> biasTreinamento = new ArrayList<double[]>();
	private List<double[][][]> pesosTreinamento = new ArrayList<double[][][]>();
	
	private List<Double> errosTreinamento = new ArrayList<Double>();
	private List<Double> errosValidacao = new ArrayList<Double>();
	
	
	//REFAZER
	public RedeNeural treinamentoRede(RedeNeural rede, List<double[][]> entradasTreino, List<double[][]> saidasTreino, 
			List<double[][]> entradasValidacao, List<double[][]> saidasValidacao ){

		List<Integer> ordemTreino = new ArrayList<Integer>();
		for (int i = 0; i < entradasTreino.size(); i++) {
			ordemTreino.add(i);
		}
		
		int ciclo = 0;
		
		while ( this.validacaoCruzada(50, 0.0) ) {
			System.out.println("Ciclo: "+ciclo++);
			Collections.shuffle(ordemTreino);
			
			for (int j = 0; j < ordemTreino.size(); j++) {
				int nImagem = ordemTreino.get(j);
				System.out.println("Treinando.. Imagem "+(nImagem+1));
				
				this.treinarExemplo(rede, entradasTreino.get(nImagem), saidasTreino.get(nImagem));
				this.reajustarPesos(rede);

				this.zerarSensibilidades(rede);
				this.armazenaPesos(rede);
			}
			
			this.armazenaErrosTreinamento(rede, entradasTreino, saidasTreino);
			this.armazenaErrosValidacao(rede, entradasValidacao, saidasValidacao);
		}

		return rede;
	}

	public void treinarExemplo(RedeNeural rede, double[][] entradas, double[][] saidas){ 
		
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
					double sensibilidade;
					if(c == camadas.length-1){
						sensibilidade =  saidas[i][j] - saidasRede[i][j];
					} else {
						sensibilidade = neuronio.getSensibilidade();
					}
					sensibilidade *= neuronio.estimularDerivadaFuncao();
					neuronio.setSensibilidade(sensibilidade);
					//---
					
					//RETROPROPAGAR SENSIBILIDADE
					if(c > 0){
						for (int a=0, x = iMin; x <= iMax; x++, a++) {
							for (int b=0, y = jMin; y <= jMax; y++, b++) {
								Neuronio neuronioAnterior = camadaAnterior.getNeuronios()[x][y];
								double sensibilidadeAnterior = neuronioAnterior.getSensibilidade() + neuronio.getSensibilidade()*pesos[a][b];
								neuronioAnterior.setSensibilidade(sensibilidadeAnterior);
							}
						}
					}
					//--
					
					//CALCULAR GRADIENTES
					double gradienteBias = neuronio.getSensibilidade()*neuronio.getBias();
					neuronio.setGradienteBias(gradienteBias);
					
					double[][] entradasCamada = c > 0 ? camadaAnterior.getSaidasCamada() : entradas;
					double[][] entradasNeuronio = camada.getEntradasNeuronio(i, j, entradasCamada);
					
					double[][] gradientes = new double[pesos.length][pesos[0].length];
					for (int x = 0; x < gradientes.length; x++) {
						for (int y = 0; y < gradientes[x].length; y++) {
							gradientes[x][y] = neuronio.getSensibilidade()*entradasNeuronio[x][y];
						}
					}
					neuronio.setGradientes(gradientes);
					//---
					
					//CALCULAR DELTAS
					this.updateBias(neuronio);
					
					for (int a=0, x = iMin; x <= iMax; x++, a++) {
						for (int b=0, y = jMin; y <= jMax; y++, b++) {
							this.updatePeso(neuronio, a, b);
						}
					}
					//---
				}
			}
		}
	}
	
	private void updatePeso(Neuronio neuronio, final int x, final int y) {

		double[][] gradientes = neuronio.getGradientes();
		double[][] gradientesAnterior = neuronio.getGradientesAnterior();
		
		final double sinal = Math.signum(gradientes[x][y] * gradientesAnterior[x][y]);
		double deltaPeso = 0;

		double[][] deltas = neuronio.getDeltas();
		
		if (sinal > 0) {
			double delta = deltas[x][y] * nPos;
			delta = Math.min(delta, this.deltaMax);
			deltaPeso = Math.signum(gradientes[x][y]) * delta;
			deltas[x][y] = delta;
			gradientesAnterior[x][y] = gradientes[x][y];
		} else if (sinal < 0) {
			double delta = deltas[x][y] * nNeg;
			delta = Math.max(delta, deltaMin);
			deltas[x][y] = delta;
			deltaPeso = - neuronio.getDeltasPeso()[x][y];
			gradientesAnterior[x][y] = 0;
		} else if (sinal == 0) {
			final double delta = deltas[x][y];
			deltaPeso = Math.signum(gradientes[x][y]) * delta;
			gradientesAnterior[x][y] = gradientes[x][y];
		}

		neuronio.getDeltasPeso()[x][y] = deltaPeso;
	}
	
	
	private void updateBias(Neuronio neuronio) {

		double gradienteBias = neuronio.getGradienteBias();
		double gradienteBiasAnterior = neuronio.getGradienteBiasAnterior();
		
		final double sinal = Math.signum(gradienteBias * gradienteBiasAnterior);
		double deltaPesoBias = 0;

		double deltaBias = neuronio.getDeltaBias();
		
		if (sinal > 0) {
			double delta = deltaBias * nPos;
			delta = Math.min(delta, this.deltaMax);
			deltaPesoBias = Math.signum(gradienteBias) * delta;
			neuronio.setDeltaBias(delta);
			neuronio.setGradienteBiasAnterior(neuronio.getGradienteBias());
		} else if (sinal < 0) {
			double delta = deltaBias * nNeg;
			delta = Math.max(delta, deltaMin);
			neuronio.setDeltaBias(delta);
			deltaPesoBias = - neuronio.getDeltaPesoBias();
			neuronio.setGradienteBiasAnterior(0);
		} else if (sinal == 0) {
			final double delta = deltaBias;
			deltaPesoBias = Math.signum(gradienteBias) * delta;
			neuronio.setGradienteBiasAnterior(neuronio.getGradienteBias());
		}

		neuronio.setDeltaPesoBias(deltaPesoBias);
	}
	
	public void reajustarPesos(RedeNeural rede){
		
		//REAJUSTAR PESOS
		Camada[] camadas = rede.getCamadas();
		
		for (int c = 0; c < camadas.length; c++) {
			Camada camada = camadas[c];
			
			//BIAS
			double pesoBiasCamada = 0;
			double atualizaPesoBiasCamada = 0;
			
			for (int i = 0; i < camada.getLinhas(); i++) {
				for (int j = 0; j < camada.getColunas(); j++) {
					Neuronio neuronio = camada.getNeuronios()[i][j];
					atualizaPesoBiasCamada += neuronio.getDeltaPesoBias(); 
				}
			}
			
			atualizaPesoBiasCamada /= camada.getLinhas()*camada.getColunas();
			pesoBiasCamada = camada.getBiasCamada() + atualizaPesoBiasCamada;
			
			camada.setBiasCamada(pesoBiasCamada);
			
			//PESOS
			double[][] pesosCamada = new double[camada.getPesosCamada().length][camada.getPesosCamada()[0].length];
			double[][] atualizaPesosCamada = new double[camada.getPesosCamada().length][camada.getPesosCamada()[0].length];
			
			int tamSobreposicao = camada.getTamSobreposicao();
			int tamCampo = camada.getTamCampo();
			
			int linhasAnt = (camada.getLinhas() - tamSobreposicao)/(tamCampo-tamSobreposicao);
			int colunasAnt = (camada.getColunas() - tamSobreposicao)/(tamCampo-tamSobreposicao);
			
			int gap = tamCampo-tamSobreposicao;
			
			for (int i = 0; i < camada.getLinhas(); i++) {
				for (int j = 0; j < camada.getColunas(); j++) {
					Neuronio neuronio = camada.getNeuronios()[i][j];
					
					int[] posicoes = camada.getPosicoesAnterior(i, j);					
					int iMin = posicoes[0];
					int iMax = posicoes[1];
					int jMin = posicoes[2];
					int jMax = posicoes[3];					
					
					for (int a=0, x = iMin; x <= iMax; x++, a++) {
						for (int b=0, y = jMin; y <= jMax; y++, b++) {
							if(camada instanceof CamadaConvolucao) {
								atualizaPesosCamada[x - i*gap][y - j*gap] += 
										neuronio.getDeltasPeso()[a][b];
							} else if(camada instanceof CamadaReconstrucao){
								atualizaPesosCamada[i - x*gap][j - y*gap] += 
										neuronio.getDeltasPeso()[a][b];
							}
						}
					}
							
				}
			}
						
			for (int i = 0; i < atualizaPesosCamada.length; i++) {
				for (int j = 0; j < atualizaPesosCamada[i].length; j++) {
					if(camada instanceof CamadaConvolucao){						
						atualizaPesosCamada[i][j] /= camada.getLinhas()*camada.getColunas();
					} else if(camada instanceof CamadaReconstrucao){
						atualizaPesosCamada[i][j] /= linhasAnt*colunasAnt;
					}
					pesosCamada[i][j] = camada.getPesosCamada()[i][j] + atualizaPesosCamada[i][j];
				}
			}
			
			camada.setPesosCamada(pesosCamada);
			//---
			
			//ATUALIZACAO
			
			for (int i = 0; i < camada.getLinhas(); i++) {
				for (int j = 0; j < camada.getColunas(); j++) {
					Neuronio neuronio = camada.getNeuronios()[i][j];
					neuronio.setDeltaPesoBias(atualizaPesoBiasCamada); 
					
					int[] posicoes = camada.getPosicoesAnterior(i, j);					
					int iMin = posicoes[0];
					int iMax = posicoes[1];
					int jMin = posicoes[2];
					int jMax = posicoes[3];	
					
					for (int a=0, x = iMin; x <= iMax; x++, a++) {
						for (int b=0, y = jMin; y <= jMax; y++, b++) {
							if(camada instanceof CamadaConvolucao) {
								neuronio.getDeltasPeso()[a][b] = atualizaPesosCamada[x - i*gap][y - j*gap]; 
							} else if(camada instanceof CamadaReconstrucao){
								neuronio.getDeltasPeso()[a][b] = atualizaPesosCamada[i - x*gap][j - y*gap];
							}
						}
					}
				}
			}
			
			
			//---
		}
	
	}

	public void zerarSensibilidades(RedeNeural rede){
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
	
	private boolean validacaoCruzada(int ciclos, double precisao){
		boolean treina = true;

		if (this.errosValidacao.size() >= ciclos) {
			double erro = this.errosValidacao.get((errosValidacao.size()-ciclos)) - this.errosValidacao.get((errosValidacao.size()-1));
			if(erro < precisao){
				treina = false;
			}
		}
		
		return treina;
	}
	
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
