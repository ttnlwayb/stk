<%@ page language="java" contentType="text/html; charset=BIG5"
    pageEncoding="BIG5"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="BIG5">
<title>Stk Chart</title>
<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.9.3/Chart.min.js"></script>
<style>
</style>
</head>
<body>
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
var labels = [1, 2, 3, 4, 5, 6, 7, 8 ,9, 10, 11 ,12];
Object.keys(result).forEach(function (item, index) {
	var cloneLab = $('#lab').clone(true);
	cloneLab.attr('id', 'lab' + index);
	cloneLab.html(item);
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
	var rows = [];
	let datasets = [];
	for (var i = 0; i < result[item].length; i++) {
		let data = result[item][i]['d'];
		let labName = data[0];
		data.shift();
		data.reverse();
		datasets[i] = {
	    	label: labName,
	        data: result[item][i]['d'],
	        fill: false,
	        borderColor: rgbs[i]
        };
	}
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
});
</script>
</body>
</html>