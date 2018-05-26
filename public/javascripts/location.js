
function handleLocationError(browserHasGeolocation, infoWindow, pos) {
    infoWindow.setPosition(pos);
    infoWindow.setContent(browserHasGeolocation ?
        'Error: The Geolocation service failed.' :
        'Error: Your browser doesn\'t support geolocation.');
}

function check(results) {
    $('span').remove();
    for (let key in results) {
        let element = document.getElementById(key);
        let span = document.createElement('span');
        span.innerHTML = results[key];
        span.className = "text-danger";
        // inputの上にエラーの生成
        element.parentNode.insertBefore(span, element);
    }
}


function sendForm() {

    var data = JSON.stringify(parseJson($('#paikka-form').serializeArray()));

    $.post({
        url: "/paikka/create",
        data: data,
        contentType: 'application/json',
    }).then(
        results => {
            if(results["ok"]) location.href= '/paikka/' + results["ok"];
            check(results);
        },
        error => alert("なにか問題が発生しました")
    );

}

function parseJson(data) {
    var returnJson = {};
    for (idx = 0; idx < data.length; idx++) {
        returnJson[data[idx].name] = data[idx].value
    }
    return returnJson;
}


function initMap(){
    var map = new google.maps.Map(document.getElementById('map'), {
        center: "", // 地図の中心を指定
        zoom: 17// 地図のズームを指定
    });

    // geolocationが使えるか
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function(position) {

            var geocoder = new google.maps.Geocoder();

            map.setCenter({
                lat: position.coords.latitude,
                lng: position.coords.longitude
            });

            var marker = new google.maps.Marker({ // マーカーの追加
                position: map.center, // マーカーを立てる位置を指定
                map: map // マーカーを立てる地図を指定
            });

            // 　マーカーを常に中心に配置する
            google.maps.event.addListener(map, 'center_changed', function(){
                var location = map.getCenter();
                marker.setPosition(location);
                infoWindow.close();
            });
             var infoWindow = new google.maps.InfoWindow({ // 吹き出しの追加
                content: '' // 吹き出しに表示する内容
            });

            marker.addListener('click', function() { // マーカーをクリックしたとき

                geocoder.geocode( {latLng: marker.position}, function(results, status){
                    results = results[0].formatted_address.split(' ');
                    if (results[0].match(/\d/g) == null) {
                        alert('この場所は登録出来ません');
                        return;
                    }

                    // resultからう郵便番号、住所を取得。その後モーダルに出力
                    let postalCode = results[0].match(/\d/g).toString().replace(/,/g,'');
                    let address = results[1];
                    document.getElementById("postalCode").value = postalCode;
                    document.getElementById("address").value = address;
                    $('.modal').modal('show');
                });
            });

        }, function() {
            handleLocationError(true, infoWindow, map.getCenter());
        });
    } else {
        // Browser doesn't support Geolocation
        handleLocationError(false, infoWindow, map.getCenter());
    }
}


