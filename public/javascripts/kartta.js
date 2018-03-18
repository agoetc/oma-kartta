
function initMap() {
    $.getJSON(
        "/getkarttana", //リクエストURL
        null,       //送信データ
        function (json, status) {
            initialize(json);
        });
}

function initialize(json){

    var map = new google.maps.Map(document.getElementById('map'), {
        center: {lat: 34.702485, lng: 135.495951}, // 地図の中心を指定
        zoom: 15// 地図のズームを指定
    });



    json.forEach(function (value) {
        var marker = new google.maps.Marker({ // マーカーの追加
            position: {lat: value.lat, lng: value.lng}, // マーカーを立てる位置を指定
            map: map // マーカーを立てる地図を指定
        });

    });

    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function(position) {
            map.setCenter({
                lat: position.coords.latitude,
                lng: position.coords.longitude
            });
        });
    }
}

