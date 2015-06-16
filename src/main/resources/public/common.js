$(document).ready(function() {
	
	$("#menu").load("menu.html");
	
	//alert(QueryString.menu)
	
	
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

var QueryString = function () {
	  // This function is anonymous, is executed immediately and 
	  // the return value is assigned to QueryString!
	  var query_string = {};
	  var query = window.location.search.substring(1);
	  var vars = query.split("&");
	  for (var i=0;i<vars.length;i++) {
	    var pair = vars[i].split("=");
	        // If first entry with this name
	    if (typeof query_string[pair[0]] === "undefined") {
	      query_string[pair[0]] = pair[1];
	        // If second entry with this name
	    } else if (typeof query_string[pair[0]] === "string") {
	      var arr = [ query_string[pair[0]], pair[1] ];
	      query_string[pair[0]] = arr;
	        // If third or later entry with this name
	    } else {
	      query_string[pair[0]].push(pair[1]);
	    }
	  } 
	    return query_string;
	} ();

