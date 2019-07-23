
function go() {
var database = firebase.database();
var roll_no = document.querySelector('.add__description').value;
var refStudent = database.ref('Students/' + roll_no + '/Leaves');
refStudent.on('value', getdata, errdata);  
}
document.addEventListener('keypress', function(event) {
        if(event.keyCode === 13) {
           go();
        }
});


function getdata(data) {
    var databa = data.val();
    var keys = Object.keys(databa);
    console.log(keys);
    var count = 0;
    for (var k = 0; k < keys.length; k++) {
        var i = keys[k] ;
        var Leave_address = databa[i].address;
        var Leave_date  = databa[i].from.date;
        var Leave_month = databa[i].from.month;
        var Leave_year = databa[i].from.year;
        var End_date = databa[i].to.date;
        var End_month = databa[i].to.month;
        var End_year = databa[i].to.year;
        var roll = document.querySelector('.add__description').value;
        count++;
        addrow(Leave_date, Leave_month, Leave_year, End_date, End_month, End_year, Leave_address, count, roll);
    }
    
}

function errdata(err) {
    console.log('Error');
    console.log(err);
}


function addrow( Leave_date, Leave_month, Leave_year, End_date, End_month, End_year, Leave_address, k, roll) {
    var table = document.getElementById("details");
    

    
        var row = table.insertRow(k);
            cell1 = row.insertCell(0);
            cell1.innerHTML = roll;
            cell2 = row.insertCell(1);
            cell2.innerHTML = Leave_date+'/'+Leave_month+'/'+Leave_year;
            cell3 = row.insertCell(2);
            cell3.innerHTML = End_date+'/'+End_month+'/'+End_year;
            cell4 = row.insertCell(3);
            cell4.innerHTML = Leave_address;
            
        
        
    }
        
    

function Signout(){
    
  firebase.auth().signOut();
  location.href= "index.html";
}

