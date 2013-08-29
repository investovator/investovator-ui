
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
        console.log("in");
        $.getJSON('http://localhost:8080/ui-0.1-a/graphData?callback=?', function(data) {
            console.log("ss");
            // Create the chart
            console.log(data);
            $('#container').highcharts('StockChart', {


                rangeSelector : {
                    selected : 1
                },

                title : {
                    text : 'AAPL Stock Price'
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