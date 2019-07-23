firebase.auth().onAuthStateChanged(function(user) {
  if (user) {
    // User is signed in.

    var user = firebase.auth().currentUser;

    if(user != null){

      var email_id = user.email;
      console.log(email_id);
      if(email_id === 'caretaker@ostello.com' || email_id === 'messmanager@ostello.com' ) {
          console.log(email_id);
          location.href = "indexdash.html";

      }else {
          function Signout(){
       window.alert('Not authorized to use the website');
  firebase.auth().signOut();
  location.href= "index.html";
}
      }

    }

  } else {
    // No user is signed in.

    document.getElementById("user_div").style.display = "none";
    document.getElementById("login_div").style.display = "block";

  }
});
document.addEventListener('keypress', function(event) {
        if(event.keyCode === 13) {
           login();
        }
});

function login(){

  var userEmail = document.getElementById("email_field").value;
  var userPass = document.getElementById("password_field").value;

  firebase.auth().signInWithEmailAndPassword(userEmail, userPass).catch(function(error) {
    // Handle Errors here.
    var errorCode = error.code;
    var errorMessage = error.message;

    window.alert("Error : " + errorMessage);

    // ...
  });
    

}

 