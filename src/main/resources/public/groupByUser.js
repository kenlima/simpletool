$(document).ready(function() {
	//alert(getUrlParameter("fromDate"));
	$('#fromDate').val(function(i, inputTxt){
		var cookieValue = $.cookie('fromDate');
		if(cookieValue === undefined) {
			return fnToDate()
		}
		return cookieValue;
	})
			
	$('#toDate').val(function(i, inputTxt){
		var cookieValue = $.cookie('toDate');
		if(cookieValue === undefined) {
			return fnToDate()
		}
		return cookieValue;
	})
	
	$('#searchFrm').on("submit", function(e) {
		var fromDate =$('#fromDate').val()
		var toDate = $('#toDate').val()
		$.cookie('fromDate', fromDate)
		$.cookie('toDate', toDate)
		
		e.preventDefault();
		var formData = $(this).serialize();
		bindData(formData);
	})
	
	
	var formData = $('#searchFrm').serialize();	
	bindData(formData);
});



/*
  현재날짜를 YYYYMMDD 형태로 리턴
 */
function fnToDate()
{
     var toDate = new Date(); // 날자 변수 선언
     toDate.setDate(toDate.getDate() - 1)
     var dateNow = fnLPAD(String(toDate.getDate()),"0",2); //일자를 구함
     var monthNow = fnLPAD(String((toDate.getMonth()+1)),"0",2); // 월(month)을 구함
     var yearNow = String(toDate.getFullYear()); //년(year)을 구함

     return yearNow + monthNow + dateNow;
}


/*
  왼쪽에 원하는 텍스트 추가
  오라클 LPAD 함수와 같음
  
 val         원래 값 
 set         왼쪽에 추가하려는 값
 cnt         set 갯수
 */
function fnLPAD(val,set,cnt)
{
     if( !set || !cnt || val.length >= cnt)
     {
          return val;
     }

     var max = (cnt - val.length)/set.length;

     for(var i = 0; i < max; i++)
     {
          val = set + val;
     }

     return val;
}


function bindResult(data) {
	$('#dataTable > tbody').empty();
	
	$.each(data, function(index){
		index++
		var tablehtml = '<tr>'
			tablehtml = tablehtml + '<td>' + index + '</td>';
			tablehtml = tablehtml + '<td>' + this['user'].userCd + '</td>';
			tablehtml = tablehtml + '<td>' + this['user'].jikmooName + '</td>';
			tablehtml = tablehtml + '<td>' + this['user'].jikgubName + '</td>';
			tablehtml = tablehtml + '<td>' + this['user'].jikchakName + '</td>';
			tablehtml = tablehtml + '<td>' + this['user'].userName + '</td>';
			tablehtml = tablehtml + '<td>' + this['count'] + '</td>';
			tablehtml = tablehtml + '<td>';
			tablehtml = tablehtml + '<a id="perUrl" class="btn btn-primary btn-xs" data-target="#myModal" data-user_cd="' + this['user'].userCd + '" data-user_name="' + this['user'].userName + '">접속기록보기</a>&nbsp;';
			tablehtml = tablehtml + '</td>';
			tablehtml = tablehtml + '</tr>';
			
			tablehtml = tablehtml + '<tr id="tblCollapse_' + index + '" class="collapse">';
			tablehtml = tablehtml + '<td colspan="6" id="tblAAA_' + index + '">'
			
			tablehtml = tablehtml + '</td>';
			tablehtml = tablehtml + '</tr>';
			
		$('#dataTable > tbody').append(tablehtml);
	});	
	
	
}

function bindData(requestData) {
	$.ajax({
		type :"GET",
		url : "/rest/groupByUser",
		cache : false,
		data : requestData
	}).then(function(data){
		bindResult(data)	
		
		//perUrl click
		$('a#perUrl').on('click', function (event) {
		
			$('#pageRankPerJikmoo > tbody').empty();
			
			var userCd = $(this).data('user_cd')
			var userName = $(this).data('user_name')
			
			
			$('#myModal').modal('show')
			$('#myModal').find('.modal-title').text(userName)
			
			var fromDate = $('#fromDate').val();
			var toDate = $('#toDate').val();
			
			
			$.ajax({
				type :"GET",
				url : "/rest/userLog?userCd=" + userCd + "&fromDate=" + fromDate + "&toDate=" + toDate,
				cache : false
			}).then(function(data){
	    		$.each(data, function(index){
	    			index++;
	    			var tablehtml = '<tr>'
	    				tablehtml = tablehtml + '<td>' + index + '</td>';
	    				tablehtml = tablehtml + '<td>' + this['date'] + '</td>';
	    				tablehtml = tablehtml + '<td>' + this['urlName'] + '</td>';
	    				tablehtml = tablehtml + '<td>' + this['url'] + '</td>';
	    				tablehtml = tablehtml + '</tr>';
	    			$('#pageRankPerJikmoo > tbody').append(tablehtml);
	    		})
			})
		}) //perUrl click end 
		
		
		$('a#perTest').on('click', function (event) {
			
			//$('a#perTest').collapse('toggle')
			
			$('#tblAAA > tbody').empty();
			
			var jikmooCd = $(this).data('jikmoo_cd')
			var jikmooName = $(this).data('jikmoo_name')
			var idx = $(this).data('idx')
			
			// collapse hide check
			if($("#tblCollapse_" + idx).attr('class') == 'collapse in') {
				return;
			}
			
			var fromDate = $('#fromDate').val();
			var toDate = $('#toDate').val();
			
			
			$.ajax({
				type :"GET",
				url : "/wmpsimple/requestCountPerJikchak?jikmooCd=" + jikmooCd + "&fromDate=" + fromDate + "&toDate=" + toDate,
				cache : false
			}).then(function(data){

	    		$('#tblAAA_' + idx).empty();
	    		
				var tablehtml = '<table class="table table-bordered">'
				tablehtml = tablehtml + '<thead>'
				tablehtml = tablehtml + '<tr>'
				tablehtml = tablehtml + '<th>Rank</th>'
				tablehtml = tablehtml + '<th>직책</th>'
				tablehtml = tablehtml + '<th>RequestCount</th>'
				tablehtml = tablehtml + '</tr>'
				tablehtml = tablehtml + '</thead>'
				tablehtml = tablehtml + '<tbody>'
				
	    		$.each(data, function(index){
	    			console.log(this);
	    			index++;
	    				tablehtml = tablehtml + '<tr>'
	    				tablehtml = tablehtml + '<td>' + index + '</td>';
	    				tablehtml = tablehtml + '<td>' + this['jikchak'] + '</td>';
	    				tablehtml = tablehtml + '<td>' + this['cnt'] + '</td>';
	    				tablehtml = tablehtml + '</tr>';
	    		})
	    		tablehtml = tablehtml + '</tbody>'
	    		tablehtml = tablehtml + '</table>'
	    		console.log(tablehtml)
    			$('#tblAAA_' + idx).append(tablehtml);
			})
		}) // click
	})
}


function getUrlParameter(sParam)
{
    var sPageURL = window.location.search.substring(1);
    var sURLVariables = sPageURL.split('&');
    for (var i = 0; i < sURLVariables.length; i++) 
    {
        var sParameterName = sURLVariables[i].split('=');
        if (sParameterName[0] == sParam) 
        {
            return sParameterName[1];
        }
    }
} 

