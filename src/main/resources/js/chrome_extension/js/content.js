//var dictionary_base_url = "http://localhost:8080/dictionary/";
var dictionary_base_url = "http://ec2-52-29-0-236.eu-central-1.compute.amazonaws.com:8080/dictionary/";

chrome.runtime.onMessage.addListener(function(request, sender, sendResponse) {
	var selectedText = request.selectionText;
	if(selectedText !== ""){
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
        var headerTable = $('<table />').appendTo(jqueryUiDialog);
        var headerRow = $('<tr />').appendTo(headerTable);
        $('<td><h3>' + data.name + '</h3></td>').appendTo(headerRow);
        $('<td />').append(createPronunciationAudio(data)).appendTo(headerRow);
        var dialogBody = $('<div />').appendTo(jqueryUiDialog);
        
        dialogBody.append('<legend />');
        $.map(collectWordDefinitionsByType(data), function(definitions, wordType){
        	$('<div />').append(wordType).css({'font-size':'15px'}).appendTo(dialogBody);
        	var listOfDefinitionTag = $('<ul />').appendTo(dialogBody);
            $.each(definitions, function(index, definition){
            	var eachDefinitionElement = $('<li />').append(definition.definition).css({'font-size':'15px'}).appendTo(listOfDefinitionTag);
                if(definition.example.length > 0){
                	eachDefinitionElement.append('<i> Example:' + definition.example + '</i>');
                }
            });
            dialogBody.append('<legend />');
        });

        jqueryUiDialog.dialog({
            title : 'English',
            autoOpen: true,
            maxWidth:550,
            maxHeight: 500,
            width: 550,
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
        var audio = $('<audio controls></audio>').css({'margin-top':'5px','margin-left':'25px'});
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