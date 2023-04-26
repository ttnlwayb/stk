<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Stk 0050 Table</title>
</head>
<link rel="stylesheet"
	href="https://cdn.datatables.net/1.11.3/css/jquery.dataTables.min.css">
<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
<script src="https://code.jquery.com/ui/1.13.2/jquery-ui.js"></script>
<link type="text/css" rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css"/>
<script src="https://cdn.datatables.net/1.11.3/js/jquery.dataTables.min.js"></script>
<style>
td {
	white-space: nowrap;
	width: 178px;
}
#mainDiv{
}
#lab0{
    position: relative;
    left: 40%;
    transform: translate(-50%,-50%)
}
</style>
<div id='mainDiv' style='width:98%'></div>
<div id='tmpDiv' style='display: none'>
	<label id='lab' style='font-size:20px' ><label>
			<table id="table">
				<thead>
					<tr style='background-color: #A6CDE7'>
						<th id='th1'>min time</th>
						<th>min value</th>
						<th>max time</th>
						<th>diff</th>
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
</div>
<script>
	var result = ${result};
	function initData() {
		var index = 0;
		var item = '';
		var cloneLab = $('#lab').clone(true);
		cloneLab.attr('id', 'lab' + index);
		cloneLab.html(
				[
					'ma1: ' + result.ma.ma1,
					'ma5: ' + result.ma.ma5,
					'ma20: ' + result.ma.ma20,
					'ma60: ' + result.ma.ma60,
					'ma5-ma1: '+ (result.ma.ma5 - result.ma.ma1)
				].join('<br/>')

		);
		cloneLab.appendTo('#mainDiv');
		var cloneTable = $("#table").clone(true);
		cloneTable.attr('id', 'table' + index);
		cloneTable.appendTo('#mainDiv');
		var rows = [];
		//for (var i = result.min.t.length - 1; i > -1 ; i--) {
		for (var i = 0 - 1; i < result.min.t.length ; i++) {
			var row = [];
			row[0] = result.min.t[i];
			row[1] = result.min.n[i];
			row[2] = result.max.n[i];
			row[3] = row[2] - row[1];
			rows[i] = row;
		}

		/*$('#table' + index).fixedHeaderTable({    
		    footer: true,    
		    cloneHeadToFoot: true,    
		    altClass: 'odd'  
		});*/
		$('#table' + index).DataTable({
			searching : false,
			//stripeClasses: ["odd", "even"],
			fixedHeader: true,
			scrollY: '1000px',
			paging : false,
			info : false,
			rowCallback : RowCallBack,
			data : rows
		});
		 $('#table' + index).DataTable().$("tr:even").css("background-color", "#DCDCDC");


	}
	var up = 0;
	var low = 0;
	function RowCallBack(row, data, index) {
	    //if (index % 2 == 0) { $(row).css('background-color', '#DCDCDC'); }
	    let tmp = [...data];
	    var minIdx = 1;
	    var maxIdx = 2;
	    var diffIdx = 3;
	    if (data[minIdx] + data[maxIdx] < 60) {
		    if (data[minIdx] < 10) {
			    $('td', row).eq(minIdx).css('font-weight', "bold").css("background-color", "red");
		    }
		    if (data[minIdx] > 24) {
			    $('td', row).eq(minIdx).css('font-weight', "bold").css("background-color", "green");
		    }
		    if (data[maxIdx] < 10) {
			    $('td', row).eq(maxIdx).css('font-weight', "bold").css("background-color", "green");
		    }
		    if (data[maxIdx] > 24) {
			    $('td', row).eq(maxIdx).css('font-weight', "bold").css("background-color", "red");
		    }
		    if (data[diffIdx] < -25) {
		    	low++;
		    	$('td', row).eq(diffIdx).css('font-weight', "bold").css("background-color", "green");
		    }
		    if (data[diffIdx] > 25) {
		    	up++;
		    	$('td', row).eq(diffIdx).css('font-weight', "bold").css("background-color", "red");
		    }
	    }
	}
	var waittime = 29 * 1000;
	function timeout() {
		var date = new Date();
		if (date.getHours() < 13) {
			setTimeout(() => location.reload(), waittime);
		} else if (date.getHours() == 13 &&　date.getMinutes() < 25) {
			setTimeout(() => location.reload(), waittime);
		}
	}

	$(document).ready(function() {
		console.log('ready');
		initData();
		$('#th1').click();
		timeout();
		console.log('up' + up);
		console.log('low' + low);
	});


</script>
</html>