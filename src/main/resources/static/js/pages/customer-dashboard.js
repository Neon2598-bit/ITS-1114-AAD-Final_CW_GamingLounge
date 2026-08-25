// =============================================================================
// AUTH / PAGE SETUP
// =============================================================================
requireAuth(["USER", "GUEST"]);
$('#welcomeText').text("Hi, " + getUserName());

const isGuest = (localStorage.getItem("role") === "GUEST");
if (isGuest) {
    $('#guestHidden1, #guestHidden2, #placeOrderBtn').hide();
}


// =============================================================================
// SHARED HELPERS
// =============================================================================
function formatDate(isoString) {
    const d = new Date(isoString);
    return d.toLocaleString();
}


// =============================================================================
// STATIONS - browse + start a booking
// =============================================================================
let selectedStationId = null;

function loadStations() {
    $.get(API_BASE + "/station", function (response) {
        const stations = response.body;
        const rows = stations.map(function (s) {
            let actionCell;
            if (isGuest) {
                actionCell = '<span style="color:#9497a3">Sign in to book</span>';
            } else if (s.status === "AVAILABLE") {
                actionCell = '<button style="width:auto; padding:6px 14px" onclick="openBookingForm(' + s.id + ', \'' + s.stationCode + '\')">Book</button>';
            } else {
                actionCell = '<span style="color:#9497a3">' + s.status + '</span>';
            }
            return '<tr><td>' + s.stationCode + '</td><td>' + s.typeName + '</td><td>' +
                    s.branchName + '</td><td>Rs. ' + s.hourlyRate + '</td><td>' + s.status +
                    '</td><td>' + actionCell + '</td></tr>';
        });
        $('#stationsBody').html(rows.join(''));
    });
}

function openBookingForm(stationId, stationCode) {
    selectedStationId = stationId;
    $('#bookingStationCode').text(stationCode);
    $('#bookingForm').show();
    $('html, body').animate({ scrollTop: $('#bookingForm').offset().top - 20 }, 300);
}

function cancelBookingForm() {
    $('#bookingForm').hide();
    selectedStationId = null;
}

function submitBooking() {
    const startTime = $('#startTime').val();
    const endTime = $('#endTime').val();
    $('#bookingError').hide();
    $('#bookingSuccess').hide();

    if (!startTime || !endTime) {
        $('#bookingError').text("Please choose both a start and end time.").show();
        return;
    }

    $.ajax({
        url: API_BASE + "/booking",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify({
            startTime: startTime,
            endTime: endTime,
            customerId: getUserId(),
            stationId: selectedStationId
        }),
        success: function () {
            $('#bookingSuccess').text("Station booked successfully!").show();
            $('#bookingForm').hide();
            loadStations();
            loadMyBookings();
        },
        error: function (xhr) {
            const msg = (xhr.responseJSON && xhr.responseJSON.message)
                    ? xhr.responseJSON.message
                    : "Could not book this station. It may no longer be available.";
            $('#bookingError').text(msg).show();
        }
    });
}


// =============================================================================
// BOOKINGS
// =============================================================================
function loadMyBookings() {
    $.get(API_BASE + "/booking/by-customer/" + getUserId(), function (response) {
        const bookings = response.body;
        if (bookings.length === 0) {
            $('#bookingsBody').html('<tr><td colspan="7" style="color:#9497a3">No bookings yet.</td></tr>');
        } else {
            const rows = bookings.map(function (b) {
                let actionCell = "-";
                if (b.status === "BOOKED") {
                    actionCell = '<button style="width:auto; padding:6px 10px" onclick="updateBookingStatus(' + b.id + ', \'CANCELLED\')">Cancel</button>';
                }

                let paymentCell = '<span style="color:#9497a3">-</span>';
                if (b.status === "CANCELLED") {
                    paymentCell = '<span style="color:#9497a3">-</span>';
                } else if (paidBookingIds.has(b.id)) {
                    paymentCell = '<span style="color:var(--success)">Paid ✓</span>';
                } else {
                    paymentCell = '<button style="width:auto; padding:6px 10px" onclick="openPaymentForm(' +
                            b.id + ', \'' + b.stationCode + '\', ' + b.totalAmount + ')">Pay Now</button>';
                }

                return '<tr><td>' + b.stationCode + '</td><td>' + formatDate(b.startTime) + '</td><td>' +
                        formatDate(b.endTime) + '</td><td>Rs. ' + b.totalAmount + '</td><td>' + b.status +
                        '</td><td>' + paymentCell + '</td><td>' + actionCell + '</td></tr>';
            });
            $('#bookingsBody').html(rows.join(''));
        }
        const completed = bookings.filter(function (b) { return b.status === "COMPLETED"; });
        if (completed.length === 0) {
            $('#feedbackBookingId').html('<option value="">No completed bookings yet</option>');
        } else {
            $('#feedbackBookingId').html(completed.map(function (b) {
                return '<option value="' + b.id + '">' + b.stationCode + ' - ' + formatDate(b.startTime) + '</option>';
            }).join(''));
        }
    });
}

function updateBookingStatus(bookingId, status) {
    $.ajax({
        url: API_BASE + "/booking/" + bookingId + "/status?status=" + status,
        type: "PATCH",
        success: function () {
            loadStations();
            loadMyBookings();
        }
    });
}


// =============================================================================
// SNACKS / FOOD ORDER
// =============================================================================
function loadSnacks() {
    $.get(API_BASE + "/snack", function (response) {
        const snacks = response.body;
        const rows = snacks.map(function (s) {
            return '<tr><td>' + s.name + '</td><td>Rs. ' + s.price + '</td><td>' + s.stockQty +
                    '</td><td><input type="number" min="0" max="' + s.stockQty +
                    '" value="0" style="width:60px" id="qty-' + s.id + '" data-snack-id="' + s.id + '"></td></tr>';
        });
        $('#snacksBody').html(rows.join(''));
    });
}

function submitFoodOrder() {
    $('#orderError').hide();
    $('#orderSuccess').hide();

    const items = [];
    $('#snacksBody input[type="number"]').each(function () {
        const qty = parseInt($(this).val());
        if (qty > 0) {
            items.push({ snackId: $(this).data('snack-id'), quantity: qty });
        }
    });

    if (items.length === 0) {
        $('#orderError').text("Please enter a quantity for at least one snack.").show();
        return;
    }

    $.ajax({
        url: API_BASE + "/food-order",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify({ customerId: getUserId(), items: items }),
        success: function () {
            $('#orderSuccess').text("Order placed successfully!").show();
            loadSnacks();
        },
        error: function (xhr) {
            const msg = (xhr.responseJSON && xhr.responseJSON.message)
                    ? xhr.responseJSON.message
                    : "Could not place order - check stock availability.";
            $('#orderError').text(msg).show();
        }
    });
}


// =============================================================================
// PAYMENTS
// =============================================================================
let selectedPaymentBookingId = null;
let paidBookingIds = new Set();

function openPaymentForm(bookingId, stationCode, amount) {
    selectedPaymentBookingId = bookingId;
    $('#paymentStationCode').text(stationCode);
    $('#paymentAmountText').text(amount);
    $('#paymentForm').show();
    $('html, body').animate({ scrollTop: $('#paymentForm').offset().top - 20 }, 300);
}

function cancelPaymentForm() {
    $('#paymentForm').hide();
    selectedPaymentBookingId = null;
}

function submitPayment() {
    $('#paymentError').hide();
    $('#paymentSuccess').hide();

    const amount = Number($('#paymentAmountText').text());

    $.ajax({
        url: API_BASE + "/payment",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify({
            amount: amount,
            paymentMethod: $('#paymentMethod').val(),
            paymentFor: "BOOKING",
            referenceId: selectedPaymentBookingId,
            customerId: getUserId()
        }),
        success: function () {
            $('#paymentSuccess').text("Payment successful! Invoice generated.").show();
            $('#paymentForm').hide();
            loadMyPayments(loadMyBookings);
        },
        error: function (xhr) {
            const msg = (xhr.responseJSON && xhr.responseJSON.message)
                    ? xhr.responseJSON.message
                    : "Payment failed. Please try again.";
            $('#paymentError').text(msg).show();
        }
    });
}

function loadMyPayments(callback) {
    $.get(API_BASE + "/payment/by-customer/" + getUserId(), function (response) {
        const payments = response.body;

        paidBookingIds = new Set(
            payments.filter(function (p) { return p.paymentFor === "BOOKING"; })
                    .map(function (p) { return p.referenceId; })
        );

        if (payments.length === 0) {
            $('#paymentsBody').html('<tr><td colspan="6" style="color:#9497a3">No payments yet.</td></tr>');
        } else {
            $('#paymentsBody').html(payments.map(function (p) {
                return '<tr><td>' + p.paymentFor + ' #' + p.referenceId + '</td><td>Rs. ' + p.amount +
                        '</td><td>' + p.paymentMethod + '</td><td>' + p.status + '</td><td>' +
                        formatDate(p.paymentDate) + '</td><td>' +
                        '<button style="width:auto; padding:6px 10px" onclick="viewInvoice(' + p.id + ')">View</button></td></tr>';
            }).join(''));
        }

        if (callback) callback();
    });
}

function viewInvoice(paymentId) {
    $.get(API_BASE + "/invoice/by-payment/" + paymentId, function (response) {
        const inv = response.body;
        $('#invoiceNumber').text(inv.invoiceNumber);
        $('#invoiceDate').text(formatDate(inv.issueDate));
        $('#invoiceCustomer').text(inv.customerName);
        $('#invoiceAmount').text(inv.amount);
        $('#invoiceCard').show();
        $('html, body').animate({ scrollTop: $('#invoiceCard').offset().top - 20 }, 300);
    }).fail(function () {
        $('#paymentError').text("Could not load invoice for this payment.").show();
    });
}


// =============================================================================
// MEMBERSHIP PLANS
// =============================================================================
function loadMembershipPlans() {
    $.get(API_BASE + "/membership-plan", function (response) {
        const plans = response.body;
        if (plans.length === 0) {
            $('#plansBody').html('<tr><td colspan="5" style="color:#9497a3">No plans available yet.</td></tr>');
            return;
        }
        $('#plansBody').html(plans.map(function (p) {
            const actionCell = isGuest
                    ? '<span style="color:#9497a3">Sign in to subscribe</span>'
                    : '<button style="width:auto; padding:6px 14px" onclick="subscribeToPlan(' + p.id + ')">Subscribe</button>';
            return '<tr><td>' + p.planName + '</td><td>Rs. ' + p.price + '</td><td>' + p.durationDays +
                    ' days</td><td>' + p.discountPercentage + '%</td><td>' + actionCell + '</td></tr>';
        }).join(''));
    });
}

function subscribeToPlan(planId) {
    $('#membershipError').hide();
    $('#membershipSuccess').hide();

    $.ajax({
        url: API_BASE + "/membership",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify({ customerId: getUserId(), membershipPlanId: planId }),
        success: function () {
            $('#membershipSuccess').text("Subscribed successfully!").show();
            loadMyMemberships();
        },
        error: function (xhr) {
            const msg = (xhr.responseJSON && xhr.responseJSON.message)
                    ? xhr.responseJSON.message
                    : "Could not subscribe to this plan. Please try again.";
            $('#membershipError').text(msg).show();
        }
    });
}

function loadMyMemberships() {
    $.get(API_BASE + "/membership/by-customer/" + getUserId(), function (response) {
        const memberships = response.body;
        if (memberships.length === 0) {
            $('#myMembershipsBody').html('<tr><td colspan="5" style="color:#9497a3">No membership yet - subscribe to a plan above.</td></tr>');
            return;
        }
        $('#myMembershipsBody').html(memberships.map(function (m) {
            const actionCell = m.status === "ACTIVE"
                    ? '<button style="width:auto; padding:6px 10px" onclick="cancelMembership(' + m.id + ')">Cancel</button>'
                    : '<span style="color:#9497a3">-</span>';
            return '<tr><td>' + m.planName + '</td><td>' + m.startDate + '</td><td>' + m.endDate +
                    '</td><td>' + m.status + '</td><td>' + actionCell + '</td></tr>';
        }).join(''));
    });
}

function cancelMembership(membershipId) {
    $.ajax({
        url: API_BASE + "/membership/" + membershipId + "/status?status=CANCELLED",
        type: "PATCH",
        success: function () { loadMyMemberships(); },
        error: function () {
            $('#membershipError').text("Could not cancel membership. Please try again.").show();
        }
    });
}


// =============================================================================
// FEEDBACK
// =============================================================================
function submitFeedback() {
    $('#feedbackError').hide();
    $('#feedbackSuccess').hide();

    const bookingId = $('#feedbackBookingId').val();
    if (!bookingId) {
        $('#feedbackError').text("You need at least one completed booking before you can leave feedback.").show();
        return;
    }

    $.ajax({
        url: API_BASE + "/feedback",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify({
            customerId: getUserId(),
            bookingId: bookingId,
            rating: parseInt($('#feedbackRating').val()),
            comment: $('#feedbackComment').val().trim()
        }),
        success: function () {
            $('#feedbackSuccess').text("Thanks for your feedback!").show();
            $('#feedbackComment').val('');
            loadMyFeedback();
        },
        error: function (xhr) {
            const msg = (xhr.responseJSON && xhr.responseJSON.message)
                    ? xhr.responseJSON.message
                    : "Could not submit feedback. Please try again.";
            $('#feedbackError').text(msg).show();
        }
    });
}

function loadMyFeedback() {
    $.get(API_BASE + "/feedback", function (response) {
        const myId = String(getUserId());
        const mine = response.body.filter(function (f) { return String(f.customerId) === myId; });
        if (mine.length === 0) {
            $('#myFeedbackBody').html('<tr><td colspan="4" style="color:#9497a3">You haven\'t left any feedback yet.</td></tr>');
            return;
        }
        $('#myFeedbackBody').html(mine.map(function (f) {
            return '<tr><td>#' + f.bookingId + '</td><td>' + f.rating + ' / 5</td><td>' +
                    (f.comment || '-') + '</td><td>' + formatDate(f.feedbackDate) + '</td></tr>';
        }).join(''));
    });
}


// =============================================================================
// PAGE INIT
// =============================================================================
loadStations();
loadSnacks();
loadMembershipPlans();
if (!isGuest) {
    loadMyPayments(loadMyBookings);
    loadMyMemberships();
    loadMyFeedback();
}
