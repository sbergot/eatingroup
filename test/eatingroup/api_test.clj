(ns eatingroup.api-test
  (:use clojure.test
        eatingroup.api))

(deftest a-test
  (testing "publish group should return the same structure"
    (is (=
         (add-group
          {"description" "toto desc"
           "time" 15}
          "toto")
         {"name" "addGroup"
          "data" {"group" {"description" "toto desc"
                           "time" 15
                           "members" "me"}}}))))
