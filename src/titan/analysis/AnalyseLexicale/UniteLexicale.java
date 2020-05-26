package titan.analysis.AnalyseLexicale;

public class UniteLexicale {
	private Categorie ulmc;
	private Object lexeme;
	
	/* le constructeur de la classe lexique
	 * initialise les attributs de la classe par les type d'unité lexicale et 
	 *le type de lexeme passé en parametre 
	 * */
	public UniteLexicale(Categorie categorie, Object lexem) {
	this.ulmc= categorie;
	this.lexeme=lexem;
	}
	// la méthode retournant le mot clé courant de la classe ou enumeration Unités lexicales mots clés
	public Categorie getulmc(){
		return  ulmc;
	}
	public Object getlexem(){
		return  lexeme;
	}
}
