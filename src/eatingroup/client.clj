(ns eatingroup.client)

(defn add-group [group user]
  {"name" "addGroup"
   "data"
   {"group"
    {"description" (group "description")
     "time" (group "time")
     "members" "me"}}})
