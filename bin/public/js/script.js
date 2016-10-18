
document.getElementById("recommendbutton").addEventListener("click", loadingImage);

function loadingText() {
    if(document.getElementById("searchfield").checkValidity()) {
      document.getElementById("load").innerHTML = "Loading...";
    }
    else{
      document.getElementById("load").innerHTML = "Please check your input";
    }
}

function loadingImage() {
    if(document.getElementById("searchfield").checkValidity()) {
      document.getElementById("load").innerHTML = "<img src=\'images/spinner.gif\'>";
    }
    else{
      document.getElementById("load").innerHTML = "Please check your input";
    }
}
