function SendToCassandra (entry) {

	var Connection = require('cassandra-client').Connection;
	var db = new Connection({host: '127.0.0.1', port: 9160, keyspace: 'CityBug'});

	db.connect(function(err) {
  	if (err) {
    throw err;
  		} else {
  		var uuid = guid();
    	db.execute('INSERT INTO Event (Id, Mac, Lat, Lon, Height, DateTime, EntryType) VALUES (?, ?,?,?,?,?,?)', [uuid, entry.mac, entry.latitude,entry.longitude,entry.height,entry.Datetime, entry.entrytype],
      function (err) {
        if (err) {
          throw err;
        } else {
          console.log('success!');
        }
    });
  }
});
}


function guid() {
  function s4() {
    return Math.floor((1 + Math.random()) * 0x10000)
      .toString(16)
      .substring(1);
  }
  return s4() + s4() + '-' + s4() + '-' + s4() + '-' +
    s4() + '-' + s4() + s4() + s4();
}

exports.SendToCassandra = SendToCassandra;