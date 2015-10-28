chrome.contextMenus.create({
    title: "Look up the word",
    contexts: ["all"],
    onclick: function(info) {
        chrome.tabs.query({
            active: true,
            currentWindow: true
        }, function(tabs) {
            chrome.tabs.sendMessage(tabs[0].id, {
                selectionText: info.selectionText
            });
        });
    }
});