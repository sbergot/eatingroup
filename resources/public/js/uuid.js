(function() {
    'use strict';

    var uuid_template = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx';

    function uuid() {
        return uuid_template.replace(
            /[xy]/g,
            function(c) {
                var r = Math.random()*16|0;
                var v = c == 'x' ? r : (r&0x3|0x8);
                return v.toString(16);
            }
        );
    }

    window.uuid = uuid;
})();
