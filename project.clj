(defproject magicluster "0.0.1"
  :dependencies [[org.clojure/clojure "1.3.0"]
		 [org.clojure/data.json "0.1.2"]
                 [congomongo "0.1.8"]
		 [ring/ring-jetty-adapter "1.0.1"]]
  :run-aliases {:extractor mc.extractor})
