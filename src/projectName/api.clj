(ns ~{projectName}.api
  (:require
   [next.jdbc :as jdbc]
   [castra.core :as castra :refer [defrpc]]))

(defrpc random [ctx]
  (jdbc/execute! (:db ctx) ["select * from public.migrations;"]))
