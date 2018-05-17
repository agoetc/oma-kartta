
function initMap() {
    if (navigator.geolocation) {
        $.getJSON(
            "/getkartalla", //リクエストURL
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


function setMarker(map,kartallaJson) {
    let marker = [];

    for (let i in kartallaJson) {
        marker[i] = new google.maps.Marker({ // マーカーの追加
            position: new google.maps.Marker({lat: kartallaJson[i].lat, lng: kartallaJson[i].lng}), // マーカーを立てる位置を指定
            map: map // マーカーを立てる地図を指定
        });


        /* マーカーをクリックしたとき
         * jsonからinfoWindowを作成する.
         */
        marker[i].addListener('click', function () {
            $.getJSON(
                "/getpaikka/" + kartallaJson[i]['paikkaId'],
                function (paikkaJson, status) {
                    document.getElementById('paikka-title').innerHTML = createHeader(paikkaJson);
                    document.getElementById('paikka-body').innerHTML = paikkaJson.text ;
                    setSter(kartallaJson[i]);
                    $('.modal').modal('show');
                });
        });

    }
}

function setSter(kartallaJson) {
    let generalStar = kartallaJson['star'] * 100 / 5;

    $(function() {
        $(".star-rating-front").css({
            "width": generalStar + "%"
        });
    });
}

function createHeader(paikkaJson) {
    return '' +
        '<h5 class="text-muted"><a href="/paikka/' + paikkaJson.id + '">' +
        paikkaJson.name + '</a></h5>'
}

function setCenter(map) {
    navigator.geolocation.getCurrentPosition(function (position) {
        map.setCenter({
            lat: position.coords.latitude,
            lng: position.coords.longitude
        });
    });
}
