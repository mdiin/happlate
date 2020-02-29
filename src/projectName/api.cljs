(ns ~{projectName}.api
  (:require
   [castra.core :as c]
   [javelin.core :as j :include-macros true :refer [defc]]))

(defc res nil)
(defc err nil)
(defc loading nil)

(def random (c/mkremote '~{projectName}.api/random res err loading))
