(ns ~{projectName}.system
  (:require
   [~{projectName}.components.database :as db]
   [~{projectName}.components.webserver :as web]
   [com.stuartsierra.component :as component]))


(defn system [config]
  (component/system-map
   :database (db/database (:db config))
   :webserver (component/using
               (web/webserver (:webserver config))
               [:database])))

(def current-system nil)

(defn init
  [system-map]
  (alter-var-root #'current-system (constantly system-map)))

(defn start
  []
  (alter-var-root #'current-system component/start))

(defn stop
  []
  (alter-var-root #'current-system component/stop))

(defn restart
  []
  (stop)
  (start))

(comment
  (init (system {:db {:username "~{projectDBUser}"
                      :password "~{projectDBUserPassword}"
                      :dbtype "postgresql"
                      :dbname "~{projectDB}"
                      :webserver {}}}))
  (start)
  (stop)
  (restart)

  (next.jdbc/execute! (get-in current-system [:database :datasource]) ["select * from public.migrations;"]))
