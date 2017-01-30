//  gGA.java
//
//  Author:
//       Antonio J. Nebro <antonio@lcc.uma.es>
//       Juan J. Durillo <durillo@lcc.uma.es>
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

package jmetal.metaheuristics.singleObjective.geneticAlgorithm;

import jmetal.core.*;
import jmetal.util.JMException;
import jmetal.util.comparators.ObjectiveComparator;

import java.util.Comparator;

/** 
 * Class implementing a generational genetic algorithm
 */
public class gGA extends Algorithm {

	private static final long serialVersionUID = 1L;

	/**
	 *
	 * Constructor
	 * Create a new GGA instance.
	 * @param problem Problem to solve.
	 */
	public gGA(Problem problem){
		super(problem) ;
	} // GGA
	
	public SolutionSet initPopulation() throws ClassNotFoundException, JMException{

		int populationSize = ((Integer)this.getInputParameter("populationSize")).intValue();
		
		SolutionSet population = new SolutionSet(populationSize) ;
		// Create the initial population
		Solution newIndividual;
		for (int i = 0; i < populationSize; i++) {
			newIndividual = new Solution(problem_);                    
			problem_.evaluate(newIndividual);            
			population.add(newIndividual);
		} //for       

		return population;
	}

	public SolutionSet iteration(SolutionSet population) throws JMException, ClassNotFoundException {
		int populationSize = ((Integer)this.getInputParameter("populationSize")).intValue(); 

		SolutionSet offspringPopulation = new SolutionSet(populationSize) ;

		Operator    mutationOperator  ;
		Operator    crossoverOperator ;
		Operator    selectionOperator ;

		Comparator comparator = new ObjectiveComparator(0) ; // Single objective comparator

		// Read the operators
		mutationOperator  = this.operators_.get("mutation");
		crossoverOperator = this.operators_.get("crossover");
		selectionOperator = this.operators_.get("selection");         

		// Copy the best two individuals to the offspring population
		offspringPopulation.add(new Solution(population.get(0))) ;	
		offspringPopulation.add(new Solution(population.get(1))) ;	

		// Reproductive cycle
		for (int i = 0 ; i < (populationSize / 2 - 1) ; i ++) {
			// Selection
			Solution [] parents = new Solution[2];

			parents[0] = (Solution)selectionOperator.execute(population);
			parents[1] = (Solution)selectionOperator.execute(population);

			// Crossover
			Solution [] offspring = (Solution []) crossoverOperator.execute(parents);                

			// Mutation
			mutationOperator.execute(offspring[0]);
			mutationOperator.execute(offspring[1]);

			// Evaluation of the new individual
			problem_.evaluate(offspring[0]);            
			problem_.evaluate(offspring[1]);            

			// Replacement: the two new individuals are inserted in the offspring
			//                population
			offspringPopulation.add(offspring[0]) ;
			offspringPopulation.add(offspring[1]) ;
		} // for

		// The offspring population becomes the new current population
		population.clear();
		for (int i = 0; i < populationSize; i++) {
			population.add(offspringPopulation.get(i)) ;
		}
		
		offspringPopulation.clear();
		population.sort(comparator) ;

		return population ;
	}

} // gGA