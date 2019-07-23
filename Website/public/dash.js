function SignOut() {
   
     firebase.auth().signOut();
     location.href= "index.html";
}

