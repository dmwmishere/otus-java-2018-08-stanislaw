var ws;

init = function () {
    ws = new WebSocket("ws://localhost:8090/asyncadduser");
    ws.onopen = function (event) {

    }
    ws.onmessage = function (event) {
        var $textarea = document.getElementById("messages");
        $textarea.value = $textarea.value + event.data + "\n";
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