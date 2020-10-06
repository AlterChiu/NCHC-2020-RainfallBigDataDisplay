class MapControl {

    static mapControl;


    constructor(){
        this.rainfallMap = L.map("rainfallMap").setView([23.6,121],7);
        this.ainfallPngLayer = "";
    
        this.floodMap = L.map("floodMap").setView([23.6,121],7);
        this.floodPngLayer = "";
    }


    setViewRainfallMap(x, y, z) {
        this.rainfallMap.setView([y, x], z);
    }

    setViewFloodMap(x, y, z) {
        this.floodMap.setView([y, x], z);
    }
    


    /*
        initail map
    */
    initialMap(map) {

        /*
            setting layers
        */
        var streetLayer = L.tileLayer('https://api.mapbox.com/styles/v1/{id}/tiles/{z}/{x}/{y}?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw', {
            maxZoom: 18,
            id: 'mapbox/streets-v11',
            tileSize: 512,
            zoomOffset: -1
        }).addTo(map);

        var satelliteLayer = L.tileLayer('https://api.mapbox.com/styles/v1/{id}/tiles/{z}/{x}/{y}?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw', {
            maxZoom: 18,
            id: 'mapbox/satellite-streets-v11',
            tileSize: 512,
            zoomOffset: -1
        }).addTo(map);


        /*
            setting controler 
         */
        var layers = {
            "Street": streetLayer,
            "Satellite": satelliteLayer
        };

        L.control.layers(layers).addTo(map);
    }

    /*
        set image
    */
    addRainfallPNG(imageUrl , maxX , maxY , minX , minY){
        let imageBound = [[minX , minY] , [maxX , maxY]];
        try{
            this.rainfallMap.removeLayer(this.rainfallPngLayer);
        }catch(e){
        }
        this.rainfallPngLayer = new L.imageOverlay( imageUrl , imageBound);
        this.rainfallPngLayer.addTo(this.rainfallMap);
    }

    addFloodPNG(imageUrl , maxX , maxY , minX , minY){
        let imageBound = [[minX , minY] , [maxX , maxY]];
        try{
            this.floodMap.removeLayer(this.floodPngLayer);
        }catch(e){
        }
        this.floodPngLayer = new L.imageOverlay(imageUrl , imageBound);
        this.floodPngLayer.addTo(this.floodMap);
        this.rainfallMap.flyToBounds(imageBound);
    }


    /*
        prevent for stack exceed
    */
    disableDragging() {
        this.rainfallMap.dragging.disable();
        this.floodMap.dragging.disable();
    }

    enableDragging() {
        this.rainfallMap.dragging.enable();
        this.floodMap.dragging.enable();
    }
}

MapControl.mapControl = new MapControl();

var initialMap = function() {
    // initial the map
    MapControl.mapControl.initialMap(MapControl.mapControl.floodMap);
    MapControl.mapControl.initialMap(MapControl.mapControl.rainfallMap);

    MapControl.mapControl.rainfallMap.sync(MapControl.mapControl.floodMap);
    MapControl.mapControl.floodMap.sync(MapControl.mapControl.rainfallMap);
}
initialMap();
