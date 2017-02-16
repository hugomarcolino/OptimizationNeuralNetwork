package testes;

import rede.RedeNeural;
import treinamento.Resilientpropagation;

public class TesteRede {

	public static String caminho = "C:/Users/Hugo/OneDrive/teste/resultados/";
	
	public static void main(String[] args) throws Exception {
		
		double[][] entrada = {{0}};
		double[][] saida = {{1}};
		
		RedeNeural rede = new RedeNeural(1, 1, 1, 1, 0);
		
		//SETAR PESOS
		double[] pesos = {1, 1, 1, 1};
		
		rede.setPesos(pesos);
		//---
		
		Resilientpropagation rprop = new Resilientpropagation();
		
		for (int j = 0; j < 20; j++) {
			
			//rprop.treinarRede(rede, entrada, saida);
			//rprop.reajustarPesos(rede);

			//rprop.zerarSensibilidades(rede);
			
			//rede.imprimeGradientes();
			rede.imprimePesos();
		}
		
		
	}	
}