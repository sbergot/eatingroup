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
        var groups = this.groups;
        this.form_description = ko.observable("");
        var form_description = this.form_description;
        this.form_time = ko.observable(5);
        var form_time = this.form_time;

        this.addGroup = function () {
            var form_visible = this.form_visible;
            onFadeComplete(function() {
                form_visible(true);
            });
            this.button_visible(false);
        };

        this.publishGroup = function () {
            var button_visible = this.button_visible;
            onFadeComplete(function() {
                button_visible(true);
            });
            this.form_visible(false);
            server.send(
                "publish_group",
                {
                    description : form_description(),
                    time : form_time()
                }
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

        server.listen(
            "addGroup",
            function(data) {
                var group = data.group;
                groups.push({
                    description : data.description,
                    time : data.time,
                    members : "me"
                });
            }
        );
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
