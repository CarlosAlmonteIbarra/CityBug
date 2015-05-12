var http = require('http');
var cassandra = require('cassandra-connection');

http.createServer(function(req, res){
	var data = "";
	req.setEncoding("utf-8");
	req.on("data", function (chunk) {
		data += chunk;
	});
	req.on("end", function () {
		var jsonObject = JSON.parse(data);
		insertIntoCassandra(jsonObject);
	});
	
	res.writeHead(200, {'Content-Type': 'text/plain'});
	res.end("ok");
}).listen(5000);

function insertIntoCassandra (entry) {
	cassandra.SendToCassandra(entry);
	console.log(entry.mac);
}

console.log("Listening...");

