function SendToCassandra (entry) {

var cql = require('node-cassandra-cql');
var client = new cql.Client({hosts: ['10.10.11.101'], keyspace: 'citybug'});
client.execute('INSERT INTO events (id, mac_address, latitude, longitude, height, datetime, event_type) VALUES (now(), ?,?,?,?,?,?)', [entry.mac, entry.latitude,entry.longitude,entry.height,entry.datetime, entry.type],
  function(err, result) {
    if (err) throw err;
    else console.log('success!');
  });
}
/*	var Connection = require('cassandra-client').Connection;
	var db = new Connection({host: '10.10.11.101', port: 9160, keyspace: 'citybug'});

	db.connect(function(err) {
  	if (err) {
      throw err;
  		} else {
  		var uuid = guid();
    	//db.execute('INSERT INTO events (id, mac, latitude, longitude, height, datetime, type) VALUES (?, ?,?,?,?,?,?)', [uuid, entry.mac, entry.latitude,entry.longitude,entry.height,entry.datetime, entry.type],
      db.execute('INSERT INTO events (id, mac, latitude, longitude, height, datetime, type) VALUES (now(), ?,?,?,?,?,?)', [entry.mac, entry.latitude,entry.longitude,entry.height,entry.datetime, entry.type],
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
*/
exports.SendToCassandra = SendToCassandra;