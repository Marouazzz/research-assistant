// Function to load notes on side panel open
function loadNotes() {
    // 1. Send message to background script to get notes
    chrome.runtime.sendMessage({ action: "getNotes" }, (response) => {
        if (response && response.notes) {
            // 2. Set the textarea value using the correct ID 'notes'
            document.getElementById('notes').value = response.notes;
        }
    });
}

// Function to save notes via the background script
function saveNotes() {
    // Get notes from the correct textarea ID 'notes'
    const notes = document.getElementById('notes').value; 

    // Send a message to the background service worker
    chrome.runtime.sendMessage({ action: "saveNotes", notes: notes }, (response) => {
        if (response && response.status === "saved") {
            showResult('Notes saved successfully!');
        } else {
            showResult('Error saving notes.');
        }
    });
}


async function summarizeText() {
    try{
        // Get the active tab
        const [tab] = await chrome.tabs.query({ active: true, currentWindow: true });
        
        // Execute script to get selected text
        const [{result}] = await chrome.scripting.executeScript({
            target: { tabId: tab.id },
            function: () => window.getSelection().toString()
        });
        
        if (!result) {
            showResult('Please select text to summarize.'); 
            return; 
        }

        // Send text to local server (allowed by the updated CSP connect-src)
        const response = await fetch('http://localhost:8080/api/research/process', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                operation: "summarize",   // required field name for backend
                content: result           // selected text
            })
        });
        
        if (!response.ok) {
            throw new Error(`Server responded with status: ${response.status}`);
        }

        const text = await response.text();
        showResult(text.replace(/\n/g, '<br>'));

    }catch(error){
        showResult('Error: ' + error.message);
    }
}

function showResult(content) {
    document.getElementById('results').innerHTML= `<div class="result-item"><div class="result-content">${content}</div></div>`;
}

// Event Listeners
document.addEventListener('DOMContentLoaded', loadNotes);
document.getElementById('summarizeBtn').addEventListener('click', summarizeText);
document.getElementById('saveNotesBtn').addEventListener('click', saveNotes);