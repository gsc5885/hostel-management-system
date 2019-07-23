var database = firebase.database();
var myref = database.ref('Managers/Complaints');
myref.on('value', function(snapshot) {
   
      snapshot.forEach(function(childSnapshot) {
      myref2 = database.ref('Students/'+childSnapshot.key+'/Complaints');
      myref2.on('value', function(snapshot) {
              snapshot.forEach(function(childSnapshot) {
              addrow(childSnapshot.key, childSnapshot.child('text').val() ) ;
            });
        })
    });
});

function addrow(key, text) {
    var table = document.getElementById("complaints");
    var row = table.insertRow();
    cell1= row.insertCell(0);
    cell1.innerHTML = key;

    cell2= row.insertCell(1);
    
    cell2.innerHTML = text;
}