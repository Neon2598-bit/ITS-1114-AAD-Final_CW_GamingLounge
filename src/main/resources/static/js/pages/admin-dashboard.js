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
    station:       { endpoint: "/station",         fields: ["stationCode", "branchId", "stationTypeId", "status"] }
};

const CRUD_LABEL = {
    branch: "Branches",
    stationType: "Station Types",
    station: "Stations"
};

const SECTIONS = [
    { key: "branch", label: "Branches" },
    { key: "stationType", label: "Station Types" },
    { key: "station", label: "Stations" }
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

// =============================================================================
// PAGE INIT - load every simple CRUD table, plus the special screens
// =============================================================================
Object.keys(CRUD).forEach(function (key) {
    if (key !== 'station' && key !== 'snack') loadCrud(key);
});

loadStationDropdownsThenTable();