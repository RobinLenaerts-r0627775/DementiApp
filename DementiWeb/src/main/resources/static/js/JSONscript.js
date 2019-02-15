var editButton = document.getElementById("edit");
var addButton = document.getElementById("save");
var form = document.getElementById("form");

var xhrObj = new XMLHttpRequest();

function savePatient() {
    var formDataSerAr = $("form").serializeArray();
    var result = {};
    $.each(formDataSerAr, function () {
        if (result[this.name]) {
            if (!result[this.name].push) {
                result[this.name] = [o[this.name]];
            }
            result[this.name].push(this.value || '');
        } else {
            result[this.name] = this.value || '';
        }
    });
    $.ajax({
        url:"http://localhost:8080/patients",
        type:"POST",
        data: JSON.stringify(result),
        contentType: 'application/json',
        success: function (data) {
            if (data.redirect){
                window.location.href = data.redirect;
            } else {
                window.location.replace("/")
            }
        }
    });
    //$("<a href='/patients'></a>").click();
}

function editPatient() {
    var formDataSerAr = $("form").serializeArray();
    var result = {};
    $.each(formDataSerAr, function () {
        if (result[this.name]) {
            if (!result[this.name].push) {
                result[this.name] = [o[this.name]];
            }
            result[this.name].push(this.value || '');
        } else {
            result[this.name] = this.value || '';
        }
    });
    $.ajax({
        url:"http://localhost:8080/patients",
        type:"PUT",
        data: JSON.stringify(result),
        contentType: 'application/json',
        success: function (result) {
        }
    });
}



/*function getData(){
    var patientId = document.getElementById(patientId);
    var firstName = document.getElementById(firstName);
    var lastName = document.getElementById(lastName);
    var birthDate = document.getElementById(birthDate);
    var dementiaLevel = document.getElementById(dementiaLevel);

    var result = "";
}*/