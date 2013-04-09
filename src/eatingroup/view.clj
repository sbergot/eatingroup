(ns eatingroup.view
  (:use hiccup.page))

(defn header []
  [:head
   [:title "eatinGroup"]
   [:meta
    {:name "viewport"
     :content "width=device-width, initial-scale=1.0"}]
   [:link
    {:href "static/master.css"
     :rel "stylesheet"}]
   [:link
    {:href "static/bootstrap/css/bootstrap.css"
     :rel "stylesheet"}]
   [:link
    {:href "static/bootstrap/css/bootstrap-responsive.css"
     :rel "stylesheet"}]])

(defn navbar []
  [:div.navbar.navbar-inverse.navbar-fixed-top
   [:div.navbar-inner
    [:div.container
     [:button.btn.btn-navbar
      {:type "button"
       :data-toggle "collapse"
       :data-target ".nav-collapse"}
      (repeat 3 [:span.icon-bar])]
     [:a.brand {:href "#"} "eatinGroup"]
     [:div.nav-collapse.collapse
      [:ul.nav
       [:li.active [:a {:href "#"} "home"]]
       [:li [:a {:href "#about"} "About"]]
       [:li [:a {:href "#contact"} "Contact"]]]]]]])

(defn js-files []
  (include-js
   "http://code.jquery.com/jquery.js"
   "http://ajax.aspnetcdn.com/ajax/knockout/knockout-2.2.1.js"
   "static/bootstrap/js/bootstrap.js"
   "static/js/controller.js"))

(defn main-container []
  [:div.container
   [:h1 "eating groups"]
   [:div.row-fluid
    {:data-bind "template: {foreach: groups,beforeRemove: hideGroup,afterAdd: showGroup}"}
    [:div.span6
     [:dl.dl-horizontal.well.group
      [:dt "Quoi?"]
      [:dd {:data-bind "text: description"}]
      [:dt "Quand?"]
      [:dd {:data-bind "text: time"}]
      [:dt "Qui?"]
      [:dd {:data-bind "text: members"}]]]]
   [:div.row-fluid
     [:div.span12
      [:h1.btn.btn-large.btn-block.btn-primary
       {:data-bind "click: addGroup"}
       "create new group"]]]])




(defn page [request]
  (html5
   (header)
   [:body
    (navbar)
    (main-container)
    (js-files)]))
