function removeConfirmation(id, firstname, lastname){
    if(confirm("Are you sure you want to REMOVE " + firstname + " " + lastname + " for good")){
        console.log(id);
    }
}