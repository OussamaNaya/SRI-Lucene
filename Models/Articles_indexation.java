package Models;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.text.PDFTextStripper;
import java.io.File;
import java.io.IOException;
import java.util.regex.*;

import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Hits;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.search.TopDocs;




public class Articles_indexation {
    String repertoire_index = "C:\\Users\\user\\Desktop\\Projet SRI Lucene\\Projet SRI Lucene\\Index";
    static String pdfDirPath = "C:\\Users\\user\\Desktop\\Projet SRI Lucene\\Projet SRI Lucene\\Articles";
    Analyzer analyseur;
    QueryParser parser;  // Le parser de requête Lucene
    int resultats_par_page = 5;  // Nombre de résultats par page


    // Méthode pour créer un document d'article
    private Document createArticles_2(String contenu, String titre) {

        //System.out.println("Les donnees recue sont : contenu = "+contenu+",  titre = "+titre);

        // Création d'un nouveau document
        Document doc_article = new Document();

        // Ajout des champs avec les bonnes méthodes pour Lucene 2.9.4
        doc_article.add(new Field("titre", titre, Field.Store.YES, Field.Index.ANALYZED));
        doc_article.add(new Field("contenu", contenu, Field.Store.YES, Field.Index.ANALYZED));

        // Affichage des données ajoutées
        //System.out.println("Les données après l'ajout sont :");
        //System.out.println("Titre : " + doc_article.get("titre"));
        //System.out.println("Contenu : " + doc_article.get("contenu"));

        return doc_article;
    }
    private void indexArticles(Document document) throws Exception {
        // Liste des mots vides (Stop words)
        List<String> stopWordsList = Arrays.asList("0", "1", "000", "$", "et", "ou");
        
        // Conversion de la liste en tableau de String
        String[] stopWordsArray = stopWordsList.toArray(new String[0]);

        // Création de l'analyzer avec les mots vides
        Analyzer analyseur = new StandardAnalyzer(stopWordsArray);

        // Création de l'index avec l'IndexWriter
        IndexWriter writer = new IndexWriter(repertoire_index, analyseur, IndexWriter.MaxFieldLength.UNLIMITED);
        
        // Ajout du document à l'index
        writer.addDocument(document);
        
        // Fermeture de l'IndexWriter
        writer.close();
    }
    public static List<String> listerFichiers() {
        List<String> fichiersTxt = new ArrayList<>();
        String cheminDossier = Articles_indexation.pdfDirPath;
        File dossier = new File(cheminDossier);

        if (dossier.isDirectory()) {
            for (File fichier : dossier.listFiles()) {
                if (fichier.isFile() && fichier.getName().endsWith(".pdf")) {
                    fichiersTxt.add(fichier.getName());
                }
            }
        }

        return fichiersTxt;
    }
    public static String extractInfoContentPDF(String pdfFilePath) {
        PDDocument document = null;
        StringBuilder content = new StringBuilder();
    
    
        try {
                // Charger le PDF
                document = PDDocument.load(new File(pdfFilePath));
    
                // Extraction des métadonnées
                PDDocumentInformation info = document.getDocumentInformation();
    
                // Extraction du contenu
                PDFTextStripper stripper = new PDFTextStripper();
                content.append(stripper.getText(document));
    
    
        } catch (IOException e) {
                e.printStackTrace();
        } finally {
                if (document != null) {
                    try {
                        document.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        }


        // Vérification de la longueur du contenu avant d'afficher un extrait
        //String extractedContent = content.toString();
        //if (extractedContent.length() > 500) {
        //    System.out.println("Contenu extrait :\n" + extractedContent.substring(0, 500) + "...");
        //} else {
        //    System.out.println("Contenu extrait :\n" + extractedContent);
        //}
        return content.toString();
    }
    public void Searcher(String expression_recherche) throws IOException {
        // Ouverture de l'index
        IndexSearcher index_cherche = new IndexSearcher(repertoire_index);

        // Création du parser de requêtes
        this.analyseur = new StandardAnalyzer();
        QueryParser parser = new QueryParser("contenu", analyseur); // Assurez-vous que le champ "content" est correct

        // Création de la requête
        Query query = null;
        try {
            query = parser.parse(expression_recherche); // Parse la requête
        } catch (org.apache.lucene.queryParser.ParseException e) {
            System.err.println("Erreur de syntaxe dans la requête: " + e.getMessage());
            return;
        }

        // Exécution de la recherche et récupération des résultats sous forme de TopDocs
        TopDocs topDocs = index_cherche.search(query, resultats_par_page);

        // Affichage des résultats
        int totalHits = topDocs.scoreDocs.length; // Taille des résultats trouvés
        for (int i = 0; i < totalHits; i++) { // Utilisez totalHits au lieu de topDocs.totalHits
            Document doc = index_cherche.doc(topDocs.scoreDocs[i].doc); // Récupère le document correspondant
            System.out.println((i + 1) + ". " + doc.get("titre"));
        }

        // Fermeture du searcher
        index_cherche.close();
    } 
    public List<String> Searcher_2(String expression_recherche) throws IOException {
    	List<String> list_articles = null;
    	
        // Ouverture de l'index
        IndexSearcher index_cherche = new IndexSearcher(repertoire_index);

        // Création du parser de requêtes
        this.analyseur = new StandardAnalyzer();
        QueryParser parser = new QueryParser("contenu", analyseur); // Assurez-vous que le champ "content" est correct

        // Création de la requête
        Query query = null;
        try {
            query = parser.parse(expression_recherche); // Parse la requête
            list_articles = new ArrayList<>();
        } catch (org.apache.lucene.queryParser.ParseException e) {
            System.err.println("Erreur de syntaxe dans la requête: " + e.getMessage());
            return list_articles;
        }

        // Exécution de la recherche et récupération des résultats sous forme de TopDocs
        TopDocs topDocs = index_cherche.search(query, resultats_par_page);

        // Affichage des résultats
        int totalHits = topDocs.scoreDocs.length; // Taille des résultats trouvés
        for (int i = 0; i < totalHits; i++) { // Utilisez totalHits au lieu de topDocs.totalHits
            Document doc = index_cherche.doc(topDocs.scoreDocs[i].doc); // Récupère le document correspondant
            //System.out.println((i + 1) + ". " + doc.get("titre"));
            
            list_articles.add(doc.get("titre"));
        }

        // Fermeture du searcher
        index_cherche.close();
        
        return list_articles;
    }
    public  List<Map<String, String>> Searcher_3(String expression_recherche) throws IOException {
    	List<Map<String, String>> list_articles = new ArrayList<>();
    	
        // Ouverture de l'index
        IndexSearcher index_cherche = new IndexSearcher(repertoire_index);

        // Création du parser de requêtes
        this.analyseur = new StandardAnalyzer();
        QueryParser parser = new QueryParser("contenu", analyseur); // Assurez-vous que le champ "content" est correct

        // Création de la requête
        Query query = null;
        try {
            query = parser.parse(expression_recherche); // Parse la requête
            list_articles = new ArrayList<>();
        } catch (org.apache.lucene.queryParser.ParseException e) {
            System.err.println("Erreur de syntaxe dans la requête: " + e.getMessage());
            return list_articles;
        }

        // Exécution de la recherche et récupération des résultats sous forme de TopDocs
        TopDocs topDocs = index_cherche.search(query, resultats_par_page);

        // Affichage des résultats
        int totalHits = topDocs.scoreDocs.length; // Taille des résultats trouvés
        for (int i = 0; i < totalHits; i++) { // Utilisez totalHits au lieu de topDocs.totalHits
            Document doc = index_cherche.doc(topDocs.scoreDocs[i].doc); // Récupère le document correspondant
            //System.out.println((i + 1) + ". " + doc.get("titre"));
            
            Map<String, String> article = new HashMap<>();
            article.put("titre", doc.get("titre"));
            article.put("contenu", doc.get("contenu")); // Assurez-vous que le champ "contenu" existe
            list_articles.add(article);
        }

        // Fermeture du searcher
        index_cherche.close();
        
        return list_articles;
    }
    
    public static void main(String[] args) {
        
    /////// 1. Tester le compilation du code.
        System.out.println("My First Java Program.");
        Articles_indexation art = new Articles_indexation();


    ////// 7. Indexer Les Documents.
//         List<Document> list_documents = new ArrayList<>();
//         List<String> list_files =  listerFichiers();
//
//         for (String string : list_files) {
//             String content = extractInfoContentPDF(Articles_indexation.pdfDirPath+ "\\"+string);          
//             Document doc = art.createArticles_2(content, string);
//            
//             // Ajout du document à la liste
//             list_documents.add(doc);
//         }
//
//        
//         for (Document d : list_documents) {
//             System.out.println("Le Document " + d.get("titre") + " est indexé.");
//            
//             try {
//                 art.indexArticles(d); // Appelez indexArticles dans un bloc try-catch
//             } catch (Exception e) {
//                 // Affiche l'exception en cas d'erreur lors de l'indexation
//                 System.out.println("Erreur lors de l'indexation du document : " + e.getMessage());
//                e.printStackTrace();
//            }
//         }
         
         
        

    ////// 8. Fait la recherche.
        try{
            //String expression_recherche = "Hadoop"; // Expression à rechercher

            // 1. Recherche par sujet principal :
            //String expression_recherche = "online proctoring";

            // 2. Recherche par type de détection ou technologie :
            //String expression_recherche = "cheating detection";

            // 3. Recherche par combinaison de mots-clés :
            //String expression_recherche = "AI and privacy in online exams";

            // 4. Recherche par types d'anomalies
            //String expression_recherche = "abnormal behavior detection";

            //5. Recherche par étude géographique :
            String expression_recherche = "Indonesia data protection"; 
            
            
            List<String> list_articles_2 = art.Searcher_2(expression_recherche);
            for (String article : list_articles_2) {
                System.out.println("***** Titre: " + article);
            }

        } catch (IOException e) {
            // Gestion des exceptions
            System.err.println("Erreur lors de la recherche : " + e.getMessage());
        }

        
        
        
        
        
  /////// 2. Tester l'indexation.
        //simple_index();

    ///// 3. Lister les fichier PDF qui existe dans le Repertoire pdfDirPath.
//         List<String> list_files =  listerFichiers();
//         for (String string : list_files) {
//            System.out.println("=====> "+string);
//            //System.out.println(PDFExtractor.pdfDirPath+ "\\"+string);
//            //extractInfoContentPDF(PDFExtractor.pdfDirPath+ "\\"+string);
//         }

    /////// 4. Extraire le contenu du fichier.
//        List<String> list_files =  listerFichiers();
//        for (String string : list_files) {
//            System.out.println("=====> "+string);
//            extractInfoContentPDF(Articles_indexation.pdfDirPath+ "\\"+string);
//        }

    /////// 5. Creation un simple Objet Document.
//        Document doc = art.createArticles_2("This is a simple Article", "Doc 1");
//        System.out.println("Title: " + doc.get("titre")); // Remplacez "title" par le nom exact du champ
//        System.out.println("Content: " + doc.get("contenu"));

    ////// 6. Creation les Objets Document.
//        List<String> list_files =  listerFichiers();
//        int i = 0;
//         for (String string : list_files) {
//             String content = extractInfoContentPDF(Articles_indexation.pdfDirPath+ "\\"+string);
//             //System.out.println(content);
//             //System.out.println(num_doc+""+i);           
//             Document doc = art.createArticles_2(content, string);
//             System.out.println(doc.get("titre"));
//             i++;
//         }
//         System.out.println("i = "+i);
        
    }
    
}


