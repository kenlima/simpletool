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

function bindResult(data) {
	
	$.each(data, function(index){
		index++
		console.log(this);
		var tablehtml = '<tr>'
			tablehtml = tablehtml + '<td>' + index + '</td>';
			tablehtml = tablehtml + '<td>' + this['urlName'] + '</td>';
			tablehtml = tablehtml + '<td>' + this['url'] + '</td>';
			tablehtml = tablehtml + '<td>' + this['cnt'] + '</td>';
			tablehtml = tablehtml + '<td>';
			tablehtml = tablehtml + '<a id="perUsers" class="btn btn-primary btn-xs" data-target="#myModal" data-url="' + this['url'] + '" data-url_name="' + this['urlName'] + '">Users</a>&nbsp';
			tablehtml = tablehtml + '<a id="perTest" class="btn btn-primary btn-xs" data-toggle="collapse" data-target="#tblCollapse_' + index + '" data-jikmoo_cd="' + this['jikmooCd'] + '" data-jikmoo_name="' + this['jikmoo'] + '" data-idx="' + index + '">직책별</a>';
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
		url : "/rest/groupByUrl",
		beforeSend: function (xhr) {
			$('#loadingCircle').empty();
			$('#dataTable > tbody').empty();
			$('#loadingCircle').append('<span class="glyphicon glyphicon-refresh glyphicon-refresh-animate"></span>');
		},
		cache : false,
		data : requestData
	}).done(function(data){
		bindResult(data)	
		
		//perUrl click
		$('a#perUsers').on('click', function (event) {
			
			$('#perUsers > tbody').empty();
			
			var url = $(this).data('url')
			var url_name = $(this).data('url_name')
			
			
			$('#myModal').modal('show')
			$('#myModal').find('.modal-title').text(url_name)
			
			var fromDate = $('#fromDate').val();
			var toDate = $('#toDate').val();
			
			
			$.ajax({
				type :"GET",
				url : "/rest/groupByUserPerUrl?url=" + url + "&fromDate=" + fromDate + "&toDate=" + toDate,
				beforeSend: function() {
					$('#modalLoadingCircle').append('<span class="glyphicon glyphicon-refresh glyphicon-refresh-animate"></span>');
				},
				cache : false
			}).then(function(data){
	    		$.each(data, function(index){
	    			index++;
	    			var tablehtml = '<tr>'
	    				tablehtml = tablehtml + '<td>' + index + '</td>';
	    				tablehtml = tablehtml + '<td>' + this['count'] + '</td>';
	    				tablehtml = tablehtml + '<td>' + this['user'].userCd + '</td>';
	    				tablehtml = tablehtml + '<td>' + this['user'].userName + '</td>';
	    				tablehtml = tablehtml + '<td>' + this['user'].jikmooName + '</td>';
	    				tablehtml = tablehtml + '<td>' + this['user'].jikgubName + '</td>';
	    				tablehtml = tablehtml + '<td>' + this['user'].jikchakName + '</td>';
	    				tablehtml = tablehtml + '</tr>';
	    			$('#perUsers > tbody').append(tablehtml);
	    		})
			})
			.always(function() {
				$('#modalLoadingCircle').empty();
			})
		}) //perUrl click end 
	})
	.fail(function(jqXHR, status, e) {
		alert(jqXHR.responseText);
	})
	.always(function() {
		$('#loadingCircle').empty();
	})
}

