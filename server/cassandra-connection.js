function sendToCassandra(entry) {
  var cassandra = require('cassandra-driver');
  var Connection = require('cassandra-client').Connection;
  var db = new Connection({host:'localhost', port: 9160, keyspace: 'citybug'});

  db.connect(function(err) {
      if (err) {
        console.log(err);
      } else {
        db.execute('INSERT INTO events (id,mac_address,latitude,longitude,height,datetime,event_type) VALUES(?, ?, ?, ?, ?, ?, ?)',
          [cassandra.types.timeuuid(),
              entry.mac,
              entry.latitude,
              entry.longitude,
              entry.height,
              entry.datetime, entry.type],
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

exports.sendToCassandra = sendToCassandra;