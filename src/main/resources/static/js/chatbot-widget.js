$(function () {
    const widgetHtml = `
        <div id="chatToggleBtn" style="position:fixed; bottom:24px; right:24px; width:56px; height:56px;
             border-radius:50%; background:#6c5ce7; color:white; display:flex; align-items:center;
             justify-content:center; font-size:26px; cursor:pointer; box-shadow:0 4px 14px rgba(0,0,0,0.4); z-index:999;">
            🎮
        </div>

        <div id="chatPanel" style="display:none; position:fixed; bottom:92px; right:24px; width:320px;
             max-height:440px; background:#171a23; border:1px solid #2a2e3a; border-radius:10px;
             flex-direction:column; overflow:hidden; z-index:999;">
            <div style="padding:12px 16px; background:#1f2330; border-bottom:1px solid #2a2e3a; font-weight:600; font-size:14px;">
                Gaming Lounge Assistant
            </div>
            <div id="chatMessages" style="flex:1; overflow-y:auto; padding:12px; font-size:13px; max-height:280px;"></div>
            <div style="padding:8px; border-top:1px solid #2a2e3a; display:flex; gap:6px; flex-wrap:wrap;">
                <button class="chat-chip" data-q="What's the hourly rate?">Rates</button>
                <button class="chat-chip" data-q="What snacks do you have?">Snacks</button>
                <button class="chat-chip" data-q="What are your opening hours?">Hours</button>
            </div>
            <div style="display:flex; padding:10px; gap:6px; border-top:1px solid #2a2e3a;">
                <input type="text" id="chatInput" placeholder="Ask something..."
                       style="flex:1; padding:8px; background:#0f1117; border:1px solid #2a2e3a; border-radius:6px; color:#e8e9ed; font-size:13px;">
                <button id="chatSendBtn" style="width:auto; padding:8px 14px;">Send</button>
            </div>
        </div>
    `;
    $('body').append(widgetHtml);

    $('#chatPanel').css('display', 'none');

    $('#chatToggleBtn').on('click', function () {
        const panel = $('#chatPanel');
        if (panel.is(':visible')) {
            panel.hide();
        } else {
            panel.css('display', 'flex');
            if ($('#chatMessages').children().length === 0) {
                addBotMessage("Hi! I'm the Gaming Lounge Assistant. Ask me about station rates, " +
                        "games, snacks, bookings, membership plans, or opening hours.");
            }
        }
    });

    $('#chatSendBtn').on('click', sendChatMessage);
    $('#chatInput').on('keypress', function (e) {
        if (e.which === 13) sendChatMessage();
    });
    $(document).on('click', '.chat-chip', function () {
        sendChatMessage($(this).data('q'));
    });

    function sendChatMessage(presetMessage) {
        const message = presetMessage || $('#chatInput').val().trim();
        if (!message) return;

        addUserMessage(message);
        $('#chatInput').val('');

        $.ajax({
            url: API_BASE + "/chatbot/ask",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify({ message: message }),
            success: function (response) {
                addBotMessage(response.body.reply);
            },
            error: function () {
                addBotMessage("Sorry, something went wrong. Please try again.");
            }
        });
    }

    function addUserMessage(text) {
        $('#chatMessages').append(
                '<div style="text-align:right; margin-bottom:8px;">' +
                '<span style="background:#6c5ce7; color:white; padding:6px 10px; border-radius:10px; display:inline-block; max-width:80%;">' +
                escapeHtml(text) + '</span></div>');
        scrollChatToBottom();
    }

    function addBotMessage(text) {
        $('#chatMessages').append(
                '<div style="text-align:left; margin-bottom:8px;">' +
                '<span style="background:#2a2e3a; padding:6px 10px; border-radius:10px; display:inline-block; max-width:80%;">' +
                escapeHtml(text) + '</span></div>');
        scrollChatToBottom();
    }

    function scrollChatToBottom() {
        const el = document.getElementById('chatMessages');
        el.scrollTop = el.scrollHeight;
    }

    function escapeHtml(text) {
        return $('<div>').text(text).html();
    }
});
