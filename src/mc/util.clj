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

(defn get-ordinal
  [x]
  (let [n (if (number? x) x (Integer/parseInt x))]
    (str n
         (let [rem (mod n 100)]
           (if (and (>= rem 11) (<= rem 13))
             "th"
             (condp = (mod n 10)
                 1 "st"
                 2 "nd"
                 3 "rd"
                 "th"))))))
