(ns ~{projectName}.components.database
  (:require
   [com.stuartsierra.component :as component]
   [next.jdbc :as jdbc]
   [next.jdbc.connection :as connection])
  (:import [com.zaxxer.hikari HikariDataSource]))

(def datasource-options
  {:autoCommit true
   :readOnly false
   :connectionTimeout 30000
   :validationTimeout 5000
   :idleTimeout 600000
   :maxLifetime 1800000
   :minimumIdle 10
   :maximumPoolSize 10
   :poolName "~{projectName}-pool"})

(defrecord Database [db-spec ^HikariDataSource datasource]
  component/Lifecycle
  (start [this]
    (if datasource
      this
      (do
        (println ";; Starting database connection")
        (assoc this :datasource (connection/->pool HikariDataSource (merge datasource-options db-spec))))))

  (stop [this]
    (if datasource
      (do
        (println ";; Stopping database connection")
        (.close datasource)
        (dissoc this :datasource))
      this)))

(defn database
  [db-spec]
  (map->Database {:db-spec db-spec}))
