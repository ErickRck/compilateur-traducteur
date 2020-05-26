package titan.analysis1.AnalyseLexicale;

import java.util.ArrayList;

public class TestAlex {

	public static void main(String[] args) {
		ArrayList<String> fluxlexeme=new ArrayList<String>();
		String source;
		String lexeme;
		StringBuffer sb=new StringBuffer();
		Scanners sc=new Scanners("test1.txt");
		source=sc.toString();
		String str =sb.append(source).toString();
		String str2=sb.append(source).toString();
		if(str.equalsIgnoreCase(str2));
		System.out.println(str);
		
		/*while(true){
			sc.lexemeSuivant();
			fluxlexeme.add(sc.lexemeSuivant());
			System.out.println("les unites lexicales sont : ");
			System.out.println(fluxlexeme);
			
		}*/
		/*fluxlexeme.add(sc.lexemeSuivant());
		fluxlexeme.add(sc.lexemeSuivant());
		fluxlexeme.add(sc.lexemeSuivant());
		fluxlexeme.add(sc.lexemeSuivant());
		fluxlexeme.add(sc.lexemeSuivant());
		fluxlexeme.add(sc.lexemeSuivant());
		fluxlexeme.add(sc.lexemeSuivant());
		fluxlexeme.add(sc.lexemeSuivant());
		fluxlexeme.add(sc.lexemeSuivant());
		fluxlexeme.add(sc.lexemeSuivant());
		fluxlexeme.add(sc.lexemeSuivant());
		fluxlexeme.add(sc.lexemeSuivant());
		fluxlexeme.add(sc.lexemeSuivant());
		fluxlexeme.add(sc.lexemeSuivant());
		fluxlexeme.add(sc.lexemeSuivant());
		fluxlexeme.add(sc.lexemeSuivant());
		fluxlexeme.add(sc.lexemeSuivant());
		fluxlexeme.add(sc.lexemeSuivant());
		System.out.println("les unit�s lexicales sont : ");
		System.out.println(fluxlexeme);*/
	
		
	}

}
