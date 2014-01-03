# goat

A clojure library designed to profile clojure function performance during runtime.

The instrumentation is meant to be run from a repl and can be removed and added dynamically.

The api makes use of https://github.com/technomancy/robert-hooke for adding hooks arround instrumented functions, and was inspired by https://github.com/MichaelDrogalis/night-vision.

## Usage

Add this to your project.clj `:dependencies` list:

    [goat "0.1.0-SNAPSHOT"]

## Examples

```clojure

(use 'goat.core)

;;dummy function
(defn myfun [n] (Thread/sleep n))

;;instrument a namespace
(instrument-functions! 'user)

(myfun 100)

;;collect performance data for a method

(get-fperf 'user/myfun) 

;;#goat.core.FPerf{:name user/myfun, :call-count 1, :total-time 101}

;;reset instrumentation for 'user)
(reset-instrumentation! 'user)

;;reset all instrumentation
(reset-instrumentation!)

;;view perf data
(get-fperf-data)
;;{user/cdoc #goat.core.FPerf{:name user/cdoc, :call-count 0, :total-time 0}, 
;;user/help #goat.core.FPerf{:name user/help, :call-count 0, :total-time 0}, 
;;user/apropos-better #goat.core.FPerf{:name user/apropos-better, :call-count 0, :total-time 0},
;;user/set-signal-handler! #goat.core.FPerf{:name user/set-signal-handler!, :call-count 0, :total-time 0}, 
;;user/myfun #goat.core.FPerf{:name user/myfun, :call-count 2, :total-time 203}, 
;;user/find-name #goat.core.FPerf{:name user/find-name, :call-count 0, :total-time 0}, 
;;user/clojuredocs #goat.core.FPerf{:name user/clojuredocs, :call-count 0, :total-time 0}}

```

### Ordering stats

```clojure

(use 'goat.core)

(defn myfun [n] (Thread/sleep n))
(defn abc [] (+ 1 2))

(instrument-functions! 'user)
(myfun 100)
(doseq [i (range 10)] (abc))

;;get-top-fperf filters out call-count == 0 and returns ordered by total-count and total-time
(get-top-fperf 3)
;;(#goat.core.FPerf{:name user/myfun, :call-count 1, :total-time 100} 
;; #goat.core.FPerf{:name user/abc, :call-count 10, :total-time 0})

;;filter only by call-count
(get-top-fperf 3 :call-count)
;;(#goat.core.FPerf{:name user/abc, :call-count 10, :total-time 0} 
;; #goat.core.FPerf{:name user/myfun, :call-count 1, :total-time 100})


```

## License

Copyright 2013 Gerrit Jansen van Vuuren and contributors

Distributed under the Eclipse Public License, the same as Clojure.
