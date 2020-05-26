  package titan.analysis1.AnalyseLexicale;

  import java.io.BufferedReader;
  import java.io.FileReader;
  import java.io.IOException;
  import java.util.*;

  public class Parsers {
      private  UnitesLexicalesMotsCles axiome;
      private TreeMap< UnitesLexicalesMotsCles,ArrayList<ArrayList< UnitesLexicalesMotsCles>>> production;
      private TreeMap< UnitesLexicalesMotsCles, Boolean> nullable;
      private TreeMap< UnitesLexicalesMotsCles, TreeSet< UnitesLexicalesMotsCles>> premier;
      private TreeMap< UnitesLexicalesMotsCles, TreeSet< UnitesLexicalesMotsCles>> suivant;
      private HashMap<ArrayList< UnitesLexicalesMotsCles>, TreeSet< UnitesLexicalesMotsCles>> premierRegles;
      private String fichierproduction;
      private Stack< UnitesLexicalesMotsCles> pile;
      private ArrayList[ ] [ ] TA;
      public Parsers() {
          fichierproduction="grammairevP";
          production=new TreeMap< UnitesLexicalesMotsCles, ArrayList<ArrayList< UnitesLexicalesMotsCles>>>();
          nullable=new TreeMap< UnitesLexicalesMotsCles, Boolean>();
          premier=new TreeMap< UnitesLexicalesMotsCles, TreeSet< UnitesLexicalesMotsCles>>();
          suivant=new TreeMap< UnitesLexicalesMotsCles, TreeSet< UnitesLexicalesMotsCles>>();
          premierRegles=new HashMap<ArrayList< UnitesLexicalesMotsCles>, TreeSet< UnitesLexicalesMotsCles>>();

          TA=new ArrayList[ UnitesLexicalesMotsCles.indexmax1- UnitesLexicalesMotsCles.indexmax][ UnitesLexicalesMotsCles.indexmax- UnitesLexicalesMotsCles.indexmin+2];

          pile=new Stack<UnitesLexicalesMotsCles>();
      }

      public void initialiserPile() {
          pile.clear();
          //pile.push(UnitesLexicalesMotsCles.$);
          pile.push(axiome);
          System.out.println("les �l�ments initiale de la pile : "+pile);
      }

      public void afficher() {

          for(int i=0;i<TA.length;i++) {
              for(int j=0;j<TA[i].length;j++) {
                  if(TA[i][j]==null)
                      System.out.print("--- ");
                  else
                      System.out.print(TA[i][j]);
              }
              System.out.println();
          }
      }

      public void lireProduction() {
          setAxiome();
          try {
              BufferedReader br=new BufferedReader(new FileReader(fichierproduction));
              String ligne=null;
              while((ligne=br.readLine())!=null) {
                  /* le mot cle split permet de cender une chaine de caracteres selon qu'il rencontre la chaine passer en parametre
                   * en l'affectant au tableau, il cende les éléments de celui ci apres avoir rencontré le mot qui est
                   * en parametre
                   * */
                  String t1[]=ligne.split("::=");
                  ArrayList<UnitesLexicalesMotsCles> liste=new ArrayList<UnitesLexicalesMotsCles>();
                  // nous mettons toutes les production en majuscule
                  UnitesLexicalesMotsCles cles=UnitesLexicalesMotsCles.toUnitesLexicalesMotsCles(t1[0]);

                  ArrayList<ArrayList<UnitesLexicalesMotsCles>> valeur=production.get(cles);

                  String t2[]=t1[1].split("\\s+");

                  for(int i=0;i<t2.length;i++){
                      liste.add(UnitesLexicalesMotsCles.toUnitesLexicalesMotsCles(t2[i]));
                      }
                  if(valeur==null) {
                      valeur=new ArrayList<ArrayList<UnitesLexicalesMotsCles>>();
                      valeur.add(liste);
                  production.put(cles,valeur);

                  }
                  else
                      valeur.add(liste);
                  }

          } catch (IOException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
          }
      }

  /* il est non terminal selon l'algorithme de ce programm des qu'on rencontre le premier non terminal on le met
       * dans l'axiome */
      public UnitesLexicalesMotsCles setAxiome() {
          for(UnitesLexicalesMotsCles c:UnitesLexicalesMotsCles.values())
              if(c.estNonTerminal()) {
                  axiome=c;
                  return axiome;
              }
          return null;
      }
      public void regle(){


      }

  public void setNullable() {
          System.out.println("la production est : "+production);
          nullable.put(UnitesLexicalesMotsCles.NUL, true);

          for(UnitesLexicalesMotsCles c:UnitesLexicalesMotsCles.values()) {
               if(c.estTerminal()){
                  nullable.put(c, false);
                  }
              if(c.estNonTerminal()) {
                  ArrayList<ArrayList<UnitesLexicalesMotsCles>> valeur=production.get(c);

                  for(int i=0;i<valeur.size();i++) {
                      if(valeur.get(i).get(0).equals(UnitesLexicalesMotsCles.NUL)){
                          nullable.put(c, true);

                      }
                  }
              }
          }
          for(UnitesLexicalesMotsCles c:UnitesLexicalesMotsCles.values()) {
              if(c.estNonTerminal()) {

                  ArrayList<ArrayList<UnitesLexicalesMotsCles>> valeur=production.get(c);

                  for(int i=0;i<valeur.size();i++) {
                      ArrayList<UnitesLexicalesMotsCles> prod=valeur.get(i);
                      boolean b=true;
                      for(int j=0;j<prod.size();j++) {
                          if(nullable.get(c)!=null && nullable.get(c)){
                              break;
                              }
                          if(nullable.get(prod.get(j))==null || !nullable.get(prod.get(j))){
                              b=false;
                          }
                      }
                      nullable.put(c, b);
                  }
              }
          }
      }


  public void Premiercalcul() {
      TreeSet<UnitesLexicalesMotsCles> ts=new TreeSet<UnitesLexicalesMotsCles>();
      ts.add(UnitesLexicalesMotsCles.NUL);
      premier.put(UnitesLexicalesMotsCles.NUL, ts);

      // pour ce qui concerne les terminaux commence ici
      for(UnitesLexicalesMotsCles c:UnitesLexicalesMotsCles.values()) {
          // dans le cas de non terminal remplir l'arbre premier des terminaux et comme valeur leurs eux memes
          if(c.estTerminal()) {
              TreeSet<UnitesLexicalesMotsCles> valeur=new TreeSet<UnitesLexicalesMotsCles>();
              valeur.add(c);

              premier.put(c, valeur);

          }
      }

      boolean changement=false;
      // pour ce qui concerne les nonterminaux
      do {
          changement=false;

          // les non terminaux commencent ici avec une boucle de parcourt
          for(UnitesLexicalesMotsCles c:UnitesLexicalesMotsCles.values()) {
              if(c.estNonTerminal()) {
                  TreeSet<UnitesLexicalesMotsCles> set=premier.get(c);
                  if(set==null){
                      set=new TreeSet<UnitesLexicalesMotsCles>();
                      }
                  // le cas que le terminal est aussi nullable alors, l'élément reçoit comme valeur nul
                  if(nullable.get(c)) {
                      if(set.add(UnitesLexicalesMotsCles.NUL)) {
                          changement=true;
                          premier.put(c, set);
                      }
                  }

                  ArrayList<ArrayList<UnitesLexicalesMotsCles>> valeur=production.get(c);
                  /*on parcourt dans l'arbre production toutes les valeurs de l'élément non terminal
                   * jusqu' à atendre les feuilles de ce non terminal
                   * */
                  for(int i=0;i<valeur.size();i++) {
                          ArrayList<UnitesLexicalesMotsCles> prod=valeur.get(i);
                          UnitesLexicalesMotsCles elt=prod.get(0);
                          /* la premiere valeur trouvé s'il est terminal  on le joint comme valeur de ce élément non terminal
                           * à l'arbre premier
                           * */
                          if(premier.get(elt)!=null && !nullable.get(elt)) {
                              for(Iterator<UnitesLexicalesMotsCles> it = premier.get(elt).iterator(); it.hasNext();) {
                                  UnitesLexicalesMotsCles ca=it.next();
                                  if(!ca.equals(UnitesLexicalesMotsCles.NUL) && set.add(ca)) {
                                      changement=true;
                                      premier.put(c, set);
                              }
                          }
                      }
                      // on continu le parcourt ou on trove que le premier élément est un non terminal
                      for(int j=1;j<prod.size();j++) {
                          if(nullable.get(prod.get(j-1))) {
                              // c aj
                              if(premier.get(elt)!=null && nullable.get(elt)) {
                                  for(Iterator<UnitesLexicalesMotsCles> it = premier.get(elt).iterator(); it.hasNext();) {
                                      UnitesLexicalesMotsCles ca=it.next();
                                      if(!ca.equals(UnitesLexicalesMotsCles.NUL) && set.add(ca)) {
                                          changement=true;
                                          premier.put(c, set);
                                      }
                                  }

                              }
                              // fin du code
                              if(premier.get(elt)!=null && !nullable.get(elt)) {
                                  for(Iterator<UnitesLexicalesMotsCles> it = premier.get(elt).iterator(); it.hasNext();) {
                                      UnitesLexicalesMotsCles ca=it.next();
                                      if(!ca.equals(UnitesLexicalesMotsCles.NUL) && set.add(ca)) {
                                          changement=true;
                                          premier.put(c, set);
                                      }
                                  }
                              }
                          }
                          else
                              break;
                      }
                  }
              }
          }
      }
      while(changement);
  }

  private boolean ajouterSuivant(UnitesLexicalesMotsCles c1, UnitesLexicalesMotsCles c2) {
      boolean rep=false;
      if(c1.estNonTerminal() && c2.estNonTerminal()) {
          TreeSet<UnitesLexicalesMotsCles> set2=suivant.get(c2);
          TreeSet<UnitesLexicalesMotsCles> set1=suivant.get(c1);
          if(set1==null)
              set1=new TreeSet<UnitesLexicalesMotsCles>();
          if(set2!=null) {
              for(Iterator<UnitesLexicalesMotsCles> it = set2.iterator(); it.hasNext();)
                  rep=set1.add(it.next());
              suivant.put(c1, set1);
          }
      }
      return rep;
  }

  private boolean ajouterPremier(UnitesLexicalesMotsCles c1, UnitesLexicalesMotsCles c2) {
      boolean rep=false;
      if(c1.estNonTerminal()) {
          TreeSet<UnitesLexicalesMotsCles> set2=premier.get(c2);
          TreeSet<UnitesLexicalesMotsCles> set1=suivant.get(c1);
          if(set1==null)
              set1=new TreeSet<UnitesLexicalesMotsCles>();
          if(set2!=null) {
              for(Iterator<UnitesLexicalesMotsCles> it = set2.iterator(); it.hasNext();) {
                  UnitesLexicalesMotsCles ca=it.next();
                  if(!ca.equals(UnitesLexicalesMotsCles.NUL))
                      rep=set1.add(ca);
              }
              suivant.put(c1, set1);
          }
      }
      return rep;
  }

      public void calculSuivant() {
          TreeSet<UnitesLexicalesMotsCles> set=new TreeSet<UnitesLexicalesMotsCles>();
          set.add(UnitesLexicalesMotsCles.PT);
          suivant.put(axiome, set);
          for(UnitesLexicalesMotsCles c:UnitesLexicalesMotsCles.values()) {
              if(c.estNonTerminal()) {
                  ArrayList<ArrayList<UnitesLexicalesMotsCles>> productions=production.get(c);
                  for(ArrayList<UnitesLexicalesMotsCles> uneProduction:productions) {
                      for(int i=0;i<uneProduction.size()-1;i++) {
                          for(int j=i+1;j<uneProduction.size();j++) {
                              UnitesLexicalesMotsCles c1=uneProduction.get(j);
                              ajouterPremier(uneProduction.get(i), c1);
                              if(nullable.get(c1)!=null && !nullable.get(c1))
                                  break;
                          }
                      }
                  }
              }
          }

          boolean continuer=true;
          while(continuer) {
              continuer=false;
              for(UnitesLexicalesMotsCles c:UnitesLexicalesMotsCles.values()) {
                  if(c.estNonTerminal()) {
                      ArrayList<ArrayList<UnitesLexicalesMotsCles>> productions=production.get(c);
                      for(ArrayList<UnitesLexicalesMotsCles> uneProduction:productions) {
                          for(int i=uneProduction.size()-1;i>=0;i--) {
                              UnitesLexicalesMotsCles c1=uneProduction.get(i);
                              continuer=ajouterSuivant(c1, c);
                              if(nullable.get(c1)!=null && !nullable.get(c1))
                                  break;
                          }
                      }
                  }
              }
          }

      }
      public void calculPremierRegles() {
          for(UnitesLexicalesMotsCles c:UnitesLexicalesMotsCles.values()) {
              if(c.estNonTerminal()) {
                  ArrayList<ArrayList<UnitesLexicalesMotsCles>> productions=production.get(c);
                  for(ArrayList<UnitesLexicalesMotsCles> uneProduction:productions) {
                      TreeSet<UnitesLexicalesMotsCles> set=premierRegles.get(uneProduction);
                      if(set==null)
                          set=new TreeSet<UnitesLexicalesMotsCles>();
                      for(int i=0;i<uneProduction.size();i++) {
                          UnitesLexicalesMotsCles X=uneProduction.get(i);
                          TreeSet<UnitesLexicalesMotsCles> premierX=premier.get(X);
                          int j=0;
                          for(UnitesLexicalesMotsCles ca:premierX) {
                              if(!ca.equals(UnitesLexicalesMotsCles.NUL))
                                  set.add(ca);
                          }
                          if(nullable.get(X)!=null && !nullable.get(X))
                              break;
                          else
                              if(nullable.get(X) && i==uneProduction.size()-1)
                                  set.add(UnitesLexicalesMotsCles.NUL);
                      }
                      premierRegles.put(uneProduction, set);
                  }
              }
          }
      }
      public int indice(UnitesLexicalesMotsCles c) {
          if(c.estNonTerminal()) {
              return c.ordinal()-UnitesLexicalesMotsCles.indexmax-1;
          }
          if(c.estTerminal()) {
              return c.ordinal()-UnitesLexicalesMotsCles.indexmin;
          }

          return -1;

      }

      public void remplirTableAnalyse() {
          for(int i=0;i<TA.length;i++)
              for(int j=0;j<TA[i].length;j++)
                  TA[i][j]=null;

          for(UnitesLexicalesMotsCles c:UnitesLexicalesMotsCles.values()) {
              int n1,n2;
              if(c.estNonTerminal()) {
                  n1=indice(c);
                  ArrayList<ArrayList<UnitesLexicalesMotsCles>> productions=production.get(c);
                  for(ArrayList<UnitesLexicalesMotsCles> uneProduction:productions) {
                      TreeSet<UnitesLexicalesMotsCles> premierX=premierRegles.get(uneProduction);
                      for(Iterator<UnitesLexicalesMotsCles> it = premierX.iterator(); it.hasNext();) {
                          UnitesLexicalesMotsCles ca=it.next();
                          if(ca.equals(UnitesLexicalesMotsCles.NUL)) {
                              TreeSet<UnitesLexicalesMotsCles> suivantA=suivant.get(c);
                              for(Iterator<UnitesLexicalesMotsCles> it1 = suivantA.iterator(); it1.hasNext();) {
                                  UnitesLexicalesMotsCles ca1=it1.next();
                                  n2=indice(ca1);
                                  TA[n1][n2]=uneProduction;
                              }
                          }
                          else {
                              n2=indice(ca);
                              TA[n1][n2]=uneProduction;
                          }
                      }
                  }
              }
          }

      }
      public void analyserSyntaxe(UnitesLexicalesMotsCles a) {
          System.out.println("....................................................................... ");
          System.out.println("ceux ci concerne l'analyse syntaxique ");
          //System.out.println("....................................................................... ");
          int iX,ia;
          ia=indice(a);
          System.out.println("l'indice de l'élément  "+a+" passer en paramétre qui vaut : "+ia);
          boolean accepter;
      do {
          accepter=false;
          UnitesLexicalesMotsCles X=pile.peek();
          System.out.println("l'élément x de la méthode peek est "+X);
              if(X.estNonTerminal()) {
                  iX=indice(X);
                  System.out.println("l'indice de l'élément peek concerné"+X+" étant nonterminal vaut : "+iX);
                  ArrayList<UnitesLexicalesMotsCles> regle=(ArrayList<UnitesLexicalesMotsCles>)TA[iX][ia];
                  System.out.println("nous voulons voir l'indice de l'élément non terminal concernéTA [iX]= " +iX+" et celui du parametre [ia]= "+ia);
                  System.out.println("et la regle vaut  "+regle);
                  if(regle==null) {
                      System.out.println("la regle est null alors : ");
                      System.err.println("ERREUR DE SYNTAXE!!! a");
                      System.exit(0);
                      }
                  else {
                      UnitesLexicalesMotsCles b= pile.pop();
                      System.out.println("la méthode pop donne comme élément "+b);
                      for(int i=regle.size()-1;i>=0;i--) {
                          UnitesLexicalesMotsCles Y=regle.get(i);
                          if(!Y.equals(UnitesLexicalesMotsCles.NUL)) {
                              pile.push(Y);
                              System.out.println(" voyons l'élément mit dans la pile qui vaut  "+Y+" et la pile est "+pile);
                          }
                      }
                      System.out.println(X+" --> "+regle);
                      System.out.println(" la pile est  "+pile);
                  }
              }
              else
                  if(X.equals(UnitesLexicalesMotsCles.PT)) {
                      if(a==UnitesLexicalesMotsCles.PT ) {
                          System.out.println("La syntaxe est correcte!!");
                          return;
                      }
                      else {

                          System.err.println("ERREUR DE SYNTAXE!!! b");
                          System.exit(0);
                      }
                  }
                  else
                      if(X.equals(a)) {
                          pile.pop();
                          accepter=true;
                      }
                      else {
                          System.err.println("ERREUR DE SYNTAXE!!! c");
                          System.exit(0);
                      }
      }
      while(!accepter);



      }

  }
