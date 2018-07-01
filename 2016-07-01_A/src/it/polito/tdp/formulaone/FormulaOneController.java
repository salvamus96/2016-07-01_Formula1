package it.polito.tdp.formulaone;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.formulaone.model.Driver;
import it.polito.tdp.formulaone.model.Model;
import it.polito.tdp.formulaone.model.Season;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FormulaOneController {
	
	Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<Season> boxAnno;

    @FXML
    private TextField textInputK;

    @FXML
    private TextArea txtResult;

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	this.txtResult.clear();
    	
    	try {
    		
    		Season s = this.boxAnno.getValue();
    		if (s == null) {
    			this.txtResult.appendText("Selezionare una stagione\n");
    			return;
    		}
    		
    		model.creaGrafo(s);
    		Driver d = this.model.bestDriver();
    		
    		if (d != null)
    			this.txtResult.appendText("Il pilota con il miglior risultato è " + d);
    		else
    			this.txtResult.appendText("ERRORE: creazione grafo!\n");
    		
    	}catch(RuntimeException e) {
    		e.printStackTrace();
    		this.txtResult.appendText("ERRORE: connessione DB\n!");
    	}
    }

    @FXML
    void doTrovaDreamTeam(ActionEvent event) {
    	this.txtResult.clear();
    	
    	try {
	    	
    		try {
    			
    			int k = Integer.parseInt(this.textInputK.getText());
    			if (k <= 0) {
    				this.txtResult.appendText("Inserire un valore numerico maggiore di 0!\n");
    				return;
    			}
   
    			List <Driver> dreamTeam = this.model.getDreamTeam(k);	
    			for (Driver d : dreamTeam)
    				this.txtResult.appendText(d + "\n");
    			
    			this.txtResult.appendText("Il minimo tasso di sconfitta è " + model.getMinTasso());
    			
    		}catch (NumberFormatException e) {
    			this.txtResult.appendText("Inserire un valore numerico maggiore di 0!\n");
				
    		}
    	
    		
		}catch(RuntimeException e) {
			e.printStackTrace();
			this.txtResult.appendText("ERRORE: connessione DB\n!");
		}
    
    }

    @FXML
    void initialize() {
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'FormulaOne.fxml'.";
        assert textInputK != null : "fx:id=\"textInputK\" was not injected: check your FXML file 'FormulaOne.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'FormulaOne.fxml'.";

    }
    
    public void setModel(Model model){
    	this.model = model;
    	
    	this.boxAnno.getItems().addAll(model.getAllSeason());
    }
}
