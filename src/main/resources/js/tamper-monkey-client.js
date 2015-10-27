// ==UserScript==
// @name         Client that connects to the dictionary service
// @namespace    http://your.homepage/
// @version      0.1
// @description  My client
// @author       Chao Pang
// @require      http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.min.js
// @require      https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js
// @require      http://code.jquery.com/ui/1.11.4/jquery-ui.js

// ==/UserScript==

$(document).ready(function() {

    var previous_selection = "";
    var dictionary_base_url = "http://localhost:8080/dictionary/";

    loadStyleSheet("http://code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css");

    $(document).mouseup(function() {
        var selectedText = getSelectionText();
        if(selectedText !== "" && selectedText !== previous_selection){
            previous_selection = selectedText;
            $.ajax({ 
                type: 'GET', 
                url: dictionary_base_url + selectedText, 
                success: function (data) { 
                    createPopWindow(data);
                }
            });   
        }
    });

    function createPopWindow(data){
        if(data.name.length > 0){
            var jqueryUiDialog = $('#word-dialog');
            if(jqueryUiDialog.length === 0){
                jqueryUiDialog = $('<div id="word-dialog"></div>');
                $('body').append(jqueryUiDialog);
            }
            jqueryUiDialog.empty();

            var pronunciationAudio = createPronunciationAudio(data);
            if(pronunciationAudio){
                jqueryUiDialog.append(pronunciationAudio);
            }

            var dialogBody = $('<div />').appendTo(jqueryUiDialog);

            $.map(collectWordDefinitionsByType(data), function(definitions, wordType){
                var typeDiv = $('<div />').append(wordType).css('font-weight', 'bold');
                dialogBody.append(typeDiv).append('</br>');
                $.each(definitions, function(index, definition){
                    dialogBody.append(++index + '. ' + definition.definition + '</br>');
                    if(definition.example.length > 0){
                        var exampleDiv = $('<div />').append('<i>Example:' + definition.example + '</i>').css({'text-font':'10px', 'padding-left':'20px', 'padding-top':'10px'});
                        dialogBody.append(exampleDiv);
                    }
                    dialogBody.append('</br>');
                });
                dialogBody.append('<legend />');
            });

            jqueryUiDialog.dialog({
                title : 'Word: ' + data.name,
                autoOpen: true,
                maxWidth:700,
                maxHeight: 600,
                width: 700,
                height: 500,
                modal: true,
                buttons: {
                    Cancel: function() {
                        $(this).dialog("close");
                    }
                }
            });
        }
    }

    function createPronunciationAudio(data){

        if(data.pronunciation){
            var audio = $('<audio controls></audio></br></br>');
            audio.append('<source src="' + data.pronunciation.oggSource + '" type="audio/ogg">');
            audio.append('<source src="' + data.pronunciation.mp3Source + '" type="audio/mpeg">');
            audio.append('Your browser does not support the audio element.');
            return audio;
        }
        return '';
    }

    function collectWordDefinitionsByType(data){
        var wordDefinitionsByTypes = {};
        $.each(data.wordSenses, function(index, definition){
            if(!wordDefinitionsByTypes[definition.wordType]){
                wordDefinitionsByTypes[definition.wordType] = [];
            }
            wordDefinitionsByTypes[definition.wordType].push(definition);
        });
        return wordDefinitionsByTypes;
    }

    function getSelectionText() {
        var text = "";
        if (window.getSelection) {
            text = window.getSelection().toString();
        } else if (document.selection && document.selection.type != "Control") {
            text = document.selection.createRange().text;
        }
        return text;
    }

    function loadStyleSheet(style){
        var link = window.document.createElement('link');
        link.rel = 'stylesheet';
        link.type = 'text/css';
        link.href = style;
        document.getElementsByTagName("HEAD")[0].appendChild(link);
    }
});