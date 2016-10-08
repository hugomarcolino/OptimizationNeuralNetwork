//  Rastrigin.java
//
//  Author:
//       Esteban López-Camacho <esteban@lcc.uma.es>
//
//  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU Lesser General Public License for more details.
//
//  You should have received a copy of the GNU Lesser General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.

package jmetal.problems.singleObjective;

import java.util.ArrayList;
import java.util.List;

import rede.RedeNeural;
import smile.math.DoubleArrayList;
import smile.math.distance.EuclideanDistance;
import smile.math.distance.JensenShannonDistance;
import smile.math.distance.MahalanobisDistance;
import smile.math.distance.ManhattanDistance;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.Variable;
import jmetal.encodings.solutionType.BinaryRealSolutionType;
import jmetal.encodings.solutionType.RealSolutionType;
import jmetal.util.JMException;

public class RedeNeuralProblem extends Problem {
	
	List<double[][]> entradas;
	List<double[][]> saidas;
	RedeNeural rede;

  /**
   * Constructor
   * Creates a default instance of the Rastrigin problem
   * @param numberOfVariables Number of variables of the problem
   * @param solutionType The solution type must "Real" or "BinaryReal".
   */
  public RedeNeuralProblem(String solutionType, Integer numberOfVariables)  throws ClassNotFoundException {
    numberOfVariables_   = numberOfVariables ;
    numberOfObjectives_  = 1;
    numberOfConstraints_ = 0;
    problemName_         = "RedeNeural";

    upperLimit_ = new double[numberOfVariables_];
    lowerLimit_ = new double[numberOfVariables_];
    for (int var = 0; var < numberOfVariables_; var++){
      lowerLimit_[var] = -5;
      upperLimit_[var] = 5;
    } // for

    if (solutionType.compareTo("BinaryReal") == 0)
      solutionType_ = new BinaryRealSolutionType(this) ;
    else if (solutionType.compareTo("Real") == 0)
      solutionType_ = new RealSolutionType(this) ;
    else {
      System.out.println("Error: solution type " + solutionType + " invalid") ;
      System.exit(-1) ;
    }

  }

  /**
   * Evaluates a solution
   * @param solution The solution to evaluate
   * @throws jmetal.util.JMException
   */
  public void evaluate(Solution solution) throws JMException {
    Variable[] decisionVariables  = solution.getDecisionVariables();

    double result = 0.0;
    
    double [] x = new double[numberOfVariables_];

    for (int i = 0; i < numberOfVariables_; i++) {
      x[i] = decisionVariables[i].getValue() ;
    }

    result = evaluateRede(x);
    
    solution.setObjective(0, result);
  } // evaluate

  
  public double evaluateRede(double[] position) { 
		
	  int  p = 0;
		
		List<Double> biasCamada = new ArrayList<Double>();
		List<double[][]> pesosCamadas = new ArrayList<double[][]>();
		
		for (int c = 0; c < rede.getCamadas().length; c++) {			
			double[][] pesosCamada = new double[rede.getTamCampo()][rede.getTamCampo()];
			
			for (int i = 0; i < pesosCamada.length; i++) {
				for (int j = 0; j < pesosCamada.length; j++) {
					pesosCamada[i][j] = position[p++]; 
				}
			}
			pesosCamadas.add(pesosCamada);
		}
		
		for (int c = 0; c < rede.getCamadas().length; c++) {			
			biasCamada.add(position[p++]);
		}
		
		for (int c = 0; c < rede.getCamadas().length; c++) {
			rede.getCamadas()[c].setPesosCamada(pesosCamadas.get(c));
			rede.getCamadas()[c].setBiasCamada(biasCamada.get(c));
		}
		//---
		
		//CALCULAR ERRO
		double erro = calcularErroDistancia(rede, entradas, saidas);
		//---
		
		return erro;
	}
	
	private double calcularErro(RedeNeural rede, List<double[][]> entradas, List<double[][]> saidas) {
		
		double erro = 0;
		int nSoma = 0;
		for (int imagem = 0; imagem < entradas.size(); imagem++) {
			double[][] entrada = entradas.get(imagem);
			double[][] saidaDesejada = saidas.get(imagem);

			double[][] saida = rede.estimular(entrada);
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

	private double calcularErroDistancia(RedeNeural rede, List<double[][]> entradas, List<double[][]> saidas) {
		
		double erro = 0;
		
		DoubleArrayList saidaArray = new DoubleArrayList();
		DoubleArrayList saidaRedeArray = new DoubleArrayList();
		
		for (int a = 0; a < entradas.size(); a++) {
			double[][] entrada = entradas.get(a);
			double[][] saida = saidas.get(a);
			
			double[][] saidaRede = rede.estimular(entrada);
			
			for (int i = 0; i < entrada.length; i++) {
				saidaArray.add(saida[i]);
				saidaRedeArray.add(saidaRede[i]);
			}
		}
		
		EuclideanDistance ed = new EuclideanDistance();
		erro = ed.d(saidaArray.toArray(), saidaRedeArray.toArray());
		
//		ManhattanDistance md = new ManhattanDistance();
//		erro = md.d(saidaArray.toArray(), saidaRedeArray.toArray());
		
		return erro;
	}

	public List<double[][]> getEntradas() {
		return entradas;
	}

	public void setEntradas(List<double[][]> entradas) {
		this.entradas = entradas;
	}

	public List<double[][]> getSaidas() {
		return saidas;
	}

	public void setSaidas(List<double[][]> saidas) {
		this.saidas = saidas;
	}

	public RedeNeural getRede() {
		return rede;
	}

	public void setRede(RedeNeural rede) {
		this.rede = rede;
	}
	
} 

