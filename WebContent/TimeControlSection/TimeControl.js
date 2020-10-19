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
        amount : eventAmount,
        flood : {
            maxX : pngBoundMaxX,
            maxY : pngBoundMaxY,
            minX : pngBoundMinX,
            minY : pngBoundMinY,
        },
        rainfall : {
            maxX : pngBoundMaxX,
            maxY : pngBoundMaxY,
            minX : pngBoundMinX,
            minY : pngBoundMinY,
        }

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

    setPngProperties(properties) {
        this.pngProperties = properties;
    }

    getPngProperties() {
        return this.pngProperties;
    }



    run(startTime, endTime) {
        this.pause();

        // // disable the map control
        // MapControl.mapControl.disableDragging();
        // this.timers.push(setTimeout(() => {
        //     MapControl.mapControl.enableDragging();
        // }, this.timeStepDelay * (endTime - startTime)));

        for (let index = 0; index <= (endTime - startTime); index++) {
            this.timers.push(setTimeout(function () {
                $("#timeLine").val(index + startTime);
                $('#timeLine').trigger('change');
            }, this.timeStepDelay * index))
        }

        this.timers.push(setTimeout(function () {
            $("#stop-button").trigger("click");
        }, this.timeStepDelay * (endTime - startTime + 2)))
    }

    pause() {
        this.timers.forEach(timer => clearTimeout(timer))
        this.timers = [];
    }

    stop() {
        this.pause();
        $("#timeLine").val(1);
        $("#timeLine").trigger("change")
    }

    reSet() {
        this.stop();
        MapControl.mapControl.setViewRainfallMap(this.pngProperties.maxX, this.pngProperties.maxY, this.pngProperties.minX, this.pngProperties.minY);
        // MapControl.mapControl.setViewRainfallMap(this.pngProperties.centerX, this.pngProperties.centerY, TimeLineTimer.pngProperties.zoom);
    }

    loadPNG() {
        MapControl.mapControl.disableDragging();
        var currentTimeStep = $("#timeLine").val() - 1;

        // load rainfall png

        var rainfallUrl = this.pngProperties["pngUrl"][currentTimeStep].rainfallUrl;
        var maxX = this.pngProperties.rainfall.maxX;
        var maxY = this.pngProperties.rainfall.maxY;
        var minX = this.pngProperties.rainfall.minX;
        var minY = this.pngProperties.rainfall.minY;

        MapControl.mapControl.addRainfallPNG(rainfallUrl, maxX, maxY, minX, minY);

        // load flood png

        var floodUrl = this.pngProperties["pngUrl"][currentTimeStep].floodUrl;
        var maxX = this.pngProperties.flood.maxX;
        var maxY = this.pngProperties.flood.maxY;
        var minX = this.pngProperties.flood.minX;
        var minY = this.pngProperties.flood.minY;

        MapControl.mapControl.addFloodPNG(floodUrl, maxX, maxY, minX, minY);

        MapControl.mapControl.enableDragging();
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

$("#timeLine").on("click", function () {
    $("#pause-button").trigger("click");
})