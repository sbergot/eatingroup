(function() {
    var api_url = window.location.href.replace("http://", "ws://") + "api";
    var socket = new WebSocket(api_url);
    socket.onopen = function() {
        console.log("socket opened");
    };
    function EventHook() {
        this.events = {};
    }
    EventHook.prototype.on = function(name, handler){
        this.events[name] = this.events[name] || [];
        this.events[name].push(handler);
    };
    EventHook.prototype.fire = function(name, data){
        var handlers = this.events[name];
        var l = handlers.length;
        for (var i = 0; i < l; i++) {
            handlers[i](data);
        }
    };
    var listener = new EventHook();
    socket.onmessage = function(e) {
        console.log("server: " + e.data);
        var server_obj = JSON.parse(e.data);
        listener.fire(server_obj.name, server_obj.data);
    };
    window.listener = listener;
})();
