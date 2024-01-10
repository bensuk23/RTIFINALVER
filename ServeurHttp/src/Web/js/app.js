(function() {
    console.log("Ceci est une fonction auto-exécutante !");
    updateTable();
})();
/*document.getElementById('add').addEventListener("click",function(e) {
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function()
    {
        console.log(this);
        if (this.readyState == 4 && this.status == 201)
        {
            console.log(this.response);
            miseAJourTable();
            var monTexte = document.getElementById("monTexte");
            monTexte.innerHTML = this.responseText;
        }
        else if (this.readyState == 4) {
            alert("Une erreur est survenue...");
        }
    };
    xhr.open("POST","http://localhost:8080/api/tasks",true);
    xhr.responseType = "text";
    xhr.setRequestHeader("Content-type","text/plain");
    var body = document.getElementById('description').value;
    xhr.send(body);
    document.getElementById('description').value = "";
    document.getElementById('id').value = "";
});*/

document.addEventListener('DOMContentLoaded', function () {
    // Récupération du tableau et ajout d'un gestionnaire d'événements de clic
    var maTable = document.getElementById('maTable');
    maTable.addEventListener('click', function (event) {
        // Vérification si l'élément cliqué est une cellule du tableau
        if (event.target.tagName === 'TD') {

            var selectedRow = event.target.parentNode;

            // Vérifier si la ligne est déjà sélectionnée
            var isAlreadySelected = selectedRow.classList.contains('selected');

            // Supprimer la classe "selected" de toutes les lignes dans la table
            var allRows = document.querySelectorAll('#maTable tr');
            allRows.forEach(function (row) {
                row.classList.remove('selected');
            });

            // Ajouter ou supprimer la classe "selected" en fonction de l'état actuel
            if (!isAlreadySelected) {
                selectedRow.classList.add('selected');
            }
            // Récupération des valeurs de la ligne cliquée

            var id = event.target.parentNode.cells[0].textContent;
            var articles = event.target.parentNode.cells[1].textContent;
            var prix = event.target.parentNode.cells[2].textContent;
            var quantite = event.target.parentNode.cells[3].textContent;

            // Mise à jour des champs de texte
            document.getElementById('id').value = id;
            document.getElementById('Articles').value = articles;
            document.getElementById('Prix').value = prix;
            document.getElementById('quantite').value = quantite;


            var imagePath = "../images/"+articles + ".jpg";

            // Mettre à jour l'image avec le nouveau chemin
            document.getElementById('img').src = imagePath;

        }
    });

    document.getElementById('SubmitBtn').addEventListener("click", function (e) {
        e.preventDefault(); // Prevents the default form submission behavior

        // Create a new XMLHttpRequest
        var xhr = new XMLHttpRequest();

        xhr.onreadystatechange = function () {
            if (this.readyState == 4) {
                if (this.status == 201) {
                    console.log("Article ajouté avec succès !");
                    // Call the updateTable function to refresh the displayed data
                    updateTable();
                } else {
                    console.error("Erreur lors de l'ajout de l'article");
                }
            }
        };

        xhr.open("POST", "http://localhost:8080/api/stock", true);
        xhr.setRequestHeader("Content-type", "application/json");

        // Create an object with the data from the form
        var id = document.getElementById("id").value;
        var intitule = document.getElementById("Articles").value;
        var prix = document.getElementById("Prix").value;
        var stock = document.getElementById("quantite").value;

        // Créer un objet JSON
        var jsonObject = {
            "id": parseInt(id),
            "intitule": intitule,
            "prix": parseFloat(prix),
            "stock": parseInt(stock)
        };

        // Convertir l'objet JSON en chaîne JSON
        var jsonString = JSON.stringify(jsonObject);

        // Afficher la chaîne JSON dans la console (à des fins de vérification)
        console.log(jsonString);

        // Convert the object to JSON and send it in the request body

        xhr.send(jsonString);

    });
});

function updateTable()
{
    var xhr = new XMLHttpRequest();

    xhr.onreadystatechange = function() {


        console.log(this);
        if (this.readyState == 4 && this.status == 200) {
            //console.log(this.responseText);
            articles = this.response;
            videTable();
            articles.forEach(article => {
                ajouteLigne(article.id, article.intitule, article.prix, article.stock);
            });
        } else if (this.readyState == 4 && this.status == 404) {
            console.log("Erreur 404");
        }
    };
    xhr.open("GET", "http://localhost:8080/api/stock", true);
    xhr.responseType = "json";
    xhr.send();
}
function ajouteLigne(id,intitule,prix,stock)
{
    var maTable = document.getElementById("maTable");
    // Créer une nouvelle ligne
    var nouvelleLigne = document.createElement("tr");
    // Créer des cellules
    celluleId = document.createElement("td");
    celluleId.textContent = id;
    celluleArticle = document.createElement("td");
    celluleArticle.textContent = intitule;
    cellulePrixaU = document.createElement("td");
    cellulePrixaU.textContent = prix;
    celluleQuantite = document.createElement("td");
    celluleQuantite.textContent = stock;

    // Ajouter les cellules à la ligne
    nouvelleLigne.appendChild(celluleId);
    nouvelleLigne.appendChild(celluleArticle);
    nouvelleLigne.appendChild(cellulePrixaU);
    nouvelleLigne.appendChild(celluleQuantite);
    // Ajouter la nouvelle ligne au tableau
    maTable.appendChild(nouvelleLigne);
}
function videTable()
{
    var maTable = document.getElementById("maTable");
    while (maTable.rows.length > 1) {
        maTable.deleteRow(-1); // supprimer dernière ligne
    }
}