<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Stk Table</title>
</head>
<body>
	<h1 class="color-red"></h1>
</body>
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

</style>
<div id='mainDiv' style='width: 100%'></div>
<div id='tmpDiv' style='display: none'>
	<label id='lab'><label>
			<table id="table">
				<thead>
					<tr style='background-color: #A6CDE7'>
						<th>stk</th>
						<th>1</th>
						<th>2</th>
						<th>3</th>
						<th>4</th>
						<th>5</th>
						<th>6</th>
						<th>7</th>
						<th>8</th>
						<th>9</th>
						<th>10</th>
						<th>11</th>
						<th>12</th>
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
</div>
<div id="divDialog" />

<script>
var stkcount = 0;
var notices = [];
console.log('DataTable');
var result = ${result};

function initData() {
	Object.keys(result).forEach(function (item, index) {
		var cloneLab = $('#lab').clone(true);
		cloneLab.attr('id', 'lab' + index);
		cloneLab.html(item);
		cloneLab.appendTo('#mainDiv');
		var cloneTable = $("#table").clone(true);
		cloneTable.attr('id', 'table' + index);
		cloneTable.appendTo('#mainDiv');
		var rows = [];
		for (var i = 0; i < result[item].length; i++) {
			stkcount++;
			result[item][i]['d'][0] += '(' + result[item][i]['c'] + ')';
			rows[i] = result[item][i]['d'];
		}
		$('#table' + index).DataTable({ 
			/*"lengthMenu": [[10, 20, 50], ['每頁10筆', '每頁20筆', '每頁50筆']],*/
			searching: false,
			paging: false,
			info: false,
			rowCallback: RowCallBack,
			"data": rows
		});
	});
}
function RowCallBack(row, data, index) {
    if (index % 2 == 0) { $(row).css('background-color', '#DCDCDC'); }
    let tmp = [...data];
    stk = tmp[0];
    tmp[0] = '999999(999999)';
    var cclone = tmp.filter(e =>e !='').map(s => parseFloat(s.split('(')[0]))
    let min = Math.min.apply(null, cclone);
    let minIdx = cclone.indexOf(min);
    $('td', row).eq(minIdx).css('font-weight', "bold").css("color", "green");
    cclone[0] = 0;
    let max = Math.max.apply(null, cclone);
    let maxIdx = cclone.indexOf(max);
    $('td', row).eq(maxIdx).css('font-weight', "bold").css("color", "red");
    let regex = /\([^)]+\)/g;
    var vclone = tmp.filter(e =>e !='').map(s => 
    	{
    		s = s.match(regex)[0];
    		s = s.substring(1, s.length - 1);
    	    return parseFloat(s);
    	}
    );
    vclone[0] = 0;
    if (vclone[1] > 1000) {
		notices.push(stk);
    }
    vclone.forEach(function (item, index) {
    	if (item > 1000) {
    	    $('td', row).eq(index).css("background-color", "#BBFFFF");
    	}
	});
}


function messageDialog() {
	if (notices.length == 0) {
		return;
	}
	$("#divDialog").html(notices.join('<br/>'));
	notices.length = 0;
	$( "#divDialog" ).dialog({
	    buttons: {
	        "Ok": function() {
	            $(this).dialog("close");
	        }
	    },
		open: function(event, ui) {
			$(this).parent().find('.ui-dialog-titlebar-close').hide();
		}
	});
}
var waittime = 19 * 1000;
$(document).ready(function() {
	initData();
	//messageDialog();
	var date = new Date();
	if (date.getHours() < 13) {
		setTimeout(() => location.reload(), waittime);
	} else if (date.getHours() == 13 &&　date.getMinutes() < 25) {
		setTimeout(() => location.reload(), waittime);
	}
});

</script>
</html>