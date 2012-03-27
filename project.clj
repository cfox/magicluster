(defproject magicluster "0.0.1"
  :dependencies [[org.clojure/clojure "1.2.1"]
		 [org.clojure/data.json "0.1.2"]
                 [congomongo "0.1.8"]
                 [noir "1.1.0-SNAPSHOT"]
		 [ring/ring-jetty-adapter "1.0.1"]]
  :run-aliases {:extractor mc.extractor
                :server mc.server})
