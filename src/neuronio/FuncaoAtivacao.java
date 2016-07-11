package neuronio;

public class FuncaoAtivacao {

	public static double sigmoideLogistica(double x){
		return 1/(1+Math.pow(Math.E, -1*x));
	}
	
	public static double derivadaSigmoideLogistica(double y){
		return y*(1-y);
	}
	
	public static double tangenteHiperbolica(double x){
		return Math.tanh(x);
	}
	
	public static double derivadaTangenteHiperbolica(double x){
		return 1 - Math.pow(Math.tanh(x),2);
	}
	
}
