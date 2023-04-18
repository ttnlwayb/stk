<%@ page language="java" contentType="text/html; charset=BIG5"
    pageEncoding="BIG5"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="BIG5">
<title>KDJ Chart</title>
<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.9.3/Chart.min.js"></script>
<style>
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
	'rgb(0, 255, 0)',
	'rgb(0, 0, 255)',
	'rgb(255, 0, 0)',
	'rgb(255, 255, 0)',
	'rgb(255, 0, 255)',
	'rgb(0, 255, 255)',
	'rgb(255, 255, 255)'
];
var result = ${result};
var labels = result['t'];
delete result['t'];
var cloneLab = $('#lab').clone(true);
var index = 0;
var item = '';
cloneLab.attr('id', 'lab' + index);
cloneLab.html(item);
cloneLab.appendTo('#mainDiv');
var cloneChart = $("#chart").clone(true);
cloneChart.attr('id', 'chart' + index);
jQuery("<div></div>", {
    id: 'div' + index
})
.css('height', '800px')
.css('width', '1500px')
.appendTo('#mainDiv');
cloneChart.appendTo('#div' + index);
let datasets = [];
Object.keys(result).forEach(function (item, index) {
	let data = result[item];
	datasets[index] = {
    	label: item,
        data: data,
        fill: false,
        borderColor: rgbs[index]
    };
});
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
var waittime = 29 * 1000;
$(document).ready(function() {
	var date = new Date();
	console.log('ready time' + date);
	if (date.getHours() < 13) {
		setTimeout(() => location.reload(), waittime * 1000);
	} else if (date.getHours() == 13 &&　date.getMinutes() < 25) {
		setTimeout(() => location.reload(), waittime * 1000);
	}
});
</script>
</body>
</html>