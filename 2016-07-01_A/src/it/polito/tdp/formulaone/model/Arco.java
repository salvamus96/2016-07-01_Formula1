package it.polito.tdp.formulaone.model;

public class Arco {
	
	private Driver partenza;
	private Driver arrivo;
	
	private int peso;

	public Arco(Driver partenza, Driver arrivo, int peso) {
		super();
		this.partenza = partenza;
		this.arrivo = arrivo;
		this.peso = peso;
	}

	public Driver getPartenza() {
		return partenza;
	}

	public void setPartenza(Driver partenza) {
		this.partenza = partenza;
	}

	public Driver getArrivo() {
		return arrivo;
	}

	public void setArrivo(Driver arrivo) {
		this.arrivo = arrivo;
	}

	public int getPeso() {
		return peso;
	}

	public void setPeso(int peso) {
		this.peso = peso;
	}
	
	

}
