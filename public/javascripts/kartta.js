
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


function setMarker(map,karttanaJson) {
    let marker = [];
    var infoWindow = new google.maps.InfoWindow({});

    for (let i in karttanaJson) {
        marker[i] = new google.maps.Marker({ // マーカーの追加
            position: new google.maps.Marker({lat: karttanaJson[i].lat, lng: karttanaJson[i].lng}), // マーカーを立てる位置を指定
            map: map // マーカーを立てる地図を指定
        });


        /* マーカーをクリックしたとき
         * jsonからinfoWindowを作成する.
         */
        marker[i].addListener('click', function () {
            $.getJSON(
                "/getrestaurant/" + karttanaJson[i]['restaurantId'],
                function (restaurantJson, status) {
                    infoWindow.setContent(createContent(restaurantJson, karttanaJson[i]));
                    map.panTo(marker[i]['position']);
                    infoWindow.open(map, marker[i]); // 吹き出しの表示
                });
        });

    }
}

function createContent(restaurantJson, karttanaJson) {
    return '' +
        '<table class="table">' +
        '<thead>' +
        '   <tr><h4><a href="/restaurant/detail/' + restaurantJson.id + '">' +
        restaurantJson.name + '</a></h4></tr>' +
        '</thead>' +
        '  <tbody>' +
        '    <tr>' +
        '      <td>詳細</td>' +
        '      <td>' + restaurantJson.text + '</td>' +
        '    </tr>' +
        '    <tr>' +
        '      <td>サナ</td>' +
        '      <td>' + karttanaJson.sana + '</td>' +
        '    </tr>' +
        '  </tbody>' +
        '</table>';
}

function setCenter(map) {
    navigator.geolocation.getCurrentPosition(function (position) {
        map.setCenter({
            lat: position.coords.latitude,
            lng: position.coords.longitude
        });
    });
}
