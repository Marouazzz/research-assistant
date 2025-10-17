console.log("Research Assistant background service worker loaded.");

// Listen for messages from side panel
chrome.runtime.onMessage.addListener((message, sender, sendResponse) => {
    if (message.action === "saveNotes") {
        // Save notes to Chrome storage using key 'notes'
        chrome.storage.local.set({ notes: message.notes }, () => {
            console.log("Notes saved:", message.notes);
            sendResponse({ status: "saved" });
        });
        return true; // Keep the message channel open for async response
    } else if (message.action === "getNotes") {
        // Retrieve notes from storage
        chrome.storage.local.get("notes", (data) => {
            sendResponse({ notes: data.notes || "" });
        });
        return true;
    }
});


chrome.runtime.onInstalled.addListener(() => {
  console.log("Research Assistant installed.");
});

// When the extension icon is clicked
chrome.action.onClicked.addListener(async (tab) => {
  try {
    await chrome.sidePanel.open({ tabId: tab.id });
    console.log("Side panel opened!");
  } catch (e) {
    console.error("Failed to open side panel:", e);
  }
});