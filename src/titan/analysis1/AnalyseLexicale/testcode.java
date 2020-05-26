package titan.analysis1.AnalyseLexicale;

public class testcode {
	public static void main(String[] args) {
		Scanners anaLex=new Scanners("test1.txt");
		Parsers parser=new Parsers();
		cible cib=new cible();
		parser.lireProduction();
		parser.setNullable();
		parser.Premiercalcul();
		parser.calculSuivant();
		parser.calculPremierRegles();
		parser.remplirTableAnalyse();
		parser.afficher();
		parser.initialiserPile();
		System.out.println(anaLex);
		Lexique ul=null;
		String valeurC=null;
		int i=0;
		boolean b=true;
		while(b){
			ul=anaLex.lexemeSuivant();
			
			if(ul.getulmc().equals(UnitesLexicalesMotsCles.EOF)){
				System.out.println(" l'element avant d'entree dans le parser est :"+ul.getulmc());
				anaLex.lexemeSuivant();
				System.out.println("affiche les elements de ul rencontre");
				System.out.println("le fichier touche a sa fin et la syntaxe est correcte !!!");
				b=false;
			}
			else	{	
				//System.out.println(" l'�l�ment avant d'entree dans le parser est :"+ul.getulmc());
				parser.analyserSyntaxe(ul.getulmc());
				System.out.println("affiche les elements de ul rencontres");
				System.out.println(ul.getulmc());
				//System.out.println("c'est le tours de lexeme"+ul.getlexem());
				
				cib.tablelexmc(ul.getlexem().toString());
				
			}
		}
		
		cib.cl_valeur();
		valeurC=cib.cpc();
		cib.affichage("affiche sa aussi 0902281075 ");
		System.out.println("l'equivalent pascal en c est : ");
		System.out.println("............................................ ");
		System.out.println(valeurC);
		
		
		}
		
	
		
			
	}



