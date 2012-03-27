(ns mc.views.common
  (:use noir.core
        hiccup.core
        hiccup.page-helpers))

(defpartial layout [& content]
            (html5
              [:head
               [:title "magicluster"]
               (include-css "/css/reset.css")]
              [:body
               [:div#wrapper
                content]]))
