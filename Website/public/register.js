const auth = firebase.auth();
const database = firebase.database();
function signup(){
	if(isNaN(document.getElementById("rollno_field").value) || document.getElementById("rollno_field").value.length!=9 ){
		window.alert("Invalid Rollno \nRollno Length shouldbe 9 and it shouldbe numeric");
		return 0;
 		}
	var userEmail = document.getElementById("rollno_field").value.concat('@ostello.com');
	console.log(userEmail);
	var father_name= document.getElementById("father_name_field").value;
	console.log(father_name);
	var dateofbirth = document.getElementById("dateofbirth_field").value;
	console.log(dateofbirth);
	var date_regex = /^(0[1-9]|1\d|2\d|3[01])\/(0[1-9]|1[0-2])\/(19|20)\d{2}$/ ;
	if(!(date_regex.test(dateofbirth)))
		{
			window.alert("Invalid Date Format");
			return 0;
		}
	var userPassword = '@ostello'.concat(father_name.slice(0,3),dateofbirth.slice(0,2));
	console.log(userPassword);
	auth.createUserWithEmailAndPassword(userEmail, userPassword).then(function(user){
		database.ref('Students/'+ document.getElementById("rollno_field").value).set({
			UID: auth.currentUser.uid,
			name: document.getElementById("name_field").value,
			fathername: document.getElementById("father_name_field").value,
			Room_no: document.getElementById("room_field").value,
			DOB: document.getElementById("dateofbirth_field").value });  
		//console.log("UID : " + uid);
	}).catch(function(error) {
            var errorCode = error.code;
            var errorMessage = error.message;
            console.log(errorCode + ' - ' + errorMessage);
            console.log("error0 ");
       		});

	auth.signOut().then(function() {
  // Sign-out successful.
}).catch(function(error) {
console.log(error);
  // An error happened.
});

}
document.getElementById("submit").addEventListener('click', signup);



