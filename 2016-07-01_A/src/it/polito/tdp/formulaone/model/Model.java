package it.polito.tdp.formulaone.model;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.formulaone.db.FormulaOneDAO;

public class Model {

	private FormulaOneDAO fdao;
	
	private List <Season> seasons;
	
	private Graph <Driver, DefaultWeightedEdge> graph;
	
	private List <Driver> soluzione;
	private int best;
	
	private List <Driver> drivers;
	private DriverIdMap map;
	
	public Model () {
		
		this.fdao = new FormulaOneDAO();
		this.map = new DriverIdMap();
		
		
	}

	public List<Season> getAllSeason() {
		return fdao.getAllSeasons();
	}

	public void creaGrafo(Season s) {
		
		this.graph = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		// Caricamento vertici
		this.drivers = this.fdao.getAllDrivers(s, map);
		Graphs.addAllVertices(this.graph, drivers);
		
		// Caricamento archi
		List <Arco> archi = fdao.getAllEdge(s, map);
		for (Arco a : archi)
			Graphs.addEdgeWithVertices(this.graph, a.getPartenza(), a.getArrivo(), a.getPeso());
			
		System.out.println(this.graph.vertexSet().size() + " " + this.graph.edgeSet().size());
	
	}

	public Driver bestDriver () {
		Driver bestDriver = null;
		int result = Integer.MIN_VALUE;
		
		for (Driver d : this.drivers) {
			int sum = 0;
			 
			// itero sugli archi uscenti
			for (DefaultWeightedEdge e : this.graph.outgoingEdgesOf(d))
				sum += this.graph.getEdgeWeight(e);
			
			// itero sugli archi entranti
			for (DefaultWeightedEdge e : this.graph.incomingEdgesOf(d))
				sum -= this.graph.getEdgeWeight(e);
			
			if (sum > result) {
				bestDriver = d;
				result = sum;
			}
		}
		return bestDriver;	
	}
	
	
	public List<Driver> getDreamTeam(int k){
		this.soluzione = new ArrayList<>();
		this.best = Integer.MAX_VALUE;
		
		List <Driver> parziale = new ArrayList<>();
		
		this.ricorsiva (k, 0, parziale);
		
		return soluzione;
	}

	private void ricorsiva(int k, int livello, List<Driver> parziale) {
		System.out.println(parziale + " " + this.calcoloTasso(parziale));
		
		// condizione di terminazione
		if (livello >= k) {
			if (this.calcoloTasso (parziale) < best) {
				this.soluzione = new ArrayList<>(parziale);
				this.best = this.calcoloTasso (parziale);
			}
			// fondamentale altrimenti esplora altre soluzione di lunghezza maggiore
			return;
		}
		
		// generazione soluzione parziale
		for (Driver d : this.graph.vertexSet())
			if (!parziale.contains(d)) {
				parziale.add(d);
				this.ricorsiva(k, livello + 1, parziale);
				parziale.remove(d);
			}
		
	}

	/**
	 * Calcolo del TASSO DI SCONFITTA: numero di vittorie di un nonDream su un Dream
	 * IL NUMERO DI VITTORIE E' RAPPRESENTATO DAL PESO DELL'ARCO
	 * il tasso è la somma delle vittorie di ogni nonDream su ogni Dream
	 * @param parziale
	 * @return
	 */
	
	private int calcoloTasso(List<Driver> parziale) {
	
		// lista dei piloti nonDream
		List<Driver> nonDreamTeam = new ArrayList<>(drivers);
		nonDreamTeam.removeAll(parziale);
		
		int tasso = 0;
		
		// occorre lavorare sugli arco perchè si vuole sommare il peso
		for (DefaultWeightedEdge e : this.graph.edgeSet()) {
			
			// trovare i vertici di uno specifico arco
			Driver partenzaNonDream = this.graph.getEdgeSource(e);
			Driver destinazioneDream = this.graph.getEdgeTarget(e);
			
			// se l'arco tra i due vertici è l'arco desiderato
			// (cioè collega un nonDream a un dream)
			if (nonDreamTeam.contains(partenzaNonDream) && parziale.contains(destinazioneDream))
				
				tasso += this.graph.getEdgeWeight(e);
		}
		
		return tasso;
	}

	public int getMinTasso() {
		return best;
	}
}
