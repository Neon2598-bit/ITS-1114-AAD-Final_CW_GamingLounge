const API_BASE = "http://localhost:8080/api/v1";

const customerId = localStorage.getItem("pendingCustomerId");
const pendingEmail = localStorage.getItem("pendingEmail");

if (!customerId) {
    window.location.href = "register.html";
} else {
    $('#subtitleText').text("We sent a 6-digit code to " + pendingEmail);
}

function handleVerify() {
    const otpCode = $('#otpCode').val().trim();
    $('#errorMsg').hide();
    $('#successMsg').hide();

    if (!otpCode) {
        showError("Please enter the code from your email.");
        return;
    }

    $.ajax({
        url: API_BASE + "/otp/verify",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify({ customerId: customerId, otpCode: otpCode }),
        success: function () {
            $('#successMsg').text("Email verified! Redirecting to login...").show();
            localStorage.removeItem("pendingCustomerId");
            localStorage.removeItem("pendingEmail");
            setTimeout(function () {
                window.location.href = "index.html";
            }, 1500);
        },
        error: function () {
            showError("Invalid or expired code. Try again or resend a new one.");
        }
    });
}

function handleResend() {
    $('#errorMsg').hide();
    $('#successMsg').hide();
    $.ajax({
        url: API_BASE + "/otp/send",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify({ customerId: customerId }),
        success: function () {
            $('#successMsg').text("A new code has been sent.").show();
        },
        error: function () {
            showError("Could not resend the code. Please try again shortly.");
        }
    });
}

function showError(message) {
    $('#errorMsg').text(message).show();
}
