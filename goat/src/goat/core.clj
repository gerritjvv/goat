(ns goat.core
  (:require [clojure.core.reducers :as r]
            [robert.hooke :refer [add-hook ]])
  )

(defrecord FPerf [name call-count total-time])  

(def perf-funs (ref {}))


(defn fun-name [fun]
  (symbol (str (.getName (.-ns fun)) "." (.-sym fun))))

(defn get-fperf [name]
  "Argument name is a fully qualified symbol"
  (get @perf-funs name))


(defn update-fperf [m fun call-count total-time]
  (let [fperf (get m fun (FPerf. fun 0 0))]
       (FPerf. (:name fperf) (+ (:call-count fperf) call-count) (+ (:total-time fperf) total-time))))

(defn add-perf-data [n fun total-time]
     (prn "add-perf-data " n)
     (dosync (commute perf-funs
                      #(assoc % n (update-fperf % n 1 total-time)))))

(defn time-fun [n f args]
  (let [start-time (System/currentTimeMillis)
        res (apply f args)
        stop-time (System/currentTimeMillis)]
    (add-perf-data n f (- stop-time start-time))
    res))

(defn instrument-function [n fun]
  (add-hook fun (fn [f & args] (time-fun n f args) )))

(defn reset-instrumentation! []
  (dosync (ref-set perf-funs {})))

(defn instrument-functions! [target-ns]  (let [all-vars (vals (ns-publics target-ns))        fns (r/filter #(contains? (meta %) :arglists) all-vars)
        fun-map (r/fold (fn ([] {})
                            ([m fun]
                               (let [n (fun-name fun)]
                                     (instrument-function n fun)
                                     (assoc m n (FPerf. n 0 0) )))) fns)
        ]
        (dosync (alter perf-funs into fun-map))))
        
                  