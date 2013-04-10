$(function() {

    function GroupViewModel(desc, time, members) {
        this.description = desc;
        this.time = time;
        this.members = members.join(", ");
    }

    function AppViewModel(groups) {
        this.groups = ko.observableArray(groups);
        this.addGroup = function () {
            this.groups.push(
                new GroupViewModel(
                    "manger un delicieux sandwich chez subway!!",
                    "1 : 00 : 00",
                    ["simon",
                     "lucien",
                     "françois"])
            );
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
