package titan.analysis.AnalyseLexicale;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class  Scanner {
	private ArrayList<Character> fluxCaracteres;
	private int indiceCourant;
	private char caractereCourant; 
	private boolean eof;
	
	public Scanner(String nomFich) {
		BufferedReader f=null;
		int car=0;
		fluxCaracteres=new ArrayList<Character>();
		indiceCourant=0;
		eof=false;
		try {
			f=new BufferedReader(new FileReader(nomFich));
			System.out.println("ça marche");
		}
		catch(IOException e) {
			System.out.println("taper votre texte ci-dessous (ctrl+z pour finir)");
			f=new BufferedReader(new InputStreamReader(System.in));
		}
		
		try {
			while((car=f.read())!=-1)
				fluxCaracteres.add((char)car);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void caractereSuivant() {
		if(indiceCourant<fluxCaracteres.size())
			caractereCourant=fluxCaracteres.get(indiceCourant++);
		else
			eof=true;
	}
	
	public void reculer() {
		if(indiceCourant>0)
			indiceCourant--;
	}
	
	public UniteLexicale lexemeSuivant() {
		int signal;
		caractereSuivant();
		while(eof || Character.isWhitespace(caractereCourant)) { 
			if (eof){
				//System.out.println("le lexeme est :"+Categorie.EOF.toString());
				return new UniteLexicale(Categorie.EOF, "");
				}
			caractereSuivant();
		}
		if(Character.isLetter(caractereCourant)){
				return getMotcleOrId();
		}
		if(caractereCourant==';'){
			System.out.println("le lexeme est :"+ Categorie.PTV.toString());
			return new UniteLexicale(Categorie.PTV, ";");
			
		}
		if(caractereCourant=='('){
			System.out.println("le lexeme est :"+ Categorie.PARENTOUV.toString());
			return new UniteLexicale(Categorie.PARENTOUV, "(");
			
		}
		if(caractereCourant==')'){
			System.out.println("le lexeme est :"+ Categorie.PARENTFERM.toString());
			return new UniteLexicale(Categorie.PARENTFERM, ")");
			
		}
		if(caractereCourant=='['){
			System.out.println("le lexeme est :"+ Categorie.CROCHETOUV.toString());
			return new UniteLexicale(Categorie.CROCHETOUV, "[");
			
		}
		if(caractereCourant==']'){
			System.out.println("le lexeme est :"+ Categorie.CROCHETFERM.toString());
			return new UniteLexicale(Categorie.CROCHETFERM, "]");
			
		}
		if(caractereCourant=='\''){
			System.out.println("le lexeme est :"+ Categorie.APOSTROPHE.toString());
			return new UniteLexicale(Categorie.APOSTROPHE, "'");
			
		}
		/*if(caractereCourant=='('){
			System.out.println("le lexeme est :"+Categorie.PARENOUV.toString());
			return new UniteLexicale(Categorie.PARENOUV, "(");
			
		}
		if(caractereCourant==')'){
			System.out.println("le lexeme est :"+Categorie.PARENFER.toString());
			return new UniteLexicale(Categorie.PARENFER, ")");
			
		}*/
		if(caractereCourant=='='){
			//System.out.println("le lexeme est :"+Categorie.EGALE.toString());
			return new UniteLexicale(Categorie.EGALE, "=");
			}
		if(caractereCourant==':'){
			StringBuffer sb=new StringBuffer();
			sb.append(caractereCourant);
			caractereSuivant();
			if(caractereCourant=='='){
				sb.append(caractereCourant);
				//System.out.println("le lexeme est :"+Categorie.AFFECT.toString());
				return new UniteLexicale(Categorie.AFFECT, ":=");
			}
			else{
				//System.out.println("le lexeme est :"+Categorie.DEUXPT.toString());
				return new UniteLexicale(Categorie.DEUXPT, ":");
			}
		}
		
		if(caractereCourant==','){
			//System.out.println("le lexeme est :"+Categorie.VIRG.toString());
			return new UniteLexicale(Categorie.VIRG, ",");
			}

		if(caractereCourant=='.'){
			String str="";
			StringBuffer sb=new StringBuffer();
			sb.append(caractereCourant);
			 
			 str=sb.toString();
			 if(str.equalsIgnoreCase("..")){
				
				System.out.println("le lexeme est :"+ Categorie.PTS.toString());
				return new UniteLexicale(Categorie.PTS, ".");
			}
			if(str.equalsIgnoreCase(".")){
					System.out.println("le lexeme est :"+ Categorie.PT.toString());
					return new UniteLexicale(Categorie.PT, ".");
			}
				
		
		
	}
		if(caractereCourant=='<'){
			StringBuffer sb=new StringBuffer();
			sb.append(caractereCourant);
			caractereSuivant();
			if(caractereCourant=='>'){
				sb.append(caractereCourant);
				//System.out.println("le lexeme est :"+Categorie.DIFFERENT.toString());
				return new UniteLexicale(Categorie.DIFFERENT, "<>");
			}
			else if(caractereCourant=='='){
				//System.out.println("le lexeme est :"+Categorie.PT.toString());
				return new UniteLexicale(Categorie.INFEREGALE, "<=");
			}
			else {
				//System.out.println("le lexeme est :"+Categorie.PT.toString());
				return new UniteLexicale(Categorie.INFERIEUR, "<");
			}
		}
		if(caractereCourant=='>'){
			StringBuffer sb=new StringBuffer();
			sb.append(caractereCourant);
			caractereSuivant();
			if(caractereCourant=='='){
				sb.append(caractereCourant);
				//System.out.println("le lexeme est :"+Categorie.DIFFERENT.toString());
				return new UniteLexicale(Categorie.SUPEGALE, ">=");
			}
			else {
				//System.out.println("le lexeme est :"+Categorie.PT.toString());
				return new UniteLexicale(Categorie.SUPERIEUR, ">");
			}
		}
		if(Character.isDigit(caractereCourant))
			return getNombre();
		if(Character.isWhitespace(caractereCourant)||Character.isLetterOrDigit(caractereCourant))
			return getchaine();
		if(caractereCourant=='+'){
			//System.out.println("le lexeme est :"+Categorie.PLUS.toString());
			return new UniteLexicale(Categorie.PLUS, "+");
			}
		if(caractereCourant=='-'){
			//System.out.println("le lexeme est :"+Categorie.MOINS.toString());
			return new UniteLexicale(Categorie.MOINS, "-");
			}
		
		if(caractereCourant=='*'){
			//System.out.println("le lexeme est :"+Categorie.FOIS.toString());
			return new UniteLexicale(Categorie.FOIS, "*");
			}
		if(caractereCourant=='/'){
			//System.out.println("le lexeme est :"+Categorie.DIVISION.toString());
			return new UniteLexicale(Categorie.DIVISION, "/");
			}
		
		return null;
	}
	
	public UniteLexicale getMotcleOrId() {
		int etat=0;
		StringBuffer sb=new StringBuffer();
		String str1="";
		while(true) {
			switch(etat) {
				case 0 : etat=1; 
						 sb.append(caractereCourant); 
						 
						 break;
				case 1 : caractereSuivant();
						 if(eof){
							 etat=3; 
						 }
						 else
							 if(Character.isLetterOrDigit(caractereCourant)) {
								 sb.append(caractereCourant);
								
						 		
						 		}
							 else{
								 etat=2;
								  
							 }
						 break;
						
				case 2 : reculer();
						str1=sb.toString();
						etat=4;
						break;
				case 3 :
					str1=sb.toString();
					etat=4;
						break;
				case 4: 
					str1= sb.toString();
					if(str1.equalsIgnoreCase("algorithme")){
						//System.out.println("le lexeme est :"+Categorie.PROGRAM.toString());
						return new UniteLexicale(Categorie.ALGORITHME,"algorithme") ;
						}
						if(str1.equalsIgnoreCase("entier")){
							//System.out.println("le lexeme est :"+Categorie.TYPEPRIMITIVE.toString());
							return new UniteLexicale(Categorie.TYPEPRIMITIVE,"entier") ;
							}
						if(str1.equalsIgnoreCase("réel")){
							//System.out.println("le lexeme est :"+Categorie.TYPEPRIMITIVE.toString());
							return new UniteLexicale(Categorie.TYPEPRIMITIVE,"réel") ;
							}
						if(str1.equalsIgnoreCase("chaine")){
							//System.out.println("le lexeme est :"+Categorie.TYPEPRIMITIVE.toString());
							return new UniteLexicale(Categorie.TYPEPRIMITIVE,"chaine") ;
							}
						if(str1.equalsIgnoreCase("debut")){
							//System.out.println("le lexeme est :"+Categorie.BEGIN.toString());
							return new UniteLexicale(Categorie.DEBUT,"debut") ;
							}
						if(str1.equalsIgnoreCase("lire")){
							//System.out.println("le lexeme est :"+Categorie.READ.toString());
							return new UniteLexicale(Categorie.LIRE,"lire") ;
							}
						if(str1.equalsIgnoreCase("écrire")){
							//System.out.println("le lexeme est :"+Categorie.WRITE.toString());
							return new UniteLexicale(Categorie.ECRIRE,"écrire") ;
							}
						if(str1.equalsIgnoreCase("si")){
							//System.out.println("le lexeme est :"+Categorie.IF.toString());
							return new UniteLexicale(Categorie.SI,"si") ;
							}
						if(str1.equalsIgnoreCase("alors")){
							//System.out.println("le lexeme est :"+Categorie.THEN.toString());
							return new UniteLexicale(Categorie.ALORS,"alors") ;
							}
						if(str1.equalsIgnoreCase("sinon")){
							//System.out.println("le lexeme est :"+Categorie.ELSE.toString());
							return new UniteLexicale(Categorie.SINON,"sinon") ;
							}
						
						
						
						if(str1.equalsIgnoreCase("tantque")){
							//System.out.println("le lexeme est :"+Categorie.WHILE.toString());
							return new UniteLexicale(Categorie.TANTQUE,"while") ;
							}
						if(str1.equalsIgnoreCase("var")){
							//System.out.println("le lexeme est :"+Categorie.VAR.toString());
							return new UniteLexicale(Categorie.VAR,"var") ;
							}
						if(str1.equalsIgnoreCase("tableau")){
							//System.out.println("le lexeme est :"+Categorie.ARRAY.toString());
							return new UniteLexicale(Categorie.TABLEAU,"tableau") ;
							}
						if(str1.equalsIgnoreCase("finsi")){
							//System.out.println("le lexeme est :"+Categorie.OF.toString());
							return new UniteLexicale(Categorie.FINSI,"finsi") ;
							}
						if(str1.equalsIgnoreCase("fin")){
							//System.out.println("le lexeme est :"+Categorie.END.toString());
							return new UniteLexicale(Categorie.FIN,"fin") ;
							}
						if(str1.equalsIgnoreCase("ou")){
							//System.out.println("le lexeme est :"+Categorie.OR.toString());
							 return new UniteLexicale(Categorie.OU, "ou"); }
						if(str1.equalsIgnoreCase("div")){
							//System.out.println("le lexeme est :"+Categorie.DIV.toString());
							 return new UniteLexicale(Categorie.DIV, "div");
							 }
						if(str1.equalsIgnoreCase("mod")){
							//System.out.println("le lexeme est :"+Categorie.MOD.toString());
							 return new UniteLexicale(Categorie.MOD, "mod");
							 }
						if(str1.equalsIgnoreCase("et")){
							//System.out.println("le lexeme est :"+Categorie.AND.toString());
							 return new UniteLexicale(Categorie.ET, "et");
							 }
						
						else{
							//System.out.println("le lexeme est :"+Categorie.ID.toString());
						return  new UniteLexicale(Categorie.ID, sb.toString());
						}	
			}
			
		}
	}
	public UniteLexicale getNombre() {
		int etat=0;
		StringBuffer sb=new StringBuffer();
		String str;
		while(true) {
			switch(etat) {
			case 0 : etat=1; 
					 sb.append(caractereCourant); 
					 break;
			case 1 : caractereSuivant();
					 if(eof)
						 etat=3;
					 else
						 if(Character.isDigit(caractereCourant)) 
							 sb.append(caractereCourant);
						 else
							 etat=2;
					 break;
			case 2 : {
					reculer();
					//System.out.println("le lexeme est :"+Categorie.NOMBRE.toString());
					 return  new UniteLexicale(Categorie.NOMBRE, sb.toString());
					 }
			case 3 : 
				//System.out.println("le lexeme est :"+Categorie.NOMBRE.toString());
				 return  new UniteLexicale(Categorie.NOMBRE, sb.toString());
			}
		}
		
	}
	public UniteLexicale getchaine() {
		int etat=0;
		StringBuffer sb=new StringBuffer();
		String str;
		while(true) {
			switch(etat) {
			case 0 : etat=1; 
					 sb.append(caractereCourant); 
					 break;
			case 1 : caractereSuivant();
					 if(eof)
						 etat=3;
					 else
						 if(Character.isLetterOrDigit(caractereCourant)) 
							 sb.append(caractereCourant);
						 else
							 if(Character.isWhitespace(caractereCourant)) 
								 sb.append(caractereCourant);
						 else
							 etat=2;
					 break;
			case 2 : {
					reculer();
					//System.out.println("le lexeme est :"+Categorie.CHAINE.toString());
					 return  new UniteLexicale(Categorie.CHAINE, sb.toString());
					 }
			case 3 : 
				//System.out.println("le lexeme est :"+Categorie.CHAINE.toString());
				 return  new UniteLexicale(Categorie.CHAINE, sb.toString());
			}
		}
		
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return fluxCaracteres.toString();
	}
	
	
}
