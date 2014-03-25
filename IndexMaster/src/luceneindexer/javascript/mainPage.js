

$('#searchBtn').on('click', function(){
	var query = $('#query').val();
	var arr = query.split(' ');
	var q = "attr_tag:" + arr[0] + " attr_tag:" + arr[1]; 
	console.log(q);

	$.ajax({
		'url': 'http://129.241.111.168:8983/solr/SanFrancisco/select',
		'data': {'wt':'json', 'q':q},
		'success': function(data) {
			$('#searchForm').replaceWith('<div id="searchResult"><h3>You searched for ' + q + '</h3></div>');
			for(var i=0;i<data.response.docs.length;i++){
				var URL = data.response.docs[i].url_s;
				$('#searchResult').append('<iframe src="' + URL + '"></iframe>');
				console.log(data.response.docs[i].url_s);
			}
			
		},
		'dataType':'jsonp',
		'jsonp':'json.wrf'
		});
	
});

	
