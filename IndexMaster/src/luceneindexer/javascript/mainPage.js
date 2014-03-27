

$('#searchBtn').click(function(){
	$('#searchResult').empty();
	var query = $('#query').val();
	var arr = query.split(' ');
	if(isEmpty(arr[1])){
		var q = "attr_tag:" + '"'+ arr[0]+ '" ';
	}
	else{
		var q = "attr_tag:" + '"'+ arr[0]+ '" ' + "AND" + " attr_tag:" + '"'+ arr[1]+'"'; 
	}
	console.log(q);
	
	var obj ={
		'wt':'json',
		'q':q,
		'rows':100
	}

	$.ajax({
		'url': 'http://129.241.111.168:8983/solr/SanFrancisco/select',
		'data': obj,
		'success': function(data) {
			console.log("1");
			for(var i=0;i<data.response.docs.length;i++){
				var URL = data.response.docs[i].url_s;
				$('#searchResult').append('<img src="' + URL + '">');
				console.log(data.response.docs[i]);
			}
			
		},
		'dataType':'jsonp',
		'jsonp':'json.wrf'
		});
	
	
	$.ajax({
		'url': 'http://129.241.111.168:8983/solr/Mediaeval/select',
		'data': obj,
		'success': function(data) {
			console.log("2");
			for(var i=0;i<data.response.docs.length;i++){
				var URL = data.response.docs[i].url_s;
				$('#searchResult').append('<img src="' + URL + '">');
				console.log(data.response.docs[i]);
			}
			
		},
		'dataType':'jsonp',
		'jsonp':'json.wrf'
		});
	
	$.ajax({
		'url': 'http://129.241.111.168:8983/solr/London/select',
		'data': obj,
		'success': function(data) {
			console.log("3");
			for(var i=0;i<data.response.docs.length;i++){
				var URL = data.response.docs[i].url_s;
				$('#searchResult').append('<img src="' + URL + '">');
				console.log(data.response.docs[i]);
			}
			
		},
		'dataType':'jsonp',
		'jsonp':'json.wrf'
		});
	
	$.ajax({
		'url': 'http://129.241.111.168:8983/solr/POOLFutebal/select',
		'data': obj,
		'success': function(data) {
			console.log("4");
			for(var i=0;i<data.response.docs.length;i++){
				var URL = data.response.docs[i].url_s;
				$('#searchResult').append('<img src="' + URL + '">');
				console.log(data.response.docs[i]);
			}
			
		},
		'dataType':'jsonp',
		'jsonp':'json.wrf'
		});
	
	$.ajax({
		'url': 'http://129.241.111.168:8983/solr/Upcoming/select',
		'data': obj,
		'success': function(data) {
			console.log("5");
			for(var i=0;i<data.response.docs.length;i++){
				var URL = data.response.docs[i].url_s;
				$('#searchResult').append('<img src="' + URL + '">');
				console.log(data.response.docs[i]);
			}
			
		},
		'dataType':'jsonp',
		'jsonp':'json.wrf'
		});
	
	$.ajax({
		'url': 'http://129.241.111.168:8983/solr/WorldGEOUpcoming/select',
		'data': obj,
		'success': function(data) {
			console.log("6");
			for(var i=0;i<data.response.docs.length;i++){
				var URL = data.response.docs[i].url_s;
				$('#searchResult').append('<img src="' + URL + '">');
				console.log(data.response.docs[i]);
			}
			
		},
		'dataType':'jsonp',
		'jsonp':'json.wrf'
		});
	
//	$.ajax({
//		'url': 'http://129.241.111.168:8983/solr/WorldTiles/select',
//		'data': obj,
//		'success': function(data) {
//			console.log("7");
//			for(var i=0;i<data.response.docs.length;i++){
//				var URL = data.response.docs[i].url_s;
//				$('#searchResult').append('<img src="' + URL + '">');
//				console.log(data.response.docs[i].url_s);
//			}
//			
//		},
//		'dataType':'jsonp',
//		'jsonp':'json.wrf'
//		});

}); 

	$("#query").keypress(function(e){
	if(e.keyCode == 13){
		event.preventDefault();
			$("#searchBtn").click();
		}
	});

function isEmpty(s){
	return (!s||0==s.length);
}



	
