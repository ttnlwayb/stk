<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Hello</title>
</head>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/liongogo.css">
<body>
   <h1 class="color-red"> </h1>
</body>
<link rel="stylesheet" href="https://cdn.datatables.net/1.11.3/css/jquery.dataTables.min.css">
<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
<script src="https://cdn.datatables.net/1.11.3/js/jquery.dataTables.min.js"></script>
<div style='width:80%'>
<table id="test_table" >
    <thead>
      <tr style='background-color:#A6CDE7'>
        <th>st</th>
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
<script>
console.log('DataTable');
var result = ${result};
$("#test_table").DataTable({ 
	"lengthMenu": [[20, 50, 100], ['每頁20筆', '每頁50筆', '每頁100筆']],
	rowCallback: RowCallBack,
	"data": result.data

});
function RowCallBack(row, data, index) {
    if (index % 2 == 0) { $(row).css('background-color', '#DCDCDC'); }
    let tmp = [...data];
    let stk = tmp[0];
    tmp[0] = '999999(999999)';
    var clone = tmp.map(s => parseFloat(s.split('(')[0]))
    let min = Math.min.apply(null, clone);
    let minIdx = clone.indexOf(min);
    console.log(stk, 'minIdx:', minIdx);
    $('td', row).eq(minIdx).css('font-weight', "bold").css("color", "green");
    clone[0] = 0;
    let max = Math.max.apply(null, clone);
    let maxIdx = clone.indexOf(max);
    $('td', row).eq(maxIdx).css('font-weight', "bold").css("color", "red");
}
</script>
</html>