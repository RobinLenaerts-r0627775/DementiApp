function removePatientConfirmation(id, firstname, lastname){
    if(confirm("Are you sure you want to REMOVE " + firstname + " " + lastname + " for good")){
        //var url = "http://localhost:8080/patients/delete/" + id;      //local
        var url = "http://193.191.177.178:8080/patients/delete/" + id;  //server
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
        //var url = "http://localhost:8080/webmedia/delete/" + mediaId;       //local
        var url = "http://193.191.177.178:8080/webmedia/delete/" + mediaId; //server
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