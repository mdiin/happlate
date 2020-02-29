(ns ~{projectName}.components.webserver
  (:require
   [aleph.http.server :as web]
   [castra.middleware :as castra]
   [com.stuartsierra.component :as component]
   [reitit.core :as reitit]
   [reitit.ring :as ring]
   [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
   [ring.util.response :as response]))

(defn root-handler
  [req]
  (some-> (response/resource-response "index.html" {:root "public"})
          (response/content-type "text/html; charset=utf-8")))

(defn not-found-handler
  [req]
  {:status 404
   :headers {"Content-Type" "text/html"}
   :body "Not found"})

(defn routes
  []
  [["/" {:get root-handler
         :name ::index-root}]
   ["/*" {:get not-found-handler
          :name ::not-found}]])

(defn router
  []
  (ring/router (routes)))

(defn app
  [database]
  (-> (ring/ring-handler (router))
      (castra/wrap-castra {:ctx {:db (:datasource database)}} 'planm.madd.api)
      (castra/wrap-castra-session "1themostsecretse")
      (wrap-defaults (assoc-in site-defaults [:security :anti-forgery] false))))

(defrecord Webserver [config database]
  component/Lifecycle
  (start [this]
    (if-not (:instance this)
      (do
        (println ";; Starting webserver")
        (assoc this :instance (web/start-server (app database) {:port 8080})))
      this))

  (stop [this]
    (if (:instance this)
      (do
        (println ";; Stopping webserver")
        (.close (:instance this))
        (dissoc this :instance))
      this)))

(defn webserver
  [config]
  (map->Webserver {:config config}))
