package titan.analysis.Traducteur;

import titan.analysis.AnalyseLexicale.UniteLexicale;

import java.util.ArrayList;
import java.util.TreeMap;

//import javax.swing.text.html.HTMLEditorKit.Parser;

public class Cible {
	private String lexeme;
	private UniteLexicale ulmc;

	private ArrayList<String> lexgeneral;
	private ArrayList<Object> lexmcle;
	private ArrayList<Object> val1;
	private ArrayList<Object> val2;
	private ArrayList<Object> val3;
	private ArrayList<ArrayList<Object>> lexc1;
	private ArrayList<Object> lexc2;
	private ArrayList<Object> lexc3;
	//private Scanners sc;
	private TreeMap<String,ArrayList<Object>> cpc; 
	private int nbrefois;
	private int nbrefois1;
	// nous initialisons tous les array list que nous allons utilisé
	public Cible(){
		lexgeneral=new ArrayList<String>();
		lexmcle=new ArrayList<Object>();
		val1=new ArrayList<Object>();
		val2=new ArrayList<Object>();
		val3=new ArrayList<Object>();
		lexc1= new ArrayList<ArrayList<Object>>();
		lexc2= new ArrayList<Object>();
		cpc=new TreeMap<String,ArrayList<Object>>();
		nbrefois=0;
		nbrefois1=0;
	}
	
	/* remplissage de tous les lexemes  mot cle dans une collection 
	 * les lexemes debutant l'une de partie déclarative 
	 * de algoritme pascal
	 * */
	
	
	public void tablelexmc(String lexem){
		this.lexeme=lexem;
		lexgeneral.add(lexeme);
		if(this.nbrefois1==0){
			if(lexeme=="algorithme"){
			lexmcle.add(lexeme);
			}
			else if(lexeme=="var"){
				lexmcle.add(lexeme);
			
			}
			else if(lexeme=="debut"){
				lexmcle.add(lexeme);
				
				this.nbrefois1=1;
				System.out.println("le i est à "+nbrefois1);
		
		   }
		}
		
	}
	/**nous considerons les clé comme les partie declarative et 
	 * les valeurs  les lexeme les composant
	 * 
	 */
	
	public void cl_valeur(){
		/*nous parcourons tous les éléments de lexgeneral où il y a tous les lexeme
		 * du code source et les regroupons en  catégorie selon qu'on recontre 
		 * le mot clé algorithme, var et debut  on donne leurs équivalent en C
		 *  
		 * */
		
		for(int i=0;i<lexgeneral.size();i++){
			if(lexgeneral.get(i)=="algorithme"){
				for(int j=i+1;j<=lexgeneral.size();j++){
					if(lexgeneral.get(j)=="var" ||lexgeneral.get(j)== "debut" ) break;
					else{
							val1.add(lexgeneral.get(j));
							System.out.println(" les valeurs de algorithme sont: "+val1);
							System.out.println(" les valeurs de lexeme générale: "+lexgeneral);
							System.out.println(" la valeur qui se trouve à lindice : "+(i)+"est : "+lexgeneral.get(i));
							//à revoir 
							System.out.println("aficher cpc qui vaut : "+cpc);
						
					}

					
					//System.out.println(" les valeurss de algorit sont: "+val1);
				}
				val1.add(" ");
				cpc.put(lexgeneral.get(i), val1);
				System.out.println("aficher cpc qui vaut maintennt : "+cpc);
			}
			else if(lexgeneral.get(i).toString()=="var"){
				for(int j=i+1;j<=lexgeneral.size();j++){
					if(lexgeneral.get(j)== "debut" ) break;
					else{
					val2.add(lexgeneral.get(j).toString());
					//à revoir 
					
					
					}
				}
				val2.add(" ");
				/*la collection cpc regroupe chaque categorie de mot clé elle les associe 
				 * avec les lexeme qui suivent ins de savoir leurs équivanlent en C
				 * */
				cpc.put(lexgeneral.get(i).toString(), val2);
				System.out.println("aficher cpc qui vaut maintenant après var : "+cpc);
			}
					
			else if(lexgeneral.get(i).toString()=="debut"){
			        	System.out.println("le nombre est égale à "+this.nbrefois);
			        	for(int j=i+1;j<lexgeneral.size();j++){
						
						System.out.println("voyons les élément qui compose les valeurs de debut : "+lexgeneral.get(j-1));
						val3.add(lexgeneral.get(j).toString());
						//à revoir
						
					}
				
				val3.add(" ");
				cpc.put(lexgeneral.get(i).toString(), val3);
				System.out.println("aficher cpc qui vaut maintenant après debut : "+cpc);
				System.out.println("le nbr de fois  ");
				
				}       
			}
		}
	
	// la prochaine étape consistera à convertir chaque valeur composant chaque clé 
	public String cpc(){
		StringBuffer str=new StringBuffer();
		StringBuffer strcomp=new StringBuffer();
		String EquivC=null;
		String strc=null;
		System.out.println("je veut voir les lexemes mots clés : " +lexmcle);
		for(int i=0;i<lexmcle.size();i++){
			if(lexmcle.get(i).toString()=="algorithme"){
				str.append("#include <stdio.h>"+"\n"+"#include <stdlib.h>"+"\n"+"int main(int argc, char *argv[]) { }");
				System.out.println("l'indice de str est "+str.length());
			}
			if(lexmcle.get(i).toString()=="var"){
				lexc1= new ArrayList<ArrayList<Object>>();
				lexc1.add(cpc.get(lexmcle.get(i)));
				System.out.println("je veut voir ce que donne l'indice : "+lexc1.get(0).size());
				System.out.println("les éléments de lexc1 sont : "+lexc1);
				
				for(int j=0;j<lexc1.get(0).size();j++){
					System.out.println("l'élément à l'indice  : "+j+" est "+lexc1.get(0).get(j));
					String valeurcompl="";
					if(lexc1.get(0).get(j).toString()=="entier"){
						StringBuffer str2=new StringBuffer();
						for(int t=j-1;t>=0;t--){
							if(lexc1.get(0).get(t).toString()=="réel" || lexc1.get(0).get(t).toString()=="chaine"||lexc1.get(0).get(t).toString()==";"){
								break;
							}
							else if(lexc1.get(0).get(t).toString()==":"){
								str2.append(" ");
							}
							else{
								str2.append(lexc1.get(0).get(t));
							}
							System.out.println("les contenus de str2 : "+str2);
								
							
						}
						valeurcompl=str2.toString();
						str.replace(str.length()-1, str.length(),"\n"+ "\t"+"int"+valeurcompl);
						System.out.println("le str dans la part var est : "+str);
						
					}
					
					else if(lexc1.get(0).get(j).toString()=="réel"){
						StringBuffer str2=new StringBuffer();
						for(int t=j-1;t>=0;t--){
							if(lexc1.get(0).get(t).toString()=="entier" || lexc1.get(0).get(t).toString()=="chaine"||lexc1.get(0).get(t).toString()==";"){
								break;
							}
							if(lexc1.get(0).get(t).toString()==":"){
								str2.append(" ");
							}
							else{
								str2.append(lexc1.get(0).get(t));
							}
							System.out.println("les contenus de str2 : "+str2);
								
							
						}
						valeurcompl=str2.toString();
						System.out.println("les elmts de str2"+str2);
						str.replace(str.length()-1, str.length(),"\n"+ "\t"+"double"+valeurcompl);
						System.out.println("le str dans la part var est : "+str);
						
					}
					if(lexc1.get(0).get(j).toString()=="chaine"){
						StringBuffer str2=new StringBuffer();
						for(int t=j-1;t>=0;t--){
							if(lexc1.get(0).get(t).toString()=="réel" || lexc1.get(0).get(t).toString()=="entier"||lexc1.get(0).get(t).toString()==";"){
								break;
							}
							else if(lexc1.get(0).get(t).toString()==":"){
								str2.append(" ");
							}
							else{
								str2.append(lexc1.get(0).get(t));
							}
							System.out.println("les contenus de str2 : "+str2);
								
							
						}
						valeurcompl=str2.toString();
						str.replace(str.length()-1, str.length(),"\n"+ "\t"+"String"+valeurcompl);
						System.out.println("le str dans la part var est : "+str);
						
					}
					
					else if (lexc1.get(0).get(j).toString()==";"){
						System.out.println("ceci concerne le ; dans var");
						str.append(";");
						str.append("\n");
						str.append("\t");
						System.out.println("c'est le str "+str.toString());
		
						
					}
					else if (lexc1.get(0).get(j).toString()==";" && lexc1.get(0).get(j-1).toString()=="fin"){
						System.out.println("ceci concerne le ; dans var");
						str.append("\n");
						str.append("\t");
						System.out.println("c'est le str "+str.toString());
		
						
					}
					
					
				}
				strc=str.toString();
				strcomp.append(strc);
				System.out.println("le strcomp dans la part var est : "+strcomp);
				
				
			}
			if(lexmcle.get(i).toString()=="debut"){
				str=new StringBuffer();
				lexc1= new ArrayList<ArrayList<Object>>();
				lexc1.add(cpc.get(lexmcle.get(i)));
				for(int j=0;j<lexc1.get(0).size();j++){
					/*if(lexc1.get(0).get(j)=="fin"+";"){
						
						str=new StringBuffer();
						System.out.println("concerne fin  ds debut");
											str.append("}"+"\n"+"\t"+"\t"+"\n");
											//strcomp.append(str);
								//System.out.println("on affiche le append compl : "+strcomp);
								System.out.println("c'est le str "+str);
								strc=str.toString();
								strcomp.append(strc);
								
								}
					 else */if(lexc1.get(0).get(j)=="fin" && lexc1.get(0).get(j+1)=="."){
						
						str=new StringBuffer();
						System.out.println("concerne debut  ds fin");
											str.append("return 0; "+"\n");
											str.append("}"+"\n");
											//strcomp.append(str);
								//System.out.println("on affiche le append compl : "+strcomp);
								System.out.println("c'est le str "+str);
								strc=str.toString();
								strcomp.append(strc);
								
								}
					 else if(lexc1.get(0).get(j)=="fin"){
							
							str=new StringBuffer();
							System.out.println("concerne fin  ds debut");
												str.append("}"+"\n");
												//strcomp.append(str);
									//System.out.println("on affiche le append compl : "+strcomp);
									System.out.println("c'est le str "+str);
									strc=str.toString();
									strcomp.append(strc);
									
									}
						
					
					else if(lexc1.get(0).get(j)=="."){
							//str.append("return 0;");
						    str=new StringBuffer();
							strc=str.toString();
							strcomp.append(strc);
							
							//strcomp.append(str);
							break;
						}
					else if(lexc1.get(0).get(j)==":="){
						str=new StringBuffer();
						str.append("=");
						//strcomp.append(str);
						System.out.println("c'est le str "+str);
						strc=str.toString();
						strcomp.append(strc);
						System.out.println("c'est le str "+str);
						
					}
					else if(lexc1.get(0).get(j)=="debut"){
						str=new StringBuffer();
						str.append("{");
						str.append("\n");
						str.append("\t");
						//strcomp.append(str);
						System.out.println("c'est le str "+str);
						strc=str.toString();
						strcomp.append(strc);
						System.out.println("c'est le str "+str);
						
					}
					else if(lexc1.get(0).get(j)=="lire"){
						str=new StringBuffer();
						str.append("scanf");
						//strcomp.append(str);
						System.out.println("c'est le str "+str);
						strc=str.toString();
						strcomp.append(strc);
						System.out.println("c'est le str "+str);
						
					}
					else if(lexc1.get(0).get(j)=="(" && lexc1.get(0).get(j-1)=="lire"){
						str=new StringBuffer();
						str.append("(\"%d\",");
						//strcomp.append(str);
						System.out.println("c'est le str "+str);
						strc=str.toString();
						strcomp.append(strc);
						System.out.println("c'est le str "+str);
						
					}
					else if(lexc1.get(0).get(j)=="tantque"){
						str=new StringBuffer();
						str.append("while");
						//strcomp.append(str);
						System.out.println("c'est le str "+str);
						strc=str.toString();
						strcomp.append(strc);
						System.out.println("c'est le str "+str);
						
					}
		/*dans cette partie à chaque fois qu'on rencontre le mot clé for 
		 * on lui ajoute premièrement dans le buffer et ensuite on 
		 * crée une petite boucle pour atteindre l'autre mot clé qui est to qu'on remplacerait
		 * par un point virgule (;) et après en rencontre do qui elle aussi sera remplacé par 
		 * un point virgule(;) 
		 * */		
			/*else if(lexc1.get(0).get(j)==""+lexc1.get(0).get(j)+lexc1.get(0).get(j+1)+lexc1.get(0).get(j+2)+lexc1.get(0).get(j+3)+lexc1.get(0).get(j+4)+lexc1.get(0).get(j+5)+lexc1.get(0).get(j+6)){
						str=new StringBuffer();
						str.append("\n");
						//strcomp.append(str);
						System.out.println("c'est le str "+str);
						strc=str.toString();
						strcomp.append(strc);
						System.out.println("c'est le str "+str);
					}*/
					
			/*else if(lexc1.get(0).get(j)=="for"){
						str=new StringBuffer();
						str.append("for(");
						for (int t=j+1;t<=lexc1.get(0).size();t++){
							if(lexc1.get(0).get(t)=="to"){
								for (int u=t-1;u<=lexc1.get(0).size();u--){
									if(lexc1.get(0).get(u)=="for") break;
									else if (lexc1.get(0).get(u)==lexc1.get(0).get(j+1)) {
										str.append(lexc1.get(0).get(u));
										for(int v=u+1;v<=lexc1.get(0).size();v++){
											if(lexc1.get(0).get(v)==":=") str.append("=");
											else if (lexc1.get(0).get(v)=="to") {
												str.append(";");
												break;
												}
											else str.append(lexc1.get(0).get(v));
											}
										}	
								}
	
							str.append(lexc1.get(0).get(j+1)+"<=");
							}
							
							else if(lexc1.get(0).get(t)=="do")
							{
								for (int u=t-1;u<=lexc1.get(0).size();u--){
									if(lexc1.get(0).get(u)==":=") str.append("=");
									else if(lexc1.get(0).get(u)=="to") break;
									else str.append(lexc1.get(0).get(u));	
								}
							str.append(";");
							str.append(lexc1.get(0).get(j+1)+"++"+")");
							break;
							}
						}
						
						//strcomp.append(str);
						System.out.println("c'est le str "+str);
						strc=str.toString();
						strcomp.append(strc);
						System.out.println("c'est le str "+str);	
					}*/
			
			else if(lexc1.get(0).get(j)=="for"){
				str=new StringBuffer();
				str.append("for(");
				System.out.println("c'est le str "+str);
				strc=str.toString();
				strcomp.append(strc);
				System.out.println("c'est le str "+str);
				
					}
			else if(lexc1.get(0).get(j)=="to" && lexc1.get(0).get(j-4)=="for" ){
				str=new StringBuffer();
				str.append(";"+lexc1.get(0).get(j-3)+"<=");
				System.out.println("c'est le str "+str);
				strc=str.toString();
				strcomp.append(strc);
				System.out.println("c'est le str "+str);
				
					}
			else if(lexc1.get(0).get(j)=="do" && lexc1.get(0).get(j-2)=="to" ){
				str=new StringBuffer();
				str.append(";"+lexc1.get(0).get(j-5)+"++)");
				System.out.println("c'est le str "+str);
				strc=str.toString();
				strcomp.append(strc);
				System.out.println("c'est le str "+str);
				
					}
					else if(lexc1.get(0).get(j)=="écrire"){
						str=new StringBuffer();
						str.append("printf");
						//strcomp.append(str);
						System.out.println("c'est le str "+str);
						strc=str.toString();
						strcomp.append(strc);
						System.out.println("c'est le str "+str);
						
					}
					else if(lexc1.get(0).get(j)=="à"){
						str=new StringBuffer();
						str.append(" ");
						//strcomp.append(str);
						System.out.println("c'est le str "+str);
						strc=str.toString();
						strcomp.append(strc);
						System.out.println("c'est le str "+str);
						
					}
					else if(lexc1.get(0).get(j)=="faire"){
						str=new StringBuffer();
						str.append(" ");
						//strcomp.append(str);
						System.out.println("c'est le str "+str);
						strc=str.toString();
						strcomp.append(strc);
						System.out.println("c'est le str "+str);
						
					}
					else if(lexc1.get(0).get(j)=="et"){
						str=new StringBuffer();
						str.append(" ");
						str.append("&&");
						str.append(" ");
						//strcomp.append(str);
						System.out.println("c'est le str "+str);
						strc=str.toString();
						strcomp.append(strc);
						System.out.println("c'est le str "+str);
						
					}
					else if(lexc1.get(0).get(j)=="alor"){
						str=new StringBuffer();
						str.append(" ");
						str.append("\n");
						str.append("\t");
						//strcomp.append(str);
						System.out.println("c'est le str "+str);
						strc=str.toString();
						strcomp.append(strc);
						System.out.println("c'est le str "+str);
						
					}
					else if(lexc1.get(0).get(j)==";")
						{	
						if(lexc1.get(0).get(j)==";" && lexc1.get(0).get(j-1)=="fin"){
							str.append("\n");
						
						}	
						else {
							str=new StringBuffer();
						
							str.append(lexc1.get(0).get(j));
							//strcomp.append(str);
							System.out.println("concerne le ; ds debut");
							System.out.println("c'est le str "+str);
							str.append("\n");
							str.append("\t");
							//System.out.println("on affiche le append compl : "+strcomp);
							strc=str.toString();
							strcomp.append(strc);
							}
								}
					else if(lexc1.get(0).get(j)=="si")
					{		str=new StringBuffer();
							//str.append(lexc1.get(0).get(j));
							//strcomp.append(str);
							System.out.println("concerne le ; ds debut");
							System.out.println("c'est le str "+str);
							str.append("if");
							//System.out.println("on affiche le append compl : "+strcomp);
							strc=str.toString();
							strcomp.append(strc);
								}
					else if(lexc1.get(0).get(j)=="alors")
					{		str=new StringBuffer();
							//str.append(lexc1.get(0).get(j));
							//strcomp.append(str);
							System.out.println("concerne le ; ds debut");
							System.out.println("c'est le str "+str);
							str.append("{");
							str.append("\n");
							str.append("\t");
							//System.out.println("on affiche le append compl : "+strcomp);
							strc=str.toString();
							strcomp.append(strc);
							
								}
					else if(lexc1.get(0).get(j)=="sinon")
					{		str=new StringBuffer();
							//str.append(lexc1.get(0).get(j));
							//strcomp.append(str);
							System.out.println("concerne le ; ds debut");
							System.out.println("c'est le str "+str);
							str.append("}");
							str.append("\n");
							str.append("\t");
							str.append("else{");
							str.append("\n");
							str.append("\t");
							//System.out.println("on affiche le append compl : "+strcomp);
							strc=str.toString();
							strcomp.append(str);
								}
					else if(lexc1.get(0).get(j)=="finsi")
					{		str=new StringBuffer();
							//str.append(lexc1.get(0).get(j));
							//strcomp.append(str);
							System.out.println("concerne le ; ds debut");
							System.out.println("c'est le str "+str);
							str.append("\n");
							str.append("\t");
							str.append("}");
							//System.out.println("on affiche le append compl : "+strcomp);
							strc=str.toString();
							strcomp.append(str);
								}
					
					else{
							str=new StringBuffer();
							System.out.println("concerne le sinon ds debut");
							
							str.append(lexc1.get(0).get(j));
							System.out.println("c'est le str "+str);
							//strcomp.append(str);
							//System.out.println("on affiche le append compl : "+strcomp);
							//str.deleteCharAt(0);
							strc=str.toString();
							strcomp.append(strc);
							
						
						
					}
					
				}
				
				//System.out.println("le strcomp dans la part var est : "+strcomp);
			}
			
			
		}
		EquivC=strcomp.toString();
		//System.out.println("l'équivalent en c est : "+EquivC);
		return EquivC;
		
		
	}
	public void affichage(String lc){
		
		System.out.println("l'affichage des éléments de transformation");
		
		System.out.println("tous les lexemes : "+lexgeneral);
		System.out.println("tous les lexeme lié à algorit : "+val1);
		System.out.println("tous les lexemes lié à somme : "+val2);
		System.out.println("tous les lexemes lié à debut : "+val3);
		System.out.println("tous les lexemes mots clé : "+lexmcle);
		System.out.println("transformation  sont : "+lc);
		
		
	}
}
