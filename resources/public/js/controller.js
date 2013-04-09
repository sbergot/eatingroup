(function() {

    function GroupViewModel(desc, members) {
        this.description = desc;
        this.members = members.join(", ");
    }

    function AppViewModel(groups) {
        this.groups = ko.observableArray(groups);
        this.addGroup = function () {
            this.groups.push(
                new GroupViewModel(
                    "manger un delicieux sandwich chez subway!!",
                    ["simon",
                     "lucien",
                     "fran�ois"])
            );
        };
        this.showGroup = function(elem) {
            if (elem.nodeType === 1) {
                $(elem).hide().slideDown();
            }
        }
        this.hideGroup = function(elem) {
            if (elem.nodeType === 1) {
                $(elem).slideUp(function() { $(elem).remove(); });
            }
        }

    }

    ko.applyBindings(new AppViewModel([
        new GroupViewModel(
        "manger un delicieux sandwich chez subway!!",
        ["simon",
         "lucien",
         "fran�ois"]),
        new GroupViewModel(
        "une fat salade au boboon :-)",
        ["denis",
         "olivier",
         "adrien"])]));
})();
