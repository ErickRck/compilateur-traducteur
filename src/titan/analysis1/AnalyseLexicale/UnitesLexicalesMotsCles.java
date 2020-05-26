 package titan.analysis1.AnalyseLexicale;

// la classe �num�ration qui permet de regrouper les objets de m�me type
public enum  UnitesLexicalesMotsCles   {
	// les non terminaux
	EOF,
	NUL,
	PROGRAM,
	ID,
	PTV,
	EGALE,
	NOMBRE,
	PLUS,
	MOINS,
	VAR,
	DEUXPT,
	VIRG,
	BEGIN,
	END,
	AFFECT,
	READ,
	WRITE,
	IF,
	THEN,
	ELSE,
	PARENTOUV,
	APOSTROPHE,
	TYPEPRIMITIVE,
	ARRAY,
	DIFFERENT,
	INFEREGALE,
	SUPEGALE,
	INFERIEUR,
	SUPERIEUR,
	CROCHETOUV,
	CROCHETFERM,
	OF,
	FOR,
	WHILE,
	TO,
	DO,
	DOWNTO,
	PTS,
	PARENTFERM,
	CHAINE,
	OR,
	FOIS,
	DIVISION,
	DIV,
	MOD,
	AND,
	PT,
	
	// les terminaux
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
	
	// les m�thodes de l'�numeration
	public static final int indexmin=2, indexmax=46,indexmax1=79;
	/* la m�thode to string convertit tous les terminaux et non terminaux
	 * minuscule de la grammaire en majuscule 
	 * */
	public String toString () {
	return this.name().toLowerCase();	

}
	// nous parcourons tous les Elements de l'enumeration
	public static UnitesLexicalesMotsCles toUnitesLexicalesMotsCles (String s){
		for (UnitesLexicalesMotsCles Ulmc: UnitesLexicalesMotsCles.values())
			// la methode equalsIgnoreCase compare les chaines sans tenir compte de la casse
			if (Ulmc.toString().equalsIgnoreCase(s))
				return Ulmc;
		return null;
		
	}
	/* le mot cle ordinal renvoie la position de l'element de l'�numeration
	 * en commentant par 0 donc mes methodes sont � modifier selon l'ordre des �l�ment que nous allons
	 * plac� dans l'�numeration
	 * */
	public boolean estTerminal() {
		return ordinal()>=indexmin && ordinal()<=indexmax;
	}
	
	public boolean estNonTerminal() {
		return ordinal()>indexmax;
	}
}
