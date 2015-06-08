$(document).ready(function() {
	
	// 메뉴영역 include
	$("#menu").load("menu.html");
	
	
	
	$('#searchFrm').on("submit", function(e) {
		e.preventDefault();
		var formData = $(this).serialize();

		bindData(formData);
	})
	
	var formData = $('#searchFrm').serialize();
});



function bindData(requestData) {
	var jqxhr = $.ajax({
		type :"POST",
		url : "/rest/formattedSql",
		cache : false,
		data : requestData
	})
	
	jqxhr.done(function(data, textStatus) {
		$('#formattedSql').html(data.sql);
		$('#formattedParameter').html(data.parameters);
	})
	jqxhr.fail(function(jqXHR, textStatus, errorThrown){
		alert(errorThrown)
	}) 
}
