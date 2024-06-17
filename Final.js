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
    const content = document.getElementById("message").value; // 這是你要發送的參數值
    const url = `http://localhost:8080/post?content=${encodeURIComponent(content)}`;
    fetch(url)
    .then(response => {
        if (!response.ok) {
        throw new Error('Network response was not ok');
        }
        return response.text();
    })
    .then(data => {
        console.log(data); // 输出 "success"
    })
    .catch(error => {
        console.error('There was a problem with your fetch operation:', error);
    });
    document.getElementById("message").value = "";
    list.scrollTop =list.scrollHeight;
}

function fetchStringFromBackend() {
    const url = `http://localhost:8080/get`;
    try {
        const response = fetch(url);
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        const data = response.text();
        return data;
    } catch (error) {
        console.error('There was a problem with the fetch operation:', error);
        return null;
    }
}

// fetchStringFromBackend().then(result => {
//     if (result !== null) {
//         console.log('Received string from backend:', result);
//     }
// });

function get() {
    try {
        fetchStringFromBackend().then(result => {
            if (result !== null) {
                console.log('Received string from backend:', result);
                if(result!==""){
                    var listItem = document.createElement('li');
                    var currentTime = new Date();
                    var hours = currentTime.getHours();
                    var minutes = currentTime.getMinutes(); 
                    var timeString = hours + ":" + (minutes < 10 ? "0" + minutes : minutes);
                    listItem.className = "message";
                    listItem.innerHTML = timeString+"  "+"<span class='textbox'><p>"+result+"</p></span>";
                    list.appendChild(listItem);
                }
            }
        });
    } catch (error) {
       // console.error('There was a problem with the fetch operation:', error);
    }


}
setInterval(get, 500);

// document.getElementById("send").addEventListener('click', () => {
//     const content = document.getElementById("message").value; // 這是你要發送的參數值
//     const url = `http://localhost:8080/MainPage?content=${encodeURIComponent(content)}`;
  
//     fetch(url)
//       .then(response => response.text())
//       .then(data => {
//         console.log(data); // 預期會輸出 "success"
//       })
//       .catch(error => {
//         console.error('Error:', error);
//       });
//   });  
