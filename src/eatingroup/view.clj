(ns eatingroup.view
  (:use hiccup.page))

(defn page [request]
  (html5
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
      :rel "stylesheet"}]]
   [:body
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
         [:li [:a {:href "#contact"} "Contact"]]]]]]]
    [:div.container
     [:h1 "eating groups"]
     [:p "toto toto"]]
    (include-js
     "http://code.jquery.com/jquery.js"
     "static/bootstrap/js/bootstrap.js")]))
