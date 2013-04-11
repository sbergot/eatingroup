$(function() {

    function GroupViewModel(desc, time, members) {
        this.description = desc;
        this.time = time;
        this.members = members.join(", ");
    }

    function AppViewModel(groups) {
        this.form_visible = ko.observable(false);
        this.button_visible = ko.observable(true);
        this.groups = ko.observableArray(groups);
        this.addGroup = function () {
            var form_visible = this.form_visible;
            fadeCompleteHandler = function() {
                form_visible(true);
            };
            this.button_visible(false);
        };
        this.publishGroup = function () {
            var button_visible = this.button_visible;
            fadeCompleteHandler = function() {
                button_visible(true);
            };
            this.form_visible(false);
        };
        this.showGroup = function(elem) {
            if (elem.nodeType === 1) {
                $(elem).hide().slideDown();
            }
        };
        this.hideGroup = function(elem) {
            if (elem.nodeType === 1) {
                $(elem).slideUp(function() { $(elem).remove(); });
            }
        };
        this.message = ko.observable("");
        var message = this.message;
        server.listen("message", function(data) {
            message(data.msg);
        });
        this.helloServer = function() {
            server.send("client_message", {desc : "yay"});
        };
    }

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

    ko.applyBindings(new AppViewModel([
        new GroupViewModel(
            "manger un delicieux sandwich chez subway!!",
            "2 : 00 : 00",
            ["simon",
             "lucien",
             "françois"]),
        new GroupViewModel(
            "une fat salade au boboon :-)",
            "1 : 00 : 00",
            ["denis",
             "olivier",
             "adrien"])]));
});
