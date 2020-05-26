package titan.analysis.AnalyseSyntaxique;

  import titan.analysis.AnalyseLexicale.Categorie;

  import java.io.BufferedReader;
  import java.io.FileReader;
  import java.io.IOException;
  import java.util.*;

  public class Production {
      private Categorie axiome;
      private TreeMap<Categorie,ArrayList<ArrayList<Categorie>>> production;
      private TreeMap<Categorie, Boolean> nullable;
      private TreeMap<Categorie, TreeSet<Categorie>> premier;
      private TreeMap<Categorie, TreeSet<Categorie>> suivant;
      private HashMap<ArrayList<Categorie>, TreeSet<Categorie>> premierRegles;
      private String fichierproduction;
      private Stack<Categorie> pile;
      private ArrayList [ ] [ ] TA;
      public Production() {
          fichierproduction="grammairev2";
          production=new TreeMap<Categorie, ArrayList<ArrayList<Categorie>>>();
          nullable=new TreeMap<Categorie, Boolean>();
          premier=new TreeMap<Categorie, TreeSet<Categorie>>();
          suivant=new TreeMap<Categorie, TreeSet<Categorie>>();
          premierRegles=new HashMap<ArrayList<Categorie>, TreeSet<Categorie>>();

          TA=new ArrayList [ Categorie.indexmax1- Categorie.indexmax][ Categorie.indexmax- Categorie.indexmin+2];

          pile=new Stack<Categorie>();
      }

      public void initialiserPile() {
          pile.clear();
          //pile.push(Categorie.$);
          pile.push(axiome);
          System.out.println("les éléments initiale de la pile : "+pile);
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
                  /* le mot clé split permet de cender une chaine de caracteres selon qu'il rencontre la chaine passer en parametre
                   * en l'affectant au tableau, il cende les éléments de celui ci après avoir rencontré le mot qui est
                   * en parametre
                   * */
                  String t1[]=ligne.split("::=");
                  ArrayList<Categorie> liste=new ArrayList<Categorie>();
                  // nous mettons toutes les production en majuscule
                  Categorie clé= Categorie.toCatégorie(t1[0]);

                  ArrayList<ArrayList<Categorie>> valeur=production.get(clé);

                  String t2[]=t1[1].split("\\s+");

                  for(int i=0;i<t2.length;i++){
                      liste.add(Categorie.toCatégorie(t2[i]));
                      }
                  if(valeur==null) {
                      valeur=new ArrayList<ArrayList<Categorie>>();
                      valeur.add(liste);
                  production.put(clé,valeur);

                  }
                  else
                      valeur.add(liste);
                  }

          } catch (IOException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
          }
      }

  /* il est non terminal selon l'algorithme de cet algo dès qu'on rencontre le premier non terminal on le met
       * dans l'axiome */
      public Categorie setAxiome() {
          for(Categorie c: Categorie.values())
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
          nullable.put(Categorie.NUL, true);

          for(Categorie c: Categorie.values()) {
               if(c.estTerminal()){
                  nullable.put(c, false);
                  }
              if(c.estNonTerminal()) {
                  ArrayList<ArrayList<Categorie>> valeur=production.get(c);

                  for(int i=0;i<valeur.size();i++) {
                      if(valeur.get(i).get(0).equals(Categorie.NUL)){
                          nullable.put(c, true);

                      }
                  }
              }
          }
          for(Categorie c: Categorie.values()) {
              if(c.estNonTerminal()) {

                  ArrayList<ArrayList<Categorie>> valeur=production.get(c);

                  for(int i=0;i<valeur.size();i++) {
                      ArrayList<Categorie> prod=valeur.get(i);
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
      TreeSet<Categorie> ts=new TreeSet<Categorie>();
      ts.add(Categorie.NUL);
      premier.put(Categorie.NUL, ts);

      // pour ce qui concerne les terminaux commence ici
      for(Categorie c: Categorie.values()) {
          // dans le cas de non terminal remplir l'arbre premier des terminaux et comme valeur leurs eux mêmes
          if(c.estTerminal()) {
              TreeSet<Categorie> valeur=new TreeSet<Categorie>();
              valeur.add(c);

              premier.put(c, valeur);

          }
      }

      boolean changement=false;
      // pour ce qui concerne les nonterminaux
      do {
          changement=false;

          // les non terminaux commencent ici avec une boucle de parcourt
          for(Categorie c: Categorie.values()) {
              if(c.estNonTerminal()) {
                  TreeSet<Categorie> set=premier.get(c);
                  if(set==null){
                      set=new TreeSet<Categorie>();
                      }
                  // le cas que le terminal est aussi nullable alors, l'élément reçoit comme valeur nul
                  if(nullable.get(c)) {
                      if(set.add(Categorie.NUL)) {
                          changement=true;
                          premier.put(c, set);
                      }
                  }

                  ArrayList<ArrayList<Categorie>> valeur=production.get(c);
                  /*on parcourt dans l'arbre production toutes les valeurs de l'élément non terminal
                   * jusqu' à atendre les feuilles de ce non terminal
                   * */
                  for(int i=0;i<valeur.size();i++) {
                          ArrayList<Categorie> prod=valeur.get(i);
                          Categorie elt=prod.get(0);
                          /* la première valeur trouvé s'il est terminal  on le joint comme valeur de ce élément non terminal
                           * à l'arbre premier
                           * */
                          if(premier.get(elt)!=null && !nullable.get(elt)) {
                              for(Iterator<Categorie> it = premier.get(elt).iterator(); it.hasNext();) {
                                  Categorie ca=it.next();
                                  if(!ca.equals(Categorie.NUL) && set.add(ca)) {
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
                                  for(Iterator<Categorie> it = premier.get(elt).iterator(); it.hasNext();) {
                                      Categorie ca=it.next();
                                      if(!ca.equals(Categorie.NUL) && set.add(ca)) {
                                          changement=true;
                                          premier.put(c, set);
                                      }
                                  }

                              }
                              // fin du code
                              if(premier.get(elt)!=null && !nullable.get(elt)) {
                                  for(Iterator<Categorie> it = premier.get(elt).iterator(); it.hasNext();) {
                                      Categorie ca=it.next();
                                      if(!ca.equals(Categorie.NUL) && set.add(ca)) {
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

  private boolean ajouterSuivant(Categorie c1, Categorie c2) {
      boolean rep=false;
      if(c1.estNonTerminal() && c2.estNonTerminal()) {
          TreeSet<Categorie> set2=suivant.get(c2);
          TreeSet<Categorie> set1=suivant.get(c1);
          if(set1==null)
              set1=new TreeSet<Categorie>();
          if(set2!=null) {
              for(Iterator<Categorie> it = set2.iterator(); it.hasNext();)
                  rep=set1.add(it.next());
              suivant.put(c1, set1);
          }
      }
      return rep;
  }

  private boolean ajouterPremier(Categorie c1, Categorie c2) {
      boolean rep=false;
      if(c1.estNonTerminal()) {
          TreeSet<Categorie> set2=premier.get(c2);
          TreeSet<Categorie> set1=suivant.get(c1);
          if(set1==null)
              set1=new TreeSet<Categorie>();
          if(set2!=null) {
              for(Iterator<Categorie> it = set2.iterator(); it.hasNext();) {
                  Categorie ca=it.next();
                  if(!ca.equals(Categorie.NUL))
                      rep=set1.add(ca);
              }
              suivant.put(c1, set1);
          }
      }
      return rep;
  }

      public void calculSuivant() {
          TreeSet<Categorie> set=new TreeSet<Categorie>();
          set.add(Categorie.PT);
          suivant.put(axiome, set);
          for(Categorie c: Categorie.values()) {
              if(c.estNonTerminal()) {
                  ArrayList<ArrayList<Categorie>> productions=production.get(c);
                  for(ArrayList<Categorie> uneProduction:productions) {
                      for(int i=0;i<uneProduction.size()-1;i++) {
                          for(int j=i+1;j<uneProduction.size();j++) {
                              Categorie c1=uneProduction.get(j);
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
              for(Categorie c: Categorie.values()) {
                  if(c.estNonTerminal()) {
                      ArrayList<ArrayList<Categorie>> productions=production.get(c);
                      for(ArrayList<Categorie> uneProduction:productions) {
                          for(int i=uneProduction.size()-1;i>=0;i--) {
                              Categorie c1=uneProduction.get(i);
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
          for(Categorie c: Categorie.values()) {
              if(c.estNonTerminal()) {
                  ArrayList<ArrayList<Categorie>> productions=production.get(c);
                  for(ArrayList<Categorie> uneProduction:productions) {
                      TreeSet<Categorie> set=premierRegles.get(uneProduction);
                      if(set==null)
                          set=new TreeSet<Categorie>();
                      for(int i=0;i<uneProduction.size();i++) {
                          Categorie X=uneProduction.get(i);
                          TreeSet<Categorie> premierX=premier.get(X);
                          int j=0;
                          for(Categorie ca:premierX) {
                              if(!ca.equals(Categorie.NUL))
                                  set.add(ca);
                          }
                          if(nullable.get(X)!=null && !nullable.get(X))
                              break;
                          else
                              if(nullable.get(X) && i==uneProduction.size()-1)
                                  set.add(Categorie.NUL);
                      }
                      premierRegles.put(uneProduction, set);
                  }
              }
          }
      }
      public int indice(Categorie c) {
          if(c.estNonTerminal()) {
              return c.ordinal()- Categorie.indexmax-1;
          }
          if(c.estTerminal()) {
              return c.ordinal()- Categorie.indexmin;
          }

          return -1;

      }

      public void remplirTableAnalyse() {
          for(int i=0;i<TA.length;i++)
              for(int j=0;j<TA[i].length;j++)
                  TA[i][j]=null;

          for(Categorie c: Categorie.values()) {
              int n1,n2;
              if(c.estNonTerminal()) {
                  n1=indice(c);
                  ArrayList<ArrayList<Categorie>> productions=production.get(c);
                  for(ArrayList<Categorie> uneProduction:productions) {
                      TreeSet<Categorie> premierX=premierRegles.get(uneProduction);
                      for(Iterator<Categorie> it = premierX.iterator(); it.hasNext();) {
                          Categorie ca=it.next();
                          if(ca.equals(Categorie.NUL)) {
                              TreeSet<Categorie> suivantA=suivant.get(c);
                              for(Iterator<Categorie> it1 = suivantA.iterator(); it1.hasNext();) {
                                  Categorie ca1=it1.next();
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
      public void analyserSyntaxe(Categorie a) {
          System.out.println("....................................................................... ");
          System.out.println("ceux ci concerne l'analyse syntaxique ");
          //System.out.println("....................................................................... ");
          int iX,ia;
          ia=indice(a);
          System.out.println("l'indice de l'élément  "+a+" passer en paramètre qui vaut : "+ia);
          boolean accepter;
      do {
          accepter=false;
          Categorie X=pile.peek();
          System.out.println("l'élément x de la méthode peek est "+X);
              if(X.estNonTerminal()) {
                  iX=indice(X);
                  System.out.println("l'indice de l'élément peek concerné"+X+" étant nonterminal vaut : "+iX);
                  ArrayList<Categorie> regle=(ArrayList<Categorie>)TA[iX][ia];
                  System.out.println("nous voulons voir l'indice de l'élément non terminal concernéTA [iX]= " +iX+" et celui du parametre [ia]= "+ia);
                  System.out.println("et la regle vaut  "+regle);
                  if(regle==null) {
                      System.out.println("la regle est null alors : ");
                      System.err.println("ERREUR DE SYNTAXE!!! a");
                      System.exit(0);
                      }
                  else {
                      Categorie b= pile.pop();
                      System.out.println("la méthode pop donne comme élément "+b);
                      for(int i=regle.size()-1;i>=0;i--) {
                          Categorie Y=regle.get(i);
                          if(!Y.equals(Categorie.NUL)) {
                              pile.push(Y);
                              System.out.println(" voyons l'élément mit dans la pile qui vaut  "+Y+" et la pile est "+pile);
                          }
                      }
                      System.out.println(X+" --> "+regle);
                      System.out.println(" la pile est  "+pile);
                  }
              }
              else
                  if(X.equals(Categorie.PT)) {
                      if(a== Categorie.PT ) {
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
