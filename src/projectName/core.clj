(ns ~{projectName}.core
  (:require
   [~{projectName}.system :as s]))

(defn -main [& args]
  (println "Doint the API play")
  (s/init (s/system {}))
  (s/start)
  (.addShutdownHook
   (Runtime/getRuntime)
   (Thread. (fn []
              (println ";; Shutting down")
              (s/stop)
              (println ";; ---")
              (println ";; Shutdown completed")))))
