var editButton = document.getElementById("edit");
var addButton = document.getElementById("save");
var form = document.getElementById("form");

var xhrObj = new XMLHttpRequest();

document.getElementById('file').onchange = function (evt) {
	var tgt = evt.target || window.event.srcElement,
        files = tgt.files;
		
	if (FileReader && files && files.length) {
		var fr = new FileReader();
		fr.onload = function () {
			document.getElementById('picture').src = fr.result;
		}
		fr.readAsDataURL(files[0]);
	}
};

function savePatient() {
    var formDataSerAr = $("form").serializeArray();
    var result = {};
    $.each(formDataSerAr, function () {
        if (result[this.name]) {
            if (!result[this.name].push) {
                result[this.name] = [formDataSerAr[this.name]];
            }
            result[this.name].push(this.value || '');
        } else {
            result[this.name] = this.value || '';
        }
    });
    //console.log(result);
    $.ajax({
        url:"http://localhost:8080/patients",
        type:"POST",
        data: JSON.stringify(result),
        contentType: 'application/json',
        success: function (data) {
            if (data.redirect){
                window.location.href = data.redirect;
            } else {
                window.location.replace("/patients");
            }
        },
        error: function(data){
            alert("An error occured");
        }
    });
    //$("<a href='/patients'></a>").click();
};

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
};
/*document.getElementById('file').onchange = function (evt) {
	var tgt = evt.target || window.event.srcElement,
        files = tgt.files;
		
	if (FileReader && files && files.length) {
		var fr = new FileReader();
		fr.onload = function () {
			document.getElementById('picture').src = fr.result;
		}
		fr.readAsDataURL(files[0]);
	}
};*/
/*document.getElementById('send').onchange = function (evt) {
	console.log("click");
	var tgt = evt.target || window.event.srcElement,
        files = tgt.files;
	
	console.log(files);
	if (FileReader && files && files.length) {
		console.log("if");
		var fr = new FileReader();
		fr.onload = function () {
			console.log(fr.result);
		}
		fr.readAsDataURL(files[0]);
	}
};*/

function saveProfilePhoto(){
	var formDataSerAr = $("form").serializeArray();
    var result = {};
	
    $.each(formDataSerAr, function () {
        if (result[this.name]) {
            if (!result[this.name].push) {
                result[this.name] = [formDataSerAr[this.name]];
            }
            result[this.name].push(this.value || '');
        } else {
            result[this.name] = this.value || '';
        }
    });
	
	var file = document.querySelector('input[type=file]').files[0];
	var fr = new FileReader();
	fr.addEventListener("load", function () {
		result['file'] = fr.result;
		console.log("http://localhost:8080/setProfilePicture/" + result['patientId']);
		
		$.ajax({
			url:"http://localhost:8080/setProfilePicture/",
			type:"POST",
			data: JSON.stringify(result),
			contentType: 'application/json',
			success: function (data) {
				if (data.redirect){
					window.location.href = data.redirect;
				} else {
					window.location.replace("/person/" + result['patientId']);
				}
			},
			error: function(data){
				alert("An error occured" + data);
			}
		});
		
	}, false);
	
	if (file) {
		fr.readAsDataURL(file);
	}
}

function sendPhoto(url, patientId, result) {
	$.ajax({
        url:url,
        type:"POST",
        data: JSON.stringify(result),
        contentType: 'application/json',
        success: function (data) {
            if (data.redirect){
                window.location.href = data.redirect;
            } else {
                window.location.replace("/person/" + patientId);
            }
        },
        error: function(data){
            alert("An error occured");
        }
    });
}

function addPhoto(url, patientId, result){
	var file = document.querySelector('input[type=file]').files[0];
	var fr = new FileReader();
	fr.addEventListener("load", function () {
		result['file'] = fr.result;
		
		sendPhoto(url, patientId, result);
	}, false);
	
	if (file) {
		fr.readAsDataURL(file);
	}
}

function savePhoto(url, patientId) {
	var formDataSerAr = $("form").serializeArray();
    var result = {};
	
    $.each(formDataSerAr, function () {
        if (result[this.name]) {
            if (!result[this.name].push) {
                result[this.name] = [formDataSerAr[this.name]];
            }
            result[this.name].push(this.value || '');
        } else {
            result[this.name] = this.value || '';
        }
    });
	addPhoto(url, patientId, result);
};


/*function getData(){
    var patientId = document.getElementById(patientId);
    var firstName = document.getElementById(firstName);
    var lastName = document.getElementById(lastName);
    var birthDate = document.getElementById(birthDate);
    var dementiaLevel = document.getElementById(dementiaLevel);

    var result = "";
}*/