
function initMap(){
    var map = new google.maps.Map(document.getElementById('map'), {
        center: "", // 地図の中心を指定
        zoom: 17// 地図のズームを指定
    });

    // geolocationが使えるか
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function(position) {

            var pushpos;
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
                pushpos = marker.position;
                geocoder.geocode( {latLng: pushpos}, function(results, status){
                results = results[0].formatted_address;
                var dom = '<form action="/restaurant/new" method="post">' +
                    '<button type="submit" name="content" value="'+results+'">ここを登録する</button>' +
                    '</form>';
                infoWindow.setOptions({content: dom});
                infoWindow.open(map, marker); // 吹き出しの表示
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


function handleLocationError(browserHasGeolocation, infoWindow, pos) {
    infoWindow.setPosition(pos);
    infoWindow.setContent(browserHasGeolocation ?
        'Error: The Geolocation service failed.' :
        'Error: Your browser doesn\'t support geolocation.');
}