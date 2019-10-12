var map;
function initMap() {
  var cityLocation = new google.maps.LatLng(-33.91722, 151.23064);
  map = new google.maps.Map(
      document.getElementById('map'),
      {center: cityLocation, zoom: 16});

  var iconBase =
      'https://static.thenounproject.com/png/';
      var size1 = 40;
      var size2 = 40;
 
      var icons = {
    police: {
      icon: {
        url: iconBase + '11027-200.png',
        scaledSize: new google.maps.Size(size1, size2)
      }
    },
    hospital: {
      icon: {
        url: iconBase + '231877-200.png',
        scaledSize: new google.maps.Size(size1, size2)
      }
    },
    construction: {
      icon: {
        url: iconBase + '4275-200.png',
        scaledSize: new google.maps.Size(size1, size2)
      }
    },
    alert: {
      icon: {
        url: iconBase + '10890-200.png',
        scaledSize: new google.maps.Size(size1, size2)
      }
    },
    train_subway: {
      icon: {
        url: iconBase + '26862-200.png',
        scaledSize: new google.maps.Size(size1, size2)
      }
    },
    airport: {
      icon: {
        url: iconBase + '1031715-200.png',
        scaledSize: new google.maps.Size(size1, size2)
      }
    },
    supplies: {
      icon: {
        url: iconBase + '2374718-200.png',
        scaledSize: new google.maps.Size(size1, size2)
      }
    },
  };

  var features = [
    {
      position: new google.maps.LatLng(-33.91721, 151.22630),
      type: 'police'
    }, {
      position: new google.maps.LatLng(-33.91539, 151.22820),
      type: 'police'
    }, {
      position: new google.maps.LatLng(-33.91747, 151.22912),
      type: 'alert'
    }, {
      position: new google.maps.LatLng(-33.91910, 151.22907),
      type: 'police'
    }, {
      position: new google.maps.LatLng(-33.91725, 151.23011),
      type: 'alert'
    }, {
      position: new google.maps.LatLng(-33.91872, 151.23089),
      type: 'police'
    }, {
      position: new google.maps.LatLng(-33.91784, 151.23094),
      type: 'police'
    }, {
      position: new google.maps.LatLng(-33.91682, 151.23149),
      type: 'police'
    }, {
      position: new google.maps.LatLng(-33.91790, 151.23463),
      type: 'police'
    }, {
      position: new google.maps.LatLng(-33.91666, 151.23468),
      type: 'police'
    }, {
      position: new google.maps.LatLng(-33.916988, 151.233640),
      type: 'police'
    }, {
      position: new google.maps.LatLng(-33.91662347903106, 151.22879464019775),
      type: 'airport'
    }, {
      position: new google.maps.LatLng(-33.916365282092855, 151.22937399734496),
      type: 'construction'
    }, {
      position: new google.maps.LatLng(-33.91665018901448, 151.2282474695587),
      type: 'train_subway'
    }, {
      position: new google.maps.LatLng(-33.919543720969806, 151.23112279762267),
      type: 'train_subway'
    }, {
      position: new google.maps.LatLng(-33.91608037421864, 151.23288232673644),
      type: 'construction'
    }, {
      position: new google.maps.LatLng(-33.91851096391805, 151.2344058214569),
      type: 'train_subway'
    }, {
      position: new google.maps.LatLng(-33.91818154739766, 151.2346203981781),
      type: 'train_subway'
    }, {
      position: new google.maps.LatLng(-33.91727341958453, 151.23348314155578),
      type: 'library'
    }
  ];

  // Create markers.
  for (var i = 0; i < features.length; i++) {
    var marker = new google.maps.Marker({
      position: features[i].position,
      icon: icons[features[i].type].icon,
      map: map
    });
  };
}