(ns ~{projectName}.test-fixtures
  (:require
   [clojure.test :refer :all]
   [etaoin.api :refer :all]))

(def ^:dynamic *driver*
  "Current driver")

(def driver-types [:firefox :chrome])

(defn drivers
  [f]
  (doseq [driver-type driver-types]
    (with-driver driver-type {} driver
      (binding [*driver* driver]
        (testing (format "Testing in %s browser" (name driver-type))
          (f))))))
