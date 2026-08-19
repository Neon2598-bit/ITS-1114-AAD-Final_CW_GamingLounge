// =============================================================================
// 1. AUTH / PAGE SETUP
// =============================================================================
requireAuth("ADMIN");
$('#welcomeText').text("Hi, " + getUserName());


// =============================================================================
// 2. CORE ENGINE
// =============================================================================
const CRUD = {
    branch:         { endpoint: "/branch",          fields: ["branchName", "address", "contactNumber"] }
};

const CRUD_LABEL = {
    branch: "Branches"
};

const SECTIONS = [
    { key: "branch", label: "Branches" }
];

const ROW_CACHE = {};

const ROW_RENDERERS = {};

const INACTIVE_VISIBLE = {};

const INACTIVE_ROW_RENDERERS = {};

function showSection(key) {
    $('.section').removeClass('active');
    $('.section[data-section="' + key + '"]').addClass('active');
    $('.sidebar button').removeClass('active');
    $('.sidebar button[data-key="' + key + '"]').addClass('active');
}

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
