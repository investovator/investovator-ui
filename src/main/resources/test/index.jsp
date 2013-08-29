
<html>
<head>
    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
    <script type="text/javascript" src="http://code.highcharts.com/stock/highstock.js"></script>
    <script type="text/javascript" src="http://code.highcharts.com/stock/modules/exporting.js"></script>
    <title>Example :: Spring Application</title></head>
<body>
<h1>Example - Spring Application</h1>
<p>This is my test.</p>

<div id="container" style="height: 500px; min-width: 500px"></div>


<script type="text/javascript">
    function test() {



        $.getJSON('http://localhost:8080/ui-0.1-a/graphData?callback=?', function(data) {

            var options= {



                rangeSelector : {
                    selected : 1
                },

                title : {
                    text : 'IBM Stock Price'
                },

                    series: [{
                name : 'IBM',
                data : [1,2,3,2,1],
                tooltip: {
                    valueDecimals: 2
                }
            }],
                chart : {
                    events : {
                        load : function() {

                            // set up the updating of the chart each second
                            // var series = this.series[0];
                            setInterval(function() {
//                                var x = (new Date()).getTime(), // current time
//                                        y = Math.round(Math.random() * 100);
//                                series.addPoint([x, y], true, true);
                                //this.series[0]=$.get('http://localhost:8080/ui-0.1-a/graphData?callback=?')
                                chart.series[0].setData($.get('http://localhost:8080/ui-0.1-a/graphData'));
                                console.log($.get('http://localhost:8080/ui-0.1-a/graphData'));
                            }, 1000);
                        }
                    },
                    renderTo : 'container'
                }

            }

            // Create the chart
            console.log(data);
            options.series[0].data=[5,6,7,8];
            chart = new Highcharts.StockChart(options);
            //$('#container').highcharts('StockChart', options

//                chart : {
//                    events : {
//                        load : function() {
//
//                            // set up the updating of the chart each second
//                            var series = this.series[0];
//                            setInterval(function() {
////                                var x = (new Date()).getTime(), // current time
////                                        y = Math.round(Math.random() * 100);
////                                series.addPoint([x, y], true, true);
//                                //this.series[0]=$.get('http://localhost:8080/ui-0.1-a/graphData?callback=?')
//                                //dataSet.data=$.get('http://localhost:8080/ui-0.1-a/graphData?callback=?');
//                                console.log($.get('http://localhost:8080/ui-0.1-a/graphData?callback=?'));
//                            }, 1000);
//                        }
//                    }
//                },






        });

    }

    window.onload = test;

</script>

<%--<button onclick="test()">Try it</button>--%>

</body>
</html>