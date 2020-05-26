 package titan.analysis.AnalyseLexicale;

// la classe énumération qui permet de regrouper les objets de même type
public enum Categorie {
	// les terminaux
	EOF,
	NUL,
	ALGORITHME,
	ID,
	PTV,
	EGALE,
	NOMBRE,
	PLUS,
	MOINS,
	VAR,
	DEUXPT,
	VIRG,
	DEBUT,
	FIN,
	AFFECT,
	LIRE,
	ECRIRE,
	SI,
	ALORS,
	SINON,
	PARENTOUV,
	APOSTROPHE,
	TYPEPRIMITIVE,
	TABLEAU,
	DIFFERENT,
	INFEREGALE,
	SUPEGALE,
	INFERIEUR,
	SUPERIEUR,
	CROCHETOUV,
	CROCHETFERM,
	FINSI,
	POUR,
	TANTQUE,
	A,
	FAIRE,
	JUSQUA,
	PTS,
	PARENTFERM,
	CHAINE,
	OU,
	FOIS,
	DIVISION,
	DIV,
	MOD,
	ET,
	PT,
	
	// les non-terminaux
	PROGRAMME,
	BLOCK,
	CONSTDEFPART,
	CONSTDEF,
	CONST,
	SIGN,
	VARDECPART,
	VARDECPARTP,
	VARDEC,
	VARDECP,
	TYPE,
	STATEMENTPART,
	STATEMENTPARTP,
	STATEMENTCOMPAR,
	STATEMENTCOMPARP,
	STATEMENT,
	STATEMENTIF,
	STATEMENTELSE,
	STATEMENTFOR,
	STATEMENT1,
	STATEMENT2,
	STATEMENT3,
	EXPRESSION,
	EXPRESSIONP,
	IDENTRP,
	IDSORT,
	IDSORT1,
	IDSORTP,
	OPER,
	OPERCOMPAR,
	TERM,
	COMPAR,
	FACTOR;
	
	// les méthodes de l'énumeration
	public static final int indexmin=2, indexmax=46,indexmax1=79;
	/* la méthode to string convertit tous les terminaux et non terminaux
	 * minuscule de la grammaire en majuscule 
	 * */
	public String toString () {
	return this.name().toLowerCase();	

}
	// nous parcourons tous les élements de l'enumeration 
	public static Categorie toCatégorie (String s){
		for (Categorie Ulmc: Categorie.values())
			// la méthode equalsIgnoreCase compare les chaines sans tenir compte de la casse
			if (Ulmc.toString().equalsIgnoreCase(s))
				return Ulmc;
		return null;
		
	}
	/* le mot clé ordinal renvoie la position de l'élement de l'énumeration
	 * en commençant par 0 donc mes méthodes sont à modifier selon l'ordre des élément que nous allons 
	 * placé dans l'énumeration
	 * */
	public boolean estTerminal() {
		return ordinal()>=indexmin && ordinal()<=indexmax;
	}
	
	public boolean estNonTerminal() {
		return ordinal()>indexmax;
	}
}
