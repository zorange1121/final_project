document.getElementById("message").addEventListener("keydown", function(event) {
    if (event.key === "Enter") {
        send();
    }
});
function send() {
    var message = document.getElementById("message").value;
    var list = document.getElementById('messagelist');
    var listItem = document.createElement('li');
    var currentTime = new Date();
    var hours = currentTime.getHours();
    var minutes = currentTime.getMinutes(); 
    var timeString = hours + ":" + (minutes < 10 ? "0" + minutes : minutes);
    listItem.className = "message";
    listItem.innerHTML = timeString+"  "+"<span class='textbox'><p>"+message+"</p></span>";
    list.appendChild(listItem);
    document.getElementById("message").value = "";
    list.scrollTop =list.scrollHeight;
}