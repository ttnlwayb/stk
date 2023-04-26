<html>
<head>
<meta charset="BIG5">
<title>Stk Chart</title>
<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.9.3/Chart.min.js"></script>
<style>
#lab0{
    position: relative;
    left: 40%;
    transform: translate(-50%,-50%)
}
</style>
</head>
<body bgcolor="#DDDDDD">
<div id='mainDiv' style='width:80%'></div>
<div id='tmpDiv' style='display: none'>
	<label id='lab'><label>
	<canvas id="chart"></canvas>
</div>
<script>
var rgbs = [
	'rgb(0, 0, 255)',
	'rgb(0, 255, 0)',
	'rgb(255, 0, 0)',
	'rgb(255, 255, 0)',
	'rgb(255, 0, 255)',
	'rgb(0, 255, 255)',
	'rgb(255, 255, 255)'
];
var result = ${result};
console.log('result');
var size = result.ma5.t.length;
var labels = result.ma5.t;
var index = 0;
var item = '';
var cloneLab = $('#lab').clone(true);
cloneLab.attr('id', 'lab' + index);
cloneLab.appendTo('#mainDiv');
var cloneChart = $("#chart").clone(true);
cloneChart.attr('id', 'chart' + index);
jQuery("<div></div>", {
    id: 'div' + index
})
.css('height', '250px')
.css('width', '1200px')
.appendTo('#mainDiv');
cloneChart.appendTo('#div' + index);

var datasets = [];
datasets[0] = {
    	label: 'ma5',
        data: result.ma5.m,
        fill: false,
        borderColor: rgbs[0]
    };
datasets[1] = {
    	label: 'close',
        data: result.ma5.c,
        fill: false,
        borderColor: rgbs[1]
    };
/*datasets[2] = {
    	label: 'diff',
        data: result.ma5.d,
        fill: false,
        borderColor: rgbs[2]
    };*/
var ctx = document.getElementById('chart' + index);
var myChart = new Chart(ctx, {
    type: 'line',
    options: {  
        responsive: true,
        maintainAspectRatio: false
    },
    data: {
        labels: labels,
        datasets: datasets
    },
});
cloneLab.html('' + (result.ma5.d[result.ma5.d.length - 1]));

</script>
</body>
</html>