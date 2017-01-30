package testes;

import rede.RedeNeural;
import treinamento.Resilientpropagation;

public class TesteRede {

	public static String caminho = "C:/Users/Hugo/OneDrive/teste/resultados/";
	
	public static void main(String[] args) throws Exception {
		
		RedeNeural rede = new RedeNeural(1, 1, 1, 1, 0);
		
		double[][] entradas = {{0}};
		double[][] saidas = {{1}};
		
		double[] pesos = {1, 1,
						1, 1};
		
		rede.setPesos(pesos);
		
		Resilientpropagation treinamento = new Resilientpropagation();
	
		for (int j = 0; j < 15; j++) {			
			treinamento.treinarExemplo(rede, entradas, saidas);
			treinamento.reajustarPesos(rede);

			treinamento.zerarSensibilidades(rede);
			rede.imprimePesos();
		}
		
	}
		
}
