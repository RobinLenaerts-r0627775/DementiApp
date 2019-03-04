function removePatientConfirmation(id, firstname, lastname){
    if(confirm("Are you sure you want to REMOVE " + firstname + " " + lastname + " for good")){
        var url = "/patients/delete/" + id;
        $.ajax({
            url:url,
            type:"GET",
            success: function (data) {
                if (data.redirect){
                    window.location.href = data.redirect;
                } else {
                    window.location.replace("/patients");
                }
            },
            error: function(data){
                alert("An error occurred");
            }
        });
    }
}

function removeMediaConfirmation(mediaId, patientId){
    if(confirm("Are you sure you want to REMOVE this file for good")){
        var url = "/webmedia/delete/" + mediaId;
        $.ajax({
            url:url,
            type:"GET",
            success: function (data) {
                if (data.redirect){
                    window.location.href = data.redirect;
                } else {
                    window.location.replace("/webmedia/" + patientId);
                }
            },
            error: function(data){
                alert("An error occurred");
            }
        });
    }
}

function showByCategory(patientId){
    var cat = document.getElementById("category").value;
    if (cat == null || cat == ""){
        var url = "/webmedia/" + patientId;
    } else {
        var url = "/webmedia/" + patientId + "/category/" + cat;
    }
    $.ajax({
        url:url,
        type:"GET",
        success: function (data) {
            if (data.redirect){
                window.location.href = data.redirect;
            } else {
                if (cat == null || cat == ""){
                    window.location.replace("/webmedia/" + patientId);
                } else {
                    window.location.replace("/webmedia/" + patientId + "/category/" + cat);
                }
            }
        },
        error: function(data){
            alert("An error occurred");
        }
    });
}