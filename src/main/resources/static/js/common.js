const API_BASE = "http://localhost:8080/api/v1";

function requireAuth(requiredRole) {
    const token = localStorage.getItem("token");
    const role = localStorage.getItem("role");

    if (!token) {
        window.location.href = "index.html";
        return;
    }
    if (requiredRole) {
        const allowed = Array.isArray(requiredRole) ? requiredRole : [requiredRole];
        if (!allowed.includes(role)) {
            alert("You don't have access to this page.");
            window.location.href = "index.html";
        }
    }
}

$.ajaxSetup({
    beforeSend: function (xhr) {
        const token = localStorage.getItem("token");
        if (token) {
            xhr.setRequestHeader("Authorization", "Bearer " + token);
        }
    }
});

function logout() {
    localStorage.clear();
    window.location.href = "index.html";
}

function getUserId() {
    return localStorage.getItem("userId");
}

function getUserName() {
    return localStorage.getItem("name");
}
