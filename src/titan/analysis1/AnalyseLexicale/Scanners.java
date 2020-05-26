package titan.analysis1.AnalyseLexicale;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class  Scanners {
	private ArrayList<Character> fluxCaracteres;
	private int indiceCourant;
	private char caractereCourant; 
	private boolean eof;
	
	public Scanners(String nomFich) {
		BufferedReader f=null;
		int car=0;
		fluxCaracteres=new ArrayList<Character>();
		indiceCourant=0;
		eof=false;
		try {
			f=new BufferedReader(new FileReader(nomFich));
			System.out.println("sa marche");
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
	
	public Lexique lexemeSuivant() {
		int signal;
		caractereSuivant();
		while(eof || Character.isWhitespace(caractereCourant)) {
			if (eof){
				//System.out.println("le lexeme est :"+UnitesLexicalesMotsCles.EOF.toString());	
				return new Lexique(UnitesLexicalesMotsCles.EOF, ""); 
				}
			caractereSuivant();
		}
		if(Character.isLetter(caractereCourant)){
				return getMotcleOrId();
		}
		if(caractereCourant==';'){
			System.out.println("le lexeme est :"+UnitesLexicalesMotsCles.PTV.toString());
			return new Lexique(UnitesLexicalesMotsCles.PTV, ";");
			
		}
		if(caractereCourant=='('){
			System.out.println("le lexeme est :"+UnitesLexicalesMotsCles.PARENTOUV.toString());
			return new Lexique(UnitesLexicalesMotsCles.PARENTOUV, "(");
			
		}
		if(caractereCourant==')'){
			System.out.println("le lexeme est :"+UnitesLexicalesMotsCles.PARENTFERM.toString());
			return new Lexique(UnitesLexicalesMotsCles.PARENTFERM, ")");
			
		}
		if(caractereCourant=='['){
			System.out.println("le lexeme est :"+UnitesLexicalesMotsCles.CROCHETOUV.toString());
			return new Lexique(UnitesLexicalesMotsCles.CROCHETOUV, "[");
			
		}
		if(caractereCourant==']'){
			System.out.println("le lexeme est :"+UnitesLexicalesMotsCles.CROCHETFERM.toString());
			return new Lexique(UnitesLexicalesMotsCles.CROCHETFERM, "]");
			
		}
		if(caractereCourant=='\''){
			System.out.println("le lexeme est :"+UnitesLexicalesMotsCles.APOSTROPHE.toString());
			return new Lexique(UnitesLexicalesMotsCles.APOSTROPHE, "'");
			
		}
		/*if(caractereCourant=='('){
			System.out.println("le lexeme est :"+UnitesLexicalesMotsCles.PARENOUV.toString());	
			return new Lexique(UnitesLexicalesMotsCles.PARENOUV, "(");
			
		}
		if(caractereCourant==')'){
			System.out.println("le lexeme est :"+UnitesLexicalesMotsCles.PARENFER.toString());	
			return new Lexique(UnitesLexicalesMotsCles.PARENFER, ")");
			
		}*/
		if(caractereCourant=='='){
			//System.out.println("le lexeme est :"+UnitesLexicalesMotsCles.EGALE.toString());	
			return new Lexique(UnitesLexicalesMotsCles.EGALE, "=");
			}
		if(caractereCourant==':'){
			StringBuffer sb=new StringBuffer();
			sb.append(caractereCourant);
			caractereSuivant();
			if(caractereCourant=='='){
				sb.append(caractereCourant);
				//System.out.println("le lexeme est :"+UnitesLexicalesMotsCles.AFFECT.toString());	
				return new Lexique(UnitesLexicalesMotsCles.AFFECT, ":="); 
			}
			else{
				//System.out.println("le lexeme est :"+UnitesLexicalesMotsCles.DEUXPT.toString());	
				return new Lexique(UnitesLexicalesMotsCles.DEUXPT, ":");
			}
		}
		
		if(caractereCourant==','){
			//System.out.println("le lexeme est :"+UnitesLexicalesMotsCles.VIRG.toString());	
			return new Lexique(UnitesLexicalesMotsCles.VIRG, ",");
			}

		if(caractereCourant=='.'){
			String str="";
			StringBuffer sb=new StringBuffer();
			sb.append(caractereCourant);
			 
			 str=sb.toString();
			 if(str.equalsIgnoreCase("..")){
				
				System.out.println("le lexeme est :"+UnitesLexicalesMotsCles.PTS.toString());
				return new Lexique(UnitesLexicalesMotsCles.PTS, ".");
			}
			if(str.equalsIgnoreCase(".")){
					System.out.println("le lexeme est :"+UnitesLexicalesMotsCles.PT.toString());
					return new Lexique(UnitesLexicalesMotsCles.PT, ".");
			}
				
		
		
	}
		if(caractereCourant=='<'){
			StringBuffer sb=new StringBuffer();
			sb.append(caractereCourant);
			caractereSuivant();
			if(caractereCourant=='>'){
				sb.append(caractereCourant);
				//System.out.println("le lexeme est :"+UnitesLexicalesMotsCles.DIFFERENT.toString());	
				return new Lexique(UnitesLexicalesMotsCles.DIFFERENT, "<>"); 
			}
			else if(caractereCourant=='='){
				//System.out.println("le lexeme est :"+UnitesLexicalesMotsCles.PT.toString());	
				return new Lexique(UnitesLexicalesMotsCles.INFEREGALE, "<=");
			}
			else {
				//System.out.println("le lexeme est :"+UnitesLexicalesMotsCles.PT.toString());	
				return new Lexique(UnitesLexicalesMotsCles.INFERIEUR, "<");
			}
		}
		if(caractereCourant=='>'){
			StringBuffer sb=new StringBuffer();
			sb.append(caractereCourant);
			caractereSuivant();
			if(caractereCourant=='='){
				sb.append(caractereCourant);
				//System.out.println("le lexeme est :"+UnitesLexicalesMotsCles.DIFFERENT.toString());	
				return new Lexique(UnitesLexicalesMotsCles.SUPEGALE, ">="); 
			}
			else {
				//System.out.println("le lexeme est :"+UnitesLexicalesMotsCles.PT.toString());	
				return new Lexique(UnitesLexicalesMotsCles.SUPERIEUR, ">");
			}
		}
		if(Character.isDigit(caractereCourant))
			return getNombre();
		if(Character.isWhitespace(caractereCourant)|| Character.isLetterOrDigit(caractereCourant))
			return getchaine();
		if(caractereCourant=='+'){
			//System.out.println("le lexeme est :"+UnitesLexicalesMotsCles.PLUS.toString());	
			return new Lexique(UnitesLexicalesMotsCles.PLUS, "+");
			}
		if(caractereCourant=='-'){
			//System.out.println("le lexeme est :"+UnitesLexicalesMotsCles.MOINS.toString());	
			return new Lexique(UnitesLexicalesMotsCles.MOINS, "-");
			}
		
		if(caractereCourant=='*'){
			//System.out.println("le lexeme est :"+UnitesLexicalesMotsCles.FOIS.toString());	
			return new Lexique(UnitesLexicalesMotsCles.FOIS, "*");
			}
		if(caractereCourant=='/'){
			//System.out.println("le lexeme est :"+UnitesLexicalesMotsCles.DIVISION.toString());	
			return new Lexique(UnitesLexicalesMotsCles.DIVISION, "/");
			}
		
		return null;
	}
	
	public Lexique getMotcleOrId() {
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
					if(str1.equalsIgnoreCase("program")){
						//System.out.println("le lexeme est :"+UnitesLexicalesMotsCles.PROGRAM.toString());	
						return new Lexique(UnitesLexicalesMotsCles.PROGRAM,"program") ;
						}
						if(str1.equalsIgnoreCase("integer")){
							//System.out.println("le lexeme est :"+UnitesLexicalesMotsCles.TYPEPRIMITIVE.toString());	
							return new Lexique(UnitesLexicalesMotsCles.TYPEPRIMITIVE,"integer") ;
							}
						if(str1.equalsIgnoreCase("real")){
							//System.out.println("le lexeme est :"+UnitesLexicalesMotsCles.TYPEPRIMITIVE.toString());	
							return new Lexique(UnitesLexicalesMotsCles.TYPEPRIMITIVE,"real") ;
							}
						if(str1.equalsIgnoreCase("String")){
							//System.out.println("le lexeme est :"+UnitesLexicalesMotsCles.TYPEPRIMITIVE.toString());	
							return new Lexique(UnitesLexicalesMotsCles.TYPEPRIMITIVE,"String") ;
							}
						if(str1.equalsIgnoreCase("begin")){
							//System.out.println("le lexeme est :"+UnitesLexicalesMotsCles.BEGIN.toString());	
							return new Lexique(UnitesLexicalesMotsCles.BEGIN,"begin") ;
							}
						if(str1.equalsIgnoreCase("read")){
							//System.out.println("le lexeme est :"+UnitesLexicalesMotsCles.READ.toString());	
							return new Lexique(UnitesLexicalesMotsCles.READ,"read") ;
							}
						if(str1.equalsIgnoreCase("write")){
							//System.out.println("le lexeme est :"+UnitesLexicalesMotsCles.WRITE.toString());	
							return new Lexique(UnitesLexicalesMotsCles.WRITE,"write") ;
							}
						if(str1.equalsIgnoreCase("if")){
							//System.out.println("le lexeme est :"+UnitesLexicalesMotsCles.IF.toString());	
							return new Lexique(UnitesLexicalesMotsCles.IF,"if") ;
							}
						if(str1.equalsIgnoreCase("then")){
							//System.out.println("le lexeme est :"+UnitesLexicalesMotsCles.THEN.toString());	
							return new Lexique(UnitesLexicalesMotsCles.THEN,"then") ;
							}
						if(str1.equalsIgnoreCase("else")){
							//System.out.println("le lexeme est :"+UnitesLexicalesMotsCles.ELSE.toString());	
							return new Lexique(UnitesLexicalesMotsCles.ELSE,"else") ;
							}
						if(str1.equalsIgnoreCase("for")){
							//System.out.println("le lexeme est :"+UnitesLexicalesMotsCles.FOR.toString());	
							return new Lexique(UnitesLexicalesMotsCles.FOR,"for") ;
							}
						if(str1.equalsIgnoreCase("to")){
							//System.out.println("le lexeme est :"+UnitesLexicalesMotsCles.TO.toString());	
							return new Lexique(UnitesLexicalesMotsCles.TO,"to") ;
							}
						if(str1.equalsIgnoreCase("downto")){
							//System.out.println("le lexeme est :"+UnitesLexicalesMotsCles.DOWNTO.toString());	
							return new Lexique(UnitesLexicalesMotsCles.DOWNTO,"downto") ;
							}
						if(str1.equalsIgnoreCase("do")){
							//System.out.println("le lexeme est :"+UnitesLexicalesMotsCles.DO.toString());	
							return new Lexique(UnitesLexicalesMotsCles.DO,"do") ;
							}
						if(str1.equalsIgnoreCase("while")){
							//System.out.println("le lexeme est :"+UnitesLexicalesMotsCles.WHILE.toString());	
							return new Lexique(UnitesLexicalesMotsCles.WHILE,"while") ;
							}
						if(str1.equalsIgnoreCase("var")){
							//System.out.println("le lexeme est :"+UnitesLexicalesMotsCles.VAR.toString());	
							return new Lexique(UnitesLexicalesMotsCles.VAR,"var") ;
							}
						if(str1.equalsIgnoreCase("array")){
							//System.out.println("le lexeme est :"+UnitesLexicalesMotsCles.ARRAY.toString());	
							return new Lexique(UnitesLexicalesMotsCles.ARRAY,"array") ;
							}
						if(str1.equalsIgnoreCase("of")){
							//System.out.println("le lexeme est :"+UnitesLexicalesMotsCles.OF.toString());	
							return new Lexique(UnitesLexicalesMotsCles.OF,"of") ;
							}
						if(str1.equalsIgnoreCase("end")){
							//System.out.println("le lexeme est :"+UnitesLexicalesMotsCles.END.toString());	
							return new Lexique(UnitesLexicalesMotsCles.END,"end") ;	
							}
						if(str1.equalsIgnoreCase("or")){
							//System.out.println("le lexeme est :"+UnitesLexicalesMotsCles.OR.toString());	
							 return new Lexique(UnitesLexicalesMotsCles.OR, "or"); }
						if(str1.equalsIgnoreCase("div")){
							//System.out.println("le lexeme est :"+UnitesLexicalesMotsCles.DIV.toString());	
							 return new Lexique(UnitesLexicalesMotsCles.DIV, "div"); 
							 }
						if(str1.equalsIgnoreCase("mod")){
							//System.out.println("le lexeme est :"+UnitesLexicalesMotsCles.MOD.toString());	
							 return new Lexique(UnitesLexicalesMotsCles.MOD, "mod"); 
							 }
						if(str1.equalsIgnoreCase("and")){
							//System.out.println("le lexeme est :"+UnitesLexicalesMotsCles.AND.toString());	
							 return new Lexique(UnitesLexicalesMotsCles.AND, "and"); 
							 }
						if(str1.equalsIgnoreCase("and")){
							//System.out.println("le lexeme est :"+UnitesLexicalesMotsCles.AND.toString());	
							 return new Lexique(UnitesLexicalesMotsCles.AND, "and"); 
							 }
						else{
							//System.out.println("le lexeme est :"+UnitesLexicalesMotsCles.ID.toString());	
						return  new Lexique(UnitesLexicalesMotsCles.ID, sb.toString());
						}	
			}
			
		}
	}
	public Lexique getNombre() {
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
					//System.out.println("le lexeme est :"+UnitesLexicalesMotsCles.NOMBRE.toString());	
					 return  new Lexique(UnitesLexicalesMotsCles.NOMBRE, sb.toString());
					 }
			case 3 : 
				//System.out.println("le lexeme est :"+UnitesLexicalesMotsCles.NOMBRE.toString());	
				 return  new Lexique(UnitesLexicalesMotsCles.NOMBRE, sb.toString());
			}
		}
		
	}
	public Lexique getchaine() {
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
					//System.out.println("le lexeme est :"+UnitesLexicalesMotsCles.CHAINE.toString());	
					 return  new Lexique(UnitesLexicalesMotsCles.CHAINE, sb.toString());
					 }
			case 3 : 
				//System.out.println("le lexeme est :"+UnitesLexicalesMotsCles.CHAINE.toString());	
				 return  new Lexique(UnitesLexicalesMotsCles.CHAINE, sb.toString());
			}
		}
		
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return fluxCaracteres.toString();
	}
	
	
}
