(function() {
    var fadeCompleteHandler;

    ko.bindingHandlers.fadeVisible = {

        init: function(element, valueAccessor) {
            // Initially set the element to be instantly
            // visible/hidden depending on the value
            var value = valueAccessor();
            // Use "unwrapObservable" so we can handle values that may
            // or may not be observable
            $(element).toggle(ko.utils.unwrapObservable(value));
        },

        update: function(element, valueAccessor) {
            // Whenever the value subsequently changes, slowly fade
            // the element in or out
            var value = valueAccessor();
            var duration = 200;
            if (fadeCompleteHandler) {
                ko.utils.unwrapObservable(value) ?
                    $(element).fadeIn(duration, fadeCompleteHandler) :
                    $(element).fadeOut(duration, fadeCompleteHandler);
                fadeCompleteHandler = null;
            } else {
                ko.utils.unwrapObservable(value) ?
                    $(element).fadeIn(duration) :
                    $(element).fadeOut(duration);
            }
        }

    };

    function onFadeComplete(handler) {
        fadeCompleteHandler = handler;
    }

    window.onFadeComplete = onFadeComplete;
})();
