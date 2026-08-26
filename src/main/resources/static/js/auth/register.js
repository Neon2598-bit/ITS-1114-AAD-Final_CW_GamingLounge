const API_BASE = "http://localhost:8080/api/v1";

function handleRegister() {
    const name = $('#name').val().trim();
    const email = $('#email').val().trim();
    const phone = $('#phone').val().trim();
    const address = $('#address').val().trim();
    const password = $('#password').val().trim();
    const confirmPassword = $('#confirmPassword').val().trim();

    $('#errorMsg').hide();

    if (!name || !email || !phone || !address || !password) {
        showError("Please fill in every field.");
        return;
    }
    if (password !== confirmPassword) {
        showError("Passwords do not match.");
        return;
    }

    $.ajax({
        url: API_BASE + "/customer",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify({ name, email, phone, address, password }),
        success: function (response) {
            const customerId = response.body;
            const customerEmail = email;
            localStorage.setItem("pendingCustomerId", customerId);
            localStorage.setItem("pendingEmail", customerEmail);

            $.ajax({
                url: API_BASE + "/otp/send",
                type: "POST",
                contentType: "application/json",
                data: JSON.stringify({ customerId: customerId }),
                complete: function () {
                    window.location.href = "otp-verify.html";
                }
            });
        },
        error: function (xhr) {
            const msg = (xhr.responseJSON && xhr.responseJSON.message)
                    ? xhr.responseJSON.message
                    : "Registration failed. Please check your details.";
            showError(msg);
        }
    });
}

function showError(message) {
    $('#errorMsg').text(message).show();
}
