
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

        $.getJSON('http://localhost:8080/ui-0.1-a/graphData', function(data) {    //$.getJSON('http://localhost:8080/ui-0.1-a/graphData', function(data) {
            // Create the chart
            $('#container').highcharts('StockChart', {


                rangeSelector : {
                    selected : 1
                },

                title : {
                    text : 'JASA Simulation'
                },

                series : [{
                    name : 'AAPL',
                    data : data,
                    tooltip: {
                        valueDecimals: 2
                    }
                }]
            });
        });

    }

    window.onload = test;

</script>

<%--<button onclick="test()">Try it</button>--%>

</body>
</html>