// =============================================================================
// 1. AUTH / PAGE SETUP
// =============================================================================
requireAuth("ADMIN");
$('#welcomeText').text("Hi, " + getUserName());


// =============================================================================
// 2. CORE ENGINE
// =============================================================================
const CRUD = {
    branch:         { endpoint: "/branch",          fields: ["branchName", "address", "contactNumber"] },
    stationType:   { endpoint: "/station-type",    fields: ["typeName", "hourlyRate"] },
    station:       { endpoint: "/station",         fields: ["stationCode", "branchId", "stationTypeId", "status"] },
    game:          { endpoint: "/game",            fields: ["gameName", "genre", "ageRating"] },
    snackCategory: { endpoint: "/snack-category",  fields: ["categoryName"] },
    snack:         { endpoint: "/snack",           fields: ["name", "snackCategoryId", "price", "stockQty"] }
};

const CRUD_LABEL = {
    branch: "Branches",
    stationType: "Station Types",
    station: "Stations",
    game: "Games",
    snackCategory: "Snack Categories",
    snack: "Snacks"
};

const SECTIONS = [
    { key: "branch", label: "Branches" },
    { key: "stationType", label: "Station Types" },
    { key: "station", label: "Stations" },
    { key: "game", label: "Games" },
    { key: "stationGame", label: "Station-Game Links" },
    { key: "snackCategory", label: "Snack Categories" },
    { key: "snack", label: "Snacks" }
];

const ROW_CACHE = {};

const ROW_RENDERERS = {};

const INACTIVE_VISIBLE = {};

const INACTIVE_ROW_RENDERERS = {};

const INACTIVE_ROW_CACHE = {};

function showSection(key) {
    $('.section').removeClass('active');
    $('.section[data-section="' + key + '"]').addClass('active');
    $('.sidebar button').removeClass('active');
    $('.sidebar button[data-key="' + key + '"]').addClass('active');
}

$('#sidebarNav').html(SECTIONS.map(function (s, i) {
    return '<button data-key="' + s.key + '" class="' + (i === 0 ? 'active' : '') + '" onclick="showSection(\'' + s.key + '\')">' + s.label + '</button>';
}).join(''));

function showCrudError(key, xhr) {
    let text = "Something went wrong. Please try again.";
    const body = xhr.responseJSON && xhr.responseJSON.body;
    if (body && typeof body === 'object') {
        text = Object.values(body).join(" | ");
    } else if (typeof body === 'string') {
        text = body;
    } else if (xhr.responseJSON && xhr.responseJSON.message) {
        text = xhr.responseJSON.message;
    }
    $('#' + key + '-success').hide();
    $('#' + key + '-error').text(text).show();
}

function showCrudSuccess(key, text) {
    $('#' + key + '-error').hide();
    $('#' + key + '-success').text(text).show();
}

// ---- Generic Save (handles both create and update)
function crudSave(key) {
    const cfg = CRUD[key];
    const dto = { id: $('#' + key + '-id').length ? ($('#' + key + '-id').val() || null) : null };

    cfg.fields.forEach(function (f) {
        const el = $('#' + key + '-' + f);
        let val = el.val();
        if (el.attr('type') === 'number') val = val === '' ? null : Number(val);
        dto[f] = val;
    });

    $.ajax({
        url: API_BASE + cfg.endpoint,
        type: dto.id ? "PUT" : "POST",
        contentType: "application/json",
        data: JSON.stringify(dto),
        success: function () {
            showCrudSuccess(key, dto.id ? "Updated successfully." : "Added successfully.");
            crudResetForm(key);
            loadCrud(key);
        },
        error: function (xhr) { showCrudError(key, xhr); }
    });
}

function crudResetForm(key) {
    const cfg = CRUD[key];
    $('#' + key + '-id').remove();
    cfg.fields.forEach(function (f) { $('#' + key + '-' + f).val(''); });
    if (key === 'game') $('#game-ageRating').val('T');
    if (key === 'station') $('#station-status').val('AVAILABLE');
}

function crudEdit(key, id) {
    const cfg = CRUD[key];
    const row = ROW_CACHE[key][id];
    if ($('#' + key + '-id').length === 0) {
        $('<input type="hidden" id="' + key + '-id">').appendTo('body');
    }
    $('#' + key + '-id').val(row.id);
    cfg.fields.forEach(function (f) {
        if (key === 'employee' && f === 'password') return;
        $('#' + key + '-' + f).val(row[f]);
    });
    $('html, body').animate({ scrollTop: 0 }, 200);
}

function crudDelete(key, id) {
    if (!confirm("Delete this record? This cannot be undone.")) return;
    $.ajax({
        url: API_BASE + CRUD[key].endpoint + "/" + id,
        type: "DELETE",
        success: function () { showCrudSuccess(key, "Deleted."); loadCrud(key); },
        error: function (xhr) { showCrudError(key, xhr); }
    });
}

function loadCrud(key) {
    $.get(API_BASE + CRUD[key].endpoint, function (response) {
        renderTable(key, response.body);
    });
}

function actionBtns(key, row) {
    return '<button class="secondary" onclick="crudEdit(\'' + key + '\', ' + row.id + ')">Edit</button>' +
           '<button class="danger" onclick="crudDelete(\'' + key + '\', ' + row.id + ')">Delete</button>';
}

function renderTable(key, rows) {
    const body = $('#' + key + '-body');
    ROW_CACHE[key] = {};
    (rows || []).forEach(function (r) { ROW_CACHE[key][r.id] = r; });

    if (!rows || rows.length === 0) {
        body.html('<tr><td colspan="8" style="color:var(--text-dim)">No records yet.</td></tr>');
        return;
    }
    body.html(ROW_RENDERERS[key](rows));
}

function toggleInactive(key) {
    INACTIVE_VISIBLE[key] = !INACTIVE_VISIBLE[key];
    $('#' + key + '-inactive-table').toggle(INACTIVE_VISIBLE[key]);
    $('#' + key + '-inactive-toggle').text((INACTIVE_VISIBLE[key] ? 'Hide' : 'Show') + ' Deleted ' + (CRUD_LABEL[key] || key));
    if (INACTIVE_VISIBLE[key]) loadInactive(key);
}

function loadInactive(key) {
    $.get(API_BASE + CRUD[key].endpoint + "/inactive", function (response) {
        renderInactiveTable(key, response.body);
    });
}

function crudRestore(key, id) {
    $.ajax({
        url: API_BASE + CRUD[key].endpoint + "/" + id + "/restore",
        type: "PUT",
        success: function () {
            showCrudSuccess(key, "Restored.");
            loadCrud(key);
            loadInactive(key);
        },
        error: function (xhr) { showCrudError(key, xhr); }
    });
}

function restoreBtn(key, row) {
    return '<button class="secondary" onclick="crudRestore(\'' + key + '\', ' + row.id + ')">Restore</button>';
}

function renderInactiveTable(key, rows) {
    const body = $('#' + key + '-inactive-body');
    INACTIVE_ROW_CACHE[key] = {};
    (rows || []).forEach(function (r) { INACTIVE_ROW_CACHE[key][r.id] = r; });

    if (!rows || rows.length === 0) {
        body.html('<tr><td colspan="8" style="color:var(--text-dim)">No deleted records.</td></tr>');
        return;
    }
    if (!INACTIVE_ROW_RENDERERS[key]) return;
    body.html(INACTIVE_ROW_RENDERERS[key](rows));
}

// =============================================================================
// 3. ENTITY SECTIONS
// =============================================================================

// ---- BRANCH --------------------------------------------------------------
ROW_RENDERERS.branch = function (rows) {
    return rows.map(r => '<tr><td>' + r.branchName + '</td><td>' + r.address + '</td><td>' + r.contactNumber + '</td><td>' + actionBtns('branch', r) + '</td></tr>').join('');
};
INACTIVE_ROW_RENDERERS.branch = function (rows) {
    return rows.map(r => '<tr><td>' + r.branchName + '</td><td>' + r.address + '</td><td>' + r.contactNumber + '</td><td>' + restoreBtn('branch', r) + '</td></tr>').join('');
};

// ---- STATION TYPE ----------------------------------------------------------
ROW_RENDERERS.stationType = function (rows) {
    return rows.map(r => '<tr><td>' + r.typeName + '</td><td>Rs. ' + r.hourlyRate + '</td><td>' + actionBtns('stationType', r) + '</td></tr>').join('');
};
INACTIVE_ROW_RENDERERS.stationType = function (rows) {
    return rows.map(r => '<tr><td>' + r.typeName + '</td><td>Rs. ' + r.hourlyRate + '</td><td>' + restoreBtn('stationType', r) + '</td></tr>').join('');
};

// ---- STATION --------------------------------------------------------------

ROW_RENDERERS.station = function (rows) {
    return rows.map(r => '<tr><td>' + r.stationCode + '</td><td>' + r.branchName + '</td><td>' + r.typeName + '</td><td>Rs. ' + r.hourlyRate + '</td><td>' + r.status + '</td><td>' + actionBtns('station', r) + '</td></tr>').join('');
};
INACTIVE_ROW_RENDERERS.station = function (rows) {
    return rows.map(r => '<tr><td>' + r.stationCode + '</td><td>' + r.branchName + '</td><td>' + r.typeName + '</td><td>Rs. ' + r.hourlyRate + '</td><td>' + r.status + '</td><td>' + restoreBtn('station', r) + '</td></tr>').join('');
};

// ---- GAME ---------------------------------------------------------------
ROW_RENDERERS.game = function (rows) {
    return rows.map(r => '<tr><td>' + r.gameName + '</td><td>' + r.genre + '</td><td>' + r.ageRating + '</td><td>' + actionBtns('game', r) + '</td></tr>').join('');
};
INACTIVE_ROW_RENDERERS.game = function (rows) {
    return rows.map(r => '<tr><td>' + r.gameName + '</td><td>' + r.genre + '</td><td>' + r.ageRating + '</td><td>' + restoreBtn('game', r) + '</td></tr>').join('');
};

// ---- SNACK CATEGORY --------------------------------------------------------
ROW_RENDERERS.snackCategory = function (rows) {
    return rows.map(r => '<tr><td>' + r.categoryName + '</td><td>' + actionBtns('snackCategory', r) + '</td></tr>').join('');
};
INACTIVE_ROW_RENDERERS.snackCategory = function (rows) {
    return rows.map(r => '<tr><td>' + r.categoryName + '</td><td>' + restoreBtn('snackCategory', r) + '</td></tr>').join('');
};

// ---- SNACK ------------------------------------------------------------------
ROW_RENDERERS.snack = function (rows) {
    return rows.map(r => '<tr><td>' + r.name + '</td><td>' + r.categoryName + '</td><td>Rs. ' + r.price + '</td><td>' + r.stockQty + '</td><td>' + actionBtns('snack', r) + '</td></tr>').join('');
};
INACTIVE_ROW_RENDERERS.snack = function (rows) {
    return rows.map(r => '<tr><td>' + r.name + '</td><td>' + r.categoryName + '</td><td>Rs. ' + r.price + '</td><td>' + r.stockQty + '</td><td>' + restoreBtn('snack', r) + '</td></tr>').join('');
};

function loadSnackDropdownsThenTable() {
    fillSelect('#snack-snackCategoryId', '/snack-category', 'id', r => r.categoryName)
        .then(function () { loadCrud('snack'); });
}

function loadStationDropdownsThenTable() {
    fillSelect('#station-branchId', '/branch', 'id', r => r.branchName);
    fillSelect('#station-stationTypeId', '/station-type', 'id', r => r.typeName + ' (Rs. ' + r.hourlyRate + '/hr)')
        .then(function () { loadCrud('station'); });
}

function fillSelect(selector, endpoint, valueField, labelFn) {
    return $.get(API_BASE + endpoint, function (response) {
        const options = response.body.map(function (r) {
            return '<option value="' + r[valueField] + '">' + labelFn(r) + '</option>';
        });
        $(selector).html(options.join(''));
    });
}

// ---- STATION-GAME LINKS ---------------------------------------------------

function loadStationGameSection() {
    fillSelect('#stationGame-stationId', '/station', 'id', r => r.stationCode);
    fillSelect('#stationGame-gameId', '/game', 'id', r => r.gameName);
    $.get(API_BASE + "/station-game", function (response) {
        const rows = response.body;
        if (rows.length === 0) {
            $('#stationGame-body').html('<tr><td colspan="3" style="color:var(--text-dim)">No links yet.</td></tr>');
            return;
        }
        $('#stationGame-body').html(rows.map(r =>
            '<tr><td>' + r.stationCode + '</td><td>' + r.gameName + '</td><td>' +
            '<button class="danger" onclick="deleteStationGame(' + r.id + ')">Remove</button></td></tr>'
        ).join(''));
    });
}

function addStationGame() {
    const dto = {
        stationId: $('#stationGame-stationId').val(),
        gameId: $('#stationGame-gameId').val()
    };
    $.ajax({
        url: API_BASE + "/station-game",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify(dto),
        success: function () { showCrudSuccess('stationGame', "Game linked to station."); loadStationGameSection(); },
        error: function (xhr) { showCrudError('stationGame', xhr); }
    });
}

function deleteStationGame(id) {
    if (!confirm("Remove this game from the station ? ")) return;

    $.ajax({
        url: API_BASE + "/station-game/" + id,
        type: "DELETE",
        success: function () { loadStationGameSection(); },
        error: function () { showCrudError('stationGame', xhr); }
    });
}

let stationGameInactiveVisible = false;

function toggleStationGameInactive() {
    stationGameInactiveVisible = !stationGameInactiveVisible;
    $('#stationGame-inactive-table').toggle(stationGameInactiveVisible);
    $('#stationGame-inactive-toggle').text(stationGameInactiveVisible ? 'Hide Removed Links' : 'Show Removed Links');
    if (stationGameInactiveVisible) loadInactiveStationGames();
}

function loadInactiveStationGames() {
    $.get(API_BASE + "/station-game/inactive", function (response) {
        const rows = response.body;
        if (!rows || rows.length === 0) {
            $('#stationGame-inactive-body').html('<tr><td colspan="3" style="color:var(--text-dim)">No removed links.</td></tr>');
            return;
        }
        $('#stationGame-inactive-body').html(rows.map(r =>
            '<tr><td>' + r.stationCode + '</td><td>' + r.gameName + '</td><td>' +
            '<button class="secondary" onclick="restoreStationGame(' + r.id + ')">Restore</button></td></tr>'
        ).join(''));
    });
}

function restoreStationGame(id) {
    $.ajax({
        url: API_BASE + "/station-game/" + id + "/restore",
        type: "PUT",
        success: function () {
            showCrudSuccess('stationGame', "Restored.");
            loadStationGameSection();
            loadInactiveStationGames();
        },
        error: function (xhr) { showCrudError('stationGame', xhr); }
    });
}

// =============================================================================
// PAGE INIT - load every simple CRUD table, plus the special screens
// =============================================================================
Object.keys(CRUD).forEach(function (key) {
    if (key !== 'station' && key !== 'snack') loadCrud(key);
});

loadStationDropdownsThenTable();
loadStationGameSection();
loadSnackDropdownsThenTable();