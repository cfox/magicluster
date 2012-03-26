(ns mc.util)

(defn fetch-url [address]
  (with-open [stream (.openStream (java.net.URL. address))]
    (let [buf (java.io.BufferedReader.
	       (java.io.InputStreamReader. stream))]
      (apply str (line-seq buf)))))

(defn fix-date
  "Take a string like '03/16' and return a java.util.Date."
  [partial-date]
  (let [calendar (java.util.GregorianCalendar.)
        year (.get calendar (java.util.Calendar/YEAR))
        format "MM/dd/yyyy"
        formatter (java.text.SimpleDateFormat. format)]
    (.parse formatter (str partial-date "/" year))))
