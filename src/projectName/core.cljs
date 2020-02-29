(ns ~{projectName}.core
  (:refer-clojure :exclude [group-by dosync])
  (:require
   [~{projectName}.api :as api]

   [hoplon.core :as h :include-macros true]
   [hoplon.jquery]
   [hoplon.svg :as svg]
   [javelin.core :as jav :refer [cell] :refer-macros [defc defc= cell= dosync]]

   [clojure.string :as string]
   [clojure.set :refer [rename-keys]]
   [cljs.spec.alpha :as s]
   [expound.alpha :as expound]
   [reitit.frontend :as reitit]
   [reitit.frontend.easy :as rfe]
   [reitit.frontend.controllers :as rfc]))

;;;;;;;;;;;;;;;
;; DEBUGGING ;;
;;;;;;;;;;;;;;;

(defc= print-res
  (do
    (println "RES")
    (println api/res)
    (println "===")))

(defc= print-err
  (do
    (println "ERR")
    (println api/err)
    (println "===")))

(defc= print-loading
  (do
    (println "LOADING")
    (println api/loading)
    (println "===")))

;;;;;;;;;;;
;; Utits ;;
;;;;;;;;;;;

(defn group-by
  "Augmentation of clojure.core/group-by that also allows a second function
  argument to control how overlapping values are handled."
  ([f coll]
   (group-by f (fnil conj []) coll))

  ([f1 f2 coll]
   (persistent!
    (reduce (fn [ret x]
              (let [k (f1 x)]
                (assoc! ret k (f2 (get ret k nil) x))))
            (transient {})
            coll))))

(defn key-by
  "Special case of group-by where later values when overlapping take precedence."
  [f coll]
  (group-by f (fn [_ v] v) coll))

;;;;;;;;;;;
;; Model ;;
;;;;;;;;;;;

(s/def ::current-route map?)
(defn loading-state?
  [kw]
  (or (= :requested kw)
      (= :failed kw)
      (= :succeeded kw)
      (= :not-requested kw)))
(s/def :loading/initial-user loading-state?)
(s/def ::loading-state (s/keys :req [:loading/initial-user]))
(s/def ::db (s/keys :req-un []))

(def initial-db {})


;;;;;;;;;;;
;; CELLS ;;
;;;;;;;;;;;
(defn cell-spec-check
  [spec cell]
  (when-not (s/valid? spec cell)
    (throw (ex-info (str "spec check failed: " (expound/expound-str spec cell)) {}))))

(defc current-route {})
(defc= current-route-spec
  (cell-spec-check ::current-route current-route))

;; View

(h/defelem app
  []
  (h/main
   (h/h1 "~{projectName}")))

(declare router)

;; Routing

(def router
  (reitit/router
   [["/" {:name :route/welcome
          :domain {:domain/name :welcome
                   :domain/level 0}
          :controllers [{:identity #(get-in % [:data :domain :domain/name])
                         :start (fn welcome-controller-start
                                  [identity]
                                  (println (str "Entering " identity)))
                         :stop (fn welcome-controller-stop
                                 [identity]
                                 (println (str "Leaving " identity)))}]}]]))

;; Initialisation

(defn on-navigate
  [new-match]
  (when new-match
    (reset! previous-route @current-route)
    (js/setTimeout #(reset! previous-route nil) 300)
    (swap! current-route
           (fn [old new]
             (let [controllers (rfc/apply-controllers (:controllers old) new)]
               (assoc new :controllers controllers)))
           new-match)))

(defn init-routes!
  []
  (println ";; Initialising routes")
  (rfe/start!
   router
   on-navigate
   {:use-fragment true}))

(defn mount-root []
  (init-routes!)
  (js/jQuery #(.replaceWith (js/jQuery "#app") (app))))

(defn ^:export main
  []
  (h/with-init! (mount-root)))
