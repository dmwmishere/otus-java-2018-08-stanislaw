var ws;

init = function () {
    ws = new WebSocket("ws://" + window.location.host + "/asyncadduser");
    ws.onopen = function (event) {

    }
    ws.onmessage = function (event) {

        console.log("Message received: " + event.data);

        var users = JSON.parse(event.data);

          var tbl = document.getElementById("userReportTable");

            while(tbl.rows.length > 1){
                tbl.deleteRow(tbl.rows.length-1)
            }


        for (i = 0; i < users.length; i++) {
            var user = users[i]
            console.log("User " + i + "= " + user);
            console.log("Name = " + user.name);
            console.log("Age = " + user.age);
            console.log("Address = " + user.adress.street);
            userPhones = "";
            for(j = 0; j < user.phones.length; j++){
                userPhones += user.phones[j].phone + ",";
            }
            console.log("Phones = " + userPhones);
            insRow(tbl,
            user.id, user.name, user.age, user.adress.street, userPhones)
        }

    }
    ws.onclose = function (event) {

    }
};

function sendMessage() {

    var msg_field_id = document.getElementById("id").value;
    var msg_field_name = document.getElementById("name").value;
    var msg_field_age = document.getElementById("age").value;
    var msg_field_address = document.getElementById("address").value;
    var msg_field_phone1 = document.getElementById("phone1").value;
    var msg_field_phone2 = document.getElementById("phone2").value;
    var msg_field_phone3 = document.getElementById("phone3").value;
    console.log(msg_field_id);
    var message = '{"id":"' + msg_field_id +'",' +
                  '"name":"' + msg_field_name +'",' +
                  '"age":"' + msg_field_age +'",' +
                  '"adress":{"street":"' + msg_field_address +'"},' +
                  '"phones":[{"phone":"' + msg_field_phone1 +'"},' +
                            '{"phone":"' + msg_field_phone2 +'"},' +
                            '{"phone":"' + msg_field_phone3 +'"}]}';

    console.log(message);
    ws.send(message);
}

function insRow(tbl, userid, name, age, address, phones) {
  var x = tbl.insertRow(1);
  x.insertCell(0).innerHTML = userid;
  x.insertCell(1).innerHTML = name;
  x.insertCell(2).innerHTML = age;
  x.insertCell(3).innerHTML = address;
  x.insertCell(4).innerHTML = phones;
}