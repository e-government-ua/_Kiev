angular.module('app').service('messageBusService', function() {

  var MessageBus = {
        debug : false, //Set to true to view console logs
        log : function() {// wrapper for smart calling of console.log
          if (this.debug === true) {
            //call console.log() within `console` scope with arguments array
            console.log.apply(console, arguments);
          }
        },
        topics : {}, //Object where all event listener topics will be stored
        subscribers: {},
        // Helper function to get unique id.
        // Origin: https://gist.github.com/gordonbrander/2230317
        generateId: function() {
          // Math.random should be unique because of its seeding algorithm.
          // Convert it to base 36 (numbers + letters), and grab the first 9 characters
          // after the decimal.
          return '_' + Math.random().toString(36).substr(2, 9);
        },
        subscribe : function(topic, listener) {
          // Create the topic if it has not yet been created
          if (!this.topics[topic]) {
            this.topics[topic] = [];
          }
          // Add the listener by pushing it into the array that you just created above
          this.topics[topic].push(listener);
          this.log('LISTENING FOR TOPIC', topic, this.topics[topic]);
          // Remember listener in subscribers
          var subscriberId = this.generateId();
          this.subscribers[subscriberId] = {listener: listener, topic: topic};

          return subscriberId;
        },
        publish : function(topic, data) {
          var self = this;
          // Broadcast event to '*' subscribers
          if (this.topics['*']) {
            this.log('BROADCASTING EVENT', this.topics['*']);
            this.topics['*'].forEach(function(listener) {
              if ( typeof (listener) === 'function') {
                listener(data);
              }
            });
          }
          //Do nothing if there are no topics or if the topics array is empty
          if (!this.topics[topic] || this.topics[topic].length < 1) {
            this.log('NOT PUBLISHING EVENT: no subscribers');
            return;
          }
          this.log('PUBLISHING EVENT:', this.topics);
          //Iterate through the list of topics in the topics object and fire off
          // the functions that are assigned as the event handler.
          //Pass the data to the callback functions as well
          this.topics[topic].forEach(function(listener) {
            if ( typeof listener === 'function') {
              listener(data);
            }
          });

        },
        unsubscribe : function(subscriberId) {
          if (this.subscribers[subscriberId]) {
            this.log('UNSUBSCRIBE', subscriberId);
            var topic = this.subscribers[subscriberId].topic;
            for (var i = 0, len = this.topics[topic].length; i < len; ++i) {
              if (this.topics[topic][i] == this.subscribers[subscriberId].listener) {
                this.log('DELETE topic listener', this.subscribers[subscriberId].listener)
                this.topics[topic].splice(i, 1);
                delete this.subscribers[subscriberId];
                this.log('subscribers %o, listeners to topic %o %o', this.subscribers, topic, this.topics[topic]);
                break;
              }
            }
          }
        },
        remove: function(topic) {
          //Remove the event from the list
          delete this.topics[topic];
          this.log('REMOVED THE TOPIC', topic);
          // Clear subscribers for this topic
          for (var key in this.subscribers) {
            if (this.subscribers[key].topic == topic) {
              delete this.subscribers[key];
              this.log('DELETED SUBSCRIBER', key);
            }
          }
        },
        clear: function() {
          this.log('CLEAR SUBSCRIBERS FOR ALL TOPICS', this.topics);
          for (var key in this.topics) {
            this.remove(key);
          }
        }
      };
      return MessageBus;
});