package titan.analysis1.AnalyseLexicale;

public class Lexique  {
	private UnitesLexicalesMotsCles ulmc;
	private Object lexeme;
	
	/* le constructeur de la classe lexique
	 * initialise les attributs de la classe par les type d'unité lexicale et
	 *le type de lexeme passe en parametre
	 * */
	public Lexique (UnitesLexicalesMotsCles unitelexicalemotcle, Object lexem) {
	this.ulmc=unitelexicalemotcle;
	this.lexeme=lexem;
	}
	// la méthode retournant le mot clé courant de la classe ou enumeration Unités lexicales mots clés
	public UnitesLexicalesMotsCles getulmc(){
		return  ulmc;
	}
	public Object getlexem(){
		return  lexeme;
	}
}
