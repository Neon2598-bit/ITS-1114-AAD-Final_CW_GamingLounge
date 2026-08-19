const API_BASE = "http://localhost:8080/api/v1";

function handleLogin() {
    const email = $('#email').val().trim();
    const password = $('#password').val().trim();

    $('#errorMsg').hide();

    if (!email || !password) {
        showError("Please enter both email and password");
        return;
    }

    $.ajax({
        url: API_BASE + "/auth/login",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify({ email: email, password: password}),
        success: function (response) {

            const body = response.body;

            localStorage.setItem("userId", body.id);
            localStorage.setItem("token", body.token);
            localStorage.setItem("role", body.role);
            localStorage.setItem("name", body.name);
            localStorage.setItem("email", body.email);

            if (body.role === "ADMIN") {
                window.location.href = "admin-dashboard.html";
            } else {
                window.location.href = "customer-dashboard.html";
            }
        },

        error: function (xhr) {
            const msg = (xhr.responseJSON && xhr.responseJSON.message)
            ? xhr.responseJSON.message
            : "Invalid E-Mail Or Password.";
            showError(msg);
        }
    });
}

function showError(message) {
    $('#errorMsg').text(message).show();
}