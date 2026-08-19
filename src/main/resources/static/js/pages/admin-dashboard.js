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

function fillSelect(selector, endpoint, valueField, labelFn) {
    return $.get(API_BASE + endpoint, function (response) {
        const options = response.body.map(function (r) {
            return '<option value="' + r[valueField] + '">' + labelFn(r) + '</option>';
        });
        $(selector).html(options.join(''));
    });
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
