/**
 * Created by strapper on 01.09.15.
 */
function clearFilePath() {
    document.getElementById("file-input").value = "";
 //   console.log("Changed path to " +  document.getElementById("file-input").value);

}

function sendFile(contents) {
    var request = new XMLHttpRequest();
//    request.open('POST', 'http://localhost:8081/rest/send',false);
	request.open('POST', '/rest/send',false);
    request.send(contents);

 //   console.log(request.status);

    request.onreadystatechange = function() { // (3)
        if (request.readyState != 4) return;
    }
//    return request;

}



function sendLanguage() {
    var radios = document.getElementsByName('lang');
    var lang;

    for (var i = 0, length = radios.length; i < length; i++) {
        if (radios[i].checked) {
            // do whatever you want with the checked radio
            lang = radios[i].getAttribute("value");
        }
    }

    var request = new XMLHttpRequest();
    request.open('POST', '/rest/lang',false);
    request.send(lang);
 //   console.log(request.status);
//    return request;

    request.onreadystatechange = function() { // (3)
        if (request.readyState != 4) return;
    }
}

function findFormat(fileName) {
    var length = fileName.length;
    var format = fileName.substring(length-3, length);

//    console.log( "Format " + format);

    if(format != "fb2" && format != "txt" && format != "pdf") {
        return "formatError";
    }


    var request = new XMLHttpRequest();
    request.open('POST', '/rest/format', false);
    request.send(format);

  //  return format;
    request.onreadystatechange = function() { // (3)
        if (request.readyState != 4) return;
    }
}

function readSingleFile(e) {
 //   console.log("path " + document.getElementById("file-input").value);
    if(document.getElementById("file-input").value == "") {
        return;
    }

    var format = findFormat(document.getElementById("file-input").value);
 //   console.log(format);
    if(format == "formatError") {
        alert("File format error \n50 words works with .fb2 and .txt ebooks");
        return;
    }

    changeProgressVisivility();


    var file = e.target.files[0];
    if (!file) {
        return;
    }
    var reader = new FileReader();
   // reader.readAsText(file, 'ISO-8859-1');
  //  reader.readAsArrayBuffer(file);
    reader.onload = function(e) {
        var contents = e.target.result;
        sendFile(contents);
        sendLanguage();
        httpGet();
        hideAbout();
        getBookLang();
        changeProgressVisivility();
    };
   // reader.readAsText(file);
    reader.readAsArrayBuffer(file);
    clearFilePath();
}



function getInfo() {
    var item = document.getElementById("text");
    item.innerHTML = "The text sd sd"
}

function httpGet()
{
    var theUrl = "/rest/hello";
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.open( "GET", theUrl, false ); // false for synchronous request
    xmlHttp.send( null );

    xmlHttp.onreadystatechange = function() { // (3)
        if (xmlHttp.readyState != 4) return;
    }
    var item = document.getElementById("text");
 //   console.log(xmlHttp.responseText)
    var arr = JSON.parse(xmlHttp.responseText);
    tableCreate(arr);
}

function getBookLang()
{
    var theUrl = "/rest/bookLanguage";
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.open( "GET", theUrl, false ); // false for synchronous request
    xmlHttp.send( null );

    xmlHttp.onreadystatechange = function() { // (3)
        if (xmlHttp.readyState != 4) return;
    }
    var item = document.getElementsByClassName("bookLang")[0];
    item.innerHTML = "The book " + document.getElementById("file-input").value + " is written in " + xmlHttp.responseText + ". These are the most popular words in this book."
}

function deleteRows() {
    var list = document.getElementsByTagName("tr");
    for(var i = list.length - 1; i >= 0; i--) {
        list[i].parentNode.removeChild(list[i]);
    }
}

function tableCreate(array) {
    deleteRows();
    var tbl = document.getElementById('word_table');
    var tbody = tbl.getElementsByTagName("tbody")[0];
    var thead = tbl.getElementsByTagName("thead")[0];

    var headerRow = document.createElement('tr');
    var header1 = document.createElement('th')
    header1.innerHTML = "Word";
    var header2 = document.createElement('th')
    header2.innerHTML = "Translation";
    var header3 = document.createElement('th')
    header3.innerHTML = "Frequency";

    headerRow.appendChild(header1);
    headerRow.appendChild(header2);
    headerRow.appendChild(header3);

    if(array[0].definition != "null") {
        var header4 = document.createElement('th')
        header4.innerHTML = "Definition";
        headerRow.appendChild(header4);
    }

    thead.appendChild(headerRow);

    for(var i = 0; i < array.length; i++) {
        var row = document.createElement('tr');
        var word_td = document.createElement('td');
        word_td.className = "word";
        word_td.innerHTML = array[i].key;
        var translation_td = document.createElement('td');
        translation_td.innerHTML = array[i].value;
        var frequency_td = document.createElement('td');
        frequency_td.innerHTML = array[i].frequency;

        row.appendChild(word_td);
        row.appendChild(translation_td);
        row.appendChild(frequency_td);

  //      console.log(array[i].definition);


        if(array[i].definition != "null") {
            var definition_td = document.createElement('td');
            definition_td.innerHTML = formatDefinition(array[i].definition);
            row.appendChild(definition_td);
        }

        tbody.appendChild(row);
    }
}

function formatDefinition(text) {
    var text2 = "";
    var setOfNumbers = new Set(["1", "2", "3", "4", "5", "6", "7", "8", "9"]);

    for(var i = 0; i < text.length; i++) {
        var char = text[i];
        if(setOfNumbers.has(char)) {
            //when meet a number, concat two chars at an iteration
            text2 = text2.concat("<br>");
            text2 = text2.concat(char);
            i++;
            char = text[i];
            text2 = text2.concat(char);
        } else {
            text2 = text2.concat(char);
        }
    }

   // if(text2.indexOf("|") > -1) {
        var begin = text2.indexOf("#", 0);
        var end = text2.indexOf("#", begin + 1);

        text2 = text2.substring(0,begin) + "<span>" + text2.substring(begin + 1,end) + "</span>" +text2.substring(end + 1,text2.length) ;
  //      console.log("add span" + begin + " " + end)
  //  }

   // console.log(text2);

    return text2;
}

function hideAbout() {
    var aboutDiv = document.getElementById("about");
    aboutDiv.style.display = 'none';
}

function changeProgressVisivility() {
    var progressDiv = document.getElementsByClassName("progressBar")[0];
    var visibility = progressDiv.style.visibility;

    if(visibility == "visible") {
        progressDiv.style.visibility = "hidden";
    } else {
        progressDiv.style.visibility = "visible";
    }
}



