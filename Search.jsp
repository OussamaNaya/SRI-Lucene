<%@page import="org.apache.jasper.tagplugins.jstl.core.ForEach"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.List, java.util.ArrayList" %>
<%@ page import="Models.Articles_indexation" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Recherche d'Articles</title>
    <style>
        body {
            font-family: 'Arial', sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 0;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            min-height: 100vh;
        }
        h1 { color: #333; margin-bottom: 20px; }
        form { width: 100%; max-width: 600px; margin-bottom: 30px; }
        input[type="text"] { width: calc(100% - 120px); padding: 15px; margin-right: 10px; font-size: 16px; border: 1px solid #ccc; border-radius: 5px; box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); }
        button { padding: 15px 20px; font-size: 16px; background-color: #007BFF; color: white; border: none; border-radius: 5px; cursor: pointer; box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); transition: background-color 0.3s ease; }
        button:hover { background-color: #0056b3; }
        h3 {
		    background-color: #fff;
		    padding: 15px;
		    margin: 10px auto;
		    border-radius: 5px;
		    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
		    max-width: 600px;
		    text-align: left;
		    color: #444;
		    font-size: 18px;  /* Standardisation de la taille de police */
		    line-height: 1.5;  /* Espacement entre les lignes */
		}
        .no-results { color: #888; font-size: 18px; margin-top: 20px; }
        
        #pdf-container { display: none; margin-top: 20px; }
    </style>
</head>

<body>
    <h1>Recherche d'Articles</h1>

    <form action="" method="GET">
        <input type="text" name="query" placeholder="Entrez votre requête ici..." value="<%= request.getParameter("query") != null ? request.getParameter("query") : "" %>" required>
        <button type="submit">Rechercher</button>
    </form>

    <% 
        String requet = request.getParameter("query");
        if (requet != null && !requet.trim().isEmpty()) {
            Articles_indexation art = new Articles_indexation();
            List<String> list_articles = art.Searcher_2(requet);
            
            if (list_articles != null && !list_articles.isEmpty()) {
    %>
                <h1>Les articles pertinents pour la requête "<%= requet %>" sont :</h1>
                <% for (String article : list_articles) { %>
                    <h3><%= article %></h3>
                    
                    <button onclick="togglePDF('<%= article.hashCode() %>')">Afficher le PDF</button>
  
                    <div id="pdf-container-<%= article.hashCode() %>" style="display: none;">
                        <embed src="<%= request.getContextPath() %>/Articles/<%= article %>" width="600" height="400" type="application/pdf">
                    </div>
                <% } %>
    <% 
            } else { 
    %>
                <p class="no-results">Aucun article trouvé pour votre recherche.</p>
    <% 
            }
        }
    %>

    <script>
        function togglePDF(articleId) {
            const pdfContainer = document.getElementById('pdf-container-' + articleId);
            const button = event.target;

            if (pdfContainer.style.display === 'none' || pdfContainer.style.display === '') {
                pdfContainer.style.display = 'block'; // Afficher le PDF
                button.textContent = 'Cacher le PDF'; // Changer le texte du bouton
            } else {
                pdfContainer.style.display = 'none'; // Cacher le PDF
                button.textContent = 'Afficher le PDF'; // Restaurer le texte du bouton
            }
        }
    </script>

</body>
</html>
