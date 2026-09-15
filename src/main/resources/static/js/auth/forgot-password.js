const API_BASE = "http://localhost:8080/api/v1";

function handleForgotPassword() {
    const email = $('#email').val().trim();
    $('#errorMsg').hide(); $('#successMsg').hide();

    if (!email) { $('#errorMsg').text("Please enter your email").show(); return; }

    $.ajax({
        url: API_BASE + "/auth/forgot-password",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify({ email: email }),
        success: function () {
            localStorage.setItem("resetEmail", email);
            window.location.href = "reset-password.html";
        },
        error: function (xhr) {
            const msg = (xhr.responseJSON && xhr.responseJSON.message) || "Something went wrong.";
            $('#errorMsg').text(msg).show();
        }
    });
}

function handleResetPassword() {
    const email = localStorage.getItem("resetEmail");
    const otpCode = $('#otpCode').val().trim();
    const newPassword = $('#newPassword').val().trim();
    $('#errorMsg').hide(); $('#successMsg').hide();

    $.ajax({
        url: API_BASE + "/auth/reset-password",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify({ email: email, otpCode: otpCode, newPassword: newPassword }),
        success: function () {
            $('#successMsg').text("Password reset! Redirecting to login...").show();
            setTimeout(function () { window.location.href = "index.html"; }, 1500);
        },
        error: function (xhr) {
            const msg = (xhr.responseJSON && xhr.responseJSON.message) || "Invalid or expired code.";
            $('#errorMsg').text(msg).show();
        }
    });
}