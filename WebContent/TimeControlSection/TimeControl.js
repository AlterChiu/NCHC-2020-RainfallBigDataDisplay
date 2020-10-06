//<++++++++++++++++++++++++++++++++++++++++++++++++++++>
//<==================Initial Function==================>
//<++++++++++++++++++++++++++++++++++++++++++++++++++++>
var initailTimeLineValue = function () {
    $("#timeLine").val(1);
}
initailTimeLineValue();



// <++++++++++++++++++++++++++++++++++++++++++++++++++++>
// <==================Private Function==================>
// <++++++++++++++++++++++++++++++++++++++++++++++++++++>


/*
 * in timeLine platform, switch selected sytle
 */
var platformStyleSwitch = function (e) {
    const timeLineButtonSelected = "playControl-button-selected";


    // if not playing
    if (!e.hasClass(timeLineButtonSelected)) {

        // remove other button
        var platform = e.closest(".playControl-platform");
        platform.find("." + timeLineButtonSelected).removeClass(timeLineButtonSelected);

        // add selected class to play button
        e.addClass(timeLineButtonSelected);
    }

}


/*
 * timeLine platform control, active while run and pause
 */
class TimeLineTimer {

    static timeLineTimer;
    constructor() {
    /*
	 pngProperties = {
        id : county(EN),
        name : county(CH),
        centerX: wgs84X,
        centerY : wgs84Y,
        zoom : zoom,
        maxX : pngBoundMaxX,
        maxY : pngBoundMaxY,
        minX : pngBoundMinX,
        minY : pngBoundMinY,

        pngUrl:[
            {
                rainfallUrl : "image/png;base64,......" , 
                floodUrl : "image/png;base64,......"
            },......
        ]
	 */
        this.pngProperties;
        this.timers = [];
        this.timeStepDelay = 500; // millisecond
    }

    setPngProperties(properties){
        this.pngProperties = properties;
    }

    getPngProperties(){
        return this.pngProperties;
    }



    run(startTime, endTime) {
        this.pause();

        // disable the map control
        MapControl.mapControl.disableDragging();
        this.timers.push(setTimeout(() => {
            MapControl.mapControl.enableDragging();
        }, this.timeStepDelay * (endTime - startTime)));

        for (let index = 0; index <= (endTime - startTime); index++) {
            this.timers.push(setTimeout(function () {
                $("#timeLine").val(index + startTime);
            }, this.timeStepDelay * index))
        }

        this.timers.push(setTimeout(function () {
            this.stop();
        }, this.timeStepDelay * (endTime - startTime +2)))
    }

    pause() {
        this.timers.forEach(timer => clearTimeout(timer))
        this.timers = [];
    }

    stop() {
        this.pause();
        $("#timeLine").val(1);
    }

    reSet() {
        this.stop();
        MapControl.mapControl.setViewRainfallMap(this.pngProperties.centerX, this.pngProperties.centerY, TimeLineTimer.pngProperties.zoom);
        MapControl.mapControl.setViewRainfallMap(this.pngProperties.centerX, this.pngProperties.centerY, TimeLineTimer.pngProperties.zoom);
    }

    loadPNG(){
        MapControl.mapControl.disableDragging();
        var currentTimeStep = $("#timeLine").val() -1 ;
        try{
            var rainfallUrl  = this.pngProperties["pngUrl"][currentTimeStep].rainfallUrl;
            var floodUrl = this.pngProperties["pngUrl"][currentTimeStep].floodUrl;
    
            var maxX = this.pngProperties.maxX;
            var maxY = this.pngProperties.maxY;
            var minX = this.pngProperties.minX;
            var minY = this.pngProperties.minY;
    
            MapControl.mapControl.addRainfallPNG(rainfallUrl , maxX , maxY , minX , minY);
            MapControl.mapControl.addFloodPNG(floodUrl , maxX , maxY , minX , minY);
            MapControl.mapControl.enableDragging();
        }catch(e){
        }
    }
}
TimeLineTimer.timeLineTimer = new TimeLineTimer();




// <++++++++++++++++++++++++++++++++++++++++++++++++++++>
// <===================Button Function==================>
// <++++++++++++++++++++++++++++++++++++++++++++++++++++>


/*
 * play playform, play button
 */
$("#play-button").on("click", function () {

    // style switch
    platformStyleSwitch($(this));

    // run step from current timeStep
    var startTime = parseInt($("#timeLine").val());
    var endTime = parseInt($("#timeLine").attr("max"));
    TimeLineTimer.timeLineTimer.run(startTime, endTime);
});

/*
 * play playform, pause button
 */
$("#pause-button").on("click", function () {

    // style switch
    platformStyleSwitch($(this));

    // pause timer
    TimeLineTimer.timeLineTimer.pause();
})

/*
 * play playform, stop button
 */
$("#stop-button").on("click", function () {

    // style switch
    platformStyleSwitch($(this));

    // set timeLine to 1
    TimeLineTimer.timeLineTimer.stop();
})


// <++++++++++++++++++++++++++++++++++++++++++++++++++++>
// <=================TimeLine onChange==================>
// <++++++++++++++++++++++++++++++++++++++++++++++++++++>
$("#timeLine").on("change", function () {
    TimeLineTimer.timeLineTimer.loadPNG();
})