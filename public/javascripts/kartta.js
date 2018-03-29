
function initMap() {
    if (navigator.geolocation) {
        $.getJSON(
            "/getkarttana", //リクエストURL
            function (json, status) {
                initialize(json);
            });
    }
}


function initialize(json) {

    var map = new google.maps.Map(document.getElementById('map'), {
        center: {lat: 34.702485, lng: 135.495951}, // 地図の中心を指定
        zoom: 15// 地図のズームを指定
    });

    setCenter(map);

    setMarker(map,json);
}


function setMarker(map,json) {
    let marker = [];

    for (let i in json) {
        marker[i] = new google.maps.Marker({ // マーカーの追加
            position: new google.maps.Marker({lat: json[i].lat, lng: json[i].lng}), // マーカーを立てる位置を指定
            map: map // マーカーを立てる地図を指定
        });

        marker[i].addListener('click', function () { // マーカーをクリックしたとき
            $.getJSON(
                "/getrestaurant/" + json[i]['restaurantId'],
                function (json, status) {
                    var infoWindow = new google.maps.InfoWindow({ // 吹き出しの追加
                        /* 吹き出しに表示する要素 */
                        content: '<div class="sample">'+ json.name + '</div>'
                    });
                    infoWindow.open(map, marker[i]); // 吹き出しの表示
                });
        });

    }
}

function setCenter(map) {
    navigator.geolocation.getCurrentPosition(function (position) {
        map.setCenter({
            lat: position.coords.latitude,
            lng: position.coords.longitude
        });
    });
}
