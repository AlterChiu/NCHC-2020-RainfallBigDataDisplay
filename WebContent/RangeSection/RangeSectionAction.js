const testPngProperties = {
    id: "test",
    name: "test",
    centerX: 121,
    centerY: 23.6,
    zoom: 7,
    maxX: 121.5,
    maxY: 120.5,
    minX: 24.0,
    minY: 23.0,

    pngUrl: [{
        rainfallUrl: "../pngResource/Tainan/12/100/10/forward/20170101/0_rainfall.png",
        floodUrl: "../pngResource/Tainan/12/100/10/forward/20170101/0_flood.png",
    }]
}


class RangeSectionAction {

    static rangeSectionAction;

    constructor() {
        this.selectedClass = "range-selection-value-selected";
        this.rangeClass = "range-selection-value";
        this.countyProperties;
    }

    getSelectedClass() {
        return this.selectedClass;
    }

    getRangeClass() {
        return this.rangeClass;
    }


    /*
      County Properties in Jquery jsonObject {
           countryID :{
                id: countryName(EN),
                name: countryName(CH),
                centerX: mapCenterX(WGS84_X),
                centerY: mapCenterY(WGS84_Y),
                 zoom: mapZoom(Integer) },...... 
            }
     */
    setCountryProperties(countyProperties) {
        this.countyProperties = countyProperties;
    }

    getCountryProperties() {
        return this.countyProperties;
    }
}
RangeSectionAction.rangeSectionAction = new RangeSectionAction();
// <+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++>
// <=====================PRIVATE FUNCTION==============================>
// <+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++>


/*
 * get the multi column boostrap grid which operating on counties and resultBox
 * selection
 * 
 * also initial that select the first item
 */
var getMultiColumnRangeSelectionContent = function (idList, valueList, columnNum) {
    var retrunContext = "";
    for (var index = 0; index < idList.length; index++) {
        retrunContext += "<div class ='row py-1'>";
        for (var column = 0; column < columnNum && index < idList.length; column++) {
            retrunContext += "<div class ='col-" + 12 / columnNum + "'>";
            retrunContext += "<div data-value=\"";

            // setting value
            try {
                retrunContext += valueList[index];
            } catch (e) {
                console.log("no value for setting " + idList[index]);
            }

            // setting text
            retrunContext += "\" class='" + RangeSectionAction.rangeSectionAction.getRangeClass() + "'>" + idList[index] + "</div>";
            retrunContext += "</div>";
            index++;
        }
        retrunContext += "</div>";
    }
    return retrunContext;
}






// <+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++>
// <=====================Dynamic Action================================>
// <+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++>


const initialEventSelection = async()=>{
    const selectionColNum = 4;

    // get selections
    $.ajax({
        url: "./eventSelection/getEventProperties",
        dataType: "json",
        data: {},
        success: function (data) {
            let selectedClass = RangeSectionAction.rangeSectionAction.getSelectedClass();
            let normalClass = RangeSectionAction.rangeSectionAction.getRangeClass();

            var durationList = data.duration;
            $("#rainfallDuration-value").append(getMultiColumnRangeSelectionContent(durationList , durationList , selectionColNum))
            $("#rainfallDuration-value").find("." + normalClass).first().addClass(selectedClass);

            var intensiveList = data.intensive;
            $("#rainfallIntensity-value").append(getMultiColumnRangeSelectionContent(intensiveList , intensiveList , selectionColNum))
            $("#rainfallIntensity-value").find("." + normalClass).first().addClass(selectedClass);

            var accumulationList = data.accumulation;
            $("#rainfallAccumulation-value").append(getMultiColumnRangeSelectionContent(accumulationList , accumulationList , selectionColNum))
            $("#rainfallAccumulation-value").find("." + normalClass).first().addClass(selectedClass);

            var patternName=[];
            var patternValue=[];
            $.each(data.pattern , function(key , value){
                patternName.push(key);
                patternValue.push(value);
            });
            $("#rainfallPattern-value").append(getMultiColumnRangeSelectionContent(patternName , patternValue , selectionColNum))
            $("#rainfallPattern-value").find("." + normalClass).first().addClass(selectedClass);

        }
    });
}



/*
 * county-selection
 * 
 * initial county box
 */
const initialCounties = async()=> {
    const countyColNum = 3;

    // get countiesList

    $.ajax({
        url: "./eventSelection/getCounties",
        dataType: "json",
        contentType: "application/x-www-form-urlencoded; charset=utf-8",
        data: {},
        success: function (data) {
            RangeSectionAction.rangeSectionAction.setCountryProperties(data);

            let selectedClass = RangeSectionAction.rangeSectionAction.getSelectedClass();
            let normalClass = RangeSectionAction.rangeSectionAction.getRangeClass();

            var countriesList = [];
            var countriesValueList = [];
            var countriesAmount = [];
            $.each(RangeSectionAction.rangeSectionAction.getCountryProperties(), function (key, value) {
                countriesList.push(value["name"]);
                countriesAmount.push(value["amount"]);
                countriesValueList.push(key);
            })

            // setting initial country title with values
            $("#county-value").append(getMultiColumnRangeSelectionContent(countriesList, countriesValueList, countyColNum));
            
            // setting county amount 
            $("#county-value").find("." + normalClass).each(function(index){
                $(this).attr("data-amount" , countriesAmount[index]);
            });


            // setting first trigger
            $("#county-value").find("." + normalClass).first().addClass(selectedClass);
            initialRangeStyleChangeOnClick();
            reloadResultBox();

            // setting county subtitle and each counties onClick
            $.each($("#county-value").find("." + normalClass) , function(){
                $(this).click(function(){
                    $("#countyTitle").empty();

                    var titleContent="";
                    titleContent += "<span>" + $(this).text() + "</span>";
                    titleContent += "<span class=\"range-selection-en\">(" + $(this).data("amount") + ")</span>";

                    $("#countyTitle").append(titleContent);
                    $("#countyTitle").trigger("click");
                });
            });            
            $("#county-value").find("." + normalClass).first().trigger("click");
        },
    });

};

// initial page
initialEventSelection().then(initialCounties());








/*
 * range-selection onclick function
 * 
 * 1. there is only one button(selection) in each section 2. anybutton onclick
 * will change the content in result box
 * 
 */

function initialRangeStyleChangeOnClick() {
    $(".range-selection-value").off("click")
    $(".range-selection-value").click(function () {

        let selectedClass = RangeSectionAction.rangeSectionAction.getSelectedClass();

        // detect on change
        if (!$(this).hasClass(selectedClass)) {

            // get parent by class(.card-body)
            var parentCard = $(this).closest(".card-body");

            // remove other selected button
            parentCard.find("." + selectedClass).removeClass(selectedClass);
            $(this).addClass(selectedClass)

            // reload event result box
            reloadResultBox();
        }
    })
}











/*
 * range-result
 * 
 * show the result which selected by each section ｛
 */

var reloadResultBox = function () {
    // result box properties
    const resultColNum = 3
    const resultBoxClass = "selection-result-box";
    let selectedClass = RangeSectionAction.rangeSectionAction.getSelectedClass();
    let normalClass = RangeSectionAction.rangeSectionAction.getRangeClass();

    // apped result event to result box
    $("." + resultBoxClass).empty();

    $.ajax({
        url: "./eventSelection/getEvents",
        dataType: "json",
        data: {
            county: $("#county-value").find("." + selectedClass).data("value"),
            duration: $("#rainfallDuration-value").find("." + selectedClass).data("value"),
            accumulation: $("#rainfallAccumulation-value").find("." + selectedClass).data("value"),
            intensitve: $("#rainfallIntensity-value").find("." + selectedClass).data("value"),
            pattern: $("#rainfallPattern-value").find("." + selectedClass).data("value")
        },
        success: function (eventArray) {
            var outArray = [];
            $.each(eventArray["eventList"], function (index, value) {
                outArray.push(value);
            });

            // append text in result box
            $("." + resultBoxClass).append(getMultiColumnRangeSelectionContent(outArray, outArray, resultColNum));

            // add onClick function in result events
            $("." + resultBoxClass).find("." + normalClass).on("click", function () {
                getEventPngList($(this));
            });
        }
    })
}

var getEventPngList = function (event) {

    let selectedClass = RangeSectionAction.rangeSectionAction.getSelectedClass();
    let normalClass = RangeSectionAction.rangeSectionAction.getRangeClass();

    if (!event.hasClass(selectedClass)) {

        // before respons success, block the page
        $(".loadingPage").show();

        // trigger stop to initial time-control
        try {
            $("#stop-button").trigger("click");
        } catch (error) {
        }
       

        // get parent by class(.card-body)
        var parentCard = event.closest(".selection-result-box");

        // remove other selected button
        parentCard.find("." + selectedClass).removeClass(selectedClass);
        event.addClass(selectedClass);

        // get pngList
        $.ajax({
            url: "./eventSelection/getPNG/getRainfallFlood",
            dataType: "json",
            data: {
                county: $("#county-value").find("." + selectedClass).data("value"),
                eventID: event.data("value")
            },
            success: function (data) {
            
                // setting pngList
                TimeLineTimer.timeLineTimer.setPngProperties(data);

                // set timeLine control
                $("#timeLine").attr("max", data["pngUrl"].length);

               

                // set map to county center
                MapControl.mapControl.setViewFloodMap(data.flood.maxX, data.flood.maxY, data.flood.minX , data.flood.minY);

                // load first png
                TimeLineTimer.timeLineTimer.loadPNG();
            },
            complete: function (XMLHttpRequest, textStatus) {
                // remove block page
                $(".loadingPage").css("display", "none");
            }
        })
    }


}