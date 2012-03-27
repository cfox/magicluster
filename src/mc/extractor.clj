(ns mc.extractor
  (:require [clojure.set :as set])
  (:use (mc in db)))

(defn -main
  "Load current tournament listings.  Extract and save any we don't yet have."
  [& args]
  (println "Magicluster Extractor is now running...")
  (let [listings (fetch-listings)
        fetched-ids (set (map :Hyperlink listings))
        existing-ids (set (load-tournament-ids))
        new-ids (set/difference fetched-ids existing-ids)
        filter-fn (fn [l] (contains? new-ids (:Hyperlink l)))
        new-listings (filter filter-fn listings)]
    (println (str "Extracting " (count new-ids) " new tournaments..."))
    (let [new-tournaments (map extract-tournament new-listings)]
      (println (str "Saving " (count new-tournaments)
                    " new tournaments to Mongo..."))
      (doseq [new-tournament new-tournaments]
        (try
          (save-tournament new-tournament)
          (catch Exception e
            (println (str "Exception saving tournament "
                          (:id new-tournament))))))
      (println (str "Finished.")))))
