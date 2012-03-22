(ns mc.util)

(defn fetch-url [address]
  (with-open [stream (.openStream (java.net.URL. address))]
    (let [buf (java.io.BufferedReader.
	       (java.io.InputStreamReader. stream))]
      (apply str (line-seq buf)))))
