function removeConfirmation(id, firstname, lastname){
    if(confirm("Are you sure you want to REMOVE " + firstname + " " + lastname + " for good")){
        var url = "http://localhost:8080/patients/delete/" + id;
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