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
   "static/js/socket.js"
   "static/js/controller.js"))

(defn group []
  [:dl.dl-horizontal.well.group
   [:dt "Quoi?"]
   [:dd {:data-bind "text: description"}]
   [:dt "Quand?"]
   [:dd {:data-bind "text: time"}]
   [:dt "Qui?"]
   [:dd {:data-bind "text: members"}]])

(defn group-form []
  [:form.form-horizontal.well.group
   [:div
    [:label.control-label
     {:for "what"} "Quoi?"]
    [:div.controls
     [:input {:type "text" :id "what" :placeholder "Quoi?"}]]]
   [:div
    [:label.control-label
     {:for "when"} "Quand?"]
    [:div.controls
     [:input {:type "text" :id "when" :placeholder "Quand?"}]]]
   [:div
    [:div.controls
     [:button.btn.btn-small
      {:data-bind "click: $root.publishGroup"}
      "Publish"]]]])

(defn main-container []
  [:div.container
   [:h1 "eating groups"]
   [:div.row-fluid
    {:data-bind "template: {
                   foreach: groups,
                   beforeRemove: hideGroup,
                   afterAdd: showGroup}"}
    [:div.span6 (group)]]
   [:div.row
    [:div.span6
     {:data-bind "fadeVisible: $root.form_visible"}
     (group-form)]
    [:div.span6
     {:data-bind "fadeVisible: $root.button_visible"}
     [:h1.btn.btn-large.btn-block.btn-primary.group
      {:data-bind "click: $root.addGroup"}
      "create new group"]]]])

(defn page [request]
  (html5
   (header)
   [:body
    (navbar)
    (main-container)
    (js-files)]))
