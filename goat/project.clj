(defproject goat "0.1.0-SNAPSHOT"
  :description "Measuring and Profiling performance for Clojure Apps from the Repl"
  :url "https://github.com/gerritjvv/goat"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [ 
                  [robert/hooke "1.3.0"]
		  [org.clojure/clojure "1.5.1"]]
  
  :global-vars {*warn-on-reflection* true}

  :plugins [ [no-man-is-an-island/lein-eclipse "2.0.0"]])
